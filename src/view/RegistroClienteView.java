package view;

import com.formdev.flatlaf.FlatClientProperties;
import controller.RegistroClienteController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RegistroClienteView extends JFrame {

    public JTextField txtNombre, txtCedula, txtCorreo, txtTelefono;
    public JButton btnGuardar, btnCancelar;

    // Colores del tema (Iguales a GestionEmpleados)
    private final Color COLOR_ACCENT = new Color(59, 130, 246); // Azul
    private final Color COLOR_BG_SECONDARY = new Color(40, 40, 45); // Fondo del panel

    public RegistroClienteView() {
        initComponents();
        // El controlador se vincula desde fuera o aquí mismo
        new RegistroClienteController(this).iniciarListeners();
    }

    private void initComponents() {
        setTitle("Registrar Nuevo Cliente");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel Principal con padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Título
        JLabel lblTitulo = new JLabel("Registrar Cliente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Contenedor del Formulario (Estilo tarjeta oscura)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(COLOR_BG_SECONDARY);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, COLOR_BG_SECONDARY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- CAMPOS ---
        txtNombre = crearInput(formPanel, "Nombres Completos", "user");
        txtCedula = crearInput(formPanel, "Cédula (Ecuador)", "id");
        txtCorreo = crearInput(formPanel, "Correo Electrónico", "email");
        txtTelefono = crearInput(formPanel, "Teléfono (09...)", "phone");

        // --- BOTONES ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(140, 45));
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(COLOR_ACCENT);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth: 0");

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 45));
        btnCancelar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #555; foreground: white; borderWidth:0");
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);

        // --- ENSAMBLAJE ---
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(btnPanel); // Botones al final del formPanel

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    // --- HELPERS DE DISEÑO ---

    private JTextField crearInput(JPanel parent, String labelText, String iconType) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setForeground(new Color(200, 200, 200)); // Texto gris claro
        parent.add(label);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));

        // ESTILO EXACTO AL DE EMPLEADOS (Píldora)
        field.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 999; margin: 0,10,0,10; outlineWidth: 1");
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, labelText);
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new SimpleIcon(iconType));

        parent.add(field);
        parent.add(Box.createRigidArea(new Dimension(0, 15))); // Espacio entre campos
        return field;
    }

    public void marcarCampoError(JTextField campo, boolean error) {
        if (error) {
            campo.putClientProperty(FlatClientProperties.OUTLINE, "error");
        } else {
            campo.putClientProperty(FlatClientProperties.OUTLINE, null);
        }
        campo.repaint();
    }

    // --- CLASES AUXILIARES (Iconos y Bordes) ---

    // Iconos vectoriales simples (Copia exacta de GestionEmpleados)
    private static class SimpleIcon implements Icon {
        private final String type;
        private final Color color = Color.GRAY;
        public SimpleIcon(String type) { this.type = type; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2));
            int cx = x + 8, cy = y + 8;
            if(type.equals("user")) { g2.drawArc(cx-3, cy-5, 6, 6, 0, 360); g2.drawArc(cx-5, cy+2, 10, 8, 0, 180); }
            else if(type.equals("id")) { g2.drawRoundRect(cx-6, cy-5, 12, 10, 2, 2); g2.drawLine(cx-3, cy-2, cx+3, cy-2); }
            else if(type.equals("email")) { g2.drawRect(cx-6, cy-4, 12, 8); g2.drawLine(cx-6, cy-4, cx, cy+1); g2.drawLine(cx, cy+1, cx+6, cy-4); }
            else if(type.equals("phone")) { g2.drawRoundRect(cx-3, cy-5, 6, 10, 2, 2); }
            g2.dispose();
        }
        @Override public int getIconWidth() { return 16; }
        @Override public int getIconHeight() { return 16; }
    }

    // Borde redondeado para el panel secundario
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