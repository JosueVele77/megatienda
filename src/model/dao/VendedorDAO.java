package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Vendedor;

public class VendedorDAO extends GenericDAO<Vendedor> {

    public VendedorDAO() {
        super(new GenericFile<>(
                "data/vendedores.txt",
                new Converter<Vendedor>() {
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

                        // Validamos si existen los campos nuevos para evitar errores con archivos viejos
                        String celular = (p.length > 5) ? p[5] : "Sin Celular";
                        String direccion = (p.length > 6) ? p[6] : "Sin Dirección";
                        String fecha = (p.length > 7) ? p[7] : "2024-01-01";

                        Vendedor v = new Vendedor(usuario, password, nombre, cedula, celular, direccion, fecha);

                        // Leemos el booleano al final (índice 8)
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
                }
        ));
    }
}