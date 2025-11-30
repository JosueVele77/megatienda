package view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class RegistroProveedorView extends JDialog {

    public JTextField txtCodigo, txtRazonSocial, txtContacto, txtTelefono, txtEmail;
    public JButton btnGuardar, btnCancelar;

    public RegistroProveedorView(Frame owner) {
        super(owner, "Registrar Proveedor", true);
        setSize(450, 600);
        setLocationRelativeTo(owner);
        initComponents();
    }

    private void initComponents() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        txtCodigo = addField(pnl, "Código / RUC");
        txtRazonSocial = addField(pnl, "Razón Social");
        txtContacto = addField(pnl, "Nombre de Contacto");
        txtTelefono = addField(pnl, "Teléfono");
        txtEmail = addField(pnl, "Correo Electrónico");

        JPanel pnlBtn = new JPanel();
        btnGuardar = new JButton("REGISTRAR");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnCancelar = new JButton("Cancelar");

        pnlBtn.add(btnGuardar);
        pnlBtn.add(btnCancelar);

        add(new JLabel("  Datos del Proveedor"), BorderLayout.NORTH);
        add(pnl, BorderLayout.CENTER);
        add(pnlBtn, BorderLayout.SOUTH);
    }

    private JTextField addField(JPanel p, String title) {
        p.add(new JLabel(title));
        JTextField t = new JTextField();
        t.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, title);
        t.setPreferredSize(new Dimension(300, 35));
        p.add(t);
        p.add(Box.createVerticalStrut(10));
        return t;
    }
}
