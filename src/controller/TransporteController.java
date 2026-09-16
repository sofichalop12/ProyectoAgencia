package controller;

import model.*;
import java.util.*;

public class TransporteController {
    private Agencia agencia = Agencia.getInstance(); //ver



    public Set<Transporte> obtenerTransportePorDestino(String nombreDestino) throws Exception{
        Destino destino = agencia.buscarDestinoPorNombre(nombreDestino);

        if(destino == null){
            throw new Exception("No existe el destino:" + nombreDestino);
        }

        return agencia.transportesParaDestino(destino);
    }
}



