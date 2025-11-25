package controller;

import model.entities.*;
import model.logic.*;
import view.CambioPasswordView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CambioPasswordController implements ActionListener {

    private final CambioPasswordView view;
    private final Usuario usuario;
    private final PasswordLogic passwordLogic;
    private final ValidacionesLogic validador;

    // DAOs
    private final model.dao.AdministradorDAO adminDao;
    private final model.dao.VendedorDAO vendDao;
    private final model.dao.BodegueroDAO bodDao;

    public CambioPasswordController(CambioPasswordView view, Usuario usuario) {
        this.view = view;
        this.usuario = usuario;
        this.passwordLogic = new PasswordLogic();
        this.validador = new ValidacionesLogic();

        this.adminDao = new model.dao.AdministradorDAO();
        this.vendDao = new model.dao.VendedorDAO();
        this.bodDao = new model.dao.BodegueroDAO();

        this.view.btnCambiar.addActionListener(this);
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nueva = new String(view.txtNuevaPass.getPassword());
        String confirmar = new String(view.txtConfirmarPass.getPassword());

        // Limpiar bordes rojos previos
        view.marcarError(view.txtNuevaPass, false);
        view.marcarError(view.txtConfirmarPass, false);

        // --- VALIDACIONES ---

        // 1. Vacíos
        if (nueva.isEmpty() || confirmar.isEmpty()) {
            if(nueva.isEmpty()) view.marcarError(view.txtNuevaPass, true);
            if(confirmar.isEmpty()) view.marcarError(view.txtConfirmarPass, true);
            view.mostrarErrorToast("Todos los campos son obligatorios.");
            return;
        }

        // 2. Coincidencia
        if (!nueva.equals(confirmar)) {
            view.marcarError(view.txtConfirmarPass, true);
            view.mostrarErrorToast("Las contraseñas no coinciden.");
            return;
        }

        // 3. Fortaleza (Regex) - AHORA YA NO CRASHEARÁ
        if (!validador.validarPasswordSegura(nueva)) {
            view.marcarError(view.txtNuevaPass, true);
            view.mostrarErrorToast("Débil: Use 8+ caracteres, mayúscula y símbolo.");
            return;
        }

        // 4. No repetir la anterior
        if (passwordLogic.compare(nueva, usuario.getPassword())) {
            view.marcarError(view.txtNuevaPass, true);
            view.mostrarErrorToast("No puede usar la misma contraseña anterior.");
            return;
        }

        // --- GUARDAR ---
        try {
            String nuevoHash = passwordLogic.hash(nueva);
            usuario.setPassword(nuevoHash);
            usuario.setPrimerIngreso(false);

            boolean exito = false;
            String rol = usuario.getRol().toUpperCase();

            if ("ADMINISTRADOR".equals(rol)) {
                actualizarEnArchivo(adminDao, (Administrador) usuario);
                exito = true;
            } else if ("VENDEDOR".equals(rol)) {
                actualizarEnArchivo(vendDao, (Vendedor) usuario);
                exito = true;
            } else if ("BODEGUERO".equals(rol)) {
                actualizarEnArchivo(bodDao, (Bodeguero) usuario);
                exito = true;
            }

            if (exito) {
                JOptionPane.showMessageDialog(view, "¡Contraseña actualizada con éxito!");
                view.dispose();
            }

        } catch (Exception ex) {
            view.mostrarErrorToast("Error crítico: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // (El método actualizarEnArchivo se mantiene igual que en tu versión anterior)
    private <T extends Usuario> void actualizarEnArchivo(libgeneric.GenericDAO<T> dao, T usuarioActualizado) throws IOException {
        java.util.List<T> lista = dao.getAll();
        for (int i = 0; i < lista.size(); i++) {
            T u = lista.get(i);
            if (u.getUsuario().equalsIgnoreCase(usuarioActualizado.getUsuario())) {
                lista.set(i, usuarioActualizado);
                break;
            }
        }
        String archivoDestino = "";
        if (usuarioActualizado instanceof Administrador) archivoDestino = "data/administradores.txt";
        else if (usuarioActualizado instanceof Vendedor) archivoDestino = "data/vendedores.txt";
        else if (usuarioActualizado instanceof Bodeguero) archivoDestino = "data/bodegueros.txt";

        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(archivoDestino, false))) {
            for (T item : lista) {
                pw.println(item.toString());
            }
        }
    }
}