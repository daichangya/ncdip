package nc.ui.bd.ref;
public class DipDataRefModel extends AbstractRefModel{

	public DipDataRefModel(){
		super();
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "公共数据";
	}

	@Override
	public String getTableName() {
		return super.getTableName();
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"C_CODE","C_NAME"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"编码","名称"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return super.getPkFieldCode();
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
