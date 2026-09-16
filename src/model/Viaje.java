package model;

import exceptions.ValidacionException;

import java.beans.DesignMode;
import java.io.Serializable;
import java.util.*;

;

public abstract class Viaje implements Comparable <Viaje>, Serializable {


    // #### DECLARACION DE VARIABLES ####
    private int idViaje; //Es clave primariaAutoincremental
    private String nombre; // El nombre es unico, del viaje del Destino y el Id
    private Destino destinoDelViaje;
    private static final long serialVersionUID = 1L;
    private int cantPasajeros; // Empieza en 1. Suponiendo que al crear el viaje es porque al menos un pasajero lo solicito
    private estado estadoActual;
    private  Set<ResponsableABordo>Responsables= new HashSet<>();
    private float avanceKmRecorridos;
    private transient Transporte TransporteAsignado;


    // CONSTRUCTOR
    public Viaje(int idVia, String nom, Destino destinoViaje,int cantPasajeros,Transporte t) {
        this.idViaje = idVia;
        this.nombre = nom;
        this.destinoDelViaje = destinoViaje;
        this.cantPasajeros = cantPasajeros;
        this.estadoActual = estado.PENDIENTE; // Estado inicial
        this.avanceKmRecorridos = 0;
        this.TransporteAsignado=t;
    }
    /*
    GETTERS
    Y
    SETTERS
    */

    public int getIdViaje(){
        return idViaje;
    }

    public String getNombre() {
        return nombre;
    }

    public Destino getDestinoDelViaje() {
        return destinoDelViaje;
    }

    public int getCantPasajeros() {
        return cantPasajeros;
    }

    public float getAvanceKmRecorridos() {
        return avanceKmRecorridos;
    }

    public Set<ResponsableABordo> getResponsables() {
        return Collections.unmodifiableSet(Responsables);
    }

    public Transporte getTransporteAsignado() {
        return TransporteAsignado;
    }


    @Override
    public int compareTo(Viaje o) {
        return nombre.compareTo(o.getNombre());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Viaje viaje = (Viaje) o;
        return idViaje == viaje.getIdViaje();
    }

    @Override
    public int hashCode() {
        return Objects.hash(idViaje);
    }


    public abstract float calcularCostoBase();
    public float calcularCostoFinal()
    {
        // Usa el nombre correcto del método abstracto de Transporte
        return calcularCostoBase() + TransporteAsignado.calculaCostoPorViaje(destinoDelViaje.getCantKm(),cantPasajeros);
    }


    public void iniciar() {
        if (estadoActual != estado.PENDIENTE)
            throw new IllegalStateException("El viaje ya fue iniciado o finalizado.");
        if (this.cantPasajeros <= 0)
            throw new IllegalStateException("No se puede iniciar un viaje con 0 pasajeros.");
        estadoActual = estado.EN_CURSO;
    }

    public void avanzarKm(float delta) {
        if (estadoActual != estado.EN_CURSO)
            throw new IllegalStateException("Solo se puede avanzar un viaje en curso.");
        if (delta <= 0)
            throw new IllegalArgumentException("La distancia debe ser positiva.");
        if (avanceKmRecorridos + delta > getKmTotales())
            throw new IllegalArgumentException("Los kilómetros acumulados no pueden superar los kilómetros totales del viaje (" + getKmTotales() + " km).");
        avanceKmRecorridos += delta;
    }

    public void finalizar() {
        if (estadoActual != estado.EN_CURSO)
            throw new IllegalStateException("Solo se puede finalizar un viaje en curso.");
        estadoActual = estado.FINALIZADO;
        liberarResponsables();
    }
    public estado getEstado(){return estadoActual;}


    public void liberarResponsables()
    {
        Iterator<ResponsableABordo> res=Responsables.iterator();
        while (res.hasNext())
        {
            ResponsableABordo r=res.next();
            r.AcumularKmRecorridos(avanceKmRecorridos);
            r.Liberar();
            res.remove();
        }
    }

    public void AgregarResponsable(ResponsableABordo r)
    {
        this.Responsables.add(r);
    }

    public void QuitarResponsable(ResponsableABordo r)
    {
        this.Responsables.remove(r);
    }

    public void AgregarPasajeros(int n)
    {
        if(TransporteAsignado!=null&&(cantPasajeros+n>TransporteAsignado.getCapacidadPasajeros())){
            throw new IllegalStateException("No hay capacidad disponible en el transporte.");
        }else{
            this.cantPasajeros+=n;
        }

    }
    public void AgregarUnPasajero()
    {
        this.AgregarPasajeros(1);
    }

    public void QuitarPasajeros()
    {
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

        // 2. Validar las restricciones del TIPO de viaje (Doble Dispatch)
        //this.validarRestriccionTransporte(transporte);

        this.TransporteAsignado = transporte;
    }
    public boolean estaPendiente() {
        return estadoActual == estado.PENDIENTE;
    }

    public boolean estaEnCurso() {
        return estadoActual == estado.EN_CURSO;
    }

    public boolean estaFinalizado() {
        return estadoActual == estado.FINALIZADO;
    }
    public float getKmTotales() {
        return destinoDelViaje.getCantKm();
    }

    public float getKmRestantes() {
        return getKmTotales() - avanceKmRecorridos;
    }

    public float getPorcentajeAvance() {
        float total = getKmTotales();
        return total <= 0 ? 0 : (avanceKmRecorridos / total) * 100f;
    }



}
