package sosrota.application.dtos;

import sosrota.domain.models.TipoAmbulancia;

public class AmbulanciaSugeridaDTO {
    private Long ambulanciaId;
    private String placa;
    private TipoAmbulancia tipo;
    private String baseNome;
    private Double distanciaKm;
    private Integer tempoEstimadoMinutos;
    private Boolean slaCumprido;

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

    public TipoAmbulancia getTipo() {
        return tipo;
    }

    public void setTipo(TipoAmbulancia tipo) {
        this.tipo = tipo;
    }

    public String getBaseNome() {
        return baseNome;
    }

    public void setBaseNome(String baseNome) {
        this.baseNome = baseNome;
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

