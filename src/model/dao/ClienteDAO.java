package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Cliente;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends GenericDAO<Cliente> {

    // 0 = SOLO TXT, 1 = SOLO DB, 2 = AMBOS
    private static final int TIPO_ALMACENAMIENTO = 2;
    private static final String RUTA_ARCHIVO = "data/clientes.txt";

    // --- CONSTANTE CONVERTER ---
    private static final Converter<Cliente> CONVERTER = new Converter<Cliente>() {
        @Override
        public Cliente fromLine(String line) {
            if (line == null || line.trim().isEmpty()) return null;
            String[] p = line.split(";");

            // Validación básica
            if (p.length < 3) return null;

            String telefono = (p.length > 3) ? p[3] : "Sin Teléfono";
            String direccion = (p.length > 4) ? p[4] : "Sin Dirección";

            // Constructor: Cedula, Nombre, Correo, Telefono, Direccion
            return new Cliente(p[0], p[1], p[2], telefono, direccion);
        }

        @Override
        public String toLine(Cliente c) {
            return c.getCedula() + ";" + c.getNombre() + ";" +
                    c.getCorreo() + ";" + c.getTelefono() + ";" + c.getDireccion();
        }

        @Override
        public boolean match(Cliente c, String... args) {
            return c.getCedula().equals(args[0]);
        }
    };

    public ClienteDAO() {
        super(new GenericFile<>(RUTA_ARCHIVO, CONVERTER));
    }

    // --- 1. AGREGAR (Insertar en tabla 'clientes') ---
    @Override
    public void add(Cliente c) throws IOException {
        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            // CAMBIO: Ahora insertamos en la tabla 'clientes'
            String sql = "INSERT INTO clientes (cedula, nombre, email, telefono, direccion) VALUES (?, ?, ?, ?, ?)";

            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, c.getCedula());
                    ps.setString(2, c.getNombre());
                    ps.setString(3, c.getCorreo());
                    ps.setString(4, c.getTelefono());
                    ps.setString(5, c.getDireccion());

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Cliente (Add): " + e.getMessage());
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            super.add(c);
        }
    }

    // --- 2. LISTAR (Leer de tabla 'clientes') ---
    @Override
    public List<Cliente> getAll() throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            List<Cliente> lista = new ArrayList<>();
            // CAMBIO: Select de tabla 'clientes'
            String sql = "SELECT * FROM clientes";

            try (Connection con = Conexion.getConexion();
                 Statement st = con != null ? con.createStatement() : null) {

                if (st != null) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            Cliente c = new Cliente(
                                    rs.getString("cedula"),
                                    rs.getString("nombre"),
                                    rs.getString("email"),    // Mapeamos email a correo
                                    rs.getString("telefono"),
                                    rs.getString("direccion")
                            );
                            lista.add(c);
                        }
                    }
                    return lista;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Cliente (Listar): " + e.getMessage() + " -> Usando respaldo TXT");
            }
        }
        return super.getAll();
    }

    // --- 3. ACTUALIZAR (En tabla 'clientes') ---
    public boolean update(Cliente c) throws IOException {
        boolean exito = false;

        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            // CAMBIO: Update en tabla 'clientes'
            String sql = "UPDATE clientes SET nombre=?, email=?, telefono=?, direccion=? WHERE cedula=?";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, c.getNombre());
                    ps.setString(2, c.getCorreo());
                    ps.setString(3, c.getTelefono());
                    ps.setString(4, c.getDireccion());

                    ps.setString(5, c.getCedula()); // WHERE

                    exito = ps.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Cliente (Update): " + e.getMessage());
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            List<Cliente> todos = super.getAll();
            boolean encontrado = false;
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getCedula().equals(c.getCedula())) {
                    todos.set(i, c);
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

    private void reescribirArchivoTxt(List<Cliente> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (Cliente c : lista) {
                bw.write(CONVERTER.toLine(c));
                bw.newLine();
            }
        }
    }
}