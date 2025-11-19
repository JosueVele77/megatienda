package model.entities;

public class DetalleVenta {

    private String codigoVenta;
    private String codigoProducto;
    private int cantidad;
    private double subtotal;

    public DetalleVenta(String codigoVenta, String codigoProducto,
                        int cantidad, double subtotal) {
        this.codigoVenta = codigoVenta;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public String getCodigoVenta() { return codigoVenta; }
    public String getCodigoProducto() { return codigoProducto; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return subtotal; }

    @Override
    public String toString() {
        return codigoVenta + ";" + codigoProducto + ";" + cantidad + ";" + subtotal;
    }
}
