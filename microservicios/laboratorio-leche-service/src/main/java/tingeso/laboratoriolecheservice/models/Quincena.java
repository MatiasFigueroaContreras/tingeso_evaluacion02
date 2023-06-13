package tingeso.laboratoriolecheservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public Quincena stringToQuincena(String quincena){
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
}
