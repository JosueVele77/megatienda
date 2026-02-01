package controller;

import model.entities.Producto;
import model.entities.Proveedor;
import model.logic.ProductoLogic;
import model.logic.ProveedorLogic;
import view.GestionProductoView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionProductoController implements ActionListener {

    private final GestionProductoView view;
    private final ProductoLogic productoLogic;
    private final ProveedorLogic proveedorLogic;

    public GestionProductoController(GestionProductoView view) {
        this.view = view;
        this.productoLogic = new ProductoLogic();
        this.proveedorLogic = new ProveedorLogic();

        this.view.btnGuardar.addActionListener(this);
        this.view.btnEliminar.addActionListener(this);
        this.view.btnLimpiar.addActionListener(this);
        this.view.btnBuscar.addActionListener(this);

        // Cargar proveedores al inicio
        cargarProveedores();

        // Cargar tabla
        listarProductos();

        // Listener tabla
        this.view.tblProductos.getSelectionModel().addListSelectionListener(e -> cargarDatosDeFila());

        this.view.setVisible(true);
    }

    private void cargarProveedores() {
        view.cmbProveedor.removeAllItems();
        try {
            List<Proveedor> lista = proveedorLogic.listarProveedores();
            for (Proveedor p : lista) {
                view.cmbProveedor.addItem(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error cargando proveedores: " + e.getMessage());
        }
    }

    // --- MÉTODO CLAVE PARA QUE LA EDICIÓN FUNCIONE ---
    public void cargarDatosProducto(Producto p) {
        view.txtCodigo.setText(p.getCodigo());
        view.txtNombre.setText(p.getNombre());
        view.txtPrecio.setText(String.valueOf(p.getPrecio()));
        view.txtStock.setText(String.valueOf(p.getStock()));
        view.txtCodigo.setEditable(false); // Bloquear código al editar

        // Seleccionar el proveedor correcto en el ComboBox
        if (p.getCodigoProveedor() != null) {
            for (int i = 0; i < view.cmbProveedor.getItemCount(); i++) {
                Proveedor prov = view.cmbProveedor.getItemAt(i);
                // Comparamos por CÓDIGO, que es lo seguro
                if (prov.getCodigo().equals(p.getCodigoProveedor())) {
                    view.cmbProveedor.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnGuardar) {
            guardarOActualizar();
        } else if (e.getSource() == view.btnEliminar) {
            eliminarProducto();
        } else if (e.getSource() == view.btnLimpiar) {
            limpiarFormulario();
        } else if (e.getSource() == view.btnBuscar) {
            listarProductos();
        }
    }

    private void guardarOActualizar() {
        String codigo = view.txtCodigo.getText();
        String nombre = view.txtNombre.getText();
        String precioStr = view.txtPrecio.getText();
        String stockStr = view.txtStock.getText();
        Proveedor proveedorSel = (Proveedor) view.cmbProveedor.getSelectedItem();

        if (codigo.isEmpty() || nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios");
            return;
        }

        if (proveedorSel == null) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un proveedor válido.");
            return;
        }

        try {
            // Arreglo decimales
            precioStr = precioStr.replace(",", ".");
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            // Aquí tomamos el código del proveedor seleccionado
            String codProv = proveedorSel.getCodigo();

            Producto p = new Producto(codigo, nombre, precio, stock, codProv);

            // Verificar existencia para saber si editar o crear
            Producto existente = productoLogic.buscarProducto(codigo);
            boolean exito;

            if (existente != null) {
                // UPDATE
                exito = productoLogic.actualizarProducto(p);
                if (exito) JOptionPane.showMessageDialog(view, "Producto actualizado correctamente");
            } else {
                // INSERT
                productoLogic.registrarProducto(p);
                exito = true;
                JOptionPane.showMessageDialog(view, "Producto registrado correctamente");
            }

            if (exito) {
                view.dispose(); // Cerramos la ventana solo si hubo éxito
            } else {
                JOptionPane.showMessageDialog(view, "Error al guardar en Base de Datos.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Precio o Stock inválidos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void listarProductos() {
        DefaultTableModel model = (DefaultTableModel) view.tblProductos.getModel();
        model.setRowCount(0);
        try {
            List<Producto> lista = productoLogic.listarProductos();
            for (Producto p : lista) {
                String nombreProv = "---";
                if(p.getCodigoProveedor() != null) {
                    Proveedor prov = proveedorLogic.buscarProveedor(p.getCodigoProveedor());
                    if(prov != null) nombreProv = prov.getRazonSocial();
                }

                model.addRow(new Object[]{
                        p.getCodigo(), p.getNombre(), p.getPrecio(), p.getStock(), nombreProv
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosDeFila() {
        int fila = view.tblProductos.getSelectedRow();
        if (fila >= 0) {
            String codigo = view.tblProductos.getValueAt(fila, 0).toString();
            try {
                Producto p = productoLogic.buscarProducto(codigo);
                if (p != null) {
                    cargarDatosProducto(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void eliminarProducto() {
        // Lógica de eliminar (Pendiente implementación completa)
        JOptionPane.showMessageDialog(view, "Función eliminar pendiente.");
    }

    private void limpiarFormulario() {
        view.txtCodigo.setText("");
        view.txtNombre.setText("");
        view.txtPrecio.setText("");
        view.txtStock.setText("");
        view.cmbProveedor.setSelectedIndex(-1);
        view.txtCodigo.setEditable(true);
        view.tblProductos.clearSelection();
    }
}