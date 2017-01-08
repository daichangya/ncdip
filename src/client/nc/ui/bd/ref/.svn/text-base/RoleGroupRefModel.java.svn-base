package nc.ui.bd.ref;
public class RoleGroupRefModel extends AbstractRefModel{

	@Override
	public int getDefaultFieldCount() {
		return 3;
	}

    public RoleGroupRefModel(){
    	super();
    }

	@Override
	public String[] getFieldCode() {
		return new String[]{"group_code","group_name","group_memo"};
	}

	@Override
	public String[] getFieldName() {
		return new String[]{"角色组编码","角色组名称","角色组备注"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[]{"pk_role_group"};
	}

	@Override
	public String getPkFieldCode() {
		return "pk_role_group";
	}

	@Override
	public String getRefTitle() {
		return "角色组";
	}

	@Override
	public String getTableName() {
		return "dip_rolegroup";
	}

	@Override
	public String getOrderPart() {
		return "group_code";
	}

	
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		return " nvl(dr,0)=0 ";
	}
	
	@Override
	public void addWherePart(String arg0) {
		// TODO Auto-generated method stub
		super.addWherePart(arg0);
	}
	/*
	@Override
	public int getDefaultFieldCount() {
		return 4;
	}

	public String newWherePart="";
    public CorpRoleAuthRefModel(){
    	super();
    }

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"unitcode","unitname","role_code","role_name"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"公司编码","公司名称","角色编码","角色名称"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"pk_role_corp_alloc"};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_role_corp_alloc";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "角色分配公司";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "v_dip_corproleauth";
	}

*/}
