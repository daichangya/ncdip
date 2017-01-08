package nc.ui.bd.ref;

/**
 * ��Ϣ�����Զ������
 * @author ����
 * 2011-4-14
 */
public class MessTypeRefModel extends AbstractRefModel {

	//���ò���ʾ�ֶΣ��� ����
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_messtype.pk_messtype"};		
	}
	
	public MessTypeRefModel(){
		super();
	}
	
	//����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_messtype.code","dip_messtype.name"};
	}
	
	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"��Ϣ���ͱ���","��Ϣ��������"	
		};
	}
	
	//���մ������
	public String getRefTitle(){
		return "��Ϣ����";
	}
	
	//����������form�Ӿ�
	public String getTableName(){
		return "dip_messtype";
	}
	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_messtype.pk_messtype" ;
	}

	/**
	 * ��׼���Ʋ���ָ����������Ϣ����
	 * �趨where�Ӿ�
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
//		String where ="nvl(dr,0)=0";
		return super.getWherePart();
	}

	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
		super.addWherePart(newWherePart);
	}	
	
	
}
