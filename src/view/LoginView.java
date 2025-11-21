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
    private JButton btnRegistrar; // Nuevo botón

    public LoginView() {
        initComponents();
        configurarEventos();
    }

    private void initComponents() {
        // 1. Ventana más grande para que quepa el logo
        setTitle("Acceso al Sistema - Megatienda");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- 1. LOGO ---
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Cargar imagen
        try {
            URL imgUrl = getClass().getResource("/main/resources/logo.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                // Escalar imagen a 150x150 para que se vea bien
                Image img = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            } else {
                lblLogo.setText("[LOGO AQUÍ]"); // Por si no encuentra la imagen
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo");
        }

        // --- TITULOS ---
        JLabel lblTitulo = new JLabel("Bienvenido");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Inicie sesión para continuar");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- INPUTS ---
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsuario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese su usuario");
        txtUsuario.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        txtUsuario.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "••••••••");
        txtPassword.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

        // --- BOTONES ---
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setPreferredSize(new Dimension(300, 45));
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.putClientProperty(FlatClientProperties.STYLE,
                "background: #007bff; foreground: #ffffff; arc: 10; borderWidth: 0; focusWidth: 0");
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Separador visual
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(300, 10));

        JLabel lblNoCuenta = new JLabel("¿No tienes una cuenta?");
        lblNoCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegistrar = new JButton("Crear cuenta nueva");
        btnRegistrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Estilo de botón "outline" (solo borde) para diferenciarlo del principal
        btnRegistrar.putClientProperty(FlatClientProperties.STYLE,
                "background: null; foreground: #007bff; borderWidth: 0; focusWidth: 0");

        // --- AGREGAR AL PANEL ---
        mainPanel.add(lblLogo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblSubtitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

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
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(btnIngresar);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(separator);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(lblNoCuenta);
        mainPanel.add(btnRegistrar);

        add(mainPanel);
    }

    private void configurarEventos() {
        // Actualizamos el controlador para soportar el botón de registro
        LoginController controller = new LoginController(this, txtUsuario, txtPassword, btnIngresar, btnRegistrar);
        btnIngresar.addActionListener(controller);
        txtPassword.addActionListener(controller);
        btnRegistrar.addActionListener(controller);
    }
}