-- ============================================
-- V3: Triggers de auditoria
-- ============================================

-- Trigger para auditoria de usuários
CREATE OR REPLACE TRIGGER trg_auditoria_usuarios
AFTER INSERT OR UPDATE OR DELETE ON usuarios
FOR EACH ROW
DECLARE
    v_nome_usuario VARCHAR2(255);
    v_old CLOB;
    v_new CLOB;
BEGIN
    IF INSERTING THEN
        v_nome_usuario := :NEW.email;
        v_new := 'nome_filial=' || :NEW.nome_filial || ', email=' || :NEW.email || ', perfil=' || :NEW.perfil;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'INSERT', NULL, v_new);
    ELSIF UPDATING THEN
        v_nome_usuario := :NEW.email;
        v_old := 'nome_filial=' || :OLD.nome_filial || ', email=' || :OLD.email || ', perfil=' || :OLD.perfil;
        v_new := 'nome_filial=' || :NEW.nome_filial || ', email=' || :NEW.email || ', perfil=' || :NEW.perfil;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'UPDATE', v_old, v_new);
    ELSIF DELETING THEN
        v_nome_usuario := :OLD.email;
        v_old := 'nome_filial=' || :OLD.nome_filial || ', email=' || :OLD.email || ', perfil=' || :OLD.perfil;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'DELETE', v_old, NULL);
    END IF;
END;
/

-- Trigger para auditoria de motos
CREATE OR REPLACE TRIGGER trg_auditoria_motos
AFTER INSERT OR UPDATE OR DELETE ON motos
FOR EACH ROW
DECLARE
    v_nome_usuario VARCHAR2(255);
    v_old CLOB;
    v_new CLOB;
BEGIN
    IF INSERTING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :NEW.usuario_id;
        v_new := 'placa=' || :NEW.placa || ', chassi=' || :NEW.chassi || ', motor=' || :NEW.motor;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'INSERT', NULL, v_new);
    ELSIF UPDATING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :NEW.usuario_id;
        v_old := 'placa=' || :OLD.placa || ', chassi=' || :OLD.chassi || ', motor=' || :OLD.motor;
        v_new := 'placa=' || :NEW.placa || ', chassi=' || :NEW.chassi || ', motor=' || :NEW.motor;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'UPDATE', v_old, v_new);
    ELSIF DELETING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :OLD.usuario_id;
        v_old := 'placa=' || :OLD.placa || ', chassi=' || :OLD.chassi || ', motor=' || :OLD.motor;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'DELETE', v_old, NULL);
    END IF;
END;
/

-- Trigger para auditoria de status de motos
CREATE OR REPLACE TRIGGER trg_auditoria_status_motos
AFTER INSERT OR UPDATE OR DELETE ON status_motos
FOR EACH ROW
DECLARE
    v_nome_usuario VARCHAR2(255);
    v_old CLOB;
    v_new CLOB;
BEGIN
    IF INSERTING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :NEW.usuario_id;
        v_new := 'moto_id=' || :NEW.moto_id || ', status=' || :NEW.status || ', area=' || :NEW.area;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'INSERT', NULL, v_new);
    ELSIF UPDATING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :NEW.usuario_id;
        v_old := 'moto_id=' || :OLD.moto_id || ', status=' || :OLD.status || ', area=' || :OLD.area;
        v_new := 'moto_id=' || :NEW.moto_id || ', status=' || :NEW.status || ', area=' || :NEW.area;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'UPDATE', v_old, v_new);
    ELSIF DELETING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :OLD.usuario_id;
        v_old := 'moto_id=' || :OLD.moto_id || ', status=' || :OLD.status || ', area=' || :OLD.area;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'DELETE', v_old, NULL);
    END IF;
END;
/

-- Trigger para auditoria de operações
CREATE OR REPLACE TRIGGER trg_auditoria_operacoes
AFTER INSERT OR UPDATE OR DELETE ON operacoes
FOR EACH ROW
DECLARE
    v_nome_usuario VARCHAR2(255);
    v_old CLOB;
    v_new CLOB;
BEGIN
    IF INSERTING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :NEW.usuario_id;
        v_new := 'moto_id=' || :NEW.moto_id || ', tipo_operacao=' || :NEW.tipo_operacao || ', observacoes=' || :NEW.observacoes;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'INSERT', NULL, v_new);
    ELSIF UPDATING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :NEW.usuario_id;
        v_old := 'moto_id=' || :OLD.moto_id || ', tipo_operacao=' || :OLD.tipo_operacao || ', observacoes=' || :OLD.observacoes;
        v_new := 'moto_id=' || :NEW.moto_id || ', tipo_operacao=' || :NEW.tipo_operacao || ', observacoes=' || :NEW.observacoes;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'UPDATE', v_old, v_new);
    ELSIF DELETING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :OLD.usuario_id;
        v_old := 'moto_id=' || :OLD.moto_id || ', tipo_operacao=' || :OLD.tipo_operacao || ', observacoes=' || :OLD.observacoes;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'DELETE', v_old, NULL);
    END IF;
END;
/

-- Trigger para auditoria de dashboard
CREATE OR REPLACE TRIGGER trg_auditoria_dashboard
AFTER INSERT OR UPDATE OR DELETE ON dashboard
FOR EACH ROW
DECLARE
    v_nome_usuario VARCHAR2(255);
    v_old CLOB;
    v_new CLOB;
BEGIN
    IF INSERTING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :NEW.atualizado_por;
        v_new := 'total_motos=' || :NEW.total_motos || ', motos_disponiveis=' || :NEW.motos_disponiveis || ', motos_alugadas=' || :NEW.motos_alugadas || ', motos_em_manutencao=' || :NEW.motos_em_manutencao || ', total_operacoes=' || :NEW.total_operacoes || ', total_check_in=' || :NEW.total_check_in || ', total_check_out=' || :NEW.total_check_out;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'INSERT', NULL, v_new);
    ELSIF UPDATING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :NEW.atualizado_por;
        v_old := 'total_motos=' || :OLD.total_motos || ', motos_disponiveis=' || :OLD.motos_disponiveis || ', motos_alugadas=' || :OLD.motos_alugadas || ', motos_em_manutencao=' || :OLD.motos_em_manutencao || ', total_operacoes=' || :OLD.total_operacoes || ', total_check_in=' || :OLD.total_check_in || ', total_check_out=' || :OLD.total_check_out;
        v_new := 'total_motos=' || :NEW.total_motos || ', motos_disponiveis=' || :NEW.motos_disponiveis || ', motos_alugadas=' || :NEW.motos_alugadas || ', motos_em_manutencao=' || :NEW.motos_em_manutencao || ', total_operacoes=' || :NEW.total_operacoes || ', total_check_in=' || :NEW.total_check_in || ', total_check_out=' || :NEW.total_check_out;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'UPDATE', v_old, v_new);
    ELSIF DELETING THEN
        SELECT email INTO v_nome_usuario FROM usuarios WHERE id = :OLD.atualizado_por;
        v_old := 'total_motos=' || :OLD.total_motos || ', motos_disponiveis=' || :OLD.motos_disponiveis || ', motos_alugadas=' || :OLD.motos_alugadas || ', motos_em_manutencao=' || :OLD.motos_em_manutencao || ', total_operacoes=' || :OLD.total_operacoes || ', total_check_in=' || :OLD.total_check_in || ', total_check_out=' || :OLD.total_check_out;
        INSERT INTO auditoria (nome_usuario, tipo_operacao, valores_anteriores, valores_novos)
        VALUES (v_nome_usuario, 'DELETE', v_old, NULL);
    END IF;
END;
/
