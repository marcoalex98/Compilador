package Modelos;

public class Diccionario {
    String tipo, clase, amb, valor, NoPosicion, llave, ListaPertenece;

    public Diccionario(String tipo, String clase, String amb, String valor, 
            String NoPosicion, String llave, String ListaPertenece) {
        this.tipo = tipo;
        this.clase = clase;
        this.amb = amb;
        this.valor = valor;
        this.NoPosicion = NoPosicion;
        this.llave = llave;
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getNoPosicion() {
        return NoPosicion;
    }

    public void setNoPosicion(String NoPosicion) {
        this.NoPosicion = NoPosicion;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getListaPertenece() {
        return ListaPertenece;
    }

    public void setListaPertenece(String ListaPertenece) {
        this.ListaPertenece = ListaPertenece;
    }
    
}
