package sosrota.infrastructure.repositories;

import sosrota.infrastructure.persistence.entity.BairroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BairroRepository extends JpaRepository<BairroEntity, Long> {
    Optional<BairroEntity> findByNome(String nome);
}

