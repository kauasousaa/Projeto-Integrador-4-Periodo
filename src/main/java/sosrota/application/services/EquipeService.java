package sosrota.application.services;

import sosrota.application.dtos.EquipeDTO;
import sosrota.application.dtos.EquipeRequestDTO;

import java.util.List;

public interface EquipeService {
    EquipeDTO criar(EquipeRequestDTO request);
    EquipeDTO buscarPorId(Long id);
    List<EquipeDTO> listarTodas();
    EquipeDTO buscarPorAmbulanciaId(Long ambulanciaId);
    EquipeDTO atualizar(Long id, EquipeRequestDTO request);
    void deletar(Long id);
}

