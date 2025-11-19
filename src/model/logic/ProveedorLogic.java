package model.logic;

import model.dao.ProveedorDAO;
import model.entities.Proveedor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorLogic {

    private final ProveedorDAO proveedorDao;
    private final ValidacionesLogic validator;

    public ProveedorLogic() {
        this.proveedorDao = new ProveedorDAO();
        this.validator = new ValidacionesLogic();
    }

    public void registrarProveedor(Proveedor p) throws IOException {
        if (p.getRuc() == null || p.getRuc().trim().isEmpty()) throw new IllegalArgumentException("RUC inválido");
        if (p.getNombreEmpresa() == null || p.getNombreEmpresa().trim().isEmpty()) throw new IllegalArgumentException("Nombre empresa inválido");
        proveedorDao.add(p);
    }

    public Proveedor buscarProveedor(String ruc) throws IOException {
        for (Proveedor p : proveedorDao.getAll()) {
            if (p != null && ruc.equals(p.getRuc())) return p;
        }
        return null;
    }

    public List<Proveedor> listarProveedores() throws IOException {
        return proveedorDao.getAll();
    }

    public boolean actualizarProveedor(Proveedor actualizado) throws IOException {
        List<Proveedor> list = proveedorDao.getAll();
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            Proveedor p = list.get(i);
            if (p != null && p.getRuc().equals(actualizado.getRuc())) {
                list.set(i, actualizado);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile("data/proveedores.txt", list);
        return found;
    }

    public boolean eliminarProveedor(String ruc) throws IOException {
        List<Proveedor> list = proveedorDao.getAll();
        boolean removed = list.removeIf(p -> p != null && ruc.equals(p.getRuc()));
        if (removed) writeAllToFile("proveedores.txt", list);
        return removed;
    }

    private <T> void writeAllToFile(String filePath, List<T> list) {
        final List<T> dataSnapshot = new ArrayList<>(list);

        new Thread(() -> {
            synchronized (this) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
                    for (T item : dataSnapshot) {
                        if (item != null) pw.println(item.toString());
                    }
                } catch (IOException e) {
                    System.err.println("Error guardando proveedores en background: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
