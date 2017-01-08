package nc.ui.bd.ref;
/**
 * ���ݶ��塰���ñ��ֶΡ� �Զ������
 * @author zlc
 * @date 2011-4-21
 */
//<nc.ui.bd.ref.MessageRefModel>
public class MessageRefModel extends AbstractRefModel{
	
	public String wherePart = "";
	//	���ò���ʾ�ֶΣ��� ����
	public String[] getHiddenFieldCode(){
		return new String[]{"dip_msr.pk_dip_msr"};		
	}

	public MessageRefModel(){
		super();
	}

	//����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_msr.name","dip_msr.code"};
	}

	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"����","����"	
		};
	}

	//���մ������
	public String getRefTitle(){
		return "��Ϣע��";
	}

	//����������form�Ӿ�
	public String getTableName(){
		return "dip_msr";
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_msr.pk_dip_msr" ;
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		return super.getWherePart();
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}

	
	
}
