package controller;

import model.entities.Usuario;
// import view.MenuEmpleadoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuEmpleadoController implements ActionListener {

    // private final MenuEmpleadoView view;
    private final Usuario empleado;
    private final JFrame viewFrame;

    public MenuEmpleadoController(JFrame viewFrame, Usuario empleado) {
        this.viewFrame = viewFrame;
        this.empleado = empleado;
        configurarPermisos();
    }

    private void configurarPermisos() {
        String rol = empleado.getRol();
        // Aquí ocultarías botones en la vista según el rol
        if ("BODEGUERO".equals(rol)) {
            // view.getBtnVentas().setEnabled(false);
        } else if ("VENDEDOR".equals(rol)) {
            // view.getBtnInventario().setEnabled(false);
        }
    }

    public void iniciar() {
        viewFrame.setVisible(true);
        viewFrame.setTitle("Panel Empleado - " + empleado.getUsuario());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("REALIZAR_VENTA".equals(comando)) {
            // Lógica para abrir pantalla de ventas
        } else if ("SALIR".equals(comando)) {
            viewFrame.dispose();
        }
    }
}