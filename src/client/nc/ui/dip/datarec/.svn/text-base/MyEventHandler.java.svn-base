package nc.ui.dip.datarec;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;

import nc.bs.dip.txt.TxtDataVO;
import nc.bs.excel.pub.ExpToExcel1;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.bd.ref.DataDefinitTableTreeRefModel;
import nc.ui.bd.ref.GSWJRefModel;
import nc.ui.bd.ref.ReturnBMsgRefModel;
import nc.ui.bd.ref.SourceRegistRefModel;
import nc.ui.bd.ref.ZDJSRefModel;
import nc.ui.bd.ref.ZDZQRefModel;
import nc.ui.bd.ref.ZJBRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.datarec.formatedlg.DataformatDLG;
import nc.ui.dip.datarec.formatedlg.DataformatPanel;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.esbfilesend.dlg.AskMBDLG;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
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
import nc.vo.dip.consttab.DipConsttabVO;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.dataorigin.DipDataoriginVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.datarec.DipDatarecSpecialTab;
import nc.vo.dip.datarec.MyBillVO;
import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.messagelogo.MessagelogoVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
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

public class MyEventHandler extends AbstractMyEventHandler{
	public static String extcode;
	public MyEventHandler(BillTreeCardUI clientUI, ICardController control){
		super(clientUI,control);		
	}

	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private DataRecUI getSelfUI() {
		return (DataRecUI) getBillUI();
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
		newBodyVO.setAttributeValue("pk_datarec_h", parent == null ? null : parent.getNodeID());
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

	@Override
	protected void onBoDelete() throws Exception {
		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要删除的节点!");
			return ;
		}
		DipDatarecHVO hvo=(DipDatarecHVO) tempNode.getData();
		if(hvo.getIsfolder().booleanValue()||hvo.getFpk()==null||hvo.getFpk().length()<=0){
			getSelfUI().showErrorMessage("请选择文件进行操作！");
			return ;
		}
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();

		String pk_node = hvo.getPrimaryKey();
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确认要删除?");
		if(flag == 1){

			DipDatarecHVO vo = (DipDatarecHVO) HYPubBO_Client.queryByPrimaryKey(DipDatarecHVO.class, pk_node);
			if(vo==null){
				getSelfUI().showWarningMessage("系统节点不能做删除操作！");
				return;
			}
			if(vo!=null)
				HYPubBO_Client.delete(vo);
			HYPubBO_Client.deleteByWhereClause(DipDatasynchVO.class, "dataname='"+pk_node+"'");
//			更新树
			this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());	   
			if(node1!=null){
				getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
									.getPath()));
				}
		}



	}

	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	
	/**
	 * 功能：
	 * 改动：ytq 2011-07-02 改动，增加显示是否分布式
	 * */
	@Override
	public void onTreeSelected(VOTreeNode arg0) {
		try {		
			// 获取权VO
			SuperVO vo = (SuperVO) arg0.getData();
			UFBoolean Isdeploy = new UFBoolean(false);//ytq 2011-07-02 用于显示是否分布式数据
			String pk_datarec_h = (String) vo.getAttributeValue("pk_datarec_h");//主键
			DipDatarecHVO hvo = (DipDatarecHVO) HYPubBO_Client.queryByPrimaryKey(DipDatarecHVO.class, pk_datarec_h); 
			if(hvo==null){
				hvo=(DipDatarecHVO) vo;
			}
			nc.vo.dip.datarec.MyBillVO billvo = new nc.vo.dip.datarec.MyBillVO();
			VOTreeNode treeNode=getSelectNode();
			String pid=null;
			DipSysregisterHVO syshvo = null;
			if(treeNode !=null){
				pid=(String) treeNode.getParentnodeID();
			}if(treeNode!=null&&((null==treeNode.getParentnodeID())||("".equals(treeNode.getParentnodeID())))){
				try{
					syshvo=(DipSysregisterHVO)HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, hvo.getPk_xt());
					extcode=syshvo.getExtcode();
				}catch(UifException e){
					e.printStackTrace();
				}
			};			
			

			/*
			 * 描述：”是否分布式数据“打对勾如否的变化：
			 * 如果系统注册中的”分布系统“修改了，由未打上对勾修改为打上对勾了，则该系统下的子节点的”是否分布式数据“也应该由未打上对勾变为打上对勾
			 * @date 2011-5-9 13:42
			 * begin
			 */

			if(treeNode !=null){
				pid=(String) treeNode.getParentnodeID();
				if(null==pid || "".equals(pid)){
					pid = (String) treeNode.getNodeID();
				}
			}


			/*2011-6-4 begin*/
			if(hvo !=null){
				if(hvo.getPk_datarec_h() !=null && hvo.getFpk()!=null&&hvo.getPk_xt()!=null&&(hvo.getIsfolder()==null||!hvo.getIsfolder().booleanValue())){
//					String s="select sourcetype from dip_datarec_h where pk_datarec_h='"+hvo.getPk_datarec_h()+"' and nvl(dr,0)=0";			
					try {
						String sourcetype;
							sourcetype = hvo.getSourcetype();//数据来源类型
//							String st="select name from dip_dataorigin where pk_dataorigin='"+sourcetype+"' and nvl(dr,0)=0";
//							String name=iq.queryfield(st);//数据来源连接
							//liyunzhe modify 把数据来源连接name比较改成主键pk比较。 start 2012-06-05
							String name=sourcetype;
//							String str="select sourcecon from dip_datarec_h where pk_datarec_h='"+hvo.getPrimaryKey()+"' and sourcetype='"+sourcetype.toString()+"' and nvl(dr,0)=0";
							String sourcecon=hvo.getSourcecon();
//							sourcecon=iq.queryfield(str);
							UIRefPane pane=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourcecon").getComponent();//数据来源连接
							if(name.equals(IContrastUtil.DATAORIGIN_ZDZQ)){
								ZDZQRefModel model3=new ZDZQRefModel();
								model3.addWherePart(" and nvl(dr,0)=0 ");
								pane.setPK(sourcecon);
								pane.setRefModel(model3);
							}else if(name.equals(IContrastUtil.DATAORIGIN_XXZX)){
								ZDJSRefModel model4=new ZDJSRefModel();
								model4.addWherePart(" and nvl(dip_msr.dr,0)=0");
								pane.setPK(sourcecon);
								pane.setRefModel(model4);
							}else if(name.equals(IContrastUtil.DATAORIGIN_GSWJ)){
								GSWJRefModel model5=new GSWJRefModel();
								model5.addWherePart(" and nvl(dr,0)=0");
								pane.setPK(sourcecon);
								pane.setRefModel(model5);
								
							}else if(name.equals(IContrastUtil.DATAORIGIN_ZJB)){
								ZJBRefModel model6=new ZJBRefModel();
								model6.addWherePart(" and nvl(dr,0)=0");
								pane.setPK(sourcecon);
								pane.setRefModel(model6);
							}else if(name.equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)){
								SourceRegistRefModel model6=new SourceRegistRefModel();
								pane.setPK(sourcecon);
								pane.setRefModel(model6);
							}
							if(name.equals(IContrastUtil.DATAORIGIN_XXZX)){
								getSelfUI().getBillCardPanel().getHeadItem("trancon").setEdit(true);//.setEnabled(true);
								getSelfUI().getBillCardPanel().getHeadItem("backcon").setEdit(true);
								getSelfUI().getBillCardPanel().getHeadItem("databakfile").setEdit(true);//数据流类型
							}else{
								getSelfUI().getBillCardPanel().getHeadItem("trancon").setEdit(false);
								getSelfUI().getBillCardPanel().getHeadItem("backcon").setEdit(false);
								getSelfUI().getBillCardPanel().getHeadItem("databakfile").setEdit(false);//数据流类型
							}
							hvo.setSourcecon(sourcecon);
							getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourcecon").setValue(sourcecon);

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
//			liyunzhe modify 把数据来源连接name比较改成主键pk比较。 end 2012-06-05
			/*2011-6-4 end*/

			//设置主VO,ytq 增加如下几行，用于显示是否分布式
			if (null!=syshvo){
				if(null==hvo){
					hvo = new DipDatarecHVO();
				}
				hvo.setIsdeploy(Isdeploy);
				hvo.setSyscode(syshvo.getCode());
				hvo.setRecname(syshvo.getExtname());
			}

			billvo.setParentVO(hvo);


			// 显示数据
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));
			/*
			 * 修改业务表名的限制条件，将原来的参照数据定义主表的参照类的限制条件去掉
			 * 2011-06-09
			 * zlc*/
			if(hvo!=null&&hvo.getFpk()!=null&&(hvo.getIsfolder()==null||!hvo.getIsfolder().booleanValue())){
				/*2012-06-04 liyunzhe modify 修改物理表为树形结构 start*/
				UIRefPane uir=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//				SysDefinitRefModel model2=new SysDefinitRefModel();
//				model2.addWherePart(" and pk_xt='"+hvo.getPk_xt()+"'");
//				uir.setRefModel(model2);
				DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
				model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
				model.addWherePart(" and tabsoucetype='自定义'");
				uir.setBlurValue("last");
				uir.setRefModel(model);
				/*2012-06-04 liyunzhe modify 修改物理表为树形结构 end*/
				UIRefPane uir2=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("backerrinfo").getComponent();
				ReturnBMsgRefModel m2=new ReturnBMsgRefModel();
				if(hvo.getBackcon()!=null&&hvo.getBackcon().equals("Y")&&hvo.getBackerrtab()!=null&&hvo.getBackerrtab().length()==20){
					m2.addWherePart(" and nvl(dr,0)=0 and pk_returnmess_h='"+hvo.getBackerrtab()+"'");
				}else{
					m2.addWherePart(" and 1=2");
					
				}
				uir2.setRefModel(m2);
			}

		} catch (ComponentException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要增加的文件夹！");
			return ;
		}
		DipDatarecHVO hvo=(DipDatarecHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()||hvo.getFpk()==null||hvo.getFpk().length()<=0){
			getSelfUI().showErrorMessage("请选择文件夹进行操作");
			return ;
		}
		DipSysregisterHVO vo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, hvo.getPk_xt());
		if(SJUtil.isNull(vo)){
			getSelfUI().showErrorMessage("请选择系统节点！");
			return;
		}
		super.onBoAdd(bo);
		getSelfUI().getBillCardPanel().setHeadItem("fpk", hvo.getPrimaryKey());
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", hvo.getPk_xt());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		if(vo.getIsdeploy()==null||!vo.getIsdeploy().equals("Y")){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
		}


//		String pk_sysregister_h =hvo.getPk_xt();

//		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytable").getComponent();
//		
////		SysDefinitRefModel model2=(SysDefinitRefModel) uir.getRefModel();
////		model2.addWherePart(" and pk_sysregister_h='"+pk_sysregister_h+"'");
////		uir.setRefModel(model2);
//		DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
//		model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+pk_sysregister_h+"'");
//		model.addWherePart(" and tabsoucetype='自定义'");
//		uir.setRefModel(model);
		controHZstats(false);
		getSelfUI().getBillCardPanel().getHeadItem("databakfile").setEdit(false);//数据流类型
		getSelfUI().getBillCardPanel().getHeadItem("sourceparam").setEdit(false);//参数
	}

	private void controHZstats(boolean con){

		//回执错误码表
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("backerrtab").setEnabled(con);
		//回执错误码表信息
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("backerrinfo").setEnabled(con);
		//回执消息服务器
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_str_1").setEnabled(con);
	}

	private boolean isnew=false;
	@Override
	protected void onBoEdit() throws Exception {
		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		DipDatarecHVO hvo=(DipDatarecHVO) tempNode.getData();
		if(hvo.getIsfolder().booleanValue()||hvo.getFpk()==null||hvo.getFpk().length()<=0){
			getSelfUI().showErrorMessage("请选择文件进行操作");
			return ;
		}
		super.onBoEdit();
//		UIRefPane uir=(UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytable").getComponent();
////		SysDefinitRefModel model2=(SysDefinitRefModel) uir.getRefModel();
////		model2.addWherePart(" and pk_sysregister_h='"+hvo.getPk_xt()+"'");
//		DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
//		model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
//		model.addWherePart(" and tabsoucetype='自定义'");
//		uir.setBlurValue("last");
//		uir.setRefModel(model);
//		
//		Vector vec=new Vector();
//		vec.add(hvo.getMemorytable());
//		uir.getRefModel().setSelectedData(vec);
//	    uir.getRefModel().setValue(1, "pk_datadefinit_h", hvo.getMemorytable());
		//回执
		Object ob=getSelfUI().getBillCardPanel().getHeadItem("backcon").getValueObject();
		if(SJUtil.isNull(ob)){
			controHZstats(false);
		}else{
			String bool=(String) ob;
			if("true".equals(bool)){
				controHZstats(true);
			}else{
				controHZstats(false);
			}
		}

		if(hvo.getIsdeploy()==null||!hvo.getIsdeploy().booleanValue()){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
		}
		setButEnable(false);
		

		Object sourcetype=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourcetype").getValueObject();
		UIRefPane pane=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourcecon").getComponent();//数据来源连接
		if(!SJUtil.isNull(sourcetype)){
			DipDataoriginVO vo=null;
			try {
//				vo = (DipDataoriginVO) HYPubBO_Client.queryByPrimaryKey(DipDataoriginVO.class, sourcetype.toString());
//				String name=vo.getName();
				String name=sourcetype.toString();
				String ioflag=(String)getBillCardPanelWrapper(). getBillCardPanel().getHeadItem("ioflag").getValueObject();
				String iotype=(String)getBillCardPanelWrapper(). getBillCardPanel().getHeadItem("databakfile").getValueObject();
				BillItem item=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sourceparam");
//				liyunzhe modify 把数据来源连接name比较改成主键pk比较。 start 2012-06-05
				if(name.equals(IContrastUtil.DATAORIGIN_XXZX)){
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("trancon").setEdit(true);//.setEnabled(true);
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("backcon").setEdit(true);
					if(ioflag!=null&&ioflag.equals("输出")&&iotype!=null&&iotype.equals("文件流")){
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("format").setEdit(true);

						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_datadefinit_h").setEdit(true);
					}else{
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("format").setEdit(false);
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_datadefinit_h").setEdit(false);
					}
					
					if(ioflag!=null&&ioflag.equals("输入")&&iotype!=null&&iotype.equals("文件流")){
						getSelfUI().setParameter(item,IContrastUtil.DATAORIGIN_XXZX,IContrastUtil.DATAORIGIN_XXZX_IN_FILE_PARAMETAR,false);
					}else{
						item.setValue("");
						item.setEdit(false);
					}
//					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ioflag").setEdit(false);
				}else{
				
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_datadefinit_h").setEdit(false);
					if(name.equals(IContrastUtil.DATAORIGIN_ZDZQ)){
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ioflag").setEdit(false);
						getSelfUI().setParameter(item, IContrastUtil.DATAORIGIN_ZDZQ,IContrastUtil.DATAORIGIN_ZDZQ_PARAMETAR,false);
					}else{
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ioflag").setEdit(true);
					}
					if(name.equals(IContrastUtil.DATAORIGIN_GSWJ)&&ioflag!=null&&ioflag.equals("输出")){
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("format").setEdit(true);
						getSelfUI().setParameter(item,IContrastUtil.DATAORIGIN_GSWJ,IContrastUtil.DATAORIGIN_GSWJ_PARAMETAR,false);
					}else{
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("format").setEdit(false);
					}
					
					if(name.equals(IContrastUtil.DATAORIGIN_ZJB)){
						getSelfUI().setParameter(item,IContrastUtil.DATAORIGIN_ZJB,IContrastUtil.DATAORIGIN_ZJB_PARAMETAR,false);
					}
					if(name.equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)){
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ioflag").setEdit(false);
						getSelfUI().setParameter(item,IContrastUtil.DATAORIGIN_WEBSERVICEZQ,IContrastUtil.DATAORIGIN_WEBSERVICEZQ_PARAMETAR,false);
					}
					
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("trancon").setValue(null);
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("trancon").setEdit(false);
					
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("backcon").setValue(null);
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("backcon").setEdit(false);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
//		liyunzhe modify 把数据来源连接name比较改成主键pk比较。 end 2012-06-05
		
	}
	public void setButEnable(Boolean flag){
		getButtonManager().getButton(IBtnDefine.DataCheck).setEnabled(flag);
   		getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(flag);
   	 	getButtonManager().getButton(IBtnDefine.JSDYDATAFORMAT).setEnabled(flag);
   	 	try {
			getSelfUI().updateButtonUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <P>此方法为覆盖父类方法
	 * <BR>将父类中的ISingleController判断去掉，否则保存时，表头为空
	 * @throws Exception
	 * @see nc.ui.trade.treecard.TreeCardEventHandler#onBoSave()
	 */

	@Override
	protected void onBoSave() throws Exception {
		//super.onBoSave();
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		DataRecUI ui=(DataRecUI)getBillUI();
		String pk=(String)ui.getBillCardPanel().getHeadItem("pk_datarec_h").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		String syscode=(String)ui.getBillCardPanel().getHeadItem("syscode").getValueObject();
		String fpk=(String)ui.getBillCardPanel().getHeadItem("pk_xt").getValueObject();
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		Collection ccode=bs.retrieveByClause(DipDatarecHVO.class, " pk_xt='"+fpk+"' and (isfolder is null or isfolder='N') and syscode='"+syscode+"' and nvl(dr,0)=0 and pk_datarec_h <>'"+pk+"'");
		Collection ccode=bs.retrieveByClause(DipDatarecHVO.class, " pk_xt='"+fpk+"' and syscode='"+syscode+"' and nvl(dr,0)=0 and pk_datarec_h <>'"+pk+"'");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage(IContrastUtil.getCodeRepeatHint("编码", syscode));
				return;
			}
		}
		/*
		 * 回执标志为true时，绘制错误码表和回执错误马表信息不可为空
		 * 2011-5-31
		 * zlc*/
		String flag=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("backcon").getValueObject()==null?null:this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("backcon").getValueObject().toString();
		if(flag!=null){
			boolean f=Boolean.parseBoolean(flag);
			if(f==true){
				if(ui.getBillCardPanel().getHeadItem("def_str_1").getValueObject()==null
						||ui.getBillCardPanel().getHeadItem("def_str_1").getValueObject().equals("")
						/*&&(ui.getBillCardPanel().getHeadItem("backerrtab").getValueObject()==null
								||ui.getBillCardPanel().getHeadItem("backerrtab").getValueObject().equals(""))*/){
					getSelfUI().showErrorMessage("存在回执标志时，回执消息服务器不可为空！");
					return ;
				}
			}
		}
		
		/*
		 * 2011-6-30
		 * 程莉
		 * 数据来源类型在为 消息总线(主键为0001AA10000000013SVI) 时，相同的存储表名只能被选择一次
		 */
		String ioflag=(String) ui.getBillCardPanel().getHeadItem("ioflag").getValueObject();
		String memorytype=(String) ui.getBillCardPanel().getHeadItem("memorytype").getValueObject();
		if(ioflag!=null&&ioflag.equals("输入")&&memorytype!=null&&memorytype.equals("视图")){
			getSelfUI().showErrorMessage("数据类型为视图不能定义为输入！");
			return;
		}
		String sourcetype=(String) ui.getBillCardPanel().getHeadItem("sourcetype").getValueObject();
		String memorytable=(String) ui.getBillCardPanel().getHeadItem("memorytable").getValueObject();
		String canshu=(String) ui.getBillCardPanel().getHeadItem("sourceparam").getValueObject();
		//项目主键	sourceparam
		//liyunzhe 2012-06-05 modify 把数据来源类型比较改成主键比较 start
		if(sourcetype !=null && !"".equals(sourcetype) && sourcetype.length()>0){
			if(IContrastUtil.DATAORIGIN_XXZX.equals(sourcetype)){
				Collection coll=bs.retrieveByClause(DipDatarecHVO.class, "sourcetype='"+sourcetype+"' and memorytable='"+memorytable+"' " +
						"and nvl(dr,0)=0 and pk_datarec_h <>'"+pk+"' and ioflag='"+ioflag+"'");
				if(coll !=null&&((pk.length()==20&&coll.size()>1)||pk.length()<20&&coll.size()>0)){
								getSelfUI().showErrorMessage("数据来源类型为【消息总线】格式，业务数据(存储表名)不能重复被引用！");
								return;
				}
				
				//项目主键	sourcecon ,项目主键	databakfile
				String sourcecon=(String) ui.getBillCardPanel().getHeadItem("sourcecon").getValueObject();
				String databakfile=(String) ui.getBillCardPanel().getHeadItem("databakfile").getValueObject();
				if(sourcecon==null||sourcecon.trim().equals("")||databakfile==null||databakfile.trim().equals("")){
					getSelfUI().showErrorMessage("数据来源类型为【消息总线】格式，数据来源连接和数据流类型不能为空！");
					return;
				}
//				if(databakfile==null||databakfile.trim().equals("")){
//					getSelfUI().showErrorMessage("数据来源类型为【消息总线】格式，数据流类型不能为空！");
//					return;
//				}
			}
			if(IContrastUtil.DATAORIGIN_ZJB.equals(sourcetype)){
				if(canshu==null||canshu.equals("")){
					getSelfUI().showErrorMessage("数据来源类型为【中间表】格式，数据来源参数(来源表名)不能为空！");
					return;
				}
			}
			if(IContrastUtil.DATAORIGIN_WEBSERVICEZQ.equals(sourcetype)){
				if(canshu==null||canshu.equals("")){
//					getSelfUI().showErrorMessage("数据来源类型为【webservice抓取】格式，数据来源参数(系统标识，站点标志，业务标识，唯一标识)不能为空！");
					getSelfUI().showErrorMessage("数据来源类型为【webservice抓取】格式，数据来源参数("+IContrastUtil.DATAORIGIN_WEBSERVICEZQ_PARAMETAR+")");
					return;
				}else{
//					if(canshu.split(",")==null||!(canshu.split(",").length==4||canshu.split(",").length==6)){
						if(canshu.split(",")==null||!(canshu.split(",").length==3)){
							getSelfUI().showErrorMessage("数据来源类型为【webservice抓取】格式，数据来源参数("+IContrastUtil.DATAORIGIN_WEBSERVICEZQ_PARAMETAR+")");
						return;
					}
//					DipDatarecHVO[] list=(DipDatarecHVO[]) HYPubBO_Client.queryByCondition(DipDatarecHVO.class, "pk_datarec_h<>'"+pk+"' and ioflag='"+ioflag+"' and sourcetype='"+sourcetype+"' and sourceparam='"+canshu+"' and nvl(dr,0)=0");
//					if(list!=null&&list.length>0){
//						getSelfUI().showErrorMessage("数据来源类型为【webservice抓取】格式，数据来源参数(系统标识，站点标志，业务标识，唯一标识)重复定义！");
//						return;
//					}
				}
			}
			if(IContrastUtil.DATAORIGIN_ZDZQ.equals(sourcetype)){
				if(canshu==null||canshu.equals("")){
					getSelfUI().showErrorMessage("数据来源类型为【主动抓取】格式，数据来源参数不能为空！");
					return;
				}
			}
			
		}
		//liyunzhe 2012-06-05 modify 把数据来源类型比较改成主键比较 end

		//获得界面原始数据的vo
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);

		//获得界面新数据vo
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

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

		DipDatarecHVO hvo=(DipDatarecHVO) checkVO.getParentVO();
		if(hvo.getTrancon()==null||!hvo.getTrancon().equals("Y")){
			HYPubBO_Client.deleteByWhereClause(DataformatdefHVO.class, "pk_datarec_h ='"+hvo.getPrimaryKey()+"' and Messflowtype in ('0001AA100000000142YQ','0001ZZ10000000018K7M') and nvl(dr,0)=0");
		}
		//如果该单据增加保存时是自动维护树结构，则执行如下操作
		if (getUITreeCardController().isAutoManageTree()) {		

			getSelfUI().insertNodeToTree(billVO.getParentVO());

		}
//		setButEnable(true);
//		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//		getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//		getSelfUI().updateButtonUI();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		
	}

	@Override
	protected void onBoLineDel() throws Exception {
		// TODO Auto-generated method stub
		int row=getSelfUI().getBillCardPanel().getBodyPanel().getTable().getSelectedRow();
		if(row==-1){
			getSelfUI().showWarningMessage("请在表体选择一行数据！");
			return;
		}
		super.onBoLineDel();
	}
//	数据格式：传输控制标志为'Y'时，数据格式按钮才可用

//	public void dataStyle(){
//		VOTreeNode tempNode=getSelectNode();
//		if(tempNode==null){
//			getSelfUI().showErrorMessage("请选择要操作的节点！");
//			return ;
//		}
//		String str=(String)tempNode.getParentnodeID();
//		if(str==null||str.length()==0){
//			getSelfUI().showErrorMessage("请选择子节点进行数据格式定义！");
//			return ;
//		}
//		DipDatarecHVO hvo=(DipDatarecHVO) tempNode.getData();
//
////		nc.ui.jyprj.dataformatdef.DataForDefClientUI clientUI = new nc.ui.jyprj.dataformatdef.DataForDefClientUI();
//		DataFormatDlg dlg = new DataFormatDlg(getSelfUI(),new UFBoolean(true),hvo);
//		dlg.show();
//
//	}
	//数据校验
	public void dataValidate(){

		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择节点！");
			return;
		}
		DipDatarecHVO hvo=(DipDatarecHVO) node.getData();
		if(hvo!=null){
			String fpk=hvo.getFpk();
			if(fpk==null||fpk.length()<=0||(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue())){
				this.getSelfUI().showWarningMessage("系统节点或文件夹不能编辑！");
				return;
			}
		}
//		String pk=node.getParentnodeID().toString();
//		if(pk==null||pk.length()==0){
//		this.getSelfUI().showWarningMessage("系统节点不能编辑！");
//		return;
//		}
		HashMap map = new HashMap();
		map.put("pk", getSelfUI().selectnode);
		map.put("type", "数据接收定义");
		/*将当前页面的表头code和name放到map中，用于当弹出数据校验窗口时，编码和名称为空，自动填充
		 * 2011-06-14
		 * zlc*/
		map.put("code", hvo.getSyscode());
  		map.put("name", hvo.getRecname());
		DatarecDlg dlg = new DatarecDlg(getSelfUI(),new UFBoolean(true),map);
		dlg.show();
		
		

	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		switch (intBtn) {
//		case IBtnDefine.DataStyle:
//			dataStyle();
//			break;
		case IBtnDefine.DataCheck:
			dataValidate();
			break;
		case IBtnDefine.JSDYDATAFORMAT:
			onBoDataFormat();
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
		case IBtnDefine.MBZH:
			onBoMBZH();
			break;
		default:
			break;
		}
	}
	/**
	 * @desc 文件移动
	 * */
	public void onBoMoveFolder() throws Exception{
		
		MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
		if(billvo!=null&&billvo.getParentVO()!=null){
			SuperVO hvo=(SuperVO) billvo.getParentVO();
			MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_JS,hvo);
			dlg.showModal();
			String ret=dlg.getRes();
			if(ret!=null){
				hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_JS), ret);
				HYPubBO_Client.update(hvo);
				hvo=(DipDatarecHVO) HYPubBO_Client.queryByPrimaryKey(DipDatarecHVO.class, hvo.getPrimaryKey());
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
	public void onBoDeleteFolder()  throws Exception{
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
		DipDatarecHVO vo =(DipDatarecHVO) tempNode.getData();
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipDatarecHVO.class, "fpk='"+vo.getPrimaryKey()+"'");
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

	public void onBoAddFolder()  throws Exception{
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null||tempNode.getData()==null){
			getSelfUI().showErrorMessage("请选择要增加的节点！");
			return ;
		}
		DipDatarecHVO hvo=(DipDatarecHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipDatarecHVO newhvo=new DipDatarecHVO();
		newhvo.setFpk(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipDatarecHVO[] listvos=(DipDatarecHVO[]) HYPubBO_Client.queryByCondition(DipDatarecHVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
		List<String> listcode=new ArrayList<String>();
		List<String> listname=new ArrayList<String>();
		if(listvos!=null&&listvos.length>0){
			for(int i=0;i<listvos.length;i++){
				listcode.add(listvos[i].getSyscode());
				listname.add(listvos[i].getRecname());
			}
		}
			
		AddFolderDlg addlg=new AddFolderDlg(getBillUI(),listcode,listname,null,null);
		addlg.showModal();
		if(addlg.isOk()){
			newhvo.setSyscode(addlg.getCode());
			newhvo.setRecname(addlg.getName());
			String pk = null;
			try {
				pk = HYPubBO_Client.insert(newhvo);
			} catch (UifException e) {
				e.printStackTrace();
			}
			try {
				newhvo=(DipDatarecHVO) HYPubBO_Client.queryByPrimaryKey(DipDatarecHVO.class, pk);
				
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

	public void onBoEditFolder() throws Exception{
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
		DipDatarecHVO vo =(DipDatarecHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
//			DipDatarecHVO[] listvos=(DipDatarecHVO[]) HYPubBO_Client.queryByCondition(DipDatarecHVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_datarec_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
			DipDatarecHVO[] listvos=(DipDatarecHVO[]) HYPubBO_Client.queryByCondition(DipDatarecHVO.class, "fpk='"+str+"' and isfolder='Y' and pk_datarec_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
			List<String> listcode=new ArrayList<String>();
			List<String> listname=new ArrayList<String>();
			if(listvos!=null&&listvos.length>0){
				for(int i=0;i<listvos.length;i++){
					listcode.add(listvos[i].getSyscode());
					listname.add(listvos[i].getRecname());
				}
			}
				
			AddFolderDlg adlg=new AddFolderDlg(getBillUI(),listcode,listname,vo.getSyscode(),vo.getRecname());
			adlg.showModal();
			if(adlg.isOk()){
				String tempc=adlg.getCode();
				String tempn=adlg.getName();
				if(!tempc.equals(vo.getSyscode())||!tempn.equals(vo.getRecname())){
					vo.setSyscode(tempc);
					vo.setRecname(tempn);
					HYPubBO_Client.update(vo);
					vo=(DipDatarecHVO) HYPubBO_Client.queryByPrimaryKey(DipDatarecHVO.class, vo.getPrimaryKey());
					if (getUITreeCardController().isAutoManageTree()) {	
						getSelfUI().insertNodeToTree(vo);
						getBillTreeCardUI().updateUI();
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("syscode", tempc);
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("recname", tempn);
					}
				}
			}
			return;
		}
	}

	public void onBoControl(){

		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showErrorMessage("请选择要操作的节点");
			return;
		}
		DipDatarecHVO hvo=null;
		try {
			hvo = (DipDatarecHVO) HYPubBO_Client.queryByPrimaryKey(DipDatarecHVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}

		ControlHVO chvo=new ControlHVO();
		chvo.setBustype("数据接收定义");
		chvo.setCode(hvo.getSyscode());
		chvo.setName(hvo.getRecname());

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPrimaryKey(),IContrastUtil.YWLX_JS,hvo.getMemorytable());
		cd.showModal();
	}
	private void onBoDataFormat() {
		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要操作的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str==null||str.length()==0){
			getSelfUI().showErrorMessage("请选择子节点进行数据格式定义！");
			return ;
		}
		DipDatarecHVO hvo=(DipDatarecHVO) tempNode.getData();
		DataformatDLG dlg=new DataformatDLG(getSelfUI(),hvo);
		dlg.showModal();
		
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
//		DipDatarecHVO hvo=(DipDatarecHVO) getSelectNode().getData();
//		if(hvo!=null&&hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
//			setButEnable(false);
//			getSelfUI().getButtonManager().getButton(IBillButton.Copy).setEnabled(false);
//			getSelfUI().getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//			getSelfUI().getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//			getSelfUI().getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//			
//		}else{
//			setButEnable(true);
//			getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//			getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);	
//		}
//		
//		getSelfUI().updateButtonUI();
		
	}

	@Override
	protected void onBoCopy() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要增加的文件夹！");
			return ;
		}
		DipDatarecHVO hvo=(DipDatarecHVO) tempNode.getData();
		
		DipSysregisterHVO vo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, hvo.getPk_xt());
		if(SJUtil.isNull(vo)){
			getSelfUI().showErrorMessage("请选择系统节点！");
			return;
		}
		super.onBoCopy();
		setButEnable(false);
//		String pk_sysregister_h =hvo.getPk_xt();
		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytable").getComponent();
//		SysDefinitRefModel model2=(SysDefinitRefModel) uir.getRefModel();
//		model2.addWherePart(" and pk_sysregister_h='"+pk_sysregister_h+"'");
//		uir.setRefModel(model2);
//		
		
		DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
		model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
		model.addWherePart(" and tabsoucetype='自定义'");
		uir.setBlurValue("last");
		uir.setRefModel(model);
		
		controHZstats(false);
		getSelfUI().getBillCardPanel().getHeadItem("databakfile").setEdit(false);//数据流类型
		getSelfUI().getBillCardPanel().getHeadItem("pk_datarec_h").setValue(null);
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		setButEnable(false);
	}

	/*@Override
	protected void onBoRefresh() throws Exception {
		IOptByPlg io=(IOptByPlg) NCLocator.getInstance().lookup(IOptByPlg.class.getName());
//		io.styleToMsg("##BEGIN,00055,0001,9897,E##BEGIN,00055,0001,9897,a,a,a,a,E##BEGIN,00055,0001,9897,b,b,b,b,E##BEGIN,00055,0001,9897,E##B,00055,0001,CSYL,END##B,00055,0001,CSYL,1,1,1,1,END##B,00055,0001,CSYL,END##");
		io.radeFile("e:/excel/imp/abc.txt");
	}*/
	private String pk_dataorigin;
	public void onBoMBZH()throws Exception{
		AskMBDLG ask=new AskMBDLG(getSelfUI(),null,"模板","        请选择操作类型?",new String[]{"导入同步定义数据格式","导出同步定义数据格式"});
		ask.showModal();
		int result=ask.getRes();
		if(result==0){
			onBoImport();
		}else if(result==1){
			onBoExport();
		}
	}
	List<DataformatdefBVO> successbvo;//通过校验会都正确，需要插入到最后的数据库中的vo集合
	Map<String,List<DataformatdefBVO>> successbvoMap;//
	Map<String,List<DipDatarecSpecialTab>> successspecialbvoMap;//
	String messflowtype[]=null;
	@Override
	protected void onBoImport() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能该操作！"); 
			return;
		}
		DipDatarecHVO hvo=null;
		if(tempNode.getData() instanceof DipDatarecHVO){
			hvo=(DipDatarecHVO) tempNode.getData();
		}
		if(!(hvo!=null&&!hvo.getIsfolder().booleanValue())){
			getSelfUI().showWarningMessage("选择节点不能该操作！");
			return;
		}
		DataformatdefHVO dataformatdefhvo=new DataformatdefHVO();
		String pk_datarec_h=hvo.getPk_datarec_h();
		
		
		
		String backCon=hvo.getBackcon()==null?"":hvo.getBackcon();
		String tranCon=hvo.getTrancon()==null?"":hvo.getTrancon();
		String ioflag=hvo.getIoflag()==null?"":hvo.getIoflag();
		if(ioflag.trim().equals("")){
			showErrorMessage("改节点输入输出标志不能为空");
			return;
		}
	     pk_dataorigin=hvo.getSourcetype();//数据来源类型主键sourcetype
	    DipDataoriginVO vo=(DipDataoriginVO) HYPubBO_Client.queryByPrimaryKey(DipDataoriginVO.class, pk_dataorigin);
	    if(vo==null){
	    	showErrorMessage("该节点数据来源类型不能为空");
	    	return;
	    }
	    String selectNodeDataoriginname=vo.getName();//数据来源类型名称
	    if(selectNodeDataoriginname==null||selectNodeDataoriginname.trim().equals("")){
	    	showErrorMessage("该节点数据来源类型名称不能为空");
	    	return;
	    }
	    String selectNodeBussinesstableKey=hvo.getMemorytable();//业务表
	    if(selectNodeBussinesstableKey==null||selectNodeBussinesstableKey.trim().equals("")){
	    	showErrorMessage("业务表不能为空");
	    	return;
	    }
	    HashMap<String, DipDatadefinitBVO> bussinessMap=new HashMap<String, DipDatadefinitBVO>();//存放的是业务表的字段英文名和整个DipDatadefinitBVO值
	    DipDatadefinitBVO datadefinitbvos[]=null;
	    datadefinitbvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+selectNodeBussinesstableKey+"' and nvl(dr,0)=0 ");
	    if(datadefinitbvos==null&&datadefinitbvos.length==0){
	    	showErrorMessage("业务表不能为空");
	    	return;
	    }else{
	    	for(int i=0;i<datadefinitbvos.length;i++){
	    		DipDatadefinitBVO bvo=datadefinitbvos[i];
	    		if(bvo!=null&&bvo.getEname()!=null&&!bvo.getEname().trim().equals("")){
	    			bussinessMap.put(bvo.getEname().toUpperCase(), bvo);
	    		}
	    	}
	    }
	    if(bussinessMap.size()<=0){
	    	showErrorMessage("业务表不能为空");
	    	return;
	    }
		 successbvoMap=new HashMap<String,List<DataformatdefBVO>>();//
		 successspecialbvoMap=new HashMap<String, List<DipDatarecSpecialTab>>();
	    
		String filePath=getImpFilePath();
		if(filePath.trim().length()<=0){
			return;
		}
		String expTitleName[]=getFileNames(2,true,pk_dataorigin);
		String impTitleCode[]=getFileCodes(2,pk_dataorigin);
	    String dataoriginname="";//数据来源类型名称
	    Class classvos[]=new Class[]{DataformatdefHVO.class,DataformatdefBVO.class};
	    //如果是格式文件或者消息总线格式，要增加特殊页签。2012-06-11 liyunzhe
	    if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_GSWJ)||pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX)){
	    	List<String> listname=new ArrayList<String>();
	    	for(int i=0;i<expTitleName.length;i++){
	    		listname.add(expTitleName[i]);
	    	}
	    	List<String> listcode=new ArrayList<String>();
	    	for(int i=0;i<impTitleCode.length;i++){
	    		listcode.add(impTitleCode[i]);
	    	}
	    	if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_GSWJ)){
	    		listname.add(getSpecialFieldsname(FIX_FORMAT, true));
		    	listname.add(getSpecialFieldsname(XML_FORMAT, true));
		    	listcode.add(getSpecialFieldscode(FIX_FORMAT, true));
		    	listcode.add(getSpecialFieldscode(XML_FORMAT, true));
	    	}
	    	if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX)){
	    		listname.add(getSpecialFieldsname(FIX_FORMAT, true));
		    	listname.add(getSpecialFieldsname(FIX_FORMAT, true));
		    	listcode.add(getSpecialFieldscode(FIX_FORMAT, true));
		    	listcode.add(getSpecialFieldscode(FIX_FORMAT, true));
	    	}
	    	expTitleName=listname.toArray(new String[0]);
	    	impTitleCode=listcode.toArray(new String[0]);
	    	 classvos=new Class[]{DataformatdefHVO.class,DataformatdefBVO.class,DipDatarecSpecialTab.class};
	    }
	    
	    String bussinesstable="";//业务表
	    String division="";//分割标志/根节点
	    String folwdef="";//对照方式
	    String filetype="";//文件类型
	    String twoPoint="";//二级节点
	    int filetypeint=-1;//文件类型 
	    String folwdefstr="0";
	    
	    
	    //liyunzhe add 数据格式增加新的参数。
//	    esb增加字段 mergestyle,mergemark,mergecount,pagerunsys,sendstyle,delayed
//	    系统标志 接口平台格式，自定义格式
//	    String systemflag="";
//	    合并类型 不合并，记录合并，完全合并
	    String mergestyle="";
//	    合并分隔符
	    String mergemark="";
//	    记录合并数
	    String mergecount="";
//	    分页参数
	    String pagerunsys="";
//	    发送类型 空数据不发送,空数据发送
	    String sendstyle="";
//	    延时（秒）
	    String delayed="";
	    Map<String,Integer> delayedMap=new HashMap<String, Integer>();//key 是 数据类型pk，value是延时时间
	    //liyunzhe add 数据格式增加新的参数。
	    
	    String []selectNodeNeedSheetNames=null;//
//		Class classvos[]=new Class[]{DataformatdefHVO.class,DataformatdefBVO.class};
		TxtDataVO txtdatavos[]=getTxtDataVO(filePath, expTitleName, impTitleCode, classvos);
		//得到excel的所有数据，每个TxtDataVO对象是单个页签的数据。
		
		if(txtdatavos!=null&&txtdatavos.length>0){
			String name=txtdatavos[0].getSheetName();
			if(!(name!=null&&name.equals("业务表信息"))){
				showErrorMessage("第一个页签名必须是业务表信息！");
				return;
			}
			RowDataVO[] rowdatavo=txtdatavos[0].getData();
			if(rowdatavo!=null&&rowdatavo.length>0){
				dataoriginname=rowdatavo[0].getAttributeValue("0")==null?"":rowdatavo[0].getAttributeValue("0").toString();
				bussinesstable=rowdatavo[0].getAttributeValue("1")==null?"":rowdatavo[0].getAttributeValue("1").toString();
				folwdef=rowdatavo[0].getAttributeValue("2")==null?"":rowdatavo[0].getAttributeValue("2").toString();
				division=rowdatavo[0].getAttributeValue("3")==null?"":rowdatavo[0].getAttributeValue("3").toString();
				if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX)){
					mergestyle=rowdatavo[0].getAttributeValue("4")==null?"":rowdatavo[0].getAttributeValue("4").toString();
//					if(mergestyle!=null&&mergestyle.equals("1")){
//						mergestyle="记录合并";
//					}else if(mergestyle!=null&&mergestyle.equals("2")){
//						mergestyle="完整合并";
//					}else{
//						mergestyle="不合并";
//					}
					mergemark=rowdatavo[0].getAttributeValue("5")==null?"":rowdatavo[0].getAttributeValue("5").toString();
					mergecount=rowdatavo[0].getAttributeValue("6")==null?"":rowdatavo[0].getAttributeValue("6").toString();
					pagerunsys=rowdatavo[0].getAttributeValue("7")==null?"":rowdatavo[0].getAttributeValue("7").toString();
					sendstyle=rowdatavo[0].getAttributeValue("8")==null?"":rowdatavo[0].getAttributeValue("8").toString();
					delayed=rowdatavo[0].getAttributeValue("9")==null?"":rowdatavo[0].getAttributeValue("9").toString();
				}else{
					filetype=rowdatavo[0].getAttributeValue("4")==null?"":rowdatavo[0].getAttributeValue("4").toString();
				}
				//没有二级节点名称 2012-06-11 liyunzhe
				//twoPoint=rowdatavo[0].getAttributeValue("5")==null?"":rowdatavo[0].getAttributeValue("5").toString();
			}
		}else{
			return;
		}
		String dbdataoriginame=iq.queryfield(" select name from dip_dataorigin where pk_dataorigin='"+pk_dataorigin+"' and nvl(dr,0)=0");
		if(getSheetName(dataoriginname)!=null&&!dataoriginname.equals(dbdataoriginame)){
			showErrorMessage("导入excel数据来源类型和选择节点数据来源类型不相同");
			return;
		}
		//String titleValue[]=new String[]{dataoriginname,folwdef,division,filetype,twoPoint};
		//对第个页签（表头）做校验，如果表头没有通过就直接返回错误消息。
		String errmessage=getTitleCheck(dataoriginname, folwdef, division, filetype, twoPoint, filetypeint, selectNodeNeedSheetNames,mergestyle,mergemark,mergecount,pagerunsys,sendstyle,delayed,delayedMap);
		if(errmessage!=null&&errmessage.trim().length()>0){
			showErrorMessage(errmessage);
			return;
		}
		
		if(filetype.equals("txt")){
			filetypeint=0;	
		}else if(filetype.equals("xml")){
			filetypeint=1;
		}else if(filetype.equals("excel")){
			filetypeint=2;
		}
		
//		if(!division.trim().equals("")||!twoPoint.trim().equals("")){
		if(!division.trim().equals("")){
			if(division.trim().length()>0){
				MessagelogoVO logovo[]=null;
				logovo=getMessageLogo(division,null,null);
			    if(logovo==null||logovo.length==0){
			    	showErrorMessage("分割标志["+division+"],不存在");
			    	return;
			    }else{
			    	division=logovo[0].getPk_messagelogo();
			    }
			    
			}
			if(mergemark.trim().length()>0){
				MessagelogoVO logovo[]=null;
				logovo=getMessageLogo(mergemark,null,null);
			    if(logovo==null||logovo.length==0){
			    	showErrorMessage("合并分隔符["+mergemark+"],不存在");
			    	return;
			    }else{
			    	mergemark=logovo[0].getPk_messagelogo();
			    }
			}
		}
		
		//组合主表数据。设置表头数据
		dataformatdefhvo.setPk_datarec_h(pk_datarec_h);
		dataformatdefhvo.setBeginsign(division);//根节点标志
//		dataformatdefhvo.setEndsign(twoPoint);//二级节点标志
		dataformatdefhvo.setFiletype(filetypeint);//文件类型
		dataformatdefhvo.setMessflowdef(folwdefstr);//排序
		if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX)){
			if(mergestyle.equals("不合并")){
				mergestyle="0";
			}else if(mergestyle.equals("记录合并")){
					mergestyle="1";
			}else if(mergestyle.equals("完整合并")){
					mergestyle="2";
			}
				
			dataformatdefhvo.setMergestyle(mergestyle);
			dataformatdefhvo.setMergemark(mergemark);
			dataformatdefhvo.setMergecount(mergecount==null?null:Integer.parseInt(mergecount));
			dataformatdefhvo.setPagerunsys(pagerunsys==null?null:Integer.parseInt(pagerunsys));
			if(sendstyle.equals("空数据不发送")){
				sendstyle="0";
			}else{
				sendstyle="1";
			}
//			dataformatdefhvo.setSendstyle(sendstyle);
//			if(delayedArray!=null&&delayedArray.length>0){
//				dataformatdefhvo.setDelayed(delayedArray[0]);
//			}
		}

		checkImportData(txtdatavos, dataoriginname, dataformatdefhvo, hvo, folwdef, filePath, filetype, bussinessMap,delayedMap);
	}
	/**
	 * liyunzhe 2012-06-13
	 * @param txtdatavos  excel所有页签的数据
	 * @param dataoriginname  数据来源类型名称
	 * @param dataformatdefhvo 
	 * @param hvo 同步定义表头vo
	 * @param folwdef 顺序还是字段
	 * @param filePath excel路径
	 * @param filetype 文件类型
	 * @param bussinessMap 业务表的ename英文名和对应DipDatadefinitBVO对象
	 * @throws Exception
	 */
	public void checkImportData(TxtDataVO[] txtdatavos,String dataoriginname,DataformatdefHVO dataformatdefhvo,DipDatarecHVO hvo,String folwdef,String filePath,String filetype, HashMap<String, DipDatadefinitBVO> bussinessMap,Map<String,Integer> delayedMap) throws Exception{
		String backCon=hvo.getBackcon()==null?"":hvo.getBackcon();
		String tranCon=hvo.getTrancon()==null?"":hvo.getTrancon();
		String pk_datarec_h=hvo.getPk_datarec_h();
		
		
		
		if(txtdatavos!=null&&txtdatavos.length>1){
			List<TxtDataVO> errTxtDataVOs=new ArrayList<TxtDataVO>();//通过校验后，如果有错误的页签，会加入到这个list中。
		    boolean flag=false;//表示是否通过校验，如果falg=true，表示要输出错误信息
			for(int i=1;i<txtdatavos.length;i++){//从第二个数组开始，表头已经做过校验和组合数据了
				successbvo=new ArrayList<DataformatdefBVO>();
				TxtDataVO txtdatavo=txtdatavos[i];
				Vector<SuperVO> vector=txtdatavo.getDatavo();
				String sheetname=txtdatavo.getSheetName()==null?"":txtdatavo.getSheetName();
				StringBuffer sb=new StringBuffer();//错误提示语句
				if(txtdatavo!=null&&txtdatavo.getDatavo()!=null){
//					if(dataoriginname.equals("消息总线")){
						if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX)){
//在表头校验处已经校验了						
//						if(!folwdef.equals("顺序")){
//							showErrorMessage("消息总线的对照方式只能是顺序");
//							return;
//						} 
						String esbSheetNames[]=getEsbSheetNames( tranCon, backCon);//消息总线应该有的页签。
						if(esbSheetNames!=null||esbSheetNames.length>=1){//
							if(txtdatavos.length!=(esbSheetNames.length+1)){
								showErrorMessage("该节点导入的excel只能有"+(esbSheetNames.length+1)+"个页签");
								return;
							}else{
									if(sheetname.equals(esbSheetNames[i-1])){
									//进行校验 2012-06-12 liyunzhe add 增加对消息总线的固定页签和回执页签做校验
										if(i==txtdatavos.length-1||i==txtdatavos.length-2){//固定标志页签 ,回执标志页签
											boolean fixformat=false;
											if(i==txtdatavos.length-2){
												fixformat=true;
												
											}
											if(vector!=null&&vector.size()>0){
												for(int w=0;w<vector.size();w++){
												DipDatarecSpecialTab spetab=(DipDatarecSpecialTab) vector.get(w);
												spetab.setPk_datarec_h(pk_datarec_h);
													if(w==0){
													  if(1!=spetab.getNodenumber()){
														  sb.append("第一行编码只能是1，");
													  }
//													DataformatPanel.XTBZ
													    if(spetab.getName()!=null&&!spetab.getName().equals(DataformatPanel.XTBZ)){
													    	sb.append("第一行名称只能是"+DataformatPanel.XTBZ+"，");
													    }
													  
													}else if(w==1){
														 if(2!=spetab.getNodenumber()){
															  sb.append("第二行编码是2，");
														  }
														    if(!DataformatPanel.ZDBZ.equals(spetab.getName())){
														    	sb.append("第二行名称只能是"+DataformatPanel.ZDBZ+"，");
														    }
													}else if(w==2){
														if(3!=spetab.getNodenumber()){
															  sb.append("第三行编码是3，");
														  }
														 if(DataformatPanel.ZDBZ.equals(spetab.getName())){
														    	sb.append("第三行名称只能是"+DataformatPanel.YWBZ+"，");
														 }
													}else{
														sb.append("该页签只允许有3行数据，");
													}
													  if(fixformat){
														  spetab.setIs_xtfixed(new UFBoolean(true));
														  if(successspecialbvoMap.get(DataformatPanel.SPECIAL_XT)==null){
															  List<DipDatarecSpecialTab> list=new ArrayList<DipDatarecSpecialTab>();
															  list.add(spetab);
															  successspecialbvoMap.put(DataformatPanel.SPECIAL_XT, list);
														  }else{
															  successspecialbvoMap.get(DataformatPanel.SPECIAL_XT).add(spetab);
														  }
													  }else{
														  spetab.setIs_back(new UFBoolean(true));
														  if(successspecialbvoMap.get(DataformatPanel.SPECIAL_BACK)==null){
															  List<DipDatarecSpecialTab> list=new ArrayList<DipDatarecSpecialTab>();
															  list.add(spetab);
															  successspecialbvoMap.put(DataformatPanel.SPECIAL_BACK, list);
														  }else{
															  successspecialbvoMap.get(DataformatPanel.SPECIAL_BACK).add(spetab);
														  }
													  }
													  
													
//													如果有错误，就处理错误
													if(sb!=null&&sb.toString().trim().length()>0){
														RowDataVO rowvo=txtdatavo.getData()[w];
														int m=txtdatavo.getColCount();
														rowvo.setAttributeValue(m+"", sb.toString().substring(0, sb.length()-1));
														txtdatavos[i].getErrList().add(rowvo);
														flag=true;
														sb=new StringBuffer();
													}
												}	
											}
//										}else if(vector!=null&&vector.size()>=5){
										}else if(vector!=null&&vector.size()>=1){//liyunzhe modify 2012-06-11 
					  					//0，数据项不能为空
					  					//1，如果是顺序导入对应项不能有值
						  				//2，如果是字段对照的话，对应项必须有值
						  				//3，如果都是业务数据，必须保证所有数据项都存在目标业务表中。
						  				//4，如果还有非业务数据，要保证所有非业务数据都存在消息标志表中。
					  					//"code,datakind,vdef3,vedf2,correskind";
										for(int w=0;w<vector.size();w++){
				  							sb=new StringBuffer();
					  						DataformatdefBVO bvo=(DataformatdefBVO) vector.get(w);
					  						if(bvo!=null){
					  							String[] notNullLine=null;
//					  							if(folwdef.equals("顺序")){
					  								//数据项,中文名称,类型,对应项
					  								notNullLine=new String[]{"datakind","vdef3","vdef2"};
//					  							}
					  							bvo.setCode(w+1);
					  							sb=checkImportSequenceData(bvo, notNullLine, txtdatavo, bussinessMap);
					  							//liyunzhe 修改注释掉下边校验代码 2012-06-11 这个是对输入的接口格式的强制校验，现在取消了强制校验
//					  							if(sb==null||sb.toString().trim().length()==0){
//					  								if(ioflag.equals("输入")){//
//					  									if(w==0){
//						  									if(!"标志头".equals(bvo.getVdef2())){
//						  										sb.append("本行类型字段必须是【标志头】");
//						  									}
//						  								}
//						  								if(w==1){
//						  									if(!"固定标志".equals(bvo.getVdef2())||!"系统标志".equals(bvo.getDatakind())){
//						  										sb.append("本行类型字段必须是【固定标志】,数据项字段必须是【系统标志】");
//						  									}
//						  								}
//						  								if(w==2){
//						  									if(!"固定标志".equals(bvo.getVdef2())||!"站点标志".equals(bvo.getDatakind())){
//						  										sb.append("本行类型字段必须是【固定标志】,数据项字段必须是【站点标志】");
//						  									}
//						  								}
//						  								if(w==3){
//						  									if(!"固定标志".equals(bvo.getVdef2())||!"业务标志".equals(bvo.getDatakind())){
//						  										sb.append("本行类型字段必须是【固定标志】,数据项字段必须是【业务标志】");
//						  									}
//						  								}
//						  								if(w==vector.size()-1){
//						  									if(!"标志尾".equals(bvo.getVdef2())){
//						  										sb.append("本行类型字段必须是【标志尾】");
//						  									}
//						  								}
//					  								}
//					  								
//					  							}
					  						}
					  						if(sb!=null&&sb.toString().trim().length()>0){
												RowDataVO rowvo=txtdatavo.getData()[w];
												int m=txtdatavo.getColCount();
												rowvo.setAttributeValue(m+"", sb);
												txtdatavos[i].getErrList().add(rowvo);
												flag=true;
												sb=new StringBuffer();
											}
					  					}	
									}
										
										if(flag){//表示没有通过校验，有错误信息
											errTxtDataVOs.add(txtdatavos[i]);
											flag=false;
										}else{
											if(i!=txtdatavos.length-1&&i!=txtdatavos.length-2){//如果不是消息固定格式页签和回执格式页签，都要存到successbvoMap格式map中，否则就存到specialmap中
												RetMessage ret=getSheetName(sheetname);
												String pk="";
												if(ret.getIssucc()){
													pk=ret.getMessage();
												}else{
													showErrorMessage(ret.getMessage());
													return;
												}
												if(pk!=null&&pk.trim().length()>0){
													successbvoMap.put(pk, successbvo);	
												}else{
													showErrorMessage(sheetname+"页签对应的主键为空");
													return;
												}
											}
										}
										if(i==txtdatavos.length-1){
											//输出错误结果 
											if(errTxtDataVOs.size()>0){
												String fileNames[]=getFileNames(esbSheetNames.length-1, false,pk_dataorigin);
												//liyunzhe add 2012-06-11 增加特殊页签字段名称
												List<String> listname=new ArrayList<String>();
										    	for(int m=0;m<fileNames.length;m++){
										    		listname.add(fileNames[m]);
										    	}
										    		listname.add(getSpecialFieldsname(FIX_FORMAT, false));
											    	listname.add(getSpecialFieldsname(FIX_FORMAT, false));
											    	fileNames=listname.toArray(new String[0]);
												
					  							String sheetNames[]=null;
					  							//String names[]=getSourceTypeToSheet(dataoriginname);
					  							if(esbSheetNames!=null&&esbSheetNames.length>0){
					  								sheetNames=new String[esbSheetNames.length+1];
					  								sheetNames[0]="业务表信息";
					  								for(int n=0;n<esbSheetNames.length;n++){
					  										sheetNames[n+1]=esbSheetNames[n];
//					  										sheetNames[n]=names[n-1];
					  								}
					  							}
					  							
					  							List <RowDataVO[]> list=new ArrayList<RowDataVO[]>();
					  							if(txtdatavos[0].getData()!=null){
					  								list.add(txtdatavos[0].getData());	
					  							}
					  							for(int m=1;m<txtdatavos.length;m++){
					  								TxtDataVO txtvo=txtdatavos[m];
					  								if(txtvo!=null){
					  									Vector<RowDataVO> errlist=txtvo.getErrList();
							  							if(errlist!=null){
							  								RowDataVO vos[]=errlist.toArray(new RowDataVO[0]);
							  								if(vos!=null){
							  									list.add(vos);
							  								}
							  							}
					  								}
					  							}
					  							filePath=filePath.replace(".xls", "-err.xls");
					  							ExpToExcel1 excel=new ExpToExcel1(filePath,sheetNames,fileNames,list,null);
					  					 		 if(excel.create()){
					  					 			 getSelfUI().showWarningMessage("导入失败，请查看错误信息["+filePath+"]");
					  					 			 return;
					  					 		 }else{
					  					 			getSelfUI().showWarningMessage("导入失败");
					  					 			return;
					  					 		 }
											}
										}	
//										else{
//										showErrorMessage(sheetname+"页签，最少有5行数据");
//										return;
//									}
								}else{
									showErrorMessage("导入excel的第"+(i+1)+"个页签名称应该是"+esbSheetNames[i-1]);
									return;
								}	
							}
						}
					}else{
					  	if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_GSWJ)||txtdatavos.length==2){
					  		if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_GSWJ)&&i>=2){
					  			if(filetype.equals("xml")){
					  				if(i==txtdatavos.length-1){
					  					if(sheetname.equals(DataformatPanel.RIGHT_XMLGS)){
					  						if(vector!=null&&vector.size()>0){
												for(int w=0;w<vector.size();w++){
												DipDatarecSpecialTab spetab=(DipDatarecSpecialTab) vector.get(w);
												spetab.setPk_datarec_h(pk_datarec_h);
													if(w==0){
													  if(1!=spetab.getNodenumber()){
														  sb.append("第一行编码只能是1，");
													  }
//													DataformatPanel.XTBZ
													    if(spetab.getName()!=null&&!spetab.getName().equals(DataformatPanel.RIGHT_XMLGS_ROOT)){
													    	sb.append("第一行节点名称只能是"+DataformatPanel.RIGHT_XMLGS_ROOT+"，");
													    }
													    if(spetab.getValue()==null||spetab.getValue().toString().trim().equals("")){
													    	sb.append("第一行对应值不能为空，");
													    }
													    if(spetab.getNodeproperty()!=null&&!spetab.getNodeproperty().trim().equals("")){
													    	if(!DataformatPanel.checkxmlPropertys(spetab.getNodeproperty())){
													    		sb.append("节点属性必须是key=value，如果是多个参数用\",\"隔开，");
													    	}
													    }
													}else if(w==1){
														 if(2!=spetab.getNodenumber()){
															  sb.append("第二行编码是2，");
														  }
														    if(spetab.getName()!=null&&!spetab.getName().equals(DataformatPanel.RIGHT_XMLGS_ONT)){
														    	sb.append("第二行节点名称只能是"+DataformatPanel.RIGHT_XMLGS_ONT+"，");
														    }
														    
														    if(spetab.getValue()==null||spetab.getValue().toString().trim().equals("")){
														    	sb.append("第二行对应值不能为空，");
														    }
														    
														    if(spetab.getNodeproperty()!=null&&!spetab.getNodeproperty().trim().equals("")){
														    	if(!DataformatPanel.checkxmlPropertys(spetab.getNodeproperty())){
														    		sb.append("节点属性必须是key=value，如果是多个参数用\",\"隔开，");
														    	}
														    }
														    
													}else if(w==2){
														if(3!=spetab.getNodenumber()){
															  sb.append("第三行编码是3，");
														  }
														 if(spetab.getName()!=null&&!spetab.getName().equals(DataformatPanel.RIGHT_XMLGS_TWO)){
														    	sb.append("第三行节点名称只能是"+DataformatPanel.RIGHT_XMLGS_TWO+"，");
														 }
														 if(spetab.getNodeproperty()!=null&&!spetab.getNodeproperty().trim().equals("")){
														    	if(!DataformatPanel.checkxmlPropertys(spetab.getNodeproperty())){
														    		sb.append("节点属性必须是key=value，如果是多个参数用\",\"隔开，");
														    	}
														    }
													}else{
														sb.append("该页签只允许有3行数据，");
													}
														  spetab.setIs_xml(new UFBoolean(true));
														  if(successspecialbvoMap.get(DataformatPanel.SPECIAL_XML)==null){
															  List<DipDatarecSpecialTab> list=new ArrayList<DipDatarecSpecialTab>();
															  list.add(spetab);
															  successspecialbvoMap.put(DataformatPanel.SPECIAL_XML, list);
														  }else{
															  successspecialbvoMap.get(DataformatPanel.SPECIAL_XML).add(spetab);
														  }
//													如果有错误，就处理错误
													if(sb!=null&&sb.toString().trim().length()>0){
														RowDataVO rowvo=txtdatavo.getData()[w];
														int m=txtdatavo.getColCount();
														rowvo.setAttributeValue(m+"", sb.toString().substring(0, sb.length()-1));
														txtdatavos[i].getErrList().add(rowvo);
														flag=true;
														sb=new StringBuffer();
													}
												}	
											}
					  					}else{
						  					sb.append("数据来源类型["+DataformatPanel.MESSTYPE_GSWJ+"]文件类型是xml，最后一个（第四个）页签必须是"+DataformatPanel.RIGHT_XMLGS);
						  					showErrorMessage(sb.toString());
						  					return;
						  				}
					  					
					  				}
					  				if(i==txtdatavos.length-2){
					  					if(sheetname.equals(DataformatPanel.RIGHT_GDGS)){
					  						if(vector!=null&&vector.size()>0){
												for(int w=0;w<vector.size();w++){
												DipDatarecSpecialTab spetab=(DipDatarecSpecialTab) vector.get(w);
												spetab.setPk_datarec_h(pk_datarec_h);
													if(w==0){
													  if(1!=spetab.getNodenumber()){
														  sb.append("第一行编码只能是1，");
													  }
//													DataformatPanel.XTBZ
													    if(spetab.getName()!=null&&!spetab.getName().equals(DataformatPanel.XTBZ)){
													    	sb.append("第一行名称只能是"+DataformatPanel.XTBZ+"，");
													    }
													  
													}else if(w==1){
														 if(2!=spetab.getNodenumber()){
															  sb.append("第二行编码是2，");
														  }
														    if(!DataformatPanel.ZDBZ.equals(spetab.getName())){
														    	sb.append("第二行名称只能是"+DataformatPanel.ZDBZ+"，");
														    }
													}else if(w==2){
														if(3!=spetab.getNodenumber()){
															  sb.append("第三行编码是3，");
														  }
														 if(DataformatPanel.ZDBZ.equals(spetab.getName())){
														    	sb.append("第三行名称只能是"+DataformatPanel.YWBZ+"，");
														 }
													}else{
														sb.append("该页签只允许有3行数据，");
													}
														  spetab.setIs_xtfixed(new UFBoolean(true));
														  if(successspecialbvoMap.get(DataformatPanel.SPECIAL_XT)==null){
															  List<DipDatarecSpecialTab> list=new ArrayList<DipDatarecSpecialTab>();
															  list.add(spetab);
															  successspecialbvoMap.put(DataformatPanel.SPECIAL_XT, list);
														  }else{
															  successspecialbvoMap.get(DataformatPanel.SPECIAL_XT).add(spetab);
														  }
//													如果有错误，就处理错误
													if(sb!=null&&sb.toString().trim().length()>0){
														RowDataVO rowvo=txtdatavo.getData()[w];
														int m=txtdatavo.getColCount();
														rowvo.setAttributeValue(m+"", sb.toString().substring(0, sb.length()-1));
														txtdatavos[i].getErrList().add(rowvo);
														flag=true;
														sb=new StringBuffer();
													}
												}	
											}
					  					}else{
						  					sb.append("数据来源类型["+DataformatPanel.MESSTYPE_GSWJ+"]文件类型是"+filetype+"，第三个页签必须是"+DataformatPanel.RIGHT_GDGS);
						  					showErrorMessage(sb.toString());
						  					return;
						  				}
					  					
					  				}
					  			}else{
					  				if(i==txtdatavos.length-1&&sheetname.equals(DataformatPanel.RIGHT_GDGS)){
					  					if(vector!=null&&vector.size()>0){
											for(int w=0;w<vector.size();w++){
											DipDatarecSpecialTab spetab=(DipDatarecSpecialTab) vector.get(w);
											spetab.setPk_datarec_h(pk_datarec_h);
												if(w==0){
												  if(1!=spetab.getNodenumber()){
													  sb.append("第一行编码只能是1，");
												  }
//												DataformatPanel.XTBZ
												    if(spetab.getName()!=null&&!spetab.getName().equals(DataformatPanel.XTBZ)){
												    	sb.append("第一行名称只能是"+DataformatPanel.XTBZ+"，");
												    }
												  
												}else if(w==1){
													 if(2!=spetab.getNodenumber()){
														  sb.append("第二行编码是2，");
													  }
													    if(!DataformatPanel.ZDBZ.equals(spetab.getName())){
													    	sb.append("第二行名称只能是"+DataformatPanel.ZDBZ+"，");
													    }
												}else if(w==2){
													if(3!=spetab.getNodenumber()){
														  sb.append("第三行编码是3，");
													  }
													 if(!DataformatPanel.YWBZ.equals(spetab.getName())){
													    	sb.append("第三行名称只能是"+DataformatPanel.YWBZ+"，");
													 }
												}else{
													sb.append("该页签只允许有3行数据，");
												}
													  spetab.setIs_xtfixed(new UFBoolean(true));
													  if(successspecialbvoMap.get(DataformatPanel.SPECIAL_XT)==null){
														  List<DipDatarecSpecialTab> list=new ArrayList<DipDatarecSpecialTab>();
														  list.add(spetab);
														  successspecialbvoMap.put(DataformatPanel.SPECIAL_XT, list);
													  }else{
														  successspecialbvoMap.get(DataformatPanel.SPECIAL_XT).add(spetab);
													  }
//												如果有错误，就处理错误
												if(sb!=null&&sb.toString().trim().length()>0){
													RowDataVO rowvo=txtdatavo.getData()[w];
													int m=txtdatavo.getColCount();
													rowvo.setAttributeValue(m+"", sb.toString().substring(0, sb.length()-1));
													txtdatavos[i].getErrList().add(rowvo);
													flag=true;
													sb=new StringBuffer();
												}
											}	
										}
					  					
					  				}else{
					  					sb.append("数据来源类型["+DataformatPanel.MESSTYPE_GSWJ+"]文件类型是"+filetype+"，最后一个（第三个）页签必须是"+DataformatPanel.RIGHT_GDGS);
					  					showErrorMessage(sb.toString());
					  					return;
					  				}
					  			}
				  				
				  			}else{
					  		String name[]=getSourceTypeToSheet(pk_dataorigin);
					  		if(name==null||name.length<=0){
					  			showErrorMessage("数据来源类型["+dataoriginname+"]不存在");
					  			return;
					  		}else{
//					  			if(dataoriginname.equals("中间表")){
//					  				if(!folwdef.equals("字段")){
//										showErrorMessage("中间表的对照方式只能是字段");
//										return;
//									}
//					  			}
					  			
					  			
					  			if(sheetname.equals(name[0])){
					  				//进入到这步说明，导入excel合法，现在开始进行导入的字段进行校验。
					  				if(vector!=null&&vector.size()>0){
					  					//0，数据项不能为空
					  					//1，如果是顺序导入对应项不能有值
						  				//2，如果是字段对照的话，对应项必须有值
						  				//3，如果都是业务数据，必须保证所有数据项都存在目标业务表中。
						  				//4，如果还有非业务数据，要保证所有非业务数据都存在消息标志表中。
					  					//"code,datakind,vdef3,vedf2,correskind";
//					  					if(folwdef.equals("顺序")){
					  						for(int w=0;w<vector.size();w++){
					  							sb=new StringBuffer();
						  						DataformatdefBVO bvo=(DataformatdefBVO) vector.get(w);
						  						if(bvo!=null){
						  							String[] notNullLine=null;
						  							if(folwdef.equals("顺序")){
						  								//数据项,中文名称,类型,对应项
						  								notNullLine=new String[]{"datakind","vdef3","vdef2"};
						  							}else{
						  								notNullLine=new String[]{"datakind","vdef3","vdef2","correskind"};
						  							}
						  							sb=checkImportSequenceData(bvo, notNullLine, txtdatavo, bussinessMap);
						  						}
						  						if(sb!=null&&sb.toString().trim().length()>0){
													RowDataVO rowvo=txtdatavo.getData()[w];
													int m=txtdatavo.getColCount();
													rowvo.setAttributeValue(m+"", sb);
													txtdatavo.getErrList().add(rowvo);
													flag=true;
													sb=new StringBuffer();
													txtdatavos[i]=txtdatavo;
												}
						  					}	
					  				}else{
					  					showErrorMessage(sheetname+"页签中数据为空");
					  					return;
					  				}

					  				
					  			}else{
					  				showErrorMessage("数据来源类型["+dataoriginname+"]的第二个页签必须是["+name[0]+"]");
					  				return;
					  			}
					  		}
					  	}
					  		
					  		if(flag){//输出错误信息excel
	  							errTxtDataVOs.add(txtdatavos[i]);
	  							flag=false;
	  						}else{
	  							if(i<2){//第三个页签和第四个页签是特殊格式，要加到successspecialbvoMap中
	  								RetMessage ret=getSheetName(sheetname);
		  							String pk="";
		  							if(ret.getIssucc()){
		  								pk=ret.getMessage();
		  							}else{
		  								showErrorMessage(ret.getMessage());
		  								return;
		  							}
									if(pk!=null&&pk.trim().length()>0){
										successbvoMap.put(pk, successbvo);	
									}else{
										showErrorMessage(sheetname+"页签对应的主键为空");
										return;
									}
	  							}
	  						}
					  		
					  		if(i==txtdatavos.length-1){
								//输出错误结果 
								if(errTxtDataVOs.size()>0){
									
									String fileNames[]=getFileNames(2, false,pk_dataorigin);
		  							String sheetNames[]=null;
		  							String names[]=getSourceTypeToSheet(pk_dataorigin);
		  							if(names!=null&&names.length>0){
		  								sheetNames=new String[names.length+1];
		  								sheetNames[0]="业务表信息";
		  								for(int n=0;n<names.length;n++){
		  										sheetNames[n+1]=names[n];
		  								}
		  							}
		  							List<String>listsheet=new ArrayList<String>();
		  							for(int k=0;k<sheetNames.length;k++){
		  								listsheet.add(sheetNames[k]);
		  							}
		  							
		  							
									List<String> listname=new ArrayList<String>();
							    	for(int m=0;m<fileNames.length;m++){
							    		listname.add(fileNames[m]);
							    	}
							    	if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_GSWJ)){
							    		listname.add(getSpecialFieldsname(FIX_FORMAT, false));
							    		listsheet.add(DataformatPanel.RIGHT_GDGS);
							    		if(filetype.equals("xml")){
							    			listname.add(getSpecialFieldsname(XML_FORMAT, false));
							    			listsheet.add(DataformatPanel.RIGHT_XMLGS);
							    		}
								    	
							    	}
							    	sheetNames=listsheet.toArray(new String[0]);
								    	fileNames=listname.toArray(new String[0]);
		  							List <RowDataVO[]> list=new ArrayList<RowDataVO[]>();
		  							if(txtdatavos[0].getData()!=null){
		  								list.add(txtdatavos[0].getData());	
		  							}
		  							for(int m=1;m<txtdatavos.length;m++){
		  								TxtDataVO txtvo=txtdatavos[m];
		  								if(txtvo!=null){
		  									Vector<RowDataVO> errlist=txtvo.getErrList();
				  							if(errlist!=null){
				  								RowDataVO vos[]=errlist.toArray(new RowDataVO[0]);
				  								if(vos!=null){
				  									list.add(vos);
				  								}
				  							}
		  								}
		  							}
		  							filePath=filePath.replace(".xls", "-err.xls");
		  							ExpToExcel1 excel=new ExpToExcel1(filePath,sheetNames,fileNames,list,null);
		  					 		 if(excel.create()){
		  					 			 getSelfUI().showWarningMessage("导入失败，请查看错误信息["+filePath+"]");
		  					 			 return;
		  					 		 }else{
		  					 			getSelfUI().showWarningMessage("导入失败");
		  					 			return;
		  					 		 }
								}
							}	
					  	}else{
					  		showErrorMessage("数据来源类型是["+dataoriginname+"]的只能有两个页签");
					  		return;
					  	}
					}
				}
			}
		}else{
			showErrorMessage("导入excel最少有两个页签");
			return;
		}
		
		//把导入的格式插入到数据库中
		saveRightFormatbyImport(hvo, dataformatdefhvo,delayedMap);
		 getSelfUI().showWarningMessage("导入格式成功！");
	}
	/**
	 * 保存导入的数据
	 * @param hvo
	 * @param dataformatdefhvo
	 */
	private void saveRightFormatbyImport(DipDatarecHVO hvo,DataformatdefHVO dataformatdefhvo,Map<String,Integer> delayedMap){
//		把导入的格式插入到数据库中
		try {
			if(successbvoMap!=null&&successbvoMap.size()>0){
				 String sql=" update dip_dataformatdef_b b"+
					" set b.dr = 1"+
					" where b.pk_dataformatdef_h in "+
					"   (select h.pk_dataformatdef_h"+
			        "    from dip_dataformatdef_h h"+
			        "    where h.pk_datarec_h = '"+hvo.getPk_datarec_h()+"' and h.messflowtype='"+"'"+
			        "    and nvl(h.dr, 0) = 0)"+
			        " and nvl(b.dr, 0) = 0 ";
				 String sql2=" update dip_dataformatdef_h h set h.dr=1 where h.pk_datarec_h='"+hvo.getPk_datarec_h()+"' and nvl(h.dr,0)=0 ";
				 iq.exesql(sql);
				 iq.exesql(sql2);
				Iterator<String> it=successbvoMap.keySet().iterator();
				while(it.hasNext()){
					String messflowtype=it.next();
					if(messflowtype!=null&&messflowtype.trim().length()>0){
						dataformatdefhvo.setMessflowtype(messflowtype);
						dataformatdefhvo.setPk_dataformatdef_h(null);
						if(delayedMap!=null&&delayedMap.get(messflowtype)!=null){
								dataformatdefhvo.setDelayed(delayedMap.get(messflowtype));
						}
						List<DataformatdefBVO> list=successbvoMap.get(messflowtype);
						if(list!=null&&list.size()>0){
							DataformatdefBVO insertBvo[]=list.toArray(new DataformatdefBVO[0]);
							String pk=HYPubBO_Client.insert(dataformatdefhvo);
							if(insertBvo!=null&&insertBvo.length>0){
								for(int i=0;i<insertBvo.length;i++){
									insertBvo[i].setPk_dataformatdef_h(pk);
								}	
								HYPubBO_Client.insertAry(insertBvo);
						    }
						}
						
				   }
				}
			}
			if(successspecialbvoMap!=null&&successspecialbvoMap.size()>0){//插入特殊页签格式
				String sysSideBussinessCode[]=IContrastUtil.getSysSideBussinessCode(hvo.getPk_xt()	,hvo.getMemorytable());
				String updatesql="update dip_datarec_specialtab set dr=1 where pk_datarec_h='"+hvo.getPk_datarec_h()+"' and nvl(dr,0)=0 ";
				 iq.exesql(updatesql);
				 Iterator<String> it=successspecialbvoMap.keySet().iterator();
				 while(it.hasNext()){
					 String specialFormat=it.next();
					 List<DipDatarecSpecialTab> list=successspecialbvoMap.get(specialFormat);
					 if(specialFormat.equals(DataformatPanel.SPECIAL_BACK)||specialFormat.equals(DataformatPanel.SPECIAL_XT)){
						 if(list!=null&&list.size()>0){
							 for(int i=0;i<list.size();i++){
								 if(list.get(i)!=null&&list.get(i).getValue()!=null&&list.get(i).getValue().trim().length()>0){
									 list.get(i).setValue(list.get(i).getValue().toUpperCase());
								 }
								 DipDatarecSpecialTab spcialtabvo=list.get(i);
								 if(spcialtabvo!=null&&1==spcialtabvo.getNodenumber()&&spcialtabvo.getValue()!=null&&sysSideBussinessCode[0].equals(spcialtabvo.getValue())){
									 list.get(i).setValue("");
								 }
								 if(spcialtabvo!=null&&2==spcialtabvo.getNodenumber()&&spcialtabvo.getValue()!=null&&sysSideBussinessCode[1].equals(spcialtabvo.getValue())){
									 list.get(i).setValue("");
								 }
								 if(spcialtabvo!=null&&3==spcialtabvo.getNodenumber()&&spcialtabvo.getValue()!=null&&sysSideBussinessCode[2].equals(spcialtabvo.getValue())){
									 list.get(i).setValue("");
								 }
							 }
							 HYPubBO_Client.insertAry(list.toArray(new DipDatarecSpecialTab[0]));
						 }
					 }else if(specialFormat.equals(DataformatPanel.SPECIAL_XML)){
						 if(list!=null&&list.size()>0){
							 HYPubBO_Client.insertAry(list.toArray(new DipDatarecSpecialTab[0]));
						 }
					 }
				 }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public MessagelogoVO[] getMessageLogo(String cdata,String ctype,String cname) throws UifException{
		MessagelogoVO[] vo=null;
		if(ctype==null&&cname==null){
			vo= (MessagelogoVO[]) HYPubBO_Client.queryByCondition(MessagelogoVO.class, "cdata='"+cdata+"' and nvl(dr,0)=0 ");	
		}else{
			vo= (MessagelogoVO[]) HYPubBO_Client.queryByCondition(MessagelogoVO.class, "cdata='"+cdata+"' and ctype='"+ctype+"' and cname='"+cname+"' and nvl(dr,0)=0 ");
		}
		 return vo;
	}
	public String[] getEsbSheetNames(String tranCon,String backCon){
		String[] str=null;
		List<String> listesb=new ArrayList<String>();
		String []selectNodeNeedSheetNames=getSourceTypeToSheet(pk_dataorigin);
		if(selectNodeNeedSheetNames!=null&&selectNodeNeedSheetNames.length==4){
			if(tranCon.equals("Y")&&backCon.equals("Y")){
				listesb.add(selectNodeNeedSheetNames[0]);
				listesb.add(selectNodeNeedSheetNames[1]);
				listesb.add(selectNodeNeedSheetNames[2]);
				listesb.add(selectNodeNeedSheetNames[3]);
			}
			if(tranCon.equals("Y")&&!backCon.equals("Y")){
				listesb.add(selectNodeNeedSheetNames[0]);
				listesb.add(selectNodeNeedSheetNames[1]);
				listesb.add(selectNodeNeedSheetNames[2]);
			}
			if(!tranCon.equals("Y")&&backCon.equals("Y")){
				listesb.add(selectNodeNeedSheetNames[1]);
				listesb.add(selectNodeNeedSheetNames[3]);
			}
			if(!tranCon.equals("Y")&&!backCon.equals("Y")){
				listesb.add(selectNodeNeedSheetNames[1]);
			}	
			listesb.add(DataformatPanel.RIGHT_GDGS);
			listesb.add(DataformatPanel.RIGHT_HZGS);
			if(listesb!=null&&listesb.size()>0){
				str=listesb.toArray(new String[0]);
			}
		}
		return str;
	}
	public void checkImportData(TxtDataVO[] txtDataVOs){
		if(txtDataVOs!=null&&txtDataVOs.length>1){
			for(int i=1;i<txtDataVOs.length;i++){
				TxtDataVO txtdatavo=txtDataVOs[i];
				if(txtdatavo!=null&&txtdatavo.getDatavo()!=null){
					if(true){
						
					}
				}
			}
		}
		
	}
	/**
	 * 验证数据项和类型是否正确，如果错误会返回相应的错误 message，否则就是正确的
	 * 
	 * @param bvo
	 * @param notNullLine
	 * @param txtdatavo
	 * @param bussinessMap
	 * @return
	 * @throws Exception
	 */
	public StringBuffer checkImportSequenceData(DataformatdefBVO bvo,String[] notNullLine,TxtDataVO txtdatavo,HashMap<String, DipDatadefinitBVO> bussinessMap )throws Exception{
		StringBuffer sb=new StringBuffer();
				if(bvo!=null){
						if(notNullLine!=null&&notNullLine.length>0){
						for(int k=0;k<notNullLine.length;k++){
							String notnullcode=notNullLine[k];
							Object notNullCodeValue=bvo.getAttributeValue(notnullcode);
							if(notNullCodeValue==null||notNullCodeValue.toString().trim().equals("")){
								HashMap map=txtdatavo.getCodeToNameMap();
								if(map!=null&&map.get(notnullcode)!=null){
									sb.append(map.get(notnullcode)+"字段不能为空");
									break;
								}
								
							}	
						}
					}
						if(sb!=null&&sb.toString().trim().length()<=0){
							if(bvo.getVdef2().equals(DataformatPanel.YWSJ)){
								DipDatadefinitBVO datadefinitbvo=bussinessMap.get(bvo.getDatakind().toUpperCase());
								
								if(datadefinitbvo==null){
									sb.append("业务表中不存在["+bvo.getDatakind()+"]字段");
								}else{
									if(!(datadefinitbvo.getCname()!=null&&bvo.getVdef3().equals(datadefinitbvo.getCname()))){
										sb.append("业务表中不存在英文名为["+bvo.getDatakind()+"],中文名为["+bvo.getVdef3()+"]的字段");	
									}else{
										bvo.setVdef3(datadefinitbvo.getCname());
										bvo.setVdef1(datadefinitbvo.getPk_datadefinit_b());
										successbvo.add(bvo);
									}
									
								}
							}else if(!bvo.getVdef2().equals(DataformatPanel.GDBZ)){
								MessagelogoVO messlogovo[]=getMessageLogo(bvo.getDatakind(),bvo.getVdef2(),bvo.getVdef3());
								if(messlogovo==null||messlogovo.length==0){
									if(bvo.getVdef2()!=null&&bvo.getVdef2().equals("自定义")){
										DipConsttabVO[] vos=(DipConsttabVO[]) HYPubBO_Client.queryByCondition(DipConsttabVO.class, "descs='"+bvo.getDatakind()+"' and name='"+bvo.getVdef3()+"'");
										if(vos!=null&&vos.length>0){
											bvo.setVdef3(vos[0].getName());
											bvo.setVdef1(vos[0].getPrimaryKey());
											successbvo.add(bvo);
										}else {
											sb.append("消息标志表中不存在名称是["+bvo.getVdef3()+"],类型是["+bvo.getVdef2()+"],数值是["+bvo.getDatakind()+"]的记录");
										}
									}else{
										sb.append("消息标志表中不存在名称是["+bvo.getVdef3()+"],类型是["+bvo.getVdef2()+"],数值是["+bvo.getDatakind()+"]的记录");
									}
								}else{
									bvo.setVdef3(messlogovo[0].getCname());
									successbvo.add(bvo);
								}
							}else if(bvo.getVdef2().equals(DataformatPanel.GDBZ)){
								String datakind=bvo.getDatakind();
								String vdef3=bvo.getVdef3();
								if(datakind.equals(vdef3)&&(datakind.equals(DataformatPanel.XTBZ)||datakind.equals(DataformatPanel.ZDBZ)||datakind.equals(DataformatPanel.YWBZ))){
									successbvo.add(bvo);	
								}else{
									sb.append("类型是固定标志，数据项和中文名称的值必须相等且只能是"+DataformatPanel.XTBZ+"、"+DataformatPanel.ZDBZ+"、"+DataformatPanel.YWBZ);
								}
								
							}
						}
						
					}
		return sb;
	}
	public String[] getSourceTypeToSheet(String sourcetype){
		String[] str=null;
//		if(sourcetype.equals("主动抓取")){
//			str=new String[]{"主动抓取"};
//		}else if(sourcetype.equals("中间表")){
//			str=new String[]{"中间表"};
//		}else if(sourcetype.equals("消息总线")){
//			str=new String[]{"传输开始标志","传输数据标志","传输结束标志","回执标志"};
//		}else if(sourcetype.equals("格式文件")){
//			str=new String[]{"格式文件"};
//		}else if(sourcetype.equals("webservice抓取")){
//			str=new String[]{"webservice抓取"};
//		}
		if(sourcetype.equals(IContrastUtil.DATAORIGIN_ZDZQ)){
			str=new String[]{DataformatPanel.MESSTYPE_ZDZQ};
		}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_ZJB)){
			str=new String[]{DataformatPanel.MESSTYPE_ZJB};
		}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
			str=new String[]{DataformatPanel.MESSTYPE_CSKS,DataformatPanel.MESSTYPE_CSSJ,DataformatPanel.MESSTYPE_CSJS,DataformatPanel.MESSTYPE_HZBZ};
		}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_GSWJ)){
			str=new String[]{DataformatPanel.MESSTYPE_GSWJ};
		}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)){
			str=new String[]{DataformatPanel.MESSTYPE_WEBSERVICEZQ};
		}
		return str;
	}
	/**
	 * 校验表头
	 * @param dataoriginname
	 * @param folwdef
	 * @param division
	 * @param filetype
	 * @param twoPoint
	 * @param filetypeint
	 * @param selectNodeNeedSheetNames
	 * @return
	 */
	public String getTitleCheck(String dataoriginname,String folwdef,String division,String filetype,String twoPoint,int filetypeint,String[] selectNodeNeedSheetNames,String mergestyle,String mergemark,String mergecount,String pagerunsys,String sendstyle,String delayed,Map<String,Integer> delayedMap){
		StringBuffer errSb=new StringBuffer();
		String lx[]=null;//对照方式
		if(dataoriginname==null||dataoriginname.trim().equals("")){
			errSb.append("数据来源类型不能为空");
			return errSb.toString();
		}
		if(getSourceTypeToSheet(pk_dataorigin)!=null){
			selectNodeNeedSheetNames=getSourceTypeToSheet(pk_dataorigin);
		}else{
			errSb.append("数据来源类型名称不能为"+dataoriginname);
			return errSb.toString();
		}
		if(!(folwdef.equals("顺序")||folwdef.equals("字段"))){
			errSb.append("对照方式只能是顺序或者字段");
			return errSb.toString();
		}
		if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX)){
			if(!folwdef.equals("顺序")){
				errSb.append("消息总线的对照方式只能是顺序");
				return errSb.toString();
			}
			if(division.trim().equals("")){
				errSb.append("消息总线的分割标记/根节点不能为空");
				return errSb.toString();
			}
			//mergestyle,mergemark,mergecount,pagerunsys,sendstyle,delayed
			if(mergestyle==null||mergestyle.equals("")){
				errSb.append("消息总线的合并类型不能为空");
				return errSb.toString();
			}else{
				if(mergestyle.equals("不合并")){
					mergestyle="0";
					if(!mergemark.trim().equals("")||!mergecount.trim().equals("")||!pagerunsys.trim().equals("")){
						errSb.append("消息总线的合并类型是不合并时，合并分隔符、记录合并数、分页参数必须都为空");
						return errSb.toString();
					}
				}else{
					if(mergestyle.equals("记录合并")){
						mergestyle="1";
						if(mergemark.trim().equals("")||mergecount.trim().equals("")||pagerunsys.trim().equals("")){
							errSb.append("消息总线的合并类型是记录合并时，合并分隔符、记录合并数、分页参数必须都为空");
							return errSb.toString();
						}
					}else if(mergestyle.equals("完整合并")){
						mergestyle="2";
						if(mergemark.trim().equals("")||mergecount.trim().equals("")||pagerunsys.trim().equals("")){
							errSb.append("消息总线的合并类型是完整合并时，合并分隔符、记录合并数、分页参数必须都为空");
							return errSb.toString();
						}
					}
					
				}
			}
			
			if(!(sendstyle.equals("空数据不发送")||sendstyle.equals("空数据发送"))){
				errSb.append("消息总线的发送类型只能是空数据不发送或者空数据发送");
				return errSb.toString();
			}else{
				if(sendstyle.equals("空数据不发送")){
					sendstyle="0";
				}else{
					sendstyle="1";
				}
			}
			
			if(delayed!=null&&!delayed.trim().equals("")){
				if(delayed.contains(",")){
					String split[]=delayed.split(",");
					for(int i=0;i<split.length;i++){
						String regex="\\d{1,9}";
						if(split[i].trim().equals("")){
							continue;
						}else {
							if(!split[i].matches(regex)){
							  errSb.append("消息总线的延时必须是【1,2,3,4】或【1,2,3】或【1,2】或【1】格式或为空");
							  return errSb.toString();	
							}
							String key="";
							if(i==0){
								key=DataformatPanel.MESSTYPE_CSKS_PK;
							}else if(i==1){
								key=DataformatPanel.MESSTYPE_CSSJ_PK;
							}else if(i==2){
								key=DataformatPanel.MESSTYPE_CSJS_PK;
							}else if(i==3){
								key=DataformatPanel.MESSTYPE_HZBZ_PK;
							}else{
								continue;
							}
							delayedMap.put(key, Integer.parseInt(split[i]));
						}
					}
				}else{
					if(!delayed.matches("\\d{1,9}")){
						errSb.append("消息总线的延时必须是【1,2,3,4】或【1,2,3】或【1,2】或【1】格式或为空");
						return errSb.toString();
					}
					delayedMap.put(DataformatPanel.MESSTYPE_CSKS_PK, Integer.parseInt(delayed));
				}
			}
			
//			if(!filetype.equals("")||!twoPoint.equals("")){
//				errSb.append("消息总线的文件类型和二级节点必须为空");
//				return errSb.toString();
//			}
//			if(!filetype.equals("")){
//				errSb.append("消息总线的文件类型必须为空");
//				return errSb.toString();
//			}
		}else if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_GSWJ)){
			if(filetype.equals("txt")){
				filetypeint=0;
				if(division.trim().equals("")){
					errSb.append("格式文件是txt的分割标记不能为空");
					return errSb.toString();
				}
			}else if(filetype.equals("xml")){
				filetypeint=1;
//				if(division.trim().equals("")||twoPoint.trim().equals("")){
//					errSb.append("格式文件是xml的分割标记/根节点和二级节点不能为空");
//					return errSb.toString();
//				}
			}else if(filetype.equals("excel")){
				filetypeint=2;
//				if(!division.trim().equals("")||!twoPoint.trim().equals("")){
					if(!division.trim().equals("")){
					errSb.append("格式文件是excel的分割标记必须为空");
					return errSb.toString();
				}
				
			}else{
				errSb.append("文件类型只能是txt、xml、excel");
				return errSb.toString();
			}
			lx=DataformatPanel.MESSTYPE_GSWJ_LX;
		}else if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_ZDZQ)){
//			if(!division.equals("")||!filetype.equals("")||!twoPoint.equals("")){
//				errSb.append("主动抓取和中间表的分割标志、文件类型、二级节点必须为空");
//				return errSb.toString();
//			}
			if(!division.equals("")||!filetype.equals("")){
				errSb.append("主动抓取和中间表的分割标志、文件类型必须为空");
				return errSb.toString();
			}
			lx=DataformatPanel.MESSTYPE_ZDZQ_LX;
		}else if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_ZJB)){
			if(!division.equals("")||!filetype.equals("")){
				errSb.append("主动抓取和中间表的分割标志、文件类型必须为空");
				return errSb.toString();
			}
			lx=DataformatPanel.MESSTYPE_ZJB_LX;
		}else if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)){
			lx=DataformatPanel.MESSTYPE_WEBSERVICEZQ_LX;
		}else{
			errSb.append("数据来源类型不能为"+dataoriginname);
			return errSb.toString();
		}
		
		if(lx!=null){
			boolean isright=false;
			String ss="";
			for(int i=0;i<lx.length;i++){
			  if(folwdef.equals(lx[i])){
				  isright=true;
				  ss+=lx[i]+"、";
			  }	
			}
			if(!isright){
				errSb.append("数据来源类型是"+dataoriginname+"的，对照方式只能是["+ss+"]");
				return errSb.toString();
			}
		}
		
		
		return errSb.toString();
	}
	@Override
	protected void onBoExport() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能该操作！"); 
			return;
		}
		DipDatarecHVO hvo=null;
		if(tempNode.getData() instanceof DipDatarecHVO){
			hvo=(DipDatarecHVO) tempNode.getData();
		}
		if(!(hvo!=null&&!hvo.getIsfolder().booleanValue())){
			getSelfUI().showWarningMessage("选择节点不能该操作！");
			return;
		}
		String[] sheetNames=null;
		String[] fileNames=null;
	    List<RowDataVO []> list= new ArrayList<RowDataVO[]>();
	    String filePath=getExpFilePath();
	    if(filePath!=null&&filePath.trim().equals("")){
	    	return;
	    }
	    String pk_datarec_h=hvo.getPk_datarec_h();
	    String pk_dataorigin=hvo.getSourcetype();//数据来源类型主键sourcetype
	    DipDataoriginVO vo=(DipDataoriginVO) HYPubBO_Client.queryByPrimaryKey(DipDataoriginVO.class, pk_dataorigin);
	    String dataoriginname=vo.getName();//数据来源类型名称
	    String bussinesstable=hvo.getDataname();//业务表
	    String division="";//分割标志/分割标志
	    String folwdef="";//对照方式
	    String filetype="";//文件类型
	    String twoPoint="";//二级节点
	    
	    //liyunzhe add 数据格式增加新的参数。
//	    esb增加字段
//	    系统标志 接口平台格式，自定义格式
//	    String systemflag="";
//	    合并类型 不合并，记录合并，完全合并
	    String mergestyle="";
//	    合并分隔符
	    String mergemark="";
//	    记录合并数
	    String mergecount="";
//	    分页参数
	    String pagerunsys="";
//	    发送类型 空数据不发送,空数据发送
	    String sendstyle="空数据不发送";
//	    延时（秒）
	    String delayed="";
	    //liyunzhe add 数据格式增加新的参数。
	    
	    
	    DataformatdefHVO[] dataFormatdefHvos=(DataformatdefHVO[]) HYPubBO_Client.queryByCondition(DataformatdefHVO.class, " pk_datarec_h='"+pk_datarec_h +"' and nvl(dr,0)=0 order by messflowtype");
	    RowDataVO[] rowDataVOTail=new RowDataVO[1];
	    if(dataFormatdefHvos!=null&&dataFormatdefHvos.length>0){
	    	sheetNames=new String[dataFormatdefHvos.length+1];
    		fileNames=new String[dataFormatdefHvos.length+1];
	    	for(int i=0;i<dataFormatdefHvos.length;i++){
	    		String sheetname="";
	    		RowDataVO[] rowDataVO=null;
	    		DataformatdefHVO dataFormatHvo=dataFormatdefHvos[i];
	    		String messflowtypekey= dataFormatHvo.getMessflowtype();//sheetname 的key
    			if(messflowtypekey!=null){
    				RetMessage ret=getSheetName(messflowtypekey);
    				if(ret.getIssucc()){
    					sheetname=ret.getMessage();
    				}else{
    				 showErrorMessage(ret.getMessage());
    				 return;
    				}
    				
    			}
	    		if(i==0){//得到第一个页签信息
	    			
		    		String messflowdef=dataFormatHvo.getMessflowdef();//顺序
		    		if(messflowdef!=null){
		    			if(messflowdef.equals("0")){
		    				folwdef="顺序";
		    			}else if(messflowdef.equals("1")){
		    				folwdef="字段";
		    			}
		    		}
		    		String divisionKey=dataFormatHvo.getBeginsign();//分割标记
		    		if(divisionKey!=null&&!divisionKey.trim().equals("")){
		    			MessagelogoVO logovo=(MessagelogoVO) HYPubBO_Client.queryByPrimaryKey(MessagelogoVO.class, divisionKey);
			    		if(logovo!=null&&logovo.getCdata()!=null){
			    			division=logovo.getCdata();
			    		}
		    		}
		    		String twoPointKey=dataFormatHvo.getEndsign();//二级节点标志；
		    		if(twoPointKey!=null&&!twoPoint.trim().equals("")){
		    			MessagelogoVO logovo=(MessagelogoVO) HYPubBO_Client.queryByPrimaryKey(MessagelogoVO.class, twoPointKey);
			    		if(logovo!=null&&logovo.getCdata()!=null){
			    			twoPoint=logovo.getCdata();
			    		}
		    		}
		    		int type=dataFormatHvo.getFiletype();
		    		if(type==0){
		    			filetype="txt";
		    		}else if(type==1){
		    			filetype="xml";
		    		}else if(type==2){
		    			filetype="excel";
		    		}
		    		//"数据来源类型,业务表,对照方式,分割标志";
		    		rowDataVOTail[0]=new RowDataVO();
		    		rowDataVOTail[0].setAttributeValue("0", dataoriginname);
		    		rowDataVOTail[0].setAttributeValue("1", bussinesstable);
		    		rowDataVOTail[0].setAttributeValue("2", folwdef);
		    		rowDataVOTail[0].setAttributeValue("3", division);
		    		if(pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX)){
		    			mergestyle=dataFormatHvo.getMergestyle()==null?"":dataFormatHvo.getMergestyle();
		    			if(mergestyle!=null&&mergestyle.equals("1")){
							mergestyle="记录合并";
						}else if(mergestyle!=null&&mergestyle.equals("2")){
							mergestyle="完整合并";
						}else{
							mergestyle="不合并";
						}
		    			
		    			mergemark=dataFormatHvo.getMergemark()==null?"":dataFormatHvo.getMergemark();
		    			if(mergemark!=null&&!mergemark.trim().equals("")){
		    				MessagelogoVO logovo=(MessagelogoVO) HYPubBO_Client.queryByPrimaryKey(MessagelogoVO.class, mergemark);
				    		if(logovo!=null&&logovo.getCdata()!=null){
				    			mergemark=logovo.getCdata();
				    		}
		    			}
		    			mergecount=dataFormatHvo.getMergecount()==null?"":dataFormatHvo.getMergecount()+"";
		    			pagerunsys=dataFormatHvo.getPagerunsys()==null?"":dataFormatHvo.getPagerunsys()+"";
		    			if(dataFormatHvo.getSendstyle()!=null&&dataFormatHvo.getSendstyle().equals("1")){
		    				sendstyle="空数据发送";
		    			}
//		    			sendstyle=dataFormatHvo.getSendstyle();
		    			rowDataVOTail[0].setAttributeValue("4", mergestyle);
		    			rowDataVOTail[0].setAttributeValue("5", mergemark);
		    			rowDataVOTail[0].setAttributeValue("6", mergecount);
		    			rowDataVOTail[0].setAttributeValue("7", pagerunsys);
		    			rowDataVOTail[0].setAttributeValue("8", sendstyle);
		    			
		    		}else{
		    			rowDataVOTail[0].setAttributeValue("4", filetype);
		    		}
//		    		rowDataVOTail[0].setAttributeValue("5", twoPoint);
		    		sheetNames[0]=new String("业务表信息");
		    		list.add(0, rowDataVOTail);
	    		}
	    		
	    		DataformatdefBVO bvos[]=null;
	    		bvos=(DataformatdefBVO[]) HYPubBO_Client.queryByCondition(DataformatdefBVO.class, " pk_dataformatdef_h='"+dataFormatHvo.getPk_dataformatdef_h()+"' and nvl(dr,0)=0 order by code");
	    		if(bvos!=null&&bvos.length>0){
	    			rowDataVO=new RowDataVO[bvos.length];
	    			for(int w=0;w<bvos.length;w++){
	    				DataformatdefBVO bvo=bvos[w];
	    				rowDataVO[w]=new RowDataVO();
	    				rowDataVO[w].setAttributeValue("0", bvo.getCode());//code 编码
	    				rowDataVO[w].setAttributeValue("1", bvo.getDatakind()==null?"":bvo.getDatakind());//datakind 数据项
	    				rowDataVO[w].setAttributeValue("2", bvo.getVdef3()==null?"":bvo.getVdef3());//vdef3 中文名称
	    				rowDataVO[w].setAttributeValue("3", bvo.getVdef2()==null?"":bvo.getVdef2());//vdef2 类型
	    				rowDataVO[w].setAttributeValue("4", bvo.getCorreskind()==null?"":bvo.getCorreskind().toString());//correskind 对应项
	    			}
	    		}
	    		
	    		if(sheetname!=null&&!sheetname.equals("")&&rowDataVO!=null){
	    			sheetNames[i+1]=new String(sheetname);
	    			list.add(i+1, rowDataVO);
	    		}
	    		delayed=delayed+(dataFormatHvo.getDelayed()==null?",":dataFormatHvo.getDelayed()+",");
	    		if(i==dataFormatdefHvos.length-1){
	    			if(delayed!=null&&delayed.length()>0){
	    				delayed=delayed.substring(0, delayed.length()-1);
	    			}
	    			rowDataVOTail[0].setAttributeValue("9", delayed);
//	    			list.add(0, rowDataVOTail);
	    		}
	    	}
	    	 fileNames=getFileNames(dataFormatdefHvos.length+1,true,pk_dataorigin);
	    	 //liyunzhe add 增加特殊格式（固定格式，回执格式，xml格式，）；2012-06-08
	    	 List<String> sheetlist=new ArrayList<String>();
	    	 List<String> filelist=new ArrayList<String>();
	    	 for(int i=0;i<sheetNames.length;i++){
	    		 sheetlist.add(sheetNames[i]);
	    	 }
	    	 for(int i=0;i<fileNames.length;i++){
	    		 filelist.add(fileNames[i]);
	    	 }
	    	 getSpecialSheet(hvo,sheetlist,filelist,list,filetype);
	    	 
	    	 ExpToExcel1 excel=new ExpToExcel1(filePath,sheetlist.toArray(new String[0]),filelist.toArray(new String[0]),list,null);
	 		 if(excel.create()){
	 			 getSelfUI().showWarningMessage("导出成功");
	 		 }else{
	 			getSelfUI().showWarningMessage("导出失败");
	 		 }
	    }else{
	    	getSelfUI().showErrorMessage("选择节点没有定义数据格式");
	    }
	}
	/**
	 * 组织导出去的特殊页签数据，【固定格式、回执格式、xml格式】 liyunzhe 2012-06-08 add
	 * @param hvo
	 * @param sheetlist
	 * @param filelist
	 * @param list
	 * @param filetype
	 */
	public void getSpecialSheet(DipDatarecHVO hvo,List<String> sheetlist,List<String> filelist,List<RowDataVO[]> list,String filetype){
		
		String[] specialSheetnames=new String[2];
		String specialfilenames[]=new String[2];
    	Map<String, DipDatarecSpecialTab[]> map=getSpecialSheetValue(hvo.getPk_datarec_h(), hvo.getMemorytable(), hvo.getPk_xt());
    	boolean flag=false;
    	 if(map.size()>0){
    		 String gdgs=getSpecialFieldsname(FIX_FORMAT, true);
    		 String xmlgs=getSpecialFieldsname(XML_FORMAT, true);
    		 RowDataVO[] rowData=new RowDataVO[3];
    		 if(hvo.getSourcetype().equals(IContrastUtil.DATAORIGIN_XXZX)){//消息总线格式有 固定格式和回执格式页签
    			 specialSheetnames[0]=new String(DataformatPanel.RIGHT_GDGS);
    			 specialfilenames[0]=new String();
    			 specialfilenames[0]=gdgs;
    			 DipDatarecSpecialTab specialtabXT[]=map.get(DataformatPanel.SPECIAL_XT);
    			 if(specialtabXT!=null&&specialtabXT.length==3){
    				 for(int i=0;i<specialtabXT.length;i++){
    					 rowData[i]=new RowDataVO();
    					 rowData[i].setAttributeValue("0", specialtabXT[i].getNodenumber());
    					 rowData[i].setAttributeValue("1", specialtabXT[i].getName());
    					 rowData[i].setAttributeValue("2", specialtabXT[i].getValue()==null?"":specialtabXT[i].getValue());
    				 }
    			 }
    			 list.add(rowData);
    			 specialSheetnames[1]=new String(DataformatPanel.RIGHT_HZGS);
    			 specialfilenames[1]=new String();
    			 specialfilenames[1]=gdgs;
    			 rowData=new RowDataVO[3];
    			 DipDatarecSpecialTab specialtabBACK[]=map.get(DataformatPanel.SPECIAL_BACK);
    			 if(specialtabBACK!=null&&specialtabBACK.length==3){
    				 for(int i=0;i<specialtabBACK.length;i++){
    					 rowData[i]=new RowDataVO();
    					 rowData[i].setAttributeValue("0", specialtabBACK[i].getNodenumber());
    					 rowData[i].setAttributeValue("1", specialtabBACK[i].getName());
    					 rowData[i].setAttributeValue("2", specialtabBACK[i].getValue()==null?"":specialtabBACK[i].getValue());
    				 }
    			 }    
    			 list.add(rowData);
    			 flag=true;
    		 }
    		 
    		 if(hvo.getSourcetype().equals(IContrastUtil.DATAORIGIN_GSWJ)){//格式文件格式有 固定格式和xml格式页签
    			 specialSheetnames[0]=new String(DataformatPanel.RIGHT_GDGS);
    			 specialfilenames[0]=new String();
    			 specialfilenames[0]=gdgs;
    			 DipDatarecSpecialTab specialtabXT[]=map.get(DataformatPanel.SPECIAL_XT);
    			 if(specialtabXT!=null&&specialtabXT.length==3){
    				 for(int i=0;i<specialtabXT.length;i++){
    					 rowData[i]=new RowDataVO();
    					 rowData[i].setAttributeValue("0", specialtabXT[i].getNodenumber());
    					 rowData[i].setAttributeValue("1", specialtabXT[i].getName());
    					 rowData[i].setAttributeValue("2", specialtabXT[i].getValue()==null?"":specialtabXT[i].getValue());
    				 }
    				 
    			 }
    			 list.add(rowData);
    			 if(filetype.equals(DataformatPanel.SPECIAL_XML)){
        			 specialSheetnames[1]=new String(DataformatPanel.RIGHT_XMLGS);
        			 specialfilenames[1]=new String();
        			 specialfilenames[1]=xmlgs;
        			 rowData=new RowDataVO[3];
        			 DipDatarecSpecialTab specialtabXML[]=map.get(DataformatPanel.SPECIAL_XML);
        			 if(specialtabXML!=null&&specialtabXML.length==3){
        				 for(int i=0;i<specialtabXML.length;i++){
        					 rowData[i]=new RowDataVO();
        					 rowData[i].setAttributeValue("0", specialtabXML[i].getNodenumber());
        					 rowData[i].setAttributeValue("1", specialtabXML[i].getNodename());
        					 rowData[i].setAttributeValue("2", specialtabXML[i].getValue()==null?"":specialtabXML[i].getValue());
        					 rowData[i].setAttributeValue("3", specialtabXML[i].getNodeproperty()==null?"":specialtabXML[i].getNodeproperty());
        				 }
        			 }    
        			 list.add(rowData);
    			 }
    			
    			 flag=true;
    		 }
    		 if(flag){
    			 for(int i=0;i<specialSheetnames.length;i++){
    				 if(specialSheetnames[i]!=null&&specialSheetnames[i].trim().length()>0){
    					 sheetlist.add(specialSheetnames[i]);
    				 }
    			 }
    			 for(int i=0;i<specialfilenames.length;i++){
    				 if(specialfilenames[i]!=null&&specialfilenames[i].trim().length()>0){
    					 filelist.add(specialfilenames[i]);
    				 }
    			 }
    		 }
    		 
    	 }
	}
	/**
	 * 固定格式和回执格式编码名称
	 */
	public static String FIX_FORMAT="gdgs";
	/**
	 * xml格式编码名称
	 */
	public static String XML_FORMAT="xmlgs";
	public String getSpecialFieldsname(String str,boolean flag){
		 String fields=null;
		 if(str!=null){
			 if(str.equals(FIX_FORMAT)){
				 fields=new String("编码,名称,对应值");
				 if(!flag){
					 fields=fields+",错误";
				 }
			 }else if(str.equals(XML_FORMAT)){
			  fields=new String("编码,节点名称,对应值,节点属性");
			  	 if(!flag){
					 fields=fields+",错误";
				 }
			 }
		 }
		 return fields;
	}
	public String getSpecialFieldscode(String str,boolean flag){
		 String fields=null;
		 if(str!=null){
			 if(str.equals(FIX_FORMAT)){
			  fields=new String("nodenumber,name,value");	 
			 }else if(str.equals(XML_FORMAT)){
			  fields=new String("nodenumber,nodename,value,nodeproperty");
			 }
		 }
		 return fields;
	}
	public Map<String, DipDatarecSpecialTab[]> getSpecialSheetValue(String pk_datarec_h,String pk_datadefint_h,String pk_xt){
		DipDatarecSpecialTab[] specialXT=null;
		Map<String, DipDatarecSpecialTab[]> map=new HashMap<String, DipDatarecSpecialTab[]>();
		DipDatarecSpecialTab[] specialBack=null;
		DipDatarecSpecialTab[] specialXml=null;
		try {
			specialXT = (DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+pk_datarec_h+"' and nvl(is_xtfixed,'N')='Y' and nvl(dr,0)=0 order by  nodenumber");
			specialBack=(DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+pk_datarec_h+"' and nvl(is_back,'N')='Y' and nvl(dr,0)=0 order by  nodenumber");
			specialXml=(DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+pk_datarec_h+"' and nvl(is_xml,'N')='Y' and nvl(dr,0)=0 order by  nodenumber");
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] syscode=IContrastUtil.getSysSideBussinessCode(pk_xt, pk_datadefint_h);
		if(specialXT!=null&&specialXT.length==3){
			for(int i=0;i<specialXT.length;i++){
				if(specialXT[i].getValue()==null||specialXT[i].getValue().equals("")){
					specialXT[i].setValue(syscode[i]);
				}
			}
			map.put(DataformatPanel.SPECIAL_XT, specialXT);
		}
		if(specialBack!=null&&specialBack.length>1){
			for(int i=0;i<specialBack.length;i++){
				if(specialBack[i].getValue()==null||specialBack[i].getValue().equals("")){
					specialBack[i].setValue(syscode[i]);
				}
			}
			map.put(DataformatPanel.SPECIAL_BACK, specialBack);
		}
		if(specialXml!=null&&specialXml.length>1){
			map.put(DataformatPanel.SPECIAL_XML, specialXml);
		}
		return map;
	}
	
//	new String[]{"传输开始标志","传输数据标志","传输结束标志","回执标志"};
//	new String[]{"0001AA100000000142YQ","0001AA100000000142YS","0001ZZ10000000018K7M","0001ZZ1000000001GFD7"};
//	--"格式文件"};0001AA1000000001HYWP
//	--"主动抓取 "0001AA1000000001HYWO"};
//	--中间表 0001AA1000000001HYWQ
	/**
	 * 通过消息标志名称或者主键来得到消息标志主键或者名称
	 * 格式定义中，通过中间表可以得到0001AA1000000001HYWQ，通过0001AA1000000001HYWQ可以得到中间表。
	 */
	public RetMessage getSheetName(String sheetName){
		String retursheetName=null;
		RetMessage ret=DataformatPanel.getStaticValue();
		if(!ret.getIssucc()){
			return ret;
		}
//		if(sheetName!=null&&sheetNames.length>0){
//			retursheetNames=new String[sheetNames.length];
//			for(int i=0;i<sheetNames.length;i++){
//				String sheetName=sheetNames[i];
				if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_CSKS_PK)){
					retursheetName=DataformatPanel.MESSTYPE_CSKS;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_CSKS)){
					retursheetName=DataformatPanel.MESSTYPE_CSKS_PK;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_CSSJ_PK)){
					retursheetName=DataformatPanel.MESSTYPE_CSSJ;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_CSSJ)){
					retursheetName=DataformatPanel.MESSTYPE_CSSJ_PK;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_CSJS_PK)){
					retursheetName=DataformatPanel.MESSTYPE_CSJS;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_CSJS)){
					retursheetName=DataformatPanel.MESSTYPE_CSJS_PK;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_HZBZ_PK)){
					retursheetName=DataformatPanel.MESSTYPE_HZBZ;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_HZBZ)){
					retursheetName=DataformatPanel.MESSTYPE_HZBZ_PK;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_GSWJ_PK)){
					retursheetName=DataformatPanel.MESSTYPE_GSWJ;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_GSWJ)){
					retursheetName=DataformatPanel.MESSTYPE_GSWJ_PK;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_ZDZQ_PK)){
					retursheetName=DataformatPanel.MESSTYPE_ZDZQ;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_ZDZQ)){
					retursheetName=DataformatPanel.MESSTYPE_ZDZQ_PK;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_ZJB_PK)){
					retursheetName=DataformatPanel.MESSTYPE_ZJB;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_ZJB)){
					retursheetName=DataformatPanel.MESSTYPE_ZJB_PK;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_WEBSERVICEZQ_PK)){
					retursheetName=DataformatPanel.MESSTYPE_WEBSERVICEZQ;
				}else if(sheetName!=null&&sheetName.equals(DataformatPanel.MESSTYPE_WEBSERVICEZQ)){
					retursheetName=DataformatPanel.MESSTYPE_WEBSERVICEZQ_PK;
				}
//			}
		//}
		return new RetMessage(true,retursheetName);
	}
	/**
	 * 
	 * @param count
	 * @param flag
	 * @param pk_dataorigin 数据来源类型
	 * @return
	 */
	public String[] getFileNames(int count,boolean flag,String pk_dataorigin){
		String fileNames[]=null;
		if(count<=1){
			return null;
		}else{
			String fileName1="数据来源类型,业务表,对照方式,分割标志,文件类型";
			String fileName2="编码,数据项,中文名称,类型,对应项";
			String esbstyle="数据来源类型,业务表,对照方式,分割标志,合并类型,合并分隔符,记录合并数,分页参数,发送类型,延时（秒）";
			               //sourcetype,dataname,messflowdef,beginsign,mergestyle,mergemark,mergecount,pagerunsys,sendstyle,delayed
			if(!flag){
				fileName2=fileName2+",错误";
			}
			fileNames=new String[count];
			for(int i=0;i<count;i++){
				if(i==0){
					if(pk_dataorigin!=null&&pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX)){//消息总线
						fileNames[0]=esbstyle;
					}else{
						fileNames[0]=fileName1;
					}
				}else{
					fileNames[i]=fileName2;
				}
			}
		}
		
		return fileNames;
	}
	/**
	 * 
	 * @param count          第几个页签
	 * @param pk_dataorigin
	 * @return
	 */
	public String[] getFileCodes(int count,String pk_dataorigin){
		String fileCodes[]=null;
		if(count<=1){
			return null;
		}else{
			String fileName1="sourcetype,dataname,messflowdef,beginsign,filetype";
			String fileName2="code,datakind,vdef3,vdef2,correskind";
			String esbcode="sourcetype,dataname,messflowdef,beginsign,mergestyle,mergemark,mergecount,pagerunsys,sendstyle,delayed";
			fileCodes=new String[count];
			for(int i=0;i<count;i++){
				if(i==0){
					if(pk_dataorigin!=null&&pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX)){
						fileCodes[0]=esbcode;
					}else{
						fileCodes[0]=fileName1;
					}
				}else{
					fileCodes[i]=fileName2;
				}
			}
		}
		
		return fileCodes;
	}
	
	public String getExpFilePath(){
		String file="";
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		if(jfileChooser.showSaveDialog(this.getSelfUI()) == javax.swing.JFileChooser.CANCEL_OPTION){
			 file="";
		}else{
			 file=jfileChooser.getSelectedFile().toString();
			if(!file.endsWith(".xls")){
				file=file+".xls";
			}
			File jfile=new File(file);
			if(jfile.exists()){
				if(2==MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", file+"文件已经存在，是否覆盖")){
					 file="";
				}
			}
	    }
		return file;
	}
	
	public String getImpFilePath(){
		String file="";
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		if(jfileChooser.showSaveDialog(this.getSelfUI()) == javax.swing.JFileChooser.CANCEL_OPTION){
			 file="";
		}else{
			 file=jfileChooser.getSelectedFile().toString();
			if(!file.endsWith(".xls")){
				file=file+".xls";
			}
		}
		return file;
	}
	 public TxtDataVO[] getTxtDataVO(String path,String expTitleName[],String impTitleCode[],Class classvo[]){
		 File file=new File(path);
		 if(!file.exists()){
			 showErrorMessage(path+"文件不存在");
			 return null;
		 }
		 TxtDataVO txtDataVos[]=null;
			FileInputStream fin=null;
			HSSFWorkbook book=null;
			try{
				fin=new FileInputStream(path);
				book = new HSSFWorkbook(fin);
				int count=book.getNumberOfSheets();
				txtDataVos=new TxtDataVO[count];
				for(int c=0;c<count;c++){
					 TxtDataVO tvo=new TxtDataVO();
					 SuperVO supervo=null;
					HSSFSheet sheet = book.getSheetAt(c);
					if(sheet == null){
						showErrorMessage("导入文件格式不正确！");
						return null;
					}
					String sheetName=book.getSheetName(c);
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
					String codes[]=null;
					String names[]=null;
					if(c==0){
						codes=impTitleCode[0].split(",");
						names=expTitleName[0].split(",");//导出name
					}else{
						
						if((pk_dataorigin.equals(IContrastUtil.DATAORIGIN_GSWJ)||pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX))&&(c==count-1||c==count-2)&&!sheetName.equals(DataformatPanel.MESSTYPE_GSWJ)){
							//添加特殊页签
							if(c==count-1){
								codes=impTitleCode[impTitleCode.length-1].split(",");
								names=expTitleName[expTitleName.length-1].split(",");
							}
							if(c==count-2){
								codes=impTitleCode[impTitleCode.length-2].split(",");
								names=expTitleName[expTitleName.length-2].split(",");
							}
							if(sheetName.equals(DataformatPanel.RIGHT_GDGS)){
								codes=impTitleCode[impTitleCode.length-2].split(",");
								names=expTitleName[expTitleName.length-2].split(",");
							}
							
							
						}else{
							codes=impTitleCode[1].split(",");
							names=expTitleName[1].split(",");//导出name
						}
						
							
					}
					tvo.setErrorTitle(names+",错误");	
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
						showErrorMessage("["+sheetName+"]页签，导入excel字段长度和导出字段长度不相等！");
						return null;
					}
					if(titlemap!=null&&titlemap.size()>0&&codes!=null&&codes.length==titlemap.size()){
						for(int i=0;i<titlemap.size();i++){
							String impName="";
							if(titlemap.get(i)==null){
							   impName=titlemap.get(i+"");//导入name
							  if(impName==null||impName.trim().equals("")){
								  getSelfUI().showErrorMessage("["+sheetName+"]页签导入字段名称不能为空！");
								  return null;
							  }
							  if(names.length<i){
								  showErrorMessage("["+sheetName+"]页签，导出excel字段错误！");
								  return null;
							  }else{
								 if(!(names[i]!=null&&names[i].equals(impName))){
									 showErrorMessage("["+sheetName+"]页签，第"+(i+1)+"个导入字段名["+impName+"]和导出字段名["+names[i]+"]不相同！");
									 return null;
								 }
							  }
							}
						 	if(nameToCode!=null){
						 		if(!impName.equals("")&&nameToCode.get(impName)!=null&&!nameToCode.get(impName).trim().equals("")){
						 			lineToCode.put(i+"", nameToCode.get(impName));
						 		}else{
						 			showErrorMessage("["+sheetName+"]页签，导入字段["+impName+"]没有找到对应编码！");
						 			return null;
						 		}
						 	}else{
						 		showErrorMessage("["+sheetName+"]页签，导入字段没有找到对应编码！");
						 		return null;
						 	}
							
						}
					}else{
						showErrorMessage("["+sheetName+"]页签，导入excel字段长度和导出字段长度不相等！");
						return null;
					}
					tvo.setLinetocodeMap(lineToCode);
					tvo.setNametocodeMap(nameToCode);
					tvo.setCodeToNameMap(codeToName);

					for(int i=1;i<=tvo.getRowCount();i++){
						HSSFRow row = sheet.getRow(tvo.getFirstDataRow() + i);
						if(c==0){
							supervo=(SuperVO) Class.forName(classvo[0].getName()).newInstance();
					    }else{
					    	if((pk_dataorigin.equals(IContrastUtil.DATAORIGIN_GSWJ)||pk_dataorigin.equals(IContrastUtil.DATAORIGIN_XXZX))&&(c==count-1||c==count-2)&&!sheetName.equals(DataformatPanel.MESSTYPE_GSWJ)){
								//添加特殊页签
										supervo=(SuperVO) Class.forName(classvo[classvo.length-1].getName()).newInstance();
							}else{
								     supervo=(SuperVO) Class.forName(classvo[1].getName()).newInstance();	
							}
					    }
//						SuperVO supervo=null;
//						if(c==){}
						
						for(short j=0;j<titleRow.getPhysicalNumberOfCells();j++){
							tvo.setCellData(i-1, j, SJUtil.getCellValue(row.getCell(j)));
							if(lineToCode.get(j+"")!=null){
								String field=lineToCode.get(j+"");
								Class<Field> fiel=null;
								String ss="";
								try{
								 ss=SJUtil.getCellValue(row.getCell(j))==null?"":SJUtil.getCellValue(row.getCell(j)).toString();
								 fiel=Class.forName(supervo.getClass().getName()).getDeclaredField(field)==null?null:(Class<Field>) Class.forName(supervo.getClass().getName()).getDeclaredField(field).getType();
//								 if(field!=null&&field.equals("datakind")){
//									 ss=ss.trim().toUpperCase();
//								 }
//									if(checkline!=null&&checkline.length>0){
//										for(int c=0;c<checkline.length;c++){
//											if(c==j&&ss.trim().equals("")){
//											}
//										}
//									}
								}catch(java.lang.NoSuchFieldException e){
									fiel=null;
								}
								if(fiel==null||fiel.toString().equals("class java.lang.String")){
									supervo.setAttributeValue(field, ss);	
								}else if(fiel.toString().equals("class java.lang.Integer")||fiel.toString().equals("int")){
								
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
								showErrorMessage("["+sheetName+"]页签，第"+j+"列，没有找到对应的编码字段！");
								return null;
							}
						}
						datavos.add(i-1, supervo);
					}
					if(datavos!=null){
						tvo.setDatavo(datavos);
					}
					//txtDataVos[c]=new TxtDataVO();
					txtDataVos[c]=tvo;
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
			return txtDataVos;
		}
	private void showErrorMessage(String str){
		getSelfUI().showErrorMessage(str);
	}
}