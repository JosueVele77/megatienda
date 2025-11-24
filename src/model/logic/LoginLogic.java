package model.logic;

import model.dao.AdministradorDAO;
import model.dao.BodegueroDAO;
import model.dao.VendedorDAO;
import model.entities.Administrador;
import model.entities.Usuario;

import java.io.IOException;
import java.util.List;

public class LoginLogic {

    private final AdministradorDAO adminDao;
    private final VendedorDAO vendedorDao;
    private final BodegueroDAO bodegueroDao;
    private final PasswordLogic passwordLogic;

    public LoginLogic() {
        this.adminDao = new AdministradorDAO();
        this.vendedorDao = new VendedorDAO();
        this.bodegueroDao = new BodegueroDAO();
        this.passwordLogic = new PasswordLogic();
    }

    /**
     * Intenta autenticar y devuelve la instancia de Usuario (Administrador/Vendedor/Bodeguero)
     * o null si no existe o contraseña incorrecta.
     */
    public Usuario login(String usuario, String password) throws IOException {
        Usuario u;

        // --- INICIO DE CODIGO DE DEPURACION (BORRAR LUEGO) ---
        System.out.println("--- INTENTO DE LOGIN ---");
        System.out.println("Buscando usuario: " + usuario);
        System.out.println("Contraseña ingresada: " + password);

        List<Administrador> admins = adminDao.getAll();
        System.out.println("Admins encontrados en archivo: " + admins.size());
        for(Administrador a : admins) {
            if(a != null) {
                System.out.println(" -> Leído: " + a.getUsuario() + " | Pass: " + a.getPassword());
                boolean check = passwordLogic.compare(password, a.getPassword());
                System.out.println("    ¿Coincide contraseña?: " + check);
            }
        }
        // --- FIN DE CODIGO DE DEPURACION ---

        u = findUsuarioInList(adminDao.getAll(), usuario);
        if (u != null && passwordLogic.compare(password, u.getPassword())) return u;

        return null;
    }

    private Usuario findUsuarioInList(List<? extends Usuario> list, String usuario) {
        if (list == null) return null;
        for (Usuario u : list) {
            if (u != null && u.getUsuario() != null && u.getUsuario().equalsIgnoreCase(usuario)) {
                return u;
            }
        }
        return null;
    }

    /** Valida credenciales (true/false) */
    public boolean validarCredenciales(String usuario, String password) throws IOException {
        return login(usuario, password) != null;
    }
}

