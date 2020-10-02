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

public class GenerarExcelSintaxis {

    public void generarExcel(String ruta, int[] contadores) {
        try {//Hoja 1 - Lista de Tokens
            WorkbookSettings conf = new WorkbookSettings();
            conf.setEncoding("ISO-8859-1");
            WritableWorkbook woorkbook = Workbook.createWorkbook(new File(ruta), conf);
            WritableSheet sheet = woorkbook.createSheet("Lista de Tokens", 0);
            WritableFont h = new WritableFont(WritableFont.COURIER, 16, WritableFont.NO_BOLD);
            WritableCellFormat hFormat = new WritableCellFormat(h);
            try {
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
                for (int i = 0; i < contadores.length; i++) {//fila
                    sheet.addCell(new jxl.write.Label(i, 1, contadores[i]+ "", hFormat));
                }
                
                woorkbook.write();
                woorkbook.close();
            } catch (WriteException ex) {
                Logger.getLogger(GenerarExcelLexico.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(GenerarExcelLexico.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
