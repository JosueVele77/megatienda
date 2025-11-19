package model.entities;

public class PagoEfectivo extends Pago {

    public PagoEfectivo(double monto) {
        super(monto);
    }

    @Override
    public double calcularTotal() {
        return monto; // sin recargos
    }
}
