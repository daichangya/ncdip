package nc.ui.dip.esbfilesend;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import nc.ui.bd.ref.FtpSourceRegisterRefModel;
import nc.ui.bd.ref.ZDJSRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.esbfilesend.dlg.AskMBDLG;
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
import nc.ui.trade.treemanage.ITreeManageController;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.esbfilesend.DipEsbSendVO;
import nc.vo.dip.filepath.FilepathVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.dip.warningset.MyBillVO;
import nc.vo.pfxx.pub.PfxxVocabulary;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.uap.busibean.exception.BusiBeanException;

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
		DipEsbSendVO hvo=(DipEsbSendVO) node.getData();
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
		}

		super.onBoAdd(bo);
		getSelfUI().getBillCardPanel().setHeadItem("pk_sys", getSelectNode().getNodeID());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", hvo.getPk_xt());
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sendfilename").setEnabled(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("bakpath").setEdit(false);
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
	private ESBmsgSendUI getSelfUI() {
		return (ESBmsgSendUI) getBillUI();
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
		newBodyVO.setAttributeValue("pk_esbsend", parent == null ? null : parent.getNodeID());

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
		VOTreeNode node=getSelectNode();
		
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		ESBmsgSendUI ui=(ESBmsgSendUI) getBillUI();
		String pk=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_esbsend").getValueObject();
		if(pk==null||pk.trim().equals("")){
			pk="";
		}
		String issendfolder=(String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("issendfolder").getValueObject();
		if(issendfolder==null||issendfolder.equals("N")||issendfolder.equals("false")){
			String sendfilename=(String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sendfilename").getValueObject();
			if(sendfilename==null||sendfilename.length()<=0){
				getSelfUI().showErrorMessage("请编辑文件名！");
				return;
			}
		}
		String deeltype=(String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deeltype").getValueObject();
		if(deeltype.equals("m1")||deeltype.equals("m6")){
			String bakpath=(String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("bakpath").getValueObject();
			if(bakpath==null||bakpath.length()<=0){
				getSelfUI().showErrorMessage("请选择备份路径！");
				return;
			}
		}else if(deeltype.equals("m3")){
			String bakpath=(String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("server").getValueObject();
			if(bakpath==null||bakpath.length()<=0){
				getSelfUI().showErrorMessage("请选择目标服务器！");
				return;
			}
		}
		//编码，校验编码唯一 2011-07-02 冯建芬
		String pk_xt=(String) getSelfUI().getBillCardPanel().getHeadItem("pk_xt").getValueObject();
		String code =  getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("code").getValueObject()==null?"": getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("code").getValueObject().toString();
		String sql = "";
		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		if("".equals(pk)){//增加状态
//			sql = "select count(*) from dip_esbsend where code='"+code+"' and nvl(dr,0)=0 and isfolder='N' and pk_xt='"+pk_xt+"'";
			sql = "select count(*) from dip_esbsend where code='"+code+"' and nvl(dr,0)=0  and pk_xt='"+pk_xt+"'";
			String count = queryfield.getNumber(sql);
			if(!"0".equals(count)){
				MessageDialog.showErrorDlg(this.getBillUI(), "提示", IContrastUtil.getCodeRepeatHint("编码", code));
				return ;
			}

		}else{//修改状态
//			sql = "select count(*) from dip_esbsend where code='"+code+"' and nvl(dr,0)=0 and pk_esbsend<>'"+pk+"'  and isfolder='N' and pk_xt='"+pk_xt+"'";
			sql = "select count(*) from dip_esbsend where code='"+code+"' and nvl(dr,0)=0 and pk_esbsend<>'"+pk+"'  and pk_xt='"+pk_xt+"'";
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
		DipEsbSendVO vo=(DipEsbSendVO) node.getData();
		if(vo!=null&&vo.getIsfolder()!=null&&vo.getIsfolder().booleanValue()){
//			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		}else{
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
			getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
			getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
			getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
			getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
		}
		getSelfUI().updateButtonUI();
	}

	@Override
	protected void onBoDelete() throws Exception {
		
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确认要删除?");
		if(flag == 1){
			
			String pk_node =(String) getSelectNode().getNodeID();
			if("".equals(pk_node)){
				NCOptionPane.showMessageDialog(this.getBillUI(),"请选择要删除的节点。");
				return ;
			}
			VOTreeNode node1=(VOTreeNode) getSelectNode().getParent();
			DipEsbSendVO vo = (DipEsbSendVO) HYPubBO_Client.queryByPrimaryKey(DipEsbSendVO.class, pk_node);
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
				getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
									.getPath()));
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
		DipEsbSendVO hvo=(DipEsbSendVO) treeNode.getData();
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
		super.onBoEdit();
		if(hvo.getDeeltype().equals("m1")||hvo.getDeeltype().equals("m4")||hvo.getDeeltype().equals("m5")||hvo.getDeeltype().equals("m6")){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("bakpath").setEdit(true);
		}else{
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("bakpath").setEdit(false);
		}
		if(hvo.getDeeltype().equals("m3")||hvo.getDeeltype().equals("m4")||hvo.getDeeltype().equals("m5")){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("server").setEdit(true);
		}else{
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("server").setEdit(false);
		}
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
		case IBtnDefine.dealFile:
			onBoDealFile();
			break;
		case IBtnDefine.uploadFile:
			onBoUploadDownFile();
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
			
			nc.vo.dip.esbfilesend.MyBillVO billvo=(nc.vo.dip.esbfilesend.MyBillVO) getBufferData().getCurrentVO();
			if(billvo!=null&&billvo.getParentVO()!=null){
				DipEsbSendVO hvo=(DipEsbSendVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_ESBSEND,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_ESBSEND), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipEsbSendVO) HYPubBO_Client.queryByPrimaryKey(DipEsbSendVO.class, hvo.getPrimaryKey());
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
		DipEsbSendVO vo =(DipEsbSendVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipEsbSendVO[] listvos=(DipEsbSendVO[]) HYPubBO_Client.queryByCondition(DipEsbSendVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_esbsend<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
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
					vo=(DipEsbSendVO) HYPubBO_Client.queryByPrimaryKey(DipEsbSendVO.class, vo.getPrimaryKey());
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
		DipEsbSendVO vo =(DipEsbSendVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipEsbSendVO.class, "pk_sys='"+vo.getPrimaryKey()+"'");
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
		DipEsbSendVO hvo=(DipEsbSendVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipEsbSendVO newhvo=new DipEsbSendVO();
		newhvo.setPk_sys(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipEsbSendVO[] listvos=(DipEsbSendVO[]) HYPubBO_Client.queryByCondition(DipEsbSendVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
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
				newhvo=(DipEsbSendVO) HYPubBO_Client.queryByPrimaryKey(DipEsbSendVO.class, pk);
				
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
		DipEsbSendVO hvo=null;
		try {
			hvo = (DipEsbSendVO) HYPubBO_Client.queryByPrimaryKey(DipEsbSendVO.class, node.getNodeID().toString());
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
	private void onBoUploadDownFile(){
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
		if(pk==null||pk.trim().equals("")){
			getSelfUI().showErrorMessage("该节点数据错误");
			return;
		}
		AskMBDLG ask=new AskMBDLG(getSelfUI(),null,"模板","        请选择操作类型?",new String[]{"上传文件","下载文件"});
		ask.showModal();
		if(ask.getRes()==0){
			onBoUploadFile(pk);
		}else if(ask.getRes()==1){
			onBoDownloadFile(pk);
		}
	}
	
	private void onBoUploadFile(String pk) {
//		VOTreeNode node=getSelectNode();
//		if(SJUtil.isNull(node)){
//			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
//			return;
//		}
//		Object fpk=node.getParentnodeID();
//		if(fpk==null){
//			getSelfUI().showErrorMessage("请选择子节点操作!");
//			return;
//		}
//		String pk=node.getNodeID().toString();
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(jfileChooser.DIRECTORIES_ONLY);
		if (jfileChooser.showDialog(getSelfUI(), "文件上传")/*.showSaveDialog(getSelfUI())*/ == javax.swing.JFileChooser.CANCEL_OPTION)
			return;
		String path = jfileChooser.getSelectedFile().toString();
		RetMessage rt=doQTSend(pk,path);
		MessageDialog.showHintDlg(getBillUI(), "提示", rt.getIssucc()?"上传成功":"上传失败!\n"+rt.getMessage());
	}

	private void onBoDownloadFile(String pk){
		DipEsbSendVO vo=null;
		if(getSelectNode().getData() instanceof DipEsbSendVO){
			vo=(DipEsbSendVO) getSelectNode().getData();
		} else{
			return;
		}
		FilepathVO filepathvo=null;
		try {
			filepathvo = (FilepathVO) HYPubBO_Client.queryByPrimaryKey(FilepathVO.class, vo.getFilepath());
		} catch (UifException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(filepathvo==null){
			return;
		}
		
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		if (jfileChooser.showSaveDialog(getSelfUI())/*.showSaveDialog(getSelfUI())*/ == javax.swing.JFileChooser.CANCEL_OPTION)
			return;
		String selectPath = jfileChooser.getSelectedFile().toString();
		String path=filepathvo.getDescriptions();
		if(path==null||path.trim().equals("")){
			getSelfUI().showErrorMessage("服务器路径不能为空！");
		}
		if(!(path.endsWith("/")||path.endsWith("\\"))){
			path=path+"/";
		}
		path=path.replace("\\", "/");
		if(!(selectPath.endsWith("/")||selectPath.endsWith("\\"))){
			selectPath=selectPath+"/";
		}
		selectPath=selectPath.replace("\\", "/");
		String name=vo.getSendfilename();//文件名称。
		String filePath="";
		String selectFilePath="";
		if(!(name==null||name.trim().equals(""))){
			//下载一个文件
			 filePath=path+name;
			 selectFilePath=selectPath+name;
			 File downfile=new File(selectFilePath);
			 if(downfile.exists()){
				if( MessageDialog.ID_NO==getSelfUI().showYesNoMessage("文件已经存在是否覆盖？")){
					return;	
				}
				 
			 }
			 byte[] bytes=null;
				try {
					bytes	=iq.downLoadFile(filePath);
					
				} catch (BusiBeanException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				if(bytes==null){
					getSelfUI().showErrorMessage(filePath+" 文件不存在");
					return;
				}
				if(bytes!=null){
					writeFileToClient(bytes, selectFilePath);		
					getSelfUI().showWarningMessage("文件下载完成");
				}

		}else{
			//下载整个文件夹
			//文件夹名称。
			File files[]=null;
			RetMessage ret=iq.getFileNamesList(path);
			if(ret!=null&&ret.getIssucc()&&ret.getfilename()!=null&&ret.getfilename().size()>0){
				List<String>list=ret.getfilename();
				if(list!=null&&list.size()>0){
					int count=0;
					for(int i=0;i<list.size();i++){
						String filename=list.get(i);
						if(filename!=null&&filename.length()>0){
							String filePath1=path+filename;
							String selectFilePath1=selectPath+filename;
							 byte[] bytes=null;
								try {
									bytes	=iq.downLoadFile(filePath1);
									
								} catch (BusiBeanException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
								if(bytes!=null){
									writeFileToClient(bytes, selectFilePath1);		
									count++;
								}
						}
						
					}
					getSelfUI().showWarningMessage("文件下载完成("+count+"个文件)");
					return;
				}else{
					
				}
			}else{
				getSelfUI().showErrorMessage(ret.getMessage());
				return;
			}
		}
		
		
		
		
	}
	
	
	public void writeFileToClient(byte[] bytes,String filepath){
		File file=new File(filepath);
		FileOutputStream out=null;
		try {
			 out=new FileOutputStream(file);
			out.write(bytes);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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
		DipEsbSendVO dvo=(DipEsbSendVO) node.getData();

		//得到主键值
		String pk=dvo.getPrimaryKey();
		dvo=(DipEsbSendVO) HYPubBO_Client.queryByPrimaryKey(DipEsbSendVO.class, pk);
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
	public void onBoDealFile() throws Exception{
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
		Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确定处理文件！");
		if(flag==1){
			pk=node.getNodeID().toString();
			new Thread() {
				@Override
				public void run() {
					BannerDialog dialog = new BannerDialog(getSelfUI());
					dialog.setTitle("正在处理，请稍候...");
					dialog.setStartText("正在处理，请稍候...");
								
					try {
						dialog.start();	
						ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
						RetMessage rm=ite.doESBSendTask(pk);
						if(rm!=null&&rm.getIssucc()){
							if(rm.getMessage()==null||rm.getMessage().trim().equals("")){
								MessageDialog.showHintDlg(getSelfUI(), SJUtil.getYWnameByLX(IContrastUtil.YWLX_ESBSEND), "处理成功！");
							}else{
								MessageDialog.showHintDlg(getSelfUI(), SJUtil.getYWnameByLX(IContrastUtil.YWLX_ESBSEND),rm.getMessage());
							}
						}else{
							getSelfUI().showErrorMessage("处理失败！"+rm.getMessage());
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
		if(pk==null || pk.length()==0 || "".equals(pk)){
			rm=new RetMessage(false,"没有传入相应的数据发送主键!");
			return rm;
		}
		//数据发送vo
		DipEsbSendVO ddvo=null;
		try {
			ddvo = (DipEsbSendVO) HYPubBO_Client.queryByPrimaryKey(DipEsbSendVO.class, pk);
		} catch (UifException e1) {
			e1.printStackTrace();
			rm=new RetMessage(false,"查询对应的数据发送vo出错！"+pk+e1.getMessage());
			return rm;
		}
		if(ddvo==null){
			rm=new RetMessage(false,"没有找到对应的数据发送vo:"+pk);
			return rm;
		}
		String tofilepath=getFilepath(ddvo.getFilepath());
		if(tofilepath==null||tofilepath.length()<=0){
			return new RetMessage(false,"没有定义上传目录");
		}
		

			java.io.File file = new java.io.File(path);
			byte ba[] = new byte[(int) file.length()];
			FileInputStream fis =null;
			try {
				fis = new FileInputStream(file);
				int nBebinReadLoc = 0;
				do {
					int nReadedInSize = fis.read(ba, nBebinReadLoc, ba.length
							- nBebinReadLoc);
					nBebinReadLoc += nReadedInSize;
				} while (nBebinReadLoc < ba.length);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			RetMessage rs = null;
			try {
				rs = iqf.upLoadFile(path, ba,tofilepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rs;
	}
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public String getFilepath(String pkFilepath){
		String fp="";
		if(pkFilepath==null||pkFilepath.length()<=0){
			fp="";
		}else{
			String sql5 = "select descriptions from dip_filepath where pk_filepath='" + pkFilepath + "' and nvl(dr,0)=0";
			try {
				fp = iqf.queryfield(sql5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fp;
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
		DipEsbSendVO hvo=(DipEsbSendVO) arg0.getData();
		if(hvo.getDeeltype()!=null){
			String deeltype=hvo.getDeeltype();
			if(deeltype.equals("m4")||deeltype.equals("m5")){
				UIRefPane ref=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("server").getComponent();
				FtpSourceRegisterRefModel ftpModel =new FtpSourceRegisterRefModel();
				ref.setRefModel(ftpModel);
			}
			if(deeltype.equals("m3")){
				UIRefPane ref=(UIRefPane)  getBillCardPanelWrapper().getBillCardPanel().getHeadItem("server").getComponent();
				
				ZDJSRefModel refModel=new ZDJSRefModel();
				ref.setRefModel(refModel);
			}
		}
			nc.vo.dip.esbfilesend.MyBillVO billvo =new nc.vo.dip.esbfilesend.MyBillVO();
			// 设置子VO
			billvo.setParentVO(hvo);
			billvo.setChildrenVO(new DipEsbSendVO[]{hvo});
			// 显示数据
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));
//			UIRefPane rp=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("server").getComponent();
//			ZDJSRefModel zm=(ZDJSRefModel) rp.getRefModel();
//			zm.addWherePart("dip_msr.mestype='文件流' and  dip_messageplugregister.name='ESB消息'  ");

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
//			DipEsbSendVO send=(DipEsbSendVO) node.getData();
//			if(send!=null&&send.getIsfolder()!=null&&send.getIsfolder().booleanValue()){
//				getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//				getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.uploadFile).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.dealFile).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
//				getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//			}else{
//				getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//				getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//			}
			getSelfUI().onTreeSelectSetButtonState(node);
			getSelfUI().updateButtonUI();
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