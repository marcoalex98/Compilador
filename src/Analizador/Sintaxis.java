/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Analizador.Auxiliar.AuxiliarSintaxis;
import Controladores.ControladorTokenError;
import Modelos.OperToken;
import Excel.LeerExcelSintactico;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Sintaxis {

    int contadorIdentificadores;
    int[] contadoresSintaxis, contadorDiagrama;
    int[][] matrizSintactico, producciones, contadorAmbito;
    String urlLog;
    String[][] matrizExcelSintactico;
    Stack<Integer> pilaSintaxis;
    OperToken oper;
    AuxiliarSintaxis auxiliarSintaxis;
    ControladorTokenError controladorTokenError;
    Ambito analizadorAmbito;

    public Sintaxis(String urlLog, OperToken oper, int contadorIdentificadores,
            ControladorTokenError controladorTokenError,
            Ambito analizadorAmbito) {
        this.urlLog = urlLog;
        this.oper = oper;
        this.contadorIdentificadores = contadorIdentificadores;
        this.controladorTokenError = controladorTokenError;
        this.analizadorAmbito = analizadorAmbito;
    }

    public void iniciarSintaxis() {
        crearArchivoLog();
        inicializarVariables();
        cargarMatriz();
        analizadorSintaxis();
    }

    private void cargarMatriz() {
        File f = new File("matriz-sintaxis.xlsx");
        if (f.exists()) {
            LeerExcelSintactico xlsSintactico = new LeerExcelSintactico(f);
            matrizExcelSintactico = xlsSintactico.matrizGeneral;
            encabezadoSintactico();
        }
    }

    private void encabezadoSintactico() {
        matrizSintactico = new int[68][95];
        for (int i = 0; i < 68; i++) {
            for (int j = 0; j < 95; j++) {
                matrizSintactico[i][j] = (int) Math.round(Float.parseFloat(matrizExcelSintactico[i + 1][j + 1]));
//                System.out.print(matrizSintactico[i][j]+"  ");
            }
//            System.out.println("");
        }
//        for (int i = 0; i < 68; i++) {
//            for (int j = 0; j < 95; j++) {
//                System.out.print(matrizSintactico[i][j] + "  ");
//            }
//            System.out.println("");
//        }
    }

    private void inicializarVariables() {
        contadoresSintaxis = new int[21];
        contadorDiagrama = new int[21];
        auxiliarSintaxis = new AuxiliarSintaxis();
        producciones = auxiliarSintaxis.obtenerArregloProducciones();
        pilaSintaxis = new Stack<Integer>();
    }

    private void analizadorSintaxis() {
        System.out.println("----------SINTAXIS DE LA MUERTE----------");
        boolean tamArrPrimeraVez = false;
        pilaSintaxis.push(-1000);
        pilaSintaxis.push(200);
        oper.insertarUltimo(-1000, "$", 999999);
        int fila = 0, columna = 0, cont = 1, lineaAvance = 0;
        String avanceLinea = "", ultimoAgregado = "";
        while (pilaSintaxis.empty() == false && oper.listaVacia() == false) {
            System.out.println("--------------<INICIO DE CICLO SINTAXIS VUELTA " + cont + ">--------------");
            System.out.println("<SINTAXIS> Cima de la pila: " + pilaSintaxis.peek());
            System.out.println("<SINTAXIS> Lexema de primero: " + oper.mostrarLexemaPrimero());
            System.out.println("<SINTAXIS> Linea de primero: " + oper.mostrarLineaPrimero());
            System.out.println("<SINTAXIS> Avance Linea: " + avanceLinea);
            pilaSintaxis = analizadorAmbito.analizadorAmbito(pilaSintaxis, oper);
            if(lineaAvance != oper.mostrarLineaPrimero()){
                avanceLinea = "";
                ultimoAgregado = "";
                lineaAvance = oper.mostrarLineaPrimero();
            }
            if(ultimoAgregado != oper.mostrarLexemaPrimero()){
                avanceLinea += oper.mostrarLexemaPrimero();
                ultimoAgregado = oper.mostrarLexemaPrimero();
            }
            int produccion, lugar;
            if (pilaSintaxis.peek() > 199 && pilaSintaxis.peek() < 800) {
                fila = auxiliarSintaxis.valorFila(pilaSintaxis.peek());
                System.out.println("<SINTAXIS> Valor de la fila: " + fila);
                System.out.println("");
                System.out.println("<SINTAXIS> INICIA TRY COLUMNA");
                try { //TRY COLUMNA
                    columna = auxiliarSintaxis.valorColumna(oper.mostrarPrimero());
//                    System.out.println("<SINTAXIS TRY COLUMNA>  Numero de columna: " + columna);
                } catch (Exception ex) {
                    System.err.println("<SINTAXIS CATCH COLUMNA EN VUELTA: " + cont + ">    Primero de cola: " + oper.mostrarPrimero());
                    System.err.println("<SINTAXIS CATCH COLUMNA EN VUELTA: " + cont + ">    Causa " + ex.getCause());
                    System.err.println("<SINTAXIS CATCH COLUMNA EN VUELTA: " + cont + ">    Mensaje " + ex.getMessage());
                    System.err.println("<SINTAXIS CATCH COLUMNA EN VUELTA: " + cont + ">    Mensaje " + ex.getLocalizedMessage());
                    Logger.getLogger(Sintaxis.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("<SINTAXIS> TERMINA TRY COLUMNA");
                System.out.println("");
                System.out.println("Valor de la columna: " + columna);
                System.out.println("Valor de la fila: " + fila);
                produccion = matrizSintactico[fila][columna];
                System.out.println("<SINTAXIS Fila: " + (fila+1) + ", Columna: " + (columna+1) + ">   Produccion: " + produccion);
                System.out.println("Contenido de columnas");
                for (int i = 0; i <= columna; i++) {
                    System.out.print(matrizSintactico[fila][i] + "    ");
                }
            } else {
                produccion = 0;
            }

            if ((pilaSintaxis.peek() >= 200 && produccion < 600 && produccion != 369) && !oper.listaVacia()) {
                contadoresSintaxis(pilaSintaxis.peek());
                contadorDiagrama(produccion);
                pilaSintaxis.pop();
                lugar = produccion;

                System.out.println("<SINTAXIS> Valor de lugar: " + lugar + ", valor de arreglo de producciones: " + (lugar+1));
                for (int i = 0; i < producciones[lugar].length; i++) {
                    System.out.println("<SINTAXIS> Se agrego " + producciones[lugar][i] + " a la pila");
                    pilaSintaxis.push(producciones[lugar][i]);
                    
                    
                }
            } else if (produccion == 369) {
                System.out.println("<SINTAXIS> EPSILON DETECTADO");
                pilaSintaxis.pop();
            } else if (produccion > 599) {
                System.out.println("<SINTAXIS ERROR> " + produccion);
                controladorTokenError.agregarError(produccion, "Error de sintaxis Ciclo: " + cont, produccion + "", oper.mostrarLineaPrimero(), "Sintaxis");
                //errores[nR] = new Estructuras.Error(produccion, "Error de sintaxis", produccion + "", cont, "Sintaxis");

                oper.eliminarInicio();
//                    pila.pop();
            } else {
                try {
                    if (pilaSintaxis.peek() == oper.mostrarPrimero()) {
                        if (analizadorAmbito.obtenerBanderaArreglo() == true) {//Arreglonizador de Mundos
                            if (tamArrPrimeraVez == false && pilaSintaxis.peek() == -7) {
                                analizadorAmbito.establecerTamArr(Integer.parseInt(oper.mostrarLexemaPrimero()));
                                tamArrPrimeraVez = true;
                            } else {
                                if (pilaSintaxis.peek() == -7) {
                                    analizadorAmbito.establecerTamArr(analizadorAmbito.obtenerTamArr()
                                            * Integer.parseInt(oper.mostrarLexemaPrimero()));
                                    //tamArr *= Integer.parseInt(oper.mostrarLexemaPrimero());
                                }
                            }
                        }
                        if (pilaSintaxis.peek() == -6) {
                            analizadorAmbito.establecerNombreVariable(oper.mostrarLexemaPrimero());
//                            System.out.println( + "<AMBITO:sintaxis> ID Detectado" + );
//                            System.out.println( + "<AMBITO:sintaxis> Nombre variable: " + nombreVariable + );
//                            System.out.println( + "<AMBITO:sintaxis> Tipo variable: " + claseVariable + );
//                            System.out.println( + "<AMBITO:sintaxis> Clase variable: " + tipoVariable + );

                        }
//                        if (tipoVariable != "" && nombreVariable != "") {
//                            System.out.println( + "<AMBITO:sintaxis> IF agregar variable" + );
//                            agregarVariable();
//                        }
                        System.out.println("<SINTAXIS> Eliminar oper y pila sintaxis pop");
                        analizadorAmbito.agregarOperadorOperando(pilaSintaxis.peek(), oper);
                        oper.eliminarInicio();
                        pilaSintaxis.pop();
                    } else if (pilaSintaxis.peek() != oper.mostrarPrimero()) {
                        controladorTokenError.agregarError(1000, "ERROR DE FUERZA BRUTA", "", oper.mostrarLineaPrimero(), "Sintaxis");
                        //errores[nR] = new Estructuras.Error(1000, "ERROR DE FUERZA BRUTA", "", -1, "Sintaxis");
                        System.out.println("<SINTAXIS ERROR> FUERZA BRUTA");
                        System.out.println("<SINTAXIS ERROR> FUERZA BRUTA Cima de pila sintaxis:" + pilaSintaxis.peek() + "");
                        System.out.println("<SINTAXIS ERROR> FUERZA BRUTA Primero:" + oper.mostrarPrimero());
                        System.out.println("<SINTAXIS ERROR> FUERZA BRUTA Lexema Primero:" + oper.mostrarLexemaPrimero());
                        System.out.println("<SINTAXIS ERROR> FUERZA BRUTA Linea Primero:" + oper.mostrarLineaPrimero());
                        //reproducir("src/fuerzabruta.wav");
                        break;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Sintaxis.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("Excepcion cachada: " + ex.getMessage());
                }

            }
            System.out.println("--------------<FIN DE CICLO SINTAXIS>--------------");
            System.out.println("");
            cont++;

        }
        System.out.println("---------- FIN SINTAXIS DE LA MUERTE----------");
        if (analizadorAmbito.obtenerAmbito() == 0) {
            System.out.println("<AMBITO> Ambito 0");
        } else {
            try{
                controladorTokenError.agregarError(2000, "ERROR DE AMBITO", "Ambito Final: "
                    + analizadorAmbito.obtenerAmbito() + "", oper.mostrarLineaPrimero(), "Ambito");
            }
            catch(Exception e){
                controladorTokenError.agregarError(9999, "Ha ocurrido un error de sintaxis o ambito inesperado, "
                        + "ha sido captado en una excepcion", e.getCause()+"", -1, "SintaxisAmbito");
            }
            
            //errores[nR] = new Estructuras.Error(2000, "ERROR DE AMBITO", "Ambito Final: " + ambito + "", -1, "Ambito");
        }
        contadorAmbito = analizadorAmbito.obtenerContadorAmbito();
    }

    private void contadoresSintaxis(int produccion) {
        System.out.println("---Contadores Sintaxis---");
        System.out.println("Produccion: " + produccion);

        switch (produccion) {
            case 200://Program
                contadoresSintaxis[0]++;
                break;
            case 205://TerminoPascal
                contadoresSintaxis[4]++;
                break;
            case 208://Constante
                contadoresSintaxis[1]++;
                break;
            case 210://Elevacion
                contadoresSintaxis[5]++;
                break;
            case 213://ConstEntero
                contadoresSintaxis[2]++;
                break;
            case 215://ListUpRangos
                contadoresSintaxis[3]++;
                break;
            case 220://SimpleExpPas
                contadoresSintaxis[6]++;
                break;
            case 223://Or
                contadoresSintaxis[9]++;
                break;
            case 226://OpBit
                contadoresSintaxis[10]++;
                break;
            case 229://And
                contadoresSintaxis[11]++;
                break;
            case 232://AndLog
                contadoresSintaxis[12]++;
                break;
            case 235://OrLog
                contadoresSintaxis[13]++;
                break;
            case 238://XOrLog
                contadoresSintaxis[14]++;
                break;
            case 241://Factor
                contadoresSintaxis[7]++;
                break;
            case 247://Not
                contadoresSintaxis[8]++;
                break;
            case 250://Est
                contadoresSintaxis[15]++;
                break;
            case 256://ExpPas
                contadoresSintaxis[20]++;
                break;
            case 259://Funciones
                contadoresSintaxis[19]++;
                break;
            case 261://Funlist
                contadoresSintaxis[17]++;
                break;
            case 263://Asign
                contadoresSintaxis[16]++;
                break;
            case 265://Arr
                contadoresSintaxis[18]++;
                break;
        }
    }

    private void contadorDiagrama(int i) {
        System.out.println("SE AUMENTO EL CONTADOR DE " + i);
        switch (i) {
            case 1://Program
                contadorDiagrama[0]++;
                break;
            case 9://Constante
                contadorDiagrama[1]++;
                break;
            case 14://ConstEntero
                contadorDiagrama[2]++;
                break;
            case 16://ListUpRangos
                contadorDiagrama[3]++;
                break;
            case 6://TerminoPascal
                contadorDiagrama[4]++;
                break;
            case 31://Elevacion
                contadorDiagrama[5]++;
                break;
            case 35://Simple ExpPas
                contadorDiagrama[6]++;
                break;
            case 43://Factor
                contadorDiagrama[7]++;
                break;
            case 46://Not
                contadorDiagrama[8]++;
                break;
            case 61://Or
                contadorDiagrama[9]++;
                break;
            case 64://Opbit
                contadorDiagrama[10]++;
                break;
            case 68://And
                contadorDiagrama[11]++;
                break;
            case 72://Andlog
                contadorDiagrama[12]++;
                break;
            case 75://Orlog
                contadorDiagrama[13]++;
                break;
            case 78://Xorlog
                contadorDiagrama[14]++;
                break;
            case 81://Est
                contadorDiagrama[15]++;
                break;
            case 99://Asign
                contadorDiagrama[16]++;
                break;
            case 108://Funlist
                contadorDiagrama[17]++;
                break;
            case 118://Arr
                contadorDiagrama[18]++;
                break;
            case 130://Funciones
                contadorDiagrama[19]++;
                break;
            case 142://ExpPas
                contadorDiagrama[20]++;
                break;
        }
    }

    private void crearArchivoLog() {
        File file = new File("logs/" + urlLog + "/" + urlLog + "-SintaxisAmbito.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream out = new PrintStream(fos);
            System.setOut(out);
        } catch (FileNotFoundException e) {

        }
    }

    public int[] obtenerContadorDiagramasSintaxis() {
        return contadorDiagrama;
    }
}
