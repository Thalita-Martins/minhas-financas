CREATE TABLE lancamento (
  id SERIAL PRIMARY KEY,
  mes INTEGER NOT NULL,
  ano INTEGER NOT NULL,
  usuario_id BIGINT NOT NULL,
  descricao VARCHAR(100),
  valor NUMERIC(16,2) NOT NULL,
  data_cadastro DATE DEFAULT now(),
  tipo VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  ativo boolean,
  CONSTRAINT lancamento_usuario_fkey FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);



