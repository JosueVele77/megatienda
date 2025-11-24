package model.entities;

public abstract class Usuario {

    protected String usuario;
    protected String password;
    protected String rol;
    protected boolean primerIngreso; // NUEVO CAMPO

    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
        this.primerIngreso = true; // Por defecto, todo usuario nuevo debe cambiar clave
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isPrimerIngreso() { return primerIngreso; }
    public void setPrimerIngreso(boolean primerIngreso) { this.primerIngreso = primerIngreso; }

    @Override
    public String toString() {
        return usuario + ";" + password + ";" + rol + ";" + primerIngreso;
    }
}