package sosrota.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.dtos.AmbulanciaDTO;
import sosrota.application.dtos.AmbulanciaSugeridaDTO;
import sosrota.application.dtos.DespachoSugestaoDTO;
import sosrota.application.dtos.RotaCalculadaDTO;
import sosrota.application.mappers.AmbulanciaMapper;
import sosrota.application.mappers.OcorrenciaMapper;
import sosrota.application.services.DespachoService;
import sosrota.domain.models.Ambulancia;
import sosrota.domain.models.Conexao;
import sosrota.domain.models.Equipe;
import sosrota.domain.models.Ocorrencia;
import sosrota.domain.models.StatusAmbulancia;
import sosrota.domain.models.StatusOcorrencia;
import sosrota.domain.services.DespachoDomainService;
import sosrota.domain.services.impl.DespachoDomainServiceImpl;
import sosrota.domain.services.impl.DijkstraService;
import sosrota.infrastructure.persistence.entity.AmbulanciaEntity;
import sosrota.infrastructure.persistence.entity.AtendimentoEntity;
import sosrota.infrastructure.persistence.entity.ConexaoEntity;
import sosrota.infrastructure.persistence.entity.EquipeEntity;
import sosrota.infrastructure.persistence.entity.EquipeProfissionalEntity;
import sosrota.infrastructure.persistence.entity.OcorrenciaEntity;
import sosrota.infrastructure.repositories.AmbulanciaRepository;
import sosrota.infrastructure.repositories.AtendimentoRepository;
import sosrota.infrastructure.repositories.ConexaoRepository;
import sosrota.infrastructure.repositories.EquipeProfissionalRepository;
import sosrota.infrastructure.repositories.EquipeRepository;
import sosrota.infrastructure.repositories.OcorrenciaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DespachoServiceImpl implements DespachoService {

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private AmbulanciaRepository ambulanciaRepository;

    @Autowired
    private ConexaoRepository conexaoRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private EquipeProfissionalRepository equipeProfissionalRepository;

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private OcorrenciaMapper ocorrenciaMapper;

    @Autowired
    private AmbulanciaMapper ambulanciaMapper;

    @Autowired
    private DespachoDomainService despachoDomainService;

    @Override
    public DespachoSugestaoDTO sugerirAmbulancias(Long ocorrenciaId) {
        OcorrenciaEntity ocorrenciaEntity = ocorrenciaRepository.findById(ocorrenciaId)
            .orElseThrow(() -> new IllegalArgumentException("Ocorrência não encontrada: " + ocorrenciaId));

        Ocorrencia ocorrencia = ocorrenciaMapper.toDomain(ocorrenciaEntity);

        // Buscar todas as ambulâncias disponíveis
        List<AmbulanciaEntity> ambulanciasDisponiveis = ambulanciaRepository
            .findByStatus(StatusAmbulancia.DISPONIVEL);

        // Filtrar ambulâncias aptas
        List<AmbulanciaSugeridaDTO> ambulanciasSugeridas = new ArrayList<>();

        for (AmbulanciaEntity ambulanciaEntity : ambulanciasDisponiveis) {
            Ambulancia ambulancia = ambulanciaMapper.toDomain(ambulanciaEntity);
            
            // Carregar equipe da ambulância
            carregarEquipeNaAmbulancia(ambulancia);

            try {
                // Validar se pode despachar
                despachoDomainService.podeDespachar(ambulancia, ocorrencia);

                // Calcular rota
                Long origemId = ambulancia.getBase().getBairro().getId();
                Long destinoId = ocorrencia.getBairro().getId();
                RotaCalculadaDTO rota = calcularRota(origemId, destinoId);

                if (rota != null) {
                    // Validar SLA
                    despachoDomainService.validarSLA(ocorrencia, rota.getDistanciaKm());

                    // Criar sugestão
                    AmbulanciaSugeridaDTO sugestao = new AmbulanciaSugeridaDTO();
                    sugestao.setAmbulanciaId(ambulancia.getId());
                    sugestao.setPlaca(ambulancia.getPlaca());
                    sugestao.setTipo(ambulancia.getTipo());
                    sugestao.setBaseNome(ambulancia.getBase().getNome());
                    sugestao.setDistanciaKm(rota.getDistanciaKm());
                    sugestao.setTempoEstimadoMinutos(
                        (int) Math.ceil(DespachoDomainServiceImpl.calcularTempoEstimadoMinutos(rota.getDistanciaKm()))
                    );
                    sugestao.setSlaCumprido(true);

                    ambulanciasSugeridas.add(sugestao);
                }
            } catch (Exception e) {
                // Ambulância não é apta, pular
                continue;
            }
        }

        // Ordenar por distância (mais próxima primeiro)
        ambulanciasSugeridas.sort(Comparator.comparingDouble(AmbulanciaSugeridaDTO::getDistanciaKm));

        // Criar DTO de resposta
        DespachoSugestaoDTO resultado = new DespachoSugestaoDTO();
        resultado.setOcorrenciaId(ocorrenciaId);
        resultado.setAmbulanciasSugeridas(ambulanciasSugeridas);

        if (!ambulanciasSugeridas.isEmpty()) {
            AmbulanciaSugeridaDTO melhor = ambulanciasSugeridas.get(0);
            resultado.setDistanciaKm(melhor.getDistanciaKm());
            resultado.setTempoEstimadoMinutos(melhor.getTempoEstimadoMinutos());
            resultado.setSlaCumprido(melhor.getSlaCumprido());
        }

        return resultado;
    }

    @Override
    public RotaCalculadaDTO calcularRota(Long origemId, Long destinoId) {
        // Buscar todas as conexões
        List<ConexaoEntity> conexoesEntity = conexaoRepository.findAll();
        
        // Converter para modelos de domínio
        List<Conexao> conexoes = conexoesEntity.stream()
            .map(this::toDomainConexao)
            .collect(Collectors.toList());

        // Calcular caminho mínimo usando Dijkstra
        DijkstraService.ResultadoDijkstra resultado = DijkstraService.calcularCaminhoMinimo(
            origemId, destinoId, conexoes
        );

        if (resultado == null) {
            return null;
        }

        // Converter para DTO
        RotaCalculadaDTO rota = new RotaCalculadaDTO();
        rota.setOrigemId(origemId);
        rota.setDestinoId(destinoId);
        rota.setDistanciaKm(resultado.getDistanciaKm());
        rota.setCaminho(resultado.getCaminho());
        rota.setTempoMinutos(
            (int) Math.ceil(DespachoDomainServiceImpl.calcularTempoEstimadoMinutos(resultado.getDistanciaKm()))
        );

        return rota;
    }

    @Override
    @Transactional
    public void executarDespacho(Long ocorrenciaId, Long ambulanciaId) {
        OcorrenciaEntity ocorrenciaEntity = ocorrenciaRepository.findById(ocorrenciaId)
            .orElseThrow(() -> new IllegalArgumentException("Ocorrência não encontrada: " + ocorrenciaId));

        AmbulanciaEntity ambulanciaEntity = ambulanciaRepository.findById(ambulanciaId)
            .orElseThrow(() -> new IllegalArgumentException("Ambulância não encontrada: " + ambulanciaId));

        Ocorrencia ocorrencia = ocorrenciaMapper.toDomain(ocorrenciaEntity);
        Ambulancia ambulancia = ambulanciaMapper.toDomain(ambulanciaEntity);
        
        // Carregar equipe da ambulância
        carregarEquipeNaAmbulancia(ambulancia);

        // Validar se pode despachar
        despachoDomainService.podeDespachar(ambulancia, ocorrencia);

        // Calcular rota
        Long origemId = ambulancia.getBase().getBairro().getId();
        Long destinoId = ocorrencia.getBairro().getId();
        RotaCalculadaDTO rota = calcularRota(origemId, destinoId);

        if (rota == null) {
            throw new IllegalArgumentException("Não foi possível calcular rota entre base e ocorrência");
        }

        // Validar SLA
        despachoDomainService.validarSLA(ocorrencia, rota.getDistanciaKm());

        // Criar registro de atendimento
        AtendimentoEntity atendimento = new AtendimentoEntity();
        atendimento.setOcorrencia(ocorrenciaEntity);
        atendimento.setAmbulancia(ambulanciaEntity);
        atendimento.setDataHoraDespacho(LocalDateTime.now());
        atendimento.setDistanciaKm(rota.getDistanciaKm());
        atendimentoRepository.save(atendimento);

        // Atualizar status da ocorrência
        ocorrenciaEntity.setStatus(StatusOcorrencia.DESPACHADA);
        ocorrenciaRepository.save(ocorrenciaEntity);

        // Atualizar status da ambulância
        ambulanciaEntity.setStatus(StatusAmbulancia.EM_ATENDIMENTO);
        ambulanciaRepository.save(ambulanciaEntity);
    }

    /**
     * Converte ConexaoEntity para modelo de domínio Conexao
     */
    private Conexao toDomainConexao(ConexaoEntity entity) {
        Conexao conexao = new Conexao();
        conexao.setId(entity.getId());
        conexao.setDistanciaKm(entity.getDistanciaKm());
        
        if (entity.getOrigem() != null) {
            sosrota.domain.models.Bairro origem = new sosrota.domain.models.Bairro();
            origem.setId(entity.getOrigem().getId());
            origem.setNome(entity.getOrigem().getNome());
            conexao.setOrigem(origem);
        }
        
        if (entity.getDestino() != null) {
            sosrota.domain.models.Bairro destino = new sosrota.domain.models.Bairro();
            destino.setId(entity.getDestino().getId());
            destino.setNome(entity.getDestino().getNome());
            conexao.setDestino(destino);
        }
        
        return conexao;
    }
    
    /**
     * Carrega a equipe na ambulância para validação
     */
    private void carregarEquipeNaAmbulancia(Ambulancia ambulancia) {
        // A equipe será carregada pelo domain service quando necessário
        // Este método pode ser usado para pré-carregar se necessário
    }
}

