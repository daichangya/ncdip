package nc.ui.dip.exp;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

/**
 * 
 * ������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
 * 
 * @author �α�
 * @version tempProject version
 */

public abstract class AbstractMyEventHandler extends CardEventHandler {

	public AbstractMyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {

	}

}