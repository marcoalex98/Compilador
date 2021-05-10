package Excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LeerExcelSintactico {
    public String [][] matrizGeneral;
    public LeerExcelSintactico(File fileName){
        List cellData=new ArrayList();
        try {
            FileInputStream fis=new FileInputStream(fileName);
            XSSFWorkbook workbook=new XSSFWorkbook(fis);
            XSSFSheet hssfSheet=workbook.getSheetAt(0);
            Iterator rowIterator=hssfSheet.rowIterator();
            while(rowIterator.hasNext()){
                XSSFRow hssfRow=(XSSFRow) rowIterator.next();
                Iterator iterator=hssfRow.cellIterator();
                List cellTemp=new ArrayList();
                while(iterator.hasNext()){
                    XSSFCell hssfCell=(XSSFCell) iterator.next();
                    cellTemp.add(hssfCell);
                }
                cellData.add(cellTemp);
            }
        } catch (Exception e) {
        }
        matrizGeneral=obtener(cellData);
//        for(int i=0;i<60;i++){
//            for (int j = 0; j < 96; j++) {
//                System.out.print(matrizGeneral[i][j]+"   ");
//            }
//            System.out.println();
//        }
    }
    public String[][] obtener(List cellDataList){
        //70 96
        String matriz[][]=new String[70][96];
        for (int i = 0; i < cellDataList.size(); i++) {
            List cellTempList=(List) cellDataList.get(i);
            for (int j = 0; j < cellTempList.size(); j++) {
                XSSFCell hssfCell=(XSSFCell) cellTempList.get(j);
                String stringCellValue=hssfCell.toString();
                matriz[i][j]=stringCellValue;
//                System.out.print(stringCellValue+"  ");
            }
//            System.out.println();
        }
        return matriz; 
    }
    
    public static void main(String[] args) {
        File f=new File("matriz-sintaxis.xlsx");
        if(f.exists()){
            LeerExcelSintactico obj=new LeerExcelSintactico(f);
        }
    }
}
