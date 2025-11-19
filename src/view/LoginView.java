package view;

import com.formdev.flatlaf.FlatClientProperties;
import controller.LoginController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public LoginView() {
        initComponents();
        configurarEventos();
    }

    private void initComponents() {
        // 1. Configuración básica de la ventana
        setTitle("Acceso al Sistema - Megatienda");
        setSize(400, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);

        // 2. Panel Principal con márgenes (Padding)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // --- LOGO / TITULO ---
        JLabel lblTitulo = new JLabel("Bienvenido");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Inicie sesión para continuar");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- CAMPO USUARIO ---
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        // Estilo FlatLaf: Placeholder y bordes redondeados
        txtUsuario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese su usuario");
        txtUsuario.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        txtUsuario.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        // --- CAMPO PASSWORD ---
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        // Estilo FlatLaf: Botón de "ver contraseña" y bordes redondeados
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "••••••••");
        txtPassword.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, true);
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

        // --- BOTÓN INGRESAR ---
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setPreferredSize(new Dimension(300, 45));
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Estilo FlatLaf: Resaltar botón como principal (generalmente azul)
        btnIngresar.putClientProperty(FlatClientProperties.STYLE,
                "background: #007bff; foreground: #ffffff; arc: 10; borderWidth: 0; focusWidth: 0");

        // --- AGREGAR COMPONENTES AL PANEL CON ESPACIADO ---
        mainPanel.add(Box.createVerticalGlue()); // Empujar contenido al centro vert
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblSubtitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Espacio

        // Contenedor para alinear etiquetas a la izquierda
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
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    private void configurarEventos() {
        // Conectamos con el Controlador que hicimos anteriormente
        // Pasamos 'this' (la vista), y los campos de texto
        LoginController controller = new LoginController(this, txtUsuario, txtPassword);

        btnIngresar.addActionListener(controller);

        // Permitir dar ENTER en el campo de password para ingresar
        txtPassword.addActionListener(controller);
    }
}