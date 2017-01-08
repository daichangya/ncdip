package nc.ui.bd.ref;

/**
 * 数据类型自定义参照
 * @author chengli
 * @date 2011-3-28
 */
public class DataStyleRefModel extends AbstractRefModel {
	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_datastyle.pk_datastyle"};		
	}

	public DataStyleRefModel(){
		super();
	}

	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_datastyle.code","dip_datastyle.name"};
	}

	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"类型编码","类型名称"	
		};
	}

	//参照窗体标题
	public String getRefTitle(){
		return "数据类型";
	}

	//表名：设置form子句
	public String getTableName(){
		return "dip_datastyle";
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_datastyle.pk_datastyle" ;
	}

	/**
	 * 标准名称查找指定档案：数据类型
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
//		String where="nvl(dr,0)=0"+super.getWherePart()==null ?"":super.getWherePart();
//		return where ;
		String where =" nvl(dr,0)=0 and pk_datastyle<>'0001BB100000000JKANO'";
		return where;
		//return super.getWherePart();
	}	
}
