package sosrota.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.dtos.BairroDTO;
import sosrota.application.dtos.BairroRequestDTO;
import sosrota.application.services.BairroService;
import sosrota.infrastructure.persistence.entity.BairroEntity;
import sosrota.infrastructure.repositories.BairroRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BairroServiceImpl implements BairroService {

    @Autowired
    private BairroRepository bairroRepository;

    @Override
    @Transactional
    public BairroDTO criar(BairroRequestDTO request) {
        BairroEntity entity = new BairroEntity();
        entity.setNome(request.getNome());
        entity = bairroRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public BairroDTO buscarPorId(Long id) {
        BairroEntity entity = bairroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado com ID: " + id));
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BairroDTO> listarTodos() {
        return bairroRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BairroDTO atualizar(Long id, BairroRequestDTO request) {
        BairroEntity entity = bairroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado com ID: " + id));
        entity.setNome(request.getNome());
        entity = bairroRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!bairroRepository.existsById(id)) {
            throw new RuntimeException("Bairro não encontrado com ID: " + id);
        }
        bairroRepository.deleteById(id);
    }

    private BairroDTO toDTO(BairroEntity entity) {
        BairroDTO dto = new BairroDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        return dto;
    }
}



