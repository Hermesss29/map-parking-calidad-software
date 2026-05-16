package com.map.parking_project.controllers;

import com.map.parking_project.models.Tarifa;
import com.map.parking_project.services.ITarifaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
@CrossOrigin(origins = {"http://localhost:4200"})
public class TarifaRestController {

    @Autowired
    private ITarifaServices tarifaService;

    // 🔹 Obtener todas las tarifas
    @GetMapping
    public ResponseEntity<List<Tarifa>> getAllTarifas() {
        return ResponseEntity.ok(tarifaService.findAll());
    }

    // 🔹 Obtener una tarifa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> getTarifaById(@PathVariable Long id) {
        Tarifa tarifa = tarifaService.findById(id);
        return tarifa != null ? ResponseEntity.ok(tarifa) : ResponseEntity.notFound().build();
    }

    // 🔹 Crear nueva tarifa
    @PostMapping
    public ResponseEntity<?> crearTarifa(@RequestBody Tarifa tarifa) {
        try {
            // Validar los datos
            if (tarifa.getTipoVehiculo() == null || tarifa.getTarifaDiurna() == null || tarifa.getTarifaNocturna() == null) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            // Guardar la tarifa en la base de datos
            Tarifa nuevaTarifa = tarifaService.save(tarifa);
            return ResponseEntity.ok(nuevaTarifa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la tarifa: " + e.getMessage());
        }
    }

    // 🔹 Actualizar tarifa existente
    @PutMapping("/{id}")
    public ResponseEntity<Tarifa> updateTarifa(@PathVariable Long id, @RequestBody Tarifa tarifaDetails) {
        Tarifa tarifa = tarifaService.findById(id);
        if (tarifa == null) {
            return ResponseEntity.notFound().build();
        }

        tarifa.setTipoVehiculo(tarifaDetails.getTipoVehiculo());
        tarifa.setTarifaDiurna(tarifaDetails.getTarifaDiurna());
        tarifa.setTarifaNocturna(tarifaDetails.getTarifaNocturna());
        tarifa.setImagen(tarifaDetails.getImagen()); // 🖼️ Añadido: Actualizar imagen

        return ResponseEntity.ok(tarifaService.save(tarifa));
    }

    // 🔹 Eliminar tarifa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarifa(@PathVariable Long id) {
        tarifaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

