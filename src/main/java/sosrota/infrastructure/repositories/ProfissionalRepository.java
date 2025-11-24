package sosrota.infrastructure.repositories;

import sosrota.infrastructure.persistence.entity.ProfissionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<ProfissionalEntity, Long> {
    List<ProfissionalEntity> findByAtivoTrue();
}

