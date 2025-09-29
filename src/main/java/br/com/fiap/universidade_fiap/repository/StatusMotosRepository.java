package br.com.fiap.universidade_fiap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fiap.universidade_fiap.model.StatusMoto;

@Repository
public interface StatusMotosRepository extends JpaRepository<StatusMoto, Long>{

    List<StatusMoto> findByMotoId(Long motoId);
    /**
     * Busca status de moto ordenados por data (mais recente primeiro)
     */
    List<StatusMoto> findByMotoIdOrderByDataCriacaoDesc(Long motoId);
    
    /**
     * Busca todos os status com relacionamentos carregados
     */
    @Query("SELECT s FROM StatusMoto s LEFT JOIN FETCH s.moto LEFT JOIN FETCH s.usuario")
    List<StatusMoto> findAllWithRelations();

}
