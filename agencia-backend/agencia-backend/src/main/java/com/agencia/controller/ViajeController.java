package com.agencia.controller;

import com.agencia.model.Viaje;
import com.agencia.service.AgenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    private final AgenciaService agenciaService;

    public ViajeController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    @GetMapping
    public ResponseEntity<List<Viaje>> obtenerTodos() {
        return ResponseEntity.ok(agenciaService.obtenerTodosLosViajes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> obtenerPorId(@PathVariable Long id) {
        Viaje viaje = agenciaService.buscarViajePorId(id);
        if (viaje == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(viaje);
    }

    @PostMapping
    public ResponseEntity<Viaje> crearViaje(@RequestBody Viaje viaje) {
        Viaje nuevoViaje = agenciaService.guardarViaje(viaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoViaje);
    }

    @PutMapping("/{id}/avanzar")
    public ResponseEntity<Viaje> registrarAvance(@PathVariable Long id, @RequestParam float km) {
        Viaje viaje = agenciaService.buscarViajePorId(id);
        if (viaje == null) {
            return ResponseEntity.notFound().build();
        }
        viaje.AvanzarViaje(km);
        agenciaService.guardarViaje(viaje);
        return ResponseEntity.ok(viaje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarViaje(@PathVariable Long id) {
        Viaje viaje = agenciaService.buscarViajePorId(id);
        if (viaje == null) {
            return ResponseEntity.notFound().build();
        }
        agenciaService.eliminarViaje(id);
        return ResponseEntity.noContent().build();
    }
}