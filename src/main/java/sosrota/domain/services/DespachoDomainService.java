package sosrota.domain.services;

import sosrota.domain.models.Ambulancia;
import sosrota.domain.models.Ocorrencia;

public interface DespachoDomainService {
    boolean podeDespachar(Ambulancia ambulancia, Ocorrencia ocorrencia);
    void validarSLA(Ocorrencia ocorrencia, Double distanciaKm);
    void validarCompatibilidadeTipo(Ambulancia ambulancia, Ocorrencia ocorrencia);
}

