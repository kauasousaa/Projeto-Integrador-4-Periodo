package sosrota.application.services;

import sosrota.application.dtos.DespachoSugestaoDTO;
import sosrota.application.dtos.RotaCalculadaDTO;

public interface DespachoService {
    DespachoSugestaoDTO sugerirAmbulancias(Long ocorrenciaId);
    RotaCalculadaDTO calcularRota(Long origemId, Long destinoId);
    void executarDespacho(Long ocorrenciaId, Long ambulanciaId);
}

