package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.DetalleVenta;

public class DetalleVentaDAO extends GenericDAO<DetalleVenta> {

    public DetalleVentaDAO() {
        super(new GenericFile<>(
                "detalles_venta.txt",
                new Converter<DetalleVenta>() {
                    @Override
                    public DetalleVenta fromLine(String line) {
                        String[] p = line.split(";");
                        return new DetalleVenta(
                                p[0],
                                p[1],
                                Integer.parseInt(p[2]),
                                Double.parseDouble(p[3])
                        );
                    }

                    @Override
                    public String toLine(DetalleVenta d) {
                        return d.getCodigoVenta() + ";" + d.getCodigoProducto() + ";" +
                                d.getCantidad() + ";" + d.getSubtotal();
                    }

                    @Override
                    public boolean match(DetalleVenta d, String... args) {
                        return d.getCodigoVenta().equals(args[0]);
                    }
                }
        ));
    }
}
