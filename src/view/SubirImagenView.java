package view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class SubirImagenView extends JDialog {

    public JTextField txtCodigo;
    public JTextField txtRutaImagen;
    public JButton btnExaminar;
    public JButton btnGuardar;
    public JButton btnCancelar;
    public JLabel lblPreview; // Para mostrar una vista previa pequeña

    public SubirImagenView(Frame owner) {
        super(owner, "Subir Imagen de Producto", true);
        initComponents();
    }

    private void initComponents() {
        setSize(500, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Título
        JLabel lblTitulo = new JLabel("Asignar Imagen");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlForm.add(lblTitulo);
        pnlForm.add(Box.createVerticalStrut(20));

        // Campo Código
        pnlForm.add(crearLabel("Código del Producto:"));
        txtCodigo = new JTextField();
        estilizarInput(txtCodigo);
        pnlForm.add(txtCodigo);
        pnlForm.add(Box.createVerticalStrut(15));

        // Campo Archivo
        pnlForm.add(crearLabel("Archivo de Imagen:"));
        JPanel pnlFile = new JPanel(new BorderLayout(10, 0));
        pnlFile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        txtRutaImagen = new JTextField();
        txtRutaImagen.setEditable(false);
        estilizarInput(txtRutaImagen);

        btnExaminar = new JButton("Buscar...");

        pnlFile.add(txtRutaImagen, BorderLayout.CENTER);
        pnlFile.add(btnExaminar, BorderLayout.EAST);
        pnlForm.add(pnlFile);
        pnlForm.add(Box.createVerticalStrut(20));

        // Preview
        lblPreview = new JLabel("Vista Previa", SwingConstants.CENTER);
        lblPreview.setPreferredSize(new Dimension(150, 150));
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlForm.add(lblPreview);

        // Botones
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnGuardar = new JButton("GUARDAR IMAGEN");
        btnGuardar.setBackground(new Color(59, 130, 246));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(150, 45));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 45));

        pnlBtn.add(btnGuardar);
        pnlBtn.add(btnCancelar);

        add(pnlForm, BorderLayout.CENTER);
        add(pnlBtn, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void estilizarInput(JTextField txt) {
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.setPreferredSize(new Dimension(300, 40));
        txt.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
    }
}