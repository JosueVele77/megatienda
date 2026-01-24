package view;

import model.entities.Empleado;
import model.entities.Turno;

import javax.swing.*;
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
        setSize(700, 750); // Un poco más alto para que quepan los 5 días cómodamente
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // --- 1. HEADER (SOLUCIÓN MODO OSCURO) ---
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        pnlHeader.setBackground(new Color(60, 63, 65)); // Fondo Gris Oscuro

        JLabel lblTitulo = new JLabel("Seleccionar Empleado:");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setForeground(Color.WHITE); // Letras BLANCAS para que se vean

        cmbEmpleados = new JComboBox<>();
        cmbEmpleados.setPreferredSize(new Dimension(350, 30));

        // Renderizador para ver: ROL | Nombre | Cédula
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

        // --- 2. DÍAS (SOLUCIÓN 5 DÍAS) ---
        JPanel pnlDias = new JPanel();
        pnlDias.setLayout(new BoxLayout(pnlDias, BoxLayout.Y_AXIS));
        pnlDias.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Arreglo fijo de Lunes a Viernes
        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};

        for (String dia : dias) {
            FilaDia fila = new FilaDia(dia);
            pnlDias.add(fila);
            pnlDias.add(Box.createVerticalStrut(10)); // Espacio entre filas
            filasDias.put(dia, fila);
        }

        JScrollPane scroll = new JScrollPane(pnlDias);
        scroll.setBorder(null);

        // --- 3. FOOTER ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlFooter.setBackground(new Color(60, 63, 65)); // Fondo oscuro también abajo

        btnGuardar = new JButton("GUARDAR ASIGNACIÓN");
        btnGuardar.setBackground(new Color(40, 167, 69)); // Verde
        btnGuardar.setForeground(Color.WHITE);
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

    // Clase interna para cada fila (Día)
    public class FilaDia extends JPanel {
        public String nombreDia;
        public JComboBox<Turno> cmbTurno;
        public JLabel lblEntrada;
        public JLabel lblSalida;

        public FilaDia(String dia) {
            this.nombreDia = dia;
            setLayout(new GridLayout(1, 4, 15, 0)); // Grid de 4 columnas

            // Borde con título del día
            setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    dia,
                    0,
                    0,
                    new Font("SansSerif", Font.BOLD, 12)
                    // Nota: FlatLaf maneja el color del título automáticamente
            ));

            cmbTurno = new JComboBox<>(Turno.values());

            // Labels grandes y claros
            lblEntrada = crearLabelHora("07:00");
            lblSalida = crearLabelHora("15:00");

            // Panelitos para etiquetas
            add(new JLabel("Turno:"));
            add(cmbTurno);
            add(crearPanelHora("Inicio:", lblEntrada));
            add(crearPanelHora("Fin:", lblSalida));
        }

        private JLabel crearLabelHora(String texto) {
            JLabel lbl = new JLabel(texto);
            lbl.setFont(new Font("Monospaced", Font.BOLD, 14));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setForeground(new Color(0, 102, 204)); // Azul para resaltar la hora
            return lbl;
        }

        private JPanel crearPanelHora(String titulo, JLabel lblHora) {
            JPanel p = new JPanel(new BorderLayout());
            JLabel title = new JLabel(titulo);
            title.setFont(new Font("SansSerif", Font.PLAIN, 10));
            p.add(title, BorderLayout.NORTH);
            p.add(lblHora, BorderLayout.CENTER);
            return p;
        }
    }
}