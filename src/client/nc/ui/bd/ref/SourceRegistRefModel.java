package nc.ui.bd.ref;

/**
 * webserviceע���Զ������
 * @author ����
 * 2011-4-14
 */
public class SourceRegistRefModel extends AbstractRefModel {

	//���ò���ʾ�ֶΣ��� ����
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_sourceregist.pk_sourceregist"};		
	}
	
	public SourceRegistRefModel(){
		super();
	}
	
	//����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_sourceregist.code","dip_sourceregist.name","dip_sourceregist.url"};
	}
	
	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"����","����"	,"URL"
		};
	}
	
	//���մ������
	public String getRefTitle(){
		return "Webserviceע��";
	}
	
	//����������form�Ӿ�
	public String getTableName(){
		return "dip_sourceregist";
	}
	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_sourceregist.pk_sourceregist" ;
	}

	/**
	 * ��׼���Ʋ���ָ������������Դע��
	 * �趨where�Ӿ�
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}

	@Override
	public int getDefaultFieldCount() {
		return 3;
	}	
}
