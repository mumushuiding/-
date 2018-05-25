package com.crm.springboot.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;

import org.apache.poi.ddf.EscherColorRef.SysIndexProcedure;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.util.IOUtils;

import com.alibaba.druid.sql.ast.SQLStructDataType.Field;

public class ExcelUtils {
	
	public static List<List<String>> readExcel(File file) throws FileNotFoundException, IOException{
		if(!file.exists()) return null;
		String extension=FileUtils.getFileExtensionName(file);
        
        if("xls".equals(extension)){
        	return readXls(file);
        }
		if("xlsx".equals(extension)){
			return readXlsx(file);
		}
		return null;
		
	}
	public static List<List<String>> readXls(File file) throws FileNotFoundException, IOException{
		
		List<List<String>> result=new ArrayList<List<String>>();
		HSSFWorkbook hwb=new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet=hwb.getSheetAt(0);
		HSSFRow row=null;
		HSSFCell cell=null;
		for(int i=sheet.getFirstRowNum();i<sheet.getPhysicalNumberOfRows();i++){
			row=sheet.getRow(i);
			if(row==null)continue;
			List<String> rowResult=new ArrayList<>();
			for(int j=row.getFirstCellNum();j<=row.getLastCellNum();j++){
				cell=row.getCell(j);
//				if(cell==null)continue;
				String cellValue=getCellValue(cell);
				rowResult.add(cellValue);
			}
			result.add(rowResult);
		}
		return result;
		
	}
	public static List<List<String>> readXlsx(File file){
		List<List<String>> result=new ArrayList<List<String>>();
		
		return null;
		
	}
	public static String getCellValue(Cell cell){
		String result="";
		if(cell==null) return result;
		CellType cellType=cell.getCellTypeEnum();
		switch (cellType) {
		case STRING:
			result=cell.getStringCellValue();
			break;
		case NUMERIC:
			NumberFormat nf = NumberFormat.getInstance();
			result=String.valueOf(nf.format(cell.getNumericCellValue()));
			result=result.replaceAll(",", "");
		   
			break;
		case BLANK:
			result="";
		default:
			break;
		}
		return result;
	}
}
