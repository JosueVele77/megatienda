package controller;

import model.entities.*;
import model.logic.*;
import view.CambioPasswordView;
// Importamos los DAOs concretos para acceder a sus métodos update
import model.dao.AdministradorDAO;
import model.dao.BodegueroDAO;
import model.dao.VendedorDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CambioPasswordController implements ActionListener {

    private final CambioPasswordView view;
    private final Usuario usuario;
    private final PasswordLogic passwordLogic;
    private final ValidacionesLogic validador;

    // DAOs concretos
    private final AdministradorDAO adminDao;
    private final VendedorDAO vendDao;
    private final BodegueroDAO bodDao;

    public CambioPasswordController(CambioPasswordView view, Usuario usuario) {
        this.view = view;
        this.usuario = usuario;
        this.passwordLogic = new PasswordLogic();
        this.validador = new ValidacionesLogic();

        // Inicializamos los DAOs
        this.adminDao = new AdministradorDAO();
        this.vendDao = new VendedorDAO();
        this.bodDao = new BodegueroDAO();

        this.view.btnCambiar.addActionListener(this);
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nueva = new String(view.txtNuevaPass.getPassword());
        String confirmar = new String(view.txtConfirmarPass.getPassword());

        // Limpiar errores visuales
        view.marcarError(view.txtNuevaPass, false);
        view.marcarError(view.txtConfirmarPass, false);

        // --- VALIDACIONES ---
        if (nueva.isEmpty() || confirmar.isEmpty()) {
            if (nueva.isEmpty()) view.marcarError(view.txtNuevaPass, true);
            if (confirmar.isEmpty()) view.marcarError(view.txtConfirmarPass, true);
            view.mostrarErrorToast("Todos los campos son obligatorios.");
            return;
        }

        if (!nueva.equals(confirmar)) {
            view.marcarError(view.txtConfirmarPass, true);
            view.mostrarErrorToast("Las contraseñas no coinciden.");
            return;
        }

        if (!validador.validarPasswordSegura(nueva)) {
            view.marcarError(view.txtNuevaPass, true);
            view.mostrarErrorToast("Débil: Use 8+ caracteres, mayúscula y símbolo.");
            return;
        }

        if (passwordLogic.compare(nueva, usuario.getPassword())) {
            view.marcarError(view.txtNuevaPass, true);
            view.mostrarErrorToast("No puede usar la misma contraseña anterior.");
            return;
        }

        // --- GUARDADO ---
        try {
            // 1. Actualizamos el objeto en memoria
            String nuevoHash = passwordLogic.hash(nueva);
            usuario.setPassword(nuevoHash);
            usuario.setPrimerIngreso(false); // <--- ESTO CAMBIA EL ESTADO

            boolean exito = false;
            String rol = usuario.getRol().toUpperCase();

            // 2. Delegamos la persistencia al DAO correspondiente
            // El DAO se encarga de decidir si guarda en BD, TXT o ambos.
            if ("ADMINISTRADOR".equals(rol)) {
                exito = adminDao.update((Administrador) usuario);
            }
            else if ("VENDEDOR".equals(rol)) {
                // Asegúrate de implementar el método update en VendedorDAO
                exito = vendDao.update((Vendedor) usuario);
            }
            else if ("BODEGUERO".equals(rol)) {
                // Asegúrate de implementar el método update en BodegueroDAO
                exito = bodDao.update((Bodeguero) usuario);
            }

            if (exito) {
                JOptionPane.showMessageDialog(view, "¡Contraseña actualizada! Por favor inicie sesión nuevamente.");
                view.dispose();
                // Opcional: Cerrar la sesión actual completamente si fuera necesario
            } else {
                view.mostrarErrorToast("Error al guardar: No se pudo actualizar la base de datos ni el archivo.");
            }

        } catch (Exception ex) {
            view.mostrarErrorToast("Error crítico: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}