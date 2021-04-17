/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Analizador.Auxiliar.AuxiliarSemantica1;
import Modelos.OperandosOperadores;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author marco
 */
public class Semantica1 {

    AuxiliarSemantica1 auxiliar;
    Stack<OperandosOperadores> operadores;
    Stack<OperandosOperadores> operandos;
    String urlLog;
    int listaOperandos[] = {-1, -4, -7, -8, -9, -10, -12, -81, -82};
    //                        +     -    *    /    =
    int listaOperadores[] = {-14, -17, -20, -24, -42};

    public Semantica1(String urlLog) {
        this.urlLog = urlLog;
        auxiliar = new AuxiliarSemantica1();
        inicializarVariables();
    }

    private void inicializarVariables() {
        operandos = new Stack<OperandosOperadores>();
        operadores = new Stack<OperandosOperadores>();
    }

    public void iniciarSemantica1() {
        crearArchivoLog();
        cicloSemantica1();
    }

    private void cicloSemantica1() {
        try {
            int contadorCiclo = 0;
            String valorA = asignarValorOperando();
            String valorB = asignarValorOperando();
            String operacion = asignarValorOperador();
            valorA = auxiliar.getRelacion(operacion, valorA, valorB);
            System.out.println("<SEMANTICA 1> valorA " + valorA);
            System.out.println("<SEMANTICA 1> valorB " + valorB);
            while (operandos.empty() == false || operadores.empty() == false) {
                System.out.println("---------------INICIO DE CICLO SEMANTICA 1, VUELTA: " + contadorCiclo + "---------------");
                if (operadores.peek().getToken() == -42){ // Se ha encontrado un igual (=)
                    System.out.println("<SEMANTICA 1> Se ha encontrado un igual");
                    System.out.println("<SEMANTICA 1> Valor final: "+valorA);
                    operadores.pop();
                }
                else{
                    if(operandos.empty() == false){
                        valorB = asignarValorOperando();
                        if(operadores.empty() == false){
                            operacion = asignarValorOperador();
                            System.out.println("<SEMANTICA 1> Operacion " + operacion);
                        }
                    }
                    valorA = auxiliar.getRelacion(operacion, valorA, valorB);
                }
                System.out.println("---------------FIN DE CICLO SEMANTICA 1, VUELTA: " + contadorCiclo++ + "---------------\n");
            }
        } catch (Exception e) {
            System.err.println("<SEMANTICA 1> Ha ocurrido una excepcion en semantica 1");
            System.out.println("<SEMANTICA 1> Ha ocurrido una excepcion en semantica 1");
            System.err.println(e);
            System.out.println(e);
        }

    }

    private String asignarValorOperador() {
        if (!operadores.empty()) {
            String valor = auxiliar.getTipoOperador(operadores.peek().getToken());
            operadores.pop();
            return valor;
        }
        return "";
    }

    private String asignarValorOperando() {
        if (!operandos.empty()) {
            String valor = auxiliar.getTipoConstante(operandos.peek().getToken());
            operandos.pop();
            return valor;
        }
        return "";
    }

    public void agregarOperando(int operando, int linea, String lexema) {
        if (auxiliar.verificarExistencia(operando, listaOperandos)) {
            System.out.println("<SEMANTICA 1> Se ha agregado el operando " + operando);
            operandos.push(new OperandosOperadores(linea, operando, lexema));
        }
    }

    public void agregarOperador(int operador, int linea, String lexema) {
        if (auxiliar.verificarExistencia(operador, listaOperadores)) {
            System.err.println("<SEMANTICA 1> Se ha agregado el operador " + operador);
            operadores.push(new OperandosOperadores(linea, operador, lexema));
        }
    }

    private void crearArchivoLog() {
        File file = new File("logs/" + urlLog + "/" + urlLog + "-Semantica1.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream out = new PrintStream(fos);
            System.setOut(out);
        } catch (FileNotFoundException e) {

        }
    }
}
