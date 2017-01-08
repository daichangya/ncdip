package nc.ui.dip.ftpsourceregister;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.card.CardEventHandler;
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
 public class FtpSourceRegisterClientUI extends AbstractFtpSourceRegisterClientUI{
       public FtpSourceRegisterClientUI(){
    	   super();
    	   getButtonManager().getButton(IBtnDefine.TESTESBSEND).setName("连接测试");
    	   getBillCardPanel().getBillTable().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    	   getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
    	   ((MyEventHandler)this.getCardEventHandler()).ini();
       }
       protected CardEventHandler createEventHandler() {
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
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
		}
		if(e.getKey().equals("defstr_1")){
			this.getBillCardPanel().setBodyValueAt(this.getBillCardPanel().getBodyValueAt(e.getRow(), "defstr_1"), e.getRow(), "password");
			this.getBillCardPanel().setBodyValueAt("******", e.getRow(), "defstr_1");
		}
	}

}
