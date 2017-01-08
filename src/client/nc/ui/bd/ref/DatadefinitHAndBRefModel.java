package nc.ui.bd.ref;
/*
 * 数据定义表体引用表字段参照
 * 2011-6-4
 * zlc*/
public class DatadefinitHAndBRefModel extends AbstractRefModel{

 //	设置不显示字段：如 主键
	

	public String[] getHiddenFieldCode(){
		return new String[]{"v_dip_yyzd.pk_datadefinit_b"};		
	}

	public DatadefinitHAndBRefModel(){
		super();
	}

	//设置select子句，显示字段、表名字段
	public String[] getFieldCode(){
		return new String[]{"v_dip_yyzd.memorytable","v_dip_yyzd.sysname","cname","ename"};
	}

	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"引用表","引用表中文名称","引用字段中文名称","引用字段"
		};
	}

	//参照窗体标题
	public String getRefTitle(){
		return "数据定义视图表";
	}

	//表名：设置form子句
	public String getTableName(){
		return "v_dip_yyzd";
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "v_dip_yyzd.pk_datadefinit_b" ;
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		return super.getWherePart();
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}

}
