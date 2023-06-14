package tingeso.pagoservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AcopioLeche {
    private String id;
    private String turno;
    private Integer cantidadLeche;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date fecha;
    private String codigoProveedor;
    private String quincena;
}
