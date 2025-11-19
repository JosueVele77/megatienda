package model.logic;

import utils.RegexPatterns;

public class ValidacionesLogic {

    public boolean validarCedula(String cedula) {
        if (cedula == null) return false;
        return cedula.matches(RegexPatterns.CEDULA);
    }

    public boolean validarEmail(String email) {
        if (email == null) return false;
        return email.matches(RegexPatterns.EMAIL);
    }

    public boolean validarNombre(String nombre) {
        if (nombre == null) return false;
        return nombre.matches(RegexPatterns.NAME);
    }

    public boolean validarUsuario(String usuario) {
        if (usuario == null) return false;
        // Mantenemos tu lógica original o podrías mover este regex a RegexPatterns también
        return usuario.matches("^[A-Za-z0-9_]{3,20}$");
    }

    public boolean validarPasswordSegura(String password) {
        if (password == null) return false;
        return password.matches(RegexPatterns.PASSWORD);
    }
}