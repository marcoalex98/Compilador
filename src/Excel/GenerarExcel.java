package Excel;

import SQL.TablaSimbolosMySQL;
import Contadores.ContadorAmbito;
import Estructuras.Token;
import Estructuras.Error;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import Contadores.ContadorAmbito;
import SQL.ControladorSQL;

public class GenerarExcel {

    Connection con;

    public void generarExcel(Token[] entradaTokens, Error[] entradaErrores, int[] contadores,
            int[][] contadoresLinea, int[] contadoresSin, ContadorAmbito[] ambito) {
        String ruta = "MarcoAlejandroMarcialCoronado-Ambito.xls";
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
                    sheet.addCell(new jxl.write.Label(i, 0, encabezado[i], hFormat));
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
                    sheet.addCell(new jxl.write.Label(i, 0, encabezado2[i], hFormat));
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
                    sheet.addCell(new jxl.write.Label(i, 1, contadoresSin[i] + "", hFormat));
                }
                //Pagina 6 -----------------------------------Ambito------------------------------
                sheet = woorkbook.createSheet("Ambito", 5);
                sheet.addCell(new jxl.write.Label(0, 0, "Ambito", hFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "Decimal", hFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "Binario", hFormat));
                sheet.addCell(new jxl.write.Label(3, 0, "Octal", hFormat));
                sheet.addCell(new jxl.write.Label(4, 0, "Hexadecimal", hFormat));
                sheet.addCell(new jxl.write.Label(5, 0, "Flotante", hFormat));
                sheet.addCell(new jxl.write.Label(6, 0, "Cadena", hFormat));
                sheet.addCell(new jxl.write.Label(7, 0, "Caracter", hFormat));
                sheet.addCell(new jxl.write.Label(8, 0, "Compleja", hFormat));
                sheet.addCell(new jxl.write.Label(9, 0, "Booleana", hFormat));
                sheet.addCell(new jxl.write.Label(10, 0, "None", hFormat));
                sheet.addCell(new jxl.write.Label(11, 0, "Arreglo", hFormat));
                sheet.addCell(new jxl.write.Label(12, 0, "Tupla", hFormat));
                sheet.addCell(new jxl.write.Label(13, 0, "Lista", hFormat));
                sheet.addCell(new jxl.write.Label(14, 0, "Rango", hFormat));
                sheet.addCell(new jxl.write.Label(15, 0, "Diccionario", hFormat));
                sheet.addCell(new jxl.write.Label(16, 0, "Datos de estructuras", hFormat));
                sheet.addCell(new jxl.write.Label(17, 0, "Total/Ambito", hFormat));

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
                sheet.addCell(new jxl.write.Label(0, 0, "ID", hFormat));
                sheet.addCell(new jxl.write.Label(1, 0, "Tipo", hFormat));
                sheet.addCell(new jxl.write.Label(2, 0, "Clase", hFormat));
                sheet.addCell(new jxl.write.Label(3, 0, "Ambito", hFormat));
                sheet.addCell(new jxl.write.Label(4, 0, "Tamano Arreglo", hFormat));
                sheet.addCell(new jxl.write.Label(5, 0, "Ambito Creado", hFormat));
                sheet.addCell(new jxl.write.Label(6, 0, "NoPos", hFormat));
                sheet.addCell(new jxl.write.Label(7, 0, "Lista Pertenece", hFormat));
                sheet.addCell(new jxl.write.Label(8, 0, "Rango", hFormat));
                sheet.addCell(new jxl.write.Label(9, 0, "Avance", hFormat));
                sheet.addCell(new jxl.write.Label(10, 0, "Llave", hFormat));
                sheet.addCell(new jxl.write.Label(11, 0, "Valor", hFormat));

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
