/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador.Auxiliar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author marco
 */
public class AuxiliarSemantica1 {Workbook excel;
    String[][] matrizSuma, matrizResta, matrizMultiplicacion, matrizDivisionNormal,
            matrizDivisionResiduo, matrizAsignacion, matrizOperadores,
            matrizOperadoresAst, matrizDesplazamiento, matrizRelacionales,
            matrizLogicos;
    String[][][] matrices;
    String[] nombresMatrices = {"Suma", "Resta", "Multiplicacion", "DivisionNormal",
        "DivisionResiduo", "Asignacion", "Operadores", "OperadoresAst", "Desplazamiento",
        "Relacionales", "Logicos"};

    public AuxiliarSemantica1() {
        inicializarVariables();
    }

    public String getRelacion(String tipo, String fila, String columna) {
        System.err.println("<AUXILIAR SEMANTICA1 getRelacion> Tipo: " + tipo + ", " + getTipoMatriz(tipo));
        System.err.println("<AUXILIAR SEMANTICA1 getRelacion> Fila: " + fila + ", " + getIndiceTipoVariable(fila));
        System.err.println("<AUXILIAR SEMANTICA1 getRelacion> Columna: " + columna + ", " + getIndiceTipoVariable(columna));
        System.err.println("<AUXILIAR SEMANTICA1 getRelacion> Relacion: " + matrices[getTipoMatriz(tipo)][getIndiceTipoVariable(fila)][getIndiceTipoVariable(columna)]);
        return matrices[getTipoMatriz(tipo)][getIndiceTipoVariable(fila)][getIndiceTipoVariable(columna)];
    }

    public String getAsignacion(String fila, String columna) {
        System.err.println("<AUXILIAR SEMANTICA1 getAsignacion> Fila: " + fila + ", " + getIndiceTipoVariable(fila));
        System.err.println("<AUXILIAR SEMANTICA1 getAsignacion> Columna: " + columna + ", " + getIndiceTipoVariable(columna));
        return matrices[getTipoMatriz("Asignacion")][getIndiceTipoVariable(fila)][getIndiceTipoVariable(columna)];
    }

    private void inicializarVariables() {
        try {
            InputStream inp = new FileInputStream(new File("matriz-semantica1.xlsx"));
            excel = WorkbookFactory.create(inp);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AuxiliarSemantica1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(AuxiliarSemantica1.class.getName()).log(Level.SEVERE, null, ex);
        }
        matrices = new String[11][][];
        for (int i = 0; i < 11; i++) {
            matrices[i] = getMatrizFromExcel(nombresMatrices[i]);
        }
    }

    private int getTipoMatriz(String tipo) {
        for (int i = 0; i < nombresMatrices.length; i++) {
            if (nombresMatrices[i].equals(tipo)) {
                return i;
            }
        }
        return -1;
    }

    public String getTipoOperador(int operador) {
        switch (operador) {
            case -14:
                return "Suma";
            case -17:
                return "Resta";
            case -20:
                return "Multiplicacion";
            case -24:
                return "DivisionNormal";
            case -28:
                return "DivisionResiduo";
            case -44:
            case -35:
            case -33:
            case -32:
            case -34:
            case -30:
            case -117:
                return "Logicos";
            case -15:
            case -18:
                return "OperadoresAst";
            case -36:
            case -38:
            case -43:
            case -31:
            case -41:
            case -39:
            case -71:
            case -72:
            case -70:
            case -98:
                return "Relacionales";
            case -37:
            case -40:
                return "Desplazamiento";
            default:
                return "null";
        }
    }

    private int getIndiceTipoVariable(String tipo) {
        switch (tipo) {
            case "TD":
            case "decimal":
            case "Decimal":
                return 0;
            case "TO":
            case "Octal":
            case "octal":
                return 1;
            case "TB":
            case "Binario":
            case "binario":
                return 2;
            case "TH":
            case "Hexadecimal":
            case "hexadecimal":
                return 3;
            case "TF":
            case "flotante":
            case "Flotante":
                return 4;
            case "TCAD":
            case "Cadena":
            case "cadena":
                return 5;
            case "TCAR":
            case "Caracter":
            case "caracter":
                return 6;
            case "TCOM":
            case "Compleja":
            case "complejo":
            case "Complejo":
            case "compleja":
                return 7;
            case "TBOL":
            case "Booleana":
            case "Boolean":
            case "booleano":
                return 8;
            case "TN":
            case "None":
            case "none":
                return 9;
            case "TT":
            case "Tupla":
            case "tupla":
                return 10;
            case "TL":
            case "Lista":
            case "lista":
                return 11;
            case "TA":
            case "Arreglo":
            case "arreglo":
                return 12;
            case "TDIC":
            case "Diccionario":
            case "diccionario":
                return 13;
            case "TV":
            case "variant":
                return 14;
            default:
                return -1;
        }
    }

    public int getTokenTemporal(String tipo, Contadores.ContadorSemantica1 contadores[]) {
        switch (tipo) {
            case "decimal":
            case "Decimal":
                contadores[contadores.length-1].setTD(contadores[contadores.length-1].getTD()+1);
                return listaTemporales[0];
            case "Octal":
            case "octal":
                contadores[contadores.length-1].setTDO(contadores[contadores.length-1].getTDO()+1);
                return listaTemporales[1];
            case "Binario":
            case "binario":
                contadores[contadores.length-1].setTDB(contadores[contadores.length-1].getTDB()+1);
                return listaTemporales[2];
            case "Hexadecimal":
            case "hexadecimal":
                contadores[contadores.length-1].setTDH(contadores[contadores.length-1].getTDH()+1);
                return listaTemporales[3];
            case "flotante":
            case "Flotante":
                contadores[contadores.length-1].setTF(contadores[contadores.length-1].getTF()+1);
                return listaTemporales[4];
            case "Cadena":
            case "cadena":
                contadores[contadores.length-1].setTC(contadores[contadores.length-1].getTC()+1);
                return listaTemporales[5];
            case "Caracter":
            case "caracter":
                contadores[contadores.length-1].setTCH(contadores[contadores.length-1].getTCH()+1);
                return listaTemporales[6];
            case "Compleja":
            case "complejo":
            case "Complejo":
            case "compleja":
                contadores[contadores.length-1].setTCM(contadores[contadores.length-1].getTCM()+1);
                return listaTemporales[7];
            case "Booleana":
            case "Boolean":
            case "booleano":
                contadores[contadores.length-1].setTB(contadores[contadores.length-1].getTB()+1);
                return listaTemporales[8];
            case "None":
            case "none":
                return listaTemporales[9];
            case "Tupla":
            case "tupla":
                contadores[contadores.length-1].setTT(contadores[contadores.length-1].getTT()+1);
                return listaTemporales[10];
            case "Lista":
            case "lista":
                return listaTemporales[11];
            case "Arreglo":
            case "arreglo":
                contadores[contadores.length-1].setTA(contadores[contadores.length-1].getTA()+1);
                return listaTemporales[12];
            case "Diccionario":
            case "diccionario":
                contadores[contadores.length-1].setTDic(contadores[contadores.length-1].getTDic()+1);
                return listaTemporales[13];
            case "variant":
                contadores[contadores.length-1].setTV(contadores[contadores.length-1].getTV()+1);
                return listaTemporales[14];
            default:
                return 950;
        }
    }
    //                         TD    TO    TB    TH    TF  TCAD  TCAR  TCOM  TBOL    TN    TT    TL    TA  TDIC    TV
    int listaTemporales[] = {-400, -401, -402, -403, -404, -405, -406, -407, -408, -409, -410, -411, -412, -413, -414};

    public String getTipoConstante(int token) {
        String tipo = "";
        switch (token) {
            case -7:
            case -400:
                tipo = "Decimal";
                break;
            case -401:
            case -11:
                tipo = "octal";
                break;
            case -9:
            case -407:
                tipo = "Complejo";
                break;
            case -10:
            case -402:
                tipo = "Binario";
                break;
            case -12:
            case -403:
                tipo = "Hexadecimal";
                break;
            case -8:
            case -404:
                tipo = "Flotante";
                break;
            case -4:
            case -405:
                tipo = "Cadena";
                break;
            case -1:
            case -406:
                tipo = "Caracter";
                break;
            case -81:
            case -82:
            case -408:
                tipo = "Boolean";
                break;
            case -414:
                tipo = "variant";
                break;
            case -411:
                tipo = "lista";
                break;
            case -409:
                tipo = "none";
                break;
            case -410:
                tipo = "tupla";
                break;
            case -412:
                tipo = "arreglo";
                break;
            case -413:
                tipo = "diccionario";
                break;
            default:
                return tipo;
        }
        return tipo;
    }

    public boolean verificarExistencia(int token, int[] arreglo) {
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i] == token) {
                return true;
            }
        }
        return false;
    }

    private String[][] getMatrizFromExcel(String hoja) {
        String matriz[][] = new String[15][15];
        Sheet sheet = excel.getSheet(hoja);
        for (int i = 1; i < 16; i++) {
            Row row = sheet.getRow(i);
            for (int j = 1; j < 16; j++) {
                matriz[i - 1][j - 1] = row.getCell(j) + "";
            }
        }
        return matriz;
    }
}
