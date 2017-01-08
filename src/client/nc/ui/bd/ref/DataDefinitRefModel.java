package nc.ui.bd.ref;
//<nc.ui.bd.ref.DataDefinitRefModel>
public class DataDefinitRefModel extends AbstractRefModel{
    //	���ò���ʾ�ֶΣ��� ����
	

	public String[] getHiddenFieldCode(){
		return new String[]{"dip_dataDefinit_h.pk_datadefinit_h"};		
	}

	public DataDefinitRefModel(){
		super();
	}

	//����select�Ӿ䣬��ʾ�ֶΡ������ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_datadefinit_h.syscode","dip_dataDefinit_h.sysname","dip_dataDefinit_h.memorytable"};
	}

	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"����","����","�������"
		};
	}

	//���մ������
	public String getRefTitle(){
		return "���ݶ�������";
	}

	//����������form�Ӿ�
	public String getTableName(){
		return "dip_dataDefinit_h";
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_dataDefinit_h.pk_datadefinit_h" ;
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
//		return super.getWherePart();
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}

	@Override
	public int getDefaultFieldCount() {
		return 3;
	}

	
	
	
}
