package nc.ui.dip.ftpsourceregister;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

/**
  *
  *������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler 
                                          extends CardEventHandler{

        public AbstractMyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);		
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		switch(intBtn){
	    case IBtnDefine.TESTESBSEND:
	    	onBoFtpTest();
		break;
        }
	 }

	protected void onBoFtpTest() throws Exception{
		// TODO Auto-generated method stub
		
	}
	
		   	
	
}