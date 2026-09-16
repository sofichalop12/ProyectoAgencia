package view.responsables;

import controller.ResponsableController;
import exceptions.ValidacionException;

import javax.swing.*;
import java.awt.*;

public class CrearResponsableDialog extends JDialog {

    private ResponsableController responsableController;
    private JTextField txtNombre;
    private JTextField txtDni;
    private JTextField txtSueldo;

    public CrearResponsableDialog(ResponsableController controller) {
        this.responsableController = controller;

        setTitle("Crear Responsable a Bordo");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblNombre, gbc);

        txtNombre = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(txtNombre, gbc);

        // DNI
        JLabel lblDni = new JLabel("DNI:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblDni, gbc);

        txtDni = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtDni, gbc);

        // Sueldo por viaje
        JLabel lblSueldo = new JLabel("Sueldo por viaje:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblSueldo, gbc);

        txtSueldo = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtSueldo, gbc);

        // Botón Crear
        JButton btnCrear = new JButton("Crear");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(btnCrear, gbc);

        btnCrear.addActionListener(e -> crearResponsable());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void crearResponsable() {
        String nombre = txtNombre.getText().trim();
        String dni = txtDni.getText().trim();
        String sueldoStr = txtSueldo.getText().trim();

        try {
            if (nombre.isEmpty() || dni.isEmpty() || sueldoStr.isEmpty()) {
                throw new ValidacionException("Todos los campos son obligatorios.");
            }

            float sueldo = Float.parseFloat(sueldoStr);

            responsableController.crearResponsable(dni, nombre, sueldo);

            JOptionPane.showMessageDialog(this, "Responsable creado correctamente.");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Sueldo debe ser un número válido.");
        } catch (ValidacionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}