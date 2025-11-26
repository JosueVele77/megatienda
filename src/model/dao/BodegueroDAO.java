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
                        if (line == null || line.trim().isEmpty()) return null;
                        String[] p = line.split(";");

                        if (p.length < 5) return null;

                        // Saltamos p[0] que es "BODEGUERO"
                        String usuario = p[1];
                        String password = p[2];
                        String nombre = p[3];
                        String cedula = p[4];

                        String celular = (p.length > 5) ? p[5] : "Sin Celular";
                        String direccion = (p.length > 6) ? p[6] : "Sin Dirección";
                        String fecha = (p.length > 7) ? p[7] : "2024-01-01";

                        Bodeguero b = new Bodeguero(usuario, password, nombre, cedula, celular, direccion, fecha);

                        if (p.length > 8) {
                            b.setPrimerIngreso(Boolean.parseBoolean(p[8]));
                        }
                        return b;
                    }

                    @Override
                    public String toLine(Bodeguero b) {
                        return "BODEGUERO;" +
                                b.getUsuario() + ";" +
                                b.getPassword() + ";" +
                                b.getNombre() + ";" +
                                b.getCedula() + ";" +
                                b.getCelular() + ";" +
                                b.getDireccion() + ";" +
                                b.getFechaIngreso() + ";" +
                                b.isPrimerIngreso();
                    }

                    @Override
                    public boolean match(Bodeguero b, String... args) {
                        return b.getUsuario().equalsIgnoreCase(args[0]);
                    }
                }
        ));
    }
}