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
                        if (p.length < 5) return null; // Ahora esperamos 5 campos

                        Administrador a = new Administrador(p[0], p[1], p[2], p[3]);
                        a.setPrimerIngreso(Boolean.parseBoolean(p[4])); // Leemos el flag
                        return a;
                    }

                    @Override
                    public String toLine(Administrador a) {
                        // Guardamos el flag al final
                        return a.getUsuario() + ";" + a.getPassword() + ";" + a.getNombre() + ";" + a.getCedula() + ";" + a.isPrimerIngreso();
                    }

                    @Override
                    public boolean match(Administrador a, String... args) {
                        return a.getUsuario().equalsIgnoreCase(args[0]);
                    }
                }
        ));
    }
}