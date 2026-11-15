ALTER TABLE rodape DROP CHECK ck_rodape_unico;
ALTER TABLE rodape
    MODIFY id INT NOT NULL AUTO_INCREMENT,
    ADD COLUMN nome VARCHAR(100) NOT NULL DEFAULT 'Padrão';

CREATE TABLE rodape_ativo (
    id INT NOT NULL,
    rodape_id INT NOT NULL,
    CONSTRAINT pk_rodape_ativo PRIMARY KEY (id),
    CONSTRAINT ck_rodape_ativo_unico CHECK (id = 1),
    CONSTRAINT fk_rodape_ativo_rodape FOREIGN KEY (rodape_id) REFERENCES rodape(id)
) ENGINE=InnoDB;

INSERT INTO rodape_ativo (id, rodape_id) VALUES (1, 1);
