/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Modelos.ReglaSemantica2;
import java.util.ArrayList;

/**
 *
 * @author marco
 */
public class Semantica2 {
    ArrayList<ReglaSemantica2> reglas;

    public Semantica2() {
        inicializarVariables();
    }
    
    private void inicializarVariables(){
        reglas = new ArrayList<>();
    }
    
    public void agregarRegla(int regla, int linea, int ambito, boolean aceptada){
        reglas.add(new ReglaSemantica2(regla, linea, ambito, aceptada));
    }
    
    public void imprimirReglas(){
        reglas.forEach((n) -> System.err.println(n.getRegla() + "\t" + n.getLinea() + "\t" + n.getAmbito() + "\t" + n.isAceptada()));
    }
}
