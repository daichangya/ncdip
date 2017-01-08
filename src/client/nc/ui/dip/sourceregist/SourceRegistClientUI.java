package nc.ui.dip.sourceregist;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;


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
 public class SourceRegistClientUI extends AbstractSourceRegistClientUI{
       
	 public SourceRegistClientUI(){
		 super();
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
//	�����ֶ�����Ϊ"******"   zlc   2011-07-08
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
//		�Զ�����
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
		}
		if(e.getKey().equals("def_str_1")){
			this.getBillCardPanel().setBodyValueAt(this.getBillCardPanel().getBodyValueAt(e.getRow(), "def_str_1"), e.getRow(), "pass");
			this.getBillCardPanel().setBodyValueAt("******", e.getRow(), "def_str_1");
//			this.getBillCardWrapper().getBillCardPanel().execBodyFormulas(e.getRow(),getBillCardWrapper().getBillCardPanel().getBodyItem("def_str_1").getEditFormulas());
//			this.getBillCardWrapper().getBillCardPanel().execBodyFormulas(e.getRow(),getBillCardWrapper().getBillCardPanel().getBodyItem("def_str_1").getLoadFormula());
		}
	}

}
