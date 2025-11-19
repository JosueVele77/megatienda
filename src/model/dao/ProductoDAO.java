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
                        String[] p = line.split(";");
                        return new Producto(
                                p[0],
                                p[1],
                                Double.parseDouble(p[2]),
                                Integer.parseInt(p[3])
                        );
                    }

                    @Override
                    public String toLine(Producto p) {
                        return p.getCodigo() + ";" + p.getNombre() + ";" +
                                p.getPrecio() + ";" + p.getStock();
                    }

                    @Override
                    public boolean match(Producto p, String... args) {
                        return p.getCodigo().equals(args[0]);
                    }
                }
        ));
    }
}
