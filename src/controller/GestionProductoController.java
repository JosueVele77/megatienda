package controller;

import model.entities.Producto;
import model.entities.Proveedor;
import model.logic.ProductoLogic;
import model.logic.ProveedorLogic;
import view.GestionProductoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class GestionProductoController implements ActionListener {

    private final GestionProductoView view;
    private final ProductoLogic productoLogic;
    private final ProveedorLogic proveedorLogic;

    public GestionProductoController(GestionProductoView view) {
        this.view = view;
        this.productoLogic = new ProductoLogic();
        this.proveedorLogic = new ProveedorLogic();

        // Cargar proveedores al combo
        cargarProveedores();

        this.view.btnGuardar.addActionListener(this);
        this.view.btnCancelar.addActionListener(e -> view.dispose());
    }

    private void cargarProveedores() {
        try {
            List<Proveedor> lista = proveedorLogic.listarProveedores();
            view.cmbProveedor.removeAllItems();
            for (Proveedor p : lista) {
                view.cmbProveedor.addItem(p);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Error cargando proveedores");
        }
    }

    // Método para pre-llenar datos si es Actualización
    public void cargarDatosProducto(Producto p) {
        view.txtCodigo.setText(p.getCodigo());
        view.txtCodigo.setEditable(false); // No se puede cambiar el ID al editar
        view.txtNombre.setText(p.getNombre());
        view.spnUnidades.setValue(p.getStock());
        view.spnPrecio.setValue(p.getPrecio());
        view.modoEdicion = true;

        // Seleccionar proveedor en el combo
        for(int i=0; i<view.cmbProveedor.getItemCount(); i++){
            Proveedor prov = view.cmbProveedor.getItemAt(i);
            if(prov.getCodigo().equals(p.getCodigoProveedor())){
                view.cmbProveedor.setSelectedIndex(i);
                break;
            }
        }
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Recoger datos
        String codigo = view.txtCodigo.getText().trim();
        String nombre = view.txtNombre.getText().trim();
        int stock = (int) view.spnUnidades.getValue();
        double precio = (double) view.spnPrecio.getValue();
        Proveedor proveedorSel = (Proveedor) view.cmbProveedor.getSelectedItem();

        if (codigo.isEmpty() || nombre.isEmpty() || proveedorSel == null) {
            JOptionPane.showMessageDialog(view, "Complete todos los campos", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Producto p = new Producto(codigo, nombre, precio, stock, proveedorSel.getCodigo());

            if (view.modoEdicion) {
                // Lógica de actualización (necesitas agregar actualizarProducto en ProductoLogic o reusar registrar sobreescribiendo)
                // Asumiremos que ProductoLogic tiene un método actualizar o que registrar sobreescribe si existe
                productoLogic.actualizarProducto(p);
                JOptionPane.showMessageDialog(view, "Producto actualizado correctamente");
            } else {
                // Registro nuevo
                if (productoLogic.buscarProducto(codigo) != null) {
                    JOptionPane.showMessageDialog(view, "El código ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                productoLogic.registrarProducto(p);
                JOptionPane.showMessageDialog(view, "Producto registrado correctamente");
            }
            view.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}