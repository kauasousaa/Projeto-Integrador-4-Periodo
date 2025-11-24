package sosrota.application.services;

import sosrota.application.dtos.ProfissionalDTO;
import sosrota.application.dtos.ProfissionalRequestDTO;

import java.util.List;

public interface ProfissionalService {
    ProfissionalDTO criar(ProfissionalRequestDTO request);
    ProfissionalDTO buscarPorId(Long id);
    List<ProfissionalDTO> listarTodas();
    ProfissionalDTO atualizar(Long id, ProfissionalRequestDTO request);
    void deletar(Long id);
}

