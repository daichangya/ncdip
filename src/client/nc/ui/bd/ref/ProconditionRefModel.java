package nc.ui.bd.ref;

public class ProconditionRefModel extends  AbstractRefModel{
	//���ò���ʾ�ֶ�
	public String[] getHiddenFieldCode(){
		return new String[]{"dip_procondition.pk_procondition"};
	}
	
	public ProconditionRefModel(){
		super();
	}
//	����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_procondition.code","dip_procondition.name"};
	}
//	��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"����","����"	
		};

}
//	���մ������
	public String getRefTitle(){
		return "�ӹ�����";
	}
	
	//����������form�Ӿ�
	public String getTableName(){
		return "dip_procondition";
	}
	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_procondition.pk_procondition" ;
	}
	/**
	 * ��׼���Ʋ���ָ���������ӹ�����
	 * �趨where�Ӿ�
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
	
}
