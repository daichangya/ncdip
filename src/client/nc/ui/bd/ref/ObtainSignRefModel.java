package nc.ui.bd.ref;
/**
 * ��ȡ��־�Զ������
 * @author db2admin
 *
 */
//nc.ui.bd.ref.ObtainSignRefModel
public class ObtainSignRefModel extends AbstractRefModel {

	//���ò���ʾ�ֶΣ��� ����
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_obtainsign.pk_obtainsign"};		
	}
	
	public ObtainSignRefModel(){
		super();
	}
	
	//����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_obtainsign.code","dip_obtainsign.name"};
	}
	
	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"��ȡ��־����","��ȡ��־����"	
		};
	}
	
	//���մ������
	public String getRefTitle(){
		return "��ȡ��־";
	}
	
	//����������form�Ӿ�
	public String getTableName(){
		return "dip_obtainsign";
	}
	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_obtainsign.pk_obtainsign" ;
	}

	/**
	 * ��׼���Ʋ���ָ����������ȡ��־
	 * �趨where�Ӿ�
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
}
