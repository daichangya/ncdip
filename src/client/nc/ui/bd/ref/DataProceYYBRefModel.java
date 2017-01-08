package nc.ui.bd.ref;
/**
 * ���ݼӹ����ñ����
 * <nc.ui.bd.ref.DataProceYYBRefModel>
 * @date 2011-6-27
 */
public class DataProceYYBRefModel extends AbstractRefModel{

	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 6;
	}

	//	���ò���ʾ�ֶΣ��� ����
	public String[] getHiddenFieldCode(){
		return new String[]{"v_dip_jgyyzd.pk_datadefinit_b"};		
	}

	public DataProceYYBRefModel(){
		super();
	}

	//����select�Ӿ䣬��ʾ�ֶΡ������ֶ�
	public String[] getFieldCode(){
		return new String[]{"v_dip_jgyyzd.memorytable","v_dip_jgyyzd.sysname","cname","ename","v_dip_jgyyzd.syscod","v_dip_jgyyzd.sysna"};
	}

	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"���ñ�","���ñ���������","�����ֶ���������","�����ֶ�","����ϵͳ����","����ϵͳ����"
		};
	}

	//���մ������
	public String getRefTitle(){
		return "���ݶ�����ͼ��";
	}

	//����������form�Ӿ�
	public String getTableName(){
		return "v_dip_jgyyzd";
	}

	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		return "v_dip_jgyyzd.pk_datadefinit_b" ;
	}

	@Override
	public String getWherePart() {
		return super.getWherePart();
	}
	@Override
	public void addWherePart(String newWherePart) {
		super.addWherePart(newWherePart);
	}

	@Override
	public void setOrderPart(String strOrderPart) {
		// TODO Auto-generated method stub
		super.setOrderPart("v_dip_jgyyzd.syscod");
	}

}
