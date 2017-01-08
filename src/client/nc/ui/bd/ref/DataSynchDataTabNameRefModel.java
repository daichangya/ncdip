package nc.ui.bd.ref;
/**
 * 数据同步：数据表名参照
 * 参照 数据定义当前系统下的表且在数据接收定义中 被引用了的表名
 * @author db2admin
 *
 */
public class DataSynchDataTabNameRefModel extends AbstractRefModel{
	public DataSynchDataTabNameRefModel(){
		super();
	}

	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
		super.addWherePart(newWherePart);
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_dataDefinit_h.pk_datadefinit_h"};
	}

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_dataDefinit_h.memorytable","dip_dataDefinit_h.sysname"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"名称","存储表名"};
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_dataDefinit_h inner join dip_datarec_h on dip_datarec_h.memorytable=dip_datadefinit_h.pk_datadefinit_h";
	}
	
	@Override
	public String getRefTitle(){
		return "数据表名";
	}
	
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_dataDefinit_h.pk_datadefinit_h" ;
	}
	
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where =" nvl(dip_dataDefinit_h.dr,0)=0 and nvl(dip_datarec_h.dr,0)=0";
		return where;
	}
}
