package nc.ui.bd.ref;

public class TyzhRefModel extends AbstractRefModel{
	
	
//	public TyzhRefModel(){
//		super();
//		
//	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_tyzhq_h.pk_tyzhq_h","dip_datadefinit_h.pk_datadefinit_h"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "ͨ��ת��������";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String sql="dip_tyzhq_h inner join  dip_datadefinit_h  on dip_tyzhq_h.targettab=dip_datadefinit_h.pk_datadefinit_h  and dip_tyzhq_h.pk_xt=dip_datadefinit_h.pk_xt";
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
		return new String[]{"dip_tyzhq_h.code","dip_tyzhq_h.name","dip_datadefinit_h.sysname","dip_datadefinit_h.memorytable"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"ͨ��ת������","ͨ��ת������","Ŀ�������","Ŀ����������"};
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_tyzhq_h.pk_tyzhq_h";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 4;
	}

}
