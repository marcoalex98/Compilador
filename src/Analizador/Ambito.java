/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Contadores.ContadorAmbito;
import Controladores.ControladorTokenError;
import Estructuras.Conjunto;
import Estructuras.Controladores.ControladorDatoLista;
import Estructuras.DatoDiccionario;
import Estructuras.Lista;
import Estructuras.OperToken;
import Estructuras.Tupla;
import Estructuras.Variable;
import SQL.ControladorSQL;
import SQL.TablaSimbolos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Ambito {

    int contadorIdentificadores, ambito, tamArr, tamVariablesGuardadasArr,
            contadorConjunto, contadorDiccionario, contadorElementosLista,
            ambitoActualDisponible, contadorTupla, contadorVariablesArreglo,
            ambitoMayor, auxiliarTipoVariable, contadorParametros;
    String claseVariable, nombreVariable, auxiliarNombreVariable, tipoVariable, valorVariable,
            ambitoCreado, ambitoVariable, tarrVariable, listaPerteneceVariable, rango1,
            rango2, avance, auxiliarID, auxiliarDato, nombreFuncion;
    boolean estadoError, banderaParametro, banderaArreglo, banderaTupla, banderaListaMultiple,
            banderaRango, banderaListaNormal, agregarLista, banderaFor, agregarTupla,
            agregarConjunto, agregarDiccionario, banderaConstante, banderaFuncion,
            banderaConjunto, banderaAgregandoConjuntos, banderaDiccionario, asignarValor,
            bandera814, areaDeclaracion, primeraVezTipoLista, llaveDiccionario;
    Stack<Integer> pilaSintaxis;
    Stack<Integer> pilaAmbito;
    ControladorSQL controladorSQL;
    TablaSimbolos tablaSimbolos[];
    DatoDiccionario diccionario[];
    Conjunto conjunto[];
    Lista[] listaArreglo;
    Tupla[] tuplaArreglo;
    Variable variableArreglo[];
    ContadorAmbito contadorAmbitoArr[];
    ControladorTokenError controladorTokenError;
    OperToken oper;
    ControladorDatoLista controladorDatoLista;

    public Ambito(ControladorSQL controladorSQL,
            ControladorTokenError controladorTokenError) {
        this.controladorSQL = controladorSQL;
        this.controladorTokenError = controladorTokenError;
        this.controladorDatoLista = new ControladorDatoLista();
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
        auxiliarDato = "";
        nombreFuncion = "";
        banderaConstante = false;
        banderaFuncion = false;
        banderaConjunto = false;
        banderaAgregandoConjuntos = false;
        banderaDiccionario = false;
        asignarValor = false;
        bandera814 = false;
        llaveDiccionario = false;
        areaDeclaracion = true;
        primeraVezTipoLista = true;
        ambito = 0;
        ambitoMayor = 0;
        contadorElementosLista = 0;
        contadorParametros = 0;
        ambitoActualDisponible = 0;
        contadorTupla = 0;
        contadorDiccionario = 0;
        contadorVariablesArreglo = 0;
        auxiliarTipoVariable = 0;
        conjunto = new Conjunto[400];
        listaArreglo = new Lista[400]; // este tenia [3]
        tuplaArreglo = new Tupla[400];
        diccionario = new DatoDiccionario[400];
        tablaSimbolos = new TablaSimbolos[500];
        variableArreglo = new Variable[400];
        contadorAmbitoArr = new ContadorAmbito[1];
        contadorAmbitoArr[0] = new ContadorAmbito();
        pilaSintaxis = new Stack<Integer>();
        pilaAmbito = new Stack<Integer>();
        pilaAmbito.push(ambitoActualDisponible);
        ambitoActualDisponible++;
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
//                reducirAmbito();
        }
        if (pilaSintaxis.peek() == 8162) {
            pilaSintaxis.pop();
            agregarTupla = true;
            //reducirAmbito();
        }
        if (pilaSintaxis.peek() == 8202) {
            pilaSintaxis.pop();
            if (banderaConjunto) {
                System.out.println("<AMBITO:sintaxis> Bandera Agregar Conjunto");
                agregarConjunto = true;
            } else if (banderaDiccionario) {
                System.out.println("<AMBITO:sintaxis> Bandera Agregar Diccionario");
                agregarDiccionario = true;
            }
        }
        if (pilaSintaxis.peek() == 840) {//For
            pilaSintaxis.pop();
            banderaFor = true;
        }

        if (banderaDiccionario) {
            if (pilaSintaxis.peek() == 821) {
                pilaSintaxis.pop();
            }
        }

        if (nombreVariable != "" && claseVariable != "" && valorVariable != "" && banderaConstante) {
            System.out.println("<AMBITO:sintaxis> Agregar Variable Constante");
            agregarVariableBaseDatos();
        } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && banderaFuncion) {
            System.out.println("<AMBITO:sintaxis> Agregar Variable Funcion");
            agregarVariableBaseDatos();
        } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && banderaParametro) {
            System.out.println("<AMBITO:sintaxis> Agregar Variable Parametro");
            agregarVariableBaseDatos();
        } else if (nombreVariable != "" && claseVariable != "" && rango1 != "" && rango2 != ""
                && avance != "" && banderaRango) {
            System.out.println("<AMBITO:sintaxis> Agregar Variable Rango");
            agregarVariableBaseDatos();
        } else if (agregarLista) {
            System.out.println("<AMBITO:sintaxis> Agregar Variable Lista");
            agregarVariableBaseDatos();
            primeraVezTipoLista = true;
            auxiliarTipoVariable = 0;
            contadorElementosLista = 0;
            banderaListaMultiple = false;
            banderaListaNormal = false;
            agregarLista = false;
        } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && ambitoVariable != ""
                && tarrVariable != "" && banderaTupla && agregarTupla) {
            System.out.println("<AMBITO:sintaxis> Agregar Variable Tupla");
            agregarVariableBaseDatos();
        } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && ambitoVariable != ""
                && tarrVariable != "" && banderaConjunto && agregarConjunto) {
            System.out.println("<AMBITO:sintaxis> Agregar Variable Conjunto");
            agregarVariableBaseDatos();
        } else if (nombreVariable != "" && claseVariable != "" && tipoVariable != "" && ambitoVariable != ""
                && tarrVariable != "" && banderaDiccionario && agregarDiccionario) {
            System.out.println("<AMBITO:sintaxis> Agregar Variable Diccionario");
            agregarVariableBaseDatos();
        }

        if (pilaSintaxis.peek() == -6) {
            auxiliarID = oper.mostrarLexemaPrimero();
        }

        if (banderaFor) {
            if (pilaSintaxis.peek() == -6) {
                System.out.println("simonfor6");
                System.out.println("nombreVariable: " + auxiliarID);
                System.out.println("oper.primero: " + oper.mostrarLexemaPrimero());
                nombreVariable = auxiliarID;
            }
            if (oper.mostrarPrimero() == -7) {
                aumentarAmbito();
                System.out.println("simonfor7");
                valorVariable = oper.mostrarLexemaPrimero();
                claseVariable = "Decimal";
                banderaConstante = true;
            }
        }

        if (pilaSintaxis.peek() == 8402) {
            reducirAmbito();
        }

        switch (pilaSintaxis.peek()) {
            case 801:
                System.out.println("<AMBITO> AREA DE DECLARACION HA SIDO DESACTIVADA");
                areaDeclaracion = false;
                pilaSintaxis.pop();
                break;
            case 802:
                System.out.println("<AMBITO> AREA DE DECLARACION HA SIDO ACTIVADA");
                areaDeclaracion = true;
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
        } else if (pilaSintaxis.peek() > 814 && pilaSintaxis.peek() <= 821) {
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
            } else if (claseVariable.equals("Conjunto") && !banderaDiccionario) {
                banderaConjunto = true;
                tipoVariable = "struct";
                System.out.println("<AMBITO:sintaxis> Bandera Conjunto");
                ambitoVariable = ambito + "";
            } else if (claseVariable.equals("Diccionario")) {
                banderaDiccionario = true;
                banderaConjunto = false;
                tipoVariable = "struct";
                System.out.println("<AMBITO:sintaxis> Bandera Diccionario");
                ambitoVariable = ambito + "";
            }
            pilaSintaxis.pop();
        }
        /////////////////////////DICCIONARIOS///////////////////////////////
        if (banderaDiccionario) {
            banderaConjunto = false;
            if (nombreVariable == "") {
                nombreVariable = auxiliarID;
            }
            if (ambitoVariable == "") {
                ambitoVariable = ambito + "";
            }
            if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                    || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81
                    || pilaSintaxis.peek() == -82 && !llaveDiccionario) {
                auxiliarDato = oper.mostrarLexemaPrimero();
                llaveDiccionario = true;
            } else if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                    || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81
                    || pilaSintaxis.peek() == -82 && llaveDiccionario) {
                System.out.println("AGREGAR DICCIONARIO");
                diccionario[contadorDiccionario] = new DatoDiccionario(tipoConstante(pilaSintaxis.peek()),
                        "datoDic", ambito + "", auxiliarDato, contadorDiccionario + "", oper.mostrarLexemaPrimero(), nombreVariable);
                contadorDiccionario++;
                llaveDiccionario = false;
            }
        }
        /////////////////////FIN DICCIONARIOS///////////////////////////////

        //////////////////////////CONJUNTOS/////////////////////////////////
        if (banderaConjunto && !banderaDiccionario) {
            if (nombreVariable == "") {
                nombreVariable = auxiliarID;
            }
            if (ambitoVariable == "") {
                ambitoVariable = ambito + "";
            }
            if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                    || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81
                    || pilaSintaxis.peek() == -82) {
                conjunto[contadorConjunto] = new Conjunto(tipoConstante(pilaSintaxis.peek()), "datoConj",
                        ambito + "", contadorConjunto + "", nombreVariable);
                contadorConjunto++;
                tarrVariable = contadorConjunto + "";
            }
        }
        //////////////////////////FIN CONJUNTOS/////////////////////////////

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
            
            if(pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -8 
                    || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82){
                controladorDatoLista.push(tipoConstante(pilaSintaxis.peek()), ambito+"",
                    (controladorDatoLista.obtenerCantidadDatosLista()+1)+"", nombreVariable);
            }
            if (listaPerteneceVariable == "") {
                switch (pilaSintaxis.peek()) {
                    case -4:
                    case -7:
                    case -8:
                    case -81:
                    case -82:
                        listaPerteneceVariable = tipoConstante(pilaSintaxis.peek());
                        break;
                }
            } else if((pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
                    || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82) &&
                    (listaPerteneceVariable != tipoConstante(pilaSintaxis.peek()) && tipoConstante(pilaSintaxis.peek()) != "")){
                banderaListaMultiple = true;
                banderaListaNormal = false;
            }

        } else if (banderaListaMultiple) {
            if(pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7 || pilaSintaxis.peek() == -8 
                    || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82){
                controladorDatoLista.push(tipoConstante(pilaSintaxis.peek()), ambito+"",
                    (controladorDatoLista.obtenerCantidadDatosLista()+1)+"", nombreVariable);
            }
        }
//        if (banderaListaNormal && !banderaListaMultiple) {
//            if (nombreVariable == "") {
//                nombreVariable = auxiliarID;
//            }
//            if (ambitoVariable == "") {
//                ambitoVariable = ambito + "";
//            }
//            if (tipoVariable == "") {
//                tipoVariable = "struct";
//            }
//            if (claseVariable == "") {
//                claseVariable = "Lista";
//            }
//            if ((pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
//                    || pilaSintaxis.peek() == -8 || (pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82))
//                    && primeraVezTipoLista) {
//                primeraVezTipoLista = false;
//                auxiliarTipoVariable = pilaSintaxis.peek();
//                listaArreglo[contadorElementosLista] = new Lista(tipoConstante(pilaSintaxis.peek()), "datoLista",
//                        (ambito + 1) + "", contadorElementosLista + "", nombreVariable);
//                contadorElementosLista++;
//                tarrVariable = contadorElementosLista + "";
//            } else if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
//                    || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81
//                    || pilaSintaxis.peek() == -82) {
//                tarrVariable = contadorElementosLista + "";
//                if (pilaSintaxis.peek() == auxiliarTipoVariable
//                        || ((pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82)
//                        && (auxiliarTipoVariable == -81 || auxiliarTipoVariable == -82))) {
//                    listaArreglo[contadorElementosLista] = new Lista(tipoConstante(pilaSintaxis.peek()), "datoLista",
//                            (ambito + 1) + "", contadorElementosLista + "", nombreVariable);
//                    contadorElementosLista++;
//                    tarrVariable = contadorElementosLista + "";
//                } else {
//                    banderaListaMultiple = true;
//                    banderaListaNormal = false;
//                    listaArreglo[contadorElementosLista] = new Lista(tipoConstante(pilaSintaxis.peek()), "datoLista",
//                            ambito + "", contadorElementosLista + "", nombreVariable);
//                    contadorElementosLista++;
//                    tarrVariable = contadorElementosLista + "";
//                }
//            }
//        } else if (banderaListaMultiple && !banderaListaNormal) {
//            if (nombreVariable == "") {
//                nombreVariable = auxiliarID;
//            }
//            if (ambitoVariable == "") {
//                ambitoVariable = ambito + "";
//            }
//
//            if (pilaSintaxis.peek() == -4 || pilaSintaxis.peek() == -7
//                    || pilaSintaxis.peek() == -8 || pilaSintaxis.peek() == -81 || pilaSintaxis.peek() == -82) {
//                System.out.println("Tipo de constante: " + tipoConstante(pilaSintaxis.peek()));
//                System.out.println("Ambito: " + (ambito + 1));
//                System.out.println("NoPos | contadorElementosLista: " + contadorElementosLista);
//                System.out.println("ListaPertenece: " + nombreVariable);
//
//                System.out.println("AGREGAR ELEMENTO LISTA");
//                listaArreglo[contadorElementosLista] = new Lista(tipoConstante(pilaSintaxis.peek()), "datoLista",
//                        (ambito + 1) + "", contadorElementosLista + "", nombreVariable);
//                contadorElementosLista++;
//                tarrVariable = contadorElementosLista + "";
//            }
//
//        }
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

    private void agregarVariableBaseDatos() {
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
                } else if (banderaFor) {
                    tipoVariable = "var";
                    ambitoVariable = ambito + "";
                    aumentarContadorAmbito();
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,valor) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + valorVariable + "');";
                    controladorSQL.ejecutarQuery(query);
                    variableArreglo[contadorVariablesArreglo] = new Variable(nombreVariable, ambitoVariable);
                    banderaFor = false;
                } else if (!areaDeclaracion) {
                    //@@@
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
                        String noPosE = tuplaArreglo[i].getNoPos();
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
                        query = "UPDATE tablasimbolos SET tamanoArreglo = '" + contadorTupla + "' where id='" + auxiliarNombreVariable + "';";
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
                    query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,listaPertenece,tamanoArreglo) VALUES("
                            + "'" + nombreVariable + "',"
                            + "'" + claseVariable + "',"
                            + "'" + tipoVariable + "',"
                            + "'" + ambitoVariable + "',"
                            + "'" + listaPerteneceVariable + "',"
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
                }
                controladorDatoLista.vaciarDatosLista();
                listaPerteneceVariable = "";
                contadorElementosLista = 0;
                agregarLista = false;
                bandera814 = false;
                banderaListaMultiple = false;
                banderaListaNormal = true;
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
            case "Conjunto":

                aumentarAmbito();
                ambitoCreado = ambito + "";
                aumentarContadorAmbito();
                query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
                        + "'" + nombreVariable + "',"
                        + "'" + claseVariable + "',"
                        + "'" + tipoVariable + "',"
                        + "'" + ambitoVariable + "',"
                        + "'" + ambitoCreado + "');";
                auxiliarNombreVariable = nombreVariable;
                controladorSQL.ejecutarQuery(query);
                System.out.println("contadorConjunto: " + contadorConjunto);
                for (int i = 0; i < contadorConjunto; i++) {
                    conjunto[i].setAmb(ambito + "");
                    String tipoE = conjunto[i].getTipo();
                    String claseE = conjunto[i].getClase();
                    String ambitoE = conjunto[i].getAmb();
                    String noPosE = conjunto[i].getNoPosicion();
                    String listaPerE = conjunto[i].getListaPertenece();
                    aumentarContadorAmbito();
                    query = "INSERT INTO tablasimbolos (clase,tipo,ambito,noPos,listaPertenece) VALUES("
                            + "'" + claseE + "',"
                            + "'" + tipoE + "',"
                            + "'" + ambitoE + "',"
                            + "'" + noPosE + "',"
                            + "'" + listaPerE + "');";
                    controladorSQL.ejecutarQuery(query);
                    query = "UPDATE tablasimbolos SET tamanoArreglo = '" + contadorConjunto + "' where id='" + auxiliarNombreVariable + "';";
                    controladorSQL.ejecutarQuery(query);
                }
                agregarConjunto = false;
                contadorConjunto = 0;
                reducirAmbito();
                banderaConjunto = false;
                agregarConjunto = false;
                bandera814 = false;
                break;
            case "Diccionario":
                System.out.println("InsertarDiccionario");
                aumentarAmbito();
                ambitoCreado = ambito + "";
                aumentarContadorAmbito();
                query = "INSERT INTO tablasimbolos (id,clase,tipo,ambito,ambitoCreado) VALUES("
                        + "'" + nombreVariable + "',"
                        + "'" + claseVariable + "',"
                        + "'" + tipoVariable + "',"
                        + "'" + ambitoVariable + "',"
                        + "'" + ambitoCreado + "');";
                auxiliarNombreVariable = nombreVariable;
                controladorSQL.ejecutarQuery(query);
                System.out.println("contadorDiccionario: " + contadorDiccionario);
                for (int i = 0; i < contadorDiccionario; i++) {
                    diccionario[i].setAmb(ambito + "");
                    String tipoE = diccionario[i].getTipo();
                    String claseE = diccionario[i].getClase();
                    String ambitoE = diccionario[i].getAmb();
                    String valorE = diccionario[i].getValor();
                    String noPosE = diccionario[i].getNoPosicion();
                    String llaveE = diccionario[i].getLlave();
                    String listaPerE = diccionario[i].getListaPertenece();
                    query = "INSERT INTO tablasimbolos (clase,tipo,ambito,valor,noPos,llave,listaPertenece) VALUES("
                            + "'" + claseE + "',"
                            + "'" + tipoE + "',"
                            + "'" + ambitoE + "',"
                            + "'" + valorE + "',"
                            + "'" + noPosE + "',"
                            + "'" + llaveE + "',"
                            + "'" + listaPerE + "');";
                    controladorSQL.ejecutarQuery(query);
                    query = "UPDATE tablasimbolos SET tamanoArreglo = '" + contadorDiccionario + "' where id='" + auxiliarNombreVariable + "';";
                    controladorSQL.ejecutarQuery(query);
                }
                agregarDiccionario = false;
                contadorDiccionario = 0;
                reducirAmbito();
                banderaDiccionario = false;
                agregarDiccionario = false;
                bandera814 = false;
                break;
        }
        tamVariablesGuardadasArr = 0;
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
                tipo = "Conjunto";
                break;
            case 821:
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
                tipo = "Real";
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
        boolean existencia;
        int estadoInt = 0;
        String query = "SELECT COUNT(id) FROM tablasimbolos WHERE id="
                + "'" + id + "' AND ambito='" + ambitoVariable + "';";
        try {
            ResultSet rs = controladorSQL.obtenerResultSet(query);
            while (rs.next()) {
                estadoInt = rs.getInt(1);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("VALOR DE ESTADO INT: " + estadoInt);
        if (estadoInt >= 1) {
            existencia = true;
            System.out.println("VARIABLE DUPLICADA");
        } else {
            existencia = false;
            System.out.println("VARIABLE NO DUPLICADA");
        }

        return existencia;
    }

    private boolean variableDeclarada(String id) {
        boolean existencia;
        int estadoInt = 0;
        System.out.println("Valor ID: " + id);
        System.out.println("Valor ambito: " + ambitoVariable);
        String query = "SELECT COUNT(id) FROM tablasimbolos WHERE (id="
                + "'" + id + "' AND ambito='" + ambitoVariable + "')";
        try {
            ResultSet rs = controladorSQL.obtenerResultSet(query);
            while (rs.next()) {
                estadoInt = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ambito.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("VALOR DE ESTADO INT: " + estadoInt);
        if (estadoInt == 1) {
            existencia = true;
            System.out.println("VARIABLE EXITENTE");
        } else {
            existencia = false;
            System.out.println("VARIABLE NO EXISTENTE");
        }
        return existencia;
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
        controladorSQL.ejecutarQuery("UPDATE tablasimbolos SET tamanoArreglo='" + contadorParametros + "' WHERE id='" + nombreFuncion + "'");
    }

    public void generarTablaSimbolos() {

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
