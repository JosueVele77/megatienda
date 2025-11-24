package controller;

import model.entities.Cliente;
import model.logic.ClienteLogic;
import model.logic.ValidacionesLogic;
import view.ActualizarClienteView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ActualizarClienteController implements ActionListener {

    private final ActualizarClienteView view;
    private final ClienteLogic clienteLogic;
    private final ValidacionesLogic validador;
    private Cliente clienteActual; // Para guardar la referencia del cliente encontrado

    public ActualizarClienteController(ActualizarClienteView view) {
        this.view = view;
        this.clienteLogic = new ClienteLogic();
        this.validador = new ValidacionesLogic();

        // Listeners
        this.view.btnBuscar.addActionListener(this);
        this.view.btnActualizar.addActionListener(this);
        this.view.btnCancelar.addActionListener(e -> view.dispose());

        // Iniciar con formulario oculto
        this.view.mostrarFormulario(false);
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnBuscar) {
            buscarCliente();
        } else if (e.getSource() == view.btnActualizar) {
            guardarCambios();
        }
    }

    private void buscarCliente() {
        String cedula = view.txtBusqueda.getText().trim();

        if (cedula.isEmpty()) {
            view.mostrarToast("Ingrese una cédula", true);
            return;
        }

        try {
            // Usamos la lógica existente para buscar
            clienteActual = clienteLogic.buscarCliente(cedula);

            if (clienteActual != null) {
                // ¡Cliente Encontrado!
                view.cargarDatos(clienteActual);
                view.mostrarFormulario(true); // Desplegar formulario
                view.mostrarToast("Cliente encontrado", false);
            } else {
                // Cliente no existe
                view.mostrarFormulario(false);
                view.mostrarToast("Cliente no encontrado", true);
            }

        } catch (IOException ex) {
            view.mostrarToast("Error al buscar: " + ex.getMessage(), true);
        }
    }

    private void guardarCambios() {
        if (clienteActual == null) return;

        String nuevosNombres = view.txtNombres.getText().trim();
        String nuevoEmail = view.txtEmail.getText().trim();
        String nuevaDireccion = view.txtDireccion.getText().trim();
        String nuevoTelefono = view.txtTelefono.getText().trim();

        // Validaciones
        if (nuevosNombres.isEmpty() || nuevoEmail.isEmpty()) {
            view.mostrarToast("Complete los campos obligatorios", true);
            return;
        }
        if (!validador.validarEmail(nuevoEmail)) {
            view.mostrarToast("Email inválido", true);
            return;
        }

        // Crear objeto actualizado (Mantenemos la cédula original)
        // Nota: Concatenamos dirección y teléfono si tu modelo no tiene campo teléfono separado
        String dirFinal = nuevaDireccion + " | Telf: " + nuevoTelefono;

        Cliente clienteActualizado = new Cliente(
                clienteActual.getCedula(), // NO CAMBIA
                nuevosNombres,
                nuevoEmail,
                dirFinal
        );

        try {
            boolean exito = clienteLogic.actualizarCliente(clienteActualizado); //

            if (exito) {
                view.mostrarToast("Cliente actualizado correctamente", false);
                view.dispose(); // Cerrar ventana al terminar
            } else {
                view.mostrarToast("No se pudo actualizar el registro", true);
            }
        } catch (IOException ex) {
            view.mostrarToast("Error de escritura: " + ex.getMessage(), true);
        }
    }
}