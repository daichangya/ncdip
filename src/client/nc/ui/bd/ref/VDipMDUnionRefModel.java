package nc.ui.bd.ref;
/**
 * 数据格式定义
 * @author db2admin
 * <nc.ui.bd.ref.VDipMDUnionRefModel>
 * @date 2011-6-9
 */
public class VDipMDUnionRefModel extends AbstractRefModel{
	public VDipMDUnionRefModel(){
		super();
	}

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"v_dip_mdunion.ename","v_dip_mdunion.cname","v_dip_mdunion.datatype"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"英文名称","中文名称","数据类型"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"v_dip_mdunion.bpk"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "引用表字段";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "v_dip_mdunion";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		return super.getWherePart();
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "v_dip_mdunion.bpk";
	}

	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
		super.addWherePart(newWherePart);
	}
}
