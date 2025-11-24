package controller;

import model.entities.Usuario;
import model.logic.LoginLogic;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginController implements ActionListener {

    private final LoginLogic loginLogic;
    private JFrame viewFrame;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

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
            Usuario user = loginLogic.login(usuario, password); //

            if (user != null) {
                viewFrame.dispose();
                abrirMenuPrincipal(user);
            } else {
                JOptionPane.showMessageDialog(viewFrame, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(viewFrame, "Error de sistema: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirMenuPrincipal(Usuario user) {
        String rol = user.getRol().toUpperCase();

        if ("ADMINISTRADOR".equals(rol)) {
            // Abrimos el menú que crearemos a continuación
            new MenuAdministradorController(new view.MenuAdminView(), user).iniciar();
        } else {
            // Lógica futura para Bodeguero/Vendedor
            JOptionPane.showMessageDialog(null, "Bienvenido " + user.getUsuario() + " (Panel " + rol + " en construcción)");
        }
    }
}