package nc.ui.bd.ref;
public class DipADContRoleRefModel extends AbstractRefModel{
	
	
	public DipADContRoleRefModel(){
		super();
		
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"pk_role"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "����Ȩ��ɫ";
	}

	@Override
	public String getTableName() {
		return "sm_role";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"role_code","role_name"};
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
		return "pk_role";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
