package controller;

import model.entities.Empleado;
import model.entities.Horario;
import model.entities.Turno;
import model.logic.EmpleadoLogic;
import model.logic.HorarioLogic;
import view.GestionHorarioView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestionHorarioController implements ActionListener {

    private final GestionHorarioView view;
    private final HorarioLogic horarioLogic;
    private final EmpleadoLogic empleadoLogic;

    public GestionHorarioController(GestionHorarioView view) {
        this.view = view;
        this.horarioLogic = new HorarioLogic();
        this.empleadoLogic = new EmpleadoLogic();

        cargarEmpleados();
        configurarListeners();
    }

    public void iniciar() {
        view.setVisible(true);
    }

    private void cargarEmpleados() {
        try {
            view.cmbEmpleados.removeAllItems();

            // --- CORRECCIÓN: Usamos .listarTodos() ---
            for (Empleado e : empleadoLogic.listarTodos()) {
                view.cmbEmpleados.addItem(e);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Error cargando empleados: " + e.getMessage());
        }
    }

    private void configurarListeners() {
        view.btnGuardar.addActionListener(this);
        view.btnCancelar.addActionListener(e -> view.dispose());

        // Agregar listener a cada combo de día para actualizar horas automáticamente
        for (GestionHorarioView.FilaDia fila : view.filasDias.values()) {
            fila.cmbTurno.addActionListener(e -> actualizarHoras(fila));
            // Actualizar inicial para que se muestren las horas del turno por defecto
            actualizarHoras(fila);
        }
    }

    private void actualizarHoras(GestionHorarioView.FilaDia fila) {
        Turno t = (Turno) fila.cmbTurno.getSelectedItem();
        if (t != null) {
            String[] horas = obtenerHorasPorTurno(t);
            fila.lblEntrada.setText(horas[0]);
            fila.lblSalida.setText(horas[1]);
        }
    }

    private String[] obtenerHorasPorTurno(Turno t) {
        switch (t) {
            case MATUTINO: return new String[]{"07:00", "15:00"};
            case VESPERTINO: return new String[]{"13:00", "21:00"};
            case NOCTURNO: return new String[]{"18:00", "00:00"};
            case MADRUGADA: return new String[]{"23:00", "07:00"};
            default: return new String[]{"00:00", "00:00"};
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnGuardar) {
            guardarHorarios();
        }
    }

    private void guardarHorarios() {
        Empleado emp = (Empleado) view.cmbEmpleados.getSelectedItem();
        if (emp == null) {
            JOptionPane.showMessageDialog(view, "Seleccione un empleado");
            return;
        }

        List<Horario> horariosSemana = new ArrayList<>();

        // Recorrer los 5 días (orden fijo)
        String[] ordenDias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};

        for (String dia : ordenDias) {
            GestionHorarioView.FilaDia fila = view.filasDias.get(dia);
            Turno turnoSel = (Turno) fila.cmbTurno.getSelectedItem();
            String entrada = fila.lblEntrada.getText();
            String salida = fila.lblSalida.getText();

            // Creamos el horario para ese día
            horariosSemana.add(new Horario(emp.getCedula(), dia, turnoSel, entrada, salida));
        }

        try {
            horarioLogic.guardarHorariosSemana(emp.getCedula(), horariosSemana);
            JOptionPane.showMessageDialog(view, "Horarios asignados correctamente para: " + emp.getNombre());
            view.dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}