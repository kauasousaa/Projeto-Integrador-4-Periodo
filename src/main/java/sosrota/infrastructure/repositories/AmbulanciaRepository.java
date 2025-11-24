package sosrota.infrastructure.repositories;

import sosrota.domain.models.StatusAmbulancia;
import sosrota.domain.models.TipoAmbulancia;
import sosrota.infrastructure.persistence.entity.AmbulanciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmbulanciaRepository extends JpaRepository<AmbulanciaEntity, Long> {
    Optional<AmbulanciaEntity> findByPlaca(String placa);
    List<AmbulanciaEntity> findByStatus(StatusAmbulancia status);
    List<AmbulanciaEntity> findByTipo(TipoAmbulancia tipo);
    List<AmbulanciaEntity> findByBaseId(Long baseId);
}

