package nc.ui.dip.datalook;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;

/**
 * 
 * ����Ƭ�͵�����,������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
 * 
 * @author �α�
 * @version tempProject version
 */

public abstract class AbstractMyEventHandler extends TreeCardEventHandler {

	public AbstractMyEventHandler(BillTreeCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		
	}
			
		
        protected void onDataClear(int intBtn)throws Exception {}
		protected void onDatalookQuery(int intBtn) throws Exception{};
}