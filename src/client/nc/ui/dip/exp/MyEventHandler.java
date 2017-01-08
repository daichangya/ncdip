package nc.ui.dip.exp;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.vo.dip.exp.MyBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * 
 * 卡片型单表体,
 * 该类是AbstractMyEventHandler抽象类的实现类， 
 * 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author 何冰
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	/**
	 * 改方法在增行和插行后被调用，可以在该方法中为新增的行设置一些默认值
	 * @param newBodyVO
	 * @return TODO
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {
		
		newBodyVO.setAttributeValue("pk_corp", _getCorp().getPk_corp());

		// 添加结束
		return newBodyVO;
	}
	
	 public void lodDefaultVo(SuperVO[] vo) {
			try {
//				getBufferData().clear();
				MyBillVO mybillvo=new MyBillVO();
				mybillvo.setChildrenVO(vo);
				getBufferData().setCurrentVO(mybillvo);
				
				updateBuffer();
				getBufferData().setCurrentRow(0);
				getBufferData().setCurrentVO(getBufferData().getCurrentVO());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		case 11: onBoLineAdd();break;
		case IBillButton.DelLine:onBoLineDel();break;
			
		
		}
	}


}