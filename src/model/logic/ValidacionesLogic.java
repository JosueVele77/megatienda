package model.logic;

import util.RegexPatterns;
import util.RegexValidator;

public class ValidacionesLogic {

    public boolean validarCedula(String cedula) {
        return RegexValidator.validate(cedula, RegexPatterns.CEDULA);
    }

    public boolean validarEmail(String email) {
        return RegexValidator.validate(email, RegexPatterns.EMAIL);
    }

    public boolean validarNombre(String nombre) {
        return RegexValidator.validate(nombre, RegexPatterns.NAME);
    }

    public boolean validarUsuario(String usuario) {
        if (usuario == null) return false;
        // ejemplo: usuario alfanumérico de 3-20 caracteres
        return usuario.matches("^[A-Za-z0-9_]{3,20}$");
    }

    public boolean validarPasswordSegura(String password) {
        return RegexValidator.validate(password, RegexPatterns.PASSWORD);
    }
}
