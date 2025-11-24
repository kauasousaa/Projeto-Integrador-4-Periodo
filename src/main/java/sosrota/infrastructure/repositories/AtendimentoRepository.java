package sosrota.infrastructure.repositories;

import sosrota.infrastructure.persistence.entity.AtendimentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<AtendimentoEntity, Long> {
    List<AtendimentoEntity> findByAmbulanciaId(Long ambulanciaId);
    List<AtendimentoEntity> findByOcorrenciaId(Long ocorrenciaId);

    /**
     * Consulta SQL de Negócio 1: Ambulâncias mais utilizadas (top 10)
     * Retorna as ambulâncias ordenadas por quantidade de atendimentos realizados
     */
    @Query(value = "SELECT " +
            "a.id as ambulanciaId, " +
            "a.placa as placa, " +
            "COUNT(at.id) as totalAtendimentos " +
            "FROM ambulancias a " +
            "LEFT JOIN atendimentos at ON a.id = at.ambulancia_id " +
            "GROUP BY a.id, a.placa " +
            "ORDER BY totalAtendimentos DESC, a.placa ASC " +
            "LIMIT 10", nativeQuery = true)
    List<AmbulanciaMaisUtilizadaProjection> findAmbulanciasMaisUtilizadas();

    /**
     * Interface projection para o resultado da consulta de ambulâncias mais utilizadas
     */
    interface AmbulanciaMaisUtilizadaProjection {
        Long getAmbulanciaId();
        String getPlaca();
        Long getTotalAtendimentos();
    }
}

