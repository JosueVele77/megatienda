package model.dao;

import model.entities.Horario;
import model.entities.Turno;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HorarioDAO {

    // 0 = SOLO TXT, 1 = SOLO DB, 2 = AMBOS
    private static final int TIPO_ALMACENAMIENTO = 2;
    private static final String FILE_PATH = "data/horarios.txt";

    // --- 1. OBTENER TODOS ---
    public List<Horario> getAll() throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            List<Horario> listaDB = new ArrayList<>();
            String sql = "SELECT * FROM horarios";

            try (Connection con = Conexion.getConexion();
                 Statement st = con != null ? con.createStatement() : null) {
                if (st != null) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            listaDB.add(new Horario(
                                    rs.getString("empleado_cedula"),
                                    rs.getString("dia"),
                                    Turno.valueOf(rs.getString("turno")),
                                    rs.getString("hora_entrada"),
                                    rs.getString("hora_salida")
                            ));
                        }
                    }
                    return listaDB;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Horario (Listar): " + e.getMessage());
            }
        }
        return getAllFromFile();
    }

    public List<Horario> obtenerPorCedula(String cedula) throws IOException {
        return getAll().stream()
                .filter(h -> h.getIdEmpleado().equals(cedula))
                .collect(Collectors.toList());
    }

    // --- 2. AGREGAR UN SOLO HORARIO (EL MÉTODO QUE FALTABA) ---
    public void add(Horario h) throws IOException {
        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "INSERT INTO horarios (empleado_cedula, dia, turno, hora_entrada, hora_salida) VALUES (?, ?, ?, ?, ?)";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {
                if (ps != null) {
                    ps.setString(1, h.getIdEmpleado());
                    ps.setString(2, h.getDia());
                    ps.setString(3, h.getTurno().toString());
                    ps.setString(4, h.getEntrada());
                    ps.setString(5, h.getSalida());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Horario (Add): " + e.getMessage());
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
                pw.println(h.toString());
            }
        }
    }

    // --- 3. ACTUALIZAR UN SOLO HORARIO ---
    public boolean update(Horario h) throws IOException {
        boolean exito = false;
        // DB: Actualizamos por cédula y día
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "UPDATE horarios SET turno=?, hora_entrada=?, hora_salida=? WHERE empleado_cedula=? AND dia=?";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {
                if (ps != null) {
                    ps.setString(1, h.getTurno().toString());
                    ps.setString(2, h.getEntrada());
                    ps.setString(3, h.getSalida());
                    ps.setString(4, h.getIdEmpleado());
                    ps.setString(5, h.getDia());
                    exito = ps.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Horario (Update): " + e.getMessage());
            }
        }

        // TXT: Sobrescribir todo
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            List<Horario> todos = getAllFromFile();
            boolean encontrado = false;
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getIdEmpleado().equals(h.getIdEmpleado()) &&
                        todos.get(i).getDia().equals(h.getDia())) {
                    todos.set(i, h);
                    encontrado = true;
                    break;
                }
            }
            if (encontrado) {
                saveAllToFile(todos);
                exito = true;
            }
        }
        return exito;
    }

    // --- 4. GUARDAR SEMANA COMPLETA (Lógica "Borrar y Reemplazar") ---
    public void guardarSemanaCompleta(String cedula, List<Horario> nuevosHorarios) throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            try (Connection con = Conexion.getConexion()) {
                if (con != null) {
                    con.setAutoCommit(false);
                    // 1. Borrar anteriores
                    try (PreparedStatement psDel = con.prepareStatement("DELETE FROM horarios WHERE empleado_cedula = ?")) {
                        psDel.setString(1, cedula);
                        psDel.executeUpdate();
                    }
                    // 2. Insertar nuevos
                    String sql = "INSERT INTO horarios (empleado_cedula, dia, turno, hora_entrada, hora_salida) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement psIns = con.prepareStatement(sql)) {
                        for (Horario h : nuevosHorarios) {
                            psIns.setString(1, h.getIdEmpleado());
                            psIns.setString(2, h.getDia());
                            psIns.setString(3, h.getTurno().toString());
                            psIns.setString(4, h.getEntrada());
                            psIns.setString(5, h.getSalida());
                            psIns.executeUpdate();
                        }
                    }
                    con.commit();
                    con.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Horario (Semana): " + e.getMessage());
            }
        }

        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            List<Horario> todos = getAllFromFile();
            todos.removeIf(h -> h.getIdEmpleado().equals(cedula));
            todos.addAll(nuevosHorarios);
            saveAllToFile(todos);
        }
    }

    // --- Helpers Privados para TXT ---
    private List<Horario> getAllFromFile() throws IOException {
        List<Horario> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    list.add(new Horario(parts[0], parts[1], Turno.valueOf(parts[2]), parts[3], parts[4]));
                }
            }
        }
        return list;
    }

    private void saveAllToFile(List<Horario> lista) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (Horario h : lista) {
                pw.println(h.toString());
            }
        }
    }
}