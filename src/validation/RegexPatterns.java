package validation;

public class RegexPatterns {
    public static final String EMAIL = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String CEDULA = "^[0-9]{10}$";
    public static final String NAME = "^[A-Za-zÁÉÍÓÚáéíóú ]+$";
    public static final String PASSWORD = "^(?=.*[A-Z])(?=.*[@-/]).{8,}$";
}
