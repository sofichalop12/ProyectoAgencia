package com.agencia.model;

import com.agencia.exceptions.ValidacionException;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "viajes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Viaje implements Comparable<Viaje>, Serializable {

    private static final long serialVersionUID = 1L;

    public enum Estado {
        PENDIENTE, EN_CURSO, FINALIZADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idViaje;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "destino_id")
    private Destino destinoDelViaje;

    private int cantPasajeros;

    @Enumerated(EnumType.STRING)
    private Estado estadoActual;

    @ManyToMany
    @JsonIgnoreProperties("responsables")
    private Set<ResponsableABordo> responsables = new HashSet<>();

    private float avanceKmRecorridos;

    @ManyToOne
    @JoinColumn(name = "transporte_patente")
    @JsonIgnoreProperties("listaViajes")
    private Transporte transporteAsignado;

    // Constructor vacío obligatorio para JPA
    public Viaje() {
    }

    // CONSTRUCTOR
    public Viaje(String nom, Destino destinoViaje, int cantPasajeros, Transporte t) {
        this.nombre = nom;
        this.destinoDelViaje = destinoViaje;
        this.cantPasajeros = cantPasajeros;
        this.estadoActual = Estado.PENDIENTE;
        this.avanceKmRecorridos = 0;
        this.transporteAsignado = t;
    }

    // GETTERS Y SETTERS
    public Long getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Long idViaje) {
        this.idViaje = idViaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Destino getDestinoDelViaje() {
        return destinoDelViaje;
    }

    public void setDestinoDelViaje(Destino destinoDelViaje) {
        this.destinoDelViaje = destinoDelViaje;
    }

    public int getCantPasajeros() {
        return cantPasajeros;
    }

    public void setCantPasajeros(int cantPasajeros) {
        this.cantPasajeros = cantPasajeros;
    }

    public float getAvanceKmRecorridos() {
        return avanceKmRecorridos;
    }

    public void setAvanceKmRecorridos(float avanceKmRecorridos) {
        this.avanceKmRecorridos = avanceKmRecorridos;
    }

    public Set<ResponsableABordo> getResponsables() {
        return Collections.unmodifiableSet(responsables);
    }

    public void setResponsables(Set<ResponsableABordo> responsables) {
        this.responsables = responsables;
    }

    public Transporte getTransporteAsignado() {
        return transporteAsignado;
    }

    public Estado getEstado() {
        return estadoActual;
    }

    public void setEstado(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }

    @Override
    public int compareTo(Viaje o) {
        if (this.nombre == null || o.getNombre() == null)
            return 0;
        return nombre.compareTo(o.getNombre());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Viaje viaje = (Viaje) o;
        return Objects.equals(idViaje, viaje.idViaje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idViaje);
    }

    public abstract float calcularCostoBase();

    public float calcularCostoFinal() {
        if (transporteAsignado == null || destinoDelViaje == null)
            return calcularCostoBase();
        return calcularCostoBase()
                + transporteAsignado.calculaCostoPorViaje(destinoDelViaje.getCantKm(), cantPasajeros);
    }

    public void iniciar() {
        if (estadoActual != Estado.PENDIENTE)
            throw new IllegalStateException("El viaje ya fue iniciado o finalizado.");
        if (this.cantPasajeros <= 0)
            throw new IllegalStateException("No se puede iniciar un viaje con 0 pasajeros.");
        estadoActual = Estado.EN_CURSO;
    }

    public void avanzarKm(float delta) {
        if (estadoActual != Estado.EN_CURSO)
            throw new IllegalStateException("Solo se puede avanzar un viaje en curso.");
        if (delta <= 0)
            throw new IllegalArgumentException("La distancia debe ser positiva.");
        if (avanceKmRecorridos + delta > getKmTotales())
            throw new IllegalArgumentException(
                    "Los kilómetros acumulados no pueden superar los kilómetros totales del viaje (" + getKmTotales()
                            + " km).");
        avanceKmRecorridos += delta;
    }

    public void finalizar() {
        if (estadoActual != Estado.EN_CURSO)
            throw new IllegalStateException("Solo se puede finalizar un viaje en curso.");
        estadoActual = Estado.FINALIZADO;
        liberarResponsables();
    }

    public void liberarResponsables() {
        Iterator<ResponsableABordo> res = responsables.iterator();
        while (res.hasNext()) {
            ResponsableABordo r = res.next();
            r.AcumularKmRecorridos(avanceKmRecorridos);
            r.Liberar();
            res.remove();
        }
    }

    public void AgregarResponsable(ResponsableABordo r) {
        this.responsables.add(r);
    }

    public void QuitarResponsable(ResponsableABordo r) {
        this.responsables.remove(r);
    }

    public void AgregarPasajeros(int n) {
        if (transporteAsignado != null && (cantPasajeros + n > transporteAsignado.getCapacidadPasajeros())) {
            throw new IllegalStateException("No hay capacidad disponible en el transporte.");
        } else {
            this.cantPasajeros += n;
        }
    }

    public void AgregarUnPasajero() {
        this.AgregarPasajeros(1);
    }

    public void QuitarPasajeros() {
        this.cantPasajeros--;
    }

    public void setTransporteAsignado(Transporte transporte) throws ValidacionException {
        if (transporte == null) {
            throw new ValidacionException("El transporte no puede ser nulo.");
        }

        if (transporte.getCapacidadPasajeros() < this.cantPasajeros) {
            throw new ValidacionException("Capacidad excedida. El transporte solo permite " +
                    transporte.getCapacidadPasajeros() + " pasajeros (este viaje tiene " + this.cantPasajeros + ").");
        }

        this.transporteAsignado = transporte;
    }

    public boolean estaPendiente() {
        return estadoActual == Estado.PENDIENTE;
    }

    public boolean estaEnCurso() {
        return estadoActual == Estado.EN_CURSO;
    }

    public boolean estaFinalizado() {
        return estadoActual == Estado.FINALIZADO;
    }

    public float getKmTotales() {
        return destinoDelViaje != null ? destinoDelViaje.getCantKm() : 0;
    }

    public float getKmRestantes() {
        return getKmTotales() - avanceKmRecorridos;
    }

    public float getPorcentajeAvance() {
        float total = getKmTotales();
        return total <= 0 ? 0 : (avanceKmRecorridos / total) * 100f;
    }
}