package nc.ui.bd.ref;

public class MessagePlugRegisterRefModel extends AbstractRefModel{
	public MessagePlugRegisterRefModel(){
		super();
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_messageplugregister.pk_messageplugregister"};
	}
	
	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "��Ϣ���ͱ�־";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String sql="dip_messageplugregister";
		return sql;
	}
	
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_messageplugregister.code","dip_messageplugregister.name"};
	}
	
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"����","����"};
	}
	
//	�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_messageplugregister.pk_messageplugregister";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}

	
	
	
}
