package controller;

import model.logic.EmpleadoLogic;
// import view.RegistroEmpleadoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegistroEmpleadoController implements ActionListener {

    private final EmpleadoLogic empleadoLogic;
    // private final RegistroEmpleadoView view;

    // Referencias a componentes (reemplaza con tu Vista real)
    private JFrame viewFrame;
    private JTextField txtNombre;
    private JTextField txtCedula;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRol;

    public RegistroEmpleadoController(JFrame viewFrame,
                                      JTextField txtNombre,
                                      JTextField txtCedula,
                                      JTextField txtUsuario,
                                      JPasswordField txtPassword,
                                      JComboBox<String> cmbRol) {
        this.empleadoLogic = new EmpleadoLogic();
        this.viewFrame = viewFrame;
        this.txtNombre = txtNombre;
        this.txtCedula = txtCedula;
        this.txtUsuario = txtUsuario;
        this.txtPassword = txtPassword;
        this.cmbRol = cmbRol;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("REGISTRAR".equals(e.getActionCommand())) {
            registrar();
        } else if ("CANCELAR".equals(e.getActionCommand())) {
            viewFrame.dispose();
        }
    }

    private void registrar() {
        try {
            String nombre = txtNombre.getText();
            String cedula = txtCedula.getText();
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());
            String rol = (String) cmbRol.getSelectedItem();

            if (rol == null) return;

            switch (rol.toUpperCase()) {
                case "ADMINISTRADOR":
                    empleadoLogic.registrarAdministrador(usuario, password, nombre, cedula); //
                    break;
                case "VENDEDOR":
                    empleadoLogic.registrarVendedor(usuario, password, nombre, cedula);
                    break;
                case "BODEGUERO":
                    empleadoLogic.registrarBodeguero(usuario, password, nombre, cedula);
                    break;
                default:
                    JOptionPane.showMessageDialog(viewFrame, "Rol no válido");
                    return;
            }

            JOptionPane.showMessageDialog(viewFrame, "Empleado registrado exitosamente");
            limpiarFormulario();

        } catch (IllegalArgumentException | IOException ex) {
            JOptionPane.showMessageDialog(viewFrame, "Error: " + ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCedula.setText("");
        txtUsuario.setText("");
        txtPassword.setText("");
        cmbRol.setSelectedIndex(0);
    }
}