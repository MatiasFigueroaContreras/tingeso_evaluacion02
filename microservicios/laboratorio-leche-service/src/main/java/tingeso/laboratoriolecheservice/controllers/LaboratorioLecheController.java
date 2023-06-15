package tingeso.laboratoriolecheservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tingeso.laboratoriolecheservice.entities.LaboratorioLecheEntity;
import tingeso.laboratoriolecheservice.models.Quincena;
import tingeso.laboratoriolecheservice.services.LaboratorioLecheService;

import java.util.List;

@RestController
@RequestMapping("/laboratorio-leche")
public class LaboratorioLecheController {
    @Autowired
    LaboratorioLecheService laboratorioLecheService;
    //@Autowired
    //PagoService pagoService;
    @GetMapping("/{id}")
    public ResponseEntity<LaboratorioLecheEntity> get(@PathVariable("id") String id) {
        try {
            LaboratorioLecheEntity laboratorioLeche = laboratorioLecheService.getById(id);
            return ResponseEntity.ok(laboratorioLeche);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody LaboratorioLecheEntity laboratorioLeche) {
        try{
            laboratorioLecheService.validarDatosLaboratorioLeche(laboratorioLeche);
            laboratorioLecheService.guardarDatosLaboratorioLeche(laboratorioLeche);
            return ResponseEntity.ok("Datos registrados correctamente!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/importar")
    public ResponseEntity<String> importarLaboratorioLeche(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("year") Integer year,
                                                           @RequestParam("mes") Integer mes,
                                                           @RequestParam("quincena") Integer numero) {
        Quincena quincena = new Quincena(year, mes, numero);

        if(laboratorioLecheService.existenPagosPorQuincena(quincena)){
            return ResponseEntity.badRequest().body("Ya existen datos calculados para la quincena seleccionada");
        }

        try {
            List<LaboratorioLecheEntity> laboratorioLecheList = laboratorioLecheService.leerExcel(file);
            laboratorioLecheService.validarListaDatosLaboratorioLeche(laboratorioLecheList);
            laboratorioLecheService.guardarListaDatosLaboratorioLeche(laboratorioLecheList, quincena.toString());
            return ResponseEntity.ok("Datos registrados correctamente!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // Ver si se arregla la ruta
    @GetMapping("/byproveedor-quincena")
    public ResponseEntity<LaboratorioLecheEntity> getByProveedorQuincena(@RequestParam("codigoProveedor") String codigoProveedor,
                                                                         @RequestParam("year") Integer year,
                                                                         @RequestParam("mes") Integer mes,
                                                                         @RequestParam("quincena") Integer numero) {
        Quincena quincena = new Quincena(year, mes, numero);
        LaboratorioLecheEntity laboratorioLeche = laboratorioLecheService.obtenerLaboratorioLechePorProveedorQuincena(codigoProveedor, quincena.toString());
        return ResponseEntity.ok(laboratorioLeche);
    }

    @GetMapping("/exists/byquincena")
    public ResponseEntity<Boolean> existsByQuincena(@RequestParam("year") Integer year,
                                                    @RequestParam("mes") Integer mes,
                                                    @RequestParam("quincena") Integer numero){
        Quincena quincena = new Quincena(year, mes, numero);
        Boolean exists = laboratorioLecheService.existeLaboratorioLechePorQuincena(quincena.toString());
        return ResponseEntity.ok(exists);
    }
}
