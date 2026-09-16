package model;

import java.io.Serializable;

public class CortaDistancia extends Viaje implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final float COBRO_BASE = 1500f;

    public CortaDistancia(int idViaje, String nombre, Destino destino, int cantPasajeros, Transporte transporte) {
        super(idViaje, nombre, destino, cantPasajeros, transporte);
    }

    public float getCobroBase() {
        return COBRO_BASE;
    }

    @Override
    public float calcularCostoBase() {
        return COBRO_BASE;
    }

    @Override
    public String toString() {
        return getNombre() + " [" + getEstado() + "] (Corta Distancia)";
    }
}