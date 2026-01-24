package model.dao;

import model.entities.Horario;
import model.entities.Turno;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioDAO {
    private static final String FILE_PATH = "data/horarios.txt";

    public List<Horario> getAll() throws IOException {
        List<Horario> horarios = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return horarios;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                // Verificamos que tenga 5 partes (Cédula, Día, Turno, Entrada, Salida)
                if (parts.length == 5) {
                    Horario h = new Horario(
                            parts[0],                   // idEmpleado
                            parts[1],                   // dia
                            Turno.valueOf(parts[2]),    // turno
                            parts[3],                   // entrada
                            parts[4]                    // salida
                    );
                    horarios.add(h);
                }
            }
        }
        return horarios;
    }

    public void add(Horario horario) throws IOException {
        // true para 'append' (agregar al final sin borrar lo anterior)
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            pw.println(horario.toString());
        }
    }

    // Método para reescribir toda la lista (usado al actualizar/eliminar)
    public void saveAll(List<Horario> lista) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (Horario h : lista) {
                pw.println(h.toString());
            }
        }
    }
}