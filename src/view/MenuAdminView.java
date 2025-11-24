package view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class MenuAdminView extends JFrame {

    public JButton btnGestionEmpleados;
    public JButton btnActualizarCliente;
    public JButton btnGestionHorarios;
    public JButton btnResetPassword;
    public JButton btnSalir;
    private JLabel lblUsuarioInfo;

    public MenuAdminView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Panel de Control - Administrador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Megatienda Admin");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        lblUsuarioInfo = new JLabel("Usuario: ...");
        lblUsuarioInfo.setForeground(Color.GRAY);

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(lblUsuarioInfo, BorderLayout.EAST);

        // --- GRID DE OPCIONES ---
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2 filas, 2 columnas
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        btnGestionEmpleados = crearBotonMenu("Gestión de Empleados", "Registrar, listar y editar personal", new Color(0, 123, 255));
        btnActualizarCliente = crearBotonMenu("Actualizar Cliente", "Modificar datos por cédula", new Color(40, 167, 69));
        btnGestionHorarios = crearBotonMenu("Gestión de Horarios", "Asignar turnos semanales", new Color(255, 193, 7));
        btnResetPassword = crearBotonMenu("Resetear Contraseña", "Restaurar acceso a empleados", new Color(220, 53, 69));

        gridPanel.add(btnGestionEmpleados);
        gridPanel.add(btnActualizarCliente);
        gridPanel.add(btnGestionHorarios);
        gridPanel.add(btnResetPassword);

        // --- FOOTER (SALIR) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalir = new JButton("Cerrar Sesión");
        footerPanel.add(btnSalir);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton crearBotonMenu(String titulo, String desc, Color colorBase) {
        JButton btn = new JButton("<html><center><span style='font-size:16px'><b>" + titulo + "</b></span><br/><span style='font-size:10px'>" + desc + "</span></center></html>");
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE); // Texto blanco si el fondo es oscuro (ajustar según flatlaf)

        // Si usas un tema claro de FlatLaf, el texto blanco sobre amarillo (horarios) no se ve bien.
        // Ajuste rapido para el amarillo:
        if (colorBase.equals(new Color(255, 193, 7))) {
            btn.setForeground(Color.DARK_GRAY);
        }

        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 20; margin: 10,10,10,10");
        return btn;
    }

    public void setUsuarioInfo(String info) {
        lblUsuarioInfo.setText(info);
    }
}