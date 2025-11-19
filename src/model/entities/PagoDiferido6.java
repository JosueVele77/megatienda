package model.entities;

public class PagoDiferido6 extends Pago {

    public PagoDiferido6(double monto) {
        super(monto);
    }

    @Override
    public double calcularTotal() {
        return monto * 1.20; // ejemplo 20% por 6 meses
    }
}
