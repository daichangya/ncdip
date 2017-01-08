package nc.ui.dip.authorize.role;

import nc.ui.dip.buttons.RelationSetBtn;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
  public abstract class AbstractRoleGroupUI extends BillTreeCardUI implements  ILinkQuery{

	protected ICardController  createController() {
		return new RoleGroupUICtrl();
	}
	
	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.tabstatus.MyDelegator();
	}

	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Commit )
				hasCommit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Audit )
				hasAudit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
				hasCancelAudit = true;
		}		
		if( hasCommit ){
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance()
			.build(nc.ui.trade.button.IBillButton.Commit);
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}
		
		if( hasAudit ){
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance()
				.build(nc.ui.trade.button.IBillButton.Audit);
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}
		
		if( hasCancelAudit ){
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance()
			.build(nc.ui.trade.button.IBillButton.CancelAudit);
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);	
		}	
		addPrivateButton(new RelationSetBtn().getButtonVO());
	}

	/**
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return new RoleGroupUICheckRuleGetter();
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
	            	AggregatedValueObject vo = loadHeadData(billId);
	                getBufferData().addVOToBuffer(vo);
	                setListHeadData(new CircularlyAccessibleValueObject[]{vo.getParentVO()});
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}
}
