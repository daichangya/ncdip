package nc.ui.bd.ref;

public class ProconditionRefModel extends  AbstractRefModel{
	//设置不显示字段
	public String[] getHiddenFieldCode(){
		return new String[]{"dip_procondition.pk_procondition"};
	}
	
	public ProconditionRefModel(){
		super();
	}
//	设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_procondition.code","dip_procondition.name"};
	}
//	显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"编码","名称"	
		};

}
//	参照窗体标题
	public String getRefTitle(){
		return "加工条件";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_procondition";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_procondition.pk_procondition" ;
	}
	/**
	 * 标准名称查找指定档案：加工条件
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
	
}
