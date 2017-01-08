package nc.ui.dip.statemanage;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.ui.bd.ref.DataDefinitTableTreeRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.datarec.DatarecDlg;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.warningset.WarningsetDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.CheckMessage;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.control.ControlVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataverify.DataverifyBVO;
import nc.vo.dip.dataverify.DataverifyHVO;
import nc.vo.dip.statemanage.DipStateManageHVO;
import nc.vo.dip.statemanage.MyBillVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
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

	public static String extcode;
	public MyEventHandler(BillTreeCardUI clientUI, ICardController control) {
		super(clientUI, control);
	}

	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private StateManageUI getSelfUI() {
		return (StateManageUI) getBillUI();
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
		newBodyVO.setAttributeValue("pk_statemanage_h", parent == null ? null : parent.getNodeID());

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


	/**
	 * 
	 * <P>此方法为覆盖父类方法
	 * <BR>将父类中的ISingleController判断去掉，否则保存时，表头为空
	 * @throws Exception
	 * @see nc.ui.trade.treecard.TreeCardEventHandler#onBoSave()
	 */
	protected void onBoSave() throws Exception {
		//获取当前单据的数据 zgy
//		liyunzhe 2011-06-04 start
		if(isAdding()){
			getSelfUI().FF=1;
		}else{
			getSelfUI().FF=0;
		}
		UIRefPane pane=(UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
		String[] arrpk=pane.getRefPKs();
		String data=null;
		if(arrpk!=null&&arrpk.length>0){
			for(int i=0;i<arrpk.length;i++){
				if(i==0){
					data=arrpk[i];
				}else{
					data=data+","+arrpk[i];
				}
			}
		}
		//liyunzhe 2011-06-04 end
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		//获取当前ui的主键和主表主键zgy
		StateManageUI ui=(StateManageUI) getBillUI();
		//判断编码、业务标识在当前系统下唯一性
		VOTreeNode node=getSelectNode();
		if(node==null){
			getSelfUI().showErrorMessage("请选择要操作的节点！");
			return ;
		}
		DipStateManageHVO hho=(DipStateManageHVO) node.getData();
		String pkxt=hho.getPk_xt();
		String fp=(String) node.getParentnodeID();			
		//如果父pk为空，则把节点pk赋给fpk，避免增加时，增加了重复的编码还可以保存
//		if(fp==null||fp.trim().equals("")){
//			fp=(String) node.getNodeID();
//		}
		String  pk=(String) ui.getBillCardPanel().getHeadItem("pk_statemanage_h").getValueObject();
		String  fpk=(String) ui.getBillCardPanel().getHeadItem("fpk").getValueObject();
		//判断当前ui的主键是否为空zgy
		if(pk==null||pk.trim().equals("")){
			pk=" ";
		}
		String code=(String)ui.getBillCardWrapper().getBillCardPanel().getHeadItem("code").getValueObject();
		if(code.length()>20){
			ui.showErrorMessage("编码长度不能大于20位！");
			return;
		}
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		Collection ccode=bs.retrieveByClause(DipStateManageHVO.class, "code='"+code+"' and isnull(dr,0)=0 and pk_statemanage_h<>'"+pk+"' and pk_xt='"+pkxt+"' and (isfolder is null or isfolder='N')");
		Collection ccode=bs.retrieveByClause(DipStateManageHVO.class, "code='"+code+"' and isnull(dr,0)=0 and pk_statemanage_h<>'"+pk+"' and pk_xt='"+pkxt+"' ");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage(IContrastUtil.getCodeRepeatHint("编码",code));
				return;
			}
		}	
		String name=(String) (ui.getBillCardPanel().getHeadItem("name").getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("name").getValueObject());
//		Collection cname=bs.retrieveByClause(DipStateManageHVO.class,"name='"+name+"' and isnull(dr,0)=0 and pk_statemanage_h<>'"+pk+"' and pk_xt='"+pkxt+"' and (isfolder is null or isfolder='N')");
//		Collection cname=bs.retrieveByClause(DipStateManageHVO.class,"name='"+name+"' and isnull(dr,0)=0 and pk_statemanage_h<>'"+pk+"' and (isfolder is null or isfolder='N') and fpk='"+fpk+"'");
//		if(cname!=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage(IContrastUtil.getCodeRepeatHint("名称",name));
//				return;
//			}
//		}

		//获得界面原始数据的vo
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);

		//获得界面新数据vo
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
//		String tablename=null;

		DipStateManageHVO hvo=(DipStateManageHVO) checkVO.getParentVO();
		//2011-7-20 业务数据存值时只有一个问题
		/*UIRefPane pane=(UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
		String[] arrpk=pane.getRefPKs();
		String data=null;
		for(int i=0;i<arrpk.length;i++){
			if(i==0){
				data=arrpk[i];
			}else{
				data=data+","+arrpk[i];
			}
		}
		hvo.setFirsttab(data);*/
		hvo.setFirsttab(data);
		hvo.setIsfolder(new UFBoolean(false));
		hvo.setPk_xt(pkxt);
		checkVO.setParentVO(hvo);


		Object o = null;
		ISingleController sCtrl = null;

		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether()){
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		}
		else{
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		}
		HYPubBO_Client.deleteByWhereClause(ControlVO.class, " pk_bus='"+hvo.getPrimaryKey()+"' and bustype='"+IContrastUtil.YWLX_JG+"' and nvl(dr,0)=0 and tabcname not in('"+data.replaceAll(",", "','")+"')");

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



		/*2011-6-16 begin*/
		String tab=hvo.getFirstdata();//原始表名
		String proceType=hvo.getProcetype();//加工类型
		UFBoolean isAddProceTab=hvo.getIsadd();//新建加工表
		//2011-6-22
		String proceTab=hvo.getProcetab();//加工表名
		setProceBtnIsEnable(tab,proceTab,isAddProceTab,proceType);
		/* end */

		getFirstData(tab);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("refprocecond", "条件保存在下边");
//		getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//		getSelfUI().updateButtonUI();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		
	}

	//2011-7-20 多选 原始数据 存值
	public void getFirstData(String tab){
		VOTreeNode node=getSelectNode();
		if(node !=null){
			if(node.getParentnodeID() !=null && node.getNodeID() !=null){
				UIRefPane pane=(UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
				String[] arrtab=tab.split(",");
				String asql="";
				String aname=null;
				for(int i=0;i<arrtab.length;i++){
					asql="select sysname from dip_datadefinit_h where memorytable='"+arrtab[i]+"' and nvl(dr,0)=0";
					if(i==0){
						try {
							aname=iq.queryfield(asql);
						} catch (BusinessException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}else{
						try {
							aname=aname+","+iq.queryfield(asql);
						} catch (BusinessException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
				}
				pane.setValue(aname);		
			}

		}
	}

	/**
	 * 设置”加工“按钮是否可用
	 * 1、当”原始表名“已经在数据库中存在，”加工“按钮可用，否则不可用
	 * 2、当原始表名存在：判断：
	 * 2.1：加工类型是”数据汇总“：
	 *	 2.1.1：如果新建加工表为true且加工表名在数据库中存在，则加工按钮 可用，创建表按钮 不可用；
	 *	 2.1.2：如果新建加工表为true且加工表名在数据库中不存在，则加工按钮 不可用，创建表按钮 可用；
	 * 2.2:数据清洗、卸载、预置:只需原始表名存在，加工按钮就可用，不需创建表（创建表按钮不可用）；
	 * 2.3:数据转换：
	 * 	 2.3.1：如果新建加工表为true且加工表名在数据库中存在，则加工按钮 可用，创建表按钮 不可用；
	 *	 2.3.2：如果新建加工表为true且加工表名在数据库中不存在，则加工按钮 不可用，创建表按钮 可用；
	 * @author 程莉
	 * @date 2011-6-16
	 * @date 2011-6-22修改
	 * @param tab
	 * 			原始表名
	 * @param proceTab
	 * 			加工表名
	 * @param isAddProceTab
	 * 			新建加工表
	 * @param proceType
	 * 			加工类型
	 */
	public void setProceBtnIsEnable(String tab,String proceTab,UFBoolean isAddProceTab,String proceType){
		if(tab!=null && !"".equals(tab)&& tab.length()>0){
//			boolean isExist=isTableExist(tab);	
//			if(isExist==false){
//				getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//				getSelfUI().updateButtons();
//			}else{
				if("数据汇总".equals(proceType) || "数据转换".equals(proceType)){		
					//新建加工表为null或者N
					if(isAddProceTab==null || !isAddProceTab.booleanValue()){
						getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//						getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
						getSelfUI().updateButtons();
					}else{
						//判断加工表是否已经在数据库中存在
						boolean isProceTab=isTableExist(proceTab);
						if(isProceTab){
//							getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
							getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(true);
//							getSelfUI().getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
							getSelfUI().updateButtons();
						}else{
//							getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
							getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//							getSelfUI().getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
							getSelfUI().updateButtons();
						}
					}
				}else /*if("数据清洗".equals(proceType) ||"数据卸载".equals(proceType) || "数据预置".equals(proceType))*/{
//					getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
					getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(true);
//					getSelfUI().getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
					getSelfUI().updateButtons();
				}
//			}
		}
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
	String errmsg="";
	public boolean nameType(String type,String value){

		errmsg="";
		boolean ret=true;
		if(type.equals("中文名称")&&(!value.matches("[\u4E00-\u9FA5]+[[\u4E00-\u9FA5]*+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+"【中文名称】必须以中文开头,可以以数字结尾！";
		}if(type.equals("英文名称")&&(!value.matches("[A-Za-z]+[[A-Za-z]*+[_]+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+"【英文名称】只能是字母开头,可以包含下划线，结尾可以包含数字！";
		}if(type.equals("加工表名")&&(!value.matches("[A-Z]+[[A-z]*+[_]+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+"【加工表名】只能以大写英文字母开头、可以包含下划线，结尾可以包含数字！";
		}
		return ret;

	}


	@Override
	protected void onBoDelete() throws Exception {
		//找到要删除的当前节点的主键zgy

		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要删除的节点！");
			return;
		}

		//String where="";
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
//		DipDataproceBVO[] ddbvo=(DipDataproceBVO[]) checkVO.getChildrenVO();
//		if(ddbvo!=null && ddbvo.length>0){
//			for(int i=0;i<ddbvo.length;i++){
//				where=where+"'"+ddbvo[i].getPrimaryKey()+"',";
//			}
//		}
		String pk=(String) tempNode.getParentnodeID();
		String pk_statemanage_h=(String) tempNode.getNodeID();
		if(pk==null||pk.length()==0){
			getSelfUI().showErrorMessage("系统节点不能做删除操作！");
			return;
		}/*else{
			 2011-5-23 NC系统下的节点不能删除 
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+pk_statemanage_h+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能删除！"); 
				return;
			}
			 end 
		}*/

		
		StateManageUI ui=(StateManageUI) getBillUI();
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确认要删除?");
		if(flag != 1){
			return;
		}
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
//		删除表体zgy
//		DipDataproceBVO[] vos = (DipDataproceBVO[])HYPubBO_Client.queryByCondition(DipDataproceBVO.class, " pk_statemanage_h='"+pk_statemanage_h+"' and isnull(dr,0)=0  ");
//		if(vos!=null && vos.length>0){
//			for(DipDataproceBVO bvo : vos ){
//				HYPubBO_Client.delete(bvo);
//			}
//		}
		//删除表头zgy
		DipStateManageHVO vo = (DipStateManageHVO) HYPubBO_Client.queryByPrimaryKey(DipStateManageHVO.class,pk_statemanage_h);
		if(vo==null){
			getSelfUI().showErrorMessage("不是子节点不能删除");
//			String uapsql="update dip_sysregister_b set isyy='N' where pk_sysregister_h='"+pk_statemanage_h+"'";
//			iq.exesql(uapsql);
			return;

		}
		if(vo!=null)
			HYPubBO_Client.delete(vo);	

		//2011-6-28 549--550行
		//String sql="update dip_dataproce_b set isquote='N',quotetable=null,quotecolu=null where dip_dataproce_b.quotetable in ("+(where.length()>0?where.substring(0, where.length()-1):where)+")";
		//iq.exesql(sql);

		//更新树
		this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());

		if(node1!=null){
		getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
							.getPath()));
		}
//		try {
//		onBoRefresh();

//		} catch (Exception e) {
//		e.printStackTrace();
//		}


	}


	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);

		switch(intBtn){

		case IBtnDefine.YuJing:
			onBoWarning(intBtn);
			break;
		case IBtnDefine.DataValidate:
			onBoValidate(intBtn);
			break;
//		case IBtnDefine.Movedup:
//			onBoMovedup(intBtn);
//			break;
//		case IBtnDefine.Moveddown:
//			onBoMoveddown(intBtn);
//			break;
		case IBtnDefine.DataProce:
			onBoDataPorce(intBtn);
			break;
		case IBtnDefine.moveFolderBtn:
			onBoMoveFolder();
			break;
//		case IBtnDefine.CreateTable:
//			onBoCreateImport(intBtn);
//			break;
		case IBtnDefine.CONTROL:
			onBoControl();
			break;
		case IBtnDefine.DataProcessCheck:
			onBoCheck();
			break;

		} 
	}


	/**
		 * @desc 文件移动
		 * */
		public void onBoMoveFolder() throws Exception{
			
			MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
			if(billvo!=null&&billvo.getParentVO()!=null){
				DipStateManageHVO hvo=(DipStateManageHVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_ZTYZ,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_ZTYZ), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipStateManageHVO) HYPubBO_Client.queryByPrimaryKey(DipStateManageHVO.class, hvo.getPrimaryKey());
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
	public void onBoCheck(){
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showErrorMessage("请选择要操作的节点");
			return;
		}
		DipStateManageHVO hvo=null;
		try {
			hvo = (DipStateManageHVO) HYPubBO_Client.queryByPrimaryKey(DipStateManageHVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}
		try {
			DataverifyHVO[] vhvo=(DataverifyHVO[]) HYPubBO_Client.queryByCondition(DataverifyHVO.class, "pk_datachange_h='"+hvo.getPrimaryKey()+"'");
			if(vhvo==null||vhvo.length<=0){
				getSelfUI().showErrorMessage("还没有定义数据校验！");
				return;
			}else{
				DataverifyHVO hv=vhvo[0];
				DataverifyBVO[] bvo=(DataverifyBVO[]) HYPubBO_Client.queryByCondition(DataverifyBVO.class, "pk_dataverify_h='"+hv.getPrimaryKey()+"'");
				if(bvo==null||bvo.length<=0){
					getSelfUI().showErrorMessage("还没有定义数据校验的校验类");
					return;
				}else{
					String errlog="";
					for(int i=0;i<bvo.length;i++){
						DataverifyBVO bv=bvo[i];
						if(bv.getVdef2().equals("通用校验")){

							String checkclass=bv.getName();//类的名称。
							Object inst;
							try {
								Class cls = Class.forName(checkclass);
								inst = cls.newInstance();
								if (inst instanceof ICheckPlugin) {
									ICheckPlugin m = (ICheckPlugin) inst;
									CheckMessage cmsg=m.doCheck(bv.getVerifycon());
									if(!cmsg.isSuccessful()&&cmsg.getMessage()!=null&&cmsg.getMessage().length()>0){
										errlog=errlog+bv.getVdef3()+"："+cmsg.getMessage()+";  ";
										ILogAndTabMonitorSys ilt=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
										ilt.writToDataLog_RequiresNew(hvo.getPrimaryKey(), IContrastUtil.YWLX_JG, "数据校验失败！"+bv.getVdef3()+";"+cmsg.getMessage());
										if(bv.getVector()!=null&&bv.getVector().equals("终止执行")){
											break;
										}
									}
								}
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								errlog=errlog+e.getMessage()+";  ";
							} catch (InstantiationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								errlog=errlog+e.getMessage()+";  ";
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								errlog=errlog+e.getMessage()+";  ";
							}
						
						}
					}
					if(errlog!=null&&errlog.length()>0){
						getSelfUI().showErrorMessage(errlog);
						return;
					}else{
						MessageDialog.showHintDlg(getSelfUI(), "校验检查", "校验检查成功！");
					}
				}
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getSelfUI().showErrorMessage("查询数据校验定义失败！");
		}
	}
	/**
	 * @desc 控制按钮，控制表状态功能
	 * */
	public void onBoControl(){



		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showErrorMessage("请选择要操作的节点");
			return;
		}
		DipStateManageHVO hvo=null;
		try {
			hvo = (DipStateManageHVO) HYPubBO_Client.queryByPrimaryKey(DipStateManageHVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}

		ControlHVO chvo=new ControlHVO();
		chvo.setBustype(SJUtil.getYWnameByLX(IContrastUtil.YWLX_JG));
		chvo.setCode(hvo.getCode());
		chvo.setName(hvo.getName());

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPrimaryKey(),IContrastUtil.YWLX_JG,hvo.getFirsttab());
		cd.showModal();



	
		/*
		VOTreeNode node=getSelectNode();
		DipDataproceHVO hvo=null;
		try {
			hvo = (DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		ControlDlg cd=new ControlDlg(getSelfUI(),hvo.getPrimaryKey(),IContrastUtil.YWLX_JG,hvo.getFirsttab());
		cd.showModal();
		if(cd.getRes()==0){
			ControlVO[] vo=cd.getVos();
			try {
				HYPubBO_Client.updateAry(vo);
			} catch (UifException e) {
				e.printStackTrace();
			}
		}
	*/}
//	private void onBoMoveddown(int intBtn) throws Exception {
//		int  row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
//		DipDataproceBVO bvo=(DipDataproceBVO) getBillTreeCardUI().getVOFromUI().getChildrenVO()[row];
//
//	}

//	private void onBoMovedup(int intBtn) throws Exception {
//		int  row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
//		DipDataproceBVO bvo=(DipDataproceBVO) getBillTreeCardUI().getVOFromUI().getChildrenVO()[row];
//
//	}

	private void onBoValidate(int intBtn) {

		VOTreeNode tempNode=getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("请选择要操作的节点");
			return;
		}
		DipStateManageHVO hvo=(DipStateManageHVO) tempNode.getData();
		String  hpk=hvo.getPk_statemanage_h();
		try {
			hvo=(DipStateManageHVO) HYPubBO_Client.queryByPrimaryKey(DipStateManageHVO.class, hpk);
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}
		// TODO Auto-generated method stub
		StateManageUI dpui=(StateManageUI) getBillUI();
//		int row=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTable().getSelectedRow();
//		if(row==-1){
//		dpui.showWarningMessage("请在表体选择一条信息！");
//		return;
//		}else{
////		//弹出数据校验窗体
//		ToftPanel toft=SFClientUtil.showNode("H4H1H0",IFuncWindow.WINDOW_TYPE_DLG);
//		DataVerifyClientUI ui=(DataVerifyClientUI) toft;
		HashMap map = new HashMap();
		map.put("pk", getSelfUI().selectnode);
		map.put("type", SJUtil.getYWnameByLX(IContrastUtil.YWLX_JG));
		/*将当前页面的表头code和name放到map中，用于当弹出数据校验窗口时，编码和名称为空，自动填充
		 * 2011-06-14
		 * zlc*/
		map.put("code", hvo.getCode());
		map.put("name", hvo.getName());
		map.put("fpk", getSelectNode().getParentnodeID().toString());
		DatarecDlg dlg = new DatarecDlg(getSelfUI(),new UFBoolean(true),map);
		dlg.show();


	}
//	Map<String,ProcessstyleVO> map=new HashMap<String, ProcessstyleVO>();
	public void onTreeSelected(VOTreeNode arg0){
		//获取vo
		try {
			SuperVO vo=(SuperVO) arg0.getData();
			String pk_statemanage_h=(String) vo.getAttributeValue("pk_statemanage_h");
//			SuperVO[] vos=HYPubBO_Client.queryByCondition(nc.vo.dip.dataproce.DipDataproceBVO.class ,"pk_statemanage_h='"+pk_statemanage_h+"' and isnull(dr,0)=0");
			nc.vo.dip.statemanage.MyBillVO billvo=new nc.vo.dip.statemanage.MyBillVO();
			DipStateManageHVO hvo = (DipStateManageHVO)vo;

			hvo.setRefprocecond("条件保存在下边");
			billvo.setParentVO(vo);
			//设置子VO
//			billvo.setChildrenVO(vos);
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));


			//联动源数据表名参照
			VOTreeNode tempNode = getSelectNode();
			String pid=null;
			DipSysregisterHVO syshvo;//
			if(tempNode!=null/*&&tempNode.getParentnodeID()!=null*/){
				if(tempNode.getParentnodeID()!=null&&!"".equals(tempNode.getParentnodeID())&& (tempNode.getNodeID() !=null && !"".equals(tempNode.getNodeID()))){
//					String pk=(String) tempNode.getParentnodeID();
					DipStateManageHVO dataprocehvo=(DipStateManageHVO) tempNode.getData();
					//String firsttab=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getValue();
					
//					if(firsttab!=null&&firsttab.length()>0){
//						uir.setPKs(firsttab.split(","));
//						model2.setFilterPks(firsttab.split(","));
//					}

					syshvo = (DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, dataprocehvo.getPk_xt());
					if(syshvo!=null){
						extcode=syshvo.getExtcode();
					}
				}else{
//					DipSysregisterHVO syshvo;
					try {
						syshvo = (DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, tempNode.getNodeID().toString());
						extcode=syshvo.getExtcode();

					} catch (UifException e) {
						e.printStackTrace();
					}
				}
			}
			/*liyunzhe modify 业务表修改成树形参照 2012-06-04 strat*/
			UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
			DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
			model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+vo.getAttributeValue("pk_xt")+"'");
			model.addWherePart(" and tabsoucetype='自定义'");
			uir.setRefModel(model);
			/*liyunzhe modify 业务表修改成树形参照 2012-06-04 end*/
		} catch (UifException e) {
			e.printStackTrace();
		}
	}
	public void rewrit(){
		if(getSelectNode()!=null){
			DipStateManageHVO hvo=(DipStateManageHVO) getSelectNode().getData();
			if(hvo!=null&&hvo.getFpk()!=null&&hvo.getFpk().length()>0){
				
				String tab=hvo.getFirsttab();
				
				getSelfUI().getBillCardPanel().getHeadItem("firsttab").setValue(getName(tab));
			}	
		}
		
	}
	public String getName(String tab){
		String ret="";
		if(tab!=null&&tab.length()>0){
			try {
				DipDatadefinitHVO[] hvo=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_datadefinit_h in ('"+tab.replaceAll(",", "','")+"')");
				if(hvo!=null&&hvo.length>0){
					for(int i=0;i<hvo.length;i++){
						ret=ret+hvo[i].getSysname()+",";
					}
				}
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(ret!=null&&ret.length()>0){
			ret=ret.substring(0,ret.length()-1);
		}
		return ret;
	}
	public  void isAdd() throws Exception{

		StateManageUI ui=(StateManageUI) getBillUI();
		String  isadd=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("isadd").getValueObject();

		String  pk_statemanage_h=(String) ui.getBillCardPanel().getHeadItem("pk_statemanage_h").getValueObject();
		UFBoolean bool=new UFBoolean(false);
		Boolean boolea=new Boolean(false);
		String boole=bool.toString();
		DipStateManageHVO[]  dvo= (DipStateManageHVO[])HYPubBO_Client.queryByCondition(DipStateManageHVO.class,"pk_statemanage_h='"+pk_statemanage_h+"' and isnull(dr,0)=0");
		for(int i=0;i<dvo.length;i++){
			String disadd=dvo[i].getIsadd().toString();
			if(disadd==boole){
				getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").setEnabled(false);
				getSelfUI().getBillCardWrapper().getBillCardPanel().getBodyHeadMenu().enable(boolea);
				getSelfUI().getBillCardWrapper().getBillCardPanel().getBodyPanel().enable(boolea);
				return;
			}


		}
	}


	@Override
	protected void onBoEdit() throws Exception {
		getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//		getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return;
		}
		String pk=(String) tempNode.getParentnodeID();
		if(pk==null||pk.equals("")){
			getSelfUI().showErrorMessage("系统节点不能做修改操作！");
			return;
		}
		super.onBoEdit();
		isAdd();

        DipStateManageHVO datahvo=(DipStateManageHVO) tempNode.getData();
		StateManageUI ui=(StateManageUI) getBillUI();
		String pk_statemanage_h=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_statemanage_h").getValueObject();

		// TODO Auto-generated method stub


		setHeadFieldVisble(false);
		if("数据清洗".equals(getSelfUI().getBillCardPanel().getHeadItem("procetype").getValue())||"数据预置".equals(getSelfUI().getBillCardPanel().getHeadItem("procetype").getValue())||"数据卸载".equals(getSelfUI().getBillCardPanel().getHeadItem("procetype").getValue())){
			getSelfUI().getBillCardPanel().getHeadItem("isadd").setEnabled(false);
		}else{
			getSelfUI().getBillCardPanel().getHeadItem("isadd").setEnabled(true);
		}
		BillItem item1 = getSelfUI().getBillCardPanel().getHeadItem("refprocecond");//加工条件
		if (item1 != null) {
			UIRefPane ref1 = (UIRefPane) item1.getComponent();

			if (ref1 != null) { 
				ref1.getUIButton().removeActionListener(ref1.getUIButton().getListeners(ActionListener.class)[0]);
				ref1.getUIButton().addActionListener(new DataProcActionListener(getSelfUI(),item1));
				ref1.setAutoCheck(false);
				ref1.setEditable(false);
			}
		}
		DipStateManageHVO hvo=(DipStateManageHVO) HYPubBO_Client.queryByPrimaryKey(DipStateManageHVO.class, tempNode.getNodeID().toString());
		String firsttab=hvo.getFirsttab();
//		String firsttab=((DipDataproceHVO)tempNode.getData()).getFirsttab();
//		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").setValue(firsttab);
		//2011-7-20
		/*liyunzhe modify 业务表修改成树形参照 2012-06-04 strat*/
		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
		DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
		model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+datahvo.getPk_xt()+"'");
		model.addWherePart(" and tabsoucetype='自定义'");
//		DataDefinitRefModel model2=new DataDefinitRefModel();
//		model2.addWherePart("  and pk_xt='"+datahvo.getPk_xt()+"' and nvl(dr,0)=0 and dip_datadefinit_h.iscreatetab='Y' and (isfolder is null or isfolder='N')");
		uir.setRefModel(model);
		uir.setPKs(firsttab.split(","));
		/*liyunzhe modify 表修改成树形参照 2012-06-04 end*/
	}
	public void setHeadFieldVisble(boolean b){
//		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procecond").setShow(b);
		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("refprocecond").setShow(!b);
		this.getBillCardPanelWrapper().getBillCardPanel().initPanelByPos(IBillItem.HEAD);		
	}
	@Override
	/*
	 * 功能：
	 * 在增加时候的半段是否是子节点并且给表头的主键，编码名称，原始表和数据定义主表主键字段付初值
	 * zgy
	 */
	public void onBoAdd(ButtonObject bo) throws Exception {
		getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要增加的节点！");
			return;
		}
		String pk=(String) tempNode.getParentnodeID();
		if(pk==null){
			getSelfUI().showErrorMessage("系统节点不能做增加操作！");
			return;
		}
		DipStateManageHVO hvo=(DipStateManageHVO) tempNode.getData();
		if(!(hvo!=null&&hvo.getIsfolder().booleanValue())){
			getSelfUI().showErrorMessage("不是文件节点，不能做增加操作！");
		}
		

		super.onBoAdd(bo);
		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procetab").setEnabled(false);


		getSelfUI().getBillCardPanel().setHeadItem("fpk", tempNode.getNodeID());
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", tempNode.getParentnodeID());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));

		DipStateManageHVO vo=(DipStateManageHVO) tempNode.getData();

//		getSelfUI().getBillCardPanel().setHeadItem("code",vo.getCode());
//		getSelfUI().getBillCardPanel().setHeadItem("name",vo.getName());
		getSelfUI().getBillCardPanel().setHeadItem("firsttab",vo.getFirsttab());
		getSelfUI().FF=1;//确定是增加按钮。
		BillItem item12 = getSelfUI().getBillCardPanel().getHeadItem("refprocecond");//加工条件
		if (item12 != null) {
			UIRefPane ref = (UIRefPane) item12.getComponent();

			if (ref != null) { 
				ref.getUIButton().removeActionListener(ref.getUIButton().getListeners(ActionListener.class)[0]);
				ref.getUIButton().addActionListener(new DataProcActionListener(getSelfUI(),item12));
				ref.setAutoCheck(false);
				ref.setEditable(false);
			}
		}
	}



	private void onBoWarning(int intBtn) throws Exception {


		VOTreeNode tempNode=getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("请选择要操作的节点");
			return;
		}
		DipStateManageHVO hvo=(DipStateManageHVO) tempNode.getData();
		String  hpk=hvo.getPk_statemanage_h();
		hvo=(DipStateManageHVO) HYPubBO_Client.queryByPrimaryKey(DipStateManageHVO.class, hpk);
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}
		//当前单据的预警业务类型
		String tasktype=hvo.getTasktype();
		if(SJUtil.isNull(tasktype)||tasktype.length()==0){
			getSelfUI().showErrorMessage("请选择预警类型！");
			return;
		}
		/*ToftPanel toft = SFClientUtil.showNode("H4H2H5", IFuncWindow.WINDOW_TYPE_DLG);
		WarningSetClientUI ui=(WarningSetClientUI) toft;*/

		String pk_statemanage_h=hvo.getPrimaryKey();//.getPk_datadefinit_h();
		nc.vo.dip.warningset.MyBillVO mybillvo=new nc.vo.dip.warningset.MyBillVO() ;
		DipWarningsetVO[] warvo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, " tasktype='"+tasktype+"' and pk_bustab='"+pk_statemanage_h+"'");

		DipWarningsetVO vo=null;
		boolean isadd=false;

		if(SJUtil.isNull(warvo)||warvo.length==0){
			vo=new  DipWarningsetVO();
			vo.setPk_bustab(hpk);
			vo.setTasktype(tasktype);
			vo.setPk_sys(hvo.getFpk());
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
		//ui.init(mybillvo, isadd,tempNode.getParentnodeID().toString(),1);
		WarningsetDlg wd=new WarningsetDlg(getSelfUI(),mybillvo, isadd,hvo.getPk_xt(),1);		
		wd.showModal();		



	}
	nc.vo.dip.statemanage.MyBillVO nodec=null;

	@Override
	protected void onBoCancel() throws Exception {
		getSelfUI().FF=0;
		DipStateManageHVO hh22=(DipStateManageHVO) getSelectNode().getData();
		super.onBoCancel();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
			rewrit();
		
		


		/*2011-6-22 cl 设置按钮”加工“、”创建表“按钮状态 begin*/
		AggregatedValueObject avo=getBillCardPanelWrapper().getBillVOFromUI();
		DipStateManageHVO ddhvo=(DipStateManageHVO) avo.getParentVO();
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
		DipStateManageHVO vo =(DipStateManageHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipStateManageHVO[] listvos=(DipStateManageHVO[]) HYPubBO_Client.queryByCondition(DipStateManageHVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_statemanage_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
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
					vo=(DipStateManageHVO) HYPubBO_Client.queryByPrimaryKey(DipStateManageHVO.class, vo.getPrimaryKey());
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
		DipStateManageHVO vo =(DipStateManageHVO) tempNode.getData();
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipStateManageHVO.class, "fpk='"+vo.getPrimaryKey()+"'");
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
		DipStateManageHVO hvo=(DipStateManageHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipStateManageHVO newhvo=new DipStateManageHVO();
		newhvo.setFpk(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipStateManageHVO[] listvos=(DipStateManageHVO[]) HYPubBO_Client.queryByCondition(DipStateManageHVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
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
				newhvo=(DipStateManageHVO) HYPubBO_Client.queryByPrimaryKey(DipStateManageHVO.class, pk);
				
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
		SJUtil.setAllButtonsEnalbe(getSelfUI().getButtons());
		getSelfUI().updateButtonUI();
	}
	
	
	
	
	
}