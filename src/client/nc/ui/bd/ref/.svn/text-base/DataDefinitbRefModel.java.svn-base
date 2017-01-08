package nc.ui.bd.ref;
/**
 * 数据定义“引用表字段” 自定义参照
 * @author zlc
 * @date 2011-4-21
 */
public class DataDefinitbRefModel extends AbstractRefModel{
	
	public String wherePart = "";
	//	设置不显示字段：如 主键
	public String[] getHiddenFieldCode(){
		return new String[]{"dip_datadefinit_b.pk_datadefinit_b"};		
	}

	public DataDefinitbRefModel(){
		super();
	}

	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_datadefinit_b.ename","dip_datadefinit_b.cname"};
	}

	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"英文名称","中文名称"	
		};
	}

	//参照窗体标题
	public String getRefTitle(){
		return "引用表字段";
	}

	//表名：设置form子句
	public String getTableName(){
		return "dip_datadefinit_b inner join dip_datadefinit_h on dip_datadefinit_h.pk_datadefinit_h =dip_datadefinit_b.pk_datadefinit_h";
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_datadefinit_b.pk_datadefinit_b" ;
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}

	
	
}
