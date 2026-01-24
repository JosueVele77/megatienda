package view;

import model.entities.Horario;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VerHorarioView extends JDialog {

    public VerHorarioView(Frame owner, Horario horario, String nombreEmpleado) {
        super(owner, "Visualización de Horario", true);
        setSize(900, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // 1. Cabecera con datos del empleado
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(40, 40, 45)); // Fondo oscuro

        JLabel lblNombre = new JLabel("Empleado: " + nombreEmpleado);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblNombre.setForeground(Color.WHITE);

        JLabel lblDetalle = new JLabel("Turno: " + horario.getTurno() + " | Entrada: " + horario.getEntrada() + " - Salida: " + horario.getSalida());
        lblDetalle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDetalle.setForeground(new Color(200, 200, 200));

        header.add(lblNombre);
        header.add(lblDetalle);
        add(header, BorderLayout.NORTH);

        // 2. Tabla Gráfica (Horas vs Días)
        String[] dias = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        DefaultTableModel model = new DefaultTableModel(dias, 0);

        // Generamos filas de 06:00 a 22:00
        for (int i = 6; i <= 22; i++) {
            model.addRow(new Object[]{String.format("%02d:00", i), "", "", "", "", "", "", ""});
        }

        JTable tabla = new JTable(model);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Renderizador para pintar las celdas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // La primera columna es la Hora (gris)
                if (column == 0) {
                    c.setBackground(Color.LIGHT_GRAY);
                    c.setForeground(Color.BLACK);
                    setFont(new Font("SansSerif", Font.BOLD, 12));
                    return c;
                }

                // Obtener hora de la fila y compararla con el horario del empleado
                try {
                    String horaFilaStr = (String) table.getValueAt(row, 0); // Ej: "08:00"
                    int horaFila = Integer.parseInt(horaFilaStr.split(":")[0]);

                    int horaEntrada = Integer.parseInt(horario.getEntrada().split(":")[0]);
                    int horaSalida = Integer.parseInt(horario.getSalida().split(":")[0]);

                    // Si la fila está dentro del rango de trabajo, pintar de AZUL
                    if (horaFila >= horaEntrada && horaFila < horaSalida) {
                        c.setBackground(new Color(59, 130, 246)); // Azul bonito
                        c.setForeground(Color.WHITE);
                        setText("TRABAJO");
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                        setText("");
                    }
                } catch (Exception e) {
                    // Si hay error parseando (ej. horario vacío), dejar en blanco
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                    setText("");
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}