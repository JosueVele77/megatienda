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

    private RegistroClienteView view; // Tipo específico para acceder a marcarCampo
    private JTextField txtCedula, txtNombre, txtCorreo, txtTelefono;
    private ClienteLogic clienteLogic;
    private ValidacionesLogic validador;

    public RegistroClienteController(RegistroClienteView view, JTextField txtCedula, JTextField txtNombre, JTextField txtCorreo, JTextField txtTelefono) {
        this.view = view;
        this.txtCedula = txtCedula;
        this.txtNombre = txtNombre;
        this.txtCorreo = txtCorreo;
        this.txtTelefono = txtTelefono; // En la vista es txtDireccion, pero funciona como teléfono
        this.clienteLogic = new ClienteLogic();
        this.validador = new ValidacionesLogic();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();

        boolean todoValido = true;
        StringBuilder msgError = new StringBuilder();

        // 1. Validar Cédula (Ecuador)
        if (validador.validarCedula(cedula)) { //
            view.marcarCampo(txtCedula, true); // Verde
        } else {
            view.marcarCampo(txtCedula, false); // Rojo
            msgError.append("- La cédula es incorrecta o no es ecuatoriana.\n");
            todoValido = false;
        }

        // 2. Validar Teléfono (Ecuador 09...)
        if (validador.validarTelefono(telefono)) {
            view.marcarCampo(txtTelefono, true);
        } else {
            view.marcarCampo(txtTelefono, false);
            msgError.append("- El teléfono debe empezar con 09 y tener 10 dígitos.\n");
            todoValido = false;
        }

        // 3. Validar Nombre
        if (validador.validarNombre(nombre)) {
            view.marcarCampo(txtNombre, true);
        } else {
            view.marcarCampo(txtNombre, false);
            msgError.append("- El nombre contiene caracteres inválidos.\n");
            todoValido = false;
        }

        // 4. Validar Correo
        if (validador.validarEmail(correo)) {
            view.marcarCampo(txtCorreo, true);
        } else {
            view.marcarCampo(txtCorreo, false);
            msgError.append("- El correo no tiene un formato válido.\n");
            todoValido = false;
        }

        // --- RESULTADO FINAL ---
        if (!todoValido) {
            // Mostrar ventana emergente con los errores
            JOptionPane.showMessageDialog(view,
                    "Se encontraron errores en el formulario:\n" + msgError.toString(),
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                // Si todo es verde, procedemos a guardar
                // Nota: Guardamos el teléfono en el campo dirección o creamos un campo nuevo en Cliente si lo prefieres
                Cliente nuevoCliente = new Cliente(cedula, nombre, correo, telefono);
                clienteLogic.registrarCliente(nuevoCliente);

                JOptionPane.showMessageDialog(view, "¡Registro exitoso! Bienvenido " + nombre);
                view.dispose();

            } catch (IllegalArgumentException | IOException ex) {
                JOptionPane.showMessageDialog(view, "Error al guardar: " + ex.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}