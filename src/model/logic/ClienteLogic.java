package model.logic;

import model.dao.ClienteDAO;
import model.entities.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteLogic {

    private final ClienteDAO clienteDao;
    private final ValidacionesLogic validator;

    public ClienteLogic() {
        this.clienteDao = new ClienteDAO();
        this.validator = new ValidacionesLogic();
    }

    public void registrarCliente(Cliente c) throws IOException {
        if (!validator.validarCedula(c.getCedula())) throw new IllegalArgumentException("Cédula inválida");
        if (!validator.validarNombre(c.getNombre())) throw new IllegalArgumentException("Nombre inválido");
        if (!validator.validarEmail(c.getCorreo())) throw new IllegalArgumentException("Correo inválido");
        clienteDao.add(c);
    }

    public List<Cliente> listarClientes() throws IOException {
        return clienteDao.getAll();
    }

    public Cliente buscarCliente(String cedula) throws IOException {
        for (Cliente c : clienteDao.getAll()) {
            if (c != null && cedula.equals(c.getCedula())) return c;
        }
        return null;
    }

    public boolean actualizarCliente(Cliente actualizado) throws IOException {
        // Reescribir archivo con cambios
        List<Cliente> list = clienteDao.getAll();
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            Cliente c = list.get(i);
            if (c != null && c.getCedula().equals(actualizado.getCedula())) {
                list.set(i, actualizado);
                found = true;
                break;
            }
        }
        if (found) {
            writeAllToFile("data/clientes.txt", list);
        }
        return found;
    }

    public boolean eliminarCliente(String cedula) throws IOException {
        List<Cliente> list = clienteDao.getAll();
        boolean removed = list.removeIf(c -> c != null && cedula.equals(c.getCedula()));
        if (removed) writeAllToFile("clientes.txt", list);
        return removed;
    }

    private <T> void writeAllToFile(String filePath, List<T> list) {
        // 1. Creamos una COPIA de la lista para asegurar que los datos no cambien
        // mientras el hilo intenta escribirlos (evita ConcurrentModificationException).
        final List<T> dataSnapshot = new ArrayList<>(list);

        // 2. Creamos un nuevo Hilo (Thread)
        Thread backgroundWriter = new Thread(() -> {
            // Sincronizamos solo para evitar que dos hilos escriban el MISMO archivo a la vez
            synchronized (this) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
                    for (T item : dataSnapshot) {
                        if (item != null) pw.println(item.toString());
                    }
                    System.out.println("Archivo actualizado correctamente: " + filePath);
                } catch (IOException e) {
                    System.err.println("Fallo al guardar en segundo plano: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        // 3. Iniciamos el hilo
        backgroundWriter.start();
    }
}
