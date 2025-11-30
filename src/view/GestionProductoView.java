package view;

import com.formdev.flatlaf.FlatClientProperties;
import model.entities.Proveedor;

import javax.swing.*;
import java.awt.*;

public class GestionProductoView extends JDialog {

    public JTextField txtCodigo;
    public JTextField txtNombre;
    public JSpinner spnUnidades;
    public JSpinner spnPrecio;
    public JComboBox<Proveedor> cmbProveedor; // ComboBox de objetos Proveedor
    public JButton btnGuardar, btnCancelar;
    public boolean modoEdicion = false; // Flag para saber si actualizamos o creamos

    public GestionProductoView(Frame owner, String titulo) {
        super(owner, titulo, true);
        initComponents();
    }

    private void initComponents() {
        setSize(450, 550);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Campos
        txtCodigo = crearInput(pnlForm, "Código de Producto");
        txtNombre = crearInput(pnlForm, "Nombre del Producto");

        // Spinners numéricos
        pnlForm.add(new JLabel("Unidades (Stock)"));
        spnUnidades = new JSpinner(new SpinnerNumberModel(1, 0, 9999, 1));
        estilizarSpinner(spnUnidades);
        pnlForm.add(spnUnidades);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        pnlForm.add(new JLabel("Precio Unitario ($)"));
        spnPrecio = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.01));
        estilizarSpinner(spnPrecio);
        pnlForm.add(spnPrecio);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        // Proveedor
        pnlForm.add(new JLabel("Proveedor"));
        cmbProveedor = new JComboBox<>();
        cmbProveedor.setPreferredSize(new Dimension(300, 40));
        pnlForm.add(cmbProveedor);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 25)));

        // Botones
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setBackground(new Color(59, 130, 246));
        btnGuardar.setForeground(Color.WHITE);

        btnCancelar = new JButton("Cancelar");

        pnlBtn.add(btnGuardar);
        pnlBtn.add(btnCancelar);

        add(new JLabel("   " + getTitle()), BorderLayout.NORTH); // Título simple
        add(pnlForm, BorderLayout.CENTER);
        add(pnlBtn, BorderLayout.SOUTH);
    }

    private JTextField crearInput(JPanel p, String titulo) {
        p.add(new JLabel(titulo));
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(300, 40));
        t.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, titulo);
        t.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        p.add(t);
        p.add(Box.createRigidArea(new Dimension(0, 15)));
        return t;
    }

    private void estilizarSpinner(JSpinner s) {
        s.setPreferredSize(new Dimension(300, 40));
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        // Alineación izquierda dentro del editor
        JComponent editor = s.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)editor).getTextField().setHorizontalAlignment(JTextField.LEFT);
        }
    }
}