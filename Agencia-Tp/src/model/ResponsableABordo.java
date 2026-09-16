package model;

import java.io.Serializable;
import java.util.Objects;

public class ResponsableABordo implements Comparable<ResponsableABordo>, Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String dni;
    private float sueldo;
    private boolean estaDisponible;
    private float cantKmAcumulados;

    public ResponsableABordo(String nombre, String dni, float sueldo) {
        this.nombre = nombre;
        this.dni = dni;
        this.sueldo = sueldo;
        this.estaDisponible = true;
        this.cantKmAcumulados = 0;
    }

    // #### GETTERS Y SETTERS ####
    public String getNombre() { return nombre; }
    public String GetNombre() { return getNombre(); }

    public String getDni() { return dni; }
    public String GetDni() { return getDni(); }

    public float getSueldo() { return sueldo; }
    public float GetSueldo() { return getSueldo(); }

    public boolean isEstaDisponible() { return estaDisponible; }
    public boolean GetEstaDisp() { return isEstaDisponible(); }

    public float getCantKmAcumulados() { return cantKmAcumulados; }

    // #### LÓGICA DE NEGOCIO ####
    public void ocupar() { this.estaDisponible = false; }
    public void Ocupar() { ocupar(); }

    public void liberar() { this.estaDisponible = true; }
    public void Liberar() { liberar(); }

    public void acumularKmRecorridos(float km) {
        if (km > 0) {
            this.cantKmAcumulados += km;
        }
    }
    public void AcumularKmRecorridos(float km) { acumularKmRecorridos(km); }

    @Override
    public int compareTo(ResponsableABordo o) {
        return Float.compare(o.cantKmAcumulados, this.cantKmAcumulados);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponsableABordo that = (ResponsableABordo) o;
        return Objects.equals(dni, that.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }
}