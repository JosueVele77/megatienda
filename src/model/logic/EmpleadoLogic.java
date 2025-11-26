package model.logic;

import model.dao.AdministradorDAO;
import model.dao.BodegueroDAO;
import model.dao.VendedorDAO;
import model.entities.Administrador;
import model.entities.Bodeguero;
import model.entities.Empleado;
import model.entities.Vendedor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    // --- MÉTODOS ACTUALIZADOS PARA RECIBIR 7 PARÁMETROS ---

    public void registrarAdministrador(String usuario, String password, String nombre, String cedula,
                                       String celular, String direccion, String fecha) throws IOException {
        if (!validator.validarNombre(nombre)) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarCedula(cedula)) throw new IllegalArgumentException("Cédula inválida");

        // CORRECCIÓN: Ahora pasamos los 7 datos al constructor
        Administrador a = new Administrador(usuario, passwordLogic.hash(password), nombre, cedula, celular, direccion, fecha);
        adminDao.add(a);
    }

    public void registrarVendedor(String usuario, String password, String nombre, String cedula,
                                  String celular, String direccion, String fecha) throws IOException {
        if (!validator.validarNombre(nombre)) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarCedula(cedula)) throw new IllegalArgumentException("Cédula inválida");

        // CORRECCIÓN
        Vendedor v = new Vendedor(usuario, passwordLogic.hash(password), nombre, cedula, celular, direccion, fecha);
        vendedorDao.add(v);
    }

    public void registrarBodeguero(String usuario, String password, String nombre, String cedula,
                                   String celular, String direccion, String fecha) throws IOException {
        if (!validator.validarNombre(nombre)) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarCedula(cedula)) throw new IllegalArgumentException("Cédula inválida");

        // CORRECCIÓN
        Bodeguero b = new Bodeguero(usuario, passwordLogic.hash(password), nombre, cedula, celular, direccion, fecha);
        bodegueroDao.add(b);
    }

    public List<Empleado> listarTodos() throws IOException {
        List<Empleado> res = new ArrayList<>();
        res.addAll((List<Administrador>)(List<?>) adminDao.getAll());
        res.addAll((List<Vendedor>)(List<?>) vendedorDao.getAll());
        res.addAll((List<Bodeguero>)(List<?>) bodegueroDao.getAll());
        return res;
    }

    // ... (El resto del código como resetearClave sigue igual) ...
    // --- NUEVO MÉTODO PARA RESETEAR ---
    public boolean resetearClave(String email, String nombre, String cedula) throws IOException {
        // 1. Buscar en Administradores
        List<Administrador> admins = adminDao.getAll();
        for (int i = 0; i < admins.size(); i++) {
            Administrador a = admins.get(i);
            if (match(a, email, nombre, cedula)) {
                a.setPassword(passwordLogic.hash(cedula));
                a.setPrimerIngreso(true);
                admins.set(i, a);
                writeList("data/administradores.txt", admins);
                return true;
            }
        }

        // 2. Buscar en Vendedores
        List<Vendedor> vends = vendedorDao.getAll();
        for (int i = 0; i < vends.size(); i++) {
            Vendedor v = vends.get(i);
            if (match(v, email, nombre, cedula)) {
                v.setPassword(passwordLogic.hash(cedula));
                v.setPrimerIngreso(true);
                vends.set(i, v);
                writeList("data/vendedores.txt", vends);
                return true;
            }
        }

        // 3. Buscar en Bodegueros
        List<Bodeguero> bods = bodegueroDao.getAll();
        for (int i = 0; i < bods.size(); i++) {
            Bodeguero b = bods.get(i);
            if (match(b, email, nombre, cedula)) {
                b.setPassword(passwordLogic.hash(cedula));
                b.setPrimerIngreso(true);
                bods.set(i, b);
                writeList("data/bodegueros.txt", bods);
                return true;
            }
        }
        return false;
    }

    private boolean match(Empleado e, String email, String nombre, String cedula) {
        if (e == null) return false;
        return e.getUsuario().equalsIgnoreCase(email) &&
                e.getNombre().equalsIgnoreCase(nombre) &&
                e.getCedula().equals(cedula);
    }

    private <T> void writeList(String path, List<T> list) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, false))) {
            for (T item : list) {
                if (item != null) pw.println(item.toString());
            }
        }
    }
}