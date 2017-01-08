package nc.ui.bd.ref;

public class ProcessstypeRefModel extends AbstractRefModel{
	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_processstyle.pk_processstype"};		
	}
	
	public ProcessstypeRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_processstyle.code","dip_processstyle.name"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"加工类型编码","加工类型名称"	
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "加工类型档案";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_processstyle";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_processstyle.pk_processstype" ;
	}

	/**
	 * 标准名称查找指定档案：获取标志
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
}
