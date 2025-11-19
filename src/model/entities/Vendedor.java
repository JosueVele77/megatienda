package model.entities;

public class Vendedor extends Empleado {

    public Vendedor(String usuario, String password,
                    String nombre, String cedula) {
        super(usuario, password, nombre, cedula);
    }

    @Override
    public String getRol() {
        return "VENDEDOR";
    }
}
