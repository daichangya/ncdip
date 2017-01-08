package nc.ui.dip.contdatawh;

import nc.ui.dip.buttons.BContSysQueryBtn;
import nc.ui.dip.buttons.ContSaveBtn;
import nc.ui.dip.buttons.ContSysQueryBtn;
import nc.ui.dip.buttons.ContresutBtn;
import nc.ui.dip.buttons.DataCheck;
import nc.ui.dip.buttons.DataClearBtn;
import nc.ui.dip.buttons.EditBtn;
import nc.ui.dip.buttons.ExportModelBtn;
import nc.ui.dip.buttons.SetBtn;
import nc.ui.dip.buttons.ValidateCheckBtn;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.vo.pub.AggregatedValueObject;
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
public abstract class AbstractContdatawhClientUI extends BillTreeManageUI implements  ILinkQuery{

	protected AbstractTreeManageController  createController() {
		return new ContDataWHClientUICtrl();
	}

	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.contdatawh.MyDelegator();
	}

	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		
		ValidateCheckBtn vcb=new ValidateCheckBtn();
		addPrivateButton(vcb.getButtonVO());
		
		DataClearBtn dcb=new DataClearBtn();
		addPrivateButton(dcb.getButtonVO());
		
		ExportModelBtn emb=new ExportModelBtn();
		addPrivateButton(emb.getButtonVO());
		
		//2011-5-14
		ContSysQueryBtn csqb=new ContSysQueryBtn();
		addPrivateButton(csqb.getButtonVO());
		
		BContSysQueryBtn bcsqb=new BContSysQueryBtn();
		addPrivateButton(bcsqb.getButtonVO());
		
		ContSaveBtn contSave = new ContSaveBtn();
		addPrivateButton(contSave.getButtonVO());
		
		ContresutBtn conresult = new ContresutBtn();
		addPrivateButton(conresult.getButtonVO());
		
		//2011-6-3 增加自定义 "设置" 按钮
		SetBtn sb=new SetBtn();
		addPrivateButton(sb.getButtonVO());
		EditBtn editbtn = new EditBtn();
		
		addPrivateButton(editbtn.getButtonVO());
		
		DataCheck dataCheck=new DataCheck();
		addPrivateButton(dataCheck.getButtonVO());
		
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
	}

	/**
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return new ContDataWHClientUICheckRuleGetter();
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
