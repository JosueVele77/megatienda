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
                        // Validamos que tenga al menos los campos básicos (Rol, User, Pass, Nombre, Cedula)
                        if (p.length < 5) return null;

                        // p[0] es el ROL ("VENDEDOR"), lo saltamos.
                        // p[1] es USUARIO
                        // p[2] es PASSWORD
                        // p[3] es NOMBRE
                        // p[4] es CEDULA
                        Vendedor v = new Vendedor(p[1], p[2], p[3], p[4]);

                        // Si existe el campo del booleano (posición 5), lo leemos
                        if (p.length > 5) {
                            v.setPrimerIngreso(Boolean.parseBoolean(p[5]));
                        }
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