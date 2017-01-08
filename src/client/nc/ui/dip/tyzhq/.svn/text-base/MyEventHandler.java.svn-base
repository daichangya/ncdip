package nc.ui.dip.tyzhq;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;

import nc.bs.dip.txt.TxtDataVO;
import nc.bs.excel.pub.ExpExcelVO;
import nc.bs.excel.pub.ExpToExcel;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.ITaskExecute;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.bd.ref.ContDataRefModel;
import nc.ui.bd.ref.DataDefinitTableTreeRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.tyzhq.iniufoenv.InitUFOEnvDlg;
import nc.ui.dip.tyzhq.tygs.AskMBDLG;
import nc.ui.dip.tyzhq.tygs.DapFormuDefUI;
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
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.tyzhq.DipTYZHDatachangeBVO;
import nc.vo.dip.tyzhq.DipTYZHDatachangeHVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.dip.warningset.MyBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{



	/**
	 * @author cl
	 * @date2011-6-13
	 * @description 复制模板
	 */
	nc.vo.dip.credence.MyBillVO copymap=null;



	DataChangeClientUI myui=(DataChangeClientUI) this.getBillUI();
	public MyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}

	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private DataChangeClientUI getSelfUI() {
		return (DataChangeClientUI) getBillUI();
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
		newBodyVO.setAttributeValue("pk_datachange_h", parent == null ? null : parent.getNodeID());

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
	protected void onBoSave() throws Exception {
		
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd!=null){
			bd.dataNotNullValidate();
		}

		//校验编码唯一 2011-7-4 cl
		DataChangeClientUI ui=(DataChangeClientUI) getBillUI();
		String pk=(String) ui.getBillCardPanel().getHeadItem("pk_tyzhq_h").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		VOTreeNode node=getSelectNode();
		String pk_xt=(String) ui.getBillCardPanel().getHeadItem("pk_xt").getValueObject();
		String code=(String) ui.getBillCardPanel().getHeadItem("code").getValueObject();
		IUAPQueryBS bs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		Collection ccode=bs.retrieveByClause(DipTYZHDatachangeHVO.class,"code='"+code+"' and isfolder='N' and pk_tyzhq_h <> '"+pk+"' and pk_xt='"+pk_xt+"' and nvl(dr,0)=0");
		Collection ccode=bs.retrieveByClause(DipTYZHDatachangeHVO.class,"code='"+code+"'  and pk_tyzhq_h <> '"+pk+"' and pk_xt='"+pk_xt+"' and nvl(dr,0)=0");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage(IContrastUtil.getCodeRepeatHint("编码", code));
				return;
			}
		}
		String sourcetab=(String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourcetab").getValueObject();
		String targettab=(String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("targettab").getValueObject();
//		if(sourcetab.equals(targettab)){
//			getSelfUI().showErrorMessage("目标表和来源表不能为同一张表！");
//			return;
//		}

		//获得界面原始数据的vo
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);


		//获得界面新数据vo
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		DipTYZHDatachangeHVO hvo=(DipTYZHDatachangeHVO) checkVO.getParentVO();
		DipTYZHDatachangeBVO[] bvo=(DipTYZHDatachangeBVO[]) checkVO.getChildrenVO();


		Object o = null;
		ISingleController sCtrl = null;

		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether())
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		else{

			DipTYZHDatachangeBVO[] bvos=(DipTYZHDatachangeBVO[]) checkVO.getChildrenVO();
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), billVO);
			List<DipTYZHDatachangeBVO> changevo=new ArrayList<DipTYZHDatachangeBVO>();
			if(bvos!=null&&bvos.length>=0){
				String pkb="";
				for(int k=0;k<bvos.length;k++){
					pkb=pkb+",'"+bvos[k].getPk_lyzd()+"'";
					if(bvos[k].getPrimaryKey()!=null){
						changevo.add(bvos[k]);
					}
				}
				if(pkb!=null&&pkb.length()>0){
					HYPubBO_Client.deleteByWhereClause(DipTYZHDatachangeBVO.class, "pk_tyzhq_h='"+billVO.getParentVO().getPrimaryKey()+"' and pk_lyzd not in ("+pkb.substring(1)+")");
				}
				if(changevo!=null&&changevo.size()>0){
					HYPubBO_Client.updateAry(changevo.toArray(new DipTYZHDatachangeBVO[changevo.size()]));
				}
				HYPubBO_Client.deleteByWhereClause(DipTYZHDatachangeBVO.class, "pk_tyzhq_h='"+billVO.getParentVO().getPrimaryKey()+"' and pk_lyzd not in (select pk_datadefinit_b from dip_datadefinit_b where pk_datadefinit_h='"+targettab+"' and nvl(dr,0)=0)");
				billVO.setChildrenVO(HYPubBO_Client.queryByCondition(DipTYZHDatachangeBVO.class, "pk_tyzhq_h='"+billVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=0"));
			}
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
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());

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
	@Override
	public  void  onTreeSelected(VOTreeNode arg0){
		try {
			DipTYZHDatachangeHVO  hvo=(DipTYZHDatachangeHVO) arg0.getData();
			String pk_datachange_h=hvo.getPrimaryKey();
			SuperVO[] vos=HYPubBO_Client.queryByCondition(DipTYZHDatachangeBVO.class,"pk_tyzhq_h='"+pk_datachange_h+"' and  isnull(dr,0)=0");
			nc.vo.dip.tyzhq.MyBillVO billvo=new nc.vo.dip.tyzhq.MyBillVO();
			//设置主VO
			billvo.setParentVO(hvo);
			//设置VO
			billvo.setChildrenVO(vos);				
			//显示数据
			getBufferData().addVOToBuffer(billvo);

			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(),"" + (getBufferData().getVOBufferSize() - 1));
			if(hvo!=null){
				if(!hvo.getIsfolder().booleanValue()){
					UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourcetab").getComponent();
					/*liyunzhe modify 来源表，目标表修改成树形参照 2012-06-04 strat*/
					DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
					model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
					model.addWherePart(" and tabsoucetype='自定义'");
					uir.setRefModel(model);
					UIRefPane uir1=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("targettab").getComponent();
					 model=new DataDefinitTableTreeRefModel();
					model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
					model.addWherePart(" and tabsoucetype='自定义'");
					uir1.setRefModel(model);
					/*liyunzhe modify 来源表目标表修改成树形参照 2012-06-04 end*/
				}
			}
		} catch (UifException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onBoDelete() throws Exception {
		VOTreeNode tempNode=getSelectNode();
		
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要删除的节点！");
			return ;
		}
		String pk_node=(String)tempNode.getParentnodeID();
		if(pk_node==null||pk_node.equals("")){
			getSelfUI().showErrorMessage("系统节点不能做删除操作！");
			return ;
		}
		
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确认要删除?");
		if(flag == 1){
			pk_node = ((DataChangeClientUI) getBillUI()).selectnode;
			if("".equals(pk_node)){
				NCOptionPane.showMessageDialog(this.getBillUI(),"请选择要删除的节点。");
				return ;
			}
			VOTreeNode node1=(VOTreeNode) tempNode.getParent();
			DipTYZHDatachangeBVO[] vos = (DipTYZHDatachangeBVO[])HYPubBO_Client.queryByCondition(DipTYZHDatachangeBVO.class, " pk_tyzhq_h='"+pk_node+"' and isnull(dr,0)=0  ");
			if(vos!=null && vos.length>0){
				for(DipTYZHDatachangeBVO bvo : vos ){
					HYPubBO_Client.delete(bvo);
				}
			}	
			DipTYZHDatachangeHVO vo = (DipTYZHDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYZHDatachangeHVO.class, pk_node);
			if(vo!=null)
				HYPubBO_Client.delete(vo);

//			更新树
			this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
			if(node1!=null){
				getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
									.getPath()));
				}
		}


	}
	private boolean isnew=false;
	@Override
	protected void onBoEdit() throws Exception {
		VOTreeNode node=getSelectNode();
		if(node==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		DipTYZHDatachangeHVO hvo=(DipTYZHDatachangeHVO) node.getData();
		if(hvo.getFpk()==null||hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("请选择节点操作！");
			return ;
		}
		
		super.onBoEdit();


		DataChangeClientUI ui= (DataChangeClientUI) getBillUI();

		String pk=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_tyzhq_h").getValueObject();
		if(pk==null&&pk.equals("")){
			isnew=true;
		}else {
			DipTYZHDatachangeHVO vo=(DipTYZHDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYZHDatachangeHVO.class, pk);
			if(SJUtil.isNull(vo)){
				isnew=true;
			}else{
				isnew=false;
			}

			UIRefPane rf=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_zdym").getComponent();
			ContDataRefModel cdm=(ContDataRefModel) rf.getRefModel();
			cdm.setWhere(" nvl(dip_datadefinit_b.dr,0)=0 and dip_datadefinit_b.pk_datadefinit_h='"+vo.getTargettab()+"'");
		}
		
		this.getButtonManager().getButton(IBtnDefine.initUFOENV).setEnabled(false);
//		this.getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
//		this.getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
		this.getSelfUI().updateButtonUI();

	}
	@Override
	protected void onBoCancel() throws Exception{
		//2011-4-27 程莉 点击取消按钮时，将“启用”和“停用”按钮设置为可用
		super.onBoCancel();

		//2011-6-28 程莉 修改取消时，nc组织编码值显示问题
		VOTreeNode node=getSelectNode();
		if(node !=null){
			String selectnode=(String) node.getNodeID();
			if(selectnode !=null && !"".equals(selectnode) && selectnode.length()>0){
			}
		}

		getSelfUI().delstr ="";//cl 2011-07-04 取消清空删行缓存
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
//		DipTYZHDatachangeHVO hvo=(DipTYZHDatachangeHVO) node.getData();
//		if(hvo!=null&&hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
//			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//			getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.Conversion).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
//			getButtonManager().getButton(IBtnDefine.initUFOENV).setEnabled(false);
//		}else{
//			getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//		}
//		getSelfUI().updateButtonUI();
		
	}







	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.YuJing://预警
			onBoWarning();
			break;
		case IBtnDefine.Conversion://转换
			conversion();
			break;
		case IBtnDefine.CONTROL://模板，暂时不用了
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
		case IBtnDefine.initUFOENV:
			onBoInitUFOENV();
			break;
		case IBtnDefine.MBZH:
			onBoMBZH();
		}
	}


	/**
		 * @desc 文件移动
		 * */
		public void onBoMoveFolder() throws Exception{
			
			nc.vo.dip.tyzhq.MyBillVO billvo=(nc.vo.dip.tyzhq.MyBillVO) getBufferData().getCurrentVO();
			if(billvo!=null&&billvo.getParentVO()!=null){
				DipTYZHDatachangeHVO hvo=(DipTYZHDatachangeHVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_TYZH,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_TYZH), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipTYZHDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYZHDatachangeHVO.class, hvo.getPrimaryKey());
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
	public void onBoMBZH() throws Exception {
		VOTreeNode node=getSelectNode();
		if(node==null){
			getSelfUI().showErrorMessage("选择节点数据错误");
			return;
		}
		DipTYZHDatachangeHVO hvo=null;
		if(node.getData() instanceof DipTYZHDatachangeHVO){
			hvo=(DipTYZHDatachangeHVO) node.getData();
		}else{
			getSelfUI().showErrorMessage("选择节点数据错误");
			return;
		}
		AskMBDLG ask=new AskMBDLG(getSelfUI(),hvo,"模板","        请选择操作类型?",new String[]{"导入模板","导出模板","复制模板"});
		ask.showModal();
		int result=ask.getRes();
		if(result==0){
			onBoImport();
		}else if(result==1){
			onBoExport();
		}else if(result==2){
		//	String selectPk_datadefinit_h=(String) ask.getOrgnizeRefPnl().getRefModel().getValue("dip_datadefinit_h.pk_datadefinit_h");
			if(!(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue())){
				getBillUI().showErrorMessage("非文件夹节点不能做复制操作");
				return;
			}
			String selectPk_tyzhq_h=(String) ask.getOrgnizeRefPnl().getRefModel().getValue("dip_tyzhq_h.pk_tyzhq_h");
			if(selectPk_tyzhq_h==null||selectPk_tyzhq_h.trim().length()!=20){
				return;
			}
			DipTYZHDatachangeHVO newhvo=null;
			newhvo=(DipTYZHDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYZHDatachangeHVO.class, selectPk_tyzhq_h);
			newhvo.setPk_tyzhq_h(null);
			newhvo.setFpk(hvo.getPk_tyzhq_h());
			SuperVO[] supervos=HYPubBO_Client.queryByCondition(DipTYZHDatachangeBVO.class, " pk_tyzhq_h='"+selectPk_tyzhq_h+"' and nvl(dr,0)=0");
			if(supervos!=null&&supervos.length>0){
				for(int i=0;i<supervos.length;i++){
					SuperVO vo=supervos[i];
					vo.setAttributeValue("pk_tyzhq_h", null);
					vo.setPrimaryKey(null);
				}
				
			}
			onBoAdd(null);
			nc.vo.dip.tyzhq.MyBillVO billvo=new nc.vo.dip.tyzhq.MyBillVO();
			billvo.setParentVO(newhvo);
			billvo.setChildrenVO(supervos);
			getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(billvo);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
//			String targetTabPK=hvo.getTargettab();
//			//根据选择的目标表名和节点目标表去匹配，如果存在相同的字段，就复制该格式，如果没有相同的字段，就提示，没有相同的字段，不能复制。
//			String sql=" select b.pk_datadefinit_b,b.ename from Dip_Datadefinit_b b where b.pk_datadefinit_h='"+selectPk_datadefinit_h+"' and b.ename " +
//					   " in( " +
//					   "    select bb.ename from dip_datadefinit_b bb where bb.pk_datadefinit_h ='"+targetTabPK+"'"+
//					   " ) ";
//			
//			String sql1=" select bb.ename,bb.pk_datadefinit_b from dip_datadefinit_b bb where bb.pk_datadefinit_h ='"+targetTabPK+"'";
//			List list=iq.queryListMapSingleSql(sql);//得到需要复制到表体目标表的字段(表体目标表的字段编码和选中的表的字段相等的)。pk_datadefinit_b，ename
//			List list1=iq.queryListMapSingleSql(sql1);//插入到目标表的字段。
//			
//			
//			
//			if(list==null||list.size()==0){
//				getSelfUI().showErrorMessage("没有相同的字段，不能复制模板");
//				return;
//			}else{
//				List<DipTYZHDatachangeBVO> copylist=new ArrayList<DipTYZHDatachangeBVO>();
//				//得到需要复制的记录
//				copylist=getCopyListData(list, list1, hvo.getPk_tyzhq_h(), selectPk_tyzhq_h);
//				//先删除主表vo对应的子表记录，在把复制到的子表vo插入到选中的节点的子表
//				if(copylist!=null&&copylist.size()>0){
//					String sqlupdate=" update dip_tyzhq_b b set dr=1 where b.pk_tyzhq_h='"+hvo.getPk_tyzhq_h()+"' and nvl(dr,0)=0 ";;
//					iq.exesql(sqlupdate);
//					HYPubBO_Client.insertAry(copylist.toArray(new DipTYZHDatachangeBVO[0]));
//					SuperVO vos[]=HYPubBO_Client.queryByCondition(DipTYZHDatachangeBVO.class, " pk_tyzhq_h='"+hvo.getPk_tyzhq_h()+"'");
//					getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_tyzhq_b").setBodyDataVO(vos);
//					getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_tyzhq_b").execLoadFormula();
//					getSelfUI().updateUI();
//					nc.vo.dip.tyzhq.MyBillVO billvo=new nc.vo.dip.tyzhq.MyBillVO();
//					//设置主VO
//					billvo.setParentVO(hvo);
//					//设置VO
//					billvo.setChildrenVO(vos);				
//					//显示数据
//					getBufferData().addVOToBuffer(billvo);
//					getBufferData().refresh();
//
//					getBillTreeCardUI().getTreeToBuffer().put(node.getNodeID(),"" + (getBufferData().getVOBufferSize() - 1));
//					getSelfUI().showWarningMessage("复制成功");
//				}
//				
//				
//			}
			
		}
	}
	/**
	 * 得到需要复制的字表vo
	 * @param list
	 * @param list1
	 * @param pk_tyzhq_h 选中节点的主表pk
	 * @param selectPk_tyzhq_h 选中复制参照的主表pk
	 * @return
	 */
	public List<DipTYZHDatachangeBVO> getCopyListData(List list,List list1,String pk_tyzhq_h,String selectPk_tyzhq_h){
		StringBuffer copysb=new StringBuffer();
		Map<String,String> copymap=new HashMap<String,String>();
		Map<String,String> pastemap=new HashMap<String,String>();
		for(int i=0;i<list.size();i++){
			Map map=(Map) list.get(i);
			String pk=map.get("pk_datadefinit_b".toUpperCase())==null?"":map.get("pk_datadefinit_b".toUpperCase()).toString();
			String ename=map.get("ename".toUpperCase())==null?"":map.get("ename".toUpperCase()).toString();
			copymap.put(pk, ename);
			if(pk.length()>0){
				copysb.append("'"+pk+"',");	
			}
			
		}
		
		for(int i=0;i<list1.size();i++){
			Map map=(Map) list1.get(i);
			String pk=map.get("pk_datadefinit_b".toUpperCase())==null?"":map.get("pk_datadefinit_b".toUpperCase()).toString();
			String ename=map.get("ename".toUpperCase())==null?"":map.get("ename".toUpperCase()).toString();
			pastemap.put(ename, pk);
		}
		
		DipTYZHDatachangeBVO copyVos[]=null;
		List<DipTYZHDatachangeBVO> copylist=new ArrayList<DipTYZHDatachangeBVO>();
		try {
			copyVos=(DipTYZHDatachangeBVO[]) HYPubBO_Client.queryByCondition(DipTYZHDatachangeBVO.class, " pk_lyzd in("+copysb.substring(0, copysb.length()-1)+") and pk_tyzhq_h ='"+selectPk_tyzhq_h+"'");
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(copyVos!=null&&copyVos.length>0){
			for(int i=0;i<copyVos.length;i++){
				DipTYZHDatachangeBVO bvo=copyVos[i];
				String pk_lyzd=bvo.getPk_lyzd();
				if(copymap.get(pk_lyzd)!=null&&copymap.get(pk_lyzd).length()>0){
					String pk_paste=pastemap.get(copymap.get(pk_lyzd));
					bvo.setPk_lyzd(pk_paste);
					bvo.setPk_tyzhq_b(null);
					bvo.setPk_tyzhq_h(pk_tyzhq_h);
					copylist.add(bvo);
				}
			}
			
		}else{
			getSelfUI().showErrorMessage("相同字段没有模板记录");
			return null;
		}
		return copylist;
	}
	

	/**
	 * @desc 初始化环境按钮
	 * */
	public void onBoInitUFOENV() {
		InitUFOEnvDlg a=new InitUFOEnvDlg(getSelfUI());
		a.showModal();
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
		DipTYZHDatachangeHVO vo =(DipTYZHDatachangeHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipTYZHDatachangeHVO[] listvos=(DipTYZHDatachangeHVO[]) HYPubBO_Client.queryByCondition(DipTYZHDatachangeHVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_tyzhq_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
			List<String> listcode=new ArrayList<String>();
			List<String> listname=new ArrayList<String>();
			if(listvos!=null&&listvos.length>0){
				for(int i=0;i<listvos.length;i++){
					listcode.add(listvos[i].getCode());
					listname.add(listvos[i].getName());
				}
			}
				
//			AddFolderDlg adlg=new AddFolderDlg(getBillUI(),vo.getCode(),vo.getName());
			AddFolderDlg adlg=new AddFolderDlg(getBillUI(),listcode,listname,vo.getCode(),vo.getName());
			adlg.showModal();
			if(adlg.isOk()){
				String tempc=adlg.getCode();
				String tempn=adlg.getName();
				if(!tempc.equals(vo.getCode())||!tempn.equals(vo.getName())){
					vo.setCode(tempc);
					vo.setName(tempn);
					HYPubBO_Client.update(vo);
					vo=(DipTYZHDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYZHDatachangeHVO.class, vo.getPrimaryKey());
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
		DipTYZHDatachangeHVO vo =(DipTYZHDatachangeHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipTYZHDatachangeHVO.class, "fpk='"+vo.getPrimaryKey()+"'");
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
		DipTYZHDatachangeHVO hvo=(DipTYZHDatachangeHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipTYZHDatachangeHVO newhvo=new DipTYZHDatachangeHVO();
		newhvo.setFpk(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipTYZHDatachangeHVO[] listvos=(DipTYZHDatachangeHVO[]) HYPubBO_Client.queryByCondition(DipTYZHDatachangeHVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
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
				newhvo=(DipTYZHDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYZHDatachangeHVO.class, pk);
				
				if (getUITreeCardController().isAutoManageTree()) {	
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
		DipTYZHDatachangeHVO hvo=null;
		try {
			hvo = (DipTYZHDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYZHDatachangeHVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}

		ControlHVO chvo=new ControlHVO();
		chvo.setBustype(SJUtil.getYWnameByLX(IContrastUtil.YWLX_TYZH));
		chvo.setCode(hvo.getCode());
		chvo.setName(hvo.getName());

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPrimaryKey(),IContrastUtil.YWLX_TYZH,hvo.getSourcetab()+","+hvo.getTargettab());
		cd.showModal();
	}
	String datachangepk;
	public void conversion(){
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getBillUI().showErrorMessage("请选择要操作的节点！");
			return;
		}
		DipTYZHDatachangeHVO hvo=(DipTYZHDatachangeHVO) node.getData();
		String fpk=hvo.getFpk();
		if(SJUtil.isNull(fpk)||fpk.length()<=0){

			getSelfUI().showErrorMessage("系统节点不可编辑！");
			return;
		}
		datachangepk=hvo.getPrimaryKey();
		new Thread() {
			@Override
			public void run() {
				BannerDialog dialog = new BannerDialog(getSelfUI());
				dialog.setTitle("正在转换，请稍候...");
				dialog.setStartText("正在转换，请稍候...");
							
				try {
					dialog.start();		
					ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
					RetMessage rm=ite.doTYZHTask(datachangepk);
					if(rm.getIssucc()){
						MessageDialog.showHintDlg(getSelfUI(), "数据转换", "转换成功！"+rm.getMessage());
					}else{
						getSelfUI().showErrorMessage(rm.getMessage());
					}
				} catch (Throwable er) {
					er.printStackTrace();
				} finally {
					dialog.end();
				}
			}			
		}.start();
		
	}
	//预警弹出框
	public void onBoWarning() throws UifException{
		//获取选中的节点
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		//得到当前VO类
		DipTYZHDatachangeHVO dvo=(DipTYZHDatachangeHVO) node.getData();
		//得到主键值
		String fpk=dvo.getFpk();
		String pk=dvo.getPrimaryKey();
		dvo=(DipTYZHDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYZHDatachangeHVO.class, pk);
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

			dwvo.setPk_sys(fpk);
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

		WarningsetDlg wd=new WarningsetDlg(getSelfUI(),bill, isadd,dvo.getPk_xt(),3);		
		wd.showModal();		
//		new WarningSetClientUI().init(bill, isadd,node.getParentnodeID().toString(),3);
	}

	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要增加的系统节点！");
			return ;
		}
		DipTYZHDatachangeHVO hvo=(DipTYZHDatachangeHVO) tempNode.getData();
		if(hvo.getFpk()==null||!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("请选择文件夹操作！");
			return ;
		}
		super.onBoAdd(bo);
		getSelfUI().getBillCardPanel().setHeadItem("fpk", hvo.getPrimaryKey());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", hvo.getPk_xt());

		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourcetab").getComponent();
		/*liyunzhe modify 来源表，目标表修改成树形参照 2012-06-04 strat*/
		DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
		model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
		model.addWherePart(" and tabsoucetype='自定义'");
		uir.setRefModel(model);
		UIRefPane uir1=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("targettab").getComponent();
		model=new DataDefinitTableTreeRefModel();
		model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
		model.addWherePart(" and tabsoucetype='自定义'");
		uir1.setRefModel(model);
		/*liyunzhe modify 来源表目标表修改成树形参照 2012-06-04 end*/
		isnew=true;
		this.getButtonManager().getButton(IBtnDefine.initUFOENV).setEnabled(false);
		this.getSelfUI().updateButtonUI();
	}
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getButtonManager().getButton(IBtnDefine.initUFOENV).setEnabled(false);
		getSelfUI().updateButtonUI();
	}

	public void onLoadTable(String targettab) {
		int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount(); 
		if(rowcount>0){
			getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(0,0,false,false);
			for(int i=0;i<rowcount;i++){
				try {
					onBoLineDel();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(targettab!=null&&targettab.length()>0){
			DipDatadefinitBVO[] vos = null;
			try {
				vos = (DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+targettab+"' and nvl(dr,0)=0");
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(vos!=null&&vos.length>0){
				DipTYZHDatachangeBVO[] bvos=new DipTYZHDatachangeBVO[vos.length];
				for(int i=0;i<vos.length;i++){
					bvos[i]=new DipTYZHDatachangeBVO();
					bvos[i].setPk_lyzd(vos[i].getPk_datadefinit_b());
					bvos[i].setChangformu("@"+vos[i].getEname()+"@");
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
					/*if(showlist.get(i).getPrimaryKey()!=null&&showlist.get(i).getPrimaryKey().length()>0){
						showlist.get(i).setStatus(nc.vo.pub.VOStatus.UPDATED);
					}*/
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(bvos[i], i);
					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_lyzd").getLoadFormula() );
				}
			}
		}
		
	}

	private String[] xmFoulmuer=null;
	private String[] getXmFoulmuer(){
		if(xmFoulmuer==null){
			String[] f1=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_lyzd").getLoadFormula();
			String[] f2=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("gdh").getEditFormulas();
			xmFoulmuer=new String[f1.length+f2.length];
			for(int i=0;i<xmFoulmuer.length;i++){
				if(i<f1.length){
					xmFoulmuer[i]=f1[i];
				}else{
					xmFoulmuer[i]=f2[i-f1.length];
				}
			}
		}
		return xmFoulmuer;
	}

	@Override
	protected void onBoImport() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		Object selectData=node.getData();
		DipTYZHDatachangeHVO hvo=null;
		if(selectData!=null&& selectData instanceof DipTYZHDatachangeHVO){
			hvo=(DipTYZHDatachangeHVO) selectData;
		}else{
			this.getSelfUI().showWarningMessage("该节点数据错误！");
			return;
		}
		if(!(hvo.isfolder!=null&&hvo.getIsfolder().booleanValue())){
			this.getSelfUI().showErrorMessage("非文件夹节点不能做导入操作！");
			return;
		}
		
		
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		if (jfileChooser.showSaveDialog(this.getBillUI()) == javax.swing.JFileChooser.CANCEL_OPTION)
			return;
		String path = jfileChooser.getSelectedFile().toString();
		if(!path.endsWith(".xls")){
			path=path+".xls";
		}
		File file=new File(path);
		if(!file.exists()){
			getBillUI().showErrorMessage(path+"文件不存在！");
			return;
		}
		TxtDataVO tvo=getTxtDataVO(path);
		if(tvo==null){
			return;
		}else{
			String key="5";//强制校验的列,英文名称
			Map titleMap=tvo.getTitlemap();//key 字段中文名，value 列数，控制列=4, 字段主键=0, 转换公式=3, 字段名称=2, 字段编码=1
			Map<String,String> titleMap1=new HashMap<String,String>();//{3=转换公式, 2=字段名称, 0=字段主键, 4=控制列, 1=字段编码}
			Iterator<String> it =titleMap.keySet().iterator();
			while(it.hasNext()){
				String ename=it.next();
				String col=titleMap.get(ename)==null?"":titleMap.get(ename).toString();
				titleMap1.put(col, ename);
			}
			String impFieldArray[]=EXPFIELDNAMES.split(",");
			if(impFieldArray.length!=titleMap.size()){
				this.getSelfUI().showErrorMessage("导入的excel文件列数和界面上列数不相等");
				return;
			}
			for(int w=0;w<impFieldArray.length;w++){
				String field=impFieldArray[w];
				if(!titleMap.containsKey(field)){
					this.getSelfUI().showErrorMessage("导入的excel文件没有【"+field+"】列");
					return;
				}
			}
			
			
			
			RowDataVO rowVos[]=tvo.getData();
			if(rowVos==null||rowVos.length==0){
				this.getSelfUI().showErrorMessage("导入的excel文件没有数据");
				return;
			}
			List<RowDataVO> errList=new ArrayList<RowDataVO>();
			List<DipTYZHDatachangeBVO> rightList=new ArrayList<DipTYZHDatachangeBVO>();
			DapFormuDefUI dapFormuDefUi=new DapFormuDefUI(this.getSelfUI(),hvo.getTargettab(),hvo.getPk_xt());
			
			String sSourcetal="";//来源表
			String sTargettab="";//目标表
			String sRepdatactl="";//重复控制
			String sConstatus="";//控制处理状态
			String sEndstatus="";//控制结束状态
			String sSourcetalErr="";//来源表错误
			String sTargettabErr="";//目标表错误
			String sRepdatactlErr="";//重复控制错误
			String sConstatusErr="";//控制处理状态错误
			String sEndstatusErr="";//控制结束状态错误
			StringBuffer tailErr=new StringBuffer();
			DipTYZHDatachangeHVO newhvo=new DipTYZHDatachangeHVO();
			for(int i=0;i<rowVos.length;i++){
				RowDataVO rowVo=rowVos[i];
				Hashtable valueTable=rowVo.getTab();
				
				if(i==1){
				   String[] mustColum={"1"};//必须存在字段	
				   for(int w=0;w<mustColum.length;w++){
					   Object obj=valueTable.get(mustColum[w]);//4=, 3=getDataByKey( "DIP_DD_CSXT_123123.ITEMID" , "BD_ACCSUBJ.SUBJCODE" , @ITEMID@ ), 2=科目编码, 1=ACCSUBCODE, 0=0001AA1000000000JLA2
						if(obj==null){
//							rowVo.setErrorMsg(titleMap1.get(key)+"列不能为空");
							this.getBillUI().showErrorMessage("第1行"+titleMap1.get("1")+"列不能为空");
							return;
						}
						
				   }
				   sSourcetal=valueTable.get("0")==null?"":valueTable.get("0").toString();
				   sTargettab=valueTable.get("1")==null?"":valueTable.get("1").toString();
				   sRepdatactl=valueTable.get("2")==null?"":valueTable.get("2").toString();
				   sConstatus=valueTable.get("3")==null?"":valueTable.get("3").toString();
				   sEndstatus=valueTable.get("4")==null?"":valueTable.get("4").toString();
				   if(sTargettab.trim().length()>1){
					   String value=iq.queryfield("select hh.pk_datadefinit_h from Dip_Datadefinit_h hh where hh.memorytable='"+sTargettab+"' and nvl(dr,0)=0 and pk_xt='"+hvo.getPk_xt()+"'");
					   if(value!=null&&value.trim().length()>1){
						   sTargettab=value;
						   newhvo.setTargettab(sTargettab);
							map=getTableCodeMap(value);
					   }else{
						   getBillUI().showErrorMessage(titleMap1.get("1")+sTargettab+"不存在");
						   return;
					   }
				   }
				   if(sSourcetal.trim().length()>1){
					   String value=iq.queryfield("select hh.pk_datadefinit_h from Dip_Datadefinit_h hh where hh.memorytable='"+sSourcetal+"' and nvl(dr,0)=0 ");
					   if(value!=null&&value.trim().length()>1){
						   sSourcetal=value;
						   newhvo.setSourcetab(sSourcetal);
					   }else{
						   sSourcetalErr=titleMap1.get("0")+sSourcetal+"不存在";
						   if(tailErr.length()>0){
							   tailErr.append(",");
						   }
						   tailErr.append(sSourcetalErr);
					   }
				   }
			     	
			     	if(sRepdatactl.trim().length()>1){
			     		String value=iq.queryfield("select pk_repeatdata from dip_repeatdata aa where aa.name='"+sRepdatactl+"' and nvl(dr,0)=0 ");
			     		if(value!=null&&value.trim().length()>1){
			     			sRepdatactl=value;
			     			newhvo.setRepdatactl(sRepdatactl);
			     		}else{
			     			sRepdatactlErr=titleMap1.get("2")+sRepdatactl+"不存在";
			     			if(tailErr.length()>0){
								   tailErr.append(",");
							   }
							   tailErr.append(sRepdatactlErr);
			     		}
					   }
			     	 
			     	if(sConstatus.trim().length()>1){
			     		String value=iq.queryfield("select pk_statusregist from dip_statusregist bb where bb.name='"+sConstatus+"' and nvl(dr,0)=0 ");
			     		if(value!=null&&value.trim().length()>1){
			     			sConstatus=value;
			     			newhvo.setConstatus(sConstatus);
			     		}else{
			     			sConstatusErr=titleMap1.get("3")+sConstatus+"不存在";
			     			if(tailErr.length()>0){
								   tailErr.append(",");
							   }
							   tailErr.append(sConstatusErr);
			     		}
					   }
			     	
			     	if(sEndstatus.trim().length()>1){
			     		String value=iq.queryfield("select pk_statusregist from dip_statusregist bb where bb.name='"+sEndstatus+"' and nvl(dr,0)=0 ");
			     		if(value!=null&&value.trim().length()>1){
			     			sEndstatus=value;
			     			newhvo.setEndstatus(sEndstatus);
			     		}else{
			     			sEndstatusErr=titleMap1.get("4")+sEndstatus+"不存在";
			     			if(tailErr.length()>0){
								   tailErr.append(",");
							   }
							   tailErr.append(sEndstatusErr);
			     		}
					   }
			     	
				   
				}
				Object obj=valueTable.get(key);//4=, 3=getDataByKey( "DIP_DD_CSXT_123123.ITEMID" , "BD_ACCSUBJ.SUBJCODE" , @ITEMID@ ), 2=科目编码, 1=ACCSUBCODE, 0=0001AA1000000000JLA2
				if(obj==null){
					rowVo.setErrorMsg(titleMap1.get(key)+"列不能为空");
					errList.add(rowVo);
				}else{
					if(map.containsKey(obj)){
						//这个地方可以加转换公式校验。
						StringBuffer sb=new StringBuffer();
						if(i==1&&tailErr.length()>0){
							sb.append(tailErr.toString());
						}
						DipTYZHDatachangeBVO tyzhBvo=new DipTYZHDatachangeBVO();
						DipDatadefinitBVO datadefinitBvo=(DipDatadefinitBVO) map.get(obj);
						String changeFormula=valueTable.get(titleMap.get("转换公式"))==null?"":valueTable.get(titleMap.get("转换公式")).toString();
						String conFormula=valueTable.get(titleMap.get("控制列"))==null?"":valueTable.get(titleMap.get("控制列")).toString();
//						String fieldcode=obj.toString();
//						String fieldname=
//						tyzhBvo.setFieldcode(fieldcode)
//						tyzhBvo.setFieldname(fieldname);
						if((changeFormula!=null&&changeFormula.toString().trim().length()>0)||(conFormula!=null&&conFormula.toString().trim().length()>0)){
							boolean flag=dapFormuDefUi.ischeckpass(changeFormula);
							boolean flag1=dapFormuDefUi.ischeckpass(conFormula);
							if(!flag){
								if(sb.length()>0){
									sb.append(",");
								}
								sb.append("转换公式错误");
							}
							if(!flag1){
								if(sb.length()>0){
									sb.append(",");
								}
								sb.append("控制列错误");
							}
							
							
						}
						
						if(sb.length()>0){
							sb.append("。");
							rowVo.setErrorMsg(sb.toString());
							errList.add(rowVo);
						}else{
							tyzhBvo.setChangformu(changeFormula);
							tyzhBvo.setContrl(conFormula);
							tyzhBvo.setPk_lyzd(datadefinitBvo.getPk_datadefinit_b());
							tyzhBvo.setPk_tyzhq_h(hvo.getPk_tyzhq_h());
//							tyzhBvo.setStatus()
							rightList.add(tyzhBvo);
						}
					}else{//目标表不存在该ename编码字段
						rowVo.setErrorMsg("目标表不存在"+obj.toString()+"字段。");
						errList.add(rowVo);
					}
				}
			}
			StringBuffer message=new StringBuffer();
			if(rightList!=null&&rightList.size()>0){
				//保存到数据库中
				onBoAdd(null);
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("targettab").setValue(newhvo.getTargettab());
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourcetab").setValue(newhvo.getSourcetab());
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("constatus").setValue(newhvo.getConstatus());
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("endstatus").setValue(newhvo.getEndstatus());
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("repdatactl").setValue(newhvo.getRepdatactl());
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(rightList.toArray(new DipTYZHDatachangeBVO[0]));
//				String sql=" update dip_tyzhq_b b set dr=1 where b.pk_tyzhq_h='"+hvo.getPk_tyzhq_h()+"' and nvl(dr,0)=0 ";
//				iq.exesql(sql);
//				HYPubBO_Client.insertAry(rightList.toArray(new DipTYZHDatachangeBVO[0]));
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
				message.append("导入成功!");
			}
			if(errList!=null&&errList.size()>0){
				//输出到excel中。
				ExpExcelVO expErrExcelvos[]=new ExpExcelVO[errList.size()];
				String expErrorNames=EXPFIELDNAMES+",错误";
				for(int k=0;k<errList.size();k++){
					RowDataVO errRowVo=errList.get(k);
					Hashtable errvalue=errRowVo.getTab();
					expErrExcelvos[k]=new ExpExcelVO();
					expErrExcelvos[k].setAttributeValue("line"+1, errvalue.get("0"));
					expErrExcelvos[k].setAttributeValue("line"+2, errvalue.get("1"));
					expErrExcelvos[k].setAttributeValue("line"+3, errvalue.get("2"));
					expErrExcelvos[k].setAttributeValue("line"+4, errvalue.get("3"));
					expErrExcelvos[k].setAttributeValue("line"+5, errvalue.get("4"));
					expErrExcelvos[k].setAttributeValue("line"+6, errvalue.get("5"));
					expErrExcelvos[k].setAttributeValue("line"+7, errvalue.get("6"));
					expErrExcelvos[k].setAttributeValue("line"+8, errvalue.get("7"));
					expErrExcelvos[k].setAttributeValue("line"+9, errvalue.get("8"));
					expErrExcelvos[k].setAttributeValue("line"+10, errRowVo.getErrorMsg());
				}
				String errPath=path.substring(0, path.length()-4)+"-error.xls";
				ExpToExcel toexcel=new ExpToExcel(errPath,tvo.getTableName(),expErrorNames,expErrExcelvos,null);
				if(message.length()>0){
					message=new StringBuffer("部分导入成功，请查看错误文档"+errPath);
				}else{
					message=new StringBuffer("导入失败，请查看错误文档"+errPath);
				}
			}
//			SuperVO vos[]=HYPubBO_Client.queryByCondition(DipTYZHDatachangeBVO.class, " pk_tyzhq_h='"+hvo.getPk_tyzhq_h()+"'");
//			getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_tyzhq_b").setBodyDataVO(vos);
//			getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_tyzhq_b").execLoadFormula();
//			getSelfUI().updateUI();
//			nc.vo.dip.tyzhq.MyBillVO billvo=new nc.vo.dip.tyzhq.MyBillVO();
//			//设置主VO
//			billvo.setParentVO(hvo);
//			//设置VO
//			billvo.setChildrenVO(vos);				
//			//显示数据
//			getBufferData().addVOToBuffer(billvo);
//			getBufferData().refresh();
//
//			getBillTreeCardUI().getTreeToBuffer().put(node.getNodeID(),"" + (getBufferData().getVOBufferSize() - 1));
//
//			getSelfUI().showHintMessage(message.toString());
			MessageDialog.showHintDlg(this.getSelfUI(), "提示", message.toString());
			
		}
	}
	
	private Map map=new HashMap();//表的ename，和DipDatadefinitBVO对象。
	public TxtDataVO getTxtDataVO(String path){
		TxtDataVO tvo=new TxtDataVO();
		FileInputStream fin=null;
		HSSFWorkbook book=null;
		try{
			fin=new FileInputStream(path);
			book = new HSSFWorkbook(fin);
			HSSFSheet sheet = book.getSheetAt(0);
			if(sheet == null){
				getSelfUI().showErrorMessage("导入文件格式不正确！");
				return null;
			}
			String sheetName=book.getSheetName(0);
			if(sheetName==null||sheetName.trim().equals("")){
				getSelfUI().showErrorMessage("导入文件格式不正确！");
				return null;
			}
//			String targetTabPk=hvo.getTargettab();
//			if(targetTabPk==null||targetTabPk.length()!=20){
//				return null;
//			}
//			DipDatadefinitHVO datadefintHvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, targetTabPk);
//			if(datadefintHvo==null||datadefintHvo.getMemorytable()==null){
//				getSelfUI().showErrorMessage("目标表不存在！");
//				return null;
//			}
//			if(!datadefintHvo.getMemorytable().equals(sheetName)){
//				getSelfUI().showErrorMessage("目标表【"+datadefintHvo.getMemorytable()+"】和导入excel第一个页签名【"+sheetName+"】不相同");
//				return null;
//			}
//		String sql=" select * from Dip_Datadefinit_h hh where hh.memorytable='"+sheetName+"' and nvl(dr,0)=0 ";
//			//List list=iq.querySuperVO(" select * from Dip_Datadefinit_h hh where hh.memorytable='"+sheetName+"' and nvl(dr,0)=0 ");
//		List list=iq.queryVOBySql(sql, new DipDatadefinitHVO());
//			if(list==null||list.size()<1){
//				getSelfUI().showErrorMessage("导入的表"+sheetName+"不存在！");
//				return null;
//			}
//			tvo.setTableName(sheetName);
//			String pk_datadefinit_h=datadefintHvo.getPk_datadefinit_h();
//			map=getTableCodeMap(pk_datadefinit_h);//得到表的ename，cname
			tvo.setSheetName(sheetName);
			tvo.setStartRow(sheet.getFirstRowNum());
			tvo.setStartCol(sheet.getLeftCol());
			tvo.setRowCount(sheet.getPhysicalNumberOfRows() - 1);//去掉标题行
			tvo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());
			tvo.setFirstDataRow(sheet.getFirstRowNum());
			tvo.setFirstDataCol(sheet.getLeftCol());

			HSSFRow titleRow = sheet.getRow(tvo.getStartRow());
			HashMap<String, String> titlemap = new HashMap<String, String>();
			for(short i=0;i<titleRow.getPhysicalNumberOfCells();i++){
				titlemap.put((String)SJUtil.getCellValue(titleRow.getCell(i)),Short.toString(i));
			}
			tvo.setTitlemap(titlemap);
//			int titlesize=titlemap.size();
			for(int i=1;i<=tvo.getRowCount();i++){
				HSSFRow row = sheet.getRow(tvo.getFirstDataRow() + i);
				for(short j=0;j<titleRow.getPhysicalNumberOfCells();j++){
					tvo.setCellData(i-1, j, SJUtil.getCellValue(row.getCell(j)));
				}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fin!=null){try {
				fin.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		}
		return tvo;
	}
	public void getValidate(){
		
	}
	
	public Map getTableCodeMap(String pk_datadefinit_h){
		Map<String,DipDatadefinitBVO> map=new HashMap<String,DipDatadefinitBVO>();
		String sql=" select * from Dip_Datadefinit_b bb where bb.pk_datadefinit_h='"+pk_datadefinit_h+"' and nvl(dr,0)=0 ";
		//List list=iq.querySuperVO(" select * from Dip_Datadefinit_h hh where hh.memorytable='"+sheetName+"' and nvl(dr,0)=0 ");
		List list=iq.queryVOBySql(sql, new DipDatadefinitBVO());
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DipDatadefinitBVO bvo=(DipDatadefinitBVO) list.get(i);
				//String cname=bvo.getCname()==null?"":bvo.getCname();
				String ename=bvo.getEname()==null?"":bvo.getEname();
				map.put(ename, bvo);
			}
		}
		return map;
	}
  private final	String EXPFIELDNAMES="来源表,目标表,重复控制,数据处理状态,数据结束状态,字段编码,字段名称,转换公式,控制列";
	@Override
	protected void onBoExport() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		//得到当前VO类
		DipTYZHDatachangeHVO dvo=(DipTYZHDatachangeHVO) node.getData();
		String sheetname=dvo.getCode()+"-"+dvo.getName();
		String sTargettab=dvo.getTargettab();//目标表
		String sSourcetal=dvo.getSourcetab();//来源表
		String sRepdatactl=dvo.getRepdatactl();//重复控制
		String sConstatus=dvo.getConstatus();//控制处理状态
		String sEndstatus=dvo.getEndstatus();//控制结束状态
		if(sTargettab==null||sTargettab.trim().equals("")){
			this.getSelfUI().showErrorMessage("目标表不存在");
			return;
		}
		String targetTableName=iq.queryfield("select hh.memorytable from Dip_Datadefinit_h hh where hh.pk_datadefinit_h='"+sTargettab+"' and nvl(dr,0)=0 ");
     	String sourceTableName=iq.queryfield("select hh.memorytable from Dip_Datadefinit_h hh where hh.pk_datadefinit_h='"+sSourcetal+"' and nvl(dr,0)=0 ");
     	String sRepdatactlValue=iq.queryfield("select name from dip_repeatdata aa where aa.pk_repeatdata='"+sRepdatactl+"' and nvl(dr,0)=0 ");
     	String sConstatusValue=iq.queryfield("select name from dip_statusregist bb where bb.pk_statusregist='"+sConstatus+"' and nvl(dr,0)=0 ");
     	String sEndstatusValue=iq.queryfield("select name from dip_statusregist bb where bb.pk_statusregist='"+sEndstatus+"' and nvl(dr,0)=0 ");
		if(targetTableName==null||targetTableName.trim().equals("")){
			this.getSelfUI().showErrorMessage("目标表不存在");
			return;
		}
		if(sourceTableName==null||sourceTableName.trim().equals("")){
			this.getSelfUI().showErrorMessage("来源表不存在");
			return;
		}
		if(sRepdatactlValue==null||sRepdatactlValue.trim().equals("")){
			this.getSelfUI().showErrorMessage("重复控制不存在");
			return;
		}
		if(sConstatusValue==null||sConstatusValue.trim().equals("")){
			this.getSelfUI().showErrorMessage("控制处理状态不存在");
			return;
		}
		if(sEndstatusValue==null||sEndstatusValue.trim().equals("")){
			this.getSelfUI().showErrorMessage("控制结束状态不存在");
			return;
		}
		
		String pk_tyzhq_h=dvo.getPk_tyzhq_h();
		String sql=" select  tb.pk_tyzhq_h,tb.pk_tyzhq_b,tb.pk_lyzd,tb.contrl,tb.changformu,hb.cname fieldname,hb.ename fieldcode from dip_tyzhq_b tb  "+
				   " left join dip_datadefinit_b hb on tb.pk_lyzd = hb.pk_datadefinit_b"+  
				   " where  tb.pk_tyzhq_h='"+pk_tyzhq_h+"' and nvl(tb.dr,0)=0 and nvl(hb.dr,0)=0; ";
		List list=iq.queryVOBySql(sql, new DipTYZHDatachangeBVO());		
		ExpExcelVO expExcelvos[]=null;
		
		if(list!=null&&list.size()>0){
			expExcelvos=new ExpExcelVO[list.size()];
			for(int i=0;i<list.size();i++){
				expExcelvos[i]=new ExpExcelVO();
				DipTYZHDatachangeBVO bvo=(DipTYZHDatachangeBVO) list.get(i);
				//expExcelvos[i].setAttributeValue("line"+(1),bvo.getPk_lyzd());
				expExcelvos[i].setAttributeValue("line"+(1),sourceTableName);
				expExcelvos[i].setAttributeValue("line"+(2),targetTableName);
				expExcelvos[i].setAttributeValue("line"+(3),sRepdatactlValue);
				expExcelvos[i].setAttributeValue("line"+(4),sConstatusValue);
				expExcelvos[i].setAttributeValue("line"+(5),sEndstatusValue);
				
				expExcelvos[i].setAttributeValue("line"+(6),bvo.getFieldcode());
				expExcelvos[i].setAttributeValue("line"+(7),bvo.getFieldname());
				expExcelvos[i].setAttributeValue("line"+(8),bvo.getChangformu()==null?"":bvo.getChangformu());
				expExcelvos[i].setAttributeValue("line"+(9),bvo.getContrl()==null?"":bvo.getContrl());
			}	
		}
		
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(jfileChooser.SAVE_DIALOG);
		if(jfileChooser.showSaveDialog(this.getSelfUI()) == javax.swing.JFileChooser.CANCEL_OPTION){
			return;
		}else{
			String file=jfileChooser.getSelectedFile().toString();
			if(!file.endsWith(".xls")){
				file=file+".xls";
			}
			File jfile=new File(file);
			if(jfile.exists()){
				if(2==MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", file+"文件已经存在，是否覆盖")){
					return;
				}
			}
			ExpToExcel exp=new ExpToExcel(file,sheetname,EXPFIELDNAMES,expExcelvos,null);
			this.getSelfUI().showWarningMessage("导出成功");
		}
		
		
		
		
	}
	
	
	
}