package tingeso.proveedorservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tingeso.proveedorservice.entities.ProveedorEntity;
import tingeso.proveedorservice.services.ProveedorService;

import java.util.List;

@RestController
@RequestMapping
public class ProveedorController {
    @Autowired
    ProveedorService proveedorService;

    @PostMapping
    public ResponseEntity create(@RequestParam("codigo") String codigo,
                                 @RequestParam("nombre") String nombre,
                                 @RequestParam("categoria") String categoria,
                                 @RequestParam("retencion") String retencion) {
        try {
            ProveedorEntity proveedor = proveedorService.registrarProveedor(codigo, nombre, categoria, retencion);
            return ResponseEntity.ok(proveedor);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProveedorEntity>> getAll() {
        List<ProveedorEntity> proveedores = proveedorService.obtenerProveedores();
        if(proveedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorEntity> getById(@PathVariable("id") String codigo){
        ProveedorEntity proveedor = proveedorService.obtenerProveedor(codigo);
        if(proveedor == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(proveedor);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable("id") String codigo){
        Boolean exists = proveedorService.existeProveedor(codigo);
        return ResponseEntity.ok(exists);
    }
}
