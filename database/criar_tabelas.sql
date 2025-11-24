-- ============================================
-- Script SQL para criar todas as tabelas do SOS-Rota
-- PostgreSQL
-- ============================================

-- Conectar ao banco de dados
\c sosrota_db

-- ============================================
-- 1. Tabela BAIRROS (sem dependências)
-- ============================================
CREATE TABLE IF NOT EXISTS bairros (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE
);

-- ============================================
-- 2. Tabela BASES (depende de bairros)
-- ============================================
CREATE TABLE IF NOT EXISTS bases (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    bairro_id BIGINT NOT NULL,
    CONSTRAINT fk_base_bairro FOREIGN KEY (bairro_id) REFERENCES bairros(id)
);

-- ============================================
-- 3. Tabela PROFISSIONAIS (sem dependências)
-- ============================================
CREATE TABLE IF NOT EXISTS profissionais (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    funcao VARCHAR(255) NOT NULL,
    contato VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================
-- 4. Tabela AMBULANCIAS (depende de bases)
-- ============================================
CREATE TABLE IF NOT EXISTS ambulancias (
    id BIGSERIAL PRIMARY KEY,
    placa VARCHAR(255) NOT NULL UNIQUE,
    tipo VARCHAR(50) NOT NULL, -- BASICA ou UTI
    status VARCHAR(50) NOT NULL, -- DISPONIVEL, EM_ATENDIMENTO, EM_MANUTENCAO
    base_id BIGINT NOT NULL,
    CONSTRAINT fk_ambulancia_base FOREIGN KEY (base_id) REFERENCES bases(id)
);

-- ============================================
-- 5. Tabela EQUIPES (depende de ambulancias)
-- ============================================
CREATE TABLE IF NOT EXISTS equipes (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    ambulancia_id BIGINT NOT NULL,
    CONSTRAINT fk_equipe_ambulancia FOREIGN KEY (ambulancia_id) REFERENCES ambulancias(id)
);

-- ============================================
-- 6. Tabela EQUIPE_PROFISSIONAL (depende de equipes e profissionais)
-- ============================================
CREATE TABLE IF NOT EXISTS equipe_profissional (
    id BIGSERIAL PRIMARY KEY,
    equipe_id BIGINT NOT NULL,
    profissional_id BIGINT NOT NULL,
    CONSTRAINT fk_equipe_profissional_equipe FOREIGN KEY (equipe_id) REFERENCES equipes(id),
    CONSTRAINT fk_equipe_profissional_profissional FOREIGN KEY (profissional_id) REFERENCES profissionais(id)
);

-- ============================================
-- 7. Tabela OCORRENCIAS (depende de bairros)
-- ============================================
CREATE TABLE IF NOT EXISTS ocorrencias (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(255) NOT NULL,
    gravidade VARCHAR(50) NOT NULL, -- ALTA, MEDIA, BAIXA
    local VARCHAR(255) NOT NULL,
    data_hora_abertura TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL, -- ABERTA, DESPACHADA, EM_ATENDIMENTO, CONCLUIDA, CANCELADA
    observacao TEXT,
    bairro_id BIGINT NOT NULL,
    CONSTRAINT fk_ocorrencia_bairro FOREIGN KEY (bairro_id) REFERENCES bairros(id)
);

-- ============================================
-- 8. Tabela ATENDIMENTOS (depende de ocorrencias e ambulancias)
-- ============================================
CREATE TABLE IF NOT EXISTS atendimentos (
    id BIGSERIAL PRIMARY KEY,
    ocorrencia_id BIGINT NOT NULL,
    ambulancia_id BIGINT NOT NULL,
    data_hora_despacho TIMESTAMP NOT NULL,
    data_hora_chegada TIMESTAMP,
    distancia_km DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_atendimento_ocorrencia FOREIGN KEY (ocorrencia_id) REFERENCES ocorrencias(id),
    CONSTRAINT fk_atendimento_ambulancia FOREIGN KEY (ambulancia_id) REFERENCES ambulancias(id)
);

-- ============================================
-- 9. Tabela CONEXOES (depende de bairros)
-- ============================================
CREATE TABLE IF NOT EXISTS conexoes (
    id BIGSERIAL PRIMARY KEY,
    bairro_origem_id BIGINT NOT NULL,
    bairro_destino_id BIGINT NOT NULL,
    distancia_km DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_conexao_origem FOREIGN KEY (bairro_origem_id) REFERENCES bairros(id),
    CONSTRAINT fk_conexao_destino FOREIGN KEY (bairro_destino_id) REFERENCES bairros(id)
);

-- ============================================
-- 10. Tabela USUARIOS (sem dependências)
-- ============================================
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL -- REGULADOR ou GESTOR
);

-- ============================================
-- Índices para melhorar performance
-- ============================================
CREATE INDEX IF NOT EXISTS idx_base_bairro ON bases(bairro_id);
CREATE INDEX IF NOT EXISTS idx_ambulancia_base ON ambulancias(base_id);
CREATE INDEX IF NOT EXISTS idx_equipe_ambulancia ON equipes(ambulancia_id);
CREATE INDEX IF NOT EXISTS idx_equipe_profissional_equipe ON equipe_profissional(equipe_id);
CREATE INDEX IF NOT EXISTS idx_equipe_profissional_profissional ON equipe_profissional(profissional_id);
CREATE INDEX IF NOT EXISTS idx_ocorrencia_bairro ON ocorrencias(bairro_id);
CREATE INDEX IF NOT EXISTS idx_ocorrencia_status ON ocorrencias(status);
CREATE INDEX IF NOT EXISTS idx_atendimento_ocorrencia ON atendimentos(ocorrencia_id);
CREATE INDEX IF NOT EXISTS idx_atendimento_ambulancia ON atendimentos(ambulancia_id);
CREATE INDEX IF NOT EXISTS idx_conexao_origem ON conexoes(bairro_origem_id);
CREATE INDEX IF NOT EXISTS idx_conexao_destino ON conexoes(bairro_destino_id);

-- ============================================
-- Verificar tabelas criadas
-- ============================================
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;

