package model;

import java.io.Serializable;

public class Auto extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int CAPACIDAD_MAXIMA = 4;

    private float valorBasePorViaje;
    private float valorPorKmRecorrido;

    public Auto(String patente, float velocidadPromedioXhora, float valorBasePorViaje, float valorPorKmRecorrido) {
        super(patente, CAPACIDAD_MAXIMA, velocidadPromedioXhora);
        this.valorBasePorViaje = valorBasePorViaje;
        this.valorPorKmRecorrido = valorPorKmRecorrido;
    }

    public float getValorBasePorViaje() { return valorBasePorViaje; }

    public void setValorBasePorViaje(float valorBasePorViaje) {
        if (valorBasePorViaje >= 0) {
            this.valorBasePorViaje = valorBasePorViaje;
        }
    }

    public float getValorPorKmRecorrido() { return valorPorKmRecorrido; }

    public void setValorPorKmRecorrido(float valorPorKmRecorrido) {
        if (valorPorKmRecorrido >= 0) {
            this.valorPorKmRecorrido = valorPorKmRecorrido;
        }
    }

    @Override
    public float calculaCostoPorViaje(float kilometros, int pasajeros) {
        return this.valorBasePorViaje + (valorPorKmRecorrido * kilometros);
    }

    @Override
    public boolean cumpleCondiciones(Destino d) {
        return !d.esLargaDistancia();
    }

    @Override
    public String toString() {
        return super.toString() + " - Auto [Valor Base: " + valorBasePorViaje + ", Valor/Km: " + valorPorKmRecorrido + "]";
    }
}