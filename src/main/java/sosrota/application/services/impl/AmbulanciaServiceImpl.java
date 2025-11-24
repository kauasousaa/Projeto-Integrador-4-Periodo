package sosrota.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.dtos.AmbulanciaDTO;
import sosrota.application.dtos.AmbulanciaRequestDTO;
import sosrota.application.mappers.AmbulanciaMapper;
import sosrota.application.services.AmbulanciaService;
import sosrota.domain.models.StatusAmbulancia;
import sosrota.infrastructure.persistence.entity.AmbulanciaEntity;
import sosrota.infrastructure.persistence.entity.BaseEntity;
import sosrota.infrastructure.repositories.AmbulanciaRepository;
import sosrota.infrastructure.repositories.BaseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmbulanciaServiceImpl implements AmbulanciaService {

    @Autowired
    private AmbulanciaRepository ambulanciaRepository;

    @Autowired
    private BaseRepository baseRepository;

    @Autowired
    private AmbulanciaMapper ambulanciaMapper;

    @Override
    @Transactional
    public AmbulanciaDTO criar(AmbulanciaRequestDTO request) {
        // Verificar se placa já existe
        if (ambulanciaRepository.findByPlaca(request.getPlaca()).isPresent()) {
            throw new RuntimeException("Já existe uma ambulância com a placa: " + request.getPlaca());
        }

        BaseEntity base = baseRepository.findById(request.getBaseId())
                .orElseThrow(() -> new RuntimeException("Base não encontrada com ID: " + request.getBaseId()));

        AmbulanciaEntity entity = new AmbulanciaEntity();
        entity.setPlaca(request.getPlaca());
        entity.setTipo(request.getTipo());
        entity.setStatus(StatusAmbulancia.DISPONIVEL); // Por padrão, nova ambulância está disponível
        entity.setBase(base);
        entity = ambulanciaRepository.save(entity);
        return ambulanciaMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public AmbulanciaDTO buscarPorId(Long id) {
        AmbulanciaEntity entity = ambulanciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ambulância não encontrada com ID: " + id));
        return ambulanciaMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmbulanciaDTO> listarTodas() {
        return ambulanciaRepository.findAll().stream()
                .map(ambulanciaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmbulanciaDTO> listarDisponiveis() {
        return ambulanciaRepository.findByStatus(StatusAmbulancia.DISPONIVEL).stream()
                .map(ambulanciaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AmbulanciaDTO atualizar(Long id, AmbulanciaRequestDTO request) {
        AmbulanciaEntity entity = ambulanciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ambulância não encontrada com ID: " + id));

        // Verificar se placa já existe em outra ambulância
        ambulanciaRepository.findByPlaca(request.getPlaca())
                .ifPresent(amb -> {
                    if (!amb.getId().equals(id)) {
                        throw new RuntimeException("Já existe outra ambulância com a placa: " + request.getPlaca());
                    }
                });

        BaseEntity base = baseRepository.findById(request.getBaseId())
                .orElseThrow(() -> new RuntimeException("Base não encontrada com ID: " + request.getBaseId()));

        entity.setPlaca(request.getPlaca());
        entity.setTipo(request.getTipo());
        entity.setBase(base);
        entity = ambulanciaRepository.save(entity);
        return ambulanciaMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!ambulanciaRepository.existsById(id)) {
            throw new RuntimeException("Ambulância não encontrada com ID: " + id);
        }
        ambulanciaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void atualizarStatus(Long id, String status) {
        AmbulanciaEntity entity = ambulanciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ambulância não encontrada com ID: " + id));

        try {
            StatusAmbulancia novoStatus = StatusAmbulancia.valueOf(status.toUpperCase());
            entity.setStatus(novoStatus);
            ambulanciaRepository.save(entity);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + status);
        }
    }
}



