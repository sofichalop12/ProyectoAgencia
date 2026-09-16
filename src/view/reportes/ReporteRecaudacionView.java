package view.reportes;

import controller.AgenciaController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ReporteRecaudacionView extends JFrame {
    private JTextArea txtArea;
    private JButton btnExportar;
    private AgenciaController agenciaController;

    public ReporteRecaudacionView(String tipoReporte) {
        super("Reporte: " + tipoReporte);
        agenciaController = new AgenciaController();
        initUI(tipoReporte);
    }

    private void initUI(String tipoReporte) {
        setSize(500, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        txtArea = new JTextArea();
        txtArea.setEditable(false);
        add(new JScrollPane(txtArea), BorderLayout.CENTER);

        btnExportar = new JButton("Exportar a archivo");
        add(btnExportar, BorderLayout.SOUTH);

        // Mostrar el reporte correspondiente
        switch (tipoReporte) {
            case "Destino":
                txtArea.setText(agenciaController.getReporteRecaudacionDestinoComoTexto());
                btnExportar.addActionListener(e -> exportarReporte("Destino"));
                break;
            case "Transporte":
                txtArea.setText(agenciaController.getReporteRecaudacionTransporteComoTexto());
                btnExportar.addActionListener(e -> exportarReporte("Transporte"));
                break;
            case "Responsables":
                txtArea.setText(agenciaController.getRankingResponsablesComoTexto());
                btnExportar.addActionListener(e -> exportarReporte("Responsables"));
                break;
        }

        setVisible(true);
    }

    private void exportarReporte(String tipo) {
        JFileChooser fileChooser = new JFileChooser();
        int opcion = fileChooser.showSaveDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                switch (tipo) {
                    case "Destino" -> agenciaController.exportarReporteRecaudacionDestino(path);
                    case "Transporte" -> agenciaController.exportarReporteRecaudacionTransporte(path);
                    case "Responsables" -> agenciaController.exportarRankingResponsables(path);
                }
                JOptionPane.showMessageDialog(this, "Reporte exportado correctamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage());
            }
        }
    }
}
