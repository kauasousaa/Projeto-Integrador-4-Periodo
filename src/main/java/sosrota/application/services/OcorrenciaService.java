package sosrota.application.services;

import sosrota.application.dtos.OcorrenciaDTO;
import sosrota.application.dtos.OcorrenciaRequestDTO;
import sosrota.application.dtos.OcorrenciaPorGravidadeDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface OcorrenciaService {
    OcorrenciaDTO criar(OcorrenciaRequestDTO request);
    OcorrenciaDTO buscarPorId(Long id);
    List<OcorrenciaDTO> listarTodas();
    List<OcorrenciaDTO> listarPorStatus(String status);
    OcorrenciaDTO atualizar(Long id, OcorrenciaRequestDTO request);
    void deletar(Long id);
    List<OcorrenciaPorGravidadeDTO> consultarOcorrenciasPorGravidadeEPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);
}

