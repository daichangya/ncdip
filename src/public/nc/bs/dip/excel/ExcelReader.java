package nc.bs.dip.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import nc.vo.dip.excel.ExcelDataVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelReader {
//	private String pk_billtype;
	public ExcelReader(){
	}
	
	public String[] getAllPage(File file) throws FileNotFoundException, IOException{
		HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(file));
		int num = book.getNumberOfSheets();
		String[] pages = new String[num];
		for(int i=0;i<num;i++){
			pages[i] = book.getSheetName(i);
		}
		return pages;
	}

	public ExcelDataVO[] readAll(File file) throws FileNotFoundException, IOException{
		HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(file));
		ArrayList<ExcelDataVO> list = new ArrayList<ExcelDataVO>();
		for(int i=0;i<book.getNumberOfSheets();i++){
			HSSFSheet sheet = book.getSheetAt(i);
			if(sheet == null){
				continue;
			}

			ExcelDataVO dataVo = new ExcelDataVO();
			dataVo.setSheetName(book.getSheetName(i));
//			dataVo.setPk_billtype(pk_billtype);
			dataVo.setStartRow(sheet.getFirstRowNum());
			dataVo.setStartCol(sheet.getLeftCol());
			dataVo.setRowCount(sheet.getPhysicalNumberOfRows() - 1);//去掉标题行
			dataVo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());

			list.add(dataVo);
		}

		return list.toArray(new ExcelDataVO[0]);
	}

	public ExcelDataVO read(File file, int index) throws FileNotFoundException, IOException{
		FileInputStream fin=new FileInputStream(file);
		ExcelDataVO dataVo = new ExcelDataVO();
		try{
			HSSFWorkbook book = new HSSFWorkbook(fin);
			HSSFSheet sheet = book.getSheetAt(index);
			if(sheet == null){
				return null;
			}
	
			dataVo.setSheetName(book.getSheetName(index));
			dataVo.setStartRow(sheet.getFirstRowNum());
			dataVo.setStartCol(sheet.getLeftCol());
			dataVo.setRowCount(sheet.getPhysicalNumberOfRows() - 1);//去掉标题行
	//		dataVo.setPk_billtype(pk_billtype);
			dataVo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());
			dataVo.setFirstDataRow(sheet.getFirstRowNum() + 1);
			dataVo.setFirstDataCol(sheet.getLeftCol());
			
	//		FieldConstant field = new FieldConstant(pk_billtype);
			HSSFRow titleRow = sheet.getRow(dataVo.getStartRow());
			HashMap<String, String> titlemap = new HashMap<String, String>();
			for(short i=0;i<titleRow.getPhysicalNumberOfCells();i++){
				titlemap.put(Short.toString(i),(String)getCellValue(titleRow.getCell(i)));
			}
	//		field.setTitleMap(titlemap);
			dataVo.setTitlemap(titlemap);
	
			for(int i=0;i<dataVo.getRowCount();i++){
				HSSFRow row = sheet.getRow(dataVo.getFirstDataRow() + i);
				for(short j=0;j<row.getPhysicalNumberOfCells();j++){
					dataVo.setCellData(i, j, getCellValue(row.getCell(j)));
				}
			}
		}finally{
			fin.close();
		}
		return dataVo;
	}

	private Object getCellValue(HSSFCell cell) {
		Object rst = null;
		switch(cell.getCellType()){   
		case HSSFCell.CELL_TYPE_BOOLEAN:   
			//得到Boolean对象的方法 
			System.out.print(cell.getBooleanCellValue()+" ");
			rst =  new UFBoolean(cell.getBooleanCellValue());			   
			break;   
		case HSSFCell.CELL_TYPE_NUMERIC:  
			String value = Double.toString(cell.getNumericCellValue());
			if(value.contains(".")){
				try {
					String temp = value.replace(".", "-");
					int lastNo = Integer.parseInt(temp.split("-")[1]);

					if(lastNo == 0){
						rst = Integer.parseInt(temp.split("-")[0]);
					}else{
						rst = new UFDouble(cell.getNumericCellValue());
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					rst = new UFDouble(cell.getNumericCellValue());
				}
			}
			break;   
		case HSSFCell.CELL_TYPE_FORMULA:   
			//读取公式   
			rst = cell.getCellFormula();   
			break;   
		case HSSFCell.CELL_TYPE_STRING:   
			//读取String   
			rst = cell.getRichStringCellValue();
			try {
				rst = new UFDate(rst.toString());
			}catch(Exception e){
				rst = cell.getRichStringCellValue().toString();
			}
			System.out.print(cell.getRichStringCellValue().toString()+" ");   
			break;                     
		}
		return rst;
	}

	public ExcelDataVO read(File file) throws FileNotFoundException, IOException{
		return read(file, 0);
	}
}
