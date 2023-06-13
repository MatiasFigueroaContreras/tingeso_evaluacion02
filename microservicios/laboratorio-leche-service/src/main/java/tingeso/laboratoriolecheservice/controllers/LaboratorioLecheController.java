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

    @PostMapping("/importar")
    public ResponseEntity<String> importarAcopioLeche(@RequestParam("file") MultipartFile file,
                                              @RequestParam("year") Integer year,
                                              @RequestParam("mes") Integer mes,
                                              @RequestParam("quincena") Integer numero) {
        Quincena quincena = new Quincena(year, mes, numero);
        /*
        if(pagoService.existenPagosPorQuincena(quincena)){
            redirectAttr.addFlashAttribute("message", "Ya existen datos calculados para la quincena seleccionada")
                    .addFlashAttribute("class", "error-alert");
        }
                else{
         */


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
    public ResponseEntity<LaboratorioLecheEntity> getAllByProveedorQuincena(@RequestParam("codigoProveedor") String codigoProveedor,
                                                                            @RequestParam("quincena") String quincena) {
        LaboratorioLecheEntity laboratorioLeche = laboratorioLecheService.obtenerLaboratorioLechePorProveedorQuincena(codigoProveedor, quincena);
        return ResponseEntity.ok(laboratorioLeche);
    }

    @GetMapping("/exists/byquincena/{quincena}")
    public ResponseEntity<Boolean> existsByQuincena(@PathVariable("quincena") String quincena){
        Boolean exists = laboratorioLecheService.existeLaboratorioLechePorQuincena(quincena);
        return ResponseEntity.ok(exists);
    }
}
