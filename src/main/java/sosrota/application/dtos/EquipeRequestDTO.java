package sosrota.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class EquipeRequestDTO {
    @NotBlank
    private String descricao;

    @NotNull
    private Long ambulanciaId;

    private List<Long> profissionalIds;

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

    public List<Long> getProfissionalIds() {
        return profissionalIds;
    }

    public void setProfissionalIds(List<Long> profissionalIds) {
        this.profissionalIds = profissionalIds;
    }
}

