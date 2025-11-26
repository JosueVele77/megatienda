package model.entities;

public class Vendedor extends Empleado {

    public Vendedor(String usuario, String password, String nombre, String cedula,
                    String celular, String direccion, String fechaIngreso) {
        super(usuario, password, nombre, cedula, celular, direccion, fechaIngreso);
    }

    @Override
    public String getRol() {
        return "VENDEDOR";
    }
}