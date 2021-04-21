/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Analizador.Auxiliar.AuxiliarSemantica1;
import Controladores.ControladorTokenError;
import Modelos.Operadores;
import Modelos.Operando;
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
    Stack<Operadores> operadores;
    Stack<Operando> operandos;
    ControladorSQL controladorSQL;
    ControladorTokenError controladorTokenError;
    //                      ID   
    int listaOperandos[] = {-6, -1, -4, -7, -8, -9, -10, -12, -81, -82};
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
        Operando valorA = asignarValorOperando();
        Operando auxiliarOperando = operandos.peek();
        Operando valorB = asignarValorOperando();
        String operacion = asignarValorOperador();
        System.err.println("<SEMANTICA 1 ejecutarOperacion> operacion: " + operacion);
        System.err.println("<SEMANTICA 1 ejecutarOperacion> valorA: " + valorA.getClase());
        System.err.println("<SEMANTICA 1 ejecutarOperacion> valorB: " + valorB.getClase());

        int temporal;
        if (valorA.getToken() == -6 && valorB.getToken() == -6) {
            temporal = auxiliar.getTokenTemporal(auxiliar.getRelacion(operacion, valorA.getClase(), valorB.getClase()));
        } else if (valorA.getToken() == -6) {
            temporal = auxiliar.getTokenTemporal(auxiliar.getRelacion(operacion, valorA.getClase(),
                    auxiliar.getTipoConstante(valorB.getToken())));
        } else if (valorB.getToken() == -6) {
            temporal = auxiliar.getTokenTemporal(auxiliar.getRelacion(operacion, auxiliar.getTipoConstante(valorA.getToken()),
                    valorB.getClase()));
        } else {
            temporal = auxiliar.getTokenTemporal(auxiliar.getRelacion(operacion, auxiliar.getTipoConstante(valorA.getToken()),
                    auxiliar.getTipoConstante(valorB.getToken())));
        }
        System.err.println("<SEMANTICA 1 ejecutarOperacion> temporal: " + temporal);
        if (temporal == 950) {
            controladorTokenError.agregarError(950, valorA + " no es compatible con " + valorB, "",
                    auxiliarOperando.getLinea(), "Semantica 1");
            agregarTemporal(valorB.getLinea(), -414, 0, valorB.getLexema(), "variant");
        } else {
            agregarTemporal(valorB.getLinea(), temporal, valorB.getAmbito(), "Temporal", valorB.getClase());
        }
        System.err.println("");
    }

    private void agregarTemporal(int linea, int temporal, int ambito, String lexema, String clase) {
        System.out.println("<SEMANTICA 1> Se ha agregado el temporal " + temporal);
        operandos.push(new Operando(linea, temporal, ambito, lexema, clase));
    }

    private String obtenerClaseVariable(String variable, int ambito) {
        String query = "SELECT clase FROM tablasimbolos WHERE (id = BINARY '" + variable + "' AND ambito = '"+ambito+"')";
        String claseVariable = "";
        try {
            ResultSet rs = controladorSQL.obtenerResultSet(query);
            while (rs.next()) {
                claseVariable = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Semantica1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.err.println("<SEMANTICA 1 obtenerClaseVariable> query: "+query);
        System.err.println("<SEMANTICA 1 obtenerClaseVariable> clase: "+claseVariable);
        return claseVariable;
    }

    public void comprobarAsignacion() {
        System.err.println("\n---------------<SEMANTICA 1> Comprobar Asignacion---------------");
        String valorTemporal = auxiliar.getTipoConstante(operandos.peek().getToken());
        operandos.pop();
        Operando variable = operandos.peek();
        operandos.pop();
        String claseVariable = obtenerClaseVariable(variable.getLexema(), variable.getAmbito());
        System.err.println("<SEMANTICA 1> Comprobar Asignacion");
        System.err.println("<SEMANTICA 1> Nombre Variable: " + variable.getLexema());
        System.err.println("<SEMANTICA 1> Clase Variable: " + variable.getClase());
        if (!auxiliar.getAsignacion(claseVariable, valorTemporal).equals("Error")) {
            System.err.println("<SEMANTICA 1> La operacion de la variable " + variable.getLexema() + " es aceptable");
        } else {
            controladorTokenError.agregarError(951, "No se puede asignar un " + valorTemporal + " en la variable " + variable.getLexema(), "",
                    variable.getLinea(), "Semantica 1");
            System.err.println("<SEMANTICA 1> La operacion de la variable " + variable.getLexema() + " no es aceptable");
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

    private Operando asignarValorOperando() {
        if (!operandos.empty()) {
            Operando oper = operandos.peek();
            System.out.println("<SEMANTICA 1> asignarValorOperando, "
                    + "operandos.peek.getToken:" + operandos.peek().getToken() + ", valor: " + auxiliar.getTipoConstante(oper.getToken()));
            operandos.pop();
            return oper;
        }
        return null;
    }

    public void agregarOperando(int operando, int linea, String lexema, int ambito) {
        String idAuxiliar = lexema.replaceAll("\\s", "");
        String clase = "";
        if (operando == -6) {
            String query = "SELECT clase FROM tablasimbolos WHERE (id = BINARY '" + idAuxiliar + "'"
                    + "AND ambito = '" + ambito + "')";
            try {
                ResultSet rs = controladorSQL.obtenerResultSet(query);
                while (rs.next()) {
                    clase = rs.getString(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Semantica1.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (auxiliar.verificarExistencia(operando, listaOperandos)) {
                System.err.println("<SEMANTICA 1> Se ha agregado el operando " + operando);
                operandos.push(new Operando(linea, operando, ambito, idAuxiliar, clase));
            }
        } else {
            if (auxiliar.verificarExistencia(operando, listaOperandos)) {
                System.err.println("<SEMANTICA 1> Se ha agregado el operando " + operando);
                operandos.push(new Operando(linea, operando, ambito, idAuxiliar, auxiliar.getTipoConstante(operando)));
            }
        }
    }

    public void agregarOperador(int operador, int linea, String lexema) {
        if (auxiliar.verificarExistencia(operador, listaOperadores)) {
            System.err.println("<SEMANTICA 1> Se ha agregado el operador " + operador);
            operadores.push(new Operadores(linea, operador, lexema));
        }
    }
}
