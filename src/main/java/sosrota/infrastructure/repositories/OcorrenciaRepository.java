package sosrota.infrastructure.repositories;

import sosrota.domain.models.Gravidade;
import sosrota.domain.models.StatusOcorrencia;
import sosrota.infrastructure.persistence.entity.OcorrenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OcorrenciaRepository extends JpaRepository<OcorrenciaEntity, Long> {
    List<OcorrenciaEntity> findByStatus(StatusOcorrencia status);
    List<OcorrenciaEntity> findByGravidade(Gravidade gravidade);
    List<OcorrenciaEntity> findByStatusAndGravidade(StatusOcorrencia status, Gravidade gravidade);

    /**
     * Consulta SQL de Negócio 2: Ocorrências por gravidade e período
     * Retorna a contagem de ocorrências agrupadas por gravidade dentro de um período
     */
    @Query(value = "SELECT " +
            "o.gravidade as gravidade, " +
            "COUNT(o.id) as totalOcorrencias " +
            "FROM ocorrencias o " +
            "WHERE o.data_hora_abertura >= :dataInicio " +
            "AND o.data_hora_abertura <= :dataFim " +
            "GROUP BY o.gravidade " +
            "ORDER BY o.gravidade", nativeQuery = true)
    List<OcorrenciaPorGravidadeProjection> countOcorrenciasPorGravidadeEPeriodo(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Interface projection para o resultado da consulta de ocorrências por gravidade
     */
    interface OcorrenciaPorGravidadeProjection {
        String getGravidade();
        Long getTotalOcorrencias();
    }
}

