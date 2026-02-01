package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Producto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends GenericDAO<Producto> {

    // 0 = SOLO TXT, 1 = SOLO DB, 2 = AMBOS
    private static final int TIPO_ALMACENAMIENTO = 2;
    private static final String RUTA_ARCHIVO = "data/productos.txt";

    // --- CONSTANTE CONVERTER ---
    private static final Converter<Producto> CONVERTER = new Converter<Producto>() {
        @Override
        public Producto fromLine(String line) {
            if (line == null || line.trim().isEmpty()) return null;
            String[] p = line.split(";");

            if (p.length < 4) return null;

            String codigo = p[0];
            String nombre = p[1];
            double precio = Double.parseDouble(p[2]);
            int stock = Integer.parseInt(p[3]);

            String codProv = (p.length > 4) ? p[4] : "Sin Proveedor";

            return new Producto(codigo, nombre, precio, stock, codProv);
        }

        @Override
        public String toLine(Producto p) {
            return p.getCodigo() + ";" +
                    p.getNombre() + ";" +
                    p.getPrecio() + ";" +
                    p.getStock() + ";" +
                    p.getCodigoProveedor();
        }

        @Override
        public boolean match(Producto p, String... args) {
            return p.getCodigo().equals(args[0]);
        }
    };

    public ProductoDAO() {
        super(new GenericFile<>(RUTA_ARCHIVO, CONVERTER));
    }

    // --- 1. AGREGAR ---
    @Override
    public void add(Producto p) throws IOException {
        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            // CORRECCIÓN: Usamos 'proveedor_codigo' en lugar de 'proveedor_ruc'
            String sql = "INSERT INTO productos (codigo, nombre, precio, stock, proveedor_codigo) VALUES (?, ?, ?, ?, ?)";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, p.getCodigo());
                    ps.setString(2, p.getNombre());
                    ps.setDouble(3, p.getPrecio());
                    ps.setInt(4, p.getStock());
                    ps.setString(5, p.getCodigoProveedor());

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Producto (Add): " + e.getMessage());
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            super.add(p);
        }
    }

    // --- 2. LISTAR ---
    @Override
    public List<Producto> getAll() throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            List<Producto> lista = new ArrayList<>();
            String sql = "SELECT * FROM productos";

            try (Connection con = Conexion.getConexion();
                 Statement st = con != null ? con.createStatement() : null) {

                if (st != null) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            Producto p = new Producto(
                                    rs.getString("codigo"),
                                    rs.getString("nombre"),
                                    rs.getDouble("precio"),
                                    rs.getInt("stock"),
                                    // CORRECCIÓN: Leemos 'proveedor_codigo'
                                    rs.getString("proveedor_codigo")
                            );
                            lista.add(p);
                        }
                    }
                    return lista;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Producto (Listar): " + e.getMessage() + " -> Usando respaldo TXT");
            }
        }
        return super.getAll();
    }

    // --- 3. ACTUALIZAR ---
    public boolean update(Producto p) throws IOException {
        boolean exito = false;

        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            // CORRECCIÓN: Usamos 'proveedor_codigo'
            String sql = "UPDATE productos SET nombre=?, precio=?, stock=?, proveedor_codigo=? WHERE codigo=?";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, p.getNombre());
                    ps.setDouble(2, p.getPrecio());
                    ps.setInt(3, p.getStock());
                    ps.setString(4, p.getCodigoProveedor());

                    ps.setString(5, p.getCodigo());

                    exito = ps.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Producto (Update): " + e.getMessage());
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            List<Producto> todos = super.getAll();
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

    private void reescribirArchivoTxt(List<Producto> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (Producto p : lista) {
                bw.write(CONVERTER.toLine(p));
                bw.newLine();
            }
        }
    }
}