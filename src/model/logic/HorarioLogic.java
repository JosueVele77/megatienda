package model.logic;

import model.dao.HorarioDAO;
import model.entities.Horario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HorarioLogic {

    private final HorarioDAO horarioDao;

    public HorarioLogic() {
        this.horarioDao = new HorarioDAO();
    }

    public void registrarHorario(Horario h) throws IOException {
        if (h == null) throw new IllegalArgumentException("Horario nulo");
        horarioDao.add(h);
    }

    // Método para guardar una lista completa (borra anteriores del mismo empleado)
    public void guardarHorariosSemana(String idEmpleado, List<Horario> nuevosHorarios) throws IOException {
        List<Horario> todos = horarioDao.getAll();

        // Eliminar horarios anteriores de este empleado
        todos.removeIf(h -> h.getIdEmpleado().equals(idEmpleado));

        // Agregar los nuevos
        todos.addAll(nuevosHorarios);

        writeAllToFile("data/horarios.txt", todos);
    }

    public List<Horario> buscarPorEmpleado(String idEmpleado) throws IOException {
        return horarioDao.getAll().stream()
                .filter(h -> h.getIdEmpleado().equals(idEmpleado))
                .collect(Collectors.toList());
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