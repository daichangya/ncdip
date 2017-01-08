package nc.ui.dip.datasend;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;

/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler 
                                          extends TreeCardEventHandler{

	  public AbstractMyEventHandler(BillTreeCardUI billUI, ICardController control){
			super(billUI,control);		
		}

		protected void onBoElse(int intBtn) throws Exception {
		}

		   	
	
}