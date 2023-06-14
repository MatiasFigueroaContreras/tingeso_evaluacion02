package tingeso.pagoservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Proveedor {
    private String codigo;
    private String nombre;
    private String categoria;
    private String retencion;

    public boolean estaAfectoARetencion(){
        return retencion.equals("Si");
    }
}
