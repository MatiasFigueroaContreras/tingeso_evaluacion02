package tingeso.acopiolecheservice.models;

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

    public static Quincena stringToQuincena(String quincena){
        String[] quincenaString = quincena.split("/");
        Integer year = Integer.parseInt(quincenaString[0]);
        Integer mes = Integer.parseInt(quincenaString[1]);
        Integer numero = Integer.parseInt(quincenaString[2]);
        return new Quincena(year, mes, numero);
    }

    public boolean estaDentroQuincena(Date fecha){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(fecha);
        int mesFecha = calendar.get(Calendar.MONTH) + 1;
        int yearFecha = calendar.get(Calendar.YEAR);
        int diaFecha = calendar.get(Calendar.DAY_OF_MONTH);
        if(numero == 1 && diaFecha > 15){
            return false;
        } else if (numero == 2 && diaFecha < 15) {
            return false;
        }
        return yearFecha == year && mesFecha == mes;
    }

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("year", year.toString());
        map.put("mes", mes.toString());
        map.put("quincena", numero.toString());
        return map;
    }
}