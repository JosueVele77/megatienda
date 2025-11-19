package model.entities;

public class PagoDiferido3 extends Pago {

    public PagoDiferido3(double monto) {
        super(monto);
    }

    @Override
    public double calcularTotal() {
        return monto * 1.10; // ejemplo 10% por 3 meses
    }
}
