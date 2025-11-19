package model.logic;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordLogic {

    /** Hashea usando SHA-256 */
    public String hash(String password) {
        if (password == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] b = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(b);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /** Compara: acepta tanto contraseña en claro guardada (legacy) como hasheada */
    public boolean compare(String rawPassword, String stored) {
        if (rawPassword == null || stored == null) return false;
        String hashed = hash(rawPassword);
        // acepta si stored ya era hash o estaba en claro
        return stored.equals(hashed) || stored.equals(rawPassword);
    }

    /** Valida fuerza mínima (puede usarse desde EmpleadoLogic) */
    public boolean validateStrength(String password) {
        if (password == null) return false;
        // Ejemplo simple: longitud >= 8
        return password.length() >= 8;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
