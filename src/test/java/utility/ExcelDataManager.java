/**
 * 
 */
package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.testng.util.Strings;

/**
 * @author Abhishek
 *
 */

public class ExcelDataManager{
    HSSFWorkbook workbook = null;
    public HSSFSheet sheet = null;
    File file1 = null;
    DataFormatter formatter = new DataFormatter();

    public void openExcelFile(String path, int sheetNumber){
    	try{
    		if (System.getProperty("os.name").contains("Mac"))
    			path.replace("\\", "/");
    		file1 = new File(path);
            FileInputStream file = new FileInputStream(new File(path)); 
            workbook = new HSSFWorkbook(file);
            sheet = workbook.getSheetAt(sheetNumber-1);	
    	}catch(Exception e){
    		System.out.println("Not able to open Excel File due to error message" + e.getMessage());
    	}
     }
    
    public void emptyExcel(){
        Row row = null;
        for(int i=sheet.getLastRowNum(); i>=0; i--) {
            row = sheet.getRow(i);
            if(row!=null) {
            sheet.removeRow(row);
            }
        }
        saveChangesInExcel();
    }
    
    public void createRow(int rownum){
    	sheet.createRow(rownum);
    }

    public void addCellData(int rownum, int col, String value){
        Row row = sheet.getRow(rownum);
        row.createCell(col).setCellValue(value);
        saveChangesInExcel();
        } 
    
    public void updateCellData(int rownum, int colNumber, String data){
        Row row = sheet.getRow(rownum);
        row.getCell(colNumber).setCellValue(data);
        saveChangesInExcel();
    }
    
    public int getLastRow(){
    	return sheet.getLastRowNum();
    }
    
    public boolean isSheetEmpty(){
		if (sheet.getRow(0) == null) {
			System.out.println("Sheet is empty");
			return true;
		}
        return false;
    }
    
    public int getRowNumberForCellValue(int searchColumnNumber1, String searchString1, int searchColumnNumber2, String searchString2, int searchColumnNumber3, String searchString3) {
		Row row = null;
		if (sheet.getRow(0) == null) {
			System.out.println("Sheet is empty");
			return -1;
		}

		for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
			row = sheet.getRow(i);
			String rowValue = row.getCell(searchColumnNumber1).getStringCellValue();
			if (row != null && row.getCell(searchColumnNumber1) != null && !rowValue.isEmpty() && rowValue.equals(searchString1)) {
				if (row.getCell(searchColumnNumber2).getStringCellValue().equals(searchString2)) {
					if (row.getCell(searchColumnNumber3).getStringCellValue().equals(searchString3)) {
						return i;
					}
				}
			}
		}
		return -1;
    }
    
    public String getCellValue(int rownum, int colnum){
    	Row row = sheet.getRow(rownum);
    	if(row != null) {
    		String rowValue = formatter.formatCellValue(row.getCell(colnum));
    		return rowValue;
    	}else {
    		return null;
    	}
    }
    
    public void saveChangesInExcel(){
    	try{
    		FileOutputStream out = new FileOutputStream(file1);
            workbook.write(out);
            out.flush();
            out.close();
    	}catch(Exception e){
    		System.out.println("Not able to save changes in Excel File due to error message" + e.getMessage());
    	} 
    }

	public int getTotalNumberOfRows() {

		int rowCount = 0;
		for (Row row : sheet) {
			for (Cell cell : row) {
				String text = formatter.formatCellValue(cell);
				if (Strings.isNotNullAndNotEmpty(text)) {
					rowCount++;
					break;
				}
			}
		}
		return rowCount;
	}
}
