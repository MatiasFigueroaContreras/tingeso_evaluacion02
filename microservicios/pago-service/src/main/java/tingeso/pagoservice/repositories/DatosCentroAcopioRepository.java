package tingeso.pagoservice.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.pagoservice.entities.DatosCentroAcopioEntity;

import java.util.Optional;

@Repository
public interface DatosCentroAcopioRepository extends JpaRepository<DatosCentroAcopioEntity, String>{
    Optional<DatosCentroAcopioEntity> findByCodigoProveedorAndQuincena(String codigoProveedor, String quincena);
}
