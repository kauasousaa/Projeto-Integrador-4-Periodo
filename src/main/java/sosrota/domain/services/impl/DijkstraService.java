package sosrota.domain.services.impl;

import sosrota.domain.models.Bairro;
import sosrota.domain.models.Conexao;

import java.util.*;

/**
 * Implementação do algoritmo de Dijkstra para cálculo de caminho mínimo
 * Usa estruturas de dados do java.util (List, Map, PriorityQueue)
 */
public class DijkstraService {

    /**
     * Calcula o caminho mínimo entre dois bairros usando o algoritmo de Dijkstra
     * 
     * @param origemId ID do bairro de origem
     * @param destinoId ID do bairro de destino
     * @param conexoes Lista de todas as conexões (arestas do grafo)
     * @return Resultado contendo distância e caminho, ou null se não houver caminho
     */
    public static ResultadoDijkstra calcularCaminhoMinimo(
            Long origemId, 
            Long destinoId, 
            List<Conexao> conexoes) {
        
        // Mapa para armazenar distâncias mínimas de cada vértice
        Map<Long, Double> distancias = new HashMap<>();
        
        // Mapa para armazenar o vértice anterior no caminho mínimo
        Map<Long, Long> anterior = new HashMap<>();
        
        // Conjunto de vértices visitados
        Set<Long> visitados = new HashSet<>();
        
        // Fila de prioridade para processar vértices (menor distância primeiro)
        PriorityQueue<VerticeDistancia> fila = new PriorityQueue<>(
            Comparator.comparingDouble(VerticeDistancia::getDistancia)
        );
        
        // Construir grafo como lista de adjacência
        Map<Long, List<Aresta>> grafo = construirGrafo(conexoes);
        
        // Inicializar distâncias: origem = 0, demais = infinito
        distancias.put(origemId, 0.0);
        fila.offer(new VerticeDistancia(origemId, 0.0));
        
        // Algoritmo de Dijkstra
        while (!fila.isEmpty()) {
            VerticeDistancia atual = fila.poll();
            Long verticeAtual = atual.getVerticeId();
            
            // Se já foi visitado, pular
            if (visitados.contains(verticeAtual)) {
                continue;
            }
            
            visitados.add(verticeAtual);
            
            // Se chegou ao destino, parar
            if (verticeAtual.equals(destinoId)) {
                break;
            }
            
            // Processar vizinhos
            List<Aresta> vizinhos = grafo.getOrDefault(verticeAtual, new ArrayList<>());
            for (Aresta aresta : vizinhos) {
                Long vizinho = aresta.getDestino();
                double novaDistancia = distancias.get(verticeAtual) + aresta.getPeso();
                
                // Se encontrou caminho mais curto
                if (!distancias.containsKey(vizinho) || novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    anterior.put(vizinho, verticeAtual);
                    fila.offer(new VerticeDistancia(vizinho, novaDistancia));
                }
            }
        }
        
        // Se não encontrou caminho até o destino
        if (!distancias.containsKey(destinoId)) {
            return null;
        }
        
        // Reconstruir caminho
        List<Long> caminho = reconstruirCaminho(origemId, destinoId, anterior);
        Double distancia = distancias.get(destinoId);
        
        return new ResultadoDijkstra(caminho, distancia);
    }
    
    /**
     * Constrói o grafo como lista de adjacência a partir das conexões
     */
    private static Map<Long, List<Aresta>> construirGrafo(List<Conexao> conexoes) {
        Map<Long, List<Aresta>> grafo = new HashMap<>();
        
        for (Conexao conexao : conexoes) {
            Long origem = conexao.getOrigem().getId();
            Long destino = conexao.getDestino().getId();
            Double peso = conexao.getDistanciaKm();
            
            // Adicionar aresta direcionada
            grafo.computeIfAbsent(origem, k -> new ArrayList<>())
                 .add(new Aresta(destino, peso));
            
            // Adicionar aresta reversa (grafo não direcionado)
            grafo.computeIfAbsent(destino, k -> new ArrayList<>())
                 .add(new Aresta(origem, peso));
        }
        
        return grafo;
    }
    
    /**
     * Reconstrói o caminho mínimo do destino até a origem
     */
    private static List<Long> reconstruirCaminho(Long origem, Long destino, Map<Long, Long> anterior) {
        List<Long> caminho = new ArrayList<>();
        Long atual = destino;
        
        while (atual != null) {
            caminho.add(0, atual); // Adicionar no início
            atual = anterior.get(atual);
        }
        
        return caminho;
    }
    
    /**
     * Classe auxiliar para representar um vértice com sua distância
     */
    private static class VerticeDistancia {
        private Long verticeId;
        private Double distancia;
        
        public VerticeDistancia(Long verticeId, Double distancia) {
            this.verticeId = verticeId;
            this.distancia = distancia;
        }
        
        public Long getVerticeId() {
            return verticeId;
        }
        
        public Double getDistancia() {
            return distancia;
        }
    }
    
    /**
     * Classe auxiliar para representar uma aresta do grafo
     */
    private static class Aresta {
        private Long destino;
        private Double peso;
        
        public Aresta(Long destino, Double peso) {
            this.destino = destino;
            this.peso = peso;
        }
        
        public Long getDestino() {
            return destino;
        }
        
        public Double getPeso() {
            return peso;
        }
    }
    
    /**
     * Classe para armazenar o resultado do algoritmo de Dijkstra
     */
    public static class ResultadoDijkstra {
        private List<Long> caminho;
        private Double distanciaKm;
        
        public ResultadoDijkstra(List<Long> caminho, Double distanciaKm) {
            this.caminho = caminho;
            this.distanciaKm = distanciaKm;
        }
        
        public List<Long> getCaminho() {
            return caminho;
        }
        
        public Double getDistanciaKm() {
            return distanciaKm;
        }
    }
}



