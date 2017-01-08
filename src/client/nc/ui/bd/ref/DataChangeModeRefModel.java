package nc.ui.bd.ref;
//<nc.ui.bd.ref.DataChangeModeRefModel>
public class DataChangeModeRefModel  extends AbstractRefModel {
//	设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"pk_datachange_b","fpk"};
	}
	public DataChangeModeRefModel(){
		super();
	}
	//设置select子句，显示编码、显示名称、类型字段
	public String[] getFieldCode(){
		return new String[]{"syscode","sysname","memorytable","unitcode","unitname","name"};
	}
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"业务数据编码","业务数据名称","业务表名","公司编码","公司名称","数据转换"	
		};
		}
		//参照窗体标题
		public String getRefTitle(){
			return "凭证模板";
		}

		//表名：设置form子句
		public String getTableName(){
			return "v_dip_creref";
		}

	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_datachange_b" ;
	}
	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 6;
	}

		
	
	

}
