package view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import model.entities.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public class MenuBodegueroView extends JFrame {

    // Botones del Menú Lateral
    public JButton btnInventario;
    public JButton btnRegistrarProd;
    public JButton btnActualizarProd;
    public JButton btnRegistrarProv;
    public JButton btnSubirImagen;
    public JButton btnSalir;

    // Botón de Tema (Toggle)
    public JToggleButton btnTema;

    // Componentes Estructurales
    public JPanel sidebar;
    public JPanel pnlContent;
    public JPanel pnlInventarioContainer;
    private JPanel pnlListaProductos;

    // Etiquetas
    public JLabel lblInfoBodeguero;
    public JLabel lblLogo;

    public MenuBodegueroView() {
        initComponents();
        actualizarColores(); // Aplicar colores iniciales
    }

    private void initComponents() {
        setTitle("Panel de Bodega - Megatienda");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- 1. SIDEBAR ---
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        lblLogo = new JLabel("BODEGA");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 50)));

        // Botones del menú (Solo texto, sin iconos laterales)
        btnInventario = crearBotonMenu("Inventario");
        btnRegistrarProd = crearBotonMenu("Registrar Producto");
        btnActualizarProd = crearBotonMenu("Actualizar Producto");
        btnRegistrarProv = crearBotonMenu("Registrar Proveedor");
        btnSubirImagen = crearBotonMenu("Subir Imagen");

        sidebar.add(btnInventario);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnRegistrarProd);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnActualizarProd);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnRegistrarProv);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnSubirImagen);

        // --- ESPACIO ELÁSTICO ---
        sidebar.add(Box.createVerticalGlue());

        // --- BOTÓN TEMA (TOGGLE) ---
        JPanel pnlTema = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTema.setOpaque(false);

        btnTema = new JToggleButton();
        btnTema.setPreferredSize(new Dimension(60, 30));
        btnTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTema.setContentAreaFilled(false);
        btnTema.setBorderPainted(false);
        btnTema.setFocusPainted(false);
        btnTema.setIcon(new ToggleSwitchIcon(false));
        btnTema.setSelectedIcon(new ToggleSwitchIcon(true));
        btnTema.setToolTipText("Cambiar Tema");

        JLabel lblTema = new JLabel("Tema: ");
        lblTema.putClientProperty("FlatLaf.styleClass", "h4"); // Estilo sutil
        pnlTema.add(lblTema);
        pnlTema.add(btnTema);

        sidebar.add(pnlTema);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- BOTÓN SALIR ---
        btnSalir = crearBotonMenu("Cerrar Sesión");
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        sidebar.add(btnSalir);

        // --- 2. CONTENIDO ---
        pnlContent = new JPanel(new CardLayout());

        // A. Panel Inventario
        pnlInventarioContainer = new JPanel(new BorderLayout());
        pnlInventarioContainer.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel lblTituloInv = new JLabel("Control de Inventario");
        lblTituloInv.setFont(new Font("Segoe UI", Font.BOLD, 28));

        lblInfoBodeguero = new JLabel("Bodeguero: ...");
        // El estilo se aplica en actualizarColores()

        pnlHeader.add(lblTituloInv, BorderLayout.WEST);
        pnlHeader.add(lblInfoBodeguero, BorderLayout.EAST);
        pnlInventarioContainer.add(pnlHeader, BorderLayout.NORTH);

        // Lista Productos
        pnlListaProductos = new JPanel();
        pnlListaProductos.setLayout(new BoxLayout(pnlListaProductos, BoxLayout.Y_AXIS));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(pnlListaProductos, BorderLayout.NORTH);
        wrapper.setOpaque(false);
        pnlListaProductos.setOpaque(false);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        pnlInventarioContainer.add(scroll, BorderLayout.CENTER);

        // Agregar paneles
        pnlContent.add(pnlInventarioContainer, "INVENTARIO");
        pnlContent.add(new JLabel("Panel Registrar Producto"), "REG_PROD");
        pnlContent.add(new JLabel("Panel Registrar Proveedor"), "REG_PROV");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(pnlContent, BorderLayout.CENTER);

        add(mainPanel);
    }

    // --- MÉTODO PARA GESTIONAR LOS COLORES (DARK vs LIGHT) ---
    public void actualizarColores() {
        boolean isDark = FlatLaf.isLafDark();

        if (isDark) {
            // --- MODO OSCURO ---
            sidebar.setBackground(new Color(30, 30, 35));
            pnlContent.setBackground(new Color(40, 40, 45));
            lblLogo.setForeground(Color.WHITE);
            pnlInventarioContainer.setBackground(new Color(40, 40, 45));

            // Badge (Estilo Píldora Oscura)
            lblInfoBodeguero.setForeground(new Color(98, 196, 255)); // Azul neón
            lblInfoBodeguero.putClientProperty(FlatClientProperties.STYLE, "" +
                    "arc: 999; background: #262a2e; border: 1,1,1,1, #4a90e2; margin: 6,15,6,15; font: bold"
            );
        } else {
            // --- MODO CLARO ---
            sidebar.setBackground(new Color(245, 245, 250)); // Gris muy suave
            pnlContent.setBackground(Color.WHITE);
            lblLogo.setForeground(new Color(60, 60, 60)); // Gris oscuro
            pnlInventarioContainer.setBackground(Color.WHITE);

            // Badge (Estilo Píldora Clara)
            lblInfoBodeguero.setForeground(new Color(30, 30, 30)); // Texto casi negro
            lblInfoBodeguero.putClientProperty(FlatClientProperties.STYLE, "" +
                    "arc: 999; background: #e0e0e0; border: 1,1,1,1, #aaaaaa; margin: 6,15,6,15; font: bold"
            );
        }

        btnTema.setSelected(isDark);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void limpiarInventario() {
        pnlListaProductos.removeAll();
        pnlListaProductos.revalidate();
        pnlListaProductos.repaint();
    }

    public void agregarTarjetaProducto(Producto p, String nombreProveedor, Icon imagenProducto, java.awt.event.ActionListener accionEditar) {
        boolean isDark = FlatLaf.isLafDark();
        JPanel card = new JPanel(new BorderLayout(15, 0));

        // Color de la tarjeta dinámico
        if (isDark) {
            card.setBackground(new Color(55, 55, 60));
            card.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        } else {
            card.setBackground(new Color(250, 250, 250));
            card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        }

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setPreferredSize(new Dimension(0, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
                card.getBorder(),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 15");

        // 1. Imagen
        JLabel lblImg = new JLabel();
        if(imagenProducto != null) lblImg.setIcon(imagenProducto);
        else lblImg.setIcon(new SimpleIcon("box", 64, Color.LIGHT_GRAY));
        lblImg.setPreferredSize(new Dimension(80, 80));
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);

        // 2. Datos
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setOpaque(false);

        JLabel lblNombre = new JLabel(p.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        if (isDark) lblNombre.setForeground(Color.WHITE);
        else lblNombre.setForeground(new Color(50,50,50));

        JLabel lblCodigo = new JLabel("Cod: " + p.getCodigo());
        lblCodigo.setForeground(Color.GRAY);

        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        pnlInfo.setOpaque(false);
        pnlInfo.add(crearDato("Precio", String.format("$%.2f", p.getPrecio())));
        pnlInfo.add(crearDato("Unidades", String.valueOf(p.getStock())));
        pnlInfo.add(crearDato("Proveedor", (nombreProveedor != null ? nombreProveedor : "N/A")));

        pnlCentro.add(lblNombre, BorderLayout.NORTH);
        pnlCentro.add(lblCodigo, BorderLayout.CENTER);
        pnlCentro.add(pnlInfo, BorderLayout.SOUTH);

        // 3. SECCIÓN DERECHA: Título Editar + Botón Cuadrado
        JPanel pnlDerecha = new JPanel(new BorderLayout());
        pnlDerecha.setOpaque(false);

        // Etiqueta "Editar" arriba
        JLabel lblEditTitle = new JLabel("Editar");
        lblEditTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblEditTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEditTitle.setForeground(Color.GRAY);

        // Botón Cuadrado con el icono nuevo
        JButton btnEditar = new JButton(new SimpleIcon("pencil_outline", 24, new Color(59, 130, 246)));
        btnEditar.setPreferredSize(new Dimension(45, 45)); // Cuadrado
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setFocusPainted(false);

        // Estilo del botón (Borde y Fondo suave)
        if (isDark) {
            btnEditar.setBackground(new Color(70, 70, 75)); // Fondo gris oscuro botón
            btnEditar.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderWidth: 0");
        } else {
            btnEditar.setBackground(new Color(230, 230, 235)); // Fondo gris claro botón
            btnEditar.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderWidth: 0");
        }

        btnEditar.addActionListener(accionEditar);

        pnlDerecha.add(lblEditTitle, BorderLayout.NORTH);
        pnlDerecha.add(btnEditar, BorderLayout.CENTER);

        card.add(lblImg, BorderLayout.WEST);
        card.add(pnlCentro, BorderLayout.CENTER);
        card.add(pnlDerecha, BorderLayout.EAST);

        pnlListaProductos.add(card);
        pnlListaProductos.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    private JPanel crearDato(String titulo, String valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l1 = new JLabel(titulo);
        l1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l1.setForeground(Color.GRAY);
        l1.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel l2 = new JLabel(valor);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        // El color se ajusta solo (negro en light, blanco en dark) por defecto en Swing si no se fuerza
        l2.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(l1, BorderLayout.NORTH);
        p.add(l2, BorderLayout.CENTER);
        return p;
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Sin icono, solo texto centrado
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 15; margin: 0,10,0,10; Button.borderWidth:0");
        return btn;
    }

    // --- ICONO SWITCH (Interruptor) ---
    private static class ToggleSwitchIcon implements Icon {
        private final boolean isSelected;
        private final int width = 50;
        private final int height = 24;

        public ToggleSwitchIcon(boolean isSelected) { this.isSelected = isSelected; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isSelected) g2.setColor(new Color(59, 130, 246)); // Azul Dark
            else g2.setColor(new Color(200, 200, 200)); // Gris Light
            g2.fill(new RoundRectangle2D.Double(x, y, width, height, height, height));

            g2.setColor(Color.WHITE);
            int circleSize = height - 4;
            int circleX = isSelected ? (x + width - circleSize - 2) : (x + 2);
            g2.fill(new Ellipse2D.Double(circleX, y + 2, circleSize, circleSize));

            // Sol / Luna
            g2.setColor(isSelected ? new Color(59, 130, 246) : Color.ORANGE);
            if (isSelected) {
                g2.fill(new Ellipse2D.Double(circleX + 4, y + 6, 8, 8));
                g2.setColor(Color.WHITE);
                g2.fill(new Ellipse2D.Double(circleX + 2, y + 4, 8, 8));
            } else {
                g2.fill(new Ellipse2D.Double(circleX + 4, y + 6, 8, 8));
            }
            g2.dispose();
        }
        @Override public int getIconWidth() { return width; }
        @Override public int getIconHeight() { return height; }
    }

    // --- ICONOS SIMPLES (LÁPIZ NUEVO) ---
    public static class SimpleIcon implements Icon {
        String t; int s; Color c;
        public SimpleIcon(String t, int s, Color c){this.t=t;this.s=s;this.c=c;}
        public void paintIcon(Component cmp,Graphics g,int x,int y){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.setStroke(new BasicStroke(2));

            if(t.equals("box")) g2.drawRect(x+2,y+2,s-4,s-4);

            // --- NUEVO LÁPIZ (CONTORNO) ---
            if(t.equals("pencil_outline")) {
                // Dibujamos un lápiz en diagonal
                // Cuerpo (Rectángulo rotado)
                Path2D path = new Path2D.Double();
                // Coordenadas ajustadas para el tamaño s (ej: 24)
                double padding = s * 0.2;
                double w = s - (padding * 2);

                // Rotar 45 grados para dibujar recto y que salga inclinado no es fácil con drawLine
                // Dibujamos líneas diagonales manualmente

                // Línea inferior del cuerpo
                g2.drawLine(x+6, y+s-6, x+s-10, y+10);
                // Línea superior del cuerpo
                g2.drawLine(x+10, y+s-10, x+s-6, y+6);
                // Base (arriba derecha)
                g2.drawLine(x+s-10, y+10, x+s-6, y+6);
                // Punta (triángulo abajo izquierda)
                g2.drawLine(x+6, y+s-6, x+4, y+s-4); // lado 1 punta
                g2.drawLine(x+10, y+s-10, x+4, y+s-4); // lado 2 punta
                // Borrador (opcional, línea pequeña arriba)
                g2.drawLine(x+s-9, y+9, x+s-7, y+7);
            }
            g2.dispose();
        }
        public int getIconWidth(){return s;} public int getIconHeight(){return s;}
    }
}