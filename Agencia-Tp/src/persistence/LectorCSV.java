package persistence;

import model.*;
import exceptions.ValidacionException;
import exceptions.DestinoYaExisteException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV {

    public void cargarDatos(Agencia agencia, String rutaArchivo) {
        List<String> informeErrores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] partes = linea.split(";");
                String tipo = partes[0].trim();

                try {
                    switch (tipo.toLowerCase()) {
                        case "destino":
                            parsearDestino(agencia, partes);
                            break;
                        case "responsable":
                            parsearResponsable(agencia, partes);
                            break;
                        case "auto":
                        case "combi":
                        case "colectivosemicama":
                        case "colectivocochecama":
                            parsearTransporte(agencia, tipo, partes);
                            break;
                        default:
                            informeErrores.add("Tipo desconocido en CSV: " + tipo);
                    }
                } catch (Exception e) {
                    informeErrores.add("Error procesando línea [" + linea + "]: " + e.getMessage());
                }
            }

            System.out.println("\n--- Informe de carga CSV (" + rutaArchivo + ") ---");
            if (informeErrores.isEmpty()) {
                System.out.println(">>> Todos los datos del CSV fueron cargados con éxito.");
            } else {
                System.err.println("!!! Errores al cargar CSV:");
                for (String err : informeErrores) System.err.println(" - " + err);
            }

        } catch (IOException e) {
            System.err.println("Error al leer archivo CSV: " + e.getMessage());
        }
    }

    private void parsearDestino(Agencia agencia, String[] partes) {
        String nombre = partes[1].trim();
        float km = Float.parseFloat(partes[2].trim());
        agencia.agregarDestino(new Destino(nombre, km));
    }

    private void parsearResponsable(Agencia agencia, String[] partes) {
        String nombre = partes[1].trim();
        String dni = partes[2].trim();
        float sueldo = Float.parseFloat(partes[3].trim());
        agencia.agregarResponsable(new ResponsableABordo(nombre, dni, sueldo));
    }

    private void parsearTransporte(Agencia agencia, String tipo, String[] partes) {
        String patente = partes[1].trim();
        float vel = Float.parseFloat(partes[2].trim());
        float costo = Float.parseFloat(partes[3].trim());

        Transporte t = null;
        switch (tipo.toLowerCase()) {
            case "auto":
                float kmLitro = Float.parseFloat(partes[4].trim());
                t = new Auto(patente, vel, costo, kmLitro);
                break;
            case "combi":
                float costoPasajeros = Float.parseFloat(partes[4].trim());
                t = new Combi(patente, vel, costo, costoPasajeros);
                break;
            case "colectivosemicama":
                t = new ColectivoSemiCama(patente, vel, costo);
                break;
            case "colectivocochecama":
                t = new ColectivoCocheCama(patente, vel, costo);
                break;
        }
        if (t != null) agencia.agregarTransporte(t);
    }
}