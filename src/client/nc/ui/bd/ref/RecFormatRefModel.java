package nc.ui.bd.ref;
/*
 * ���ݽ��ո�ʽ�Զ������
 */
public class RecFormatRefModel extends AbstractRefModel {
	public RecFormatRefModel(){
		super();
	}
	
	//���ò���ʾ�ֶΣ��� ����
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_recformat.pk_recformat"};		
	}
	
	//����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_recformat.code","dip_recformat.name"};
	}
	
	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"����","����"	,"��ֵ"
		};
	}
	
	//���մ������
	public String getRefTitle(){
		return "���ݽ��ո�ʽ";
	}
	
	//����������form�Ӿ�
	public String getTableName(){
		return "dip_recformat";
	}
	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_recformat.pk_recformat" ;
	}

	/**
	 * �趨where�Ӿ�
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
}
