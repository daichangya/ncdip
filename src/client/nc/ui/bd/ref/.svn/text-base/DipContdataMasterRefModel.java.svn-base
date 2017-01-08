package nc.ui.bd.ref;
public class DipContdataMasterRefModel extends AbstractRefModel{
	
	
	public DipContdataMasterRefModel(){
		super();
		
	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"pk_contdata_h"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "主控节点表";
	}

	@Override
	public String getTableName() {
		return "dip_adcontdata";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		
		String wherePart = super.getWherePart();
		if(null != wherePart && !"".equals(wherePart)){
			wherePart += " and ismaster='Y' ";
		}else{
			wherePart = " ismaster='Y' ";
		}
		return wherePart;
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"code","name"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"编码","名称"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_contdata_h";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
		
		super.addWherePart(newWherePart);
	}
	
}
