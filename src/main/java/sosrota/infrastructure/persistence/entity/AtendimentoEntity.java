package sosrota.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "atendimentos")
public class AtendimentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ocorrencia_id", nullable = false)
    private OcorrenciaEntity ocorrencia;

    @ManyToOne
    @JoinColumn(name = "ambulancia_id", nullable = false)
    private AmbulanciaEntity ambulancia;

    @Column(nullable = false)
    private LocalDateTime dataHoraDespacho;

    @Column
    private LocalDateTime dataHoraChegada;

    @Column(nullable = false)
    private Double distanciaKm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OcorrenciaEntity getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(OcorrenciaEntity ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public AmbulanciaEntity getAmbulancia() {
        return ambulancia;
    }

    public void setAmbulancia(AmbulanciaEntity ambulancia) {
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

