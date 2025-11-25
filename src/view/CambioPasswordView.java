package view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CambioPasswordView extends JDialog {

    public JPasswordField txtNuevaPass;
    public JPasswordField txtConfirmarPass;
    public JButton btnCambiar;

    public CambioPasswordView(Frame owner) {
        super(owner, "Primer Ingreso - Cambio de Contraseña", true);
        initComponents();
    }

    private void initComponents() {
        setSize(400, 480);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        // Si usas tema oscuro, esto ayuda al fondo
        panel.setBackground(UIManager.getColor("Panel.background"));

        JLabel lblIcon = new JLabel("🔒");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Seguridad");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInfo = new JLabel("<html><center>Por seguridad, actualice su contraseña<br>antes de continuar.</center></html>");
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblInfo.setForeground(Color.GRAY);

        // --- INPUTS ---
        JLabel lblP1 = new JLabel("Nueva Contraseña");
        lblP1.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtNuevaPass = new JPasswordField();
        estilizarPass(txtNuevaPass);

        JLabel lblP2 = new JLabel("Confirmar Contraseña");
        lblP2.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtConfirmarPass = new JPasswordField();
        estilizarPass(txtConfirmarPass);

        // --- BOTÓN ---
        btnCambiar = new JButton("ACTUALIZAR CLAVE");
        btnCambiar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCambiar.setBackground(new Color(59, 130, 246));
        btnCambiar.setForeground(Color.WHITE);
        btnCambiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth: 0");
        btnCambiar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // --- AGREGAR ---
        panel.add(lblIcon);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblInfo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(lblP1);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(txtNuevaPass);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(lblP2);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(txtConfirmarPass);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(btnCambiar);

        add(panel);
    }

    private void estilizarPass(JPasswordField txt) {
        txt.setPreferredSize(new Dimension(300, 40));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.putClientProperty(FlatClientProperties.STYLE, "arc: 10; showRevealButton: true");
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void marcarError(JPasswordField campo, boolean hayError) {
        if (hayError) {
            campo.putClientProperty(FlatClientProperties.OUTLINE, "error");
        } else {
            campo.putClientProperty(FlatClientProperties.OUTLINE, null);
        }
        campo.repaint();
    }

    // --- NUEVO: MÉTODO PARA MOSTRAR EL TOAST (VENTANA FLOTANTE ROJA) ---
    public void mostrarErrorToast(String mensaje) {
        JWindow toast = new JWindow(this);
        // Hacemos el fondo transparente para que se vean los bordes redondeados
        toast.setBackground(new Color(0,0,0,0));

        JPanel content = new JPanel(new BorderLayout(15, 0));
        // Color Rojo Oscuro estilo Material Design
        content.setBackground(new Color(60, 20, 20));
        content.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(220, 53, 69)), // Borde rojo brillante
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Icono X
        JLabel lblIcon = new JLabel("✕");
        lblIcon.setForeground(new Color(220, 53, 69)); // Rojo brillante
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // Texto
        JLabel lblText = new JLabel("<html><div style='color:#ffaaaa'><b>Error</b><br/>" + mensaje + "</div></html>");
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        content.add(lblIcon, BorderLayout.WEST);
        content.add(lblText, BorderLayout.CENTER);

        toast.setContentPane(content);
        toast.pack();

        // Calcular posición (Arriba a la derecha de la ventana padre)
        Point loc = this.getLocationOnScreen();
        toast.setLocation(loc.x + this.getWidth() - toast.getWidth() + 20, loc.y + 30);

        toast.setVisible(true);

        // Desaparecer a los 4 segundos
        Timer t = new Timer(4000, e -> toast.dispose());
        t.setRepeats(false);
        t.start();
    }

    // Clase auxiliar para bordes redondeados
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