package com.conAle.projeto.Model;

// Tamanho final de cada posição. Largura/altura 0 = posição sem versão mobile.
// Os nomes são iguais aos valores de posição usados no front.
public enum BannerFormato {
    HOME_TOPO(1351, 468, 800, 800),
    SERVICOS_TOPO(1351, 468, 800, 800),
    SOBRE_NOS(572, 320, 576, 300),
    AREA_CLIENTE(684, 220, 0, 0),
    SOBRE_MIM_FOTO(448, 320, 0, 0);

    private final int largura, altura, larguraMobile, alturaMobile;

    BannerFormato(int largura, int altura, int larguraMobile, int alturaMobile) {
        this.largura = largura;
        this.altura = altura;
        this.larguraMobile = larguraMobile;
        this.alturaMobile = alturaMobile;
    }

    public boolean temMobile() 
    { 
        return larguraMobile > 0; 
    }
    
    public int largura(boolean mobile) 
    { 
        return mobile ? larguraMobile : largura; 
    }
    
    public int altura(boolean mobile) 
    { 
        return mobile ? alturaMobile : altura; 
    }
}
