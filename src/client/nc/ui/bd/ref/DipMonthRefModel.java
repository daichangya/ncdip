package nc.ui.bd.ref;
public class DipMonthRefModel extends AbstractRefModel{
	
	
	public DipMonthRefModel(){
		super();
		
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "日历月";
	}

	@Override
	public String getTableName() {
		return "ZMDM_OCALMONTH2";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"OCALMONTH2"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"日历月"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "OCALMONTH2";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
