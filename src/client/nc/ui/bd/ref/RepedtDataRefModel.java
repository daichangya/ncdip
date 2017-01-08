package nc.ui.bd.ref;
/*
 * 重复数据控制自定义参照
 */
public class RepedtDataRefModel extends AbstractRefModel {
	public RepedtDataRefModel(){
		super();
	}
	
	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_repeatdata.pk_repeatdata"};		
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_repeatdata.code","dip_repeatdata.name"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"编码","名称"	
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "重复数据控制";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_repeatdata";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_repeatdata.pk_repeatdata" ;
	}

	/**
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
}
