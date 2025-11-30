package controller;

import model.entities.Cliente;
import model.logic.ClienteLogic;
import model.logic.ValidacionesLogic;
import view.RegistroClienteView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegistroClienteController implements ActionListener {

    private final RegistroClienteView view;
    private final ClienteLogic clienteLogic;
    private final ValidacionesLogic validador;

    public RegistroClienteController(RegistroClienteView view) {
        this.view = view;
        this.clienteLogic = new ClienteLogic();
        this.validador = new ValidacionesLogic();
        this.view.btnGuardar.addActionListener(this);
        this.view.btnCancelar.addActionListener(e -> view.dispose());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnGuardar) {
            guardarCliente();
        }
    }

    private void guardarCliente() {
        String nombre = view.txtNombre.getText().trim();
        String cedula = view.txtCedula.getText().trim();
        String correo = view.txtCorreo.getText().trim();
        String telefono = view.txtTelefono.getText().trim();
        String direccion = view.txtDireccion.getText().trim(); // <--- Recoger Dirección

        boolean hayError = false;

        // Validaciones Visuales
        if (!validador.validarCedula(cedula)) {
            view.marcarCampoError(view.txtCedula, true);
            hayError = true;
        } else view.marcarCampoError(view.txtCedula, false);

        if (!validador.validarNombre(nombre)) {
            view.marcarCampoError(view.txtNombre, true);
            hayError = true;
        } else view.marcarCampoError(view.txtNombre, false);

        if (!validador.validarEmail(correo)) {
            view.marcarCampoError(view.txtCorreo, true);
            hayError = true;
        } else view.marcarCampoError(view.txtCorreo, false);

        if (!telefono.matches("^09\\d{8}$")) {
            view.marcarCampoError(view.txtTelefono, true);
            hayError = true;
        } else view.marcarCampoError(view.txtTelefono, false);

        // Validación simple para dirección (solo que no esté vacía)
        if (direccion.isEmpty()) {
            view.marcarCampoError(view.txtDireccion, true);
            hayError = true;
        } else view.marcarCampoError(view.txtDireccion, false);

        if (hayError) {
            JOptionPane.showMessageDialog(view, "Por favor corrija los campos marcados.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Constructor con 5 parámetros
            Cliente c = new Cliente(cedula, nombre, correo, telefono, direccion);
            clienteLogic.registrarCliente(c);

            JOptionPane.showMessageDialog(view, "Cliente registrado exitosamente.");
            view.dispose();

        } catch (IllegalArgumentException | IOException ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}