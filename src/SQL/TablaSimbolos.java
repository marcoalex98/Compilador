package SQL;

public class TablaSimbolos {
    String id, tipo, clase, amb, tarr, ambCreado, noPos, listaPertence, rango,
            avance, llave, valor;

    public TablaSimbolos(String id, String tipo, String clase, String amb, String tarr, 
            String ambCreado, String noPos, String listaPertence, String rango, String avance, 
            String llave, String valor) {
        this.id = id;
        this.tipo = tipo;
        this.clase = clase;
        this.amb = amb;
        this.tarr = tarr;
        this.ambCreado = ambCreado;
        this.noPos = noPos;
        this.listaPertence = listaPertence;
        this.rango = rango;
        this.avance = avance;
        this.llave = llave;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTarr() {
        return tarr;
    }

    public void setTarr(String tarr) {
        this.tarr = tarr;
    }

    public String getAmbCreado() {
        return ambCreado;
    }

    public void setAmbCreado(String ambCreado) {
        this.ambCreado = ambCreado;
    }

    public String getNoPos() {
        return noPos;
    }

    public void setNoPos(String noPos) {
        this.noPos = noPos;
    }

    public String getListaPertence() {
        return listaPertence;
    }

    public void setListaPertence(String listaPertence) {
        this.listaPertence = listaPertence;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getAvance() {
        return avance;
    }

    public void setAvance(String avance) {
        this.avance = avance;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    

}
