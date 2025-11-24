package sosrota.application.dtos;

import java.time.LocalDateTime;

public class AtendimentoDTO {
    private Long id;
    private Long ocorrenciaId;
    private Long ambulanciaId;
    private LocalDateTime dataHoraDespacho;
    private LocalDateTime dataHoraChegada;
    private Double distanciaKm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOcorrenciaId() {
        return ocorrenciaId;
    }

    public void setOcorrenciaId(Long ocorrenciaId) {
        this.ocorrenciaId = ocorrenciaId;
    }

    public Long getAmbulanciaId() {
        return ambulanciaId;
    }

    public void setAmbulanciaId(Long ambulanciaId) {
        this.ambulanciaId = ambulanciaId;
    }

    public LocalDateTime getDataHoraDespacho() {
        return dataHoraDespacho;
    }

    public void setDataHoraDespacho(LocalDateTime dataHoraDespacho) {
        this.dataHoraDespacho = dataHoraDespacho;
    }

    public LocalDateTime getDataHoraChegada() {
        return dataHoraChegada;
    }

    public void setDataHoraChegada(LocalDateTime dataHoraChegada) {
        this.dataHoraChegada = dataHoraChegada;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }
}

