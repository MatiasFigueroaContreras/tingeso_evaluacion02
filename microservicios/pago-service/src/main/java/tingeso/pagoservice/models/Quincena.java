package tingeso.pagoservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public static Quincena stringToQuincena(String quincena){
        String[] quincenaString = quincena.split("/");
        Integer year = Integer.parseInt(quincenaString[0]);
        Integer mes = Integer.parseInt(quincenaString[1]);
        Integer numero = Integer.parseInt(quincenaString[2]);
        return new Quincena(year, mes, numero);
    }

    public Quincena obtenerQuincenaAnterior(){
        Quincena quincenaAnterior = new Quincena();
        if(numero == 2){
            quincenaAnterior.setNumero(1);
            quincenaAnterior.setMes(mes);
            quincenaAnterior.setYear(year);
        }
        else if(mes == 1){
            quincenaAnterior.setNumero(2);
            quincenaAnterior.setMes(12);
            quincenaAnterior.setYear(year - 1);
        }
        else {
            quincenaAnterior.setNumero(2);
            quincenaAnterior.setMes(mes - 1);
            quincenaAnterior.setYear(year);
        }
        return quincenaAnterior;
    }

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("year", year.toString());
        map.put("mes", mes.toString());
        map.put("quincena", numero.toString());
        return map;
    }
}
