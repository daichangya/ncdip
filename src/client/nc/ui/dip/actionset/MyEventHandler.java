package nc.ui.dip.actionset;

import java.awt.event.ActionListener;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dip.actionset.ActionSetBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;


/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	ActionSetClientUI ui = (ActionSetClientUI)this.getBillUI();
	public MyEventHandler(BillManageUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	protected void onBoSave() throws Exception {
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		String hpk=checkVO.getParentVO().getPrimaryKey();
		if(hpk==null||hpk.length()<=0){
			// write to database
			checkVO = getBusinessAction().save(checkVO,
					getUIController().getBillType(), _getDate().toString(),
					getBillUI().getUserObject(), checkVO);
			getBufferData().clear();
			getBufferData().addVOToBuffer(checkVO);
			getBufferData().setCurrentVO(checkVO);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_actionset_h").setValue(checkVO.getParentVO().getPrimaryKey());
		}else{
			HYPubBO_Client.update((SuperVO) checkVO.getParentVO());
			HYPubBO_Client.deleteByWhereClause(ActionSetBVO.class, ActionSetBVO.PK_ACTIONSET_H+"='"+hpk+"'");
			if(checkVO.getChildrenVO()!=null){
				ActionSetBVO[] bvos;
				if(checkVO.getChildrenVO()!=null&&checkVO.getChildrenVO().length>0){
					bvos=(ActionSetBVO[]) checkVO.getChildrenVO();
					for(ActionSetBVO bvo:bvos){
						bvo.setPk_actionset_h(hpk);
					}
					HYPubBO_Client.insertAry(bvos);
				}
			}
		}

		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	}


	public void onButtonAction(ButtonObject bo) throws Exception {
		int intBtn = Integer.parseInt(bo.getTag());
		long lngTime = System.currentTimeMillis();
		buttonActionBefore(getBillUI(), intBtn);
		switch (intBtn) {
		default:
			break;

		case 1: // '\001'
			if (!getBillUI().isBusinessType().booleanValue()) {
				getBillUI().showHintMessage(
						NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000061"));
				onBoAdd(bo);
				buttonActionAfter(getBillUI(), intBtn);
			}
			break;

		case 3: // '\003'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000067"));
			onBoEdit();
			buttonActionAfter(getBillUI(), intBtn);
			break;

		case 0: // '\0'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000072"));
			onBoSave();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000073")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;

		case 7: // '\007'
			onBoCancel();
			getBillUI().showHintMessage("");
			buttonActionAfter(getBillUI(), intBtn);
			break;
		case 32: // ' '
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000070"));
			onBoDelete();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;

		case 8: // '\b'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000076"));
			onBoRefresh();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000077")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;
		case  IBillButton.AddLine: // ' '
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000070"));
			onBoLineAdd();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;

		case  IBillButton.DelLine: // ' '
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000070"));
			onBoLineDel();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;
		case 30: // ' '
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000070"));
			this.onBoCard();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;

		}
	}


	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
		onrowchange();
	}
	private void onrowchange(){
		int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
		for(int i=0;i<rowcount;i++){
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(i+1, i, "disno");
		}
	}
	@Override
	protected void onBoLineDel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineDel();
		onrowchange();
	}

	@Override
	protected void onBoDelete() throws Exception {
		int row=getBufferData().getCurrentRow();
		if(row<0){
			return;
		}
		int flag = MessageDialog.showOkCancelDlg(ui, "删除", "确定是否删除");
		if(flag==2){
			return;
		}
		HYPubBO_Client.deleteBill(ui.getBufferData().getCurrentVO());
		this.getBufferData().clear();
		ui.getBufferData().updateView();
	}

	public void lodDefaultVo(SuperVO[] vo) {
		try {
			getBufferData().clear();
			addDataToBuffer(vo);
			updateBuffer();
			getBufferData().setCurrentRow(0);
			getBufferData().setCurrentVO(getBufferData().getCurrentVO());
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().sortByColumn("code", true);
			onBoEdit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void show(String fpk) {
		BillItem item1 = getBillCardPanelWrapper().getBillCardPanel().getBodyItem("verifycon_ref");//加工条件
		if (item1 != null) {
			UIRefPane ref1 = (UIRefPane) item1.getComponent();
			if (ref1 != null) { 
				ref1.getUIButton().removeActionListener(ref1.getUIButton().getListeners(ActionListener.class)[0]);
				ref1.getUIButton().addActionListener(new ActionSetActionListener((ActionSetClientUI) getBillUI(),item1,fpk));
				ref1.setAutoCheck(false);
				ref1.setEditable(false);
			}
		}
		BillItem item2 = getBillCardPanelWrapper().getBillCardPanel().getBodyItem("updatecon_ref");//加工条件
		if (item2 != null) {
			UIRefPane ref1 = (UIRefPane) item2.getComponent();
			if (ref1 != null) { 
				ref1.getUIButton().removeActionListener(ref1.getUIButton().getListeners(ActionListener.class)[0]);
				ref1.getUIButton().addActionListener(new ActionSetActionListener((ActionSetClientUI) getBillUI(),item2,fpk));
				ref1.setAutoCheck(false);
				ref1.setEditable(false);
			}
		}
	}
}
