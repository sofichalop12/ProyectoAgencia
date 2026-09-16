package com.agencia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "transportes_coche_cama")
public class ColectivoCocheCama extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;

    private int cantSemiCama;
    private int cantCama;
    private float valorPorPasajeroPorKmRecorrido;
    private float valorPlazaTipoCamaPorKmRecorrido;

    // Constructor vacío obligatorio para JPA
    public ColectivoCocheCama() {
        super();
    }

    public ColectivoCocheCama(String patente, float velocidadPromedioXhora, float valorPorPasajeroPorKmRecorrido, float valorPlazaTipoCamaPorKmRecorrido) {
        super(patente, 32, velocidadPromedioXhora);
        this.cantSemiCama = 6;
        this.cantCama = 26;
        this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
        this.valorPlazaTipoCamaPorKmRecorrido = valorPlazaTipoCamaPorKmRecorrido;
    }

    public int getCantSemiCama() {
        return cantSemiCama;
    }

    public void setCantSemiCama(int cantSemiCama) {
        this.cantSemiCama = cantSemiCama;
    }

    public int getCantCama() {
        return cantCama;
    }

    public void setCantCama(int cantCama) {
        this.cantCama = cantCama;
    }

    public float getValorPorPasajeroPorKmRecorrido() {
        return valorPorPasajeroPorKmRecorrido;
    }

    public void setValorPorPasajeroPorKmRecorrido(float valorPorPasajeroPorKmRecorrido) {
        this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
    }

    public float getValorPlazaTipoCamaPorKmRecorrido() {
        return valorPlazaTipoCamaPorKmRecorrido;
    }

    public void setValorPlazaTipoCamaPorKmRecorrido(float valorPlazaTipoCamaPorKmRecorrido) {
        this.valorPlazaTipoCamaPorKmRecorrido = valorPlazaTipoCamaPorKmRecorrido;
    }

    @Override
    public float calculaCostoPorViaje(float kms, int ps) {
        int pasajerosEnCama = Math.min(ps, cantCama);
        float costoBase = valorPorPasajeroPorKmRecorrido * ps * kms;
        float costoCamas = valorPlazaTipoCamaPorKmRecorrido * pasajerosEnCama * kms;

        return costoBase + costoCamas;
    }

    @Override
    public boolean cumpleCondiciones(Destino d) {
        return d != null && d.esLargaDistancia();
    }

    @Override
    public String toString() {
        return "ColectivoCocheCama [Cantidad Semi Cama: " + cantSemiCama + ", Cantidad Cama: " + cantCama + ", Valor por pasajero km: " + valorPorPasajeroPorKmRecorrido + " ]";
    }
}