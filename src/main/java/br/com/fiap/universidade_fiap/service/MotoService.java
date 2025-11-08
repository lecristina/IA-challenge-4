package br.com.fiap.universidade_fiap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.MotoRepository;

/**
 * Serviço para lógica de negócio relacionada a Motos
 * Extrai lógica do controller para seguir Single Responsibility Principle
 */
@Service
public class MotoService {

    private static final Logger logger = LoggerFactory.getLogger(MotoService.class);

    @Autowired
    private MotoRepository motoRepository;

    /**
     * Valida se a placa já existe
     */
    public boolean placaExiste(String placa) {
        return motoRepository.findByPlaca(placa).isPresent();
    }

    /**
     * Valida se o chassi já existe
     */
    public boolean chassiExiste(String chassi) {
        return motoRepository.findByChassi(chassi).isPresent();
    }

    /**
     * Valida se a placa pode ser alterada (não existe ou é a mesma)
     */
    public boolean podeAlterarPlaca(Long idMoto, String novaPlaca) {
        return motoRepository.findById(idMoto)
            .map(moto -> moto.getPlaca().equals(novaPlaca) || !placaExiste(novaPlaca))
            .orElse(false);
    }

    /**
     * Valida se o chassi pode ser alterado (não existe ou é o mesmo)
     */
    public boolean podeAlterarChassi(Long idMoto, String novoChassi) {
        return motoRepository.findById(idMoto)
            .map(moto -> moto.getChassi().equals(novoChassi) || !chassiExiste(novoChassi))
            .orElse(false);
    }

    /**
     * Salva uma nova moto
     */
    public Moto salvarMoto(Moto moto, Usuario usuario) {
        logger.info("Salvando nova moto: placa={}, chassi={}", moto.getPlaca(), moto.getChassi());
        moto.setUsuario(usuario);
        Moto motoSalva = motoRepository.save(moto);
        logger.info("Moto salva com sucesso: id={}", motoSalva.getId());
        return motoSalva;
    }

    /**
     * Atualiza uma moto existente
     */
    public Moto atualizarMoto(Moto motoExistente, Moto motoAtualizada) {
        logger.info("Atualizando moto: id={}", motoExistente.getId());
        motoExistente.setPlaca(motoAtualizada.getPlaca());
        motoExistente.setChassi(motoAtualizada.getChassi());
        motoExistente.setMotor(motoAtualizada.getMotor());
        Moto motoSalva = motoRepository.save(motoExistente);
        logger.info("Moto atualizada com sucesso: id={}", motoSalva.getId());
        return motoSalva;
    }
}




