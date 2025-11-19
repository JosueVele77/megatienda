package controller;

import model.entities.Usuario;
import model.logic.LoginLogic;
// import view.LoginView; // Asumo que existe esta vista
// import view.MenuAdminView;
// import view.MenuEmpleadoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginController implements ActionListener {

    private final LoginLogic loginLogic;
    // private final LoginView view; // Tu clase de vista
    // Referencias temporales a componentes de vista para el ejemplo
    private JFrame viewFrame;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public LoginController(JFrame viewFrame, JTextField txtUsuario, JPasswordField txtPassword) {
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
                JOptionPane.showMessageDialog(viewFrame, "Bienvenido " + user.getUsuario());
                abrirMenuPrincipal(user);
                viewFrame.dispose(); // Cerrar login
            } else {
                JOptionPane.showMessageDialog(viewFrame, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(viewFrame, "Error de acceso a datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirMenuPrincipal(Usuario user) {
        String rol = user.getRol().toUpperCase(); //

        if ("ADMINISTRADOR".equals(rol)) {
            // new MenuAdministradorController(new MenuAdminView(), user).iniciar();
            System.out.println("Abriendo Menú Administrador...");
        } else if ("VENDEDOR".equals(rol) || "BODEGUERO".equals(rol)) {
            // new MenuEmpleadoController(new MenuEmpleadoView(), user).iniciar();
            System.out.println("Abriendo Menú Empleado (" + rol + ")...");
        }
    }
}