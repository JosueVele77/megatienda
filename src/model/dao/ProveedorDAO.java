package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Proveedor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO extends GenericDAO<Proveedor> {

    private static final int TIPO_ALMACENAMIENTO = 2;
    private static final String RUTA_ARCHIVO = "data/proveedores.txt";

    // --- CORRECCIÓN AQUÍ: Aceptar archivos viejos y nuevos ---
    private static final Converter<Proveedor> CONVERTER = new Converter<Proveedor>() {
        @Override
        public Proveedor fromLine(String line) {
            if (line == null || line.trim().isEmpty()) return null;
            String[] p = line.split(";");

            // Aceptamos formato viejo (4 columnas) y nuevo (5 columnas)
            if (p.length < 4) return null;

            String codigo = p[0];
            String razonSocial = p[1];
            String contacto = p[2]; // En el viejo esto era telefono, ajustamos abajo
            String telefono = p[3]; // En el viejo esto era direccion
            String email = "sin_email@tienda.com"; // Valor por defecto

            // Lógica para detectar si es formato viejo o nuevo
            if (p.length == 4) {
                // Formato viejo: RUC;Nombre;Telefono;Direccion
                // Re-mapeamos para que no se pierdan datos
                codigo = p[0];
                razonSocial = p[1];
                telefono = p[2];
                contacto = p[3]; // Usamos la dirección como contacto temporal
            } else if (p.length >= 5) {
                // Formato nuevo: Codigo;Razon;Contacto;Telefono;Email
                contacto = p[2];
                telefono = p[3];
                email = p[4];
            }

            return new Proveedor(codigo, razonSocial, contacto, telefono, email);
        }

        @Override
        public String toLine(Proveedor p) {
            return p.toFileString();
        }

        @Override
        public boolean match(Proveedor p, String... args) {
            return p.getCodigo().equals(args[0]);
        }
    };

    public ProveedorDAO() {
        super(new GenericFile<>(RUTA_ARCHIVO, CONVERTER));
    }

    // --- 1. AGREGAR ---
    @Override
    public void add(Proveedor p) throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "INSERT INTO proveedores (codigo, razon_social, contacto, telefono, email) VALUES (?, ?, ?, ?, ?)";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {
                if (ps != null) {
                    ps.setString(1, p.getCodigo());
                    ps.setString(2, p.getRazonSocial());
                    ps.setString(3, p.getContacto());
                    ps.setString(4, p.getTelefono());
                    ps.setString(5, p.getEmail());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Proveedor (Add): " + e.getMessage());
            }
        }
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            super.add(p);
        }
    }

    // --- 2. LISTAR ---
    @Override
    public List<Proveedor> getAll() throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            List<Proveedor> lista = new ArrayList<>();
            String sql = "SELECT * FROM proveedores";
            try (Connection con = Conexion.getConexion();
                 Statement st = con != null ? con.createStatement() : null) {
                if (st != null) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            lista.add(new Proveedor(
                                    rs.getString("codigo"),
                                    rs.getString("razon_social"),
                                    rs.getString("contacto"),
                                    rs.getString("telefono"),
                                    rs.getString("email")
                            ));
                        }
                    }
                    return lista;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Proveedor (Listar): " + e.getMessage());
            }
        }
        return super.getAll();
    }

    // --- 3. ACTUALIZAR ---
    public boolean update(Proveedor p) throws IOException {
        boolean exito = false;
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "UPDATE proveedores SET razon_social=?, contacto=?, telefono=?, email=? WHERE codigo=?";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {
                if (ps != null) {
                    ps.setString(1, p.getRazonSocial());
                    ps.setString(2, p.getContacto());
                    ps.setString(3, p.getTelefono());
                    ps.setString(4, p.getEmail());
                    ps.setString(5, p.getCodigo());
                    exito = ps.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Proveedor (Update): " + e.getMessage());
            }
        }

        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            List<Proveedor> todos = super.getAll();
            boolean encontrado = false;
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getCodigo().equals(p.getCodigo())) {
                    todos.set(i, p);
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

    private void reescribirArchivoTxt(List<Proveedor> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (Proveedor p : lista) {
                bw.write(CONVERTER.toLine(p));
                bw.newLine();
            }
        }
    }
}