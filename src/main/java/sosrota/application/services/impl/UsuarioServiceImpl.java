package sosrota.application.services.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.dtos.LoginDTO;
import sosrota.application.dtos.UsuarioDTO;
import sosrota.application.dtos.UsuarioRequestDTO;
import sosrota.application.services.UsuarioService;
import sosrota.infrastructure.persistence.entity.UsuarioEntity;
import sosrota.infrastructure.repositories.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO autenticar(LoginDTO loginDTO) {
        if (loginDTO.getLogin() == null || loginDTO.getLogin().trim().isEmpty()) {
            throw new RuntimeException("Login não pode ser vazio");
        }
        if (loginDTO.getSenha() == null || loginDTO.getSenha().trim().isEmpty()) {
            throw new RuntimeException("Senha não pode ser vazia");
        }

        UsuarioEntity usuarioEntity = usuarioRepository.findByLogin(loginDTO.getLogin())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        // Verificar senha usando BCrypt
        if (!BCrypt.checkpw(loginDTO.getSenha(), usuarioEntity.getSenhaHash())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        return toDTO(usuarioEntity);
    }

    @Override
    @Transactional
    public UsuarioDTO criar(UsuarioRequestDTO request) {
        if (request.getLogin() == null || request.getLogin().trim().isEmpty()) {
            throw new RuntimeException("Login não pode ser vazio");
        }
        if (request.getSenha() == null || request.getSenha().trim().isEmpty()) {
            throw new RuntimeException("Senha não pode ser vazia");
        }
        if (request.getPerfil() == null) {
            throw new RuntimeException("Perfil não pode ser nulo");
        }

        // Verificar se login já existe
        if (usuarioRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new RuntimeException("Login já existe");
        }

        // Gerar hash da senha usando BCrypt
        String senhaHash = BCrypt.hashpw(request.getSenha(), BCrypt.gensalt());

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setLogin(request.getLogin());
        usuarioEntity.setSenhaHash(senhaHash);
        usuarioEntity.setPerfil(request.getPerfil());

        usuarioEntity = usuarioRepository.save(usuarioEntity);
        return toDTO(usuarioEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        return toDTO(usuarioEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO toDTO(UsuarioEntity entity) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entity.getId());
        dto.setLogin(entity.getLogin());
        dto.setPerfil(entity.getPerfil());
        return dto;
    }
}

