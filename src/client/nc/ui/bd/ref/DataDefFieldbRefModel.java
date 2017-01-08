package nc.ui.bd.ref;
/**
 * ���ݶ��塰���ñ��ֶΡ� �Զ������
 * @author zlc
 * @date 2011-4-21
 */
public class DataDefFieldbRefModel extends AbstractRefModel{
	
	public String wherePart = "";
	//	���ò���ʾ�ֶΣ��� ����
	public String[] getHiddenFieldCode(){
		return new String[]{"dip_datadefinit_b.pk_datadefinit_b"};		
	}

	public DataDefFieldbRefModel(){
		super();
	}

	//����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_datadefinit_b.cname","dip_datadefinit_b.ename"};
	}

	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"��������","Ӣ������"	
		};
	}

	//���մ������
	public String getRefTitle(){
		return "���ñ��ֶ�";
	}

	//����������form�Ӿ�
	public String getTableName(){
		return "dip_datadefinit_b";
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_datadefinit_b.pk_datadefinit_b" ;
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
