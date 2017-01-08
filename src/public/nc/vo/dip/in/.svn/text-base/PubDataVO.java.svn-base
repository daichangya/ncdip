package nc.vo.dip.in;

import java.util.Vector;

import nc.vo.pub.SuperVO;


public class PubDataVO extends SuperVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 使用列序号(COLNO)对照列信息(顺序对照)
	 */
	public static final int FIELD_MATCH_COLNO = 0;
	/**
	 * 使用字段关键字(KEY)对照
	 */
	public static final int FIELD_MATCH_KEY = 1;
	/**
	 * 使用字段名称(NAME)对照
	 */
	public static final int FIELD_MATCH_NAME = 2;
	
	private Vector<RowDataVO> data = new Vector<RowDataVO>();

	public void addLastRow(RowDataVO rowVo){
		data.addElement(rowVo);
	}
	
	public void insertRow(int row, RowDataVO rowVo){
		if(row > data.size()){
			row = data.size();
		}
		data.add(row, rowVo);
	}
	
	public RowDataVO getFirstRow(){
		return data.get(0);
	}
	
	public RowDataVO getRowData(int row){
		if(getDataSize() <= 0){
			return null;
		}
		return data.get(row);
	}
	
	public int getDataSize(){
		return data.size();
	}
	
	public RowDataVO[] getData(){
		return data.toArray(new RowDataVO[0]);
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
