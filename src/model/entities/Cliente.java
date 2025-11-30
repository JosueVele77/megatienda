package model.entities;

public class Cliente {

    private String cedula;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;

    public Cliente(String cedula, String nombre,
                   String correo, String telefono,  String direccion) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }

    @Override
    public String toString() {
        return cedula + ";" + nombre + ";" + correo + ";" + telefono + ";" + direccion;
    }
}
