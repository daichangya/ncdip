package nc.ui.dip.dateprocess;


import nc.ui.trade.card.CardEventHandler;

public class DateProcessClientUI extends AbstractDateProcessClientUI{
	/**
	 * 
	 */
	public DateProcessClientUI(){
		super();
		this.getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
	}
	private static final long serialVersionUID = 1L;
	
	
	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

//	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
//	throws Exception {}
//
//	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
//			int intRow) throws Exception {}
//
//	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
//	throws Exception {	}

	/**
	 * 修改此方法初始化模板控件数据
	 */
	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}

	public String getRefBillType() {
		return null;
	}


}
