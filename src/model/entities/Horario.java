package model.entities;

public class Horario {
    private String idEmpleado;
    private String dia;
    private Turno turno;
    private String entrada;
    private String salida;

    // --- CONSTRUCTOR 1: COMPLETO (5 Parámetros - Con Día) ---
    // Este lo usa tu lógica de guardar días específicos
    public Horario(String idEmpleado, String dia, Turno turno, String entrada, String salida) {
        this.idEmpleado = idEmpleado;
        this.dia = dia;
        this.turno = turno;
        this.entrada = entrada;
        this.salida = salida;
    }

    // --- CONSTRUCTOR 2: COMPATIBILIDAD (4 Parámetros - Sin Día) ---
    // <--- ESTE ES EL QUE SOLUCIONA TU ERROR ACTUAL --->
    public Horario(String idEmpleado, Turno turno, String entrada, String salida) {
        this.idEmpleado = idEmpleado;
        this.dia = ""; // Pone el día vacío por defecto
        this.turno = turno;
        this.entrada = entrada;
        this.salida = salida;
    }

    // --- CONSTRUCTOR 3: SOLO ID Y TURNO (Para crear defaults rápidos) ---
    public Horario(String idEmpleado, Turno turno) {
        this(idEmpleado, "", turno, "00:00", "00:00");
    }

    // --- CONSTRUCTOR 4: VACÍO ---
    public Horario() {}

    // Getters y Setters
    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }

    public String getEntrada() { return entrada; }
    public void setEntrada(String entrada) { this.entrada = entrada; }

    public String getSalida() { return salida; }
    public void setSalida(String salida) { this.salida = salida; }

    @Override
    public String toString() {
        return idEmpleado + ";" + dia + ";" + turno + ";" + entrada + ";" + salida;
    }
}