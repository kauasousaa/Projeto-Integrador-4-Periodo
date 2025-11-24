package sosrota.application.mappers.impl;

import org.springframework.stereotype.Component;
import sosrota.application.dtos.OcorrenciaDTO;
import sosrota.application.mappers.OcorrenciaMapper;
import sosrota.domain.models.Bairro;
import sosrota.domain.models.Ocorrencia;
import sosrota.infrastructure.persistence.entity.BairroEntity;
import sosrota.infrastructure.persistence.entity.OcorrenciaEntity;

@Component
public class OcorrenciaMapperImpl implements OcorrenciaMapper {

    @Override
    public OcorrenciaDTO toDTO(OcorrenciaEntity entity) {
        if (entity == null) return null;
        
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo());
        dto.setGravidade(entity.getGravidade());
        dto.setLocal(entity.getLocal());
        dto.setDataHoraAbertura(entity.getDataHoraAbertura());
        dto.setStatus(entity.getStatus());
        dto.setObservacao(entity.getObservacao());
        if (entity.getBairro() != null) {
            dto.setBairroId(entity.getBairro().getId());
            dto.setBairroNome(entity.getBairro().getNome());
        }
        return dto;
    }

    @Override
    public OcorrenciaEntity toEntity(Ocorrencia domain) {
        if (domain == null) return null;
        
        OcorrenciaEntity entity = new OcorrenciaEntity();
        entity.setId(domain.getId());
        entity.setTipo(domain.getTipo());
        entity.setGravidade(domain.getGravidade());
        entity.setLocal(domain.getLocal());
        entity.setDataHoraAbertura(domain.getDataHoraAbertura());
        entity.setStatus(domain.getStatus());
        entity.setObservacao(domain.getObservacao());
        if (domain.getBairro() != null) {
            BairroEntity bairroEntity = new BairroEntity();
            bairroEntity.setId(domain.getBairro().getId());
            entity.setBairro(bairroEntity);
        }
        return entity;
    }

    @Override
    public Ocorrencia toDomain(OcorrenciaEntity entity) {
        if (entity == null) return null;
        
        Ocorrencia domain = new Ocorrencia();
        domain.setId(entity.getId());
        domain.setTipo(entity.getTipo());
        domain.setGravidade(entity.getGravidade());
        domain.setLocal(entity.getLocal());
        domain.setDataHoraAbertura(entity.getDataHoraAbertura());
        domain.setStatus(entity.getStatus());
        domain.setObservacao(entity.getObservacao());
        if (entity.getBairro() != null) {
            Bairro bairro = new Bairro();
            bairro.setId(entity.getBairro().getId());
            bairro.setNome(entity.getBairro().getNome());
            domain.setBairro(bairro);
        }
        return domain;
    }
}

