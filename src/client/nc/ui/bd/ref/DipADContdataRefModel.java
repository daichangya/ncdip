package nc.ui.bd.ref;
public class DipADContdataRefModel extends AbstractRefModel{
	
	
	public DipADContdataRefModel(){
		super();
		
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"middletab"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "����Ȩ�����";
	}

	@Override
	public String getTableName() {
		return "dip_adcontdata";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"code","name"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"����","����"};
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "middletab";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
