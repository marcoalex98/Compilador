package Estructuras;

public class Token {
    private int estado;
    private String lexema;
    private int linea;

    public Token(int estado, String lexema, int linea) {
        this.estado = estado;
        this.lexema = lexema;
        this.linea = linea;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
