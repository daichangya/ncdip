package nc.ui.dip.authorize.define;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.text.Document;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.ddc.IBizObjStorage;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.ref.DataDefinitTableTreeRefModel;
import nc.ui.bd.ref.DataDefinitbRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.AskDLG;
import nc.ui.dip.dlg.design.DataAuthDesignDLG;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.ddc.datadict.DDCBO_Client;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.authorize.define.DipADContdataBVO;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.authorize.define.MyBillVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
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

	private boolean isnew=false;

	@Override
	protected void onBoEdit() throws Exception {

		VOTreeNode treeNode=getSelectNode();
		isnew=false;
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		String fpk=(String)treeNode.getParentnodeID();
		if(fpk ==null || fpk.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做修改操作！"); 
			return;
		}/*else{
			查询NC系统下的节点，不允许做修改操作 2011-5-23 程莉 begin 
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+fpk+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能修改！"); 
				return;
			}
			 end 
		}*/
		super.onBoEdit();
		ContDataClientUI ui=(ContDataClientUI) getBillUI();
		//得到界面上主表的主键
		String pk=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_contdata_h").getValueObject();
		//如果主键是空的，那么就是新增，否则判断数据库中是否有这条主键的数据，如果没有的话就是新增，有的话，就是修改
		DipADContdataVO vo=null;
		if(pk==null||pk.trim().equals("")){
//			getBillUI().setBillOperate(IBillOperate.OP_ADD);
			isnew=true;
		}else{
			vo=(DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, pk);
			if(SJUtil.isNull(vo)){
//				getBillUI().setBillOperate(IBillOperate.OP_ADD);
				isnew=true;
			}else{
				isnew=false;
			}
			if("Y".equals(vo.getIsmaster())){
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_master").setEdit(false);
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ismobile").setEdit(true);
			}else{
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_master").setEdit(true);
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ismobile").setEdit(false);
			}
		}

		UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
		DipSysregisterHVO hvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, vo.getPk_xt());
		String ss=jcp.getText();
		docu=jcp.getUITextField().getDocument();
		jcp.getUITextField().setDocument(new StringDoc(jcp.getUITextField(),"DIP_AD_"+hvo.getCode()+"_"));
		jcp.getUITextField().setText(ss);
		onBoLineAdd();
		tempMybillVO=(MyBillVO) getBillUI().getVOFromUI();

	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		//2011-6-4
		VOTreeNode node=getSelectNode();
		
		if(docu!=null){
			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
			jcp.getUITextField().setDocument(docu);
			jcp.getUITextField().setText("DIP_DC_");
		}
		super.onBoCancel();
		tempMybillVO=null;
		if(node!=null){
			getSelfUI().onTreeSelectSetButtonState(node);
		}
		
		
		
	}

	@Override
	protected void onBoSave() throws Exception {
		/*2011-5-26
		 * 非空校验*/
		int rowcount=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
        if(rowcount>0){
        	List list=new ArrayList();
        	BillModel bodyBillModel =getBillCardPanelWrapper().getBillCardPanel().getBillModel();
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
        
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd!=null){
			bd.dataNotNullValidate();
		}
		String pk=(String)ui.getBillCardPanel().getHeadItem("pk_contdata_h").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		DipADContdataVO hvo=(DipADContdataVO) billVO.getParentVO();		
		setTSFormBufferToVO(billVO);

		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		DipADContdataVO dhvo=(DipADContdataVO) checkVO.getParentVO();	
		
		//存储表名转换成大写
		String middletab=dhvo.getMiddletab().toUpperCase();
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());		
		//2011-7-4 校验编码唯一 cl
		String code=(String) ui.getBillCardPanel().getHeadItem("code").getValueObject();
//		Collection ccode=bs.retrieveByClause(DipADContdataVO.class, " pk_sysregister_h='"+dhvo.getPk_sysregister_h()+"' and code='"+code+"' and pk_contdata_h <> '"+pk+"' and nvl(dr,0)=0");
		Collection ccode=bs.retrieveByClause(DipADContdataVO.class, " pk_xt='"+dhvo.getPk_xt()+"' and code='"+code+"' and pk_contdata_h <> '"+pk+"' and nvl(dr,0)=0");
		if(ccode !=null&&ccode.size()>0){
				getSelfUI().showErrorMessage(IContrastUtil.getCodeRepeatHint("编码", code));
				return;
		}
//		ccode=bs.retrieveByClause(DipADContdataVO.class, " pk_sysregister_h='"+dhvo.getPk_sysregister_h()+"' and name='"+dhvo.getName()+"' and pk_contdata_h <> '"+pk+"' and nvl(dr,0)=0");
//		if(ccode !=null&&ccode.size()>0){
//				getSelfUI().showErrorMessage("该名称【"+dhvo.getName()+"】已经存在！");
//				return;
//		}
		DipSysregisterHVO syshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, 
				getSelfUI().getBillCardPanel().getHeadItem("pk_xt").getValueObject().toString());
		String extcode=syshvo.getExtcode();
		int k=8+extcode.length();
		if(dhvo.getContcolcode()!=null&&dhvo.getContcolcode().length()>0){
			if(middletab.length()<=k){
				ui.showWarningMessage("请输入存储表名");//修改长度校验 2011-06-25 zlc 添加extcode 静态成员变量
//				ui.showWarningMessage("存储表名长度最少"+(k+1)+"位！");//修改长度校验 2011-06-25 zlc 添加extcode 静态成员变量
				return;
			}else{
				//校验表名是否重复
				Collection memoryName=bs.retrieveByClause(DipADContdataVO.class, "middletab='"+middletab.toUpperCase()+"' and nvl(dr,0)=0 and pk_contdata_h <>'"+pk+"'");
				if(memoryName !=null){
					if(memoryName.size()>=1){
						ui.showWarningMessage("该【"+middletab.toUpperCase()+"】存储表名已经存在！");
						return ;
					}
				}
			}
			if((checkVO.getChildrenVO()==null||checkVO.getChildrenVO().length<=0)){
				ui.showErrorMessage("请编辑授权角色！");
				return;
			}else{
				checkVO.getChildrenVO();
			}
		}
		dhvo.setMiddletab(middletab);
		
		//2011-7-14 begin
		VOTreeNode node=getSelectNode();
		String fpk=null;
		if(node !=null){
			fpk=(String) node.getParentnodeID();
			if(fpk==null || fpk.trim().equals("")){
				fpk=(String) node.getNodeID();
			}
		}

		setTSFormBufferToVO(checkVO);

		DipADContdataBVO[] bvo=(DipADContdataBVO[]) checkVO.getChildrenVO();
		List<DipADContdataBVO> l1=new ArrayList<DipADContdataBVO>();
		Map map=new HashMap();
		for(int i=0;i<bvo.length;i++){
			if(bvo[i].getPk_fp()!=null&&bvo[i].getPk_fp().length()>0){
				l1.add(bvo[i]);
				if(map.get(bvo[i].getPk_fp())!=null){
					ui.showErrorMessage("授权角色不能重复！");
					return;
				}else{
					map.put(bvo[i].getPk_fp(),"1");
				}
			}
			
		}
		
		//非主控节点必须选中参照主控节点
		if("N".equals(dhvo.getIsmaster())){
			String pk_master = dhvo.getPk_master();
			if(null == pk_master || "".equals(pk_master)){
				ui.showErrorMessage("请选择主控节点！");
				return;
			}
		}
//		else{
//			String sql = " contabcode='"+dhvo.getContabcode()+"' and ismaster='Y' and coalesce(dr,0)=0 ";
//			String pk_contdata_h = dhvo.getPk_contdata_h();
//			if(null != pk_contdata_h && !"".equals(pk_contdata_h)){
//				sql += " and pk_contdata_h<>'"+pk_contdata_h+"'";
//			}
//			DipADContdataVO[] vos = (DipADContdataVO[])HYPubBO_Client.queryByCondition(DipADContdataVO.class,sql);
//			if(vos.length>0){
//				ui.showErrorMessage("相同表已存在主控节点！");
//				return;
//			}
//		}
//		String wheresql = " contabcode='"+dhvo.getContabcode()+"' and contcolcode='"+dhvo.getContcolcode()+"' and coalesce(dr,0)=0 ";
//		String pk_contdata_h = dhvo.getPk_contdata_h();
//		if(null != pk_contdata_h && !"".equals(pk_contdata_h)){
//			wheresql += " and pk_contdata_h<>'"+pk_contdata_h+"'";
//		}
//		DipADContdataVO[] vos = (DipADContdataVO[])HYPubBO_Client.queryByCondition(DipADContdataVO.class,wheresql);
//		if(vos.length>0){
//			ui.showErrorMessage("相同控制字段已存在权限定义！");
//			return;
//		}
		
		if(l1!=null&&l1.size()>0){
			checkVO.setChildrenVO(l1.toArray(new DipADContdataBVO[l1.size()]));
		}else{
			checkVO.setChildrenVO(null);
		}
		DipADContdataBVO[] bvo1=(DipADContdataBVO[]) billVO.getChildrenVO();
		List<DipADContdataBVO> l2=new ArrayList<DipADContdataBVO>();
		if(null != bvo1 && bvo1.length>0){
			for(int i=0;i<bvo1.length;i++){
				if(bvo1[i].getPk_fp()!=null&&bvo1[i].getPk_fp().length()>0){
					l2.add(bvo1[i]);
				}
			}
		}
		if(l2!=null&&l2.size()>0){
			billVO.setChildrenVO(l2.toArray(new DipADContdataBVO[l2.size()]));
		}else{
			billVO.setChildrenVO(null);
		}
		boolean isrewritdf=false;
		boolean iscreattab=false;
		boolean isdelet=false;
		String mtabt="";
		String mtab="";
		//B--判断表结构的变化
		if(tempMybillVO!=null){
			DipADContdataVO hvo1=(DipADContdataVO) checkVO.getParentVO();
			DipADContdataVO hvot=(DipADContdataVO) tempMybillVO.getParentVO();
			mtabt=hvot.getMiddletab();//存储表名
			mtab=hvo1.getMiddletab();
			String ctabt=hvot.getContabcode();//对照表的表名
			String ctab=hvo1.getContabcode();
			String ftabt=hvot.getContcolcode();//对照表的字段
			String ftab=hvo1.getContcolcode();
			if(ftabt==null||ftabt.length()<=0){//原表不存在
				if(ftab!=null&&ftab.length()>0){//如果有新表
					isrewritdf=true;
					iscreattab=true;
				}
			}else if(mtab==null||mtab.length()==(k+1)||ftab==null||ftab.length()<=0){//原表在，新表不用建
				if(isTableExist(mtabt)){
					Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","不需要建表，是否删除原表？");
					if(flag==1){
						isdelet=true;
//						String sql="drop table "+mtabt;
//						iq.exectEverySql(sql);
//						deletDataDefinByTabname(mtabt);
					}else{
					
						return;
					}
				}
			}else if(!mtabt.equals(mtab)){//表名不一致，删除重建
				if(isTableExist(mtabt)){
					Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","数据存储表名变化，必须删除表重建。");
					if(flag==1){
						isdelet=true;
//						String sql="drop table "+mtabt;
//						iq.exectEverySql(sql);
//						deletDataDefinByTabname(mtabt);
						isrewritdf=true;
						iscreattab=true;
					}else{
						return;
					}
				}
				
			}else {
				isrewritdf=true;
				DipDatadefinitBVO bvot1=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, ftabt);
				DipDatadefinitBVO bvot2=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, ftab);
				if(bvot1.getType().equals(bvot2.getType())&&((bvot1.getLength()==null&&bvot2.getLength()==null)||(bvot1.getLength()!=null&&bvot2.getLength()!=null&&bvot1.getLength().intValue()==bvot2.getLength().intValue()))&&((bvot1.getDeciplace()==null&&bvot2.getDeciplace()==null)||(bvot1.getDeciplace()!=null&&bvot2.getDeciplace()!=null&&bvot1.getDeciplace().intValue()==bvot2.getDeciplace().intValue()))){
					if(isTableExist(mtabt)){
						String sql="select count(*) from "+mtabt;
						Integer it=Integer.parseInt(iq.queryfield(sql));
						String msg="数据存储结构没有变化，表中";
						if(it>0){
							msg=msg+"有数据，";
						}else{
							msg=msg+"没有数据，";
						}
						AskDLG ad=new AskDLG(getBillUI(),"提示","请选择？",new String[]{msg+"不删除表重建",msg+"删除表重建"});
						ad.showModal();
						if(ad.getRes()==1){
							isdelet=true;
	//						String sql2="drop table "+mtabt;
	//						iq.exectEverySql(sql2);
							iscreattab=true;
						}else if(ad.getRes()==0){
							getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
							getSelfUI().updateButtonUI();
						}else{
							return;
						}
					}else{
						iscreattab=true;
						isrewritdf=true;
					}
				}else{
					Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","数据存储表结构变化，必须删除表城建。");
					if(flag==1){
//						String sql="drop table "+mtabt;
//						iq.exectEverySql(sql);
						isdelet=true;
						isrewritdf=true;
						iscreattab=true;
					}else{
						return;
					}
				}
			}
		}else{
			iscreattab=true;
			isrewritdf=true;
		}
		
		//e--判断表结构的变化
		
		
		//end
//		checkVO.getChildrenVO();
		int nCurrentRow = -1;
		if(isAdding()){
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), billVO);

			//2011-6-4 begin
			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
			String ss=jcp.getText();
			jcp.getUITextField().setDocument(docu);
			jcp.getUITextField().setText(ss);

			setAddNewOperate(isAdding(), billVO);
			setSaveOperateState();
			if (nCurrentRow >= 0)
				getBufferData().setCurrentRow(nCurrentRow);

			//如果该单据增加保存时是自动维护树结构，则执行如下操作
			if (getUITreeCardController().isAutoManageTree()) {			
				getSelfUI().insertNodeToTree(billVO.getParentVO());
			}
		}else{
			String pk_datasynch=hvo.getPrimaryKey();
			iq.exesql(" delete from dip_adcontdata_b where pk_contdata_h='"+pk_datasynch+"'");
			DipADContdataBVO[] vo=(DipADContdataBVO[]) checkVO.getChildrenVO();
			if(vo!=null){
				for(int i=0;i<vo.length;i++){
					vo[i].setAttributeValue("pk_contdata_h", pk_datasynch);
				}
				HYPubBO_Client.insertAry(vo);
			}
//			HYPub
//			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), billVO);
			HYPubBO_Client.update((SuperVO) checkVO.getParentVO());
			/*DipADContdataBVO[] bvos=(DipADContdataBVO[]) billVO.getChildrenVO();
			List<DipADContdataBVO> uvos=new ArrayList<DipADContdataBVO>();
			List<DipADContdataBVO> ivos=new ArrayList<DipADContdataBVO>();
			if(bvos!=null&&bvos.length>0){
				for(int i=0;i<bvos.length;i++){
					if(bvos[i].getPrimaryKey()!=null&&bvos[i].getPrimaryKey().length()>0){
						uvos.add(bvos[i]);
					}else{
						bvos[i].setPk_contdata_h(pk_datasynch);
						ivos.add(bvos[i]);
					}
				}
			}
			if(uvos!=null&&uvos.size()>0){
				HYPubBO_Client.updateAry(uvos.toArray(new DipDatadefinitBVO[uvos.size()]));
			}
			if(ivos!=null&&ivos.size()>0){
				HYPubBO_Client.insertAry(ivos.toArray(new DipDatadefinitBVO[ivos.size()]));
			}*/

			MyBillVO billvo =new MyBillVO();
			billvo.setParentVO(HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, pk_datasynch));
			billvo.setChildrenVO(HYPubBO_Client.queryByCondition(DipADContdataBVO.class, "pk_contdata_h='"+pk_datasynch+"'"));
			getBufferData().setCurrentVO(billvo);
			nCurrentRow = getBufferData().getCurrentRow();

			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
			String ss=jcp.getText();
			jcp.getUITextField().setDocument(docu);
			jcp.getUITextField().setText(ss);

			setAddNewOperate(isAdding(), billvo);

			setSaveOperateState();
			if (nCurrentRow >= 0)
				getBufferData().setCurrentRow(nCurrentRow);

			//如果该单据增加保存时是自动维护树结构，则执行如下操作
			if (getUITreeCardController().isAutoManageTree()) {			
				getSelfUI().insertNodeToTree(billvo.getParentVO());
			}
			
			
		}
		hvo=(DipADContdataVO) billVO.getParentVO();
//		if(tempMybillVO!=null){
//			
//		}else{
		/*	if(isTableExist(hvo.getMiddletab())){
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","存储表已经存在，是否删除重新定义？");
				if(flag==1){
					String sql="drop table "+hvo.getMiddletab();
					iq.exectEverySql(sql);
					if(hvo.getContcolcode()!=null&&hvo.getContabcode().length()>0){
						rewriteDatadefin(dhvo);
					}
				}
			}else{*/
//				if(hvo.getContcolcode()!=null&&hvo.getContabcode().length()>0){
//					rewriteDatadefin(dhvo);
//				}
//			}
//		}
				rewriteDatadefn(mtabt,mtab,isrewritdf,
				iscreattab,dhvo,isdelet);
//				DipADContdataVO datavo=(DipADContdataVO) node.getData();
//				if(datavo!=null&&datavo.getIsfolder()!=null&&datavo.getIsfolder().booleanValue()){
//					
//				}else{
//					getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//					getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//					
//				}
				if(getSelectNode()!=null){
					getSelfUI().onTreeSelectSetButtonState(getSelectNode());
				}
				getSelfUI().updateButtonUI();
//		tempMybillVO=(MyBillVO) getBillUI().getVOFromUI();
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
			boolean iscreattab,DipADContdataVO contdataVO,boolean isdelete) throws Exception{
	//	String pk=contdataVO.getPk_sysregister_h();
		String pk_xt=contdataVO.getPk_xt();
		if(isdelete){
			String sql="drop table "+mtabt;
			iq.exectEverySql(sql);
		}
		deletDataDefinByTabname(mtabt);
		if(!isrewritdf){
			return;
		}
		
		boolean suc=false;
//		if(!isTableExist(contdataVO.getMiddletab())){
			DipDatadefinitBVO[] bvoinert=new DipDatadefinitBVO[3];
			String ccode=contdataVO.getContcolcode();
			String ecode=contdataVO.getExtecolcode();
			if(ccode==null||ccode.length()<=0||ecode==null||ecode.length()<=0){
			}else{
				try {
//					if(!isrewritdf||!iscreattab){
//						return;
//					}
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
					bvoinert[2].setLength(bvos[1].getLength());
					bvoinert[2].setDeciplace(bvos[1].getDeciplace());
					
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

				String d="select isdeploy from dip_sysregister_h where pk_sysregister_h='"+pk_xt+"' and nvl(dr,0)=0";
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
				String pkf=getPkf(pk_xt);
				//向数据定义中插入该中间表的表结构
				DipDatadefinitHVO hdvo=new DipDatadefinitHVO();
				hdvo.setSyscode("*"+contdataVO.getCode());//编码
				hdvo.setSysname(contdataVO.getName());//名称
				hdvo.setMemorytype("表");//存储类型
				hdvo.setDatatype(IContrastUtil.DATASTYLE_SYSTEMINF);//2011-7-21增加： 数据类型：对照信息结构
				hdvo.setMemorytable(contdataVO.getMiddletab());//存储表名
				hdvo.setIscreatetab("Y");//是否已建表
				hdvo.setTabsoucetype("数据权限定义");//表来源类型
				hdvo.setDispostatus("");//控制处理状态
				hdvo.setEndstatus("");//控制结束状态
				hdvo.setPk_sysregister_h(pkf);//父节点主键
				hdvo.setIsfolder(new UFBoolean(false));
				hdvo.setPk_xt(pk_xt);
				hdvo.setDef_str_1(contdataVO.getPrimaryKey());
				hdvo.setIsdeploy(new UFBoolean(true));
				
				String st=HYPubBO_Client.insert(hdvo);//主键
				hdvo.setPk_datadefinit_h(st);
				
				//查询列名以及对应的类型
				
				//try {
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
				DipSysregisterHVO hvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk_xt);
//					String sql2="select code from dip_sysregister_h where pk_sysregister_h='"+pk+"' and nvl(dr,0)=0";
//					String code=iq.queryfield(sql2);
				String code=hvo.getCode();
				if(code==null||"".equals(code)){
					return;
				}
//				createForder(hvo.getExtname(), code,IContrastUtil.DATADICTFORDER_CODE);
//				Datadict m_dict = new Datadict();
//				DDCData data = null;
//				ObjectNode[] objNodes = null;
//				BizObject[] objs = null;
//				data=DDCBO_Client.fromDBMetaData(IContrastUtil.DESIGN_CON,new String[]{st} , 0, code);
//				objNodes = (ObjectNode[]) data.getNode().toArray(new ObjectNode[0]);
//				objs = (BizObject[]) data.getTable().toArray(new BizObject[0]);
//				m_dict.saveObjectNode(objNodes, objs, 20);
			}
//liyunzhe modify 回写数据定义错误
//				} catch (SQLException e) {
//					e.printStackTrace();
//				} catch (DbException e) {
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}			
//			}
//		}
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
			fo.setDisplayName(id);
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
	public void rewriteDatadefin(DipADContdataVO contdataVO) throws UifException{
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
				hdvo.setSyscode("*"+contdataVO.getCode());//编码
				hdvo.setSysname(contdataVO.getName());//名称
				hdvo.setMemorytype("表");//存储类型
				hdvo.setDatatype(IContrastUtil.DATASTYLE_SYSTEMINF);//2011-7-21增加： 数据类型：对照信息结构
				hdvo.setMemorytable(contdataVO.getMiddletab());//存储表名
				hdvo.setIscreatetab("Y");//是否已建表
				hdvo.setTabsoucetype("数据对照定义");//表来源类型
				hdvo.setDispostatus("");//控制处理状态
				hdvo.setEndstatus("");//控制结束状态
				hdvo.setPk_sysregister_h(pk);//父节点主键
				hdvo.setDef_str_1(contdataVO.getPrimaryKey());
				hdvo.setIsdeploy(de);
				hdvo.setBusicode(contdataVO.getMiddletab());
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
		VOTreeNode nn=(VOTreeNode) tempNode.getParent();
		String fpk=tempNode.getParentnodeID()==null?"":tempNode.getParentnodeID().toString();
		if(fpk==null || fpk.trim().equals("")){
			getSelfUI().showWarningMessage("系统节点不能做删除操作！");
			return ;
		}/*else{
			 2011-5-23 NC系统下的节点不能删除 
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+tempNode.getNodeID()+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能删除！"); 
				return;
			}
			 end 
		}*/
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确认要删除?");
		if(flag == 1){
			String pk=(String) tempNode.getNodeID();
			//2011-7-12
			DipADContdataVO hvo=(DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, pk);
			String middletab=hvo.getMiddletab();	

			HYPubBO_Client.deleteByWhereClause(DipADContdataBVO.class, "pk_contdata_h='"+pk+"'");  
			HYPubBO_Client.delete(HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class,pk));
		
			//HYPubBO_Client.deleteByWhereClause(ContdataBVO.class, " pk_contdata_h ='"+pk+"'");
			//HYPubBO_Client.deleteByWhereClause(ContdataB2VO.class, " pk_contdata_h ='"+pk+"'");

			//2011-7-12
			delete(fpk,middletab);

			//更新树
			this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());

		}
		
		if(nn!=null&&nn.getPath()!=null){
			getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(nn.getPath()));			
		}
		
		
	}

	/**
	 * @author 程莉
	 * @date 2011-7-12
	 * @desc 删除时，删除中间表，同时判断该中间表是否回写到数据定义了，如果回写到数据定义，则删除
	 * @param 系统注册主键 pk_sysregister_h、中间表名：middletab
	 */
	public void delete(String pk_sysregister_h,String middletab){
		if(pk_sysregister_h !=null && middletab !=null){
			//删除中间表
			String sql="drop table "+middletab;
			try {
				deletDataDefinByTabname(middletab);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				iq.exesql(sql);
//				//根据pk_sysregister_h和middletab查询数据定义中是否有数据，有数据则回写到数据定义了,进行删除操作
//				DipDatadefinitHVO[] hvo=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, " pk_sysregister_h='"+pk_sysregister_h+"' and memorytable='"+middletab+"' and nvl(dr,0)=0");
//				if(hvo !=null){
//					String pk_datadefinit_h=hvo[0].getPk_datadefinit_h();
//					DipDatadefinitBVO[] bvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+pk_datadefinit_h+"' and nvl(dr,0)=0");
//					if(bvo !=null){
//						//HYPubBO_Client.deleteByWhereClause(DipDatadefinitBVO.class, " pk_datadefinit_h='"+pk_datadefinit_h+"' and nvl(dr,0)=0");
//						//HYPubBO_Client.deleteByWhereClause(DipDatadefinitHVO.class," pk_datadefinit_h='"+pk_datadefinit_h+"' and nvl(dr,0)=0");
//						String delDefBvo="delete from dip_datadefinit_b where pk_datadefinit_h='"+pk_datadefinit_h+"' and nvl(dr,0)=0";
//						String delDefHvo="delete from dip_datadefinit_h where pk_datadefinit_h='"+pk_datadefinit_h+"' and nvl(dr,0)=0";
//						String delNC="delete from pub_datadict where id='"+middletab+"' and nvl(dr,0)=0";
//						String delNCdetail="delete from pub_datadictdetail  where  reserved3='"+middletab+"' and nvl(dr,0)='0' ";
//						iq.exesql(delDefBvo);
//						iq.exesql(delDefHvo);
//						iq.exesql(delNC);
//						iq.exesql(delNCdetail);
//					}
//				}
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}

	@Override 
	public void onTreeSelected(VOTreeNode selectnode) {
		VOTreeNode treeNode=getSelectNode();
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
					syshvo = (DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, treeNode.getParentnodeID().toString());
				} catch (UifException e) {
					e.printStackTrace();
				}
			}
		}

		super.onTreeSelected(selectnode);
		SuperVO vo=(SuperVO) selectnode.getData();
//		获取VO
		try {


			MyBillVO billvo =new MyBillVO();
			billvo.setParentVO(vo);
			// 显示数据
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(selectnode.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));

			DipADContdataVO headvo = (DipADContdataVO)vo;
			String pk_xt=headvo.getPk_xt()==null?headvo.getPrimaryKey():headvo.getPk_xt();
			/*
			 * 修改表头和表体的对照、被对照物理字段的参照筛选条件
			 * 使修改状态下也能在对照系统物理表和被对照系统物理表字段所选的表的字段内做参照选择
			 * 2011-5-17
			 * zlc*/
			String tablename=headvo.getContabcode();
			//页面加载 
			//表头,限制非0000nc系统的表
			
			
			
			UIRefPane pane1=(UIRefPane) ui.getBillCardPanel().getHeadItem("contabcode").getComponent();
			
			/*liyunzhe modify 来源表修改成树形参照 2012-06-04 strat*/
			DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
			model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+headvo.getPk_xt()+"'");
			model.addWherePart(" and tabsoucetype='自定义'");
			pane1.setRefModel(model);
			/*liyunzhe modify 来源表修改成树形参照 2012-06-04 end*/
			
			if(tablename!=null&&tablename.length()>0){
				UIRefPane pa=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("contcolcode").getComponent();
				DataDefinitbRefModel modelB=new DataDefinitbRefModel();
				modelB.addWherePart(" and dip_datadefinit_B.pk_datadefinit_h='"+tablename+"'  and( dip_datadefinit_b.isquote = 'Y'  ) and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa.setRefModel(modelB);
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
//	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		isnew=true;
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择系统节点做增加操作！");
			return ;
		}
		String ss=(String)treeNode.getParentnodeID();
		if(ss!=null&&ss.length()>0){
			DipADContdataVO vo=(DipADContdataVO) treeNode.getData();
			if(vo!=null&&vo.getIsfolder()!=null&&vo.getIsfolder().booleanValue()){
				
			}else{
				getSelfUI().showErrorMessage("不能做增加操作！");
				getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				ui.updateButtons();
				return ;	
			}
		}
		//2011-07-06 
		super.onBoAdd(bo);
		getSelfUI().getBillCardPanel().setHeadItem("pk_sysregister_h", treeNode.getNodeID());
		

		DipADContdataVO hvo=(DipADContdataVO)treeNode.getData();
		String pk_xt=hvo.getPk_xt();
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt",hvo.getPk_xt());
		
		String pk=null;
		/*if(hvo.getContcolcode()!=null){

	    }*/
		if(hvo.getPk_contdata_h()!=null){
			pk=hvo.getPk_contdata_h();
		}

		getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("contsys").getLoadFormula());

		//2011-6-4

		UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("middletab").getComponent();
		docu=jcp.getUITextField().getDocument();

		DipSysregisterHVO syshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk_xt);
		String extcode=syshvo.getExtcode();
		jcp.getUITextField().setDocument(new StringDoc(jcp.getUITextField(),"DIP_AD_"+extcode.toUpperCase()+"_"));
		jcp.getUITextField().setText("DIP_AD_"+extcode.toUpperCase()+"_");
		onBoLineAdd();
		tempMybillVO=null;
	}

	Document docu;
	/*public void onTreeSelected(VOTreeNode arg0){
SuperVO vo=(SuperVO) arg0.getData();

	MyBillVO billvo =new MyBillVO();
	// 设置子VO
	billvo.setParentVO(vo);
	billvo.setChildrenVO(null);
	// 显示数据
	getBufferData().addVOToBuffer(billvo);
	getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1)); 


	getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
}

	/*public void onTreeSelected(VOTreeNode arg0){
		//获取VO
		try {
			SuperVO vo=(SuperVO) arg0.getData();
			IQueryField query = (IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());

			//2011-4-29 程莉 描述：得到父主键，根据父主键查询名称，用来显示在对照系统 begin
			String treeNode=(String) arg0.getParentnodeID();
			String strsql="select name from dip_contdata where pk_contdata_h='"+treeNode+"'";
			String strname=null;
			strname=query.queryfield(strsql);
			vo.setAttributeValue("extesys", strname);
			//end

			//拿到主键
			String pk_contdata_h=(String) vo.getAttributeValue("pk_contdata_h");
			String code=(String) vo.getAttributeValue("code");
			String name=(String) vo.getAttributeValue("name");
			String sql = "select memorytable from dip_datadefinit_h where syscode='"+ code +"' and nvl(dr,0)=0";
			String tableName = "";
			tableName = query.queryfield(sql);
			sql = "select pk_datadefinit_h from dip_datadefinit_h where syscode='"+ code +"' and nvl(dr,0)=0";
			String pk = query.queryfield(sql);
			vo.setAttributeValue("extetabcode", tableName);
			vo.setAttributeValue("extetabname", name);
//			String pk_datadefinit_h = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("").getValue();

			if(null!=pk&&!"".equals(pk)){
				UIRefPane ref = (UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("extecolname").getComponent();
				AbstractRefModel model = ref.getRefModel();
				if(null!=model){
					model.setWherePart(" dip_datadefinit_b.pk_datadefinit_h='"+pk+"'");
				}

			}

			System.out.println("=="+pk_contdata_h+"==");
//			SuperVO[] vos = HYPubBO_Client.queryByCondition(nc.vo.jyprj.contdata.DipContdataVO.class, " pk_contdata_h='" + pk_contdata_h + "' and isnull(dr,0)=0  ");

			nc.vo.jyprj.contdata.MyBillVO billvo =new nc.vo.jyprj.contdata.MyBillVO();

			//设置主VO
			billvo.setParentVO(vo);
			//设置子VO
			billvo.setChildrenVO(null);
			// 显示数据
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	}*/

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
		
		if(tableName.toLowerCase().startsWith("v_dip")){
			isExist=true;
		}else {
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
		}
		return isExist;
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
		DipADContdataVO vo =(DipADContdataVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipADContdataVO[] listvos=(DipADContdataVO[]) HYPubBO_Client.queryByCondition(DipADContdataVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_contdata_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
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
					vo=(DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, vo.getPrimaryKey());
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
		VOTreeNode node=(VOTreeNode) tempNode.getParent();
		DipADContdataVO vo =(DipADContdataVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipADContdataVO.class, "pk_sysregister_h='"+vo.getPrimaryKey()+"'");
			if(hvos!=null&&hvos.length>0){
				throw new Exception("文件夹下已经有数据定义，不能删除！");
			}else{
				HYPubBO_Client.delete(vo);
				getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
				
				getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
				getSelfUI().updateButtonUI();
				
				if(node!=null&&node.getPath()!=null){
					getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node.getPath()));
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
				DipADContdataVO hvo=(DipADContdataVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_SJQX,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_SJQX), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, hvo.getPrimaryKey());
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
		DipADContdataVO hvo=(DipADContdataVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipADContdataVO newhvo=new DipADContdataVO();
		newhvo.setPk_sysregister_h(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipADContdataVO[] listvos=(DipADContdataVO[]) HYPubBO_Client.queryByCondition(DipADContdataVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
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
				newhvo=(DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, pk);
				
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
	
	@Override
	protected void onBoLineDel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineDel();
	}
	
	//设置按钮
	public void onBoSet() {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择要导入的节点！");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		DipADContdataVO vo=(DipADContdataVO) treeNode.getData();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做保存操作！"); 
			return;
		}
		if("N".equals(vo.getIsmaster())){
			getSelfUI().showWarningMessage("非主控节点不能设置！"); 
			return;
		}
		DataAuthDesignDLG dlg=null;
		if(vo.getContcolcode()!=null&&vo.getContcolcode().length()>0){
			dlg=new DataAuthDesignDLG(getSelfUI(),new String[]{vo.getPk_contdata_h()},new String[]{vo.getContcolcode()},true,vo.getIsmobile());
//			dlg=new DesignDLG(getSelfUI(),new String[]{vo.getExtetabcode()},new String[]{vo.()});
		}else{
			dlg=new DataAuthDesignDLG(getSelfUI(),new String[]{vo.getPk_contdata_h()});
		}
		dlg.showModal();
	}
	
	public void onBoActionSet() {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择要导入的节点！");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		DipADContdataVO vo=(DipADContdataVO) treeNode.getData();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做保存操作！"); 
			return;
		}
		if("N".equals(vo.getIsmaster())){
			getSelfUI().showWarningMessage("非主控节点不能操作设置！"); 
			return;
		}
		BillCardPanel billCardPanel = getBillCardPanelWrapper().getBillCardPanel();
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(row <0){
			getSelfUI().showWarningMessage("请先选择表体角色组！"); 
			return;
		}
		Object bodyValueAt = billCardPanel.getBodyValueAt(row, "pk_fp");
		HashMap map = new HashMap();
		map.put("adcontdatevo", vo);
		map.put("fpk", vo.getPrimaryKey());
		map.put("pk_fp",bodyValueAt.toString());
		ActionSetDlg dlg = new ActionSetDlg(getSelfUI(),new UFBoolean(true),map);
		dlg.show();
	}
	
}