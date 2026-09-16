package com.agencia.repository;

import com.agencia.model.Viaje;
import com.agencia.model.Viaje.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    // Buscar viajes según su estado (PENDIENTE, EN_CURSO, FINALIZADO)
    List<Viaje> findByEstadoActual(Estado estadoActual);

    // Buscar viajes por el ID del destino
    List<Viaje> findByDestinoDelViajeId(Long idDestino);
}