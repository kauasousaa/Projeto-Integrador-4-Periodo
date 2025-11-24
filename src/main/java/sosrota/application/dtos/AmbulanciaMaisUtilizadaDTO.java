package sosrota.application.dtos;

public class AmbulanciaMaisUtilizadaDTO {
    private Integer posicao;
    private Long ambulanciaId;
    private String placa;
    private Long totalAtendimentos;

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public Long getAmbulanciaId() {
        return ambulanciaId;
    }

    public void setAmbulanciaId(Long ambulanciaId) {
        this.ambulanciaId = ambulanciaId;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Long getTotalAtendimentos() {
        return totalAtendimentos;
    }

    public void setTotalAtendimentos(Long totalAtendimentos) {
        this.totalAtendimentos = totalAtendimentos;
    }
}

