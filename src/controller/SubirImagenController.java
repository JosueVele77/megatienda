package controller;

import model.logic.ProductoLogic;
import view.SubirImagenView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SubirImagenController implements ActionListener {

    private final SubirImagenView view;
    private final ProductoLogic productoLogic;
    private File archivoSeleccionado;

    public SubirImagenController(SubirImagenView view) {
        this.view = view;
        this.productoLogic = new ProductoLogic();

        this.view.btnExaminar.addActionListener(this);
        this.view.btnGuardar.addActionListener(this);
        this.view.btnCancelar.addActionListener(e -> view.dispose());
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnExaminar) {
            seleccionarArchivo();
        } else if (e.getSource() == view.btnGuardar) {
            guardarImagen();
        }
    }

    private void seleccionarArchivo() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg"));

        if (fc.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fc.getSelectedFile();
            view.txtRutaImagen.setText(archivoSeleccionado.getAbsolutePath());

            // Mostrar vista previa
            ImageIcon icon = new ImageIcon(archivoSeleccionado.getPath());
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            view.lblPreview.setText("");
            view.lblPreview.setIcon(new ImageIcon(img));
        }
    }

    private void guardarImagen() {
        String codigo = view.txtCodigo.getText().trim();

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debe ingresar el código del producto.");
            return;
        }
        if (archivoSeleccionado == null) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una imagen.");
            return;
        }

        try {
            // 1. Verificar si el producto existe (Opcional, pero recomendado)
            if (productoLogic.buscarProducto(codigo) == null) {
                int confirm = JOptionPane.showConfirmDialog(view,
                        "El código '" + codigo + "' no existe en productos. ¿Desea guardar la imagen de todas formas?",
                        "Advertencia", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
            }

            // 2. Preparar carpeta destino
            File carpetaImagenes = new File("data/images");
            if (!carpetaImagenes.exists()) {
                carpetaImagenes.mkdirs(); // Crea la carpeta si no existe
            }

            // 3. Copiar archivo y renombrar a [codigo].png
            // Nota: Guardamos siempre como .png para estandarizar la lectura
            File destino = new File(carpetaImagenes, codigo + ".png");

            Files.copy(archivoSeleccionado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            JOptionPane.showMessageDialog(view, "Imagen guardada correctamente.");
            view.dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar imagen: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}