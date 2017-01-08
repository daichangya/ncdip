package nc.ui.bd.ref;

/**
 * NC接收服务器注册自定义参照
 * @author 张进双
 * 2011-5-21
 */
public class RecserverPathRefModel extends AbstractRefModel {

	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_recserver.pk_recserver"};		
	}
	
	public RecserverPathRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		//2011-6-15 增加了“dip_recserver.descs	”	
		return new String[]{"dip_recserver.code","dip_recserver.name","dip_recserver.descs"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		//2011-6-15 增加了“服务器属性”字段
		return new String[]{
				"服务器编码","服务器名称","服务器属性"
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "NC接收服务器注册";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_recserver";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_recserver.pk_recserver" ;
	}

	/**
	 * 标准名称查找指定档案：NC接收服务器注册
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
}
