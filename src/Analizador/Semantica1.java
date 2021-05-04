/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Analizador.Auxiliar.AuxiliarSemantica1;
import Contadores.ContadorSemantica1;
import Controladores.ControladorTokenError;
import Modelos.NodoToken;
import Modelos.OperToken;
import Modelos.Operadores;
import Modelos.Operando;
import SQL.ControladorSQL;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    Semantica2 analizadorSemantica2;
    Stack<Operadores> pilaOperadores;
    Stack<Operando> pilaOperandos;
    ArrayList<Operadores> operadores;
    ArrayList<Operando> operandos;
    ControladorSQL controladorSQL;
    public ContadorSemantica1[] contadores;

    ControladorTokenError controladorTokenError;
    PrintStream sys = System.out;
    //                      ID   
    int listaOperandos[] = {-6, -1, -4, -7, -8, -9, -10, -12, -81, -82};
    //                         >    +    -    *    /    %    =    ^   
    int listaOperadores[] = {-39, -14, -17, -20, -24, -28, -42, -44, -33, -32, -34, -30, -37, -117,
        //    +=   -=   *=   /=   <<   >>   ++   --    <   <=    !=  >=   IS  ISNOT IN INNOT
        -16, -19, -23, -27, -37, -40, -15, -18, -36, -38, -43, -31, -41, -71, -72, -70, -98, -35};
    String listaAsignadores[] = {"=","+=","-=","*=","/="};
    String operadorAsignador;
    
    public Semantica1(ControladorSQL controladorSQL, ControladorTokenError controladorTokenError,
            Semantica2 analizadorSemantica2) {
        auxiliar = new AuxiliarSemantica1(sys);
        this.controladorSQL = controladorSQL;
        this.controladorTokenError = controladorTokenError;
        this.analizadorSemantica2 = analizadorSemantica2;
        inicializarVariables();
    }

    private void inicializarVariables() {
        operadores = new ArrayList<>();
        operandos = new ArrayList<>();
        pilaOperandos = new Stack<>();
        pilaOperadores = new Stack<>();
        operadorAsignador = "";
        contadores = new ContadorSemantica1[1];
        contadores[0] = new ContadorSemantica1();
    }

    public String ejecutarOperacionSemantica2() {
        Operando operandosArr[] = new Operando[pilaOperandos.size()];
        Operadores operadoresArr[] = new Operadores[pilaOperadores.size()];
        for (int i = pilaOperandos.size(); i > 0; i--) {
            operandosArr[i - 1] = pilaOperandos.peek();
            pilaOperandos.pop();
        }
        for (int i = pilaOperadores.size(); i > 0; i--) {
            operadoresArr[i - 1] = pilaOperadores.peek();
            pilaOperadores.pop();
        }
        operadores = new ArrayList<>();
        operandos = new ArrayList<>();
        operandos.addAll(Arrays.asList(operandosArr));
        operadores.addAll(Arrays.asList(operadoresArr));
        while (operandos.size() > 1) {
            int index = auxiliar.obtenerPosicionMayorOperador(operadores);
            Operando valorA = operandos.get(index);
            Operando valorB = operandos.get(index + 1);
            
            operandos.remove(index);
            sys.println("<SEMANTICA 1 ejecutarOperacionSemantica2> Se va a remover: " + operandos.get(index).getLexema());
            operandos.remove(index);
            Operadores operador = operadores.get(index);
            sys.println("<SEMANTICA 1 ejecutarOperacionSemantica2> Se va a remover: " + operadores.get(index).getLexema());
            operadores.remove(operador);
            String operacion = auxiliar.getTipoOperador(operador.getToken());
            int temporal;
            String relacion;
            if (valorA.getToken() == -6 && valorB.getToken() == -6) {
                sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> variable contra variable");
                relacion = auxiliar.getRelacion(operacion,
                        obtenerClaseVariable(valorA.getLexema(), valorA.getAmbito()),
                        obtenerClaseVariable(valorB.getLexema(), valorB.getAmbito()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            } else if (valorA.getToken() == -6) {
                sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> variable contra otra cosa");
                relacion = auxiliar.getRelacion(operacion,
                        obtenerClaseVariable(valorA.getLexema(), valorA.getAmbito()),
                        auxiliar.getTipoConstante(valorB.getToken()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            } else if (valorB.getToken() == -6) {
                sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> otra cosa contra variable");
                relacion = auxiliar.getRelacion(operacion,
                        auxiliar.getTipoConstante(valorA.getToken()),
                        obtenerClaseVariable(valorB.getLexema(), valorB.getAmbito()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            } else {
                sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> otra cosa contra otra cosa");
                relacion = auxiliar.getRelacion(operacion,
                        auxiliar.getTipoConstante(valorA.getToken()),
                        auxiliar.getTipoConstante(valorB.getToken()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            }
            if (temporal == 950) {
                agregarTemporal(valorB.getLinea(), -414, valorB.getAmbito(), "Temporal", "variant", index);
            } else {
                agregarTemporal(valorB.getLinea(), temporal, valorB.getAmbito(), "Temporal", relacion, index);
            }
        }
        return operandos.get(0).getClase();
    }

    public void ejecutarOperacion(Operando valorA, Operando valorB, String operacion, int index) {
        sys.println("");
        sys.println("@--------------------------------------Inicio Ejecutar operacion--------------------------------------");
//        Operando valorB = asignarValorOperando();
        if (contadores[contadores.length - 1].getLinea() == 0) {
            contadores[contadores.length - 1].setLinea(valorB.getLinea());
        } else if (contadores[contadores.length - 1].getLinea() != valorB.getLinea()) {
            aumentarArregloContadores();
            contadores[contadores.length - 1].setLinea(valorB.getLinea());
        }
        sys.println("    <SEMANTICA 1 ejecutarOperacion> operacion: " + operacion);
        sys.println("    <SEMANTICA 1 ejecutarOperacion> valorA: " + valorA.getLexema() + ", " + valorA.getClase() + ", " + valorA.getToken());
        sys.println("    <SEMANTICA 1 ejecutarOperacion> valorB: " + valorB.getLexema() + ", " + valorB.getClase() + ", " + valorB.getToken());
        int temporal;
        String relacion;
        if (valorA.getToken() == -6 && valorB.getToken() == -6) {
                sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> variable contra variable");
                relacion = auxiliar.getRelacion(operacion,
                        obtenerClaseVariable(valorA.getLexema(), valorA.getAmbito()),
                        obtenerClaseVariable(valorB.getLexema(), valorB.getAmbito()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            } else if (valorA.getToken() == -6) {
                sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> variable contra otra cosa");
                relacion = auxiliar.getRelacion(operacion,
                        obtenerClaseVariable(valorA.getLexema(), valorA.getAmbito()),
                        auxiliar.getTipoConstante(valorB.getToken()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            } else if (valorB.getToken() == -6) {
                sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> otra cosa contra variable");
                relacion = auxiliar.getRelacion(operacion,
                        auxiliar.getTipoConstante(valorA.getToken()),
                        obtenerClaseVariable(valorB.getLexema(), valorB.getAmbito()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            } else {
                sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> otra cosa contra otra cosa");
                relacion = auxiliar.getRelacion(operacion,
                        auxiliar.getTipoConstante(valorA.getToken()),
                        auxiliar.getTipoConstante(valorB.getToken()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            }
        sys.println("    <SEMANTICA 1 ejecutarOperacion> temporal: " + temporal);
        if (temporal == 950) {
            controladorTokenError.agregarError(950, valorB.getClase() + " no es compatible con "
                    + valorA.getClase() + " en la operacion " + operacion, valorB.getLexema() + ", " + valorA.getLexema(),
                    valorA.getLinea(), "Semantica 1");
            agregarTemporal(valorB.getLinea(), -414, valorB.getAmbito(), "Temporal", "variant", index);
        } else {
            agregarTemporal(valorB.getLinea(), temporal, valorB.getAmbito(), "Temporal", relacion, index);
        }
        sys.println("--------------------------------------FIN EJECUTAR OPERACION--------------------------------------");
    }

    private void agregarTemporal(int linea, int temporal, int ambito, String lexema, String clase, int index) {
        sys.println("<SEMANTICA 1 agregarTemporal> Se ha agregado el temporal " + temporal + ", " + clase + ", " + lexema);
        operandos.add(index, new Operando(linea, temporal, ambito, lexema, clase));
//        pilaOperandos.push(new Operando(linea, temporal, ambito, lexema, clase));
    }

    private void analizador() {
        Operando operandosArr[] = new Operando[pilaOperandos.size()];
        Operadores operadoresArr[] = new Operadores[pilaOperadores.size()];
        for (int i = pilaOperandos.size(); i > 0; i--) {
            operandosArr[i - 1] = pilaOperandos.peek();
            pilaOperandos.pop();
        }
        for (int i = pilaOperadores.size(); i > 0; i--) {
            operadoresArr[i - 1] = pilaOperadores.peek();
            pilaOperadores.pop();
        }
        operadores = new ArrayList<>();
        operandos = new ArrayList<>();
        operandos.addAll(Arrays.asList(operandosArr));
        operadores.addAll(Arrays.asList(operadoresArr));

        boolean banderaWhile = true;
        while (banderaWhile) {
            int index = auxiliar.obtenerPosicionMayorOperador(operadores);
            if (index == 0) {
                break;
            }
            Operando valorA = operandos.get(index);
            Operando valorB = operandos.get(index + 1);
            sys.println("<SEMANTICA 1 analizador> Se va a remover: " + operandos.get(index).getLexema());
            operandos.remove(index);
            sys.println("<SEMANTICA 1 analizador> Se va a remover: " + operandos.get(index).getLexema());
            operandos.remove(index);
            Operadores operador = operadores.get(index);
            for (int i = 0; i < listaAsignadores.length; i++) {
                if(operador.getLexema().equals(listaAsignadores[i])){
                    operadorAsignador = listaAsignadores[i];
                }
            }
            sys.println("<SEMANTICA 1 analizador> Se va a remover: " + operadores.get(index).getLexema());
            operadores.remove(operador);
            ejecutarOperacion(valorA, valorB, auxiliar.getTipoOperador(operador.getToken()), index);
        }
    }

    public void comprobarAsignacion() {
//        sys.println("Operador: "+operadores.get(0).getLexema());
        analizador();
        sys.println("\n--------------------------------------<SEMANTICA 1> Comprobar Asignacion--------------------------------------");
        String valorTemporal;
        if (operandos.get(1).getToken() == -6) {
            sys.println("<SEMANTICA 1 comprobarAsignacion if> Token: " + operandos.get(1).getToken());
            sys.println("<SEMANTICA 1 comprobarAsignacion if> Lexema: " + operandos.get(1).getLexema());
            sys.println("<SEMANTICA 1 comprobarAsignacion if> Clase: " + operandos.get(1).getClase());
            valorTemporal = obtenerClaseVariable(operandos.get(1).getLexema(), operandos.get(1).getAmbito());
        } else {
            sys.println("<SEMANTICA 1 comprobarAsignacion else if> Lexema: " + operandos.get(1).getLexema());
            valorTemporal = auxiliar.getTipoConstante(operandos.get(1).getToken());
        }
//        valorTemporal = obtenerClaseVariable(operandos.peek().getLexema(), operandos.peek().getAmbito());
        sys.println("<SEMANTICA 1 comprobarAsignacion> valorTemporal: " + valorTemporal);
        operandos.remove(1);
        Operando variable = operandos.get(0);
        sys.println("<SEMANTICA 1 comprobarAsignacion> variable: " + operandos.get(0).getLexema());
        operandos.remove(0);
        operadores.remove(0);
        String claseVariable;
        if (variable.getClase() != "variant" && variable.getLexema() != "Temporal") {
            sys.println("<SEMANTICA 1 comprobarAsignacion if variable.getClase()> Token: " + variable.getToken());
            sys.println("<SEMANTICA 1 comprobarAsignacion if variable.getClase()> Lexema: " + variable.getLexema());
            sys.println("<SEMANTICA 1 comprobarAsignacion if variable.getClase()> Clase: " + variable.getClase());
            claseVariable = obtenerClaseVariable(variable.getLexema(), variable.getAmbito());
        } else {
            claseVariable = variable.getClase();
        }
        sys.println("<SEMANTICA 1 comprobarAsignacion> Token Variable: " + variable.getToken());
        sys.println("<SEMANTICA 1 Comprobar Asignacion> Nombre Variable: " + variable.getLexema());
        sys.println("<SEMANTICA 1 Comprobar Asignacion> Clase Variable: " + variable.getClase());
        String asign = auxiliar.getAsignacion(claseVariable, valorTemporal);
        if (!asign.equals("Error")) {
            sys.println("<SEMANTICA 1 Comprobar Asignacion> La operacion de la variable " + variable.getLexema() + " es aceptable");
//            controladorTokenError.agregarToken(1100, "La operacion de la variable " + variable.getLexema() + " es aceptable", variable.getLinea());
        } else {
            contadores[contadores.length - 1].setErrores(contadores[contadores.length - 1].getErrores() + 1);
            controladorTokenError.agregarError(951, "No se puede asignar un "
                    + valorTemporal + " en la variable " + variable.getLexema() + " (" + variable.getClase() + ")", "",
                    variable.getLinea(), "Semantica 1");
            sys.println("<SEMANTICA 1 Comprobar Asignacion> La operacion de la variable " + variable.getLexema() + " no es aceptable");
        }
        contadores[contadores.length - 1].setAsignacion(variable.getLexema() + " -> T" + asign);
        if(!operadorAsignador.equals("")){
            comprobarAsignador(valorTemporal, variable);
        }
        sys.println("--------------------------------------<SEMANTICA 1> Fin Comprobar Asignacion--------------------------------------\n");
    }
    
    private void comprobarAsignador(String valor, Operando variable){
        String valorAux = valor.toLowerCase();
        String claseVariableAux = variable.getClase().toLowerCase();
        if(operadorAsignador.equals("=")){
            if(valorAux.equals(claseVariableAux)){
                analizadorSemantica2.agregarRegla(1020, variable.getLinea(), variable.getAmbito(), true);
            }
            else{
                analizadorSemantica2.agregarRegla(1020, variable.getLinea(), variable.getAmbito(), false);
            }
        }else{
            if(valorAux.equals(claseVariableAux)){
                analizadorSemantica2.agregarRegla(1021, variable.getLinea(), variable.getAmbito(), true);
            }
            else{
                analizadorSemantica2.agregarRegla(1021, variable.getLinea(), variable.getAmbito(), false);
            }
        }
        operadorAsignador = "";
    }

    private String obtenerClaseVariable(String variable, int ambito) {
        String query = "SELECT clase FROM tablasimbolos WHERE (id = BINARY '" + variable + "' AND (ambito = '" + ambito + "' OR ambito = '0'))";
        String claseVariable = "";
        try {
            ResultSet rs = controladorSQL.obtenerResultSet(query);
            while (rs.next()) {
                claseVariable = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Semantica1.class.getName()).log(Level.SEVERE, null, ex);
        }
        sys.println("   <SEMANTICA 1 obtenerClaseVariable> query: " + query);
        sys.println("   <SEMANTICA 1 obtenerClaseVariable> clase: " + claseVariable);
        return claseVariable;
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
                sys.println("<SEMANTICA 1> Se ha agregado el operando " + operando + ", " + lexema);
                pilaOperandos.push(new Operando(linea, operando, ambito, idAuxiliar, clase));
            }
        } else {
            if (auxiliar.verificarExistencia(operando, listaOperandos)) {
                sys.println("<SEMANTICA 1> Se ha agregado el operando " + operando + ", " + lexema);
                pilaOperandos.push(new Operando(linea, operando, ambito, idAuxiliar, auxiliar.getTipoConstante(operando)));
            }
        }
    }

    public void agregarOperador(int operador, int linea, String lexema) {
        if (auxiliar.verificarExistencia(operador, listaOperadores)) {
            if (operador == -15 || operador == -18) {
                agregarOperando(-7, linea, "1", -1);
            }
            sys.println("<SEMANTICA 1> Se ha agregado el operador " + operador + ", " + lexema);
            pilaOperadores.push(new Operadores(linea, operador, lexema));
            for (int i = 0; i < listaAsignadores.length; i++) {
                if(lexema.equals(listaAsignadores[i])){
                    operadorAsignador = listaAsignadores[i];
                }
            }
        }
    }

    private void aumentarArregloContadores() {
        sys.println("------------aumentar arreglo---------");
        ContadorSemantica1[] aux = contadores;
        contadores = new ContadorSemantica1[aux.length + 1];
        for (int i = 0; i < aux.length; i++) {
            contadores[i] = aux[i];
        }
        contadores[contadores.length - 1] = new ContadorSemantica1();
    }
}
