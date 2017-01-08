package nc.ui.dip.sysregister;

import nc.ui.bd.ref.MessageLogoRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.SJUtil;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
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
 public class SysRegisterClientUI extends AbstractSysRegisterClientUI{
       public String delstr="";
	 public SysRegisterClientUI(){
		 super();
		 SJUtil.setAllButtonsEnalbe(this.getButtons());
		 getSplitPane().setDividerLocation(200);
	 }
	 /** 字段描述 */
		private static final long serialVersionUID = 5692169789554885827L;

//		public String selectnode = "";//选择树的节点

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
		getButtonManager().getButton(IBtnDefine.Start).setName("启用");
		getButtonManager().getButton(IBtnDefine.Stop).setName("停用");
		
		UIRefPane rf=(UIRefPane)getBillCardPanel().getHeadItem("pk_fathsysregister").getComponent();
		MessageLogoRefModel mode=(MessageLogoRefModel) rf.getRefModel();
		mode.addWherePart(" and ctype='分割标记'");
		
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
		return new nc.ui.dip.sysregister.SampleTreeCardData();
	}
	//end_2
	
	public void afterInit() throws Exception {

		super.afterInit();
		//给根节点赋名称
		this.modifyRootNodeShowName("接口系统");

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
		return super.afterTreeSelected(node);
	}

	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {

		/*if ("root".equals(snode.getNodeID().toString().trim())){
			selectnode = "";
		}*/
		
		super.onTreeSelectSetButtonState(snode);
		String str = (String)snode.getParentnodeID();
		if(str!=null && str.length()>0){
			
			this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 } else if(snode==snode.getRoot()){
			 this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			 this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			 this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
			 try {
					updateButtonUI();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		 }
		else{
			this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			this.getButtonManager().getButton(IBtnDefine.Start).setEnabled(true);
			this.getButtonManager().getButton(IBtnDefine.Stop).setEnabled(true);
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		//getButtonManager().getButton(201).setEnabled(false);

	}
	/*编辑后行操作状态控制，
	 * 2011-06-21
	 * zlc*/
	@Override
	public void afterEdit(BillEditEvent e) {
		
		
		if(e.getKey().equals("sitecode")||e.getKey().equals("sitename")||e.getKey().equals("isstop")||e.getKey().equals("isyy")){
			int k=this.getBillCardPanel().getBillModel("dip_sysregister_b").getRowCount();
			int m=this.getBillCardPanel().getBillTable("dip_sysregister_b").getSelectedRow();
			if(k-1==m){
				this.getBillCardPanel().getBillModel().addLine();
			}
		}
		
		
//		if(e.getKey().equals("isdeploy")){
//			String isdeploy=getBillCardWrapper().getBillCardPanel().getHeadItem("isdeploy").getValueObject()==null?"":getBillCardWrapper().getBillCardPanel().getHeadItem("isdeploy").getValueObject().toString();
//			if(isdeploy!=null){
//				Boolean flag=new UFBoolean(isdeploy).booleanValue();
//			    if(flag){
//			    	getButtonManager().getButton(IBillButton.Line).setEnabled(true);
//			    	try {
//						updateButtonUI();
//					} catch (Exception e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//			    }else{
//			    	getButtonManager().getButton(IBillButton.Line).setEnabled(false);
//			    	try {
//						updateButtonUI();
//					} catch (Exception e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//			    }
//			}
//		}
		super.afterEdit(e);
		if(e.getKey().equals("code")){
			String code=this.getBillCardPanel().getHeadItem("code").getValueObject()==null?"":this.getBillCardPanel().getHeadItem("code").getValueObject().toString();
			if(code!=null&&code.length()>0)
				this.getBillCardPanel().getHeadItem("code").setValue(code.toUpperCase());
		}
		if(e.getKey().equals("extcode")){
			String code=this.getBillCardPanel().getHeadItem("extcode").getValueObject()==null?"":this.getBillCardPanel().getHeadItem("extcode").getValueObject().toString();
			if(code!=null&&code.length()>0)
				this.getBillCardPanel().getHeadItem("extcode").setValue(code.toUpperCase());
		}
		if(e.getKey().equals("def_str_1")){
			String code=this.getBillCardPanel().getHeadItem("def_str_1").getValueObject()==null?"":this.getBillCardPanel().getHeadItem("def_str_1").getValueObject().toString();
			if(code!=null&&code.length()>0)
				this.getBillCardPanel().getHeadItem("def_str_1").setValue(code.toUpperCase());
		}
		if(e.getKey().equals("sitecode")){
			String code=this.getBillCardPanel().getBillModel().getValueAt(e.getRow(), "sitecode")==null?"":this.getBillCardPanel().getBillModel().getValueAt(e.getRow(), "sitecode").toString();
			if(code!=null&&code.length()>0)
				this.getBillCardPanel().getBillModel().setValueAt(code.toUpperCase(), e.getRow(), "sitecode");
		}
	}
	
	
}
