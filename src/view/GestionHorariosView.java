package view;

import com.formdev.flatlaf.FlatClientProperties;
import model.entities.Empleado;
import model.entities.Turno;

import javax.swing.*;
import java.awt.*;

public class GestionHorariosView extends JDialog {

    public JComboBox<EmpleadoWrapper> cmbEmpleados;
    public JComboBox<Turno> cmbTurno;
    public JTextField txtEntrada;
    public JTextField txtSalida;
    public JButton btnGuardar;
    public JButton btnCancelar;

    public GestionHorariosView(Frame owner) {
        super(owner, "Gestión de Horarios", true);
        initComponents();
    }

    private void initComponents() {
        setSize(600, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Selector de Empleado
        JLabel lblEmp = new JLabel("Seleccionar Empleado:");
        lblEmp.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbEmpleados = new JComboBox<>();
        cmbEmpleados.setPreferredSize(new Dimension(500, 40));
        cmbEmpleados.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        pnlContent.add(lblEmp);
        pnlContent.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlContent.add(cmbEmpleados);
        pnlContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // Panel de Configuración (Estilo Tarjeta Oscura como la imagen)
        JPanel pnlCard = new JPanel(new GridLayout(1, 3, 15, 0));
        pnlCard.setBorder(BorderFactory.createTitledBorder("Configuración de Horario Semanal"));
        pnlCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Columna Turno
        JPanel pnlTurno = new JPanel(new BorderLayout());
        pnlTurno.add(new JLabel("Turno:"), BorderLayout.NORTH);
        cmbTurno = new JComboBox<>(Turno.values());
        pnlTurno.add(cmbTurno, BorderLayout.CENTER);

        // Columna Entrada
        JPanel pnlEntrada = new JPanel(new BorderLayout());
        pnlEntrada.add(new JLabel("Entrada (HH:00):"), BorderLayout.NORTH);
        txtEntrada = new JTextField();
        txtEntrada.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "08:00");
        pnlEntrada.add(txtEntrada, BorderLayout.CENTER);

        // Columna Salida
        JPanel pnlSalida = new JPanel(new BorderLayout());
        pnlSalida.add(new JLabel("Salida (HH:00):"), BorderLayout.NORTH);
        txtSalida = new JTextField();
        txtSalida.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "17:00");
        pnlSalida.add(txtSalida, BorderLayout.CENTER);

        pnlCard.add(pnlTurno);
        pnlCard.add(pnlEntrada);
        pnlCard.add(pnlSalida);

        pnlContent.add(pnlCard);
        pnlContent.add(Box.createVerticalGlue());

        // Botones
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnGuardar = new JButton("GUARDAR HORARIO");
        btnGuardar.setBackground(new Color(40, 167, 69)); // Verde
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(180, 45));
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(120, 45));

        pnlBtn.add(btnGuardar);
        pnlBtn.add(btnCancelar);

        add(pnlContent, BorderLayout.CENTER);
        add(pnlBtn, BorderLayout.SOUTH);

        // Padding inferior
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
    }

    // Wrapper para mostrar datos bonitos en el ComboBox
    public static class EmpleadoWrapper {
        private Empleado emp;
        public EmpleadoWrapper(Empleado emp) { this.emp = emp; }
        public Empleado getEmpleado() { return emp; }
        @Override
        public String toString() {
            return emp.getRol() + " | " + emp.getNombre() + " | " + emp.getCedula();
        }
    }
}