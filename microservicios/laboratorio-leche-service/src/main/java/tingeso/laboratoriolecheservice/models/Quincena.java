package tingeso.laboratoriolecheservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@Data
public class Quincena {
    private Integer year;
    private Integer mes;
    private Integer numero;

    public Quincena(Integer year, Integer mes, Integer numero){
        this.year = year;
        this.mes = mes;
        this.numero = numero;
    }

    public String toString(){
        String mesFormateado = mes.toString();
        if(mesFormateado.length() == 1){
            mesFormateado = "0" + mesFormateado;
        }
        return year.toString() + "/" + mesFormateado + "/" + numero.toString();
    }

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("year", year.toString());
        map.put("mes", mes.toString());
        map.put("quincena", numero.toString());
        return map;
    }
}
