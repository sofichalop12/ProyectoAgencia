import controller.*;
import model.Agencia;
import persistence.LectorCSV;
import persistence.LectorJSON;
import persistence.PersistenciaClasica;
import view.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando sistema de Agencia...");

        PersistenciaClasica persistencia = new PersistenciaClasica();
        Agencia agencia = null;

        try {
            agencia = persistencia.cargarEstado();
        } catch (Exception e) {
            System.out.println("Main: 'agencia.dat' no encontrado o corrupto. Cargando datos iniciales...");
            agencia = Agencia.getInstance();

            // Carga de datos iniciales si no existe el .dat
            LectorJSON lectorJSON = new LectorJSON();
            lectorJSON.cargarDatos(agencia, "resources/datos_iniciales.json");
        }

        final Agencia agenciaFinal = agencia;

        // Guardar estado al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                persistencia.guardarEstado(agenciaFinal);
            } catch (Exception e) {
                System.err.println("Error al guardar estado al cerrar: " + e.getMessage());
            }
        }));

        // Iniciar GUI Swing
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}