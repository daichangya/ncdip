package nc.ui.bd.ref;
/**
 * ���ݽ���״̬ά���Զ������
 * @author db2admin
 *
 */
public class RecStatusRefModel extends AbstractRefModel {
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_recstatus.pk_recstatus"};
	}

	public RecStatusRefModel(){
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_recstatus.code","dip_recstatus.name"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"����״̬����","����״̬����"};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_recstatus.pk_recstatus";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_recstatus";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "���ݽ���״̬ά��";
	}
	
	/**
	 * ��׼���Ʋ���ָ�������� ���ݽ���״̬ά��
	 * �趨where�Ӿ�
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where =" nvl(dr,0)=0";
		return where;
	}	
	
	
}
