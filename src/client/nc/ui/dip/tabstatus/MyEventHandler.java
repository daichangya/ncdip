package nc.ui.dip.tabstatus;

import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillListUI billUI, IListController control) {
		super(billUI, control);		
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		super.onBoSave();
	}
	
		
}