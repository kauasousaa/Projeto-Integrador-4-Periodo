package sosrota.application.mappers;

import sosrota.application.dtos.AmbulanciaDTO;
import sosrota.domain.models.Ambulancia;
import sosrota.infrastructure.persistence.entity.AmbulanciaEntity;

public interface AmbulanciaMapper {
    AmbulanciaDTO toDTO(AmbulanciaEntity entity);
    AmbulanciaEntity toEntity(Ambulancia domain);
    Ambulancia toDomain(AmbulanciaEntity entity);
}

