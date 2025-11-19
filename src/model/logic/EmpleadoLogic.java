package model.logic;

import model.dao.AdministradorDAO;
import model.dao.BodegueroDAO;
import model.dao.VendedorDAO;
import model.entities.Administrador;
import model.entities.Bodeguero;
import model.entities.Empleado;
import model.entities.Vendedor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoLogic {

    private final AdministradorDAO adminDao;
    private final VendedorDAO vendedorDao;
    private final BodegueroDAO bodegueroDao;
    private final ValidacionesLogic validator;
    private final PasswordLogic passwordLogic;

    public EmpleadoLogic() {
        this.adminDao = new AdministradorDAO();
        this.vendedorDao = new VendedorDAO();
        this.bodegueroDao = new BodegueroDAO();
        this.validator = new ValidacionesLogic();
        this.passwordLogic = new PasswordLogic();
    }

    public void registrarAdministrador(String usuario, String password, String nombre, String cedula) throws IOException {
        if (!validator.validarNombre(nombre)) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarCedula(cedula)) throw new IllegalArgumentException("Cédula inválida");
        if (!passwordLogic.validateStrength(password)) throw new IllegalArgumentException("Contraseña débil");

        Administrador a = new Administrador(usuario, passwordLogic.hash(password), nombre, cedula);
        adminDao.add(a);
    }

    public void registrarVendedor(String usuario, String password, String nombre, String cedula) throws IOException {
        if (!validator.validarNombre(nombre)) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarCedula(cedula)) throw new IllegalArgumentException("Cédula inválida");
        if (!passwordLogic.validateStrength(password)) throw new IllegalArgumentException("Contraseña débil");

        Vendedor v = new Vendedor(usuario, passwordLogic.hash(password), nombre, cedula);
        vendedorDao.add(v);
    }

    public void registrarBodeguero(String usuario, String password, String nombre, String cedula) throws IOException {
        if (!validator.validarNombre(nombre)) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarCedula(cedula)) throw new IllegalArgumentException("Cédula inválida");
        if (!passwordLogic.validateStrength(password)) throw new IllegalArgumentException("Contraseña débil");

        Bodeguero b = new Bodeguero(usuario, passwordLogic.hash(password), nombre, cedula);
        bodegueroDao.add(b);
    }

    public List<Empleado> listarTodos() throws IOException {
        List<Empleado> res = new ArrayList<>();
        res.addAll((List<Administrador>)(List<?>) adminDao.getAll());
        res.addAll((List<Vendedor>)(List<?>) vendedorDao.getAll());
        res.addAll((List<Bodeguero>)(List<?>) bodegueroDao.getAll());
        return res;
    }

    /** Buscar por usuario en los tres DAOs */
    public Empleado buscarPorUsuario(String usuario) throws IOException {
        for (Administrador a : adminDao.getAll()) {
            if (a != null && a.getUsuario().equalsIgnoreCase(usuario)) return a;
        }
        for (Vendedor v : vendedorDao.getAll()) {
            if (v != null && v.getUsuario().equalsIgnoreCase(usuario)) return v;
        }
        for (Bodeguero b : bodegueroDao.getAll()) {
            if (b != null && b.getUsuario().equalsIgnoreCase(usuario)) return b;
        }
        return null;
    }

    /* Update / delete: se implementan leyendo todo, modificando y reescribiendo.
       Si quieres, puedo añadir métodos explícitos para actualizar archivos. */
}
