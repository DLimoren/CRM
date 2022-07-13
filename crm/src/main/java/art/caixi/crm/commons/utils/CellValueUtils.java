package art.caixi.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class CellValueUtils {
    public static String getCellValue(HSSFCell cell){
        String retStr = "";
        if(HSSFCell.CELL_TYPE_STRING == cell.getCellType()){
            retStr = cell.getStringCellValue();
        } else if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
            retStr = cell.getNumericCellValue()+"";
        }else if (HSSFCell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
            retStr = cell.getBooleanCellValue()+"";
        }else if (HSSFCell.CELL_TYPE_FORMULA == cell.getCellType()) {
            retStr = cell.getCellFormula()+"";
        }
        return retStr;
    }
}
