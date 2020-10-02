public class Lista {
    String tipo, clase, amb, noPos, listaPertenece;

    public Lista(String tipo, String clase, String amb, String noPos, String listaPertenece) {
        this.tipo = tipo;
        this.clase = clase;
        this.amb = amb;
        this.noPos = noPos;
        this.listaPertenece = listaPertenece;
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
