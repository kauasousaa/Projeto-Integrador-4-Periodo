package sosrota.application.dtos;

import java.util.List;

public class DespachoSugestaoDTO {
    private Long ocorrenciaId;
    private List<AmbulanciaSugeridaDTO> ambulanciasSugeridas;
    private Double distanciaKm;
    private Integer tempoEstimadoMinutos;
    private Boolean slaCumprido;

    public Long getOcorrenciaId() {
        return ocorrenciaId;
    }

    public void setOcorrenciaId(Long ocorrenciaId) {
        this.ocorrenciaId = ocorrenciaId;
    }

    public List<AmbulanciaSugeridaDTO> getAmbulanciasSugeridas() {
        return ambulanciasSugeridas;
    }

    public void setAmbulanciasSugeridas(List<AmbulanciaSugeridaDTO> ambulanciasSugeridas) {
        this.ambulanciasSugeridas = ambulanciasSugeridas;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Integer getTempoEstimadoMinutos() {
        return tempoEstimadoMinutos;
    }

    public void setTempoEstimadoMinutos(Integer tempoEstimadoMinutos) {
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
    }

    public Boolean getSlaCumprido() {
        return slaCumprido;
    }

    public void setSlaCumprido(Boolean slaCumprido) {
        this.slaCumprido = slaCumprido;
    }
}

