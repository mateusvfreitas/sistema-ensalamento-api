CREATE TABLE curso_usuario
(
  id_curso    bigint NOT NULL,
  id_usuario bigint NOT NULL
);

ALTER TABLE curso_usuario
  ADD CONSTRAINT FK_curso_TO_curso_usuario
    FOREIGN KEY (id_curso)
    REFERENCES curso (id);

ALTER TABLE curso_usuario
  ADD CONSTRAINT FK_usuario_TO_curso_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario (id);

