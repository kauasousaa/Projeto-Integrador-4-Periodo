package sosrota.application.services;

import sosrota.application.dtos.BaseDTO;
import sosrota.application.dtos.BaseRequestDTO;

import java.util.List;

public interface BaseService {
    BaseDTO criar(BaseRequestDTO request);
    BaseDTO buscarPorId(Long id);
    List<BaseDTO> listarTodas();
    BaseDTO atualizar(Long id, BaseRequestDTO request);
    void deletar(Long id);
}

