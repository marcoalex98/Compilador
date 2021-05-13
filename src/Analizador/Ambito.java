/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Contadores.ContadorAmbito;
import Controladores.ControladorTokenError;
import Estructuras.Controladores.ControladorDatoDiccionario;
import Estructuras.Controladores.ControladorDatoLista;
import Modelos.Diccionario;
import Modelos.Lista;
import Modelos.NodoToken;
import Modelos.OperToken;
import Modelos.Semantica1.Arreglo;
import Modelos.Semantica2.ElementoDiccionario;
import Modelos.Tupla;
import Modelos.Variable;
import SQL.ControladorSQL;
import SQL.TablaSimbolos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 *
 * @author marco
 */
public class Ambito {

    int contadorIdentificadores, ambito, tamArr, tamVariablesGuardadasArr,
            contadorDiccionario, contadorElementosLista,
            ambitoActualDisponible, contadorTupla, contadorVariablesArreglo,
            ambitoMayor, auxiliarTipoVariable, contadorParametros, contadorDiccionariosMortales;
    String claseVariable, nombreVariable, auxiliarNombreVariable, tipoVariable, valorVariable,
            ambitoCreado, ambitoVariable, tarrVariable, listaPerteneceVariable, rango1,
            rango2, avance, auxiliarID, nombreFuncion, ultimoDiccionarioCreado;
    boolean estadoError, banderaParametro, banderaArreglo, banderaTupla, banderaListaMultiple,
            banderaRango, banderaListaNormal, agregarLista, banderaFor, agregarTupla,
            agregarDiccionario, banderaConstante, banderaFuncion, agregarConstante,
            banderaDiccionario, asignarValor, agregarDiccionarioLopez, bandera1060, agregarLlaveDiccionario,
            bandera814, areaDeclaracion, primeraVezTipoLista, llaveDiccionario, agregarDatoDiccionario, switchLlaveValor;
    //agregarDiccionarioPerez se encarga de avisar que se va a agregar un diccionario dentro de otro diccionario :$
    NodoToken llaveAuxiliar;
    NodoToken llaveAuxiliarDiccionario;
    NodoToken nodoDiccionarioCreado;
    NodoToken auxiliarDiccionario; //Este se usa para guardar la informacion de la variable tipo diccionario, en caso de que contenga otro diccionario
    Stack<Integer> pilaSintaxis;
    Stack<Integer> pilaAmbito;
    ControladorSQL controladorSQL;
    TablaSimbolos tablaSimbolos[];
    HashMap<String, Arreglo> arreglos;
    HashMap<String, Modelos.Semantica2.Diccionario> diccionarios;
    Diccionario diccionario[];
    Lista[] listaArreglo;
    Tupla[] tuplaArreglo;
    Variable variableArreglo[];
    ContadorAmbito contadorAmbitoArr[];
    ControladorTokenError controladorTokenError;
    OperToken oper;
    ControladorDatoLista controladorDatoLista;
    ControladorDatoDiccionario controladorDatoDiccionario;
    Semantica1 analizadorSemantica1;
    Semantica2 analizadorSemantica2;

    public Ambito(ControladorSQL controladorSQL,
            ControladorTokenError controladorTokenError,
            HashMap<String, Arreglo> arreglos,
            HashMap<String, Modelos.Semantica2.Diccionario> diccionarios,
            Semantica1 analizadorSemantica1,
            Semantica2 analizadorSemantica2) {
        this.controladorSQL = controladorSQL;
        this.controladorTokenError = controladorTokenError;
        this.controladorDatoLista = new ControladorDatoLista();
        this.arreglos = arreglos;
        this.diccionarios = diccionarios;
        this.controladorDatoDiccionario = new ControladorDatoDiccionario();
        this.analizadorSemantica1 = analizadorSemantica1;
        this.analizadorSemantica2 = analizadorSemantica2;
    }

    public void iniciarAmbito() {
        inicializarVariables();
    }

    private void inicializarVariables() {
        claseVariable = "";
        nombreVariable = "";
        auxiliarNombreVariable = "";
        tipoVariable = "";
        valorVariable = "";
        ambitoCreado = "";
        ambitoVariable = "";
        tarrVariable = "";
        listaPerteneceVariable = "";
        rango1 = "";
        rango2 = "";
        avance = "";
        auxiliarID = "";
        nombreFuncion = "";
        banderaConstante = false;
        banderaFuncion = false;
        banderaDiccionario = false;
        agregarDatoDiccionario = false;
        agregarDiccionarioLopez = false;
        switchLlaveValor = false;
        agregarConstante = false;
        bandera1060 = true;
        asignarValor = false;
        bandera814 = false;
        llaveDiccionario = false;
        areaDeclaracion = true;
        agregarLlaveDiccionario = true;
        primeraVezTipoLista = true;
        contadorDiccionariosMortales = 0;
        ambito = 0;
        ambitoMayor = 0;
        contadorElementosLista = 0;
        contadorParametros = 0;
        ambitoActualDisponible = 0;
        contadorTupla = 0;
        contadorDiccionario = 0;
        contadorVariablesArreglo = 0;
        auxiliarTipoVariable = 0;
        listaArreglo = new Lista[400]; // este tenia [3]
        tuplaArreglo = new Tupla[400];
        diccionario = new Diccionario[400];
        tablaSimbolos = new TablaSimbolos[500];
        variableArreglo = new Variable[400];
        contadorAmbitoArr = new ContadorAmbito[1];
        contadorAmbitoArr[0] = new ContadorAmbito();
        pilaSintaxis = new Stack<Integer>();
        pilaAmbito = new Stack<Integer>();
        pilaAmbito.push(ambitoActualDisponible);
        ambitoActualDisponible++;
    }

    public void agregarOperadorOperando(int token, OperToken oper) {
        if (!areaDeclaracion) {
            if (token == -6) {
                analizadorSemantica1.agregarOperando(
                        token,
                        oper.mostrarLineaPrimero(),
                        oper.mostrarLexemaPrimero(),
                        pilaAmbito.peek());
            } else {
                analizadorSemantica1.agregarOperando(
                        token, oper.mostrarLineaPrimero(),
                        oper.mostrarLexemaPrimero(),
                        -1);
            }
            analizadorSemantica1.agregarOperador(token,
                    oper.mostrarLineaPrimero(),
                    oper.mostrarLexemaPrimero());
        }
    }

    public Stack<Integer> analizadorAmbito(Stack<Integer> pilaSintaxis, OperToken oper) {
        System.out.println("<AMBITO> Nombre variable: " + nombreVariable);
        System.out.println("<AMBITO> Tipo variable: " + claseVariable);
        System.out.println("<AMBITO> Clase variable: " + tipoVariable);
        System.out.println("<AMBITO> Rango 1: " + rango1);
        System.out.println("<AMBITO> Rango 2: " + rango2);
        System.out.println("<AMBITO> Avance: " + avance);
        System.out.println("<AMBITO> Ambito: " + ambito);
        System.out.println("<AMBITO> Ambito Creado: " + ambitoCreado);
        System.out.println("<AMBITO> tarrVariable: " + tarrVariable);
        System.out.println("<AMBITO> Cima de pila: " + pilaSintaxis.peek());
        this.oper = oper;

        if (pilaSintaxis.peek() == 10601) {
            pilaSintaxis.pop();
            agregarDiccionarioLopez = false;
            agregarDiccionario = true;
        }

        if (pilaSintaxis.peek() == 800) {
            agregarConstante = true;
            pilaSintaxis.pop();
        }

        try {
            if (analizadorSemantica2.analizarReglas(pilaSintaxis.peek(), oper.mostrarLineaPrimero(), ambito)) {
                pilaSintaxis.pop();
            }
            if (analizadorSemantica2.analizarArreglo(pilaSintaxis.peek(), areaDeclaracion, oper.obtenerPrimero(), ambito)) {
                pilaSintaxis.pop();
            }
        } catch (Exception e) {
            controladorTokenError.agregarError(999, "Ha ocurrido una excepcion", "", oper.mostrarLineaPrimero(), "Semantica 2");
        }

        if (pilaSintaxis.peek() == 920) {
            pilaSintaxis.pop();
            try {
                analizadorSemantica1.comprobarAsignacion();
            } catch (Exception e) {
                controladorTokenError.agregarError(999, "Ha ocurrido una excepcion al comprobar asignacion", "", oper.mostrarLineaPrimero(), "Semantica 1");
            }
        }

        if (pilaSintaxis.peek() == 8152) {
            pilaSintaxis.pop();
            agregarLista = true;
        }
        if (pilaSintaxis.peek() == 8032) {
            pilaSintaxis.pop();
            banderaParametro = false;
            actualizarCantidadParametrosFuncion();
            contadorParametros = 0;
            nombreFuncion = "";
            claseVariable = "";
            tipoVariable = "";
            ambitoVariable = "";
//                reducirAmbito();
        }
        if (pilaSintaxis.peek() == 8162) {
            pilaSintaxis.pop();
            if (areaDeclaracion) {
                agregarTupla = true;
            }
            //reducirAmbito();
        }
        if (pilaSintaxis.peek() == 8202) {
            pilaSintaxis.pop();
            agregarDiccionario = true;
            agregarLlaveDiccionario = true;

        }
        if (pilaSintaxis.peek() == 840) {//For
            pilaSintaxis.pop();
            banderaFor = true;
        }

        if (pilaSintaxis.peek() == 820) {
            if (!banderaDiccionario) {
                ultimoDiccionarioCreado = (nombreVariable + ambito);
                nodoDiccionarioCreado = oper.obtenerPrimero();
                diccionarios.put((nombreVariable + ambito), new Modelos.Semantica2.Diccionario(0, null, ambito, oper.mostrarLineaPrimero()));
                auxiliarDiccionario = new NodoToken(oper.mostrarTokenPrimero(), nombreVariable, oper.mostrarLineaPrimero());
            } else if (banderaDiccionario && pilaSintaxis.peek() == 820) {
                if (diccionarios.get((nombreVariable + ambito)).getElementos() == null) {
                    diccionarios.get((nombreVariable + ambito)).setElementos(new ArrayList<>());
                }
                agregarDiccionarioLopez = true;
                contadorDiccionariosMortales++;
                agregarDatoDiccionario = true;
//                diccionarios.get(ultimoDiccionarioCreado).getElementos().get(contadorDiccionariosMortales-1).setArrayList();
            }
            banderaDiccionario = true;
            pilaSintaxis.pop();
        }

        if (areaDeclaracion) {
            if (nombreVariable != "" && claseVariable != "" && valorVariable != ""
                    && banderaConstante && !banderaDiccionario && !agregarDiccionarioLopez && !agregarLista && agregarConstante) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Constante");
                agregarVariableBaseDatos("Constante");
                agregarConstante = false;
            } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && banderaFuncion) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Funcion");
                agregarVariableBaseDatos("Funcion");
            } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && banderaParametro) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Parametro");
                agregarVariableBaseDatos("Parametro");
            } else if (nombreVariable != "" && claseVariable != "" && rango1 != "" && rango2 != ""
                    && avance != "" && banderaRango) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Rango");
                agregarVariableBaseDatos("Rango");
            } else if (agregarLista) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Lista");
                agregarVariableBaseDatos("Lista");
                primeraVezTipoLista = true;
                auxiliarTipoVariable = 0;
                contadorElementosLista = 0;
                banderaListaMultiple = false;
                banderaListaNormal = false;
                agregarLista = false;
            } else if (agregarDiccionario && !agregarDiccionarioLopez) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Diccionario");
                agregarVariableBaseDatos("!diccionarioLopez");
                agregarDiccionario = false;
            } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && ambitoVariable != ""
                    && tarrVariable != "" && banderaTupla && agregarTupla) {
                System.out.println("<AMBITO:sintaxis> Agregar Variable Tupla");
                agregarVariableBaseDatos("Tupla");
            }
        } else {
            if (pilaSintaxis.peek() == -6) {
                if (!variableDeclarada(oper.mostrarLexemaPrimero())) {
                    controladorTokenError.agregarError(1001, "Variable " + oper.mostrarLexemaPrimero() + " no declarada",
                            "Ambito: " + ambito + "", oper.mostrarLineaPrimero(), "Ambito");
                }
            }

        }

        if (pilaSintaxis.peek() == -6) {
            auxiliarID = oper.mostrarLexemaPrimero();
        }

//        if (banderaFor) {
//            if (pilaSintaxis.peek() == -6) {
//                System.out.println("simonfor6");
//                System.out.println("nombreVariable: " + auxiliarID);
//                System.out.println("oper.primero: " + oper.mostrarLexemaPrimero());
//                nombreVariable = auxiliarID;
//            }
//            if (oper.mostrarPrimero() == -7) {
//                aumentarAmbito();
//                System.out.println("simonfor7");
//                valorVariable = oper.mostrarLexemaPrimero();
//                claseVariable = "Decimal";
//                banderaConstante = true;
//            }
//        }
        if (pilaSintaxis.peek() == 8402) {
            reducirAmbito();
        }

        if (pilaSintaxis.peek() == 8403) {
            pilaSintaxis.pop();
            aumentarAmbito();
            if (oper.mostrarTokenPrimero() == -6) {
                System.out.println("<AMBITO> Se ha agregado la variable " + oper.mostrarLexemaPrimero() + " perteneciente a un for");
                controladorSQL.ejecutarQuery("INSERT INTO tablasimbolos (id,clase,tipo,listaPertenece,ambito) VALUES("
                        + "'" + oper.mostrarLexemaPrimero() + "',"
                        + "'" + "Decimal" + "',"
                        + "'" + "var" + "',"
                        + "'" + "for" + "',"
                        + "'" + ambito + "');");
            }
        }

        switch (pilaSintaxis.peek()) {
            case 801:
                System.out.println("<AMBITO> AREA DE DECLARACION HA SIDO DESACTIVADA");
                areaDeclaracion = false;
                banderaListaNormal = false;
                analizadorSemantica2.elementosArreglo.clear();
                banderaConstante = false;
                banderaFuncion = false;
                banderaDiccionario = false;
                asignarValor = false;
                agregarDiccionarioLopez = false;
                bandera814 = false;
                llaveDiccionario = false;
                primeraVezTipoLista = true;
                bandera814 = false;
                bandera1060 = true;
                tamVariablesGuardadasArr = 0;
                contadorDiccionariosMortales = 0;
                claseVariable = "";
                nombreVariable = "";
                auxiliarID = "";
                tipoVariable = "";
                ultimoDiccionarioCreado = "";
                valorVariable = "";
                ambitoCreado = "";
                tarrVariable = "";
                ambitoVariable = "";
                pilaSintaxis.pop();
                break;
            case 802:
                System.out.println("<AMBITO> AREA DE DECLARACION HA SIDO ACTIVADA");
                areaDeclaracion = true;
                bandera814 = false;
                banderaListaNormal = false;
                agregarDiccionarioLopez = false;
                tamVariablesGuardadasArr = 0;
                contadorDiccionariosMortales = 0;
                banderaConstante = false;
                banderaFuncion = false;
                banderaDiccionario = false;
                bandera1060 = true;
                asignarValor = false;
                bandera814 = false;
                llaveDiccionario = false;
                primeraVezTipoLista = true;
                claseVariable = "";
                auxiliarID = "";
                nombreVariable = "";
                analizadorSemantica2.elementosArreglo.clear();
                tipoVariable = "";
                valorVariable = "";
                ultimoDiccionarioCreado = "";
                ambitoCreado = "";
                tarrVariable = "";
                ambitoVariable = "";
                pilaSintaxis.pop();
                break;
            case 803:
                if (banderaParametro) {
                    banderaParametro = false;
                    pilaSintaxis.pop();
                    claseVariable = "";
//                        aumentarAmbito();
                } else {
                    banderaParametro = true;
                    claseVariable = "None";
                    tipoVariable = "par";
                    ambitoVariable = ambito + "";
                    pilaSintaxis.pop();
                }
                break;
            case 804:
                reducirAmbito();
                pilaSintaxis.pop();
                break;
            case 8033:
                pilaSintaxis.pop();
                contadorParametros++;
                break;
        }

        if (pilaSintaxis.peek() == 815) {
            banderaListaNormal = true;
        }
        if (pilaSintaxis.peek() == 8153) {
            contadorElementosLista++;
        }

        if (pilaSintaxis.peek() >= 805 && pilaSintaxis.peek() <= 814 && !bandera814) {
            banderaConstante = true;
            System.out.println("<AMBITO:sintaxis>  Entro a if de 805 y 814 | 800:" + pilaSintaxis.peek());
            tipo800(pilaSintaxis.peek());
            pilaSintaxis.pop();
        } else if (pilaSintaxis.peek() > 814 && pilaSintaxis.peek() <= 820) {
            bandera814 = true;
            System.out.println("<AMBITO:sintaxis>  Entro a if de 815 y 821 | 800:" + pilaSintaxis.peek());
            tipo800(pilaSintaxis.peek());
            if (claseVariable.equals("Lista")) {
                banderaListaNormal = true;
                tipoVariable = "struct";
                ambitoVariable = ambito + "";
            } else if (claseVariable.equals("Rango")) {
                banderaRango = true;
                System.out.println("<AMBITO:sintaxis> Bandera Rango");
            } else if (claseVariable.equals("Tupla")) {
                banderaTupla = true;
                tipoVariable = "struct";
                System.out.println("<AMBITO:sintaxis> Bandera Tupla");
                ambitoVariable = ambito + "";
            } else if (claseVariable.equals("Diccionario")) {
                banderaDiccionario = true;
                tipoVariable = "struct";
            }
            pilaSintaxis.pop();
        }

        /////////////////////////DICCIONARIOS///////////////////////////////
        if (banderaDiccionario) {
            if (nombreVariable == "") {
                nombreVariable = auxiliarID;
            }
            if (ambitoVariable == "") {
                ambitoVariable = ambito + "";
            }
            claseVariable = "Diccionario";
            if (pilaSintaxis.peek() == 8203 && agregarLlaveDiccionario && agregarDiccionarioLopez) {
                llaveAuxiliarDiccionario = oper.obtenerPrimero();
                agregarLlaveDiccionario = false;
                pilaSintaxis.pop();
            } else if ((pilaSintaxis.peek() == 8203 && !switchLlaveValor)
                    || (!agregarDiccionarioLopez && pilaSintaxis.peek() == 8203 && !switchLlaveValor)) {
                llaveAuxiliar = oper.obtenerPrimero();
                switchLlaveValor = true;
                pilaSintaxis.pop();
            } else if ((pilaSintaxis.peek() == 8203 && switchLlaveValor)
                    || (!agregarDiccionarioLopez && pilaSintaxis.peek() == 8203 && switchLlaveValor)) {
                switchLlaveValor = false;
                pilaSintaxis.pop();
                NodoToken valorAuxiliar = oper.obtenerPrimero();
                controladorDatoDiccionario.push(tipoConstante(pilaSintaxis.peek()),
                        ambito + "", llaveAuxiliar.getLexema(), (controladorDatoDiccionario.obtenerCantidadDatosDiccionario() + 1) + "",
                        oper.mostrarLexemaPrimero(), nombreVariable);
                if (diccionarios.get(ultimoDiccionarioCreado).getElementos() == null && !agregarDiccionarioLopez) {
                    diccionarios.get(ultimoDiccionarioCreado).addElemento(new ElementoDiccionario(
                            llaveAuxiliar.getLexema(),
                            llaveAuxiliar.getPosicion(),
                            ambito,
                            oper.mostrarLexemaPrimero(),
                            oper.mostrarTokenPrimero(),
                            null));
                    diccionarios.get(ultimoDiccionarioCreado).setTipoLlave(llaveAuxiliar.getPosicion());

                } else {
                    if (!agregarDiccionarioLopez) { // Agregar elementos normales a diccionario normal
                        diccionarios.get(ultimoDiccionarioCreado).addElemento(new ElementoDiccionario(llaveAuxiliar.getLexema(),
                                llaveAuxiliar.getPosicion(),
                                ambito,
                                oper.mostrarLexemaPrimero(),
                                oper.mostrarTokenPrimero(),
                                null));
                        if (!(diccionarios.get(ultimoDiccionarioCreado).getTipoLlave() == llaveAuxiliar.getPosicion())) {
                            bandera1060 = false;
                        }
                    } else { // Agregar diccionario a diccionario mortal
                        int tamano = diccionarios.get(ultimoDiccionarioCreado).getElementos().size();
                        if (agregarDatoDiccionario) {
                            diccionarios.get(ultimoDiccionarioCreado).getElementos().add(
                                    new ElementoDiccionario(
                                            llaveAuxiliarDiccionario.getLexema(),
                                            llaveAuxiliarDiccionario.getPosicion(),
                                            ambito,
                                            "Diccionario" + (contadorDiccionariosMortales - 1),
                                            -4,
                                            null));
                            diccionarios.get(ultimoDiccionarioCreado).getElementos().get(contadorDiccionariosMortales - 1).setArrayList();
                            agregarDatoDiccionario = false;
                        }
//                        else{
//                            diccionarios.get(ultimoDiccionarioCreado).getElementos().get(contadorDiccionariosMortales-1).getElementosHijos().add(
//                                    new ElementoDiccionario(
//                                            llaveAuxiliar.getLexema(), 
//                                            llaveAuxiliar.getPosicion(), 
//                                            ambito, 
//                                            valorAuxiliar.getLexema(), 
//                                            valorAuxiliar.getPosicion(), 
//                                            null));
//                        }                      
                    }
                }
                if (agregarDiccionarioLopez) {
                    diccionarios.get(ultimoDiccionarioCreado).getElementos().get(contadorDiccionariosMortales - 1).getElementosHijos().add(
                            new ElementoDiccionario(
                                    llaveAuxiliar.getLexema(),
                                    llaveAuxiliar.getPosicion(),
                                    ambito,
                                    valorAuxiliar.getLexema(),
                                    valorAuxiliar.getPosicion(),
                                    null));
                }
            }
        }
        /////////////////////FIN DICCIONARIOS///////////////////////////////

        ////////////////////////////TUPLAS//////////////////////////////////
        if (banderaTupla) {
            if (nombreVariable == "") {
                nombreVariable = auxiliarID;
            }
            if (ambitoVariable == "") {
                ambitoVariable = ambito + "";
            }
            if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                    || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82) {

                tuplaArreglo[contadorTupla] = new Tupla(tipoConstante(pilaSintaxis.peek()), "datoTupla",
                        ambito + "", contadorTupla + "", nombreVariable, valorVariable);
                contadorTupla++;
                tarrVariable = contadorTupla + "";
            }
        }
        ////////////////////////////FIN TUPLAS//////////////////////////////

        ////////////////////////////LISTAS//////////////////////////////////
        if (banderaListaNormal) {
            if (nombreVariable == "") {
                nombreVariable = auxiliarID;
            }
            if (ambitoVariable == "") {
                ambitoVariable = ambito + "";
            }
            if (tipoVariable == "") {
                tipoVariable = "struct";
            }
            if (claseVariable == "") {
                claseVariable = "Lista";
            }

            if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -8
                    || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82) {
                controladorDatoLista.push(tipoConstante(pilaSintaxis.peek()), ambito + "",
                        (controladorDatoLista.obtenerCantidadDatosLista() + 1) + "", nombreVariable);
            }
            if (listaPerteneceVariable == "") {
                if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -8
                        || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82) {
                    listaPerteneceVariable = tipoConstante(pilaSintaxis.peek());
                }
            } else if ((pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                    || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82)
                    && (listaPerteneceVariable != tipoConstante(pilaSintaxis.peek()) && tipoConstante(pilaSintaxis.peek()) != "")) {
                banderaListaMultiple = true;
                banderaListaNormal = false;
            }

        } else if (banderaListaMultiple) {
            if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -8
                    || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82) {
                controladorDatoLista.push(tipoConstante(pilaSintaxis.peek()), ambito + "",
                        (controladorDatoLista.obtenerCantidadDatosLista() + 1) + "", nombreVariable);
            }
        }
        ////////////////////////////FIN LISTAS//////////////////////////////

        //////////////////////////////RANGO/////////////////////////////////
        if (banderaRango && (pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -10 || pilaSintaxis.peek() == -11
                || pilaSintaxis.peek() == -12) && rango1 == "") {
            System.out.println("<AMBITO:sintaxis> Agregar Rango 1");
            rango1 = oper.mostrarLexemaPrimero();
        } else if (banderaRango && (pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -10 || pilaSintaxis.peek() == -11
                || pilaSintaxis.peek() == -12) && !rango1.equals("") && rango2.equals("")) {
            System.out.println("<AMBITO:sintaxis> Agregar Rango 2");
            rango2 = oper.mostrarLexemaPrimero();
        } else if (banderaRango && (pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -10 || pilaSintaxis.peek() == -11
                || pilaSintaxis.peek() == -12) && !rango1.equals("") && !rango2.equals("") && avance.equals("")) {
            System.out.println("<AMBITO:sintaxis> Agregar Avance");
            avance = oper.mostrarLexemaPrimero();
        }
        //////////////////////////////FIN DE RANGO//////////////////////////

        //Activadores auxiliares de ambito
        switch (pilaSintaxis.peek()) {
            case 900:
                valorVariable = oper.mostrarLexemaPrimero();
                pilaSintaxis.pop();
                break;
            case 901:
                tipoVariable = "fun";
                claseVariable = "None";
                pilaSintaxis.pop();
                ambitoVariable = ambito + "";
//                    aumentarAmbito();
                ambitoCreado = ambito + "";
                banderaFuncion = true;
                nombreFuncion = oper.mostrarLexemaPrimero();
                break;
        }
//            if(pilaSintaxis.peek()==805){
//                pilaSintaxis.pop();
//            }
        return pilaSintaxis;
    }

    private void agregarVariableBaseDatos(String produce) {
        valorVariable = valorVariable.replace("'", "\"");
        System.out.println("----Agregar Variable----");
        System.out.println("<AMBITO:agregarVariable> " + "Variable: " + nombreVariable
                + ", Tipo: " + claseVariable
                + ", Ambito: " + ambito
                + ", Clase: " + tipoVariable);
        String query;
        boolean variableDuplicada = variableDuplicada(nombreVariable);
        boolean variableDeclarada = variableDeclarada(nombreVariable);
        switch (claseVariable) {
            case "Decimal":
            case "Binario":
            case "Octal":
            case "Hexadecimal":
            case "Flotante":
            case "Cadena":
            case "Caracter":
            case "Compleja":
            case "Booleana":
            case "datoTupla":
            case "datoLista":
                if (areaDeclaracion && !variableDuplicada) {
                    aumentarContadorAmbito();
                    tipoVariable = "var";
                    ambitoVariable = ambito + "";
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,valor) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + valorVariable + "');";
                    controladorSQL.ejecutarQuery(query);
                    variableArreglo[contadorVariablesArreglo] = new Variable(nombreVariable, ambitoVariable);
                } else if (!areaDeclaracion) {

                    if (variableDeclarada) {
                    } else {
                        controladorTokenError.agregarError(1001, "Variable " + nombreVariable + " no declarada, area de declaracion: " + areaDeclaracion,
                                "Ambito: " + ambito + "", oper.mostrarLineaPrimero(), "Ambito");
//                            errores[nR] = new Estructuras.Error(1001, "Variable " + nombreVariable + " no declarada",
//                                    "Ambito: " + ambito + "", 0, "Ambito");
                    }
                } else if (variableDuplicada) {
                    controladorTokenError.agregarError(1002, "Variable " + nombreVariable + " duplicada",
                            "Ambito: " + ambito + ", Tipo: " + tipoVariable, oper.mostrarLineaPrimero(), "Ambito");
//                        errores[nR] = new Estructuras.Error(1002, "Variable " + nombreVariable + " duplicada",
//                                "Ambito: " + ambito + ", Tipo: " + tipoVariable, 0, "Ambito");
                }

                valorVariable = "";
                banderaConstante = false;
                break;
            case "None":
                if (areaDeclaracion && !variableDuplicada) {
                    if (banderaFuncion && claseVariable != "par") {
                        ambitoVariable = ambito + "";
                        aumentarAmbito();
                        ambitoCreado = ambito + "";
                    }
                    if (tipoVariable == "par") {
                        query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado, noPos, listaPertenece) VALUES("
                                + "'" + nombreVariable + "',"
                                + "'" + claseVariable + "',"
                                + "'" + tipoVariable + "',"
                                + "'" + ambitoVariable + "',"
                                + "'" + ambitoCreado + "',"
                                + "'" + contadorParametros + "',"
                                + "'" + nombreFuncion + "');";
                    } else {
                        query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
                                + "'" + nombreVariable + "',"
                                + "'" + claseVariable + "',"
                                + "'" + tipoVariable + "',"
                                + "'" + ambitoVariable + "',"
                                + "'" + ambitoCreado + "');";
                    }
                    aumentarContadorAmbito();
                    controladorSQL.ejecutarQuery(query);
                } else {
                    if (!areaDeclaracion) {
                        if (variableDeclarada) {

                        } else {
                            controladorTokenError.agregarError(1001, "Variable " + nombreVariable + " no declarada",
                                    "Ambito: " + ambito + "", oper.mostrarLineaPrimero(), "Ambito");
//                            errores[nR] = new Estructuras.Error(1001, "Variable " + nombreVariable + " no declarada",
//                                    "Ambito: " + ambito + "", 0, "Ambito");
                        }
                    } else if (variableDuplicada) {
                        controladorTokenError.agregarError(1002, "Variable " + nombreVariable + " duplicada",
                                "Ambito: " + ambito + ", Tipo: " + tipoVariable, oper.mostrarLineaPrimero(), "Ambito");
//                        errores[nR] = new Estructuras.Error(1002, "Variable " + nombreVariable + " duplicada",
//                                "Ambito: " + ambito + ", Tipo: " + tipoVariable, 0, "Ambito");
                    }
                }
                bandera814 = false;
                ambitoCreado = "";
                banderaFuncion = false;
//                if (banderaFuncion) {
//                    reducirAmbito();   
//                }
                break;
            case "Tupla":
                if (areaDeclaracion && !variableDuplicada) {
                    aumentarAmbito();
                    ambitoCreado = ambito + "";
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + ambitoCreado + "');";
                    auxiliarNombreVariable = nombreVariable;
                    controladorSQL.ejecutarQuery(query);
                    banderaTupla = false;
                    agregarTupla = false;
                    bandera814 = false;
                    System.out.println("contadorTupla: " + contadorTupla);
                    for (int i = 0; i < contadorTupla; i++) {
                        tuplaArreglo[i].setAmb(ambito + "");
                        String tipoE = tuplaArreglo[i].getTipo();
                        String claseE = tuplaArreglo[i].getClase();
                        String ambitoE = tuplaArreglo[i].getAmb();
                        String noPosE = (Integer.parseInt(tuplaArreglo[i].getNoPos()) + 1) + "";
                        String listaPerE = tuplaArreglo[i].getListaPertenece();
                        String valorDatoTupla = tuplaArreglo[i].getValor();
                        query = "INSERT INTO tablasimbolos (clase,tipo,ambito,noPos,listaPertenece,valor) VALUES("
                                + "'" + claseE + "',"
                                + "'" + tipoE + "',"
                                + "'" + ambitoE + "',"
                                + "'" + noPosE + "',"
                                + "'" + listaPerE + "',"
                                + "'" + valorDatoTupla + "');";
                        System.err.println("ambito: " + ambito);
                        System.err.println("datoEstructura: " + contadorAmbitoArr[ambito].DatoEstructura);
                        contadorAmbitoArr[ambito].DatoEstructura++;
                        controladorSQL.ejecutarQuery(query);
                        query = "UPDATE tablasimbolos SET tamanoArreglo = '" + (contadorTupla) + "' where id='" + auxiliarNombreVariable + "';";
                        controladorSQL.ejecutarQuery(query);
                    }
                }
                contadorElementosLista = 0;
                reducirAmbito();
                contadorTupla = 0;
                bandera814 = false;
                break;
            case "Lista":
                if (banderaListaNormal) {
                    analizadorSemantica2.asignarArreglo(nombreVariable, ambitoVariable, Integer.parseInt(ambitoVariable),
                            oper.mostrarLineaPrimero());
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,tamanoArreglo) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + "Arreglo" + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + (controladorDatoLista.obtenerCantidadDatosLista()) + "');";
                    aumentarContadorAmbito();
                    controladorSQL.ejecutarQuery(query);
                } else if (banderaListaMultiple) {
                    aumentarAmbito();
                    ambitoCreado = ambito + "";
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado, tamanoArreglo) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + ambitoCreado + "',"
                            + "'" + (controladorDatoLista.obtenerCantidadDatosLista()) + "');";
                    auxiliarNombreVariable = nombreVariable;
                    aumentarContadorAmbito();
                    controladorSQL.ejecutarQuery(query);

                    tamVariablesGuardadasArr++;
                    System.out.println("contadorElemenosLista: " + contadorElementosLista);
                    Lista[] auxiliar = controladorDatoLista.obtenerDatosLista();
                    for (int i = 0; i < controladorDatoLista.obtenerCantidadDatosLista(); i++) {
                        query = "INSERT INTO tablasimbolos (clase,tipo,ambito,noPos,listaPertenece) VALUES("
                                + "'" + auxiliar[i].getClase() + "',"
                                + "'" + auxiliar[i].getTipo() + "',"
                                + "'" + ambito + "',"
                                + "'" + auxiliar[i].getNoPos() + "',"
                                + "'" + auxiliar[i].getListaPertenece() + "');";
                        contadorAmbitoArr[ambito].DatoEstructura++;
                        controladorSQL.ejecutarQuery(query);
                    }

                    reducirAmbito();
                    controladorDatoLista.vaciarDatosLista();
                    listaPerteneceVariable = "";
                    contadorElementosLista = 0;
                    agregarLista = false;
                    bandera814 = false;
                    banderaListaMultiple = false;
                    banderaListaNormal = true;
                    ambitoVariable = "";
                    claseVariable = "";
                    ambitoCreado = "";
                    tipoVariable = "";
                }

                controladorDatoDiccionario.vaciarDatosDiccionario();
                agregarDiccionario = false;
                contadorDiccionario = 0;
                contadorDiccionariosMortales = 0;
                agregarDiccionarioLopez = false;
                agregarDatoDiccionario = false;
                agregarLlaveDiccionario = true;
                switchLlaveValor = false;
                banderaDiccionario = false;
                bandera1060 = true;
                bandera814 = false;
                break;
            case "Rango":
                tipoVariable = "struct";
                ambitoVariable = ambito + "";
                String r1 = rango1 + ", " + rango2;
                aumentarContadorAmbito();
                query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,rango,avance) VALUES("
                        + "'" + nombreVariable + "',"
                        + "'" + claseVariable + "',"
                        + "'" + tipoVariable + "',"
                        + "'" + ambitoVariable + "',"
                        + "'" + r1 + "',"
                        + "'" + avance + "');";
                controladorSQL.ejecutarQuery(query);

//                tablaSimbolos[tamVariablesGuardadasArr] = new TablaSimbolos(nombreVariable, claseVariable,
//                        tipoVariable, ambito + "", null, null, null, null, r1 + "", avance, null, null);
                banderaRango = false;
                rango1 = "";
                rango2 = "";
                avance = "";
                bandera814 = false;
                break;
//            case "Conjunto":
//
//                aumentarAmbito();
//                ambitoCreado = ambito + "";
//                aumentarContadorAmbito();
//                query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
//                        + "'" + nombreVariable + "',"
//                        + "'" + claseVariable + "',"
//                        + "'" + tipoVariable + "',"
//                        + "'" + ambitoVariable + "',"
//                        + "'" + ambitoCreado + "');";
//                auxiliarNombreVariable = nombreVariable;
//                controladorSQL.ejecutarQuery(query);
//                System.out.println("contadorConjunto: " + contadorConjunto);
//                for (int i = 0; i < contadorConjunto; i++) {
//                    conjunto[i].setAmb(ambito + "");
//                    String tipoE = conjunto[i].getTipo();
//                    String claseE = conjunto[i].getClase();
//                    String ambitoE = conjunto[i].getAmb();
//                    String noPosE = conjunto[i].getNoPosicion();
//                    String listaPerE = conjunto[i].getListaPertenece();
//                    aumentarContadorAmbito();
//                    query = "INSERT INTO tablasimbolos (clase,tipo,ambito,noPos,listaPertenece) VALUES("
//                            + "'" + claseE + "',"
//                            + "'" + tipoE + "',"
//                            + "'" + ambitoE + "',"
//                            + "'" + noPosE + "',"
//                            + "'" + listaPerE + "');";
//                    controladorSQL.ejecutarQuery(query);
//                    query = "UPDATE tablasimbolos SET tamanoArreglo = '" + contadorConjunto + "' where id='" + auxiliarNombreVariable + "';";
//                    controladorSQL.ejecutarQuery(query);
//                }
//                agregarConjunto = false;
//                contadorConjunto = 0;
//                reducirAmbito();
//                banderaConjunto = false;
//                agregarConjunto = false;
//                bandera814 = false;
//                break;
            case "Diccionario":
                if (bandera1060) {
                    analizadorSemantica2.agregarRegla(1060, nodoDiccionarioCreado.getNumlinea(), ambito, true);
                } else {
                    analizadorSemantica2.agregarRegla(1060, nodoDiccionarioCreado.getNumlinea(), ambito, false);
                }
                analizadorSemantica2.agregarRegla(1061, nodoDiccionarioCreado.getNumlinea(), ambito, true);
                System.out.println("InsertarDiccionario");
                aumentarAmbito();
                ambitoCreado = ambito + "";
                verificarLlavesDiccionariosHijos();
                aumentarContadorAmbito();
                query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado,tamanoArreglo) VALUES("
                        + "'" + nombreVariable + "',"
                        + "'" + claseVariable + "',"
                        + "'" + tipoVariable + "',"
                        + "'" + ambitoVariable + "',"
                        + "'" + ambitoCreado + "',"
                        + "'" + (controladorDatoDiccionario.obtenerCantidadDatosDiccionario()) + "');";
                auxiliarNombreVariable = nombreVariable;
                controladorSQL.ejecutarQuery(query);
                System.out.println("contadorDiccionario: " + contadorDiccionario);
                Diccionario[] auxiliar = controladorDatoDiccionario.obtenerDatosDiccionario();
                for (int i = 0; i < auxiliar.length; i++) {
                    auxiliar[i].setAmb(ambito + "");
                    query = "INSERT INTO tablasimbolos (clase,tipo,ambito,valor,noPos,llave,listaPertenece) VALUES("
                            + "'" + auxiliar[i].getClase() + "',"
                            + "'" + auxiliar[i].getTipo() + "',"
                            + "'" + auxiliar[i].getAmb() + "',"
                            + "'" + auxiliar[i].getLlave() + "',"
                            + "'" + auxiliar[i].getNoPosicion() + "',"
                            + "'" + auxiliar[i].getValor() + "',"
                            + "'" + auxiliar[i].getListaPertenece() + "');";
                    controladorSQL.ejecutarQuery(query);
                    query = "UPDATE tablasimbolos SET tamanoArreglo = '" + contadorDiccionario + "' where id='" + auxiliarNombreVariable + "';";
                    controladorSQL.ejecutarQuery(query);
                }
                controladorDatoDiccionario.vaciarDatosDiccionario();
                agregarDiccionario = false;
                contadorDiccionario = 0;
                contadorDiccionariosMortales = 0;
                agregarDiccionarioLopez = false;
                agregarDatoDiccionario = false;
                agregarLlaveDiccionario = true;
                switchLlaveValor = false;
                reducirAmbito();
                banderaDiccionario = false;
                bandera1060 = true;
                bandera814 = false;

//                controladorDatoLista.vaciarDatosLista();
//                listaPerteneceVariable = "";
//                contadorElementosLista = 0;
//                agregarLista = false;
//                bandera814 = false;
//                banderaListaMultiple = false;
//                banderaListaNormal = true;
//                ambitoVariable = "";
//                claseVariable = "";
//                ambitoCreado = "";
//                tipoVariable = "";
                break;
        }
        tamVariablesGuardadasArr = 0;
        ultimoDiccionarioCreado = "";
        agregarDiccionarioLopez = false;
        banderaListaNormal = true;
        claseVariable = "";
        nombreVariable = "";
        tipoVariable = "";
        valorVariable = "";
        ambitoCreado = "";
        tarrVariable = "";
        if (banderaParametro) {
            claseVariable = "None";
            tipoVariable = "par";
        }
        System.out.println("---- Fin Agregar Variable----");
    }

    private void verificarLlavesDiccionariosHijos() {
//        int tokenLlaves = 0;
//        
//        ArrayList<String> listOfKeys = diccionarios.keySet().stream().collect(
//                Collectors.toCollection(ArrayList::new));
//        ArrayList<Modelos.Semantica2.Diccionario> listOfValues = diccionarios.values().stream().collect(
//                Collectors.toCollection(ArrayList::new));
//        for (int j = 0; j < listOfValues.get(listOfValues.size() - 1).getElementos().size(); j++) {
//            boolean banderaDiccionaritos1060 = true;
//            if (listOfValues.get(listOfValues.size() - 1).getElementos().get(listOfValues.size() - 1).getElementosHijos() != null) {
//                for (int k = 0; k < listOfValues.get(listOfValues.size() - 1).getElementos().get(j).getElementosHijos().size(); k++) {
//                    if (tokenLlaves == 0){
//                        tokenLlaves = listOfValues.get(listOfValues.size() - 1).getElementos().get(j).getElementosHijos().get(k).getTokenLlave();
//                    }else if (listOfValues.get(listOfValues.size() - 1).getElementos().get(j).getElementosHijos().get(k).getTokenLlave() != tokenLlaves){
//                        banderaDiccionaritos1060 = false;
//                    }
//                }
//            }
//            analizadorSemantica2.agregarRegla(1060, nodoDiccionarioCreado.getNumlinea(), ambito, banderaDiccionaritos1060);
//        }

    }

    private void aumentarAmbito() {
        pilaAmbito.push(ambitoActualDisponible);
        ambito = pilaAmbito.peek();
        if (ambito >= ambitoMayor) {
            ambitoMayor = ambito;
        }
        aumentarArregloAmbito();
        System.out.println("<AUMENTAR AMBITO> Se ha aumentado el ambito");
        ambitoActualDisponible++;
    }

    private void aumentarArregloAmbito() {
        ContadorAmbito aux[] = contadorAmbitoArr;
        contadorAmbitoArr = new ContadorAmbito[aux.length + 1];
        contadorAmbitoArr[aux.length] = new ContadorAmbito();
        for (int i = 0; i < aux.length; i++) {
            contadorAmbitoArr[i] = new ContadorAmbito();
            contadorAmbitoArr[i] = aux[i];
        }
    }

    private void reducirAmbito() {
        pilaAmbito.pop();
        ambito = pilaAmbito.peek();
    }

    private void tipo800(int arroba) {
        String tipo;
        if (areaDeclaracion) {
            switch (arroba) {
                case 805:
                    tipo = "Decimal";
                    break;
                case 806:
                    tipo = "Binario";
                    break;
                case 807:
                    tipo = "Octal";
                    break;
                case 808:
                    tipo = "Hexadecimal";
                    break;
                case 809:
                    tipo = "Flotante";
                    break;
                case 810:
                    tipo = "Cadena";
                    break;
                case 811:
                    tipo = "Caracter";
                    break;
                case 812:
                    tipo = "Compleja";
                    break;
                case 813:
                    tipo = "Booleana";
                    break;
                case 814:
                    tipo = "None";
                    break;
                case 816:
                    tipo = "Tupla";
                    break;
                case 815:
                    tipo = "Lista";
                    break;
                case 817:
                    tipo = "Lista";
                    break;
                case 818:
                    tipo = "Registro";
                    break;
                case 819:
                    tipo = "Rango";
                    break;
                case 820:
                    tipo = "Diccionario";
                    break;
                default:
                    tipo = "Desconocido";
            }
            if (banderaParametro) {
                tipo = "None";
                tipoVariable = "par";
            }
            claseVariable = tipo;
        }
    }

    private String tipoConstante(int token) {
        String tipo = "";
        switch (token) {
            case -7:
                tipo = "Decimal";
                break;
            case -9:
                tipo = "Complejo";
                break;
            case -10:
                tipo = "Binario";
                break;
            case -12:
                tipo = "Hexadecimal";
                break;
            case -8:
                tipo = "Flotante";
                break;
            case -4:
                tipo = "Cadena";
                break;
            case -1:
                tipo = "Caracter";
                break;
            case -81:
            case -82:
                tipo = "Boolean";
                break;
            default:
                return tipo;
        }
        return tipo;
    }

    private boolean variableDuplicada(String id) {
        return controladorSQL.comprobarVariableDuplicada(id, ambitoVariable);
    }

    private boolean variableDeclarada(String id) {
        return controladorSQL.comprobarVariableDeclarada(id, ambitoVariable);
    }

    private void aumentarContadorAmbito() {
        switch (claseVariable) {
            case "Decimal":
                contadorAmbitoArr[ambito].Decimal++;
                break;
            case "Binario":
                contadorAmbitoArr[ambito].Binario++;
                break;
            case "Octal":
                contadorAmbitoArr[ambito].Octal++;
                break;
            case "Hexadecimal":
                contadorAmbitoArr[ambito].Hexadecimal++;
                break;
            case "Flotante":
                contadorAmbitoArr[ambito].Flotante++;
                break;
            case "Cadena":
                contadorAmbitoArr[ambito].Cadena++;
                break;
            case "Caracter":
                contadorAmbitoArr[ambito].Caracter++;
                break;
            case "Compleja":
                contadorAmbitoArr[ambito].Compleja++;
                break;
            case "Booleana":
                contadorAmbitoArr[ambito].Booleana++;
                break;
            case "None":
                contadorAmbitoArr[ambito].None++;
                break;
            case "Tupla":
                contadorAmbitoArr[ambito].Tupla++;
                break;
            case "Lista":
                contadorAmbitoArr[ambito].Lista++;
                break;
            case "Rango":
                contadorAmbitoArr[ambito].Rango++;
                break;
            case "Diccionario":
                contadorAmbitoArr[ambito].Diccionario++;
                break;
            case "datoTupla":
            case "datoLista":
                contadorAmbitoArr[ambito].DatoEstructura++;
                break;
        }
    }

    public int[][] obtenerContadorAmbito() {
        /////////////////////CREACION DE ARREGLOS DE CONTEO DE VARIABLES Y AMBITOS
        int contadorAmbito[][] = new int[(ambitoMayor + 4)][19];
        for (int i = 0; i < contadorAmbito.length; i++) {
            for (int j = 0; j < contadorAmbito[i].length; j++) {
                contadorAmbito[i][j] = 0;
            }
        }

        try {
            int j = 0, sumatoriaTotalVariablesEnAmbito = 0;
            for (int i = 1; i < ambitoMayor + 1; i++) {
                if (j <= ambito) {
                    contadorAmbito[i][0] = j;
                    contadorAmbito[i][1] = obtenerNumeroBD(j + "", "Decimal", "Tipo");
                    contadorAmbito[i][2] = obtenerNumeroBD(j + "", "Binario", "Tipo");
                    contadorAmbito[i][3] = obtenerNumeroBD(j + "", "Octal", "Tipo");
                    contadorAmbito[i][4] = obtenerNumeroBD(j + "", "Hexadecimal", "Tipo");
                    contadorAmbito[i][5] = obtenerNumeroBD(j + "", "FLotante", "Tipo");
                    contadorAmbito[i][6] = obtenerNumeroBD(j + "", "Cadena", "Tipo");
                    contadorAmbito[i][7] = obtenerNumeroBD(j + "", "Caracter", "Tipo");
                    contadorAmbito[i][8] = obtenerNumeroBD(j + "", "Compleja", "Tipo");
                    contadorAmbito[i][9] = obtenerNumeroBD(j + "", "Booleana", "Tipo");
                    contadorAmbito[i][10] = obtenerNumeroBD(j + "", "None", "Tipo");
                    contadorAmbito[i][11] = obtenerNumeroBD(j + "", "Arreglo", "Clase");
                    contadorAmbito[i][12] = obtenerNumeroBD(j + "", "Tuplas", "Clase");
                    contadorAmbito[i][13] = obtenerNumeroBD(j + "", "Lista", "Clase");
                    contadorAmbito[i][14] = obtenerNumeroBD(j + "", "Rango", "Clase");
                    contadorAmbito[i][15] = obtenerNumeroBD(j + "", "Diccionario", "Clase");
                    for (int k = 1; k < contadorAmbito[i].length; k++) {
                        sumatoriaTotalVariablesEnAmbito += contadorAmbito[i][k];
                    }
                    contadorAmbito[i][18] = sumatoriaTotalVariablesEnAmbito;
                    sumatoriaTotalVariablesEnAmbito = 0;
                    j++;
                }
            }
            int sumatoriaDecimales = 0;
            for (int q = 1; q < 19; q++) {
                for (int i = 1; i < ambitoMayor + 2; i++) {
                    sumatoriaDecimales += contadorAmbito[i][q];
                    if (i == ambitoMayor + 1) {
                        contadorAmbito[ambitoMayor + 3][q] = sumatoriaDecimales;
                        sumatoriaDecimales = 0;
                    }
                }
            }

        } catch (SQLException ex) {
            //Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contadorAmbito;
    }

    public void establecerNombreVariable(String nombre) {
        this.nombreVariable = nombre;
    }

    private int obtenerNumeroBD(String ambito, String campo, String tipoOClase) throws SQLException {
        int total = 0;
        ResultSet rs = controladorSQL.obtenerResultSet("SELECT COUNT(*) FROM tablasimbolos WHERE Ambito=" + "'" + ambito + "'" + " AND " + tipoOClase + "= '" + campo + "'");
        while (rs.next()) {
            System.out.println("Simon carnal si cuenta " + rs.getInt("count(*)"));
            return rs.getInt("count(*)");
        }
        return total;
    }

    private void actualizarCantidadParametrosFuncion() {
        controladorSQL.ejecutarQuery("UPDATE tablasimbolos SET tamanoArreglo='" + contadorParametros + "' WHERE id= BINARY '" + nombreFuncion + "'");
    }

    public boolean obtenerBanderaArreglo() {
        return banderaArreglo;
    }

    public void establecerTamArr(int tamArr) {
        this.tamArr = tamArr;
    }

    public int obtenerTamArr() {
        return this.tamArr;
    }

    public int obtenerAmbito() {
        return ambito;
    }

    public ContadorAmbito[] obtenerContadorEstructurasAmbito() {
        return contadorAmbitoArr;
    }
}
