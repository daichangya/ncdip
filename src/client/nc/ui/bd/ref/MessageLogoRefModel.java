package nc.ui.bd.ref;
//<nc.ui.bd.ref.MessageLogoRefModel>

/**
 * 消息标志自定义参照
 * @author 刘洋
 * 2011-4-14
 */
public class MessageLogoRefModel extends AbstractRefModel {

	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_messagelogo.pk_messagelogo"};		
	}
	
	public MessageLogoRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段、数值字段
	public String[] getFieldCode(){
		return new String[]{"dip_messagelogo.code","dip_messagelogo.cname","dip_messagelogo.ctype","dip_messagelogo.cdata",};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"消息标志编码","消息标志名称","类型","数值"	
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "消息标志";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_messagelogo";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_messagelogo.pk_messagelogo" ;
	}

	/**
	 * 标准名称查找指定档案
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}

	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
		super.addWherePart(newWherePart);
	}	
	
	
}
