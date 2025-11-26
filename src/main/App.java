package main;

import com.formdev.flatlaf.FlatLightLaf;
import view.LoginView;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

// Importaciones necesarias para el bypass
import model.dao.AdministradorDAO;
import model.entities.Administrador;
import model.logic.PasswordLogic;

public class App {
    public static void main(String[] args) {
        // 1. Cargar Estilo
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.themes.FlatMacDarkLaf());
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 15);
        } catch (Exception e) {
            System.err.println("Error al cargar FlatLaf");
        }

        // 2. VERIFICACIÓN DE ARCHIVOS
        verificarArchivosDeDatos();

        // 3. Iniciar ventana
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }

    private static void verificarArchivosDeDatos() {
        try {
            // A. Crear carpeta data si no existe
            File carpeta = new File("data");
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            // B. Lista de archivos requeridos por el sistema
            String[] archivos = {
                    "data/administradores.txt",
                    "data/vendedores.txt",
                    "data/bodegueros.txt",
                    "data/clientes.txt",
                    "data/productos.txt",
                    "data/proveedores.txt",
                    "data/ventas.txt",
                    "data/detalles_venta.txt",
                    "data/horarios.txt"
            };

            // C. Crear cada archivo si no existe
            for (String ruta : archivos) {
                File f = new File(ruta);
                if (!f.exists()) {
                    f.createNewFile();
                }
            }

            // D. CORRECCIÓN CLAVE: Crear Admin por defecto saltando validaciones
            // D. CORRECCIÓN CLAVE: Crear Admin por defecto saltando validaciones
            File fAdmin = new File("data/administradores.txt");
            if (fAdmin.length() == 0) {
                System.out.println("Creando admin por defecto...");

                AdministradorDAO adminDao = new AdministradorDAO();
                PasswordLogic passLogic = new PasswordLogic();

                // ACTUALIZAR AQUÍ: Agregamos celular, dirección y fecha dummy
                Administrador admin = new Administrador(
                        "admin@gmail.com",
                        passLogic.hash("admin1234"),
                        "Super Admin",
                        "1700000000",
                        "0999999999",       // <--- Nuevo: Celular
                        "Oficina Central",  // <--- Nuevo: Dirección
                        "2024-01-01"        // <--- Nuevo: Fecha
                );

                adminDao.add(admin);
                System.out.println("Admin por defecto creado con éxito.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error crítico creando archivos de datos: " + e.getMessage());
        }
    }

}