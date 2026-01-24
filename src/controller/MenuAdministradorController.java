package controller;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import model.entities.Empleado;
import model.entities.Horario;
import model.entities.Usuario;
import model.logic.EmpleadoLogic;
import model.logic.HorarioLogic;
import view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class MenuAdministradorController implements ActionListener {

    private final MenuAdminView view;
    private final Usuario admin;
    private final EmpleadoLogic empleadoLogic;
    private HorarioLogic horarioLogic;

    public MenuAdministradorController(MenuAdminView view, Usuario admin) {
        this.view = view;
        this.admin = admin;
        this.empleadoLogic = new EmpleadoLogic();
        this.horarioLogic = new HorarioLogic();

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
        this.view.btnVerHorario.addActionListener(this);

        // Listener Tema
        this.view.btnTema.addActionListener(e -> cambiarTema());
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
        // --- AQUÍ ESTABA EL ERROR: AHORA LLAMAMOS A LA VERSIÓN SINGULAR (LA NUEVA) ---
        else if (source == view.btnGestionHorarios) {
            // Usamos GestionHorarioView (Singular), no GestionHorariosView (Plural)
            GestionHorarioView horarioView = new GestionHorarioView(view);
            new GestionHorarioController(horarioView).iniciar();
        }
        // ----------------------------------------------------------------------------
        else if (source == view.btnResetPassword) {
            mostrarDialogoReset();
        }
        else if (source == view.btnSalir) {
            view.dispose();
            new LoginView().setVisible(true);
        }
        else if (source == view.btnVerHorario) {
            verHorarioEmpleado();
        }
    }

    private void cambiarTema() {
        try {
            if (FlatLaf.isLafDark()) {
                UIManager.setLookAndFeel(new FlatMacLightLaf());
            } else {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
            }
            FlatLaf.updateUI();
            view.actualizarColores();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mostrarDialogoReset() {
        ResetPasswordView resetView = new ResetPasswordView(view);
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

    private void verHorarioEmpleado() {
        String cedula = JOptionPane.showInputDialog(view, "Ingrese Cédula del Empleado:");
        if (cedula == null || cedula.trim().isEmpty()) return;

        try {
            List<Horario> lista = horarioLogic.obtenerHorarioEmpleado(cedula);

            String nombre = "Empleado (" + cedula + ")";
            Empleado emp = empleadoLogic.buscarPorCedula(cedula);
            if (emp != null) nombre = emp.getNombre();

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Este empleado no tiene horarios asignados.");
            } else {
                VerHorarioView v = new VerHorarioView(view, lista, nombre);
                v.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
}