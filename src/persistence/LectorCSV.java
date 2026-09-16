package persistence;

import model.*;
import exceptions.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reemplaza a LectorJSON.
 * Carga los datos iniciales (catálogos) desde un archivo de texto
 * delimitado por comas (CSV), usando solo Java clásico.
 * (Cumple Requisito [23])
 */
public class LectorCSV {

    // Guarda los errores para el informe final
    private List<String> informeErrores = new ArrayList<>();

    public void cargarDatos(Agencia agencia, String rutaArchivo) throws IOException {
        // Usamos try-with-resources (cierra el archivo automáticamente)
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

            String linea;
            int nroLinea = 0;
            while ((linea = br.readLine()) != null) {
                nroLinea++;

                // Ignora líneas vacías o comentarios (#)
                if (linea.trim().isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                // Separa la línea por la coma
                String[] campos = linea.split(",");

                try {
                    String tipo = campos[0]; // El primer campo dice qué es

                    switch (tipo) {
                        case "DESTINO":
                            // Formato: DESTINO,Nombre,Kms
                            parsearDestino(agencia, campos);
                            break;

                        case "RESPONSABLE":
                            // Formato: RESPONSABLE,Nombre,Sueldo,DNI
                            parsearResponsable(agencia, campos);
                            break;

                        case "TRANSPORTE":
                            // Formato: TRANSPORTE,TIPO,Patente,Capacidad,Velocidad,...costos...
                            parsearTransporte(agencia, campos);
                            break;

                        default:
                            informeErrores.add("Línea " + nroLinea + ": Tipo desconocido '" + tipo + "'");
                    }
                } catch (Exception e) {
                    // Atrapa cualquier error en la línea (NumberFormat, IndexOutOfBounds, etc.)
                    informeErrores.add("Línea " + nroLinea + ": Error de formato o validación (" + e.getMessage() + ") -> " + linea);
                }
            }
        }

        // Imprime el informe de carga
        System.out.println("\n--- Informe de carga de datos (CSV) ---");
        if (informeErrores.isEmpty()) {
            System.out.println(">>> Todos los datos han sido cargados exitosamente.");
        } else {
            System.err.println("!!! Se encontraron " + informeErrores.size() + " errores al cargar (se omitieron los registros fallidos):");
            for (String error : informeErrores) {
                System.err.println("   - " + error);
            }
        }
    }

    // --- MÉTODOS PARSER PRIVADOS ---

    private void parsearDestino(Agencia agencia, String[] campos) throws ValidacionException, DestinoYaExisteException {
        // Formato: DESTINO,Nombre,Kms
        String nombreDestino = campos[1];
        float kms = Float.parseFloat(campos[2]);
        // El constructor de Destino valida los datos (km > 0, etc.)
        agencia.agregarDestino(new Destino(nombreDestino, kms));
    }

    private void parsearResponsable(Agencia agencia, String[] campos) {
        // Formato: RESPONSABLE,Nombre,Sueldo,DNI
        String nombreResp = campos[1];
        float sueldo = Float.parseFloat(campos[2]);
        String dni = campos[3];
        // Asumo que el constructor de Responsable no falla
        agencia.agregarResponsable(new ResponsableABordo(nombreResp, dni, sueldo));
    }

    private void parsearTransporte(Agencia agencia, String[] campos) throws ValidacionException{
        // Formato: TRANSPORTE,TIPO,Patente,Capacidad,Velocidad,...costos...
        String subTipo = campos[1];
        String patente = campos[2];
        int capacidad = Integer.parseInt(campos[3]);
        float velocidad = Float.parseFloat(campos[4]);

        switch (subTipo) {
            case "AUTO": {
                // AUTO,Patente,Cap(4),Vel,ValorBase,ValorKm
                float vBase = Float.parseFloat(campos[5]);
                float vKm = Float.parseFloat(campos[6]);
                agencia.agregarTransporte(new Auto(patente, velocidad, vBase, vKm));
                break;
            }
            case "COMBI": {
                // COMBI,Patente,Cap(16),Vel,ValorBase,ValorPasajeroKm
                float vBase = Float.parseFloat(campos[5]);
                float vPasKm = Float.parseFloat(campos[6]);
                agencia.agregarTransporte(new Combi(patente, velocidad, vBase, vPasKm));
                break;
            }
            // --- ¡EL CÓDIGO QUE FALTABA! ---
            case "SEMICAMA": {
                // SEMICAMA,Patente,Cap(40),Vel,ValorPasajeroKm
                float vPasKm = Float.parseFloat(campos[5]);
                agencia.agregarTransporte(new ColectivoSemiCama(patente, velocidad, vPasKm));
                break;
            }
            case "COCHECAMA": {
                // COCHECAMA,Patente,Cap(32),Vel,ValorPasajeroKm,ValorCamaKm
                float vPasKm = Float.parseFloat(campos[5]);
                float vCamaKm = Float.parseFloat(campos[6]);
                // (Asumo que el constructor de CocheCama es así, ¡verificalo!)
                agencia.agregarTransporte(new ColectivoCocheCama(patente, velocidad, vPasKm, vCamaKm));
                break;
            }
            default:
                throw new ValidacionException("Subtipo de transporte desconocido: " + subTipo);
        }
    }
}