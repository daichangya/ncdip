package nc.ui.bd.ref;
//nc.ui.bd.ref.DataConsultRefModel
public class DataConsultRefModel extends AbstractRefModel {
	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
//		return new String[]{"dip_dataorigin.pk_dataorigin"};
		return new String[]{"dataconsult.pk_dataconsult"};
	}
	public DataConsultRefModel(){
		super();
	}
	//设置select子句，显示编码、类型字段
	public String[] getFieldCode(){
//		return new String[]{"dip_dataorigin.code","dip_dataorigin.name"};
		return new String[]{"dataconsult.code","dataconsult.type"};
	}
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"数据项定义编码","数据项定义类型"	
		};
		}
		//参照窗体标题
		public String getRefTitle(){
			return "数据顶表体";
		}

		//表名：设置form子句
		public String getTableName(){
			return "dataconsult";
		}

	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dataconsult.pk_dataconsult" ;
	}
	/**
	 * 标准名称查找指定档案：数据来源类型
	 * 设定where子句
	 */
	public  String getWherePart(){
		String where="nvl(dr,0)=0";
		return where;
	}

		
	
	

}
