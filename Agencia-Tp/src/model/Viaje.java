package model;

import exceptions.ValidacionException;
import java.io.Serializable;
import java.util.*;

public abstract class Viaje implements Comparable<Viaje>, Serializable {

    private static final long serialVersionUID = 1L;

    private int idViaje;
    private String nombre;
    private Destino destinoDelViaje;
    private int cantPasajeros;
    private EstadoViaje estadoActual;
    private Set<ResponsableABordo> responsables;
    private float avanceKmRecorridos;
    private Transporte transporteAsignado;

    public Viaje(int idViaje, String nombre, Destino destinoDelViaje, int cantPasajeros, Transporte transporte) {
        this.idViaje = idViaje;
        this.nombre = nombre;
        this.destinoDelViaje = destinoDelViaje;
        this.cantPasajeros = cantPasajeros;
        this.estadoActual = EstadoViaje.PENDIENTE;
        this.avanceKmRecorridos = 0;
        this.transporteAsignado = transporte;
        this.responsables = new HashSet<>();
    }

    // #### GETTERS Y SETTERS ####
    public int getIdViaje() { return idViaje; }
    public String getNombre() { return nombre; }
    public Destino getDestinoDelViaje() { return destinoDelViaje; }
    public int getCantPasajeros() { return cantPasajeros; }
    public float getAvanceKmRecorridos() { return avanceKmRecorridos; }
    public EstadoViaje getEstado() { return estadoActual; }
    public Transporte getTransporteAsignado() { return transporteAsignado; }

    public Set<ResponsableABordo> getResponsables() {
        return Collections.unmodifiableSet(responsables);
    }

    // #### LÓGICA DE COSTOS Y ESTADO ####
    public abstract float calcularCostoBase();

    public float calcularCostoFinal() {
        return calcularCostoBase() + transporteAsignado.calculaCostoPorViaje(destinoDelViaje.getCantKm(), cantPasajeros);
    }

    public void iniciar() {
        if (estadoActual != EstadoViaje.PENDIENTE) {
            throw new IllegalStateException("El viaje ya fue iniciado o finalizado.");
        }
        if (this.cantPasajeros <= 0) {
            throw new IllegalStateException("No se puede iniciar un viaje con 0 pasajeros.");
        }
        this.estadoActual = EstadoViaje.EN_CURSO;
    }

    public void avanzarKm(float delta) {
        if (estadoActual != EstadoViaje.EN_CURSO) {
            throw new IllegalStateException("Solo se puede avanzar un viaje en curso.");
        }
        if (delta <= 0) {
            throw new IllegalArgumentException("La distancia debe ser positiva.");
        }
        if (avanceKmRecorridos + delta > getKmTotales()) {
            throw new IllegalArgumentException("Los kilómetros acumulados no pueden superar el total del viaje (" + getKmTotales() + " km).");
        }
        this.avanceKmRecorridos += delta;
    }

    public void finalizar() {
        if (estadoActual != EstadoViaje.EN_CURSO) {
            throw new IllegalStateException("Solo se puede finalizar un viaje en curso.");
        }
        this.estadoActual = EstadoViaje.FINALIZADO;
        liberarResponsables();
    }

    private void liberarResponsables() {
        for (ResponsableABordo r : responsables) {
            r.acumularKmRecorridos(avanceKmRecorridos);
            r.liberar();
        }
    }

    public void agregarResponsable(ResponsableABordo r) {
        if (r != null) {
            this.responsables.add(r);
        }
    }
    public void AgregarResponsable(ResponsableABordo r) { agregarResponsable(r); }

    public void quitarResponsable(ResponsableABordo r) {
        this.responsables.remove(r);
    }
    public void QuitarResponsable(ResponsableABordo r) { quitarResponsable(r); }

    public void agregarPasajeros(int n) {
        if (transporteAsignado != null && (cantPasajeros + n > transporteAsignado.getCapacidadPasajeros())) {
            throw new IllegalStateException("No hay capacidad disponible en el transporte.");
        }
        this.cantPasajeros += n;
    }

    public void agregarUnPasajero() {
        this.agregarPasajeros(1);
    }

    public void quitarPasajeros() {
        if (this.cantPasajeros > 0) {
            this.cantPasajeros--;
        }
    }

    public void setTransporteAsignado(Transporte transporte) throws ValidacionException {
        if (transporte == null) {
            throw new ValidacionException("El transporte no puede ser nulo.");
        }
        if (transporte.getCapacidadPasajeros() < this.cantPasajeros) {
            throw new ValidacionException("Capacidad excedida. El transporte solo permite " +
                    transporte.getCapacidadPasajeros() + " pasajeros.");
        }
        this.transporteAsignado = transporte;
    }

    public boolean estaPendiente() { return estadoActual == EstadoViaje.PENDIENTE; }
    public boolean estaEnCurso() { return estadoActual == EstadoViaje.EN_CURSO; }
    public boolean estaFinalizado() { return estadoActual == EstadoViaje.FINALIZADO; }

    public float getKmTotales() { return destinoDelViaje.getCantKm(); }
    public float getKmRestantes() { return getKmTotales() - avanceKmRecorridos; }

    public float getPorcentajeAvance() {
        float total = getKmTotales();
        return total <= 0 ? 0 : (avanceKmRecorridos / total) * 100f;
    }

    @Override
    public int compareTo(Viaje o) {
        return nombre.compareTo(o.getNombre());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Viaje viaje = (Viaje) o;
        return idViaje == viaje.idViaje;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idViaje);
    }
}