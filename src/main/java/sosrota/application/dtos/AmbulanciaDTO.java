package sosrota.application.dtos;

import sosrota.domain.models.StatusAmbulancia;
import sosrota.domain.models.TipoAmbulancia;

public class AmbulanciaDTO {
    private Long id;
    private String placa;
    private TipoAmbulancia tipo;
    private StatusAmbulancia status;
    private Long baseId;
    private String baseNome;

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

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public String getBaseNome() {
        return baseNome;
    }

    public void setBaseNome(String baseNome) {
        this.baseNome = baseNome;
    }
}

