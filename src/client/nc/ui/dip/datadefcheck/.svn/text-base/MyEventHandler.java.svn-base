package nc.ui.dip.datadefcheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
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
import nc.itf.uap.ddc.IBizObjStorage;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.ref.DataDefFieldbRefModel;
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
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefcheck.DipDatadefinitBVO;
import nc.vo.dip.datadefcheck.DipDatadefinitCVO;
import nc.vo.dip.datadefcheck.DipDatadefinitHVO;
import nc.vo.dip.datadefcheck.MyBillVO;
import nc.vo.dip.in.RowDataVO;
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


	public MyEventHandler(BillTreeManageUI billUI, ICardController control) {
		super(billUI, control);
	}

	public static String extcode;
	
	Integer selectrow=0;

	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private DataDefcheckClientUI getSelfUI() {
		return (DataDefcheckClientUI) getBillUI();
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
	 * 表头联动，表体
	 * */
	public void onCheckQuery() throws Exception{
		
//add by qw 2016-11-28
		int headcount = getSelfUI().getBillListPanel().getHeadTable().getSelectedRow();
		//被对照系统主键
		String headpk = getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitCVO.PK_DATADEFINIT_B)==null?"":getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitCVO.PK_DATADEFINIT_B).toString();

		SuperVO[] vos = HYPubBO_Client.queryByCondition(DipDatadefinitCVO.class, "pk_datadefinit_b='" + headpk + "' and isnull(dr,0)=0  ");

		getSelfUI().getBillListPanel().getBodyBillModel().setBodyDataVO(vos);
		getSelfUI().getBillListPanel().getBodyBillModel().execLoadFormula();
		getSelfUI().onTreeSelectSetButtonState(getSelfUI().getBillTreeSelectNode());

	}

	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());

	@Override
	protected void onBoDelete() throws Exception {		
//		IUAPQueryBS queryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//
//		Integer flags=0;//删除标志：1代表点击了确定按钮
//		String pk_node = ((DataDefcheckClientUI) getBillUI()).selectnode;
//		if("".equals(pk_node)){
//			NCOptionPane.showMessageDialog(this.getBillUI(),"请选择要删除的节点。");
//			return ;
//		}
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
		DipDatadefinitHVO hvo =(DipDatadefinitHVO) tempNode.getData();
		
		if(null!= hvo && !hvo.getMemorytype().equals("表")){//
			getSelfUI().showWarningMessage("只能修改表节点"); 
			return;
		}
		
		if(getSelfUI().getBillListPanel().getHeadTable().getSelectedRows().length!=1 ){
			getSelfUI().showWarningMessage("请选择一条主表数据"); 
			return;
		}

		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		if(rowcount<1){
			getSelfUI().showWarningMessage("子表没有数据，删除失败"); 
			return;
		}

		int headcount = getSelfUI().getBillListPanel().getHeadTable().getSelectedRow();
		String headpk = getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitCVO.PK_DATADEFINIT_B)==null?"":getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitCVO.PK_DATADEFINIT_B).toString();
		
		int i =getSelfUI().showOkCancelMessage("将删除子表中的所有数据，是否继续？");
		if(i==1){//确定
		}else{
			return;	
		}
		String sql=" DELETE FROM dip_datadefinit_c where pk_datadefinit_b = '"+headpk+"' ";
		iq.exesql(sql);
		//更新树
		onCheckQuery();
		getSelfUI().showWarningMessage("删除成功");

		
	}
	private void delete() throws Exception{
		return ;
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

	
	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		// 获取权VO
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要查询的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("只能查询表节点"); 
			return;
		}
		DipDatadefinitHVO vo =(DipDatadefinitHVO) tempNode.getData();
		if(null!= vo && !vo.getMemorytype().equals("表")){//
			getSelfUI().showWarningMessage("只能查询表节点"); 
			return;
		}
		String pk_datadefinit_h = (String) vo.getAttributeValue("pk_datadefinit_h");

		SuperVO[] vos = HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='" + pk_datadefinit_h + "' and isnull(is_check,'N')='Y'  ");

//		nc.vo.dip.datadefcheck.MyBillVO billvo = new nc.vo.dip.datadefcheck.MyBillVO();
//		
//		DipDatadefinitHVO hvo= (DipDatadefinitHVO)vo;

		
		getSelfUI().getBillListPanel().getHeadBillModel().setBodyDataVO(vos);
		getSelfUI().getBillListPanel().getBodyBillModel().setBodyDataVO(null);		
	}
	/**
	 * 2011-4-18 cl
	 */
	@Override
	protected void onBoEdit() throws Exception {
		//修改时，将"创建表"按钮设置为不可用
//		getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);

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
		DipDatadefinitHVO hvo =(DipDatadefinitHVO) tempNode.getData();
		
		if(null!= hvo && !hvo.getMemorytype().equals("表")){//
			getSelfUI().showWarningMessage("只能修改表节点"); 
			return;
		}
		
		if(getSelfUI().getBillListPanel().getHeadTable().getSelectedRows().length!=1 ){
			getSelfUI().showWarningMessage("请选择一条主表数据"); 
			return;
		}

//		super.onBoEdit();	
		selectrow=getSelfUI().getBillListPanel().getHeadTable().getSelectedRow();

		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
//		getSelfUI().getBillListPanel().getHeadTable().changeSelection(0, 0, false, false);
		getSelfUI().getBillListPanel().getHeadTable().setEnabled(false);
		getSelfUI().isEditBtn(true);
		getSelfUI().setTreeEnable(false);

//		int row=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		if(getSelfUI().getBillListPanel().getBodyBillModel()!=null){
			//for(int i=row;i<201;i++){
				onBoLineAdd();
			//}
		}
		//编辑前
		getSelfUI().getBillListPanel().getBodyScrollPane("dip_datadefinit_c").addEditListener2(new BillEditListener2() {
			
			public boolean beforeEdit(BillEditEvent e) {
				if("def_quote_field".equals(e.getKey())){			
					int row=e.getRow();
					String pk_datadefinit_h=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row, "quote_table")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"quote_table").toString();
					if("".equals(pk_datadefinit_h)||pk_datadefinit_h.length()==0){
						getSelfUI().showWarningMessage("请先选择引用表");
						return false;
					}
					
					UIRefPane pane3=(UIRefPane) getSelfUI().getBillListPanel().getBodyItem("def_quote_field").getComponent();
					DataDefFieldbRefModel model3=new DataDefFieldbRefModel();
					model3.addWherePart(" and dip_datadefinit_b.pk_datadefinit_h = '"+pk_datadefinit_h+"' and nvl(dip_datadefinit_b.dr ,0)=0 " );
					pane3.setRefModel(model3);	
				}	
				return true;
			}
		});
		//编辑后	
		getSelfUI().getBillListPanel().getBodyScrollPane("dip_datadefinit_c").addEditListener(new BillEditListener() {
			
			public void bodyRowChange(BillEditEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterEdit(BillEditEvent e) {
				
				if("def_quote_table".equals(e.getKey())){			
					int row=e.getRow();
					String pk_datadefinit_h=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row, "def_quote_table")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"def_quote_table").toString();
					
					if(!"".equals(pk_datadefinit_h)){
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt(2, row, "dtype");
					}else{
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt(2, row, "dtype");
					}
				}
				if("input_value".equals(e.getKey())){
					int row=e.getRow();
					String dtype=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"dtype")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"dtype").toString();
					String input_value=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row, "input_value")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"input_value").toString();
					if(input_value==null||input_value.trim().length()<=0){
						return;
					}
					if(input_value!=null&& null!=dtype && dtype.equalsIgnoreCase("长度校验")){
						String regex = "^[0-9]*[1-9][0-9]*$";
						if(!input_value.matches(regex)){//不是正整数时
							getSelfUI().showWarningMessage("只能输入整数");
							getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "input_value");
						}
					}
				}
				
				if(e.getKey().equals(DipDatadefinitCVO.DTYPE)){
					int row=e.getRow();
					Object isdo= getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row, DipDatadefinitCVO.DTYPE);
		            String istype = isdo ==null?"":isdo.toString();
		           if(istype.equals("长度校验")||istype.equals("转换校验")||istype.equals("数值比较")){
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "input_value", true);
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "def1", true);
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "ref", false);
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref");
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref_pk");
		           }else if(istype.equals("引用校验") || istype.equals("必输校验") || istype.equals("数据权限校验")){
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "input_value", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "def1", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "ref", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "input_value");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "def1");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref_pk");
					}else if(istype.equals("引用正则校验") || istype.equals("引用关键字")){
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "input_value", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "def1", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "ref", true);
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "input_value");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "def1");
					}else if(istype.equals("手工正则校验") || istype.equals("手工关键字")){
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "input_value", true);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "def1", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "ref", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "def1");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref_pk");
					}
				}
				int k=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
				int m=getSelfUI().getBillListPanel().getBodyTable().getSelectedRow();
				if(k-1==m){
					getSelfUI().getBillListWrapper().addLine();
				}
			}
		});
		
		
		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		getSelfUI().getBillListPanel().getBodyBillModel().setEnabledAllItems(true);
		
		for(int i=0;i<rowcount;i++){
			getSelfUI().getBillListPanel().getBodyBillModel().setEditRow(i);

			Object isdo= getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(i, DipDatadefinitCVO.DTYPE);
            String istype = isdo ==null?"":isdo.toString();
            if(istype.equals("长度校验") || istype.equals("手工正则校验") ||istype.equals("手工关键字")||istype.equals("引用正则校验") || istype.equals("引用关键字")){
            	getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "is_content", true);
            }else{
            	getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "is_content", false);
            }
            
            if(istype.equals("长度校验") ||istype.equals("手工正则校验") ||istype.equals("手工关键字")){
            	getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "input_value", true);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_value", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_table", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_field", false);


			}else if(istype.equals("引用校验") || istype.equals("必输校验") || istype.equals("数据权限校验")){
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "input_value", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def1", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "ref", false);
				
			}else if(istype.equals("引用正则校验") || istype.equals("引用关键字")){
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "input_value", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_value", true);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_table", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_field", false);

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
//						getBillTreeManageUI().updateUI();
//						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("syscode", tempc);
//						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sysname", tempn);
//					}
//				}
//			}
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
				getBillTreeManageUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeManageUI().getBillTreeSelectNode());
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
				
//				if (getUITreeCardController().isAutoManageTree()) {	
////					MyBillVO mvo=new MyBillVO();
////					mvo.setParentVO(newhvo);
//					getSelfUI().insertNodeToTree(newhvo);
//				}
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
//		dataNotNullValidate();
		int headcount = getSelfUI().getBillListPanel().getHeadTable().getSelectedRow();
		if(headcount==-1){
			getSelfUI().showWarningMessage("请选择一条主表数据"); 
			return;
		}
		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
        if(rowcount>0){
        	List list=new ArrayList();
        	BillModel bodyBillModel = getSelfUI().getBillListPanel().getBodyBillModel();
        	BillItem[] bodyItems = bodyBillModel.getBodyItems();
        	for(int i=0 ; i< rowcount;i ++ ){
				Boolean isNull = true;
        		for (BillItem billItem : bodyItems) {
					if(billItem.isShow()){
						Object valueAt = bodyBillModel.getValueAt(i, billItem.getKey());
						if(null != valueAt && !"".equals(valueAt)){
							isNull = false;
							break;
						}
					}
				}
        		if(isNull){
        			list.add(i);
        		}
        	}
        	if(list.size()!=0){
    			int []delrow=new int [list.size()];
    			for(int i=0;i<list.size();i++){
    				delrow[i]=(Integer)list.get(i);
    			}
    			bodyBillModel.delLine(delrow);
    		}
        }
		String bool =  valuedate();
		if(null !=bool && bool.length()>0){
			getSelfUI().showWarningMessage(bool); 
			return;
		}
		String headpk = getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitBVO.PK_DATADEFINIT_B)==null?"":getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitBVO.PK_DATADEFINIT_B).toString();
		//先删除子表数据
		String sql=" DELETE FROM dip_datadefinit_c where pk_datadefinit_b = '"+headpk+"' ";
		iq.exesql(sql);
		
		rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
        if(rowcount>0){
        	List<DipDatadefinitCVO> bvos = new ArrayList<DipDatadefinitCVO>();
        	for(int i=0 ; i< rowcount;i ++ ){
        		DipDatadefinitCVO bodyValueRowVO = (DipDatadefinitCVO)getSelfUI().getBillListPanel().getBodyBillModel().getBodyValueRowVO(i, DipDatadefinitCVO.class.getName());
        		bodyValueRowVO.setPk_datadefinit_b(headpk);
        		bodyValueRowVO.setPk_datadefinit_c(null);
        		bvos.add(bodyValueRowVO);
        	}
        	HYPubBO_Client.insertAry(bvos.toArray(new DipDatadefinitCVO[0]));
        }	
		// 设置保存后状态
        setSaveOperateState();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		getSelfUI().getBillListPanel().getHeadTable().setEnabled(true);
        getSelfUI().showWarningMessage("保存成功");
	}
	
	private String valuedate() {
	    StringBuffer msg = new StringBuffer();
		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
        if(rowcount>0){
        	Map<String, String> codemap = new HashMap<String, String>();
        	Map<String, String> namemap = new HashMap<String, String>();

        	for(int i=0 ; i< rowcount;i ++ ){
        		int row = i+1;
        		DipDatadefinitCVO bodyValueRowVO = (DipDatadefinitCVO)getSelfUI().getBillListPanel().getBodyBillModel().getBodyValueRowVO(i, DipDatadefinitCVO.class.getName());
        		if(bodyValueRowVO.getDcode()==null || "".equals(bodyValueRowVO.getDcode()) ||
        				bodyValueRowVO.getDname()==null || "".equals(bodyValueRowVO.getDname())||
        				bodyValueRowVO.getDtype()==null || "".equals(bodyValueRowVO.getDtype())){	
        			msg.append("第"+row+"行 ,【校验编码、校验名称、校验类型】 不能为空!\n");
        		}else{
        			if(null!=codemap && null!=codemap.get(bodyValueRowVO.getDcode())){
            			msg.append("第"+row+"行, 【校验编码】 出现重复! \n");
        			}else{
        				codemap.put(bodyValueRowVO.getDcode(), "code");
        			}
        			if(null!=namemap && null!=namemap.get(bodyValueRowVO.getDname())){
            			msg.append("第"+row+"行, 【校验名称】 出现重复! \n");
        			}else{
        				namemap.put(bodyValueRowVO.getDname(), "code");
        			}
        		}
        		if(null!=bodyValueRowVO.getDtype() &&(bodyValueRowVO.getDtype()==1||bodyValueRowVO.getDtype()==3||bodyValueRowVO.getDtype()==5)){
        			if(bodyValueRowVO.getInput_value()==null || "".equals(bodyValueRowVO.getInput_value())){
            			msg.append("第"+row+"行, 【手工输入】 不能为空! \n");
        			}
        		}else if(null!=bodyValueRowVO.getDtype() &&(bodyValueRowVO.getDtype()==4||bodyValueRowVO.getDtype()==6)){
        			if(bodyValueRowVO.getRef_pk()==null || "".equals(bodyValueRowVO.getRef_pk())){
            			msg.append("第"+row+"行, 【引用参照】 不能为空! \n");
        			}
        		}
        		if(null!=bodyValueRowVO.getDtype() &&(bodyValueRowVO.getDtype()==1||bodyValueRowVO.getDtype()==7)){
        			if(bodyValueRowVO.getDef1()==null || "".equals(bodyValueRowVO.getDef1())){
            			msg.append("第"+row+"行, 【操作符】 不能为空! \n");
        			}
        		}
        	}
        }	
        if(null!=msg && msg.toString().length()>0){
        	return "保存失败，校验不通过！\n"+msg.toString();
        }
		
		return null;
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
	public String checkTableHead(DataDefcheckClientUI ui,String pk){
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
//			DipDatadefinitHVO tnhvo=(DipDatadefinitHVO) arg0.getData();
			
//			String tab=tnhvo.getMemorytable()==null?"":tnhvo.getMemorytable().toString();
//			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//			if(docu!=null){
//				jcp.getUITextField().setDocument(docu);
//				jcp.setText(tab);	
//			}else{
//				docu=jcp.getUITextField().getDocument();
//			}
			
			String pk_datadefinit_h = (String) vo.getAttributeValue("pk_datadefinit_h");

			SuperVO[] vos = HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='" + pk_datadefinit_h + "' and isnull(dr,0)=0 and isnull(is_check,'N')='Y' ");

			nc.vo.dip.datadefcheck.MyBillVO billvo = new nc.vo.dip.datadefcheck.MyBillVO();
			
			DipDatadefinitHVO hvo= (DipDatadefinitHVO)vo;
//			// 设置主、子VO
//			billvo.setParentVO(hvo);
//			billvo.setChildrenVO(vos);
//			// 显示数据
//			getBufferData().addVOToBuffer(billvo);
			
			getSelfUI().getBillListPanel().getHeadBillModel().setBodyDataVO(vos);
			getSelfUI().getBillListPanel().getBodyBillModel().setBodyDataVO(null);

			/*
			 * 描述：”是否分布式数据“打对勾如否的变化：
			 * 如果系统注册中的”分布系统“修改了，由未打上对勾修改为打上对勾了，则该系统下的子节点的”是否分布式数据“也应该由未打上对勾变为打上对勾
			 * @date 2011-5-9 13:42
			 * begin
			 */
//			VOTreeNode treeNode=getSelectNode();
//			String pid=null;
//			DipSysregisterHVO syshvo;
//			if(treeNode !=null){
//				pid=(String) treeNode.getParentnodeID();
//			}
//			if(treeNode!=null&&"".equals(treeNode.getParentnodeID())){
////				DipSysregisterHVO syshvo;
//				try{
//					syshvo=(DipSysregisterHVO)HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, treeNode.getNodeID().toString());
//					extcode=syshvo.getExtcode();
//				}catch(UifException e){
//					e.printStackTrace();
//				}
//			}else if(treeNode!=null&&treeNode.getParentnodeID()!=null&&treeNode.getParentnodeID().toString().length()>0){
//				try{
//					syshvo=(DipSysregisterHVO)HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, tnhvo.getPk_xt()/*treeNode.getParentnodeID().toString()*/);
//					extcode=syshvo.getExtcode();
//				}catch(UifException e){
//					e.printStackTrace();
//				}
//			}
//
//
//			String sql="select isdeploy from dip_sysregister_h where pk_sysregister_h='"+pid+"' and nvl(dr,0)=0";
//			try{
//				//是否分布式数据不可编辑
//				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
//				String isdeploy=iq.queryfield(sql);	
//				//如果是分布式数据
//				if(isdeploy.equals("Y")){
//					//分布式标识为可编辑	
//					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
//				}else{
//					//分布式标识为不可编辑	
//					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//			}




			//2011-4-26 程莉 动态自定义参照：查询当前系统下的分布式标识			
////			String pk_sysregister_h = hvo.getPk_sysregister_h();
//			UIRefPane pane=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").getComponent();
//			SysRegisterRefModel model = new SysRegisterRefModel();
//			model.addWherePart(" and dip_sysregister_b.pk_sysregister_h='"+fpk+"' and nvl(dip_sysregister_b.dr,0)=0  and (dip_sysregister_b.isstop='N' or dip_sysregister_b.isstop='') ");
//			pane.setRefModel(model);
//
//
//
//			UIRefPane pane2=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
//			QuoteTableTreeRefModel model2=new QuoteTableTreeRefModel();
//			model2.addWherePart(" and pk_datadefinit_h1 <>'"+pk_datadefinit_h+"'");
//			pane2.setRefModel(model2);

//			getBillTreeManageUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));
			//显示主子表的
			


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
//	public void onBoMBCopy(AskMBDLG ask) throws Exception{
//		VOTreeNode tempNode = getSelectNode();
//		if(tempNode==null){
//			getSelfUI().showErrorMessage("请选择要复制的节点！");
//			return ;
//		}
//		String str=(String)tempNode.getParentnodeID();
//		if(str ==null || str.equals("")){
//			getSelfUI().showWarningMessage("系统节点不能做复制操作！"); 
//			return;
//		}else{
//			DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();
//			
//			if(defhvo!=null&&defhvo.getIsfolder().booleanValue()){
////				UIRefPane ref=new UIRefPane();
////				ContDataBussinessTableRefModel mo=new ContDataBussinessTableRefModel();
////				ref.setRefModel(mo);
////				ref.connEtoC1();
//				String selectPk=ask.getOrgnizeRefPnl().getRefValue("pk_datadefinit_h")==null?"":ask.getOrgnizeRefPnl().getRefValue("pk_datadefinit_h").toString();
//				if(selectPk!=null&&selectPk.trim().length()==20){
//						
//							DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, selectPk);
//							SuperVO[] bvos=HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+selectPk+"' and nvl(dr,0)=0 ");
//							if(bvos!=null&&bvos.length>0){
//								for(int i=0;i<bvos.length;i++){
//								  bvos[i].setPrimaryKey(null);	
//								}	
//							}
//							onBoAdd(null);
//							getSelfUI().getBillCardPanel().getBillModel("dip_datadefinit_b").setBodyDataVO(bvos);
//								UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
////								docu=jcp.getUITextField().getDocument();
//								if(hvo.getMemorytype().equals("表")){
//									if(defhvo.getPk_xt()!=null&&!defhvo.getPk_xt().equals("0001AA1000000001XQ1B")){
//										jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
//										jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");	
//									}
//								}
//								UIRefPane pane=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
//								//如果只参照当前系统下已经建表的,加上下面条件，如果是全部系统下定义的表，把下面的条件注释即可
//								//2011-6-29 由于要参照所有系统下的已经定义的表,以及原始的参照类条件没有加“是否系统预置主键=Y”，所以把参照类给换了，上面限制本系统下的条件给注释了
//								QuoteTableTreeRefModel model=new QuoteTableTreeRefModel();
//								model.addWherePart(" and pk_datadefinit_h1 <> '"+selectPk+"'");
//								pane.setRefModel(model);
//								if(hvo.getIsdeploy()==null||hvo.getIsdeploy().equals("Y")){//不是分布系统
//									getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(true);
//									getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
//								}else{
//									getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
//									getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
//								}
//					this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("iscreatetab").setValue(new UFBoolean(false));
//				}
//			}else{
//				getSelfUI().showErrorMessage("叶节点不能复制！");
//				return;
//			}
//		}
//		
//		
//	
//	}
	
	@Override
	protected void onBoCancel() throws Exception {
		//取消，将"创建表"按钮设置为可用
//		if(docu!=null){
//			getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
//			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//			jcp.getUITextField().setDocument(docu);
//			//jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");
//		}
		super.onBoCancel();
		getSelfUI().delstr ="";//ytq 2011-07-02 取消清空删行缓存
//		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
//		getSelfUI().getBillListPanel().getHeadTable().changeSelection(selectrow, 0, false, false);
		getSelfUI().isEditBtn(false);
		getSelfUI().setTreeEnable(true);
		onCheckQuery();
		getSelfUI().getBillListPanel().getHeadTable().setEnabled(true);

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
//		super.onBoLinePaste();
//		int currow=getSelfUI().getBillListPanel().getBodyTable().getSelectedRow();
//		DipDatadefinitCVO bodyValueRowVO = (DipDatadefinitCVO)getSelfUI().getBillListPanel().getBodyBillModel().getBodyValueRowVO(currow, DipDatadefinitCVO.class.getName());
//		int count = getSelfUI().getBillListPanel().getBodyTable().getRowCount();
//		getSelfUI().getBillListPanel().getBodyBillModel().addLine();
//		//复制粘贴行时子表主键置为空：避免保存时报违反唯一性错误
//		getSelfUI().getBillListPanel().getBodyBillModel().setBodyRowVO(bodyValueRowVO, count);
//		getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", count, "pk_datadefinit_c");
	}
	
	@Override
	protected void onBoLineCopy() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoLineCopy();
//		int selectedRow = getSelfUI() getBilllist.getBillTable().getSelectedRow();
//		if (selectedRow != -1) {
//			CircularlyAccessibleValueObject[] vos = getSelectedBodyVOs();
//			// getBillCardPanel().getBillData().getBillModel().getBodySelectedVOs(
//			// m_BodyVOClass.getName());
//			setCopyedBodyVOs(vos);
//
//		}
	}
	
	@Override
	protected void onBoLineIns() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoLineIns();

	}
	
	@Override
	protected void onBoLinePasteToTail() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoLinePasteToTail();
	}

	@Override
	protected void onBoLineDel() throws Exception {

		int countrow=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		if(countrow<0){
			return;
		}
 
		int rownos[]=getSelfUI().getBillListPanel().getBodyTable().getSelectedRows();
		if(rownos!=null&&rownos.length>0){
			int rowno=-1;
			for(int i=0;i<rownos.length;i++){
				rowno=rownos[i];	
				if(rowno>=0){
//					getSelfUI().showErrorMessage("请选择要操作的行！");
//					return;
//					String ob=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(rowno, "pk_datadefinit_c")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(rowno, "pk_datadefinit_c").toString();
//					if(ob==null||"".equalsIgnoreCase("pk_datadefinit_c")){
//						super.onBoLineDel();
//						getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";
//					}else{
////						super.onBoLineDel();
////						
//						getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";			
//						String sb=ob.toString();
//						System.out.println("删除主键是"+sb+"的vo");
						if(i==rownos.length-1){
							getSelfUI().getBillListPanel().getBodyBillModel().delLine(rownos);	
//						}
					}
				}
				
			}
			
		}
		
	}	
	@Override
	protected void onBoLineAdd() throws Exception {
		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		getSelfUI().getBillListPanel().getBodyBillModel().addLine();	
		getSelfUI().getBillListPanel().getBodyBillModel().setEditRow(rowcount-1);
		
	}
	
	
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoRefresh();
		onTreeSelected(getSelfUI().getBillTreeSelectNode());
		getSelfUI().showHintMessage("刷新成功");
	}
	
	public void onBoMBZH()throws Exception{
//		 VOTreeNode tempNode = getSelectNode();
//		 if(tempNode==null){
//			 getSelfUI().showErrorMessage("选择节点数据错误！");
//			 return;
//		 }
//		 DipDatadefinitHVO hvo=(DipDatadefinitHVO) tempNode.getData();
//		 AskMBDLG ask=null;
//		 if(hvo!=null&&hvo.getPk_xt().equals("0001AA1000000001XQ1B")){
//			 ask=new AskMBDLG(getSelfUI(),null,"模板","        请选择操作类型?",new String[]{"导入外部文件数据定义格式","导出外部文件数据定义格式","根据NC数据字典自动生成数据定义格式","根据数据库物理表生成数据定义格式","根据数据定义复制格式"});
//			 ask.showModal();
//			 int result=ask.getRes();
//				if(result==0){
//					onBoImport();
//				}else if(result==1){
//					onBoExport();
//				}else if(result==2){
//					onBoAutoBuiltDatadefinit();
//				}else if(result==3){
//					onBoPhysicTable();
//				}else if(result==4){
//					onBoMBCopy(ask);
//				}
//		 }else{
//			  ask=new AskMBDLG(getSelfUI(),null,"模板","        请选择操作类型?",new String[]{"导入外部文件数据定义格式","导出外部文件数据定义格式","根据数据库物理表生成数据定义格式","根据数据定义复制格式"});
//			  ask.showModal();
//			  int result=ask.getRes();
//				if(result==0){
//					onBoImport();
//				}else if(result==1){
//					onBoExport();
//				}else if(result==2){
//					onBoPhysicTable();
//				}else if(result==3){
//					onBoMBCopy(ask);
//				}
//		 }
		 
		
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
									  
//									  if (getUITreeCardController().isAutoManageTree()) {			
//											getSelfUI().insertNodeToTree(hvo);
//										}
										
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