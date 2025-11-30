package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Producto;

public class ProductoDAO extends GenericDAO<Producto> {

    public ProductoDAO() {
        super(new GenericFile<>(
                "data/productos.txt",
                new Converter<Producto>() {
                    @Override
                    public Producto fromLine(String line) {
                        if(line == null || line.trim().isEmpty()) return null;
                        String[] p = line.split(";");
                        // Validamos longitud para evitar errores con archivos viejos
                        String codProv = (p.length > 4) ? p[4] : "Sin Proveedor";

                        return new Producto(
                                p[0],
                                p[1],
                                Double.parseDouble(p[2]),
                                Integer.parseInt(p[3]),
                                codProv
                        );
                    }

                    @Override
                    public String toLine(Producto p) {
                        return p.toString();
                    }

                    @Override
                    public boolean match(Producto p, String... args) {
                        return p.getCodigo().equals(args[0]);
                    }
                }
        ));
    }
}