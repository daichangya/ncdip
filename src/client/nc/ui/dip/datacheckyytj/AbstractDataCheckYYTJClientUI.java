package nc.ui.dip.datacheckyytj;

import nc.ui.trade.bill.IListController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.dip.buttons.DataValidateBtn;
import nc.ui.dip.buttons.StartBtn;
import nc.ui.dip.buttons.StopBtn;
import nc.ui.pub.linkoperate.*;
import nc.vo.trade.button.ButtonVO;
import nc.ui.trade.base.IBillOperate;




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
  public abstract class AbstractDataCheckYYTJClientUI extends nc.ui.trade.list.BillListUI implements  ILinkQuery{

	  /**
		 * 此方法关联控制器
		 */
		protected IListController createController() {
			return new DataCheckYYTJClientUICtrl();
		}

	
	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.datacheckyytj.MyDelegator();
	}

	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {		
		int[] listButns = getUIControl().getListButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		for (int i = 0; i < listButns.length; i++) {
			if( listButns[i] == nc.ui.trade.button.IBillButton.Commit )
				hasCommit = true;
			if( listButns[i] == nc.ui.trade.button.IBillButton.Audit )
				hasAudit = true;
			if( listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
				hasCancelAudit = true;
		}
//		int[] cardButns = getUIControl().getCardButtonAry();
//		for (int i = 0; i < cardButns.length; i++) {
//			if( cardButns[i] == nc.ui.trade.button.IBillButton.Commit )
//				hasCommit = true;
//			if( cardButns[i] == nc.ui.trade.button.IBillButton.Audit )
//				hasAudit = true;
//			if( cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
//				hasCancelAudit = true;
//		}		
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
		DataValidateBtn db=new DataValidateBtn();
		addPrivateButton(db.getButtonVO());
	}

	/**
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return new DataCheckYYTJClientUICheckRuleGetter();
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
//	            	setCurrentPanel(BillTemplateWrapper.CARDPANEL);
//	            	AggregatedValueObject vo = loadHeadData(billId);
//	                getBufferData().addVOToBuffer(vo);
//	                setListHeadData(new CircularlyAccessibleValueObject[]{vo.getParentVO()});
	            	getBufferData().addVOToBuffer(loadHeadData(billId));
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}
}
