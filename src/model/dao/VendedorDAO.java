package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Vendedor;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAO extends GenericDAO<Vendedor> {

    // 0 = SOLO TXT, 1 = SOLO DB, 2 = AMBOS
    private static final int TIPO_ALMACENAMIENTO = 2;
    private static final String RUTA_ARCHIVO = "data/vendedores.txt";

    // --- CONSTANTE CONVERTER ---
    private static final Converter<Vendedor> CONVERTER = new Converter<Vendedor>() {
        @Override
        public Vendedor fromLine(String line) {
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

            Vendedor v = new Vendedor(usuario, password, nombre, cedula, celular, direccion, fecha);
            if (p.length > 8) v.setPrimerIngreso(Boolean.parseBoolean(p[8]));
            return v;
        }

        @Override
        public String toLine(Vendedor v) {
            return "VENDEDOR;" +
                    v.getUsuario() + ";" +
                    v.getPassword() + ";" +
                    v.getNombre() + ";" +
                    v.getCedula() + ";" +
                    v.getCelular() + ";" +
                    v.getDireccion() + ";" +
                    v.getFechaIngreso() + ";" +
                    v.isPrimerIngreso();
        }

        @Override
        public boolean match(Vendedor v, String... args) {
            return v.getUsuario().equalsIgnoreCase(args[0]);
        }
    };

    public VendedorDAO() {
        super(new GenericFile<>(RUTA_ARCHIVO, CONVERTER));
    }

    // --- 1. AGREGAR ---
    @Override
    public void add(Vendedor v) throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "INSERT INTO usuarios (cedula, nombre, email, password, telefono, direccion, rol, primer_ingreso, fecha_ingreso) VALUES (?, ?, ?, ?, ?, ?, 'VENDEDOR', ?, ?)";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {
                if (ps != null) {
                    ps.setString(1, v.getCedula());
                    ps.setString(2, v.getNombre());
                    ps.setString(3, v.getUsuario());
                    ps.setString(4, v.getPassword());
                    ps.setString(5, v.getCelular());
                    ps.setString(6, v.getDireccion());
                    ps.setBoolean(7, v.isPrimerIngreso());
                    ps.setString(8, v.getFechaIngreso()); // Guardar fecha
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Vendedor (Add): " + e.getMessage());
            }
        }
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            super.add(v);
        }
    }

    // --- 2. LISTAR ---
    @Override
    public List<Vendedor> getAll() throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            List<Vendedor> lista = new ArrayList<>();
            String sql = "SELECT * FROM usuarios WHERE rol = 'VENDEDOR'";
            try (Connection con = Conexion.getConexion();
                 Statement st = con != null ? con.createStatement() : null) {
                if (st != null) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            // Leemos la fecha real de la BD
                            String fechaBD = rs.getString("fecha_ingreso");
                            if(fechaBD == null) fechaBD = "2024-01-01";

                            Vendedor v = new Vendedor(
                                    rs.getString("email"),
                                    rs.getString("password"),
                                    rs.getString("nombre"),
                                    rs.getString("cedula"),
                                    rs.getString("telefono"),
                                    rs.getString("direccion"),
                                    fechaBD
                            );
                            v.setPrimerIngreso(rs.getBoolean("primer_ingreso"));
                            lista.add(v);
                        }
                    }
                    return lista;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Vendedor (Listar): " + e.getMessage());
            }
        }
        return super.getAll();
    }

    // --- 3. ACTUALIZAR (CORREGIDO) ---
    public boolean update(Vendedor v) throws IOException {
        boolean exito = false;
        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            // AQUÍ ESTABA EL ERROR: Faltaba fecha_ingreso=?
            String sql = "UPDATE usuarios SET nombre=?, email=?, password=?, telefono=?, direccion=?, primer_ingreso=?, fecha_ingreso=? WHERE cedula=?";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {
                if (ps != null) {
                    ps.setString(1, v.getNombre());
                    ps.setString(2, v.getUsuario());
                    ps.setString(3, v.getPassword());
                    ps.setString(4, v.getCelular());
                    ps.setString(5, v.getDireccion());
                    ps.setBoolean(6, v.isPrimerIngreso());
                    ps.setString(7, v.getFechaIngreso()); // Fecha en índice 7

                    ps.setString(8, v.getCedula());       // Cédula en índice 8 (WHERE)

                    exito = ps.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Vendedor (Update): " + e.getMessage());
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            List<Vendedor> todos = super.getAll();
            boolean encontrado = false;
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getCedula().equals(v.getCedula())) {
                    todos.set(i, v);
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

    private void reescribirArchivoTxt(List<Vendedor> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (Vendedor v : lista) {
                bw.write(CONVERTER.toLine(v));
                bw.newLine();
            }
        }
    }
}