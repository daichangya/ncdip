package nc.ui.bd.ref;

/**
 * 消息类型自定义参照
 * @author 刘洋
 * 2011-4-14
 */
public class MessTypeRefModel extends AbstractRefModel {

	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_messtype.pk_messtype"};		
	}
	
	public MessTypeRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_messtype.code","dip_messtype.name"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"消息类型编码","消息类型名称"	
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "消息类型";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_messtype";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_messtype.pk_messtype" ;
	}

	/**
	 * 标准名称查找指定档案：消息类型
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
//		String where ="nvl(dr,0)=0";
		return super.getWherePart();
	}

	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
		super.addWherePart(newWherePart);
	}	
	
	
}
