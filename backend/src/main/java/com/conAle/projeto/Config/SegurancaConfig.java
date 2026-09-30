package com.conAle.projeto.config;

import com.conAle.projeto.repository.UsuarioRepository;
import com.conAle.projeto.security.*;
import jakarta.servlet.DispatcherType;
import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.context.*;
import org.springframework.security.web.csrf.*;

@Configuration
public class SegurancaConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean UserDetailsService userDetailsService(UsuarioRepository repository) {
        return email -> repository.findByEmail(email.trim().toLowerCase(Locale.ROOT))
                .filter(u -> u.isAdministrador()).map(AdminPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas."));
    }

    @Bean AuthenticationManager authenticationManager(UserDetailsService users, PasswordEncoder encoder) {
        var provider = new DaoAuthenticationProvider(users);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    @Bean SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
    @Bean CsrfTokenRepository csrfTokenRepository() { return new HttpSessionCsrfTokenRepository(); }

    @Bean SessionAuthenticationStrategy sessionAuthenticationStrategy(CsrfTokenRepository csrf) {
        // Troca o ID da sessão e descarta o token CSRF anterior após o login.
        return new CompositeSessionAuthenticationStrategy(List.of(
                new ChangeSessionIdAuthenticationStrategy(), new CsrfAuthenticationStrategy(csrf)));
    }

    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository contexts,
            CsrfTokenRepository csrf, UsuarioRepository usuarios) throws Exception {
        http.securityContext(c -> c.securityContextRepository(contexts))
            .csrf(c -> c.csrfTokenRepository(csrf))
            .authorizeHttpRequests(a -> a
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/health", "/api/auth/csrf").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/noticias", "/api/noticias/*").permitAll()
                .requestMatchers("/api/auth/me", "/api/usuarios", "/api/usuarios/**",
                        "/api/noticias", "/api/noticias/**").hasRole("ADMIN")
                .anyRequest().denyAll())
            .requestCache(c -> c.disable())
            .formLogin(c -> c.disable()).httpBasic(c -> c.disable())
            .exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(401); res.setContentType("application/problem+json;charset=UTF-8");
                    res.getWriter().write("{\"status\":401,\"detail\":\"Autenticação necessária.\"}");
                })
                .accessDeniedHandler((req, res, ex) -> {
                    res.setStatus(403); res.setContentType("application/problem+json;charset=UTF-8");
                    res.getWriter().write("{\"status\":403,\"detail\":\"Acesso negado ou token CSRF inválido.\"}");
                }))
            .logout(l -> l.logoutUrl("/api/auth/logout").deleteCookies("JSESSIONID")
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(204)))
            .addFilterAfter(new AdminSessionFilter(usuarios), SecurityContextHolderFilter.class);
        return http.build();
    }
}
