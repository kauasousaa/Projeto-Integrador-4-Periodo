package sosrota.infrastructure.persistence.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "equipes")
public class EquipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "ambulancia_id", nullable = false)
    private AmbulanciaEntity ambulancia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public AmbulanciaEntity getAmbulancia() {
        return ambulancia;
    }

    public void setAmbulancia(AmbulanciaEntity ambulancia) {
        this.ambulancia = ambulancia;
    }
}

