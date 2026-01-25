package view;

import model.entities.Empleado;
import model.entities.Turno;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GestionHorarioView extends JDialog {

    public JComboBox<Empleado> cmbEmpleados;
    public JButton btnGuardar, btnCancelar;
    public Map<String, FilaDia> filasDias;

    public GestionHorarioView(Frame owner) {
        super(owner, "Asignación de Horarios", true);
        filasDias = new HashMap<>();
        initComponents();
    }

    private void initComponents() {
        setSize(700, 750);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // HEADER
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        pnlHeader.setBackground(UIManager.getColor("Panel.background")); // Adaptable

        JLabel lblTitulo = new JLabel("Seleccionar Empleado:");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setForeground(UIManager.getColor("Label.foreground")); // Adaptable

        cmbEmpleados = new JComboBox<>();
        cmbEmpleados.setPreferredSize(new Dimension(350, 30));
        cmbEmpleados.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Empleado) {
                    Empleado e = (Empleado) value;
                    setText(e.getRol() + " | " + e.getNombre() + " | " + e.getCedula());
                }
                return this;
            }
        });

        pnlHeader.add(lblTitulo);
        pnlHeader.add(cmbEmpleados);

        // DÍAS
        JPanel pnlDias = new JPanel();
        pnlDias.setLayout(new BoxLayout(pnlDias, BoxLayout.Y_AXIS));
        pnlDias.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};

        for (String dia : dias) {
            FilaDia fila = new FilaDia(dia);
            pnlDias.add(fila);
            pnlDias.add(Box.createVerticalStrut(10));
            filasDias.put(dia, fila);
        }

        JScrollPane scroll = new JScrollPane(pnlDias);
        scroll.setBorder(null);

        // FOOTER
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlFooter.setBackground(UIManager.getColor("Panel.background")); // Adaptable

        btnGuardar = new JButton("GUARDAR ASIGNACIÓN");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        // Dejar que el Look and Feel maneje los colores para adaptabilidad
        // btnGuardar.setBackground(new Color(40, 167, 69));
        // btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(200, 40));
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(120, 40));

        pnlFooter.add(btnGuardar);
        pnlFooter.add(btnCancelar);

        add(pnlHeader, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    // Java
    public class FilaDia extends JPanel {
        public String nombreDia;
        public JComboBox<Turno> cmbTurno;
        public JLabel lblEntrada;
        public JLabel lblSalida;

        public FilaDia(String dia) {
            this.nombreDia = dia;

            Color fondoBase = UIManager.getColor("Panel.background");
            if (fondoBase == null) {
                fondoBase = new Color(43, 43, 43); // Fallback para modo oscuro
            }
            setBackground(fondoBase);
            setOpaque(true);

            Color colorContraste = obtenerColorContraste(fondoBase);

            setLayout(new GridLayout(1, 4, 15, 0));
            TitledBorder border = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(colorContraste.darker()),
                    dia,
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION,
                    new Font("SansSerif", Font.BOLD, 12),
                    colorContraste
            );
            setBorder(border);

            cmbTurno = new JComboBox<>(Turno.values());
            cmbTurno.setBackground(fondoBase);
            cmbTurno.setForeground(colorContraste);

            lblEntrada = crearLabelHora("07:00", colorContraste);
            lblSalida = crearLabelHora("15:00", colorContraste);

            add(crearLabelSimple("Turno:", colorContraste));
            add(cmbTurno);
            add(crearPanelHora("Inicio:", lblEntrada, fondoBase, colorContraste));
            add(crearPanelHora("Fin:", lblSalida, fondoBase, colorContraste));
        }

        private Color obtenerColorContraste(Color fondo) {
            double lum = 0.2126 * fondo.getRed() + 0.7152 * fondo.getGreen() + 0.0722 * fondo.getBlue();
            return lum < 140 ? Color.WHITE : Color.BLACK;
        }

        private JLabel crearLabelSimple(String texto, Color color) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(color);
            return lbl;
        }

        private JLabel crearLabelHora(String texto, Color color) {
            JLabel lbl = new JLabel(texto);
            lbl.setFont(new Font("Monospaced", Font.BOLD, 14));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setForeground(color);
            return lbl;
        }

        private JPanel crearPanelHora(String titulo, JLabel lblHora, Color fondo, Color textoColor) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lblTitulo.setForeground(textoColor);
            panel.add(lblTitulo, BorderLayout.NORTH);
            panel.add(lblHora, BorderLayout.CENTER);
            return panel;
        }
    }
}
