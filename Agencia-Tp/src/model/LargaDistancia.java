package model;

import java.io.Serializable;

public class LargaDistancia extends Viaje implements Serializable {

    private static final long serialVersionUID = 1L;

    public LargaDistancia(int idViaje, String nombre, Destino destino, int cantPasajeros, Transporte transporte) {
        super(idViaje, nombre, destino, cantPasajeros, transporte);
    }

    @Override
    public float calcularCostoBase() {
        return (float) getResponsables().stream()
                .mapToDouble(ResponsableABordo::getSueldo)
                .sum();
    }

    @Override
    public String toString() {
        return getNombre() + " [" + getEstado() + "] (Larga Distancia)";
    }
}