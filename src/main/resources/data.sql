-- Inserção de dados iniciais para Oracle
-- Senhas: Admin123!, Gerente123!, Operador123!

-- Usuários iniciais
INSERT INTO usuarios (nome_filial, email, senha_hash, cnpj, endereco, telefone, perfil, data_criacao) VALUES 
('Admin TrackZone', 'admin@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '12.345.678/0001-99', 'Rua A, 123', '(11) 99999-0001', 'ADMIN', SYSTIMESTAMP);

INSERT INTO usuarios (nome_filial, email, senha_hash, cnpj, endereco, telefone, perfil, data_criacao) VALUES 
('Filial Centro', 'gerente@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '12.345.678/0002-88', 'Rua B, 456', '(11) 98888-0002', 'GERENTE', SYSTIMESTAMP);

INSERT INTO usuarios (nome_filial, email, senha_hash, cnpj, endereco, telefone, perfil, data_criacao) VALUES 
('Filial Sul', 'operador@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '12.345.678/0003-66', 'Rua C, 789', '(11) 97777-0003', 'OPERADOR', SYSTIMESTAMP);

-- Motos de exemplo
INSERT INTO motos (placa, chassi, motor, usuario_id, data_criacao) VALUES 
('ABC1234', 'CHASSI001ABC123456789', '150CC', 1, SYSTIMESTAMP);

INSERT INTO motos (placa, chassi, motor, usuario_id, data_criacao) VALUES 
('XYZ9876', 'CHASSI002XYZ987654321', '200CC', 2, SYSTIMESTAMP);

INSERT INTO motos (placa, chassi, motor, usuario_id, data_criacao) VALUES 
('DEF5678', 'CHASSI003DEF567890123', '250CC', 3, SYSTIMESTAMP);

-- Status das motos
INSERT INTO status_motos (moto_id, status, area, usuario_id, data_criacao) VALUES 
(1, 'PRONTA', 'Área A', 1, SYSTIMESTAMP);

INSERT INTO status_motos (moto_id, status, area, usuario_id, data_criacao) VALUES 
(2, 'PENDENTE', 'Área B', 2, SYSTIMESTAMP);

INSERT INTO status_motos (moto_id, status, area, usuario_id, data_criacao) VALUES 
(3, 'REPARO_SIMPLES', 'Área C', 3, SYSTIMESTAMP);

-- Operações de exemplo
INSERT INTO operacoes (moto_id, tipo_operacao, usuario_id, observacoes, data_criacao) VALUES 
(1, 'CHECK_IN', 1, 'Check-in inicial da moto ABC1234', SYSTIMESTAMP);

INSERT INTO operacoes (moto_id, tipo_operacao, usuario_id, observacoes, data_criacao) VALUES 
(2, 'CHECK_OUT', 2, 'Check-out da moto XYZ9876 para manutenção', SYSTIMESTAMP);

INSERT INTO operacoes (moto_id, tipo_operacao, usuario_id, observacoes, data_criacao) VALUES 
(3, 'CHECK_IN', 3, 'Check-in da moto DEF5678 após reparo', SYSTIMESTAMP);