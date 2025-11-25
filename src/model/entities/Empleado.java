package model.entities;

public abstract class Empleado extends Usuario {

    protected String nombre;
    protected String cedula;

    public Empleado(String usuario, String password,String nombre, String cedula) {
        super(usuario, password);
        this.nombre = nombre;
        this.cedula = cedula;
    }

    public String getNombre() { return nombre; }
    public String getCedula() { return cedula; }

    @Override
    public String toString() {
        return getRol() + ";" + usuario + ";" + password + ";" + nombre + ";" + cedula + ";" + primerIngreso;
    }
}
