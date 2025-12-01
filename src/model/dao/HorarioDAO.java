package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Horario;
import model.entities.Turno;

public class HorarioDAO extends GenericDAO<Horario> {

    public HorarioDAO() {
        super(new GenericFile<>(
                "data/horarios.txt",
                new Converter<Horario>() {
                    @Override
                    public Horario fromLine(String line) {
                        if (line == null || line.trim().isEmpty()) return null;
                        String[] p = line.split(";");
                        if (p.length < 5) return null;
                        return new Horario(
                                p[0],               // idEmpleado
                                p[1],               // dia
                                Turno.valueOf(p[2]),// turno
                                p[3],               // entrada
                                p[4]                // salida
                        );
                    }

                    @Override
                    public String toLine(Horario h) {
                        return h.getIdEmpleado() + ";" +
                                h.getDia() + ";" +
                                h.getTurno().name() + ";" +
                                h.getEntrada() + ";" +
                                h.getSalida();
                    }

                    @Override
                    public boolean match(Horario h, String... args) {
                        return h.getIdEmpleado().equals(args[0]);
                    }
                }
        ));
    }
}