package com.agencia.controller;

import com.agencia.model.ResponsableABordo;
import com.agencia.service.AgenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responsables")
public class ResponsableController {

    private final AgenciaService agenciaService;

    public ResponsableController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    @GetMapping
    public ResponseEntity<List<ResponsableABordo>> obtenerTodos() {
        return ResponseEntity.ok(agenciaService.obtenerTodosLosResponsables());
    }

    @PostMapping
    public ResponseEntity<ResponsableABordo> crearResponsable(@RequestBody ResponsableABordo responsable) {
        ResponsableABordo nuevoResponsable = agenciaService.guardarResponsable(responsable);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoResponsable);
    }
}