package tingeso.pagoservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.pagoservice.entities.PagoEntity;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, String>{
    boolean existsByQuincena(String quincena);

    List<PagoEntity> findAllByQuincena(String quincena);
    List<PagoEntity> findAllByOrderByQuincenaDescProveedorCodigoAsc();
}
