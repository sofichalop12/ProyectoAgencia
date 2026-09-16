package view.transportes;

import controller.TransporteController;
import exceptions.ValidacionException;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Set;

public class TransporteView extends JFrame {

    private JTable tablaTransporte;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear;
    private JButton btnCerrar;

    private TransporteController transporteController;

    public TransporteView() {
        super("Gestión de Transportes");

        transporteController = new TransporteController();

        initUI();
        cargarTransportes();
    }

    private void initUI() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(
                new Object[]{"Tipo", "Patente", "Capacidad", "Velocidad (km/h)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaTransporte = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaTransporte);

        JPanel panelBotones = new JPanel();
        btnCrear = new JButton("Crear Transporte");
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnCrear);
        panelBotones.add(btnCerrar);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnCrear.addActionListener(e -> new CrearTransporteDialog(this).setVisible(true));
        btnCerrar.addActionListener(e -> dispose());
    }

    public void cargarTransportes() {
        modeloTabla.setRowCount(0);

        Set<Transporte> transportes = Agencia.getInstance().getTransportes();

        for (Transporte t : transportes) {
            modeloTabla.addRow(new Object[]{
                    obtenerTipoTransporte(t),
                    t.getPatente(),
                    t.getCapacidadPasajeros(),
                    t.getVelocidadPromedioXhora()
            });
        }
    }

    private String obtenerTipoTransporte(Transporte t) {
        if (t instanceof Auto) return "Auto";
        if (t instanceof Combi) return "Combi";
        if (t instanceof ColectivoSemiCama) return "Colectivo Semi Cama";
        if (t instanceof ColectivoCocheCama) return "Colectivo Coche Cama";
        return t.getClass().getSimpleName();
    }
}
