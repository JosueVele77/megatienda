package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Proveedor;

public class ProveedorDAO extends GenericDAO<Proveedor> {

    public ProveedorDAO() {
        super(new GenericFile<>(
                "proveedores.txt",
                new Converter<Proveedor>() {
                    @Override
                    public Proveedor fromLine(String line) {
                        String[] p = line.split(";");
                        return new Proveedor(p[0], p[1], p[2], p[3]);
                    }

                    @Override
                    public String toLine(Proveedor p) {
                        return p.getRuc() + ";" + p.getNombreEmpresa() + ";" +
                                p.getRepresentante() + ";" + p.getArea();
                    }

                    @Override
                    public boolean match(Proveedor p, String... args) {
                        return p.getRuc().equals(args[0]);
                    }
                }
        ));
    }
}
