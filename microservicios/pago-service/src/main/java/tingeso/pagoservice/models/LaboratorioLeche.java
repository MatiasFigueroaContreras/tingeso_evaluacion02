package tingeso.pagoservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LaboratorioLeche {
    private String id;
    private Integer porcentajeGrasa;
    private Integer porcentajeSolidoTotal;
    private String codigoProveedor;
    private String quincena;
}
