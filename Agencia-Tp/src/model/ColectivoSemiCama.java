package model;

import java.io.Serializable;

public class ColectivoSemiCama extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int CAPACIDAD_MAXIMA = 40;

    private float valorPorPasajerosPorKmRecorrido;

    public ColectivoSemiCama(String patente, float velocidadPromedioXhora, float valorPorPasajerosPorKmRecorrido) {
        super(patente, CAPACIDAD_MAXIMA, velocidadPromedioXhora);
        this.valorPorPasajerosPorKmRecorrido = valorPorPasajerosPorKmRecorrido;
    }

    public float getValorPorPasajerosPorKmRecorrido() {
        return valorPorPasajerosPorKmRecorrido;
    }

    public void setValorPorPasajerosPorKmRecorrido(float valorPorPasajerosPorKmRecorrido) {
        if (valorPorPasajerosPorKmRecorrido >= 0) {
            this.valorPorPasajerosPorKmRecorrido = valorPorPasajerosPorKmRecorrido;
        }
    }

    @Override
    public float calculaCostoPorViaje(float kms, int ps) {
        return valorPorPasajerosPorKmRecorrido * ps * kms;
    }

    @Override
    public String toString() {
        return super.toString() + " - ColectivoSemiCama [Valor/Pasajero/Km: " + valorPorPasajerosPorKmRecorrido + "]";
    }
}