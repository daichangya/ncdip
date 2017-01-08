package nc.ui.dip.warningset;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PaConstant;
import nc.itf.dip.pub.ITaskExecute;
import nc.itf.dip.pub.ITaskManage;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.dlg.warn.WarnTimingDlg;
import nc.ui.dip.dlg.warntime.WarTimeDLG;
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
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetDayTimeVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.dip.warningset.MyBillVO;
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

	private  WarningSetClientUI warningui ;
	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);	
		this.warningui = (WarningSetClientUI)billUI;
	}
	private WarningSetClientUI getSelfUI() {
		return (WarningSetClientUI) getBillUI();
	}


	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.WARNTIME:
			onBoWarnTime();
			break;
		}
	}

	@Override
	protected void onBoSave() throws Exception {
//		super.onBoSave();
//		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
//		if(bd!=null){
//		bd.dataNotNullValidate();

//		}		
		/*BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}*/
		ITaskManage manage = (ITaskManage)NCLocator.getInstance().lookup(ITaskManage.class.getName());
		MyBillVO vo= (MyBillVO)getBillUI().getVOFromUI();
		RetMessage msg=manage.saveOrUpdateWarnSet(vo);
		if(!msg.getIssucc()){
			getSelfUI().showErrorMessage("保存失败！"+msg.getMessage());
			throw new Exception("保存失败！"+msg.getMessage());
//			return ;
		}
//		得到禁用启用状态
		DipWarningsetVO hvo=(DipWarningsetVO)vo.getParentVO();
		hvo.setPrimaryKey(msg.getMessage());
		String isProp=hvo.getIsnotwarning();
		
		if(isProp!=null&&isProp.equals("可用")){
			RetMessage isp=manage.startOrStopWarn(msg.getMessage(), true);
			if(!isp.getIssucc()){
				getSelfUI().showErrorMessage("保存成功，但是修改启用状态失败！");
				throw new Exception("保存成功，但是修改启用状态失败！");
			}
		}
		String hpk=msg.getMessage();
		if(hpk!=null&&hpk.length()>0){
			vo.setParentVO(HYPubBO_Client.queryByPrimaryKey(DipWarningsetVO.class, hpk));
			vo.setChildrenVO(HYPubBO_Client.queryByCondition(DipWarningsetBVO.class, "pk_warningset='"+hpk+"' and nvl(dr,0)=0"));
			getBufferData().clear();
			addDataToBuffer(new SuperVO[]{(SuperVO) vo.getParentVO()});
			updateBuffer();
			getBufferData().setCurrentRow(0);
			getBufferData().setCurrentVO(getBufferData().getCurrentVO());

		}
		warningui.setBillOperate(IBillOperate.OP_NOTEDIT);
		
		if(!SJUtil.isNull(vo)&&!SJUtil.isNull(vo.getParentVO())){
			String isnotwarning=hvo.getIsnotwarning();//是否启用预警
			String isnottiming=hvo.getIsnottiming();//是否即时
			RetMessage rm=null;
			if(!SJUtil.isNull(isnottiming)&&isnottiming.equals("即时")&&!SJUtil.isNull(isnotwarning)&&isnotwarning.equals("可用")){
				ITaskExecute idw=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
				if(idw.isFitWarnCondition(hvo.getPrimaryKey())){
					rm=idw.doTaskAtOnce(hvo.getPrimaryKey());
					if(rm!=null&&rm.getIssucc()){
						MessageDialog.showHintDlg(getSelfUI(), "预警", "执行成功");
					}else{
						getSelfUI().showErrorMessage("执行失败！"+rm.getMessage());
					}
				}
			}
		}
		Object time=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("nexttime").getValueObject();
		if(time!=null&&time.toString().length()>0){
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_timeset", time);
		}else{
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_timeset", "未定义");
		}
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);

	}
	public void onBoWarnTime() throws Exception{
		MyBillVO vo= (MyBillVO) getSelfUI().getVOFromUI();
		if(SJUtil.isNull(vo)||SJUtil.isNull(vo.getParentVO())){
		}else{
//			DipWarningsetVO hvo=(DipWarningsetVO) vo.getParentVO();
//			String pkhvo=hvo.getPrimaryKey();
//			String pkdtvo=hvo.getWartime();
//			DipWarningsetDayTimeVO timvo;
//			boolean issave=false;
			/*if(SJUtil.isNull(pkdtvo)||pkdtvo.length()==0){
				issave=true;
				timvo=null;
			}else{
				timvo=(DipWarningsetDayTimeVO) HYPubBO_Client.queryByPrimaryKey(DipWarningsetDayTimeVO.class, pkdtvo);
				if(SJUtil.isNull(timvo)){
					issave=true;
				}
			}*/
			/*WarTimeDLG wdl=new WarTimeDLG(getSelfUI(),timvo);
			wdl.showModal();
			timvo=wdl.getDTVO();
			if(!SJUtil.isNull(timvo)){
				if(issave){
					String pk=HYPubBO_Client.insert(timvo);
					getSelfUI().getBillCardPanel().setHeadItem("wartime", pk);
				}else{
					HYPubBO_Client.update(timvo);
				}
			}*/
			AlertTimeConfig atc = null;
			Object ob=getSelfUI().getBillCardPanel().getHeadItem("wartime").getValueObject();
			if(ob==null||ob.toString().length()<=0){
				
			}else{
				String wartime=ob.toString();
				TimingSettingVO tvo=(TimingSettingVO) HYPubBO_Client.queryByPrimaryKey(TimingSettingVO.class, wartime);
				if(tvo!=null){
					atc=PaConstant.transTimingSettingVO2AlertTimeConfig(tvo);
				}
			}
			WarnTimingDlg dlg=new WarnTimingDlg(getSelfUI());
			if(atc!=null){
				dlg.setAlertTime(atc);
			}
			dlg.showModal();
			if (dlg.getResult() == UIDialog.ID_OK) {
				atc = dlg.getAlertTime();
				if(atc!=null){
					TimingSettingVO tsvo=PaConstant.transAlertTimeConfig2TimingSettingVO(atc);
					if(tsvo.getPrimaryKey()!=null&&tsvo.getPrimaryKey().length()>0){
						HYPubBO_Client.update(tsvo);
					}else{
						String warn=HYPubBO_Client.insert(tsvo);
						getSelfUI().getBillCardPanel().getHeadItem("wartime").setValue(warn);
					}
					TimeConfigVO tcvo=PaConstant.transTimingSettingVO2TimeConfigVO(tsvo);
					if(tcvo!=null){
						tcvo.resume();
						Long time=tcvo.getScheExecTime();
						String times=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("nexttime", times);
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_timeset").setValue(times);
						System.out.println(times);
					}
				}
			}

		}
	}

	public void add(){
		try {
			getBillUI().setBillOperate(IBillOperate.OP_ADD);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	@Override
	protected void onBoLinePaste() throws Exception {
		super.onBoLinePaste();
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		getSelfUI().getBillCardPanel().setBodyValueAt(null, row,new DipWarningsetBVO().getPKFieldName());

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
			MyBillVO billvo=(MyBillVO) getBillCardPanelWrapper().getBillVOFromUI();
			if(billvo!=null&&billvo.getParentVO()!=null){
				DipWarningsetVO hvo=(DipWarningsetVO) billvo.getParentVO();
				if(hvo.getTasktype()!=null&&hvo.getTasktype().equals("0001AA1000000001FVAD")){
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isnotwarning").setEdit(true);
				}else{
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isnotwarning").setEdit(false);
				}
			}
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
			rewriteOrderNo();
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
			rewriteOrderNo();
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

		case 119:
			getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000070"));
			this.onBoWarnTime();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;
		/*case 31:
			getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000070"));
			this.onBoReturn();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;*/
		}
	}
	private void rewriteOrderNo() {
		int rowCount=getSelfUI().getBillCardPanel().getBillModel().getRowCount();
		if(rowCount>0){
			for(int i=0;i<rowCount;i++){
				getSelfUI().getBillCardPanel().setBodyValueAt(i+1, i, "orderno");
			}
		}
	}

}