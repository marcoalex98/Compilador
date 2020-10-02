public class Conjunto {
    String tipo, clase, amb, NoPosicion,ListaPertenece;

    public Conjunto(String tipo, String clase, String amb, String NoPosicion, String ListaPertenece) {
        this.tipo = tipo;
        this.clase = clase;
        this.amb = amb;
        this.NoPosicion = NoPosicion;
        this.ListaPertenece = ListaPertenece;
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

    public String getNoPosicion() {
        return NoPosicion;
    }

    public void setNoPosicion(String NoPosicion) {
        this.NoPosicion = NoPosicion;
    }

    public String getListaPertenece() {
        return ListaPertenece;
    }

    public void setListaPertenece(String ListaPertenece) {
        this.ListaPertenece = ListaPertenece;
    }
}
