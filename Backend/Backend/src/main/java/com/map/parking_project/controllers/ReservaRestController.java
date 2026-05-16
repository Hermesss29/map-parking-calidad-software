package com.map.parking_project.controllers;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.map.parking_project.models.Reservas;
import com.map.parking_project.services.IReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ReservaRestController {

    @Autowired
    private IReservaService reservaService;

    public ReservaRestController() {
    }

    @GetMapping("/reservas")
    public List<Reservas> listarReservas() {
        return reservaService.findAll();
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<?> obtenerReserva(@PathVariable Long id) {
        Optional<Reservas> reserva = reservaService.findById(id);
        if (reserva.isPresent()) {
            return ResponseEntity.ok(reserva.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
        }
    }

    @PostMapping("/reservas")
    public ResponseEntity<?> registrarReserva(@RequestBody Reservas reserva) {
        try {
            reserva.setId(null);
            Reservas nuevaReserva = reservaService.save(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Reserva registrada correctamente",
                    "reserva", nuevaReserva
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Error al registrar la reserva",
                    "error", e.getMessage()
            ));
        }
    }

    @PutMapping("/reservas/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> actualizarReserva(@RequestBody Reservas reserva, @PathVariable Long id) {
        Optional<Reservas> reservaActual = reservaService.findById(id);
        if (reservaActual.isPresent()) {
            reservaService.update(reserva, id);
            return ResponseEntity.ok(Map.of(
                    "message", "Reserva actualizada correctamente",
                    "reserva", reserva
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Reserva no encontrada"
            ));
        }
    }
    @PutMapping("/reservas/{id}/confirmar")
    public ResponseEntity<?> confirmarReserva(@PathVariable Long id) {
        Optional<Reservas> reserva = reservaService.findById(id);
        if (reserva.isPresent()) {
            Reservas reservaActual = reserva.get();
            reservaActual.setConfirmada(true);
            reservaService.save(reservaActual);
            return ResponseEntity.ok("Reserva confirmada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
        }
    }

    @DeleteMapping("/reservas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> eliminarReserva(@PathVariable Long id) {
        Optional<Reservas> reserva = reservaService.findById(id);
        if (reserva.isPresent()) {
            reservaService.delete(id);
            return ResponseEntity.ok("Reserva eliminada correctamente");
        } else {
            return ResponseEntity.ok().body(Map.of("message", "Reserva eliminada correctamente"));
        }
    }



}