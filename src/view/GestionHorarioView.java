package view;

import com.formdev.flatlaf.FlatClientProperties;
import model.entities.Empleado;
import model.entities.Turno;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GestionHorarioView extends JDialog {

    public JComboBox<Empleado> cmbEmpleados;
    public JButton btnGuardar, btnCancelar;

    // Mapa para guardar las referencias de los combos por día
    public Map<String, FilaDia> filasDias;

    public GestionHorarioView(Frame owner) {
        super(owner, "Gestión de Horarios", true);
        filasDias = new HashMap<>();
        initComponents();
    }

    private void initComponents() {
        setSize(650, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        pnlHeader.add(new JLabel("Seleccionar Empleado:"));

        cmbEmpleados = new JComboBox<>();
        cmbEmpleados.setPreferredSize(new Dimension(400, 35)); // Un poco más ancho para que quepa el texto

        // --- CAMBIO AQUÍ: Renderizador para mostrar solo Rol - Nombre - Cédula ---
        cmbEmpleados.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Empleado) {
                    Empleado e = (Empleado) value;
                    // Formato limpio: VENDEDOR | Juan Perez | 172635...
                    String texto = String.format("%s | %s | %s",
                            e.getRol().toUpperCase(),
                            e.getNombre(),
                            e.getCedula());
                    setText(texto);
                }
                return this;
            }
        });
        // -----------------------------------------------------------------------

        pnlHeader.add(cmbEmpleados);

        // --- CENTER (Días) ---
        JPanel pnlDias = new JPanel();
        pnlDias.setLayout(new BoxLayout(pnlDias, BoxLayout.Y_AXIS));
        pnlDias.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};

        for (String dia : dias) {
            FilaDia fila = new FilaDia(dia);
            pnlDias.add(fila);
            pnlDias.add(Box.createVerticalStrut(15));
            filasDias.put(dia, fila);
        }

        JScrollPane scroll = new JScrollPane(pnlDias);
        scroll.setBorder(null);

        // --- FOOTER ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnGuardar = new JButton("GUARDAR HORARIO");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(180, 40));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(120, 40));

        pnlFooter.add(btnGuardar);
        pnlFooter.add(btnCancelar);

        add(pnlHeader, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    // Clase interna para manejar cada fila de día visualmente
    public class FilaDia extends JPanel {
        public String nombreDia;
        public JComboBox<Turno> cmbTurno;
        public JLabel lblEntrada;
        public JLabel lblSalida;

        public FilaDia(String dia) {
            this.nombreDia = dia;
            setLayout(new GridLayout(1, 4, 10, 0));
            setBorder(BorderFactory.createTitledBorder(dia));

            cmbTurno = new JComboBox<>(Turno.values());
            lblEntrada = new JLabel("07:00");
            lblSalida = new JLabel("15:00");

            // Estilos
            lblEntrada.setHorizontalAlignment(SwingConstants.CENTER);
            lblEntrada.setBorder(BorderFactory.createTitledBorder("Entrada"));
            lblSalida.setHorizontalAlignment(SwingConstants.CENTER);
            lblSalida.setBorder(BorderFactory.createTitledBorder("Salida"));

            add(new JLabel("Turno:"));
            add(cmbTurno);
            add(lblEntrada);
            add(lblSalida);
        }
    }
}