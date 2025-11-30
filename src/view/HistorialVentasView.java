package view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistorialVentasView extends JDialog {

    public JTable tablaHistorial;
    public DefaultTableModel modelo;
    public JButton btnCerrar;

    public HistorialVentasView(Frame owner) {
        super(owner, "Historial Detallado de Ventas", true);
        initComponents();
    }

    private void initComponents() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(20, 20));

        // Título
        JLabel lblTitulo = new JLabel("Historial de Transacciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        // Tabla
        modelo = new DefaultTableModel();
        // Columnas solicitadas
        modelo.addColumn("Cliente");
        modelo.addColumn("Cédula");
        modelo.addColumn("Forma de Pago");
        modelo.addColumn("Producto");
        modelo.addColumn("Cant.");
        modelo.addColumn("Total Compra");

        tablaHistorial = new JTable(modelo);
        tablaHistorial.setRowHeight(25);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Botón
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCerrar = new JButton("Cerrar");
        btnCerrar.putClientProperty(FlatClientProperties.STYLE, "arc:10; background:#333; foreground:white");
        pnlSur.add(btnCerrar);
        pnlSur.setBorder(BorderFactory.createEmptyBorder(0,0,20,20));

        add(lblTitulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(pnlSur, BorderLayout.SOUTH);
    }
}