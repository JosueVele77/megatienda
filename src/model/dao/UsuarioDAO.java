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

                    // Formato: usuario;password;ROL;nombre;cedula
                    @Override
                    public Usuario fromLine(String line) {
                        if (line == null || line.trim().isEmpty()) return null;
                        String[] p = line.split(";", -1); // -1 para mantener campos vacíos
                        // Asegurar longitud mínima
                        String usuario = p.length > 0 ? p[0] : "";
                        String password = p.length > 1 ? p[1] : "";
                        String rol = p.length > 2 ? p[2] : "";
                        String nombre = p.length > 3 ? p[3] : "";
                        String cedula = p.length > 4 ? p[4] : "";

                        switch (rol.toUpperCase()) {
                            case "ADMINISTRADOR":
                                // Administrador tiene (usuario, password)
                                return new Administrador(usuario, password, nombre, cedula);

                            case "VENDEDOR":
                                // Vendedor extiende Empleado -> (usuario, password, nombre, cedula)
                                return new Vendedor(usuario, password, nombre, cedula);

                            case "BODEGUERO":
                                return new Bodeguero(usuario, password, nombre, cedula);

                            default:
                                // Si aparece un rol desconocido, puedes devolver null o lanzar excepción.
                                // Aquí devolvemos null para que el resto del sistema lo ignore.
                                return null;
                        }
                    }

                    @Override
                    public String toLine(Usuario u) {
                        if (u == null) return "";
                        // campos comunes
                        String usuario = u.getUsuario() != null ? u.getUsuario() : "";
                        String password = u.getPassword() != null ? u.getPassword() : "";
                        String rol = u.getRol() != null ? u.getRol() : "";

                        // Por defecto nombre y cedula vacíos
                        String nombre = "";
                        String cedula = "";

                        // Si es instancia de Empleado, extraemos nombre/cedula
                        if (u instanceof Empleado) {
                            Empleado e = (Empleado) u;
                            nombre = e.getNombre() != null ? e.getNombre() : "";
                            cedula = e.getCedula() != null ? e.getCedula() : "";
                        }

                        // Resultado consistente: 5 campos
                        return String.join(";", usuario, password, rol, nombre, cedula);
                    }

                    @Override
                    public boolean match(Usuario u, String... args) {
                        // Uso típico: match por usuario (args[0]) o por cedula (args[1]) si es empleado
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
