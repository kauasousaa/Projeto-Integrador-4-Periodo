package sosrota.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sosrota.domain.exceptions.AmbulanciaNaoDisponivelException;
import sosrota.domain.exceptions.EquipeIncompletaException;
import sosrota.domain.exceptions.SLAAtingidoException;
import sosrota.domain.models.Ambulancia;
import sosrota.domain.models.Equipe;
import sosrota.domain.models.Gravidade;
import sosrota.domain.models.Ocorrencia;
import sosrota.domain.models.Profissional;
import sosrota.domain.models.StatusAmbulancia;
import sosrota.domain.models.TipoAmbulancia;
import sosrota.domain.services.DespachoDomainService;
import sosrota.infrastructure.persistence.entity.EquipeEntity;
import sosrota.infrastructure.persistence.entity.EquipeProfissionalEntity;
import sosrota.infrastructure.persistence.entity.ProfissionalEntity;
import sosrota.infrastructure.repositories.EquipeProfissionalRepository;
import sosrota.infrastructure.repositories.EquipeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DespachoDomainServiceImpl implements DespachoDomainService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private EquipeProfissionalRepository equipeProfissionalRepository;

    // SLAs por gravidade (em minutos)
    private static final int SLA_ALTA = 8;
    private static final int SLA_MEDIA = 15;
    private static final int SLA_BAIXA = 30;

    // Velocidade média estimada (km/h) para cálculo de tempo
    private static final double VELOCIDADE_MEDIA_KMH = 60.0;

    @Override
    public boolean podeDespachar(Ambulancia ambulancia, Ocorrencia ocorrencia) {
        // Regra: Ambulância só pode ser despachada se estiver Disponível
        if (ambulancia.getStatus() != StatusAmbulancia.DISPONIVEL) {
            throw new AmbulanciaNaoDisponivelException(
                "Ambulância " + ambulancia.getPlaca() + " não está disponível. Status: " + ambulancia.getStatus()
            );
        }

        // Regra: Validar compatibilidade de tipo
        validarCompatibilidadeTipo(ambulancia, ocorrencia);

        // Regra: Validar se a equipe está completa (apenas se a ambulância tiver ID)
        if (ambulancia.getId() != null) {
            validarEquipeCompleta(ambulancia);
        }

        return true;
    }

    @Override
    public void validarSLA(Ocorrencia ocorrencia, Double distanciaKm) {
        int slaMinutos = getSLAPorGravidade(ocorrencia.getGravidade());
        
        // Calcular tempo estimado em minutos
        double tempoEstimadoMinutos = (distanciaKm / VELOCIDADE_MEDIA_KMH) * 60;
        
        if (tempoEstimadoMinutos > slaMinutos) {
            throw new SLAAtingidoException(
                String.format(
                    "SLA não será cumprido. Distância: %.2f km, Tempo estimado: %.1f min, SLA máximo: %d min",
                    distanciaKm, tempoEstimadoMinutos, slaMinutos
                )
            );
        }
    }

    @Override
    public void validarCompatibilidadeTipo(Ambulancia ambulancia, Ocorrencia ocorrencia) {
        // Regra: Gravidade Alta exige ambulância UTI
        if (ocorrencia.getGravidade() == Gravidade.ALTA && ambulancia.getTipo() != TipoAmbulancia.UTI) {
            throw new IllegalArgumentException(
                "Ocorrências de gravidade ALTA exigem ambulância do tipo UTI"
            );
        }
        
        // Para gravidade MÉDIA ou BAIXA, qualquer tipo de ambulância serve
    }

    /**
     * Valida se a equipe da ambulância está completa
     * Equipe completa: deve ter pelo menos 1 médico, 1 enfermeiro e 1 condutor
     */
    private void validarEquipeCompleta(Ambulancia ambulancia) {
        Equipe equipe = buscarEquipePorAmbulancia(ambulancia);
        
        if (equipe == null || equipe.getProfissionais() == null || equipe.getProfissionais().isEmpty()) {
            throw new EquipeIncompletaException(
                "Ambulância " + ambulancia.getPlaca() + " não possui equipe cadastrada"
            );
        }

        List<Profissional> profissionais = equipe.getProfissionais();
        
        boolean temMedico = profissionais.stream()
            .anyMatch(p -> "Médico".equalsIgnoreCase(p.getFuncao()) && Boolean.TRUE.equals(p.getAtivo()));
        
        boolean temEnfermeiro = profissionais.stream()
            .anyMatch(p -> "Enfermeiro".equalsIgnoreCase(p.getFuncao()) && Boolean.TRUE.equals(p.getAtivo()));
        
        boolean temCondutor = profissionais.stream()
            .anyMatch(p -> "Condutor".equalsIgnoreCase(p.getFuncao()) && Boolean.TRUE.equals(p.getAtivo()));

        if (!temMedico || !temEnfermeiro || !temCondutor) {
            throw new EquipeIncompletaException(
                "Equipe da ambulância " + ambulancia.getPlaca() + " está incompleta. " +
                "Necessário: Médico, Enfermeiro e Condutor ativos"
            );
        }
    }

    /**
     * Busca a equipe associada à ambulância
     */
    private Equipe buscarEquipePorAmbulancia(Ambulancia ambulancia) {
        EquipeEntity equipeEntity = equipeRepository.findByAmbulanciaId(ambulancia.getId())
            .orElse(null);
        
        if (equipeEntity == null) {
            return null;
        }

        // Buscar profissionais da equipe
        List<EquipeProfissionalEntity> equipeProfissionais = equipeProfissionalRepository
            .findByEquipeId(equipeEntity.getId());

        List<Profissional> profissionais = equipeProfissionais.stream()
            .map(ep -> {
                ProfissionalEntity pe = ep.getProfissional();
                Profissional p = new Profissional();
                p.setId(pe.getId());
                p.setNome(pe.getNome());
                p.setFuncao(pe.getFuncao());
                p.setContato(pe.getContato());
                p.setAtivo(pe.getAtivo());
                return p;
            })
            .collect(Collectors.toList());

        Equipe equipe = new Equipe();
        equipe.setId(equipeEntity.getId());
        equipe.setDescricao(equipeEntity.getDescricao());
        equipe.setProfissionais(profissionais);
        
        return equipe;
    }

    /**
     * Retorna o SLA em minutos baseado na gravidade
     */
    public static int getSLAPorGravidade(Gravidade gravidade) {
        switch (gravidade) {
            case ALTA:
                return SLA_ALTA;
            case MEDIA:
                return SLA_MEDIA;
            case BAIXA:
                return SLA_BAIXA;
            default:
                return SLA_BAIXA;
        }
    }

    /**
     * Calcula o tempo estimado em minutos baseado na distância
     */
    public static double calcularTempoEstimadoMinutos(Double distanciaKm) {
        return (distanciaKm / VELOCIDADE_MEDIA_KMH) * 60;
    }
}

