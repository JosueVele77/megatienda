package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.*;

public class UsuarioDAO extends GenericDAO<Usuario> {

    private static final String FILE = "data/usuarios.txt";

    public UsuarioDAO() {
        super(new GenericFile<>(
                FILE,
                new Converter<Usuario>() {

                    // Formato esperado: usuario;password;ROL;nombre;cedula;celular;direccion;fecha
                    @Override
                    public Usuario fromLine(String line) {
                        if (line == null || line.trim().isEmpty()) return null;
                        String[] p = line.split(";", -1);

                        // Leer campos básicos
                        String usuario = p.length > 0 ? p[0] : "";
                        String password = p.length > 1 ? p[1] : "";
                        String rol = p.length > 2 ? p[2] : "";
                        String nombre = p.length > 3 ? p[3] : "";
                        String cedula = p.length > 4 ? p[4] : "";

                        // Leer campos nuevos (con validación de longitud por si el archivo es viejo)
                        String celular = p.length > 5 ? p[5] : "Sin Celular";
                        String direccion = p.length > 6 ? p[6] : "Sin Dirección";
                        String fecha = p.length > 7 ? p[7] : "2024-01-01";

                        switch (rol.toUpperCase()) {
                            case "ADMINISTRADOR":
                                // CORREGIDO: Constructor con 7 parámetros
                                return new Administrador(usuario, password, nombre, cedula, celular, direccion, fecha);

                            case "VENDEDOR":
                                return new Vendedor(usuario, password, nombre, cedula, celular, direccion, fecha);

                            case "BODEGUERO":
                                return new Bodeguero(usuario, password, nombre, cedula, celular, direccion, fecha);

                            default:
                                return null;
                        }
                    }

                    @Override
                    public String toLine(Usuario u) {
                        if (u == null) return "";

                        String usuario = u.getUsuario() != null ? u.getUsuario() : "";
                        String password = u.getPassword() != null ? u.getPassword() : "";
                        String rol = u.getRol() != null ? u.getRol() : "";
                        String nombre = "";
                        String cedula = "";
                        String celular = "";
                        String direccion = "";
                        String fecha = "";

                        // Si es Empleado, extraemos los datos extra
                        if (u instanceof Empleado) {
                            Empleado e = (Empleado) u;
                            nombre = e.getNombre() != null ? e.getNombre() : "";
                            cedula = e.getCedula() != null ? e.getCedula() : "";
                            celular = e.getCelular() != null ? e.getCelular() : "";
                            direccion = e.getDireccion() != null ? e.getDireccion() : "";
                            fecha = e.getFechaIngreso() != null ? e.getFechaIngreso() : "";
                        }

                        // Guardamos todo separado por punto y coma
                        return String.join(";", usuario, password, rol, nombre, cedula, celular, direccion, fecha);
                    }

                    @Override
                    public boolean match(Usuario u, String... args) {
                        if (u == null || args == null || args.length == 0) return false;
                        String key = args[0];
                        if (key == null) return false;
                        if (u.getUsuario() != null && u.getUsuario().equalsIgnoreCase(key)) return true;
                        if (u instanceof Empleado && args.length > 1) {
                            Empleado e = (Empleado) u;
                            return e.getCedula() != null && e.getCedula().equals(args[1]);
                        }
                        return false;
                    }
                }
        ));
    }
}