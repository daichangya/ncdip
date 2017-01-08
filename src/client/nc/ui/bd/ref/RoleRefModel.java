package nc.ui.bd.ref;
//<nc.ui.bd.ref.CorpRoleAuthRefModel>
/**
 * 数据接收定义中格式复制使用的参照
 * */
public class RoleRefModel extends AbstractRefModel{

	@Override
	public int getDefaultFieldCount() {
		return 4;
	}

    public RoleRefModel(){
    	super();
    }

	@Override
	public String[] getFieldCode() {
		return new String[]{"sm_role.role_code","sm_role.role_name","case when bd_corp.unitcode is null then '0001' else bd_corp.unitcode end unitcode"
				,"case when bd_corp.unitcode is null then '集团' else bd_corp.unitname end unitname"
				};
	}

	@Override
	public String[] getFieldName() {
		return new String[]{"角色编码","角色名称","公司编码","公司名称"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[]{"sm_role.pk_role"};
	}

	@Override
	public String getPkFieldCode() {
		return "sm_role.pk_role";
	}

	@Override
	public String getRefTitle() {
		return "角色";
	}

	@Override
	public String getTableName() {
		return "sm_role left join bd_corp on sm_role.pk_corp=bd_corp.pk_corp ";
	}

	@Override
	public String getOrderPart() {
		return "unitcode";
	}

	
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		return " nvl(sm_role.dr,0)=0 ";
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
