package sosrota.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.dtos.BaseDTO;
import sosrota.application.dtos.BaseRequestDTO;
import sosrota.application.services.BaseService;
import sosrota.infrastructure.persistence.entity.BaseEntity;
import sosrota.infrastructure.persistence.entity.BairroEntity;
import sosrota.infrastructure.repositories.BaseRepository;
import sosrota.infrastructure.repositories.BairroRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseRepository baseRepository;

    @Autowired
    private BairroRepository bairroRepository;

    @Override
    @Transactional
    public BaseDTO criar(BaseRequestDTO request) {
        BairroEntity bairro = bairroRepository.findById(request.getBairroId())
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado com ID: " + request.getBairroId()));

        BaseEntity entity = new BaseEntity();
        entity.setNome(request.getNome());
        entity.setEndereco(request.getEndereco());
        entity.setBairro(bairro);
        entity = baseRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseDTO buscarPorId(Long id) {
        BaseEntity entity = baseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Base não encontrada com ID: " + id));
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BaseDTO> listarTodas() {
        return baseRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BaseDTO atualizar(Long id, BaseRequestDTO request) {
        BaseEntity entity = baseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Base não encontrada com ID: " + id));

        BairroEntity bairro = bairroRepository.findById(request.getBairroId())
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado com ID: " + request.getBairroId()));

        entity.setNome(request.getNome());
        entity.setEndereco(request.getEndereco());
        entity.setBairro(bairro);
        entity = baseRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!baseRepository.existsById(id)) {
            throw new RuntimeException("Base não encontrada com ID: " + id);
        }
        baseRepository.deleteById(id);
    }

    private BaseDTO toDTO(BaseEntity entity) {
        BaseDTO dto = new BaseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEndereco(entity.getEndereco());
        if (entity.getBairro() != null) {
            dto.setBairroId(entity.getBairro().getId());
            dto.setBairroNome(entity.getBairro().getNome());
        }
        return dto;
    }
}



