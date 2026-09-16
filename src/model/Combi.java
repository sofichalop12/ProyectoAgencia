package model;

import java.io.Serializable;
import java.util.Objects;

public class Combi extends Transporte implements Serializable {

    private float valorBasePorViaje;
    private float valorPorPasajeroPorKmRecorrido;
    private static final long serialVersionUID = 1L;

    public Combi(String patente, float velocidadPromedioXhora, float valorBasePorViaje, float valorPorPasajeroPorKmRecorrido) {
        super(patente, 16, velocidadPromedioXhora); //La capacidad la mando como parametro fijo?
        this.valorBasePorViaje = valorBasePorViaje;
        this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
    } //Tengo que cambiar los nombres como en c??

    public float getValorBasePorViaje() {
        return valorBasePorViaje;
    }

    public void setValorBasePorViaje(float valorBasePorViaje) {
        if (valorBasePorViaje>0)
            this.valorBasePorViaje = valorBasePorViaje;
    }

    public float getValorPorPasajeroPorKmRecorrido() {
        return valorPorPasajeroPorKmRecorrido;
    }

    public void setValorPorPasajeroPorKmRecorrido(float valorPorPasajeroPorKmRecorrido) {
        if(valorPorPasajeroPorKmRecorrido>0)
            this.valorPorPasajeroPorKmRecorrido = valorPorPasajeroPorKmRecorrido;
    }


    @Override
    public float calculaCostoPorViaje(float kms, int ps) {
        return valorBasePorViaje + (valorPorPasajeroPorKmRecorrido * ps * kms);
    }

    @Override
    public String toString() {
        return "Combi [Valor Base Por viaje:" + valorBasePorViaje + ",Valor por pasajero por km recorrido: " + valorPorPasajeroPorKmRecorrido + " ]";
    }

}