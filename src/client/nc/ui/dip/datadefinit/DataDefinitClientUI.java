package nc.ui.dip.datadefinit;

import com.sun.java_cup.internal.internal_error;

import nc.ui.bd.ref.SysRegisterRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
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
public class DataDefinitClientUI extends AbstractDataDefinitClientUI{
	public String delstr =" ";//删除条件, ytq 2011-07-02
	public DataDefinitClientUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
		UITree t=this.getBillTree();
		WaTreeRenderer renderer=new WaTreeRenderer();
		t.setCellRenderer(renderer);

	}
	/** 字段描述 */
	private static final long serialVersionUID = 5692169789554885827L;

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
		return new nc.ui.dip.datadefinit.SampleTreeCardData();
	}
	//end_2

	public void afterInit() throws Exception {
		super.afterInit();
		//给根节点赋名称
		this.modifyRootNodeShowName("数据定义");

		//页面加载时,将"删除"按钮设置为不可用
		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		//页面加载时,将"修改"按钮设置为不可用
		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		//页面加载时，将"创建表"按钮设置为不可用
		getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
		updateButtonUI();
	}
	@Override
	public void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}

	@Override
	public boolean afterTreeSelected(VOTreeNode node) {
		selectnode = node.getNodeID().toString();
		System.out.println("node:"+selectnode);
		return super.afterTreeSelected(node);
	}
	
	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {
		if ("root".equals(snode.getNodeID().toString().trim())){
			selectnode = "";
		}
		super.onTreeSelectSetButtonState(snode);
		String str = (String) snode.getParentnodeID();
		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		if(str!=null&& str.length()>0){
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//			this.getButtonManager().getButton(IBillButton.Copy).setEnabled(true);
			DipDatadefinitHVO hvo=(DipDatadefinitHVO) ((VOTreeNode)snode).getData();
			if(hvo.getIsfolder().booleanValue()){//判断是一级父节点
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				this.getButtonManager().getButton(IBillButton.Copy).setEnabled(false);
//				if(hvo.getPk_xt().equals(hvo.getPk_sysregister_h())){
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
//				}else{
//					this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//				}
			}else{

				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
				if(hvo.getMemorytype().equals("表")){
					this.getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
				}else{
					this.getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
					
				}
			}
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(snode==snode.getRoot()){
			this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
			this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//			this.getButtonManager().getButton(IBillButton.Copy).setEnabled(false);
			DipDatadefinitHVO hvo=(DipDatadefinitHVO) ((VOTreeNode)snode).getData();

			this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			if(hvo.getIsfolder().booleanValue()){
				this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(true);
			}else{
				this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
			}
			this.getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
	public void doQueryAction(ILinkQueryData arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		//自动增加一行。
		if(!(e.getKey().equals("isdeploy")||e.getKey().equals("deploycode")||e.getKey().equals("deployname")||
				e.getKey().equals("syscode")||e.getKey().equals("sysname")||e.getKey().equals("busicode")||
				e.getKey().equals("datatype")||e.getKey().equals("memorytable")||e.getKey().equals("iscreatetab")||
				e.getKey().equals("isdeploy")||e.getKey().equals("isdeploy")||e.getKey().equals("isfolder")
				||e.getKey().equals("memorytype")||e.getKey().equals("userdefined"))){
			int k=this.getBillCardPanel().getBillModel("dip_datadefinit_b").getRowCount();
			int m=this.getBillCardPanel().getBillTable("dip_datadefinit_b").getSelectedRow();
			if(k-1==m){
				this.getBillCardPanel().getBillModel().addLine();
			}
		}
		if(e.getKey().equals("userdefined")){
			String userdefined=(String) e.getValue();
			if("是".equals(userdefined)){
				UIRefPane jcp=(UIRefPane) getBillCardPanel().getHeadItem("memorytable").getComponent();
				jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),""));
				jcp.getUITextField().setText("");
			}else{
				UIRefPane jcp=(UIRefPane) getBillCardPanel().getHeadItem("memorytable").getComponent();
				String memorytype = getBillCardPanel().getHeadItem("memorytype").getValue();
				if(memorytype.equals("视图")){
					jcp.getUITextField().setDocument(((MyEventHandler)getCardEventHandler()).docu);
				}else{
					String code=(String) getBillCardPanel().getHeadItem("syscode").getValueObject();
					if(!code.equals("0000")){
						String extcode=((MyEventHandler)getCardEventHandler()).extcode;
						jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
						jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");
					}
				}
			}
		}
		if(e.getKey().equals("memorytype")){
			String memorytype=(String) e.getValue();
			UIRefPane jcp=(UIRefPane) getBillCardPanel().getHeadItem("memorytable").getComponent();
			if(memorytype.equals("视图")){
				jcp.getUITextField().setDocument(((MyEventHandler)getCardEventHandler()).docu);
			}else{
				String code=(String) getBillCardPanel().getHeadItem("syscode").getValueObject();
				if(!code.equals("0000")){
					String extcode=((MyEventHandler)getCardEventHandler()).extcode;
					jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
					jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");
				}
			}
		}
		
		
		//2011-5-8 程莉 引用字段自定义参照
		if("def_quotetable".equals(e.getKey())){			
//			String fpk=(String) this.getBillCar/dPanel().getHeadItem("pk_sysregister_h").getValueObject();

			/*UIRefPane pane3=(UIRefPane) this.getBillCardPanel().getBodyItem("quotecolu").getComponent();
			DatadefinitHAndBRefModel model3=new DatadefinitHAndBRefModel();
			String node=selectnode;
			model3.addWherePart(" and pk_datadefinit_h <>'"+node+"' and pk_datadefinit_h='"+pk_datadefinit_h+"' and pk_sysregister_h='"+fpk+"'");
			pane3.setRefModel(model3);*/

			//执行显示公式
			int row=e.getRow();
			String pk_datadefinit_h= this.getBillCardPanel().getBodyItem("def_quotetable").getValueObject()==null?"":this.getBillCardPanel().getBodyItem("def_quotetable").getValueObject().toString();
			
			if(!"".equals(pk_datadefinit_h)){
				this.getBillCardPanel().setBodyValueAt(new UFBoolean(true), row, "isquote");
			}else{
				this.getBillCardPanel().setBodyValueAt(new UFBoolean(false), row, "isquote");
			}
			this.getBillCardPanel().execBodyFormulas(row, getBillCardPanel().getBodyItem("def_quotetable").getEditFormulas());//.execBodyFormula(row, "def_quotetable");

		}
		if("type".equals(e.getKey())){
			int row=e.getRow();
			Object o=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "type");
			if(o!=null){
				String type=o.toString();

				if(type.equals("Char".toUpperCase())||type.equals("Varchar".toUpperCase())){
					if(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")==null||
							(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")).equals("") ){
						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(50, row, "length");
					}
					this.getBillCardPanel().setBodyValueAt(null, row, "deciplace");
				}else if(type.equals("Number".toUpperCase())){
//					if(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")==null||
//							(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")).equals("")){
						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(20, row, "length");
						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(2, row,"deciplace");
						
			//		}

				}
			}
			
		}
		/*定义number类型字段的长度必须为20位和小数位数必须为8位校验，若不是直接改为length=20，deciplace=8
		 * 2011-06-27
		 * zlc  begin*/
		if("length".equals(e.getKey())){
			int row=e.getRow();
			String type=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"type")==null?"":this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"type").toString();
			String length=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "length")==null?"":this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length").toString();
			if(length==null||length.trim().length()<=0){
				return;
			}
			if(type!=null&&type.equalsIgnoreCase("number")){
				if(Integer.parseInt(length)>20){
					this.getBillCardPanel().getBillModel().setValueAt(20, row, "length");
					return;
				}
			}
		}
		if("deciplace".equals(e.getKey())){
			int row=e.getRow();
			String type=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"type")==null?"":this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"type").toString();
			String deciplace=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "deciplace")==null?"":this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"deciplace").toString();
			//String type=this.getBillCardWrapper().getBillCardPanel().getBodyItem("type").getValueObject()==null?"":this.getBillCardWrapper().getBillCardPanel().getBodyItem("type").getValueObject().toString();
			//String deciplace=this.getBillCardWrapper().getBillCardPanel().getBodyItem("deciplace").getValueObject()==null?"":this.getBillCardWrapper().getBillCardPanel().getBodyItem("deciplace").getValueObject().toString();
			if(deciplace==null||deciplace.trim().length()<=0){
				return;
			}
			if(type!=null&&type.equalsIgnoreCase("number")){
				if(Integer.parseInt(deciplace)>8){
					this.getBillCardPanel().getBillModel().setValueAt(8, row, "deciplace");
					return;
				}
			}
		}
		/*end*/
		if("issyspk".equals(e.getKey())){
			int row=e.getRow();
			Object o=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "issyspk");
			if(o !=null){
				boolean issyspk=Boolean.parseBoolean(o.toString());
				if(issyspk){
					this.getBillCardPanel().getBillModel("dip_datadefinit_b").setValueAt("", row, "type");
					this.getBillCardPanel().getBillModel("dip_datadefinit_b").setValueAt("CHAR", row, "type");
					
					this.getBillCardPanel().getBillModel("dip_datadefinit_b").setValueAt("", row, "length");
					this.getBillCardPanel().getBillModel("dip_datadefinit_b").setValueAt("20", row, "length");
				}
			}
		}
		/*
		 * 修改筛选条件，如果是是分布系统，并且是否分布系统为‘Y’时才能参照出分部站点
		 * 2011-06-03
		 * zlc*/
		/*if("isdeploy".equals(e.getKey())){
			String pk_sysregister_h =getBillCardPanel().getHeadItem("pk_xt").getValueObject().toString();
			//2011-4-26 程莉 动态自定义参照：查询当前系统下的分布式标识	
			boolean f=false;
			String isdep=getBillCardPanel().getHeadItem("isdeploy").getValueObject()==null?null:
				getBillCardPanel().getHeadItem("isdeploy").getValueObject().toString();
			if(isdep!=null&&isdep.length()>0){
				f=Boolean.parseBoolean(isdep);
					UIRefPane pane=(UIRefPane)getBillCardPanel().getHeadItem("deploycode").getComponent();
					SysRegisterRefModel model = new SysRegisterRefModel();
					model.addWherePart(" and dip_sysregister_b.pk_sysregister_h='"+pk_sysregister_h+"' and dip_sysregister_h.isdeploy='"+(f?'Y':'N')+ "' and nvl(dip_sysregister_b.dr,0)=0  and (dip_sysregister_b.isstop='N' or dip_sysregister_b.isstop='') ");
					pane.setRefModel(model);
			}
		}*/
		
		if("ename".equals(e.getKey())){
			int row=e.getRow();
			Object o= this.getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(row, "ename");
			String str=null;
			if(o!=null&&o.toString().length()>0){
				 str=o.toString().trim().toUpperCase();
				 this.getBillCardPanel().getBillModel("dip_datadefinit_b").setValueAt(str, row,"ename");
			}
			
		}
		// TODO Auto-generated method stub
		//主表引用字段 自定义参照
		/*	if("citetable".equals(e.getKey())){

			String pk_datadefinit_h= this.getBillCardPanel().getHeadItem("citetable").getValueObject()==null?"":this.getBillCardPanel().getHeadItem("citetable").getValueObject().toString();
			UIRefPane pane3=(UIRefPane) this.getBillCardPanel().getHeadItem("citecolum").getComponent();
			DataDefinitbRefModel model3=new DataDefinitbRefModel();
			model3.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h ='"+pk_datadefinit_h+"' and nvl(dip_datadefinit_b.dr,0)=0 and nvl(dip_datadefinit_h.dr,0)=0");
			pane3.setRefModel(model3);

		}*/

		//执行编辑公式
		this.getBillCardPanel().execHeadEditFormulas();
//		this.getBillCardPanel().getBodyItem("def_quotetable").getLoadFormula();
//		this.getBillCardPanel().getBodyItem("quotecolu").getEditFormulas();
		//将业务标志转为大写   2011-07-05 zlc
		if(e.getKey().equals("busicode")){
			String code=this.getBillCardPanel().getHeadItem("busicode").getValueObject()==null?"":this.getBillCardPanel().getHeadItem("busicode").getValueObject().toString();
			if(code!=null&&code.length()>0)
				this.getBillCardPanel().getHeadItem("busicode").setValue(code.toUpperCase());
		}
		if(e.getKey().equals("isquote")){
			int row=e.getRow();
			Object isdo= getBillCardPanel().getBodyValueAt(row, "isquote");
			UFBoolean isd=isdo==null?new UFBoolean(false):new UFBoolean(isdo.toString());
			if(isd.booleanValue()){
				getBillCardPanel().getBillModel().setCellEditable(row, "def_quotetable", true);
			}else{
				getBillCardPanel().setBodyValueAt(null, row, "def_quotetable");
				getBillCardPanel().setBodyValueAt(null, row, "quotecolu");
				getBillCardPanel().setBodyValueAt(null, row, "quotetable");
				getBillCardPanel().getBillModel().setCellEditable(row, "def_quotetable", false);
				
			}
		}

	}
	
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
}
