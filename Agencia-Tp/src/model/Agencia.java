package model;

import exceptions.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Agencia implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Agencia instancia;

    private final Map<String, Transporte> transportesPorPatente;
    private final Map<String, Destino> destinosPorNombre;
    private final Map<String, ResponsableABordo> responsablesPorDni;
    private final Map<Integer, Viaje> viajesPorId;
    private final Map<Destino, Integer> cantidadDeViajesPorDestino;

    private int cantViajesCreados;

    private Agencia() {
        this.transportesPorPatente = new HashMap<>();
        this.destinosPorNombre = new HashMap<>();
        this.responsablesPorDni = new HashMap<>();
        this.viajesPorId = new HashMap<>();
        this.cantidadDeViajesPorDestino = new HashMap<>();
        this.cantViajesCreados = 0;
    }

    public static synchronized Agencia getInstance() {
        if (instancia == null) {
            instancia = new Agencia();
        }
        return instancia;
    }

    public static void setInstance(Agencia agencia) {
        instancia = agencia;
    }

    // #### GETTERS SEGUROS ####
    public Set<Transporte> getTransportes() {
        return Collections.unmodifiableSet(new HashSet<>(transportesPorPatente.values()));
    }

    public Set<Destino> getDestinos() {
        return Collections.unmodifiableSet(new HashSet<>(destinosPorNombre.values()));
    }

    public Set<ResponsableABordo> getResponsables() {
        return Collections.unmodifiableSet(new HashSet<>(responsablesPorDni.values()));
    }

    // #### BÚSQUEDAS OPTIMIZADAS O(1) ####
    public Transporte buscarTransportePorPatente(String patente) {
        if (patente == null) return null;
        return transportesPorPatente.get(patente.toUpperCase());
    }

    public Destino buscarDestinoPorNombre(String nombre) {
        if (nombre == null) return null;
        return destinosPorNombre.get(nombre.toLowerCase());
    }

    public ResponsableABordo buscarResponsablePorDni(String dni) {
        if (dni == null) return null;
        return responsablesPorDni.get(dni);
    }

    public Viaje buscarViajePorId(int idViaje) {
        return viajesPorId.get(idViaje);
    }

    // #### REGISTRO Y ALTAS ####
    public void agregarDestino(Destino d) {
        if (d != null) {
            if (destinosPorNombre.containsKey(d.getNombre().toLowerCase())) {
                throw new DestinoYaExisteException(d.getNombre());
            }
            destinosPorNombre.put(d.getNombre().toLowerCase(), d);
        }
    }
    public void AgregarDestino(Destino d) { agregarDestino(d); }

    public void agregarTransporte(Transporte t) {
        if (t != null) {
            transportesPorPatente.put(t.getPatente().toUpperCase(), t);
        }
    }
    public void AgregarTransporte(Transporte t) { agregarTransporte(t); }

    public void agregarResponsable(ResponsableABordo r) {
        if (r != null) {
            responsablesPorDni.put(r.getDni(), r);
        }
    }
    public void AgregarResponsable(ResponsableABordo r) { agregarResponsable(r); }

    public Set<Transporte> transportesParaDestino(Destino d) {
        return transportesPorPatente.values().stream()
                .filter(t -> t.estaDisponible() && t.cumpleCondiciones(d))
                .collect(Collectors.toSet());
    }

    public int obtenerProximoNumeroDeViaje(Destino d) {
        return cantidadDeViajesPorDestino.getOrDefault(d, 0) + 1;
    }

    public Viaje crearViaje(String nombreViaje, Destino destino, int cantPasajeros, Transporte t) {
        if (destino == null) throw new IllegalArgumentException("Destino no existente");
        if (t == null) throw new IllegalArgumentException("Transporte no puede ser nulo");

        if (!t.estaDisponible()) {
            throw new ValidacionException("El transporte " + t.getPatente() + " ya no está disponible.");
        }
        if (!t.cumpleCondiciones(destino)) {
            throw new ValidacionException("El transporte " + t.getPatente() + " no cumple las condiciones para este destino.");
        }
        if (t.getCapacidadPasajeros() < cantPasajeros) {
            throw new ValidacionException("Capacidad excedida. Capacidad máxima: " + t.getCapacidadPasajeros());
        }

        cantViajesCreados++;
        Viaje nuevoViaje = destino.esLargaDistancia()
                ? new LargaDistancia(cantViajesCreados, nombreViaje, destino, cantPasajeros, t)
                : new CortaDistancia(cantViajesCreados, nombreViaje, destino, cantPasajeros, t);

        t.agregarViaje(nuevoViaje);
        viajesPorId.put(nuevoViaje.getIdViaje(), nuevoViaje);
        cantidadDeViajesPorDestino.put(destino, cantidadDeViajesPorDestino.getOrDefault(destino, 0) + 1);

        return nuevoViaje;
    }

    // #### MÉTODOS DE REPORTES Y COMPATIBILIDAD ####
    public List<ResponsableABordo> generarRankingResponsables() {
        return responsablesPorDni.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }
    public List<ResponsableABordo> GenerarRankingResponsables() { return generarRankingResponsables(); }

    public Map<Destino, Float> getReporteRecaudacionPorDestino() {
        return viajesPorId.values().stream()
                .filter(Viaje::estaFinalizado)
                .collect(Collectors.groupingBy(
                        Viaje::getDestinoDelViaje,
                        Collectors.reducing(0f, Viaje::calcularCostoFinal, Float::sum)
                ));
    }

    public Map<Transporte, Float> generarReporteRecaudadoPorTransporte() {
        return viajesPorId.values().stream()
                .filter(Viaje::estaFinalizado)
                .collect(Collectors.groupingBy(
                        Viaje::getTransporteAsignado,
                        Collectors.reducing(0f, Viaje::calcularCostoFinal, Float::sum)
                ));
    }
    public Map<Transporte, Float> GenerarReporteRecaudadoPorTransporte() { return generarReporteRecaudadoPorTransporte(); }

    // #### MÉTODO DE PERSISTENCIA Y DESERIALIZACIÓN ####
    public void relinkData() {
        System.out.println("Relinkeando datos cargados...");
        for (Transporte t : this.transportesPorPatente.values()) {
            if (t.getListaViajes() != null) {
                for (Viaje v : t.getListaViajes()) {
                    try {
                        v.setTransporteAsignado(t);
                        viajesPorId.put(v.getIdViaje(), v);
                    } catch (ValidacionException e) {
                        System.err.println("Error al relinkear viaje ID " + v.getIdViaje() + ": " + e.getMessage());
                    }
                }
            }
        }
        System.out.println("Relinkeo completo.");
    }
}