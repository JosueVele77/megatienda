package model.logic;

import model.dao.ProductoDAO;
import model.entities.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoLogic {

    private final ProductoDAO productoDao;
    private final ValidacionesLogic validator;

    public ProductoLogic() {
        this.productoDao = new ProductoDAO();
        this.validator = new ValidacionesLogic();
    }

    public void registrarProducto(Producto p) throws IOException {
        if (p.getCodigo() == null || p.getCodigo().trim().isEmpty()) throw new IllegalArgumentException("Código inválido");
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) throw new IllegalArgumentException("Nombre inválido");
        if (p.getPrecio() < 0) throw new IllegalArgumentException("Precio negativo");
        if (p.getStock() < 0) throw new IllegalArgumentException("Stock negativo");
        productoDao.add(p);
    }

    public Producto buscarProducto(String codigo) throws IOException {
        for (Producto p : productoDao.getAll()) {
            if (p != null && codigo.equals(p.getCodigo())) return p;
        }
        return null;
    }

    public boolean restarStock(String codigo, int cantidad) throws IOException {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser positiva");
        List<Producto> list = productoDao.getAll();
        boolean updated = false;
        for (int i = 0; i < list.size(); i++) {
            Producto p = list.get(i);
            if (p != null && p.getCodigo().equals(codigo)) {
                if (p.getStock() < cantidad) throw new IllegalStateException("Stock insuficiente");
                p.setStock(p.getStock() - cantidad);
                list.set(i, p);
                updated = true;
                break;
            }
        }
        if (updated) writeAllToFile("data/productos.txt", list);
        return updated;
    }

    public boolean agregarStock(String codigo, int cantidad) throws IOException {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser positiva");
        List<Producto> list = productoDao.getAll();
        boolean updated = false;
        for (int i = 0; i < list.size(); i++) {
            Producto p = list.get(i);
            if (p != null && p.getCodigo().equals(codigo)) {
                p.setStock(p.getStock() + cantidad);
                list.set(i, p);
                updated = true;
                break;
            }
        }
        if (updated) writeAllToFile("productos.txt", list);
        return updated;
    }

    public List<Producto> listarProductos() throws IOException {
        return productoDao.getAll();
    }

    private <T> void writeAllToFile(String filePath, List<T> list) {
        // 1. Snapshot para evitar ConcurrentModificationException
        final List<T> dataSnapshot = new ArrayList<>(list);

        // 2. Hilo independiente
        new Thread(() -> {
            synchronized (this) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
                    for (T item : dataSnapshot) {
                        if (item != null) pw.println(item.toString());
                    }
                } catch (IOException e) {
                    System.err.println("Error guardando productos en background: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
