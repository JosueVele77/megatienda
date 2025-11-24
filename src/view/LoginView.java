package view;

import com.formdev.flatlaf.FlatClientProperties;
import controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public LoginView() {
        initComponents();
        configurarEventos();
    }

    private void initComponents() {
        setTitle("Megatienda - Acceso Interno");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- LOGO ---
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            URL imgUrl = getClass().getResource("/images/logo.png"); // Asegúrate de tener la imagen ahí
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) { /* Ignorar */ }

        JLabel lblTitulo = new JLabel("Bienvenido");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- INPUTS ---
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsuario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "admin@gmail.com");
        txtUsuario.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "••••••••");
        txtPassword.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

        // --- BOTÓN INGRESAR ---
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setPreferredSize(new Dimension(300, 45));
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.putClientProperty(FlatClientProperties.STYLE,
                "background: #007bff; foreground: #ffffff; arc: 10; borderWidth: 0");
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- AGREGAR ---
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(lblLogo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Contenedor inputs
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        formContainer.add(lblUser);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(txtUsuario);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        formContainer.add(lblPass);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(txtPassword);

        mainPanel.add(formContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(btnIngresar);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    private void configurarEventos() {
        // Controlador simplificado (ya no recibe el botón registrar)
        LoginController controller = new LoginController(this, txtUsuario, txtPassword, btnIngresar);
        btnIngresar.addActionListener(controller);
        txtPassword.addActionListener(controller);
    }
}