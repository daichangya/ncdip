package nc.ui.dip.dataverify;

import java.awt.event.ActionListener;

import nc.ui.dip.dataproce.DataProcActionListener;
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
import nc.vo.dip.dataverify.DataverifyBVO;
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

	DataVerifyClientUI ui = (DataVerifyClientUI)this.getBillUI();
	public MyEventHandler(BillManageUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
//		BillData bd = getBillCardPanelWrapper().getBillCardPanel()
//		.getBillData();
//		if (bd != null) {
//		bd.dataNotNullValidate();
//		}
//		// 表头重复校验
//		DataVerifyClientUI ui = (DataVerifyClientUI) getBillUI();
//		// String
//		// pk=(String)ui.getBillCardPanel().getHeadItem("pk_returnmess_h").getValueObject();
//		String pk = (String) ui.getBillCardPanel().getHeadItem(
//		"pk_dataverify_h").getValueObject();
//		// pk_dataformatdef_h
//		if (pk == null || pk.trim().equals("")) {
//		pk = " ";
//		}
//		String code = (String) ui.getBillCardPanel().getHeadItem("code")
//		.getValueObject();
//		if (code.length() != 4) {
//		ui.showErrorMessage("表头编码为4位！");

//		}
//		String name = (String) ui.getBillCardPanel().getHeadItem("name")
//		.getValueObject();
//		String bcode = (String) ui.getBillCardWrapper().getBillCardPanel()
//		.getBodyItem("code").getValueObject();
//		if (bcode.length() != 4) {
//		ui.showWarningMessage("表体编码为4位！");
//		return;
//		}

//		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
//		IUAPQueryBS.class.getName());

//		Collection ccode = bs
//		.retrieveByClause(DataverifyHVO.class, "code='" + code
//		+ "' and nvl(dr,0)=0 and pk_dataverify_h <>'" + pk
//		+ "'");
//		if (ccode != null) {
//		if (ccode.size() >= 1) {
//		ui.showWarningMessage("该【" + code + "】编码已经存在！");
//		return;
//		}
//		}
//		Collection cname = bs
//		.retrieveByClause(DataverifyHVO.class, "name='" + name
//		+ "' and nvl(dr,0)=0 and pk_dataverify_h <>'" + pk
//		+ "'");
//		if (cname != null) {
//		if (cname.size() >= 1) {
//		ui.showWarningMessage("该【" + name + "】名称已经存在！");
//		return;
//		}
//		}
//		super.onBoSave();
//		获得界面原始数据的vo
		/*AggregatedValueObject billVO = this.getBillUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);

		//获得界面新数据vo
		AggregatedValueObject checkVO = this.getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		Object o = null;
		ISingleController sCtrl = null;

		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether())
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		else
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);

		if (sCtrl != null && sCtrl.isSingleDetail())
			billVO.setParentVO((CircularlyAccessibleValueObject) o);
		int nCurrentRow = -1;
		if (isSave) {
			//修改
			if (isEditing())
				if (getBufferData().isVOBufferEmpty()) {	
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;
				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}

			setAddNewOperate(isAdding(), billVO);

		}
		// 退出保存，恢复浏览状态
		setSaveOperateState();
		if (nCurrentRow >= 0)
			getBufferData().setCurrentRow(nCurrentRow);
		 */
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
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_dataverify_h").setValue(checkVO.getParentVO().getPrimaryKey());
		}else{
			HYPubBO_Client.update((SuperVO) checkVO.getParentVO());
			HYPubBO_Client.deleteByWhereClause(DataverifyBVO.class, DataverifyBVO.PK_DATAVERIFY_H+"='"+hpk+"'");
			if(checkVO.getChildrenVO()!=null){
				DataverifyBVO[] bvos;
				if(checkVO.getChildrenVO()!=null&&checkVO.getChildrenVO().length>0){
					bvos=(DataverifyBVO[]) checkVO.getChildrenVO();
					for(DataverifyBVO bvo:bvos){
						bvo.setPk_dataverify_h(hpk);
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
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub

		String pk_dataverify_h = ui.getBillCardPanel().getHeadItem("pk_dataverify_h").getValueObject()==null?"":ui.getBillCardPanel().getHeadItem("pk_dataverify_h").getValueObject().toString();
		if("".equals(pk_dataverify_h)){
			super.onBoAdd(bo);
			String pk = ui.tableMap.get("pk")==null?"":ui.tableMap.get("pk").toString();
			String type = ui.tableMap.get("type")==null?"":ui.tableMap.get("type").toString();
			ui.getBillCardPanel().getHeadItem("vdef1").setValue(pk);
			ui.getBillCardPanel().getHeadItem("vdef2").setValue(type);
		}else{
			MessageDialog.showWarningDlg(ui, "提示", "同一节点下只能增加一条数据！！！");
		}


	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoDelete();
//		DataverifyHVO hvo = (DataverifyHVO)ui.getBufferData().getCurrentVO().getParentVO();
//		DataverifyBVO bvo[] = (DataverifyBVO[])ui.getBufferData().getCurrentVO().getChildrenVO();
//		HYPubBO_Client.delete(hvo);
		//若表体为空，return ，否则执行删除操作 不return 报bo异常  2011-07-04 zlc
//		int rows=this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		int row=getBufferData().getCurrentRow();
		if(row<0){
//			getBillUI().showHintMessage("表体为空不可删除!");
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
			onBoEdit();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void show(String fpk) {
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().showTableCol("verifycon");//.getBodyItem("verifycon")..setShow(true);//.setEnabled(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().showTableCol("vdef3");//.getBodyItem("verifycon")..setShow(true);//.setEnabled(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("verifycon").setLength(200);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vdef3").setLength(200);
		BillItem item1 = getBillCardPanelWrapper().getBillCardPanel().getBodyItem("verifycon");//加工条件
		if (item1 != null) {
			UIRefPane ref1 = (UIRefPane) item1.getComponent();

			if (ref1 != null) { 
				ref1.getUIButton().removeActionListener(ref1.getUIButton().getListeners(ActionListener.class)[0]);
				ref1.getUIButton().addActionListener(new DataVeryActionListener((DataVerifyClientUI) getBillUI(),item1,fpk));
				ref1.setAutoCheck(false);
				ref1.setEditable(false);
			}
		}
		
	}
}
