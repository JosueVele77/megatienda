package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Administrador;

public class AdministradorDAO extends GenericDAO<Administrador> {

    public AdministradorDAO() {
        super(new GenericFile<>(
                "administradores.txt",
                new Converter<Administrador>() {
                    @Override
                    public Administrador fromLine(String line) {
                        String[] p = line.split(";");
                        return new Administrador(p[0], p[1], p[2],p[3]);
                    }

                    @Override
                    public String toLine(Administrador a) {
                        return a.getUsuario() + ";" + a.getPassword() + ";" + a.getNombre();
                    }

                    @Override
                    public boolean match(Administrador a, String... args) {
                        return a.getUsuario().equalsIgnoreCase(args[0]);
                    }
                }
        ));
    }
}

