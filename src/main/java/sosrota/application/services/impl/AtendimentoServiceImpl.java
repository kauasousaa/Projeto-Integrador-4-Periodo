package sosrota.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.dtos.AtendimentoDTO;
import sosrota.application.dtos.AmbulanciaMaisUtilizadaDTO;
import sosrota.application.services.AtendimentoService;
import sosrota.infrastructure.persistence.entity.AtendimentoEntity;
import sosrota.infrastructure.repositories.AtendimentoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AtendimentoServiceImpl implements AtendimentoService {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    @Transactional(readOnly = true)
    public AtendimentoDTO buscarPorId(Long id) {
        AtendimentoEntity entity = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado com ID: " + id));
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtendimentoDTO> listarTodos() {
        return atendimentoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtendimentoDTO> listarPorAmbulancia(Long ambulanciaId) {
        return atendimentoRepository.findByAmbulanciaId(ambulanciaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtendimentoDTO> listarPorOcorrencia(Long ocorrenciaId) {
        return atendimentoRepository.findByOcorrenciaId(ocorrenciaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtendimentoDTO> listarPorPeriodo(String dataInicio, String dataFim) {
        LocalDate inicio = LocalDate.parse(dataInicio, DATE_FORMATTER);
        LocalDate fim = LocalDate.parse(dataFim, DATE_FORMATTER);
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        return atendimentoRepository.findAll().stream()
                .filter(atendimento -> {
                    LocalDateTime dataDespacho = atendimento.getDataHoraDespacho();
                    return dataDespacho != null &&
                           !dataDespacho.isBefore(inicioDateTime) &&
                           !dataDespacho.isAfter(fimDateTime);
                })
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmbulanciaMaisUtilizadaDTO> consultarAmbulanciasMaisUtilizadas() {
        List<AtendimentoRepository.AmbulanciaMaisUtilizadaProjection> resultados = 
                atendimentoRepository.findAmbulanciasMaisUtilizadas();
        
        return IntStream.range(0, resultados.size())
                .mapToObj(i -> {
                    AtendimentoRepository.AmbulanciaMaisUtilizadaProjection proj = resultados.get(i);
                    AmbulanciaMaisUtilizadaDTO dto = new AmbulanciaMaisUtilizadaDTO();
                    dto.setPosicao(i + 1); // Posição começa em 1
                    dto.setAmbulanciaId(proj.getAmbulanciaId());
                    dto.setPlaca(proj.getPlaca());
                    dto.setTotalAtendimentos(proj.getTotalAtendimentos());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private AtendimentoDTO toDTO(AtendimentoEntity entity) {
        AtendimentoDTO dto = new AtendimentoDTO();
        dto.setId(entity.getId());
        if (entity.getOcorrencia() != null) {
            dto.setOcorrenciaId(entity.getOcorrencia().getId());
        }
        if (entity.getAmbulancia() != null) {
            dto.setAmbulanciaId(entity.getAmbulancia().getId());
        }
        dto.setDataHoraDespacho(entity.getDataHoraDespacho());
        dto.setDataHoraChegada(entity.getDataHoraChegada());
        dto.setDistanciaKm(entity.getDistanciaKm());
        return dto;
    }
}

