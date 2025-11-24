package view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionEmpleadosView extends JFrame {

    // Formulario
    public JTextField txtNombre, txtCedula, txtUsuario;
    public JPasswordField txtPassword;
    public JComboBox<String> cmbRol;
    public JButton btnRegistrar, btnLimpiar;

    // Tabla
    public JTable tablaEmpleados;
    public DefaultTableModel modeloTabla;

    public GestionEmpleadosView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestión de Empleados");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal dividido: Izquierda (Form), Derecha (Tabla)
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- PANEL IZQUIERDO (Formulario) ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new Dimension(300, 0));
        formPanel.setBorder(BorderFactory.createTitledBorder("Nuevo Empleado"));

        txtNombre = crearInput(formPanel, "Nombre Completo");
        txtCedula = crearInput(formPanel, "Cédula");
        txtUsuario = crearInput(formPanel, "Usuario (Email)");

        // Password
        formPanel.add(new JLabel("Contraseña"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Rol
        formPanel.add(new JLabel("Rol / Perfil"));
        cmbRol = new JComboBox<>(new String[]{"VENDEDOR", "BODEGUERO", "ADMINISTRADOR"});
        formPanel.add(cmbRol);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        btnRegistrar = new JButton("Guardar Empleado");
        btnRegistrar.putClientProperty(FlatClientProperties.STYLE, "background: #28a745; foreground: #fff");
        btnLimpiar = new JButton("Limpiar");

        formPanel.add(btnRegistrar);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(btnLimpiar);

        // --- PANEL DERECHO (Tabla) ---
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Rol");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Cédula");
        modeloTabla.addColumn("Usuario");

        tablaEmpleados = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaEmpleados);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Lista de Empleados"));

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(scrollTabla, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JTextField crearInput(JPanel p, String titulo) {
        p.add(new JLabel(titulo));
        JTextField t = new JTextField();
        p.add(t);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        return t;
    }
}