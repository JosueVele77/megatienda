package controller;

import model.entities.Empleado;
import model.logic.EmpleadoLogic;
import model.logic.ValidacionesLogic;
import view.GestionEmpleadosView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class GestionEmpleadosController implements ActionListener {

    private final GestionEmpleadosView view;
    private final EmpleadoLogic empleadoLogic;
    private final ValidacionesLogic validador;

    public GestionEmpleadosController(GestionEmpleadosView view) {
        this.view = view;
        this.empleadoLogic = new EmpleadoLogic();
        this.validador = new ValidacionesLogic();

        view.btnGuardar.addActionListener(this);
        view.btnLimpiar.addActionListener(this);

        cargarTabla();
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnGuardar) {
            guardarEmpleado();
        } else if (e.getSource() == view.btnLimpiar) {
            limpiarFormulario();
        }
    }

    private void guardarEmpleado() {
        // Recoger datos
        String codigo = view.txtCodigo.getText().trim();
        String nombres = view.txtNombres.getText().trim();
        String cedula = view.txtCedula.getText().trim();
        String email = view.txtEmail.getText().trim();
        String tConv = view.txtTelConvencional.getText().trim();
        String tCel = view.txtCelular.getText().trim();
        String direccion = view.txtDireccion.getText().trim();
        String fecha = view.txtFechaIngreso.getText().trim();
        String rol = (String) view.cmbRol.getSelectedItem();

        boolean hayError = false;

        // --- VALIDACIONES CON EFECTO VISUAL ---

        // 1. Cédula
        if (!validador.validarCedula(cedula)) { //
            view.marcarCampoError(view.txtCedula, true);
            hayError = true;
        } else {
            view.marcarCampoError(view.txtCedula, false);
        }

        // 2. Nombres
        if (!validador.validarNombre(nombres)) {
            view.marcarCampoError(view.txtNombres, true);
            hayError = true;
        } else {
            view.marcarCampoError(view.txtNombres, false);
        }

        // 3. Email
        if (!validador.validarEmail(email)) {
            view.marcarCampoError(view.txtEmail, true);
            hayError = true;
        } else {
            view.marcarCampoError(view.txtEmail, false);
        }

        // 4. Celular (Validamos que sea números y longitud)
        if (!tCel.matches("^09\\d{8}$")) {
            view.marcarCampoError(view.txtCelular, true);
            hayError = true;
        } else {
            view.marcarCampoError(view.txtCelular, false);
        }

        // Si hay errores, mostramos el TOAST y detenemos
        if (hayError) {
            view.mostrarErrorFlotante("Revise los campos marcados en rojo.<br/>Cédula o datos inválidos.");
            return;
        }

        // --- GUARDAR ---
        try {
            // Contraseña por defecto: cédula
            String passwordDefault = cedula;

            // NOTA: Aquí estoy usando el User como Email, tú puedes cambiarlo si prefieres otro usuario
            switch (rol) {
                case "ADMINISTRADOR":
                    empleadoLogic.registrarAdministrador(email, passwordDefault, nombres, cedula);
                    break;
                case "VENDEDOR":
                    empleadoLogic.registrarVendedor(email, passwordDefault, nombres, cedula);
                    break;
                case "BODEGUERO":
                    empleadoLogic.registrarBodeguero(email, passwordDefault, nombres, cedula);
                    break;
            }

            view.mostrarExitoFlotante("Empleado " + nombres + " registrado con éxito.");
            limpiarFormulario();
            cargarTabla();

        } catch (IllegalArgumentException | IOException ex) {
            view.mostrarErrorFlotante("Error al guardar: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        view.modeloTabla.setRowCount(0);
        try {
            List<Empleado> lista = empleadoLogic.listarTodos(); //
            for (Empleado emp : lista) {
                if (emp != null) {
                    view.modeloTabla.addRow(new Object[]{
                            emp.getRol(),
                            emp.getNombre(),
                            emp.getCedula(),
                            emp.getUsuario() // Mostramos el usuario/email
                    });
                }
            }
        } catch (IOException ex) {
            System.err.println("Error cargando tabla: " + ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        view.txtCodigo.setText("");
        view.txtNombres.setText("");
        view.txtCedula.setText("");
        view.txtEmail.setText("");
        view.txtTelConvencional.setText("");
        view.txtCelular.setText("");
        view.txtDireccion.setText("");
        view.txtFechaIngreso.setText("");
        view.cmbRol.setSelectedIndex(0);

        // Limpiar errores visuales
        view.marcarCampoError(view.txtCedula, false);
        view.marcarCampoError(view.txtNombres, false);
        view.marcarCampoError(view.txtEmail, false);
        view.marcarCampoError(view.txtCelular, false);
    }
}