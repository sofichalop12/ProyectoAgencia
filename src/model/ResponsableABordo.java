package model;

import java.lang.*;
import java.io.Serializable;
import java.util.Objects;

public class ResponsableABordo implements Comparable<ResponsableABordo>, Serializable {
    private String nombre;
    private Boolean EstaDisp; //S o N
    private String Dni;
    private float SueldoXViaje;
    private static final long serialVersionUID = 1L;
    private float CantKmAcumulados;


    public ResponsableABordo(String nombre, String Dni, float SueldoXViaje){
        this.nombre = nombre;
        this.EstaDisp = true;
        this.Dni = Dni;
        this.SueldoXViaje = SueldoXViaje;
        CantKmAcumulados=0;
    }
    public String GetNombre(){
        return nombre;
    }
    public Boolean GetEstaDisp(){
        return EstaDisp;
    }
    public String GetDni(){
        return Dni;
    }
    public float GetSueldo(){
        return SueldoXViaje;
    }
    public float getCantKmAcumulados(){return CantKmAcumulados;}


    public void AcumularKmRecorridos(float km)
    {
        CantKmAcumulados+=km;
    }

    public void Ocupar() { //unico set que podriamos llegar a necesitar, dar de baja o no el responsable
        EstaDisp = false;
    }
    public void Liberar(){
        EstaDisp=true;
    }

    @Override
    public int compareTo(ResponsableABordo otro){
        return Float.compare(otro.getCantKmAcumulados(), this.getCantKmAcumulados()); //devuelve 0 si son iguales,-1 o 1
    }
    @Override
    public boolean equals(Object o){
        if(this == o) //son el mismo objeto en memoria?
            return true;
        if(o == null || getClass() != o.getClass()) // o el objeto es nulo o son de clases distintas
            return false;
        ResponsableABordo that  = (ResponsableABordo) o; //casteo necesario para usar una variable de tipo clase
        return Objects.equals(this.Dni,that.GetDni());
    }
    @Override
    public int hashCode(){
        return Objects.hash(Dni);
    }
    @Override
    public String toString(){
        return "Responsable [Nombre: " + nombre + ", DNI: " + Dni + ", Disponible: " + EstaDisp + "]";
    }
}