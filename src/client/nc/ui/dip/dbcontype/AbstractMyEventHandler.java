package nc.ui.dip.dbcontype;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;


/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler  extends CardEventHandler{

        public AbstractMyEventHandler(BillCardUI billcardUI, ICardController control){
		super(billcardUI,control);		
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		     	}
	
		   	
	
}