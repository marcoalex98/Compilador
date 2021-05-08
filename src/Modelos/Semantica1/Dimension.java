/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos.Semantica1;

/**
 *
 * @author marco
 */
public class Dimension {
    int posicion;
    int inicio;
    int fin;

    public Dimension(int posicion, int inicio, int fin) {
        this.posicion = posicion;
        this.inicio = inicio;
        this.fin = fin;
    }
    
    public boolean perteneceAlRango(int numero){
        return numero >= inicio && numero <= fin;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }
    
    
}
