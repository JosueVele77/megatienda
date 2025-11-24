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

    // Necesitamos las lógicas para guardar
    private final EmpleadoLogic empleadoLogic;
    // Nota: Lo ideal sería tener un método "actualizarPassword" genérico,
    // pero usaremos las lógicas existentes para re-guardar el usuario modificado.

    public CambioPasswordController(CambioPasswordView view, Usuario usuario) {
        this.view = view;
        this.usuario = usuario;
        this.passwordLogic = new PasswordLogic();
        this.validador = new ValidacionesLogic();
        this.empleadoLogic = new EmpleadoLogic(); // Asumimos que esto maneja el guardado general

        this.view.btnCambiar.addActionListener(this);
    }

    public void iniciar() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nueva = new String(view.txtNuevaPass.getPassword());
        String confirmar = new String(view.txtConfirmarPass.getPassword());

        // 1. Validar coincidencia
        if (!nueva.equals(confirmar)) {
            JOptionPane.showMessageDialog(view, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validar fortaleza (Regex)
        if (!validador.validarPasswordSegura(nueva)) {
            JOptionPane.showMessageDialog(view,
                    "La contraseña es débil.\nDebe tener al menos 8 caracteres, una mayúscula y un símbolo (@, -, /)",
                    "Seguridad", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Validar que no sea igual a la anterior (opcional pero recomendado)
        // Como la anterior está hasheada, comparamos el hash de la nueva con el hash almacenado
        if (passwordLogic.compare(nueva, usuario.getPassword())) {
            JOptionPane.showMessageDialog(view, "La nueva contraseña no puede ser igual a la actual.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. ACTUALIZAR
        try {
            // A. Hashear nueva clave
            String nuevoHash = passwordLogic.hash(nueva);

            // B. Actualizar objeto en memoria
            usuario.setPassword(nuevoHash);
            usuario.setPrimerIngreso(false); // ¡Importante! Ya no es primer ingreso

            // C. Guardar en archivo (Persistencia)
            // Aquí debemos llamar al DAO correspondiente según el rol
            boolean exito = false;
            String rol = usuario.getRol().toUpperCase();

            model.dao.AdministradorDAO adminDao = new model.dao.AdministradorDAO();
            model.dao.VendedorDAO vendDao = new model.dao.VendedorDAO();
            model.dao.BodegueroDAO bodDao = new model.dao.BodegueroDAO();

            if ("ADMINISTRADOR".equals(rol)) {
                // Como los DAOs genéricos suelen tener update por reescritura,
                // necesitamos un método para actualizar un registro específico.
                // Truco rápido: Leer todos, modificar en lista, reescribir todo.
                // O usar un método en Logic si lo implementaste.
                // Usaremos una lógica manual aquí para asegurar el cambio:
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
                JOptionPane.showMessageDialog(view, "Contraseña actualizada correctamente.\nPor favor inicie sesión nuevamente.");
                view.dispose();
                // Cerrar todo y volver al login (opcional) o dejar pasar
                // En este flujo, como es modal, al cerrarse devolverá el control al LoginController
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Método genérico para actualizar usando tus DAOs existentes
    private <T extends Usuario> void actualizarEnArchivo(libgeneric.GenericDAO<T> dao, T usuarioActualizado) throws IOException {
        java.util.List<T> lista = dao.getAll();
        for (int i = 0; i < lista.size(); i++) {
            T u = lista.get(i);
            // Buscamos por usuario (ID único)
            if (u.getUsuario().equalsIgnoreCase(usuarioActualizado.getUsuario())) {
                lista.set(i, usuarioActualizado);
                break;
            }
        }

        // Reescribir archivo (El DAO genérico debería tener un método para esto,
        // si no, usamos el truco de borrar y rellenar o un método específico en tu Logic).
        // Como tu GenericDAO parece solo tener add/getAll, asumiremos que
        // tienes que reescribir el archivo manualmente o implementar update en el DAO.
        // Aquí haré una implementación rápida de reescritura usando FileWriter directo para no complicar tu GenericDAO:

        String archivoDestino = "";
        if (usuarioActualizado instanceof Administrador) archivoDestino = "data/administradores.txt";
        else if (usuarioActualizado instanceof Vendedor) archivoDestino = "data/vendedores.txt";
        else if (usuarioActualizado instanceof Bodeguero) archivoDestino = "data/bodegueros.txt";

        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(archivoDestino, false))) {
            // Necesitamos el converter del DAO para convertir a String...
            // Como no tenemos acceso fácil al converter desde fuera, usaremos el toString()
            // PERO asegurándonos que toString() en las entidades coincida con toLine().
            // REVISIÓN: Tus entidades tienen toString() con ;. Perfecto.
            for (T item : lista) {
                pw.println(item.toString());
            }
        }
    }
}
