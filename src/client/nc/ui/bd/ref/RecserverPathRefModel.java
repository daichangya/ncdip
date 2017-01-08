package nc.ui.bd.ref;

/**
 * NC���շ�����ע���Զ������
 * @author �Ž�˫
 * 2011-5-21
 */
public class RecserverPathRefModel extends AbstractRefModel {

	//���ò���ʾ�ֶΣ��� ����
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_recserver.pk_recserver"};		
	}
	
	public RecserverPathRefModel(){
		super();
	}
	
	//����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		//2011-6-15 �����ˡ�dip_recserver.descs	��	
		return new String[]{"dip_recserver.code","dip_recserver.name","dip_recserver.descs"};
	}
	
	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		//2011-6-15 �����ˡ����������ԡ��ֶ�
		return new String[]{
				"����������","����������","����������"
		};
	}
	
	//���մ������
	public String getRefTitle(){
		return "NC���շ�����ע��";
	}
	
	//����������form�Ӿ�
	public String getTableName(){
		return "dip_recserver";
	}
	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_recserver.pk_recserver" ;
	}

	/**
	 * ��׼���Ʋ���ָ��������NC���շ�����ע��
	 * �趨where�Ӿ�
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
}
