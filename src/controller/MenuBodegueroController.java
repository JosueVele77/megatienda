package controller;

import model.entities.Producto;
import model.entities.Proveedor;
import model.entities.Usuario;
import model.logic.ProductoLogic;
import model.logic.ProveedorLogic;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MenuBodegueroController implements ActionListener {

    private final MenuBodegueroView view;
    private final Usuario usuario;
    private final ProductoLogic productoLogic;
    private final ProveedorLogic proveedorLogic;

    public MenuBodegueroController(MenuBodegueroView view, Usuario usuario) {
        this.view = view;
        this.usuario = usuario;
        this.productoLogic = new ProductoLogic();
        this.proveedorLogic = new ProveedorLogic();

        this.view.btnInventario.addActionListener(this);
        this.view.btnRegistrarProd.addActionListener(this);
        this.view.btnActualizarProd.addActionListener(this); // Botón Editar menú lateral
        this.view.btnRegistrarProv.addActionListener(this);
        this.view.btnSalir.addActionListener(this);
        this.view.btnSubirImagen.addActionListener(this);
        this.view.btnSalir.addActionListener(this);

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
            // Abrir formulario de registro
            GestionProductoView regView = new GestionProductoView(view, "Registrar Nuevo Producto");
            new GestionProductoController(regView).iniciar();
            cargarInventario(); // Refrescar al cerrar
        }
        else if (source == view.btnActualizarProd) {
            // Flujo: Pedir Código -> Buscar -> Abrir Formulario lleno
            actualizarProductoFlow();
        }
        else if (source == view.btnRegistrarProv) {
            RegistroProveedorView provView = new RegistroProveedorView(view);
            new RegistroProveedorController(provView).iniciar();
        }
        else if (source == view.btnSalir) {
            view.dispose();
            new LoginView().setVisible(true);
        }else if (source == view.btnSubirImagen) {
            // Abrir la ventana de carga de imagen
            SubirImagenView imgView = new SubirImagenView(view);
            new SubirImagenController(imgView).iniciar();
            // Al volver, refrescamos el inventario por si se actualizó alguna imagen visible
            cargarInventario();
        }
        else if (source == view.btnSalir) {
            view.dispose();
            new LoginView().setVisible(true);
        }
    }

    private void actualizarProductoFlow() {
        String codigo = JOptionPane.showInputDialog(view, "Ingrese el Código del Producto a actualizar:");
        if (codigo != null && !codigo.trim().isEmpty()) {
            try {
                Producto p = productoLogic.buscarProducto(codigo.trim());
                if (p != null) {
                    GestionProductoView editView = new GestionProductoView(view, "Actualizar Producto");
                    GestionProductoController ctrl = new GestionProductoController(editView);
                    ctrl.cargarDatosProducto(p); // Pre-llenar datos
                    ctrl.iniciar();
                    cargarInventario(); // Refrescar
                } else {
                    JOptionPane.showMessageDialog(view, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void cargarInventario() {
        view.limpiarInventario();
        try {
            List<Producto> productos = productoLogic.listarProductos();

            for (Producto p : productos) {
                // Ahora carga desde la carpeta local
                Icon icono = cargarImagenProducto(p.getCodigo());

                String nombreProv = "Desconocido";
                Proveedor prov = proveedorLogic.buscarProveedor(p.getCodigoProveedor());
                if (prov != null) nombreProv = prov.getRazonSocial();

                view.agregarTarjetaProducto(p, nombreProv, icono, e -> {
                    GestionProductoView editView = new GestionProductoView(view, "Editar Producto");
                    GestionProductoController ctrl = new GestionProductoController(editView);
                    ctrl.cargarDatosProducto(p);
                    ctrl.iniciar();
                    cargarInventario();
                });
            }
            view.revalidate();
            view.repaint();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al cargar inventario: " + ex.getMessage());
        }
    }

    private Icon cargarImagenProducto(String codigo) {
        // 1. Buscar en carpeta local "data/images"
        File f = new File("data/images/" + codigo + ".png");

        if (f.exists()) {
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }

        // 2. Si no existe en disco, intentar buscar en resources (por si tienes imágenes por defecto)
        // Esto es opcional, si quieres mantener compatibilidad con las imágenes viejas
        try {
            java.net.URL imgUrl = getClass().getResource("/images/" + codigo + ".png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {}

        return null; // Retorna null y la vista pondrá el icono gris por defecto
    }
}