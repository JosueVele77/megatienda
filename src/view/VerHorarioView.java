package view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import model.entities.Horario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VerHorarioView extends JDialog {

    private final Horario horario;

    public VerHorarioView(Frame owner, Horario horario, String nombreEmpleado) {
        super(owner, "Horario de Empleado", true);
        this.horario = horario;
        initComponents(nombreEmpleado);
    }

    private void initComponents(String nombre) {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Horario Semanal");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblSub = new JLabel("Empleado: " + nombre + " | Cédula: " + horario.getIdEmpleado());
        lblSub.setForeground(Color.GRAY);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblTurno = new JLabel("Turno Asignado: " + horario.getTurno());
        lblTurno.setForeground(new Color(59, 130, 246));
        lblTurno.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel pnlInfo = new JPanel(new GridLayout(2, 1));
        pnlInfo.add(lblSub);
        pnlInfo.add(lblTurno);

        pnlHeader.add(lblTitulo, BorderLayout.NORTH);
        pnlHeader.add(pnlInfo, BorderLayout.CENTER);

        // --- TABLA TIPO CALENDARIO ---
        String[] columnas = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Generar filas de horas (De 06:00 a 22:00 por ejemplo)
        for (int i = 6; i <= 22; i++) {
            String hora = String.format("%02d:00", i);
            model.addRow(new Object[]{hora, "", "", "", "", ""});
        }

        JTable tabla = new JTable(model);
        tabla.setRowHeight(35);
        tabla.setShowGrid(true);
        tabla.setGridColor(new Color(200, 200, 200));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(240, 240, 240));

        // --- RENDERIZADO PERSONALIZADO PARA COLOREAR CELDAS ---
        tabla.setDefaultRenderer(Object.class, new HorarioCellRenderer(horario));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // --- Botón Cerrar ---
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        btnCerrar.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 5,20,5,20");
        JPanel pnlSur = new JPanel();
        pnlSur.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        pnlSur.add(btnCerrar);

        add(pnlHeader, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(pnlSur, BorderLayout.SOUTH);
    }

    // Clase interna para pintar las celdas
    private static class HorarioCellRenderer extends DefaultTableCellRenderer {
        private final int horaEntrada;
        private final int horaSalida;
        private final Color COLOR_SHIFT = new Color(59, 130, 246); // Azul
        private final Color COLOR_SHIFT_DARK = new Color(30, 80, 160); // Azul Oscuro

        public HorarioCellRenderer(Horario h) {
            setHorizontalAlignment(SwingConstants.CENTER);
            // Parsear horas (Ej: "07:00" -> 7)
            int hIn = 0, hOut = 0;
            try {
                if(h.getEntrada() != null && h.getEntrada().contains(":"))
                    hIn = Integer.parseInt(h.getEntrada().split(":")[0]);
                if(h.getSalida() != null && h.getSalida().contains(":"))
                    hOut = Integer.parseInt(h.getSalida().split(":")[0]);
            } catch (Exception e) {}

            this.horaEntrada = hIn;
            this.horaSalida = hOut;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Obtener la hora de la fila actual (Columna 0 tiene el texto "HH:00")
            String horaTexto = (String) table.getValueAt(row, 0);
            int horaFila = Integer.parseInt(horaTexto.split(":")[0]);

            // Columna 0 es la etiqueta de la hora -> Gris
            if (column == 0) {
                c.setBackground(FlatLaf.isLafDark() ? new Color(60, 60, 65) : new Color(240, 240, 240));
                c.setForeground(FlatLaf.isLafDark() ? Color.WHITE : Color.BLACK);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }

            // Verificar si esta celda cae dentro del horario
            // Lógica simple: Si la hora de la fila está entre entrada y salida (exclusiva salida)
            if (horaFila >= horaEntrada && horaFila < horaSalida) {
                c.setBackground(FlatLaf.isLafDark() ? COLOR_SHIFT_DARK : COLOR_SHIFT);
                c.setForeground(Color.WHITE);
                setText("Trabajo"); // Texto dentro de la barra
            } else {
                c.setBackground(table.getBackground());
                c.setForeground(table.getForeground());
                setText("");
            }

            // Restablecer fuente normal para el contenido
            setFont(new Font("Segoe UI", Font.PLAIN, 12));

            // Si está seleccionada la fila (opcional, para que no tape el color)
            if (isSelected) {
                c.setBackground(c.getBackground().darker());
            }

            return c;
        }
    }
}