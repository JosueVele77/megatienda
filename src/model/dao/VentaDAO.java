package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Venta;

public class VentaDAO extends GenericDAO<Venta> {

    public VentaDAO() {
        super(new GenericFile<>(
                "data/ventas.txt",
                new Converter<Venta>() {
                    @Override
                    public Venta fromLine(String line) {
                        if (line == null || line.trim().isEmpty()) return null;
                        String[] p = line.split(";");

                        // Validación para soportar archivos viejos sin tipoPago
                        String tipoPago = (p.length > 4) ? p[4] : "Desconocido";

                        return new Venta(
                                p[0],
                                p[1],
                                Double.parseDouble(p[2]),
                                p[3],
                                tipoPago // Nuevo parámetro
                        );
                    }

                    @Override
                    public String toLine(Venta v) {
                        return v.getCodigo() + ";" + v.getCedulaCliente() + ";" +
                                v.getTotal() + ";" + v.getFecha() + ";" + v.getTipoPago();
                    }

                    @Override
                    public boolean match(Venta v, String... args) {
                        return v.getCodigo().equals(args[0]);
                    }
                }
        ));
    }
}