package nc.ui.dip.exp;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author 何冰
 * @version tempProject version
 */

public abstract class AbstractMyEventHandler extends CardEventHandler {

	public AbstractMyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {

	}

}