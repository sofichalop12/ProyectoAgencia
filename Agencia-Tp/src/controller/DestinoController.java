package controller;

import exceptions.DestinoYaExisteException;
import exceptions.ValidacionException;
import model.Agencia;
import model.Destino;

import java.util.Set;

public class DestinoController {

    public void crearDestino(String nombre, int km) {
        try {
            Destino d1 = new Destino(nombre, km);
            Agencia.getInstance().agregarDestino(d1);
        } catch (DestinoYaExisteException e) {
            throw new ValidacionException(e.getMessage());
        }
    }

    public Set<Destino> listarDestinos() {
        return Agencia.getInstance().getDestinos();
    }

    public Destino buscarDestinoPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre del destino no puede ser vacío.");
        }

        Destino encontrado = Agencia.getInstance().buscarDestinoPorNombre(nombre);

        if (encontrado == null) {
            throw new ValidacionException("Destino '" + nombre + "' no encontrado.");
        }

        return encontrado;
    }
}