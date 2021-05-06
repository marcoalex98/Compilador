/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Modelos.Semantica2.Reglas;
import java.util.ArrayList;

/**
 *
 * @author marco
 */
public class Semantica2 {
    Semantica1 analizadorSemantica1;
    ArrayList<Reglas> reglas;

    public Semantica2() {
        inicializarVariables();
    }
    
    public boolean analizarCimaPila(int peek, int linea, int ambito){
        if (peek == 1010 || peek == 1011 || peek == 1012) { //IF WHILE ELIF
            if (analizadorSemantica1.ejecutarOperacionSemantica2().equals("booleano")) {
                agregarRegla(peek, linea, ambito, true);
            }else{
                agregarRegla(peek, linea, ambito, false);
            } 
            return true;
        }
        return false;
    }
    
    private void inicializarVariables(){
        reglas = new ArrayList<>();
    }
    
    public void agregarRegla(int regla, int linea, int ambito, boolean aceptada){
        reglas.add(new Reglas(regla, linea, ambito, aceptada));
    }
    
    public void imprimirReglas(){
        reglas.forEach((n) -> System.err.println(n.getRegla() + "\t" + n.getLinea() + "\t" + n.getAmbito() + "\t" + n.isAceptada()));
    }
    
    public void setAnalizadorSemantica1(Semantica1 analizadorSemantica1){
        this.analizadorSemantica1 = analizadorSemantica1;
    }
}
