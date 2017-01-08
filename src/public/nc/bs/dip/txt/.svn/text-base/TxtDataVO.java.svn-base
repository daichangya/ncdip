package nc.bs.dip.txt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import nc.vo.dip.in.PubDataVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.pub.SuperVO;


public class TxtDataVO extends PubDataVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sheetName;
	private String pk_billtype;
	private int startRow;
	private int startCol;
	private int rowCount;
	private int colCount;
	private int firstDataRow;
	private int firstDataCol;
	private String[] fieldName;
	private String pkFieldName;
	private String tableName;
	private HashMap<String, String> titlemap;
	////name和code  对应关系。导出的title中文字和导入测key对应关系。例如导出是第一行第一列是姓名，对应的code是name，nametocodeMap.put(name,code);
	private HashMap<String, String> nametocodeMap;
	private HashMap<String, String> codeToNameMap;
	////列数和code   对应关系。导入的列数和code所对应的关系。line从0开始
	private HashMap<String, String> linetocodeMap;
	private Vector<SuperVO> datavo=new Vector<SuperVO>();//是把excel内容封装成一个vo容器。
	private Vector<RowDataVO> errList=new Vector<RowDataVO>();
	private String errorTitle="";
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

	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
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

	public int getStartCol() {
		return startCol;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	public Object getCellData(int row, int col){
		RowDataVO rowVo = getRowData(row);
//		String key = getField().getKeyByIndex(col);
		return rowVo.getAttributeValue(col+"");
	}
	
	public void setCellData(int row, int col, Object value){
		if(getDataSize()<row+1||getRowData(row) == null){
			insertRow(row, new RowDataVO());
		}
		RowDataVO rowVo = getRowData(row);
		if(value!=null){
			rowVo.setAttributeValue(""+col, value);
		}
	}

	public int getFirstDataCol() {
		return firstDataCol;
	}

	public void setFirstDataCol(int firstDataCol) {
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

	public HashMap<String, String> getLinetocodeMap() {
		return linetocodeMap;
	}

	public void setLinetocodeMap(HashMap<String, String> linetocodeMap) {
		this.linetocodeMap = linetocodeMap;
	}

	public HashMap<String, String> getNametocodeMap() {
		return nametocodeMap;
	}

	public void setNametocodeMap(HashMap<String, String> nametocodeMap) {
		this.nametocodeMap = nametocodeMap;
	}

	public Vector<SuperVO> getDatavo() {
		return datavo;
	}

	public void setDatavo(Vector<SuperVO> datavo) {
		this.datavo = datavo;
	}

	public Vector<RowDataVO> getErrList() {
		return errList;
	}

	public void setErrList(Vector<RowDataVO> errList) {
		this.errList = errList;
	}

	public String getErrorTitle() {
		return errorTitle;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

	public HashMap<String, String> getCodeToNameMap() {
		return codeToNameMap;
	}

	public void setCodeToNameMap(HashMap<String, String> codeToNameMap) {
		this.codeToNameMap = codeToNameMap;
	}
	
	
}
