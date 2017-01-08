package nc.ui.bd.ref;
//<nc.ui.bd.ref.DataDefinitRefModel>
public class DataDefinitRefModel extends AbstractRefModel{
    //	设置不显示字段：如 主键
	

	public String[] getHiddenFieldCode(){
		return new String[]{"dip_dataDefinit_h.pk_datadefinit_h"};		
	}

	public DataDefinitRefModel(){
		super();
	}

	//设置select子句，显示字段、表名字段
	public String[] getFieldCode(){
		return new String[]{"dip_datadefinit_h.syscode","dip_dataDefinit_h.sysname","dip_dataDefinit_h.memorytable"};
	}

	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"编码","名称","物理表名"
		};
	}

	//参照窗体标题
	public String getRefTitle(){
		return "数据定义类型";
	}

	//表名：设置form子句
	public String getTableName(){
		return "dip_dataDefinit_h";
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_dataDefinit_h.pk_datadefinit_h" ;
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
//		return super.getWherePart();
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}

	@Override
	public int getDefaultFieldCount() {
		return 3;
	}

	
	
	
}
