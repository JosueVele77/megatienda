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

    // --- REGISTRO (Ya corregido para incluir fecha) ---
    public void registrarAdministrador(String usuario, String password, String nombre, String cedula,
                                       String celular, String direccion, String fecha) throws IOException {
        if (!validator.validarNombre(nombre)) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarCedula(cedula)) throw new IllegalArgumentException("Cédula inválida");

        Administrador a = new Administrador(usuario, passwordLogic.hash(password), nombre, cedula, celular, direccion, fecha);
        adminDao.add(a);
    }

    public void registrarVendedor(String usuario, String password, String nombre, String cedula,
                                  String celular, String direccion, String fecha) throws IOException {
        if (!validator.validarNombre(nombre)) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarCedula(cedula)) throw new IllegalArgumentException("Cédula inválida");

        Vendedor v = new Vendedor(usuario, passwordLogic.hash(password), nombre, cedula, celular, direccion, fecha);
        vendedorDao.add(v);
    }

    public void registrarBodeguero(String usuario, String password, String nombre, String cedula,
                                   String celular, String direccion, String fecha) throws IOException {
        if (!validator.validarNombre(nombre)) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarCedula(cedula)) throw new IllegalArgumentException("Cédula inválida");

        Bodeguero b = new Bodeguero(usuario, passwordLogic.hash(password), nombre, cedula, celular, direccion, fecha);
        bodegueroDao.add(b);
    }

    // --- LISTAR ---
    public List<Empleado> listarTodos() throws IOException {
        List<Empleado> res = new ArrayList<>();
        // Convertimos las listas específicas a genéricas para unirlas
        res.addAll(new ArrayList<>(adminDao.getAll()));
        res.addAll(new ArrayList<>(vendedorDao.getAll()));
        res.addAll(new ArrayList<>(bodegueroDao.getAll()));
        return res;
    }

    // --- RESETEAR CLAVE (SOLUCIÓN AL PROBLEMA) ---
    public boolean resetearClave(String email, String nombre, String cedula) throws IOException {
        // La contraseña por defecto será la CÉDULA hasheada
        String nuevaClaveHash = passwordLogic.hash(cedula);

        // 1. Buscar en Administradores
        List<Administrador> admins = adminDao.getAll();
        for (Administrador a : admins) {
            if (match(a, email, nombre, cedula)) {
                a.setPassword(nuevaClaveHash);
                a.setPrimerIngreso(true); // Obligar a cambiar clave
                return adminDao.update(a); // <--- USA EL UPDATE HÍBRIDO (BD + TXT)
            }
        }

        // 2. Buscar en Vendedores
        List<Vendedor> vends = vendedorDao.getAll();
        for (Vendedor v : vends) {
            if (match(v, email, nombre, cedula)) {
                v.setPassword(nuevaClaveHash);
                v.setPrimerIngreso(true);
                return vendedorDao.update(v); // <--- USA EL UPDATE HÍBRIDO
            }
        }

        // 3. Buscar en Bodegueros
        List<Bodeguero> bods = bodegueroDao.getAll();
        for (Bodeguero b : bods) {
            if (match(b, email, nombre, cedula)) {
                b.setPassword(nuevaClaveHash);
                b.setPrimerIngreso(true);
                return bodegueroDao.update(b); // <--- USA EL UPDATE HÍBRIDO
            }
        }

        return false; // No se encontró el usuario
    }

    private boolean match(Empleado e, String email, String nombre, String cedula) {
        if (e == null) return false;
        // Comparamos ignorando mayúsculas y espacios extra
        return e.getUsuario().trim().equalsIgnoreCase(email.trim()) &&
                e.getNombre().trim().equalsIgnoreCase(nombre.trim()) &&
                e.getCedula().trim().equals(cedula.trim());
    }

    public Empleado buscarPorCedula(String cedula) {
        try {
            for (Administrador a : adminDao.getAll()) {
                if (a.getCedula().equals(cedula)) return a;
            }
            for (Vendedor v : vendedorDao.getAll()) {
                if (v.getCedula().equals(cedula)) return v;
            }
            for (Bodeguero b : bodegueroDao.getAll()) {
                if (b.getCedula().equals(cedula)) return b;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}