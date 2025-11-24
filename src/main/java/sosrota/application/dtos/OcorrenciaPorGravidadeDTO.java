package sosrota.application.dtos;

public class OcorrenciaPorGravidadeDTO {
    private String gravidade;
    private Long totalOcorrencias;

    public String getGravidade() {
        return gravidade;
    }

    public void setGravidade(String gravidade) {
        this.gravidade = gravidade;
    }

    public Long getTotalOcorrencias() {
        return totalOcorrencias;
    }

    public void setTotalOcorrencias(Long totalOcorrencias) {
        this.totalOcorrencias = totalOcorrencias;
    }
}



