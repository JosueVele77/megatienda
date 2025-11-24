package view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MenuVendedorView extends JFrame {

    // --- Componentes Principales ---
    public JPanel pnlContent;
    public JButton btnNuevaVenta, btnRegistrarCliente, btnSalir;

    // --- Panel Venta ---
    public JTextField txtBusquedaCliente, txtBusquedaProducto;
    public JButton btnBuscarCliente, btnAgregarProducto;
    public JLabel lblClienteNombre, lblClienteCedula;
    public JTable tablaDetalle;
    public DefaultTableModel modeloTabla;
    public JSpinner spinnerCantidad;

    // --- Panel Totales ---
    public JLabel lblSubtotal, lblIva, lblTotal;
    public JComboBox<String> cmbFormaPago;
    public JLabel lblInfoPago; // Para mostrar cuotas
    public JButton btnProcesarPago;

    // Colores
    private final Color COLOR_BG_DARK = new Color(30, 30, 35);
    private final Color COLOR_ACCENT = new Color(59, 130, 246); // Azul
    private final Color COLOR_SUCCESS = new Color(40, 167, 69); // Verde

    public MenuVendedorView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Punto de Venta - Megatienda");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout Principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- 1. SIDEBAR (Menú Izquierdo) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_BG_DARK);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel lblLogo = new JLabel("MEGATIENDA");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        btnNuevaVenta = crearBotonMenu("Nueva Venta", "cart");
        btnRegistrarCliente = crearBotonMenu("Nuevo Cliente", "user_add");
        sidebar.add(btnNuevaVenta);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnRegistrarCliente);
        sidebar.add(Box.createVerticalGlue());

        btnSalir = crearBotonMenu("Cerrar Sesión", "exit");
        btnSalir.setBackground(new Color(220, 53, 69));
        sidebar.add(btnSalir);

        // --- 2. AREA DE TRABAJO (Centro) ---
        pnlContent = new JPanel(new BorderLayout(15, 15));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel Superior: Búsquedas
        JPanel pnlTop = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlTop.setPreferredSize(new Dimension(0, 140));

        // -> Caja Cliente
        JPanel pnlCliente = crearPanelSeccion("Datos del Cliente");
        txtBusquedaCliente = new JTextField();
        txtBusquedaCliente.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Cédula del cliente...");
        txtBusquedaCliente.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        btnBuscarCliente = new JButton("Buscar");

        lblClienteNombre = new JLabel("Cliente: -");
        lblClienteNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblClienteCedula = new JLabel("CI: -");

        JPanel pnlBusqCli = new JPanel(new BorderLayout(5, 0));
        pnlBusqCli.setOpaque(false);
        pnlBusqCli.add(txtBusquedaCliente, BorderLayout.CENTER);
        pnlBusqCli.add(btnBuscarCliente, BorderLayout.EAST);

        pnlCliente.add(pnlBusqCli);
        pnlCliente.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlCliente.add(lblClienteNombre);
        pnlCliente.add(lblClienteCedula);

        // -> Caja Producto
        JPanel pnlProd = crearPanelSeccion("Agregar Producto");
        txtBusquedaProducto = new JTextField();
        txtBusquedaProducto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Código de producto...");
        txtBusquedaProducto.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        btnAgregarProducto = new JButton("Agregar");
        btnAgregarProducto.setBackground(COLOR_ACCENT);
        btnAgregarProducto.setForeground(Color.WHITE);

        JPanel pnlBusqProd = new JPanel(new BorderLayout(5, 0));
        pnlBusqProd.setOpaque(false);
        pnlBusqProd.add(txtBusquedaProducto, BorderLayout.CENTER);
        pnlBusqProd.add(spinnerCantidad, BorderLayout.EAST);

        pnlProd.add(pnlBusqProd);
        pnlProd.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlProd.add(btnAgregarProducto);

        pnlTop.add(pnlCliente);
        pnlTop.add(pnlProd);

        // Panel Central: Tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Código");
        modeloTabla.addColumn("Producto");
        modeloTabla.addColumn("Cant.");
        modeloTabla.addColumn("P. Unit");
        modeloTabla.addColumn("Subtotal");

        tablaDetalle = new JTable(modeloTabla);
        tablaDetalle.setRowHeight(30);
        JScrollPane scrollTabla = new JScrollPane(tablaDetalle);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        // Panel Derecho: Resumen y Pago
        JPanel pnlPago = new JPanel();
        pnlPago.setLayout(new BoxLayout(pnlPago, BoxLayout.Y_AXIS));
        pnlPago.setPreferredSize(new Dimension(280, 0));
        pnlPago.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(45, 45, 50)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        pnlPago.setBackground(new Color(45, 45, 50));

        JLabel lblResumen = new JLabel("Resumen de Pago");
        lblResumen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblResumen.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Etiquetas de Totales
        lblSubtotal = crearLabelTotal("Subtotal: $0.00");
        lblIva = crearLabelTotal("IVA (15%): $0.00");
        lblTotal = crearLabelTotal("TOTAL: $0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(COLOR_SUCCESS);

        // Selector Pago
        cmbFormaPago = new JComboBox<>(new String[]{"EFECTIVO", "CORRIENTE", "DIFERIDO 3 MESES", "DIFERIDO 6 MESES"});
        cmbFormaPago.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        lblInfoPago = new JLabel(" ");
        lblInfoPago.setForeground(Color.LIGHT_GRAY);

        btnProcesarPago = new JButton("PROCESAR VENTA");
        btnProcesarPago.setBackground(COLOR_SUCCESS);
        btnProcesarPago.setForeground(Color.WHITE);
        btnProcesarPago.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnProcesarPago.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnProcesarPago.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProcesarPago.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth:0");

        pnlPago.add(lblResumen);
        pnlPago.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlPago.add(lblSubtotal);
        pnlPago.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlPago.add(lblIva);
        pnlPago.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlPago.add(new JSeparator());
        pnlPago.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlPago.add(lblTotal);
        pnlPago.add(Box.createRigidArea(new Dimension(0, 30)));
        pnlPago.add(new JLabel("Forma de Pago:"));
        pnlPago.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlPago.add(cmbFormaPago);
        pnlPago.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlPago.add(lblInfoPago);
        pnlPago.add(Box.createVerticalGlue());
        pnlPago.add(btnProcesarPago);

        // Ensamblaje final
        pnlContent.add(pnlTop, BorderLayout.NORTH);
        pnlContent.add(scrollTabla, BorderLayout.CENTER);
        pnlContent.add(pnlPago, BorderLayout.EAST);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(pnlContent, BorderLayout.CENTER);

        add(mainPanel);
    }

    // --- Helpers ---
    private JButton crearBotonMenu(String texto, String icono) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 0,20,0,0; Button.borderWidth:0");
        return btn;
    }

    private JPanel crearPanelSeccion(String titulo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(45, 45, 50));
        p.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, new Color(45, 45, 50)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel l = new JLabel(titulo);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(l);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        return p;
    }

    private JLabel crearLabelTotal(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    // Simulación de Pasarela de Pago
    public boolean mostrarPasarelaPago(double total, String detallePago) {
        JDialog dialog = new JDialog(this, "Pasarela de Pago", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lbl = new JLabel("Confirmar Transacción");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMonto = new JLabel("Monto a debitar: $" + String.format("%.2f", total));
        lblMonto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDetalle = new JLabel(detallePago);
        lblDetalle.setForeground(Color.GRAY);
        lblDetalle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Simulación de proceso
        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setVisible(false);

        JButton btnConfirmar = new JButton("PAGAR AHORA");
        btnConfirmar.setBackground(COLOR_SUCCESS);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Resultado (final array hack para acceder desde listener)
        final boolean[] resultado = {false};

        btnConfirmar.addActionListener(e -> {
            btnConfirmar.setEnabled(false);
            progress.setVisible(true);

            // Simular delay de red
            Timer t = new Timer(2000, evt -> {
                // Simular probabilidad de fallo (10%)
                if (Math.random() > 0.9) {
                    JOptionPane.showMessageDialog(dialog, "Transacción rechazada por el banco.", "Error", JOptionPane.ERROR_MESSAGE);
                    resultado[0] = false;
                } else {
                    JOptionPane.showMessageDialog(dialog, "¡Transacción Aprobada!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    resultado[0] = true;
                }
                dialog.dispose();
            });
            t.setRepeats(false);
            t.start();
        });

        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(lblMonto);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
        p.add(lblDetalle);
        p.add(Box.createRigidArea(new Dimension(0, 30)));
        p.add(progress);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(btnConfirmar);

        dialog.add(p);
        dialog.setVisible(true);

        return resultado[0];
    }

    // Clase borde (reutilizada)
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        int r; Color c; RoundedBorder(int r, Color c){this.r=r;this.c=c;}
        public void paintBorder(Component cmp,Graphics g,int x,int y,int w,int h){
            Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.drawRoundRect(x,y,w-1,h-1,r,r);
        }
    }
}