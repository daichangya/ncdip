package nc.ui.dip.alertmanag;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;

/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
/*依据列表但表头修改此类：只修改了构造方法
 * 2011-5-14
 * zlc
 * */
  public class AbstractMyEventHandler 
                                          extends ListEventHandler/*ManageEventHandler*/{

        public AbstractMyEventHandler(BillListUI billUI, IListController control){
		super(billUI,control);		
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IBtnDefine.Enable:
			onBoEnable(intBtn);
			break;
			
		case IBtnDefine.Disable:
			onBoDisable(intBtn);
		break;
		}
	}

	protected void onBoDisable(int intBtn) {
		
	}

	protected void onBoEnable(int intBtn) {
		
	}
	
		   	
	
}