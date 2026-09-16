package view.destinos;

import controller.DestinoController;
import model.Destino;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Set;

public class DestinoView extends JFrame {

    private DestinoController destinoController;
    private JTable tablaDestinos;
    private DefaultTableModel tablaModel;

    public DestinoView(DestinoController controller) {
        this.destinoController = controller;

        setTitle("Listado de Destinos");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // Tabla
        tablaModel = new DefaultTableModel(new Object[]{"Nombre", "Km"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // que no sea editable
            }
        };
        tablaDestinos = new JTable(tablaModel);
        JScrollPane scroll = new JScrollPane(tablaDestinos);
        add(scroll, BorderLayout.CENTER);

        // Panel inferior con botón Crear
        JPanel panelBoton = new JPanel();
        JButton btnCrear = new JButton("Crear nuevo destino");
        panelBoton.add(btnCrear);
        add(panelBoton, BorderLayout.SOUTH);

        btnCrear.addActionListener(e -> new CrearDestinoDialog(destinoController).setVisible(true));

        // Cargar datos
        cargarDestinos();
    }

    public void cargarDestinos() {
        tablaModel.setRowCount(0); // limpiar tabla
        try {
            Set<Destino> destinos = destinoController.listarDestinos();
            destinos.stream()
                    .sorted((d1, d2) -> d1.getNombre().compareToIgnoreCase(d2.getNombre()))
                    .forEach(d -> tablaModel.addRow(new Object[]{d.getNombre(), d.getCantKm()}));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar destinos: " + e.getMessage());
        }
    }
}