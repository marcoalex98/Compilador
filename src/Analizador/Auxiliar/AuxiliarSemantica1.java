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
    String[][]  matrizSuma, matrizResta, matrizMultiplicacion, matrizDivisionNormal,
            matrizDivisionResiduo, matrizAsignacion, matrizOperadores,
            matrizOperadoresAst, matrizDesplazamiento, matrizRelacionales,
            matrizLogicos;
    String [][][] matrices;
    String[] nombresMatrices = {"Suma", "Resta", "Multiplicacion", "DivisionNormal",
        "DivisionResiduo", "Asignacion", "Operadores", "OperadoresAst", "Desplazamiento",
        "Relacionales", "Logicos"};
    
    public AuxiliarSemantica1(){
        inicializarVariables();
    }
    
    public String getRelacion(String tipo, String fila, String columna){
        return matrices[getTipoMatriz(tipo)][getTipoVariable(fila)][getTipoVariable(columna)];
    }
    
    private void inicializarVariables(){
        try {
            InputStream inp = new FileInputStream(new File("matriz-semantica1.xlsx"));
            excel = WorkbookFactory.create(inp);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AuxiliarSemantica1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(AuxiliarSemantica1.class.getName()).log(Level.SEVERE, null, ex);
        }
        matrices = new String[11][][];
        for(int i = 0; i < 11; i++){
            matrices[i] = getMatrizFromExcel(nombresMatrices[i]);
        }
        System.err.println(getRelacion("Suma", "Decimal", "Decimal"));
        
        for(int i = 0; i < nombresMatrices.length; i++){
            imprimirMatriz(matrices[i]);
        }
    }
    
    private void imprimirMatriz(String[][] matriz){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                System.err.print(matriz[i][j]+" ");
            }
            System.err.println("");
        }
        System.err.println("");
    }
    
    private int getTipoMatriz(String tipo){
        for(int i = 0; i < nombresMatrices.length; i++){
            if(nombresMatrices[i].equals(tipo))
                return i;
        }
        return -1;
    }
    
    private int getTipoVariable(String tipo){
        switch (tipo){
            case "Decimal": return 0;
            case "Octal": return 1;
            case "Binario": return 2;
            case "Hexadecimal": return 3;
            case "Flotante": return 4;
            case "Cadena": return 5;
            case "Caracter": return 6;
            case "Compleja": return 7;
            case "Booleana": return 8;
            case "None": return 9;
            case "Tupla": return 10;
            case "Lista": return 11;
            case "Arreglo": return 12;
            case "Diccionario": return 13;
            case "variant": return 14;
            default: return -1;
        }
    }
    
    private String[][] getMatrizFromExcel(String hoja){
        String matriz[][] = new String[15][15];
        Sheet sheet = excel.getSheet(hoja);
        for(int i = 1; i < 16; i++){
            Row row = sheet.getRow(i);
            for(int j = 1; j<16; j++){
                matriz[i-1][j-1] = row.getCell(j)+"";
            }
        }
        return matriz;
    }
}
