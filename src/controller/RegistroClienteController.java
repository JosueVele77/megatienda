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
    }

    public void iniciarListeners() {
        view.btnGuardar.addActionListener(this);
        view.btnCancelar.addActionListener(e -> view.dispose());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnGuardar) {
            guardarCliente();
        }
    }

    private void guardarCliente() {
        // Obtener datos
        String nombre = view.txtNombre.getText().trim();
        String cedula = view.txtCedula.getText().trim();
        String correo = view.txtCorreo.getText().trim();
        String telefono = view.txtTelefono.getText().trim();

        boolean hayError = false;

        // --- VALIDACIONES VISUALES ---

        // 1. Cédula
        if (!validador.validarCedula(cedula)) {
            view.marcarCampoError(view.txtCedula, true);
            hayError = true;
        } else {
            view.marcarCampoError(view.txtCedula, false);
        }

        // 2. Nombre
        if (!validador.validarNombre(nombre)) {
            view.marcarCampoError(view.txtNombre, true);
            hayError = true;
        } else {
            view.marcarCampoError(view.txtNombre, false);
        }

        // 3. Correo
        if (!validador.validarEmail(correo)) {
            view.marcarCampoError(view.txtCorreo, true);
            hayError = true;
        } else {
            view.marcarCampoError(view.txtCorreo, false);
        }

        // 4. Teléfono (Regex simple de 10 dígitos empezando con 09)
        if (!telefono.matches("^09\\d{8}$")) {
            view.marcarCampoError(view.txtTelefono, true);
            hayError = true;
        } else {
            view.marcarCampoError(view.txtTelefono, false);
        }

        // Si hay error, detenemos
        if (hayError) {
            JOptionPane.showMessageDialog(view, "Por favor corrija los campos marcados en rojo.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- GUARDAR ---
        try {
            // Concatenamos dirección y teléfono si tu modelo Cliente aún no tiene campo separado
            // O usamos el campo dirección para guardar el teléfono temporalmente
            Cliente c = new Cliente(cedula, nombre, correo, telefono);

            clienteLogic.registrarCliente(c);

            JOptionPane.showMessageDialog(view, "Cliente registrado exitosamente.");
            view.dispose();

        } catch (IllegalArgumentException | IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}