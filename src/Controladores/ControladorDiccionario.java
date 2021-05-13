/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Semantica2.Diccionario;
import Modelos.Semantica2.ElementoDiccionario;
import Modelos.Semantica2.Reglas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 *
 * @author marco
 */
public class ControladorDiccionario {
    HashMap<String, Diccionario> diccionarios;
    ArrayList<Reglas> reglas;
    
    public ControladorDiccionario(HashMap<String, Diccionario> diccionarios, ArrayList<Reglas> reglas){
        this.diccionarios = diccionarios;
        this.reglas = reglas;
    }
    
    public void agregarNuevoDiccionario(String id, int ambito, int linea){
        this.diccionarios.put(id, new Diccionario(0, null, ambito, linea));
        diccionarios.get(id).setElementos(new ArrayList<>());
    }
    
    public void agregarElementoDiccionario(String id, String llave, int tokenLlave, String valor, int tokenValor){
        diccionarios.get(id).addElemento(new ElementoDiccionario(llave, tokenLlave, 
                diccionarios.get(id).getAmbito(), valor, tokenValor, null));
        if (diccionarios.get(id).getTipoLlave() == 0) {
            diccionarios.get(id).setTipoLlave(tokenLlave);
        }
    }
    
    //Agregar un diccionario dentro de un diccionario
    public void agregarDiccionarioLopez(String id, String llave, int tokenLlave, 
            String llaveHijo, int tokenLlaveHijo, String valorHijo, int tokenValorHijo){
        diccionarios.get(id).getElementos().add(new ElementoDiccionario(
                llave,
                tokenLlave, 
                diccionarios.get(id).getAmbito(), 
                "DiccionarioLopez", 
                -4, 
                null));
        if(diccionarios.get(id).getElementos().get(diccionarios.get(id).getElementos().size()-1).getElementosHijos() == null){
            diccionarios.get(id).getElementos().get(diccionarios.get(id).getElementos().size()-1).crearArrayListElementosHijos();
        }
        diccionarios.get(id).getElementos().get(diccionarios.get(id).getElementos().size()-1).getElementosHijos().add(
            new ElementoDiccionario(valorHijo, tokenLlaveHijo, diccionarios.get(id).getAmbito(), valorHijo, tokenValorHijo, null));
    }
    
    
    public void verificarReglas(){
        verificarRegla1060();
        verificarRegla1061();
    }
    
    private void verificarRegla1061(){
        ArrayList<Diccionario> listOfValues = diccionarios.values().stream().collect(
                Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < listOfValues.size(); i++) {
            agregarRegla(1061, listOfValues.get(i).getLinea(), listOfValues.get(i).getAmbito(), true);
        }
    }
    
    //Todas las llaves del diccionario deben ser del mismo tipo
    private void verificarRegla1060(){
        ArrayList<Diccionario> listOfValues = diccionarios.values().stream().collect(
                Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < listOfValues.size(); i++) {
            boolean regla1060 = true;
            for (int j = 0; j < listOfValues.get(i).getElementos().size(); j++) {
                if(listOfValues.get(i).getElementos().get(j).getTokenLlave() != 
                        listOfValues.get(i).getTipoLlave())
                    regla1060 = false;
            }
            agregarRegla(1060, listOfValues.get(i).getLinea(), listOfValues.get(i).getAmbito(), regla1060);
        }
    }
    
    private void agregarRegla(int regla, int linea, int ambito, boolean aceptada){
        reglas.add(new Reglas(regla, linea, ambito, aceptada));
    }
    
    public void imprimirDiccionarios(){
        ArrayList<String> listOfKeys = diccionarios.keySet().stream().collect(
                Collectors.toCollection(ArrayList::new));
        ArrayList<Diccionario> listOfValues = diccionarios.values().stream().collect(
                Collectors.toCollection(ArrayList::new));
        System.err.println("\nID\t\t\tLlave\t\t\tValor\t\t\tTiene Hijos\t\t\tLlave Hijo\t\t\tValor Hijo");
        for (int i = 0; i < listOfValues.size(); i++) {
            System.err.print(listOfKeys.get(i) + "\t\t\t");
            for (int j = 0; j < listOfValues.get(i).getElementos().size(); j++) {
                System.err.print(listOfValues.get(i).getElementos().get(j).getLlave() + "\t\t\t");
                System.err.print(listOfValues.get(i).getElementos().get(j).getValor() + "\t\t\t");
                if (listOfValues.get(i).getElementos().get(i).getElementosHijos() != null) {
                    System.err.print("true\t\t\t");
                    for (int k = 0; k < listOfValues.get(i).getElementos().get(j).getElementosHijos().size(); k++) {
                        System.err.print(listOfValues.get(i).getElementos().get(j).getElementosHijos().get(k).getLlave() + "\t\t\t\t");
                        System.err.print(listOfValues.get(i).getElementos().get(j).getElementosHijos().get(k).getValor() + "\n\t\t\t\t\t\t\t\t\t\t\t");
                    }
                } else {
                    System.err.print("false");
                }
                System.err.print("\n\t\t\t");
            }
            System.err.println();
        }
    }
}
