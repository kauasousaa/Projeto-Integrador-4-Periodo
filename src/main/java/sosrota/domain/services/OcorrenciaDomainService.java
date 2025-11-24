package sosrota.domain.services;

import sosrota.domain.models.Ocorrencia;

public interface OcorrenciaDomainService {
    void validarRegrasNegocio(Ocorrencia ocorrencia);
    boolean podeSerDespachada(Ocorrencia ocorrencia);
}

