CREATE TABLE grupo_sala (
  id bigint NOT NULL,
  nome varchar NOT NULL,
  id_usuario_responsavel bigint NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_usuario_responsavel)
  REFERENCES usuario (id)
);

ALTER TABLE sala
  ADD COLUMN id_grupo_sala bigint,
  ADD CONSTRAINT FK_sala_TO_grupo_sala
  FOREIGN KEY (id_grupo_sala)
  REFERENCES grupo_sala (id);
