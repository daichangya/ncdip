package nc.ui.bd.ref;


//<nc.ui.ty.ref.BillStatusRefModel>
public class WarningStatusRefModel extends AbstractRefModel {

	public String whereStr = "";

	// 参照所需要显示的数据库表中的字段，必须包含主键
	@Override
	public String[] getFieldCode() {
		String sql = "(select custname from bd_cubasdoc where nvl(dr,0)=0 and pk_cubasdoc = cvendorbaseid)";

		return new String[] { "vordercode", sql, "corderid" };
	}

	// 参照所显示的数据库中对应的字段的汉字
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[] { "采购订单号", "供应商", "主键" };
	}

	// 隐藏字段，即此字段不显示，一般为主键
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[] { "corderid" };
	}

	// 参照对应数据库表中的主键字段
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "corderid";
	}

	// 参照标题
	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "采购订单";
	}

	// 参照对应的取数的数据库表结构，可以为多个表连接
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "po_order ";
	}

	// //查询条件
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
