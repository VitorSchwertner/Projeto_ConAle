-- A posição liga cada banner à seção correspondente do site público.
-- NULL preserva banners antigos; ao editá-los pelo painel, a posição passa a ser obrigatória.
ALTER TABLE banner
    ADD COLUMN posicao VARCHAR(30) NULL AFTER link,
    ADD CONSTRAINT ck_banner_posicao CHECK (
        posicao IS NULL OR posicao IN (
            'HOME_TOPO',
            'SERVICOS_TOPO',
            'SOBRE_NOS',
            'AREA_CLIENTE',
            'SOBRE_MIM_FOTO'
        )
    );
