package nc.ui.bd.ref;
/**
 * 数据加工引用表参照
 * <nc.ui.bd.ref.DataProceYYBRefModel>
 * @date 2011-6-27
 */
public class DataProceYYBRefModel extends AbstractRefModel{

	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 6;
	}

	//	设置不显示字段：如 主键
	public String[] getHiddenFieldCode(){
		return new String[]{"v_dip_jgyyzd.pk_datadefinit_b"};		
	}

	public DataProceYYBRefModel(){
		super();
	}

	//设置select子句，显示字段、表名字段
	public String[] getFieldCode(){
		return new String[]{"v_dip_jgyyzd.memorytable","v_dip_jgyyzd.sysname","cname","ename","v_dip_jgyyzd.syscod","v_dip_jgyyzd.sysna"};
	}

	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"引用表","引用表中文名称","引用字段中文名称","引用字段","引用系统编码","引用系统名称"
		};
	}

	//参照窗体标题
	public String getRefTitle(){
		return "数据定义视图表";
	}

	//表名：设置form子句
	public String getTableName(){
		return "v_dip_jgyyzd";
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		return "v_dip_jgyyzd.pk_datadefinit_b" ;
	}

	@Override
	public String getWherePart() {
		return super.getWherePart();
	}
	@Override
	public void addWherePart(String newWherePart) {
		super.addWherePart(newWherePart);
	}

	@Override
	public void setOrderPart(String strOrderPart) {
		// TODO Auto-generated method stub
		super.setOrderPart("v_dip_jgyyzd.syscod");
	}

}
