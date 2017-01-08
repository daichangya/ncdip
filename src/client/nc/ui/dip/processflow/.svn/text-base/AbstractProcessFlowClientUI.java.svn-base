package nc.ui.dip.processflow;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.dip.buttons.LCLineBtn;
import nc.ui.dip.buttons.MoveddownBtn;
import nc.ui.dip.buttons.MovedupBtn;
import nc.ui.dip.buttons.ProcessFlowBtn;
import nc.ui.dip.buttons.YuJingBtn;
import nc.ui.dip.buttons.folder.AddFolderBtn;
import nc.ui.dip.buttons.folder.DeleteFolderBtn;
import nc.ui.dip.buttons.folder.EditFolderBtn;
import nc.ui.dip.buttons.folder.FolderManageBtn;
import nc.ui.dip.buttons.folder.MoveFolderBtn;
import nc.ui.dip.buttons.line.LCLineAddBtn;
import nc.ui.dip.buttons.line.LCLinecopyBtn;
import nc.ui.dip.buttons.line.LCLinedelBtn;
import nc.ui.dip.buttons.line.LCLineinsBtn;
import nc.ui.dip.buttons.line.LCLinepastBtn;
import nc.ui.dip.processflow.ProcessFlowClientUICheckRuleGetter;
import nc.ui.pub.linkoperate.*;
import nc.vo.trade.button.ButtonVO;
import nc.vo.pub.AggregatedValueObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.treecard.BillTreeCardUI;




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
  public abstract class AbstractProcessFlowClientUI extends BillTreeCardUI implements  ILinkQuery{

	protected ICardController createController() {
		return new ProcessFlowClientUICtrl();
	}
	
	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.processflow.MyDelegator();
	}

	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		addPrivateButton(new LCLineBtn().getButtonVO());
	    addPrivateButton(new MoveddownBtn().getButtonVO());
	    addPrivateButton(new MovedupBtn().getButtonVO());
		addPrivateButton(new LCLineAddBtn().getButtonVO());
		addPrivateButton(new LCLinecopyBtn().getButtonVO());
		addPrivateButton(new LCLinedelBtn().getButtonVO());
		addPrivateButton(new LCLineinsBtn().getButtonVO());
		addPrivateButton(new LCLinepastBtn().getButtonVO());
		addPrivateButton(new MoveFolderBtn().getButtonVO());
		
		
		
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
		/**	chengli 2011-4-12 自定义流程定义、预警按钮注册 begin	 */
		ProcessFlowBtn pfbtn=new ProcessFlowBtn();
		addPrivateButton(pfbtn.getButtonVO());
		//预警按钮
		YuJingBtn yjbtn=new YuJingBtn();
		addPrivateButton(yjbtn.getButtonVO());
		/**  end */
		
		addPrivateButton(new FolderManageBtn().getButtonVO());
		addPrivateButton(new AddFolderBtn().getButtonVO());
		addPrivateButton(new EditFolderBtn().getButtonVO());
		addPrivateButton(new DeleteFolderBtn().getButtonVO());
	}

	/**
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return new ProcessFlowClientUICheckRuleGetter();
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
	            	AggregatedValueObject vo = loadHeadData(billId);
	                getBufferData().addVOToBuffer(vo);
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}
}
