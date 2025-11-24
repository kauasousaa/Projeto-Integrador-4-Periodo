package sosrota.application.services;

import sosrota.application.dtos.AtendimentoDTO;
import sosrota.application.dtos.AmbulanciaMaisUtilizadaDTO;

import java.util.List;

public interface AtendimentoService {
    AtendimentoDTO buscarPorId(Long id);
    List<AtendimentoDTO> listarTodos();
    List<AtendimentoDTO> listarPorAmbulancia(Long ambulanciaId);
    List<AtendimentoDTO> listarPorOcorrencia(Long ocorrenciaId);
    List<AtendimentoDTO> listarPorPeriodo(String dataInicio, String dataFim);
    List<AmbulanciaMaisUtilizadaDTO> consultarAmbulanciasMaisUtilizadas();
}

