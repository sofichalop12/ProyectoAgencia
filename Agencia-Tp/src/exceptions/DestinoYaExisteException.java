package exceptions;

public class DestinoYaExisteException extends RuntimeException {
    public DestinoYaExisteException(String NombreDestino){
        super("El Destino "+ NombreDestino+" Ya existe en la agencia");
    }
}
