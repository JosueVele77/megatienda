package model.logic;

import model.dao.DetalleVentaDAO;
import model.dao.VentaDAO;
import model.entities.DetalleVenta;
import model.entities.Producto;
import model.entities.Venta;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class VentaLogic {

    private final VentaDAO ventaDao;
    private final DetalleVentaDAO detalleDao;
    private final ProductoLogic productoLogic;

    public VentaLogic() {
        this.ventaDao = new VentaDAO();
        this.detalleDao = new DetalleVentaDAO();
        this.productoLogic = new ProductoLogic();
    }

    /**
     * Crea una venta con sus detalles: valida stock, actualiza stock, guarda venta y detalles.
     * Devuelve la venta creada (con código) o lanza excepción si algo falla.
     */
    public Venta crearVenta(String cedulaCliente, List<DetalleVenta> detalles) throws IOException {
        if (detalles == null || detalles.isEmpty()) throw new IllegalArgumentException("Detalles vacíos");
        // validar stock para todos los productos primero
        for (DetalleVenta d : detalles) {
            Producto p = productoLogic.buscarProducto(d.getCodigoProducto());
            if (p == null) throw new IllegalStateException("Producto no existe: " + d.getCodigoProducto());
            if (p.getStock() < d.getCantidad()) throw new IllegalStateException("Stock insuficiente para: " + p.getCodigo());
        }

        // generar código único
        String codigoVenta = UUID.randomUUID().toString();
        double total = 0.0;
        for (DetalleVenta d : detalles) {
            total += d.getSubtotal();
        }

        String fecha = LocalDate.now().toString();
        Venta v = new Venta(codigoVenta, cedulaCliente, total, fecha);
        ventaDao.add(v);

        // guardar detalles y actualizar stock
        for (DetalleVenta d : detalles) {
            // asociar al código de la venta
            DetalleVenta detalle = new DetalleVenta(codigoVenta, d.getCodigoProducto(), d.getCantidad(), d.getSubtotal());
            detalleDao.add(detalle);
            productoLogic.restarStock(d.getCodigoProducto(), d.getCantidad());
        }

        return v;
    }

    public List<Venta> listarVentas() throws IOException {
        return ventaDao.getAll();
    }
}
