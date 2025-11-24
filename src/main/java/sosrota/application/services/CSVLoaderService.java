package sosrota.application.services;

public interface CSVLoaderService {
    void carregarBairros(String caminhoArquivo);
    void carregarConexoes(String caminhoArquivo);
    void carregarTodos(String caminhoBairros, String caminhoConexoes);
}

