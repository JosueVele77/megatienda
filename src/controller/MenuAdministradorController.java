package controller;

import model.entities.Usuario;
// import view.MenuAdminView;
// import view.RegistroEmpleadoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAdministradorController implements ActionListener {

    // private final MenuAdminView view;
    private final Usuario administrador;
    private final JFrame viewFrame;

    public MenuAdministradorController(JFrame viewFrame, Usuario administrador) {
        this.viewFrame = viewFrame;
        this.administrador = administrador;
    }

    public void iniciar() {
        viewFrame.setVisible(true);
        viewFrame.setTitle("Panel Administrador - " + administrador.getUsuario());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            case "GESTIONAR_EMPLEADOS":
                abrirRegistroEmpleado();
                break;
            case "GESTIONAR_PRODUCTOS":
                // abrirGestionProductos();
                System.out.println("Abrir gestión productos");
                break;
            case "SALIR":
                viewFrame.dispose();
                // new LoginController(...).iniciar();
                break;
            default:
                System.out.println("Comando no reconocido: " + comando);
        }
    }

    private void abrirRegistroEmpleado() {
        // Ejemplo de cómo conectarías con el siguiente controlador
        // RegistroEmpleadoView regView = new RegistroEmpleadoView();
        // new RegistroEmpleadoController(regView).iniciar();
        System.out.println("Abriendo Registro de Empleados...");
    }
}