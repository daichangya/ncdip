package nc.ui.bd.ref;
/**
 * ����״̬ע���Զ������
 * @author db2admin
 *
 */
//<nc.ui.bd.ref.StatusRegistRefModel>
public class StatusRegistRefModel extends AbstractRefModel {

	public StatusRegistRefModel(){
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_statusregist.code","dip_statusregist.name"," case when isstack is null then '��' when isstack='Y' then '��' else  '��' end  isstack"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"����","����","�Ƿ��ռ"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "����״̬ע��";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_statusregist";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where="nvl(dr,0)=0";
		return where;
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_statusregist.pk_statusregist"};
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_statusregist.pk_statusregist" ;
	}

	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 3;
	}
}
