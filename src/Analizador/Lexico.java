/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

import Controladores.ControladorTokenError;
import Modelos.OperToken;
import Excel.LeerExcelLexico;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;

/**
 *
 * @author marco
 */
public class Lexico {

    String urlLog, lineaHoja, lineaAuxiliar, lexemaMultiple, lexemaNumero;
    String[] encabezadoLexico;
    String[][] matrizExcelLexico;
    int lineas, contadorParentesis, contadorCorchetes, contadorLlaves, lineaMulticomentario,
            lineaActual, activadorLexicoMultilinea, multiLinea, contadorIdentificadores,
            vueltaCicloLexico, vueltaDesmenuzador, vueltaDeslexemizador;
    int[] contadoresLexico;
    int[][] contadoresLinea, matrizLexico;
    boolean estadoError, banderaMultilinea;
    OperToken oper;
    ControladorTokenError controladorTokenError;

    public Lexico(String urlLog, ControladorTokenError controladorTokenError) {
        this.urlLog = urlLog;
        this.controladorTokenError = controladorTokenError;
    }

    public void iniciarLexico(JTextArea textArea) {
        crearArchivoLog();
        inicializarVariables();
        cargarMatriz();
        cicloLexico(textArea);

    }

    private void crearArchivoLog() {
        File file = new File("logs/" + urlLog + "/" + urlLog + "-Lexico.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream out = new PrintStream(fos);
            System.setOut(out);
        } catch (Exception e) {

        }
    }

    private void inicializarVariables() {
        contadorParentesis = 0;
        contadorCorchetes = 0;
        contadorIdentificadores = 0;
        lineaActual = 0;
        lineaMulticomentario = 0;
        contadorLlaves = 0;
        activadorLexicoMultilinea = 0;
        multiLinea = 0;
        vueltaCicloLexico = 0;
        vueltaDesmenuzador = 0;
        vueltaDeslexemizador = 0;
        lexemaMultiple = "";
        lexemaNumero = "";
        estadoError = false;
        banderaMultilinea = false;
        contadoresLexico = new int[21];
        oper = new OperToken();
    }

    public int obtenerContadorIdentificadores() {
        return contadorIdentificadores;
    }

    public OperToken obtenerOperToken() {
        return oper;
    }

    private void cicloLexico(JTextArea textArea) {
        String[] aux = textArea.getText().split("\n");
        String[] lineasHoja = new String[aux.length];
        System.out.println(textArea.getText() + "\n");
        for (int i = 0; i < aux.length; i++) {
//            lineasHoja[i] = aux[i].replaceAll(" ", "");
            lineasHoja[i] = aux[i];
        }
        lineas = lineasHoja.length;
        contadoresLinea = new int[lineas][21];
        for (int i = 0; i < lineasHoja.length; i++) {//Lineas del textarea
            lineaActual++;
            char c[] = new char[lineasHoja[i].length()];//Arreglo de char para la linea i
//            System.out.println("Largo de arreglo de la linea [" + i + "]: " + c.length);
            for (int j = 0; j < c.length; j++) {//Convertir la lina a un arreglo de caracter por caracter
                if (c[j] != ' ' || c[j] != '\t') {
                    c[j] = lineasHoja[i].charAt(j);
                    //System.out.println("j: " + j + ", contenido: " + c[j]);
                }
            }
            try {
                lineaAuxiliar = "";
                for (int j = 0; j < c.length; j++) {
                    lineaAuxiliar += c[j];
                }
                lineaAuxiliar = lineaAuxiliar.replaceAll(" ", "");
                analizador(c);
            } catch (Exception e) {
                System.err.println("Excepcion: " + e.getMessage());
            }
        }
    }

    private void cargarMatriz() {
        File f = new File("matriz-lexico.xlsx");
        if (f.exists()) {
            LeerExcelLexico xlsLexico = new LeerExcelLexico(f);
            matrizExcelLexico = xlsLexico.matrizGeneral;
            encabezadoLexico();
        }
    }

    private void analizador(char[] linea) {
        String lexema = "";
        lineaHoja = "";
        int fil = 0, largoExpresion = 0;
        for (int i = 0; i < linea.length; i++) {//Desmenuzador de lineas
            System.out.println("------------------Inicio Vuelta Ciclo Lexico: " + vueltaCicloLexico + "------------------");
            largoExpresion++;
            lineaHoja += linea[i] + "";
            boolean quebrador = false;
            System.out.println("Caracter: [" + linea[i] + "]");
            int col = asignarColumna(linea[i]);
            char aux = ' ';
            int colAux = 0;
            if (i + 1 < linea.length) {
                aux = linea[i + 1];
                colAux = asignarColumna(aux);
            }

            System.out.println("Fila: " + fil + "  /// Columna: " + col);
            int estado;
            if (fil < 500 && fil > 0) {
                estado = matrizLexico[fil][col];
            } else {
                estado = fil;
            }

            System.out.println("Estado: " + estado);
            if (estado < 500 && estado >= 0) {
                fil = matrizLexico[fil][col];
            } else if (estado >= 500) {
                estadoError = true;
            }
            int estadoAux;
            if (fil < 500 && fil > 0) {
                estadoAux = matrizLexico[fil][colAux];
            } else {
                estadoAux = fil;
            }
//            System.out.println("Estado auxiliar: " + estadoAux);
            if (estado == 7 && banderaMultilinea == false) {
                banderaMultilinea = true;
                lineaMulticomentario = lineaActual;
            }
            if (banderaMultilinea == true) {
                if ("".equals(lexemaMultiple)) {
                    lexemaMultiple = "''";
                }
                lexemaMultiple += linea[i];
                if (activadorLexicoMultilinea == 1) {
                    if (linea[i] == '\'') {
                        multiLinea++;
                    } else {
                        multiLinea = 0;
                    }
                }
                activadorLexicoMultilinea = 1;
                if (multiLinea == 3) {
                    estado = -3;
                    banderaMultilinea = false;
                    controladorTokenError.agregarToken(estado, lexemaMultiple, lineaMulticomentario);
                    if (estado == -6) {
                        contadorIdentificadores++;
                    }
//                    oper.insertarUltimo(estado, lexemaMultiple, lineaMultiComentario);
//                    actualizarTablaToken(tokens);
//                    contadoresLexico(estado);

                    lexema = deslexemizador(lexemaMultiple);
                    activadorLexicoMultilinea = 0;
                    multiLinea = 0;
                    lexemaMultiple = "";
                    lineaMulticomentario = 0;
                    estado = 0;
                    fil = 0;
                }
            } else {//-------------Resto de las expresiones------------
//                System.out.println("Largo de expresion: " + largoExpresion + ", linea.lenght: " + linea.length);
                try {
                    if (largoExpresion == linea.length) {
                        col = 1;
                    }
                } catch (Exception e) {

                }
//                System.out.println("Estado: " + estado);
                if (linea[i] != ' ' || linea[i] != ' ') {
                    lexema += linea[i];
                }
                estado = estadoAux;
//                System.out.println("--Estado: " + estado);
                if (i + 1 == linea.length) {
                    if (fil > 0 && fil < 500) {
                        estadoAux = matrizLexico[fil][50];
                    }
                    //  System.out.println("Largo de la linea, fil: " + fil + ", col: 50");
//                    System.out.println("Contenido: " + estadoAux);
                    estado = estadoAux;
                }
                if (estadoAux >= 500 || estadoError == true) {
                    estadoError = false;
                    contadoresLinea[lineaActual - 1][0]++;
                    System.err.println("-------------Entro a error "+estado+"------------");
                    System.out.println("Fila: " + fil + " Columna: " + col + " Error Numero: " + controladorTokenError.obtenerArregloErrores().length);
                    System.out.println("Contenido del error: " + estado);
                    System.out.println("Lexema del error: " + lexema);
                    switch (estado) {
                        case 500:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un carácter o un '", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                            break;
                        case 501:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un '", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                            break;
                        case 502:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un carácter o una \"", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                            desmenuzadorCriminalMutilador(lexema);
                            System.out.println("Lexema despues de desmenuzadorCriminalMutilador: " + lexema);
                            break;
                        case 505:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un número del 0 al 9 o un - o un +", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                            break;
                        case 503:
                        case 504:
                        case 506:
                        case 507:
                        case 508:
                        case 509:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        case 510:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un número o un )", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                            break;
                        case 511:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un ]", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                            break;
                        case 512:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un }", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                            break;
                        case 513:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un ' , \" , # , _ , letra, número, "
                                    + "( , + , - , * , / , % , | , & , < , > , = , ; , . , [ , { , ^ o una ,", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                            //desmenuzadorCriminalMutilador(lineaAuxiliar);
                            break;
                        case 514:
                            desmenuzadorCriminalMutilador(lexema);
                            System.out.println("[ANALIZADOR ERROR 514] Lexema antes del error " + lexema);
                            //desmenuzadorCriminalMutilador(lexema.charAt(0) + "");
                            //lexema=deslexemizador(lexema.charAt(0)+"");
                            System.out.println("[ANALIZADOR ERROR 514] Lexema despues del error " + lexema);
                            break;
                        case 515:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un punto, j, x, b, e, E o un número del 0 al 8",
                                    lexema, lineaActual, "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                            break;
                        case 516:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Se esperaba un 0 o 1", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
//                            desmenuzadorCriminalMutilador(lineaAuxiliar);
                            break;
                        case 517:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        case 518:
                            contadoresLexico[0]++;
                            desmenuzadorCriminalMutilador(lexema);
                            break;
                        default:
                            contadoresLexico[0]++;
                            controladorTokenError.agregarError(estado, "Error desconocido", lexema, lineaActual,
                                    "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                    }
                    lineaHoja = "";
                    lexema = "";
                    if (quebrador == true) {
                        desmenuzadorCriminalMutilador(lineaHoja);
                        quebrador = false;
                        lineaHoja = "";
                    }
                    fil = 0;
                    col = 0;
                    break;
                } else if (estadoAux < 0) {
//                    System.out.println("Nt antes de ingresar a arreglo: " + nT);
//                    System.out.println("Exito");
//                    System.out.println("Fila: " + fil + " Columna: " + col + " nT: " + nT);
                    estado = palabraReservada(lexema, estado);
//                    System.out.println("[ANALIZADOR] Estado despues de palabra reservada: " + estado);
                    if (lexema.equals(":")) {
                        estado = -110;
                    }
                    if (lexema.equals(".")) {
                        fil = -112;
                    }
                    if (lexema.equals(",")) {
                        estado = -46;
                    }
                    controladorTokenError.agregarToken(estado, lexema, lineaActual);

//                    System.out.println("[[ANALIZADOR - TOKENIZADO]]");
                    if (estado != -5) {
                        oper.insertarUltimo(estado, lexema, lineaActual);
                    }
                    //lexema = deslexemizador(lexema);
                    if (estado == -6) {
                        contadorIdentificadores++;
                    }
//                    contadoresLexico(estado);
//                    for (int w = 0; w < tokens.length; w++) {
//                        System.out.println("----------Tokens----------");
//                        System.out.println("Estado del token [" + w + "]: " + tokens[w].getEstado());
//                        System.out.println("Lexema del token [" + w + "]: " + tokens[w].getLexema());
//                        System.out.println("--------------------------");
//                    }
//                    System.out.println("Lexema antes de deslexemizador: " + lexema);
                    lexema = deslexemizador(lexema);
//                    System.out.println("Lexema despues de deslexemizador: " + lexema);
                    lexema = "";
                    fil = 0;
                    col = 0;
                }
            }
            col = 0;//Se necesita para comenzar el recorrido desde la primera columna
            System.out.println("------------------Fin Vuelta Ciclo Lexico: " + vueltaCicloLexico++ + "------------------\n");
        }
//        System.out.println(oper.mostrarPrimero());
    }

    String deslexemizador(String lexemaTokenizado) {//Elimina de la linea actual el lexema tokenizado
        System.out.println("\n        --------------INICIO DESLEXEMIZADOR--------------");
        System.out.println("        Vuelta desLex: " + ++vueltaDeslexemizador);
        System.out.println("        [DESLEXEMIZADOR] Lexema a eliminar: " + lexemaTokenizado);
        System.out.println("        [DESLEXEMIZADOR] Linea entrante: " + lineaAuxiliar);
        String lexemaAuxiliar = "";
        char[] aux = new char[lineaAuxiliar.length()];
//        System.out.println("[DESLEXEMIZADOR] aux[].length: " + aux.length);
        for (int i = 0; i < aux.length; i++) {
            aux[i] = lineaAuxiliar.charAt(i);
//            System.out.println("[DESLEXEMIZADOR] aux[" + i + "]: " + aux[i]);
        }
//        System.out.println("---------------------------------------");

        char[] newAux = new char[lineaAuxiliar.length() - lexemaTokenizado.length()];
//        System.out.println("[DESLEXEMIZADOR] newAux[].length: " + newAux.length);

        int banderaEspacio = 0;
        if (aux[0] == ' ') {
            banderaEspacio = 1;
        }
        for (int i = lexemaTokenizado.length() + banderaEspacio, j = 0; i < aux.length; i++, j++) {
//            System.out.println("[DESLEXEMIZADOR FOR] aux[" + i + "]: " + aux[i]);
            newAux[j] = aux[i];
            // System.out.println("[DESLEXEMIZADOR FOR] newAux[" + j + "]: " + newAux[j]);
        }
        for (int i = 0; i < newAux.length; i++) {
            lexemaAuxiliar += newAux[i] + "";
        }
        lineaAuxiliar = lexemaAuxiliar;
//        System.out.println("[DESLEXEMIZADOR] Linea auxiliar saliente: |" + lineaAuxiliar + "|");
        System.out.println("        [DESLEXEMIZADOR] Linea Saliente: " + lexemaAuxiliar);
        System.out.println("        --------------FIN DESLEXEMIZADOR--------------\n");
        return lexemaAuxiliar;
    }

    private int asignarColumna(char c) {
        int col = 0;
        boolean igual = false;
        if (c == 10) {
            col = 1;
            igual = true;
        } else if (c == 32) {
            col = 2;
            igual = true;
        } else if (c == 10 || c == '\t') {
            col = 3;
            igual = true;
        } else if (c == 'a' | c == 'A') {
            col = 14;
            igual = true;
        } else if (c == 'c' | c == 'C') {
            col = 16;
            igual = true;
        } else if (c == 'd' | c == 'D') {
            col = 17;
            igual = true;
        } else if (c == 'e' | c == 'E') {
            col = 18;
            igual = true;
        } else if (c == 'f' | c == 'F') {
            col = 19;
            igual = true;
        } else if (((c >= 103 && c <= 105) || (c >= 107 && c <= 119) || (c >= 121 && c <= 122)) || c == 'ñ') {
            col = 23;
            igual = true;
        } else if ((c >= 71 && c <= 90) || c == 'Ñ') {
            col = 24;
            igual = true;
        } else if (c == '.') {
            col = 48;
            igual = true;
        } else if (c == ':') {
            col = 46;
            igual = true;
        } else if (c == ',') {
            col = 47;
        } else {
            for (int j = 0; j < encabezadoLexico.length; j++) {//Comparar si existe el elemento char en los encabezados
                if (encabezadoLexico[j].equals(c + "")) {
                    igual = true;
                    break;
                }
                if (c == '(') {
                    contadorParentesis++;
                }
                if (c == '[') {
                    contadorCorchetes++;
                }
                if (c == '{') {
                    contadorLlaves++;
                }
                if (igual == false) {
                    col++;
                }
            }
        }
        if (igual = false) {
            col = 50;
        }
        if (col == 51) {
            col = 50;
        }
        return col;
    }

    void encabezadoLexico() {
        encabezadoLexico = new String[51];
        for (int i = 0; i < 51; i++) {
            switch (matrizExcelLexico[0][i]) {
                case "0.0":
                    encabezadoLexico[i] = 0 + "";
                    break;
                case "1.0":
                    encabezadoLexico[i] = 1 + "";
                    break;
                case "2.0":
                    encabezadoLexico[i] = 2 + "";
                    break;
                case "3.0":
                    encabezadoLexico[i] = 3 + "";
                    break;
                case "4.0":
                    encabezadoLexico[i] = 4 + "";
                    break;
                case "5.0":
                    encabezadoLexico[i] = 5 + "";
                    break;
                case "6.0":
                    encabezadoLexico[i] = 6 + "";
                    break;
                case "7.0":
                    encabezadoLexico[i] = 7 + "";
                    break;
                case "8.0":
                    encabezadoLexico[i] = 8 + "";
                    break;
                case "9.0":
                    encabezadoLexico[i] = 9 + "";
                    break;
                default:
                    encabezadoLexico[i] = matrizExcelLexico[0][i];
            }
        }
        matrizLexico = new int[88][51];
        for (int i = 0; i < 88; i++) {
            for (int j = 0; j < 51; j++) {
                matrizLexico[i][j] = (int) Math.round(Float.parseFloat(matrizExcelLexico[i + 1][j]));
//                System.out.print(matrizLexico[i][j] + " ");
            }
//            System.out.println("");
        }
    }

    boolean banderaBin = false, banderaHex = false, banderaDec = false;

    private void enumeradorCriminalMutilador(char caracter) {
        if (caracter >= '0' && caracter <= '9' || caracter == 'b' || caracter == 'x') {

        } else {

        }
    }

    void desmenuzadorCriminalMutilador(String lineaAux) {
        System.out.println("    --------DESMENUZADOR CRIMINAL MUTILADOR--------");
        System.out.println("    Vuelta DesCriMu: " + ++vueltaDesmenuzador);
        System.out.println("    [DCM] lineaAux: " + lineaAux);
        lineaAux = lineaAuxiliar;
        boolean igual = false, banMas = false, banMenos = false, banderaBinHexOct = false,
                banderaOctal = false, banderaDecimal = false;
        lineaAux = lineaAux.replaceAll(" ", "");
        lineaAuxiliar = lineaAuxiliar.replaceAll(" ", "");
        int fil = 0, bin = 0, oct = 0, hex = 0;
        String lexema = "";
        char linea[] = new char[lineaAux.length()];
        System.out.print("    [[DCM]] Contenido de linea a desmenuzar: ");
        System.out.print("    ");
        for (int i = 0; i < lineaAux.length(); i++) {
            linea[i] = lineaAux.charAt(i);
            System.out.print(linea[i]);
        }
        System.out.println("");
//        int espacios=0;
//        for(int i=0;linea[i]==' ';i++){
//            System.out.println("Espacios juju");
//            espacios++;
//        }
//        char lineaAuxEsp[]=linea;
//        for(int i=0;i<(linea.length-espacios);i++){
//            linea[i]=lineaAuxEsp[i+espacios];
//        }
        for (int i = 0; i < linea.length; i++) {
            System.out.println("    [[DCM]] Caracter de la linea a menuzar en la posicion [" + i + "]: " + linea[i]);
            
            if(linea[i] == '\'' && linea[i+2] == '\''){
                controladorTokenError.agregarToken(-1, "'" + linea[i+1] + "'", lineaActual);
                oper.insertarUltimo(-1, "'" + linea[i+1] + "'", lineaActual);
                lexema = "";
                i+=3;   
            }
            else if(linea[i] == '\''){
                int contStr = 1;
                while(linea[i + contStr] != '\''){
                    lexema += linea[i + contStr];
                    contStr++;
                }
                controladorTokenError.agregarToken(-4, "\'" + lexema + "\'", lineaActual);
                oper.insertarUltimo(-4, "\'" + lexema + "\'", lineaActual);
                lexema = "";
                i += contStr+1;
            }
            else if(linea[i] == '"'){
                int contStr = 1;
                while(linea[i + contStr] != '"'){
                    lexema += linea[i + contStr];
                    contStr++;
                }
                controladorTokenError.agregarToken(-4, "\"" + lexema + "\"", lineaActual);
                oper.insertarUltimo(-4, "\"" + lexema + "\"", lineaActual);
                lexema = "";
                i += contStr+1;
            }
            
            //--------BINARIO----------
            if (i + 2 < linea.length) {
                // System.out.println( +  + "I+2" + );

                if (linea[i] == '0') {
                    //   System.out.println( +  + "I==0" + );

                    if (linea[i + 1] == 'b') {
                        //     System.out.println( +  + "I+1==b" + );
                        if (linea[i + 2] == '0' || linea[i + 2] == '1') {
                            System.out.println("    [[DCM]] BINARIO");
                            lexema += "0b";
                            bin = 2;
                            boolean binario = true;
                            int j = i + 2;
                            while (binario) {
                                //         System.out.println( +  + "Vuelta binario ciclo" + );

                                if (linea[j] == '0' || linea[j] == '1') {
                                    bin++;
                                    lexema += linea[j];
                                    j++;
                                } else {
                                    //           System.out.println("else asqueroso");
                                    binario = false;
                                }
                            }
                            // System.out.println("Salio de ciclo");

                            controladorTokenError.agregarToken(-10, lexema, lineaActual);
                            oper.insertarUltimo(-10, lexema, lineaActual);
                            lexema = deslexemizador(lexema);
                            if (fil == -6) {
                                contadorIdentificadores++;
                            }
                            // System.out.println("[[[TOKENIZADO]]] en -10");
//                            contadoresLexico(-10);
                            i += bin;
                            bin = 0;
                            // System.out.println("Valor de i:" + i);
                            lexema = "";
                        }
                    }
                }
            }
            //-----FIN DE BINARIO-----
            if (linea[i] >= '1' && linea[i] <= '9' && !banderaOctal) {
                banderaDecimal = true;
            } else if (linea[i] == '0' && !banderaDecimal) {
                banderaOctal = true;
            }
            //--------OCTAL----------               
            if (i + 2 < linea.length) {
                if (linea[i] == '0') {
                    if (linea[i + 1] >= '0' && linea[i + 1] <= '7' && banderaOctal) {
                        System.out.println("[[DCM]] OCTAL");
                        //  System.out.println("[OCTAL] Tercer if");
                        lexema += "0";
                        oct = 2;
                        boolean octal = true;
                        int k = i + 2;
                        while (octal) {
                            //    System.out.println("[OCTAL]: " + linea[k]);
                            if (linea[k] >= '0' && linea[k] <= '7') {
                                //      System.out.println("[OCTAL] Ciclo if");
                                oct++;
                                lexema += linea[k];
                                k++;
                            } else {
                                octal = false;
                            }
                        }
                        if (linea[k] >= '7' && linea[k] <= '9' || linea[k] == '0' && banderaDecimal) {
                            System.out.println("    [[DCM]] DECIMAL");
                            boolean dec = true;
                            while (dec) {
                                if (linea[k] >= '0' && linea[k] <= '9') {
                                    oct++;
                                    lexema += linea[k];
                                    k++;
                                } else {
                                    dec = false;
                                }
                                controladorTokenError.agregarToken(-7, lexema, lineaActual);
                                oper.insertarUltimo(-7, lexema, lineaActual);
                                lexema = deslexemizador(lexema);
                                if (fil == -6) {
                                    contadorIdentificadores++;
                                }
//                                    contadoresLexico(-7);
                                i += oct;
                                oct = 0;
                                lexema = "";
                                banderaDecimal = false;
                                banderaOctal = false;
                            }
                        } else {
                            controladorTokenError.agregarToken(-11, lexema, lineaActual);
                            oper.insertarUltimo(-11, lexema, lineaActual);
                            lexema = deslexemizador(lexema);
                            if (fil == -6) {
                                contadorIdentificadores++;
                            }
//                                contadoresLexico(-11);
                            i += oct;
                            oct = 0;
                            lexema = "";
                            banderaOctal = false;
                            banderaDecimal = false;
                        }
                    }

                }
            }
            //-----FIN DE OCTAL-----
            //--------HEX----------
            if (i + 2 < linea.length) {
                if (linea[i] == '0') {
                    if (linea[i + 1] == 'x') {
                        if ((linea[i + 2] >= '0' && linea[i + 2] <= '9')
                                || (linea[i + 2] >= 'A' && linea[i + 2] <= 'F')) {
                            lexema += "0x";
                            hex = 2;
                            boolean hexa = true;
                            int l = i + 2;
                            while (hexa) {
                                if ((linea[l] >= '0' && linea[l] <= '9')
                                        || (linea[l] >= 'A' && linea[l] <= 'F')
                                        || (linea[l] >= 'a' && linea[l] <= 'f')) {
                                    hex++;
                                    lexema += linea[l];
                                    l++;
                                } else {
                                    hexa = false;
                                }
                            }
                            controladorTokenError.agregarToken(-12, lexema, lineaActual);
                            oper.insertarUltimo(-12, lexema, lineaActual);
                            lexema = deslexemizador(lexema);
                            if (fil == -6) {
                                contadorIdentificadores++;
                            }
//                            contadoresLexico(-12);
                            i += hex;
                            hex = 0;
                            lexema = "";
                        }
                    }
                }
            }
            //-----FIN DE HEX-----
            lexema += linea[i];
            if (igual = false) {
                fil = 0;
            }
            char aux = 0;
            int colAux;

            if (i + 1 < linea.length) {
                aux = linea[i + 1];
                colAux = asignarColumna(aux);
            } else {
                colAux = 50;
            }
            int columnaElemento = asignarColumna(linea[i]);
            System.out.println("    >>>>dCM: Lexema: " + lexema);
            System.out.println("    >>>>dCM: Columna caracter[" + linea[i] + "]: " + columnaElemento);
            if (i + 1 < linea.length && linea[i] == '0' && linea[i + 1] == 'b' && banderaOctal == false) {
                fil = 28;
                colAux = 4;
                banderaOctal = true;
            } else {
                fil = matrizLexico[fil][columnaElemento];
            }

//                System.out.println(">>>>dCM: Fila: " + fil);
            if (fil == 31 || fil == 32 || fil == 78 || fil == 79 || fil == 80 || fil == 81) {
                fil = matrizLexico[fil][50];
//                    System.out.println(">>>>dCM: Fila actualizada caracter[" + linea[i] + "]: " + fil);
                if (fil > 0) {
                    fil = matrizLexico[fil][50];
                }
                if (lexema.equals(":")) {
                    fil = -110;
                }
                if (lexema.equals(".")) {
                    fil = -112;
                }
                if (lexema.equals(",")) {
                    fil = -46;
                }
                fil = palabraReservada(lexema, fil);
                controladorTokenError.agregarToken(fil, lexema, lineaActual);
                if (fil != -5) {
                    oper.insertarUltimo(fil, lexema, lineaActual);
                }
                if (fil == -6) {
                    contadorIdentificadores++;
                }
                lexema = deslexemizador(lexema);
                contadoresLexico(fil);
                System.out.println("    [[[TOKENIZADO]]] en " + fil);
                fil = 0;
                lexema = "";
            } else {
                if (fil < 0 || fil >= 500) {
                    if (fil >= 500) {
                        contadoresLexico[0]++;
                        agregarErrorDesmenuzado(fil, lexema);
                        System.out.println("    [[[ERRORIZADO]]] en " + fil);
                        fil = 0;
                        lexema = "";

                    } else {
                        if (fil > 0) {
                            fil = matrizLexico[fil][50];
                        }
                        if (lexema == ":") {
                            fil = -110;
                        }
                        if (lexema.equals(".")) {
                            fil = -112;
                        }
                        if (lexema.equals(",")) {
                            fil = -46;
                        }

                        fil = palabraReservada(lexema, fil);
                        controladorTokenError.agregarToken(fil, lexema, lineaActual);
                        contadoresLexico(fil);
                        System.out.println("    >>>>dCM [[[TOKENIZADO]]] en " + fil);
                        if (fil != -5) {
                            oper.insertarUltimo(fil, lexema, lineaActual);
                        }
                        if (fil == -6) {
                            contadorIdentificadores++;
                        }
                        lexema = deslexemizador(lexema);
                        fil = 0;
                        lexema = "";
                    }
                } else {
                    int estadoAux = matrizLexico[fil][colAux];
                    if (estadoAux != fil) {
                        if (fil >= 500 || estadoAux >= 500) {
                            contadoresLexico[0]++;
                            agregarErrorDesmenuzado(fil, lexema);
                            fil = 0;
                            lexema = "";
                            System.out.println("    [[[ERRORIZADO]]]");
                        } else {
                            if (lexema == ":") {
                                fil = -110;
                            }
                            if (lexema.equals(".")) {
                                fil = -112;
                            }
                            if (fil > 0) {
                                fil = matrizLexico[fil][50];
                            }
                            if (lexema.equals(",")) {
                                fil = -46;
                            }
                            fil = palabraReservada(lexema, fil);
                            controladorTokenError.agregarToken(fil, lexema, lineaActual);
                            contadoresLexico(fil);
                            if (fil != -5) {
                                oper.insertarUltimo(fil, lexema, lineaActual);
                            }
                            lexema = deslexemizador(lexema);
                            if (fil == -6) {
                                contadorIdentificadores++;
                            }
                            System.out.println("    [[[TOKENIZADO]]] en " + fil);
                            fil = 0;
                            lexema = "";
                            banderaBinHexOct = false;
                        }
                    }
                }
            }

        }
        System.out.println("    ----- FIN DE DESMENUZADOR CRIMINAL MUTILADOR-----");
    }

    void agregarErrorDesmenuzado(int fil, String lexema) {
        switch (fil) {
            case 500:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un carácter o un '", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 501:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un '", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 502:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un carácter o una \"", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 503:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba cualquier cosa menos un salto de linea o \" ", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 504:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un número", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 505:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un número del 0 al 9 o un - o un +", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 506:
                controladorTokenError.agregarError(fil, "Se esperaba un número del 0 al 9 o una j", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                contadoresLexico[0]++;
                break;
            case 507:
                controladorTokenError.agregarError(fil, "Se esperaba un número del 0 al 9, punto o una j", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                contadoresLexico[0]++;
                break;
            case 508:
                controladorTokenError.agregarError(fil, "Se esperaba un )", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                contadoresLexico[0]++;
                break;
            case 509:
                controladorTokenError.agregarError(fil, "Se esperaba un número del o al 9 o un +", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                contadoresLexico[0]++;
                break;
            case 510:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un número o un )", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 511:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un ]", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 512:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un }", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 513:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un ' , \" , # , _ , letra, número, "
                        + "( , + , - , * , / , % , | , & , < , > , = , ; , . , [ , { , ^ o una ,", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            case 514:
                controladorTokenError.agregarError(fil, "Se esperaba un número del 0 al 9, un +, una j o un punto", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                contadoresLexico[0]++;
                break;
            case 515:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un punto, j, x, b, e, E o un número del 0 al 8",
                        lexema, lineaActual, "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                contadoresLexico[0]++;
                break;
            case 516:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se esperaba un 0 o 1", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                contadoresLexico[0]++;
                break;
            case 517:
                controladorTokenError.agregarError(fil, "Se espera un numero del 0 al 7", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                contadoresLexico[0]++;
                break;
            case 518:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Se espera un numero del 0 al 9 o una letra de la A a la F", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
                break;
            default:
                contadoresLexico[0]++;
                controladorTokenError.agregarError(fil, "Error desconocido", lexema, lineaActual,
                        "Lexico, DCM: " + vueltaDesmenuzador + ", DES: " + vueltaDeslexemizador);
        }
    }

    private int palabraReservada(String lexema, int estado) {
        String lexema2 = "";
        lexema = lexema.toLowerCase();
        lexema = destabulador(lexema);
        char[] c = new char[lexema.length()];
//        System.out.println("Lexema:" + lexema + "|");
        if (lexema.charAt(lexema.length() - 1) == ' ') {
            for (int i = 0; i < lexema.length() - 1; i++) {
                lexema2 += lexema.charAt(i);
            }
            lexema = lexema2;
        }
        lexema = lexema.toLowerCase();
        int igualdad = 0;
        switch (lexema) {
            case "break":
                estado = -59;
                break;
            case "continue":
                estado = -61;
                break;
            case "elif":
                estado = -62;
                break;
            case "else":
                estado = -63;
                break;
            case "for":
                estado = -67;
                break;
            case "if":
                estado = -68;
                break;
            case "in":
                estado = -70;
                break;
            case "is":
                estado = -71;
                break;
            case "isnot":
                estado = -72;
                break;
            case "print":
                estado = -75;
                break;
            case "return":
                estado = -77;
                break;
            case "while":
                estado = -79;
                break;
            case "true":
                estado = -81;
                break;
            case "false":
                estado = -82;
                break;
            case "none":
                estado = -83;
                break;
            case "range":
                estado = -84;
                break;
            case "input":
                estado = -85;
                break;
            case "println":
                estado = -86;
                break;
            case "end":
                estado = -87;
                break;
            case "wend":
                estado = -88;
                break;
            case "sort":
                estado = -89;
                break;
            case "reverse":
                estado = -90;
                break;
            case "count":
                estado = -91;
                break;
            case "index":
                estado = -92;
                break;
            case "append":
                estado = -93;
                break;
            case "extend":
                estado = -94;
                break;
            case "pop":
                estado = -95;
                break;
            case "remove":
                estado = -96;
                break;
            case "insert":
                estado = -97;
                break;
            case "innot":
                estado = -98;
                break;
            case "findall":
                estado = -99;
                break;
            case "replace":
                estado = -100;
                break;
            case "len":
                estado = -101;
                break;
            case "sample":
                estado = -102;
                break;
            case "choice":
                estado = -103;
                break;
            case "random":
                estado = -104;
                break;
            case "randrange":
                estado = -105;
                break;
            case "mean":
                estado = -106;
                break;
            case "median":
                estado = -107;
                break;
            case "variance":
                estado = -108;
                break;
            case "sum":
                estado = -109;
                break;
            case "def":
                estado = -111;
                break;
            case "to":
                estado = -116;
        }
        return estado;
    }

    private String destabulador(String lexema) {
        char[] aux = new char[lexema.length()];
        for (int i = 0; i < lexema.length(); i++) {
            aux[i] = lexema.charAt(i);
        }

        lexema = "";
        for (int i = 0; i < aux.length; i++) {
            if (aux[i] == '\t') {

            } else {
                lexema += aux[i];
            }
        }
        return lexema;
    }

    public int[] obtenerContadoresLexico() {
        for (int i = 0; i < controladorTokenError.obtenerArregloTokens().length; i++) {
            //contadoresLexico(controladorTokenError.obtenerArregloTokens()[i].getEstado());
        }
        return contadoresLexico;
    }

    public int[][] obtenerContadoresPorLineaLexico() {
        contadoresLinea = new int[lineaActual][21];
        for (int i = 0; i < controladorTokenError.obtenerArregloTokens().length - 1; i++) {
            switch (controladorTokenError.obtenerArregloTokens()[i].getEstado()) {
                case -6:
//                contadoresLexico[1]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][1]++;
                    break;
                case -3:
                case -5:
//                contadoresLexico[2]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][2]++;
                    break;
                case -56://Palabras reservadas
                case -57:
                case -58:
                case -59:
                case -60:
                case -61:
                case -62:
                case -63:
                case -64:
                case -65:
                case -66:
                case -67:
                case -68:
                case -69:
                case -73:
                case -74:
                case -75:
                case -76:
                case -77:
                case -78:
                case -79:
                case -80:
                case -81:
                case -82:
                case -83:
                case -84:
                case -85:
                case -86:
                case -87:
                case -88:
                case -89:
                case -90:
                case -91:
                case -92:
                case -93:
                case -94:
                case -95:
                case -96:
                case -97:
                case -99:
                case -100:
                case -101:
                case -102:
                case -103:
                case -104:
                case -105:
                case -106:
                case -107:
                case -108:
                case -109:
                case -111:
                case -116:
//                contadoresLexico[3]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][3]++;
                    break;
                case -7:
//                contadoresLexico[4]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][4]++;
                    break;
                case -10:
//                contadoresLexico[5]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][5]++;
                    break;
                case -12:
//                contadoresLexico[6]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][6]++;
                    break;
                case -11:
//                contadoresLexico[7]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][7]++;
                    break;
                case -2:
                case -4:
//                contadoresLexico[8]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][8]++;
                    break;
                case -8:
//                contadoresLexico[9]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][9]++;
                    break;
                case -9:
//                contadoresLexico[10]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][10]++;
                    break;
                case -1:
//                contadoresLexico[11]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][11]++;
                    break;
                case -14:
                case -17:
                case -20:
                case -22:
                case -24:
                case -26:
                case -28:
//                contadoresLexico[12]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][12]++;
                    break;
                case -15:
                case -18:
//                contadoresLexico[13]++;
                    contadoresLinea[lineaActual - 1][13]++;
                    break;
                case -30:
                case -33:
                case -35:
                case -117:
//                contadoresLexico[14]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][14]++;
                    break;
                case -32:
                case -34:
                case -44:
                case -37:
                case -40:
//                contadoresLexico[15]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][15]++;
                    break;
                case -71: //is    Identidad
                case -72: //is not
                case -98:
                case -70:
//                contadoresLexico[16]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][16]++;
                    break;
                case -36:
                case -38:
                case -39:
                case -41:
                case -43:
                case -31:
//                contadoresLexico[17]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][17]++;
                    break;
                case -45:
                case -46:
                case -112:
                case -110:
//                contadoresLexico[18]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][18]++;
                    break;
                case -13:
                case -47:
                case -48:
                case -53:
                case -51:
                case -52:
//                contadoresLexico[19]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][19]++;
                    break;
                case -42:
                case -16:
                case -19:
                case -23:
                case -21:
                case -27:
                case -25:
                case -29:
//                contadoresLexico[20]++;
                    contadoresLinea[controladorTokenError.obtenerArregloTokens()[i].getLinea() - 1][20]++;
                    break;
                default:
                    System.err.println("Ha llegado un contador no valido: "
                            + controladorTokenError.obtenerArregloTokens()[i].getEstado());
            }
        }
        for (int i = 0; i < controladorTokenError.obtenerArregloErrores().length - 1; i++) {
            contadoresLinea[controladorTokenError.obtenerArregloErrores()[i].getLinea()][0]++;
        }
        return contadoresLinea;
    }

    private void contadoresLexico(int estado) {
        switch (estado) {
            case -6:
                contadoresLexico[1]++;
                contadoresLinea[lineaActual - 1][1]++;
                break;
            case -3:
            case -5:
                contadoresLexico[2]++;
                contadoresLinea[lineaActual - 1][2]++;
                break;
            case -56://Palabras reservadas
            case -57:
            case -58:
            case -59:
            case -60:
            case -61:
            case -62:
            case -63:
            case -64:
            case -65:
            case -66:
            case -67:
            case -68:
            case -69:
            case -73:
            case -74:
            case -75:
            case -76:
            case -77:
            case -78:
            case -79:
            case -80:
            case -81:
            case -82:
            case -83:
            case -84:
            case -85:
            case -86:
            case -87:
            case -88:
            case -89:
            case -90:
            case -91:
            case -92:
            case -93:
            case -94:
            case -95:
            case -96:
            case -97:
            case -99:
            case -100:
            case -101:
            case -102:
            case -103:
            case -104:
            case -105:
            case -106:
            case -107:
            case -108:
            case -109:
            case -111:
            case -116:
                contadoresLexico[3]++;
                contadoresLinea[lineaActual - 1][3]++;
                break;
            case -7:
                contadoresLexico[4]++;
                contadoresLinea[lineaActual - 1][4]++;
                break;
            case -10:
                contadoresLexico[5]++;
                contadoresLinea[lineaActual - 1][5]++;
                break;
            case -12:
                contadoresLexico[6]++;
                contadoresLinea[lineaActual - 1][6]++;
                break;
            case -11:
                contadoresLexico[7]++;
                contadoresLinea[lineaActual - 1][7]++;
                break;
            case -2:
            case -4:
                contadoresLexico[8]++;
                contadoresLinea[lineaActual - 1][8]++;
                break;
            case -8:
                contadoresLexico[9]++;
                contadoresLinea[lineaActual - 1][9]++;
                break;
            case -9:
                contadoresLexico[10]++;
                contadoresLinea[lineaActual - 1][10]++;
                break;
            case -1:
                contadoresLexico[11]++;
                contadoresLinea[lineaActual - 1][11]++;
                break;
            case -14:
            case -17:
            case -20:
            case -22:
            case -24:
            case -26:
            case -28:
                contadoresLexico[12]++;
                contadoresLinea[lineaActual - 1][12]++;
                break;
            case -15:
            case -18:
                contadoresLexico[13]++;
                contadoresLinea[lineaActual - 1][13]++;
                break;
            case -30:
            case -33:
            case -35:
            case -117:
                contadoresLexico[14]++;
                contadoresLinea[lineaActual - 1][14]++;
                break;
            case -32:
            case -34:
            case -44:
            case -37:
            case -40:
                contadoresLexico[15]++;
                contadoresLinea[lineaActual - 1][15]++;
                break;
            case -71: //is    Identidad
            case -72: //is not
            case -98:
            case -70:
                contadoresLexico[16]++;
                contadoresLinea[lineaActual - 1][16]++;
                break;
            case -36:
            case -38:
            case -39:
            case -41:
            case -43:
            case -31:
                contadoresLexico[17]++;
                contadoresLinea[lineaActual - 1][17]++;
                break;
            case -45:
            case -46:
            case -112:
            case -110:
                contadoresLexico[18]++;
                contadoresLinea[lineaActual - 1][18]++;
                break;
            case -13:
            case -47:
            case -48:
            case -53:
            case -51:
            case -52:
                contadoresLexico[19]++;
                contadoresLinea[lineaActual - 1][19]++;
                break;
            case -42:
            case -16:
            case -19:
            case -23:
            case -21:
            case -27:
            case -25:
            case -29:
                contadoresLexico[20]++;
                contadoresLinea[lineaActual - 1][20]++;
                break;
            default:
                System.err.println("Ha llegado un contador no valido");
        }
    }
}
