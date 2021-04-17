/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Controladores;

import Modelos.Diccionario;

/**
 *
 * @author marco
 */
public class ControladorDatoDiccionario {
    private Diccionario[] datosDiccionario;
    
    public ControladorDatoDiccionario(){
        this.datosDiccionario = new Diccionario[0];
    }
    
    public void push(String tipo, String amb, String valor, String noPos, String llave,
            String listaPertenece){
        aumentarArreglo();
        datosDiccionario[datosDiccionario.length-1] = new Diccionario(tipo, 
                "datoDiccionario", amb, valor, noPos, llave, listaPertenece);
    }
    
    public Diccionario[] obtenerDatosDiccionario(){
        return datosDiccionario;
    }
    
    public void vaciarDatosDiccionario(){
        this.datosDiccionario = new Diccionario[0];
    }
    
    public int obtenerCantidadDatosDiccionario(){
        return datosDiccionario.length;
    }
    
    private void aumentarArreglo(){
        Diccionario auxiliar[] = datosDiccionario;
        datosDiccionario = new Diccionario[datosDiccionario.length+1];
        for(int i = 0; i < auxiliar.length; i++){
            datosDiccionario[i] = auxiliar[i];
        }
    }
}
