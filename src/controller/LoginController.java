package controller;

import model.entities.Usuario;
import model.entities.Empleado; // Importante
import model.logic.LoginLogic;
import view.LoginView;
import view.MenuAdminView;
import view.MenuVendedorView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginController implements ActionListener {

    private final LoginLogic loginLogic;
    private final JFrame viewFrame;
    private final JTextField txtUsuario;
    private final JPasswordField txtPassword;

    public LoginController(JFrame viewFrame, JTextField txtUsuario, JPasswordField txtPassword, JButton btnIngresar) {
        this.loginLogic = new LoginLogic();
        this.viewFrame = viewFrame;
        this.txtUsuario = txtUsuario;
        this.txtPassword = txtPassword;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        try {
            Usuario user = loginLogic.login(usuario, password);

            if (user != null) {
                // --- LÓGICA DE PRIMER INGRESO ---
                if (user.isPrimerIngreso()) {
                    // Abrimos la ventana de cambio de contraseña
                    view.CambioPasswordView passView = new view.CambioPasswordView(viewFrame);
                    new CambioPasswordController(passView, user).iniciar();

                    // Si la cierra sin cambiar (sigue true), no dejamos pasar
                    if (user.isPrimerIngreso()) {
                        JOptionPane.showMessageDialog(viewFrame, "Debe cambiar su clave para continuar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                viewFrame.dispose(); // Cerrar Login
                abrirMenuPrincipal(user);

            } else {
                JOptionPane.showMessageDialog(viewFrame, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(viewFrame, "Error de sistema: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void abrirMenuPrincipal(Usuario user) {
        String rol = user.getRol().toUpperCase();

        // CORRECCIÓN: Convertimos a Empleado para poder usar sus métodos si es necesario
        // Aunque Usuario tiene getUsuario(), para mensajes personalizados es útil.

        if ("ADMINISTRADOR".equals(rol)) {
            MenuAdminView adminView = new MenuAdminView();
            new MenuAdministradorController(adminView, user).iniciar();
        }
        else if ("VENDEDOR".equals(rol)) {
            MenuVendedorView vendedorView = new MenuVendedorView();
            new MenuVendedorController(vendedorView, user).iniciar();
        }
        else if ("BODEGUERO".equals(rol)) {
            // CÓDIGO NUEVO:
            view.MenuBodegueroView bodegaView = new view.MenuBodegueroView();
            new controller.MenuBodegueroController(bodegaView, user).iniciar();
        }
    }
}