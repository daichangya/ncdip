package nc.ui.dip.datalook;

import nc.ui.dip.buttons.AddBtn;
import nc.ui.dip.buttons.DataClearBtn;
import nc.ui.dip.buttons.DatalookQueryBtn;
import nc.ui.dip.buttons.DeleteBtn;
import nc.ui.dip.buttons.SetBtn;
import nc.ui.dip.datalook.DataLookClientUICheckRuleGetter;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.trade.button.ButtonVO;

/**
 * <b> 树卡片型单表体抽象UI类 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * @author 何冰
 * @version tempProject version
 */

public abstract class AbstractDataLookClientUI extends BillTreeCardUI implements ILinkQuery {

	/**
	 * 此方法关联控制器
	 */
	protected ICardController createController() {
		return new DataLookClientUICtrl();
	}

	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类
	 * 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.datalook.MyDelegator();
	}

	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		SetBtn sb=new SetBtn();
		addPrivateButton(sb.getButtonVO());
		//查询定义按钮
		DatalookQueryBtn csqb=new DatalookQueryBtn();
		addPrivateButton(csqb.getButtonVO());
		
		DataClearBtn clear=new DataClearBtn();
		addPrivateButton(clear.getButtonVO());
		AddBtn ad=new AddBtn();
		addPrivateButton(ad.getButtonVO());
		DeleteBtn de=new DeleteBtn();
		addPrivateButton(de.getButtonVO());
		
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
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return new DataLookClientUICheckRuleGetter();
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
