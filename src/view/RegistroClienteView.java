package view;

import com.formdev.flatlaf.FlatClientProperties;
import controller.RegistroClienteController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class RegistroClienteView extends JFrame {

    private JTextField txtNombre;
    private JTextField txtCedula;
    private JTextField txtCorreo;
    private JTextField txtDireccion; // Usaremos este como Teléfono según tu imagen
    private JButton btnEnviar;

    private final Color COLOR_PRIMARY = new Color(123, 31, 162);
    private final Color COLOR_BG_ICON = new Color(140, 50, 170);

    // Colores para validación
    private final Color COLOR_SUCCESS = new Color(0, 200, 83); // Verde
    private final String COLOR_ERROR = "error"; // Rojo estándar de FlatLaf

    public RegistroClienteView() {
        initComponents();
        configurarEventos();
    }

    private void initComponents() {
        setTitle("Registro");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel lblTitulo = new JLabel("BIENVENIDO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.DARK_GRAY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos
        txtNombre = crearCampoEstiloImagen("Nombre Completo", "user");
        txtCedula = crearCampoEstiloImagen("Cédula", "lock");
        txtCorreo = crearCampoEstiloImagen("Correo Electrónico", "email");
        // Nota: En tu imagen el último campo dice "Teléfono", así que lo etiquetamos visualmente acorde
        txtDireccion = crearCampoEstiloImagen("Teléfono (09xxxxxxxx)", "phone");

        JLabel lblTerminos = new JLabel("Términos y Condiciones");
        lblTerminos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTerminos.setForeground(Color.GRAY);
        lblTerminos.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTerminos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setBackground(COLOR_PRIMARY);
        btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEnviar.putClientProperty(FlatClientProperties.STYLE,
                "arc: 999; borderWidth: 0; focusWidth: 0; innerFocusWidth: 0");
        btnEnviar.setMaximumSize(new Dimension(200, 45));
        btnEnviar.setPreferredSize(new Dimension(200, 45));

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        mainPanel.add(txtNombre);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(txtCedula);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(txtCorreo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(txtDireccion); // Este hará de teléfono visualmente

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(lblTerminos);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(btnEnviar);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    private JTextField crearCampoEstiloImagen(String placeholder, String iconType) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        field.setPreferredSize(new Dimension(300, 50));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        field.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        // Importante: outlineWidth define grosor del borde al validar
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 999; margin: 0, 10, 0, 10; outlineWidth: 2");
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new CircleIcon(iconType));

        return field;
    }

    private void configurarEventos() {
        RegistroClienteController controller = new RegistroClienteController(this, txtCedula, txtNombre, txtCorreo, txtDireccion);
        btnEnviar.addActionListener(controller);
    }

    /**
     * Método mágico para cambiar el color del borde
     * @param campo El JTextField a modificar
     * @param esValido true para Verde, false para Rojo
     */
    public void marcarCampo(JTextField campo, boolean esValido) {
        if (esValido) {
            // Pone el borde verde
            campo.putClientProperty(FlatClientProperties.OUTLINE, COLOR_SUCCESS);
        } else {
            // Pone el borde rojo (error)
            campo.putClientProperty(FlatClientProperties.OUTLINE, COLOR_ERROR);
        }
        campo.repaint();
    }

    // --- Getters para limpiar bordes si fuera necesario ---
    public void limpiarBorde(JTextField campo) {
        campo.putClientProperty(FlatClientProperties.OUTLINE, null);
        campo.repaint();
    }

    // Clase interna de Iconos (igual que antes)
    private class CircleIcon implements Icon {
        private final String type;
        private final int size = 32;
        public CircleIcon(String type) { this.type = type; }
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(COLOR_BG_ICON);
            g2.fill(new Ellipse2D.Double(x, y + 4, size, size));
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            int cx = x + size / 2; int cy = y + 4 + size / 2;
            if("user".equals(type)) { g2.drawArc(cx - 6, cy - 4, 12, 12, 0, 360); g2.drawArc(cx - 10, cy + 2, 20, 20, 0, 180); }
            else if("lock".equals(type)) { g2.drawRect(cx - 6, cy - 2, 12, 10); g2.drawArc(cx - 4, cy - 8, 8, 10, 0, 180); }
            else if("email".equals(type)) { g2.drawRect(cx - 8, cy - 5, 16, 10); g2.drawLine(cx - 8, cy - 5, cx, cy + 2); g2.drawLine(cx, cy + 2, cx + 8, cy - 5); }
            else if("phone".equals(type)) { g2.drawRoundRect(cx - 4, cy - 8, 8, 16, 2, 2); }
            g2.dispose();
        }
        @Override public int getIconWidth() { return size + 10; }
        @Override public int getIconHeight() { return 40; }
    }
}