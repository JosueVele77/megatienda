package view;

import com.formdev.flatlaf.FlatClientProperties;
import model.entities.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionProductoView extends JDialog {

    // Campos de Texto (Usamos JTextField para facilitar el manejo de decimales)
    public JTextField txtCodigo;
    public JTextField txtNombre;
    public JTextField txtStock; // Antes JSpinner
    public JTextField txtPrecio; // Antes JSpinner

    // ComboBox y Tabla
    public JComboBox<Proveedor> cmbProveedor;
    public JTable tblProductos;

    // Botones
    public JButton btnGuardar, btnCancelar, btnEliminar, btnLimpiar, btnBuscar;

    public GestionProductoView(Frame owner, String titulo) {
        super(owner, titulo, true);
        initComponents();
    }

    private void initComponents() {
        setSize(900, 600); // Aumenté el tamaño para que quepa la tabla
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // --- PANEL IZQUIERDO (FORMULARIO) ---
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlForm.setPreferredSize(new Dimension(350, 0));

        // Título del Formulario
        JLabel lblForm = new JLabel("Datos del Producto");
        lblForm.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlForm.add(lblForm);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        // Inputs
        txtCodigo = crearInput(pnlForm, "Código");
        txtNombre = crearInput(pnlForm, "Nombre del Producto");
        txtStock = crearInput(pnlForm, "Stock (Unidades)");
        txtPrecio = crearInput(pnlForm, "Precio Unitario (Ej: 2.50)");

        // Proveedor
        pnlForm.add(new JLabel("Proveedor:"));
        cmbProveedor = new JComboBox<>();
        cmbProveedor.setPreferredSize(new Dimension(300, 35));
        cmbProveedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cmbProveedor.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlForm.add(cmbProveedor);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 25)));

        // Botones del Formulario (Guardar / Limpiar)
        JPanel pnlBtnForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBtnForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setBackground(new Color(59, 130, 246)); // Azul
        btnGuardar.setForeground(Color.WHITE);

        btnLimpiar = new JButton("Limpiar");

        pnlBtnForm.add(btnGuardar);
        pnlBtnForm.add(btnLimpiar);
        pnlForm.add(pnlBtnForm);

        // --- PANEL DERECHO (TABLA) ---
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        // Barra superior de tabla (Búsqueda y Eliminar)
        JPanel pnlTopTable = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBuscar = new JButton("Refrescar/Buscar");
        btnEliminar = new JButton("ELIMINAR");
        btnEliminar.setBackground(new Color(220, 53, 69)); // Rojo
        btnEliminar.setForeground(Color.WHITE);

        pnlTopTable.add(btnBuscar);
        pnlTopTable.add(btnEliminar);

        // Configuración de la Tabla
        String[] columnas = {"Código", "Nombre", "Precio", "Stock", "Proveedor"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblProductos = new JTable(model);
        tblProductos.setRowHeight(25);

        pnlTable.add(pnlTopTable, BorderLayout.NORTH);
        pnlTable.add(new JScrollPane(tblProductos), BorderLayout.CENTER);

        // --- AGREGAR PANELES A LA VENTANA ---
        add(pnlForm, BorderLayout.WEST);
        add(pnlTable, BorderLayout.CENTER);

        // Botón cerrar abajo (opcional)
        btnCancelar = new JButton("Cerrar Ventana");
        // add(btnCancelar, BorderLayout.SOUTH); // Descomentar si quieres un botón abajo
    }

    private JTextField crearInput(JPanel p, String titulo) {
        JLabel lbl = new JLabel(titulo);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);

        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(300, 35));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Estilo FlatLaf
        t.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, titulo);
        t.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        p.add(t);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        return t;
    }
}