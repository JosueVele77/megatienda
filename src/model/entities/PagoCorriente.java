package model.entities;

public class PagoCorriente extends Pago {

    public PagoCorriente(double monto) {
        super(monto);
    }

    @Override
    public double calcularTotal() {
        return monto * 1.05; // ejemplo 5% interés
    }
}
