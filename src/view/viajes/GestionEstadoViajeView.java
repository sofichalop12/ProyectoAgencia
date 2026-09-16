package view.viajes;

import controller.ViajeController;
import exceptions.ValidacionException;
import model.Agencia;
import model.Transporte;
import model.Viaje;
import model.estado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestionEstadoViajeView extends JFrame {

    private JTable tablaViajes;
    private DefaultTableModel modeloTabla;
    private List<Viaje> viajesVisibles;

    private JTextField txtKm;
    private JButton btnIniciar;
    private JButton btnAvanzar;
    private JButton btnFinalizar;

    private ViajeController viajeController;

    public GestionEstadoViajeView() {
        super("Gestión de Estado del Viaje");
        viajeController = new ViajeController();
        viajesVisibles = new ArrayList<>();

        initUI();
        cargarViajes();
        setVisible(true);
    }

    private void initUI() {
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Tabla central con los viajes existentes
        modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Destino", "Estado", "Km avanzados / Km totales"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaViajes = new JTable(modeloTabla);
        tablaViajes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaViajes);
        add(scroll, BorderLayout.CENTER);

        // Panel inferior con controles
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnIniciar = new JButton("Iniciar Viaje");
        txtKm = new JTextField(6);
        btnAvanzar = new JButton("Avanzar Km");
        btnFinalizar = new JButton("Finalizar Viaje");

        panelInferior.add(btnIniciar);
        panelInferior.add(new JLabel("Km:"));
        panelInferior.add(txtKm);
        panelInferior.add(btnAvanzar);
        panelInferior.add(btnFinalizar);

        add(panelInferior, BorderLayout.SOUTH);

        configurarEventos();
    }

    public void cargarViajes() {
        modeloTabla.setRowCount(0);
        viajesVisibles.clear();

        Agencia agencia = Agencia.getInstance();
        for (Transporte t : agencia.getTransportes()) {
            for (Viaje v : t.getListaViajes()) {
                if (v.getEstado() != estado.FINALIZADO) {
                    viajesVisibles.add(v);
                }
            }
        }

        viajesVisibles.sort(Comparator.comparing(Viaje::getEstado).thenComparing(Viaje::getNombre));

        for (Viaje v : viajesVisibles) {
            modeloTabla.addRow(new Object[]{
                    v.getNombre(),
                    v.getDestinoDelViaje().getNombre(),
                    v.getEstado(),
                    v.getAvanceKmRecorridos() + " / " + v.getKmTotales()
            });
        }
    }

    private Viaje getViajeSeleccionado() {
        int fila = tablaViajes.getSelectedRow();
        if (fila < 0 || fila >= viajesVisibles.size()) {
            mostrarError("Debe seleccionar un viaje de la tabla.");
            return null;
        }
        return viajesVisibles.get(fila);
    }

    private void configurarEventos() {

        // INICIAR VIAJE
        btnIniciar.addActionListener(e -> {
            Viaje v = getViajeSeleccionado();
            if (v == null) return;

            try {
                viajeController.iniciarViaje(v.getIdViaje());
                int filaSel = tablaViajes.getSelectedRow();
                cargarViajes();
                if (filaSel >= 0 && filaSel < tablaViajes.getRowCount()) {
                    tablaViajes.setRowSelectionInterval(filaSel, filaSel);
                }

                JOptionPane.showMessageDialog(this,
                        "Viaje iniciado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (ValidacionException ex) {
                mostrarError(ex.getMessage());
            }
        });

        // AVANZAR VIAJE
        btnAvanzar.addActionListener(e -> {
            Viaje v = getViajeSeleccionado();
            if (v == null) return;

            try {
                String textoKm = txtKm.getText().trim();
                if (textoKm.isEmpty()) {
                    mostrarError("Debe ingresar los kilómetros a avanzar.");
                    return;
                }
                float km = Float.parseFloat(textoKm);

                viajeController.avanzarViaje(v.getIdViaje(), km);
                int filaSel = tablaViajes.getSelectedRow();
                cargarViajes();
                if (filaSel >= 0 && filaSel < tablaViajes.getRowCount()) {
                    tablaViajes.setRowSelectionInterval(filaSel, filaSel);
                }
                txtKm.setText("");

                JOptionPane.showMessageDialog(this,
                        "Se avanzó el viaje " + km + " km.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                mostrarError("Los KM deben ser numéricos.");
            } catch (ValidacionException ex) {
                mostrarError(ex.getMessage());
            }
        });

        // FINALIZAR VIAJE
        btnFinalizar.addActionListener(e -> {
            Viaje v = getViajeSeleccionado();
            if (v == null) return;

            try {
                viajeController.finalizarViaje(v.getIdViaje());
                int filaSel = tablaViajes.getSelectedRow();
                cargarViajes();
                if (filaSel >= 0 && filaSel < tablaViajes.getRowCount()) {
                    tablaViajes.setRowSelectionInterval(filaSel, filaSel);
                }

                JOptionPane.showMessageDialog(this,
                        "Viaje finalizado con éxito.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (ValidacionException ex) {
                mostrarError(ex.getMessage());
            }
        });
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}