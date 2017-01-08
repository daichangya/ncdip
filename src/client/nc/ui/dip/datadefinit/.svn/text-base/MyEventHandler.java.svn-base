package nc.ui.dip.datadefinit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.text.Document;

import nc.bs.dip.txt.TxtDataVO;
import nc.bs.excel.pub.ExpExcelVO;
import nc.bs.excel.pub.ExpToExcel;
import nc.bs.excel.pub.ExpToExcel1;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.ddc.IBizObjStorage;
import nc.jdbc.framework.JdbcPersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.ref.PhysicTableRefModel;
import nc.ui.bd.ref.PubDatadictTreeRefModel;
import nc.ui.bd.ref.QuoteTableTreeRefModel;
import nc.ui.bd.ref.SysRegisterRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datadefinit.MyBillVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.runsys.DipRunsysBVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.util.PhysicTableVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.core.FolderNode;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.ddc.datadict.FieldDefList;
import nc.vo.pub.ddc.datadict.TableDef;
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

public class MyEventHandler extends AbstractMyEventHandler{

	public MyEventHandler(BillTreeCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	public static String extcode;
	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private DataDefinitClientUI getSelfUI() {
		return (DataDefinitClientUI) getBillUI();
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
		newBodyVO.setAttributeValue("pk_datadefinit_h", parent == null ? null : parent.getNodeID());

		// 添加结束
		return newBodyVO;
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



	/**
	 * 版权所有  商佳科技
	 * 作者 程莉
	 * 功能描述：删除 按钮
	 * 一、判断数据库中是否已经创建该表:
	 * 1.有表，有数据：提示【数据库已经建表，且有数据，是否确认删除？】
	 * 2.有表，无数据：提示【数据库已经建表，没有数据，是否确认删除？】
	 * 3.无表：提示【数据库还没建表，是否确认删除？】
	 * 二、如果是分布系统，查询数据定义主表，如果当前分布系统没有任何表，那么把系统注册子表是否引用标志改为N
	 * 三、如果当期系统下没有数据定义了，那么更新系统注册子表，把所有的是否引用标志改为N
	 * @date 2011-4-22
	 */
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());

	@Override
	protected void onBoDelete() throws Exception {		
		IUAPQueryBS queryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

		Integer flags=0;//删除标志：1代表点击了确定按钮
		String pk_node = ((DataDefinitClientUI) getBillUI()).selectnode;
		if("".equals(pk_node)){
			NCOptionPane.showMessageDialog(this.getBillUI(),"请选择要删除的节点。");
			return ;
		}
		VOTreeNode node1=null;
		String where="";
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) checkVO.getChildrenVO();
		if(bvos!=null&&bvos.length>0){
			for(int i=0;i<bvos.length;i++){
				where=where+"'"+bvos[i].getPrimaryKey()+"',";
			}
		}

		DipDatadefinitHVO vo = (DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk_node);	
		/** 2011-4-18 cl begin */
		if(vo==null){
			getSelfUI().showWarningMessage("系统节点不能做删除操作！");
			//2011-4-25 cl 更新系统注册子表，把所有的是否引用标志改为N
			String uapsql="update dip_sysregister_b set isyy='N' where pk_sysregister_h='"+pk_node+"'";
			iq.exesql(uapsql);
			return;
		}else{
			/* 2011-5-23 NC系统下的节点不能删除 */
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+vo.getPk_xt()+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			boolean nc=false;
			if(isNC !=null && !"".equals(isNC.trim())){
				int i =getSelfUI().showOkCancelMessage("NC系统下，只删除界面节点显示，不删除表结构，是否继续？");
				if(i==1){//确定
					nc=true;
				}else{
					nc=false;
					return;	
				}
			}
			VOTreeNode node=getSelectNode();
			
			if(node!=null){
				node1=(VOTreeNode) node.getParent();
			}
			//2011-7-12 
			String tabsoucetype=vo.getTabsoucetype();//表来源类型
			if("数据对照定义".equals(tabsoucetype)){
				getSelfUI().showWarningMessage("为数据对照定义回写的数据，不能删除！");
				return;
			}
			
			boolean isdeploy=vo.getIsdeploy().booleanValue();//是否分布式系统
			String pk_sysregister_h=vo.getPk_xt();
			String deploycode=vo.getDeploycode();//分布式标识

			//1.执行查询操作:判断数据库中是否已经建表
			String memoryTable=vo.getMemorytable();
			//调用查询数据库是否有该表的方法
			boolean isExist=false;
			if(vo.getMemorytype().equals("表")){
				isExist=isTableExist(memoryTable);
			}
			StringBuffer buffer=new StringBuffer();
			if(isExist){
				//2.执行查询是否有数据操作
				buffer.append("select 1 from ");
				buffer.append(memoryTable);
				ArrayList al=null;
				try{
					al=(ArrayList)queryBS.executeQuery(buffer.toString(), new MapListProcessor());
					if(al.size()>=1){
						//有数据
						if(nc){//确定删除nc节点显示。
							flags=1;
						}else{
							flags = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "系统已经建表！有数据！确认是否删除?");
						}
						if(flags==1){
							this.delete();

							//修改系统注册当前系统下表体的"是否引用"标志
							updateIsYY(isdeploy, pk_sysregister_h, deploycode, pk_node);							

							//2011-5-8 程莉 如果表删除了，更改”是否已建表“标志为N
							updateIsCreateFlag("N", pk_sysregister_h, pk_node);

							/*2011-5-20 执行删除数据库中的表操作 begin*/
							//liyunzhe 2011-06-28 增加不能删除nc节点表结构条件。
							if(!nc){//如果选择删除的不是nc节点，就可以删除表结构，否则不能删除表结构，只是把界面上的节点显示删除掉。
								StringBuffer sb=new StringBuffer();
								sb.append("drop table ");
								sb.append(memoryTable);
								iq.exesql(sb.toString());		
								
								//2011-7-13 删除NC数据字典中的表
								String delNC="delete from pub_datadict where id='"+memoryTable+"' and nvl(dr,0)=0";
								String delNCdetail="delete from pub_datadictdetail  where  reserved3='"+memoryTable+"' and nvl(dr,0)='0' ";
								iq.exesql(delNC);
								iq.exesql(delNCdetail);
							}

							//避免选则一个节点删除数据后，表头数据还留在界面上
							getSelfUI().getBillCardPanel().getBillData().clearViewData();
							/* end */
						}else{
							//点击取消
							return;
						}
					}else{
						//没有数据
						if(nc){//确定删除nc节点数据。
							flags=1;
						}else{
							flags = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "系统已经建表！没有数据！确认是否删除?");	
						}

						if(flags==1){
							this.delete();	

							updateIsYY(isdeploy, pk_sysregister_h, deploycode, pk_node);

							/* 2011-5-20 程莉 修改了系统注册当前系统下的是否引用标志后执行删除数据库中的表操作 begin*/
							//liyunzhe 2011-06-28 增加不能删除nc节点表结构条件。
							if(!nc){//如果选择删除的不是nc节点，就可以删除表结构，否则不能删除表结构，只是把界面上的节点显示删除掉。
								StringBuffer str=new StringBuffer();
								str.append("drop table ");
								str.append(memoryTable);
								iq.exesql(str.toString());
								
								//2011-7-13 删除NC数据字典中的表
								String delNC="delete from pub_datadict where id='"+memoryTable+"' and nvl(dr,0)=0";
								String delNCdetail="delete from pub_datadictdetail  where  reserved3='"+memoryTable+"' and nvl(dr,0)='0' ";
								iq.exesql(delNC);
								iq.exesql(delNCdetail);
							}

							//避免选则一个节点删除数据后，表头数据还留在界面上
							getSelfUI().getBillCardPanel().getBillData().clearViewData();
							/* end */
						}else{
							//点击取消
							return;
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}

			}else{
				//没有此表:执行删除操作
				flags = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "系统没有建表！没有数据！确认是否删除?");
				if(flags==1){					
					this.delete();
					updateIsYY(isdeploy, pk_sysregister_h, deploycode, pk_node);
				}else{
					//点击取消
					return;
				}
			}
		}
		String sql="update dip_datadefinit_b set isquote='N',quotetable=null,quotecolu=null where dip_datadefinit_b.quotetable in ("+(where.length()>0?where.substring(0,where.length()-1):where)+")";
		iq.exesql(sql);
		//更新树
		this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
		
		if(node1!=null){
			getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
					.getPath()));
		}
	}
	private void delete() throws Exception{
		if(isAllowDelNode(getBillTreeCardUI().getBillTreeSelectNode())){

			AggregatedValueObject modelVo = getBufferData().getCurrentVO();
			//进行数据清空
			Object o = null;
			ISingleController sCtrl = null;
			if (getUIController() instanceof ISingleController)
			{
				sCtrl = (ISingleController) getUIController();
				if (sCtrl.isSingleDetail())
				{
					o = modelVo.getParentVO();
					modelVo.setParentVO(null);
				}
				else
				{
					o = modelVo.getChildrenVO();
					modelVo.setChildrenVO(null);
				}
			}

			//getBusinessAction().delete(modelVo,getUIController().getBillType(),getBillUI()._getDate().toString(),	getBillUI().getUserObject());
			if(getSelectNode().getData()!=null){
				DipDatadefinitHVO hvo=(DipDatadefinitHVO) getSelectNode().getData();
				if(hvo!=null&&hvo.getPk_datadefinit_h()!=null){
					String sql=" update dip_datadefinit_b set dr=1 where pk_datadefinit_h='"+hvo.getPk_datadefinit_h()+"' and nvl(dr,0)=0 ";
					String sql1=" update dip_datadefinit_h set dr=1 where pk_datadefinit_h='"+hvo.getPk_datadefinit_h()+"' and nvl(dr,0)=0 ";
					iq.exesql(sql);
					iq.exectEverySql(sql1);
				}
			}
			
			if (PfUtilClient.isSuccess())
			{
				//从树中删除节点
				if(getUITreeCardController().isAutoManageTree()){
					getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(getBillTreeCardUI().getBillTreeSelectNode());
					getBillCardPanelWrapper().getBillCardPanel().getBillData().clearViewData();
				}
				//清除界面数据
				if (getUIController() instanceof ISingleController)
				{
					ISingleController sctl = (ISingleController) getUIController();
					if (sctl.isSingleDetail())
						getBufferData().refresh();
					else
						getBufferData().removeCurrentRow();
				}else{
					getBufferData().removeCurrentRow();
				}
				getBillTreeCardUI().resetTreeToBufferData();
			}

			if (getBufferData().getVOBufferSize() == 0)
				getBillUI().setBillOperate(IBillOperate.OP_INIT);
			else
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);

		}
	}

	/**
	 * 作者:程莉
	 * 功能：删除时：修改是否引用标志的方法
	 * 参数：是否分布式系统、系统注册主表主键、分布式标识、节点pk
	 * 日期：2011-4-25
	 */
	public void updateIsYY(boolean isdeploy,String pk_sysregister_h,String deploycode,String pk_node){
		//查询数据定义主表：当前系统是否还有该分布式标识的表，没有的话，把系统注册子表的是否引用标志改为N
		ArrayList arrayList=null;
		String sql="select deploycode from dip_datadefinit_h where pk_xt ='"+pk_sysregister_h+"'" +
		" and nvl(dip_datadefinit_h.dr,0)=0 and deploycode='"+deploycode+"' and pk_datadefinit_h<>'"+pk_node+"' and nvl(dr,0)=0";
		if(isdeploy){			
			try {
				arrayList=(ArrayList)queryBS.executeQuery(sql, new MapListProcessor());
				if(arrayList !=null){
					//当前系统标志除了本身外，即<=1，则表示没有
					if(arrayList.size()<1){
						//把系统注册子表的是否引用标志改为N
						String uapsql="update dip_sysregister_b set isyy='N' where pk_sysregister_h='"+pk_sysregister_h+"'"
						+" and dip_sysregister_b.pk_sysregister_b='"+deploycode+"' and nvl(dr,0)=0" ;					
						try {
							iq.exesql(uapsql);
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 2011-4-27 程莉
	 * 增加保存时判断是否是分布式系统，如果是，则更新当前系统当前站点的是否引用标志为Y，如果不是，则更新当前系统下的所有站点的是否引用标志为Y
	 */
	public void updateIsYY(boolean isdeploy,String isYY,String pk_sysregister_h,String deploycode,String pk_node){
		if(isdeploy){			
			//把系统注册子表的是否引用标志改为N
			String uapsql="update dip_sysregister_b set isyy='"+isYY+"' where pk_sysregister_h='"+pk_sysregister_h+"'"
			+" and dip_sysregister_b.pk_sysregister_b='"+deploycode+"' and nvl(dr,0)=0" ;					
			try {
				try {
					iq.exesql(uapsql);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
//		else{
//			//把系统注册子表的是否引用标志改为Y
//			String uapsql="update dip_sysregister_b set isyy='"+isYY+"' where pk_sysregister_h='"+pk_sysregister_h+"' and nvl(dr,0)=0";					
//			try {
//				try {
//					iq.exesql(uapsql);
//				} catch (SQLException e) {
//					e.printStackTrace();
//				} catch (DbException e) {
//					e.printStackTrace();
//				}
//			} catch (BusinessException e) {
//				e.printStackTrace();
//			}
//		}
	}

	/**
	 * 修改保存时：修改是否引用标志
	 * 修改时，判断站点是否改变
	 * 1.改变了：查询是否有其他系统引用了变化前的站点，如果有，则对勾保留，如果没有，则不打上对勾
	 * 2.没改变，则还是以前的状态
	 */
	public void updateIsYY(){

	}

	/**
	 * 修改"是否已建表"标志
	 * @description 如果已经建表，"是否已建表"打上对勾，否则则不打对勾
	 * @param  是否已建表、系统注册主表主键、数据定义主键
	 * @date 2011-5-8
	 * @author 程莉
	 */
	public void updateIsCreateFlag(String iscreatetab, String fpk,String pk){
		String sql="update dip_datadefinit_h set iscreatetab='"+iscreatetab+"' where pk_datadefinit_h='"+pk+"' and nvl(dr,0)=0";
		try{		
			iq.exesql(sql);		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 版权所有 商佳科技
	 * 作者 程莉
	 * 增加按钮
	 * 功能描述：1.如果选择的是最外层节点，给予不能操作的提示
	 * 2.如果选择的是第三级节点（0000NC 人员表），则不能增加（如：数据定义--NC系统--0000NC 人员表）
	 * 创建日期 2011-4-21
	 */
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要增加的系统节点！");
			return ;
		}
		DipDatadefinitHVO tnhvo=(DipDatadefinitHVO) tempNode.getData();
		if(!tnhvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("请选择文件夹进行增加操作！");
			return ;
		}
		super.onBoAdd(bo);
		if(this.getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()!=null){
				super.onBoLineAdd();
				
		}
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytype").setEnabled(true);
		getSelfUI().getBillCardPanel().setHeadItem("pk_sysregister_h", tempNode.getNodeID());
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", tnhvo.getPk_xt());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().setHeadItem("tabsoucetype", "自定义");
		//如果系统注册 其节点分布式系统为true，则在数据定义、数据接收定义表头中对应的节点 中的“是否分布式数据”都应该true，即应该打上勾
		DipSysregisterHVO syshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, tnhvo.getPk_xt());
//		DipDatadefinitHVO vo =(DipDatadefinitHVO) tempNode.getData();

		if(syshvo.getIsdeploy()==null||syshvo.getIsdeploy().equals("N")){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
		}else{
			//2011-6-3 如果是分布式系统，则分布式系统数据可编辑
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(true);
			//liyunzhe
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
		}

		getSelfUI().getBillCardPanel().setHeadItem("isdeploy", syshvo.getIsdeploy());

		/*
		 * 修改筛选条件，如果是是分布系统，并且是否分布系统为‘Y’时才能参照出分部站点
		 * 2011-06-03
		 * zlc*/
		String pk_sysregister_h =tnhvo.getPk_xt();
		//2011-4-26 程莉 动态自定义参照：查询当前系统下的分布式标识	
		boolean f=false;
		String isdep=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").getValueObject()==null?null:
			this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").getValueObject().toString();
		if(isdep!=null&&isdep.length()>0){
			f=Boolean.parseBoolean(isdep);
			if(f==true){


				UIRefPane pane=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").getComponent();
				SysRegisterRefModel model = new SysRegisterRefModel();
				model.addWherePart(" and dip_sysregister_b.pk_sysregister_h='"+pk_sysregister_h+"' and dip_sysregister_h.isdeploy='"+(f?'Y':'N')+ "' and nvl(dip_sysregister_b.dr,0)=0  and (dip_sysregister_b.isstop='N' or dip_sysregister_b.isstop='') ");
				pane.setRefModel(model);
				this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setComponent(pane);
			}}
		//引用表 自定义参照

		String pk_datadefinit_h=tnhvo.getPk_datadefinit_h();
		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
		//2011-6-29 由于要参照所有系统下的已经定义的表,以及原始的参照类条件没有加“是否系统预置主键=Y”，所以把参照类给换了，限制本系统下的条件给注释了
		QuoteTableTreeRefModel model2=new QuoteTableTreeRefModel();
		model2.addWherePart(" and pk_datadefinit_h1 <>'"+pk_datadefinit_h+"'");
		uir.setRefModel(model2);

		UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//		docu=jcp.getUITextField().getDocument();
		if(syshvo!=null){
			String code=syshvo.getCode();
			if(!code.equals("0000")){
				jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
				jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");
			}
		}
		this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(0,0,true,true);
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(0, "def_quotetable", false);

	}
	Document docu;

	/**
	 * 2011-4-18 cl
	 */
	@Override
	protected void onBoEdit() throws Exception {
		//修改时，将"创建表"按钮设置为不可用
		getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);

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
		DipDatadefinitHVO vo =(DipDatadefinitHVO) tempNode.getData();
		
//		if(vo.getIsfolder().booleanValue()){
//			AddFolderDlg adlg=new AddFolderDlg(getBillUI(),vo.getSyscode(),vo.getSysname());
//			adlg.showModal();
//			if(adlg.isOk()){
//				String tempc=adlg.getCode();
//				String tempn=adlg.getName();
//				if(!tempc.equals(vo.getSyscode())||!tempn.equals(vo.getSysname())){
//					vo.setSyscode(tempc);
//					vo.setSysname(tempn);
//					HYPubBO_Client.update(vo);
//					vo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, vo.getPrimaryKey());
//					if (getUITreeCardController().isAutoManageTree()) {	
//						getSelfUI().insertNodeToTree(vo);
//						getBillTreeCardUI().updateUI();
//						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("syscode", tempc);
//						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sysname", tempn);
//					}
//				}
//			}
//			return;
//		}
		/* 如果表来源类型为 数据加工，则不允许修改 2011-5-20 程莉 begin */
		String tabsoucetype=vo.getTabsoucetype();//表来源类型
		if(SJUtil.getYWnameByLX(IContrastUtil.YWLX_JG).equals(tabsoucetype)){
			getSelfUI().showWarningMessage("表来源类型为数据加工的数据，不能修改！");
			return;
		}else if("数据对照定义".equals(tabsoucetype)){
			getSelfUI().showWarningMessage("为数据对照定义回写的数据，不能修改！");
			return;
		}

		super.onBoEdit();		
		DipSysregisterHVO syshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, vo.getPk_xt());
		//如果“是否分布式数据"未选中
		if(syshvo.getIsdeploy()==null||syshvo.getIsdeploy().equals("N")){
			//是否分布式数据、分布式标识为不可编辑
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
		}else{
			//2011-6-3 如果是分布式系统，则分布式系统数据可编辑
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
		}
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytype").setEnabled(false);
		//2011-45-8 程莉 修改时：如果数据库中已经建表，则存储表名不可编辑，否则可以修改
		DataDefinitClientUI dataui=(DataDefinitClientUI) getBillUI();
		String tableName=(String) dataui.getBillCardPanel().getHeadItem("memorytable").getValueObject();
		boolean isExist=isTableExist(tableName);
		if(isExist||vo.getMemorytype().equals("视图")){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytable").setEnabled(false);
		}else{
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytable").setEnabled(true);
		}
		UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//		docu=jcp.getUITextField().getDocument();
		if(vo.getMemorytype().equals("表")){
			String ss=jcp.getText();
			jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
			jcp.getUITextField().setText(ss);
		}
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("userdefined").setEnabled(false);
		int row=this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(this.getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()!=null){
			//for(int i=row;i<201;i++){
				super.onBoLineAdd();
			//}
		}
		this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(0,0,false,false);
		int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		for(int i=0;i<rowcount;i++){
			Object isdo= getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "isquote");
			UFBoolean isd=isdo==null?new UFBoolean(false):new UFBoolean(isdo.toString());
			if(isd.booleanValue()){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "def_quotetable", true);
			}else{
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "def_quotetable", false);
				
			}
		}
	}

	/**
	 * 校验中文名称、英文名称
	 * @param name
	 * @description:中文名称只能是中文开始，结尾可以包含数字；英文名称只能以英文字母、下划线开始，结尾可以包含数字
	 * @author 程莉
	 * @date 2011-5-5
	 */
	String errmsg="";
	public boolean nameType(String type,String value){
		errmsg="";
		boolean ret=true;
		if(type.equals("中文名称")&& (! value.matches("[\u4E00-\u9FA5]+[[\u4E00-\u9FA5]*+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+" 【中文名称】必须以中文开头,可以以数字结尾！";
		}

		if(type.equals("英文名称")&& (! value .matches("[A-Za-z]+[[A-Za-z]*+[_]+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+" 【英文名称】只能是大写英文字母开头，可以包含下划线，结尾可以包含数字！";
		}
		if(type.equals("存储表名")&&(! value .matches("[A-Za-z]+[[A-Za-z]*+[_]+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+" 【英文名称】只能是大写英文字母开头，可以包含下划线，结尾可以包含数字！";
		}
		return ret;
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
		DipDatadefinitHVO vo =(DipDatadefinitHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipDatadefinitHVO[] listvos=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_datadefinit_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
			List<String> listcode=new ArrayList<String>();
			List<String> listname=new ArrayList<String>();
			if(listvos!=null&&listvos.length>0){
				for(int i=0;i<listvos.length;i++){
					listcode.add(listvos[i].getSyscode());
					listname.add(listvos[i].getSysname());
				}
			}
				
			AddFolderDlg adlg=new AddFolderDlg(getBillUI(),listcode,listname,vo.getSyscode(),vo.getSysname());
			adlg.showModal();
			if(adlg.isOk()){
				String tempc=adlg.getCode();
				String tempn=adlg.getName();
				if(!tempc.equals(vo.getSyscode())||!tempn.equals(vo.getSysname())){
					vo.setSyscode(tempc);
					vo.setSysname(tempn);
					HYPubBO_Client.update(vo);
					vo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, vo.getPrimaryKey());
					if (getUITreeCardController().isAutoManageTree()) {	
						getSelfUI().insertNodeToTree(vo);
						getBillTreeCardUI().updateUI();
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("syscode", tempc);
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sysname", tempn);
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
		DipDatadefinitHVO vo =(DipDatadefinitHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_sysregister_h='"+vo.getPrimaryKey()+"'");
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
	/**
	 * @desc 文件移动
	 * */
	public void onBoMoveFolder() throws Exception{
		
		MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
		if(billvo!=null&&billvo.getParentVO()!=null){
			DipDatadefinitHVO hvo=(DipDatadefinitHVO) billvo.getParentVO();
			MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_DY,hvo);
			dlg.showModal();
			String ret=dlg.getRes();
			if(ret!=null){
				hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_DY), ret);
				HYPubBO_Client.update(hvo);
				hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, hvo.getPrimaryKey());
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
	public void onBoAddFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null||tempNode.getData()==null){
			getSelfUI().showErrorMessage("请选择要增加的节点！");
			return ;
		}
		DipDatadefinitHVO hvo=(DipDatadefinitHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipDatadefinitHVO newhvo=new DipDatadefinitHVO();
		newhvo.setPk_sysregister_h(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipDatadefinitHVO[] listvos=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
		List<String> listcode=new ArrayList<String>();
		List<String> listname=new ArrayList<String>();
		if(listvos!=null&&listvos.length>0){
			for(int i=0;i<listvos.length;i++){
				listcode.add(listvos[i].getSyscode());
				listname.add(listvos[i].getSysname());
			}
		}
			
		AddFolderDlg addlg=new AddFolderDlg(getBillUI(),listcode,listname,null,null);
		addlg.showModal();
		if(addlg.isOk()){
			newhvo.setSyscode(addlg.getCode());
			newhvo.setSysname(addlg.getName());
			String pk = null;
			try {
				pk = HYPubBO_Client.insert(newhvo);
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				newhvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk);
				
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
	/**
	 * 
	 * <P>
	 * 功能：保存时，验证：
	 * 1、如果是分布系统，则分布标志必填。
	 * 2、唯一性校验：编码、业务标志、存储表名
	 * 3、表头：
	 * 4、表体:  a\
	 * 5、按钮，保存后，创建表按钮显示。
	 * 
	 * 此方法为覆盖父类方法 <BR>
	 * 将父类中的ISingleController判断去掉，否则保存时，表头为空
	 * 版权信息 商佳科技
	 * @author 程莉
	 * @description:唯一性校验：编码、业务标志、存储表名
	 * @throws Exception
	 * @see nc.ui.trade.treecard.TreeCardEventHandler#onBoSave()
	 * 创建日期 2011-4-21
	 */
	protected void onBoSave() throws Exception {		
		/** 2011-4-16 chengli 校验唯一性 begin */
		DataDefinitClientUI ui=(DataDefinitClientUI)getBillUI();
		/**程莉 判断编码、业务标识在当前系统下唯一性 2011-5-6 19:00 begin_1*/
		VOTreeNode node=getSelectNode();
		DipDatadefinitHVO tnhvo=(DipDatadefinitHVO) node.getData();
		String fpk=tnhvo.getPk_sysregister_h();
        String selectpk=tnhvo.getPk_datadefinit_h();
		String pk_xt=tnhvo.getPk_xt();
		if(fpk==null || fpk.trim().equals("")){
			fpk=(String) node.getNodeID();
			pk_xt=(String) node.getNodeID();
		}
		String pk=(String)ui.getBillCardPanel().getHeadItem("pk_datadefinit_h").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		//2011-5-7 程莉 系统编码长度20位以内，包含20位
		String code=ui.getBillCardPanel().getHeadItem("syscode").getValueObject()==null?null:ui.getBillCardPanel().getHeadItem("syscode").getValueObject().toString();
		if(code ==null || "".equals(code) || code.length()==0){
			ui.showWarningMessage("编码不能为空！");
			return;
		}
		if(code!=null&&code.length()>20){
			ui.showWarningMessage("编码长度最多20位！");
			return;
		}
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Collection ccode=null;
		if(pk.length()==20){
//			ccode=bs.retrieveByClause(DipDatadefinitHVO.class, "syscode='"+code+"' and nvl(dr,0)=0 and pk_datadefinit_h <>'"+pk+"' and pk_sysregister_h='"+fpk+"' and tabsoucetype='自定义'");	
			//本系统下的编码不能重复
			ccode=bs.retrieveByClause(DipDatadefinitHVO.class, "syscode='"+code+"' and nvl(dr,0)=0 and pk_datadefinit_h <>'"+pk+"' and pk_xt='"+pk_xt+"'");	
		}else{
			ccode=bs.retrieveByClause(DipDatadefinitHVO.class, "syscode='"+code+"' and nvl(dr,0)=0  and pk_xt='"+pk_xt+"' ");
		}
		
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage(IContrastUtil.getCodeRepeatHint("编码", code));
				return;
			}
		}

		String busicode=ui.getBillCardPanel().getHeadItem("busicode").getValueObject()==null?null:ui.getBillCardPanel().getHeadItem("busicode").getValueObject().toString();
		if(busicode ==null ||"".equals(busicode)|| busicode.length()==0){
			ui.showWarningMessage("业务标识不能为空！");
			return;
		}
		/*
		 * 判断当前系统、当前分布式标识下的 业务标识 是否唯一 
		 * 2011-6-7
		 * 633-654行
		 */
		//是否分布系统
		//分布式标识
		String fbsbz=(String) ui.getBillCardPanel().getHeadItem("deploycode").getValueObject();
		if(fbsbz!=null&&fbsbz.length()>0){
			ui.getBillCardPanel().setHeadItem("isdeploy", new UFBoolean(true));
		}else{
			ui.getBillCardPanel().setHeadItem("isdeploy", new UFBoolean(false));
		}
		Boolean deploy=Boolean.parseBoolean(ui.getBillCardPanel().getHeadItem("isdeploy").getValueObject().toString());
		if(deploy){
			if(fbsbz!=null && !"".equals(fbsbz)){
				Collection cname=bs.retrieveByClause(DipDatadefinitHVO.class, "busicode='"+busicode+"' and nvl(dr,0)=0 and pk_datadefinit_h <>'"+pk+"' and pk_xt='"+pk_xt+"' and tabsoucetype='自定义' and deploycode='"+fbsbz+"'");
				if(cname !=null){
					if(cname.size()>=1){
						ui.showWarningMessage("该【"+busicode+"】业务标识已经存在！");
						return;
					}
				}
			}
		}else{
			Collection cname=bs.retrieveByClause(DipDatadefinitHVO.class, "busicode='"+busicode+"' and nvl(dr,0)=0 and pk_datadefinit_h <>'"+pk+"' and pk_xt='"+pk_xt+"' and tabsoucetype='自定义' and deploycode is null");
			if(cname !=null){
				if(cname.size()>=1){
					ui.showWarningMessage("该【"+busicode+"】业务标识已经存在！");
					return;
				}
			}
		}

		String sysname=ui.getBillCardPanel().getHeadItem("sysname").getValueObject()==null?"":ui.getBillCardPanel().getHeadItem("sysname").getValueObject().toString();
		if(sysname ==null || "".equals(sysname) || sysname.length()==0){
			ui.showWarningMessage("名称不能为空！");
			return;
		}

		//存储表名默认以"DIP_DD_+  系统外部编码+"_""开头，并且都要大写
		String memoryTableName=ui.getBillCardPanel().getHeadItem("memorytable").getValueObject()==null?"":ui.getBillCardPanel().getHeadItem("memorytable").getValueObject().toString();
		String value = ui.getBillCardPanel().getHeadItem("userdefined").getValue();
		if(memoryTableName!=null&&memoryTableName.length()>0){
			if(!"是".equals(value)){
				if(!nameType("存储表名",memoryTableName)){
					getSelfUI().showErrorMessage(errmsg);
					return;
				}
			}
		}

		//2011-6-1 修改保存时：修改其是否引用标识的方法
		//1.改变了：查询是否有其他系统引用了变化前的站点，如果有，则对勾保留，如果没有，则不打上对勾
		//2.没改变，则还是以前的状态
		if ("".equals(checkTableHead(ui,pk))){
			return ;
		}; 		
		DipSysregisterHVO syshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk_xt);
		String code1=syshvo.getCode();
		String memorytype=(String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytype").getValueObject();
		if(!"是".equals(value)){
		if(!code1.equals("0000")){
			int k=8+extcode.length();
			if(memorytype.equals("表")&&(memoryTableName.length()<(k+1) || ! memoryTableName.substring(0, k).toUpperCase().equals("DIP_DD_"+extcode.toUpperCase()+"_"))){
				ui.showWarningMessage("存储表名必须以 DIP_DD_"+extcode.toUpperCase()+"_开头,长度最少"+(k+1)+"位！");
				return;
			}
//			else{
//				//校验表名是否重复
//				Collection memoryName=bs.retrieveByClause(DipDatadefinitHVO.class, "memorytable='"+memoryTableName.toUpperCase()+"' and nvl(dr,0)=0 and pk_datadefinit_h <>'"+pk+"'");
//				if(memoryName !=null){
//					if(memoryName.size()>=1){
//						ui.showWarningMessage("该【"+memoryTableName.toUpperCase()+"】存储表名已经存在！");
//						return ;
//					}
//				}
//			}
		}
		}
		
//		校验表名是否重复
		Collection memoryName=bs.retrieveByClause(DipDatadefinitHVO.class, "memorytable='"+memoryTableName.toUpperCase()+"' and nvl(dr,0)=0 and pk_datadefinit_h <>'"+pk+"'");
		if(memoryName !=null){
			if(memoryName.size()>=1){
				ui.showWarningMessage("该【"+memoryTableName.toUpperCase()+"】存储表名已经存在！");
				return ;
			}
		}
//		String memorytype=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytype").getValueObject().toString();
		if(memorytype!=null&&memorytype.equals("视图")){
			IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
			String isviewexit="select view_name from user_views where view_name='"+memoryTableName.toUpperCase()+"'";
			List file=iqf.queryfieldList(isviewexit);
			if(file!=null&&file.size()>0){
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("iscreatetab", new UFBoolean(true));
			}else {

				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("iscreatetab", new UFBoolean(false));
			}
		}
		
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		DipDatadefinitHVO hvo=(DipDatadefinitHVO) billVO.getParentVO();
		//把表名转换成大写
		String memory=hvo.getMemorytable().toUpperCase();

		hvo.setMemorytable(memory);	
		setTSFormBufferToVO(billVO);

		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		//2011-4-25 18:18 判断表体主键只能选取一个 begin
		int c=getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getRowCount();
		DipDatadefinitBVO[] bvo=new DipDatadefinitBVO[c];
		for(int i=0;i<c;i++){
			bvo[i]=new DipDatadefinitBVO();
		}
//		Vector vector=getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getDataVector();
//		.getBillModelData();
		getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getBodyValueVOs(bvo);
//		getBillTreeCardUI().getBillCardWrapper().getBillCardPanel().getbill
//		getBillCardPanelWrapper().getBillCardPanel().getBillTable
//		if(vector!=null&&vector.size()>0){
//			bvo=(DipDatadefinitBVO[]) vector.toArray(bvo);
//		}else{}
//		getBillCardPanelWrapper().getBillCardPanel().getBillTable("dip_datadefinit_b").getda
//		DipDatadefinitBVO[] bvo=(DipDatadefinitBVO[]) checkVO.getChildrenVO();

		Integer count = 0;
		Integer num=0;

		//2011-6-9 记录是否主键与是否系统预置主键是否在同一行的变量 694--695行
		int varpk=0;
		int varsyspk=0;

		String chname=null;
		String ename=null;
		String type=null;


		String gjz=null;
		DipRunsysBVO[] vos=(DipRunsysBVO[]) HYPubBO_Client.queryByCondition(DipRunsysBVO.class, "syscode='DIP-0000003' and nvl(dr,0)=0");
		if(vos !=null && vos.length>0){
			gjz=vos[0].getSysvalue();				
		}

		ArrayList<DipDatadefinitBVO> list = new ArrayList<DipDatadefinitBVO>();
		if(bvo !=null && bvo.length>0){
			Map enamemap=new HashMap();
			Map cnamemap=new HashMap();
			for(int i=0;i<bvo.length;i++){
				//判断中文名称、英文名称输入的是否合符规范
				
				ename=bvo[i].getEname();
				String quotetable = bvo[i].getQuotetable()==null?"":bvo[i].getQuotetable();
				if(bvo[i].getIsquote()!=null&&bvo[i].getIsquote().booleanValue()){
					if(bvo[i].getQuotetable()==null||bvo[i].getQuotetable().length()==0||bvo[i].getQuotetable().equals("null")){
						ui.showErrorMessage((i+1)+"行【引用表】没有编辑！");
						return;
					}
				}
				
				/*
				 * 行记录为空，continue 不可保存   646-648
				 * 2011-6-4
				 * zlc*/
				chname=bvo[i].getCname();
				if(chname==null||chname.trim().length()==0){
					continue;
				}else{
					if(ename==null || "".equals(ename)|| ename.length()==0){
						ui.showWarningMessage((i+1)+"行【英文名称】不能为空！");
						return;
					}
					
					if(cnamemap.get(chname)==null){
						cnamemap.put(chname, i+1);	
					}else{
						showErrorMessage("第"+cnamemap.get(chname)+"行和第"+(i+1)+"中文名称重复");
						return;
					}
				}

				if(ename==null||ename.trim().length()==0){
					continue;
				}else{
					ename=bvo[i].getEname();
					if(enamemap.get(ename)==null){
						enamemap.put(ename, i+1);	
					}else{
						showErrorMessage("第"+enamemap.get(ename)+"行和第"+(i+1)+"英文名称重复");
						return;
					}
					type=bvo[i].getType();
					if(type==null || "".equals(type) || type.length()==0){
						ui.showWarningMessage((i+1)+"行【类型】不能为空！");
						return;
					}
				}


				if(!nameType("英文名称",ename)){
					getSelfUI().showErrorMessage(errmsg);
					return;
				}

				//oracle关键字：如date、desc等
				if(gjz!=null){
					if(gjz.indexOf(","+ename.toLowerCase()+",")>=0){
						getSelfUI().showErrorMessage("第"+(i+1)+"行英文名称【"+ename+"】为oracle数据库中关键字，会导致无法创建表，请更改！");
						return;
					}	
				}
				

				//为主键，count++
				UFBoolean flagPk = bvo[i].getIspk()==null?new UFBoolean(false):bvo[i].getIspk();
				//为系统预置主键，num++
				UFBoolean issyspk=bvo[i].getIssyspk()==null?new UFBoolean(false):bvo[i].getIssyspk();
				if(flagPk.booleanValue()&&!issyspk.booleanValue()){
					count ++;
				}else if(!flagPk.booleanValue()&&!issyspk.booleanValue()){
				}else{
					bvo[i].setType("CHAR");
					bvo[i].setLength(20);
					bvo[i].setIspk(new UFBoolean(true));
					num++;

				}

				/*
				 * 如果为主键，把当前bvo[i]的i赋给varpk
				 * 如果系统预置主键主键，把当前bvo[i]的i赋给varsyspk
				 * 最后判断是否相等：如果相等则在同一行，不等，则提示
				 * 2011-6-9 760--765行
				 */
				if(flagPk.booleanValue()){
					varpk=i;
				}
				if(issyspk.booleanValue()){
					varsyspk=i;
				}
				if(isEditing()){
//					bvo[i].setPk_datadefinit_b(null);
					bvo[i].setPk_datadefinit_h(hvo.getPk_datadefinit_h());
				}
			}

			if(num+count>1){
				ui.showWarningMessage("只能选取一个主键或系统预置主键！");
				return ;
			}
			for(int i=0;i<bvo.length;i++){
				if((bvo[i].getEname()==null||bvo[i].getEname().trim().length()==0)&&(bvo[i].getCname()==null||bvo[i].getCname().trim().length()==0)&&(bvo[i].getType()==null||bvo[i].getType().trim().length()==0)){
					ui.getBillCardPanel().getBodyPanel().delLine();
					continue;
				}else{
					list.add(bvo[i]);
				}



			}

		}else{
			getSelfUI().showErrorMessage("表体不能为空！");
			return;
		}
		bvo = list.toArray(new DipDatadefinitBVO[0]);
		billVO.setChildrenVO(bvo);
		checkVO.setChildrenVO(bvo);


		boolean isdeploy=Boolean.parseBoolean( ui.getBillCardPanel().getHeadItem("isdeploy").getValueObject().toString());//是否分布式系统
		String pk_sysregister_h=(String) ui.getBillCardPanel().getHeadItem("pk_xt").getValueObject();
		String deploycode=(String) ui.getBillCardPanel().getHeadItem("deploycode").getValueObject();//变化后的分布式标识
		String pk_node=ui.selectnode;

		
		deleteLine();//ytq 2011-07-02
		Object o = null;
		ISingleController sCtrl = null;
		//保存前，查询一下引用站点是否还有别的数据定义引用，如果有，就不用更新引用状态，没有就更新引用状态。
		String pk_datadefinit_h="";
		if(ui.getBillCardPanel().getHeadItem("pk_datadefinit_h").getValueObject()!=null&&ui.getBillCardPanel().getHeadItem("pk_datadefinit_h").getValueObject().toString().trim().length()>0){
			pk_datadefinit_h=ui.getBillCardPanel().getHeadItem("pk_datadefinit_h").getValueObject().toString();
			//String sql=" select count(*) from dip_datadefinit_h hh where hh.isdeploy='Y' and hh.deploycode='"+pk_datadefinit_h+"'" ;
			String sql1=" select count(*) from dip_datadefinit_h hh where hh.isdeploy='Y' and hh.deploycode in( select hhh.deploycode from Dip_Datadefinit_h hhh where hhh.isdeploy='Y' and hhh.pk_datadefinit_h='"+pk_datadefinit_h+"') and hh.pk_datadefinit_h<>'"+pk_datadefinit_h+"'";
			String co=iq.queryfield(sql1);
			if(Integer.parseInt(co)<1){
				String sql2=" update dip_sysregister_b bb set bb.isyy='N' where bb.pk_sysregister_b in( "+
				" select hh.deploycode from dip_datadefinit_h hh where hh.isdeploy='Y' and hh.pk_datadefinit_h='"+pk_datadefinit_h+"')";
				iq.exesql(sql2);
			}
		}
			
		
		
		
		
		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether())
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		else{
//			billVO = getBusinessAction().save(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
			if(hvo.getPk_datadefinit_h()==null||hvo.getPk_datadefinit_h().equals("")){
				 pk=HYPubBO_Client.insert(hvo);
				 hvo.setPk_datadefinit_h(pk);
				 if(bvo!=null&&bvo.length>0){
					 for(int i=0;i<bvo.length;i++){
						 bvo[i].setPk_datadefinit_h(pk);
					 }	 
					 HYPubBO_Client.insertAry(bvo);
				 }
			}else{
				HYPubBO_Client.update(hvo);
				List updateList=new ArrayList<DipDatadefinitBVO>();
				List insertList=new ArrayList<DipDatadefinitBVO>();
				if(bvo!=null&&bvo.length>0){
					for(int i=0;i<bvo.length;i++){
						if(bvo[i].getPk_datadefinit_b()!=null){
							updateList.add(bvo[i]);
						}else{
							bvo[i].setPk_datadefinit_h(hvo.getPk_datadefinit_h());
							insertList.add(bvo[i]);
						}
					}	
				}
				if(updateList.size()>0){
					DipDatadefinitBVO updateArray[]=new DipDatadefinitBVO[updateList.size()];
					updateArray=(DipDatadefinitBVO[]) updateList.toArray(updateArray);
					HYPubBO_Client.updateAry(updateArray);
				}
				if(insertList.size()>0){
					DipDatadefinitBVO insertArray[]=new DipDatadefinitBVO[insertList.size()];
					insertArray=(DipDatadefinitBVO[]) insertList.toArray(insertArray);
					HYPubBO_Client.insertAry(insertArray);
				}
				
			}
//			HYPubBO_Client.update(hvo);
//			iq.exesql("update dip_datadefinit_b b set b.dr=1 where pk_datadefinit_h='"+hvo.pk_datadefinit_h+"'");
//			HYPubBO_Client.insertAry(bvo);
			billVO.setParentVO(hvo);
			SuperVO ss[]=HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+hvo.pk_datadefinit_h+"' and nvl(dr,0)=0 ");
			if(ss!=null&&ss.length>0){
				billVO.setChildrenVO(ss);	
			}
			
			
			
			}
		if (sCtrl != null && sCtrl.isSingleDetail())
			billVO.setParentVO((CircularlyAccessibleValueObject) o);
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()){
//				if (getBufferData().isVOBufferEmpty()) {
//					getBufferData().addVOToBuffer(billVO);
//					nCurrentRow = 0;
//				} else {
					//end
					DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+billVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=0");
					billVO.setChildrenVO(bvos);
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
//				}
				}else{
					getBufferData().addVOToBuffer(billVO);
				}
//			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//			String ss=jcp.getText();
////			jcp.getUITextField().setDocument(docu);
//			jcp.getUITextField().setText(ss);
			setAddNewOperate(isAdding(), billVO);
//			if(isAdding()){
				updateIsYY(isdeploy,"Y", pk_sysregister_h, deploycode, pk_node);
//			}

			/** 程莉 2011-5-19 在保存时，如果是新增，则直接创建表，如果是修改，则提示是否建表，如果不创建，当前的状态处于可编辑状态 begin*/

			//2011-6-3修改保存时，判断是否已经创建表：如果有创建表，则提示已经建表，是否重新创建表结构
			/** end */

			//2011-7-7 
			if(isEditing()){
			//	JdbcPersistenceManager.clearDatabaseMeta();//修改表结构后，清除缓存
				JdbcPersistenceManager.clearDatabaseMeta();
			}
		}	

		setSaveOperateState();
		if (nCurrentRow >= 0)
			getBufferData().setCurrentRow(nCurrentRow);

		//如果该单据增加保存时是自动维护树结构，则执行如下操作
		if (getUITreeCardController().isAutoManageTree()) {			
			getSelfUI().insertNodeToTree(billVO.getParentVO());
		}
		//保存后，将"创建表"按钮设置为可用
		/*getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
		getSelfUI().updateButtonUI();*/
		
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		
	}
	/**
	 * 功能：处理删除行上的数据，如果有删行，则在保存时，把真正要删除的删掉
	 * 作者：ytq
	 * 日期:2011-07-02
	 * */
	public void deleteLine(){
		String sql =" delete from dip_datadefinit_b where pk_datadefinit_b in ('11122aabb'"+getSelfUI().delstr+")";
		IQueryField qry = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		try {
			qry.exesql(sql);
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DbException e) {
			e.printStackTrace();
		}
		getSelfUI().delstr = "";

	}
	/**验证，分布标志
	 * 功能：
	 * 作者：程莉
	 * 日期：2011-07-02
	 * */
	public String checkTableHead(DataDefinitClientUI ui,String pk){
		boolean isdeploy=Boolean.parseBoolean( ui.getBillCardPanel().getHeadItem("isdeploy").getValueObject().toString());//是否分布式系统
		String pk_sysregister_h=(String) ui.getBillCardPanel().getHeadItem("pk_sysregister_h").getValueObject();
		String deploycode=(String) ui.getBillCardPanel().getHeadItem("deploycode").getValueObject();//变化后的分布式标识
		String pk_node=ui.selectnode;
		//2011-6-1 修改保存时：修改其是否引用标识的方法
		//1.改变了：查询是否有其他系统引用了变化前的站点，如果有，则对勾保留，如果没有，则不打上对勾
		//2.没改变，则还是以前的状态

		if(isdeploy){
			String sql="select deploycode from dip_datadefinit_h where pk_datadefinit_h='"+pk+"'";
			String olddeploycode = null;
			try {
				olddeploycode = iq.queryfield(sql);
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			}//变化前的分布式标识:如果olddeploycode=""的话，则为新增,否则为修改
			if(olddeploycode==null)
				olddeploycode = "";
			if(deploycode==null)
				deploycode = "";
			if("".equals(deploycode)){
				getSelfUI().showWarningMessage("分布式标识不能为空！");
				return "";
			}else{
				if(!olddeploycode.equals(deploycode)){
					String sql2="select deploycode from dip_datadefinit_h where pk_sysregister_h ='"+pk_sysregister_h+"'" +
					" and nvl(dip_datadefinit_h.dr,0)=0 and deploycode='"+olddeploycode+"' and pk_datadefinit_h<>'"+pk_node+"' and nvl(dr,0)=0";
					ArrayList arrayList = null;
					try {
						arrayList = (ArrayList)queryBS.executeQuery(sql2, new MapListProcessor());
					} catch (BusinessException e) {
						e.printStackTrace();
					}
					if(arrayList==null || arrayList.size()==0){
						String uapsql="update dip_sysregister_b set isyy='N' where pk_sysregister_h='"+pk_sysregister_h+"'"
						+" and dip_sysregister_b.pk_sysregister_b='"+olddeploycode+"' and nvl(dr,0)=0" ;	
						//修改变化前的分别站点是否引用标志
						try {
							iq.exesql(uapsql);
						} catch (BusinessException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}

						//修改变化后新站点的是否引用标志
						updateIsYY(isdeploy,"Y", pk_sysregister_h, deploycode, pk_node);					
					}
				}
			}
		}else{
			//2011-6-9 如果由以前的 是否分布式数据 修改为 非分布式数据，那么分布式标志和分布式名称（站点）都为空，这时修改系统注册表体的是否引用字段为N
			String sql="select deploycode from dip_datadefinit_h where pk_datadefinit_h='"+pk+"'";
			String olddeploycode = null;
			try {
				olddeploycode = iq.queryfield(sql);
			} catch (BusinessException e) {
				e.printStackTrace();
				return "";
			} catch (SQLException e) {
				e.printStackTrace();
				return "";
			} catch (DbException e) {
				e.printStackTrace();
				return "";
			}//变化前的分布式标识:如果olddeploycode=""的话，则为新增,否则为修改
			if(olddeploycode==null)
				olddeploycode = "";
			if(deploycode==null)
				deploycode = "";
			if(!olddeploycode.equals(deploycode)){
				String sql2="select deploycode from dip_datadefinit_h where pk_sysregister_h ='"+pk_sysregister_h+"'" +
				" and nvl(dip_datadefinit_h.dr,0)=0 and deploycode='"+olddeploycode+"' and pk_datadefinit_h<>'"+pk_node+"' and nvl(dr,0)=0";
				ArrayList arrayList = null;
				try {
					arrayList = (ArrayList)queryBS.executeQuery(sql2, new MapListProcessor());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(arrayList==null || arrayList.size()==0){
					String uapsql="update dip_sysregister_b set isyy='N' where pk_sysregister_h='"+pk_sysregister_h+"'"
					+" and dip_sysregister_b.pk_sysregister_b='"+olddeploycode+"' and nvl(dr,0)=0" ;	
					//修改变化前的分别站点是否引用标志
					try {
						iq.exesql(uapsql);
					} catch (BusinessException e) {
						e.printStackTrace();
						return "";
					} catch (SQLException e) {
						e.printStackTrace();
						return "";
					} catch (DbException e) {
						e.printStackTrace();
						return "";
					}				
				}
			}
		}
		return "ok";
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
	public void onTreeSelected(VOTreeNode arg0) {
		try {
			// 获取权VO
			SuperVO vo = (SuperVO) arg0.getData();
			DipDatadefinitHVO tnhvo=(DipDatadefinitHVO) arg0.getData();
			
			String tab=tnhvo.getMemorytable()==null?"":tnhvo.getMemorytable().toString();
			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
			if(docu!=null){
				jcp.getUITextField().setDocument(docu);
				jcp.setText(tab);	
			}else{
				docu=jcp.getUITextField().getDocument();
			}
			
//			UITextDocument uitex=new UITextDocument();
//			uitex.s
//			uitex.
//			jcp.getUITextField().setDocument(uitex);
//			docu=jcp.getUITextField().getDocument();
//			jcp.getUITextField().setDocument(new PlainDocument());
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").set
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setValue(tab);
//			getBillCardPanelWrapper().getBillCardPanel().
			
			String pk_datadefinit_h = (String) vo.getAttributeValue("pk_datadefinit_h");

			SuperVO[] vos = HYPubBO_Client.queryByCondition(nc.vo.dip.datadefinit.DipDatadefinitBVO.class, "pk_datadefinit_h='" + pk_datadefinit_h + "' and isnull(dr,0)=0  ");

			nc.vo.dip.datadefinit.MyBillVO billvo = new nc.vo.dip.datadefinit.MyBillVO();

			/*
			 * 描述：”是否分布式数据“打对勾如否的变化：
			 * 如果系统注册中的”分布系统“修改了，由未打上对勾修改为打上对勾了，则该系统下的子节点的”是否分布式数据“也应该由未打上对勾变为打上对勾
			 * @date 2011-5-9 13:42
			 * begin
			 */
			VOTreeNode treeNode=getSelectNode();
			String pid=null;
			DipSysregisterHVO syshvo;
			if(treeNode !=null){
				pid=(String) treeNode.getParentnodeID();
			}
			if(treeNode!=null&&"".equals(treeNode.getParentnodeID())){
//				DipSysregisterHVO syshvo;
				try{
					syshvo=(DipSysregisterHVO)HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, treeNode.getNodeID().toString());
					extcode=syshvo.getExtcode();
				}catch(UifException e){
					e.printStackTrace();
				}
			}else if(treeNode!=null&&treeNode.getParentnodeID()!=null&&treeNode.getParentnodeID().toString().length()>0){
				try{
					syshvo=(DipSysregisterHVO)HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, tnhvo.getPk_xt()/*treeNode.getParentnodeID().toString()*/);
					extcode=syshvo.getExtcode();
				}catch(UifException e){
					e.printStackTrace();
				}
			}


			String sql="select isdeploy from dip_sysregister_h where pk_sysregister_h='"+pid+"' and nvl(dr,0)=0";
			try{
				//是否分布式数据不可编辑
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
				String isdeploy=iq.queryfield(sql);	
				//如果是分布式数据
				if(isdeploy.equals("Y")){
					//分布式标识为可编辑	
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
				}else{
					//分布式标识为不可编辑	
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}

			DipDatadefinitHVO hvo= (DipDatadefinitHVO)vo;

			/* 2011-5-8 程莉 修改"是否已建表"标志 begin */
//			String fpk=(String) vo.getAttributeValue("pk_sysregister_h");
			String fpk=hvo.getPk_xt();//(String) vo.getAttributeValue("pk_xt");
			String tableName=hvo.getMemorytable();//(String) vo.getAttributeValue("memorytable");
			UFBoolean flag=null;//是否已建表
			if(pk_datadefinit_h !=null && (fpk !=null && !fpk.trim().equals(""))&&!hvo.getIsfolder().booleanValue()&&hvo.getMemorytype().equals("表")){
				getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
				boolean isExist=isTableExist(tableName);
				if(isExist){
					updateIsCreateFlag("Y", fpk, pk_datadefinit_h);
					//2011-6-29 重新设置是否已建表标志
					flag=new UFBoolean(true);				
				}else{
					updateIsCreateFlag("N", fpk, pk_datadefinit_h);
					flag=new UFBoolean(false);
				}
				//2011-6-29 
				hvo.setIscreatetab(flag==null?"N":flag.toString());
				vo=hvo;
			}
			/* end */

			// 设置主、子VO
			billvo.setParentVO(vo);
			billvo.setChildrenVO(vos);
			// 显示数据
			getBufferData().addVOToBuffer(billvo);

			//2011-4-26 程莉 动态自定义参照：查询当前系统下的分布式标识			
//			String pk_sysregister_h = hvo.getPk_sysregister_h();
			UIRefPane pane=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").getComponent();
			SysRegisterRefModel model = new SysRegisterRefModel();
			model.addWherePart(" and dip_sysregister_b.pk_sysregister_h='"+fpk+"' and nvl(dip_sysregister_b.dr,0)=0  and (dip_sysregister_b.isstop='N' or dip_sysregister_b.isstop='') ");
			pane.setRefModel(model);



			UIRefPane pane2=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
			QuoteTableTreeRefModel model2=new QuoteTableTreeRefModel();
			model2.addWherePart(" and pk_datadefinit_h1 <>'"+pk_datadefinit_h+"'");
			pane2.setRefModel(model2);

			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));


		} catch (ComponentException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 版权所有： 商佳科技
	 * 作者： 程莉
	 * 描述：复制当前界面的数据，并且新增一个当前的节点，改表名，编码和名称为空，且处于可编辑状态
	 * 时间：2011-4-22
	 */
	@Override
	protected void onBoCopy() throws Exception {	
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要复制的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做复制操作！"); 
			return;
		}else{
			DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();
			
			if(defhvo!=null&&defhvo.getIsfolder().booleanValue()){
				getSelfUI().showErrorMessage("文件夹节点不能复制！");
				return;
			}
			
			if(defhvo!=null&&defhvo.getIsfolder().booleanValue()==false){
				DipSysregisterHVO hvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, defhvo.getPk_xt());
				if(hvo==null){
					getSelfUI().showErrorMessage("没有找到对应的系统注册！");
					return;
				}
				/*查询NC系统下的节点，不允许做修改操作 2011-5-23 程莉 begin */
					getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
					try {
						super.onBoCopy();
					} catch (Exception e) {
						e.printStackTrace();
					}
					UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
					docu=jcp.getUITextField().getDocument();
					if(defhvo.getMemorytype().equals("表")){
						if(defhvo.getPk_xt()!=null&&!defhvo.getPk_xt().equals("0001AA1000000001XQ1B")){
							jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
							jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");	
						}
						
					}
					//2011-6-29 程莉 复制的时候，避免如果当前被复制的数据有主键或者唯一约束的情况下，表体引用表无法参照出该表问题
					UIRefPane pane=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
					//如果只参照当前系统下已经建表的,加上下面条件，如果是全部系统下定义的表，把下面的条件注释即可
					//2011-6-29 由于要参照所有系统下的已经定义的表,以及原始的参照类条件没有加“是否系统预置主键=Y”，所以把参照类给换了，上面限制本系统下的条件给注释了
					QuoteTableTreeRefModel model=new QuoteTableTreeRefModel();
					model.addWherePart(" and pk_datadefinit_h1 <> '"+tempNode.getNodeID()+"'");
					pane.setRefModel(model);
					if(hvo.getIsdeploy()==null||hvo.getIsdeploy().equals("N")){//不是分布系统
						getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
						getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
					}else{
						getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(true);
						getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
					}
			}
			
			}

		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("iscreatetab").setValue(new UFBoolean(false));
	}
	public void onBoMBCopy(AskMBDLG ask) throws Exception{
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要复制的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做复制操作！"); 
			return;
		}else{
			DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();
			
			if(defhvo!=null&&defhvo.getIsfolder().booleanValue()){
//				UIRefPane ref=new UIRefPane();
//				ContDataBussinessTableRefModel mo=new ContDataBussinessTableRefModel();
//				ref.setRefModel(mo);
//				ref.connEtoC1();
				String selectPk=ask.getOrgnizeRefPnl().getRefValue("pk_datadefinit_h")==null?"":ask.getOrgnizeRefPnl().getRefValue("pk_datadefinit_h").toString();
				if(selectPk!=null&&selectPk.trim().length()==20){
						
							DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, selectPk);
							SuperVO[] bvos=HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+selectPk+"' and nvl(dr,0)=0 ");
							if(bvos!=null&&bvos.length>0){
								for(int i=0;i<bvos.length;i++){
								  bvos[i].setPrimaryKey(null);	
								}	
							}
							onBoAdd(null);
							getSelfUI().getBillCardPanel().getBillModel("dip_datadefinit_b").setBodyDataVO(bvos);
								UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//								docu=jcp.getUITextField().getDocument();
								if(hvo.getMemorytype().equals("表")){
									if(defhvo.getPk_xt()!=null&&!defhvo.getPk_xt().equals("0001AA1000000001XQ1B")){
										jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
										jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");	
									}
								}
								UIRefPane pane=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
								//如果只参照当前系统下已经建表的,加上下面条件，如果是全部系统下定义的表，把下面的条件注释即可
								//2011-6-29 由于要参照所有系统下的已经定义的表,以及原始的参照类条件没有加“是否系统预置主键=Y”，所以把参照类给换了，上面限制本系统下的条件给注释了
								QuoteTableTreeRefModel model=new QuoteTableTreeRefModel();
								model.addWherePart(" and pk_datadefinit_h1 <> '"+selectPk+"'");
								pane.setRefModel(model);
								if(hvo.getIsdeploy()==null||hvo.getIsdeploy().equals("Y")){//不是分布系统
									getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(true);
									getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
								}else{
									getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
									getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
								}
					this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("iscreatetab").setValue(new UFBoolean(false));
				}
			}else{
				getSelfUI().showErrorMessage("叶节点不能复制！");
				return;
			}
		}
		
		
	
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		//取消，将"创建表"按钮设置为可用
		if(docu!=null){
			getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
			jcp.getUITextField().setDocument(docu);
			//jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");
		}
		super.onBoCancel();
		getSelfUI().delstr ="";//ytq 2011-07-02 取消清空删行缓存
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
//		VOTreeNode node=getSelectNode();
//		if(node!=null){
//			DipDatadefinitHVO hvo=(DipDatadefinitHVO) node.getData();
//			if(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
//				getSelfUI().getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Copy).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
//				
//			}else{
//				getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//				
//			}
//			getSelfUI().updateButtonUI();
//		}
		
		
		
	}


	@Override
	protected void onBoLinePaste() throws Exception {
		super.onBoLinePaste();
		//复制粘贴行时子表主键置为空：避免保存时报违反唯一性错误
		int currow=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		getSelfUI().getBillCardPanel().setBodyValueAt("", currow, "pk_datadefinit_b");
	}

	@Override
	protected void onBoLineDel() throws Exception {

		int countrow=getSelfUI().getBillCardPanel().getBillTable().getRowCount();
		if(countrow<0){
			return;
		}
 
//		int rowno=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		int rownos[]=getSelfUI().getBillCardPanel().getBillTable().getSelectedRows();
		if(rownos!=null&&rownos.length>0){
			int rowno=-1;
			for(int i=0;i<rownos.length;i++){
				rowno=rownos[i];	
				if(rowno>=0){
//					getSelfUI().showErrorMessage("请选择要操作的行！");
//					return;
					nc.vo.dip.datadefinit.MyBillVO billvo=(nc.vo.dip.datadefinit.MyBillVO)getSelfUI().getBufferData().getCurrentVO();
					DipDatadefinitBVO[] vos=(DipDatadefinitBVO[]) getSelfUI().getChangedVOFromUI().getChildrenVO();
					String ob=getSelfUI().getBillCardPanel().getBillModel().getValueAt(rowno, "pk_datadefinit_b")==null?"":getSelfUI().getBillCardPanel().getBillModel().getValueAt(rowno, "pk_datadefinit_b").toString();
					if(ob==null||"".equalsIgnoreCase("pk_datadefinit_b")){
						super.onBoLineDel();
						getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";
					}else{
//						super.onBoLineDel();
//						
						getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";			
						String sb=ob.toString();
						System.out.println("删除主键是"+sb+"的vo");
						if(i==rownos.length-1){
							getSelfUI().getBillCardPanel().getBillModel().delLine(rownos);	
						}
					}
				}
				
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
	
	public void onBoMBZH()throws Exception{
		 VOTreeNode tempNode = getSelectNode();
		 if(tempNode==null){
			 getSelfUI().showErrorMessage("选择节点数据错误！");
			 return;
		 }
		 DipDatadefinitHVO hvo=(DipDatadefinitHVO) tempNode.getData();
		 AskMBDLG ask=null;
		 if(hvo!=null&&hvo.getPk_xt().equals("0001AA1000000001XQ1B")){
			 ask=new AskMBDLG(getSelfUI(),null,"模板","        请选择操作类型?",new String[]{"导入外部文件数据定义格式","导出外部文件数据定义格式","根据NC数据字典自动生成数据定义格式","根据数据库物理表生成数据定义格式","根据数据定义复制格式"});
			 ask.showModal();
			 int result=ask.getRes();
				if(result==0){
					onBoImport();
				}else if(result==1){
					onBoExport();
				}else if(result==2){
					onBoAutoBuiltDatadefinit();
				}else if(result==3){
					onBoPhysicTable();
				}else if(result==4){
					onBoMBCopy(ask);
				}
		 }else{
			  ask=new AskMBDLG(getSelfUI(),null,"模板","        请选择操作类型?",new String[]{"导入外部文件数据定义格式","导出外部文件数据定义格式","根据数据库物理表生成数据定义格式","根据数据定义复制格式"});
			  ask.showModal();
			  int result=ask.getRes();
				if(result==0){
					onBoImport();
				}else if(result==1){
					onBoExport();
				}else if(result==2){
					onBoPhysicTable();
				}else if(result==3){
					onBoMBCopy(ask);
				}
		 }
		 
		
	}
	private void onBoPhysicTable() throws Exception {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		 VOTreeNode tempNode = getSelectNode();
			if(tempNode==null){
				getSelfUI().showErrorMessage("请选择要导出的节点！");
				return ;
			}
			String str=(String)tempNode.getParentnodeID();
			if(str ==null || str.equals("")){
				getSelfUI().showWarningMessage("系统节点不能做导出操作！"); 
				return;
			}else{
				DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();

				if(defhvo==null){
					getSelfUI().showErrorMessage("该节点数据错误，请刷新后在操作！");
					return;
				}
				if(defhvo.getIsfolder().booleanValue()){
					UIRefPane ref=new UIRefPane();
					PhysicTableRefModel mo=new PhysicTableRefModel();
					ref.setRefModel(mo);
					ref.connEtoC1();
					String tablename=ref.getRefValue("table_name")==null?"":ref.getRefValue("table_name").toString();
					
					if(tablename!=null&&tablename.trim().length()>0){
						PhysicTableVO tablevos[]=null;
//						tablevos=(PhysicTableVO[])HYPubBO_Client.queryByCondition(PhysicTableVO.class, "TABLE_NAME='"+tablename+"'" );
						String sqlt=" select column_name ,data_type ,data_length ,data_precision , data_Scale,nullable,data_default  " +
							 	   " from user_tab_columns where table_name='"+tablename+"'";
						List listcolumns=iq.queryVOBySql(sqlt, new PhysicTableVO());//所有表字段。
						List listPk=iq.queryfieldList("select ss.constraint_name from user_constraints ss where  ss.table_name='"+tablename+"' and ss.constraint_type='P'");
						String pkcolum="";
						if(listPk!=null&&listPk.size()==1){
							pkcolum=listPk.get(0)==null?"":listPk.get(0).toString();
						}
						if(listcolumns!=null&&listcolumns.size()>0){
							DipDatadefinitBVO bvos[]=new DipDatadefinitBVO[listcolumns.size()];
							for(int i=0;i<listcolumns.size();i++){
								PhysicTableVO tablevo=(PhysicTableVO) listcolumns.get(i);
								bvos[i]=new DipDatadefinitBVO();
								bvos[i].setEname(tablevo.getColumn_name());
								String type=tablevo.getData_type();
								bvos[i].setDefaultvalue(tablevo.getData_default());
								bvos[i].setIsimport(new UFBoolean(!tablevo.getNullable().booleanValue()));
								if(type.equals("NUMBER")){
									if(tablevo.getData_precision()==0){
										bvos[i].setType("INTEGER");	
									}else{
										bvos[i].setType(type);
									}
									bvos[i].setDeciplace(tablevo.getData_Scale());
									bvos[i].setLength(tablevo.getData_precision());	
									
								}else{
									if(type.contains("VARCHAR")){
										bvos[i].setType("VARCHAR");
									}else{
										bvos[i].setType(type);
									}
										bvos[i].setLength(tablevo.getData_length());
								}
								if(pkcolum.length()>0&&pkcolum.equals(tablevo.getColumn_name())){
									bvos[i].setIspk(new UFBoolean(true));
									bvos[i].setIsimport(new UFBoolean(true));
								}
							}	
							onBoAdd(null);
							getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(bvos);
						}
						
					}
				}else{
					showErrorMessage("叶节点不能做改操作");
					return;
				}
				
			}
		
	
		
	}

	private void onBoAutoBuiltDatadefinit() throws Exception{
		// TODO Auto-generated method stub
		 VOTreeNode tempNode = getSelectNode();
			if(tempNode==null){
				getSelfUI().showErrorMessage("请选择要导出的节点！");
				return ;
			}
			String str=(String)tempNode.getParentnodeID();
			if(str ==null || str.equals("")){
				getSelfUI().showWarningMessage("系统节点不能做导出操作！"); 
				return;
			}else{
				DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();

				if(defhvo==null){
					getSelfUI().showErrorMessage("该节点数据错误，请刷新后在操作！");
					return;
				}
				if(defhvo.getIsfolder().booleanValue()){
					UIRefPane ref=new UIRefPane();
					PubDatadictTreeRefModel mo=new PubDatadictTreeRefModel();
					ref.setRefModel(mo);
					ref.connEtoC1();
					String tablename=ref.getRefValue("id")==null?"":ref.getRefValue("id").toString();
					String display=ref.getRefValue("display")==null?"":ref.getRefValue("display").toString();
					if(tablename!=null&&tablename.trim().length()>0){
						String sqlexist=" select syscode from  dip_datadefinit_h  where memorytable='"+tablename+"' and nvl(dr,0)=0" ;
						String value=iq.queryfield(sqlexist);
						if(value!=null&&value.trim().length()>0){
							showErrorMessage("表"+tablename+"数据定义已经存在，编码是"+value);
							return;
						}
						List<DipDatadefinitBVO> list=getDataDictVOS(tablename);
						if(list!=null&&list.size()>0){
							DipDatadefinitHVO hvo=new DipDatadefinitHVO();
							hvo.setBusicode(tablename.toUpperCase());
							hvo.setSyscode(tablename);
							hvo.setSysname(display);
							hvo.setDatatype("0001BB100000000JJOKC");//0001BB100000000JJOKC 基础信息结构
							hvo.setMemorytable(tablename.toUpperCase());
							hvo.setMemorytype("表");
							hvo.setIscreatetab("Y");
							hvo.setTabsoucetype("自定义");
							hvo.setIsfolder(new UFBoolean("N"));
							hvo.setPk_xt(defhvo.getPk_xt());
							hvo.setPk_sysregister_h(defhvo.getPk_datadefinit_h());
							hvo.setIsdeploy(new UFBoolean("N"));
							String newpk=HYPubBO_Client.insert(hvo);
							if(newpk!=null&&newpk.length()==20){
								hvo.setPk_datadefinit_h(newpk);
								DipDatadefinitBVO bvos[]=list.toArray(new DipDatadefinitBVO[0]);
								if(bvos!=null&&bvos.length>0){
									String pk=defhvo.getPk_datadefinit_h();
									if(pk!=null&&pk.trim().length()>0){
									 String sql="update dip_datadefinit_b set dr=1 where pk_datadefinit_h='"+newpk+"'"+"and nvl(dr,0)=0 " ;
									 iq.exesql(sql);
									  for(int i=0;i<bvos.length;i++){
										  bvos[i].setPk_datadefinit_h(newpk);
									  }
									  HYPubBO_Client.insertAry(bvos);
//									  SuperVO[] supvos=HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+newpk+"' and nvl(dr,0)=0 ");
//									  if(supvos!=null&&supvos.length>0){
//										  getSelfUI().getBillCardPanel().getBillModel("dip_datadefinit_b").setBodyDataVO(supvos) ;
//									  }
									  
									  if (getUITreeCardController().isAutoManageTree()) {			
											getSelfUI().insertNodeToTree(hvo);
										}
										
										getSelfUI().onTreeSelectSetButtonState(getSelectNode());
									  if(errType.length()>0){
										  showErrorMessage("中文名称是【"+errType.substring(0, errType.length()-1)+"】的字段取数据类型错误，设置成默认类型varchar");
									  }
										
									}
								}
							}else{
								showErrorMessage("生成数据定义主表出错，请刷新后重做");
							}
							
						}else{
							showErrorMessage("数据字典中没有查到表"+tablename+"的信息");
						}
					}
				}else{
					showErrorMessage("叶节点不能做改操作");
					return;
				}
				
			}
		
	}

	final  String  EXPSTRINGNAME="中文名称,英文名称,类型,长度,小数位,默认值,必输,主键,预置主键,唯一约束,引用,引用表,引用列";
	final  String  IMPSTRINGCODE="cname,ename,type,length,deciplace,defaultvalue,isimport,ispk,issyspk,isonlyconst,isquote,def_quotetable,quotecolu";
   @Override
	protected void onBoExport() throws Exception {
		// TODO Auto-generated method stub
		   VOTreeNode tempNode = getSelectNode();
			if(tempNode==null){
				getSelfUI().showErrorMessage("请选择要导出的节点！");
				return ;
			}
			String str=(String)tempNode.getParentnodeID();
			if(str ==null || str.equals("")){
				getSelfUI().showWarningMessage("系统节点不能做导出操作！"); 
				return;
			}else{
				DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();

				if(defhvo==null){
					getSelfUI().showErrorMessage("该节点数据错误，请刷新后在操作！");
					return;
				}
				if(defhvo.getIsfolder().booleanValue()){
					getSelfUI().showErrorMessage("文件夹节点不能做导出操作！");
					return;
				}else{
					String pk_datadefinit_h=defhvo.getPk_datadefinit_h();
					String tablename=defhvo.getMemorytable()==null?"sheet1":defhvo.getMemorytable()+(defhvo.getSysname()==null?"":defhvo.getSysname());
					DipDatadefinitBVO datadefinitBvos[]=null;
					datadefinitBvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+pk_datadefinit_h+"' and nvl(dr,0)=0 ");
					if(datadefinitBvos!=null&&datadefinitBvos.length>0){
						ExpExcelVO expExcelvos[]=new ExpExcelVO[datadefinitBvos.length];
						for(int i=0;i<datadefinitBvos.length;i++){
							DipDatadefinitBVO bvo=datadefinitBvos[i];
							if(bvo!=null){
								expExcelvos[i]=new ExpExcelVO();
								expExcelvos[i].setAttributeValue("line1",bvo.getCname()==null?"":bvo.getCname());
								expExcelvos[i].setAttributeValue("line2",bvo.getEname()==null?"":bvo.getEname());
								expExcelvos[i].setAttributeValue("line3",bvo.getType()==null?"":bvo.getType());
								expExcelvos[i].setAttributeValue("line4",bvo.getLength()==null?"":bvo.getLength());
								expExcelvos[i].setAttributeValue("line5",bvo.getDeciplace()==null?"":bvo.getDeciplace());
								expExcelvos[i].setAttributeValue("line6",bvo.getDefaultvalue()==null?"":bvo.getDefaultvalue());
								expExcelvos[i].setAttributeValue("line7",bvo.getIsimport()==null?"":bvo.getIsimport());
								expExcelvos[i].setAttributeValue("line8",bvo.getIspk()==null?"":bvo.getIspk());
								expExcelvos[i].setAttributeValue("line9",bvo.getIssyspk()==null?"":bvo.getIssyspk());
								expExcelvos[i].setAttributeValue("line10",bvo.getIsonlyconst()==null?"":bvo.getIsonlyconst());
								expExcelvos[i].setAttributeValue("line11",bvo.getIsquote()==null?"":bvo.getIsquote());
								if(bvo.getIsquote()!=null&&bvo.getIsquote().booleanValue()){
									if(bvo.getQuotetable()!=null&&bvo.getQuotetable().trim().length()>0){
									String sql="select  h.memorytable from dip_datadefinit_h h where h.pk_datadefinit_h in( select pk_datadefinit_h from dip_datadefinit_b b where b.pk_datadefinit_b ='"+bvo.getQuotetable()+"' and nvl(dr,0)=0 ) and nvl(h.dr,0)=0 ";
									String quoteTablename=iq.queryfield(sql);
										if(quoteTablename!=null&&quoteTablename.length()>0){
											expExcelvos[i].setAttributeValue("line12",quoteTablename);
											expExcelvos[i].setAttributeValue("line13",bvo.getQuotecolu()==null?"":bvo.getQuotecolu());
										}else{
											expExcelvos[i].setAttributeValue("line12","");
											expExcelvos[i].setAttributeValue("line13","");
										}
								    }else{
										expExcelvos[i].setAttributeValue("line12","");
										expExcelvos[i].setAttributeValue("line13","");
								    }
								}else{
									expExcelvos[i].setAttributeValue("line12","");
									expExcelvos[i].setAttributeValue("line13","");
								}
						}
					   }
						JFileChooser jfileChooser = new JFileChooser();
						jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
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
							ExpToExcel exp=new ExpToExcel(file,tablename,EXPSTRINGNAME,expExcelvos,null);
							this.getSelfUI().showWarningMessage("导出成功");
						}
					}
				}
			}
		 
	}
   @Override
	protected void onBoImport() throws Exception {
		// TODO Auto-generated method stub
	   
	   String []ss={"BLOB","BINARY_DOUBLE","BINARY_FLOAT","CHAR","CLOB","DATE","INTEGER","INTERVAL","LONG","LONGRAW","NCLOB","NUMBER","NVARCHAR","NVARCHAR2","RAW","TIMESTAMP","VARCHAR"};
	    Map<String, String> styleMap=new HashMap<String, String>();
	    for(int i=0;i<ss.length;i++){
	    	styleMap.put(ss[i], "Y");
	    }
	    VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要导入的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做导入操作！"); 
			return;
		}else{
			DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();
			if(defhvo==null){
				getSelfUI().showErrorMessage("该节点数据错误，请刷新后在操作！");
				return;
			}
			if(defhvo.getIsfolder().booleanValue()){
				//getSelfUI().showErrorMessage("文件夹节点能做导入操作！");
				JFileChooser jfileChooser = new JFileChooser();
				jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				if(jfileChooser.showSaveDialog(this.getSelfUI()) == javax.swing.JFileChooser.CANCEL_OPTION){
					return;
				}else{
					String file=jfileChooser.getSelectedFile().toString();
					if(file!=null&&file.length()>0){
						if(!file.endsWith(".xls")){
							file=file+".xls";
							}
							TxtDataVO tvo=getTxtDataVO(file,EXPSTRINGNAME,IMPSTRINGCODE,DipDatadefinitBVO.class);
							String[] notNullLine=new String[]{"cname","ename","type"};
							if(tvo!=null){
								Vector vec=tvo.getDatavo();
								if(vec!=null&&vec.size()>0){
									List<DipDatadefinitBVO> displayList=new ArrayList<DipDatadefinitBVO>();//所有正确vo，要显示到界面的vo
									DipDatadefinitBVO bvos[]=new DipDatadefinitBVO[vec.size()];
										vec.toArray(bvos);
										if(bvos!=null&&bvos.length>0){
											StringBuffer sb=new StringBuffer();
											Map enameMap=new HashMap();
											Map cnameMap=new HashMap();
											for(int i=0;i<bvos.length;i++){
											    sb=new StringBuffer();
												boolean flag=false;
												DipDatadefinitBVO bvo=bvos[i];
												if(bvo!=null){
													//一般不能为空校验
													HashMap map=tvo.getCodeToNameMap();
													if(notNullLine!=null&&notNullLine.length>0){
														for(int w=0;w<notNullLine.length;w++){
															String notnullcode=notNullLine[w];
															Object notNullCodeValue=bvo.getAttributeValue(notnullcode);
															if(notNullCodeValue==null||notNullCodeValue.toString().trim().equals("")){
																
																if(map!=null&&map.get(notnullcode)!=null){
																	sb.append(map.get(notnullcode)+"字段不能为空");
																	break;
																}
																
															}else{
																if(notnullcode.equals("ename")){
																	if(enameMap.get(notNullCodeValue)==null){
																		enameMap.put(notNullCodeValue, i+1);	
																	}else{
																		sb.append("第"+enameMap.get(notNullCodeValue)+"行和第"+(i+1)+"行"+map.get(notnullcode)+"字段重复");
																	}
																}
																if(notnullcode.equals("cname")){
																	if(cnameMap.get(notNullCodeValue)==null){
																		cnameMap.put(notNullCodeValue, i+1);	
																	}else{
																		sb.append("第"+cnameMap.get(notNullCodeValue)+"行和第"+(i+1)+"行"+map.get(notnullcode)+"字段重复");
																	}
																}
																
															}	
														}
															
													}
													if(styleMap!=null){
														String type=bvo.getType();
														if(!(type!=null&&styleMap.get(type)!=null)){
															sb.append("类型字段不能是"+type);
														}
													}
													
													//特殊校验校验
													if(sb.length()<=0&&bvo.getIsquote()!=null&&bvo.getIsquote().booleanValue()){
														String def_quotetable=bvo.getDef_quotetable();
														String quotecolu=bvo.getQuotecolu();
														if(def_quotetable!=null&&def_quotetable.trim().length()>0){
															String sql=" select h.pk_datadefinit_h from Dip_Datadefinit_h h where h.memorytable='"+def_quotetable+"' and h.iscreatetab='Y' and nvl(h.dr,0)=0 ";
															List list=iq.queryfieldList(sql);
															if(list!=null&&list.size()>0){
																if(list.size()==1){
																	String value=list.get(0)==null?"":list.get(0).toString();
																	if(value.trim().length()>0){
																		if(quotecolu!=null&&quotecolu.trim().length()>0){
																		DipDatadefinitBVO queryBvo[]=null;
																		queryBvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+value+"' and ename='"+quotecolu+"' and nvl(dr,0)=0 ");
																			if(queryBvo!=null&&queryBvo.length>0){
																				if(queryBvo.length==1){
																					DipDatadefinitBVO bbvo=queryBvo[0];
																					if(bbvo!=null&&((bbvo.getIspk()!=null&&bbvo.getIspk().booleanValue())||(bbvo.getIsonlyconst()!=null&&bbvo.getIsonlyconst().booleanValue()))){
																						flag=true;
																						bvos[i].setDef_quotetable(def_quotetable);
																						bvos[i].setQuotetable(bbvo.getPk_datadefinit_b());
																					}else{
																						sb=sb.append("引用表"+def_quotetable+"的"+quotecolu+"字段没有唯一标识");
																					}
																				}else{
																					//该表改字段存在多个。
																					sb=sb.append("引用表"+def_quotetable+"重复存在"+quotecolu+"字段");
																				}
																			}else{
																				//没有改字段
																				sb=sb.append("引用表"+def_quotetable+"不存在"+quotecolu+"字段");
																			}
																		}
																	}else{
																		sb=sb.append("引用表"+def_quotetable+"不存在");
																	}
																	
																}else{
																	//不是一个
																	sb=sb.append("引用表"+def_quotetable+"存在"+list.size()+"个");
																}
															}else{
																//不存在该表 
																sb=sb.append("引用表"+def_quotetable+"不存在");
															}
															
														}else{
															//没有引用表
															sb=sb.append("引用表"+def_quotetable+"不存在");
														}
														
														
													}
												}
												
												if(!flag){
													bvos[i].setIsquote(new UFBoolean(false));
													bvos[i].setDef_quotetable(null);
													bvos[i].setQuotecolu(null);
													bvos[i].setQuotetable(null);
												}
												
												if(sb!=null&&sb.toString().trim().length()>0){
													RowDataVO vo=tvo.getData()[i];
													int w=tvo.getColCount();
													vo.setAttributeValue(w+"", sb);
													tvo.getErrList().add(vo);
													
												}else{
													displayList.add(bvos[i]);
												}
											}
										}
										String errPath="";
									if(tvo.getErrList()!=null&&tvo.getErrList().size()>0){
//										ExpToExcel exp=new ExpToExcel();
										 errPath=file.replace(".xls", "-err.xls");
										tvo.getErrList().toArray(new RowDataVO[0]);
										List<RowDataVO []> vosList=new ArrayList<RowDataVO[]>();
										RowDataVO[] rowvo=tvo.getErrList().toArray(new RowDataVO[0]);
										vosList.add(rowvo);
										ExpToExcel1 toexcel=new ExpToExcel1(errPath,new String[]{tvo.getSheetName()},new String[]{tvo.getErrorTitle()},vosList,null);
										toexcel.create();
									}	
										onBoAdd(null);
										if(displayList!=null&&displayList.size()>0){
											SuperVO supervos[]=displayList.toArray(new SuperVO[0]);
											getSelfUI().getBillCardPanel().getBillModel("dip_datadefinit_b").setBodyDataVO(supervos);	
										}
										
										if(errPath.length()>0){
											getSelfUI().showErrorMessage("请查看错误信息【"+errPath+"】");	
										}
								}
								
							//}
							
							
						}	
					 }
				}
					
					
			}else{
				getSelfUI().showErrorMessage("非文件夹节点不能做导入操作！");
				return;
			}
		}
		
	}
   
   /**
    * 得到选择路径下的excel的vo对象TxtDataVO。
    * @param path
    * @return
    */
   public TxtDataVO getTxtDataVO(String path,String expTitleName,String impTitleCode,Class classvo){
		TxtDataVO tvo=new TxtDataVO();
		FileInputStream fin=null;
		HSSFWorkbook book=null;
		try{
			fin=new FileInputStream(path);
			book = new HSSFWorkbook(fin);
			HSSFSheet sheet = book.getSheetAt(0);
			if(sheet == null){
				showErrorMessage("导入文件格式不正确！");
				return null;
			}
			String sheetName=book.getSheetName(0);
			if(sheetName==null||sheetName.trim().equals("")){
				showErrorMessage("导入文件格式不正确！");
				return null;
			}
			//tvo.setTableName(sheetName);
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
				titlemap.put(Short.toString(i),(String)SJUtil.getCellValue(titleRow.getCell(i)));
			}
			tvo.setTitlemap(titlemap);
			String codes[]=impTitleCode.split(",");
			String names[]=expTitleName.split(",");//导出name
			tvo.setErrorTitle(expTitleName+",错误");
			HashMap<String,String> lineToCode=new HashMap<String, String>();
			HashMap<String,String> codeToName=new HashMap<String, String>();
			HashMap<String,String> nameToCode=new HashMap<String, String>();
			Vector<SuperVO> datavos=new Vector<SuperVO>();
			if(names!=null&&codes!=null&&codes.length==names.length){
				for(int w=0;w<names.length;w++){
					if(names[w]!=null&&!names[w].trim().equals("")&&codes[w]!=null&&!codes[w].trim().equals("")){
						nameToCode.put(names[w], codes[w]);
						codeToName.put(codes[w], names[w]);
					}
				}
			}else{
				showErrorMessage("导入excel字段长度和导出字段长度不相等！");
				return null;
			}
			if(titlemap!=null&&titlemap.size()>0&&codes!=null&&codes.length==titlemap.size()){
				for(int i=0;i<titlemap.size();i++){
					String impName="";
					if(titlemap.get(i)==null){
					   impName=titlemap.get(i+"");//导入name
					  if(impName==null||impName.trim().equals("")){
						  getSelfUI().showErrorMessage("导入字段名称不能为空！");
						  return null;
					  }
					  if(names.length<i){
						  showErrorMessage("导出excel字段错误！");
						  return null;
					  }else{
						 if(!(names[i]!=null&&names[i].equals(impName))){
							 showErrorMessage("第"+(i+1)+"个导入字段名和导出字段名不相同！");
							 return null;
						 }
					  }
					}
				 	if(nameToCode!=null){
				 		if(!impName.equals("")&&nameToCode.get(impName)!=null&&!nameToCode.get(impName).trim().equals("")){
				 			lineToCode.put(i+"", nameToCode.get(impName));
				 		}else{
				 			showErrorMessage("导入字段["+impName+"]没有找到对应编码！");
				 			return null;
				 		}
				 	}else{
				 		showErrorMessage("导入字段没有找到对应编码！");
				 		return null;
				 	}
					
				}
			}else{
				showErrorMessage("导入excel字段长度和导出字段长度不相等！");
				return null;
			}
			tvo.setLinetocodeMap(lineToCode);
			tvo.setNametocodeMap(nameToCode);
			tvo.setCodeToNameMap(codeToName);
			for(int i=1;i<=tvo.getRowCount();i++){
				HSSFRow row = sheet.getRow(tvo.getFirstDataRow() + i);
				SuperVO supervo=(SuperVO) Class.forName(classvo.getName()).newInstance();
				for(short j=0;j<titleRow.getPhysicalNumberOfCells();j++){
					tvo.setCellData(i-1, j, SJUtil.getCellValue(row.getCell(j)));
					if(lineToCode.get(j+"")!=null){
						String field=lineToCode.get(j+"");
						Class<Field> fiel=null;
						String ss="";
						try{
						 fiel=Class.forName(classvo.getName()).getDeclaredField(field)==null?null:(Class<Field>) Class.forName(classvo.getName()).getDeclaredField(field).getType();
							ss=SJUtil.getCellValue(row.getCell(j))==null?"":SJUtil.getCellValue(row.getCell(j)).toString();
							if(field!=null&&(field.equals("ename")||field.equals("type"))){
								ss=ss.trim().toUpperCase();
							}else{
								ss=ss.trim();
							}
//							if(checkline!=null&&checkline.length>0){
//								for(int c=0;c<checkline.length;c++){
//									if(c==j&&ss.trim().equals("")){
//										
//									}
//								}
//							}
						}catch(java.lang.NoSuchFieldException e){
							fiel=null;
						}
						if(fiel==null||fiel.toString().equals("class java.lang.String")){
							supervo.setAttributeValue(field, ss);	
						}else if(fiel.toString().equals("class java.lang.Integer")){
						
							int l=ss.length();
							if(l==0){
								ss="0";	
							}else{
								if(!ss.matches("[0-9]{1,"+l+"}")){
									ss="0";
								}
							}
							supervo.setAttributeValue(field, Integer.parseInt(ss));	
						}else if(fiel.toString().equals("class nc.vo.pub.lang.UFBoolean")){
							UFBoolean bol=null;
							if(ss.equals("Y")){
								bol=new UFBoolean(true);
							}else{
								bol=new UFBoolean(false);
							}
							supervo.setAttributeValue(field, bol);	
						}else{
							supervo.setAttributeValue(field, ss);	
						}
							
					}else{
						showErrorMessage("第"+j+"列，没有找到对应的编码字段！");
						return null;
					}
				}
				datavos.add(i-1, supervo);
			}
			if(datavos!=null){
				tvo.setDatavo(datavos);
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
   public void volidateTitleMap(HashMap<String,String> titleMap){
	   if(titleMap!=null){
		   
	   } 
   }
   
   public void showErrorMessage(String message){
	   MessageDialog.showErrorDlg(getSelfUI(), "错误", message);
   }
   StringBuffer errType;
      public List<DipDatadefinitBVO> getDataDictVOS(String tablename){
    	
	   List<DipDatadefinitBVO> list=new ArrayList<DipDatadefinitBVO>();
	   errType=new StringBuffer();
	   try {
		if(tablename!=null&&!tablename.trim().equals("")&&!tablename.startsWith("dip_")){
			if(!tablename.contains("_")){
				showErrorMessage("表名必须包含下划线");
				return null;
			}
			if(tablename.startsWith("_")){
				showErrorMessage("表名开头不能是下划线");
				return null;
			}
			String parent=tablename.substring(0, tablename.indexOf("_"));
			ObjectNode node=new FolderNode();
			node.setID(tablename.toLowerCase());
			node.setGUID(tablename.toLowerCase());
			node.setParentGUID(parent.toLowerCase());
			node.setKind("table");
			IBizObjStorage storage = (IBizObjStorage) NCLocator.getInstance().lookup(
					IBizObjStorage.class.getName());
			Object obj=storage.loadObject(IContrastUtil.DESIGN_CON, "nc.bs.pub.ddc.datadict.DatadictStorage", node);
			if(obj!=null&&obj instanceof TableDef){
				TableDef def=(TableDef) obj;
				FieldDefList deflist=def.getFieldDefs();
				if(deflist!=null){
				 int count=deflist.getCount();
				 for(int i=0;i<count;i++){
					 DipDatadefinitBVO bvo=new DipDatadefinitBVO();
					
					 FieldDef fieldDef= (FieldDef) deflist.get(i);
					 String cname=fieldDef.getDisplayName();//中文名称
					 String ename=fieldDef.getID();//英文名称
					 int type=-1;
					 type=fieldDef.getDataType();//类型
					 if(cname==null||cname.trim().equals("")||ename==null||ename.trim().equals("")||type==-1){
						 showErrorMessage("数据字典取数错误！");
						 return null;
					 }else{
						 bvo.setCname(cname);
						 bvo.setEname(ename.toUpperCase());
						 String tt=getType(type);
						 if(tt.equals("")){
							 tt="VARCHAR";
							 errType.append(cname+",");
						 }
						 bvo.setType(tt);
					 }
					 int length=fieldDef.getLength();// 长度
					 if(length!=0){
						 bvo.setLength(length);
					 }
					 int precision=fieldDef.getPrecision();//小数
					 if(precision!=0){
						 bvo.setDeciplace(precision);
					 }
					 boolean isnull=fieldDef.isNull();//是否允许为空
					 if(!isnull){
						 bvo.setIsimport(new UFBoolean(true));//是否必须
					 }
					 boolean isPrimary=fieldDef.getIsPrimary();//是否主键
					 if(isPrimary){
						 bvo.setIspk(new UFBoolean(true));
					 }
					 list.add(bvo);
				 }	
				}
				
			}
		}
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return list;
   }
   public String getType(int style){
	   String ss="";
	   switch(style){
	    case 1:ss="CHAR";break;
	    case 2:ss="NUMBER";break;
	    case 3:ss="NUMBER";break;
	    case 4:ss="INTEGER";break;
        case 5:ss="INTEGER";break;//SMALLINT
        case 12:ss="VARCHAR";break;
        case 2240:ss="BLOB";break;
	   }
	   return ss;
   }
   
}