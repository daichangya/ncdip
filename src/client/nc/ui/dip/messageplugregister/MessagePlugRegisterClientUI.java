package nc.ui.dip.messageplugregister;

import javax.swing.ListSelectionModel;

import nc.ui.dip.messageplugregister.AbstractMessagePlugRegisterClientUI;
import nc.ui.dip.messageplugregister.MyEventHandler;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dip.messageplugregister.DipMessagePlugRegisterVO;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class MessagePlugRegisterClientUI extends AbstractMessagePlugRegisterClientUI {
	public MessagePlugRegisterClientUI(){
		super();
		 getBillCardPanel().getBillTable().setSortEnabled(false);
  	   getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
  	   getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		((MyEventHandler) this.getCardEventHandler()).ini();
		((MyEventHandler) this.getCardEventHandler()).setSelectRow(0, 0);
		int i=getBillCardPanel().getBillTable().getSelectedRow();
		Boolean flag=(Boolean)getBillCardPanel().getBillModel().getValueAt(i, new DipMessagePlugRegisterVO().ISSYSPREF);
		if(flag!=null&&flag){
			getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		}else{
			getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
		}
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public static boolean flag=false;
	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {	}

	protected void initSelfData() {	
		getBillCardPanel().getBodyPanel().setRowNOShow(true);

	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void bodyRowChange(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		super.bodyRowChange(arg0);
//		this.getBillCardPanel().getBillTable().getModel().get
		if(!MessagePlugRegisterClientUI.flag){
			int i=this.getBillCardPanel().getBillTable().getSelectedRow();
			Boolean flag=(Boolean)this.getBillCardPanel().getBillModel().getValueAt(i, new DipMessagePlugRegisterVO().ISSYSPREF);
			
			if(flag!=null&&flag){
				getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			}else{
				getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
			}	
		}
		
		try {
			updateButtonUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		//自动增行
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
			flag=true;
		}
	}

//	@Override
//	public boolean beforeEdit(BillEditEvent arg0) {
//		int row=getBillCardPanel().getBillTable().getSelectedRow();
//		Boolean flag=(Boolean)getBillCardPanel().getBillModel().getValueAt(row, new DipTaskregisterVO().ISSYSPREF);
//		if(flag!=null&&flag){
//			this.showErrorMessage("系统预置记录不允许修改！");
//			return false;
//			
//		}
//		// TODO Auto-generated method stub
//		return super.beforeEdit(arg0);
//	}

	
}
