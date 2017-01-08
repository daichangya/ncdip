package nc.ui.dip.processflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.ITaskExecute;
import nc.itf.dip.pub.ITaskManage;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.bd.ref.DataLCRefModel;
import nc.ui.bd.ref.DataLCTreeRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.warningset.WarningsetDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.processflow.DipProcessflowBVO;
import nc.vo.dip.processflow.DipProcessflowHVO;
import nc.vo.dip.processflow.MyBillVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{

	public MyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}


	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */


	private ProcessFlowClientUI getSelfUI() {
		// TODO Auto-generated method stub
		return (ProcessFlowClientUI)getBillUI();
	}



	/**
	 * 取得选中的节点对象
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}

	/**
	 * 该方法在增行和插行后被调用，可以在该方法中为新增的行设置一些默认值
	 * 
	 * @param newBodyVO
	 * @return TODO
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();


		// 将主表主键写到子表中
		newBodyVO.setAttributeValue("pk_processflow_h", parent == null ? null : parent.getNodeID());

		// 添加结束
		return newBodyVO;
	}

	/**
	 * 
	 * <P>此方法为覆盖父类方法
	 * <BR>将父类中的ISingleController判断去掉，否则保存时，表头为空
	 * @throws Exception
	 * @see nc.ui.trade.treecard.TreeCardEventHandler#onBoSave()
	 */

	private boolean isnew=false;
	@Override
	protected void onBoSave() throws Exception {
//		this.getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.Moveddown).setEnabled(false);
		VOTreeNode node=getSelectNode();
		int kk=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<kk;i++){
			//项目主键	orderno 
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_processflow_b").getValueAt(i, "bm");
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_processflow_b").getValueAt(i, "name");
			String orderno=getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_processflow_b").getValueAt(i, "orderno")==null?"":getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_processflow_b").getValueAt(i, "orderno").toString();
			String lclx=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_processflow_b").getValueAt(i, "lclx");
			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(orderno!=null)&&(lclx==null||lclx.trim().equals(""))){
				//getBillCardPanelWrapper().getBillCardPanel().getBillTable("dip_datachange_b").changeSelection(i, 0, true, true);
				list.add(i);
				//getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datachange_b").delLine(w);
			}
		}
		
		if(list!=null&&list.size()>0){
			int[] w=new int[list.size()];
			for(int i=0;i<list.size();i++){
				w[i]=list.get(i);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_processflow_b").delLine(w);
		}
		
		
		
		
		
		//校验页面数据是否为空
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd!=null){
			bd.dataNotNullValidate();
		}
//		//获取当前ui
		ProcessFlowClientUI ui=(ProcessFlowClientUI) getBillUI();
		String pk=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_processflow_h").getValueObject();
		if(pk==null||pk.trim().equals("")){
			pk="";
		}
		//编码，校验编码唯一 2011-07-02 冯建芬
		String code =  getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("code").getValueObject()==null?"": getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("code").getValueObject().toString();
		String sql = "";
		String fpk=getSelfUI().getBillCardPanel().getHeadItem("fpk").getValueObject()==null?"":getSelfUI().getBillCardPanel().getHeadItem("fpk").getValueObject().toString();
		String name=(String) getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("name").getValueObject();
		String pk_xt=(String) getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("pk_xt").getValueObject();
		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		if("".equals(pk)){//增加状态
//			sql = "select count(*) from dip_processflow_h where code='"+code+"' and nvl(dr,0)=0 and fpk='"+fpk+"'";
			sql = "select count(*) from dip_processflow_h where code='"+code+"' and nvl(dr,0)=0 and pk_xt='"+pk_xt+"'";
			String count = queryfield.getNumber(sql);
			if(!"0".equals(count)){
				MessageDialog.showErrorDlg(this.getBillUI(), "提示", IContrastUtil.getCodeRepeatHint("编码", code));
				return ;
			}
//			Collection cflowtype=bs.retrieveByClause(DipProcessflowHVO.class, "name='"+name+"' and isnull(dr,0)=0 and fpk='"+fpk+"'");
//
//			if(cflowtype!=null&&cflowtype.size()>=1){
//				ui.showErrorMessage("名称不能相同");
//				return;
//			}
			
		}else{//修改状态
//			sql = "select count(*) from dip_processflow_h where code='"+code+"' and nvl(dr,0)=0 and pk_processflow_h<>'"+pk+"' and fpk='"+fpk+"'";
			sql = "select count(*) from dip_processflow_h where code='"+code+"' and nvl(dr,0)=0 and pk_processflow_h<>'"+pk+"' and pk_xt='"+pk_xt+"'";
			String count = queryfield.getNumber(sql);
			if(!"0".equals(count)){
				MessageDialog.showErrorDlg(this.getBillUI(), "提示", IContrastUtil.getCodeRepeatHint("编码", code));
				return ;
			}
//			Collection cflowtype=bs.retrieveByClause(DipProcessflowHVO.class, "name='"+name+"' and isnull(dr,0)=0 and pk_processflow_h<>'"+pk+"' and fpk='"+fpk+"'");
//
//			if(cflowtype!=null&&cflowtype.size()>=1){
//				ui.showErrorMessage("名称不能相同");
//				return;
//			}
		}
		
		
		
		//String orderno=(String)getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("orderno").getValueObject();

		
		

		DipProcessflowBVO[] bvo=null;

	
		
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);

		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();

		bvo=(DipProcessflowBVO[]) checkVO.getChildrenVO();
		if(bvo == null||bvo.length<=0){
			ui.showErrorMessage("表体不能为空！");
			return;
			/*
			 * 数据流程表体编码唯一性校验162-171行
			 * 2011-06-14
			 * zlc*/
		}else{
			//修改为 相同流程类型，不能编码重复
			Map map=new HashMap();
			for(int i=0;i<bvo.length;i++){
				DipProcessflowBVO repbvo=bvo[i];
				String key=repbvo.getCode()+repbvo.getLclx();
				if(map.get(key)==null){
					map.put(key, "yes");	
				}else{
					ui.showErrorMessage("表体相同类型类编码不能重复！");
					return;
				}
				
//				for(int j=0;j<i;j++){
//					if(bvo[j].getName().equals(bvo[i].getName())){
//						
//					}
//				}
			}
		}

		Object o = null;
		ISingleController sCtrl = null;

		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether())
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		else{
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		}
		
		for(int i=0;i<bvo.length;i++){
			HYPubBO_Client.update(bvo[i]);
		}
		
		isdel=false;
		if(dle!=null&&dle.size()>0){
			String ss="";
			for(int i=0;i<dle.size();i++){
				ss=ss+"'"+dle.get(i)+"',";
			}
			HYPubBO_Client.deleteByWhereClause(DipProcessflowBVO.class, "pk_processflow_h='"+billVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=0 and pk_processflow_b in ("+ss.substring(0,ss.length()-1)+")");
		}

		DipProcessflowBVO[] bvos=(DipProcessflowBVO[]) HYPubBO_Client.queryByCondition(DipProcessflowBVO.class, "pk_processflow_h='"+billVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=0 order by orderno");
		if(bvos!=null&&bvos.length>0){
			int i=1;
			for(DipProcessflowBVO bvoi:bvos){
				bvoi.setOrderno(i);
				i++;
			}
			HYPubBO_Client.updateAry(bvos);
			billVO.setChildrenVO((DipProcessflowBVO[]) HYPubBO_Client.queryByCondition(DipProcessflowBVO.class, "pk_processflow_h='"+billVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=0 order by orderno"));
		}

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

		//如果该单据增加保存时是自动维护树结构，则执行如下操作
		if (getUITreeCardController().isAutoManageTree()) {		

			getSelfUI().insertNodeToTree(billVO.getParentVO());

		}
//		setTSFormBufferToVO(checkVO);
		
		DipWarningsetVO[] wvo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, "pk_bustab='"+getBufferData().getCurrentVO().getParentVO().getPrimaryKey()+"'");
		if(wvo!=null&&wvo.length>0){
			ITaskManage itm=(ITaskManage) NCLocator.getInstance().lookup(ITaskManage.class.getName());
			Object ob=wvo[0].getIsnotwarning();
			if(ob!=null&&ob.toString().equals("可用")){
				itm.startOrStopWarn(wvo[0].getPrimaryKey(), true);
			}
		}
		
//		DipProcessflowHVO hvo=(DipProcessflowHVO) node.getData();
//		if(hvo!=null&&hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
//			
//		}else{
//			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
////			getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
//			getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
//		}
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		getSelfUI().updateButtonUI();
	}
	@SuppressWarnings("unused")
	private CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject childVos[] = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}

	@Override
	protected void setTSFormBufferToVO(AggregatedValueObject setVo) throws Exception {
		// TODO Auto-generated method stub
		if (setVo == null)
			return;
		AggregatedValueObject vo = getBufferData().getCurrentVO();
		if (vo == null)
			return;
		if (getBillUI().getBillOperate() == 0) {
			if (vo.getParentVO() != null && setVo.getParentVO() != null)
				setVo.getParentVO().setAttributeValue("ts", vo.getParentVO().getAttributeValue("ts"));
			SuperVO changedvos[] = (SuperVO[]) (SuperVO[]) getChildVO(setVo);
			if (changedvos != null && changedvos.length != 0) {
				HashMap bufferedVOMap = null;
				SuperVO bufferedVOs[] = (SuperVO[]) (SuperVO[]) getChildVO(vo);
				if (bufferedVOs != null && bufferedVOs.length != 0) {
					bufferedVOMap = Hashlize.hashlizeObjects(bufferedVOs, new VOHashPrimaryKeyAdapter());
					for (int i = 0; i < changedvos.length; i++) {
						if (changedvos[i].getPrimaryKey() == null)
							continue;
						ArrayList bufferedAl = (ArrayList) bufferedVOMap.get(changedvos[i].getPrimaryKey());
						if (bufferedAl != null) {
							SuperVO bufferedVO = (SuperVO) bufferedAl.get(0);
							changedvos[i].setAttributeValue("ts", bufferedVO.getAttributeValue("ts"));
						}
					}

				}
			}
		}
	}

	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode tempNode = getSelectNode();
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要删除的节点！");
			return ;
		}

		String pk=(String) tempNode.getParentnodeID();
		if(pk==null){
			getSelfUI().showErrorMessage("系统节点不能做删除操作！");
			return ;
		}


//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		Collection  cpk_datadefinit_h=bs.retrieveByClause(DipDatadefinitHVO.class,"pk_sysregister_h='"+pk+"' and isnull(dr,0)=0");
//		if(cpk_datadefinit_h.size()!=0&&cpk_datadefinit_h.size()>=1){
//		getSelfUI().showWarningMessage("二级节点不能删除！");
//		return;
//		}
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确认要删除?");
		if(flag == 1){
			String pk_node = ((ProcessFlowClientUI) getBillUI()).selectnode;
			if("".equals(pk_node)){
				NCOptionPane.showMessageDialog(this.getBillUI(),"请选择要删除的节点。");
				return ;
			}

			DipProcessflowBVO[] vos = (DipProcessflowBVO[])HYPubBO_Client.queryByCondition(DipProcessflowBVO.class, " pk_processflow_h='"+pk_node+"' and isnull(dr,0)=0  ");
			if(vos!=null && vos.length>0){
				for(DipProcessflowBVO bvo : vos ){
					HYPubBO_Client.delete(bvo);
				}
			}	
			DipProcessflowHVO vo = (DipProcessflowHVO) HYPubBO_Client.queryByPrimaryKey(DipProcessflowHVO.class, pk_node);
			if(vo!=null)
				HYPubBO_Client.delete(vo);		
//			更新树
			this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
			getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			getSelfUI().getButtonManager().getButton(IBtnDefine.ProcessFlow).setEnabled(false);
			getSelfUI().getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
			getSelfUI().getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			
			getSelfUI().updateButtonUI();
			
			if(node1!=null){
				getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
						.getPath()));	
			}
			
		}

	}
	//设置行操作插入行不可用
	@Override
	protected void onBoLineIns() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineIns();
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		getSelfUI().getBillCardPanel().setBodyValueAt(null, row, new DipProcessflowBVO().getPKFieldName());
		int count=getSelfUI().getBillCardPanel().getBillTable().getRowCount();
		for(int i=row;i<count;i++){
			getSelfUI().getBillCardPanel().setBodyValueAt(i+1, i, "orderno");
		}
		
//		this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().setEnabled(false);

	}


	//上移
	private void movedUp() throws Exception{
		//得到当前有多少条数据
		int count=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		//得到当前是第几行，下标从0开始
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			this.getBillUI().showWarningMessage("请选择要操作的行！");
			return;
		}
		if(row==0){
			this.getBillUI().showWarningMessage("已经是第一行了！");
			return;
		}else{ 		
			MyBillVO billvo= (MyBillVO)getBillTreeCardUI().getVOFromUI();
			DipProcessflowBVO[] bvos=(DipProcessflowBVO[]) billvo.getChildrenVO();
			DipProcessflowBVO vo1=bvos[row-1];
			DipProcessflowBVO vo2=bvos[row];
			String pk=vo1.getPrimaryKey();
			int order=vo1.getOrderno();
			vo1.setPrimaryKey(vo2.getPrimaryKey());
			vo1.setOrderno(vo2.getOrderno());
			vo2.setPrimaryKey(pk);
			vo2.setOrderno(order);
			bvos[row-1]=vo2;
			bvos[row]=vo1;
//			if(vo1.getPrimaryKey()!=null&&vo1.getPrimaryKey().length()>0){
//				HYPubBO_Client.update(vo1);
//			}
//			if(vo2.getPrimaryKey()!=null&&vo2.getPrimaryKey().length()>0){
//				HYPubBO_Client.update(vo2);
//			}
//			billvo.setChildrenVO(bvos);
//			getBufferData().addVOToBuffer(billvo);
//			getBufferData().setCurrentVO(billvo);

			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo2, row-1);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo1, row);

			String[] formula=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("bm").getLoadFormula();
			getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row-1, formula);
			getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, formula);
			//getBufferData().refresh();

		}
		//2011-6-24 cl 上移的时候，保证鼠标焦点也随着一起上移
		this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row-1, 1, false, false);
	}

	//下移
	private void movedDown() throws UifException{
		//得到当前有多少条数据
		int count=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		//得到当前所选是第几行，下标从0开始
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			this.getBillUI().showWarningMessage("请选择要操作的行！");
			return;
		}
		if(row+1==count){
			this.getBillUI().showWarningMessage("已经是最后一行了！");
			return;
		}else{
			MyBillVO billvo = null;
			try {
				billvo = (MyBillVO)getBillTreeCardUI().getVOFromUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
			DipProcessflowBVO[]bvo=(DipProcessflowBVO[]) billvo.getChildrenVO();
			DipProcessflowBVO b=bvo[row];
			DipProcessflowBVO bb=bvo[row+1];
			String pk=b.getPrimaryKey();
			int order=b.getOrderno();
			b.setPrimaryKey(bb.getPrimaryKey());
			b.setOrderno(bb.getOrderno());
			bb.setPrimaryKey(pk);
			bb.setOrderno(order);
			bvo[row]=bb;
			bvo[row+1]=b;
//			if(b.getPrimaryKey()!=null&&b.getPrimaryKey().length()>0){
//				HYPubBO_Client.update(b);
//			}
//			if(bb.getPrimaryKey()!=null&&bb.getPrimaryKey().length()>0){
//				HYPubBO_Client.update(bb);
//			}
//			billvo.setChildrenVO(bvo);
//			getBufferData().addVOToBuffer(billvo);
//			getBufferData().setCurrentVO(billvo);

			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(bb, row);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(b, row+1);

			String[] formula=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("bm").getLoadFormula();
			getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row+1, formula);
			getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, formula);
//			getBufferData().updateView();
		}

		//2011-6-24 cl 下移的时候保证鼠标焦点也随着下移
		this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row+1, 1, false, false);
	}

	/**
	 * 自定义按钮动作事件
	 * 1.流程定义按钮
	 * 2.预警按钮
	 */
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch(intBtn){
		case IBtnDefine.YuJing:
			onBoWarning(intBtn);
			break;
		case  IBtnDefine.ProcessFlow:
			ProcessFlow(intBtn);
			break;
		case IBtnDefine.Moveddown:
			movedDown();
			break;
		case IBtnDefine.moveFolderBtn:
			onBoMoveFolder();
			break;
		case IBtnDefine.Movedup:
			movedUp();
			break;
		case IBtnDefine.LCLineadd:
			onBoLineAdd();
			break;
		case IBtnDefine.LCLinedel:
			onBoLineDel();
			break;
		case IBtnDefine.LCLinecopy:
			onBoLineCopy();
			break;
		case IBtnDefine.LCLineins:
			onBoLineIns();
			break;
		case IBtnDefine.LCLinepast:
			onBoLinePaste();
			break;
		}

	}


	/**
		 * @desc 文件移动
		 * */
		public void onBoMoveFolder() throws Exception{
			
			MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
			if(billvo!=null&&billvo.getParentVO()!=null){
				DipProcessflowHVO hvo=(DipProcessflowHVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_LC,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_LC), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipProcessflowHVO) HYPubBO_Client.queryByPrimaryKey(DipProcessflowHVO.class, hvo.getPrimaryKey());
					billvo.setParentVO(hvo);
					getBufferData().addVOToBuffer(billvo);
					getBufferData().setCurrentVO(billvo);
					getSelfUI().getBillTree().updateUI();
					onBoRefresh();
					TableTreeNode node=(TableTreeNode) getSelfUI().getBillTree().getModel().getRoot();
					Vector v=new Vector();
					getAllNode(node, v);
					if(v!=null&&v.size()>0){
						TableTreeNode tempnode=null;
						for(int i=0;i<v.size();i++){
							String pkf=(String)((TableTreeNode)v.get(i)).getNodeID();
							if(pkf!=null&&pkf.equals(hvo.getPrimaryKey())){
								tempnode=(TableTreeNode) v.get(i);
								break;
							}
						}
						if(tempnode!=null){
							getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath( tempnode.getPath()));
						}
					}
				}
			}
			
		}
		private void getAllNode(Object node, Vector v){
		    v.add(node);
		    int childCount = getSelfUI().getBillTree().getModel().getChildCount(node);
		    for (int i = 0; i < childCount; i++) {
	            Object child = getSelfUI().getBillTree().getModel().getChild(node, i);
	            getAllNode(child, v);
	        }
		}
	/*
	 * 流程执行
	 * lx 2011-5-23
	 */
	String lcpk;
	private void ProcessFlow(int intBtn) {
		// TODO Auto-generated method stub
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showWarningMessage("请选择需要操作的节点！");
			return;
		}
		DipProcessflowHVO hvo=(DipProcessflowHVO) node.getData();
		String fpk=hvo.getFpk();
		if(fpk==null||fpk.length()<=0){
			getSelfUI().showWarningMessage("系统节点不能做操作！");
			return;
		}
		lcpk=hvo.getPrimaryKey();
		new Thread() {
			@Override
			public void run() {
				BannerDialog dialog = new BannerDialog(getSelfUI());
				dialog.setTitle("正在执行流程，请稍候...");
				dialog.setStartText("正在执行流程，请稍候...");
							
				try {
					dialog.start();
					ITaskExecute exe=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
					RetMessage rm=exe.doLCTask(lcpk);
					if(rm!=null&&rm.getIssucc()){
						MessageDialog.showHintDlg(getSelfUI(), "流程执行", "执行成功");
					}else{
						getSelfUI().showErrorMessage("流程执行失败！"+rm.getMessage());
					}
				} catch (Throwable er) {
					er.printStackTrace();
				} finally {
					dialog.end();
				}
			}			
		}.start();
	}

	protected void onBoWarning(int intBtn) throws Exception {
		VOTreeNode tempNode=getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("请选择要操作的节点");
			return;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null ){
			getSelfUI().showWarningMessage("系统节点不能预警！"); 
			return;
		}
		String pk_sys=(String) ((VOTreeNode)tempNode.getParent()).getParentnodeID();
		DipProcessflowHVO hvo=(DipProcessflowHVO)tempNode.getData();

		String hpk=hvo.getPk_processflow_h();
		hvo=(DipProcessflowHVO) HYPubBO_Client.queryByPrimaryKey(DipProcessflowHVO.class, hpk);

		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}
		//当前单据的预警业务系统
		String tasktype=hvo.getTasktype();

		if(SJUtil.isNull(tasktype)||tasktype.length()==0){
			getSelfUI().showErrorMessage("请选择预警类型");
			return;
		}
		/*ToftPanel toft=SFClientUtil.showNode("H4H2H5", IFuncWindow.WINDOW_TYPE_DLG);
		WarningSetClientUI ui=(WarningSetClientUI) toft;*/
		
		String pk_processflow_h=hvo.getPrimaryKey();//.getPk_datadefinit_h();
		nc.vo.dip.warningset.MyBillVO mybillvo=new nc.vo.dip.warningset.MyBillVO();
		DipWarningsetVO[] warvo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, " tasktype='"+tasktype+"' and pk_bustab='"+pk_processflow_h+"'");
		DipWarningsetVO vo=null;
		boolean isadd=false;

		if(SJUtil.isNull(warvo)||warvo.length==0){
			vo=new DipWarningsetVO();
			vo.setPk_bustab(pk_processflow_h);
			vo.setTasktype(tasktype);
			vo.setPk_sys(hvo.getPk_xt());
			vo.setPk_bustab(hpk);
			isadd=true;
		}else{
			vo=warvo[0];
			DipWarningsetBVO[] children=(DipWarningsetBVO[]) HYPubBO_Client.queryByCondition(DipWarningsetBVO.class, "pk_warningset='"+vo.getPk_warningset()+"'");
			mybillvo.setChildrenVO(children);
			isadd=false;
		}
		vo.setWcode(hvo.getCode());
		vo.setWname(hvo.getName());
		mybillvo.setParentVO(vo);
	
		//2011-7-18
//		ui.init(mybillvo, isadd,tempNode.getParentnodeID().toString(),2);
		WarningsetDlg wd=new WarningsetDlg(getSelfUI(),mybillvo, isadd,hvo.getPk_xt(),2);		
		wd.showModal();		
	}






	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
//		this.getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(true);
//		this.getButtonManager().getButton(IBtnDefine.Moveddown).setEnabled(true);

		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null ){
			getSelfUI().showWarningMessage("系统节点不能做修改操作！"); 
			return;
		}
		
		super.onBoEdit();
		onBoLineAdd();
		
	}


	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {

		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要增加的系统节点！");
			return ;
		}

		String pk=(String) tempNode.getParentnodeID();
		DipProcessflowHVO hvo=(DipProcessflowHVO) tempNode.getData();
		
		if(pk!=null&&hvo!=null&&hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
			super.onBoAdd(bo);
			getSelfUI().getBillCardPanel().setHeadItem("fpk",tempNode.getNodeID());
			getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
			getSelfUI().getBillCardPanel().setHeadItem("pk_xt", hvo.getPk_xt());
		
			
		}else{
			getSelfUI().showErrorMessage("不能做增加操作！");
			return;
		}
		onBoLineAdd();

	}

	@Override
	public void onTreeSelected(VOTreeNode arg0){
		//获取权VO
		try {
			SuperVO vo=(SuperVO) arg0.getData();
			String pk_processflow_h= (String) vo.getAttributeValue("pk_processflow_h");
			SuperVO[] vos=HYPubBO_Client.queryByCondition(nc.vo.dip.processflow.DipProcessflowBVO.class, "pk_processflow_h='"+pk_processflow_h+"' and isnull(dr,0)=0 order by orderno");
			nc.vo.dip.processflow.MyBillVO billvo=new nc.vo.dip.processflow.MyBillVO();
			//设置主VO
			billvo.setParentVO(vo);
			//设置子VO
			billvo.setChildrenVO(vos);
			// 显示数据
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(),"" + (getBufferData().getVOBufferSize() - 1));

			DipProcessflowHVO hvo=(DipProcessflowHVO) vo;
//			Object ob=getSelfUI().getBillCardPanel().getHeadItem("fpk").getValueObject();
//			if(ob!=null&&ob.toString().length()>0){
//			String sys=ob.toString();
//			String type=(String) getSelfUI().getBillCardPanel().getHeadItem("flowtype").getValueObject();//hvo.getFlowtype();
//			String type=hvo.getFlowtype();
//			if(type!=null){
				String sys=hvo.getPk_xt();
				UIRefPane pane=(UIRefPane) getSelfUI().getBillCardPanel().getBodyItem("bm").getComponent();
				DataLCTreeRefModel model=(DataLCTreeRefModel) pane.getRefModel();//new DataLCRefModel();
//				if(type.equals("0001AA1000000001TQJ4")){
					model.setClassWherePart("nvl(dr,0)=0 and  fpk='"+sys+"'");
//					model.setWherePart("  nvl(v_dip_sjlc.dr,0)=0 and fpk='"+sys+"'");
//				}else{
//					model.setWherePart("  v_dip_sjlc.tasktyp='"+type+"' and nvl(v_dip_sjlc.dr,0)=0 and fpk='"+sys+"'");
//				}
				pane.setRefModel(model);

//			}


		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Override
	//增加行操作，序号是自动生成的
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
		//表体第几行，下标从0开始
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		//数据量
		int rowno=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(rowno, row, "orderno");
	}
	List dle=new ArrayList<String>();
	boolean isdel=false;


	@Override
	//执行行操作，删行时序号排序
	protected void onBoLineDel() throws Exception {
		if(!isdel){
			dle=new ArrayList<String>();
			isdel=true;
		}
		// TODO Auto-generated method stub
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		int rowno=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		DipProcessflowBVO[] cvos=(DipProcessflowBVO[]) getSelfUI().getVOFromUI().getChildrenVO();
		if(cvos==null||cvos.length<=0){
			super.onBoLineDel();
		}else{
			//2011-7-1 程莉 增加了一个rowno！=-1 的判断，避免删行时为选择行报数组下标越界错误
			if(rowno!=-1){
				String pk=cvos[rowno].getPrimaryKey();
				if(pk!=null&&pk.length()>0){
					dle.add(pk);
				}
				super.onBoLineDel();
			}else{
				getSelfUI().showWarningMessage("请选择要删除的行！");
				return;
			}
		}

		if(row<0){
			return;
		}
		if(rowno<0){
			this.getBillUI().showWarningMessage("请选择要操作的行！");
			return;
		}
		MyBillVO billvo=(MyBillVO) getSelfUI().getBufferData().getCurrentVO();
		DipProcessflowBVO[] vos=(DipProcessflowBVO[]) billvo.getChildrenVO();
		for(int i=0;i<row-1;i++){
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(i+1, i, "orderno");
//			if(rowno==i)
//			HYPubBO_Client.delete(vos[i]);
			billvo.setChildrenVO(vos);
			getBufferData().setCurrentVO(billvo);
			getBufferData().addVOToBuffer(billvo);


		}

	}


	@Override
	protected void onBoCancel() throws Exception {
//		this.getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.Moveddown).setEnabled(false);
		dle=new ArrayList<String>();
		isdel=false;
		// TODO Auto-generated method stub
		super.onBoCancel();
		
		VOTreeNode node=getSelectNode();
//		if(node!=null&&node.getData() instanceof DipProcessflowHVO){
//			DipProcessflowHVO hvo=(DipProcessflowHVO) node.getData();
//			if(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
//				getSelfUI().getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.ProcessFlow).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
////				getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(b);
//			}else{
//				getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//				
//			}
//		}
		getSelfUI().onTreeSelectSetButtonState(node);
		
//		getSelfUI().updateButtonUI();
		
		
	}


	@Override
	protected void onBoLinePaste() throws Exception {
		super.onBoLinePaste();
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		getSelfUI().getBillCardPanel().setBodyValueAt(null, row, new DipProcessflowBVO().getPKFieldName());
		int count=getSelfUI().getBillCardPanel().getBillTable().getRowCount();
		if(row>=0){
			for(int i=row;i<count;i++){
				getSelfUI().getBillCardPanel().setBodyValueAt(i+1, i, "orderno");
			}
		}
		
		
	}

	
	public void onBoEditFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做修改操作！"); 
			return;
		}
		DipProcessflowHVO vo =(DipProcessflowHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipProcessflowHVO[] listvos=(DipProcessflowHVO[]) HYPubBO_Client.queryByCondition(DipProcessflowHVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and PK_PROCESSFLOW_H<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
			List<String> listcode=new ArrayList<String>();
			List<String> listname=new ArrayList<String>();
			if(listvos!=null&&listvos.length>0){
				for(int i=0;i<listvos.length;i++){
					listcode.add(listvos[i].getCode());
					listname.add(listvos[i].getName());
				}
			}
				
			AddFolderDlg adlg=new AddFolderDlg(getBillUI(),listcode,listname,vo.getCode(),vo.getName());
			adlg.showModal();
			if(adlg.isOk()){
				String tempc=adlg.getCode();
				String tempn=adlg.getName();
				if(!tempc.equals(vo.getCode())||!tempn.equals(vo.getName())){
					vo.setCode(tempc);
					vo.setName(tempn);
					HYPubBO_Client.update(vo);
					vo=(DipProcessflowHVO) HYPubBO_Client.queryByPrimaryKey(DipProcessflowHVO.class, vo.getPrimaryKey());
					if (getUITreeCardController().isAutoManageTree()) {	
						getSelfUI().insertNodeToTree(vo);
						getBillTreeCardUI().updateUI();
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("code", tempc);
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("name", tempn);
					}
				}
			}
			return;
		}
	}

	public void onBoDeleteFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做修改操作！"); 
			return;
		}
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
		DipProcessflowHVO vo =(DipProcessflowHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipProcessflowHVO.class, "fpk='"+vo.getPrimaryKey()+"'");
			if(hvos!=null&&hvos.length>0){
				throw new Exception("文件夹下已经有数据定义，不能删除！");
			}else{
				HYPubBO_Client.delete(vo);
				getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
				
				getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
				getSelfUI().updateButtonUI();
				if(node1!=null){
					getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
							.getPath()));	
				}
				
			}
		}
	}
	public void onBoAddFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null||tempNode.getData()==null){
			getSelfUI().showErrorMessage("请选择要增加的节点！");
			return ;
		}
		DipProcessflowHVO hvo=(DipProcessflowHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipProcessflowHVO newhvo=new DipProcessflowHVO();
		newhvo.setFpk(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipProcessflowHVO[] listvos=(DipProcessflowHVO[]) HYPubBO_Client.queryByCondition(DipProcessflowHVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
		List<String> listcode=new ArrayList<String>();
		List<String> listname=new ArrayList<String>();
		if(listvos!=null&&listvos.length>0){
			for(int i=0;i<listvos.length;i++){
				listcode.add(listvos[i].getCode());
				listname.add(listvos[i].getName());
			}
		}
			
		AddFolderDlg addlg=new AddFolderDlg(getBillUI(),listcode,listname,null,null);
		addlg.showModal();
		if(addlg.isOk()){
			newhvo.setCode(addlg.getCode());
			newhvo.setName(addlg.getName());
			String pk = null;
			try {
				pk = HYPubBO_Client.insert(newhvo);
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				newhvo=(DipProcessflowHVO) HYPubBO_Client.queryByPrimaryKey(DipProcessflowHVO.class, pk);
				
				if (getUITreeCardController().isAutoManageTree()) {	
//					MyBillVO mvo=new MyBillVO();
//					mvo.setParentVO(newhvo);
					getSelfUI().insertNodeToTree(newhvo);
				}
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().updateButtonUI();
		
	}


}