package view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class MenuVendedorView extends JFrame {

    // --- Componentes Principales ---
    public JPanel sidebar; // Público para cambiar color
    public JPanel pnlContent;
    public JPanel pnlPago; // Panel derecho

    public JButton btnNuevaVenta, btnRegistrarCliente, btnHistorial, btnSalir;

    // Botón Tema (Toggle)
    public JToggleButton btnTema;

    // --- ETIQUETAS ---
    public JLabel lblInfoVendedor;
    public JLabel lblLogo;

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
    public JLabel lblInfoPago;
    public JButton btnProcesarPago;

    // Color de éxito (se mantiene constante o se puede adaptar)
    private final Color COLOR_SUCCESS = new Color(40, 167, 69);

    public MenuVendedorView() {
        initComponents();
        actualizarColores(); // Aplicar colores iniciales
    }

    private void initComponents() {
        setTitle("Punto de Venta - Megatienda");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout Principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- 1. SIDEBAR (Menú Izquierdo) ---
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        lblLogo = new JLabel("MEGATIENDA");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 50)));

        // Botones (Solo texto)
        btnNuevaVenta = crearBotonMenu("Nueva Venta");
        btnRegistrarCliente = crearBotonMenu("Nuevo Cliente");
        btnHistorial = crearBotonMenu("Historial");

        sidebar.add(btnNuevaVenta);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnRegistrarCliente);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnHistorial);

        // --- ESPACIO ELÁSTICO ---
        sidebar.add(Box.createVerticalGlue());

        // --- BOTÓN TEMA (TOGGLE) ---
        JPanel pnlTema = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTema.setOpaque(false);

        btnTema = new JToggleButton();
        btnTema.setPreferredSize(new Dimension(60, 30));
        btnTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTema.setContentAreaFilled(false);
        btnTema.setBorderPainted(false);
        btnTema.setFocusPainted(false);
        btnTema.setIcon(new ToggleSwitchIcon(false));
        btnTema.setSelectedIcon(new ToggleSwitchIcon(true));
        btnTema.setToolTipText("Cambiar Tema");

        JLabel lblTemaTxt = new JLabel("Tema: ");
        lblTemaTxt.putClientProperty("FlatLaf.styleClass", "h4");
        pnlTema.add(lblTemaTxt);
        pnlTema.add(btnTema);

        sidebar.add(pnlTema);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- BOTÓN SALIR ---
        btnSalir = crearBotonMenu("Cerrar Sesión");
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        sidebar.add(btnSalir);

        // --- 2. AREA DE TRABAJO (Centro) ---
        pnlContent = new JPanel(new BorderLayout(15, 15));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ... (Header y Búsquedas - Estructura igual) ...
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        JLabel lblTituloSeccion = new JLabel("Punto de Venta");
        lblTituloSeccion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        // El color se ajusta en actualizarColores()

        lblInfoVendedor = new JLabel("Vendedor: ...");
        estilizarBadge(lblInfoVendedor);

        pnlHeader.add(lblTituloSeccion, BorderLayout.WEST);
        pnlHeader.add(lblInfoVendedor, BorderLayout.EAST);

        // Panel Búsquedas
        JPanel pnlBusquedas = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlBusquedas.setOpaque(false); // Transparente para tomar color de fondo
        pnlBusquedas.setPreferredSize(new Dimension(0, 100));

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
        // Alineación izquierda spinner
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinnerCantidad.getEditor();
        editor.getTextField().setHorizontalAlignment(JTextField.LEFT);

        btnAgregarProducto = new JButton("Agregar");
        btnAgregarProducto.setBackground(new Color(59, 130, 246));
        btnAgregarProducto.setForeground(Color.WHITE);

        JPanel pnlBusqProd = new JPanel(new BorderLayout(5, 0));
        pnlBusqProd.setOpaque(false);
        pnlBusqProd.add(txtBusquedaProducto, BorderLayout.CENTER);
        pnlBusqProd.add(spinnerCantidad, BorderLayout.EAST);

        pnlProd.add(pnlBusqProd);
        pnlProd.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlProd.add(btnAgregarProducto);

        pnlBusquedas.add(pnlCliente);
        pnlBusquedas.add(pnlProd);

        JPanel pnlNorthGlobal = new JPanel(new BorderLayout(0, 15));
        pnlNorthGlobal.setOpaque(false);
        pnlNorthGlobal.add(pnlHeader, BorderLayout.NORTH);
        pnlNorthGlobal.add(pnlBusquedas, BorderLayout.CENTER);

        // Tabla Central
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

        // Panel Derecho (Pago)
        pnlPago = new JPanel();
        pnlPago.setLayout(new BoxLayout(pnlPago, BoxLayout.Y_AXIS));
        pnlPago.setPreferredSize(new Dimension(280, 0));
        // Borde y color se definen en actualizarColores()

        JLabel lblResumen = new JLabel("Resumen de Pago");
        lblResumen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblResumen.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblSubtotal = crearLabelTotal("Subtotal: $0.00");
        lblIva = crearLabelTotal("IVA (15%): $0.00");
        lblTotal = crearLabelTotal("TOTAL: $0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(COLOR_SUCCESS);

        cmbFormaPago = new JComboBox<>(new String[]{"EFECTIVO", "CORRIENTE", "DIFERIDO 3 MESES", "DIFERIDO 6 MESES"});
        cmbFormaPago.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        lblInfoPago = new JLabel(" ");

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

        pnlContent.add(pnlNorthGlobal, BorderLayout.NORTH);
        pnlContent.add(scrollTabla, BorderLayout.CENTER);
        pnlContent.add(pnlPago, BorderLayout.EAST);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(pnlContent, BorderLayout.CENTER);

        add(mainPanel);
    }

    // --- GESTIÓN DE COLORES ---
    public void actualizarColores() {
        boolean isDark = FlatLaf.isLafDark();

        if (isDark) {
            // MODO OSCURO
            sidebar.setBackground(new Color(30, 30, 35));
            pnlContent.setBackground(new Color(40, 40, 45));
            lblLogo.setForeground(Color.WHITE);

            // Panel de Pago Oscuro
            pnlPago.setBackground(new Color(45, 45, 50));
            pnlPago.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(15, new Color(45, 45, 50)),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            // Badge Oscuro
            lblInfoVendedor.setForeground(new Color(98, 196, 255));
            lblInfoVendedor.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #262a2e; border: 1,1,1,1, #4a90e2; margin: 6,15,6,15; font: bold");

        } else {
            // MODO CLARO (Todo Blanco)
            sidebar.setBackground(new Color(245, 245, 250)); // Gris muy claro
            pnlContent.setBackground(Color.WHITE);
            lblLogo.setForeground(new Color(60, 60, 60));

            // Panel de Pago Claro
            pnlPago.setBackground(new Color(250, 250, 250)); // Casi blanco
            pnlPago.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(15, new Color(220, 220, 220)), // Borde gris claro
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            // Badge Claro
            lblInfoVendedor.setForeground(new Color(30, 30, 30));
            lblInfoVendedor.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #e0e0e0; border: 1,1,1,1, #aaaaaa; margin: 6,15,6,15; font: bold");
        }

        btnTema.setSelected(isDark);
        SwingUtilities.updateComponentTreeUI(this);
    }

    // --- Helpers ---

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 15; margin: 0,10,0,10; Button.borderWidth:0");
        return btn;
    }

    private JPanel crearPanelSeccion(String titulo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        // Usamos colores de UIManager para que cambien solos, o forzamos en actualizarColores si es necesario
        // Aquí usaremos un truco: hacerlo semi-transparente o usar color por defecto del tema
        p.setBackground(null);
        p.setOpaque(false); // Hacer transparente para que tome el color de fondo (blanco/oscuro)

        // Borde con título
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150,150,150)),
                titulo
        ));

        return p;
    }

    private void estilizarBadge(JLabel label) {
        label.setOpaque(true);
        // El estilo exacto se define en actualizarColores
    }

    private JLabel crearLabelTotal(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

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

        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setVisible(false);

        JButton btnConfirmar = new JButton("PAGAR AHORA");
        btnConfirmar.setBackground(COLOR_SUCCESS);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);

        final boolean[] resultado = {false};

        btnConfirmar.addActionListener(e -> {
            btnConfirmar.setEnabled(false);
            progress.setVisible(true);
            Timer t = new Timer(2000, evt -> {
                resultado[0] = true;
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

    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        int r; Color c; RoundedBorder(int r, Color c){this.r=r;this.c=c;}
        public void paintBorder(Component cmp,Graphics g,int x,int y,int w,int h){
            Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.drawRoundRect(x,y,w-1,h-1,r,r);
        }
    }

    // --- ICONO SWITCH (IGUAL QUE BODEGUERO) ---
    private static class ToggleSwitchIcon implements Icon {
        private final boolean isSelected;
        private final int width = 50;
        private final int height = 24;

        public ToggleSwitchIcon(boolean isSelected) { this.isSelected = isSelected; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isSelected) g2.setColor(new Color(59, 130, 246));
            else g2.setColor(new Color(200, 200, 200));
            g2.fill(new RoundRectangle2D.Double(x, y, width, height, height, height));

            g2.setColor(Color.WHITE);
            int circleSize = height - 4;
            int circleX = isSelected ? (x + width - circleSize - 2) : (x + 2);
            g2.fill(new Ellipse2D.Double(circleX, y + 2, circleSize, circleSize));

            g2.setColor(isSelected ? new Color(59, 130, 246) : Color.ORANGE);
            if (isSelected) {
                g2.fill(new Ellipse2D.Double(circleX + 4, y + 6, 8, 8));
                g2.setColor(Color.WHITE);
                g2.fill(new Ellipse2D.Double(circleX + 2, y + 4, 8, 8));
            } else {
                g2.fill(new Ellipse2D.Double(circleX + 4, y + 6, 8, 8));
            }
            g2.dispose();
        }
        @Override public int getIconWidth() { return width; }
        @Override public int getIconHeight() { return height; }
    }
}