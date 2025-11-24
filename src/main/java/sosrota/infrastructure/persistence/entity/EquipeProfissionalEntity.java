package sosrota.infrastructure.persistence.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "equipe_profissional")
public class EquipeProfissionalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipe_id", nullable = false)
    private EquipeEntity equipe;

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private ProfissionalEntity profissional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EquipeEntity getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeEntity equipe) {
        this.equipe = equipe;
    }

    public ProfissionalEntity getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalEntity profissional) {
        this.profissional = profissional;
    }
}

