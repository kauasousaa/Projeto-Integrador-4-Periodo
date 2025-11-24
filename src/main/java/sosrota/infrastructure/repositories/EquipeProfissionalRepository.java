package sosrota.infrastructure.repositories;

import sosrota.infrastructure.persistence.entity.EquipeProfissionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipeProfissionalRepository extends JpaRepository<EquipeProfissionalEntity, Long> {
    List<EquipeProfissionalEntity> findByEquipeId(Long equipeId);
    List<EquipeProfissionalEntity> findByProfissionalId(Long profissionalId);
}

