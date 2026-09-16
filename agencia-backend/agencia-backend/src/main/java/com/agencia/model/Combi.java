package com.agencia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "transportes_combis")
public class Combi extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;

    private float valorBasePorViaje;
    private float valorPorPasajeroPorKmRecorrido;

    // Constructor vacío obligatorio para JPA
    public Combi() {
        super();
    }

    public Combi(String patente, float velocidadPromedioXhora, float valorBasePorViaje, float valorPorPasajeroPorKmRecorrido) {
        super(patente, 16, velocidadPromedioXhora);
        this.valorBasePorViaje = valorBasePorViaje;
        this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
    }

    public float getValorBasePorViaje() {
        return valorBasePorViaje;
    }

    public void setValorBasePorViaje(float valorBasePorViaje) {
        if (valorBasePorViaje > 0) {
            this.valorBasePorViaje = valorBasePorViaje;
        }
    }

    public float getValorPorPasajeroPorKmRecorrido() {
        return valorPorPasajeroPorKmRecorrido;
    }

    public void setValorPorPasajeroPorKmRecorrido(float valorPorPasajeroPorKmRecorrido) {
        if (valorPorPasajeroPorKmRecorrido > 0) {
            this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
        }
    }

    @Override
    public float calculaCostoPorViaje(float kms, int ps) {
        return valorBasePorViaje + (valorPorPasajeroPorKmRecorrido * ps * kms);
    }

    @Override
    public String toString() {
        return "Combi [Valor Base Por viaje: " + valorBasePorViaje + ", Valor por pasajero km: " + valorPorPasajeroPorKmRecorrido + " ]";
    }
}