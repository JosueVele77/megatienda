package model.entities;

public class Horario {

    private String idEmpleado;
    private Turno turno;
    private String entrada;
    private String salida;

    public Horario(String idEmpleado, Turno turno,
                   String entrada, String salida) {
        this.idEmpleado = idEmpleado;
        this.turno = turno;
        this.entrada = entrada;
        this.salida = salida;
    }

    public Horario(String idEmpleado, Turno turno) {
        this.idEmpleado = idEmpleado;
        this.turno = turno;
        this.entrada = "";
        this.salida = "";
    }

    public String getIdEmpleado() { return idEmpleado; }
    public Turno getTurno() { return turno; }
    public String getEntrada() { return entrada; }
    public String getSalida() { return salida; }

    @Override
    public String toString() {
        return idEmpleado + ";" + turno + ";" + entrada + ";" + salida;
    }
}
