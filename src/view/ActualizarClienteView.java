package view;

import com.formdev.flatlaf.FlatClientProperties;
import controller.ActualizarClienteController;
import model.entities.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ActualizarClienteView extends JFrame {

    // Componentes de Búsqueda
    public JTextField txtBusqueda;
    public JButton btnBuscar;

    // Componentes de Datos (Panel oculto inicialmente)
    public JPanel pnlDatos;
    public JTextField txtNombres, txtEmail, txtTelefono, txtDireccion;
    public JTextField txtCedulaInmutable; // Campo bloqueado
    public JButton btnActualizar, btnCancelar;

    // Colores
    private final Color COLOR_ACCENT = new Color(59, 130, 246); // Azul
    private final Color COLOR_BG_SECONDARY = new Color(45, 45, 50);

    public ActualizarClienteView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Actualizar Cliente");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- 1. SECCIÓN DE BÚSQUEDA (Siempre visible) ---
        JLabel lblTitulo = new JLabel("Buscar Cliente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pnlBusqueda = new JPanel(new BorderLayout(10, 0));
        pnlBusqueda.setMaximumSize(new Dimension(400, 50));
        pnlBusqueda.setOpaque(false);

        txtBusqueda = new JTextField();
        estilizarCampo(txtBusqueda, "Ingrese Cédula", "search");

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(COLOR_ACCENT);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth: 0");
        btnBuscar.setPreferredSize(new Dimension(100, 40));

        pnlBusqueda.add(txtBusqueda, BorderLayout.CENTER);
        pnlBusqueda.add(btnBuscar, BorderLayout.EAST);

        // --- 2. SECCIÓN DE DATOS (Oculta al inicio) ---
        pnlDatos = new JPanel();
        pnlDatos.setLayout(new BoxLayout(pnlDatos, BoxLayout.Y_AXIS));
        pnlDatos.setBackground(COLOR_BG_SECONDARY);
        pnlDatos.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(20, COLOR_BG_SECONDARY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        pnlDatos.setVisible(false); // ¡Secreto!

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
        btnCancelar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #555; foreground: white; borderWidth: 0");
        btnCancelar.setPreferredSize(new Dimension(100, 45));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnCancelar);
        pnlDatos.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlDatos.add(pnlBotones);

        // --- AGREGAR AL PRINCIPAL ---
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(pnlBusqueda);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(pnlDatos); // Panel oculto
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    // --- Helpers de Diseño ---

    public void mostrarFormulario(boolean mostrar) {
        pnlDatos.setVisible(mostrar);
        this.revalidate();
        this.repaint();
        // Ajustar tamaño dinámicamente si es necesario
        if(mostrar) this.setSize(500, 700);
        else this.setSize(500, 250);
    }

    public void cargarDatos(Cliente c) {
        txtCedulaInmutable.setText(c.getCedula());
        txtNombres.setText(c.getNombre());
        txtEmail.setText(c.getCorreo());

        // Como tu clase Cliente solo tiene "Direccion", asumiremos que ahí guardaste todo
        // O separamos si concatenaste. Por ahora cargamos la dirección.
        txtDireccion.setText(c.getDireccion());
        txtTelefono.setText(""); // Campo vacío para que lo llenen si desean actualizar
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
        t.setEnabled(false); // Visualmente bloqueado
        t.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #333; foreground: #888");
        return t;
    }

    private void estilizarCampo(JTextField field, String ph, String icon) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 999; margin: 0,10,0,10; outlineWidth: 2");
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, ph);
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new SimpleIcon(icon));
    }

    // --- Notificaciones ---
    public void mostrarToast(String msg, boolean error) {
        // Reutiliza la lógica del Toast que te di en la respuesta anterior
        // Si quieres te la copio aquí de nuevo, es el método mostrarToast() con JWindow
        JOptionPane.showMessageDialog(this, msg, error ? "Error" : "Éxito", error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    // --- Clase Iconos (SimpleIcon) ---
    // (La misma clase SimpleIcon de la respuesta anterior para mantener consistencia)
    private static class SimpleIcon implements Icon {
        String t; SimpleIcon(String t){this.t=t;}
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY); g2.setStroke(new BasicStroke(2));
            if(t.equals("search")) g2.drawOval(x+4,y+4,8,8);
            if(t.equals("search")) g2.drawLine(x+10,y+10,x+14,y+14);
            // ... resto de iconos (user, email, home, lock) ...
            if(t.equals("user")) { g2.drawArc(x+6, y+4, 4, 4, 0, 360); g2.drawArc(x+4, y+10, 8, 6, 0, 180); }
            if(t.equals("lock")) { g2.drawRect(x+5, y+8, 6, 6); g2.drawArc(x+6, y+4, 4, 6, 0, 180); }
        }
        public int getIconWidth(){return 16;} public int getIconHeight(){return 16;}
    }

    // Borde Redondeado (Igual que el anterior)
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        int r; Color c; RoundedBorder(int r, Color c){this.r=r;this.c=c;}
        public void paintBorder(Component cmp,Graphics g,int x,int y,int w,int h){
            Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.drawRoundRect(x,y,w-1,h-1,r,r);
        }
    }
}