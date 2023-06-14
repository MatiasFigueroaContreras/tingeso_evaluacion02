package tingeso.acopiolecheservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tingeso.acopiolecheservice.entities.AcopioLecheEntity;
import tingeso.acopiolecheservice.models.Quincena;
import tingeso.acopiolecheservice.services.AcopioLecheService;

import java.util.List;

@RestController
@RequestMapping("/acopio-leches")
public class AcopioLecheController {
    @Autowired
    AcopioLecheService acopioLecheService;

    @PostMapping("/importar")
    public ResponseEntity<String> importarAcopioLeche(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("year") Integer year,
                                                     @RequestParam("mes") Integer mes,
                                                     @RequestParam("quincena") Integer numero) {
        /*
        QuincenaEntity quincena = quincenaService.ingresarQuincena(year, mes, numero);
        if(pagoService.existenPagosPorQuincena(quincena)){
            redirectAttr.addFlashAttribute("message", "Ya existen datos calculados para la quincena seleccionada")
                    .addFlashAttribute("class", "error-alert");
        }
        else{
         */
        Quincena quincena = new Quincena(year, mes, numero);
        try {
            List<AcopioLecheEntity> acopiosLeche = acopioLecheService.leerExcel(file);
            acopioLecheService.validarListaAcopioLecheQuincena(acopiosLeche, quincena.toString());
            acopioLecheService.guardarAcopiosLeches(acopiosLeche);
            return ResponseEntity.ok("Datos registrados correctamente!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/byproveedor-quincena")
    public ResponseEntity<List<AcopioLecheEntity>> getAllByProveedorQuincena(@RequestParam("codigoProveedor") String codigoProveedor,
                                                                             @RequestParam("quincena") String quincena) {
        List<AcopioLecheEntity> acopiosLeche = acopioLecheService.obtenerAcopiosLechePorProveedorQuincena(codigoProveedor, quincena);
        return ResponseEntity.ok(acopiosLeche);
    }

    @GetMapping("/exists/byquincena/{quincena}")
    public ResponseEntity<Boolean> existsByQuincena(@PathVariable("quincena") String quincena){
        Boolean exists = acopioLecheService.existenAcopiosLechePorQuincena(quincena);
        return ResponseEntity.ok(exists);
    }
}
