package main;

import com.formdev.flatlaf.FlatLightLaf;
import model.logic.EmpleadoLogic;
import model.dao.AdministradorDAO;
import view.LoginView;
import javax.swing.*;
import java.io.File;

public class App {
    public static void main(String[] args) {
        // Estilo
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 15);
        } catch (Exception e) {
            System.err.println("Error al cargar FlatLaf");
        }

        // --- BOOTSTRAP: CREAR ADMIN POR DEFECTO SI NO EXISTE ---
        crearAdminPorDefecto();

        // Iniciar ventana
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }

    private static void crearAdminPorDefecto() {
        try {
            // Verificamos si el archivo existe o está vacío (lógica simple)
            File f = new File("data/administradores.txt");
            if (!f.exists() || f.length() == 0) {
                System.out.println("Primer inicio detectado. Creando admin por defecto...");
                // Usamos EmpleadoLogic para que haga el hash correctamente
                EmpleadoLogic logic = new EmpleadoLogic();
                // admin@gmail.com | admin1234 | Nombre Default | CI ficticia
                logic.registrarAdministrador("admin@gmail.com", "admin1234", "Super Admin", "1700000000");
                System.out.println("Admin creado: admin@gmail.com / admin1234");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}