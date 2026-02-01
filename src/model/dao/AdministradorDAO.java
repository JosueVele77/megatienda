package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Administrador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO extends GenericDAO<Administrador> {

    // 0 = SOLO TXT, 1 = SOLO DB, 2 = AMBOS
    private static final int TIPO_ALMACENAMIENTO = 2;
    private static final String RUTA_ARCHIVO = "data/administradores.txt";

    // --- CONSTANTE CONVERTER ---
    private static final Converter<Administrador> CONVERTER = new Converter<Administrador>() {
        @Override
        public Administrador fromLine(String line) {
            if (line == null || line.trim().isEmpty()) return null;
            String[] p = line.split(";");
            if (p.length < 4) return null;

            String usuario = p[0];
            String password = p[1];
            String nombre = p[2];
            String cedula = p[3];
            String celular = (p.length > 4) ? p[4] : "Sin Celular";
            String direccion = (p.length > 5) ? p[5] : "Sin Dirección";
            String fecha = (p.length > 6) ? p[6] : "2024-01-01";

            Administrador a = new Administrador(usuario, password, nombre, cedula, celular, direccion, fecha);

            if (p.length > 7) a.setPrimerIngreso(Boolean.parseBoolean(p[7]));
            return a;
        }

        @Override
        public String toLine(Administrador a) {
            return a.getUsuario() + ";" + a.getPassword() + ";" + a.getNombre() + ";" +
                    a.getCedula() + ";" + a.getCelular() + ";" + a.getDireccion() + ";" +
                    a.getFechaIngreso() + ";" + a.isPrimerIngreso();
        }

        @Override
        public boolean match(Administrador a, String... args) {
            return a.getUsuario().equalsIgnoreCase(args[0]);
        }
    };

    public AdministradorDAO() {
        super(new GenericFile<>(RUTA_ARCHIVO, CONVERTER));
    }

    // --- 1. AGREGAR (CORREGIDO: Ahora guarda fecha_ingreso) ---
    @Override
    public void add(Administrador a) throws IOException {
        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "INSERT INTO usuarios (cedula, nombre, email, password, telefono, direccion, rol, primer_ingreso, fecha_ingreso) VALUES (?, ?, ?, ?, ?, ?, 'ADMINISTRADOR', ?, ?)";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, a.getCedula());
                    ps.setString(2, a.getNombre());
                    ps.setString(3, a.getUsuario());
                    ps.setString(4, a.getPassword());
                    ps.setString(5, a.getCelular());
                    ps.setString(6, a.getDireccion());
                    ps.setBoolean(7, a.isPrimerIngreso());
                    ps.setString(8, a.getFechaIngreso()); // <--- AQUI SE GUARDA LA FECHA

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB (Add): " + e.getMessage());
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            super.add(a);
        }
    }

    // --- 2. LISTAR (CORREGIDO: Ahora lee fecha_ingreso de la BD) ---
    @Override
    public List<Administrador> getAll() throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            List<Administrador> lista = new ArrayList<>();
            String sql = "SELECT * FROM usuarios WHERE rol = 'ADMINISTRADOR'";

            try (Connection con = Conexion.getConexion();
                 Statement st = con != null ? con.createStatement() : null) {

                if (st != null) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            // Leemos la fecha de la base de datos. Si es nula, ponemos default.
                            String fechaBD = rs.getString("fecha_ingreso");
                            if(fechaBD == null) fechaBD = "2024-01-01";

                            Administrador a = new Administrador(
                                    rs.getString("email"),
                                    rs.getString("password"),
                                    rs.getString("nombre"),
                                    rs.getString("cedula"),
                                    rs.getString("telefono"),
                                    rs.getString("direccion"),
                                    fechaBD // <--- USAMOS LA FECHA REAL
                            );
                            a.setPrimerIngreso(rs.getBoolean("primer_ingreso"));
                            lista.add(a);
                        }
                    }
                    return lista;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB (Listar): " + e.getMessage() + " -> Usando respaldo TXT");
            }
        }
        return super.getAll();
    }

    // --- 3. ACTUALIZAR (CORREGIDO: Incluye fecha_ingreso) ---
    public boolean update(Administrador a) throws IOException {
        boolean exito = false;

        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "UPDATE usuarios SET nombre=?, email=?, password=?, telefono=?, direccion=?, primer_ingreso=?, fecha_ingreso=? WHERE cedula=?";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, a.getNombre());
                    ps.setString(2, a.getUsuario());
                    ps.setString(3, a.getPassword());
                    ps.setString(4, a.getCelular());
                    ps.setString(5, a.getDireccion());
                    ps.setBoolean(6, a.isPrimerIngreso());
                    ps.setString(7, a.getFechaIngreso()); // <--- AQUI ACTUALIZAMOS LA FECHA

                    ps.setString(8, a.getCedula()); // WHERE

                    exito = ps.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB (Update): " + e.getMessage());
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            List<Administrador> todos = super.getAll();
            boolean encontrado = false;
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getCedula().equals(a.getCedula())) {
                    todos.set(i, a);
                    encontrado = true;
                    break;
                }
            }
            if (encontrado) {
                reescribirArchivoTxt(todos);
                exito = true;
            }
        }
        return exito;
    }

    private void reescribirArchivoTxt(List<Administrador> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (Administrador admin : lista) {
                String linea = CONVERTER.toLine(admin);
                bw.write(linea);
                bw.newLine();
            }
        }
    }
}