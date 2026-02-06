package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.DetalleVenta;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO extends GenericDAO<DetalleVenta> {

    // 0 = SOLO TXT, 1 = SOLO DB, 2 = AMBOS
    private static final int TIPO_ALMACENAMIENTO = 2;
    private static final String RUTA_ARCHIVO = "data/detalles_venta.txt";

    private static final Converter<DetalleVenta> CONVERTER = new Converter<DetalleVenta>() {
        @Override
        public DetalleVenta fromLine(String line) {
            if (line == null || line.trim().isEmpty()) return null;
            String[] p = line.split(";");
            if (p.length < 4) return null;

            return new DetalleVenta(
                    p[0], // Codigo Venta (UUID)
                    p[1], // Codigo Producto
                    Integer.parseInt(p[2]), // Cantidad
                    Double.parseDouble(p[3]) // Subtotal
            );
        }

        @Override
        public String toLine(DetalleVenta d) {
            // CORRECCIÓN: Usamos String.format para forzar 2 decimales y punto
            return d.getCodigoVenta() + ";" +
                    d.getCodigoProducto() + ";" +
                    d.getCantidad() + ";" +
                    String.format(java.util.Locale.US, "%.2f", d.getSubtotal());
        }

        @Override
        public boolean match(DetalleVenta d, String... args) {
            return d.getCodigoVenta().equals(args[0]);
        }
    };

    public DetalleVentaDAO() {
        super(new GenericFile<>(RUTA_ARCHIVO, CONVERTER));
    }

    // --- 1. AGREGAR (Insertar Detalle) ---
    @Override
    public void add(DetalleVenta d) throws IOException {
        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "INSERT INTO detalle_ventas (venta_codigo, producto_codigo, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, d.getCodigoVenta());
                    ps.setString(2, d.getCodigoProducto());
                    ps.setInt(3, d.getCantidad());
                    ps.setDouble(4, d.getSubtotal());

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB DetalleVenta (Add): " + e.getMessage());
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            super.add(d);
        }
    }

    // --- 2. LISTAR ---
    @Override
    public List<DetalleVenta> getAll() throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            List<DetalleVenta> lista = new ArrayList<>();
            String sql = "SELECT * FROM detalle_ventas";

            try (Connection con = Conexion.getConexion();
                 Statement st = con != null ? con.createStatement() : null) {

                if (st != null) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            DetalleVenta d = new DetalleVenta(
                                    rs.getString("venta_codigo"),
                                    rs.getString("producto_codigo"),
                                    rs.getInt("cantidad"),
                                    rs.getDouble("subtotal")
                            );
                            lista.add(d);
                        }
                    }
                    return lista;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB DetalleVenta (Listar): " + e.getMessage() + " -> Usando respaldo TXT");
            }
        }
        return super.getAll();
    }
}