package view.transportes;

import model.*;
import exceptions.ValidacionException;
import javax.swing.*;
import java.awt.*;

public class CrearTransporteDialog extends JDialog {

    private JTextField txtPatente;
    private JTextField txtVelocidad;
    private JTextField txtValor1;
    private JTextField txtValor2;
    private JComboBox<String> comboTipo;

    private TransporteView parent;

    public CrearTransporteDialog(TransporteView parent) {
        super(parent, "Crear Transporte", true);
        this.parent = parent;

        initUI();
    }

    private void initUI() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setLayout(new GridLayout(6, 2, 5, 5));

        // Tipo de transporte
        add(new JLabel("Tipo de Transporte:"));
        comboTipo = new JComboBox<>(new String[]{
                "Auto", "Combi", "Colectivo SemiCama", "Colectivo CocheCama"
        });
        add(comboTipo);

        add(new JLabel("Patente:"));
        txtPatente = new JTextField();
        add(txtPatente);

        add(new JLabel("Velocidad promedio (km/h):"));
        txtVelocidad = new JTextField();
        add(txtVelocidad);

        add(new JLabel("Valor base / Valor x pasajero:"));
        txtValor1 = new JTextField();
        add(txtValor1);

        add(new JLabel("Valor adicional / Valor plaza cama (solo CocheCama)"));
        txtValor2 = new JTextField();
        add(txtValor2);

        JButton btnCrear = new JButton("Crear");
        JButton btnCancelar = new JButton("Cancelar");
        add(btnCrear);
        add(btnCancelar);

        // Acción del botón Crear
        btnCrear.addActionListener(e -> {
            try {
                String tipo = (String) comboTipo.getSelectedItem();
                String patente = txtPatente.getText().trim();
                float velocidad = Float.parseFloat(txtVelocidad.getText().trim());
                float valor1 = txtValor1.getText().isEmpty() ? 0 : Float.parseFloat(txtValor1.getText().trim());
                float valor2 = txtValor2.getText().isEmpty() ? 0 : Float.parseFloat(txtValor2.getText().trim());

                Transporte nuevo = null;

                switch (tipo) {
                    case "Auto":
                        nuevo = new Auto(patente, velocidad, valor1, valor2);
                        break;
                    case "Combi":
                        nuevo = new Combi(patente, velocidad, valor1, valor2);
                        break;
                    case "Colectivo SemiCama":
                        nuevo = new ColectivoSemiCama(patente, velocidad, valor1);
                        break;
                    case "Colectivo CocheCama":
                        nuevo = new ColectivoCocheCama(patente, velocidad, valor1, valor2);
                        break;
                }

                Agencia.getInstance().agregarTransporte(nuevo);
                JOptionPane.showMessageDialog(this, "Transporte creado exitosamente.");
                parent.cargarTransportes();
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valores numéricos incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ValidacionException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }
}
