package nc.ui.bd.ref;

public class PhysicTableRefModel extends AbstractRefModel{
	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "���ݿ��";
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{""};
	}
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "table_name";
	}
	
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"table_name"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"�������"};
	}
	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "user_all_tables";
	}
	
	
}
