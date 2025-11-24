package sosrota.application.services;

import sosrota.application.dtos.LoginDTO;
import sosrota.application.dtos.UsuarioDTO;
import sosrota.application.dtos.UsuarioRequestDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO autenticar(LoginDTO loginDTO);
    UsuarioDTO criar(UsuarioRequestDTO request);
    UsuarioDTO buscarPorId(Long id);
    List<UsuarioDTO> listarTodos();
    void deletar(Long id);
}

