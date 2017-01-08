package nc.ui.bd.ref;
/*
 * 系统注册参照
 */
public class SysRegisterRefModel extends AbstractRefModel {

	public SysRegisterRefModel(){
		super();
	}
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_sysregister_b.pk_sysregister_b"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "系统注册分布标识";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String sql="dip_sysregister_b inner join dip_sysregister_h on dip_sysregister_b.pk_sysregister_h=dip_sysregister_h.pk_sysregister_h";
		return sql;
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_sysregister_b.sitename","dip_sysregister_b.sitecode"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"分布站点名称","分布站点编码"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_sysregister_b.pk_sysregister_b" ;
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
