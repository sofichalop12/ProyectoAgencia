package com.agencia.model;

import com.agencia.exceptions.ValidacionException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "destinos")
public class Destino implements Comparable<Destino>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private float cantKm;

    public Destino() {
    }

    public Destino(String nombre, float cantKm) throws ValidacionException {
        if (cantKm <= 0) {
            throw new ValidacionException("Los kilómetros deben ser un valor positivo.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre;
        this.cantKm = cantKm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCantKm() {
        return cantKm;
    }

    public void setCantKm(float cantKm) {
        this.cantKm = cantKm;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int compareTo(Destino otro) {
        if (this.nombre == null || otro.getNombre() == null) {
            return 0;
        }
        return this.nombre.compareTo(otro.getNombre());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destino that = (Destino) o;
        return Objects.equals(this.nombre, that.nombre);
    }

    public boolean esLargaDistancia() {
        return this.cantKm > 100;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nombre);
    }
}