package com.conAle.projeto.Model;

// Tamanho final de cada posição para desktop e celular.
// Os nomes são iguais aos valores de posição usados no front.
public enum BannerFormato {
    HOME_TOPO(1923, 699, 375, 360),
    SERVICOS_TOPO(1923, 699, 383, 332),
    SOBRE_NOS(572, 320, 576, 300),
    AREA_CLIENTE(684, 220, 576, 324),
    SOBRE_MIM_FOTO(491, 574, 255, 283);

    private final int largura, altura, larguraMobile, alturaMobile;

    BannerFormato(int largura, int altura, int larguraMobile, int alturaMobile) {
        this.largura = largura;
        this.altura = altura;
        this.larguraMobile = larguraMobile;
        this.alturaMobile = alturaMobile;
    }

    public int largura(boolean mobile) {
        return mobile ? larguraMobile : largura;
    }

    public int altura(boolean mobile) {
        return mobile ? alturaMobile : altura;
    }
}
