package view.viajes;

import controller.ResponsableController;
import controller.ViajeController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class AsignarResponsableView extends JFrame {

    private ViajeController viajeController;
    private ResponsableController responsableController;

    private JComboBox<Viaje> comboViajes;
    private JComboBox<ResponsableABordo> comboResponsables;

    private boolean modoQuitar; // false = asignar, true = quitar

    public AsignarResponsableView() {
        super("Asignar Responsable a Viaje");
        this.modoQuitar = false;
        this.viajeController = new ViajeController();
        this.responsableController = new ResponsableController();
        initUI();
    }

    public AsignarResponsableView(boolean modoQuitar) {
        super(modoQuitar ? "Quitar Responsable de Viaje" : "Asignar Responsable a Viaje");
        this.modoQuitar = modoQuitar;

        this.viajeController = new ViajeController();
        this.responsableController = new ResponsableController();

        initUI();
    }

    public void initUI() {
        setTitle(modoQuitar ? "Quitar Responsable de Viaje" : "Asignar Responsable a Viaje");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Combo de viajes
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Viaje:"), gbc);

        comboViajes = new JComboBox<>();
        comboViajes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Viaje) {
                    Viaje v = (Viaje) value;
                    setText(v.getDestinoDelViaje().getNombre() + " | " + v.getEstado());
                }
                return this;
            }
        });
        cargarViajes();
        if (comboViajes.getItemCount() > 0) comboViajes.setSelectedIndex(0); // seleccionar primero
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(comboViajes, gbc);

        // Combo de responsables
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel(modoQuitar ? "Responsable asignado:" : "Responsable disponible:"), gbc);

        comboResponsables = new JComboBox<>();
        comboResponsables.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ResponsableABordo) {
                    ResponsableABordo r = (ResponsableABordo) value;
                    setText(r.GetNombre() + " | " + r.GetDni());
                }
                return this;
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(comboResponsables, gbc);

        // Botón principal
        JButton btnEjecutar = new JButton(modoQuitar ? "Quitar" : "Asignar");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnEjecutar, gbc);

        add(panel, BorderLayout.CENTER);

        // Action listeners
        comboViajes.addActionListener(e -> cargarResponsables());
        btnEjecutar.addActionListener(e -> ejecutarAccion());

        cargarResponsables(); // cargar al inicio
        pack(); // ajusta la ventana al contenido
        setMinimumSize(new Dimension(450, 200));
    }

    public void cargarResponsables() {
        comboResponsables.removeAllItems();
        Agencia agencia = Agencia.getInstance();

        if (modoQuitar) {
            Viaje viajeSeleccionado = (Viaje) comboViajes.getSelectedItem();
            if (viajeSeleccionado != null) {
                for (ResponsableABordo r : viajeSeleccionado.getResponsables()) {
                    comboResponsables.addItem(r);
                }
            }
        } else {
            for (ResponsableABordo r : agencia.getResponsables()) {
                if (r.GetEstaDisp()) {
                    comboResponsables.addItem(r);
                }
            }
        }
    }


    public void cargarViajes() {
        comboViajes.removeAllItems();

        Agencia agencia = Agencia.getInstance();

        // Recorre todos los transportes y sus viajes
        for (Transporte t : agencia.getTransportes()) {
            for (Viaje v : t.getListaViajes()) {
                if (modoQuitar) {
                    if (v.estaPendiente()) {
                        comboViajes.addItem(v);
                    }
                } else {
                    if (!v.estaFinalizado()) {
                        comboViajes.addItem(v);
                    }
                }
            }
        }
    }

    public void ejecutarAccion() {

        Viaje viaje = (Viaje) comboViajes.getSelectedItem();
        ResponsableABordo responsable = (ResponsableABordo) comboResponsables.getSelectedItem();

        if (viaje == null || responsable == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un viaje y un responsable.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (modoQuitar) {
                viajeController.quitarResponsableDeViaje(viaje.getIdViaje(), responsable.GetDni());
                JOptionPane.showMessageDialog(this,
                        "Responsable quitado correctamente.");
            } else {
                viajeController.asignarResponsableAViaje(viaje.getIdViaje(), responsable.GetDni());
                JOptionPane.showMessageDialog(this,
                        "Responsable asignado correctamente.");
            }

            // Recargar combos
            cargarResponsables();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

