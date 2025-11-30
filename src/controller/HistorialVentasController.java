package controller;

import model.entities.Cliente;
import model.entities.DetalleVenta;
import model.entities.Producto;
import model.entities.Venta;
import model.logic.ClienteLogic;
import model.logic.ProductoLogic;
import model.logic.VentaLogic;
import view.HistorialVentasView;

import java.io.IOException;
import java.util.List;

public class HistorialVentasController {

    private final HistorialVentasView view;
    private final VentaLogic ventaLogic;
    private final ClienteLogic clienteLogic;
    private final ProductoLogic productoLogic;

    public HistorialVentasController(HistorialVentasView view) {
        this.view = view;
        this.ventaLogic = new VentaLogic();
        this.clienteLogic = new ClienteLogic();
        this.productoLogic = new ProductoLogic();

        view.btnCerrar.addActionListener(e -> view.dispose());
    }

    public void iniciar() {
        cargarDatos();
        view.setVisible(true);
    }

    private void cargarDatos() {
        view.modelo.setRowCount(0);
        try {
            // Cargar todas las listas en memoria para cruzar datos
            List<Venta> ventas = ventaLogic.listarVentas();
            List<DetalleVenta> detalles = ventaLogic.listarTodosDetalles();

            // Recorremos las ventas (de la más reciente a la más antigua idealmente)
            // Aquí simplemente las recorremos en orden
            for (Venta venta : ventas) {
                if (venta == null) continue;

                // 1. Buscar Cliente
                Cliente cliente = clienteLogic.buscarCliente(venta.getCedulaCliente());
                String nombreCliente = (cliente != null) ? cliente.getNombre() : "Consumidor Final";
                String cedulaCliente = venta.getCedulaCliente();
                String formaPago = venta.getTipoPago();
                String totalVenta = String.format("$%.2f", venta.getTotal());

                // 2. Buscar Productos asociados a esta venta
                for (DetalleVenta det : detalles) {
                    if (det != null && det.getCodigoVenta().equals(venta.getCodigo())) {

                        // 3. Obtener Nombre del Producto por su código
                        Producto prod = productoLogic.buscarProducto(det.getCodigoProducto());
                        String nombreProducto = (prod != null) ? prod.getNombre() : "Prod. eliminado";
                        int cantidad = det.getCantidad();

                        // Agregar fila: [Cliente, Cedula, Pago, Producto, Cantidad, TotalVenta]
                        view.modelo.addRow(new Object[]{
                                nombreCliente,
                                cedulaCliente,
                                formaPago,
                                nombreProducto,
                                cantidad,
                                totalVenta
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}