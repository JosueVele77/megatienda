package view;

import com.formdev.flatlaf.FlatClientProperties;
import model.entities.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuBodegueroView extends JFrame {

    // Botones del Menú Lateral
    public JButton btnInventario;
    public JButton btnRegistrarProd;
    public JButton btnActualizarProd; // Opcional si usas el lápiz
    public JButton btnRegistrarProv;
    public JButton btnSalir;
    public JButton btnSubirImagen;

    // Paneles de contenido
    public JPanel pnlContent; // Panel cambiante (CardLayout)
    public JPanel pnlInventarioContainer; // Panel específico del inventario
    private JPanel pnlListaProductos; // Donde se agregan las filas de productos

    // Colores
    private final Color COLOR_BG_DARK = new Color(30, 30, 35);
    private final Color COLOR_CARD_BG = new Color(45, 45, 50);
    private final Color COLOR_ACCENT = new Color(59, 130, 246);

    public MenuBodegueroView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Panel de Bodega - Megatienda");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- 1. SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_BG_DARK);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel lblLogo = new JLabel("BODEGA");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // Botones del menú
        btnInventario = crearBotonMenu("Inventario", "box");
        btnRegistrarProd = crearBotonMenu("Registrar Producto", "add");
        btnActualizarProd = crearBotonMenu("Actualizar Producto", "edit");
        btnRegistrarProv = crearBotonMenu("Registrar Proveedor", "group");
        btnSubirImagen = crearBotonMenu("Subir Imagen", "image");

        sidebar.add(btnInventario);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnRegistrarProd);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnActualizarProd);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnRegistrarProv);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnSubirImagen);

        sidebar.add(Box.createVerticalGlue());
        btnSalir = crearBotonMenu("Cerrar Sesión", "exit");
        btnSalir.setBackground(new Color(220, 53, 69));
        sidebar.add(btnSalir);

        // --- 2. CONTENIDO ---
        pnlContent = new JPanel(new CardLayout());

        // A. Panel Inventario (El que se parece a tu foto)
        pnlInventarioContainer = new JPanel(new BorderLayout());
        pnlInventarioContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTituloInv = new JLabel("Control de Inventario");
        lblTituloInv.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlInventarioContainer.add(lblTituloInv, BorderLayout.NORTH);

        // Contenedor con Scroll para la lista de productos
        pnlListaProductos = new JPanel();
        pnlListaProductos.setLayout(new BoxLayout(pnlListaProductos, BoxLayout.Y_AXIS));
        // Truco para que empiece arriba
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(pnlListaProductos, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        pnlInventarioContainer.add(scroll, BorderLayout.CENTER);

        // Agregar paneles al CardLayout
        pnlContent.add(pnlInventarioContainer, "INVENTARIO");
        // Aquí agregarías los paneles de registro (puedes reutilizar vistas o crear nuevas)
        pnlContent.add(new JLabel("Panel Registrar Producto (En construcción)"), "REG_PROD");
        pnlContent.add(new JLabel("Panel Registrar Proveedor (En construcción)"), "REG_PROV");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(pnlContent, BorderLayout.CENTER);

        add(mainPanel);
    }

    // --- MÉTODOS PARA DIBUJAR PRODUCTOS ---

    public void limpiarInventario() {
        pnlListaProductos.removeAll();
        pnlListaProductos.revalidate();
        pnlListaProductos.repaint();
    }

    public void agregarTarjetaProducto(Producto p, String nombreProveedor, Icon imagenProducto, java.awt.event.ActionListener accionEditar) {
        // Panel base de la tarjeta
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(COLOR_CARD_BG);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setPreferredSize(new Dimension(0, 100));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Borde redondeado sutil
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 15");

        // 1. Imagen (Izquierda)
        JLabel lblImg = new JLabel();
        if(imagenProducto != null) {
            lblImg.setIcon(imagenProducto);
        } else {
            // Placeholder si no hay imagen (Caja gris)
            lblImg.setIcon(new SimpleIcon("box", 64, Color.LIGHT_GRAY));
        }
        lblImg.setPreferredSize(new Dimension(80, 80));
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);

        // 2. Datos (Centro)
        JPanel pnlDatos = new JPanel(new GridLayout(2, 1));
        pnlDatos.setOpaque(false);

        JLabel lblNombre = new JLabel(p.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(Color.WHITE);

        JLabel lblCodigo = new JLabel("Cod: " + p.getCodigo());
        lblCodigo.setForeground(Color.GRAY);

        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        pnlInfo.setOpaque(false);

        // --- AQUÍ ESTÁ EL CAMBIO ---
        // Agregamos Precio, Stock y AHORA EL PROVEEDOR
        pnlInfo.add(crearDato("Precio", String.format("$%.2f", p.getPrecio())));
        pnlInfo.add(crearDato("Unidades", String.valueOf(p.getStock())));

        // Mostramos el nombre del proveedor (o "Sin Proveedor" si viene nulo)
        pnlInfo.add(crearDato("Proveedor", (nombreProveedor != null ? nombreProveedor : "N/A")));

        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setOpaque(false);
        pnlCentro.add(lblNombre, BorderLayout.NORTH);
        pnlCentro.add(lblCodigo, BorderLayout.CENTER);
        pnlCentro.add(pnlInfo, BorderLayout.SOUTH);

        // 3. Botón Editar (Derecha) - El lápiz gigante
        JButton btnEditar = new JButton(new SimpleIcon("edit", 32, COLOR_ACCENT));
        btnEditar.setBorderPainted(false);
        btnEditar.setContentAreaFilled(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.addActionListener(accionEditar);

        JLabel lblEditTitle = new JLabel("Editar");
        lblEditTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblEditTitle.setForeground(Color.GRAY); // Color gris para el texto "Editar"

        JPanel pnlDerecha = new JPanel(new BorderLayout());
        pnlDerecha.setOpaque(false);
        pnlDerecha.add(lblEditTitle, BorderLayout.NORTH);
        pnlDerecha.add(btnEditar, BorderLayout.CENTER);

        // Armar tarjeta
        card.add(lblImg, BorderLayout.WEST);
        card.add(pnlCentro, BorderLayout.CENTER);
        card.add(pnlDerecha, BorderLayout.EAST);

        // Agregar al contenedor principal con espacio
        pnlListaProductos.add(card);
        pnlListaProductos.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JPanel crearDato(String titulo, String valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l1 = new JLabel(titulo);
        l1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l1.setForeground(Color.LIGHT_GRAY);
        l1.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel l2 = new JLabel(valor);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l2.setForeground(Color.WHITE);
        l2.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(l1, BorderLayout.NORTH);
        p.add(l2, BorderLayout.CENTER);
        return p;
    }

    private JButton crearBotonMenu(String texto, String icono) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 0,20,0,0; Button.borderWidth:0");
        // Aquí podrías poner el ícono real si tienes
        return btn;
    }

    // Clase simple para iconos (para no depender de archivos externos por ahora)
    public static class SimpleIcon implements Icon {
        String t; int s; Color c;
        public SimpleIcon(String t, int s, Color c){this.t=t;this.s=s;this.c=c;}
        public void paintIcon(Component cmp,Graphics g,int x,int y){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.setStroke(new BasicStroke(2));
            if(t.equals("box")) g2.drawRect(x+2,y+2,s-4,s-4);
            if(t.equals("edit")) { // Lápiz
                g2.drawLine(x+4, y+s-4, x+8, y+s-4); // base
                g2.drawLine(x+4, y+s-4, x+s-4, y+4); // linea
                g2.drawLine(x+8, y+s-4, x+s, y+8);   // linea 2
            }
            g2.dispose();
        }
        public int getIconWidth(){return s;} public int getIconHeight(){return s;}
    }
}