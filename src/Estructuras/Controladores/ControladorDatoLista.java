/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Controladores;

import Modelos.Lista;

/**
 *
 * @author marco
 */
public class ControladorDatoLista {
    private Lista[] datosLista;
    
    public ControladorDatoLista(){
        this.datosLista = new Lista[0];
    }
    
    public void push(String tipo, String amb, String noPos, String listaPertenece){
        aumentarArreglo();
        datosLista[datosLista.length-1] = new Lista(tipo, "datoLista", amb, noPos, 
                listaPertenece);
    }
    
    public Lista[] obtenerDatosLista(){
        return datosLista;
    }
    
    public void vaciarDatosLista(){
        this.datosLista = new Lista[0];
    }
    
    public int obtenerCantidadDatosLista(){
        return datosLista.length;
    }
    
    private void aumentarArreglo(){
        Lista auxiliar[] = datosLista;
        datosLista = new Lista[datosLista.length+1];
        for(int i = 0; i < auxiliar.length; i++){
            datosLista[i] = auxiliar[i];
        }
    }
}
