package com.agencia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "viajes_larga_distancia")
public class LargaDistancia extends Viaje implements Serializable {

    private static final long serialVersionUID = 1L;

    // Constructor vacío obligatorio para JPA
    public LargaDistancia() {
        super();
    }

    public LargaDistancia(String nom, Destino destinoViaje, int cantp, Transporte t) {
        super(nom, destinoViaje, cantp, t);
    }

    public LargaDistancia(int idVia, String nom, Destino destinoViaje, int cantp, Transporte t) {
        super(nom, destinoViaje, cantp, t);
    }

    @Override
    public float calcularCostoBase() {
        float sueldo = 0;

        for (ResponsableABordo r : this.getResponsables()) {
            sueldo += r.GetSueldo();
        }
        return sueldo;
    }

    @Override
    public String toString() {
        return getNombre() + " [" + getEstado() + "] (Larga Distancia)";
    }
}