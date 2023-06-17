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
       return toStringCustom("/");
    }

    public String toStringCustom(String sep) {
        String mesFormateado = mes.toString();
        if(mesFormateado.length() == 1){
            mesFormateado = "0" + mesFormateado;
        }
        return year.toString() + sep + mesFormateado + sep + numero.toString();
    }

    public static Quincena stringToQuincena(String quincena){
        String[] quincenaString = quincena.split("/");
        Integer year = Integer.parseInt(quincenaString[0]);
        Integer mes = Integer.parseInt(quincenaString[1]);
        Integer numero = Integer.parseInt(quincenaString[2]);
        return new Quincena(year, mes, numero);
    }

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("year", year.toString());
        map.put("mes", mes.toString());
        map.put("quincena", numero.toString());
        return map;
    }
}
