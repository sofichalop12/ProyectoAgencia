package com.agencia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "transportes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String patente;

    private int capacidadPasajeros;
    private boolean disponible;
    private float velocidadPromedioXhora;

    @JsonIgnore
    @OneToMany(mappedBy = "transporteAsignado")
    private Set<Viaje> listaViajes = new HashSet<>();

    // Constructor vacío obligatorio para JPA
    public Transporte() {
    }

    public Transporte(String patente, int capacidadPasajeros, float velocidadPromedioXhora) {
        this.patente = patente;
        this.capacidadPasajeros = capacidadPasajeros;
        this.velocidadPromedioXhora = velocidadPromedioXhora;
        this.disponible = true;
    }

    // Getters y Setters
    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public void setCapacidadPasajeros(int capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public float getVelocidadPromedioXhora() {
        return velocidadPromedioXhora;
    }

    public void setVelocidadPromedioXhora(float velocidadPromedioXhora) {
        if (velocidadPromedioXhora > 0) {
            this.velocidadPromedioXhora = velocidadPromedioXhora;
        }
    }

    public Set<Viaje> getListaViajes() {
        return Collections.unmodifiableSet(listaViajes);
    }

    public void setListaViajes(Set<Viaje> listaViajes) {
        this.listaViajes = listaViajes;
    }

    // Métodos del dominio
    public boolean cumpleCondiciones(Destino d) {
        return true;
    }

    public void agregarViaje(Viaje v) {
        if (v != null) {
            listaViajes.add(v);
        }
    }

    public boolean estaDisponible() {
        boolean disp = true;
        Iterator<Viaje> Lv = listaViajes.iterator();
        while (disp && Lv.hasNext()) {
            Viaje v = Lv.next();
            if (v.estaPendiente() || v.estaEnCurso()) {
                disp = false;
            }
        }
        return disp;
    }

    public void quitarViaje(Viaje v) {
        listaViajes.remove(v);
    }

    public void liberarTransporte() {
        disponible = true;
    }

    public void ocuparTransporte() {
        disponible = false;
    }

    public abstract float calculaCostoPorViaje(float kms, int ps);

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Transporte that = (Transporte) o;
        return Objects.equals(patente, that.patente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patente);
    }

    @Override
    public String toString() {
        StringBuilder viajesNombres = new StringBuilder();

        for (Viaje v : listaViajes) {
            viajesNombres.append(v.getNombre()).append(", ");
        }

        String resultadoViajes = viajesNombres.length() > 0
                ? viajesNombres.substring(0, viajesNombres.length() - 2)
                : "Sin viajes asignados";

        return "Transporte [ Patente: " + patente
                + ", Capacidad de Pasajeros: " + capacidadPasajeros
                + ", Disponible: " + disponible
                + ", Velocidad promedio por hora: " + velocidadPromedioXhora
                + ", Viajes: " + resultadoViajes + " ]";
    }
}