package sosrota.application.services;

import sosrota.application.dtos.AmbulanciaDTO;
import sosrota.application.dtos.AmbulanciaRequestDTO;

import java.util.List;

public interface AmbulanciaService {
    AmbulanciaDTO criar(AmbulanciaRequestDTO request);
    AmbulanciaDTO buscarPorId(Long id);
    List<AmbulanciaDTO> listarTodas();
    List<AmbulanciaDTO> listarDisponiveis();
    AmbulanciaDTO atualizar(Long id, AmbulanciaRequestDTO request);
    void deletar(Long id);
    void atualizarStatus(Long id, String status);
}

