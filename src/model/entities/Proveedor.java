package model.entities;

public class Proveedor {
    private String codigo; // Antes RUC
    private String razonSocial; // Antes nombreEmpresa
    private String contacto; // Nuevo
    private String telefono; // Nuevo
    private String email; // Nuevo

    public Proveedor(String codigo, String razonSocial, String contacto, String telefono, String email) {
        this.codigo = codigo;
        this.razonSocial = razonSocial;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public String getRazonSocial() { return razonSocial; }
    public String getContacto() { return contacto; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        // Se usa para mostrar en el ComboBox del formulario de productos
        return razonSocial;
    }

    public String toFileString() {
        return codigo + ";" + razonSocial + ";" + contacto + ";" + telefono + ";" + email;
    }
}