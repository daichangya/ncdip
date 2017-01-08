package nc.ui.dip.datasend;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.ITaskExecute;
import nc.jdbc.framework.exception.DbException;
import nc.ui.bd.ref.DataSendModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.warningset.WarningsetDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
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
import nc.ui.trade.treemanage.ITreeManageController;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.datasend.DipDatasendVO;
import nc.vo.dip.filepath.FilepathVO;
import nc.vo.dip.recserver.DipRecserverVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.dip.warningset.MyBillVO;
import nc.vo.logging.Debug;
import nc.vo.pfxx.pub.FileQueue;
import nc.vo.pfxx.pub.PfxxVocabulary;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pfxx.pub.SendResult;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;

import org.w3c.dom.Document;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		VOTreeNode node=getSelectNode();

		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要增加的系统节点！");
			return;
		}
		DipDatasendVO hvo=(DipDatasendVO) node.getData();
		/*
		 *  增加的
		 */
		if(!hvo.getIsfolder().booleanValue()||hvo.getPk_sys()==null||hvo.getPk_sys().length()<=0){
			getSelfUI().showErrorMessage("请选择文件夹做增加操作！");
			return ;
		}else{
			//2011-5-23 程莉 判断是否是NC系统:如果是，则不能做任何的操作，只能浏览 begin
			String ncsql="select code from dip_sysregister_h h where h.pk_sysregister_h='"+hvo.getPk_xt()+"' and nvl(h.dr,0)=0";
			try {
				String code=iq.queryfield(ncsql);
				if("0000".equals(code)){
					getSelfUI().showErrorMessage("NC系统不能作增加操作！");
					return;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (DbException e1) {
				e1.printStackTrace();
			}
			/* end */
		}

		//郭义军 2011-6-4
		/*String sql = "SELECT COUNT(*) FROM  DIP_DATADEFINIT_H  WHERE pk_xt='"+hvo.getPk_xt()+"' AND NVL(DR,0)=0 and isfolder='N'";
		String count = iq.getNumber(sql);
		if(null==count||"0".equals(count)){
			getSelfUI().showErrorMessage("请先添加业务数据再进行操作！");
			return;
		}*/
		/*UIRefPane uir=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("businessdata").getComponent();
		DataSendModel model2=new DataSendModel();
		//2011-7-4 增加了dip_datadefinit_h.iscreatetab='Y'的限制条件
		model2.addWherePart(" AND pk_xt='"+hvo.getPk_xt()+"' AND NVL(DR,0)=0 and dip_datadefinit_h.iscreatetab='Y' and isfolder='N'");
		uir.setRefModel(model2);*/
//		filePath();
		super.onBoAdd(bo);
		getSelfUI().getBillCardPanel().setHeadItem("pk_sys", getSelectNode().getNodeID());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", hvo.getPk_xt());
	}


	public MyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}


	protected ITreeManageController getUITreeManageController() {
		return (ITreeManageController) getUIController();
	}

	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private DataSendClientUI getSelfUI() {
		return (DataSendClientUI) getBillUI();
	}
	/**
	 * 取得选中的节点对象
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();

		// 将主表主键写到子表中
		newBodyVO.setAttributeValue("pk_datasend", parent == null ? null : parent.getNodeID());

		// 添加结束
		return newBodyVO;
	}
	private CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject childVos[] = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}

	@Override
	protected void onBoSave() throws Exception {
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		DataSendClientUI ui=(DataSendClientUI) getBillUI();
		String pk=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_datasend").getValueObject();
		String pk_xt=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_datasend").getValueObject();
		if(pk==null||pk.trim().equals("")){
			pk="";
		}

		//编码，校验编码唯一 2011-07-02 冯建芬
		String code =  getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("code").getValueObject()==null?"": getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("code").getValueObject().toString();
		String sql = "";
		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		if("".equals(pk)){//增加状态
//			sql = "select count(*) from dip_datasend where code='"+code+"' and nvl(dr,0)=0 and isfolder='N'";
			sql = "select count(*) from dip_datasend where code='"+code+"' and nvl(dr,0)=0 and pk_xt='"+pk_xt+"'";
			String count = queryfield.getNumber(sql);
			if(!"0".equals(count)){
				MessageDialog.showErrorDlg(this.getBillUI(), "提示", IContrastUtil.getCodeRepeatHint("编码", code));
				return ;
			}

		}else{//修改状态
//			sql = "select count(*) from dip_datasend where code='"+code+"' and nvl(dr,0)=0 and pk_datasend<>'"+pk+"'  and isfolder='N'";
			sql = "select count(*) from dip_datasend where code='"+code+"' and nvl(dr,0)=0 and pk_datasend<>'"+pk+"'  and pk_xt='"+pk_xt+"'";
			String count = queryfield.getNumber(sql);
			if(!"0".equals(count)){
				MessageDialog.showErrorDlg(this.getBillUI(), "提示", IContrastUtil.getCodeRepeatHint("编码", code));
				return ;
			}
		}

		//获得界面原始数据的vo
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);
		//获得界面新数据vo
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		//super.onBoSave();
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

		//如果该单据增加保存时是自动维护树结构，则执行如下操作
		if (getUITreeCardController().isAutoManageTree()) {		

			getSelfUI().insertNodeToTree(billVO.getParentVO());

		}

		getSelfUI().getBillCardPanel().execHeadLoadFormulas();
		
		
		VOTreeNode node=getSelectNode();
//		DipDatasendVO vo= (DipDatasendVO) node.getData();
//		if(vo!=null&&vo.getIsfolder()!=null&&vo.getIsfolder().booleanValue()){
//			
//		}else{
//			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
//			getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
//		}
		getSelfUI().onTreeSelectSetButtonState(node);
		getSelfUI().updateButtonUI();
	}

	@Override
	protected void onBoDelete() throws Exception {
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确认要删除?");
		if(flag == 1){
			String pk_node = ((DataSendClientUI) getBillUI()).selectnode;
			if("".equals(pk_node)){
				NCOptionPane.showMessageDialog(this.getBillUI(),"请选择要删除的节点。");
				return ;
			}
			VOTreeNode node1=(VOTreeNode) getSelectNode().getParent();
			DipDatasendVO vo = (DipDatasendVO) HYPubBO_Client.queryByPrimaryKey(DipDatasendVO.class, pk_node);
			if(vo==null){
				getSelfUI().showWarningMessage("系统节点不能做删除操作！");
				return;
			}
			if(vo!=null){
				/* 2011-5-23 NC系统下的节点不能删除 */
				String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+vo.getPk_xt()+"' and nvl(h.dr,0)=0";
				String isNC=iq.queryfield(sql);
				if(isNC !=null && !"".equals(isNC.trim())){
					getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能删除！"); 
					return;
				}
				/* end */
				HYPubBO_Client.delete(vo);
			}
//			更新树
			this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());	   
			if(node1!=null){
				getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1.getPath()));
				}

		}

	}



	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		DipDatasendVO hvo=(DipDatasendVO) treeNode.getData();
		String str=(String)treeNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做修改操作！"); 
			return;
		}else{
			/*查询NC系统下的节点，不允许做修改操作 2011-5-23 程莉 begin */
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+hvo.getPk_xt()+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能修改！"); 
				return;
			}
			/* end */
		}
		//郭义军 2011-6-4
		/*UIRefPane uir=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("businessdata").getComponent();
		DataSendModel model2=(DataSendModel) uir.getRefModel();
		model2.addWherePart(" AND pk_xt='"+hvo.getPk_xt()+"' AND NVL(DR,0)=0 and dip_datadefinit_h.iscreatetab='Y' and isfolder='N'");*/
//		uir.setRefModel(model2);
//		filePath();
		super.onBoEdit();
	}


	//张进双 2011-5-14
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.YuJing:
			yuJing();
			break;
		case IBtnDefine.DATASEND:
			onBoDataSend();
			break;
		case IBtnDefine.SENDFORM:
			onBoSendForm();
			break;
		case IBtnDefine.CONTROL:
			onBoControl();
			break;
		case IBtnDefine.addFolderBtn:
			onBoAddFolder();
			break;
		case IBtnDefine.editFolderBtn:
			onBoEditFolder();
			break;
		case IBtnDefine.delFolderBtn:
			onBoDeleteFolder();
			break;
		case IBtnDefine.moveFolderBtn:
			onBoMoveFolder();
			break;
		}
	}


	/**
		 * @desc 文件移动
		 * */
		public void onBoMoveFolder() throws Exception{
			
			nc.vo.dip.datasend.MyBillVO billvo=(nc.vo.dip.datasend.MyBillVO) getBufferData().getCurrentVO();
			if(billvo!=null&&billvo.getParentVO()!=null){
				DipDatasendVO hvo=(DipDatasendVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_FS,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_FS), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipDatasendVO) HYPubBO_Client.queryByPrimaryKey(DipDatasendVO.class, hvo.getPrimaryKey());
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
		DipDatasendVO vo =(DipDatasendVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipDatasendVO[] listvos=(DipDatasendVO[]) HYPubBO_Client.queryByCondition(DipDatasendVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_datasend<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0 and pk_sys='"+str+"'");
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
					vo=(DipDatasendVO) HYPubBO_Client.queryByPrimaryKey(DipDatasendVO.class, vo.getPrimaryKey());
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
		DipDatasendVO vo =(DipDatasendVO) tempNode.getData();
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipDatasendVO.class, "pk_sys='"+vo.getPrimaryKey()+"'");
			if(hvos!=null&&hvos.length>0){
				throw new Exception("文件夹下已经有数据定义，不能删除！");
			}else{
				HYPubBO_Client.delete(vo);
				getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
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
		DipDatasendVO hvo=(DipDatasendVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipDatasendVO newhvo=new DipDatasendVO();
		newhvo.setPk_sys(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipDatasendVO[] listvos=(DipDatasendVO[]) HYPubBO_Client.queryByCondition(DipDatasendVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
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
				newhvo=(DipDatasendVO) HYPubBO_Client.queryByPrimaryKey(DipDatasendVO.class, pk);
				
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
	public void onBoControl(){

		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showErrorMessage("请选择要操作的节点");
			return;
		}
		DipDatasendVO hvo=null;
		try {
			hvo = (DipDatasendVO) HYPubBO_Client.queryByPrimaryKey(DipDatasendVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}

		ControlHVO chvo=new ControlHVO();
		chvo.setBustype(SJUtil.getYWnameByLX(IContrastUtil.YWLX_FS));
		chvo.setCode(hvo.getCode());
		chvo.setName(hvo.getName());

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPrimaryKey(),IContrastUtil.YWLX_FS,hvo.getFilepath());
		cd.showModal();
	}
	private void onBoSendForm() {
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		Object fpk=node.getParentnodeID();
		if(fpk==null){
			getSelfUI().showErrorMessage("请选择子节点操作!");
			return;
		}
		String pk=node.getNodeID().toString();
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(jfileChooser.DIRECTORIES_ONLY);
		if (jfileChooser.showDialog(getSelfUI(), "发送")/*.showSaveDialog(getSelfUI())*/ == javax.swing.JFileChooser.CANCEL_OPTION)
			return;
		String path = jfileChooser.getSelectedFile().toString();
		if(!path.endsWith(".xml")){
			getSelfUI().showErrorMessage("请选择xml文件发送！");
			return;
		}
		doQTSend(pk,path);
	}


	//预警弹出框
	public void yuJing() throws UifException{
		//获取选中的节点
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		//得到当前VO类
		DipDatasendVO dvo=(DipDatasendVO) node.getData();

		//得到主键值
		String pk=dvo.getPk_datasend();
		dvo=(DipDatasendVO) HYPubBO_Client.queryByPrimaryKey(DipDatasendVO.class, pk);
		if(SJUtil.isNull(dvo)){
			this.getSelfUI().showWarningMessage("此节点还没有保存，请编辑");
			return;
		}
		//当前单据的预警业务类型
		String type=dvo.getTasktype();
		if(SJUtil.isNull(type)|| type.length()==0){
			this.getSelfUI().showWarningMessage("请选择预警业务类型！");
			return;
		}
		//得到主键值
		//得到MyBillVO(warningset)
		MyBillVO bill=new MyBillVO();
		DipWarningsetVO [] vo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, "tasktype='"+type+"' and pk_bustab='"+pk+"'");
		DipWarningsetVO dwvo=null;
		boolean isadd=false;
		if(SJUtil.isNull(vo)||vo.length==0){
			dwvo=new DipWarningsetVO();
			dwvo.setTasktype(type);
			dwvo.setPk_bustab(pk);
			dwvo.setPk_sys(dvo.getPk_sys());
			isadd=true;
		}
		else{
			//如果vo不是空的
			dwvo=vo[0];
			DipWarningsetBVO[]bvos=(DipWarningsetBVO[]) HYPubBO_Client.queryByCondition(DipWarningsetBVO.class,"pk_warningset='"+dwvo.getPk_warningset()+"'");
			bill.setChildrenVO(bvos);
			isadd=false;
		}
		dwvo.setWcode(dvo.getCode());
		dwvo.setWname(dvo.getName());
		bill.setParentVO(dwvo);
		WarningsetDlg wd=new WarningsetDlg(getSelfUI(),bill, isadd,dvo.getPk_xt(),4);		
		wd.showModal();		
	}
//后台发送。
	String pk;
	public void onBoDataSend() throws Exception{
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		Object fpk=node.getParentnodeID();
		if(fpk==null){
			getSelfUI().showErrorMessage("请选择子节点操作!");
			return;
		}
		Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确定发送！");
		if(flag==1){
			pk=node.getNodeID().toString();
			new Thread() {
				@Override
				public void run() {
					BannerDialog dialog = new BannerDialog(getSelfUI());
					dialog.setTitle("正在发送，请稍候...");
					dialog.setStartText("正在发送，请稍候...");
								
					try {
						dialog.start();	
						ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
						RetMessage rm=ite.doFSTask(pk);
						if(rm==null||rm.getIssucc()){
							MessageDialog.showHintDlg(getSelfUI(), SJUtil.getYWnameByLX(IContrastUtil.YWLX_FS), "发送成功！");
						}else{
							getSelfUI().showErrorMessage("发送失败！"+rm.getMessage());
						}
					} catch (Throwable er) {
						er.printStackTrace();
					} finally {
						dialog.end();
					}
				}			
			}.start();
		}

	}
	public String convoerspath(String path){
		if(path!=null){
			return path.replaceAll("\\\\", "/");
		}else{
			return path; 
		}
	}

	/**
	 * @author 程莉
	 * @description 是否后台发送为false时,执行“前台发送”的方法
	 * @date 2011-6-15
	 * 前台发送
	 */
	int postNum;
	int suspendNum;
	int m_nresult;
	Document m_backDoc ;
//	File[] listFiles;
	String strBackPath;//回执
	String strReturn;
	ILogAndTabMonitorSys ilm=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
	public RetMessage doQTSend(String pk,String path){
		RetMessage rm=new RetMessage();
		rm.setIssucc(true);
		postNum = 0;
		suspendNum = 0;
		//执行数据导入
		getSelfUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000088")/* @res "外部数据导入中..." */);
		/*VOTreeNode node=getSelectNode();
		String pk=node.getNodeID().toString();*/
		if(pk==null || pk.length()==0 || "".equals(pk)){
			rm=new RetMessage(false,"没有传入相应的数据发送主键!");
			return rm;
		}
		//数据发送vo
		DipDatasendVO ddvo=null;
		try {
			ddvo = (DipDatasendVO) HYPubBO_Client.queryByPrimaryKey(DipDatasendVO.class, pk);
		} catch (UifException e1) {
			e1.printStackTrace();
			rm=new RetMessage(false,"查询对应的数据发送vo出错！"+pk+e1.getMessage());
			return rm;
		}
		if(ddvo==null){
			rm=new RetMessage(false,"没有找到对应的数据发送vo:"+pk);
			return rm;
		}
		String server=ddvo.getServer();//目标服务器：存的NC接收服务器注册表的主键


		//NC接收服务器注册vo
		DipRecserverVO drvo=null;
		try {
			drvo = (DipRecserverVO) HYPubBO_Client.queryByPrimaryKey(DipRecserverVO.class, server);
		} catch (UifException e1) {
			e1.printStackTrace();
			rm=new RetMessage(false,"查询对应的NC接收服务器注册vo出错！"+server+e1.getMessage());
			return rm;
		}
		if(drvo==null){
			rm=new RetMessage(false,"没有找到对应的NC接收服务器注册vo:"+server);
			return rm;
		}
		String url=drvo.getDescs();//服务器属性url：如 http://127.0.0.1:80/service/XChangeServlet?...
		//服务器URL
		if (url == null || "".equals(url))
			return new RetMessage(false,"未找到该服务器属性URL："+url);
		final String sendURL = url + "&" + "langcode=" + ClientEnvironment.getInstance().getLanguage();
		//此次对每个文件进行导入
		File recFile=new File(path);
		path=convoerspath(path);
		String[] p=path.split("/");
		
		String filepath="";
		for(int i=0;i<p.length-1;i++){
			filepath=filepath+"/"+p[i];
		}
		strBackPath=filepath+"/back";
		strReturn=filepath+"/return";
		File backFile=new File(strBackPath);
		if(!backFile.exists()){
			backFile.mkdirs();
		}
		File retFile=new File(strReturn);
		if(!retFile.exists()){
			retFile.mkdirs();
		}
//					//时间测试
					long begintime = System.currentTimeMillis();
					String msg = null;
					try{
						String dir = strBackPath;
						String ret=strReturn;
						FileQueue fileQueue = null;
							System.out.println("开始发送第" + (new Integer(1)).toString() + "篇数据文件 ...");
							final boolean bcompress =false /*getCkbNeedCompress().isSelected()*/; //是否压缩
							SendResult results = PostFile.sendFileWithResults(recFile, sendURL, ret, dir, bcompress, fileQueue);
							m_nresult = results.getErrID();
							m_backDoc = results.getBackDoc();
							org.w3c.dom.Element root = m_backDoc.getDocumentElement();
							Object ob=root.getAttribute("successful");
							String iscg="";
							if(ob!=null&&ob.equals("Y")){
								iscg="发送成功！";
							}else{
								iscg="发送失败！";
							}
							doAfterSendEachFile(m_nresult);
//						}
						//提示本次发送全部完成
						msg = iscg+"  "+nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000139")/* @res "已发送数据文件篇数:" */
						+ (new Integer(postNum)).toString() + "," + nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000140")/* @res "中断篇数:" */
						+ (new Integer(suspendNum)).toString();
						//时间测试
						msg += nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-V50132")/*"整个过程耗时："*/
						+ String.valueOf(System.currentTimeMillis() - begintime) + "ms";
						MessageDialog.showHintDlg(getSelfUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000089")/* @res "提示" */, msg);
					}catch (Exception e){
						Debug.debug("发送出现不可预料错误，请规范操作:" + e.getMessage());
						MessageDialog.showHintDlg(getSelfUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000089")/* @res "提示" */, e
								.getMessage());
					}finally{
					}
		return rm;
	}

	String m_sendResult;
	protected void doAfterSendEachFile(int nfile){
		switch (m_nresult){
		case PfxxVocabulary.PFXX_CLIENT_MANUAL_LOAD_COMPLETE:
			m_sendResult =  "已完成" ;
			postNum++;
			break;
		case PfxxVocabulary.PFXX_CLIENT_MANUAL_LOAD_UNKNOWN_DETINATION_HOST:
			m_sendResult = "中断[网络上找不到目的URL地址所在的主机]";
			suspendNum++;
			break;
		default:
			m_sendResult = "中断[未知错误]" ;
		suspendNum++;
		break;
		}
	}


	public void onTreeSelected(VOTreeNode arg0){
		//获取VO
			DipDatasendVO hvo=(DipDatasendVO) arg0.getData();
			nc.vo.dip.datasend.MyBillVO billvo =new nc.vo.dip.datasend.MyBillVO();
			// 设置子VO
			billvo.setParentVO(hvo);
			// 显示数据
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));

	}


	/**
	 * @author 程莉
	 * @date 2011-7-4
	 * @功能：解决修改时，不做任何操作,点击取消按钮，文件属性、服务器属性值没值问题
	 */
	@Override
	protected void onBoCancel() throws Exception {
		super.onBoCancel();
		VOTreeNode node=getSelectNode();
		if(node !=null){	
			String filepath=(String) this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("filepath").getValueObject();
			String sql="select descriptions from dip_filepath where pk_filepath='"+filepath+"' and nvl(dr,0)=0";
			String fp=iq.queryfield(sql);
			this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("fileproperty", fp);

			String tagserver=(String)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("server").getValueObject();
			String sql2="select descs from dip_recserver where pk_recserver='"+tagserver+"' and nvl(dr,0)=0";
			String tp=iq.queryfield(sql2);
			this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("serverproperty", tp);
			
//			DipDatasendVO vo=(DipDatasendVO) node.getData();
//			if(vo!=null&&vo.isfolder!=null&&vo.isfolder.booleanValue()){
//				getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//				getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.DATASEND).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.SENDFORM).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//			}else{
//				getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//				
//			}
			getSelfUI().onTreeSelectSetButtonState(node);
//		   getSelfUI().updateButtonUI();
		}

	}


	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().updateButtonUI();
	}


}