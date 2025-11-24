package controller;

import model.entities.Empleado; // <--- IMPORTANTE: Agregar este import
import model.entities.Usuario;
import view.MenuAdminView;
import view.GestionEmpleadosView;
import view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAdministradorController implements ActionListener {

    private final MenuAdminView view;
    private final Usuario admin;

    public MenuAdministradorController(MenuAdminView view, Usuario admin) {
        this.view = view;
        this.admin = admin;

        // --- CORRECCIÓN DEL ERROR ---
        // Verificamos si es un Empleado para poder usar getNombre()
        String nombreMostrar = admin.getUsuario(); // Valor por defecto (el correo/user)

        if (admin instanceof Empleado) {
            // Hacemos el CASTING ((Empleado) admin) para acceder a los métodos de Empleado
            nombreMostrar = ((Empleado) admin).getNombre();
        }

        view.setUsuarioInfo("Admin: " + nombreMostrar);
        // -----------------------------

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
            JOptionPane.showMessageDialog(view, "Módulo Actualizar Cliente - Próximamente");
        }
        else if (source == view.btnGestionHorarios) {
            JOptionPane.showMessageDialog(view, "Módulo Horarios - Próximamente");
        }
        else if (source == view.btnResetPassword) {
            JOptionPane.showMessageDialog(view, "Use el módulo 'Gestión de Empleados' para resetear.");
        }
        else if (source == view.btnSalir) {
            view.dispose();
            new LoginView().setVisible(true);
        }
    }
}