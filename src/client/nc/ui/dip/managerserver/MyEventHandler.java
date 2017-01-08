package nc.ui.dip.managerserver;

import java.util.Iterator;

import nc.bs.dip.fun.FormmulaCache;
import nc.bs.dip.fun.YzFormulaParse;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.IServerBufferManage;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.vo.dip.managerserver.DipManagerserverVO;
import nc.vo.pub.AggregatedValueObject;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillListUI billUI, IListController control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void onBoQuery() throws Exception {
//		// TODO Auto-generated method stub
//		super.onBoQuery();
		getBufferData().clear();
		IServerBufferManage isb=(IServerBufferManage) NCLocator.getInstance().lookup(IServerBufferManage.class.getName());
		DipManagerserverVO[] vos=isb.getServerBufferManage();
		if(vos==null||vos.length<=0){
			getSelfUI().showErrorMessage("没有符合条件的数据！");
			return ;
		}
//		 增加数据到Buffer
		addDataToBuffer(vos);

		updateBuffer();
		getBufferData().setCurrentRow(0);
		getBufferData().setCurrentVO(getBufferData().getCurrentVO());
	}

	
	public void changeStartStat(boolean arg){

		getSelfUI().getButtonManager().getButton( IBtnDefine.Start).setEnabled(arg);
		getSelfUI().getButtonManager().getButton( IBtnDefine.Stop).setEnabled(arg);
		
	}


	/*
	 * 增加启用、停止按钮事件
	 * 2011-5-18
	 * zlc*/
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		switch(intBtn){
		case IBtnDefine.Start:
			onBoStart();
			break;
		case IBtnDefine.Stop:
			onBoStop();
			break;
		case IBtnDefine.CLEARCACHE:
			onClearCahce();
		}
		super.onBoElse(intBtn);
	}
	
	public void onBoStart() throws Exception{
		doSratrOrStropService(true);
		onBoRefresh();
		/*
		ManagServerClientUI ui=(ManagServerClientUI)getBillUI();
		AggregatedValueObject vobj =getBufferData().getCurrentVO();
		DipManagerserverVO msvo;
		if(vobj!=null){
			msvo=(DipManagerserverVO) vobj.getParentVO();
		}else{
			this.getSelfUI().showErrorMessage("请选择一条记录！") ;
			return;
		}
		String mstatus=msvo.getMstatus();
		if(!SJUtil.isNull(mstatus)&&"运行".equals(mstatus)){
			ui.showWarningMessage("该系统已经启用！");
			return;
		}
		msvo.setMstatus("运行");
		HYPubBO_Client.update(msvo);
		vobj.setParentVO(HYPubBO_Client.queryByPrimaryKey(DipManagerserverVO.class, msvo.getPrimaryKey()));
		getBufferData().addVOToBuffer(vobj);
		getBufferData().setCurrentVO(vobj);
		int row=getBufferData().getCurrentRow();
		getSelfUI().getBillListPanel().getHeadBillModel().setValueAt("运行", row, "mstatus");
		
	*/}
	private void doSratrOrStropService(boolean isStart){
		AggregatedValueObject billvo=getBufferData().getCurrentVO();
		ManagServerClientUI ui=(ManagServerClientUI)getBillUI();
		DipManagerserverVO msvo;/*=(DipManagerserverVO)billvo.getParentVO()*/
		if(billvo!=null){
			msvo=(DipManagerserverVO)billvo.getParentVO();
		}else{
			this.getSelfUI().showErrorMessage("请选择一条记录！");
			return;
		}
		if(!msvo.isIsmsg()){
			getSelfUI().showErrorMessage("请选择消息类的服务控制！");
		}
//		String mstatus=msvo.getMstatus();
		IServerBufferManage isb=(IServerBufferManage) NCLocator.getInstance().lookup(IServerBufferManage.class.getName());
		isb.startOrStopMsgServer(msvo.getPrimaryKey(), isStart);
	}
	public void onBoStop() throws Exception{
		doSratrOrStropService(false);
		onBoRefresh();
		/*AggregatedValueObject billvo=getBufferData().getCurrentVO();
		ManagServerClientUI ui=(ManagServerClientUI)getBillUI();
		DipManagerserverVO msvo;=(DipManagerserverVO)billvo.getParentVO()
		if(billvo!=null){
			msvo=(DipManagerserverVO)billvo.getParentVO();
		}else{
			this.getSelfUI().showErrorMessage("请选择一条记录！");
			return;
		}
		String mstatus=msvo.getMstatus();
		if(!SJUtil.isNull(mstatus)&&"停止".equals(mstatus)){
			ui.showWarningMessage("该系统已经停止！");
			return;
		}
		msvo.setMstatus("停止");
		HYPubBO_Client.update(msvo);
		billvo.setParentVO(HYPubBO_Client.queryByPrimaryKey(DipManagerserverVO.class, msvo.getPrimaryKey()));
		getBufferData().addVOToBuffer(billvo);
		getBufferData().setCurrentVO(billvo);
		int row=getBufferData().getCurrentRow();
		getSelfUI().getBillListPanel().getHeadBillModel().setValueAt("停止", row, "mstatus");*/
	}
	
	private ManagServerClientUI getSelfUI() {
		// TODO Auto-generated method stub
		return (ManagServerClientUI) getBillUI();
	}

	@Override
	protected void onBoCancel() throws Exception {
		super.onBoCancel();
		changeStartStat(true);
		super.onBoRefresh();
	}


	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		//super.onBoRefresh();
		onBoQuery();
	}
	
	protected void onClearCahce() throws Exception {
		IQueryField iq=(IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		int k=iq.getCountFormmulaCacheValue();//清除缓存前，缓存中已经缓存了k个值。
		iq.clearFormmulaCache();
		int m=iq.getCountFormmulaCacheValue();//清除缓存后，缓存中已经缓存了k个值。
		if(m==0){
			getSelfUI().showWarningMessage("清除缓存成功！一共清除了"+k+"个值！");
		}
	}
	
	
	
}