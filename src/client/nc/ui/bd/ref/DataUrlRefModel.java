package nc.ui.bd.ref;

/**
 * 数据同步URL注册自定义参照
 * @author 刘洋
 * 2011-4-14
 */
public class DataUrlRefModel extends AbstractRefModel {

	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_dataurl.pk_dataurl"};		
	}
	
	public DataUrlRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_dataurl.code","dip_dataurl.name","dip_dataurl.descriptions",};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"数据同步URL编码","数据同步url名称","数据同步URL属性"
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "数据同步URL注册";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_dataurl";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_dataurl.pk_dataurl" ;
	}

	/**
	 * 标准名称查找指定档案：数据同步URL注册
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
}
