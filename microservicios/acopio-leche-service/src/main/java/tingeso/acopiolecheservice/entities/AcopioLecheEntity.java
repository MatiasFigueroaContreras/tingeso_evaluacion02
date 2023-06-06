package tingeso.acopiolecheservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "acopio_leche")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AcopioLecheEntity {
    @Id
    private String id;
    private String turno;
    private Integer cantidadLeche;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date fecha;
    private String codigoProveedor;
    private String quincena; // Probableme cambie a un objeto
}
