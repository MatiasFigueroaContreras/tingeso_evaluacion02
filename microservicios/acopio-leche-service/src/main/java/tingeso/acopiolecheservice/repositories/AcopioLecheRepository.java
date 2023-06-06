package tingeso.acopiolecheservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.acopiolecheservice.entities.AcopioLecheEntity;

import java.util.List;

@Repository
public interface AcopioLecheRepository extends JpaRepository<AcopioLecheEntity, String> {
    List<AcopioLecheEntity> findAllByCodigoProveedorAndQuincena(String codigoProveedor, String quincena);
    boolean existsByQuincena(String quincena);

}
