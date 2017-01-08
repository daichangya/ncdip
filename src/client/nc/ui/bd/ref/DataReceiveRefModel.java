package nc.ui.bd.ref;
//数据接收定义参照
//<nc.ui.bd.ref.DataReceiveRefModel>
public class DataReceiveRefModel extends AbstractRefModel{
	public String newWherePart="";
    public DataReceiveRefModel(){
    	super();
    }

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_datarec_h.syscode rcode","dip_datarec_h.recname","dip_datarec_h.ioflag","dip_datadefinit_h.syscode","dip_datadefinit_h.sysname","dip_datadefinit_h.memorytable"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"同步定义编码","同步定义名称","输入输出标志","数据定义编码","数据表名","物理表名"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_datarec_h.pk_datarec_h"};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_datarec_h.pk_datarec_h";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "数据接收定义";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_datarec_h left join dip_datadefinit_h on dip_datarec_h.memorytable=dip_datadefinit_h.pk_datadefinit_h ";
	}

	@Override
	public String getOrderPart() {
		// TODO Auto-generated method stub
		return "dip_datarec_h.syscode";
	}

	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 6;
	}
 
//	@Override
//	public String getWherePart() {
//		// TODO Auto-generated method stub
//		return super.getWherePart() ;
//	}
//	@Override
//	public void addWherePart(String newWherePart) {
//		// TODO Auto-generated method stub
//	
//		super.addWherePart(newWherePart);
//	}
}
