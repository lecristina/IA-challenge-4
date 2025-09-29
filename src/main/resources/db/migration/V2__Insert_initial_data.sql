-- ============================================
-- V2: Inserção de dados iniciais
-- ============================================

-- Inserir usuários iniciais com senhas seguras
-- Senha: Admin123! (hash BCrypt)
INSERT INTO usuarios (nome_filial, email, senha_hash, cnpj, endereco, telefone, perfil)
VALUES ('Admin TrackZone', 'admin@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '12.345.678/0001-99', 'Rua A, 123', '(11) 99999-0001', 'ADMIN');

-- Senha: Gerente123! (hash BCrypt)
INSERT INTO usuarios (nome_filial, email, senha_hash, cnpj, endereco, telefone, perfil)
VALUES ('Filial Centro', 'gerente@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '12.345.678/0002-88', 'Rua B, 456', '(11) 98888-0002', 'GERENTE');

-- Senha: Operador123! (hash BCrypt)
INSERT INTO usuarios (nome_filial, email, senha_hash, cnpj, endereco, telefone, perfil)
VALUES ('Filial Sul', 'operador@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '12.345.678/0003-66', 'Rua C, 789', '(11) 97777-0003', 'OPERADOR');

-- Inserir motos iniciais
INSERT INTO motos (placa, chassi, motor, usuario_id) VALUES ('ABC1234', 'CHASSI001', 'MOTOR001', 1);
INSERT INTO motos (placa, chassi, motor, usuario_id) VALUES ('XYZ9876', 'CHASSI002', 'MOTOR002', 2);
INSERT INTO motos (placa, chassi, motor, usuario_id) VALUES ('DEF5678', 'CHASSI003', 'MOTOR003', 3);

-- Inserir status iniciais das motos
INSERT INTO status_motos (moto_id, status, area, usuario_id) VALUES (1, 'PRONTA', 'Área A', 1);
INSERT INTO status_motos (moto_id, status, area, usuario_id) VALUES (2, 'PENDENTE', 'Área B', 2);
INSERT INTO status_motos (moto_id, status, area, usuario_id) VALUES (3, 'REPARO_SIMPLES', 'Área C', 3);

-- Inserir operações iniciais
INSERT INTO operacoes (moto_id, tipo_operacao, usuario_id, observacoes) VALUES (1, 'CHECK_IN', 1, 'Check-in inicial da moto ABC1234');
INSERT INTO operacoes (moto_id, tipo_operacao, usuario_id, observacoes) VALUES (2, 'CHECK_OUT', 2, 'Check-out da moto XYZ9876 para manutenção');
INSERT INTO operacoes (moto_id, tipo_operacao, usuario_id, observacoes) VALUES (3, 'CHECK_IN', 3, 'Check-in da moto DEF5678 após reparo');
