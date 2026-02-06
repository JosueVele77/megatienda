package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Venta;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO extends GenericDAO<Venta> {

    // 0 = SOLO TXT, 1 = SOLO DB, 2 = AMBOS
    private static final int TIPO_ALMACENAMIENTO = 2;
    private static final String RUTA_ARCHIVO = "data/ventas.txt";

    // --- CONSTANTE CONVERTER ---
    private static final Converter<Venta> CONVERTER = new Converter<Venta>() {
        @Override
        public Venta fromLine(String line) {
            if (line == null || line.trim().isEmpty()) return null;
            String[] p = line.split(";");
            if (p.length < 4) return null;

            // Validación para soportar archivos viejos sin tipoPago
            String tipoPago = (p.length > 4) ? p[4] : "Desconocido";

            return new Venta(
                    p[0], // Codigo (UUID)
                    p[1], // Cedula Cliente
                    Double.parseDouble(p[2]), // Total
                    p[3], // Fecha
                    tipoPago
            );
        }

        @Override
        public String toLine(Venta v) {
            // CORRECCIÓN: Formateamos el total a 2 decimales
            return v.getCodigo() + ";" +
                    v.getCedulaCliente() + ";" +
                    String.format(java.util.Locale.US, "%.2f", v.getTotal()) + ";" +
                    v.getFecha() + ";" +
                    v.getTipoPago();
        }

        @Override
        public boolean match(Venta v, String... args) {
            return v.getCodigo().equals(args[0]);
        }
    };

    public VentaDAO() {
        super(new GenericFile<>(RUTA_ARCHIVO, CONVERTER));
    }

    // --- 1. AGREGAR (Insertar venta) ---
    @Override
    public void add(Venta v) throws IOException {
        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "INSERT INTO ventas (codigo, fecha, total, cliente_cedula, tipo_pago) VALUES (?, ?, ?, ?, ?)";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, v.getCodigo());
                    ps.setString(2, v.getFecha());
                    ps.setDouble(3, v.getTotal());
                    ps.setString(4, v.getCedulaCliente());
                    ps.setString(5, v.getTipoPago());

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Venta (Add): " + e.getMessage());
                // No lanzamos excepción para permitir que se guarde en TXT como respaldo
            }
        }

        // B) TXT
        if (TIPO_ALMACENAMIENTO == 0 || TIPO_ALMACENAMIENTO == 2) {
            super.add(v);
        }
    }

    // --- 2. LISTAR ---
    @Override
    public List<Venta> getAll() throws IOException {
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            List<Venta> lista = new ArrayList<>();
            String sql = "SELECT * FROM ventas";

            try (Connection con = Conexion.getConexion();
                 Statement st = con != null ? con.createStatement() : null) {

                if (st != null) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            Venta v = new Venta(
                                    rs.getString("codigo"),        // UUID
                                    rs.getString("cliente_cedula"),
                                    rs.getDouble("total"),
                                    rs.getString("fecha"),
                                    rs.getString("tipo_pago")
                            );
                            lista.add(v);
                        }
                    }
                    return lista;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Venta (Listar): " + e.getMessage() + " -> Usando respaldo TXT");
            }
        }
        return super.getAll();
    }
}