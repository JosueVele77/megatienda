package view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import controller.ActualizarClienteController;
import model.entities.Cliente;

import javax.swing.*;
import java.awt.*;

public class ActualizarClienteView extends JFrame {

    // Componentes de Búsqueda
    public JTextField txtBusqueda;
    public JButton btnBuscar;

    // Componentes de Datos
    public JPanel pnlDatos;
    public JTextField txtNombres, txtEmail, txtTelefono, txtDireccion;
    public JTextField txtCedulaInmutable;
    public JButton btnActualizar, btnCancelar;

    public ActualizarClienteView() {
        initComponents();
        actualizarColores(); // Aplicar colores al iniciar
    }

    private void initComponents() {
        setTitle("Actualizar Cliente");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- 1. SECCIÓN DE BÚSQUEDA ---
        JLabel lblTitulo = new JLabel("Buscar Cliente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pnlBusqueda = new JPanel(new BorderLayout(10, 0));
        pnlBusqueda.setMaximumSize(new Dimension(400, 50));
        pnlBusqueda.setOpaque(false);

        txtBusqueda = new JTextField();
        estilizarCampo(txtBusqueda, "Ingrese Cédula", "search");

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(59, 130, 246));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth: 0");
        btnBuscar.setPreferredSize(new Dimension(100, 40));

        pnlBusqueda.add(txtBusqueda, BorderLayout.CENTER);
        pnlBusqueda.add(btnBuscar, BorderLayout.EAST);

        // --- 2. SECCIÓN DE DATOS ---
        pnlDatos = new JPanel();
        pnlDatos.setLayout(new BoxLayout(pnlDatos, BoxLayout.Y_AXIS));
        // El color y borde se definen en actualizarColores()
        pnlDatos.setVisible(false);

        // Campos del formulario
        txtCedulaInmutable = crearInputNoEditable(pnlDatos, "Cédula (No editable)");
        txtNombres = crearInput(pnlDatos, "Nombres Completos", "user");
        txtEmail = crearInput(pnlDatos, "Email", "email");
        txtTelefono = crearInput(pnlDatos, "Teléfono", "phone");
        txtDireccion = crearInput(pnlDatos, "Dirección", "home");

        // Botones de Acción
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBotones.setOpaque(false);

        btnActualizar = new JButton("Guardar Cambios");
        btnActualizar.setBackground(new Color(40, 167, 69)); // Verde
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth: 0; font: bold");
        btnActualizar.setPreferredSize(new Dimension(150, 45));
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // El estilo del botón cancelar también se ajusta en actualizarColores

        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnCancelar);
        pnlDatos.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlDatos.add(pnlBotones);

        // --- AGREGAR AL PRINCIPAL ---
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(pnlBusqueda);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(pnlDatos);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    // --- NUEVO: GESTIÓN DE COLORES ---
    public void actualizarColores() {
        boolean isDark = FlatLaf.isLafDark();

        if (isDark) {
            // Panel Datos Oscuro
            pnlDatos.setBackground(new Color(45, 45, 50));
            pnlDatos.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(20, new Color(45, 45, 50)),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            // Botón Cancelar Oscuro
            btnCancelar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #555; foreground: #ffffff; borderWidth: 0");

            // Input Bloqueado Oscuro
            txtCedulaInmutable.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #333; foreground: #888");

        } else {
            // Panel Datos Claro (Blanco)
            pnlDatos.setBackground(Color.WHITE);
            // Borde sutil gris en modo claro
            pnlDatos.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(20, new Color(230, 230, 230)),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            // Botón Cancelar Claro
            btnCancelar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #e0e0e0; foreground: #333; borderWidth: 0");

            // Input Bloqueado Claro
            txtCedulaInmutable.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #f0f0f0; foreground: #888");
        }

        SwingUtilities.updateComponentTreeUI(this);
    }

    // --- Helpers ---
    public void mostrarFormulario(boolean mostrar) {
        pnlDatos.setVisible(mostrar);
        this.revalidate();
        this.repaint();
        if(mostrar) this.setSize(500, 700);
        else this.setSize(500, 250);
    }

    public void cargarDatos(Cliente c) {
        txtCedulaInmutable.setText(c.getCedula());
        txtNombres.setText(c.getNombre());
        txtEmail.setText(c.getCorreo());
        txtDireccion.setText(c.getDireccion());
        txtTelefono.setText(c.getTelefono());
    }

    private JTextField crearInput(JPanel parent, String label, String icon) {
        parent.add(new JLabel(label));
        parent.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField t = new JTextField();
        estilizarCampo(t, label, icon);
        parent.add(t);
        parent.add(Box.createRigidArea(new Dimension(0, 15)));
        return t;
    }

    private JTextField crearInputNoEditable(JPanel parent, String label) {
        JTextField t = crearInput(parent, label, "lock");
        t.setEditable(false);
        t.setEnabled(false);
        return t;
    }

    private void estilizarCampo(JTextField field, String ph, String icon) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 999; margin: 0,10,0,10; outlineWidth: 2");
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, ph);
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new SimpleIcon(icon));
    }

    public void mostrarToast(String msg, boolean error) {
        JOptionPane.showMessageDialog(this, msg, error ? "Error" : "Éxito", error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    // Iconos Simples
    private static class SimpleIcon implements Icon {
        String t;
        SimpleIcon(String t){this.t=t;}
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY); g2.setStroke(new BasicStroke(2));
            if(t.equals("search")) { g2.drawOval(x+4,y+4,8,8); g2.drawLine(x+10,y+10,x+14,y+14); }
            else if(t.equals("user")) { g2.drawArc(x+6, y+4, 4, 4, 0, 360); g2.drawArc(x+4, y+10, 8, 6, 0, 180); }
            else if(t.equals("lock")) { g2.drawRect(x+5, y+8, 6, 6); g2.drawArc(x+6, y+4, 4, 6, 0, 180); }
            else if(t.equals("email")) { g2.drawRect(cx(x)-6, cy(y)-4, 12, 8); g2.drawLine(cx(x)-6, cy(y)-4, cx(x), cy(y)+1); g2.drawLine(cx(x), cy(y)+1, cx(x)+6, cy(y)-4); }
            else if(t.equals("phone")) { g2.drawRoundRect(cx(x)-3, cy(y)-5, 6, 10, 2, 2); }
            else if(t.equals("home")) { int[] xp={cx(x)-5,cx(x),cx(x)+5}; int[] yp={cy(y)-2,cy(y)-7,cy(y)-2}; g2.drawPolyline(xp, yp, 3); g2.drawRect(cx(x)-4, cy(y)-2, 8, 7); }
        }
        int cx(int x){return x+8;} int cy(int y){return y+8;}
        public int getIconWidth(){return 16;} public int getIconHeight(){return 16;}
    }

    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        int r; Color c;
        RoundedBorder(int r, Color c){this.r=r;this.c=c;}
        public void paintBorder(Component cmp,Graphics g,int x,int y,int w,int h){
            Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.drawRoundRect(x,y,w-1,h-1,r,r);
        }
    }
}