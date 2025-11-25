package model.logic;

import model.dao.AdministradorDAO;
import model.dao.BodegueroDAO;
import model.dao.VendedorDAO;
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

    public Usuario login(String usuario, String password) throws IOException {
        Usuario u;

        // 1. Buscar en Administradores
        u = findUsuarioInList(adminDao.getAll(), usuario);
        if (u != null && passwordLogic.compare(password, u.getPassword())) return u;

        // 2. Buscar en Vendedores
        u = findUsuarioInList(vendedorDao.getAll(), usuario);
        if (u != null && passwordLogic.compare(password, u.getPassword())) return u;

        // 3. Buscar en Bodegueros
        u = findUsuarioInList(bodegueroDao.getAll(), usuario);
        if (u != null && passwordLogic.compare(password, u.getPassword())) return u;

        // Si no se encuentra en ninguno o la clave falla
        return null;
    }

    // Método auxiliar (sin cambios)
    private Usuario findUsuarioInList(List<? extends Usuario> list, String usuario) {
        if (list == null) return null;
        for (Usuario u : list) {
            if (u != null && u.getUsuario() != null && u.getUsuario().equalsIgnoreCase(usuario)) {
                return u;
            }
        }
        return null;
    }
}
