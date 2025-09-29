-- ============================================
-- V4: Criação da tabela de notificações
-- ============================================

-- Tabela de notificações
BEGIN
    EXECUTE IMMEDIATE 'CREATE TABLE notificacoes (
        id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        titulo VARCHAR2(100) NOT NULL,
        mensagem VARCHAR2(500) NOT NULL,
        tipo VARCHAR2(20) NOT NULL,
        status VARCHAR2(20) NOT NULL DEFAULT ''NAO_LIDA'',
        usuario_id NUMBER,
        data_criacao TIMESTAMP DEFAULT SYSTIMESTAMP,
        data_leitura TIMESTAMP,
        CONSTRAINT fk_notificacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
        CONSTRAINT ck_tipo_notificacao CHECK (tipo IN (''INFO'', ''WARNING'', ''ERROR'', ''SUCCESS'')),
        CONSTRAINT ck_status_notificacao CHECK (status IN (''LIDA'', ''NAO_LIDA''))
    )';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

-- Índices para performance
BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_notificacoes_usuario ON notificacoes(usuario_id)';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_notificacoes_status ON notificacoes(status)';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_notificacoes_data_criacao ON notificacoes(data_criacao)';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/
