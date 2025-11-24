package sosrota.application.mappers;

import sosrota.application.dtos.OcorrenciaDTO;
import sosrota.domain.models.Ocorrencia;
import sosrota.infrastructure.persistence.entity.OcorrenciaEntity;

public interface OcorrenciaMapper {
    OcorrenciaDTO toDTO(OcorrenciaEntity entity);
    OcorrenciaEntity toEntity(Ocorrencia domain);
    Ocorrencia toDomain(OcorrenciaEntity entity);
}

