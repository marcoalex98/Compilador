/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Modelos.NodoToken;
import Modelos.Semantica1.Arreglo;
import Modelos.Semantica1.Dimension;
import Modelos.Semantica2.Reglas;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author marco
 */
public class Semantica2 {

    Semantica1 analizadorSemantica1;
    ArrayList<NodoToken> elementosArreglo;
    ArrayList<Dimension> dimensionesArreglo;
    ArrayList<Reglas> reglas;
    HashMap<String, Arreglo> arreglos;
    boolean banderaArregloNormal, establecerTamanoDimension;

    public Semantica2(HashMap<String, Arreglo> arreglos) {
        this.arreglos = arreglos;
        inicializarVariables();
    }

    public void comprobarDimencionesArreglo(String variableArreglo, int ambitoArreglo,
            int numeroDimensiones, int linea) {//Regla Arreglos 1 - 1030
        System.err.println(numeroDimensiones);
        System.err.println(arreglos.get((variableArreglo + ambitoArreglo)).getDimensiones().size());

        if (arreglos.get((variableArreglo + ambitoArreglo)).getDimensiones().size() < numeroDimensiones) {
            agregarRegla(1030, linea, ambitoArreglo, false);
        } else {
            agregarRegla(1030, linea, ambitoArreglo, true);
        }
    }

    //Reglas pertenecientes a if, while y elif
    public boolean analizarReglas(int peek, int linea, int ambito) {
        if (peek == 1010 || peek == 1011 || peek == 1012) { //IF WHILE ELIF
            if (analizadorSemantica1.ejecutarOperacionSemantica2().equals("booleano")) {
                agregarRegla(peek, linea, ambito, true);
            } else {
                agregarRegla(peek, linea, ambito, false);
            }
            return true;
        }
        return false;
    }

    public boolean analizarArreglo(int peek, boolean areaDeclaracion, NodoToken oper, int ambito) {
        if ((peek == 10301 || peek == 10302 || peek == 10303) && areaDeclaracion) {
            if (peek == -6 || peek == -7 || peek == 10301 || peek == 10302 || peek == 10303) {
                switch (peek) {
                    case 10302:
                        //Arreglo con :
                        banderaArregloNormal = false;
                        elementosArreglo.add(oper);
                        return true;
                    case 10303:
                        //Agregar elemento al arreglo
                        if (!establecerTamanoDimension) {
                            elementosArreglo.add(oper);
                        } else {
                            dimensionesArreglo.add(new Dimension(dimensionesArreglo.size(),
                                    0,
                                    Integer.parseInt(elementosArreglo.get(elementosArreglo.size() - 1).getLexema()) - 1));
                            elementosArreglo.clear();
                            elementosArreglo.add(oper);
                        }
                        return true;
                    case 10301:
                        //Agregar dimensión
                        dimensionesArreglo.add(new Dimension(dimensionesArreglo.size(),
                                0,
                                Integer.parseInt(elementosArreglo.get(elementosArreglo.size() - 1).getLexema()) - 1));

                        elementosArreglo = new ArrayList<>();
                        elementosArreglo.add(oper);
                        establecerTamanoDimension = true;
                        return true;
                    default:
                        break;
                }
            }else{
//                agregarRegla(1040, oper.getNumlinea(), ambito, false);
            }
        }
        return false;
    }

    //Agregar arreglo a diccionario
    public void asignarArreglo(String nombreVariable, String ambitoVariable, int linea, int ambito) {
        if (banderaArregloNormal) {
            System.err.println(elementosArreglo.size());
            if (elementosArreglo.size() > 0 && !establecerTamanoDimension) {
                dimensionesArreglo.add(new Dimension(dimensionesArreglo.size(), 0, elementosArreglo.size() - 1));
            } else if (elementosArreglo.size() > 0 && establecerTamanoDimension) {
                dimensionesArreglo.add(new Dimension(dimensionesArreglo.size(), 0, Integer.parseInt(elementosArreglo.get(0).getLexema()) - 1));
            }
            arreglos.put(nombreVariable + ambitoVariable,
                    new Arreglo(nombreVariable, ambitoVariable, dimensionesArreglo, 1));
        } else { //Arreglos con : y avance
            ArrayList<Dimension> dimension = new ArrayList<>();
            int inicio = Integer.parseInt(elementosArreglo.get(0).getLexema());
            int fin = Integer.parseInt(elementosArreglo.get(1).getLexema());
            int avance = Integer.parseInt(elementosArreglo.get(2).getLexema());
            dimension.add(new Dimension(0,
                    Integer.parseInt(elementosArreglo.get(0).getLexema()),
                    Integer.parseInt(elementosArreglo.get(1).getLexema()) - 1));
            
            arreglos.put(nombreVariable + ambitoVariable,
                    new Arreglo(nombreVariable, ambitoVariable,
                            dimension,
                            Integer.parseInt(elementosArreglo.get(2).getLexema())));
            if (inicio > fin) {
                agregarRegla(1031, linea, ambito, true);
            } else {
                agregarRegla(1031, linea, ambito, false);
            }
        }
        elementosArreglo.clear();
        dimensionesArreglo = new ArrayList<>();
        banderaArregloNormal = true;
        establecerTamanoDimension = false;
    }

    private void inicializarVariables() {
        elementosArreglo = new ArrayList<>();
        dimensionesArreglo = new ArrayList<>();
        reglas = new ArrayList<>();
        banderaArregloNormal = true;
        establecerTamanoDimension = false;
    }

    public void agregarRegla(int regla, int linea, int ambito, boolean aceptada) {
        reglas.add(new Reglas(regla, linea, ambito, aceptada));
    }

    public void imprimirReglas() {
        reglas.forEach((n) -> System.err.println(n.getRegla() + "\t" + n.getLinea() + "\t" + n.getAmbito() + "\t" + n.isAceptada()));
    }

    public void setAnalizadorSemantica1(Semantica1 analizadorSemantica1) {
        this.analizadorSemantica1 = analizadorSemantica1;
    }
}
