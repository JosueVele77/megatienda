package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Cliente;

public class ClienteDAO extends GenericDAO<Cliente> {

    public ClienteDAO() {
        super(new GenericFile<>(
                "data/clientes.txt",
                new Converter<Cliente>() {
                    @Override
                    public Cliente fromLine(String line) {
                        if (line == null || line.trim().isEmpty()) return null;
                        String[] p = line.split(";");

                        // Validación de compatibilidad con archivos viejos (4 columnas)
                        String telefono = (p.length > 3) ? p[3] : "Sin Teléfono";
                        String direccion = (p.length > 4) ? p[4] : "Sin Dirección";

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
                }
        ));
    }
}