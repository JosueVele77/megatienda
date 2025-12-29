package controller;

import model.entities.Empleado;
import model.entities.Horario;
import model.entities.Turno;
import model.logic.EmpleadoLogic;
import model.logic.HorarioLogic;
import view.GestionHorariosView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class GestionHorariosController implements ActionListener {

    private final GestionHorariosView view;
    private final EmpleadoLogic empleadoLogic;
    private final HorarioLogic horarioLogic;

    public GestionHorariosController(GestionHorariosView view) {
        this.view = view;
        this.empleadoLogic = new EmpleadoLogic();
        this.horarioLogic = new HorarioLogic();

        this.view.btnGuardar.addActionListener(this);
        this.view.btnCancelar.addActionListener(e -> view.dispose());

        // Listener para cambio de selección en el Combo
        this.view.cmbEmpleados.addActionListener(e -> cargarDatosEmpleadoSeleccionado());
    }

    public void iniciar() {
        cargarEmpleados();
        view.setVisible(true);
    }

    private void cargarEmpleados() {
        view.cmbEmpleados.removeAllItems();
        try {
            List<Empleado> lista = empleadoLogic.listarTodos();
            for (Empleado e : lista) {
                view.cmbEmpleados.addItem(new GestionHorariosView.EmpleadoWrapper(e));
            }
            // Seleccionar el primero y cargar sus datos
            if (view.cmbEmpleados.getItemCount() > 0) {
                view.cmbEmpleados.setSelectedIndex(0);
                cargarDatosEmpleadoSeleccionado();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Error cargando empleados: " + e.getMessage());
        }
    }

    private void cargarDatosEmpleadoSeleccionado() {
        GestionHorariosView.EmpleadoWrapper wrapper = (GestionHorariosView.EmpleadoWrapper) view.cmbEmpleados.getSelectedItem();
        if (wrapper == null) return;

        Empleado emp = wrapper.getEmpleado();
        try {
            Horario h = horarioLogic.buscarPorEmpleado(emp.getCedula());
            if (h != null) {
                // Si existe, llenar campos
                view.cmbTurno.setSelectedItem(h.getTurno());
                view.txtEntrada.setText(h.getEntrada());
                view.txtSalida.setText(h.getSalida());
            } else {
                // Si no existe, defaults
                view.cmbTurno.setSelectedIndex(0);
                view.txtEntrada.setText("");
                view.txtSalida.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnGuardar) {
            guardarHorario();
        }
    }

    private void guardarHorario() {
        GestionHorariosView.EmpleadoWrapper wrapper = (GestionHorariosView.EmpleadoWrapper) view.cmbEmpleados.getSelectedItem();
        if (wrapper == null) return;

        Empleado emp = wrapper.getEmpleado();
        Turno turno = (Turno) view.cmbTurno.getSelectedItem();
        String entrada = view.txtEntrada.getText().trim();
        String salida = view.txtSalida.getText().trim();

        if (entrada.isEmpty() || salida.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debe definir hora de entrada y salida.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Horario nuevoHorario = new Horario(emp.getCedula(), turno, entrada, salida);

            // Verificar si ya existe para actualizar o crear
            Horario existente = horarioLogic.buscarPorEmpleado(emp.getCedula());

            if (existente != null) {
                horarioLogic.actualizarHorario(nuevoHorario);
                JOptionPane.showMessageDialog(view, "Horario actualizado correctamente.");
            } else {
                horarioLogic.registrarHorario(nuevoHorario);
                JOptionPane.showMessageDialog(view, "Horario asignado correctamente.");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}