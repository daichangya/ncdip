package nc.ui.bd.ref;
public class UserColRefModel extends AbstractRefModel{
	
	
	public UserColRefModel(){
		super();
		
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"table_name||'.'||column_name"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "���ݱ��ֶ�";
	}

	@Override
	public String getTableName() {
		return "user_col_comments";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"table_name","column_name"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"����","�ֶ���"};
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "table_name||'.'||column_name";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
