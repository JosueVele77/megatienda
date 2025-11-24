package controller;

import model.entities.Empleado;
import model.logic.EmpleadoLogic;
import view.GestionEmpleadosView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class GestionEmpleadosController implements ActionListener {

    private final GestionEmpleadosView view;
    private final EmpleadoLogic logic;

    public GestionEmpleadosController(GestionEmpleadosView view) {
        this.view = view;
        this.logic = new EmpleadoLogic();

        view.btnRegistrar.addActionListener(this);
        view.btnLimpiar.addActionListener(this);

        cargarTabla();
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnRegistrar) {
            registrar();
        } else if (e.getSource() == view.btnLimpiar) {
            limpiar();
        }
    }

    private void registrar() {
        try {
            String nombre = view.txtNombre.getText();
            String cedula = view.txtCedula.getText();
            String usuario = view.txtUsuario.getText();
            String pass = new String(view.txtPassword.getPassword());
            String rol = (String) view.cmbRol.getSelectedItem();

            if(rol == null) return;

            // Llamada a la lógica según el rol
            switch (rol) {
                case "ADMINISTRADOR" -> logic.registrarAdministrador(usuario, pass, nombre, cedula);
                case "VENDEDOR" -> logic.registrarVendedor(usuario, pass, nombre, cedula);
                case "BODEGUERO" -> logic.registrarBodeguero(usuario, pass, nombre, cedula);
            }

            JOptionPane.showMessageDialog(view, "Empleado registrado correctamente");
            limpiar();
            cargarTabla(); // Refrescar lista

        } catch (IllegalArgumentException | IOException ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        view.modeloTabla.setRowCount(0); // Limpiar
        try {
            List<Empleado> lista = logic.listarTodos(); //
            for (Empleado emp : lista) {
                if (emp != null) {
                    view.modeloTabla.addRow(new Object[]{
                            emp.getRol(),
                            emp.getNombre(),
                            emp.getCedula(),
                            emp.getUsuario()
                    });
                }
            }
        } catch (IOException ex) {
            System.err.println("Error al cargar empleados: " + ex.getMessage());
        }
    }

    private void limpiar() {
        view.txtNombre.setText("");
        view.txtCedula.setText("");
        view.txtUsuario.setText("");
        view.txtPassword.setText("");
        view.cmbRol.setSelectedIndex(0);
    }
}