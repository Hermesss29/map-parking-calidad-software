package com.map.parking_project.controllers;

import com.map.parking_project.models.VehicleEntry;
import com.map.parking_project.services.VehicleEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingresos")
@CrossOrigin(origins = "*")
public class VehicleEntryRestController {

    @Autowired
    private VehicleEntryService service;

    @PostMapping
    public VehicleEntry registartIngreso(@RequestBody VehicleEntry entry) {
        return service.save(entry);
    }

    @GetMapping
    public List<VehicleEntry> listarIngresos() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}