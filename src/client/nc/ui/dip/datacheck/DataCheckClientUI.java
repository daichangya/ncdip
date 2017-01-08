package nc.ui.dip.datacheck;

import javax.swing.ListSelectionModel;

import nc.ui.dip.dataorigin.DataOriginClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dip.datacheck.DataCheckVO;
import nc.vo.dip.dataorigin.DipDataoriginVO;
import nc.vo.dip.taskregister.DipTaskregisterVO;
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
 public class DataCheckClientUI extends AbstractDataCheckClientUI{
       public DataCheckClientUI(){
    	   super();
    	   getBillCardPanel().getBillTable().setSortEnabled(false);
    	   getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
    	   getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	   ((MyEventHandler)this.getCardEventHandler()).ini();
    	   ((MyEventHandler)this.getCardEventHandler()).setSelectRow(0, 0);
    		int i=getBillCardPanel().getBillTable().getSelectedRow();
    		Boolean flag=(Boolean)getBillCardPanel().getBillModel().getValueAt(i, new DataCheckVO().DEF_STR_1);
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
	
	@Override
	public void bodyRowChange(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		super.bodyRowChange(arg0);
		int i=getBillCardPanel().getBillTable().getSelectedRow();
		if(DataCheckClientUI.delFlag){
			Boolean flag=(Boolean)getBillCardPanel().getBillModel().getValueAt(i, new DataCheckVO().DEF_STR_1);
			if(flag!=null&&flag){
				getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			}else{
				getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
			}
		}
		
		try {
			updateButtonUI();
		} catch (Exception e) {
			// TODO Auto-generated catch blockpr
			e.printStackTrace();
		}
	}

}
