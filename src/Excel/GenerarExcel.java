package Excel;

import Analizador.Semantica1;
import Analizador.Semantica2;
import SQL.TablaSimbolosMySQL;
import Contadores.ContadorAmbito;
import Modelos.Token;
import Modelos.Error;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import Contadores.ContadorAmbito;
import SQL.ControladorSQL;

public class GenerarExcel {

    Connection con;
    WritableWorkbook woorkbook;
    WritableSheet sheet;
    WritableFont h;
    WritableFont htitulos;
    WritableCellFormat hFormat;
    WritableCellFormat titulosFormat;
    
    private void generarHojasSemantica2(int ambitos, Semantica2 analizador){
        generarHojaListaDeSemantica2();
        generarHojaReglas();
        generarHojaReglasPorAmbito(ambitos);
        analizador.imprimirReglas();
    }
    
    private void generarHojaListaDeSemantica2(){
        sheet = woorkbook.createSheet("Lista de Semántica 2", 8);
        String titulosHoja[] = {"Regla","Tope Pila","Valor Real","Linea","Edo","Ambito"};
        String reglas[] = {"1010","1011","1012","1020","1021","1030","1031","1040",
        "1060","1061","1070","1071","1080","1081","1082","1090","1100","1110","1120",
        "1130","1140","1150","1160","1161","1170"};
        try{
            for (int i = 0; i < titulosHoja.length; i++) {
                sheet.addCell(new jxl.write.Label(i, 0, titulosHoja[i], titulosFormat));
            }
            for (int i = 0; i < reglas.length; i++) {
                sheet.addCell(new jxl.write.Label(0, i+1, reglas[i], titulosFormat));
            }
            
            for (int i = 0; i < titulosHoja.length - 1; i++) {
                for (int j = 0; j < reglas.length; j++) {
                    sheet.addCell(new jxl.write.Label(i+1, j+1, "-", hFormat));
                }
            }
        }catch (WriteException ex) {
            Logger.getLogger(GenerarExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generarHojaReglas(){
        sheet = woorkbook.createSheet("Reglas", 9);
        String titulosHoja[] = {"Regla","Aparecen","Aceptada","Errores"};
        String reglas[] = {"1010","1011","1012","1020","1021","1030","1031","1040",
        "1060","1061","1070","1071","1080","1081","1082","1090","1100","1110","1120",
        "1130","1140","1150","1160","1161","1170"};
        try{
            for (int i = 0; i < titulosHoja.length; i++) {
                sheet.addCell(new jxl.write.Label(i, 0, titulosHoja[i], titulosFormat));
            }
            for (int i = 0; i < reglas.length; i++) {
                sheet.addCell(new jxl.write.Label(0, i+1, reglas[i], titulosFormat));
            }
            
            for (int i = 0; i < titulosHoja.length - 1; i++) {
                for (int j = 0; j < reglas.length; j++) {
                    sheet.addCell(new jxl.write.Label(i+1, j+1, "0", hFormat));
                }
            }
        }catch (WriteException ex) {
            Logger.getLogger(GenerarExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generarHojaReglasPorAmbito(int ambitos){
        sheet = woorkbook.createSheet("Reglas por Ambito", 10);
        String titulosHoja[] = {"Regla","Ambitos Definidos","Totales"};
        String reglas[] = {"1010","1011","1012","1020","1021","1030","1031","1040",
        "1060","1061","1070","1071","1080","1081","1082","1090","1100","1110","1120",
        "1130","1140","1150","1160","1161","1170","Total"};
        try{
            int col1 = 0; int col2 = 0; int row1 = 0; int row2 = 1;
            sheet.mergeCells(col1,row1,col2,row2);
            for (int i = 0; i < titulosHoja.length; i++) {
                if(i == titulosHoja.length - 1){
                    sheet.addCell(new jxl.write.Label(i+ambitos-1, 0, titulosHoja[i], titulosFormat));
                }else{
                    sheet.addCell(new jxl.write.Label(i, 0, titulosHoja[i], titulosFormat));
                }   
            }
            col1 = 1; col2 = ambitos; row1 = 0; row2 = 0;
            sheet.mergeCells(col1,row1,col2,row2);
            for (int i = 0; i < ambitos; i++){
                sheet.addCell(new jxl.write.Label(i+1, 1, i+"", titulosFormat));
            }
            col1 = ambitos + 1; col2 = ambitos + 1; row1 = 0; row2 = 1;
            sheet.mergeCells(col1,row1,col2,row2);
            for (int i = 0; i < reglas.length; i++) {
                sheet.addCell(new jxl.write.Label(0, i+2, reglas[i], titulosFormat));
            }
            for (int i = 0; i < ambitos + 1; i++) {
                for (int j = 0; j < reglas.length; j++) {
                    sheet.addCell(new jxl.write.Label(i+1, j+2, "0", hFormat));
                }
            }
        }catch (WriteException ex) {
            Logger.getLogger(GenerarExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generarExcel(Token[] entradaTokens, Error[] entradaErrores, int[] contadores,
            int[][] contadoresLinea, int[] contadoresSin, ContadorAmbito[] ambito, Semantica1 analizadorSemantica1,
            Semantica2 analizadorSemantica2, String nombreExcel) {
        String ruta = "MarcoAlejandroMarcialCoronado-"+nombreExcel+".xls";
        try {//Hoja 1 - Lista de Tokens
            WorkbookSettings conf = new WorkbookSettings();
            conf.setEncoding("ISO-8859-1");
            woorkbook = Workbook.createWorkbook(new File(ruta), conf);
            sheet = woorkbook.createSheet("Lista de Tokens", 0);
            h = new WritableFont(WritableFont.COURIER, 16, WritableFont.NO_BOLD);
            htitulos = new WritableFont(WritableFont.COURIER, 16, WritableFont.BOLD);
            hFormat = new WritableCellFormat(h);
            titulosFormat = new WritableCellFormat(htitulos);
            try {
                sheet.addCell(new jxl.write.Label(0, 0, "Linea", titulosFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "Token", titulosFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "Lexema", titulosFormat));

                for (int i = 0; i < entradaTokens.length-1; i++) {//fila
                    sheet.addCell(new jxl.write.Label(0, i + 1, entradaTokens[i].getLinea() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(1, i + 1, entradaTokens[i].getEstado() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(2, 1 + i, entradaTokens[i].getLexema() + "", hFormat));
                }
                sheet = woorkbook.createSheet("Lista de Errores", 1);
                sheet.addCell(new jxl.write.Label(0, 0, "Linea", titulosFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "Error", titulosFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "Descripción", titulosFormat));
                sheet.addCell(new jxl.write.Label(3, 0, "Lexema", titulosFormat));
                sheet.addCell(new jxl.write.Label(4, 0, "Tipo", titulosFormat));
                for (int i = 0; i < entradaErrores.length-1; i++) {//fila
                    sheet.addCell(new jxl.write.Label(0, i + 1, entradaErrores[i].getLinea() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(1, i + 1, entradaErrores[i].getEstado() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(2, 1 + i, entradaErrores[i].getDescripcion() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(3, 1 + i, entradaErrores[i].getLexema() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(4, 1 + i, entradaErrores[i].getTipo() + "", hFormat));

                }
                //---Pagina 3--- 
                sheet = woorkbook.createSheet("Contadores", 2);
                String[] encabezado = {"Errores", "Identificadores", "Comentarios", "Palabras Reservadas",
                    "CE-Dec", "CE-Bin", "CE-Hex", "CE-Oct", "CTexto", "CFloat", "CNComp", "Ccar",
                    "Aritmeticos", "Monogamo", "Logico", "Bit", "Identidad", "Relacionales",
                    "Puntuacion", "Agrupacion", "Asignacion"};
                for (int i = 0; i < 21; i++) {
                    sheet.addCell(new jxl.write.Label(i, 0, encabezado[i], titulosFormat));
                }
                for (int i = 0; i < contadores.length; i++) {
                    sheet.addCell(new jxl.write.Label(i, 1, contadores[i] + "", hFormat));
                }
                //---Pagina 4---
                sheet = woorkbook.createSheet("Tabla de Contadores", 3);
                String[] encabezado2 = {"Linea", "Errores", "Identificadores", "Comentarios", "Palabras Reservadas",
                    "CE-Dec", "CE-Bin", "CE-Hex", "CE-Oct", "CTexto", "CFloat", "CNComp", "Ccar",
                    "Aritmeticos", "Monogamo", "Logico", "Bit", "Identidad", "Relacionales",
                    "Puntuacion", "Agrupacion", "Asignacion"};
                for (int i = 0; i < 22; i++) {
                    sheet.addCell(new jxl.write.Label(i, 0, encabezado2[i], titulosFormat));
                }
                System.out.println("Largo de arreglo de contadoresLinea: " + contadoresLinea.length);
                for (int i = 0; i < contadoresLinea.length; i++) {
                    sheet.addCell(new jxl.write.Label(0, i + 1, (i + 1) + "", hFormat));
                }
                System.out.println("--------------FOR DE EXCEL LEXICO----------------");
                for (int i = 0; i < contadoresLinea.length; i++) {
                    for (int j = 0; j < 21; j++) {
                        System.out.print(contadoresLinea[i][j]);
                        sheet.addCell(new jxl.write.Label(j + 1, i + 1, contadoresLinea[i][j] + "", hFormat));
                    }
                    System.out.println("");
                }
                //---Pagina 5---
                sheet = woorkbook.createSheet("Sintaxis", 4);
                sheet.addCell(new jxl.write.Label(0, 0, "Program", titulosFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "CONSTANTE", titulosFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "CONST ENTERO", titulosFormat));
                sheet.addCell(new jxl.write.Label(3, 0, "LIST-TUP-RANGOS", titulosFormat));
                sheet.addCell(new jxl.write.Label(4, 0, "Termino Pascal", titulosFormat));
                sheet.addCell(new jxl.write.Label(5, 0, "ELEVACION", titulosFormat));
                sheet.addCell(new jxl.write.Label(6, 0, "SIMPLE EXP-PAS", titulosFormat));
                sheet.addCell(new jxl.write.Label(7, 0, "FACTOR", titulosFormat));
                sheet.addCell(new jxl.write.Label(8, 0, "NOT", titulosFormat));
                sheet.addCell(new jxl.write.Label(9, 0, "OR", titulosFormat));
                sheet.addCell(new jxl.write.Label(10, 0, "OPBIT", titulosFormat));
                sheet.addCell(new jxl.write.Label(11, 0, "AND", titulosFormat));
                sheet.addCell(new jxl.write.Label(12, 0, "ANDLOG", titulosFormat));
                sheet.addCell(new jxl.write.Label(13, 0, "ORLOG", titulosFormat));
                sheet.addCell(new jxl.write.Label(14, 0, "XORLOG", titulosFormat));
                sheet.addCell(new jxl.write.Label(15, 0, "EST", titulosFormat));
                sheet.addCell(new jxl.write.Label(16, 0, "ASIGN", titulosFormat));
                sheet.addCell(new jxl.write.Label(17, 0, "FunList", titulosFormat));
                sheet.addCell(new jxl.write.Label(18, 0, "ARR", titulosFormat));
                sheet.addCell(new jxl.write.Label(19, 0, "FUNCIONES", titulosFormat));
                sheet.addCell(new jxl.write.Label(20, 0, "EXP-PAS", titulosFormat));
                for (int i = 0; i < contadoresSin.length; i++) {//fila
                    sheet.addCell(new jxl.write.Label(i, 1, contadoresSin[i] + "", hFormat));
                }
                //Pagina 6 -----------------------------------Ambito------------------------------
                sheet = woorkbook.createSheet("Ambito", 5);
                sheet.addCell(new jxl.write.Label(0, 0, "Ambito", titulosFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "Decimal", titulosFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "Binario", titulosFormat));
                sheet.addCell(new jxl.write.Label(3, 0, "Octal", titulosFormat));
                sheet.addCell(new jxl.write.Label(4, 0, "Hexadecimal", titulosFormat));
                sheet.addCell(new jxl.write.Label(5, 0, "Flotante", titulosFormat));
                sheet.addCell(new jxl.write.Label(6, 0, "Cadena", titulosFormat));
                sheet.addCell(new jxl.write.Label(7, 0, "Caracter", titulosFormat));
                sheet.addCell(new jxl.write.Label(8, 0, "Compleja", titulosFormat));
                sheet.addCell(new jxl.write.Label(9, 0, "Booleana", titulosFormat));
                sheet.addCell(new jxl.write.Label(10, 0, "None", titulosFormat));
                sheet.addCell(new jxl.write.Label(11, 0, "Arreglo", titulosFormat));
                sheet.addCell(new jxl.write.Label(12, 0, "Tupla", titulosFormat));
                sheet.addCell(new jxl.write.Label(13, 0, "Lista", titulosFormat));
                sheet.addCell(new jxl.write.Label(14, 0, "Rango", titulosFormat));
                sheet.addCell(new jxl.write.Label(15, 0, "Diccionario", titulosFormat));
                sheet.addCell(new jxl.write.Label(16, 0, "Datos de estructuras", titulosFormat));
                sheet.addCell(new jxl.write.Label(17, 0, "Total/Ambito", titulosFormat));

                int filaMaximaAmbito = 0;
                for (int i = 0; i < ambito.length; i++) {
                    sheet.addCell(new jxl.write.Label(0, i + 1, i + "", hFormat));
                    sheet.addCell(new jxl.write.Label(1, i + 1, ambito[i].Decimal + "", hFormat));
                    sheet.addCell(new jxl.write.Label(2, i + 1, ambito[i].Binario + "", hFormat));
                    sheet.addCell(new jxl.write.Label(3, i + 1, ambito[i].Octal + "", hFormat));
                    sheet.addCell(new jxl.write.Label(4, i + 1, ambito[i].Hexadecimal + "", hFormat));
                    sheet.addCell(new jxl.write.Label(5, i + 1, ambito[i].Flotante + "", hFormat));
                    sheet.addCell(new jxl.write.Label(6, i + 1, ambito[i].Cadena + "", hFormat));
                    sheet.addCell(new jxl.write.Label(7, i + 1, ambito[i].Caracter + "", hFormat));
                    sheet.addCell(new jxl.write.Label(8, i + 1, ambito[i].Compleja + "", hFormat));
                    sheet.addCell(new jxl.write.Label(9, i + 1, ambito[i].Booleana + "", hFormat));
                    sheet.addCell(new jxl.write.Label(10, i + 1, ambito[i].None + "", hFormat));
                    sheet.addCell(new jxl.write.Label(11, i + 1, ambito[i].Arreglo + "", hFormat));
                    sheet.addCell(new jxl.write.Label(12, i + 1, ambito[i].Tupla + "", hFormat));
                    sheet.addCell(new jxl.write.Label(13, i + 1, ambito[i].Lista + "", hFormat));
                    sheet.addCell(new jxl.write.Label(14, i + 1, ambito[i].Rango + "", hFormat));
                    sheet.addCell(new jxl.write.Label(15, i + 1, ambito[i].Diccionario + "", hFormat));
                    sheet.addCell(new jxl.write.Label(16, i + 1, ambito[i].DatoEstructura + "", hFormat));
                    sheet.addCell(new jxl.write.Label(17, i + 1, ambito[i].getTotalAmbito() + "", hFormat));
                    filaMaximaAmbito = i;
                }
                filaMaximaAmbito += 2;
                int totalVariablesAmbito[] = new int[17];

                for (int i = 0; i < ambito.length; i++) {
                    totalVariablesAmbito[0] += ambito[i].Decimal;
                    totalVariablesAmbito[1] += ambito[i].Binario;
                    totalVariablesAmbito[2] += ambito[i].Octal;
                    totalVariablesAmbito[3] += ambito[i].Hexadecimal;
                    totalVariablesAmbito[4] += ambito[i].Flotante;
                    totalVariablesAmbito[5] += ambito[i].Cadena;
                    totalVariablesAmbito[6] += ambito[i].Caracter;
                    totalVariablesAmbito[7] += ambito[i].Compleja;
                    totalVariablesAmbito[8] += ambito[i].Booleana;
                    totalVariablesAmbito[9] += ambito[i].None;
                    totalVariablesAmbito[10] += ambito[i].Arreglo;
                    totalVariablesAmbito[11] += ambito[i].Tupla;
                    totalVariablesAmbito[12] += ambito[i].Lista;
                    totalVariablesAmbito[13] += ambito[i].Rango;
                    totalVariablesAmbito[14] += ambito[i].Diccionario;
                    totalVariablesAmbito[15] += ambito[i].DatoEstructura;
                    totalVariablesAmbito[16] += ambito[i].getTotalAmbito();

                }
                sheet.addCell(new jxl.write.Label(0, filaMaximaAmbito, "Total", hFormat));
                sheet.addCell(new jxl.write.Label(1, filaMaximaAmbito, totalVariablesAmbito[0] + "", hFormat));
                sheet.addCell(new jxl.write.Label(2, filaMaximaAmbito, totalVariablesAmbito[1] + "", hFormat));
                sheet.addCell(new jxl.write.Label(3, filaMaximaAmbito, totalVariablesAmbito[2] + "", hFormat));
                sheet.addCell(new jxl.write.Label(4, filaMaximaAmbito, totalVariablesAmbito[3] + "", hFormat));
                sheet.addCell(new jxl.write.Label(5, filaMaximaAmbito, totalVariablesAmbito[4] + "", hFormat));
                sheet.addCell(new jxl.write.Label(6, filaMaximaAmbito, totalVariablesAmbito[5] + "", hFormat));
                sheet.addCell(new jxl.write.Label(7, filaMaximaAmbito, totalVariablesAmbito[6] + "", hFormat));
                sheet.addCell(new jxl.write.Label(8, filaMaximaAmbito, totalVariablesAmbito[7] + "", hFormat));
                sheet.addCell(new jxl.write.Label(9, filaMaximaAmbito, totalVariablesAmbito[8] + "", hFormat));
                sheet.addCell(new jxl.write.Label(10, filaMaximaAmbito, totalVariablesAmbito[9] + "", hFormat));
                sheet.addCell(new jxl.write.Label(11, filaMaximaAmbito, totalVariablesAmbito[10] + "", hFormat));
                sheet.addCell(new jxl.write.Label(12, filaMaximaAmbito, totalVariablesAmbito[11] + "", hFormat));
                sheet.addCell(new jxl.write.Label(13, filaMaximaAmbito, totalVariablesAmbito[12] + "", hFormat));
                sheet.addCell(new jxl.write.Label(14, filaMaximaAmbito, totalVariablesAmbito[13] + "", hFormat));
                sheet.addCell(new jxl.write.Label(15, filaMaximaAmbito, totalVariablesAmbito[14] + "", hFormat));
                sheet.addCell(new jxl.write.Label(16, filaMaximaAmbito, totalVariablesAmbito[15] + "", hFormat));
                sheet.addCell(new jxl.write.Label(17, filaMaximaAmbito, totalVariablesAmbito[16] + "", hFormat));

                //Pagina 6 -----------------------------------Ambito------------------------------
                sheet = woorkbook.createSheet("Tabla de Simbolos", 6);
                sheet.addCell(new jxl.write.Label(0, 0, "ID", titulosFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "Tipo", titulosFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "Clase", titulosFormat));
                sheet.addCell(new jxl.write.Label(3, 0, "Ambito", titulosFormat));
                sheet.addCell(new jxl.write.Label(4, 0, "Tamano Arreglo", titulosFormat));
                sheet.addCell(new jxl.write.Label(5, 0, "Ambito Creado", titulosFormat));
                sheet.addCell(new jxl.write.Label(6, 0, "NoPos", titulosFormat));
                sheet.addCell(new jxl.write.Label(7, 0, "Lista Pertenece", titulosFormat));
                sheet.addCell(new jxl.write.Label(8, 0, "Rango", titulosFormat));
                sheet.addCell(new jxl.write.Label(9, 0, "Avance", titulosFormat));
                sheet.addCell(new jxl.write.Label(10, 0, "Llave", titulosFormat));
                sheet.addCell(new jxl.write.Label(11, 0, "Valor", titulosFormat));

                ResultSet rs = ejecutarQuery("SELECT * FROM tablasimbolos");
                int fila = 1;
                try {
                    while (rs.next()) {
                        sheet.addCell(new jxl.write.Label(0, fila, rs.getString("id"), hFormat));
                        sheet.addCell(new jxl.write.Label(1, fila, rs.getString("tipo"), hFormat));
                        sheet.addCell(new jxl.write.Label(2, fila, rs.getString("clase"), hFormat));
                        sheet.addCell(new jxl.write.Label(3, fila, rs.getString("ambito"), hFormat));
                        sheet.addCell(new jxl.write.Label(4, fila, rs.getString("tamanoArreglo"), hFormat));
                        sheet.addCell(new jxl.write.Label(5, fila, rs.getString("ambitoCreado"), hFormat));
                        sheet.addCell(new jxl.write.Label(6, fila, rs.getString("noPos"), hFormat));
                        sheet.addCell(new jxl.write.Label(7, fila, rs.getString("listaPertenece"), hFormat));
                        sheet.addCell(new jxl.write.Label(8, fila, rs.getString("rango"), hFormat));
                        sheet.addCell(new jxl.write.Label(9, fila, rs.getString("avance"), hFormat));
                        sheet.addCell(new jxl.write.Label(10, fila, rs.getString("llave"), hFormat));
                        sheet.addCell(new jxl.write.Label(11, fila, rs.getString("valor"), hFormat));
//                        modelo.addRow(new Object[]{rs.getString("id"), rs.getString("tipo"), rs.getString("clase"),
//                            rs.getString("ambito"), rs.getString("tamanoArreglo"), rs.getString("ambitoCreado"),
//                            rs.getString("noPos"), rs.getString("listaPertenece"), rs.getString("rango"),
//                            rs.getString("avance"), rs.getString("llave"), rs.getString("valor")});
                        fila++;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(TablaSimbolosMySQL.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex);
                }

                //---Pagina 5--- SEMANTICA 1
                sheet = woorkbook.createSheet("Semantica 1", 7);
                sheet.addCell(new jxl.write.Label(0, 0, "Linea", titulosFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "TD", titulosFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "TDO", titulosFormat));
                sheet.addCell(new jxl.write.Label(3, 0, "TDB", titulosFormat));
                sheet.addCell(new jxl.write.Label(4, 0, "TDH", titulosFormat));
                sheet.addCell(new jxl.write.Label(5, 0, "TF", titulosFormat));
                sheet.addCell(new jxl.write.Label(6, 0, "TC", titulosFormat));
                sheet.addCell(new jxl.write.Label(7, 0, "TCH", titulosFormat));
                sheet.addCell(new jxl.write.Label(8, 0, "TCM", titulosFormat));
                sheet.addCell(new jxl.write.Label(9, 0, "TB", titulosFormat));
                sheet.addCell(new jxl.write.Label(10, 0, "TT", titulosFormat));
                sheet.addCell(new jxl.write.Label(11, 0, "TL", titulosFormat));
                sheet.addCell(new jxl.write.Label(12, 0, "TA", titulosFormat));
                sheet.addCell(new jxl.write.Label(13, 0, "TDic", titulosFormat));
                sheet.addCell(new jxl.write.Label(14, 0, "TV", titulosFormat));
                sheet.addCell(new jxl.write.Label(15, 0, "Errores", titulosFormat));
                sheet.addCell(new jxl.write.Label(16, 0, "Asignacion", titulosFormat));
                
                for(int i = 0; i < analizadorSemantica1.contadores.length;i++){
                sheet.addCell(new jxl.write.Label(0, i+1, analizadorSemantica1.contadores[i].getLinea()+"", hFormat));
                sheet.addCell(new jxl.write.Label(1, i+1, analizadorSemantica1.contadores[i].getTD()+"", hFormat));
                sheet.addCell(new jxl.write.Label(2, i+1, analizadorSemantica1.contadores[i].getTDO()+"", hFormat));
                sheet.addCell(new jxl.write.Label(3, i+1, analizadorSemantica1.contadores[i].getTDB()+"", hFormat));
                sheet.addCell(new jxl.write.Label(4, i+1, analizadorSemantica1.contadores[i].getTDH()+"", hFormat));
                sheet.addCell(new jxl.write.Label(5, i+1, analizadorSemantica1.contadores[i].getTF()+"", hFormat));
                sheet.addCell(new jxl.write.Label(6, i+1, analizadorSemantica1.contadores[i].getTC()+"", hFormat));
                sheet.addCell(new jxl.write.Label(7, i+1, analizadorSemantica1.contadores[i].getTCH()+"", hFormat));
                sheet.addCell(new jxl.write.Label(8, i+1, analizadorSemantica1.contadores[i].getTCM()+"", hFormat));
                sheet.addCell(new jxl.write.Label(9, i+1, analizadorSemantica1.contadores[i].getTB()+"", hFormat));
                sheet.addCell(new jxl.write.Label(10, i+1, analizadorSemantica1.contadores[i].getTT()+"", hFormat));
                sheet.addCell(new jxl.write.Label(11, i+1, analizadorSemantica1.contadores[i].getTL()+"", hFormat));
                sheet.addCell(new jxl.write.Label(12, i+1, analizadorSemantica1.contadores[i].getTA()+"", hFormat));
                sheet.addCell(new jxl.write.Label(13, i+1, analizadorSemantica1.contadores[i].getTDic()+"", hFormat));
                sheet.addCell(new jxl.write.Label(14, i+1, analizadorSemantica1.contadores[i].getTV()+"", hFormat));
                sheet.addCell(new jxl.write.Label(15, i+1, analizadorSemantica1.contadores[i].getErrores()+"", hFormat));
                sheet.addCell(new jxl.write.Label(16, i+1, analizadorSemantica1.contadores[i].getAsignacion(), hFormat));
                }
//                for (int i = 0; i < contadoresLineaSemantica1.length; i++) {
//                    contadoresLineaSemantica1[i] = 0;
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTD();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTDO();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTDB();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTDH();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTF();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTC();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTCH();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTCM();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTB();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTT();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTL();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTA();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTDic();
//                    contadoresLineaSemantica1[i] += analizadorSemantica1.contadores[i].getTV();
//                    sheet.addCell(new jxl.write.Label(15, i+1, contadoresLineaSemantica1[i]+"", hFormat));
//                }
                
                int ultimaFila = analizadorSemantica1.contadores.length+1;
                sheet.addCell(new jxl.write.Label(0, ultimaFila, "Totales", hFormat));
                int[] contadoresSemantica1 = new int[15];
                for (int i = 0; i < analizadorSemantica1.contadores.length; i++) {
                    for (int j = 0; j < 15; j++) {
                        switch(j){
                            case 0: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTD(); break;
                            case 1: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTDO(); break;
                            case 2: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTDB(); break;
                            case 3: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTDH(); break;
                            case 4: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTF(); break;
                            case 5: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTC(); break;
                            case 6: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTCH(); break;
                            case 7: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTCM(); break;
                            case 8: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTB(); break;
                            case 9: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTT(); break;
                            case 10: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTL(); break;
                            case 11: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTA(); break;
                            case 12: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTDic(); break;
                            case 13: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getTV(); break;
                            case 14: contadoresSemantica1[j] += analizadorSemantica1.contadores[i].getErrores(); break;
                        }
                    }
                }
                for (int i = 0; i < contadoresSemantica1.length; i++) {
                    sheet.addCell(new jxl.write.Label(i+1, ultimaFila, contadoresSemantica1[i]+"", hFormat));
                }
                
                generarHojasSemantica2(ambito.length, analizadorSemantica2);
                woorkbook.write();
                woorkbook.close();
            } catch (WriteException ex) {
                Logger.getLogger(GenerarExcel.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(GenerarExcel.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }

    }

    public ResultSet ejecutarQuery(String query) {
        ResultSet rs = null;
        try {
            System.out.println("<MySQL> Prueba de conexion a MySQL");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/a16130329?verifyServerCertificate=false&useSSL=true", "root", "root");
            System.out.println("<TablaSimbolosMySQL> Conexion exitosa");

            Statement estado = con.createStatement();
            rs = estado.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("<TablaSimbolosMySQL> Error de MySQL");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("<TablaSimbolosMySQL> Error detectado: " + e.getMessage());
        }
        return rs;
    }

}
