package controller;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import model.entities.Producto;
import model.entities.Proveedor;
import model.entities.Usuario;
import model.entities.Empleado;
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

        // Mostrar nombre del usuario
        String nombreMostrar = usuario.getUsuario();
        if (usuario instanceof Empleado) {
            nombreMostrar = ((Empleado) usuario).getNombre();
        }
        view.lblInfoBodeguero.setText("Bodeguero: " + nombreMostrar);

        // Listeners
        this.view.btnInventario.addActionListener(this);
        this.view.btnRegistrarProd.addActionListener(this);
        this.view.btnActualizarProd.addActionListener(this);
        this.view.btnRegistrarProv.addActionListener(this);
        this.view.btnSubirImagen.addActionListener(this);
        this.view.btnSalir.addActionListener(this);

        // Listener del Toggle Button (Tema)
        this.view.btnTema.addActionListener(e -> cambiarTema());

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
            abrirGestionProductos(null); // Null indica modo registro (limpio)
        }
        else if (source == view.btnActualizarProd) {
            actualizarProductoFlow();
        }
        else if (source == view.btnRegistrarProv) {
            RegistroProveedorView provView = new RegistroProveedorView(view);
            new RegistroProveedorController(provView).iniciar();
        }
        else if (source == view.btnSubirImagen) {
            SubirImagenView imgView = new SubirImagenView(view);
            new SubirImagenController(imgView).iniciar();
            cargarInventario();
        }
        else if (source == view.btnSalir) {
            view.dispose();
            new LoginView().setVisible(true);
        }
    }

    /**
     * Abre la ventana completa de gestión de productos.
     * Si 'productoEditar' no es null, pre-carga los datos en el formulario.
     */
    private void abrirGestionProductos(Producto productoEditar) {
        // Usamos la nueva vista que incluye tabla y formulario
        GestionProductoView gpView = new GestionProductoView(view, "Gestión de Productos");

        // Si es edición, pre-llenamos los campos ANTES de iniciar el controlador
        if (productoEditar != null) {
            gpView.txtCodigo.setText(productoEditar.getCodigo());
            gpView.txtNombre.setText(productoEditar.getNombre());
            gpView.txtPrecio.setText(String.valueOf(productoEditar.getPrecio()));
            gpView.txtStock.setText(String.valueOf(productoEditar.getStock()));

            // Bloqueamos el código porque es clave primaria en BD
            gpView.txtCodigo.setEditable(false);

            // Intentamos seleccionar el proveedor en el combo (esto se refinará en el controller)
            // Nota: El controller GestionProductoController maneja la carga de proveedores al iniciar.
        }

        // Iniciamos el controlador de productos
        new GestionProductoController(gpView);

        // Al cerrar la ventana, recargamos el inventario visual (las tarjetas)
        cargarInventario();
    }

    private void cambiarTema() {
        try {
            if (FlatLaf.isLafDark()) {
                UIManager.setLookAndFeel(new FlatMacLightLaf());
            } else {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
            }
            FlatLaf.updateUI();
            view.actualizarColores();
            cargarInventario();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void actualizarProductoFlow() {
        String codigo = JOptionPane.showInputDialog(view, "Ingrese el Código del Producto a actualizar:");
        if (codigo != null && !codigo.trim().isEmpty()) {
            try {
                Producto p = productoLogic.buscarProducto(codigo.trim());
                if (p != null) {
                    abrirGestionProductos(p);
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
                Icon icono = cargarImagenProducto(p.getCodigo());
                String nombreProv = "Desconocido";

                // Usamos la lógica de proveedores actualizada (busca por código)
                if (p.getCodigoProveedor() != null) {
                    Proveedor prov = proveedorLogic.buscarProveedor(p.getCodigoProveedor());
                    if (prov != null) {
                        nombreProv = prov.getRazonSocial(); // Usamos razonSocial
                    }
                }

                // Al hacer click en la tarjeta, abrimos la edición
                view.agregarTarjetaProducto(p, nombreProv, icono, e -> {
                    abrirGestionProductos(p);
                });
            }
            view.revalidate();
            view.repaint();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al cargar inventario: " + ex.getMessage());
        }
    }

    private Icon cargarImagenProducto(String codigo) {
        File f = new File("data/images/" + codigo + ".png");
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        try {
            // Intento cargar desde recursos si existe
            java.net.URL imgUrl = getClass().getResource("/images/" + codigo + ".png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            // Ignorar errores de imagen
        }
        return null; // La vista pondrá un icono por defecto
    }
}