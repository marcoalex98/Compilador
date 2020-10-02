
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class GenerarExcelLexico {

    public void generarExcel(Token[] entradaTokens, Error[] entradaErrores, String ruta, int[] contadores,
            int[][] contadoresLinea, int[] contadoresSin, String[][] ambito) {
        try {//Hoja 1 - Lista de Tokens
            WorkbookSettings conf = new WorkbookSettings();
            conf.setEncoding("ISO-8859-1");
            WritableWorkbook woorkbook = Workbook.createWorkbook(new File(ruta), conf);
            WritableSheet sheet = woorkbook.createSheet("Lista de Tokens", 0);
            WritableFont h = new WritableFont(WritableFont.COURIER, 16, WritableFont.NO_BOLD);
            WritableCellFormat hFormat = new WritableCellFormat(h);
            try {
                sheet.addCell(new jxl.write.Label(0, 0, "Linea", hFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "Token", hFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "Lexema", hFormat));
                
                for (int i = 0; i < entradaTokens.length; i++) {//fila
                    sheet.addCell(new jxl.write.Label(0, i + 1, entradaTokens[i].getLinea() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(1, i + 1, entradaTokens[i].getEstado() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(2, 1 + i, entradaTokens[i].getLexema() + "", hFormat));
                    
                }
                sheet = woorkbook.createSheet("Lista de Errores", 1);
                sheet.addCell(new jxl.write.Label(0, 0, "Linea", hFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "Error", hFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "Descripción", hFormat));
                sheet.addCell(new jxl.write.Label(3, 0, "Lexema", hFormat));
                sheet.addCell(new jxl.write.Label(4, 0, "Tipo", hFormat));
                for (int i = 0; i < entradaErrores.length; i++) {//fila
                    sheet.addCell(new jxl.write.Label(0, i + 1, entradaErrores[i].getLinea() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(1, i + 1, entradaErrores[i].getEstado() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(2, 1 + i, entradaErrores[i].getDescripcion()+ "", hFormat));
                    sheet.addCell(new jxl.write.Label(3, 1 + i, entradaErrores[i].getLexema() + "", hFormat));
                    sheet.addCell(new jxl.write.Label(4, 1 + i, entradaErrores[i].getTipo()+ "", hFormat));
                    
                }
                //---Pagina 3--- 
                sheet = woorkbook.createSheet("Contadores", 2);
                String[] encabezado={"Errores","Identificadores","Comentarios","Palabras Reservadas",
                "CE-Dec","CE-Bin","CE-Hex","CE-Oct","CTexto","CFloat","CNComp","Ccar",
                "Aritmeticos","Monogamo","Logico","Bit","Identidad","Relacionales",
                "Puntuacion","Agrupacion","Asignacion"};
                for (int i = 0; i < 21; i++) {
                    sheet.addCell(new jxl.write.Label(i, 0,encabezado[i], hFormat));
                }
                for (int i = 0; i < contadores.length; i++) {
                    sheet.addCell(new jxl.write.Label(i, 1,contadores[i]+"", hFormat));
                }
                //---Pagina 4---
                sheet = woorkbook.createSheet("Tabla de Contadores", 3);
                String[] encabezado2={"Linea","Errores","Identificadores","Comentarios","Palabras Reservadas",
                "CE-Dec","CE-Bin","CE-Hex","CE-Oct","CTexto","CFloat","CNComp","Ccar",
                "Aritmeticos","Monogamo","Logico","Bit","Identidad","Relacionales",
                "Puntuacion","Agrupacion","Asignacion"};
                for (int i = 0; i < 22; i++) {
                    sheet.addCell(new jxl.write.Label(i, 0,encabezado2[i], hFormat));
                }
                System.out.println("Largo de arreglo de contadoresLinea: "+contadoresLinea.length);
                for (int i = 0; i < contadoresLinea.length; i++) {
                    sheet.addCell(new jxl.write.Label(0, i+1,(i+1)+"", hFormat));
                }
                System.out.println("--------------FOR DE EXCEL LEXICO----------------");
                for (int i = 0; i < contadoresLinea.length; i++) {
                    for (int j = 0; j < 21; j++) {
                        System.out.print(contadoresLinea[i][j]);
                        sheet.addCell(new jxl.write.Label(j+1,i+1,contadoresLinea[i][j]+"", hFormat));
                    }
                    System.out.println("");
                }
                //---Pagina 5---
                sheet = woorkbook.createSheet("Sintaxis", 4);
                sheet.addCell(new jxl.write.Label(0, 0, "Program", hFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "CONSTANTE", hFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "CONST ENTERO", hFormat));
                sheet.addCell(new jxl.write.Label(3, 0, "LIST-TUP-RANGOS", hFormat));
                sheet.addCell(new jxl.write.Label(4, 0, "Termino Pascal", hFormat));
                sheet.addCell(new jxl.write.Label(5, 0, "ELEVACION", hFormat));
                sheet.addCell(new jxl.write.Label(6, 0, "SIMPLE EXP-PAS", hFormat));
                sheet.addCell(new jxl.write.Label(7, 0, "FACTOR", hFormat));
                sheet.addCell(new jxl.write.Label(8, 0, "NOT", hFormat));
                sheet.addCell(new jxl.write.Label(9, 0, "OR", hFormat));
                sheet.addCell(new jxl.write.Label(10, 0, "OPBIT", hFormat));
                sheet.addCell(new jxl.write.Label(11, 0, "AND", hFormat));
                sheet.addCell(new jxl.write.Label(12, 0, "ANDLOG", hFormat));
                sheet.addCell(new jxl.write.Label(13, 0, "ORLOG", hFormat));
                sheet.addCell(new jxl.write.Label(14, 0, "XORLOG", hFormat));
                sheet.addCell(new jxl.write.Label(15, 0, "EST", hFormat));
                sheet.addCell(new jxl.write.Label(16, 0, "ASIGN", hFormat));
                sheet.addCell(new jxl.write.Label(17, 0, "FunList", hFormat));
                sheet.addCell(new jxl.write.Label(18, 0, "ARR", hFormat));
                sheet.addCell(new jxl.write.Label(19, 0, "FUNCIONES", hFormat));
                sheet.addCell(new jxl.write.Label(20, 0, "EXP-PAS", hFormat));
                for (int i = 0; i < contadoresSin.length; i++) {//fila
                    sheet.addCell(new jxl.write.Label(i, 1, contadoresSin[i]+ "", hFormat));
                }
                //Pagina 6
                sheet = woorkbook.createSheet("Ambito", 5);
                for (int i = 0; i < ambito.length; i++) {
                    for (int j = 0; j < 19; j++) {
                        System.out.print(ambito[i][j]);
                        sheet.addCell(new jxl.write.Label(j+1,i+1,ambito[i][j]+"", hFormat));
                    }
                    System.out.println("");
                }
//                sheet.addCell(new jxl.write.Label(0, 0, "Ambito", hFormat));
//                sheet.addCell(new jxl.write.Label(1, 0, "Decimal", hFormat));
//                sheet.addCell(new jxl.write.Label(2, 0, "Binario", hFormat));
//                sheet.addCell(new jxl.write.Label(3, 0, "Octal", hFormat));
//                sheet.addCell(new jxl.write.Label(4, 0, "Hexadecimal", hFormat));
//                sheet.addCell(new jxl.write.Label(5, 0, "Flotante", hFormat));
//                sheet.addCell(new jxl.write.Label(6, 0, "Cadena", hFormat));
//                sheet.addCell(new jxl.write.Label(7, 0, "Caracter", hFormat));
//                sheet.addCell(new jxl.write.Label(8, 0, "Compleja", hFormat));
//                sheet.addCell(new jxl.write.Label(9, 0, "Booleana", hFormat));
//                sheet.addCell(new jxl.write.Label(10, 0, "None", hFormat));
//                sheet.addCell(new jxl.write.Label(11, 0, "Arreglo", hFormat));
//                sheet.addCell(new jxl.write.Label(12, 0, "Tupla", hFormat));
//                sheet.addCell(new jxl.write.Label(13, 0, "Lista", hFormat));
//                sheet.addCell(new jxl.write.Label(14, 0, "Registro", hFormat));
//                sheet.addCell(new jxl.write.Label(15, 0, "Rango", hFormat));
//                sheet.addCell(new jxl.write.Label(16, 0, "Conjuntos", hFormat));
//                sheet.addCell(new jxl.write.Label(17, 0, "Diccionario", hFormat));
//                sheet.addCell(new jxl.write.Label(18, 0, "Total/Ambito", hFormat));
                woorkbook.write();
                woorkbook.close();
            } catch (WriteException ex) {
                Logger.getLogger(GenerarExcelLexico.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(GenerarExcelLexico.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }

    }
}
