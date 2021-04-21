/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Analizador.Auxiliar.AuxiliarSemantica1;
import Controladores.ControladorTokenError;
import Modelos.OperandosOperadores;
import SQL.ControladorSQL;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Semantica1 {

    AuxiliarSemantica1 auxiliar;
    Stack<OperandosOperadores> operadores;
    Stack<OperandosOperadores> operandos;
    ControladorSQL controladorSQL;
    ControladorTokenError controladorTokenError;
    String variable;
    int lineaVariable;
    //                      ID   
    int listaOperandos[] = {-1, -4, -7, -8, -9, -10, -12, -81, -82};
    //                        +     -    *    /    =
    int listaOperadores[] = {-14, -17, -20, -24, -42};
    //                         TD    TF
    int listaTemporales[] = {-400, -401};

    public Semantica1(ControladorSQL controladorSQL, ControladorTokenError controladorTokenError) {
        auxiliar = new AuxiliarSemantica1();
        this.controladorSQL = controladorSQL;
        this.controladorTokenError = controladorTokenError;
        inicializarVariables();
    }

    private void inicializarVariables() {
        operandos = new Stack<>();
        operadores = new Stack<>();
    }

    public void ejecutarOperacion() {
        System.err.println("");
        System.err.println("<SEMANTICA 1 ejecutarOperacion> Ejecutar operacion");
        System.err.println("<SEMANTICA 1 ejecutarOperacion> variable: "+variable);
        String valorA = asignarValorOperando();
        OperandosOperadores auxiliarOperando = operandos.peek();
        String valorB = asignarValorOperando();
        String operacion = asignarValorOperador();
        System.err.println("<SEMANTICA 1 ejecutarOperacion> operacion: " + operacion);
        System.err.println("<SEMANTICA 1 ejecutarOperacion> valorA: " + valorA);
        System.err.println("<SEMANTICA 1 ejecutarOperacion> valorB: " + valorB);
        int temporal = auxiliar.getTokenTemporal(auxiliar.getRelacion(operacion, valorA, valorB));
        System.err.println("<SEMANTICA 1 ejecutarOperacion> temporal: " + temporal);
        if(temporal == 950){
            controladorTokenError.agregarError(950, valorA+" no es compatible con "+valorB, "", 
                    auxiliarOperando.getLinea(), "Semantica 1");
            agregarTemporal(-414, lineaVariable, "variant");
        }
        else {
            agregarTemporal(temporal, lineaVariable, "Temporal");
        }
        System.err.println("");
    }

    private void agregarTemporal(int temporal, int linea, String lexema) {
        System.out.println("<SEMANTICA 1> Se ha agregado el temporal " + temporal);
        operandos.push(new OperandosOperadores(linea, temporal, lexema));
    }

    public void comprobarAsignacion() {
        System.err.println("\n---------------<SEMANTICA 1> Comprobar Asignacion---------------");
        String idAuxiliar = variable.replaceAll("\\s", "");
        String claseVariable = "";
        String query = "SELECT clase FROM tablasimbolos WHERE id = BINARY '" + idAuxiliar + "'";
        try {
            ResultSet rs = controladorSQL.obtenerResultSet(query);
            while (rs.next()) {
                claseVariable = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Semantica1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.err.println("<SEMANTICA 1> Comprobar Asignacion");
        System.err.println("<SEMANTICA 1> Nombre Variable: " + idAuxiliar);
        System.err.println("<SEMANTICA 1> Clase Variable: " + claseVariable);
        System.err.println("<SEMANTICA 1> Clase operando.peek: " + auxiliar.getTipoConstante(operandos.peek().getToken()));

        String valor = auxiliar.getTipoConstante(operandos.peek().getToken());
        if(!auxiliar.getAsignacion(claseVariable, valor).equals("Error")){
            System.err.println("<SEMANTICA 1> La operacion de la variable "+idAuxiliar+" es aceptable");
        } else {
            controladorTokenError.agregarError(951, "No se puede asignar un "+valor+" en la variable "+idAuxiliar, "", 
                    operandos.peek().getLinea(), "Semantica 1");
            System.err.println("<SEMANTICA 1> La operacion de la variable "+idAuxiliar+" no es aceptable");
        }
        System.err.println("---------------<SEMANTICA 1> Fin Comprobar Asignacion---------------\n");
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
            lineaVariable = operandos.peek().getLinea();
            System.out.println("<SEMANTICA 1> asignarValorOperando, "
                    + "operandos.peek.getToken:" + operandos.peek().getToken() + ", valor: " + valor);
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
            System.out.println("<SEMANTICA 1> Se ha agregado el operador " + operador);
            operadores.push(new OperandosOperadores(linea, operador, lexema));
        }
    }

    public void agregarNombreVariable(String nombre) {
        this.variable = nombre;
    }
}
