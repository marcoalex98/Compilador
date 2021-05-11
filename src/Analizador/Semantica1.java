/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Analizador.Auxiliar.AuxiliarSemantica1;
import Common.Utilities;
import Contadores.ContadorSemantica1;
import Controladores.ControladorTokenError;
import Modelos.Semantica1.Operadores;
import Modelos.Semantica1.Operandos;
import SQL.ControladorSQL;
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
    Stack<Operandos> pilaOperandos;
    ArrayList<Operadores> operadores;
    ArrayList<Operandos> operandos;
    ControladorSQL controladorSQL;
    public ContadorSemantica1[] contadores;
    boolean banderaArreglo, banderaEnterosArreglo;
    ControladorTokenError controladorTokenError;
    PrintStream sys = System.out;
    ArrayList<Integer> elementosArreglo;
    ArrayList<Integer> dimensionesArregloEjecucion;
    //                      ID   
    int listaOperandos[] = {-6, -1, -4, -7, -8, -9, -10, -12, -81, -82, -52, -47, -46};
    //                         >    +    -    *    /    %    =    ^   
    int listaOperadores[] = {-39, -14, -17, -20, -24, -28, -42, -44, -33, -32, -34, -30, -37, -117,
        //    +=   -=   *=   /=   <<   >>   ++   --    <   <=    !=  >=   IS  ISNOT IN INNOT
        -16, -19, -23, -27, -37, -40, -15, -18, -36, -38, -43, -31, -41, -71, -72, -70, -98, -35, -46, -52, -47};
    String listaAsignadores[] = {"=", "+=", "-=", "*=", "/="};
    ArrayList<String> listaComprobacionClase;
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
        banderaArreglo = false;
        contadores = new ContadorSemantica1[1];
        contadores[0] = new ContadorSemantica1();
        analizadorSemantica2.setAnalizadorSemantica1(this);
        elementosArreglo = new ArrayList<>();
        dimensionesArregloEjecucion = new ArrayList<>();
        listaComprobacionClase = new ArrayList<>();
        listaComprobacionClase.add("arreglo");
        listaComprobacionClase.add("diccionario");
        listaComprobacionClase.add("tupla");
    }

    public void ejecutarOperacion(Operandos valorA, Operandos valorB, String operacion, int index) {
        sys.println("\n--------------------------------------Inicio Ejecutar operacion--------------------------------------");
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
                    controladorSQL.obtenerClaseVariable(valorA.getLexema(), valorA.getAmbito()),
                    controladorSQL.obtenerClaseVariable(valorB.getLexema(), valorB.getAmbito()));
            temporal = auxiliar.getTokenTemporal2(relacion, contadores);
        } else if (valorA.getToken() == -6) {
            sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> variable contra otra cosa");
            relacion = auxiliar.getRelacion(operacion,
                    controladorSQL.obtenerClaseVariable(valorA.getLexema(), valorA.getAmbito()),
                    auxiliar.getTipoConstante(valorB.getToken()));
            temporal = auxiliar.getTokenTemporal2(relacion, contadores);
        } else if (valorB.getToken() == -6) {
            sys.println("    <SEMANTICA 1-2 ejecutarOperacion if> otra cosa contra variable");
            relacion = auxiliar.getRelacion(operacion,
                    auxiliar.getTipoConstante(valorA.getToken()),
                    controladorSQL.obtenerClaseVariable(valorB.getLexema(), valorB.getAmbito()));
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
        operandos.add(index, new Operandos(linea, temporal, ambito, lexema, clase));
//        pilaOperandos.push(new Operando(linea, temporal, ambito, lexema, clase));
    }

    private void analizarDimensiones() {
        ArrayList<Operandos> operandosResultantes = new ArrayList<>();
        ArrayList<Operandos> operandosAuxiliar = new ArrayList<>();
        ArrayList<Operadores> operadoresAuxiliar = new ArrayList<>();
        boolean banderaAgregarOperando = false, agregarOperador = false;
        banderaEnterosArreglo = true;
        for (int i = 0; i < operandos.size(); i++) {
            if (operandos.get(i).getToken() == -52) {
                banderaAgregarOperando = true;
                operandos.remove(i);
                i--;
                continue;
            } else if (operandos.get(i).getToken() == -47) {
                operandos.remove(i);
                break;
            }
            if (banderaAgregarOperando) {
                operandosAuxiliar.add(operandos.get(i));
                operandos.remove(i);
                i--;
            }
        }
        for (int i = 0; i < operadores.size(); i++) {
            if (operadores.get(i).getToken() == -52) {
                agregarOperador = true;
                operadores.remove(i);
                i--;
                continue;
            } else if (operadores.get(i).getToken() == -47) {
                operadores.remove(i);
                break;
            }
            if (agregarOperador) {
                operadoresAuxiliar.add(operadores.get(i));
                operadores.remove(i);
                i--;
            }
        }
        for (int i = 0; i < operandosAuxiliar.size(); i++) {
            System.err.print(operandosAuxiliar.get(i).getLexema() + " ");
        }
        for (int i = 0; i < operadoresAuxiliar.size(); i++) {
            System.err.println(operadoresAuxiliar.get(i).getLexema());
        }

        ArrayList<Operandos> operandosAuxiliar2 = new ArrayList<>();
        ArrayList<Operadores> operadoresAuxiliar2 = new ArrayList<>();
        OUTER:
        while (true) {
            for (int i = 0; i < operandosAuxiliar.size(); i++) {
                if (operandosAuxiliar.get(i).getToken() == -46) {
                    operandosAuxiliar.remove(i);
                    i--;
                    break;
                } else {
                    operandosAuxiliar2.add(operandosAuxiliar.get(i));
                    System.err.print("Se ha agregado: " + operandosAuxiliar.get(i).getLexema() + " ");
                    operandosAuxiliar.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < operadoresAuxiliar.size(); i++) {
                if (operadoresAuxiliar.get(i).getToken() == -46) {
                    operadoresAuxiliar.remove(i);
                    i--;
                    break;
                } else {
                    operadoresAuxiliar2.add(operadoresAuxiliar.get(i));
                    operadoresAuxiliar.remove(i);
                    i--;
                }
            }
            operandosResultantes.add(comprobarOperacionMatematica(operandosAuxiliar2, operadoresAuxiliar2));
            operadoresAuxiliar2 = new ArrayList<>();
            operandosAuxiliar2 = new ArrayList<>();
            if (operadoresAuxiliar.isEmpty() && operandosAuxiliar.isEmpty()) {
                break;
            }

            if (!banderaEnterosArreglo) {
                break;
            }
        }
        for (int i = 0; i < operandosResultantes.size(); i++) {
            System.err.println("Operandos resultantes: " + operandosResultantes.get(i).getLexema() + " ");
        }

        if (banderaEnterosArreglo) {
            operandos.add(1, new Operandos(operandos.get(0).getLinea(), -52, operandos.get(0).getAmbito(), "[", ""));
            int contadorElementos = 0;
            int operandosResultantesSize = operandosResultantes.size();
            for (int i = 0; i < operandosResultantesSize; i++) {
                System.err.println("Se va a agregar: " + operandosResultantes.get(0).getLexema() + " ");
                operandos.add(i + 2, operandosResultantes.get(0));
                operandosResultantes.remove(0);
                if (!operandosResultantes.isEmpty()) {
                    operandos.add(i + 4, new Operandos(operandos.get(0).getLinea(), -46, operandos.get(0).getAmbito(), ",", ""));
                }
                contadorElementos = i;
            }
            operandos.add(contadorElementos + 3, new Operandos(operandos.get(0).getLinea(), -47, operandos.get(0).getAmbito(), "]", ""));
            for (int i = 0; i < operandos.size(); i++) {
                System.err.println("* " + operandos.get(i).getLexema());
            }
            analizadorSemantica2.agregarRegla(1040, operandos.get(0).getLinea(), operandos.get(0).getAmbito(), true);
        } else {
            analizadorSemantica2.agregarRegla(1040, operandos.get(0).getLinea(), operandos.get(0).getAmbito(), false);
        }

    }

    private Operandos comprobarOperacionMatematica(ArrayList<Operandos> operandosAuxiliar,
            ArrayList<Operadores> operadoresAuxiliar) {
        boolean operacionValida = true;
        if (operandosAuxiliar.size() > 1) {
            OUTER:
            OUTER_1:
            while (operadoresAuxiliar.size() > 0) {
                int index = auxiliar.obtenerPosicionMayorOperador(operadoresAuxiliar);
                Operandos valorA = operandosAuxiliar.get(index);
                Operandos valorB = operandosAuxiliar.get(index + 1);
                int linea = operadoresAuxiliar.get(index).getLinea();
                int ambito = operadoresAuxiliar.get(index).getLinea();
                int enteroValorA, enteroValorB, resultadoOperacion;
                switch (valorA.getToken()) {
                    case -7:
                        enteroValorA = Integer.parseInt(valorA.getLexema());
                        break;
                    case -6:
                        if (controladorSQL.obtenerClaseVariable(valorA.getLexema(), valorA.getAmbito()).equals("Decimal")) {
                            enteroValorA = controladorSQL.obtenerValorVariable(valorA.getLexema(), valorA.getAmbito());
                        } else {
                            operacionValida = false;
                            break OUTER;
                        }
                        break;
                    default:
                        enteroValorA = 0;
                        operacionValida = false;
                        break OUTER;
                }
                switch (valorB.getToken()) {
                    case -7:
                        enteroValorB = Integer.parseInt(valorB.getLexema());
                        break;
                    case -6:
                        if (controladorSQL.obtenerClaseVariable(valorB.getLexema(), valorB.getAmbito()).equals("Decimal")) {
                            enteroValorB = controladorSQL.obtenerValorVariable(valorB.getLexema(), valorB.getAmbito());
                        } else {
                            operacionValida = false;
                            break OUTER;
                        }
                        break;
                    default:
                        enteroValorB = 0;
                        operacionValida = false;
                        break OUTER_1;
                }
                switch (operadoresAuxiliar.get(index).getLexema()) {
                    case "+":
                        resultadoOperacion = enteroValorA + enteroValorB;
                        operadoresAuxiliar.remove(index);
                        break;
                    case "-":
                        resultadoOperacion = enteroValorA - enteroValorB;
                        operadoresAuxiliar.remove(index);
                        break;
                    case "*":
                        resultadoOperacion = enteroValorA * enteroValorB;
                        operadoresAuxiliar.remove(index);
                        break;
                    case "/":
                        resultadoOperacion = 0;
                        operacionValida = false;
                        break;
                    default:
                        resultadoOperacion = 0;
                        operacionValida = false;
                }
                if (operacionValida) {
                    operandosAuxiliar.remove(index);
                    operandosAuxiliar.remove(index);
                    operandosAuxiliar.add(index, new Operandos(linea, -7, ambito, resultadoOperacion + "", ""));
                }
            }
        } else {
        }
        if (!operacionValida) {
            banderaEnterosArreglo = false;
        }
        System.err.println("---RESULTADO: " + operandosAuxiliar.get(0).getLexema());
        return operandosAuxiliar.get(0);
    }

    private void analizarArreglo() {
        boolean bandera1040 = true, bandera1050 = true, banderaTodoSalioBien = true;
        int linea1040 = 0, ambito1040 = 0, contadorDimensiones = 0;
        analizarDimensiones();
        String nombreArreglo = operandos.get(0).getLexema();
        int ambito = operandos.get(0).getAmbito();

        for (int i = 0; i < operandos.size(); i++) {
            System.err.print("--" + operandos.get(i).getLexema() + " ");
        }

        for (int i = 0; i < operandos.size(); i++) {
            if (ambito1040 == 0 && operandos.get(i).getAmbito() > 0) {
                ambito1040 = operandos.get(i).getAmbito();
            }
            if (operandos.get(i).getToken() == -52) {
                banderaArreglo = true;
                linea1040 = operandos.get(i).getLinea();
                operandos.remove(operandos.get(i));
                i--;
                continue;
            }
            if (banderaArreglo) {
                if (operandos.get(i).getToken() == -46) {
                    //contadorDimensiones++;
                    operandos.remove(operandos.get(i));
                    i--;
                    continue;
                }
                if (operandos.get(i).getToken() == -47) {//Fin del arreglo
                    operandos.remove(operandos.get(i));
                    break;
                }
                //Si se encuentra una constante, procede
                if (Utilities.isConstante(operandos.get(i).getToken())) {
                    //Si la variable es decimal, procede
                    if (Utilities.getTipoVariable(operandos.get(i).getToken()).equals("Decimal")) {
//                        elementosArreglo.add(Integer.parseInt(operandos.get(i).getLexema()));
                        dimensionesArregloEjecucion.add(Integer.parseInt(operandos.get(i).getLexema()));
                        int inicioDimension, finDimension, valorDimension;
                        if(contadorDimensiones >= analizadorSemantica2.arreglos.get((nombreArreglo+ambito)).getDimensiones().size()){
                            analizadorSemantica2.agregarRegla(1030, operandos.get(0).getLinea(), operandos.get(0).getAmbito(), false);
                            banderaTodoSalioBien = false;
                            break;
                        }
                        inicioDimension = analizadorSemantica2.arreglos.get((nombreArreglo + ambito)).getDimensiones().get(contadorDimensiones).getInicio();
                        finDimension = analizadorSemantica2.arreglos.get((nombreArreglo + ambito)).getDimensiones().get(contadorDimensiones).getFin();
                        valorDimension = Integer.parseInt(operandos.get(i).getLexema());
                        contadorDimensiones++;
//                        if(analizadorSemantica2.arreglos.get((nombreArreglo+ambito)).getDimensiones().size()>1 &&
//                                contadorDimensiones < analizadorSemantica2.arreglos.get((nombreArreglo+ambito)).getDimensiones().size()){
//                            
//                        }else{
//                            inicioDimension = analizadorSemantica2.arreglos.get((nombreArreglo+ambito)).getDimensiones().get(contadorDimensiones-1).getInicio();
//                            finDimension = analizadorSemantica2.arreglos.get((nombreArreglo+ambito)).getDimensiones().get(contadorDimensiones-1).getFin();
//                            valorDimension = Integer.parseInt(operandos.get(i).getLexema())-1;
//                        }

                        if (!(inicioDimension <= valorDimension && finDimension >= valorDimension)) {
                            bandera1050 = false;
                        }
//                        operandos.remove(operandos.get(i));
//                        i--;

                        //Cuando no lo es, se activa la bandera 1040 como false    
                    } else {
                        operandos.remove(operandos.get(i));
                        i--;
                        bandera1040 = false;
                    }
                } else if (operandos.get(i).getToken() == -6) {//Si llega una variable
                    if (!controladorSQL.obtenerClaseVariable(operandos.get(i).getLexema(), operandos.get(i).getAmbito()).equals("Decimal")) {
                        bandera1040 = false;
                        operandos.remove(operandos.get(i));
                        i--;
                    }
                } else {//Otra cosa
                    operandos.remove(operandos.get(i));
                    i--;
                }
            }
        }
        if (banderaTodoSalioBien) {
            analizadorSemantica2.agregarRegla(1030, operandos.get(0).getLinea(), operandos.get(0).getAmbito(), true);
            analizadorSemantica2.agregarRegla(1050, operandos.get(0).getLinea(), operandos.get(0).getAmbito(), bandera1050);
        }
        
    }

    private void analizarEstructuras(String tipo) {
        switch (tipo.toLowerCase()) {
            case "arreglo":
                analizarArreglo();
                break;
        }
    }

    private void analizador() {
        Operandos operandosArr[] = new Operandos[pilaOperandos.size()];
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
        analizarEstructuras(controladorSQL.obtenerClaseVariable(operandos.get(0).getLexema(), operandos.get(0).getAmbito()));
        System.err.println("");
        while (true) {
            int index = auxiliar.obtenerPosicionMayorOperador(operadores);
            if (index == 0) {
                break;
            }
            Operandos valorA = operandos.get(index);
            Operandos valorB = operandos.get(index + 1);
            operandos.remove(index);
            operandos.remove(index);
            Operadores operador = operadores.get(index);
            for (int i = 0; i < listaAsignadores.length; i++) {
                if (operador.getLexema().equals(listaAsignadores[i])) {
                    operadorAsignador = listaAsignadores[i];
                }
            }
            operadores.remove(operador);
            ejecutarOperacion(valorA, valorB, auxiliar.getTipoOperador(operador.getToken()), index);
        }
    }

    public void comprobarAsignacion() {
        analizador();
        sys.println("\n--------------------------------------<SEMANTICA 1> Comprobar Asignacion--------------------------------------");
        String valorTemporal;
        if (operandos.get(1).getToken() == -6) {
            sys.println("<SEMANTICA 1 comprobarAsignacion if> Token: " + operandos.get(1).getToken());
            sys.println("<SEMANTICA 1 comprobarAsignacion if> Lexema: " + operandos.get(1).getLexema());
            sys.println("<SEMANTICA 1 comprobarAsignacion if> Clase: " + operandos.get(1).getClase());
            valorTemporal = controladorSQL.obtenerClaseVariable(operandos.get(1).getLexema(), operandos.get(1).getAmbito());
        } else {
            sys.println("<SEMANTICA 1 comprobarAsignacion else if> Lexema: " + operandos.get(1).getLexema());
            valorTemporal = auxiliar.getTipoConstante(operandos.get(1).getToken());
        }
        sys.println("<SEMANTICA 1 comprobarAsignacion> valorTemporal: " + valorTemporal);
        operandos.remove(1);
        Operandos variable = operandos.get(0);

        sys.println("<SEMANTICA 1 comprobarAsignacion> variable: " + operandos.get(0).getLexema());
        operandos.remove(0);
        operadores.remove(0);
        String claseVariable = "";
        if (!banderaArreglo) {
            if (!"variant".equals(variable.getClase()) && !"Temporal".equals(variable.getLexema())) {
                claseVariable = controladorSQL.obtenerClaseVariable(variable.getLexema(), variable.getAmbito());
            } else {
                claseVariable = variable.getClase();
            }
        } else if (banderaArreglo || !banderaEnterosArreglo) {
            if (controladorSQL.isArregloVirgen(variable.getLexema(), variable.getAmbito() + "")) {
                claseVariable = valorTemporal;
                controladorSQL.actualizarTipoArreglo(variable.getLexema(), variable.getAmbito() + "", claseVariable);
            } else {
                claseVariable = controladorSQL.obtenerTipoArreglo(variable.getLexema(), variable.getAmbito());
            }
        }
        if (listaComprobacionClase.contains(claseVariable)) {
            analizadorSemantica2.comprobarNumeroDimencionesArreglo(variable.getLexema(), variable.getAmbito(),
                    dimensionesArregloEjecucion.size(), variable.getLinea());
        }
        sys.println("<SEMANTICA 1 comprobarAsignacion> Token Variable: " + variable.getToken());
        sys.println("<SEMANTICA 1 Comprobar Asignacion> Nombre Variable: " + variable.getLexema());
        sys.println("<SEMANTICA 1 Comprobar Asignacion> Clase Variable: " + variable.getClase());
        sys.println("<SEMANTICA 1 comprobarAsignacion> Token Auxiliar: " + variable.getToken());
        sys.println("<SEMANTICA 1 Comprobar Asignacion> Nombre Variable: " + variable.getLexema());
        sys.println("<SEMANTICA 1 Comprobar Asignacion> Clase Variable: " + variable.getClase());
        String asign = auxiliar.getAsignacion(claseVariable, valorTemporal);
        if (!asign.equals("Error")) {
            sys.println("<SEMANTICA 1 Comprobar Asignacion> La operacion de la variable " + variable.getLexema() + " es aceptable");
        } else {
            contadores[contadores.length - 1].setErrores(contadores[contadores.length - 1].getErrores() + 1);
            controladorTokenError.agregarError(951, "No se puede asignar un "
                    + valorTemporal + " en la variable " + variable.getLexema() + " (" + variable.getClase() + ")", "",
                    variable.getLinea(), "Semantica 1");
            sys.println("<SEMANTICA 1 Comprobar Asignacion> La operacion de la variable " + variable.getLexema() + " no es aceptable");
        }
        contadores[contadores.length - 1].setAsignacion(variable.getLexema() + " -> T" + asign);
        if (!operadorAsignador.equals("")) {
            if (banderaArreglo) {
                variable.setClase(claseVariable);
            }
            comprobarAsignador(valorTemporal, variable);
        }
        sys.println("--------------------------------------<SEMANTICA 1> Fin Comprobar Asignacion--------------------------------------\n");
        operandos = new ArrayList<>();
        operadores = new ArrayList<>();
        banderaArreglo = false;
        dimensionesArregloEjecucion.clear();
    }

    public String ejecutarOperacionSemantica2() {
        Operandos operandosArr[] = new Operandos[pilaOperandos.size()];
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
            Operandos valorA = operandos.get(index);
            Operandos valorB = operandos.get(index + 1);
            operandos.remove(index);
            operandos.remove(index);
            Operadores operador = operadores.get(index);
            operadores.remove(operador);
            String operacion = auxiliar.getTipoOperador(operador.getToken());
            int temporal;
            String relacion;
            if (valorA.getToken() == -6 && valorB.getToken() == -6) {
                relacion = auxiliar.getRelacion(operacion,
                        controladorSQL.obtenerClaseVariable(valorA.getLexema(), valorA.getAmbito()),
                        controladorSQL.obtenerClaseVariable(valorB.getLexema(), valorB.getAmbito()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            } else if (valorA.getToken() == -6) {
                relacion = auxiliar.getRelacion(operacion,
                        controladorSQL.obtenerClaseVariable(valorA.getLexema(), valorA.getAmbito()),
                        auxiliar.getTipoConstante(valorB.getToken()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            } else if (valorB.getToken() == -6) {
                relacion = auxiliar.getRelacion(operacion,
                        auxiliar.getTipoConstante(valorA.getToken()),
                        controladorSQL.obtenerClaseVariable(valorB.getLexema(), valorB.getAmbito()));
                temporal = auxiliar.getTokenTemporal2(relacion, contadores);
            } else {
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
                pilaOperandos.push(new Operandos(linea, operando, ambito, idAuxiliar, clase));
            }
        } else {
            if (auxiliar.verificarExistencia(operando, listaOperandos)) {
                sys.println("<SEMANTICA 1> Se ha agregado el operando " + operando + ", " + lexema);
                pilaOperandos.push(new Operandos(linea, operando, ambito, idAuxiliar, auxiliar.getTipoConstante(operando)));
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
                if (lexema.equals(listaAsignadores[i])) {
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

    private void comprobarAsignador(String valor, Operandos variable) {
        String valorAux = valor.toLowerCase();
        String claseVariableAux = variable.getClase().toLowerCase();
        if (operadorAsignador.equals("=")) {
            if (valorAux.equals(claseVariableAux)) {
                analizadorSemantica2.agregarRegla(1020, variable.getLinea(), variable.getAmbito(), true);
            } else {
                analizadorSemantica2.agregarRegla(1020, variable.getLinea(), variable.getAmbito(), false);
            }
        } else {
            if (valorAux.equals(claseVariableAux)) {
                analizadorSemantica2.agregarRegla(1021, variable.getLinea(), variable.getAmbito(), true);
            } else {
                analizadorSemantica2.agregarRegla(1021, variable.getLinea(), variable.getAmbito(), false);
            }
        }
        operadorAsignador = "";
    }
}
