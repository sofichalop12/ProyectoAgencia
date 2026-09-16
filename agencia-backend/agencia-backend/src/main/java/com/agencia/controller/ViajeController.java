package com.agencia.controller;

import com.agencia.model.Viaje;
import com.agencia.service.AgenciaService;
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
}