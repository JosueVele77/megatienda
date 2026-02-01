package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Bodeguero;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BodegueroDAO extends GenericDAO<Bodeguero> {

    // 0 = SOLO TXT, 1 = SOLO DB, 2 = AMBOS
    private static final int TIPO_ALMACENAMIENTO = 2;
    private static final String RUTA_ARCHIVO = "data/bodegueros.txt";

    // --- CONSTANTE CONVERTER ---
    private static final Converter<Bodeguero> CONVERTER = new Converter<Bodeguero>() {
        @Override
        public Bodeguero fromLine(String line) {
            if (line == null || line.trim().isEmpty()) return null;
            String[] p = line.split(";");
            if (p.length < 5) return null;

            String usuario = p[1];
            String password = p[2];
            String nombre = p[3];
            String cedula = p[4];
            String celular = (p.length > 5) ? p[5] : "Sin Celular";
            String direccion = (p.length > 6) ? p[6] : "Sin Dirección";
            String fecha = (p.length > 7) ? p[7] : "2024-01-01";

            Bodeguero b = new Bodeguero(usuario, password, nombre, cedula, celular, direccion, fecha);
            if (p.length > 8) b.setPrimerIngreso(Boolean.parseBoolean(p[8]));
            return b;
        }

        @Override
        public String toLine(Bodeguero b) {
            return "BODEGUERO;" +
                    b.getUsuario() + ";" +
                    b.getPassword() + ";" +
                    b.getNombre() + ";" +
                    b.getCedula() + ";" +
                    b.getCelular() + ";" +
                    b.getDireccion() + ";" +
                    b.getFechaIngreso() + ";" +
                    b.isPrimerIngreso();
        }

        @Override
        public boolean match(Bodeguero b, String... args) {
            return b.getUsuario().equalsIgnoreCase(args[0]);
        }
    };

    public BodegueroDAO() {
        super(new GenericFile<>(RUTA_ARCHIVO, CONVERTER));
    }

    // --- 1. AGREGAR ---
    @Override
    public void add(Bodeguero b) throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "INSERT INTO usuarios (cedula, nombre, email, password, telefono, direccion, rol, primer_ingreso, fecha_ingreso) VALUES (?, ?, ?, ?, ?, ?, 'BODEGUERO', ?, ?)";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {
                if (ps != null) {
                    ps.setString(1, b.getCedula());
                    ps.setString(2, b.getNombre());
                    ps.setString(3, b.getUsuario());
                    ps.setString(4, b.getPassword());
                    ps.setString(5, b.getCelular());
                    ps.setString(6, b.getDireccion());
                    ps.setBoolean(7, b.isPrimerIngreso());
                    ps.setString(8, b.getFechaIngreso());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Bodeguero (Add): " + e.getMessage());
            }
        }
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            super.add(b);
        }
    }

    // --- 2. LISTAR ---
    @Override
    public List<Bodeguero> getAll() throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            List<Bodeguero> lista = new ArrayList<>();
            String sql = "SELECT * FROM usuarios WHERE rol = 'BODEGUERO'";
            try (Connection con = Conexion.getConexion();
                 Statement st = con != null ? con.createStatement() : null) {
                if (st != null) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            String fechaBD = rs.getString("fecha_ingreso");
                            if(fechaBD == null) fechaBD = "2024-01-01";

                            Bodeguero b = new Bodeguero(
                                    rs.getString("email"),
                                    rs.getString("password"),
                                    rs.getString("nombre"),
                                    rs.getString("cedula"),
                                    rs.getString("telefono"),
                                    rs.getString("direccion"),
                                    fechaBD
                            );
                            b.setPrimerIngreso(rs.getBoolean("primer_ingreso"));
                            lista.add(b);
                        }
                    }
                    return lista;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Bodeguero (Listar): " + e.getMessage());
            }
        }
        return super.getAll();
    }

    // --- 3. ACTUALIZAR (CORREGIDO) ---
    public boolean update(Bodeguero b) throws IOException {
        boolean exito = false;
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "UPDATE usuarios SET nombre=?, email=?, password=?, telefono=?, direccion=?, primer_ingreso=?, fecha_ingreso=? WHERE cedula=?";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {
                if (ps != null) {
                    ps.setString(1, b.getNombre());
                    ps.setString(2, b.getUsuario());
                    ps.setString(3, b.getPassword());
                    ps.setString(4, b.getCelular());
                    ps.setString(5, b.getDireccion());
                    ps.setBoolean(6, b.isPrimerIngreso());
                    ps.setString(7, b.getFechaIngreso());

                    ps.setString(8, b.getCedula()); // WHERE

                    exito = ps.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Bodeguero (Update): " + e.getMessage());
            }
        }

        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            List<Bodeguero> todos = super.getAll();
            boolean encontrado = false;
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getCedula().equals(b.getCedula())) {
                    todos.set(i, b);
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

    private void reescribirArchivoTxt(List<Bodeguero> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (Bodeguero b : lista) {
                bw.write(CONVERTER.toLine(b));
                bw.newLine();
            }
        }
    }
}