package model;

import java.io.Serializable;

public class ColectivoCocheCama extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int CAPACIDAD_MAXIMA = 32;
    private static final int CANT_SEMI_CAMA = 6;
    private static final int CANT_CAMA = 26;

    private float valorPorPasajeroPorKmRecorrido;
    private float valorPlazaTipoCamaPorKmRecorrido;

    public ColectivoCocheCama(String patente, float velocidadPromedioXhora, float valorPorPasajeroPorKmRecorrido, float valorPlazaTipoCamaPorKmRecorrido) {
        super(patente, CAPACIDAD_MAXIMA, velocidadPromedioXhora);
        this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
        this.valorPlazaTipoCamaPorKmRecorrido = valorPlazaTipoCamaPorKmRecorrido;
    }

    public int getCantSemiCama() { return CANT_SEMI_CAMA; }
    public int getCantCama() { return CANT_CAMA; }
    public float getValorPorPasajeroPorKmRecorrido() { return valorPorPasajeroPorKmRecorrido; }
    public float getValorPlazaTipoCamaPorKmRecorrido() { return valorPlazaTipoCamaPorKmRecorrido; }

    public void setValorPorPasajeroPorKmRecorrido(float valorPorPasajeroPorKmRecorrido) {
        if (valorPorPasajeroPorKmRecorrido >= 0) {
            this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
        }
    }

    public void setValorPlazaTipoCamaPorKmRecorrido(float valorPlazaTipoCamaPorKmRecorrido) {
        if (valorPlazaTipoCamaPorKmRecorrido >= 0) {
            this.valorPlazaTipoCamaPorKmRecorrido = valorPlazaTipoCamaPorKmRecorrido;
        }
    }

    @Override
    public float calculaCostoPorViaje(float kms, int ps) {
        int pasajerosEnCama = Math.min(ps, CANT_CAMA);
        float costoBase = valorPorPasajeroPorKmRecorrido * ps * kms;
        float costoCamas = valorPlazaTipoCamaPorKmRecorrido * pasajerosEnCama * kms;
        return costoBase + costoCamas;
    }

    @Override
    public boolean cumpleCondiciones(Destino d) {
        return d.esLargaDistancia();
    }

    @Override
    public String toString() {
        return super.toString() + " - ColectivoCocheCama [Plazas Cama: " + CANT_CAMA + ", Plazas SemiCama: " + CANT_SEMI_CAMA + "]";
    }
}