package nc.ui.bd.ref;
/*
 * 系统注册：数据对照定义：“被对照系统”字段参照除当前系统外的所有系统
 * <nc.ui.bd.ref.SysRegisterHRefModel>
 */
public class SysRegisterHRefModel extends AbstractRefModel{
	public SysRegisterHRefModel(){
		super();
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_sysregister_h.pk_sysregister_h"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "系统名称";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String sql="dip_sysregister_h";
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
		return new String[]{"dip_sysregister_h.code","dip_sysregister_h.extname"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"编码","外部系统名称"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_sysregister_h.pk_sysregister_h" ;
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
}
