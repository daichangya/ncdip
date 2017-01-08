package nc.ui.bd.ref;

/**
 * ������Դ�����Զ������
 * @author db2admin
 *
 */
public class DataOriginRefModel extends AbstractRefModel {

	//���ò���ʾ�ֶΣ��� ����
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_dataorigin.pk_dataorigin"};		
	}
	
	public DataOriginRefModel(){
		super();
	}
	
	//����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_dataorigin.code","dip_dataorigin.name"};
	}
	
	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"��Դ���ͱ���","��Դ��������"	
		};
	}
	
	//���մ������
	public String getRefTitle(){
		return "������Դ����";
	}
	
	//����������form�Ӿ�
	public String getTableName(){
		return "dip_dataorigin";
	}
	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_dataorigin.pk_dataorigin" ;
	}

	/**
	 * ��׼���Ʋ���ָ��������������Դ����
	 * �趨where�Ӿ�
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
}
