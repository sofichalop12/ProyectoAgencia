package com.agencia.controller;

import com.agencia.exceptions.DestinoYaExisteException;
import com.agencia.model.Destino;
import com.agencia.service.AgenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinos")
public class DestinoController {

    private final AgenciaService agenciaService;

    public DestinoController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    @GetMapping
    public ResponseEntity<List<Destino>> obtenerTodos() {
        return ResponseEntity.ok(agenciaService.obtenerTodosLosDestinos());
    }

    @PostMapping
    public ResponseEntity<?> crearDestino(@RequestBody Destino destino) {
        try {
            Destino nuevoDestino = agenciaService.guardarDestino(destino);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDestino);
        } catch (DestinoYaExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}