package controller;

import model.*;
import java.util.*;

public class TransporteController {

    private final Agencia agencia = Agencia.getInstance();

    public Set<Transporte> obtenerTransportePorDestino(String nombreDestino) throws Exception {
        Destino destino = agencia.buscarDestinoPorNombre(nombreDestino);

        if (destino == null) {
            throw new Exception("No existe el destino: " + nombreDestino);
        }

        return agencia.transportesParaDestino(destino);
    }
}