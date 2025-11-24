package sosrota.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.dtos.EquipeDTO;
import sosrota.application.dtos.EquipeRequestDTO;
import sosrota.application.dtos.ProfissionalDTO;
import sosrota.application.services.EquipeService;
import sosrota.infrastructure.persistence.entity.EquipeEntity;
import sosrota.infrastructure.persistence.entity.EquipeProfissionalEntity;
import sosrota.infrastructure.persistence.entity.AmbulanciaEntity;
import sosrota.infrastructure.persistence.entity.ProfissionalEntity;
import sosrota.infrastructure.repositories.EquipeRepository;
import sosrota.infrastructure.repositories.EquipeProfissionalRepository;
import sosrota.infrastructure.repositories.AmbulanciaRepository;
import sosrota.infrastructure.repositories.ProfissionalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipeServiceImpl implements EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private EquipeProfissionalRepository equipeProfissionalRepository;

    @Autowired
    private AmbulanciaRepository ambulanciaRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Override
    @Transactional
    public EquipeDTO criar(EquipeRequestDTO request) {
        AmbulanciaEntity ambulancia = ambulanciaRepository.findById(request.getAmbulanciaId())
                .orElseThrow(() -> new RuntimeException("Ambulância não encontrada com ID: " + request.getAmbulanciaId()));

        EquipeEntity entity = new EquipeEntity();
        entity.setDescricao(request.getDescricao());
        entity.setAmbulancia(ambulancia);
        entity = equipeRepository.save(entity);

        // Adicionar profissionais à equipe
        if (request.getProfissionalIds() != null && !request.getProfissionalIds().isEmpty()) {
            for (Long profissionalId : request.getProfissionalIds()) {
                ProfissionalEntity profissional = profissionalRepository.findById(profissionalId)
                        .orElseThrow(() -> new RuntimeException("Profissional não encontrado com ID: " + profissionalId));

                EquipeProfissionalEntity equipeProfissional = new EquipeProfissionalEntity();
                equipeProfissional.setEquipe(entity);
                equipeProfissional.setProfissional(profissional);
                equipeProfissionalRepository.save(equipeProfissional);
            }
        }

        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipeDTO buscarPorId(Long id) {
        EquipeEntity entity = equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada com ID: " + id));
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipeDTO> listarTodas() {
        return equipeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EquipeDTO buscarPorAmbulanciaId(Long ambulanciaId) {
        EquipeEntity entity = equipeRepository.findByAmbulanciaId(ambulanciaId)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada para ambulância com ID: " + ambulanciaId));
        return toDTO(entity);
    }

    @Override
    @Transactional
    public EquipeDTO atualizar(Long id, EquipeRequestDTO request) {
        EquipeEntity entity = equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada com ID: " + id));

        AmbulanciaEntity ambulancia = ambulanciaRepository.findById(request.getAmbulanciaId())
                .orElseThrow(() -> new RuntimeException("Ambulância não encontrada com ID: " + request.getAmbulanciaId()));

        entity.setDescricao(request.getDescricao());
        entity.setAmbulancia(ambulancia);
        entity = equipeRepository.save(entity);

        // Remover profissionais antigos
        List<EquipeProfissionalEntity> profissionaisAntigos = equipeProfissionalRepository.findByEquipeId(id);
        equipeProfissionalRepository.deleteAll(profissionaisAntigos);

        // Adicionar novos profissionais
        if (request.getProfissionalIds() != null && !request.getProfissionalIds().isEmpty()) {
            for (Long profissionalId : request.getProfissionalIds()) {
                ProfissionalEntity profissional = profissionalRepository.findById(profissionalId)
                        .orElseThrow(() -> new RuntimeException("Profissional não encontrado com ID: " + profissionalId));

                EquipeProfissionalEntity equipeProfissional = new EquipeProfissionalEntity();
                equipeProfissional.setEquipe(entity);
                equipeProfissional.setProfissional(profissional);
                equipeProfissionalRepository.save(equipeProfissional);
            }
        }

        return toDTO(entity);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!equipeRepository.existsById(id)) {
            throw new RuntimeException("Equipe não encontrada com ID: " + id);
        }

        // Remover relacionamentos primeiro
        List<EquipeProfissionalEntity> profissionais = equipeProfissionalRepository.findByEquipeId(id);
        equipeProfissionalRepository.deleteAll(profissionais);

        equipeRepository.deleteById(id);
    }

    private EquipeDTO toDTO(EquipeEntity entity) {
        EquipeDTO dto = new EquipeDTO();
        dto.setId(entity.getId());
        dto.setDescricao(entity.getDescricao());
        if (entity.getAmbulancia() != null) {
            dto.setAmbulanciaId(entity.getAmbulancia().getId());
        }

        // Carregar profissionais da equipe
        List<EquipeProfissionalEntity> equipeProfissionais = equipeProfissionalRepository.findByEquipeId(entity.getId());
        List<ProfissionalDTO> profissionais = equipeProfissionais.stream()
                .map(ep -> {
                    ProfissionalDTO pDto = new ProfissionalDTO();
                    pDto.setId(ep.getProfissional().getId());
                    pDto.setNome(ep.getProfissional().getNome());
                    pDto.setFuncao(ep.getProfissional().getFuncao());
                    pDto.setContato(ep.getProfissional().getContato());
                    pDto.setAtivo(ep.getProfissional().getAtivo());
                    return pDto;
                })
                .collect(Collectors.toList());
        dto.setProfissionais(profissionais);

        return dto;
    }
}



