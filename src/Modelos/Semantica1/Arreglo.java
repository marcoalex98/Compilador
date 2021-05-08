/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos.Semantica1;

import java.util.ArrayList;

/**
 *
 * @author marco
 */
public class Arreglo {
    String id;
    String ambito;
    ArrayList<Dimension> dimensiones;
    int incremento;
    
    public Arreglo(String id, String ambito, ArrayList<Dimension> dimensiones, int incremento) {
        this.id = id;
        this.ambito = ambito;
        this.dimensiones = dimensiones;
        this.incremento = incremento;
    }
    
    public void agregarDimension(int posicion, int inicio, int fin){
        this.dimensiones.add(new Dimension(posicion, inicio, fin));
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public ArrayList<Dimension> getDimensiones() {
        return dimensiones;
    }

    public int getIncremento() {
        return incremento;
    }

    public void setIncremento(int incremento) {
        this.incremento = incremento;
    }   
}
