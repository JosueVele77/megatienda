package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Bodeguero;

public class BodegueroDAO extends GenericDAO<Bodeguero> {

    public BodegueroDAO() {
        super(new GenericFile<>(
                "data/bodegueros.txt",
                new Converter<Bodeguero>() {
                    @Override
                    public Bodeguero fromLine(String line) {
                        String[] p = line.split(";");
                        return new Bodeguero(p[0], p[1], p[2],p[3]);
                    }

                    @Override
                    public String toLine(Bodeguero b) {
                        return b.getUsuario() + ";" + b.getPassword() + ";" + b.getNombre();
                    }

                    @Override
                    public boolean match(Bodeguero b, String... args) {
                        return b.getUsuario().equalsIgnoreCase(args[0]);
                    }
                }
        ));
    }
}
