package nc.ui.dip.runsys;

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
public class RunSysClientUI extends AbstractRunSysClientUI{
	private static final long serialVersionUID = 1L;
	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
	public RunSysClientUI(){
		super();
		try {
			((MyEventHandler)this.getCardEventHandler()).init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}
	 */
	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public String getRefBillType() {
		return null;
	}

	@Override
	public void afterEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
//		�Զ�����
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
		}
	}
}
