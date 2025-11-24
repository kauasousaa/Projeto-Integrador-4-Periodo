package sosrota.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.dtos.OcorrenciaDTO;
import sosrota.application.dtos.OcorrenciaRequestDTO;
import sosrota.application.dtos.OcorrenciaPorGravidadeDTO;
import sosrota.application.mappers.OcorrenciaMapper;
import sosrota.application.services.OcorrenciaService;
import sosrota.domain.models.StatusOcorrencia;
import sosrota.infrastructure.persistence.entity.OcorrenciaEntity;
import sosrota.infrastructure.persistence.entity.BairroEntity;
import sosrota.infrastructure.repositories.OcorrenciaRepository;
import sosrota.infrastructure.repositories.BairroRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OcorrenciaServiceImpl implements OcorrenciaService {

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private BairroRepository bairroRepository;

    @Autowired
    private OcorrenciaMapper ocorrenciaMapper;

    @Override
    @Transactional
    public OcorrenciaDTO criar(OcorrenciaRequestDTO request) {
        BairroEntity bairro = bairroRepository.findById(request.getBairroId())
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado com ID: " + request.getBairroId()));

        OcorrenciaEntity entity = new OcorrenciaEntity();
        entity.setTipo(request.getTipo());
        entity.setGravidade(request.getGravidade());
        entity.setLocal(request.getLocal());
        entity.setObservacao(request.getObservacao());
        entity.setDataHoraAbertura(LocalDateTime.now());
        entity.setStatus(StatusOcorrencia.ABERTA); // Por padrão, nova ocorrência está aberta
        entity.setBairro(bairro);
        entity = ocorrenciaRepository.save(entity);
        return ocorrenciaMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public OcorrenciaDTO atualizar(Long id, OcorrenciaRequestDTO request) {
        OcorrenciaEntity entity = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada com ID: " + id));

        BairroEntity bairro = bairroRepository.findById(request.getBairroId())
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado com ID: " + request.getBairroId()));

        entity.setTipo(request.getTipo());
        entity.setGravidade(request.getGravidade());
        entity.setLocal(request.getLocal());
        entity.setObservacao(request.getObservacao());
        entity.setBairro(bairro);
        entity = ocorrenciaRepository.save(entity);
        return ocorrenciaMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public OcorrenciaDTO buscarPorId(Long id) {
        OcorrenciaEntity entity = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada com ID: " + id));
        return ocorrenciaMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OcorrenciaDTO> listarTodas() {
        return ocorrenciaRepository.findAll().stream()
                .map(ocorrenciaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OcorrenciaDTO> listarPorStatus(String status) {
        try {
            StatusOcorrencia statusEnum = StatusOcorrencia.valueOf(status.toUpperCase());
            return ocorrenciaRepository.findByStatus(statusEnum).stream()
                    .map(ocorrenciaMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + status);
        }
    }


    @Override
    @Transactional
    public void deletar(Long id) {
        if (!ocorrenciaRepository.existsById(id)) {
            throw new RuntimeException("Ocorrência não encontrada com ID: " + id);
        }
        ocorrenciaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OcorrenciaPorGravidadeDTO> consultarOcorrenciasPorGravidadeEPeriodo(
            LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<OcorrenciaRepository.OcorrenciaPorGravidadeProjection> resultados = 
                ocorrenciaRepository.countOcorrenciasPorGravidadeEPeriodo(dataInicio, dataFim);
        
        return resultados.stream()
                .map(proj -> {
                    OcorrenciaPorGravidadeDTO dto = new OcorrenciaPorGravidadeDTO();
                    dto.setGravidade(proj.getGravidade());
                    dto.setTotalOcorrencias(proj.getTotalOcorrencias());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

