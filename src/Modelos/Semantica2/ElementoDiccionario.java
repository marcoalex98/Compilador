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
public class ElementoDiccionario {
    private String llave; //El contenido de la llave
    private int tokenLlave;    //El token del tipo de llave (id, cadena, entero...)
    private int ambito;        //El ambito que creo el diccionario
    private String valor;      //El contenido del valor, si es un diccionario, el valor se pone como diccionario
    private int tokenValor;    //El token del tipo del valor (id, cadena, entero...)
    private ArrayList<ElementoDiccionario> elementosHijos; //En caso de tener un diccionario dentro, se agregan los elementos hijos como valor

    public ElementoDiccionario(String valorLlave, int tokenLlave, int ambito, String valor, int tokenValor, ArrayList<ElementoDiccionario> elementosHijos) {
        this.llave = valorLlave;
        this.tokenLlave = tokenLlave;
        this.ambito = ambito;
        this.valor = valor;
        this.tokenValor = tokenValor;
        this.elementosHijos = elementosHijos;
    }
    
    public String getLlave() {
        return llave;
    }

    public void setLlave(String valorLlave) {
        this.llave = valorLlave;
    }

    public int getTokenLlave() {
        return tokenLlave;
    }

    public void setTokenLlave(int tokenLlave) {
        this.tokenLlave = tokenLlave;
    }

    public int getAmbito() {
        return ambito;
    }

    public void setAmbito(int ambito) {
        this.ambito = ambito;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getTokenValor() {
        return tokenValor;
    }

    public void setTokenValor(int tokenValor) {
        this.tokenValor = tokenValor;
    }

    public ArrayList<ElementoDiccionario> getElementosHijos() {
        return elementosHijos;
    }
    
    public void setArrayList(){
        this.elementosHijos = new ArrayList<>();
    }

    public void setElementosHijos(ElementoDiccionario elementosHijos) {
        this.elementosHijos.add(elementosHijos);
    }
    
}
