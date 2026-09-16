package view.responsables;

import controller.ResponsableController;
import model.Agencia;
import model.ResponsableABordo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Set;

public class ResponsableView extends JFrame {

    private ResponsableController responsableController;
    private JTable tablaResponsables;
    private DefaultTableModel tablaModel;

    public ResponsableView(ResponsableController controller) {
        this.responsableController = controller;

        setTitle("Listado de Responsables a Bordo");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tabla
        tablaModel = new DefaultTableModel(new Object[]{"Nombre", "DNI", "Sueldo por viaje", "Disponible"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaResponsables = new JTable(tablaModel);
        JScrollPane scroll = new JScrollPane(tablaResponsables);
        add(scroll, BorderLayout.CENTER);

        // Panel inferior con botón Crear
        JPanel panelBoton = new JPanel();
        JButton btnCrear = new JButton("Crear nuevo responsable");
        panelBoton.add(btnCrear);
        add(panelBoton, BorderLayout.SOUTH);

        btnCrear.addActionListener(e -> new CrearResponsableDialog(responsableController).setVisible(true));

        // Cargar datos
        cargarResponsables();
    }

    public void cargarResponsables() {
        tablaModel.setRowCount(0); // limpiar tabla
        try {
            Set<ResponsableABordo> responsables = Agencia.getInstance().getResponsables();
            System.out.println(responsables.toString());
            for (ResponsableABordo r : responsables) {
                tablaModel.addRow(new Object[]{
                        r.GetNombre(),
                        r.GetDni(),
                        r.GetSueldo(),
                        r.GetEstaDisp() ? "Sí" : "No"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar responsables: " + e.getMessage());
        }
    }
}
