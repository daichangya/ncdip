package nc.vo.dip.excel;

import java.util.HashMap;

import nc.vo.dip.in.PubDataVO;
import nc.vo.dip.in.RowDataVO;


public class ExcelDataVO extends PubDataVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sheetName;
	private String pk_billtype;
	private int startRow;
	private short startCol;
	private int rowCount;
	private short colCount;
	private int firstDataRow;
	private short firstDataCol;
	private String[] fieldName;
	private String pkFieldName;
	private String tableName;
	private HashMap<String, String> titlemap;
//	private FieldConstant field;
	
//	public FieldConstant getField() {
//		if(field == null){
//			field = new FieldConstant(getPk_billtype());
//		}
//		return field;
//	}
	
//	public void setField(FieldConstant fieldCon){
//		field = fieldCon;
//	}

	public short getColCount() {
		return colCount;
	}

	public void setColCount(short colCount) {
		this.colCount = colCount;
	}

	public String[] getFieldName() {
		return fieldName;
	}

	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}

	public String getPk_billtype() {
		return pk_billtype;
	}

	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	public String getPkFieldName() {
		return pkFieldName;
	}

	public void setPkFieldName(String pkFieldName) {
		this.pkFieldName = pkFieldName;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public short getStartCol() {
		return startCol;
	}

	public void setStartCol(short startCol) {
		this.startCol = startCol;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	public Object getCellData(int row, short col){
		RowDataVO rowVo = getRowData(row);
//		String key = getField().getKeyByIndex(col);
		return rowVo.getAttributeValue(col+"");
	}
	
	public void setCellData(int row, short col, Object value){
		if(getDataSize()<row+1||getRowData(row) == null){
			insertRow(row, new RowDataVO());
		}
		RowDataVO rowVo = getRowData(row);
//		String key = getField().getKeyByIndex(col);
//		if(key != null){
//			rowVo.setAttributeValue(key, value);
//		}
		rowVo.setAttributeValue(""+col, value);
	}

	public short getFirstDataCol() {
		return firstDataCol;
	}

	public void setFirstDataCol(short firstDataCol) {
		this.firstDataCol = firstDataCol;
	}

	public int getFirstDataRow() {
		return firstDataRow;
	}

	public void setFirstDataRow(int firstDataRow) {
		this.firstDataRow = firstDataRow;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public HashMap<String, String> getTitlemap() {
		return titlemap;
	}

	public void setTitlemap(HashMap<String, String> titlemap) {
		this.titlemap = titlemap;
	}
	
}
