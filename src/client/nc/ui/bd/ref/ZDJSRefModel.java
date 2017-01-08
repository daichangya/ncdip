package nc.ui.bd.ref;
/*
 * 数据接收定义:消息总线
 * 数据来源连接 字段参照：根据数据来源类型(主动抓取、消息总线、格式文件、中间表)的不同，显示出不同的类型的参照
 * <nc.ui.bd.ref.ZDJSRefModel>
 */
public class ZDJSRefModel extends AbstractRefModel{
	
	
	public ZDJSRefModel(){
		super();
		
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_msr.pk_dip_msr"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "数据来源连接";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String sql="dip_msr left join dip_messageplugregister on dip_msr.messageplug=dip_messageplugregister.pk_messageplugregister";
		return sql;
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return " nvl(dip_msr.dr,0)=0 ";
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_msr.code","dip_msr.name","dip_msr.address"/*,"dip_msr.mestype"*/,"dip_messageplugregister.name pname"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"编码","名称","属性",/*"消息流类型",*/"消息类型"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_msr.pk_dip_msr";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}

	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 5;
	}
	
}
