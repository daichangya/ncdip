package nc.ui.bd.ref;
//<nc.ui.bd.ref.ReturnBMsgRefModel>
public class ReturnBMsgRefModel  extends AbstractRefModel {

	//	设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
//		return new String[]{"dip_dataorigin.pk_dataorigin"};
		return new String[]{"pk_returnmess_b"};
	}
	public ReturnBMsgRefModel(){
		super();
	}
	//设置select子句，显示编码、显示名称、类型字段
	public String[] getFieldCode(){
//		return new String[]{"dip_dataorigin.code","dip_dataorigin.name"};
		return new String[]{"code","def_str_3","content","def_str_1","def_str_2"};
	}
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"编码","名称","标志头"	,"标志体","标志尾"	
		};
		}
		//参照窗体标题
		public String getRefTitle(){
			return "回执错误码表信息";
		}

		//表名：设置form子句
		public String getTableName(){
			return "dip_returnmess_b";
		}

	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_returnmess_b" ;
	}
	/**
	 * 标准名称查找指定档案：数据来源类型
	 * 设定where子句
	 * 这里若是重写了这该方法，myeven。。中就不能为其增加条件了
	 */
	public  String getWherePart(){
		String where=" nvl(dr,0)=0";
		return where;
	}

		
	
	

}
