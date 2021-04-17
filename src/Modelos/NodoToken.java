package Modelos;


public class NodoToken {
    private int posicion;
    private String lexema;
    private int numLinea;
    NodoToken sig;
    
    public NodoToken(int posicion, String minilexem, int numlinea){
        this.posicion=posicion;
        this.lexema=minilexem;
        this.numLinea=numlinea;
        this.sig=null;
    }

    
    public int getPosicion() {
        return posicion;
    }

   
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

  
    public String getLexema() {
        return lexema;
    }

  
    public void setLexema(String minilexem) {
        this.lexema = minilexem;
    }

   
    public int getNumlinea() {
        return numLinea;
    }

    public void setNumlinea(int numlinea) {
        this.numLinea = numlinea;
    }

    /**
     * @return the sig
     */
    public NodoToken getSig() {
        return sig;
    }

    /**
     * @param sig the sig to set
     */
    public void setSig(NodoToken sig) {
        this.sig = sig;
    }
    
    
    
    
    
    
    
}
