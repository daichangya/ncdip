package nc.ui.bd.ref;
public class DipDayRefModel extends AbstractRefModel{
	
	
	public DipDayRefModel(){
		super();
		
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "������";
	}

	@Override
	public String getTableName() {
		return "ZMDM_OCALDAY";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"OCALDAY"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"������"};
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "OCALDAY";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
