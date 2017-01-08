package nc.ui.dip.message;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;

import nc.ui.dip.buttons.TestRecBtn;
import nc.ui.dip.buttons.TestSendBtn;
import nc.ui.dip.message.MessageClientUICheckRuleGetter;
import nc.ui.pub.linkoperate.*;
import nc.vo.trade.button.ButtonVO;
import nc.ui.trade.base.IBillOperate;

/**
 * <b> ��Ƭ�͵�����UI������ </b>
 * 
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>
 * 
 * @author �α�
 * @version tempProject version
 */

public abstract class AbstractClientUI extends nc.ui.trade.card.BillCardUI implements ILinkQuery {

	/**
	 * �˷�������������
	 */
	protected ICardController createController() {
		return new ClientUICtrl();
	}

	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ�������
	 * 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.exp.MyDelegator();
	}

	/**
	 * ע���Զ��尴ť
	 */
	protected void initPrivateButton() {
		TestSendBtn btn=new TestSendBtn();
		addPrivateButton(btn.getButtonVO());
		TestRecBtn btn1=new TestRecBtn();
		addPrivateButton(btn1.getButtonVO());
		int[] cardButns = getUIControl().getCardButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		for (int i = 0; i < cardButns.length; i++) {
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Commit)
				hasCommit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Audit)
				hasAudit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
				hasCancelAudit = true;
		}
		if (hasCommit) {
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance().build(nc.ui.trade.button.IBillButton.Commit);
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}

		if (hasAudit) {
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance().build(nc.ui.trade.button.IBillButton.Audit);
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}

		if (hasCancelAudit) {
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance().build(nc.ui.trade.button.IBillButton.CancelAudit);
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);
		}
	}

	/**
	 * ע��ǰ̨У����
	 */
	public Object getUserObject() {
		return new MessageClientUICheckRuleGetter();
	}

	public void doQueryAction(ILinkQueryData querydata) {
		String billId = querydata.getBillID();
		if (billId != null) {
			try {
				getBufferData().addVOToBuffer(loadHeadData(billId));
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
