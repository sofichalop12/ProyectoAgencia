package model;

import java.io.Serializable;

public class Combi extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int CAPACIDAD_MAXIMA = 16;

    private float valorBasePorViaje;
    private float valorPorPasajeroPorKmRecorrido;

    public Combi(String patente, float velocidadPromedioXhora, float valorBasePorViaje, float valorPorPasajeroPorKmRecorrido) {
        super(patente, CAPACIDAD_MAXIMA, velocidadPromedioXhora);
        this.valorBasePorViaje = valorBasePorViaje;
        this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
    }

    public float getValorBasePorViaje() { return valorBasePorViaje; }

    public void setValorBasePorViaje(float valorBasePorViaje) {
        if (valorBasePorViaje >= 0) {
            this.valorBasePorViaje = valorBasePorViaje;
        }
    }

    public float getValorPorPasajeroPorKmRecorrido() { return valorPorPasajeroPorKmRecorrido; }

    public void setValorPorPasajeroPorKmRecorrido(float valorPorPasajeroPorKmRecorrido) {
        if (valorPorPasajeroPorKmRecorrido >= 0) {
            this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
        }
    }

    @Override
    public float calculaCostoPorViaje(float kms, int ps) {
        return valorBasePorViaje + (valorPorPasajeroPorKmRecorrido * ps * kms);
    }

    @Override
    public String toString() {
        return super.toString() + " - Combi [Valor Base: " + valorBasePorViaje + ", Valor/Pasajero/Km: " + valorPorPasajeroPorKmRecorrido + "]";
    }
}