package sosrota.domain.models;

import java.time.LocalDateTime;

public class Atendimento {
    private Long id;
    private Ocorrencia ocorrencia;
    private Ambulancia ambulancia;
    private LocalDateTime dataHoraDespacho;
    private LocalDateTime dataHoraChegada;
    private Double distanciaKm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public Ambulancia getAmbulancia() {
        return ambulancia;
    }

    public void setAmbulancia(Ambulancia ambulancia) {
        this.ambulancia = ambulancia;
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
