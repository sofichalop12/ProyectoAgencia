package com.agencia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "viajes_corta_distancia")
public class CortaDistancia extends Viaje implements Serializable {

    private static final float cobrobase = 1500; // Valor inicial por defecto
    private static final long serialVersionUID = 1L;

    // Constructor vacío obligatorio para JPA
    public CortaDistancia() {
        super();
    }

    public CortaDistancia(int idVia, String nom, Destino destinoViaje, int cantP, Transporte t) {
        super(nom, destinoViaje, cantP, t);
    }

    public CortaDistancia(String nom, Destino destinoViaje, int cantP, Transporte t) {
        super(nom, destinoViaje, cantP, t);
    }

    public float getCobrobase() {
        return cobrobase;
    }

    @Override
    public String toString() {
        return getNombre() + " [" + getEstado() + "] (Corta Distancia)";
    }

    @Override
    public float calcularCostoBase() {
        return cobrobase;
    }
}