ALTER TABLE usuario
ADD email varchar(255);

ALTER TABLE usuario
ADD CONSTRAINT email_unique UNIQUE (email);