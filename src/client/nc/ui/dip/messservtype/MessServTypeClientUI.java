package nc.ui.dip.messservtype;

import nc.ui.pub.beans.UIRefPane;
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
 public class MessServTypeClientUI extends AbstractMessServTypeClientUI{
       public MessServTypeClientUI(){
    	   super();
    	   ((MyEventHandler)this.getCardEventHandler()).ini();
    	   UIRefPane rf=(UIRefPane) getBillCardPanel().getBodyItem("dlm").getComponent();
   			rf.getUITextField().setEnabled(false);
   			rf.getUIButton().addActionListener(new SoSTimeActionListener(this));
   			UIRefPane rf2=(UIRefPane) getBillCardPanel().getBodyItem("vdef3").getComponent();
   			rf2.getUITextField().setEnabled(false);
   			rf2.getUIButton().addActionListener(new SoSTimeActionListener(this));
       }
       /**
	 * 
	 */
       
       public void onBoTimeSet(){
   		try {
   			((MyEventHandler)getCardEventHandler()).onBoTimeSet();
   		} catch (Exception e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
   	}
	private static final long serialVersionUID = 1L;

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
		super.afterEdit(arg0);
//		自动增行
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
		}
		if(arg0.getKey().equals("type")){
			int row=arg0.getRow();
			Object value=getBillCardPanel().getBodyValueAt(row, "type");
			if(value!=null&&value.toString().equals("即时启动")){
				getBillCardPanel().setBodyValueAt(null,row , "vdef3");
				getBillCardPanel().setBodyValueAt(null,row , "vdef4");
				getBillCardPanel().setBodyValueAt(null,row , "vdef5");
				getBillCardPanel().setBodyValueAt(null,row , "dlm");
			}
		}
	}
	
	

}
