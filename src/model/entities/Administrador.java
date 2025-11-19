package model.entities;

public class Administrador extends Empleado {

    public Administrador(String usuario, String password, String nombre, String cedula) {
        super(usuario, password, nombre, cedula);
    }

    @Override
    public String getRol() {
        return "ADMINISTRADOR";
    }
}

