package model.logic;

import model.dao.ProductoDAO;
import model.entities.Producto;

import java.io.IOException;
import java.util.List;

public class ProductoLogic {

    private final ProductoDAO productoDao;

    public ProductoLogic() {
        this.productoDao = new ProductoDAO();
    }

    public void registrarProducto(Producto p) throws IOException {
        if (p == null) throw new IllegalArgumentException("Producto inválido");
        // Validaciones extra si quieres...
        productoDao.add(p);
    }

    public List<Producto> listarProductos() throws IOException {
        return productoDao.getAll();
    }

    public Producto buscarProducto(String codigo) throws IOException {
        List<Producto> lista = productoDao.getAll();
        for (Producto p : lista) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }

    // --- AQUÍ ESTÁ LA CLAVE PARA QUE FUNCIONE LA EDICIÓN ---
    public boolean actualizarProducto(Producto p) throws IOException {
        // Llamamos directamente al método update del DAO (que ya arreglamos para SQL y TXT)
        return productoDao.update(p);
    }

    // Método para restar stock en ventas
    public boolean restarStock(String codigo, int cantidad) throws IOException {
        Producto p = buscarProducto(codigo);
        if (p != null) {
            if(p.getStock() < cantidad) return false;
            p.setStock(p.getStock() - cantidad);
            return productoDao.update(p);
        }
        return false;
    }
}