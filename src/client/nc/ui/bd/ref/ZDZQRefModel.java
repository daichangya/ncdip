package nc.ui.bd.ref;


/*
 * 数据接收定义:主动抓取
 * 数据来源连接 字段参照：根据数据来源类型(主动抓取、消息总线、格式文件、中间表)的不同，显示出不同的类型的参照
 * <nc.ui.bd.ref.ZDZQRefModel>
 */
public class ZDZQRefModel extends AbstractRefModel{
	
	
	public ZDZQRefModel(){
		super();
		
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_dataurl.pk_dataurl"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "数据来源连接";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String sql="dip_dataurl";
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
		return new String[]{"dip_dataurl.code","dip_dataurl.name","dip_dataurl.descriptions"};
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
		return "dip_dataurl.pk_dataurl";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
