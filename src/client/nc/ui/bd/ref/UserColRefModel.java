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
		return "数据表字段";
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
		return new String[]{"表名","字段名"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
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
