package tingeso.laboratoriolecheservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "laboratorio_leche")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LaboratorioLecheEntity {
    @Id
    private String id;
    private Integer porcentajeGrasa;
    private Integer porcentajeSolidoTotal;
    private String codigoProveedor;
    private String quincena;
}
