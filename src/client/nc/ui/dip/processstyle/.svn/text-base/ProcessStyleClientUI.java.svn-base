package nc.ui.dip.processstyle;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
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
 public class ProcessStyleClientUI extends AbstractProcessStyleClientUI{
       /**
	 * 
	 */
	private static final long serialVersionUID = -5064783035623382987L;
	public static boolean delFlag=true;
	public ProcessStyleClientUI(){
    	   super();
    	   //this.getBillCardPanel().getBillTable().setSelectionModel(ListSelectionModel.SINGLE_SELECTION);
    	   getBillCardPanel().getBillTable().setSortEnabled(false);
    	   getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
    	   getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	   ((MyEventHandler)this.getCardEventHandler()).ini();
    	   ((MyEventHandler)this.getCardEventHandler()).setSelectRow(0, 0);
    		int i=getBillCardPanel().getBillTable().getSelectedRow();
    		Boolean flag=(Boolean)getBillCardPanel().getBillModel().getValueAt(i,"def_str_1");
    		if(flag!=null&&flag){
    			getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
    		}else{
    			getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
    		}
       }
       protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

   	@Override
   	public void bodyRowChange(BillEditEvent arg0) {
   		super.bodyRowChange(arg0);
		int i=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(ProcessStyleClientUI.delFlag){
			Boolean flag=(Boolean)this.getBillCardPanel().getBillModel().getValueAt(i, "def_str_1");
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
	public void afterEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		//自动增行
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
		}
	}


}
