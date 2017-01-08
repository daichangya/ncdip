package nc.ui.dip.sysregister;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.sysregister.DipSysregisterBVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.sysregister.MyBillVO;
import nc.vo.pub.lang.UFBoolean;

/**
 *
 *该类是一个抽象类，主要目的是生成按钮事件处理的框架
 *@author author
 *@version tempProject version
 */

public class AbstractMyEventHandler extends TreeCardEventHandler{

	public AbstractMyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch(intBtn){
		case IBtnDefine.Stop:
			onBoStop(intBtn);
			break;
		case IBtnDefine.Start:
			onBoStart(intBtn);
			break;
		}
	}
	
	
	public void changeStartStat(boolean arg){
		getSelfUI().getButtonManager().getButton( IBtnDefine.Start).setEnabled(arg);
		getSelfUI().getButtonManager().getButton( IBtnDefine.Stop).setEnabled(arg);
	}

	/**
	 * 功能：启用作用，勾选择分布系统后，表体的分布站点选中后才可以点启动和停止，基它节点不能点启动和停止按钮。
	 * 作者：程莉
	 * 日期: 2011-06-29
	 * */
	private void onBoStart(int intBtn) throws Exception {
		
		SysRegisterClientUI ui=(SysRegisterClientUI)getBillUI();
		MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
		//ytq 增加以下几行，控制一下 2011-07-02
		DipSysregisterHVO hvo = (DipSysregisterHVO)billvo.getParentVO();
		if (null ==hvo.getIsdeploy() ||!hvo.getIsdeploy().equals("Y")){
			ui.showErrorMessage("非分布系统，不需要启动，只有分布系统才可使用该功能！");
			return ;			
		}
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			ui.showErrorMessage("请选择需要操作的站点！");
			return ;
		}
		DipSysregisterBVO[] vos=(DipSysregisterBVO[]) getBillTreeCardUI().getVOFromUI().getChildrenVO();
		UFBoolean bool=vos[row].getIsstop();
//		String pk_sysregister_h=(String)ui.getBillCardPanel().getHeadItem("pk_sysregister_h").getValueObject();
//		DipSysregisterBVO[] vos=(DipSysregisterBVO[]) HYPubBO_Client.queryByCondition(DipSysregisterBVO.class, " pk_sysregister_h='"+pk_sysregister_h+"' and isnull(dr,0)=0  ");
//		String boole=bool.toString();
		if(SJUtil.isNull(bool)||!bool.booleanValue()){
//		for (int  i=0;i<vos.length;i++){
//			String	isstop=vos[i].getIsstop().toString();
//			if(isstop==boole){
				ui.showWarningMessage("该系统已经启用");

				return;
//			}
		}
		vos[row].setIsstop(new UFBoolean(false));
		
//		ui.getBillCardWrapper().getBillCardPanel().getBodyItem("isstop").setEnabled(false);
//		vos[0].setIsstop(new UFBoolean(false));
		HYPubBO_Client.update(vos[row]);
		getSelfUI().getBillCardPanel().getBillModel().setBodyRowVO(
				vos[row],
				row);
		billvo.setChildrenVO(vos);
		getBufferData().addVOToBuffer(billvo);
		getBufferData().setCurrentVO(billvo);
		
//	this.onBoRefresh();
	

	}
	private void onBoStop(int intBtn) throws Exception {
		MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
		SysRegisterClientUI ui=(SysRegisterClientUI)getBillUI();		
		//ytq 增加以下几行，控制一下 2011-07-02
		DipSysregisterHVO hvo = (DipSysregisterHVO)billvo.getParentVO();
		if (null ==hvo.getIsdeploy() || hvo.getIsdeploy().equals("N")){
			ui.showErrorMessage("非分布系统，不需要启动，只有分布系统才可使用该功能！");
			return ;			
		}

		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			ui.showErrorMessage("请选择需要操作的站点！");
			return ;
		}
		DipSysregisterBVO[] vos=(DipSysregisterBVO[]) getBillTreeCardUI().getVOFromUI().getChildrenVO();
		UFBoolean bool=vos[row].getIsstop();
		if(!SJUtil.isNull(bool)&&bool.booleanValue()){
			ui.showWarningMessage("该系统已经停用");

			return;
		}
		vos[row].setIsstop(new UFBoolean(true));
		HYPubBO_Client.update(vos[row]);
		getSelfUI().getBillCardPanel().getBillModel().setBodyRowVO(
				vos[row],
				row);
		

		billvo.setChildrenVO(vos);
		getBufferData().addVOToBuffer(billvo);
		getBufferData().setCurrentVO(billvo);
	}

	private SysRegisterClientUI getSelfUI() {
		// TODO Auto-generated method stub
		return (SysRegisterClientUI) getBillUI();
	}





}