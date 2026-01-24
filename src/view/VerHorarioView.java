package view;

import com.formdev.flatlaf.FlatLaf;
import model.entities.Horario;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class VerHorarioView extends JDialog {

    private Map<String, Horario> mapaHorarios;

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
        // Adapt header color to theme
        boolean isDark = FlatLaf.isLafDark();
        if (isDark) {
            pnlHeader.setBackground(new Color(50, 50, 50));
        } else {
            Color headerBg = UIManager.getColor("Panel.background");
            pnlHeader.setBackground(headerBg != null ? headerBg : new Color(240, 240, 240));
        }
        JLabel lblTitulo = new JLabel("Horario de: " + nombreEmpleado);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(isDark ? Color.WHITE : Color.BLACK);
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
        // Use UIManager colors for table header to adapt to theme
        Color headerBg = UIManager.getColor("TableHeader.background");
        Color headerFg = UIManager.getColor("TableHeader.foreground");
        if (headerBg != null) {
            tabla.getTableHeader().setBackground(headerBg);
        }
        if (headerFg != null) {
            tabla.getTableHeader().setForeground(headerFg);
        }

        // 4. Renderizador (Lógica de Colores)
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Detect dark mode
                boolean isDarkMode = FlatLaf.isLafDark();

                // Columna Hora (Gris)
                if (column == 0) {
                    if (isDarkMode) {
                        c.setBackground(new Color(50, 50, 55));
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(240, 240, 240));
                        c.setForeground(Color.BLACK);
                    }
                    setHorizontalAlignment(CENTER);
                    return c;
                }

                // Columnas de Días
                String diaColumna = table.getColumnName(column);
                Horario h = mapaHorarios.get(diaColumna);

                // Reset por defecto - adapt to theme
                if (isDarkMode) {
                    c.setBackground(new Color(40, 40, 45));
                    c.setForeground(new Color(200, 200, 200));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                setText("");

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
                    } catch (Exception e) {}
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