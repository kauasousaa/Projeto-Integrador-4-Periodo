package sosrota.application.dtos;

import sosrota.domain.models.TipoAmbulancia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AmbulanciaRequestDTO {
    @NotBlank
    private String placa;

    @NotNull
    private TipoAmbulancia tipo;

    @NotNull
    private Long baseId;

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

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }
}

