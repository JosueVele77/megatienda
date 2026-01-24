package controller;

import model.entities.Empleado;
import model.entities.Horario;
import model.entities.Turno;
import model.logic.EmpleadoLogic;
import model.logic.HorarioLogic;
import model.logic.ValidacionesLogic;
import view.GestionEmpleadosView;
import view.VerHorarioView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GestionEmpleadosController implements ActionListener {

    private final GestionEmpleadosView view;
    private final EmpleadoLogic empleadoLogic;
    private final HorarioLogic horarioLogic;
    private final ValidacionesLogic validador;

    public GestionEmpleadosController(GestionEmpleadosView view) {
        this.view = view;
        this.empleadoLogic = new EmpleadoLogic();
        this.horarioLogic = new HorarioLogic();
        this.validador = new ValidacionesLogic();

        view.btnGuardar.addActionListener(this);
        view.btnLimpiar.addActionListener(this);
        view.btnVerHorario.addActionListener(this);

        cargarTabla();
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == view.btnGuardar) {
            guardarEmpleado();
        } else if (source == view.btnLimpiar) {
            limpiarFormulario();
        } else if (source == view.btnVerHorario) { // <--- Lógica botón
            verHorario();
        }
    }

    // --- LÓGICA PARA VER EL HORARIO ---
    private void verHorario() {
        // 1. Obtener la cédula del campo de texto
        String cedula = view.txtCedula.getText().trim();

        if (cedula.isEmpty()) {
            view.mostrarErrorFlotante("Por favor, ingrese una cédula o seleccione un empleado.");
            return;
        }

        try {
            // 2. Buscar si el empleado existe (para obtener su nombre)
            String nombreEmpleado = "Desconocido";
            List<Empleado> empleados = empleadoLogic.listarTodos();
            boolean existe = false;

            for(Empleado emp : empleados) {
                if(emp.getCedula().equals(cedula)) {
                    nombreEmpleado = emp.getNombre();
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                view.mostrarErrorFlotante("No se encontró ningún empleado con la cédula: " + cedula);
                return;
            }

            // 3. Buscar el Horario (Ahora sí funciona porque actualizamos HorarioLogic)
            Horario h = horarioLogic.buscarPorEmpleado(cedula);

            // 4. Si no tiene horario, crear uno "Default" visual (sin guardar en BD)
            if (h == null) {
                // Ahora sí funciona porque actualizamos el constructor de Horario
                h = new Horario(cedula, Turno.MATUTINO, "", "");
                view.mostrarExitoFlotante("Este empleado no tiene horario asignado. Se muestra vacío.");
            }

            // 5. Abrir la ventana de Visualización
            VerHorarioView visualizador = new VerHorarioView(view, Collections.singletonList(h), nombreEmpleado);
            visualizador.setVisible(true);

        } catch (IOException ex) {
            view.mostrarErrorFlotante("Error al consultar datos: " + ex.getMessage());
        }
    }

    private void guardarEmpleado() {
        // 1. Recoger TODOS los datos
        String nombres = view.txtNombres.getText().trim();
        String cedula = view.txtCedula.getText().trim();
        String email = view.txtEmail.getText().trim();
        String celular = view.txtCelular.getText().trim(); // Solo celular
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
        if (!celular.matches("^09\\d{8}$")) {
            view.mostrarErrorFlotante("El celular debe tener 10 dígitos y empezar con 09.");
            return;
        }

        try {
            String passwordDefault = cedula;

            // 2. ENVIAR TODOS LOS DATOS A LA LÓGICA
            switch (rol) {
                case "ADMINISTRADOR":
                    // Asumiendo que actualizaste el método registrarAdministrador también
                    empleadoLogic.registrarAdministrador(email, passwordDefault, nombres, cedula, celular, direccion, fecha);
                    break;
                case "VENDEDOR":
                    empleadoLogic.registrarVendedor(email, passwordDefault, nombres, cedula, celular, direccion, fecha);
                    break;
                case "BODEGUERO":
                    empleadoLogic.registrarBodeguero(email, passwordDefault, nombres, cedula, celular, direccion, fecha);
                    break;
            }

            view.mostrarExitoFlotante("Empleado registrado con éxito.");
            limpiarFormulario();

            // 3. ACTUALIZACIÓN EN TIEMPO REAL
            cargarTabla(); // Al llamar a esto aquí, la tabla se refresca sola

        } catch (Exception ex) {
            view.mostrarErrorFlotante("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void cargarTabla() {
        view.modeloTabla.setRowCount(0); // Limpiar tabla
        try {
            List<Empleado> lista = empleadoLogic.listarTodos();

            for (Empleado emp : lista) {
                if (emp != null) {
                    // ORDEN CORRECTO DE COLUMNAS:
                    // 0. Rol
                    // 1. Nombres
                    // 2. Cédula
                    // 3. Celular
                    // 4. Dirección
                    // 5. Fecha Ingreso
                    // 6. Email (Usuario)

                    view.modeloTabla.addRow(new Object[]{
                            emp.getRol(),           // Columna 0
                            emp.getNombre(),        // Columna 1
                            emp.getCedula(),        // Columna 2
                            emp.getCelular(),       // Columna 3
                            emp.getDireccion(),     // Columna 4
                            emp.getFechaIngreso(),  // Columna 5
                            emp.getUsuario()        // Columna 6 (Email)
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