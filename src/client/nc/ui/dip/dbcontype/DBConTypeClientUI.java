package nc.ui.dip.dbcontype;

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
 public class DBConTypeClientUI extends AbstractDBConTypeClientUI{
       public DBConTypeClientUI(){
    	   super();
    	   ((MyEventHandler)this.getCardEventHandler()).ini();
       }
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
//	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
//			throws Exception {}
//
//	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
//			int intRow) throws Exception {}
//
//	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
//			throws Exception {	}

	protected void initSelfData() {
		
	}

	public void setDefaultData() throws Exception {
		
	}

	
	@Override
	public String getRefBillType() {
		return null;
	}
    //�����ֶ�����Ϊ"******"   zlc   2011-07-08
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		//�Զ�����
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
		}
		
		if(e.getKey().equals("vdef1")){
			this.getBillCardPanel().setBodyValueAt(this.getBillCardPanel().getBodyValueAt(e.getRow(), "vdef1"), e.getRow(), "password");
			this.getBillCardPanel().setBodyValueAt("******", e.getRow(), "vdef1");
//			this.getBillCardWrapper().getBillCardPanel().execBodyFormulas(e.getRow(),getBillCardWrapper().getBillCardPanel().getBodyItem("vdef1").getEditFormulas());
//			this.getBillCardWrapper().getBillCardPanel().execBodyFormulas(e.getRow(),getBillCardWrapper().getBillCardPanel().getBodyItem("vdef1").getLoadFormula());
		}
	}
	


}
