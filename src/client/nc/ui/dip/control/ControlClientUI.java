package nc.ui.dip.control;


import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.control.MyBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;


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
 public class ControlClientUI extends AbstractWarningSetClientUI{
       
       protected ManageEventHandler createEventHandler() {
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
	
	/* 
	public void init(String name,String code){
		getBillCardPanel().getHeadItem("wname").setValue(name);
		getBillCardPanel().getHeadItem("wcode").setValue(code);
	}*/

	public void init(ControlHVO hvo,String pk_bus,String ywlx,String tabls) throws UifException{
		try {
			((MyEventHandler)this.getManageEventHandler()).ini( hvo, pk_bus, ywlx, tabls);
			getBillCardPanel().getBillTable().setSortEnabled(false);
			int rowcount=getBillCardPanel().getBillModel().getRowCount();
			for(int i=0;i<rowcount;i++){
				getBillCardPanel().setBodyValueAt(i+1, i, "disno");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return super.beforeEdit(e);
	}
	
	public void onButtonAction(ButtonObject bo)throws Exception	{
		((MyEventHandler)this.getManageEventHandler()).onButtonAction(bo);
	}
}
