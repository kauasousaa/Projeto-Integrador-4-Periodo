package sosrota.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.dtos.ProfissionalDTO;
import sosrota.application.dtos.ProfissionalRequestDTO;
import sosrota.application.services.ProfissionalService;
import sosrota.infrastructure.persistence.entity.ProfissionalEntity;
import sosrota.infrastructure.repositories.ProfissionalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfissionalServiceImpl implements ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Override
    @Transactional
    public ProfissionalDTO criar(ProfissionalRequestDTO request) {
        ProfissionalEntity entity = new ProfissionalEntity();
        entity.setNome(request.getNome());
        entity.setFuncao(request.getFuncao());
        entity.setContato(request.getContato());
        entity.setAtivo(true); // Por padrão, novo profissional é ativo
        entity = profissionalRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfissionalDTO buscarPorId(Long id) {
        ProfissionalEntity entity = profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado com ID: " + id));
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfissionalDTO> listarTodas() {
        return profissionalRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProfissionalDTO atualizar(Long id, ProfissionalRequestDTO request) {
        ProfissionalEntity entity = profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado com ID: " + id));
        entity.setNome(request.getNome());
        entity.setFuncao(request.getFuncao());
        entity.setContato(request.getContato());
        // Não altera o status 'ativo' aqui - pode criar método separado se necessário
        entity = profissionalRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!profissionalRepository.existsById(id)) {
            throw new RuntimeException("Profissional não encontrado com ID: " + id);
        }
        profissionalRepository.deleteById(id);
    }

    private ProfissionalDTO toDTO(ProfissionalEntity entity) {
        ProfissionalDTO dto = new ProfissionalDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setFuncao(entity.getFuncao());
        dto.setContato(entity.getContato());
        dto.setAtivo(entity.getAtivo());
        return dto;
    }
}

