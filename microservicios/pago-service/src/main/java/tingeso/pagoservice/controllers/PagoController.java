package tingeso.pagoservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tingeso.pagoservice.entities.DatosCentroAcopioEntity;
import tingeso.pagoservice.entities.PagoEntity;

import tingeso.pagoservice.models.Quincena;
import tingeso.pagoservice.services.DatosCentroAcopioService;
import tingeso.pagoservice.services.PagoService;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {
    @Autowired
    PagoService pagoService;
    @Autowired
    DatosCentroAcopioService datosCentroAcopioService;

    @PostMapping("/calcular")
    public ResponseEntity<String> calcularPlanillaPagos(@RequestParam("year") Integer year,
                                                        @RequestParam("mes") Integer mes,
                                                        @RequestParam("quincena") Integer numero) {
        Quincena quincena = new Quincena(year, mes, numero);
        if(pagoService.existenPagosPorQuincena(quincena.toString())){
            return ResponseEntity.ok("Ya existen pagos calculados");
        }
        else if(datosCentroAcopioService.existenDatosCAParaCalculoPorQuincena(quincena.toString())){
            try {
                List<DatosCentroAcopioEntity> listaDatosCa = datosCentroAcopioService.calcularDatosCAPorQuincena(quincena.toString());
                datosCentroAcopioService.guardarListaDatosCA(listaDatosCa);
                List<PagoEntity> pagos = pagoService.calcularPagos(listaDatosCa);
                pagoService.guardarPagos(pagos);
                return ResponseEntity.ok("");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        else{
            return ResponseEntity.badRequest().body("No se han ingresado los datos del centro de acopio para el calculo de los pagos");
        }
    }

    @GetMapping("/byquincena")
    public ResponseEntity<List<PagoEntity>> getAllByQuincena(@RequestParam("year") Integer year,
                                                             @RequestParam("mes") Integer mes,
                                                             @RequestParam("quincena") Integer numero) {
        Quincena quincena = new Quincena(year, mes, numero);
        List<PagoEntity> pagos = pagoService.obtenerPagosPorQuincena(quincena.toString());
        return ResponseEntity.ok(pagos);
    }


    @GetMapping
    public ResponseEntity<List<PagoEntity>> getAll(){
        List<PagoEntity> pagos = pagoService.obtenerPagos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/exists/byquincena")
    public ResponseEntity<Boolean> existsByQuincena(@RequestParam("year") Integer year,
                                                    @RequestParam("mes") Integer mes,
                                                    @RequestParam("quincena") Integer numero) {
        Quincena quincena = new Quincena(year, mes, numero);
        Boolean exists = pagoService.existenPagosPorQuincena(quincena.toString());
        return ResponseEntity.ok(exists);
    }
}
