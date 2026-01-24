package model.dao;

import model.entities.Horario;
import model.entities.Turno;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Importante para filtrar

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
                // Validamos que tenga la estructura correcta (5 partes)
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

    // --- NUEVO: Obtener solo los de un empleado ---
    public List<Horario> obtenerPorCedula(String cedula) throws IOException {
        return getAll().stream()
                .filter(h -> h.getIdEmpleado().equals(cedula))
                .collect(Collectors.toList());
    }

    // --- NUEVO: Actualizar (Borrar viejos + Guardar nuevos) ---
    public void guardarSemanaCompleta(String cedula, List<Horario> nuevosHorarios) throws IOException {
        List<Horario> todos = getAll();

        // 1. Eliminamos todos los horarios previos de este empleado
        todos.removeIf(h -> h.getIdEmpleado().equals(cedula));

        // 2. Agregamos los nuevos
        todos.addAll(nuevosHorarios);

        // 3. Guardamos todo de nuevo (Sobreescritura)
        saveAll(todos);
    }

    public void saveAll(List<Horario> lista) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (Horario h : lista) {
                pw.println(h.toString());
            }
        }
    }

    // Java
    public void add(Horario h) throws IOException {
        if (h == null) throw new IllegalArgumentException("Horario nulo");
        List<Horario> lista = getAll(); // usa el método existente
        lista.add(h);
        try (PrintWriter pw = new PrintWriter(new FileWriter("data/horarios.txt", false))) {
            for (Horario item : lista) {
                if (item != null) pw.println(item.toString());
            }
        }
    }

}