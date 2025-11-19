package model.entities;

public class Proveedor {

    private String ruc;
    private String nombreEmpresa;
    private String representante;
    private String area;

    public Proveedor(String ruc, String nombreEmpresa,
                     String representante, String area) {
        this.ruc = ruc;
        this.nombreEmpresa = nombreEmpresa;
        this.representante = representante;
        this.area = area;
    }

    public String getRuc() { return ruc; }
    public String getNombreEmpresa() { return nombreEmpresa; }
    public String getRepresentante() { return representante; }
    public String getArea() { return area; }

    @Override
    public String toString() {
        return ruc + ";" + nombreEmpresa + ";" + representante + ";" + area;
    }
}
