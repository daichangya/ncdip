package nc.ui.dip.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PaConstant;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.control.ControlVO;
import nc.vo.dip.control.MyBillVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.pa.AlertTimeConfig;
import nc.vo.pub.pa.TimingSettingVO;
import nc.vo.uap.scheduler.TimeConfigVO;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{
	String pk_bus;
	String ywlx;
	public void ini(ControlHVO hvo,String pk_bus,String ywlx,String tabls) throws Exception{
		this.pk_bus=pk_bus;
		this.ywlx=ywlx;
		onBoAdd(getButtonManager().getButton(IBillButton.Add));
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);

		getBillCardPanelWrapper().getBillCardPanel().getBillData().setHeaderValueVO(hvo);
		String olddata=tabls;//原始数据：中文名称
//		String tabname=vo.getFirstdata();//原始表名
		String[] old=olddata.split(",");
//		String[] tname=tabname.split(",");

		//查询条件
		String oldstr = "'asd'";
		for(int i=0;i<old.length;i++){
			oldstr =oldstr+",'"+old[i]+"'";
		}

		ControlVO cvo=new ControlVO();
		//清数据 not in 
		HYPubBO_Client.deleteByWhereClause(ControlVO.class, " pk_bus='"+pk_bus+"' and bustype='"+ywlx+"' and nvl(dr,0)=0 and tabcname not in("+oldstr+")");

		ControlVO[] vos=(ControlVO[]) HYPubBO_Client.queryByCondition(ControlVO.class, " pk_bus='"+pk_bus+"' and bustype='"+ywlx+"' and nvl(dr,0)=0 and tabcname in("+oldstr+")");
		int v=vos.length;
		//查询修改前与修改后的有相同的值，把它替换成空，不再插入
		for (int k=0;k<v;k++){
			olddata = olddata.replaceAll(vos[k].getTabcname().toString(), "");
		}					

		if(null!=olddata || olddata.length()>0){
			old=olddata.split(",");
			for(int j=0;j<old.length;j++){
				if (null !=old[j] && old[j].length()>0){
					cvo.setTabname("");//表名
					cvo.setTabcname(old[j]);//中文名称:存主键
					cvo.setBustype(ywlx);
					cvo.setPk_bus(pk_bus);
					cvo.setDr(0);
					cvo.setVdef1(hvo.getBustype());
					cvo.setVdef2(hvo.getCode());
					cvo.setVdef3(hvo.getName());
					HYPubBO_Client.insert(cvo);
				}
			}
			vos=(ControlVO[]) HYPubBO_Client.queryByCondition(ControlVO.class, " pk_bus='"+pk_bus+"' and bustype='"+ywlx+"' and nvl(dr,0)=0  and tabcname in("+oldstr+")");
		}
		for(int i=0;i<vos.length;i++){
			onBoLineAdd();
			getSelfUI().getBillCardPanel().getBillModel().setBodyRowVO(vos[i], i);
		}
		getSelfUI().getBillCardPanel().getBillModel().execLoadFormula();
	}
	private  ControlClientUI warningui ;
	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);	
		this.warningui = (ControlClientUI)billUI;
	}
	private ControlClientUI getSelfUI() {
		return (ControlClientUI) getBillUI();
	}



	@Override
	protected void onBoSave() throws Exception {
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		MyBillVO vo= (MyBillVO)getBillUI().getVOFromUI();
		ControlVO[] bvos=(ControlVO[]) vo.getChildrenVO();
		if(bvos!=null&&bvos.length>0){
			for(int i=0;i<bvos.length;i++){
				ControlVO bvoi=bvos[i];
				if(bvoi.getPrimaryKey()!=null&&bvoi.getPrimaryKey().length()>0){
					HYPubBO_Client.update(bvoi);
				}else{
					HYPubBO_Client.insert(bvoi);
				}
			}
		}
		String hpk="";
		if(hpk!=null&&hpk.length()>0){
			vo.setChildrenVO(HYPubBO_Client.queryByCondition(ControlVO.class, " pk_bus='"+pk_bus+"' and bustype='"+ywlx+"' and nvl(dr,0)=0"));
			getBufferData().clear();
			addDataToBuffer(new SuperVO[]{(SuperVO) vo.getParentVO()});
			updateBuffer();
			getBufferData().setCurrentRow(0);
			getBufferData().setCurrentVO(getBufferData().getCurrentVO());

		}
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	}

	public void lodDefaultVo(SuperVO[] vo) {
		try {
			getBufferData().clear();
			addDataToBuffer(vo);
			updateBuffer();
			getBufferData().setCurrentRow(0);
			getBufferData().setCurrentVO(getBufferData().getCurrentVO());
		} catch (Exception e) {
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
	protected void onBoEdit() throws Exception {
		try {
			getSelfUI().setBillOperate(IBillOperate.OP_EDIT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}