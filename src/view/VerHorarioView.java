package view;

import model.entities.Horario;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class VerHorarioView extends JDialog {

    private final Map<String, Horario> mapaHorarios;

    public VerHorarioView(Frame owner, List<Horario> horariosSemana, String nombreEmpleado) {
        super(owner, "Visualización de Horario Semanal", true);
        setSize(1000, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // 1. Procesar datos para acceso rápido
        mapaHorarios = new HashMap<>();
        if (horariosSemana != null) {
            for (Horario h : horariosSemana) {
                mapaHorarios.put(h.getDia().toUpperCase(), h);
            }
        }

        // 2. Encabezado
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        // Usar colores del UIManager para adaptarse al modo oscuro/claro
        //pnlHeader.setBackground(UIManager.getColor("Panel.background"));
        JLabel lblTitulo = new JLabel("Horario de: " + nombreEmpleado);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(UIManager.getColor("Label.foreground"));
        pnlHeader.add(lblTitulo);
        add(pnlHeader, BorderLayout.NORTH);

        // 3. Modelo de Tabla (Horas vs Días)
        String[] columnas = {"HORA", "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Filas de 00:00 a 23:00
        for (int i = 0; i < 24; i++) {
            model.addRow(new Object[]{String.format("%02d:00", i), "", "", "", "", ""});
        }

        JTable tabla = new JTable(model);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        // Dejar que el UIManager maneje el color del header para el modo oscuro
        // tabla.getTableHeader().setBackground(new Color(220, 220, 220));

        // 4. Renderizador (Lógica de Colores)
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // No establecer colores si la celda está seleccionada para usar los colores de selección del sistema
                if (isSelected) {
                    return c;
                }

                // Columna Hora
                if (column == 0) {
                    c.setBackground(UIManager.getColor("Panel.background"));

                    // CAMBIO AQUÍ: Usar el color de texto dinámico de la tabla
                    c.setForeground(UIManager.getColor("Table.foreground"));

                    setHorizontalAlignment(CENTER);
                    return c;
                }

                // Columnas de Días
                String diaColumna = table.getColumnName(column);
                Horario h = mapaHorarios.get(diaColumna);

                // Reset a colores por defecto del Look and Feel
                c.setBackground(UIManager.getColor("Table.background"));
                c.setForeground(UIManager.getColor("Table.foreground"));
                setText(""); // Limpiar texto por defecto

                if (h != null) {
                    try {
                        int horaFila = Integer.parseInt(table.getValueAt(row, 0).toString().split(":")[0]);
                        int horaEnt = Integer.parseInt(h.getEntrada().split(":")[0]);
                        int horaSal = Integer.parseInt(h.getSalida().split(":")[0]);

                        boolean activo = false;
                        // Turno normal (Ej: 07 a 15)
                        if (horaEnt < horaSal) {
                            if (horaFila >= horaEnt && horaFila < horaSal) activo = true;
                        }
                        // Turno cruzado (Madrugada: 23 a 07)
                        else {
                            if (horaFila >= horaEnt || horaFila < horaSal) activo = true;
                        }

                        if (activo) {
                            c.setBackground(new Color(70, 130, 180)); // Azul Acero
                            c.setForeground(Color.WHITE);
                            setText(h.getTurno().toString());
                            setHorizontalAlignment(CENTER);
                        }
                    } catch (NumberFormatException | NullPointerException e) {
                        // Ignorar errores de parseo si los datos son inválidos, la celda quedará en blanco
                    }
                }
                return c;
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel pnlSur = new JPanel();
        pnlSur.add(btnCerrar);
        add(pnlSur, BorderLayout.SOUTH);
    }
}
