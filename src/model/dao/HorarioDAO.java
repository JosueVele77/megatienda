package model.dao;

import libgeneric.Converter;
import libgeneric.GenericDAO;
import libgeneric.GenericFile;
import model.entities.Horario;
import model.entities.Turno;

public class HorarioDAO extends GenericDAO<Horario> {

    public HorarioDAO() {
        super(new GenericFile<>(
                "horarios.txt",
                new Converter<Horario>() {
                    @Override
                    public Horario fromLine(String line) {
                        String[] p = line.split(";");
                        return new Horario(
                                p[0],//<- Me guarda el idEmpleado
                                Turno.valueOf(p[1]), //<- Me guarda el turno
                                p[2],                       // entrada
                                p[3]                        // salida
                        );
                    }

                    @Override
                    public String toLine(Horario h) {
                        return h.getIdEmpleado() + ";" + h.getTurno().name();
                    }

                    @Override
                    public boolean match(Horario h, String... args) {
                        return h.getIdEmpleado().equals(args[0]);
                    }
                }
        ));
    }
}
