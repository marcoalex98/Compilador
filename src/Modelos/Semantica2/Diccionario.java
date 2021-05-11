/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos.Semantica2;

import java.util.ArrayList;

/**
 *
 * @author marco
 */
public class Diccionario {
    private int tipoLlave; //El token que deben utilizar todas las llaves, se asigna con la llave del primer elemento
    private ArrayList<ElementoDiccionario> elementos; //Los elementos del diccionario
    private int ambito;
    private int linea;

    public Diccionario(int tipoLlave, ArrayList<ElementoDiccionario> elementos, int ambito, int linea) {
        this.tipoLlave = tipoLlave;
        this.elementos = elementos;
        this.ambito = ambito;
        this.linea = linea;
    }

    public int getTipoLlave() {
        return tipoLlave;
    }

    public void setTipoLlave(int tipoLlave) {
        this.tipoLlave = tipoLlave;
    }

    public ArrayList<ElementoDiccionario> getElementos() {
        return elementos;
    }

    public void setElementos(ArrayList<ElementoDiccionario> elementos) {
        this.elementos = elementos;
    }

    public int getAmbito() {
        return ambito;
    }

    public void setAmbito(int ambito) {
        this.ambito = ambito;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }
    
    
}
