package com.agencia.controller;

import com.agencia.model.Transporte;
import com.agencia.service.AgenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transportes")
public class TransporteController {

    private final AgenciaService agenciaService;

    public TransporteController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    @GetMapping
    public ResponseEntity<List<Transporte>> obtenerTodos() {
        return ResponseEntity.ok(agenciaService.obtenerTodosLosTransportes());
    }

    @PostMapping
    public ResponseEntity<Transporte> crearTransporte(@RequestBody Transporte transporte) {
        Transporte nuevoTransporte = agenciaService.guardarTransporte(transporte);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTransporte);
    }
}