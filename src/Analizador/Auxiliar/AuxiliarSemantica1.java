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
public class AuxiliarSemantica1 {

    Workbook excel;
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
        System.out.println("<AUXILIAR SEMANTICA 1> Tipo: "+ tipo + ", "+ getTipoMatriz(tipo));
        System.out.println("<AUXILIAR SEMANTICA 1> Fila: "+ fila + ", "+ getIndiceTipoVariable(fila));
        System.out.println("<AUXILIAR SEMANTICA 1> Columna: "+ columna + ", "+ getIndiceTipoVariable(columna));
        return matrices[getTipoMatriz(tipo)][getIndiceTipoVariable(fila)][getIndiceTipoVariable(columna)];
    }
    
    public String getAsignacion(String fila, String columna){
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
                return 1;
            case "TB":
            case "Binario":
                return 2;
            case "TH":
            case "Hexadecimal":
                return 3;
            case "TF":
            case "flotante":
            case "Flotante":
                return 4;
            case "TCAD":
            case "Cadena":
                return 5;
            case "TCAR":
            case "Caracter":
                return 6;
            case "TCOM":
            case "Compleja":
                return 7;
            case "TBOL":
            case "Booleana":
            case "Boolean":
                return 8;
            case "TN":
            case "None":
                return 9;
            case "TT":
            case "Tupla":
                return 10;
            case "TL":
            case "Lista":
                return 11;
            case "TA":
            case "Arreglo":
                return 12;
            case "TDIC":
            case "Diccionario":
                return 13;
            case "TV":
            case "variant":
                return 14;
            default:
                return -1;
        }
    }
    
    
    public int getTokenTemporal(String tipo) {
        switch (tipo) {
            case "decimal":
            case "Decimal":
                return listaTemporales[0];
            case "Octal":
                return listaTemporales[1];
            case "Binario":
                return listaTemporales[2];
            case "Hexadecimal":
                return listaTemporales[3];
            case "flotante":
            case "Flotante":
                return listaTemporales[4];
            case "Cadena":
                return listaTemporales[5];
            case "Caracter":
                return listaTemporales[6];
            case "Compleja":
                return listaTemporales[7];
            case "Booleana":
            case "Boolean":
                return listaTemporales[8];
            case "None":
                return listaTemporales[9];
            case "Tupla":
                return listaTemporales[10];
            case "Lista":
                return listaTemporales[11];
            case "Arreglo":
                return listaTemporales[12];
            case "Diccionario":
                return listaTemporales[13];
            case "variant":
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
            default:
                return tipo;
        }
        return tipo;
    }
    
    public boolean verificarExistencia(int token, int[] arreglo){
        for(int i = 0; i < arreglo.length; i++){
            if(arreglo[i] == token){
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
