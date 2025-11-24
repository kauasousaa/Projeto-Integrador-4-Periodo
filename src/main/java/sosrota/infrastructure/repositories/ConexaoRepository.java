package sosrota.infrastructure.repositories;

import sosrota.infrastructure.persistence.entity.ConexaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConexaoRepository extends JpaRepository<ConexaoEntity, Long> {
    List<ConexaoEntity> findByOrigemId(Long origemId);
    List<ConexaoEntity> findByDestinoId(Long destinoId);
}

