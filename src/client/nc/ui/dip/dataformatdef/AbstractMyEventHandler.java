package nc.ui.dip.dataformatdef;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.CardEventHandler;

/**
 *
 *������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
 *@author author
 *@version tempProject version
 */

public class AbstractMyEventHandler extends CardEventHandler{

	public AbstractMyEventHandler(DataForDefClientUI dataForDefClientUI, ICardController control){
		super(dataForDefClientUI,(ICardController) control);		
	}

	protected void onBoElse(int intBtn) throws Exception {
	}
}