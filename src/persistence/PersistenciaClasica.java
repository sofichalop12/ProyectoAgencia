package persistence;

import model.Agencia;
import java.io.*;

/**
 * Esta clase usa Serialización Clásica de Java (.dat)
 * para guardar y cargar el objeto 'Agencia' completo.
 *
 * Maneja los bucles infinitos (Transporte <-> Viaje y Responsable <-> Viaje)
 * trabajando junto con los campos 'transient' y el método 'relinkData()' de Agencia.
 */
public class PersistenciaClasica {

    // El archivo donde se guarda el estado
    private String archivo = "agencia.dat";

    /**
     * Guarda el estado completo de la Agencia.
     * Esto debe llamarse al cerrar la aplicación.
     */
    public void guardarEstado(Agencia agencia) throws IOException {

        // Usamos try-with-resources para asegurar que los flujos se cierren
        try (FileOutputStream fileOut = new FileOutputStream(this.archivo);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            // Escribe el objeto Agencia (con los campos 'transient' omitidos)
            out.writeObject(agencia);
            System.out.println(">>> Estado de la agencia guardado en " + this.archivo);

        } catch (IOException e) {
            System.err.println("Error FATAL al guardar el estado: " + e.getClass().getSimpleName()
                    + " - " + e.getMessage());
            throw e; // Lanza el error al Main/Controlador
        }
    }

    /**
     * Carga el estado completo de la Agencia desde el archivo .dat.
     * Además de devolverla, ACTUALIZA el singleton de Agencia.
     *
     * @return La instancia de Agencia guardada y "relinkeada".
     * @throws IOException Si el archivo no se encuentra o está corrupto.
     * @throws ClassNotFoundException Si las clases del modelo cambiaron.
     */
    public Agencia cargarEstado() throws IOException, ClassNotFoundException {

        File f = new File(this.archivo);
        if (!f.exists()) {
            // Esto es normal en el primer inicio, no un error fatal.
            System.out.println("INFO: No se encontró archivo de guardado '" + this.archivo + "'.");
            throw new FileNotFoundException(this.archivo);
        }

        try (FileInputStream fileIn = new FileInputStream(f);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            // 1. Lee el objeto desde el disco
            Agencia agencia = (Agencia) in.readObject();

            // 2. Arreglamos los enlaces 'transient' que quedaron en null
            if (agencia != null) {
                agencia.relinkData();        // llamada crítica
                Agencia.setInstance(agencia); // *** ACTUALIZA EL SINGLETON ***
            }

            System.out.println(">>> Estado de la agencia cargado desde " + this.archivo);
            return agencia;

        } catch (FileNotFoundException e) {
            // Ya lo chequeamos arriba, pero lo dejo por claridad
            System.out.println("INFO: No se encontró archivo de guardado '" + this.archivo + "'.");
            throw e;

        } catch (IOException e) {
            System.err.println("ERROR al leer " + this.archivo + ": "
                    + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Clase del modelo cambió o no se encuentra: " + e.getMessage());
            throw e;
        }
    }
}
