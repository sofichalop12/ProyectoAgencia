package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Agencia;
import model.Transporte;
import model.Destino;
import model.ResponsableABordo;
import exceptions.ValidacionException;
import exceptions.DestinoYaExisteException;

// Volvemos a usar FileReader
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class LectorJSON {
    public void cargarDatos(Agencia agencia, String RutaArchivo) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Transporte.class, new TransporteDeserializer());
        Gson gson = gsonBuilder.create();

        List<String> informeErrores = new ArrayList<>();


        try (FileReader reader = new FileReader(RutaArchivo)) {

            LoteDatos lote = gson.fromJson(reader, LoteDatos.class);


            if (lote.getDestinos() != null) {
                for (Destino d : lote.getDestinos()) {
                    try {
                        agencia.agregarDestino(d);
                    } catch (DestinoYaExisteException | ValidacionException e) {
                        informeErrores.add("ERROR en Destino '" + d.getNombre() + "':" + e.getMessage());
                    }
                }
            }
            if (lote.getResponsables() != null) {
                for (ResponsableABordo r : lote.getResponsables()) {
                    try {
                        agencia.agregarResponsable(r);
                    } catch (Exception e) {
                        informeErrores.add("ERROR en Responsable '" + r.GetNombre() + "':" + e.getMessage());
                    }
                }
            }
            if (lote.getTransportes() != null) {
                for (Transporte t : lote.getTransportes()) {
                    try {
                        agencia.agregarTransporte(t);
                    } catch (Exception e) {
                        informeErrores.add("ERROR en Transporte '" + t.getPatente() + "': " + e.getMessage());
                    }
                }
            }


            System.out.println("\n--- Informe de carga de datos(" + RutaArchivo + ")---");
            if (informeErrores.isEmpty()) {
                System.out.println(">>>Todos los datos han sido cargados exitosamente.");
            } else {
                System.err.println("!!!Se encontraron " + informeErrores.size() + " errores al cargar(se omitieron los registros fallidos):");
                for (String error : informeErrores) {
                    System.err.println("   -" + error);
                }
            }

        } catch (IOException e) { // Archivo no encontrado
            System.err.println("Error FATAL: No se pudo encontrar el archivo JSON en " + RutaArchivo);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error FATAL : El archivo JSON esta mal formado o dañado.");
            e.printStackTrace();
        }
    }
}
