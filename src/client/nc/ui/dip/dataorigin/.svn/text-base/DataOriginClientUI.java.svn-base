package nc.ui.dip.dataorigin;

import javax.swing.ListSelectionModel;

import nc.vo.dip.dataorigin.DipDataoriginVO;


import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.pub.bill.BillEditEvent;


import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;



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
 public class DataOriginClientUI extends AbstractDataOriginClientUI{
       public DataOriginClientUI(){
    	   super();
    	   getBillCardPanel().getBillTable().setSortEnabled(false);
    	   getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
    	   getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	   ((MyEventHandler)this.getCardEventHandler()).ini();
    	   if(getBillCardPanel().getBillModel().getRowCount()>0){
    		   getBillCardPanel().getBillTable().changeSelection(0, 0, false,false);
    	   }
    	   int i=getBillCardPanel().getBillTable().getSelectedRow();
	   		Boolean flag=(Boolean)getBillCardPanel().getBillModel().getValueAt(i, new DipDataoriginVO().ISSYSPREF);
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
	public static boolean delFlag=true;
	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
     
	public String getRefBillType() {
		return null;
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

//	@Override
//	public boolean beforeEdit(BillEditEvent arg0) {
//		// TODO Auto-generated method stub
//		int row=getBillCardPanel().getBillTable().getSelectedRow();
//		Boolean flag=(Boolean)getBillCardPanel().getBillModel().getValueAt(row, new DipDataoriginVO().ISSYSPREF);
//		if(flag!=null&&flag){
//			this.showErrorMessage("系统预置记录不允许修改！");
//			return false;
//		}
//		return super.beforeEdit(arg0);
//	}
	
	@Override
	public void bodyRowChange(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		super.bodyRowChange(arg0);
		int i=getBillCardPanel().getBillTable().getSelectedRow();
		if(DataOriginClientUI.delFlag){
			Boolean flag=(Boolean)getBillCardPanel().getBillModel().getValueAt(i, new DipDataoriginVO().ISSYSPREF);
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
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
		}
	}
}
