package controller;

import model.entities.Empleado;
import model.entities.Usuario;
import model.logic.EmpleadoLogic; // Importar Logic
import view.ActualizarClienteView;
import view.MenuAdminView;
import view.GestionEmpleadosView;
import view.LoginView;
import view.ResetPasswordView; // Importar nueva vista

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuAdministradorController implements ActionListener {

    private final MenuAdminView view;
    private final Usuario admin;
    private final EmpleadoLogic empleadoLogic; // Instancia lógica

    public MenuAdministradorController(MenuAdminView view, Usuario admin) {
        this.view = view;
        this.admin = admin;
        this.empleadoLogic = new EmpleadoLogic(); // Inicializar

        // Nombre usuario
        String nombreMostrar = admin.getUsuario();
        if (admin instanceof Empleado) {
            nombreMostrar = ((Empleado) admin).getNombre();
        }
        view.setUsuarioInfo("Admin: " + nombreMostrar);

        // Listeners
        this.view.btnGestionEmpleados.addActionListener(this);
        this.view.btnActualizarCliente.addActionListener(this);
        this.view.btnGestionHorarios.addActionListener(this);
        this.view.btnResetPassword.addActionListener(this);
        this.view.btnSalir.addActionListener(this);
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.btnGestionEmpleados) {
            GestionEmpleadosView empleadosView = new GestionEmpleadosView();
            new GestionEmpleadosController(empleadosView).iniciar();
        }
        else if (source == view.btnActualizarCliente) {
            ActualizarClienteView actView = new ActualizarClienteView();
            new ActualizarClienteController(actView).iniciar();
        }
        else if (source == view.btnGestionHorarios) {
            JOptionPane.showMessageDialog(view, "Módulo Horarios - Próximamente");
        }
        // --- AQUÍ ESTÁ EL CAMBIO ---
        else if (source == view.btnResetPassword) {
            mostrarDialogoReset();
        }
        else if (source == view.btnSalir) {
            view.dispose();
            new LoginView().setVisible(true);
        }
    }

    private void mostrarDialogoReset() {
        ResetPasswordView resetView = new ResetPasswordView(view);

        // Acción del botón Resetear dentro del diálogo
        resetView.btnResetear.addActionListener(evt -> {
            String email = resetView.txtEmail.getText().trim();
            String nombre = resetView.txtNombre.getText().trim();
            String cedula = resetView.txtCedula.getText().trim();

            if (email.isEmpty() || nombre.isEmpty() || cedula.isEmpty()) {
                JOptionPane.showMessageDialog(resetView, "Todos los campos son obligatorios", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                boolean exito = empleadoLogic.resetearClave(email, nombre, cedula);
                if (exito) {
                    JOptionPane.showMessageDialog(resetView,
                            "¡Éxito! La contraseña se ha restablecido a la Cédula.\n" +
                                    "El usuario deberá cambiarla en su próximo inicio de sesión.");
                    resetView.dispose();
                } else {
                    JOptionPane.showMessageDialog(resetView,
                            "No se encontró ningún empleado con esos datos exactos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(resetView, "Error de archivo: " + ex.getMessage());
            }
        });

        resetView.btnCancelar.addActionListener(evt -> resetView.dispose());

        resetView.setVisible(true);
    }
}