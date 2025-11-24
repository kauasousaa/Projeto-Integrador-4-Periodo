package sosrota.domain.models;

public class Conexao {
    private Long id;
    private Bairro origem;
    private Bairro destino;
    private Double distanciaKm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bairro getOrigem() {
        return origem;
    }

    public void setOrigem(Bairro origem) {
        this.origem = origem;
    }

    public Bairro getDestino() {
        return destino;
    }

    public void setDestino(Bairro destino) {
        this.destino = destino;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }
}

