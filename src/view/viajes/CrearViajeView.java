package view.viajes;

import controller.DestinoController;
import controller.TransporteController;
import controller.ViajeController;
import exceptions.ValidacionException;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class CrearViajeView extends JFrame {

    private JComboBox<String> comboDestinos;
    private JComboBox<Transporte> comboTransportes;
    private JTextField txtCantPasajeros;
    private JLabel lblCapacidad;
    private JComboBox<ResponsableABordo> comboResponsables;
    private JLabel lblResponsable;
    private JButton btnCrear;

    private ViajeController viajeController;
    private DestinoController destinoController;
    private TransporteController transporteController;

    private boolean esLargaDistancia = false;

    public CrearViajeView(ViajeController vc, DestinoController dc, TransporteController tc) {
        super("Crear Viaje");
        this.viajeController = vc;
        this.destinoController = dc;
        this.transporteController = tc;

        initUI();
        cargarDestinos();
    }

    private void initUI() {
        setSize(520, 320);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        comboDestinos = new JComboBox<>();
        comboTransportes = new JComboBox<>();
        lblResponsable = new JLabel("Responsable a bordo:");
        comboResponsables = new JComboBox<>();
        comboResponsables.setEnabled(false);
        btnCrear = new JButton("Crear Viaje");

        // Renderer para comboTransportes: TIPO DE TRANSPORTE | PATENTE
        comboTransportes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Transporte) {
                    Transporte t = (Transporte) value;
                    setText(obtenerTipoTransporte(t) + " | " + t.getPatente());
                }
                return this;
            }
        });

        // Formato visual para mostrar responsable como: NOMBRE | DNI
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

        // Panel para pasajeros con indicador de capacidad máxima
        JPanel panelPasajeros = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        txtCantPasajeros = new JTextField(8);
        lblCapacidad = new JLabel("/ --");
        panelPasajeros.add(txtCantPasajeros);
        panelPasajeros.add(lblCapacidad);

        add(new JLabel("Destino:"));
        add(comboDestinos);

        add(new JLabel("Transporte:"));
        add(comboTransportes);

        add(new JLabel("Cantidad de pasajeros:"));
        add(panelPasajeros);

        add(lblResponsable);
        add(comboResponsables);

        add(new JLabel(""));
        add(btnCrear);

        // Listeners
        comboDestinos.addActionListener(e -> actualizarTransportes());
        comboTransportes.addActionListener(e -> actualizarCapacidad());
        btnCrear.addActionListener(e -> crearViaje());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void cargarDestinos() {
        try {
            Set<Destino> destinos = destinoController.listarDestinos();
            comboDestinos.removeAllItems();
            for (Destino d : destinos) {
                comboDestinos.addItem(d.getNombre());
            }
            if (comboDestinos.getItemCount() > 0) {
                comboDestinos.setSelectedIndex(0);
                actualizarTransportes();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando destinos: " + e.getMessage());
        }
    }

    private void cargarResponsablesDisponibles() {
        comboResponsables.removeAllItems();
        Set<ResponsableABordo> responsables = Agencia.getInstance().getResponsables();
        for (ResponsableABordo r : responsables) {
            if (r.GetEstaDisp()) {
                comboResponsables.addItem(r);
            }
        }
    }

    private void actualizarTransportes() {
        try {
            String nombreDestino = (String) comboDestinos.getSelectedItem();
            Destino destino = destinoController.buscarDestinoPorNombre(nombreDestino);
            comboTransportes.removeAllItems();

            esLargaDistancia = destino.getCantKm() > 100;
            comboResponsables.setEnabled(esLargaDistancia);

            if (esLargaDistancia) {
                cargarResponsablesDisponibles();
            } else {
                comboResponsables.removeAllItems();
            }

            Set<Transporte> transportes = Agencia.getInstance().getTransportes();
            for (Transporte t : transportes) {
                String clase = t.getClass().getSimpleName();
                if (esLargaDistancia && clase.equals("Auto")) continue; // No autos en larga distancia
                if (!esLargaDistancia && clase.equals("ColectivoCocheCama")) continue; // No coche cama en corta distancia
                comboTransportes.addItem(t);
            }
            if (comboTransportes.getItemCount() > 0) {
                comboTransportes.setSelectedIndex(0);
            }
            actualizarCapacidad();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error filtrando transportes: " + e.getMessage());
        }
    }

    private void actualizarCapacidad() {
        Transporte t = (Transporte) comboTransportes.getSelectedItem();
        if (t != null) {
            lblCapacidad.setText("/ " + t.getCapacidadPasajeros());
        } else {
            lblCapacidad.setText("/ --");
        }
    }

    private String obtenerTipoTransporte(Transporte t) {
        if (t instanceof Auto) return "Auto";
        if (t instanceof Combi) return "Combi";
        if (t instanceof ColectivoSemiCama) return "Colectivo Semi Cama";
        if (t instanceof ColectivoCocheCama) return "Colectivo Coche Cama";
        return t.getClass().getSimpleName();
    }

    private void crearViaje() {
        try {
            String destino = (String) comboDestinos.getSelectedItem();
            Transporte transporteSeleccionado = (Transporte) comboTransportes.getSelectedItem();
            if (transporteSeleccionado == null) {
                throw new ValidacionException("Debe seleccionar un transporte.");
            }
            String patente = transporteSeleccionado.getPatente();
            int cantPasajeros = Integer.parseInt(txtCantPasajeros.getText().trim());

            ResponsableABordo responsableSeleccionado = null;
            if (esLargaDistancia) {
                responsableSeleccionado = (ResponsableABordo) comboResponsables.getSelectedItem();
                if (responsableSeleccionado == null) {
                    throw new ValidacionException("Debe seleccionar un responsable disponible para el viaje de larga distancia.");
                }
            }

            Viaje viaje = viajeController.crearViaje(destino, patente, cantPasajeros);

            if (esLargaDistancia && responsableSeleccionado != null) {
                viajeController.asignarResponsableAViaje(viaje.getIdViaje(), responsableSeleccionado.GetDni());
            }

            JOptionPane.showMessageDialog(this, "Viaje creado correctamente: " + viaje.getNombre());
            dispose();

        } catch (ValidacionException ve) {
            JOptionPane.showMessageDialog(this, "Error: " + ve.getMessage());
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido de pasajeros.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear viaje: " + ex.getMessage());
        }
    }
}
