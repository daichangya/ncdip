package nc.ui.bd.ref;
/**
 * 数据接收状态维护自定义参照
 * @author db2admin
 *
 */
public class RecStatusRefModel extends AbstractRefModel {
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_recstatus.pk_recstatus"};
	}

	public RecStatusRefModel(){
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_recstatus.code","dip_recstatus.name"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"接收状态编码","接收状态名称"};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_recstatus.pk_recstatus";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_recstatus";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "数据接收状态维护";
	}
	
	/**
	 * 标准名称查找指定档案： 数据接收状态维护
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where =" nvl(dr,0)=0";
		return where;
	}	
	
	
}
