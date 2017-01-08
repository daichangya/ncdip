package nc.ui.bd.ref;
/**
 * 获取标志自定义参照
 * @author db2admin
 *
 */
//nc.ui.bd.ref.ObtainSignRefModel
public class ObtainSignRefModel extends AbstractRefModel {

	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_obtainsign.pk_obtainsign"};		
	}
	
	public ObtainSignRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_obtainsign.code","dip_obtainsign.name"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"获取标志编码","获取标志名称"	
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "获取标志";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_obtainsign";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_obtainsign.pk_obtainsign" ;
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
