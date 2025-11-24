-- Script SQL para criar o banco de dados SOS-Rota
-- Execute este script após ter o PostgreSQL instalado e rodando

-- Criar banco de dados
CREATE DATABASE sosrota_db;

-- Conectar ao banco
\c sosrota_db;

-- Criar usuário
CREATE USER sosrota_user WITH PASSWORD 'sosrota_pass';

-- Dar permissões ao usuário
GRANT ALL PRIVILEGES ON DATABASE sosrota_db TO sosrota_user;

-- As tabelas serão criadas automaticamente pelo Hibernate na primeira execução
-- Mas você pode verificar se foram criadas com:
-- \dt

