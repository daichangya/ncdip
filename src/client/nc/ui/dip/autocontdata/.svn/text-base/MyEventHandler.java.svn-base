package nc.ui.dip.autocontdata;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.text.Document;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.ITaskExecute;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.ddc.IBizObjStorage;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.ref.DataDefinitRefModel;
import nc.ui.bd.ref.DataDefinitbRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.warningset.WarningsetDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.ddc.datadict.DDCBO_Client;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.contdata.ContdataB2VO;
import nc.vo.dip.contdata.ContdataBVO;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.contdata.MyBillVO;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.core.BizObject;
import nc.vo.pub.core.FolderNode;
import nc.vo.pub.core.FolderObject;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.ddc.datadict.DDCData;
import nc.vo.pub.ddc.datadict.Datadict;
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
	MyBillVO tempMybillVO=null;
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	ContDataClientUI ui = (ContDataClientUI)this.getBillUI();
	public MyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}
	public static String extcode;
	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private ContDataClientUI getSelfUI() {
		return (ContDataClientUI) getBillUI();
	}
	/**
	 * 取得选中的节点对象
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}


	@Override
	protected void onBoEdit() throws Exception {

		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		String fpk=(String)treeNode.getParentnodeID();
		if(fpk ==null || fpk.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做修改操作！"); 
			return;
		}
		DipContdataVO temphvo=(DipContdataVO) treeNode.getData();
		if(temphvo.getIsfolder()!=null&&temphvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("请选择定义的节点进行修改操作！");
			return;
		}
		super.onBoEdit();
		ContDataClientUI ui=(ContDataClientUI) getBillUI();
		//得到界面上主表的主键
		String pk=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_contdata_h").getValueObject();
		//如果主键是空的，那么就是新增，否则判断数据库中是否有这条主键的数据，如果没有的话就是新增，有的话，就是修改
		DipContdataVO vo=null;
		if(pk==null||pk.trim().equals("")){
		}else{
			vo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, pk);
			if(SJUtil.isNull(vo.getDef_str_1())||!vo.getDef_str_1().equals("Y")){
				getSelfUI().getBillCardPanel().getHeadItem("def_str_2").setEdit(false);
				getSelfUI().getBillCardPanel().getHeadItem("def_str_3").setEdit(false);
			}else{
				getSelfUI().getBillCardPanel().getHeadItem("def_str_2").setEdit(true);
				getSelfUI().getBillCardPanel().getHeadItem("def_str_3").setEdit(true);
			}
		}

		UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
		String ss=jcp.getText();
		docu=jcp.getUITextField().getDocument();
		jcp.getUITextField().setDocument(new StringDoc(jcp.getUITextField(),"DIP_DC_"));
		jcp.getUITextField().setText(ss);

		tempMybillVO=(MyBillVO) getBillUI().getVOFromUI();
	}

	@Override
	protected void onBoCancel() throws Exception {
		//2011-6-4
		if(docu!=null){
			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
			jcp.getUITextField().setDocument(docu);
			jcp.getUITextField().setText("DIP_DC_");
		}
		super.onBoCancel();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
	}

	@Override
	protected void onBoSave() throws Exception {
		/*2011-5-26
		 * 非空校验*/
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		VOTreeNode node=getSelectNode();
		if(bd!=null){
			bd.dataNotNullValidate();
		}
		String pk=(String)ui.getBillCardPanel().getHeadItem("pk_contdata_h").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		DipContdataVO hvo=(DipContdataVO) billVO.getParentVO();		
		setTSFormBufferToVO(billVO);

		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		DipContdataVO dhvo=(DipContdataVO) checkVO.getParentVO();	
		if(dhvo.getDef_str_1()!=null&&dhvo.getDef_str_1().equals("Y")){
			if(SJUtil.isNull(dhvo.getDef_str_2())||dhvo.getDef_str_2().length()<=0||SJUtil.isNull(dhvo.getDef_str_3())||dhvo.getDef_str_3().length()<=0){
				getBillUI().showErrorMessage("请维护自动对照的字段！");
				return;
			}
			//liyunzhe 注释掉 因为可以什么也不选择，可以是只做自动对照维护 2012-06-18 start
			//add by zhw 2012-05-28 
//			if(null == dhvo.getDeletetype() || "".equals(dhvo.getDeletetype())){
//				getBillUI().showErrorMessage("请维护删除类型的字段！");
//				return;
//			}
			//add by zhw 2012-05-28
//			liyunzhe 注释掉 因为可以什么也不选择，可以是只做自动对照维护 2012-06-18 end
		}
		//存储表名转换成大写
		String middletab=dhvo.getMiddletab().toUpperCase();
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());		
		//2011-7-4 校验编码唯一 cl
		String code=(String) ui.getBillCardPanel().getHeadItem("code").getValueObject();
		String name=(String) ui.getBillCardPanel().getHeadItem("name").getValueObject();
		UFBoolean floder=(UFBoolean) getSelectNode().getData().getAttributeValue("isfolder")==null?new UFBoolean(false):(UFBoolean) getSelectNode().getData().getAttributeValue("isfolder");
		if(floder!=null&&floder.booleanValue()){//确定是增加
			String hh=(String) getSelectNode().getNodeID();
			Collection ccode=bs.retrieveByClause(DipContdataVO.class, " pk_sysregister_h= '"+hh+"' and nvl(isfolder,'N')='N' and nvl(dr,0)=0 and code='"+code+"'");
			if(ccode !=null){
				if(ccode.size()>=1){
					getSelfUI().showErrorMessage("该编码【"+code+"】已经存在！");
					return;
				}
			}
			// ccode=bs.retrieveByClause(DipContdataVO.class, "((pk_xt is null and pk_sysregister_h='"+dhvo.getPk_xt()+"') or (pk_xt is not null and pk_xt='"+dhvo.getPk_xt()+"')) and (isfolder is null or isfolder='Y') and name='"+dhvo.getName()+"' and pk_contdata_h <> '"+pk+"' and nvl(dr,0)=0");
			ccode=bs.retrieveByClause(DipContdataVO.class, " pk_sysregister_h= '"+hh+"' and nvl(dr,0)=0 and nvl(isfolder,'N')='N' and name='"+name+"'");
			if(ccode !=null){
				if(ccode.size()>=1){
					getSelfUI().showErrorMessage("该名称【"+dhvo.getName()+"】已经存在！");
					return;
				}
			}
		}else{//修改
			String pkff=(String) getSelectNode().getParentnodeID();
			
			Collection ccode=bs.retrieveByClause(DipContdataVO.class, " pk_sysregister_h= '"+pkff+"' and nvl(isfolder,'N')='N' and nvl(dr,0)=0 and code='"+code+"' and pk_contdata_h<>'"+dhvo.getPk_contdata_h()+"'");
			if(ccode !=null){
				if(ccode.size()>=1){
					getSelfUI().showErrorMessage("该编码【"+code+"】已经存在！");
					return;
				}
			}
			// ccode=bs.retrieveByClause(DipContdataVO.class, "((pk_xt is null and pk_sysregister_h='"+dhvo.getPk_xt()+"') or (pk_xt is not null and pk_xt='"+dhvo.getPk_xt()+"')) and (isfolder is null or isfolder='Y') and name='"+dhvo.getName()+"' and pk_contdata_h <> '"+pk+"' and nvl(dr,0)=0");
			ccode=bs.retrieveByClause(DipContdataVO.class, " pk_sysregister_h= '"+pkff+"' and nvl(dr,0)=0 and nvl(isfolder,'N')='N' and name='"+name+"' and pk_contdata_h<>'"+dhvo.getPk_contdata_h()+"'");
			if(ccode !=null){
				if(ccode.size()>=1){
					getSelfUI().showErrorMessage("该名称【"+dhvo.getName()+"】已经存在！");
					return;
				}
			}	
		}
		//Collection ccode=bs.retrieveByClause(DipContdataVO.class, " ((pk_xt is null and pk_sysregister_h='"+dhvo.getPrimaryKey()+"') or (pk_xt is not null and pk_xt='"+dhvo.getPk_xt()+"')) and (isfolder is null or isfolder='Y') and code='"+code+"' and pk_contdata_h <> '"+pk+"' and nvl(dr,0)=0");
		//Collection ccode=bs.retrieveByClause(DipContdataVO.class, " ((pk_xt is null and pk_sysregister_h='"+dhvo.getPrimaryKey()+"') or (pk_xt is not null and pk_xt='"+dhvo.getPk_xt()+"')) and (isfolder is null or isfolder='Y') and code='"+code+"' and pk_contdata_h <> '"+pk+"' and nvl(dr,0)=0");
		
		int k=8+extcode.length();
		if(middletab.length()<=k){
			ui.showWarningMessage("请输入存储表名");//修改长度校验 2011-06-25 zlc 添加extcode 静态成员变量
			return;
		}else{
			//校验表名是否重复
			Collection memoryName=bs.retrieveByClause(DipContdataVO.class, "middletab='"+middletab.toUpperCase()+"' and nvl(dr,0)=0 and pk_contdata_h <>'"+pk+"'");
			if(memoryName !=null){
				if(memoryName.size()>=1){
					ui.showWarningMessage("该【"+middletab.toUpperCase()+"】存储表名已经存在！");
					return ;
				}
			}
		}
		dhvo.setMiddletab(middletab);
		
		//2011-7-14 begin
		String contabname=dhvo.getContabname();//对照系统物理表
		String contcolname=dhvo.getContcolname();//对照系统物理字段
		String extetabname=dhvo.getExtetabname();//被对照系统物理表
		String extecolname=dhvo.getExtecolname();//被对照系统物理字段
		if(dhvo.getContabcode().equals(dhvo.getExtetabcode())){
			ui.showErrorMessage("对照物理表和被对照物理表不能为同一张表！");
			return;
		}
		Collection yy=bs.retrieveByClause(DipContdataVO.class, "contabname='"+contabname+"' and contcolname='"+contcolname+"' and extetabname='"+extetabname+"' and extecolname='"+extecolname+"' and nvl(dr,0)=0 and pk_contdata_h <>'"+pk+"'  and ((pk_xt is null and pk_sysregister_h='"+dhvo.getPk_xt()+"') or (pk_xt is not null and pk_xt='"+dhvo.getPk_xt()+"')) and (isfolder is null or isfolder='Y')");
		if(yy !=null){
			if(yy.size()>=1){
				ui.showErrorMessage("对照、被对照系统表名、字段不能重复被引用！");
				return;
			}
		}
		/* end */
		
		/*引用字段不能重复校验   2011-07-09  zlc  begin*/		
		int rowcount=getSelfUI().getBillCardPanel().getBillTable("dip_contdata_b").getRowCount();
		if(rowcount>0){
			for(int i=0;i<rowcount;i++){
				String field1=getSelfUI().getBillCardPanel().getBodyValueAt(i, "field")==null?"":getSelfUI().getBillCardPanel().getBodyValueAt(i, "field").toString();
				if(field1!=null&&field1.length()>0){
					for(int j=0;j<i;j++){
						String field2=getSelfUI().getBillCardPanel().getBodyValueAt(j, "field")==null?"":getSelfUI().getBillCardPanel().getBodyValueAt(j, "field").toString();
						if(field1.equalsIgnoreCase(field2)){
							getSelfUI().showErrorMessage("对照系统字段不能被重复引用！");
							return;
						}
					}
				}
			}
		}
		int extrowcount=getSelfUI().getBillCardPanel().getBillTable("dip_contdata_b2").getRowCount();
		if(extrowcount>0){
			for(int i=0;i<extrowcount;i++){
				String field3=getSelfUI().getBillCardPanel().getBodyValueAt(i, "field")==null?"":getSelfUI().getBillCardPanel().getBodyValueAt(i, "field").toString();
				if(field3!=null&&field3.length()>0){
					for(int j=0;j<i;j++){
						String field4=getSelfUI().getBillCardPanel().getBodyValueAt(j, "field")==null?"":getSelfUI().getBillCardPanel().getBodyValueAt(j, "field").toString();
						if(field3.equalsIgnoreCase(field4)){
							getSelfUI().showErrorMessage("被对照系统字段不能被重复引用！");
							return;
						}
					}
				}
			}
		}/*end*/

		setTSFormBufferToVO(checkVO);
//liyunzhe 注释 注释那些不必要的方法，因为在数据对照定义是建表，回写数据定义，所以在自动对照定义这块只是维护自动对照的字段，和删除类型 start 2012-06-18 
//		boolean isrewritdf=false;
//		boolean iscreattab=false;
//		boolean isdelet=false;
//		String mtabt="";
//		String mtab="";
		//B--判断表结构的变化
//		if(tempMybillVO!=null){
//			DipContdataVO hvo1=(DipContdataVO) checkVO.getParentVO();
//			DipContdataVO hvot=(DipContdataVO) tempMybillVO.getParentVO();
//			mtabt=hvot.getMiddletab();//存储表名
//			mtab=hvo1.getMiddletab();
//			String ctabt=hvot.getContabcode();//对照表的表名
//			String ctab=hvo1.getContabcode();
//			String ftabt=hvot.getContcolcode();//对照表的字段
//			String ftab=hvo1.getContcolcode();
//			String ftabet=hvot.getExtecolcode();
//			String ftabe=hvo1.getExtecolcode();
//			if(!mtabt.equals(mtab)){//表名不一致，删除重建
//				if(isTableExist(mtabt)){
//					Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","数据存储表名变化，必须删除表重建。");
//					if(flag==1){
//						isdelet=true;
//						isrewritdf=true;
//						iscreattab=true;
//					}else{
//						return;
//					}
//				}
//				
//			}else {
//				isrewritdf=true;
//				DipDatadefinitBVO bvot1=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, ftabt);
//				DipDatadefinitBVO bvot2=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, ftab);
//				
//				DipDatadefinitBVO bvot3=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, ftabet);
//				DipDatadefinitBVO bvot4=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, ftabe);
//				if(bvot1.getType().equals(bvot2.getType())&&((bvot1.getLength()==null&&bvot2.getLength()==null)||(bvot1.getLength()!=null&&bvot2.getLength()!=null&&bvot1.getLength().intValue()==bvot2.getLength().intValue()))&&((bvot1.getDeciplace()==null&&bvot2.getDeciplace()==null)||(bvot1.getDeciplace()!=null&&bvot2.getDeciplace()!=null&&bvot1.getDeciplace().intValue()==bvot2.getDeciplace().intValue()))
//					&&bvot3.getType().equals(bvot4.getType())&&((bvot3.getLength()==null&&bvot4.getLength()==null)||(bvot3.getLength()!=null&&bvot4.getLength()!=null&&bvot3.getLength().intValue()==bvot4.getLength().intValue()))&&((bvot3.getDeciplace()==null&&bvot4.getDeciplace()==null)||(bvot3.getDeciplace()!=null&&bvot4.getDeciplace()!=null&&bvot3.getDeciplace().intValue()==bvot4.getDeciplace().intValue()))	
//				){
//					if(isTableExist(mtabt)){
//					String sql="select count(*) from "+mtabt;
//					Integer it=Integer.parseInt(iq.queryfield(sql));
//					String msg="数据存储结构没有变化，表中";
//					if(it>0){
//						msg=msg+"有数据，";
//					}else{
//						msg=msg+"没有数据，";
//					}
//					AskDLG ad=new AskDLG(getBillUI(),"提示","请选择？",new String[]{msg+"不删除表重建",msg+"删除表重建"});
//					ad.showModal();
//					if(ad.getRes()==1){
//						isdelet=true;
//						iscreattab=true;
//					}else if(ad.getRes()==0){
//						
//					}else{
//						return;
//					}
//					}else{
//						iscreattab=true;
//						isrewritdf=true;
//					}
//				}else{
//					Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","数据存储表结构变化，必须删除表城建。");
//					if(flag==1){
//						isdelet=true;
//						isrewritdf=true;
//						iscreattab=true;
//					}else{
//						return;
//					}
//				}
//			}
////		}else{
//			iscreattab=true;
//			isrewritdf=true;
//		}
		
		//e--判断表结构的变化
//		liyunzhe 注释 注释那些不必要的方法，因为在数据对照定义是建表，回写数据定义，所以在自动对照定义这块只是维护自动对照的字段，和删除类型 end 2012-06-18		
		
		
		//end
		checkVO.getChildrenVO();
		int nCurrentRow = -1;
		if(isAdding()){
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), billVO);

			//2011-6-4 begin
			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
			String ss=jcp.getText();
			jcp.getUITextField().setDocument(docu);
			jcp.getUITextField().setText(ss);
			/*end */

			setAddNewOperate(isAdding(), billVO);
			setSaveOperateState();
			if (nCurrentRow >= 0)
				getBufferData().setCurrentRow(nCurrentRow);

			//如果该单据增加保存时是自动维护树结构，则执行如下操作
			if (getUITreeCardController().isAutoManageTree()) {			
				getSelfUI().insertNodeToTree(billVO.getParentVO());
			}
		}else{
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), billVO);


			String pk_datasynch=hvo.getPrimaryKey();
			HYPubBO_Client.update((SuperVO) checkVO.getParentVO());
//			SuperVO[] vos = HYPubBO_Client.queryByCondition(ContdataBVO.class, " pk_contdata_h='" + pk_datasynch + "' and isnull(dr,0)=0  ");
//			SuperVO[] vos1 = HYPubBO_Client.queryByCondition(ContdataB2VO.class, " pk_contdata_h='" + pk_datasynch + "' and isnull(dr,0)=0  ");

			MyBillVO billvo =new MyBillVO();
			billvo.setParentVO(HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, pk_datasynch));
//			billvo.setTableVO(billvo.getTableCodes()[0], vos);
//			billvo.setTableVO(billvo.getTableCodes()[1], vos1);
			getBufferData().setCurrentVO(billvo);
			nCurrentRow = getBufferData().getCurrentRow();

			//2011-6-4 begin
			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
			String ss=jcp.getText();
			jcp.getUITextField().setDocument(docu);
			jcp.getUITextField().setText(ss);
			/*end */
			setAddNewOperate(isAdding(), billvo);
			setSaveOperateState();
			if (nCurrentRow >= 0)
				getBufferData().setCurrentRow(nCurrentRow);
			//如果该单据增加保存时是自动维护树结构，则执行如下操作
			if (getUITreeCardController().isAutoManageTree()) {			
				getSelfUI().insertNodeToTree(billVO.getParentVO());
			}
		}
//		liyunzhe 注释 注释那些不必要的方法，因为在数据对照定义是建表，回写数据定义，所以在自动对照定义这块只是维护自动对照的字段 start 2012-06-18		
//		dhvo=(DipContdataVO) billVO.getParentVO();
////		rewriteDatadefin(dhvo);
//		rewriteDatadefn(mtabt,mtab,isrewritdf,
//				iscreattab,dhvo,isdelet);
//		liyunzhe 注释 注释那些不必要的方法，因为在数据对照定义是建表，回写数据定义，所以在自动对照定义这块只是维护自动对照的字段 end 2012-06-18		
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		/*if(vo!=null&&vo.getIsfolder()!=null&&vo.getIsfolder().booleanValue()){
			
		}else{
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
		}
		getSelfUI().updateButtonUI();*/
		
	}
	 public void deletDataDefinByTabname(String tablename) throws BusinessException, SQLException, DbException{
	    	DipDatadefinitHVO[] hvo=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "memorytable='"+tablename+"'");
	    	String sql="delete from pub_datadict where id='"+tablename+"'";
	    	iq.exesql(sql);
	    	if(hvo!=null&&hvo.length>0){
	    		for(int i=0;i<hvo.length;i++){
	    			HYPubBO_Client.deleteByWhereClause(DipDatadefinitBVO.class, "pk_datadefinit_h='"+hvo[i].getPrimaryKey()+"'");
	    			HYPubBO_Client.deleteByWhereClause(DipDatadefinitHVO.class, "pk_datadefinit_h='"+hvo[i].getPrimaryKey()+"'");
	    		}
	    	}
	    }

	public void rewriteDatadefn(String mtabt,String mtab,boolean isrewritdf,
			boolean iscreattab,DipContdataVO contdataVO,boolean isdelete) throws BusinessException, SQLException, DbException{
		String pk=contdataVO.getPk_xt();
	if(isdelete){
		String sql="drop table "+mtabt;
		iq.exectEverySql(sql);
	}
//	if(!isrewritdf){
//		return;
//	}
	deletDataDefinByTabname(mtabt);
	
	boolean suc=false;
//	if(!isTableExist(contdataVO.getMiddletab())){
		DipDatadefinitBVO[] bvoinert=new DipDatadefinitBVO[3];
		String ccode=contdataVO.getContcolcode();
		String ecode=contdataVO.getExtecolcode();
		if(ccode==null||ccode.length()<=0||ecode==null||ecode.length()<=0){
		}else{
			try {
//				if(!isrewritdf||!iscreattab){
//					return;
//				}
			DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_b in ('"+ccode+"','"+ecode+"') and nvl(dr,0)=0");
			String str1="";
			String str2="";
			if(bvos==null||bvos.length<2){
				getSelfUI().showErrorMessage("请核对对照表定义是否存在！");
				return;
			}
			if(bvos[0].getPrimaryKey().equals(ccode)){
				bvoinert[1]=new DipDatadefinitBVO();
				bvoinert[1].setQuotetable(bvos[0].getPrimaryKey());
				bvoinert[1].setPrimaryKey(null);
				bvoinert[1].setCname("对照系统字段");
				bvoinert[1].setEname("contpk".toUpperCase());
				bvoinert[1].setQuotecolu(bvos[0].getEname().toUpperCase());
				bvoinert[1].setIsonlyconst(new UFBoolean(true));
				bvoinert[1].setType(bvos[0].getType());
				bvoinert[1].setLength(bvos[0].getLength());
				bvoinert[1].setDeciplace(bvos[0].getDeciplace());
				bvoinert[2]=new DipDatadefinitBVO();
				bvoinert[2].setQuotetable(bvos[1].getPrimaryKey());
				bvoinert[2].setPrimaryKey(null);
				bvoinert[2].setCname("被对照系统字段");
				bvoinert[2].setEname("extepk".toUpperCase());
				bvoinert[2].setQuotecolu(bvos[1].getEname().toUpperCase());
				bvoinert[2].setIsonlyconst(new UFBoolean(false));
				bvoinert[2].setType(bvos[1].getType());
				bvoinert[2].setLength(bvos[1].getLength());
				bvoinert[2].setDeciplace(bvos[1].getDeciplace());
				
				String type=bvos[0].getType();
				Integer len=bvos[0].getLength();
				if(type.toLowerCase().contains("char")){
					str1="contpk "+type+"("+len+"),";
				}else if(type.toLowerCase().equals("number")){
					str1="contpk "+type+"("+len+","+bvos[0].getDeciplace()+"),";
				}else{
					str1="contpk "+type+",";
				}
				type=bvos[1].getType();
				len=bvos[1].getLength();
				if(type.toLowerCase().contains("char")){
					str2="extepk "+type+"("+len+"),";
				}else if(type.toLowerCase().equals("number")){
					str2="extepk "+type+"("+len+","+bvos[0].getDeciplace()+"),";
				}else{
					str2="extepk "+type+",";
				}
			}else{
				bvoinert[1]=new DipDatadefinitBVO();
				bvoinert[1].setQuotetable(bvos[0].getPrimaryKey());
				bvoinert[1].setPrimaryKey(null);
				bvoinert[1].setCname("被对照系统字段");
				bvoinert[1].setEname("extepk".toUpperCase());
				bvoinert[1].setQuotecolu(bvos[0].getEname().toUpperCase());
				bvoinert[1].setIsonlyconst(new UFBoolean(false));

				bvoinert[1].setType(bvos[0].getType());
				bvoinert[1].setLength(bvos[0].getLength());
				bvoinert[1].setDeciplace(bvos[0].getDeciplace());
				bvoinert[2]=new DipDatadefinitBVO();
				bvoinert[2].setQuotetable(bvos[1].getPrimaryKey());
				bvoinert[2].setPrimaryKey(null);
				bvoinert[2].setCname("对照系统字段");
				bvoinert[2].setEname("contpk".toUpperCase());
				bvoinert[2].setQuotecolu(bvos[1].getEname().toUpperCase());
				bvoinert[2].setIsonlyconst(new UFBoolean(true));
				bvoinert[2].setType(bvos[1].getType());
//				if(bvos[1].getLength()!=null){
					bvoinert[2].setLength(bvos[1].getLength());
//				}
//				if(bvos[1].getDeciplace()!=null){
					bvoinert[2].setDeciplace(bvos[1].getDeciplace());
//				}
				
				String type=bvos[1].getType();
				Integer len=bvos[1].getLength();
				if(type.toLowerCase().contains("char")){
					str1="contpk "+type+"("+len+"),";
				}else if(type.toLowerCase().equals("number")){
					str1="contpk "+type+"("+len+","+bvos[0].getDeciplace()+"),";
				}else{
					str1="contpk "+type+",";
				}
				type=bvos[0].getType();
				len=bvos[0].getLength();
				if(type.toLowerCase().contains("char")){
					str2="extepk "+type+"("+len+"),";
				}else if(type.toLowerCase().equals("number")){
					str2="extepk "+type+"("+len+","+bvos[0].getDeciplace()+"),";
				}else{
					str2="extepk "+type+",";
				}
			}
			if(iscreattab){
//				创建中间表
				String sql = "create table "+contdataVO.getMiddletab()+"(" +
				"pk char(20) not null primary key," +
				str1+str2+
				"ts char(19),dr smallint)";

					//queryfield.exesql(sql);
					suc=iq.exectEverySql(sql);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}



		//2011-7-11 将中间表回写到数据定义中，并且写入数据字典
		if(iscreattab){
			//2011-7-14 对对照系统、被对照系统主键建立索引
			String icsql="create index I"+contdataVO.getMiddletab()+"CONTPK on "+contdataVO.getMiddletab()+" (CONTPK)";			
			String iesql="create index I"+contdataVO.getMiddletab()+"EXTEPK on "+contdataVO.getMiddletab()+" (EXTEPK)";

			String d="select isdeploy from dip_sysregister_h where pk_sysregister_h='"+pk+"' and nvl(dr,0)=0";
			String deploy=null;
			UFBoolean de=null;
			try {
				iq.exesql(icsql);
				iq.exesql(iesql);

				deploy=iq.queryfield(d);
				if(deploy !=null){
					if(deploy.equals("N")){
						de=new UFBoolean(false);
					}else{
						de=new UFBoolean(true);
					}
				}else{
					de=new UFBoolean(false);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		String pkf=getPkf(pk);
			//向数据定义中插入该中间表的表结构
			DipDatadefinitHVO hdvo=new DipDatadefinitHVO();
			hdvo.setSyscode(contdataVO.getCode());//编码
			hdvo.setSysname(contdataVO.getName());//名称
			hdvo.setMemorytype("表");//存储类型
			hdvo.setDatatype("0001BB100000000JKANO");//2011-7-21增加： 数据类型：系统信息结构
			hdvo.setMemorytable(contdataVO.getMiddletab());//存储表名
		//	hdvo.setIscreatetab(new UFBoolean(true));//是否已建表
			hdvo.setIscreatetab("Y");//是否已建表
			hdvo.setTabsoucetype("数据对照定义");//表来源类型
			hdvo.setDispostatus("");//控制处理状态
			hdvo.setEndstatus("");//控制结束状态
			hdvo.setPk_sysregister_h(pkf);//父节点主键
			hdvo.setDef_str_1(contdataVO.getPrimaryKey());
			hdvo.setIsdeploy(new UFBoolean(true));
			hdvo.setIsfolder(new UFBoolean(false));
			hdvo.setBusicode(contdataVO.getMiddletab());
			hdvo.setPk_xt(pk);
			String st=HYPubBO_Client.insert(hdvo);//主键
			hdvo.setPk_datadefinit_h(st);

			//查询列名以及对应的类型

			try {
				bvoinert[0]=new DipDatadefinitBVO();
				bvoinert[0].setCname("主键");
				bvoinert[0].setEname("PK");
				bvoinert[0].setLength(20);
				bvoinert[0].setType("CHAR");
				bvoinert[0].setIsimport(new UFBoolean(true));//是否必输项
				bvoinert[0].setIspk(new UFBoolean(true));//是否主键
				for(int i=0;i<3;i++){
					bvoinert[i].setPk_datadefinit_h(st);
					if(i!=0){
						bvoinert[i].setIspk(new UFBoolean(false));
						bvoinert[i].setIsimport(new UFBoolean(true));
						bvoinert[i].setIsquote(new UFBoolean(true));
					}
				}
				
				HYPubBO_Client.insertAry(bvoinert);
				//向数据字典中写入
				DipSysregisterHVO hvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk);
//				String sql2="select code from dip_sysregister_h where pk_sysregister_h='"+pk+"' and nvl(dr,0)=0";
//				String code=iq.queryfield(sql2);
				String code=hvo.getCode();
				if(code==null||"".equals(code)){
					return;
				}
				createForder(hvo.getExtname(), code, "jkpt");
				Datadict m_dict = new Datadict();
				DDCData data = null;
				ObjectNode[] objNodes = null;
				BizObject[] objs = null;
				data=DDCBO_Client.fromDBMetaData(IContrastUtil.DESIGN_CON,new String[]{st} , 0, code);
				objNodes = (ObjectNode[]) data.getNode().toArray(new ObjectNode[0]);
				objs = (BizObject[]) data.getTable().toArray(new BizObject[0]);
				m_dict.saveObjectNode(objNodes, objs, 20);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}			
//		}
//	}
}
	private String getPkf(String pk_xt){
		DipDatadefinitHVO[] hvo=null;
		String syscode=IContrastUtil.SYSCODE;
		String sysname=IContrastUtil.SYSNAME;
		try {
			hvo=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "isfolder='Y' and pk_sysregister_h='"+pk_xt+"' and pk_xt='"+pk_xt+"' and syscode='"+syscode+"' and sysname='"+sysname+"'");
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(hvo!=null&&hvo.length>0){
			return hvo[0].getPrimaryKey();
		}else{
			DipDatadefinitHVO hvoi=new DipDatadefinitHVO();
			hvoi.setPk_sysregister_h(pk_xt);
			hvoi.setPk_xt(pk_xt);
			hvoi.setIsfolder(new UFBoolean(true));
			hvoi.setSyscode(syscode);
			hvoi.setSysname(sysname);
			String pksys=null;
			try {
				pksys=HYPubBO_Client.insert(hvoi);
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pksys;
		}
	}
	 /**
	 * @desc 创建数据字典里边文件夹的方法
	 * @param String displayname 文件夹名称
	 * @param id 文件夹的id
	 * @param parentID 父节点的ID
	 * @author wyd
	 * @time 2011-07-12
	 * */

	public void createForder(String displayname,String id,String parentID){
		String sql="select id from pub_datadict where id='"+id+"'";
		String res=null;
		try {
			res=iq.queryfield(sql);
			if(res!=null&&res.equals(id)){
				return;
			}
			ObjectNode node=new FolderNode();
			FolderObject fo=new FolderObject();
			fo.setNode(node);
			fo.setDisplayName(displayname);
			fo.setGUID(id);
			fo.setID(id);
			fo.setKind("Folder");
			fo.setParentGUID(parentID);

			IBizObjStorage storage = (IBizObjStorage) NCLocator.getInstance().lookup(
					IBizObjStorage.class.getName());

			storage.saveObject(IContrastUtil.DESIGN_CON, "nc.bs.pub.ddc.datadict.DatadictStorage", node, fo);
		} catch (BusinessException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (DbException e1) {
			e1.printStackTrace();
		}catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessRuntimeException(e.getMessage());
		}

	}

	public void rewriteDatadefin(DipContdataVO contdataVO) throws UifException{
		String pk=contdataVO.getPk_sysregister_h();
		
		boolean suc=false;
		if(!isTableExist(contdataVO.getMiddletab())){
			DipDatadefinitBVO[] bvoinert=new DipDatadefinitBVO[3];
			String ccode=contdataVO.getContcolcode();
			String ecode=contdataVO.getExtecolcode();
			if(ccode==null||ccode.length()<=0||ecode==null||ecode.length()<=0){
			}else{
				try {
				DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_b in ('"+ccode+"','"+ecode+"') and nvl(dr,0)=0");
				String str1="";
				String str2="";
				if(bvos==null||bvos.length<2){
					getSelfUI().showErrorMessage("请核对对照表定义是否存在！");
					return;
				}
				if(bvos[0].getPrimaryKey().equals(ccode)){
					bvoinert[1]=new DipDatadefinitBVO();
					bvoinert[1].setQuotetable(bvos[0].getPrimaryKey());
					bvoinert[1].setPrimaryKey(null);
					bvoinert[1].setCname("对照系统字段");
					bvoinert[1].setEname("contpk".toUpperCase());
					bvoinert[1].setQuotecolu(bvos[0].getEname().toUpperCase());
					bvoinert[1].setIsonlyconst(new UFBoolean(true));
					bvoinert[1].setType(bvos[0].getType());
					bvoinert[1].setLength(bvos[0].getLength());
					bvoinert[1].setDeciplace(bvos[0].getDeciplace());
					bvoinert[2]=new DipDatadefinitBVO();
					bvoinert[2].setQuotetable(bvos[1].getPrimaryKey());
					bvoinert[2].setPrimaryKey(null);
					bvoinert[2].setCname("被对照系统字段");
					bvoinert[2].setEname("extepk".toUpperCase());
					bvoinert[2].setQuotecolu(bvos[1].getEname().toUpperCase());
					bvoinert[2].setIsonlyconst(new UFBoolean(false));
					bvoinert[2].setType(bvos[1].getType());
					bvoinert[2].setLength(bvos[1].getLength());
					bvoinert[2].setDeciplace(bvos[1].getDeciplace());
					
					String type=bvos[0].getType();
					int len=bvos[0].getLength();
					if(type.toLowerCase().contains("char")){
						str1="contpk "+type+"("+len+"),";
					}else if(type.toLowerCase().equals("number")){
						str1="contpk "+type+"("+len+","+bvos[0].getDeciplace()+"),";
					}else{
						str1="contpk "+type+",";
					}
					type=bvos[1].getType();
					len=bvos[1].getLength();
					if(type.toLowerCase().contains("char")){
						str2="extepk "+type+"("+len+"),";
					}else if(type.toLowerCase().equals("number")){
						str2="extepk "+type+"("+len+","+bvos[0].getDeciplace()+"),";
					}else{
						str2="extepk "+type+",";
					}
				}else{
					bvoinert[1]=new DipDatadefinitBVO();
					bvoinert[1].setQuotetable(bvos[0].getPrimaryKey());
					bvoinert[1].setPrimaryKey(null);
					bvoinert[1].setCname("被对照系统字段");
					bvoinert[1].setEname("extepk".toUpperCase());
					bvoinert[1].setQuotecolu(bvos[0].getEname().toUpperCase());
					bvoinert[1].setIsonlyconst(new UFBoolean(false));

					bvoinert[1].setType(bvos[0].getType());
					bvoinert[1].setLength(bvos[0].getLength());
					bvoinert[1].setDeciplace(bvos[0].getDeciplace());
					bvoinert[2]=new DipDatadefinitBVO();
					bvoinert[2].setQuotetable(bvos[1].getPrimaryKey());
					bvoinert[2].setPrimaryKey(null);
					bvoinert[2].setCname("对照系统字段");
					bvoinert[2].setEname("contpk".toUpperCase());
					bvoinert[2].setQuotecolu(bvos[1].getEname().toUpperCase());
					bvoinert[2].setIsonlyconst(new UFBoolean(true));
					bvoinert[2].setType(bvos[1].getType());
					bvoinert[2].setLength(bvos[1].getLength());
					bvoinert[2].setDeciplace(bvos[1].getDeciplace());
					
					String type=bvos[1].getType();
					int len=bvos[1].getLength();
					if(type.toLowerCase().contains("char")){
						str1="contpk "+type+"("+len+"),";
					}else if(type.toLowerCase().equals("number")){
						str1="contpk "+type+"("+len+","+bvos[0].getDeciplace()+"),";
					}else{
						str1="contpk "+type+",";
					}
					type=bvos[0].getType();
					len=bvos[0].getLength();
					if(type.toLowerCase().contains("char")){
						str2="extepk "+type+"("+len+"),";
					}else if(type.toLowerCase().equals("number")){
						str2="extepk "+type+"("+len+","+bvos[0].getDeciplace()+"),";
					}else{
						str2="extepk "+type+",";
					}
				}
//				创建中间表
				String sql = "create table "+contdataVO.getMiddletab()+"(" +
				"pk char(20) not null primary key," +
				str1+str2+
				"ts char(19),dr smallint)";

					//queryfield.exesql(sql);
					suc=iq.exectEverySql(sql);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}



			//2011-7-11 将中间表回写到数据定义中，并且写入数据字典
			if(suc==false){
				//2011-7-14 对对照系统、被对照系统主键建立索引
				String icsql="create index I"+contdataVO.getMiddletab()+"CONTPK on "+contdataVO.getMiddletab()+" (CONTPK)";			
				String iesql="create index I"+contdataVO.getMiddletab()+"EXTEPK on "+contdataVO.getMiddletab()+" (EXTEPK)";

				String d="select isdeploy from dip_sysregister_h where pk_sysregister_h='"+pk+"' and nvl(dr,0)=0";
				String deploy=null;
				UFBoolean de=null;
				try {
					iq.exesql(icsql);
					iq.exesql(iesql);

					deploy=iq.queryfield(d);
					if(deploy !=null){
						if(deploy.equals("N")){
							de=new UFBoolean(false);
						}else{
							de=new UFBoolean(true);
						}
					}else{
						de=new UFBoolean(false);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				//向数据定义中插入该中间表的表结构
				DipDatadefinitHVO hdvo=new DipDatadefinitHVO();
				hdvo.setSyscode(contdataVO.getCode());//编码
				hdvo.setSysname(contdataVO.getName());//名称
				hdvo.setMemorytype("表");//存储类型
				hdvo.setDatatype("0001BB100000000JKANO");//2011-7-21增加： 数据类型：系统信息结构
				hdvo.setMemorytable(contdataVO.getMiddletab());//存储表名
			//	hdvo.setIscreatetab(new UFBoolean(true));//是否已建表
				hdvo.setIscreatetab("Y");//是否已建表
				hdvo.setTabsoucetype("数据对照定义");//表来源类型
				hdvo.setDispostatus("");//控制处理状态
				hdvo.setEndstatus("");//控制结束状态
				hdvo.setPk_sysregister_h(pk);//父节点主键
				hdvo.setDef_str_1(contdataVO.getPrimaryKey());
				hdvo.setIsdeploy(de);

				String st=HYPubBO_Client.insert(hdvo);//主键
				hdvo.setPk_datadefinit_h(st);

				//查询列名以及对应的类型

				try {
					bvoinert[0]=new DipDatadefinitBVO();
					bvoinert[0].setCname("主键");
					bvoinert[0].setEname("PK");
					bvoinert[0].setLength(20);
					bvoinert[0].setType("CHAR");
					bvoinert[0].setIsimport(new UFBoolean(true));//是否必输项
					bvoinert[0].setIspk(new UFBoolean(true));//是否主键
					for(int i=0;i<3;i++){
						bvoinert[i].setPk_datadefinit_h(st);
						if(i!=0){
							bvoinert[i].setIspk(new UFBoolean(false));
							bvoinert[i].setIsimport(new UFBoolean(true));
							bvoinert[i].setIsquote(new UFBoolean(true));
						}
					}
					
					HYPubBO_Client.insertAry(bvoinert);
					//向数据字典中写入
					String sql2="select code from dip_sysregister_h where pk_sysregister_h='"+pk+"' and nvl(dr,0)=0";
					String code=iq.queryfield(sql2);
					if(code==null||"".equals(code)){
						return;
					}
					Datadict m_dict = new Datadict();
					DDCData data = null;
					ObjectNode[] objNodes = null;
					BizObject[] objs = null;
					data=DDCBO_Client.fromDBMetaData(null,new String[]{st} , 0, code);
					objNodes = (ObjectNode[]) data.getNode().toArray(new ObjectNode[0]);
					objs = (BizObject[]) data.getTable().toArray(new BizObject[0]);
					m_dict.saveObjectNode(objNodes, objs, 20);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (DbException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
		}
	}

	@Override
	protected void onBoDelete() throws Exception {

		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要删除的节点！");
			return;
		}
		String fpk=tempNode.getParentnodeID()==null?"":tempNode.getParentnodeID().toString();
		if(fpk==null || fpk.trim().equals("")){
			getSelfUI().showWarningMessage("系统节点不能做删除操作！");
			return ;
		}
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确认要删除?");
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
		if(flag == 1){
			String pk=(String) tempNode.getNodeID();
			//2011-7-12
			DipContdataVO hvo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, pk);
			String middletab=hvo.getMiddletab();	

			HYPubBO_Client.delete(HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class,pk));

			//2011-7-12
			delete(fpk,middletab);

			//更新树
			this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
			if(node1!=null){
				getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
									.getPath()));
				}

		}

	}

	/**
	 * @author 程莉
	 * @date 2011-7-12
	 * @desc 删除时，删除中间表，同时判断该中间表是否回写到数据定义了，如果回写到数据定义，则删除
	 * @param 系统注册主键 pk_sysregister_h、中间表名：middletab
	 */
	public void delete(String pk_sysregister_h,String middletab){
		try {
			deletDataDefinByTabname(middletab);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql="drop table "+middletab;
		try {
			iq.exesql(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override 
	public void onTreeSelected(VOTreeNode selectnode) {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){

			super.onTreeSelected(selectnode);
			return;
		}
		DipContdataVO headvo=(DipContdataVO) treeNode.getData();
		DipSysregisterHVO syshvo;
		if(treeNode!=null){
			if(treeNode.getParentnodeID()==null||"".equals(treeNode.getParentnodeID())){
				try {
					syshvo = (DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, treeNode.getNodeID().toString());
					extcode=syshvo.getExtcode();

				} catch (UifException e) {
					e.printStackTrace();
				}
			}
			/*二级节点通过所选节点的父节点getParentnodeID取系统主键，找到外部系统编码
			 * 2011-06-27
			 * zlc*/
			else if(treeNode.getParentnodeID().toString().length()>0){
				try{
					syshvo = (DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, headvo.getPk_xt());
					extcode=syshvo.getExtcode();
				} catch (UifException e) {
					e.printStackTrace();
				}
			}
		}

		super.onTreeSelected(selectnode);
		if(!SJUtil.isNull(headvo)){
			/**
			对照系统表名		contabname
			对照系统物理字段	contcolcode
			对照系统编码     	concodefiled
			对照系统名称字段 	connamefiled

			被对照系统表名 	extetabname
			被对照系统物理字段 	extecolcode
			被对照系统编码字段 	exteconcodefiled
			被对照系统名称字段 	exteconnamefiled
			 */

		}
//		获取VO
		try {
			MyBillVO billvo =new MyBillVO();
			billvo.setParentVO(headvo);
			// 显示数据
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(selectnode.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));

			String pk=headvo.getPk_xt()==null?headvo.getPrimaryKey():headvo.getPk_xt();
			/*
			 * 修改表头和表体的对照、被对照物理字段的参照筛选条件
			 * 使修改状态下也能在对照系统物理表和被对照系统物理表字段所选的表的字段内做参照选择
			 * 2011-5-17
			 * zlc*/
			String tablename=headvo.getContabcode();
			//页面加载 
			//表头,限制非0000nc系统的表
			UIRefPane pane1=(UIRefPane) ui.getBillCardPanel().getHeadItem("contabcode").getComponent();
			DataDefinitRefModel model=new DataDefinitRefModel();
			model.addWherePart(	" and dip_datadefinit_h.pk_xt = '" +pk+
			"'  and nvl(dr,0) =0 and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N') and nvl(dip_datadefinit_h.dr,0) = 0  and dip_datadefinit_h.tabsoucetype='自定义' and dip_datadefinit_h.iscreatetab='Y'");//2011-7-4增加了dip_datadefinit_h.iscreatetab='Y'
			pane1.setRefModel(model);
			if(tablename!=null&&tablename.length()>0){
				UIRefPane pa=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("contcolcode").getComponent();
				DataDefinitbRefModel modelB=new DataDefinitbRefModel();
				modelB.addWherePart(" and dip_datadefinit_B.pk_datadefinit_h='"+tablename+"'  and( dip_datadefinit_b.ispk = 'Y' or dip_datadefinit_b.isonlyconst='Y' ) and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa.setRefModel(modelB);
				
//				UIRefPane pa1=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("def_str_2").getComponent();
//				DataDefinitbRefModel model1=new DataDefinitbRefModel();
//				model1.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+tablename+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='自定义' and nvl(dip_datadefinit_b.dr, 0) = 0 ");
//				pa1.setRefModel(model1);
			}

			String extablename=headvo.getExtetabcode();
			//表头,限制0000nc系统的表
//			UIRefPane pane=(UIRefPane) ui.getBillCardPanel().getHeadItem("extetabcode").getComponent();
//			DataDefinitRefModel model3=(DataDefinitRefModel) pane.getRefModel();
//			//2011-7-4增加了dip_datadefinit_h.iscreatetab='Y'
//			model3.addWherePart(
//					" and dip_datadefinit_h.pk_xt = '"+headvo.getExtesys()+"' and ((dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')) and dip_datadefinit_h.tabsoucetype='自定义' and nvl(dip_datadefinit_h.dr,0)=0 and dip_datadefinit_h.iscreatetab='Y'" 
//			);
			if(extablename!=null&&extablename.length()>0){
				UIRefPane pah=(UIRefPane)getSelfUI().getBillCardPanel().getHeadItem("extecolcode").getComponent();
				DataDefinitbRefModel modelh=new DataDefinitbRefModel();
				modelh.addWherePart(" and dip_datadefinit_b.pk_datadefinit_h='"+extablename+"' and (dip_datadefinit_b.ispk='Y' or dip_datadefinit_b.isonlyconst='Y') and nvl(dip_datadefinit_b.dr,0) = 0 ");
				pah.setRefModel(modelh);

				UIRefPane pa1=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("def_str_3").getComponent();
				DataDefinitbRefModel model1=new DataDefinitbRefModel();
				model1.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+extablename+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='自定义' and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa1.setRefModel(model1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	/*
	 * 添加234-246行
	 * 234-245行：增加对照系统物理表字段的参照条件（只能参照本系统数据定义的表）
	 * 246行：对照系统字段由系统注册表主键的显示公式的显示控制
	 * 2011-5-11  
	 * 张龙超*/
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择系统节点做增加操作！");
			return ;
		}
		DipContdataVO hvo=(DipContdataVO)treeNode.getData();
		if(hvo.getIsfolder()==null||!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("请选择文件夹进行操作！");
			return;
		}
		super.onBoAdd(bo);
		getSelfUI().getBillCardPanel().setHeadItem("pk_sysregister_h", treeNode.getNodeID());
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", hvo.getPk_xt());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().getHeadItem("def_str_2").setEdit(false);
		getSelfUI().getBillCardPanel().getHeadItem("def_str_3").setEdit(false);

		getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("contsys").getLoadFormula());

		UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
		docu=jcp.getUITextField().getDocument();
		jcp.getUITextField().setDocument(new StringDoc(jcp.getUITextField(),"DIP_DC_"+extcode.toUpperCase()+"_"));
		jcp.getUITextField().setText("DIP_DC_"+extcode.toUpperCase()+"_");

		tempMybillVO=null;
	}

	Document docu;

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

	private CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject childVos[] = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}

	/**
	 * 改方法在增行和插行后被调用，可以在该方法中为新增的行设置一些默认值
	 * 
	 * @param newBodyVO
	 * @return TODO
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();

		// 将主表主键写到子表中
		newBodyVO.setAttributeValue("pk_multi_h", parent == null ? null : parent.getNodeID());

		// 添加结束
		return newBodyVO;
	}

	@Override
	protected void onBoLinePaste() throws Exception {
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		int rows=getSelfUI().getBillCardPanel().getBillTable().getRowCount();
		onBoLineAdd();
		Object obj=getSelfUI().getBillCardPanel().getBodyValueAt(row, new ContdataBVO().SYSFIELD);
		if(obj==null){
			obj=getSelfUI().getBillCardPanel().getBodyValueAt(row, new ContdataB2VO().EXTESYSFIELD);
			Object fieldlen=getSelfUI().getBillCardPanel().getBodyValueAt(row, new ContdataB2VO().EXTESYSFIELDLEN);

			DipDatadefinitBVO[] dvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_b='"+obj.toString()+"'");
			String field=dvo[0].getEname();
			String fieldname=dvo[0].getCname();
			getSelfUI().getBillCardPanel().setBodyValueAt(field,rows, "field");
			getSelfUI().getBillCardPanel().setBodyValueAt(fieldname, rows, "fieldname");
			getSelfUI().getBillCardPanel().setBodyValueAt(obj, rows, new ContdataB2VO().EXTESYSFIELD);
			getSelfUI().getBillCardPanel().setBodyValueAt(fieldlen, rows, new ContdataB2VO().EXTESYSFIELDLEN);


		}else{
			Object fieldlen=getSelfUI().getBillCardPanel().getBodyValueAt(row, new ContdataBVO().SYSFIELDLEN);

			DipDatadefinitBVO[] dvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_b='"+obj.toString()+"'");
			String field=dvo[0].getEname();
			String fieldname=dvo[0].getCname();
			getSelfUI().getBillCardPanel().setBodyValueAt(field,rows, "field");
			getSelfUI().getBillCardPanel().setBodyValueAt(fieldname, rows, "fieldname");
			getSelfUI().getBillCardPanel().setBodyValueAt(obj, rows, "sysfield");
			getSelfUI().getBillCardPanel().setBodyValueAt(fieldlen, rows, "sysfieldlen");
		}
		getBufferData().setCurrentVO(getBufferData().getCurrentVO());
	}

	/**
	 * 判断系统里面是否有正准备创建的此表
	 * 在点击创建表按钮时判断：
	 * 1.如果有此表，查询表里是否有数据，如果有数据，给予提示，没有，则直接删掉，重新创建
	 * 2.没有此表，则直接创建
	 */
	public boolean isTableExist(String tableName){

		if(tableName==null){
			return false;
		}
		boolean isExist=false;//默认没有此表
		IUAPQueryBS queryBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql="select 1 from user_tables where table_name='"+tableName.toUpperCase()+"';";	
		try{
			ArrayList al=(ArrayList)queryBS.executeQuery(sql, new MapListProcessor());
			if(al.size()>=1){
				isExist=true;//有此表			
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.addFolderBtn:
			onBoAddFolder();
			break;
		case IBtnDefine.moveFolderBtn:
			onBoMoveFolder();
			break;
		case IBtnDefine.editFolderBtn:
			onBoEditFolder();
			break;
		case IBtnDefine.delFolderBtn:
			onBoDeleteFolder();
			break;
		case IBtnDefine.autoContData:
			onBoAotoContData();
			break;
		case IBtnDefine.YuJing:
			onBoWarning(intBtn);
			break;
		case IBtnDefine.CONTROL:
			onBoControl();
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
				DipContdataVO hvo=(DipContdataVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_DZ,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_DZ), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, hvo.getPrimaryKey());
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
	private void onBoWarning(int intBtn) throws Exception {


		VOTreeNode tempNode=getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("请选择要操作的节点");
			return;
		}
		DipContdataVO hvo=(DipContdataVO) tempNode.getData();
		String  hpk=hvo.getPrimaryKey();
		hvo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, hpk);
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
//		liyunzhe modify 如果没有自动对照定义且没有删除类型，就提示错误 2012-06-19 start  
		if((hvo.getDef_str_1()==null||!hvo.getDef_str_1().equals("Y"))&&(hvo.getDeletetype()==null||hvo.getDeletetype().trim().equals(""))){
			getSelfUI().showErrorMessage("没有自动对照的定义或者删除类型");
			return;
		}
		//liyunzhe modify 如果没有自动对照定义且没有删除类型，就提示错误 2012-06-19 end
		String pk_dataproce_h=hvo.getPrimaryKey();//.getPk_datadefinit_h();
		nc.vo.dip.warningset.MyBillVO mybillvo=new nc.vo.dip.warningset.MyBillVO() ;
		DipWarningsetVO[] warvo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, " tasktype='"+tasktype+"' and pk_bustab='"+pk_dataproce_h+"'");

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

		WarningsetDlg wd=new WarningsetDlg(getSelfUI(),mybillvo, isadd,hvo.getPk_xt(),1);		
		wd.showModal();		



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
		DipContdataVO hvo=null;
		try {
			hvo = (DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}
		//liyunzhe modify 如果没有自动对照定义且没有删除类型，就提示错误 2012-06-19 start  
		if((hvo.getDef_str_1()==null||!hvo.getDef_str_1().equals("Y"))&&(hvo.getDeletetype()==null||hvo.getDeletetype().trim().equals(""))){
			getSelfUI().showErrorMessage("没有自动对照的定义或者删除类型");
			return;
		}
		//liyunzhe modify 如果没有自动对照定义且没有删除类型，就提示错误 2012-06-19 end

		ControlHVO chvo=new ControlHVO();
		chvo.setBustype(SJUtil.getYWnameByLX(IContrastUtil.YWLX_DZ));
		chvo.setCode(hvo.getCode());
		chvo.setName(hvo.getName());

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPrimaryKey(),IContrastUtil.YWLX_DZ,hvo.getContabcode()+","+hvo.getExtetabcode());
		cd.showModal();
	}
	String pk;
	public void onBoAotoContData() {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null||tempNode.getData()==null){
			getSelfUI().showErrorMessage("请选择要增加的节点！");
			return ;
		}
		DipContdataVO hvo=(DipContdataVO) tempNode.getData();
		if(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("文件夹不能操作！");
			return ;
		}
		//liyunzhe modify 如果没有自动对照定义且没有删除类型，就提示错误 2012-06-19 start  
		if((hvo.getDef_str_1()==null||!hvo.getDef_str_1().equals("Y"))&&(hvo.getDeletetype()==null||hvo.getDeletetype().trim().equals(""))){
			getSelfUI().showErrorMessage("没有自动对照的定义或者删除类型");
			return;
		}
		//liyunzhe modify 如果没有自动对照定义且没有删除类型，就提示错误 2012-06-19 end
		Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否自动对照！");
		if(flag==1){
			pk=hvo.getPrimaryKey();
			new Thread() {
				@Override
				public void run() {
					BannerDialog dialog = new BannerDialog(getSelfUI());
					dialog.setTitle("正在自动对照，请稍候...");
					dialog.setStartText("正在自动对照，请稍候...");
								
					try {
						dialog.start();	
						ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
						RetMessage rm=ite.doAutoContData(pk);
						if(rm.getIssucc()){
							MessageDialog.showHintDlg(getSelfUI(), SJUtil.getYWnameByLX(IContrastUtil.YWLX_FS),rm.getMessage());
						}else{
							getSelfUI().showErrorMessage("自动对照失败！"+rm.getMessage());
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
	public void onBoAddFolder()  throws Exception{
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null||tempNode.getData()==null){
			getSelfUI().showErrorMessage("请选择要增加的节点！");
			return ;
		}
		DipContdataVO hvo=(DipContdataVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipContdataVO newhvo=new DipContdataVO();
		newhvo.setPk_sysregister_h(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipContdataVO[] listvos=(DipContdataVO[]) HYPubBO_Client.queryByCondition(DipContdataVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
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
				newhvo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, pk);
				
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
		DipContdataVO vo =(DipContdataVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipContdataVO[] listvos=(DipContdataVO[]) HYPubBO_Client.queryByCondition(DipContdataVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_contdata_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
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
					vo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, vo.getPrimaryKey());
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
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
		DipContdataVO vo =(DipContdataVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipContdataVO.class, "pk_sysregister_h='"+vo.getPrimaryKey()+"'");
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
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
//		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().updateButtonUI();
	}
}