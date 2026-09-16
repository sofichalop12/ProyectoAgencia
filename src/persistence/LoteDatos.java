package persistence;

import model.Transporte;
import model.ResponsableABordo;
import model.Destino;
import java.util.List;

public class LoteDatos {
    private List<Destino> destinos;
    private List<Transporte> transportes;
    private List<ResponsableABordo> responsables;

    public List<Destino> getDestinos() { return destinos; }
    public List<ResponsableABordo> getResponsables() { return responsables; }
    public List<Transporte> getTransportes() { return transportes; }
}