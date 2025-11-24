package sosrota.domain.services;

import sosrota.domain.models.Ambulancia;

public interface AmbulanciaDomainService {
    void validarRegrasNegocio(Ambulancia ambulancia);
    boolean estaDisponivel(Ambulancia ambulancia);
    boolean temEquipeCompleta(Ambulancia ambulancia);
}

