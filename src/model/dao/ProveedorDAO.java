package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Proveedor;

public class ProveedorDAO extends GenericDAO<Proveedor> {

    public ProveedorDAO() {
        super(new GenericFile<>(
                "data/proveedores.txt",
                new Converter<Proveedor>() {
                    @Override
                    public Proveedor fromLine(String line) {
                        if(line == null || line.isEmpty()) return null;
                        String[] p = line.split(";");
                        if(p.length < 5) return null; // Validación básica
                        return new Proveedor(p[0], p[1], p[2], p[3], p[4]);
                    }

                    @Override
                    public String toLine(Proveedor p) {
                        return p.toFileString();
                    }

                    @Override
                    public boolean match(Proveedor p, String... args) {
                        return p.getCodigo().equals(args[0]);
                    }
                }
        ));
    }
}