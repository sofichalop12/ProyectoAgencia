package view.viajes;

import controller.ViajeController;
import model.*;

import javax.swing.*;
import java.awt.*;

public class AsignarResponsableView extends JFrame {

    private ViajeController viajeController;

    private JComboBox<Viaje> comboViajes;
    private JComboBox<ResponsableABordo> comboResponsables;

    private boolean modoQuitar;

    public AsignarResponsableView() {
        super("Asignar Responsable a Viaje");
        this.modoQuitar = false;
        this.viajeController = new ViajeController();
        initUI();
    }

    public AsignarResponsableView(boolean modoQuitar) {
        super(modoQuitar ? "Quitar Responsable de Viaje" : "Asignar Responsable a Viaje");
        this.modoQuitar = modoQuitar;
        this.viajeController = new ViajeController();
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
        if (comboViajes.getItemCount() > 0) comboViajes.setSelectedIndex(0);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(comboViajes, gbc);

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
                    setText(r.getNombre() + " | " + r.getDni());
                }
                return this;
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(comboResponsables, gbc);

        JButton btnEjecutar = new JButton(modoQuitar ? "Quitar" : "Asignar");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnEjecutar, gbc);

        add(panel, BorderLayout.CENTER);

        comboViajes.addActionListener(e -> cargarResponsables());
        btnEjecutar.addActionListener(e -> ejecutarAccion());

        cargarResponsables();
        pack();
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
                if (r.isEstaDisponible()) {
                    comboResponsables.addItem(r);
                }
            }
        }
    }

    public void cargarViajes() {
        comboViajes.removeAllItems();

        Agencia agencia = Agencia.getInstance();

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
                viajeController.quitarResponsableDeViaje(viaje.getIdViaje(), responsable.getDni());
                JOptionPane.showMessageDialog(this, "Responsable quitado correctamente.");
            } else {
                viajeController.asignarResponsableAViaje(viaje.getIdViaje(), responsable.getDni());
                JOptionPane.showMessageDialog(this, "Responsable asignado correctamente.");
            }

            cargarResponsables();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}