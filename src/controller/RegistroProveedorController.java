package controller;

import model.entities.Proveedor;
import model.logic.ProveedorLogic;
import view.RegistroProveedorView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroProveedorController implements ActionListener {

    private final RegistroProveedorView view;
    private final ProveedorLogic logic;

    public RegistroProveedorController(RegistroProveedorView view) {
        this.view = view;
        this.logic = new ProveedorLogic();

        view.btnGuardar.addActionListener(this);
        view.btnCancelar.addActionListener(e -> view.dispose());
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cod = view.txtCodigo.getText().trim();
        String rs = view.txtRazonSocial.getText().trim();
        String cont = view.txtContacto.getText().trim();
        String telf = view.txtTelefono.getText().trim();
        String email = view.txtEmail.getText().trim();

        if (cod.isEmpty() || rs.isEmpty() || cont.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Campos obligatorios vacíos.");
            return;
        }

        try {
            Proveedor p = new Proveedor(cod, rs, cont, telf, email);
            logic.registrarProveedor(p);
            JOptionPane.showMessageDialog(view, "Proveedor registrado con éxito");
            view.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
}