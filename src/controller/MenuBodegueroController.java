package controller;

import model.entities.Producto;
import model.entities.Usuario;
import model.logic.ProductoLogic;
import view.MenuBodegueroView;
import view.LoginView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class MenuBodegueroController implements ActionListener {

    private final MenuBodegueroView view;
    private final Usuario usuario;
    private final ProductoLogic productoLogic;

    public MenuBodegueroController(MenuBodegueroView view, Usuario usuario) {
        this.view = view;
        this.usuario = usuario;
        this.productoLogic = new ProductoLogic();

        this.view.btnInventario.addActionListener(this);
        this.view.btnRegistrarProd.addActionListener(this);
        this.view.btnRegistrarProv.addActionListener(this);
        this.view.btnSalir.addActionListener(this);

        // Cargar inventario al iniciar
        cargarInventario();
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        CardLayout cl = (CardLayout) view.pnlContent.getLayout();

        if (source == view.btnInventario) {
            cargarInventario();
            cl.show(view.pnlContent, "INVENTARIO");
        }
        else if (source == view.btnRegistrarProd) {
            // Aquí puedes mostrar el formulario de registro o abrir un diálogo
            JOptionPane.showMessageDialog(view, "Aquí abriría el formulario de Registrar Producto");
            // cl.show(view.pnlContent, "REG_PROD");
        }
        else if (source == view.btnRegistrarProv) {
            JOptionPane.showMessageDialog(view, "Aquí abriría el formulario de Registrar Proveedor");
        }
        else if (source == view.btnSalir) {
            view.dispose();
            new LoginView().setVisible(true);
        }
    }

    private void cargarInventario() {
        view.limpiarInventario(); // Borra las filas anteriores
        try {
            List<Producto> productos = productoLogic.listarProductos();

            for (Producto p : productos) {
                // Aquí es donde "metes manualmente" la imagen.
                // Por ahora pasamos null, pero podrías cargarla desde un archivo usando el código del producto
                Icon icono = cargarImagenProducto(p.getCodigo());

                // Agregamos la tarjeta a la vista y definimos qué hace el botón Editar
                view.agregarTarjetaProducto(p, icono, e -> {
                    JOptionPane.showMessageDialog(view, "Editar producto: " + p.getNombre() + "\n(Aquí abrirías la ventana de edición)");
                });
            }

            view.revalidate(); // Refrescar scroll

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al cargar inventario: " + ex.getMessage());
        }
    }

    // Método auxiliar para que luego cargues tus imágenes
    private Icon cargarImagenProducto(String codigo) {
        // Busca la imagen en src/main/resources/images/codigo.png
        java.net.URL imgUrl = getClass().getResource("/images/" + codigo + ".png");

        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            // Escalar la imagen al tamaño de la tarjeta (80x80)
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }

        return null; // Retorna null si no existe, y la vista pondrá el icono por defecto
    }
}
