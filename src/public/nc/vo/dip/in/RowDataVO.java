package nc.vo.dip.in;

import java.util.Hashtable;

import nc.vo.pub.SuperVO;

public class RowDataVO extends SuperVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Hashtable<String, Object> tab = new Hashtable<String, Object>();
	
	private String tableName = null;
	private String pkfield = null;
	private String errorType;
	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public Hashtable<String, Object> getTab() {
		return tab;
	}
	
	public Integer getFieldCount(){
		return getTab().size();
	}

	@Override
	public String[] getAttributeNames() {
		// TODO Auto-generated method stub
		return getTab().keySet().toArray(new String[0]);
	}

	@Override
	public Object getAttributeValue(String arg0) {
		// TODO Auto-generated method stub
		return getTab().get(arg0);
	}

	@Override
	public void setAttributeValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		getTab().put(arg0, arg1);
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return pkfield;
	}
	
	public void setPKField(String newPKFieldName){
		pkfield = newPKFieldName;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return tableName;
	}
	
	public void setTableName(String name){
		tableName = name;
	}
}
