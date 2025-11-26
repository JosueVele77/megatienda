package controller;

import model.logic.EmpleadoLogic;
// import view.RegistroEmpleadoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

public class RegistroEmpleadoController implements ActionListener {

    private final EmpleadoLogic empleadoLogic;
    // private final RegistroEmpleadoView view;

    // Referencias a componentes
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

            // DATOS POR DEFECTO para evitar el error (ya que este formulario no los pide)
            String celularDefault = "0999999999";
            String direccionDefault = "Sin dirección";
            String fechaDefault = LocalDate.now().toString();

            switch (rol.toUpperCase()) {
                case "ADMINISTRADOR":
                    // CORRECCIÓN: Enviamos los 7 parámetros
                    empleadoLogic.registrarAdministrador(usuario, password, nombre, cedula, celularDefault, direccionDefault, fechaDefault);
                    break;
                case "VENDEDOR":
                    empleadoLogic.registrarVendedor(usuario, password, nombre, cedula, celularDefault, direccionDefault, fechaDefault);
                    break;
                case "BODEGUERO":
                    empleadoLogic.registrarBodeguero(usuario, password, nombre, cedula, celularDefault, direccionDefault, fechaDefault);
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