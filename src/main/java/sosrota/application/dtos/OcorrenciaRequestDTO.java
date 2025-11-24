package sosrota.application.dtos;

import sosrota.domain.models.Gravidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OcorrenciaRequestDTO {
    @NotBlank
    private String tipo;

    @NotNull
    private Gravidade gravidade;

    @NotBlank
    private String local;

    @NotNull
    private Long bairroId;

    private String observacao;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Gravidade getGravidade() {
        return gravidade;
    }

    public void setGravidade(Gravidade gravidade) {
        this.gravidade = gravidade;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Long getBairroId() {
        return bairroId;
    }

    public void setBairroId(Long bairroId) {
        this.bairroId = bairroId;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}

