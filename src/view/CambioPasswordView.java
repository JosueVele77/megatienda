package view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class CambioPasswordView extends JDialog {

    public JPasswordField txtNuevaPass;
    public JPasswordField txtConfirmarPass;
    public JButton btnCambiar;

    public CambioPasswordView(Frame owner) {
        super(owner, "Primer Ingreso - Cambio de Contraseña", true); // Modal true bloquea la ventana anterior
        initComponents();
    }

    private void initComponents() {
        setSize(400, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // No dejar cerrar sin cambiar

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Icono Candado
        JLabel lblIcon = new JLabel("🔒");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Seguridad");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInfo = new JLabel("<html><center>Por seguridad, debe cambiar su contraseña generada automáticamente antes de continuar.</center></html>");
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblInfo.setForeground(Color.GRAY);

        // Campos
        JLabel lblP1 = new JLabel("Nueva Contraseña");
        lblP1.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNuevaPass = new JPasswordField();
        estilizarPass(txtNuevaPass);

        JLabel lblP2 = new JLabel("Confirmar Contraseña");
        lblP2.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtConfirmarPass = new JPasswordField();
        estilizarPass(txtConfirmarPass);

        // Botón
        btnCambiar = new JButton("ACTUALIZAR CLAVE");
        btnCambiar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCambiar.setBackground(new Color(59, 130, 246)); // Azul
        btnCambiar.setForeground(Color.WHITE);
        btnCambiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth: 0");
        btnCambiar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Agregar
        panel.add(lblIcon);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblInfo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(lblP1);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(txtNuevaPass);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

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
    }
}
