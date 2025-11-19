package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Venta;

public class VentaDAO extends GenericDAO<Venta> {

    public VentaDAO() {
        super(new GenericFile<>(
                "ventas.txt",
                new Converter<Venta>() {
                    @Override
                    public Venta fromLine(String line) {
                        String[] p = line.split(";");
                        return new Venta(
                                p[0],
                                p[1],
                                Double.parseDouble(p[2]),
                                p[3]
                        );
                    }

                    @Override
                    public String toLine(Venta v) {
                        return v.getCodigo() + ";" + v.getCedulaCliente() + ";" +
                                v.getTotal() + ";" + v.getFecha();
                    }

                    @Override
                    public boolean match(Venta v, String... args) {
                        return v.getCodigo().equals(args[0]);
                    }
                }
        ));
    }
}
