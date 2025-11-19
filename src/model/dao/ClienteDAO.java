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
                        String[] p = line.split(";");
                        return new Cliente(p[0], p[1], p[2], p[3]);
                    }

                    @Override
                    public String toLine(Cliente c) {
                        return c.getCedula() + ";" + c.getNombre() + ";" +
                                c.getCorreo() + ";" + c.getDireccion();
                    }

                    @Override
                    public boolean match(Cliente c, String... args) {
                        return c.getCedula().equals(args[0]);
                    }
                }
        ));
    }
}
