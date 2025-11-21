package controller;

import model.entities.Usuario;
import model.logic.LoginLogic;
import view.LoginView;
import view.RegistroClienteView; // Importamos la nueva vista

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginController implements ActionListener {

    private final LoginLogic loginLogic;
    private JFrame viewFrame;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;   // Referencia para identificar origen
    private JButton btnRegistrar;  // Referencia para identificar origen

    // Constructor actualizado
    public LoginController(JFrame viewFrame, JTextField txtUsuario, JPasswordField txtPassword,
                           JButton btnIngresar, JButton btnRegistrar) {
        this.loginLogic = new LoginLogic();
        this.viewFrame = viewFrame;
        this.txtUsuario = txtUsuario;
        this.txtPassword = txtPassword;
        this.btnIngresar = btnIngresar;
        this.btnRegistrar = btnRegistrar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Si le dieron click a "Registrar"
        if (e.getSource() == btnRegistrar) {
            new RegistroClienteView().setVisible(true);
            return; // Salimos del método
        }

        // Si fue Ingresar o Enter en password
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        try {
            Usuario user = loginLogic.login(usuario, password);

            if (user != null) {
                JOptionPane.showMessageDialog(viewFrame, "Bienvenido " + user.getUsuario());
                abrirMenuPrincipal(user);
                viewFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(viewFrame, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(viewFrame, "Error de acceso a datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirMenuPrincipal(Usuario user) {
        // (Mantener tu lógica existente aquí)
        String rol = user.getRol().toUpperCase();
        System.out.println("Login exitoso: " + rol);
    }
}