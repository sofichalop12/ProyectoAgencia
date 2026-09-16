package model;

import exceptions.ValidacionException;

import java.io.Serializable;


public class LargaDistancia extends Viaje implements Serializable {

    public LargaDistancia (int idVia, String nom, Destino destinoViaje,int cantp,Transporte t){
        super(idVia,nom,destinoViaje,cantp,t);
    }

    @Override
    public float calcularCostoBase() {
        float Sueldo=0;

        for(ResponsableABordo r: this.getResponsables()){
            Sueldo+=r.GetSueldo();
        }
        return Sueldo;
    }
    @Override
    public String toString(){
        return getNombre() + " [" + getEstado() + "] (Larga Distancia)";
    }
}
