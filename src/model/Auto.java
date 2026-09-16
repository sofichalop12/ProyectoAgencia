package model;

import java.io.Serializable;
import java.util.Objects;

public class Auto extends Transporte implements Serializable {
    private float ValorBasePorViaje;
    private float ValorPorKmRecorrido;
    private static final long serialVersionUID = 1L;

    public Auto (String patente, float velocidadPromedioXhora, float _ValorBasePorViaje, float _ValorPorKmRecorrido){
        super(patente,  4, velocidadPromedioXhora);
        this.ValorBasePorViaje = _ValorBasePorViaje;
        this.ValorPorKmRecorrido = _ValorPorKmRecorrido;
    } //Constructor tengo que cambiar el nombre de las variables como en c?

    public float  getValorBasePorViaje(){ return ValorBasePorViaje; }

    public void setValorBasePorViaje(float ValorBasePorViaje){
        if(ValorBasePorViaje>0){
            this.ValorBasePorViaje=ValorBasePorViaje;
        }else {
            this.ValorBasePorViaje=0; //Tiene que ir?
            }
    }
    public float  getValorPorKmRecorrido() { return ValorPorKmRecorrido; }

    public void setValorPorKmRecorrido(float ValorPorKmRecorrido){
        if(ValorPorKmRecorrido>0)
            this.ValorPorKmRecorrido=ValorPorKmRecorrido;
        }

    @Override
    public float calculaCostoPorViaje(float kilometros,int pasajeros){
        return this.ValorBasePorViaje + (ValorPorKmRecorrido * kilometros);
    }

    @Override
    public String toString() {
    return "Auto [Valor Base Por viaje:" + ValorBasePorViaje + ",Valor por km recorrido: " + ValorPorKmRecorrido + " ]";
    }

    @Override
    public boolean cumpleCondiciones(Destino d){
        return !(d.esLargaDistancia());
    }
}