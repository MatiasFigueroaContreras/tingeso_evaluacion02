package tingeso.laboratoriolecheservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.laboratoriolecheservice.entities.LaboratorioLecheEntity;

import java.util.Optional;

@Repository
public interface LaboratorioLecheRepository extends JpaRepository<LaboratorioLecheEntity, String> {
    Optional<LaboratorioLecheEntity> findByCodigoProveedorAndQuincena(String codigoProveedor, String quincena);
    boolean existsByQuincena(String quincena);
}
