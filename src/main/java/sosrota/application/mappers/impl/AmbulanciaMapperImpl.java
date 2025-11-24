package sosrota.application.mappers.impl;

import org.springframework.stereotype.Component;
import sosrota.application.dtos.AmbulanciaDTO;
import sosrota.application.mappers.AmbulanciaMapper;
import sosrota.domain.models.Ambulancia;
import sosrota.domain.models.Base;
import sosrota.infrastructure.persistence.entity.AmbulanciaEntity;
import sosrota.infrastructure.persistence.entity.BaseEntity;

@Component
public class AmbulanciaMapperImpl implements AmbulanciaMapper {

    @Override
    public AmbulanciaDTO toDTO(AmbulanciaEntity entity) {
        if (entity == null) return null;
        
        AmbulanciaDTO dto = new AmbulanciaDTO();
        dto.setId(entity.getId());
        dto.setPlaca(entity.getPlaca());
        dto.setTipo(entity.getTipo());
        dto.setStatus(entity.getStatus());
        if (entity.getBase() != null) {
            dto.setBaseId(entity.getBase().getId());
            dto.setBaseNome(entity.getBase().getNome());
        }
        return dto;
    }

    @Override
    public AmbulanciaEntity toEntity(Ambulancia domain) {
        if (domain == null) return null;
        
        AmbulanciaEntity entity = new AmbulanciaEntity();
        entity.setId(domain.getId());
        entity.setPlaca(domain.getPlaca());
        entity.setTipo(domain.getTipo());
        entity.setStatus(domain.getStatus());
        if (domain.getBase() != null) {
            BaseEntity baseEntity = new BaseEntity();
            baseEntity.setId(domain.getBase().getId());
            entity.setBase(baseEntity);
        }
        return entity;
    }

    @Override
    public Ambulancia toDomain(AmbulanciaEntity entity) {
        if (entity == null) return null;
        
        Ambulancia domain = new Ambulancia();
        domain.setId(entity.getId());
        domain.setPlaca(entity.getPlaca());
        domain.setTipo(entity.getTipo());
        domain.setStatus(entity.getStatus());
        if (entity.getBase() != null) {
            Base base = new Base();
            base.setId(entity.getBase().getId());
            if (entity.getBase().getBairro() != null) {
                base.setBairro(new sosrota.domain.models.Bairro());
                base.getBairro().setId(entity.getBase().getBairro().getId());
                base.getBairro().setNome(entity.getBase().getBairro().getNome());
            }
            domain.setBase(base);
        }
        return domain;
    }
}



