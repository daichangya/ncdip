package nc.ui.dip.returnmess;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.list.ListEventHandler;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class ReturnMessClientUI extends AbstractReturnMessClientUI{
       
       protected ListEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		super.afterEdit(arg0);
		arg0.getTableCode();
		arg0.getOldRow();
		int k=arg0.getRow();
		int w=getBillListPanel().getBodyBillModel().getRowCount();
		if(k==w-1){
			getBillListPanel().getBodyBillModel().addLine();
		}
		
	}
	
	

}
