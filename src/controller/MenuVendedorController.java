package controller;

import model.entities.*;
import model.logic.ClienteLogic;
import model.logic.ProductoLogic;
import model.logic.VentaLogic;
import view.HistorialVentasView;
import view.MenuVendedorView;
import view.RegistroClienteView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuVendedorController implements ActionListener {

    private final MenuVendedorView view;
    private final Usuario vendedor;

    // Logics
    private final ClienteLogic clienteLogic;
    private final ProductoLogic productoLogic;
    private final VentaLogic ventaLogic;

    // Estado de la venta actual
    private Cliente clienteActual;
    private List<DetalleVenta> carrito;
    private double subtotalAcumulado = 0.0;

    public MenuVendedorController(MenuVendedorView view, Usuario vendedor) {
        this.view = view;
        this.vendedor = vendedor;
        this.clienteLogic = new ClienteLogic();
        this.productoLogic = new ProductoLogic();
        this.ventaLogic = new VentaLogic();
        this.carrito = new ArrayList<>();

        // --- NUEVO: ACTUALIZAR ETIQUETA DE USUARIO ---
        String nombreMostrar = vendedor.getUsuario(); // Por defecto el usuario
        if (vendedor instanceof Empleado) {
            nombreMostrar = ((Empleado) vendedor).getNombre();
        }
        // Actualizamos el label que creamos en la vista
        view.lblInfoVendedor.setText("Vendedor: " + nombreMostrar);
        // ---------------------------------------------

        configurarListeners();
    }

    private void configurarListeners() {
        view.btnBuscarCliente.addActionListener(this);
        view.btnAgregarProducto.addActionListener(this);
        view.btnProcesarPago.addActionListener(this);

        // --- VERIFICA QUE ESTA LÍNEA ESTÉ SOLO UNA VEZ ---
        view.btnRegistrarCliente.addActionListener(this);
        // -------------------------------------------------

        view.btnNuevaVenta.addActionListener(e -> limpiarVenta());

        view.btnSalir.addActionListener(e -> {
            view.dispose();
            new view.LoginView().setVisible(true);
        });

        view.cmbFormaPago.addActionListener(e -> calcularCuotas());

        // El nuevo botón de historial
        view.btnHistorial.addActionListener(e -> abrirHistorial());
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.btnBuscarCliente) {
            buscarCliente();
        }
        else if (source == view.btnAgregarProducto) {
            agregarProducto();
        }
        else if (source == view.btnRegistrarCliente) {
            // Abrir tu ventana de registro existente
            new RegistroClienteView().setVisible(true);
        }
        else if (source == view.btnProcesarPago) {
            procesarVenta();
        }
    }

    // --- LÓGICA DE NEGOCIO ---

    private void buscarCliente() {
        String cedula = view.txtBusquedaCliente.getText().trim();
        try {
            clienteActual = clienteLogic.buscarCliente(cedula);
            if (clienteActual != null) {
                view.lblClienteNombre.setText("Cliente: " + clienteActual.getNombre());
                view.lblClienteCedula.setText("CI: " + clienteActual.getCedula());
                view.lblClienteNombre.setForeground(new java.awt.Color(40, 167, 69)); // Verde
            } else {
                view.lblClienteNombre.setText("Cliente no encontrado");
                view.lblClienteCedula.setText("");
                view.lblClienteNombre.setForeground(new java.awt.Color(220, 53, 69)); // Rojo
                clienteActual = null;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al leer clientes");
        }
    }

    private void agregarProducto() {
        String codigo = view.txtBusquedaProducto.getText().trim();
        int cantidad = (int) view.spinnerCantidad.getValue();

        try {
            Producto p = productoLogic.buscarProducto(codigo);

            if (p == null) {
                JOptionPane.showMessageDialog(view, "Producto no existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (p.getStock() < cantidad) {
                JOptionPane.showMessageDialog(view, "Stock insuficiente (Disponible: " + p.getStock() + ")", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crear detalle temporal
            double subtotalItem = p.getPrecio() * cantidad;
            DetalleVenta detalle = new DetalleVenta("temp", p.getCodigo(), cantidad, subtotalItem);

            // Agregar al carrito en memoria
            carrito.add(detalle);

            // Agregar visualmente a la tabla
            view.modeloTabla.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    cantidad,
                    String.format("$%.2f", p.getPrecio()),
                    String.format("$%.2f", subtotalItem)
            });

            // Recalcular totales
            actualizarTotales();

            // Limpiar campo producto
            view.txtBusquedaProducto.setText("");
            view.spinnerCantidad.setValue(1);
            view.txtBusquedaProducto.requestFocus();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void actualizarTotales() {
        subtotalAcumulado = 0.0;
        for (DetalleVenta d : carrito) {
            subtotalAcumulado += d.getSubtotal();
        }

        double iva = subtotalAcumulado * 0.15;
        double total = subtotalAcumulado + iva;

        view.lblSubtotal.setText(String.format("Subtotal: $%.2f", subtotalAcumulado));
        view.lblIva.setText(String.format("IVA (15%%): $%.2f", iva));
        view.lblTotal.setText(String.format("TOTAL: $%.2f", total));

        calcularCuotas();
    }

    private void calcularCuotas() {
        double totalBase = subtotalAcumulado * 1.15;
        if (totalBase == 0) return;

        String tipoPago = (String) view.cmbFormaPago.getSelectedItem();
        view.lblInfoPago.setText(" "); // Limpiar

        Pago pagoObj = null;

        if (tipoPago.contains("DIFERIDO 3")) {
            pagoObj = new PagoDiferido3(totalBase);
            double totalConInteres = pagoObj.calcularTotal();
            double cuota = totalConInteres / 3;
            view.lblInfoPago.setText(String.format("3 cuotas de: $%.2f (Total: $%.2f)", cuota, totalConInteres));
        }
        else if (tipoPago.contains("DIFERIDO 6")) {
            pagoObj = new PagoDiferido6(totalBase);
            double totalConInteres = pagoObj.calcularTotal();
            double cuota = totalConInteres / 6;
            view.lblInfoPago.setText(String.format("6 cuotas de: $%.2f (Total: $%.2f)", cuota, totalConInteres));
        }
        else if (tipoPago.contains("CORRIENTE")) {
            pagoObj = new PagoCorriente(totalBase);
            view.lblInfoPago.setText(String.format("Pago corriente total: $%.2f", pagoObj.calcularTotal()));
        }
    }

    private void abrirHistorial() {
        HistorialVentasView hView = new HistorialVentasView(view);
        new HistorialVentasController(hView).iniciar();
    }

    private void procesarVenta() {
        if (clienteActual == null) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un cliente válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(view, "El carrito está vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalBase = subtotalAcumulado * 1.15;
        double totalFinal = totalBase;
        String seleccion = (String) view.cmbFormaPago.getSelectedItem();
        String detallePago = "Pago Contado"; // Para mostrar en pantalla
        String tipoPagoBD = "EFECTIVO";

        if (seleccion.contains("DIFERIDO 3")) {
            totalFinal = new PagoDiferido3(totalBase).calcularTotal();
            detallePago = "Diferido a 3 meses";
        } else if (seleccion.contains("DIFERIDO 6")) {
            totalFinal = new PagoDiferido6(totalBase).calcularTotal();
            detallePago = "Diferido a 6 meses";
        } else if (seleccion.contains("CORRIENTE")) {
            totalFinal = new PagoCorriente(totalBase).calcularTotal();
            detallePago = "Crédito Corriente";
        }

        boolean pagoAprobado = view.mostrarPasarelaPago(totalFinal, detallePago);

        if (pagoAprobado) {
            try {
                Venta ventaRealizada = ventaLogic.crearVenta(clienteActual.getCedula(), carrito, tipoPagoBD);

                JOptionPane.showMessageDialog(view, "Venta registrada...");
                limpiarVenta();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Error crítico al guardar venta: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void limpiarVenta() {
        carrito.clear();
        view.modeloTabla.setRowCount(0);
        clienteActual = null;
        view.lblClienteNombre.setText("Cliente: -");
        view.lblClienteCedula.setText("CI: -");
        view.txtBusquedaCliente.setText("");
        view.lblClienteNombre.setForeground(java.awt.Color.GRAY);
        actualizarTotales();
    }
}