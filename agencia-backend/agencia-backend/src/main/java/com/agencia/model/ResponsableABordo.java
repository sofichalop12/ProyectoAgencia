package com.agencia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "responsables_a_bordo")
public class ResponsableABordo implements Comparable<ResponsableABordo>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Boolean estaDisp; // S o N
    private String dni;
    private float sueldoXViaje;
    private float cantKmAcumulados;

    // Constructor vacío requerido obligatoriamente por JPA
    public ResponsableABordo() {
    }

    public ResponsableABordo(String nombre, String dni, float sueldoXViaje) {
        this.nombre = nombre;
        this.estaDisp = true;
        this.dni = dni;
        this.sueldoXViaje = sueldoXViaje;
        this.cantKmAcumulados = 0;
    }

    // Getters y Setters
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

    public Boolean getEstaDisp() {
        return estaDisp;
    }

    public void setEstaDisp(Boolean estaDisp) {
        this.estaDisp = estaDisp;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public float getSueldoXViaje() {
        return sueldoXViaje;
    }

    public void setSueldoXViaje(float sueldoXViaje) {
        this.sueldoXViaje = sueldoXViaje;
    }

    public float getCantKmAcumulados() {
        return cantKmAcumulados;
    }

    public void setCantKmAcumulados(float cantKmAcumulados) {
        this.cantKmAcumulados = cantKmAcumulados;
    }

    // Compatibilidad con los nombres de métodos originales
    public String GetNombre() {
        return getNombre();
    }

    public Boolean GetEstaDisp() {
        return getEstaDisp();
    }

    public String GetDni() {
        return getDni();
    }

    public float GetSueldo() {
        return getSueldoXViaje();
    }

    // Métodos de negocio originales
    public void AcumularKmRecorridos(float km) {
        this.cantKmAcumulados += km;
    }

    public void Ocupar() {
        this.estaDisp = false;
    }

    public void Liberar() {
        this.estaDisp = true;
    }

    @Override
    public int compareTo(ResponsableABordo otro) {
        return Float.compare(otro.getCantKmAcumulados(), this.getCantKmAcumulados());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponsableABordo that = (ResponsableABordo) o;
        return Objects.equals(this.dni, that.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public String toString() {
        return "Responsable [Nombre: " + nombre + ", DNI: " + dni + ", Disponible: " + estaDisp + "]";
    }
}