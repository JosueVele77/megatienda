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
                        String[] p = line.split(";");
                        if (p.length < 5) return null;

                        Vendedor v = new Vendedor(p[0], p[1], p[2], p[3]);
                        v.setPrimerIngreso(Boolean.parseBoolean(p[4]));
                        return v;
                    }

                    @Override
                    public String toLine(Vendedor v) {
                        return v.getUsuario() + ";" + v.getPassword() + ";" + v.getNombre() + ";" + v.getCedula() + ";" + v.isPrimerIngreso();
                    }

                    @Override
                    public boolean match(Vendedor v, String... args) {
                        return v.getUsuario().equalsIgnoreCase(args[0]);
                    }
                }
        ));
    }
}