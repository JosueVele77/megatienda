package model.logic;

import model.dao.HorarioDAO;
import model.entities.Horario;
import model.entities.Turno;

import java.io.*;
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

    public Horario buscarPorEmpleado(String idEmpleado) throws IOException {
        for (Horario h : horarioDao.getAll()) {
            if (h != null && idEmpleado.equals(h.getIdEmpleado())) return h;
        }
        return null;
    }

    public List<Horario> listarHorarios() throws IOException {
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
        if (found) writeAllToFile("horarios.txt", list);
        return found;
    }

    private <T> void writeAllToFile(String filePath, List<T> list) throws IOException {
        synchronized (this) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
                for (T item : list) {
                    if (item != null) pw.println(item.toString());
                }
            }
        }
    }
}
