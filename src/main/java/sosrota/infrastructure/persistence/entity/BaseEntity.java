package sosrota.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bases")
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "bairro_id", nullable = false)
    private BairroEntity bairro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public BairroEntity getBairro() {
        return bairro;
    }

    public void setBairro(BairroEntity bairro) {
        this.bairro = bairro;
    }
}

