package Modelos;

public class Tupla {
    String tipo, clase, amb, noPos, listaPertenece, valor;

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Tupla(String tipo, String clase, String amb, String noPos, String listaPertenece, String valor) {
        this.tipo = tipo;
        this.clase = clase;
        this.amb = amb;
        this.noPos = noPos;
        this.listaPertenece = listaPertenece;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getAmb() {
        return amb;
    }

    public void setAmb(String amb) {
        this.amb = amb;
    }

    public String getNoPos() {
        return noPos;
    }

    public void setNoPos(String noPos) {
        this.noPos = noPos;
    }

    public String getListaPertenece() {
        return listaPertenece;
    }

    public void setListaPertenece(String listaPertenece) {
        this.listaPertenece = listaPertenece;
    }
    
}
