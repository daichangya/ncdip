package nc.ui.bd.ref;
/*
 * 数据接收定义:格式文件
 * 数据来源连接 字段参照：根据数据来源类型(主动抓取、消息总线、格式文件、中间表)的不同，显示出不同的类型的参照
 * <nc.ui.bd.ref.ZJBRefModel>
 */
public class GSWJRefModel extends AbstractRefModel{
	
	
	public GSWJRefModel(){
		super();
		
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_filepath.pk_filepath"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "数据来源连接";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String sql="dip_filepath";
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
		return new String[]{"dip_filepath.code","dip_filepath.name","dip_filepath.descriptions"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"编码","名称","属性"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_filepath.pk_filepath";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
