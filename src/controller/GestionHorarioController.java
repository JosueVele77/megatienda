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

        // Listener para CADA fila de día (Lunes a Viernes)
        for (GestionHorarioView.FilaDia fila : view.filasDias.values()) {
            // Cuando cambian el item del combo box...
            fila.cmbTurno.addActionListener(e -> actualizarHoras(fila));

            // Llamada inicial para que aparezcan las horas del primer elemento por defecto
            actualizarHoras(fila);
        }
    }

    // --- AQUÍ ESTÁ LA LÓGICA DE LAS HORAS AUTOMÁTICAS ---
    private void actualizarHoras(GestionHorarioView.FilaDia fila) {
        Turno t = (Turno) fila.cmbTurno.getSelectedItem();
        if (t != null) {
            String[] horas = obtenerHorasPorTurno(t);
            // Actualizamos los Labels visualmente
            fila.lblEntrada.setText(horas[0]);
            fila.lblSalida.setText(horas[1]);
        }
    }

    private String[] obtenerHorasPorTurno(Turno t) {
        // Matutino(07:00 – 15:00), Vespertino(13:00 – 21:00),
        // Nocturno(18:00 – 00:00) y Madrugada(23:00 – 07:00).
        switch (t) {
            case MATUTINO:  return new String[]{"07:00", "15:00"};
            case VESPERTINO:return new String[]{"13:00", "21:00"};
            case NOCTURNO:  return new String[]{"18:00", "00:00"};
            case MADRUGADA: return new String[]{"23:00", "07:00"};
            default:        return new String[]{"00:00", "00:00"};
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
        String[] ordenDias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};

        // Recolectamos la data de la vista
        for (String dia : ordenDias) {
            GestionHorarioView.FilaDia fila = view.filasDias.get(dia);

            Turno turnoSel = (Turno) fila.cmbTurno.getSelectedItem();
            String entrada = fila.lblEntrada.getText();
            String salida = fila.lblSalida.getText();

            // Creamos objeto Horario
            horariosSemana.add(new Horario(emp.getCedula(), dia, turnoSel, entrada, salida));
        }

        try {
            // Guardamos usando el método que REEMPLAZA los datos viejos
            horarioLogic.guardarHorariosSemana(emp.getCedula(), horariosSemana);

            JOptionPane.showMessageDialog(view, "Horario asignado exitosamente a: " + emp.getNombre());
            view.dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar en archivo: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}