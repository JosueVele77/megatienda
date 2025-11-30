package model.entities;

public class Venta {

    private String codigo;
    private String cedulaCliente;
    private double total;
    private String fecha;
    private String tipoPago;

    public Venta(String codigo, String cedulaCliente,
                 double total, String fecha, String tipoPago) {
        this.codigo = codigo;
        this.cedulaCliente = cedulaCliente;
        this.total = total;
        this.fecha = fecha;
        this.tipoPago = tipoPago;
    }

    public String getCodigo() { return codigo; }
    public String getCedulaCliente() { return cedulaCliente; }
    public double getTotal() { return total; }
    public String getFecha() { return fecha; }
    public String getTipoPago() { return tipoPago; }

    @Override
    public String toString() {
        return codigo + ";" + cedulaCliente + ";" + total + ";" + fecha + ";" + tipoPago;
    }
}
