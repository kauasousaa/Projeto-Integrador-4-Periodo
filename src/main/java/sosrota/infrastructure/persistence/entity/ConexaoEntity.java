package sosrota.infrastructure.persistence.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "conexoes")
public class ConexaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bairro_origem_id", nullable = false)
    private BairroEntity origem;

    @ManyToOne
    @JoinColumn(name = "bairro_destino_id", nullable = false)
    private BairroEntity destino;

    @Column(nullable = false)
    private Double distanciaKm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BairroEntity getOrigem() {
        return origem;
    }

    public void setOrigem(BairroEntity origem) {
        this.origem = origem;
    }

    public BairroEntity getDestino() {
        return destino;
    }

    public void setDestino(BairroEntity destino) {
        this.destino = destino;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }
}

