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

    // --- SOLUCIÓN AL ERROR: Definimos el Convertidor como una CONSTANTE ESTÁTICA ---
    // Así podemos usarlo en el constructor y en el update sin necesidad de getters.
    private static final Converter<Vendedor> CONVERTER = new Converter<Vendedor>() {
        @Override
        public Vendedor fromLine(String line) {
            if (line == null || line.trim().isEmpty()) return null;
            String[] p = line.split(";");

            // Validamos longitud mínima
            if (p.length < 5) return null;

            // ESTRUCTURA DEL ARCHIVO:
            // p[0] = "VENDEDOR" (Lo ignoramos)
            // p[1] = Usuario (Email)
            // p[2] = Password
            // p[3] = Nombre
            // p[4] = Cédula
            // p[5] = Celular
            // p[6] = Dirección
            // p[7] = Fecha
            // p[8] = Primer Ingreso (Boolean)

            String usuario = p[1];
            String password = p[2];
            String nombre = p[3];
            String cedula = p[4];

            String celular = (p.length > 5) ? p[5] : "Sin Celular";
            String direccion = (p.length > 6) ? p[6] : "Sin Dirección";
            String fecha = (p.length > 7) ? p[7] : "2024-01-01";

            Vendedor v = new Vendedor(usuario, password, nombre, cedula, celular, direccion, fecha);

            if (p.length > 8) {
                v.setPrimerIngreso(Boolean.parseBoolean(p[8]));
            }
            return v;
        }

        @Override
        public String toLine(Vendedor v) {
            // Guardamos empezando con "VENDEDOR" para mantener el formato estándar
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
        // Pasamos la constante CONVERTER al constructor del padre
        super(new GenericFile<>(RUTA_ARCHIVO, CONVERTER));
    }

    // --- 1. AGREGAR ---
    @Override
    public void add(Vendedor v) throws IOException {
        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "INSERT INTO usuarios (cedula, nombre, email, password, telefono, direccion, rol, primer_ingreso) VALUES (?, ?, ?, ?, ?, ?, 'VENDEDOR', ?)";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, v.getCedula());
                    ps.setString(2, v.getNombre());
                    ps.setString(3, v.getUsuario());
                    ps.setString(4, v.getPassword());
                    ps.setString(5, v.getCelular());
                    ps.setString(6, v.getDireccion());
                    ps.setBoolean(7, v.isPrimerIngreso()); // Guardamos el estado

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Vendedor (Add): " + e.getMessage());
            }
        }

        // B) TXT
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
                            Vendedor v = new Vendedor(
                                    rs.getString("email"),    // Usuario
                                    rs.getString("password"),
                                    rs.getString("nombre"),
                                    rs.getString("cedula"),
                                    rs.getString("telefono"),
                                    rs.getString("direccion"),
                                    rs.getString("fecha_ingreso")
                            );
                            // Recuperamos el estado real de la BD
                            v.setPrimerIngreso(rs.getBoolean("primer_ingreso"));
                            lista.add(v);
                        }
                    }
                    return lista;
                }
            } catch (SQLException e) {
                System.err.println(">> Error DB Vendedor (Listar): " + e.getMessage() + " -> Usando respaldo TXT");
            }
        }
        return super.getAll();
    }

    // --- 3. ACTUALIZAR ---
    public boolean update(Vendedor v) throws IOException {
        boolean exito = false;

        // A) DB
        if (TIPO_ALMACENAMIENTO == 1 || TIPO_ALMACENAMIENTO == 2) {
            String sql = "UPDATE usuarios SET nombre=?, email=?, password=?, telefono=?, direccion=?, primer_ingreso=? WHERE cedula=?";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

                if (ps != null) {
                    ps.setString(1, v.getNombre());
                    ps.setString(2, v.getUsuario());
                    ps.setString(3, v.getPassword());
                    ps.setString(4, v.getCelular());
                    ps.setString(5, v.getDireccion());
                    ps.setBoolean(6, v.isPrimerIngreso()); // Actualizamos estado
                    ps.setString(7, v.getCedula());
                    ps.setString(7, v.getFechaIngreso());


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
                // AQUÍ USAMOS LA CONSTANTE 'CONVERTER' -> SOLUCIONADO EL ERROR
                String linea = CONVERTER.toLine(v);
                bw.write(linea);
                bw.newLine();
            }
        }
    }
}