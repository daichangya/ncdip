package nc.ui.dip.alertmanag;


import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ITaskManage;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.warningset.WarningsetDlg;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.list.BillListUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.control.ControlVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;


/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
/*依据列表但表头修改此类：只修改了构造方法
 * 2011-5-14
 * zlc
 * */
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{
	  //查询时,将系统默认加载的and (isnull(dr,0)=0) and pk_corp='100X'查询条件去掉
		protected String getHeadCondition() {
			return null;
		}
	public MyEventHandler(/*BillManageUI*/
			BillListUI billUI, /*IControllerBase*/IListController control){
		super(billUI,control);		
	}
	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private AlertManagClientUI getSelfUI() {
		return (AlertManagClientUI) getBillUI();
	}
	/*
	 * 增加禁用、启用按钮事件
	 * 2011-5-19
	 * */
	private void onStartOrStrop(boolean isStart){
		try {
			AggregatedValueObject vo=getBufferData().getCurrentVO();
			DipWarningsetVO hvo;
			if(vo!=null){
				hvo=(DipWarningsetVO)vo.getParentVO();
			}else{
				getSelfUI().showErrorMessage("请选择一条记录！");
				return ;
			}
			ITaskManage itm=(ITaskManage) NCLocator.getInstance().lookup(ITaskManage.class.getName());
			RetMessage msg=itm.startOrStopWarn(hvo.getPrimaryKey(), isStart);
			if(!msg.getIssucc()){
				getSelfUI().showErrorMessage(msg.getMessage());
				return;
			}
			vo.setParentVO(HYPubBO_Client.queryByPrimaryKey(DipWarningsetVO.class, hvo.getPrimaryKey()));
			getBufferData().addVOToBuffer(vo);
			getBufferData().setCurrentVO(vo);
			//int row=getBufferData().getCurrentRow();
			//getSelfUI().getBillListPanel().getHeadBillModel().setValueAt(isStart?"可用":"禁用", row, "isnotwarning");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onBoDisable(int intBtn) {
		
		onStartOrStrop(false);
		try {
			onBoRefresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onBoEnable(int intBtn) {
		onStartOrStrop(true);
		try {
			onBoRefresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
		
	}    
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.YuJing://预警
			yuJing();
			break;
		case IBtnDefine.CONTROL://模板，暂时不用了
			onBoControl();
			break;
		case IBtnDefine.Clean://模板，暂时不用了
			onBoClean();
			break;
		}
	}
	private void onBoClean() throws Exception {
			AggregatedValueObject vo=getBufferData().getCurrentVO();
			DipWarningsetVO hvo;
			if(vo!=null){
				hvo=(DipWarningsetVO)vo.getParentVO();
			}else{
				getSelfUI().showErrorMessage("请选择一条记录！");
				return ;
			}
			if(hvo.getTabstatus()!=null&&!hvo.getTabstatus().equals("运行")){
				return;
			}else {
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否强制更改此预警任务状态？");
				if(flag!=1){
					return;
				}
			}
			ITaskManage itm=(ITaskManage) NCLocator.getInstance().lookup(ITaskManage.class.getName());
			itm.updatTaskstate(hvo, ITaskManage.TASKSTATE_STROP);
			vo.setParentVO(HYPubBO_Client.queryByPrimaryKey(DipWarningsetVO.class, hvo.getPrimaryKey()));
			getBufferData().addVOToBuffer(vo);
			getBufferData().setCurrentVO(vo);
			onBoRefresh();
			//int row=getBufferData().getCurrentRow();
			//getSelfUI().getBillListPanel().getHeadBillModel().setValueAt(isStart?"可用":"禁用", row, "isnotwarning");
			
	}
	public void onBoControl(){
		nc.vo.dip.alertmanag.MyBillVO vo=(nc.vo.dip.alertmanag.MyBillVO) getBufferData().getCurrentVO();
		if(vo==null){
			getSelfUI().showErrorMessage("请选择！");
			return;
		}
		DipWarningsetVO hvo=(DipWarningsetVO) vo.getParentVO();

		ControlVO[] bvos = null;
		try {
			bvos = (ControlVO[]) HYPubBO_Client.queryByCondition(ControlVO.class, "pk_bus='"+hvo.getPk_bustab()+"'");
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bvos==null||bvos.length<=0){
			getSelfUI().showErrorMessage("没有设置！");
			return;
		}
		String ss="";
		for(int i=0;i<bvos.length;i++){
			ss=ss+bvos[i].getTabcname()+",";
		}
		ControlHVO chvo=new ControlHVO();
		chvo.setBustype(bvos[0].getVdef1());
		chvo.setCode(bvos[0].getVdef2());
		chvo.setName(bvos[0].getVdef3());

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPk_bustab(),bvos[0].getBustype(),ss.substring(0,ss.length()-1),false);
		cd.showModal();
	}
	public void yuJing() throws UifException{
		
		nc.vo.dip.alertmanag.MyBillVO vo=(nc.vo.dip.alertmanag.MyBillVO) getBufferData().getCurrentVO();
		
		nc.vo.dip.warningset.MyBillVO bill=new nc.vo.dip.warningset.MyBillVO();
	
		if(vo==null){
			getSelfUI().showErrorMessage("请选择！");
			return;
		}
		DipWarningsetVO hvo=(DipWarningsetVO) vo.getParentVO();

		DipWarningsetBVO [] bvo=(DipWarningsetBVO[]) HYPubBO_Client.queryByCondition(DipWarningsetBVO.class, "pk_warningset='"+hvo.getPrimaryKey()+"' and nvl(dr,0)=0");
		bill.setChildrenVO(bvo);
		bill.setParentVO(hvo);
		WarningsetDlg wd=new WarningsetDlg(getSelfUI(),bill, false,null,3);		
		wd.showModal();		
//		new WarningSetClientUI().init(bill, isadd,node.getParentnodeID().toString(),3);
		
		try {
			onBoRefresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 @Override
		protected void onBoRefresh() throws Exception {
			  SuperVO[] queryVos = queryHeadVOs(" tasktype='0001AA1000000001FVAD' and nvl(dr,0)=0 ");
				getBufferData().clear();
				// 增加数据到Buffer
				addDataToBuffer(queryVos);
				updateBuffer();
				if(getSelfUI().getBillListPanel().getHeadTable().getRowCount()>0){
					getSelfUI().getBillListPanel().getHeadTable().changeSelection(0, 0, false, false);	
				}
				
		}
	
	
}