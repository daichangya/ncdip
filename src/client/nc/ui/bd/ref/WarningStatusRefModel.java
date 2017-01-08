package nc.ui.bd.ref;


//<nc.ui.ty.ref.BillStatusRefModel>
public class WarningStatusRefModel extends AbstractRefModel {

	public String whereStr = "";

	// ��������Ҫ��ʾ�����ݿ���е��ֶΣ������������
	@Override
	public String[] getFieldCode() {
		String sql = "(select custname from bd_cubasdoc where nvl(dr,0)=0 and pk_cubasdoc = cvendorbaseid)";

		return new String[] { "vordercode", sql, "corderid" };
	}

	// ��������ʾ�����ݿ��ж�Ӧ���ֶεĺ���
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[] { "�ɹ�������", "��Ӧ��", "����" };
	}

	// �����ֶΣ������ֶβ���ʾ��һ��Ϊ����
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[] { "corderid" };
	}

	// ���ն�Ӧ���ݿ���е������ֶ�
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "corderid";
	}

	// ���ձ���
	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "�ɹ�����";
	}

	// ���ն�Ӧ��ȡ�������ݿ��ṹ������Ϊ���������
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "po_order ";
	}

	// //��ѯ����
	// @Override
	// public String getWherePart() {
	// // TODO Auto-generated method stub
	// return "";
	// }

	public String getWhereStr() {
		return whereStr;
	}

	public void setWhereStr(String str) {
		this.whereStr = " and 1=1 " + str;
		this.addWherePart(null);
	}

	@Override
	public void addWherePart(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("...TakeBillStatusRefModel" + getWhereStr());

		super.addWherePart(" " + getWhereStr());
	}

}
