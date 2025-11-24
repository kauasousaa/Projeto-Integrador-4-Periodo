package sosrota.application.services;

import sosrota.application.dtos.BairroDTO;
import sosrota.application.dtos.BairroRequestDTO;

import java.util.List;

public interface BairroService {
    BairroDTO criar(BairroRequestDTO request);
    BairroDTO buscarPorId(Long id);
    List<BairroDTO> listarTodos();
    BairroDTO atualizar(Long id, BairroRequestDTO request);
    void deletar(Long id);
}

