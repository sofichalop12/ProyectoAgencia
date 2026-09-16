package view.destinos;

import controller.DestinoController;
import exceptions.ValidacionException;

import javax.swing.*;
import java.awt.*;

public class CrearDestinoDialog extends JDialog {

    private DestinoController destinoController;
    private JTextField txtNombre;
    private JTextField txtKm;

    public CrearDestinoDialog(DestinoController controller) {
        this.destinoController = controller;

        setTitle("Crear Destino");
        setSize(400, 200);
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

        // Km
        JLabel lblKm = new JLabel("Kilómetros:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblKm, gbc);

        txtKm = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtKm, gbc);

        // Botón Crear
        JButton btnCrear = new JButton("Crear");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(btnCrear, gbc);

        btnCrear.addActionListener(e -> crearDestino());

        // Cerrar al pulsar X
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void crearDestino() {
        String nombre = txtNombre.getText().trim();
        String kmStr = txtKm.getText().trim();

        try {
            if (nombre.isEmpty() || kmStr.isEmpty()) {
                throw new ValidacionException("Todos los campos son obligatorios.");
            }

            int km = Integer.parseInt(kmStr);

            destinoController.crearDestino(nombre, km);

            JOptionPane.showMessageDialog(this, "Destino creado correctamente.");
            dispose(); // cerrar diálogo al crear

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kilómetros debe ser un número válido.");
        } catch (ValidacionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
