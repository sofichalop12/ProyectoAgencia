package com.agencia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "transportes_autos")
public class Auto extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;

    private float valorBasePorViaje;
    private float valorPorKmRecorrido;

    // Constructor vacío obligatorio para JPA
    public Auto() {
        super();
    }

    public Auto(String patente, float velocidadPromedioXhora, float valorBasePorViaje, float valorPorKmRecorrido) {
        super(patente, 4, velocidadPromedioXhora);
        this.valorBasePorViaje = valorBasePorViaje;
        this.valorPorKmRecorrido = valorPorKmRecorrido;
    }

    public float getValorBasePorViaje() {
        return valorBasePorViaje;
    }

    public void setValorBasePorViaje(float valorBasePorViaje) {
        if (valorBasePorViaje > 0) {
            this.valorBasePorViaje = valorBasePorViaje;
        } else {
            this.valorBasePorViaje = 0;
        }
    }

    public float getValorPorKmRecorrido() {
        return valorPorKmRecorrido;
    }

    public void setValorPorKmRecorrido(float valorPorKmRecorrido) {
        if (valorPorKmRecorrido > 0) {
            this.valorPorKmRecorrido = valorPorKmRecorrido;
        }
    }

    @Override
    public float calculaCostoPorViaje(float kilometros, int pasajeros) {
        return this.valorBasePorViaje + (valorPorKmRecorrido * kilometros);
    }

    @Override
    public String toString() {
        return "Auto [Valor Base Por viaje: " + valorBasePorViaje + ", Valor por km recorrido: " + valorPorKmRecorrido + " ]";
    }

    @Override
    public boolean cumpleCondiciones(Destino d) {
        return d != null && !(d.esLargaDistancia());
    }
}