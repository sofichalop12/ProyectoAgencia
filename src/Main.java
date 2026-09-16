import model.Agencia;
import view.MainWindow;             // Tu clase de Ventana principal
import persistence.LectorCSV;         // El lector de CSV (para carga inicial)
import persistence.PersistenciaClasica; // El lector/escritor de .dat (para estado)
import javax.swing.*;
import java.io.IOException; // Para manejar errores de persistencia

/**
 * Clase principal que inicia la aplicación, maneja la carga de datos
 * (ya sea desde un estado guardado o desde la carga inicial)
 * y configura el guardado al cerrar.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("Iniciando sistema de Agencia...");

        // 1. Preparamos las clases de persistencia
        PersistenciaClasica persistenciaEstado = new PersistenciaClasica();
        Agencia agencia= Agencia.getInstance();;

        // --- 2. LÓGICA DE ARRANQUE ---
        try {
            // Intentamos cargar el estado guardado (agencia.dat)
            agencia = persistenciaEstado.cargarEstado();
            System.out.println("Main: Estado cargado exitosamente desde 'agencia.dat'.");

        } catch (Exception e) { // Abarca FileNotFound, IOException, ClassNotFound

            System.out.println("Main: 'agencia.dat' no encontrado o corrupto. Cargando datos iniciales desde CSV...");

            // Si falla, obtenemos una instancia nueva y vacía


            // Y la poblamos con la carga INICIAL (LectorCSV)
            LectorCSV lectorInicial = new LectorCSV();
            try {
                // (Asegurate que tu archivo CSV esté en esta ruta)
                lectorInicial.cargarDatos(agencia, "resources/datos_iniciales.csv");
                System.out.println("Main: Da    tos iniciales (CSV) cargados.");
            } catch (Exception ex) {
                System.err.println("Main: ERROR FATAL al cargar CSV: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        // 3. Inyectamos la instancia (cargada o nueva) en el Singleton
        // (Esto asume que tenés el 'setInstancia' en la clase Agencia)


        // --- 4. INICIAR LA VISTA (UI) ---
        System.out.println("Sistema listo. Iniciando UI...");

        // Este es el patrón de Swing para iniciar la UI de forma segura
        SwingUtilities.invokeLater(() -> {

            // 4a. Creamos tu ventana principal
            MainWindow vista = new MainWindow();

            // 4b. AGREGAMOS EL "GANCHO" DE GUARDADO AL CERRAR
            vista.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    try {
                        // Llama a guardarEstado (Clásica) con la instancia actual
                        persistenciaEstado.guardarEstado(Agencia.getInstance());
                        System.out.println("Main: Estado guardado en 'agencia.dat'. Adiós.");
                    } catch (Exception e) {
                        System.err.println("Main: ERROR CRÍTICO AL GUARDAR: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

            // 4c. La hacemos visible
            vista.setVisible(true);
        });
    }
}