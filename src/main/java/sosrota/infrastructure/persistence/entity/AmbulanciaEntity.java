package sosrota.infrastructure.persistence.entity;

import sosrota.domain.models.StatusAmbulancia;
import sosrota.domain.models.TipoAmbulancia;
import jakarta.persistence.*;
@Entity
@Table(name = "ambulancias")
public class AmbulanciaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAmbulancia tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAmbulancia status;

    @ManyToOne
    @JoinColumn(name = "base_id", nullable = false)
    private BaseEntity base;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public StatusAmbulancia getStatus() {
        return status;
    }

    public void setStatus(StatusAmbulancia status) {
        this.status = status;
    }

    public BaseEntity getBase() {
        return base;
    }

    public void setBase(BaseEntity base) {
        this.base = base;
    }
}

