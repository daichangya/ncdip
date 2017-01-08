package nc.ui.dip.message;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.bd.ref.MessageLogoRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.card.CardEventHandler;


/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class MyClientUI extends AbstractClientUI{
       public MyClientUI(){
    	   super();
    	   getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
    	   ((MyEventHandler)this.getCardEventHandler()).ini();
       }
//       protected ManageEventHandler createEventHandler() {
//		return new MyEventHandler(this, getUIControl());
//	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {
		UIRefPane rf=(UIRefPane) getBillCardPanel().getBodyItem("fg").getComponent();
		MessageLogoRefModel rfm=(MessageLogoRefModel) rf.getRefModel();
		rfm.addWherePart("and ctype='�ָ���'");
		getBillCardPanel().getBodyPanel().setShowThMark(false);
//		getBillCardPanel().setBodyShowThMark("duankou", true);//.getBodyItem("duankou").getComponent().set
//		updateUI();
	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CardEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new MyEventHandler(this,getUIControl());
	}
	
    //	�����ֶ�����Ϊ"******"   zlc   2011-07-08
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		
//		�Զ�����
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
		}
		
		if(e.getKey().equals("vdef3")){
			this.getBillCardPanel().setBodyValueAt(this.getBillCardPanel().getBodyValueAt(e.getRow(), "vdef3"), e.getRow(), "upass");
			this.getBillCardPanel().setBodyValueAt("******", e.getRow(), "vdef3");
//			this.getBillCardWrapper().getBillCardPanel().execBodyFormulas(e.getRow(),getBillCardWrapper().getBillCardPanel().getBodyItem("vdef3").getEditFormulas());
//			this.getBillCardWrapper().getBillCardPanel().execBodyFormulas(e.getRow(),getBillCardWrapper().getBillCardPanel().getBodyItem("vdef3").getLoadFormula());
		}
	}
	


}
