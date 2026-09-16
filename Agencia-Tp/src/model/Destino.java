package model;
import exceptions.ValidacionException;


import java.io.Serializable;

import java.util.Objects;

public class Destino implements Comparable <Destino>, Serializable {
    private String Nombre;
    private float CantKm;
    private static final long serialVersionUID = 1L;

    public Destino(String Nom, float Ckm) throws ValidacionException {
        //Valida datos
        if (Ckm <= 0) {
            throw new ValidacionException("Los kilómetros deben ser un valor positivo.");
        }
        if (Nom == null || Nom.trim().isEmpty()) {
            throw new ValidacionException("El nombre no puede estar vacío.");
        }

        // Si pasa, se crea el objeto
        this.Nombre = Nom;
        this.CantKm = Ckm;
    }

    public String getNombre() {
        return Nombre;
    }
    public float getCantKm() {
        return CantKm;
    }
    
    @Override
    public String toString() {
        return Nombre;
    }

    @Override
    public int compareTo(Destino otro)
    {
        return Nombre.compareTo(otro.getNombre());
    }

    @Override
    public boolean equals(Object o){
        if(this == o) //son el mismo objeto en memoria?
            return true;
        if(o == null || getClass() != o.getClass()) // o el objeto es nulo o son de clases distintas
            return false;
        Destino that  = (Destino) o; //casteo necesario para usar una variable de tipo clase
        return Objects.equals(this.Nombre,that.getNombre());
    }

    public boolean esLargaDistancia(){
        return this.CantKm>100;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.Nombre);
    }
}
