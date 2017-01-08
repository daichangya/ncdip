package nc.ui.bd.ref;
/*
 * 数据接收定义:中间表
 * 数据来源连接 字段参照：根据数据来源类型(主动抓取、消息总线、格式文件、中间表)的不同，显示出不同的类型的参照
 * <nc.ui.bd.ref.ZJBRefModel>
 */
public class ZJBRefModel extends AbstractRefModel{
	
	
	public ZJBRefModel(){
		super();
		
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_dbcontype.pk_dbcontype"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "数据来源连接";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String sql="dip_dbcontype";
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
		return new String[]{"dip_dbcontype.code","dip_dbcontype.name","dip_dbcontype.type","dip_dbcontype.constr","dip_dbcontype.usercode"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"编码","名称","类型","连接字符串","用户名"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_dbcontype.pk_dbcontype";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return getFieldCode().length;
	}

}
