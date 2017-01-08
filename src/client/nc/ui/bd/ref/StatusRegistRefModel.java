package nc.ui.bd.ref;
/**
 * 数据状态注册自定义参照
 * @author db2admin
 *
 */
//<nc.ui.bd.ref.StatusRegistRefModel>
public class StatusRegistRefModel extends AbstractRefModel {

	public StatusRegistRefModel(){
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_statusregist.code","dip_statusregist.name"," case when isstack is null then '否' when isstack='Y' then '是' else  '否' end  isstack"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"编码","名称","是否独占"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "数据状态注册";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_statusregist";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where="nvl(dr,0)=0";
		return where;
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_statusregist.pk_statusregist"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_statusregist.pk_statusregist" ;
	}

	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 3;
	}
}
