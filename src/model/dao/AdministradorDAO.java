package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Administrador;

public class AdministradorDAO extends GenericDAO<Administrador> {

    public AdministradorDAO() {
        super(new GenericFile<>(
                "data/administradores.txt",
                new Converter<Administrador>() {
                    @Override
                    public Administrador fromLine(String line) {
                        if (line == null || line.trim().isEmpty()) return null;
                        String[] p = line.split(";");

                        // Validación básica: Si el archivo es viejo (tiene menos campos), devolvemos null
                        // o podríamos asignar valores por defecto para evitar errores.
                        if (p.length < 4) return null;

                        // Leemos los campos básicos
                        String usuario = p[0];
                        String password = p[1];
                        String nombre = p[2];
                        String cedula = p[3];

                        // Leemos los NUEVOS CAMPOS con seguridad (por si el archivo es antiguo)
                        String celular = (p.length > 4) ? p[4] : "Sin Celular";
                        String direccion = (p.length > 5) ? p[5] : "Sin Dirección";
                        String fecha = (p.length > 6) ? p[6] : "2024-01-01";

                        // Creamos el objeto con el NUEVO CONSTRUCTOR de 7 parámetros
                        Administrador a = new Administrador(usuario, password, nombre, cedula, celular, direccion, fecha);

                        // Leemos el flag de primer ingreso si existe (ahora estaría en la posición 7)
                        if (p.length > 7) {
                            a.setPrimerIngreso(Boolean.parseBoolean(p[7]));
                        }
                        return a;
                    }

                    @Override
                    public String toLine(Administrador a) {
                        // AQUÍ ESTABA EL PROBLEMA: Antes no guardábamos los nuevos datos.
                        // Ahora guardamos los 8 datos separados por punto y coma.
                        return a.getUsuario() + ";" +
                                a.getPassword() + ";" +
                                a.getNombre() + ";" +
                                a.getCedula() + ";" +
                                a.getCelular() + ";" +
                                a.getDireccion() + ";" +
                                a.getFechaIngreso() + ";" +
                                a.isPrimerIngreso();
                    }

                    @Override
                    public boolean match(Administrador a, String... args) {
                        return a.getUsuario().equalsIgnoreCase(args[0]);
                    }
                }
        ));
    }
}