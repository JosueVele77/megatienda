package model.entities;

public abstract class Usuario {

    protected String usuario;
    protected String password;
    protected String rol;

    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return usuario + ";" + password + ";" + rol;
    }
}
