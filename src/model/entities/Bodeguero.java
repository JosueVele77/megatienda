package model.entities;

public class Bodeguero extends Empleado {

    public Bodeguero(String usuario, String password,
                     String nombre, String cedula) {
        super(usuario, password, nombre, cedula);
    }

    @Override
    public String getRol() {
        return "BODEGUERO";
    }
}
