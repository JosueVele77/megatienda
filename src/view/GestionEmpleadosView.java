package view;

import com.formdev.flatlaf.FlatClientProperties;
import controller.GestionEmpleadosController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GestionEmpleadosView extends JFrame {

    // Campos del Formulario
    public JTextField txtCodigo, txtNombres, txtCedula, txtEmail;
    public JTextField txtTelConvencional, txtCelular;
    public JTextField txtDireccion, txtFechaIngreso;
    public JComboBox<String> cmbRol;
    public JButton btnGuardar, btnLimpiar;

    // Tabla
    public JTable tablaEmpleados;
    public DefaultTableModel modeloTabla;

    // Colores Estilo Dark
    private final Color COLOR_ACCENT = new Color(59, 130, 246); // Azul moderno
    private final Color COLOR_BG_SECONDARY = new Color(40, 40, 45); // Gris oscuro para paneles

    public GestionEmpleadosView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestión de Empleados");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Principal
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 1. SECCIÓN IZQUIERDA: FORMULARIO ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new Dimension(380, 0));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, COLOR_BG_SECONDARY), // Borde redondeado
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Registrar Empleado");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(lblTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Campos
        txtCodigo = crearInput(formPanel, "Código", "card");
        txtNombres = crearInput(formPanel, "Nombres Completos", "user");
        txtCedula = crearInput(formPanel, "Cédula (Ecuador)", "id");
        txtEmail = crearInput(formPanel, "Email", "email");

        // Teléfonos en una fila
        JPanel phonePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        phonePanel.setOpaque(false);
        phonePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        phonePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        txtTelConvencional = crearInputSinLabel(phonePanel, "T. Convencional");
        txtCelular = crearInputSinLabel(phonePanel, "Celular");

        formPanel.add(new JLabel("Teléfonos"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(phonePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        txtDireccion = crearInput(formPanel, "Dirección", "home");
        txtFechaIngreso = crearInput(formPanel, "Fecha Ingreso (YYYY-MM-DD)", "calendar");

        // Combo Rol
        JLabel lblRol = new JLabel("Perfil / Rol");
        lblRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblRol);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        cmbRol = new JComboBox<>(new String[]{"VENDEDOR", "BODEGUERO", "ADMINISTRADOR"});
        cmbRol.setPreferredSize(new Dimension(300, 40));
        cmbRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cmbRol.putClientProperty(FlatClientProperties.STYLE, "arc: 15"); // Bordes semi-redondos para el combo
        formPanel.add(cmbRol);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(140, 45));
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(COLOR_ACCENT);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Estilo píldora
        btnGuardar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth: 0");

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setPreferredSize(new Dimension(100, 45));
        btnLimpiar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #444444; foreground: white; borderWidth:0");

        btnPanel.add(btnGuardar);
        btnPanel.add(btnLimpiar);
        formPanel.add(btnPanel);

        // --- 2. SECCIÓN DERECHA: TABLA ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Lista de Empleados"));

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // --- ORDEN DE COLUMNAS CORRECTO ---
        modeloTabla.addColumn("Rol");           // 0
        modeloTabla.addColumn("Nombres");       // 1
        modeloTabla.addColumn("Cédula");        // 2
        modeloTabla.addColumn("Celular");       // 3
        modeloTabla.addColumn("Dirección");     // 4
        modeloTabla.addColumn("Fecha Ingreso"); // 5
        modeloTabla.addColumn("Email");         // 6

        tablaEmpleados = new JTable(modeloTabla);
        tablaEmpleados.setRowHeight(30);
        tablaEmpleados.setShowVerticalLines(false);
        // Estilo de tabla moderno
        tablaEmpleados.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "font: bold; separatorColor: #333333; bottomSeparatorColor: #333333");

        JScrollPane scrollTabla = new JScrollPane(tablaEmpleados);
        // Quitar bordes del scroll
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        tablePanel.add(scrollTabla, BorderLayout.CENTER);

        // Agregar al principal
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // --- Métodos Helpers de Diseño ---

    private JTextField crearInput(JPanel parent, String labelText, String iconType) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextField field = new JTextField();
        estilizarCampo(field, labelText, iconType);

        parent.add(field);
        parent.add(Box.createRigidArea(new Dimension(0, 15)));
        return field;
    }

    private JTextField crearInputSinLabel(JPanel parent, String placeholder) {
        JTextField field = new JTextField();
        estilizarCampo(field, placeholder, "phone");
        parent.add(field);
        return field;
    }

    private void estilizarCampo(JTextField field, String placeholder, String iconType) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        // Estilo píldora exacto a la imagen
        field.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 999; margin: 0,10,0,10; outlineWidth: 2");
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        // Icono
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new SimpleIcon(iconType));
    }

    // --- SISTEMA DE NOTIFICACIONES FLOTANTES (TOAST) ---

    public void mostrarErrorFlotante(String mensaje) {
        mostrarToast(mensaje, new Color(220, 53, 69), "error"); // Rojo
    }

    public void mostrarExitoFlotante(String mensaje) {
        mostrarToast(mensaje, new Color(40, 167, 69), "check"); // Verde
    }

    private void mostrarToast(String mensaje, Color bg, String iconType) {
        JWindow toast = new JWindow(this);
        toast.setBackground(new Color(0,0,0,0)); // Transparente para permitir bordes redondos

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(bg);
        // Borde redondeado para el toast
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel lblIcon = new JLabel(new SimpleIcon(iconType, Color.WHITE));
        JLabel lblMsg = new JLabel("<html><body style='width: 200px; color: white'><b>" + (iconType.equals("error")?"Error":"Éxito") + "</b><br/>" + mensaje + "</body></html>");

        panel.add(lblIcon);
        panel.add(lblMsg);

        toast.setContentPane(panel);
        toast.pack();

        // Posicionar arriba a la derecha de la ventana actual
        Point loc = this.getLocationOnScreen();
        toast.setLocation(loc.x + this.getWidth() - toast.getWidth() - 30, loc.y + 40);
        toast.setVisible(true);

        // Cerrar automáticamente después de 3 segundos
        Timer timer = new Timer(3000, e -> toast.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    public void marcarCampoError(JTextField campo, boolean error) {
        if (error) {
            campo.putClientProperty(FlatClientProperties.OUTLINE, "error"); // Rojo FlatLaf
        } else {
            campo.putClientProperty(FlatClientProperties.OUTLINE, null);
        }
        campo.repaint();
    }

    // --- CLASE DE ICONOS SIMPLES (Para no depender de imágenes externas) ---
    private static class SimpleIcon implements Icon {
        private final String type;
        private final Color color;
        public SimpleIcon(String type) { this(type, Color.GRAY); }
        public SimpleIcon(String type, Color c) { this.type = type; this.color = c; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2));
            int cx = x + 8, cy = y + 8;
            if(type.equals("user")) { g2.drawArc(cx-3, cy-5, 6, 6, 0, 360); g2.drawArc(cx-5, cy+2, 10, 8, 0, 180); }
            else if(type.equals("card")) { g2.drawRect(cx-6, cy-4, 12, 8); g2.drawLine(cx-4, cy-1, cx+4, cy-1); }
            else if(type.equals("id")) { g2.drawRoundRect(cx-6, cy-5, 12, 10, 2, 2); g2.drawLine(cx-3, cy-2, cx+3, cy-2); }
            else if(type.equals("email")) { g2.drawRect(cx-6, cy-4, 12, 8); g2.drawLine(cx-6, cy-4, cx, cy+1); g2.drawLine(cx, cy+1, cx+6, cy-4); }
            else if(type.equals("phone")) { g2.drawRoundRect(cx-3, cy-5, 6, 10, 2, 2); }
            else if(type.equals("calendar")) { g2.drawRect(cx-6, cy-4, 12, 9); g2.drawLine(cx-6, cy-1, cx+6, cy-1); }
            else if(type.equals("error")) { g2.setColor(Color.WHITE); g2.drawOval(x, y, 16, 16); g2.drawLine(x+5, y+5, x+11, y+11); g2.drawLine(x+11, y+5, x+5, y+11); }
            else if(type.equals("check")) { g2.setColor(Color.WHITE); g2.drawOval(x, y, 16, 16); g2.drawLine(x+4, y+8, x+7, y+11); g2.drawLine(x+7, y+11, x+12, y+5); }
            g2.dispose();
        }
        @Override public int getIconWidth() { return 16; }
        @Override public int getIconHeight() { return 16; }
    }

    // Borde redondeado para el panel
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private int r; private Color c;
        RoundedBorder(int r, Color c){ this.r=r; this.c=c; }
        @Override public void paintBorder(Component cmp, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.drawRoundRect(x, y, w-1, h-1, r, r);
            g2.dispose();
        }
    }
}