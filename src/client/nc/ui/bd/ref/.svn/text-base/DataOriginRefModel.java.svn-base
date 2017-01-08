package nc.ui.bd.ref;

/**
 * 数据来源类型自定义参照
 * @author db2admin
 *
 */
public class DataOriginRefModel extends AbstractRefModel {

	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_dataorigin.pk_dataorigin"};		
	}
	
	public DataOriginRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_dataorigin.code","dip_dataorigin.name"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"来源类型编码","来源类型名称"	
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "数据来源类型";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_dataorigin";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_dataorigin.pk_dataorigin" ;
	}

	/**
	 * 标准名称查找指定档案：数据来源类型
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
}
