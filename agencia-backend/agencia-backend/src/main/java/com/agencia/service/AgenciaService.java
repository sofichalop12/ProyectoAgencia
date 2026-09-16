package com.agencia.service;

import com.agencia.exceptions.DestinoYaExisteException;
import com.agencia.exceptions.ValidacionException;
import com.agencia.model.Destino;
import com.agencia.model.ResponsableABordo;
import com.agencia.model.Transporte;
import com.agencia.model.Viaje;
import com.agencia.repository.DestinoRepository;
import com.agencia.repository.ResponsableABordoRepository;
import com.agencia.repository.TransporteRepository;
import com.agencia.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgenciaService {

    private final DestinoRepository destinoRepository;
    private final ResponsableABordoRepository responsableRepository;
    private final TransporteRepository transporteRepository;
    private final ViajeRepository viajeRepository;

    @Autowired
    public AgenciaService(DestinoRepository destinoRepository,
                          ResponsableABordoRepository responsableRepository,
                          TransporteRepository transporteRepository,
                          ViajeRepository viajeRepository) {
        this.destinoRepository = destinoRepository;
        this.responsableRepository = responsableRepository;
        this.transporteRepository = transporteRepository;
        this.viajeRepository = viajeRepository;
    }

    // --- GESTIÓN DE DESTINOS ---
    public List<Destino> obtenerTodosLosDestinos() {
        return destinoRepository.findAll();
    }

    public Destino guardarDestino(Destino destino) throws DestinoYaExisteException {
        if (destinoRepository.existsByNombre(destino.getNombre())) {
            throw new DestinoYaExisteException("El destino '" + destino.getNombre() + "' ya se encuentra registrado.");
        }
        return destinoRepository.save(destino);
    }

    // --- GESTIÓN DE RESPONSABLES ---
    public List<ResponsableABordo> obtenerTodosLosResponsables() {
        return responsableRepository.findAll();
    }

    public ResponsableABordo guardarResponsable(ResponsableABordo responsable) {
        return responsableRepository.save(responsable);
    }

    // --- GESTIÓN DE TRANSPORTES ---
    public List<Transporte> obtenerTodosLosTransportes() {
        return transporteRepository.findAll();
    }

    public Transporte guardarTransporte(Transporte transporte) {
        return transporteRepository.save(transporte);
    }

    // --- GESTIÓN DE VIAJES ---
    public List<Viaje> obtenerTodosLosViajes() {
        return viajeRepository.findAll();
    }

    public Viaje guardarViaje(Viaje viaje) throws ValidacionException {
        if (viaje.getTransporteAsignado() != null) {
            viaje.getTransporteAsignado().agregarViaje(viaje);
        }
        return viajeRepository.save(viaje);
    }
}