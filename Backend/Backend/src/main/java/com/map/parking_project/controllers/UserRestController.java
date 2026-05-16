package com.map.parking_project.controllers;

import com.map.parking_project.models.User;
import com.map.parking_project.services.IUserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private IUserService userService; // Servicio para manejar operaciones relacionadas con usuarios

    @GetMapping("/user")
    public List<User> index() {
        return userService.findAll(); // Devuelve la lista de todos los usuarios
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> show(@PathVariable Long id) {
        User user = userService.findById(id); // Busca un usuario por su ID
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build(); // Retorna el usuario si existe, de lo contrario, retorna 404
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userService.saveUser(user); // Guarda un nuevo usuario y devuelve el usuario creado
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable Long id) {
        User currentUser = userService.findById(id); // Busca el usuario actual por su ID

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna 404 si el usuario no existe
        }


        // Actualiza los datos del usuario con la nueva información proporcionada
        currentUser.setName(user.getName());
        currentUser.setLastname(user.getLastname());
        currentUser.setPhone(user.getPhone());
        currentUser.setPlate(user.getPlate());
        currentUser.setTypecar(user.getTypecar());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());
        currentUser.setRol(user.getRol());


        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(currentUser)); // Guarda los cambios y retorna el usuario actualizado
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id); // Elimina el usuario con el ID proporcionado
    }

    @PostMapping("/recuperarcontrasenia")
    public ResponseEntity<?> recuperarContraseña(@RequestParam String email) {
        User user = userService.findByEmail(email); // Busca un usuario por su correo electrónico

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Correo no registrado"); // Retorna error 404 si el usuario no existe
        }

        String nuevaContrasenia = userService.generarContraseniaAleatoria(); // Genera una nueva contraseña aleatoria

        user.setPassword(userService.ContraseniaSha256(nuevaContrasenia));
        // Asigna la nueva contraseña al usuario

        userService.save(user);
        // Guarda el usuario con la nueva contraseña

        String asunto = "Recuperación de contraseña";
        String cuerpo = "Tu nueva contraseña es: " + nuevaContrasenia;
        try {
            userService.sendEmail(email, asunto, cuerpo); // Envía la nueva contraseña por correo
            return ResponseEntity.ok("Se ha enviado un correo con la nueva contraseña.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo."); // Retorna error si falla el envío del correo
        }
    }

    @GetMapping("/send")
    public String sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {
        try {
            userService.sendEmail(to, subject, body);
            // Envía un correo con los datos proporcionados
            return "Correo enviado correctamente a " + to;
        } catch (MessagingException e) {
            return "Error enviando el correo: " + e.getMessage();
            // Retorna un mensaje de error si el envío falla
        }
    }


    @PostMapping("/validar-tarifa")
    public ResponseEntity<?> validarYCalcularTarifa(@RequestBody User request) {
        try {
            // Buscar el usuario por la placa
            User user = userService.findByPlate(request.getPlate());

            // Validar que el tipo de vehículo coincida
            if (!user.getTypecar().equals(request.getTypecar())) {
                throw new RuntimeException("El tipo de vehículo no coincide con el registrado.");
            }

            // Lógica de tarifas según el tipo de vehículo
            double tarifaBase;
            switch (user.getTypecar()) {
                case "Automóvil":
                    tarifaBase = 3.5;
                    break;
                case "Camioneta":
                    tarifaBase = 5.0;
                    break;
                case "Moto":
                    tarifaBase = 2.5;
                    break;
                default:
                    throw new RuntimeException("Tipo de vehículo no válido.");
            }

            // Calcular el costo total
            double total = tarifaBase * request.getHours();
            return ResponseEntity.ok(total);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}


