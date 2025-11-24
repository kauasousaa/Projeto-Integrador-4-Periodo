package sosrota.infrastructure.repositories;

import sosrota.infrastructure.persistence.entity.EquipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipeRepository extends JpaRepository<EquipeEntity, Long> {
    Optional<EquipeEntity> findByAmbulanciaId(Long ambulanciaId);
    List<EquipeEntity> findAllByAmbulanciaId(Long ambulanciaId);
}

