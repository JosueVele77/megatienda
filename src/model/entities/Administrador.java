package model.entities;

public class Administrador extends Empleado {

    // Constructor actualizado para recibir 7 parámetros
    public Administrador(String usuario, String password, String nombre, String cedula,
                         String celular, String direccion, String fechaIngreso) {
        // Se los pasamos a la clase padre (Empleado)
        super(usuario, password, nombre, cedula, celular, direccion, fechaIngreso);
    }

    @Override
    public String getRol() {
        return "ADMINISTRADOR";
    }
}

