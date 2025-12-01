package model.entities;

public class Horario {

    private String idEmpleado;
    private String dia;
    private Turno turno;
    private String entrada;
    private String salida;

    public Horario(String idEmpleado, String dia,  Turno turno,
                   String entrada, String salida) {
        this.idEmpleado = idEmpleado;
        this.dia = dia;
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
    public String getDia() { return dia; }
    public Turno getTurno() { return turno; }
    public String getEntrada() { return entrada; }
    public String getSalida() { return salida; }

    @Override
    public String toString() {
        return idEmpleado + ";" + dia + ";" + turno + ";" + entrada + ";" + salida;
    }
}
