package nc.ui.dip.processflow;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IExAggVO;

/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler 
                                          extends TreeCardEventHandler{

        public AbstractMyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}
        private   CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo){
        	 CircularlyAccessibleValueObject childVos[]=null;
        	 if(retVo instanceof IExAggVO)
        		 childVos=((IExAggVO)retVo).getAllChildrenVO();
        		
        	 else 
        		 childVos=retVo.getChildrenVO();
        	 return childVos;
        	
        }
   
   
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.addFolderBtn:
			onBoAddFolder();
			break;
		case IBtnDefine.editFolderBtn:
			onBoEditFolder();
			break;
		case IBtnDefine.delFolderBtn:
			onBoDeleteFolder();
			break;
		}
		
		
						/*switch(intBtn){
				//		case IBtnDefine.YuJing:
				//			onBoWarning(intBtn);
				//			break;
						case IBtnDefine.ProcessFlow:
							ProcessFlow(intBtn);
							
							
						}
						*/
	}

	/*private void ProcessFlow(int intBtn) {
		// TODO Auto-generated method stub
		
	}*/

//	private void onBoWarning(int intBtn) throws Exception {
//		VOTreeNode tempNode=getSelectNode();
//		if(SJUtil.isNull(tempNode)){
//			getSelfUI().showErrorMessage("请选择节点");
//		}
//		DipProcessflowHVO hvo=(DipProcessflowHVO)tempNode.getData();
//		String hpk=hvo.getPk_processflow_h();
//		hvo=(DipProcessflowHVO) HYPubBO_Client.queryByPrimaryKey(DipProcessflowHVO.class, hpk);
//		
//		if(SJUtil.isNull(hvo)){
//			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
//			return;
//		}
//		// TODO Auto-generated method stub
//		//当前单据的预警业务系统
//		String flowtype=hvo.getFlowtype();
//		if(SJUtil.isNull(flowtype)||flowtype.length()==0){
//			getSelfUI().showErrorMessage("请选择预警系统");
//			return;
//		}
//		ToftPanel toft=SFClientUtil.showNode("H4H2H5", IFuncWindow.WINDOW_TYPE_DLG);
//		
//		WarningSetClientUI ui=(WarningSetClientUI) toft;
//		String pk_processflow_h=hvo.getPk_datadefinit_h();
//		nc.vo.jyprj.warningset.MyBillVO mybillvo=new nc.vo.jyprj.warningset.MyBillVO();
//		DipWarningsetVO[] warvo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, " tasktype='"+flowtype+"' and pk_bustab='"+pk_processflow_h+"'");
//		DipWarningsetVO vo=null;
//		boolean isadd=false;
//		
//		if(SJUtil.isNull(warvo)||warvo.length==0){
//			vo=new DipWarningsetVO();
//			vo.setPk_bustab(pk_processflow_h);
//			vo.setTasktype(flowtype);
//			isadd=false;
//		}else{
//			vo=warvo[0];
//			DipWarningsetBVO[] children=(DipWarningsetBVO[]) HYPubBO_Client.queryByCondition(DipWarningsetBVO.class, "pk_warningset='"+vo.getPk_warningset()+"'");
//			mybillvo.setChildrenVO(children);
//			isadd=false;
//		}
//		mybillvo.setParentVO(vo);
//		ui.init(mybillvo, isadd);
//		
//	}
//
//	private ProcessFlowClientUI getSelfUI() {
//		// TODO Auto-generated method stub
//		return (ProcessFlowClientUI)getBillUI();
//	}
//
//	private VOTreeNode getSelectNode() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	public void onBoEditFolder() throws Exception  {
		// TODO Auto-generated method stub
		
	}



	public void onBoDeleteFolder() throws Exception {
		// TODO Auto-generated method stub
		
	}



	public void onBoAddFolder() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}