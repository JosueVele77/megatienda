package view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class ResetPasswordView extends JDialog {

    public JTextField txtEmail;
    public JTextField txtNombre;
    public JTextField txtCedula;
    public JButton btnResetear;
    public JButton btnCancelar;

    public ResetPasswordView(Frame owner) {
        super(owner, "Resetear Acceso de Empleado", true);
        initComponents();
    }

    private void initComponents() {
        setSize(400, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel pnlCentral = new JPanel();
        pnlCentral.setLayout(new BoxLayout(pnlCentral, BoxLayout.Y_AXIS));
        pnlCentral.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Título
        JLabel lblTitulo = new JLabel("Restaurar Cuenta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Ingrese los datos exactos del empleado");
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos
        txtEmail = crearInput(pnlCentral, "Usuario (Email)", "email");
        txtNombre = crearInput(pnlCentral, "Nombre Completo", "user");
        txtCedula = crearInput(pnlCentral, "Cédula (Será la nueva clave)", "card");

        // Botones
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnResetear = new JButton("RESETEAR");
        btnResetear.setBackground(new Color(220, 53, 69)); // Rojo
        btnResetear.setForeground(Color.WHITE);
        btnResetear.setPreferredSize(new Dimension(150, 40));
        btnResetear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnResetear.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth:0");

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 40));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth:0; background: #555; foreground: white");

        pnlBotones.add(btnResetear);
        pnlBotones.add(btnCancelar);

        // Agregar al panel
        pnlCentral.add(lblTitulo);
        pnlCentral.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlCentral.add(lblSub);
        pnlCentral.add(Box.createRigidArea(new Dimension(0, 25)));
        // Los inputs se agregaron en crearInput
        pnlCentral.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlCentral.add(pnlBotones);

        add(pnlCentral, BorderLayout.CENTER);
    }

    private JTextField crearInput(JPanel panel, String label, String icon) {
        JLabel l = new JLabel(label);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(l);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextField t = new JTextField();
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        t.setPreferredSize(new Dimension(300, 40));
        t.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, label);
        t.putClientProperty(FlatClientProperties.STYLE, "arc: 10; showClearButton: true");
        panel.add(t);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        return t;
    }
}