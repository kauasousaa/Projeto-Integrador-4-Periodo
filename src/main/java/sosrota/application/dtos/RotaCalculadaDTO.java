package sosrota.application.dtos;

import java.util.List;

public class RotaCalculadaDTO {
    private Long origemId;
    private Long destinoId;
    private Double distanciaKm;
    private Integer tempoMinutos;
    private List<Long> caminho;

    public Long getOrigemId() {
        return origemId;
    }

    public void setOrigemId(Long origemId) {
        this.origemId = origemId;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public void setDestinoId(Long destinoId) {
        this.destinoId = destinoId;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Integer getTempoMinutos() {
        return tempoMinutos;
    }

    public void setTempoMinutos(Integer tempoMinutos) {
        this.tempoMinutos = tempoMinutos;
    }

    public List<Long> getCaminho() {
        return caminho;
    }

    public void setCaminho(List<Long> caminho) {
        this.caminho = caminho;
    }
}

