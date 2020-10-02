
public class Error {
    private int estado;
    private String descripcion;
    private String lexema;
    private String tipo;
    private int linea;

    public Error(int estado, String descripcion, String lexema, int linea, String tipo) {
        this.estado = estado;
        this.descripcion = descripcion;
        this.lexema = lexema;
        this.linea = linea;
        this.tipo=tipo;
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }
    
}
