package model.entities;

public abstract class Empleado extends Usuario {

    protected String nombre;
    protected String cedula;
    // --- NUEVOS CAMPOS ---
    protected String celular;
    protected String direccion;
    protected String fechaIngreso;

    public Empleado(String usuario, String password, String nombre, String cedula,
                    String celular, String direccion, String fechaIngreso) {
        super(usuario, password);
        this.nombre = nombre;
        this.cedula = cedula;
        this.celular = celular;
        this.direccion = direccion;
        this.fechaIngreso = fechaIngreso;
    }

    public String getNombre() { return nombre; }
    public String getCedula() { return cedula; }

    // --- NUEVOS GETTERS ---
    public String getCelular() { return celular; }
    public String getDireccion() { return direccion; }
    public String getFechaIngreso() { return fechaIngreso; }

    @Override
    public String toString() {
        // Actualizamos el toString para incluir todo
        return getRol() + ";" + usuario + ";" + password + ";" + nombre + ";" + cedula + ";"
                + celular + ";" + direccion + ";" + fechaIngreso + ";" + primerIngreso;
    }
}
