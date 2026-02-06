package view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class MenuAdminView extends JFrame {

    public JButton btnGestionEmpleados;
    public JButton btnActualizarCliente;
    public JButton btnGestionHorarios;
    public JButton btnResetPassword;
    public JButton btnVerHorario;

    public JToggleButton btnTema;
    public JButton btnSalir;

    public JLabel lblUsuarioInfo;
    public JLabel lblTitulo;

    public MenuAdminView() {
        initComponents();
        actualizarColores();
    }

    private void initComponents() {
        setTitle("Panel de Control - Administrador");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- ICONO DE LA APLICACIÓN ---
        try {
            URL iconUrl = getClass().getResource("/logo.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception e) {
            System.err.println("Error cargando icono de la app: " + e.getMessage());
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- 1. HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        lblTitulo = new JLabel("Megatienda Admin");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        lblUsuarioInfo = new JLabel("Admin: ...");

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(lblUsuarioInfo, BorderLayout.EAST);

        // --- 2. GRID CENTRAL (BOTONES GRANDES) ---
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20)); // Ajusté a 3 columnas para que quepan mejor si quieres, o 2x3
        // Pero mantendré 2x2 y agregaré el quinto, Swing lo ajusta.
        // Mejor usamos un GridLayout dinámico o fijo con suficientes huecos.
        // Para 5 botones, GridLayout(2, 3) o (3, 2) es mejor.
        // Dejaré (0, 2) para filas dinámicas en 2 columnas, o (0, 3) para 3 columnas.
        // Usemos GridLayout(0, 2) para que crezca hacia abajo.
        gridPanel.setLayout(new GridLayout(0, 2, 25, 25));

        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnGestionEmpleados = crearBotonGrid("Gestión de Empleados", "Registrar, listar y editar", new Color(0, 123, 255));
        btnActualizarCliente = crearBotonGrid("Actualizar Cliente", "Modificar datos por cédula", new Color(40, 167, 69));
        btnGestionHorarios = crearBotonGrid("Asignar Horarios", "Gestión de turnos", new Color(255, 193, 7));
        btnResetPassword = crearBotonGrid("Resetear Contraseña", "Restaurar accesos", new Color(220, 53, 69));

        // --- CAMBIO AQUÍ: FORMATO IGUAL A LOS OTROS ---
        btnVerHorario = crearBotonGrid("Consultar Horario", "Ver tabla semanal", new Color(111, 66, 193)); // Color Morado

        gridPanel.add(btnGestionEmpleados);
        gridPanel.add(btnActualizarCliente);
        gridPanel.add(btnGestionHorarios);
        gridPanel.add(btnResetPassword);
        gridPanel.add(btnVerHorario);

        // --- 3. FOOTER ---
        JPanel footerContainer = new JPanel(new BorderLayout());
        footerContainer.setOpaque(false);

        JPanel verticalStack = new JPanel();
        verticalStack.setLayout(new BoxLayout(verticalStack, BoxLayout.Y_AXIS));
        verticalStack.setOpaque(false);

        JPanel pnlTema = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlTema.setOpaque(false);
        pnlTema.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTemaTxt = new JLabel("Tema");
        lblTemaTxt.putClientProperty("FlatLaf.styleClass", "h4");

        btnTema = new JToggleButton();
        btnTema.setPreferredSize(new Dimension(50, 24));
        btnTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTema.setContentAreaFilled(false);
        btnTema.setBorderPainted(false);
        btnTema.setFocusPainted(false);
        btnTema.setIcon(new ToggleSwitchIcon(false));
        btnTema.setSelectedIcon(new ToggleSwitchIcon(true));

        pnlTema.add(lblTemaTxt);
        pnlTema.add(btnTema);

        btnSalir = new JButton("Cerrar Sesión");
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.putClientProperty(FlatClientProperties.STYLE, "arc: 15; margin: 10,30,10,30; borderWidth:0");
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);

        verticalStack.add(pnlTema);
        verticalStack.add(Box.createRigidArea(new Dimension(0, 15)));
        verticalStack.add(btnSalir);

        footerContainer.add(verticalStack, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        mainPanel.add(footerContainer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public void actualizarColores() {
        boolean isDark = FlatLaf.isLafDark();
        if (isDark) {
            lblUsuarioInfo.setForeground(new Color(98, 196, 255));
            lblUsuarioInfo.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #262a2e; border: 1,1,1,1, #4a90e2; margin: 6,15,6,15; font: bold");
            lblTitulo.setForeground(Color.WHITE);
        } else {
            lblUsuarioInfo.setForeground(new Color(30, 30, 30));
            lblUsuarioInfo.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #e0e0e0; border: 1,1,1,1, #aaaaaa; margin: 6,15,6,15; font: bold");
            lblTitulo.setForeground(new Color(60, 60, 60));
        }
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnTema.setSelected(isDark);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private JButton crearBotonGrid(String titulo, String desc, Color colorBase) {
        JButton btn = new JButton("<html><center><span style='font-size:16px'><b>" + titulo + "</b></span><br/><span style='font-size:11px; opacity:0.8'>" + desc + "</span></center></html>");
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        if (colorBase.equals(new Color(255, 193, 7))) {
            btn.setForeground(Color.DARK_GRAY);
        }
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 25; margin: 15,15,15,15; borderWidth:0; shadowWidth: 1");
        return btn;
    }

    public void setUsuarioInfo(String info) {
        lblUsuarioInfo.setText(info);
    }

    private static class ToggleSwitchIcon implements Icon {
        private final boolean isSelected;
        public ToggleSwitchIcon(boolean isSelected) { this.isSelected = isSelected; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (isSelected) g2.setColor(new Color(59, 130, 246));
            else g2.setColor(new Color(200, 200, 200));
            g2.fill(new RoundRectangle2D.Double(x, y, 50, 24, 24, 24));
            g2.setColor(Color.WHITE);
            int cx = isSelected ? (x + 50 - 20 - 2) : (x + 2);
            g2.fill(new Ellipse2D.Double(cx, y + 2, 20, 20));
            g2.setColor(isSelected ? new Color(59, 130, 246) : Color.ORANGE);
            g2.fill(new Ellipse2D.Double(cx + 4, y + 6, 8, 8));
            if (isSelected) { g2.setColor(Color.WHITE); g2.fill(new Ellipse2D.Double(cx + 2, y + 4, 8, 8)); }
            g2.dispose();
        }
        public int getIconWidth() { return 50; }
        public int getIconHeight() { return 24; }
    }
}