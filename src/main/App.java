package main;

import com.formdev.flatlaf.FlatDarkLaf; // O FlatDarkLaf para modo oscuro
import view.LoginView;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // 1. Inyectar el tema ANTES de crear cualquier ventana
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());

            // Opcional: Personalizar color del botón si FlatLaf no lo toma por defecto
            UIManager.put("Button.arc", 15); // Bordes de botones más redondos globalmente
            UIManager.put("Component.focusWidth", 1);
        } catch (Exception e) {
            System.err.println("Error al cargar FlatLaf");
        }

        // 2. Iniciar la aplicación en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}