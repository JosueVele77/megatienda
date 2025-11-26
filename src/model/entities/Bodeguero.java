package model.entities;

public class Bodeguero extends Empleado {

    public Bodeguero(String usuario, String password, String nombre, String cedula,
                     String celular, String direccion, String fechaIngreso) {
        super(usuario, password, nombre, cedula, celular, direccion, fechaIngreso);
    }

    @Override
    public String getRol() {
        return "BODEGUERO";
    }
}
