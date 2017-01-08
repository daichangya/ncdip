package nc.ui.bd.ref;
/*
 * 数据接收格式自定义参照
 */
public class RecFormatRefModel extends AbstractRefModel {
	public RecFormatRefModel(){
		super();
	}
	
	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_recformat.pk_recformat"};		
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_recformat.code","dip_recformat.name"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"编码","名称"	,"数值"
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "数据接收格式";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_recformat";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_recformat.pk_recformat" ;
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
