package nc.ui.bd.ref;
/*
 * ϵͳע�᣺���ݶ��ն��壺��������ϵͳ���ֶβ��ճ���ǰϵͳ�������ϵͳ
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
		return "ϵͳ����";
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
		return new String[]{"����","�ⲿϵͳ����"};
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
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
