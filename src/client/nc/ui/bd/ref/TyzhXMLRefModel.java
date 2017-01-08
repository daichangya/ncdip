package nc.ui.bd.ref;

public class TyzhXMLRefModel extends AbstractRefGridTreeModel{
	public TyzhXMLRefModel(){
		setRefNodeName( );
	}
	public void setRefNodeName(){
		setRefTitle("通用XML转换适配器");
			setRootName("格式定义");
			setClassFieldCode(new String[]{"code","name","pk_tyxml_h","fpk"});
//			setClassTableName("(select dip_sysregister_h.pk_sysregister_h pk_tyxml_h,dip_sysregister_h.code code,dip_sysregister_h.extname name,'' fpk,'N' isfolder from dip_sysregister_h where nvl(dip_sysregister_h.dr,0)=0 "+
//" union all select pk_tyxml_h,code,name,fpk,isfolder  from dip_tyxml_h where nvl(dip_tyxml_h.dr,0)=0 and isfolder='Y' and pk_xt in (select pk_sysregister_h from dip_sysregister_h where nvl(dr,0)=0)  ) hh");
			setClassTableName("v_dip_tyxmlsys");
//			setClassWherePart(" ");
			setFatherField("fpk");
			setChildField("pk_tyxml_h");
			setClassJoinField("pk_tyxml_h");
			setClassDefaultFieldCount(2);
			setDocJoinField("fpk");
			setFieldCode(new String[]{"dip_tyxml_h.code","dip_tyxml_h.name","dip_datadefinit_h.syscode","dip_datadefinit_h.sysname","dip_datadefinit_h.memorytable"});
			setFieldName(new String[]{"通用转换编码","通用转换名称","来源表编码","来源表名称","来源表物理表名"});
			setTableName("dip_tyxml_h left join dip_datadefinit_h on dip_tyxml_h.sourcetab=dip_datadefinit_h.pk_datadefinit_h");
			setHiddenFieldCode(new String[]{"pk_tyxml_h"/*,"dip_datadefinit_h.pk_datadefinit_h"*/});
			setPkFieldCode("pk_tyxml_h");
			setDefaultFieldCount(5);
			setWherePart(" dip_tyxml_h.isfolder='N' "  );
			
		}
	
	/* extends AbstractRefModel{
	
	
	
	@Override
	public String[] getHiddenFieldCode() {
		return new String[]{"dip_tyxml_h.pk_tyxml_h","dip_datadefinit_h.pk_datadefinit_h"};
	}

	@Override
	public String getRefTitle() {
		return "通用XML转换适配器";
	}

	@Override
	public String getTableName() {
		String sql="dip_tyxml_h inner join  dip_datadefinit_h  on dip_tyxml_h.sourcetab=dip_datadefinit_h.pk_datadefinit_h  and dip_tyxml_h.pk_xt=dip_datadefinit_h.pk_xt";
		return sql;
	}

	@Override
	public String getWherePart() {
		return super.getWherePart();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{"dip_tyxml_h.code","dip_tyxml_h.name","dip_datadefinit_h.sysname","dip_datadefinit_h.memorytable"};
	}
	@Override
	public String[] getFieldName() {
		return new String[]{"通用转换编码","通用转换名称","来源表名称","来源表物理表名"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		return "dip_tyxml_h.pk_tyxml_h";
	}
	@Override
	public void addWherePart(String newWherePart) {
		super.addWherePart(newWherePart);
	}
	
	@Override
	public int getDefaultFieldCount() {
		return 4;
	}*/

}
