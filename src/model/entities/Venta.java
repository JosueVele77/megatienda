package model.entities;

public class Venta {

    private String codigo;
    private String cedulaCliente;
    private double total;
    private String fecha;

    public Venta(String codigo, String cedulaCliente,
                 double total, String fecha) {
        this.codigo = codigo;
        this.cedulaCliente = cedulaCliente;
        this.total = total;
        this.fecha = fecha;
    }

    public String getCodigo() { return codigo; }
    public String getCedulaCliente() { return cedulaCliente; }
    public double getTotal() { return total; }
    public String getFecha() { return fecha; }

    @Override
    public String toString() {
        return codigo + ";" + cedulaCliente + ";" + total + ";" + fecha;
    }
}
