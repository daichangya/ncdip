package nc.ui.bd.ref;

public class DataSendModel extends AbstractRefModel {
//	设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
//		return new String[]{"dip_dataorigin.pk_dataorigin"};
		return new String[]{"DIP_DATADEFINIT_H.PK_DATADEFINIT_H"};
	}
	public DataSendModel(){
		super();
	}
	//设置select子句，显示编码、显示名称
	public String[] getFieldCode(){
//		return new String[]{"dip_dataorigin.code","dip_dataorigin.name"};
		return new String[]{"DIP_DATADEFINIT_H.SYSCODE","DIP_DATADEFINIT_H.SYSNAME"};
	}
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"编码","名称"		
		};
		}
		//参照窗体标题
		public String getRefTitle(){
			return "业务数据";
		}

		//表名：设置form子句
		public String getTableName(){
			return "DIP_DATADEFINIT_H";
		}

	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "DIP_DATADEFINIT_H.PK_DATADEFINIT_H" ;
	}
	/**
	 * 标准名称查找指定档案：数据来源类型
	 * 设定where子句
	 * 这里若是重写了这该方法，myeven。。中就不能为其增加条件了
	 */
//	public  String getWherePart(){
//		String where=" dip_datadefinit_b.pk_datadefinit_h='0001AA10000000018Y2K' and nvl(dr,0)=0";
//		return where;
//	}

		
	
	

}
