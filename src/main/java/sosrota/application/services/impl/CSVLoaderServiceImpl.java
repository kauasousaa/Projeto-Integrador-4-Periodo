package sosrota.application.services.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosrota.application.services.CSVLoaderService;
import sosrota.application.services.LogCallback;
import sosrota.infrastructure.persistence.entity.BairroEntity;
import sosrota.infrastructure.persistence.entity.ConexaoEntity;
import sosrota.infrastructure.repositories.BairroRepository;
import sosrota.infrastructure.repositories.ConexaoRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CSVLoaderServiceImpl implements CSVLoaderService {

    @Autowired
    private BairroRepository bairroRepository;

    @Autowired
    private ConexaoRepository conexaoRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void carregarBairros(String caminhoArquivo, LogCallback callback) {
        if (callback == null) {
            callback = msg -> {}; // Callback vazio se não fornecido
        }
        
        try (CSVReader reader = new CSVReader(new FileReader(caminhoArquivo))) {
            List<String[]> linhas = reader.readAll();
            int totalLinhas = linhas.size() - 1; // Exclui cabeçalho
            callback.log("Lendo arquivo de bairros...");
            callback.log("Total de registros encontrados: " + totalLinhas);
            
            int novos = 0;
            int atualizados = 0;
            
            // Pula o cabeçalho (primeira linha)
            for (int i = 1; i < linhas.size(); i++) {
                String[] linha = linhas.get(i);
                if (linha.length >= 2) {
                    Long id = Long.parseLong(linha[0].trim());
                    String nome = linha[1].trim();
                    
                    // Verifica se já existe
                    BairroEntity bairro = bairroRepository.findById(id).orElse(null);
                    boolean isNovo = (bairro == null);
                    
                    if (bairro == null) {
                        bairro = new BairroEntity();
                        bairro.setId(id);
                        novos++;
                    } else {
                        atualizados++;
                    }
                    bairro.setNome(nome);
                    
                    // Usa merge para permitir IDs manuais
                    entityManager.merge(bairro);
                    
                    // Log de progresso a cada 50 registros
                    if (i % 50 == 0) {
                        callback.log(String.format("Processando bairros... %d/%d (%.1f%%)", 
                            i, totalLinhas, (i * 100.0 / totalLinhas)));
                    }
                    
                    // Flush a cada 10 registros para melhor performance
                    if (i % 10 == 0) {
                        entityManager.flush();
                        entityManager.clear();
                    }
                }
            }
            entityManager.flush();
            callback.log(String.format("✓ Bairros importados: %d novos, %d atualizados (Total: %d)", 
                novos, atualizados, novos + atualizados));
        } catch (IOException | CsvException e) {
            callback.log("✗ Erro ao carregar bairros: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar bairros do CSV: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void carregarConexoes(String caminhoArquivo, LogCallback callback) {
        if (callback == null) {
            callback = msg -> {}; // Callback vazio se não fornecido
        }
        
        try (CSVReader reader = new CSVReader(new FileReader(caminhoArquivo))) {
            callback.log("Carregando bairros do banco de dados...");
            // Primeiro, carrega todos os bairros em um mapa para busca rápida
            Map<Long, BairroEntity> bairrosMap = new HashMap<>();
            bairroRepository.findAll().forEach(b -> bairrosMap.put(b.getId(), b));
            callback.log("Bairros carregados: " + bairrosMap.size());
            
            List<String[]> linhas = reader.readAll();
            int totalLinhas = linhas.size() - 1; // Exclui cabeçalho
            callback.log("Lendo arquivo de conexões...");
            callback.log("Total de registros encontrados: " + totalLinhas);
            
            int novos = 0;
            int atualizados = 0;
            
            // Pula o cabeçalho (primeira linha)
            for (int i = 1; i < linhas.size(); i++) {
                String[] linha = linhas.get(i);
                if (linha.length >= 4) {
                    Long id = Long.parseLong(linha[0].trim());
                    Long origemId = Long.parseLong(linha[1].trim());
                    Long destinoId = Long.parseLong(linha[2].trim());
                    Double distancia = Double.parseDouble(linha[3].trim());
                    
                    BairroEntity origem = bairrosMap.get(origemId);
                    BairroEntity destino = bairrosMap.get(destinoId);
                    
                    if (origem == null || destino == null) {
                        callback.log(String.format("✗ Erro: Bairro não encontrado - origem=%d, destino=%d", 
                            origemId, destinoId));
                        throw new RuntimeException(
                            String.format("Bairro não encontrado: origem=%d, destino=%d", origemId, destinoId)
                        );
                    }
                    
                    // Verifica se já existe
                    ConexaoEntity conexao = conexaoRepository.findById(id).orElse(null);
                    boolean isNovo = (conexao == null);
                    
                    if (conexao == null) {
                        conexao = new ConexaoEntity();
                        conexao.setId(id);
                        novos++;
                    } else {
                        atualizados++;
                    }
                    conexao.setOrigem(origem);
                    conexao.setDestino(destino);
                    conexao.setDistanciaKm(distancia);
                    
                    // Usa merge para permitir IDs manuais
                    entityManager.merge(conexao);
                    
                    // Log de progresso a cada 50 registros
                    if (i % 50 == 0) {
                        callback.log(String.format("Processando conexões... %d/%d (%.1f%%)", 
                            i, totalLinhas, (i * 100.0 / totalLinhas)));
                    }
                    
                    // Flush a cada 10 registros para melhor performance
                    if (i % 10 == 0) {
                        entityManager.flush();
                    }
                }
            }
            entityManager.flush();
            callback.log(String.format("✓ Conexões importadas: %d novas, %d atualizadas (Total: %d)", 
                novos, atualizados, novos + atualizados));
        } catch (IOException | CsvException e) {
            callback.log("✗ Erro ao carregar conexões: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar conexões do CSV: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void carregarTodos(String caminhoBairros, String caminhoConexoes, LogCallback callback) {
        if (callback == null) {
            callback = msg -> {}; // Callback vazio se não fornecido
        }
        
        callback.log("═══════════════════════════════════════");
        callback.log("INICIANDO IMPORTAÇÃO COMPLETA");
        callback.log("═══════════════════════════════════════");
        callback.log("");
        
        // Primeiro carrega os bairros
        callback.log(">>> ETAPA 1: Importando Bairros <<<");
        carregarBairros(caminhoBairros, callback);
        callback.log("");
        
        // Depois carrega as conexões (que dependem dos bairros)
        callback.log(">>> ETAPA 2: Importando Conexões <<<");
        carregarConexoes(caminhoConexoes, callback);
        callback.log("");
        
        callback.log("═══════════════════════════════════════");
        callback.log("✓ IMPORTAÇÃO CONCLUÍDA COM SUCESSO!");
        callback.log("═══════════════════════════════════════");
    }
}

