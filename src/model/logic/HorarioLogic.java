package model.logic;

import model.dao.HorarioDAO;
import model.entities.Horario;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioLogic {

    private final HorarioDAO horarioDao;

    public HorarioLogic() {
        this.horarioDao = new HorarioDAO();
    }

    public void registrarHorario(Horario h) throws IOException {
        if (h == null) throw new IllegalArgumentException("Horario nulo");
        horarioDao.add(h);
    }

    public void guardarHorariosSemana(String cedula, List<Horario> horariosSemana) throws IOException {
        horarioDao.guardarSemanaCompleta(cedula, horariosSemana);
    }

    public List<Horario> obtenerHorarioEmpleado(String cedula) throws IOException {
        return horarioDao.obtenerPorCedula(cedula);
    }

    // --- ESTE ES EL MÉTODO QUE FALTABA ---
    public Horario buscarPorEmpleado(String cedula) throws IOException {
        List<Horario> lista = horarioDao.getAll();
        for (Horario h : lista) {
            // Compara si la cédula del horario coincide con la buscada
            if (h != null && h.getIdEmpleado().equals(cedula)) {
                return h;
            }
        }
        return null; // Retorna null si no encuentra nada (el controlador manejará esto)
    }
    // -------------------------------------

    public List<Horario> listarTodos() throws IOException {
        return horarioDao.getAll();
    }

    public boolean actualizarHorario(Horario actualizado) throws IOException {
        List<Horario> list = horarioDao.getAll();
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            Horario h = list.get(i);
            if (h != null && h.getIdEmpleado().equals(actualizado.getIdEmpleado())) {
                list.set(i, actualizado);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile("data/horarios.txt", list);
        return found;
    }

    private <T> void writeAllToFile(String filePath, List<T> list) {
        final List<T> dataSnapshot = new ArrayList<>(list);
        new Thread(() -> {
            synchronized (this) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
                    for (T item : dataSnapshot) {
                        if (item != null) pw.println(item.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}