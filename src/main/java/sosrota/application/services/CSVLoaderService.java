package sosrota.application.services;

public interface CSVLoaderService {
    void carregarBairros(String caminhoArquivo, LogCallback callback);
    void carregarConexoes(String caminhoArquivo, LogCallback callback);
    void carregarTodos(String caminhoBairros, String caminhoConexoes, LogCallback callback);
}

