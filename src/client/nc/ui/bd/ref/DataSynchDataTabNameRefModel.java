package nc.ui.bd.ref;
/**
 * ����ͬ�������ݱ�������
 * ���� ���ݶ��嵱ǰϵͳ�µı��������ݽ��ն����� �������˵ı���
 * @author db2admin
 *
 */
public class DataSynchDataTabNameRefModel extends AbstractRefModel{
	public DataSynchDataTabNameRefModel(){
		super();
	}

	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
		super.addWherePart(newWherePart);
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_dataDefinit_h.pk_datadefinit_h"};
	}

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_dataDefinit_h.memorytable","dip_dataDefinit_h.sysname"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"����","�洢����"};
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_dataDefinit_h inner join dip_datarec_h on dip_datarec_h.memorytable=dip_datadefinit_h.pk_datadefinit_h";
	}
	
	@Override
	public String getRefTitle(){
		return "���ݱ���";
	}
	
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_dataDefinit_h.pk_datadefinit_h" ;
	}
	
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where =" nvl(dip_dataDefinit_h.dr,0)=0 and nvl(dip_datarec_h.dr,0)=0";
		return where;
	}
}
