package model.logic;

import utils.RegexPatterns;

public class ValidacionesLogic {

    public boolean validarCedula(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) return false;

        try {
            int provincia = Integer.parseInt(cedula.substring(0, 2));
            int tercerDigito = Integer.parseInt(cedula.substring(2, 3));

            if (provincia < 1 || provincia > 24) return false;
            if (tercerDigito >= 6) return false; // Cédulas personales tienen 3er dígito < 6

            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int suma = 0;

            for (int i = 0; i < 9; i++) {
                int valor = Integer.parseInt(String.valueOf(cedula.charAt(i))) * coeficientes[i];
                if (valor >= 10) valor -= 9;
                suma += valor;
            }

            int verificador = Integer.parseInt(String.valueOf(cedula.charAt(9)));
            int residuo = suma % 10;
            int digitoCalculado = (residuo == 0) ? 0 : (10 - residuo);

            return verificador == digitoCalculado;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validación estricta de Teléfono Celular Ecuador (09xxxxxxxx)
    public boolean validarTelefono(String telefono) {
        if (telefono == null) return false;
        // Debe tener 10 dígitos y empezar con 09
        return telefono.matches("^09\\d{8}$");
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