package com.agencia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "transportes_semi_cama")
public class ColectivoSemiCama extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;

    private float valorPorPasajerosPorKmRecorrido;

    // Constructor vacío obligatorio para JPA
    public ColectivoSemiCama() {
        super();
    }

    public ColectivoSemiCama(String patente, float velocidadPromedioXhora, float valorPorPasajerosPorKmRecorrido) {
        super(patente, 40, velocidadPromedioXhora);
        this.valorPorPasajerosPorKmRecorrido = valorPorPasajerosPorKmRecorrido;
    }

    public float getValorPorPasajerosPorKmRecorrido() {
        return valorPorPasajerosPorKmRecorrido;
    }

    public void setValorPorPasajerosPorKmRecorrido(float valorPorPasajerosPorKmRecorrido) {
        this.valorPorPasajerosPorKmRecorrido = valorPorPasajerosPorKmRecorrido;
    }

    @Override
    public float calculaCostoPorViaje(float kms, int ps) {
        return valorPorPasajerosPorKmRecorrido * ps * kms;
    }

    @Override
    public String toString() {
        return "ColectivoSemiCama [Valor Por Pasajeros Por Km Recorrido: " + valorPorPasajerosPorKmRecorrido + " ]";
    }
}