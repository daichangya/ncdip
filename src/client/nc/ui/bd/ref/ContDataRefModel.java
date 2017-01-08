package nc.ui.bd.ref;
//<nc.ui.bd.ref.ContDataRefModel>
public class ContDataRefModel  extends AbstractRefModel {
	String where="nvl(dip_datadefinit_b.dr,0)=0";
@Override
	public int getDefaultFieldCount() {
		return 3;
	}
	//	设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
//		return new String[]{"dip_dataorigin.pk_dataorigin"};
		return new String[]{"dip_datadefinit_b.pk_datadefinit_b"};
	}
	public ContDataRefModel(){
		super();
	}
	//设置select子句，显示编码、显示名称、类型字段
	public String[] getFieldCode(){
//		return new String[]{"dip_dataorigin.code","dip_dataorigin.name"};
		return new String[]{"dip_datadefinit_b.ename","dip_datadefinit_b.cname","dip_datadefinit_b.type"};
	}
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"英文名称","中文名称"	,"类型"	
		};
		}
		//参照窗体标题
		public String getRefTitle(){
			return "对照系统字段";
		}

		//表名：设置form子句
		public String getTableName(){
			return "dip_datadefinit_b";
		}

	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_datadefinit_b.pk_datadefinit_b" ;
	}
	/**
	 * 标准名称查找指定档案：数据来源类型
	 * 设定where子句
	 * 这里若是重写了这该方法，myeven。。中就不能为其增加条件了
	 */
	public  String getWherePart(){
		return where;
	}
	public void setWhere(String str){
		where=str;
	}
		
	
	

}
