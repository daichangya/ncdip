package nc.ui.dip.statemanage;

import java.sql.SQLException;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.statemanage.DipStateManageHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;




/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
public class StateManageUI extends AbstractStateManageUI {
	private String YWLX="";
//	public String getYWLX(){
//		return YWLX;
//	}
	private String lob=null;

	public String delstr =" ";//删行删除条件, cl 2011-07-04
	public StateManageUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
		getButtonManager().getButton(IBtnDefine.DataProce).setName("执行");
		try {
			this.updateButtonUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 字段描述 */
	private static final long serialVersionUID = 5692169789554885827L;
	public  int FF=0;//判断是增加按钮还是别的按钮。如果是增加按钮则FF=1。
	public String selectnode = "";//选择树的节点

	//2011-3-22 12:42 chengli 在制作左树右表时添加 begin
	protected CardEventHandler createEventHandler() {		
		return new MyEventHandler(this, getUIControl());
	}
	public String getRefBillType() {
		return null;
	}
	/** end */

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {	}

	protected void initSelfData() {
		//2011-7-20 
		UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("firsttab").getComponent();
		AbstractRefModel model=pane.getRefModel();
		pane.setMultiSelectedEnabled(true);//设置为true时，多选
		pane.setRefModel(model);
	}

	/**
	 * 2011-3-22 14:45 chengli
	 * 修改此方法初始化模板控件数据
	 */
	@SuppressWarnings("unused")
	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	}


	//2011-3-22 12:42 chengli 在制作左树右表时添加 begin_2
	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new nc.ui.dip.statemanage.SampleTreeCardData();
	}
	//end_2

	public void afterInit() throws Exception {

		super.afterInit();
		YWLX=SJUtil.getYWnameByLX(IContrastUtil.YWLX_JG);
		//给根节点赋名称
		this.modifyRootNodeShowName(YWLX);

		//页面加载时,将"删除"按钮设置为不可用
		//getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		//页面加载时,将"修改"按钮设置为不可用
		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

		updateButtonUI();
	}
	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}

	@Override
	public boolean afterTreeSelected(VOTreeNode node) {
//		selectnode = node.getNodeID().toString();
//
//		System.out.println("node:"+selectnode);
		return super.afterTreeSelected(node);
	}
	
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	//2011-7-20 多选 原始数据 存值
	public void getFirstData(String tab){
		VOTreeNode node=getBillTreeSelectNode();
		if(node !=null){
			if(node.getParentnodeID() !=null && node.getNodeID() !=null){
				UIRefPane pane=(UIRefPane)getBillCardPanel().getHeadItem("firsttab").getComponent();
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
	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {
		DipStateManageHVO hvo=null;
		if ("root".equals(snode.getNodeID().toString().trim())){
			selectnode = "";
		}else{
			VOTreeNode treeNode=(VOTreeNode)snode;
			 hvo=(DipStateManageHVO) treeNode.getData();
		}
		super.onTreeSelectSetButtonState(snode);
		//getButtonManager().getButton(201).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
        
		try{
			
			String str = (String)snode.getParentnodeID();
//			snode.getd
			/*查询NC系统下的节点，不允许做增加操作 */
//			IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//			String sql="select h.pk_datachange_h from dip_datachange_h h where h.code='0000' and h.pk_datachange_h='"+str+"' and nvl(h.dr,0)=0";
//			String isNC=iq.queryfield(sql);
			if(str!=null&&str.length()>0&&snode!=snode.getRoot()){
//				&&hvo!=null&&!(hvo.isfolder==null?false:hvo.isfolder.booleanValue())
				if(hvo!=null&(hvo.isfolder==null?false:hvo.isfolder.booleanValue())){
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//					this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(false);
//					this.getButtonManager().getButton(IBtnDefine.DataProcessCheck).setEnabled(false);
//					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
//					this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				}else{
					this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(true);
//					this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(true);
//					this.getButtonManager().getButton(IBtnDefine.DataProcessCheck).setEnabled(true);
//					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(true);
//					this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
				}
				
				//				this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);

				updateButtonUI();
			}else if(snode==snode.getRoot()){//根节点
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
				updateButtonUI();
			}else{
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.DataProcessCheck).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
////				this.getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
				if(snode.getParentnodeID()==null){
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
				}else{
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
				}
				updateButtonUI();
			}
		}catch (Exception e) {
		}

		//2011-6-28 cl 原始表名无显示值问题
		/*if(selectnode !=null &&!"".equals(selectnode) && selectnode.length()>0){
			try {
				DipDataproceHVO hvo=(DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, selectnode);
				if(hvo !=null){
					String firsttab=hvo.getFirsttab();
					if(firsttab !=null && !"".equals(firsttab)&& firsttab.length()>0){
						DipDatadefinitHVO ddhvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, firsttab);
						String memorytable=ddhvo.getMemorytable();
						this.getBillCardPanel().getHeadItem("firstdata").setValue(memorytable);
					}
				}
			} catch (UifException e) {
				e.printStackTrace();
			}
		}*/
		((MyEventHandler)this.getCardEventHandler()).rewrit();
	}

	/**
	 * @author cl
	 * @date 2011-6-22
	 * @param tab
	 * @param proceTab
	 * @param isAddProceTab
	 * @param proceType
	 * @throws Exception
	 */
//	private void setProceBtnIsEnable(String tab, String proceTab, UFBoolean isAddProceTab, String proceType) throws Exception {
//		if(tab!=null && !"".equals(tab)&& tab.length()>0){
//			boolean isExist=isTableExist(tab);	
//			if(isExist==false){
//				this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//				this.updateButtons();
//			}else{
//				if("数据汇总".equals(proceType) || "数据转换".equals(proceType)){		
//					//新建加工表为null或者N
//					if(isAddProceTab==null || !isAddProceTab.booleanValue()){
//						this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//						this.getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
//						updateButtonUI();
//					}else{
//						//判断加工表是否已经在数据库中存在
//						boolean isProceTab=isTableExist(proceTab);
//						if(isProceTab){
//							getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
//							getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(true);
//							updateButtonUI();
//						}else{
//							getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
//							getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//							updateButtonUI();
//						}
//					}
//				}else /*if("数据清洗".equals(proceType) ||"数据卸载".equals(proceType) || "数据预置".equals(proceType))*/{
//					getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
//					updateButtonUI();
//				}
//			}
//		}		
//	}

	/**
	 * @author cl
	 * @date 2011-6-22
	 * @param tab
	 */
	IUAPQueryBS queryBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//	private boolean isTableExist(String tab) {
//		boolean isExist=false;//默认没有此表
//		String sql="select 1 from user_tables where table_name='"+tab.toUpperCase()+"';";
//		try {
//			ArrayList arr=(ArrayList)queryBS.executeQuery(sql,new MapListProcessor());
//
//			if(arr.size()>=1){
//				isExist=true;
//			}
//		} catch (BusinessException e) {
//			e.printStackTrace();
//		}
//		return isExist;
//	}


	@Override
	public void afterEdit(BillEditEvent e) {

		super.afterEdit(e);
		String extcode=MyEventHandler.extcode;
//		if("isadd".equals(e.getKey())){
//			String isadd=getBillCardPanel().getHeadItem("isadd").getValue();
//			if(isadd.equals("false")||isadd.equals("N")||"".equals(isadd)){
//				this.getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").clearViewData();
//				this.getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").setEnabled(false);
//				int rows=this.getBillCardPanel().getBillTable().getRowCount();
//				for(int i=0;i<rows;i++){
////					this.getBillCardWrapper().getBillCardPanel().getBodyPanel().enable(false);
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "cname", false);//中文名称
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ename", false);//英文名称
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "type", false);//类型
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "length", false);//长度
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "deciplace", false);//小树位数
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ispk", false);//是否主键
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isonlyconst", false);//唯一约束
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isquote", false);//引用
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isimport", false);//是否必输
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "quotetable", false);//引用表
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "quotecolu", false);//引用列
//
//				}
//			}else{
//				this.getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").setEnabled(true);
//				int rows=this.getBillCardPanel().getBillTable().getRowCount();
//				for(int i=0;i<rows;i++){
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "cname", true);//中文名称
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ename", true);//英文名称
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "type", true);//类型
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "length", true);//长度
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "deciplace", true);//小树位数
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ispk", true);//是否主键
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isonlyconst", true);//唯一约束
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isquote", true);//引用
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isimport",true);//是否必输
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "quotetable",true);//引用表
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "quotecolu",true);//引用列
//				}
//				/*修改加工表名为以dip_dd_+系统注册的外部系统编码 开头
//				 * 通过加工vo取得系统注册主键fpk，由fpk找到对应的系统注册vo找到外部系统编码的值，加到dip_dd_后面
//				 * 2011-06-24   
//				 * zlc  begin*/
//				try {
//					DipStateManageHVO prhvo=(DipStateManageHVO)this.getVOFromUI().getParentVO();
//					String fpk=prhvo.getFpk();
//					DipSysregisterHVO syshvo=(DipSysregisterHVO)HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, fpk);
//					String syscode=syshvo.getCode();
//					String  procetab=(String) this.getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").getValueObject();
//					if(!syscode.equals("0000")){
//						getBillCardPanel().getHeadItem("procetab").setValue("DIP_DD_"+extcode.toUpperCase()+"_");
//					}
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//
//		}

//		if("def_str_2".equals(e.getKey())){
//			getBillCardPanel().setHeadItem("refprocecond", "条件保存在下边");
//			getBillCardPanel().setHeadItem("procecond", "");
//			if("数据清洗".equals(e.getValue())||"数据预置".equals(e.getValue())||"数据卸载".equals(e.getValue())){
//				//2011-6-22 cl
//				getBillCardPanel().getHeadItem("isadd").setValue(false);
//				getBillCardPanel().getHeadItem("isadd").setEnabled(false);
//
//				getBillCardPanel().getHeadItem("procetab").setValue(null);
//				getBillCardPanel().getHeadItem("procetab").setEnabled(false);
//				if("数据卸载".equals(e.getValue())){
//					getBillCardPanel().getHeadItem("refprocecond").setValue("条件保存在下边");
//					getBillCardPanel().getHeadItem("procecond").setValue("DIP_BAK_TS");
//				}
//
//			}else{
//				getBillCardPanel().getHeadItem("isadd").setEnabled(false);
//				getBillCardPanel().getHeadItem("isadd").setValue(true);
//				getBillCardPanel().getHeadItem("procetab").setEnabled(true);
//				if(!extcode.equals("NCXT")){                                       //判断nc系统下存储表名没限制，其它系统下以 dip_dd_+外部系统编码+_ 限制  2011-06-25 zlc
//					getBillCardPanel().getHeadItem("procetab").setValue("DIP_DD_"+extcode.toUpperCase()+"_");
//				}
//			}
////			if(e.getValue().equals("数据汇总")){
////				//2011-6-23 cl 如果是数据汇总，新建加工表不可编辑
////				getBillCardPanel().getHeadItem("isadd").setEnabled(false);
////				getBillCardPanel().getHeadItem("isadd").setValue(true);
////				getBillCardPanel().getHeadItem("procetab").setEnabled(true);
////				if(!extcode.equals("NCXT"))                                          //判断nc系统下存储表名没限制，其它系统下以 dip_dd_+外部系统编码+_ 限制  2011-06-25 zlc
////					getBillCardPanel().getHeadItem("procetab").setValue("DIP_DD_"+extcode.toUpperCase()+"_");
////			}
//		}
		//2011-06-09 liyunzhe end
		/*if("firsttab".equals(e.getKey())){
			//liyunzhe 2011-06-04 start
			String tablename=((UIRefPane)getBillCardPanel().getHeadItem("firsttab").getComponent()).getSelectedData().get(0).toString().split(",")[0].substring(1);
			((UIRefPane)getBillCardPanel().getHeadItem("firstdata").getComponent()).setText(tablename);
			//this.getBillCardPanel().getHeadItem("firstdata").setValue(tablename);
			if(FF==1){//如果是增加按钮
				Object pk=getBillCardPanel().getHeadItem("firsttab").getValueObject();
				DipDatadefinitBVO[] dipdatavo=null;
				if(!SJUtil.isNull(pk)){

					try {
						String sql="pk_datadefinit_h='"+pk.toString()+"'";
						dipdatavo	=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, sql);

					} catch (UifException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.getBillCardWrapper().getBillCardPanel().getBillModel().clearBodyData();
					this.getBillCardWrapper().getBillCardPanel().getBillModel().setBodyDataVO(dipdatavo);
				}
			}
//			liyunzhe 2011-06-04 end
		}*/

		/*2011-5-19 程莉 给char、varchar、number类型在没有输入长度时设置默认长度 begin */
//		if("type".equals(e.getKey())){
//			int row=e.getRow();
//			Object o=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "type");
//			if(o!=null){
//				String type=o.toString();
//
//				if(type.toLowerCase().contains("char")){
//					if(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")==null||
//							(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")).equals("") ){
//						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(100, row, "length");
//					}
//				}else if(type.equals("Number".toUpperCase())){
//					if(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")==null||
//							(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")).equals("")){
//						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(20, row, "length");
//						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(8, row,"deciplace");
//					}
//
//				}
//			}
//		}

//		if("procetab".equals(e.getKey())&&"数据转换".equals(e.getValue())){
//			UIRefPane ref3=(UIRefPane)this.getBillCardPanel().getHeadItem("procetab").getComponent();
//			String newtable=null;
//			if(ref3!=null){
//				newtable=ref3.getText();
//			}
//			if(newtable!=null){
//				UIRefPane ref=(UIRefPane)this.getBillCardPanel().getHeadItem("refprocecond").getComponent();
//				if(ref!=null){
//					UIRefPane ref2=(UIRefPane)this.getBillCardPanel().getHeadItem("firstdata").getComponent();
//					String where=ref.getText();
//					String sql="insert into "+ newtable+" select * from "+ref2.getText()+" "+where.split("where")[1];
//					ref.setText(sql);
//				}
//
//			}
//		}
		/* end */
		/*将英文名称转换为大写 2011-06-24*/
//		if("ename".equals(e.getKey())){
//			int row=e.getRow();
//			Object o= this.getBillCardPanel().getBillModel("dip_dataproce_b").getValueAt(row, "ename");
//			String str=null;
//			if(o!=null&&o.toString().length()>0){
//				str=o.toString().toUpperCase();
//				this.getBillCardPanel().getBillModel("dip_dataproce_b").setValueAt(str, row,"ename");
//			}		
//		}

		//2011-6-24 cl 引用表
//		if("def_quotetable".equals(e.getKey())){			
//			//执行显示公式
//			int row=e.getRow();
//			String pk_datadefinit_h= this.getBillCardPanel().getBodyItem("def_quotetable").getValueObject()==null?"":this.getBillCardPanel().getBodyItem("def_quotetable").getValueObject().toString();
//
//			if(!"".equals(pk_datadefinit_h)){
//				this.getBillCardPanel().setBodyValueAt(new UFBoolean(true), row, "isquote");
//			}else{
//				this.getBillCardPanel().setBodyValueAt(new UFBoolean(false), row, "isquote");
//			}
//			this.getBillCardPanel().execBodyFormulas(row, getBillCardPanel().getBodyItem("def_quotetable").getEditFormulas());//.execBodyFormula(row, "def_quotetable");
//
//		}

		//2011-7-20
		if("firsttab".equals(e.getKey())){
			UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("firsttab").getComponent();
			String[] pk=pane.getRefPKs();
			String firstdata=(String) e.getValue();
			if(pk !=null && pk.length>0){
				for(int i=0;i<pk.length;i++){
					/**
					 * 组合sql语句查询
					 * 第一个参数：查询的表名；第二个参数：要显示的字段名；第三个参数：where条件
					 */
					if(i==0){
						try {
							firstdata=(String) HYPubBO_Client.findColValue("dip_datadefinit_h", "memorytable", "pk_datadefinit_h='"+pk[i]+"' and nvl(dr,0)=0");
						} catch (UifException e1) {
							e1.printStackTrace();
						}
					}else{
						try {
							firstdata=firstdata+","+HYPubBO_Client.findColValue("dip_datadefinit_h", "memorytable", "pk_datadefinit_h='"+pk[i]+"' and nvl(dr,0)=0");
						} catch (UifException e1) {
							e1.printStackTrace();
						}
					}
				}
				getBillCardPanel().setHeadItem("firstdata", firstdata);
			}else{
				//避免不做任何操作，鼠标焦点从“原始数据”字段移开时，该字段值为空问题
				String tab=this.getBillCardPanel().getHeadItem("firstdata").getValueObject()==null?"":this.getBillCardPanel().getHeadItem("firstdata").getValueObject().toString();
				if(tab !=null && !"".equals(tab)){
					getFirstData(tab);
				}
			}
		}

		//执行编辑公式
		this.getBillCardPanel().execHeadEditFormulas();
	}


	@Override
	public boolean beforeEdit(BillEditEvent e){
		return true;
	}
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
	
}
