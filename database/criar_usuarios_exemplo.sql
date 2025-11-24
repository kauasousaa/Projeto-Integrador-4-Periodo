-- ============================================
-- Script SQL para criar usuários de exemplo
-- PostgreSQL
-- ============================================

-- Conectar ao banco de dados
\c sosrota_db

-- ============================================
-- IMPORTANTE: As senhas abaixo são hashes BCrypt
-- ============================================
-- Senha "admin123" para ambos os usuários
-- Hash gerado com BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

-- Usuário REGULADOR (login: regulador, senha: admin123)
INSERT INTO usuarios (login, senha_hash, perfil) 
VALUES ('regulador', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'REGULADOR')
ON CONFLICT (login) DO NOTHING;

-- Usuário GESTOR (login: gestor, senha: admin123)
INSERT INTO usuarios (login, senha_hash, perfil) 
VALUES ('gestor', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'GESTOR')
ON CONFLICT (login) DO NOTHING;

-- Verificar usuários criados
SELECT id, login, perfil FROM usuarios;

-- ============================================
-- NOTA: Para gerar novos hashes BCrypt em Java:
-- ============================================
-- String hash = BCrypt.hashpw("sua_senha", BCrypt.gensalt());
-- System.out.println(hash);

