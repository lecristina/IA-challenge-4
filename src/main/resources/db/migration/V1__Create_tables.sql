-- ============================================
-- V1: Criação das tabelas e sequences
-- ============================================

-- Sequences para geração de IDs
CREATE SEQUENCE seq_usuarios START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_motos START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_status_motos START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_operacoes START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_dashboard START WITH 1 INCREMENT BY 1;

-- Tabela de usuários
CREATE TABLE usuarios (
    id NUMBER DEFAULT seq_usuarios.NEXTVAL PRIMARY KEY,
    nome_filial VARCHAR2(100) NOT NULL,
    email VARCHAR2(255) NOT NULL,
    senha_hash VARCHAR2(255) NOT NULL,
    cnpj VARCHAR2(18) NOT NULL,
    endereco VARCHAR2(255),
    telefone VARCHAR2(20),
    perfil VARCHAR2(20) NOT NULL,
    data_criacao TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT uk_usuario_cnpj UNIQUE (cnpj),
    CONSTRAINT ck_perfil CHECK (perfil IN ('ADMIN', 'GERENTE', 'OPERADOR'))
);

-- Tabela de motos
CREATE TABLE motos (
    id NUMBER DEFAULT seq_motos.NEXTVAL PRIMARY KEY,
    placa VARCHAR2(10) NOT NULL,
    chassi VARCHAR2(50) NOT NULL,
    motor VARCHAR2(50),
    usuario_id NUMBER,
    data_criacao TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_moto_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT uk_moto_placa UNIQUE (placa),
    CONSTRAINT uk_moto_chassi UNIQUE (chassi)
);

-- Tabela de status das motos
CREATE TABLE status_motos (
    id NUMBER DEFAULT seq_status_motos.NEXTVAL PRIMARY KEY,
    moto_id NUMBER,
    status VARCHAR2(50) NOT NULL,
    area VARCHAR2(50) NOT NULL,
    usuario_id NUMBER,
    data_criacao TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_status_moto FOREIGN KEY (moto_id) REFERENCES motos(id),
    CONSTRAINT fk_status_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT ck_status CHECK (status IN (
        'PENDENTE', 'REPARO_SIMPLES', 'DANOS_ESTRUTURAIS',
        'MOTOR_DEFEITUOSO', 'MANUTENCAO_AGENDADA',
        'PRONTA', 'SEM_PLACA', 'ALUGADA', 'AGUARDANDO_ALUGUEL'
    ))
);

-- Tabela de operações
CREATE TABLE operacoes (
    id NUMBER DEFAULT seq_operacoes.NEXTVAL PRIMARY KEY,
    moto_id NUMBER,
    tipo_operacao VARCHAR2(20) NOT NULL,
    usuario_id NUMBER,
    observacoes CLOB,
    data_criacao TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_operacao_moto FOREIGN KEY (moto_id) REFERENCES motos(id),
    CONSTRAINT fk_operacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT ck_tipo_operacao CHECK (tipo_operacao IN ('CHECK_IN', 'CHECK_OUT'))
);

-- Tabela de dashboard
CREATE TABLE dashboard (
    id NUMBER DEFAULT seq_dashboard.NEXTVAL PRIMARY KEY,
    total_motos NUMBER,
    motos_disponiveis NUMBER,
    motos_alugadas NUMBER,
    motos_em_manutencao NUMBER,
    total_operacoes NUMBER,
    total_check_in NUMBER,
    total_check_out NUMBER,
    ultima_atualizacao TIMESTAMP DEFAULT SYSTIMESTAMP,
    atualizado_por NUMBER,
    CONSTRAINT fk_dashboard_usuario FOREIGN KEY (atualizado_por) REFERENCES usuarios(id)
);

-- Tabela de auditoria
CREATE TABLE auditoria (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_usuario VARCHAR2(255),
    tipo_operacao VARCHAR2(10),
    data_hora TIMESTAMP DEFAULT SYSTIMESTAMP,
    valores_anteriores CLOB,
    valores_novos CLOB
);
