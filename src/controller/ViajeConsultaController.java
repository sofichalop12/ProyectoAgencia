package controller;

import model.*;
import java.util.*;

public class ViajeConsultaController {

    public List<Viaje> listarViajesPorDestino(String nombreDestino) {
        Agencia agencia = Agencia.getInstance();
        Destino d = agencia.buscarDestinoPorNombre(nombreDestino);

        if (d == null) {
            return Collections.emptyList();
        }

        List<Viaje> resultado = new ArrayList<>();

        for (Transporte t : agencia.getTransportes()) {
            for (Viaje v : t.getListaViajes()) {
                if (v.getDestinoDelViaje().equals(d)) {
                    resultado.add(v);
                }
            }
        }

        // Sort: name ascending, then estado priority PENDIENTE -> EN_CURSO -> FINALIZADO
        resultado.sort(java.util.Comparator.comparing(Viaje::getNombre)
                .thenComparing(v -> {
                    switch (v.getEstado()) {
                        case PENDIENTE: return 0;
                        case EN_CURSO: return 1;
                        default: return 2; // FINALIZADO
                    }
                }));
        return resultado;
    }


    public List<Viaje> listarViajesPorEstado(estado estadoBusqueda) {
        Agencia agencia = Agencia.getInstance();
        List<Viaje> resultado = new ArrayList<>();

        for (Transporte t : agencia.getTransportes()) {
            for (Viaje v : t.getListaViajes()) {
                if (v.getEstado() == estadoBusqueda) {
                    resultado.add(v);
                }
            }
        }

        // Sort: name ascending, then estado priority PENDIENTE -> EN_CURSO -> FINALIZADO
        resultado.sort(java.util.Comparator.comparing(Viaje::getNombre)
                .thenComparing(v -> {
                    switch (v.getEstado()) {
                        case PENDIENTE: return 0;
                        case EN_CURSO: return 1;
                        default: return 2; // FINALIZADO
                    }
                }));
        return resultado;
    }
}
