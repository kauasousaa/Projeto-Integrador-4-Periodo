package sosrota.application.dtos;

import java.util.List;

public class EquipeDTO {
    private Long id;
    private String descricao;
    private Long ambulanciaId;
    private List<ProfissionalDTO> profissionais;

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

    public Long getAmbulanciaId() {
        return ambulanciaId;
    }

    public void setAmbulanciaId(Long ambulanciaId) {
        this.ambulanciaId = ambulanciaId;
    }

    public List<ProfissionalDTO> getProfissionais() {
        return profissionais;
    }

    public void setProfissionais(List<ProfissionalDTO> profissionais) {
        this.profissionais = profissionais;
    }
}

