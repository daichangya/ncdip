package nc.ui.bd.ref;
/**
 * 是表nc_iufo_module的参照
 * @author Administrator
 *
 */
public class NCiufoModelRefModel extends AbstractRefModel{

    //	设置不显示字段：如 主键
	

	public String[] getHiddenFieldCode(){
		return new String[]{"module_pk"};		
	}

	public NCiufoModelRefModel(){
		super();
	}

	//设置select子句，显示字段、表名字段
	public String[] getFieldCode(){
		return new String[]{"module_name","module_prefix","module_desc"};
	}

	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"模块名称","模块前缀","模块信息"
		};
	}

	//参照窗体标题
	public String getRefTitle(){
		return "iufo接口表";
	}

	//表名：设置form子句
	public String getTableName(){
		return "nc_iufo_module";
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		return "module_pk" ;
	}

	@Override
	public String getWherePart() {
		String where ="nvl(dr,0)=0";
		return where;
	}

	@Override
	public int getDefaultFieldCount() {
		return getFieldCode().length;
	}

	
	
	

}
