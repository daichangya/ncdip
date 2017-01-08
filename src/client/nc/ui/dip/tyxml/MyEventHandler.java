package nc.ui.dip.tyxml;

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
import nc.ui.bd.ref.DataDefinitTableTreeRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.tyxml.tygs.AskXMLMBDLG;
import nc.ui.dip.tyzhq.iniufoenv.InitUFOEnvDlg;
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
import nc.vo.dip.tyxml.DipTYXMLDatachangeBVO;
import nc.vo.dip.tyxml.DipTYXMLDatachangeHVO;
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
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();

		// 将主表主键写到子表中
		newBodyVO.setAttributeValue("pk_tyxml_h", parent == null ? null : parent.getNodeID());

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
		String pk=(String) ui.getBillCardPanel().getHeadItem("pk_tyxml_h").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		String pk_xt=(String) ui.getBillCardPanel().getHeadItem("pk_xt").getValueObject();
		String code=(String) ui.getBillCardPanel().getHeadItem("code").getValueObject();
		IUAPQueryBS bs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		Collection ccode=bs.retrieveByClause(DipTYXMLDatachangeHVO.class,"code='"+code+"' and isfolder='N' and pk_tyxml_h <> '"+pk+"' and pk_xt='"+pk_xt+"' and nvl(dr,0)=0");
		Collection ccode=bs.retrieveByClause(DipTYXMLDatachangeHVO.class,"code='"+code+"'  and pk_tyxml_h <> '"+pk+"' and pk_xt='"+pk_xt+"' and nvl(dr,0)=0");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage(IContrastUtil.getCodeRepeatHint("编码", code));
				return;
			}
		}

		//获得界面原始数据的vo
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);


		//获得界面新数据vo
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		DipTYXMLDatachangeBVO[] bvo=(DipTYXMLDatachangeBVO[]) checkVO.getChildrenVO();
		StringBuffer errlist=new StringBuffer();
		String rootbq="";
		int countxhb=0;
		int countxhe=0;
		for(int i=0;i<bvo.length;i++){
			if(bvo[i].getRandend()!=null&&bvo[i].getRandend().booleanValue()){
				bvo[i].setContrl(null);
				countxhe++;
			}
			if(bvo[i].getRandstart()!=null&&bvo[i].getRandstart().booleanValue()){
				bvo[i].setContrl(null);
				countxhb++;
			}
			String bvoii=bvo[i].getBqfield();
			if(!bvoii.startsWith("<")||!bvoii.endsWith(">")){
				errlist.append("第"+(i+1)+"行:标签格式不正确\n");
				continue;
			}
			if(getCountStr(bvoii,"<")!=getCountStr(bvoii, ">")){
				errlist.append("第"+(i+1)+"行:标签格式不正确\n");
				continue;
			}
			String bvoi=bvoii.substring(1,bvoii.length()-1);
			bvoi=bvoi.replace("><", ",");
			if(!bvoi.contains(",")){
				if(rootbq.length()>0){
					errlist.append("第"+(i+1)+"行:只能包含一个根标签！根标签重复定义"+bvoii+"\n");
				}
				rootbq=bvoi;
			}else{
				String[] ss=bvoi.split(",");
				if(rootbq.length()>0&&!ss[0].equals(rootbq)){
					errlist.append("第"+(i+1)+"行:只能包含一个根标签！根标签重复定义"+ss[0]+"\n");
				}
			}
//			if(bq.contains(bvoi)){
//				errlist.append("第"+(i+1)+"行标签重复："+bvoi+"\n");
//			}
		}
		if(countxhb!=countxhe){
			getBillUI().showErrorMessage("循环开始标志和循环结束标志的个数不一致！");
			return;
		}
		DipTYXMLDatachangeHVO hvotemp=(DipTYXMLDatachangeHVO) checkVO.getParentVO();
		if(hvotemp.getIscombo()!=null&&hvotemp.getIscombo().equals("Y")&&countxhb<=0){
			getBillUI().showErrorMessage("请定义循环开始标志和循环结束标志！");
			return;
		}
		if(errlist!=null&&errlist.length()>0){
			getBillUI().showErrorMessage(errlist.toString());
			return;
		}
		Object o = null;
		ISingleController sCtrl = null;

		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether())
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		else{

			DipTYXMLDatachangeBVO[] bvos=(DipTYXMLDatachangeBVO[]) checkVO.getChildrenVO();
			
			List<DipTYXMLDatachangeBVO> changevo=new ArrayList<DipTYXMLDatachangeBVO>();
			String pks="";
			if(bvos!=null&&bvos.length>=0){
				for(int k=0;k<bvos.length;k++){
					if(bvos[k].getPrimaryKey()!=null){
						changevo.add(bvos[k]);
						pks=pks+"'"+bvos[k].getPrimaryKey()+"',";
					}
				}
				if(pks!=null&&pks.length()>0){
					pks=pks.substring(0,pks.length()-1);
					HYPubBO_Client.deleteByWhereClause(DipTYXMLDatachangeBVO.class, "pk_tyxml_b not in ("+pks+")");
				}
				billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), billVO);
				if(changevo!=null&&changevo.size()>0){
					HYPubBO_Client.updateAry(changevo.toArray(new DipTYXMLDatachangeBVO[changevo.size()]));
				}
				billVO.setChildrenVO(HYPubBO_Client.queryByCondition(DipTYXMLDatachangeBVO.class, "pk_tyxml_h='"+billVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=0"));
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


	private int getCountStr(String bvoii, String string) {
		int i=0;
		char[] c=bvoii.toCharArray();
		for(char ci:c){
			if(ci==string.toCharArray()[0]){
				i++;
			}
		}
		return i;
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
			DipTYXMLDatachangeHVO  hvo=(DipTYXMLDatachangeHVO) arg0.getData();
			String pk_datachange_h=hvo.getPrimaryKey();
			SuperVO[] vos=HYPubBO_Client.queryByCondition(DipTYXMLDatachangeBVO.class,"pk_tyxml_h='"+pk_datachange_h+"' and  isnull(dr,0)=0");
			nc.vo.dip.tyxml.MyBillVO billvo=new nc.vo.dip.tyxml.MyBillVO();
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
					/*liyunzhe modify 来源表修改成树形参照 2012-06-04 strat*/
					DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
					model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
					model.addWherePart(" and tabsoucetype='自定义'");
					uir.setRefModel(model);
					/*liyunzhe modify 来源表修改成树形参照 2012-06-04 end*/
					
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
			DipTYXMLDatachangeBVO[] vos = (DipTYXMLDatachangeBVO[])HYPubBO_Client.queryByCondition(DipTYXMLDatachangeBVO.class, " pk_tyxml_h='"+pk_node+"' and isnull(dr,0)=0  ");
			if(vos!=null && vos.length>0){
				for(DipTYXMLDatachangeBVO bvo : vos ){
					HYPubBO_Client.delete(bvo);
				}
			}	
			DipTYXMLDatachangeHVO vo = (DipTYXMLDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYXMLDatachangeHVO.class, pk_node);
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
		DipTYXMLDatachangeHVO hvo=(DipTYXMLDatachangeHVO) node.getData();
		if(hvo.getFpk()==null||hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("请选择节点操作！");
			return ;
		}
		
		super.onBoEdit();


		DataChangeClientUI ui= (DataChangeClientUI) getBillUI();

		String pk=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_tyxml_h").getValueObject();
		if(pk==null&&pk.equals("")){
			isnew=true;
		}else {
			DipTYXMLDatachangeHVO vo=(DipTYXMLDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYXMLDatachangeHVO.class, pk);
			if(SJUtil.isNull(vo)){
				isnew=true;
			}else{
				isnew=false;
			}
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
//		DipTYXMLDatachangeHVO hvo=(DipTYXMLDatachangeHVO) node.getData();
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
		case IBtnDefine.initUFOENV:
			onBoInitUFOENV();
			break;
		case IBtnDefine.MBZH://模板按钮
			onBoMBZH();
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
//	上移
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
			nc.vo.dip.tyxml.MyBillVO billvo= (nc.vo.dip.tyxml.MyBillVO)getBillTreeCardUI().getVOFromUI();
			DipTYXMLDatachangeBVO[] bvos=(DipTYXMLDatachangeBVO[]) billvo.getChildrenVO();
			DipTYXMLDatachangeBVO vo1=bvos[row-1];
			DipTYXMLDatachangeBVO vo2=bvos[row];
			String pk=vo1.getPrimaryKey();
			int order=vo1.getOrderno();
			vo1.setPrimaryKey(vo2.getPrimaryKey());
			vo1.setOrderno(vo2.getOrderno());
			vo2.setPrimaryKey(pk);
			vo2.setOrderno(order);

			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo2, row-1);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo1, row);
		}
		this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row-1, 1, false, false);
		reOrder();
	}
//	下移
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
			nc.vo.dip.tyxml.MyBillVO billvo = null;
			try {
				billvo = (nc.vo.dip.tyxml.MyBillVO)getBillTreeCardUI().getVOFromUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
			DipTYXMLDatachangeBVO[]bvo=(DipTYXMLDatachangeBVO[]) billvo.getChildrenVO();
			DipTYXMLDatachangeBVO b=bvo[row];
			DipTYXMLDatachangeBVO bb=bvo[row+1];
			String pk=b.getPrimaryKey();
			int order=b.getOrderno();
			b.setPrimaryKey(bb.getPrimaryKey());
			b.setOrderno(bb.getOrderno());
			bb.setPrimaryKey(pk);
			bb.setOrderno(order);
			bvo[row]=bb;
			bvo[row+1]=b;

			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(bb, row);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(b, row+1);

//			String[] formula=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("bm").getLoadFormula();
//			getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row+1, formula);
//			getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, formula);
		}

		//2011-6-24 cl 下移的时候保证鼠标焦点也随着下移
		this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row+1, 1, false, false);
	}


	/**
		 * @desc 文件移动
		 * */
		public void onBoMoveFolder() throws Exception{
			
			nc.vo.dip.tyxml.MyBillVO billvo=(nc.vo.dip.tyxml.MyBillVO) getBufferData().getCurrentVO();
			if(billvo!=null&&billvo.getParentVO()!=null){
				DipTYXMLDatachangeHVO hvo=(DipTYXMLDatachangeHVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_TYZH,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_TYZH), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipTYXMLDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYXMLDatachangeHVO.class, hvo.getPrimaryKey());
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
		DipTYXMLDatachangeHVO hvo=null;
		if(node.getData() instanceof DipTYXMLDatachangeHVO){
			hvo=(DipTYXMLDatachangeHVO) node.getData();
		}else{
			getSelfUI().showErrorMessage("选择节点数据错误");
			return;
		}
		AskXMLMBDLG ask=new AskXMLMBDLG(getSelfUI(),hvo,"模板","        请选择操作类型?",new String[]{"导入模板","导出模板","复制模板"});
		ask.showModal();
		int result=ask.getRes();
		if(result==0){
			onBoImport();
		}else if(result==1){
			onBoExport();
		}else if(result==2){
			String selectPk_tyxml_h=(String) ask.getOrgnizeRefPnl().getRefModel().getValue("pk_tyxml_h");
			if(selectPk_tyxml_h!=null&&selectPk_tyxml_h.equals(hvo.getPrimaryKey())){
				return;
			}
			DipTYXMLDatachangeBVO[] bvos=(DipTYXMLDatachangeBVO[]) HYPubBO_Client.queryByCondition(DipTYXMLDatachangeBVO.class, "pk_tyxml_h='"+selectPk_tyxml_h+"' and nvl(dr,0)=0");
			//得到需要复制的记录
			//先删除主表vo对应的子表记录，在把复制到的子表vo插入到选中的节点的子表
			if(bvos!=null&&bvos.length>0){
				for(int i=0;i<bvos.length;i++){
					bvos[i].setPrimaryKey(null);
					bvos[i].setPk_tyxml_h(hvo.getPrimaryKey());
				}
				String sqlupdate=" update dip_tyxml_b b set dr=1 where b.pk_tyxml_h='"+hvo.getPk_tyxml_h()+"' and nvl(dr,0)=0 ";;
				iq.exesql(sqlupdate);
				HYPubBO_Client.insertAry(bvos);
				SuperVO vos[]=HYPubBO_Client.queryByCondition(DipTYXMLDatachangeBVO.class, " pk_tyxml_h='"+hvo.getPk_tyxml_h()+"'");
				getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_tyxml_b").setBodyDataVO(vos);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_tyxml_b").execLoadFormula();
				getSelfUI().updateUI();
				nc.vo.dip.tyxml.MyBillVO billvo=new nc.vo.dip.tyxml.MyBillVO();
				//设置主VO
				billvo.setParentVO(hvo);
				//设置VO
				billvo.setChildrenVO(vos);				
				//显示数据
				getBufferData().addVOToBuffer(billvo);
				getBufferData().refresh();

				getBillTreeCardUI().getTreeToBuffer().put(node.getNodeID(),"" + (getBufferData().getVOBufferSize() - 1));
				MessageDialog.showHintDlg(getSelfUI(), "复制成功", "复制成功");
			}
		}
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
		DipTYXMLDatachangeHVO vo =(DipTYXMLDatachangeHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipTYXMLDatachangeHVO[] listvos=(DipTYXMLDatachangeHVO[]) HYPubBO_Client.queryByCondition(DipTYXMLDatachangeHVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_tyxml_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
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
					vo=(DipTYXMLDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYXMLDatachangeHVO.class, vo.getPrimaryKey());
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
		DipTYXMLDatachangeHVO vo =(DipTYXMLDatachangeHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipTYXMLDatachangeHVO.class, "fpk='"+vo.getPrimaryKey()+"'");
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
		DipTYXMLDatachangeHVO hvo=(DipTYXMLDatachangeHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipTYXMLDatachangeHVO newhvo=new DipTYXMLDatachangeHVO();
		newhvo.setFpk(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipTYXMLDatachangeHVO[] listvos=(DipTYXMLDatachangeHVO[]) HYPubBO_Client.queryByCondition(DipTYXMLDatachangeHVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
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
				e.printStackTrace();
			}
			try {
				newhvo=(DipTYXMLDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYXMLDatachangeHVO.class, pk);
				
				if (getUITreeCardController().isAutoManageTree()) {	
					getSelfUI().insertNodeToTree(newhvo);
				}
			} catch (UifException e) {
				e.printStackTrace();
			}catch (Exception e) {
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
		DipTYXMLDatachangeHVO hvo=null;
		try {
			hvo = (DipTYXMLDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYXMLDatachangeHVO.class, node.getNodeID().toString());
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

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPrimaryKey(),IContrastUtil.YWLX_TYZH,hvo.getSourcetab()+","+hvo.getPkfilepath());
		cd.showModal();
	}
	String datachangepk;
	public void conversion(){
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getBillUI().showErrorMessage("请选择要操作的节点！");
			return;
		}
		DipTYXMLDatachangeHVO hvo=(DipTYXMLDatachangeHVO) node.getData();
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
					RetMessage rm=ite.doTYXMLTask(datachangepk);
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
		DipTYXMLDatachangeHVO dvo=(DipTYXMLDatachangeHVO) node.getData();
		//得到主键值
		String fpk=dvo.getFpk();
		String pk=dvo.getPrimaryKey();
		dvo=(DipTYXMLDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipTYXMLDatachangeHVO.class, pk);
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
		DipTYXMLDatachangeHVO hvo=(DipTYXMLDatachangeHVO) tempNode.getData();
		if(hvo.getFpk()==null||!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("请选择文件夹操作！");
			return ;
		}
		super.onBoAdd(bo);
		getSelfUI().getBillCardPanel().setHeadItem("fpk", hvo.getPrimaryKey());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", hvo.getPk_xt());

		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourcetab").getComponent();
		/*liyunzhe modify 来源表修改成树形参照 2012-06-04 strat*/
		DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
		model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
		model.addWherePart(" and tabsoucetype='自定义'");
		uir.setRefModel(model);
		/*liyunzhe modify 来源表修改成树形参照 2012-06-04 end*/
		
		isnew=true;
		this.getButtonManager().getButton(IBtnDefine.initUFOENV).setEnabled(false);
		this.getSelfUI().updateButtonUI();
	}
	@Override
	protected void onBoRefresh() throws Exception {
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
					e.printStackTrace();
				}
			}
		}
		if(targettab!=null&&targettab.length()>0){
			DipDatadefinitBVO[] vos = null;
			try {
				vos = (DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+targettab+"' and nvl(dr,0)=0");
			} catch (UifException e) {
				e.printStackTrace();
			}
			if(vos!=null&&vos.length>0){
				DipTYXMLDatachangeBVO[] bvos=new DipTYXMLDatachangeBVO[vos.length];
				for(int i=0;i<vos.length;i++){
					bvos[i]=new DipTYXMLDatachangeBVO();
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
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		Object selectData=node.getData();
		DipTYXMLDatachangeHVO hvo=null;
		if(selectData!=null&& selectData instanceof DipTYXMLDatachangeHVO){
			hvo=(DipTYXMLDatachangeHVO) selectData;
		}else{
			this.getSelfUI().showWarningMessage("该节点数据错误！");
			return;
		}
		
		
		
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		if (jfileChooser.showSaveDialog(this.getBillUI()) == javax.swing.JFileChooser.CANCEL_OPTION)
			return;
		String path = jfileChooser.getSelectedFile().toString();
		if(!path.endsWith(".xls")){
			getSelfUI().showErrorMessage("请选择EXCEL文件导入！");
			return;
		}
		TxtDataVO tvo=getTxtDataVO(path,hvo);
		if(tvo==null){
			return;
		}else{
			String key="0";//强制校验的列
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
			List<DipTYXMLDatachangeBVO> rightList=new ArrayList<DipTYXMLDatachangeBVO>();
			DapFormuDefUI dapFormuDefUi=new DapFormuDefUI(this.getSelfUI(),null,hvo.getPk_xt());
			for(int i=0;i<rowVos.length;i++){
				RowDataVO rowVo=rowVos[i];
				Hashtable valueTable=rowVo.getTab();
						//这个地方可以加转换公式校验。
				DipTYXMLDatachangeBVO tyzhBvo=new DipTYXMLDatachangeBVO();
				String changeFormula=valueTable.get(titleMap.get("转换公式"))==null?"":valueTable.get(titleMap.get("转换公式")).toString();
				String conFormula=valueTable.get(titleMap.get("控制列"))==null?"":valueTable.get(titleMap.get("控制列")).toString();
				String pro=valueTable.get(titleMap.get("属性"))==null?"":valueTable.get(titleMap.get("属性")).toString();
				String orderno=valueTable.get(titleMap.get("显示顺序"))==null?"0":valueTable.get(titleMap.get("显示顺序")).toString();
				String bq=valueTable.get(titleMap.get("标签"))==null?"":valueTable.get(titleMap.get("标签")).toString();
				String bz=valueTable.get(titleMap.get("备注"))==null?"":valueTable.get(titleMap.get("备注")).toString();
				if((changeFormula!=null&&changeFormula.toString().trim().length()>0)||(conFormula!=null&&conFormula.toString().trim().length()>0)){
					boolean flag=dapFormuDefUi.ischeckpass(changeFormula);
					boolean flag1=dapFormuDefUi.ischeckpass(conFormula);
					StringBuffer sb=new StringBuffer();
					if(!flag){
						sb.append("转换公式错误");
					}
					if(!flag1){
						if(sb.length()>0){
							sb.append(",");
						}
						sb.append("控制列错误");
					}
					if(sb.length()>0){
						sb.append("。");
						rowVo.setErrorMsg(sb.toString());
						errList.add(rowVo);
					}else{
						tyzhBvo.setChangformu(changeFormula);
						tyzhBvo.setContrl(conFormula);
						tyzhBvo.setPk_tyxml_h(hvo.getPk_tyxml_h());
						tyzhBvo.setOrderno(Integer.parseInt(orderno));
						tyzhBvo.setBqfield(bq);
						tyzhBvo.setMemo(bz);
						tyzhBvo.setPro(pro);
						rightList.add(tyzhBvo);
					}
					
				}
			}
			StringBuffer message=new StringBuffer();
			if(rightList!=null&&rightList.size()>0){
				//保存到数据库中
				String sql=" update dip_tyxml_b b set dr=1 where b.pk_tyxml_h='"+hvo.getPk_tyxml_h()+"' and nvl(dr,0)=0 ";
				iq.exesql(sql);
				HYPubBO_Client.insertAry(rightList.toArray(new DipTYXMLDatachangeBVO[0]));
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
					expErrExcelvos[k].setAttributeValue("line"+7, errRowVo.getErrorMsg());
				}
				String errPath=path.substring(0, path.length()-4)+"-error.xls";
				ExpToExcel toexcel=new ExpToExcel(errPath,tvo.getTableName(),expErrorNames,expErrExcelvos,null);
				if(message.length()>0){
					message=new StringBuffer("部分导入成功，请查看错误文档"+errPath);
				}else{
					message=new StringBuffer("导入失败，请查看错误文档"+errPath);
				}
			}
			SuperVO vos[]=HYPubBO_Client.queryByCondition(DipTYXMLDatachangeBVO.class, " pk_tyxml_h='"+hvo.getPk_tyxml_h()+"'");
			getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_tyxml_b").setBodyDataVO(vos);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_tyxml_b").execLoadFormula();
			getSelfUI().updateUI();
			nc.vo.dip.tyxml.MyBillVO billvo=new nc.vo.dip.tyxml.MyBillVO();
			//设置主VO
			billvo.setParentVO(hvo);
			//设置VO
			billvo.setChildrenVO(vos);				
			//显示数据
			getBufferData().addVOToBuffer(billvo);
			getBufferData().refresh();

			getBillTreeCardUI().getTreeToBuffer().put(node.getNodeID(),"" + (getBufferData().getVOBufferSize() - 1));

			getSelfUI().showHintMessage(message.toString());
			MessageDialog.showHintDlg(this.getSelfUI(), "提示", message.toString());
			
		}
	}
	
	private Map map=new HashMap();//表的ename，和DipDatadefinitBVO对象。
	public TxtDataVO getTxtDataVO(String path,DipTYXMLDatachangeHVO hvo){
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
			tvo.setTableName(sheetName);
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
			e.printStackTrace();
		}finally{
			if(fin!=null){try {
				fin.close();
			} catch (IOException e) {
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
  private final	String EXPFIELDNAMES="显示顺序,标签,转换公式,控制列,属性,备注";
	@Override
	protected void onBoExport() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		//得到当前VO类
		DipTYXMLDatachangeHVO dvo=(DipTYXMLDatachangeHVO) node.getData();
		
		DipTYXMLDatachangeBVO[] bvos=(DipTYXMLDatachangeBVO[]) HYPubBO_Client.queryByCondition(DipTYXMLDatachangeBVO.class, "pk_tyxml_h='"+dvo.getPrimaryKey()+"' and nvl(dr,0)=0 order by orderno");//(DipTYXMLDatachangeBVO[]) billvo.getChildrenVO();
		ExpExcelVO expExcelvos[]=null;
		
		if(bvos!=null&&bvos.length>0){
			expExcelvos=new ExpExcelVO[bvos.length];
			for(int i=0;i<bvos.length;i++){
				expExcelvos[i]=new ExpExcelVO();
				DipTYXMLDatachangeBVO bvo=bvos[i];
				expExcelvos[i].setAttributeValue("line"+(1),bvo.getOrderno());
				expExcelvos[i].setAttributeValue("line"+(2),bvo.getBqfield());
				expExcelvos[i].setAttributeValue("line"+(3),bvo.getChangformu()==null?"":bvo.getChangformu());
				expExcelvos[i].setAttributeValue("line"+(4),bvo.getContrl()==null?"":bvo.getContrl());
				expExcelvos[i].setAttributeValue("line"+(5),bvo.getPro()==null?"":bvo.getPro());
				expExcelvos[i].setAttributeValue("line"+(6),bvo.getMemo()==null?"":bvo.getMemo());
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
			ExpToExcel exp=new ExpToExcel(file,"通用XML转换模板导出",EXPFIELDNAMES,expExcelvos,null);
			this.getSelfUI().showWarningMessage("导出成功");
		}
	}

	private void reOrder(){
		int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		int selectrow=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(rowcount>0){
			for(int i=selectrow;i<rowcount;i++){
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(i+1, i, "orderno");
			}
		}
	}
	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		reOrder();
	}

	@Override
	protected void onBoLineDel() throws Exception {
		super.onBoLineDel();
		reOrder();
	}

	@Override
	protected void onBoLineIns() throws Exception {
		super.onBoLineIns();
		reOrder();
	}

	@Override
	protected void onBoLinePaste() throws Exception {
		super.onBoLinePaste();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow(), "pk_tyxml_b");
		reOrder();
	}
	
	
	
}