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
        // Validaciones actualizadas para los nuevos campos
        if (p.getCodigo() == null || p.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio");
        }
        if (p.getRazonSocial() == null || p.getRazonSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("La razón social es obligatoria");
        }
        if (p.getContacto() == null || p.getContacto().trim().isEmpty()) {
            throw new IllegalArgumentException("El contacto es obligatorio");
        }
        if (p.getTelefono() == null || p.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        if (p.getEmail() == null || p.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        // Verificar duplicados
        if (buscarProveedor(p.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese código");
        }

        proveedorDao.add(p);
    }

    public Proveedor buscarProveedor(String codigo) throws IOException {
        for (Proveedor p : proveedorDao.getAll()) {
            // Buscamos por código
            if (p != null && codigo.equalsIgnoreCase(p.getCodigo())) return p;
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
            // Comparamos por código
            if (p != null && p.getCodigo().equals(actualizado.getCodigo())) {
                list.set(i, actualizado);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile("data/proveedores.txt", list);
        return found;
    }

    public boolean eliminarProveedor(String codigo) throws IOException {
        List<Proveedor> list = proveedorDao.getAll();
        // Eliminamos si coincide el código
        boolean removed = list.removeIf(p -> p != null && codigo.equals(p.getCodigo()));

        // CORRECCIÓN: Usar la ruta correcta "data/proveedores.txt"
        if (removed) writeAllToFile("data/proveedores.txt", list);
        return removed;
    }

    // Se cambia a List<Proveedor> para poder acceder a los getters específicos
    private void writeAllToFile(String filePath, List<Proveedor> list) {
        final List<Proveedor> dataSnapshot = new ArrayList<>(list);

        new Thread(() -> {
            synchronized (this) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
                    for (Proveedor p : dataSnapshot) {
                        if (p != null) {
                            // IMPORTANTE: Construimos el String manualmente para guardar el formato CSV correcto.
                            // No usamos p.toString() porque ese método ahora devuelve solo el nombre para la UI.
                            String linea = p.getCodigo() + ";" +
                                    p.getRazonSocial() + ";" +
                                    p.getContacto() + ";" +
                                    p.getTelefono() + ";" +
                                    p.getEmail();
                            pw.println(linea);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error guardando proveedores en background: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}