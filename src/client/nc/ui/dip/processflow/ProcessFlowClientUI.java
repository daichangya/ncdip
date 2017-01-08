package nc.ui.dip.processflow;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.processflow.DipProcessflowHVO;
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
public class ProcessFlowClientUI extends AbstractProcessFlowClientUI{
	private String ywlx="";

	public ProcessFlowClientUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
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
	//设置自定义排序不可用
	protected void initSelfData() {	
		this.getBillCardPanel().getBillTable().setSortEnabled(false);
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

	/**	2011-4-11 9:42 chengli 在制作左树右表时添加 begin_2 */
	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new nc.ui.dip.processflow.SampleTreeCardData();
	}
	//end_2

	public void afterInit() throws Exception {

		super.afterInit();
		ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_LC);
		//给根节点赋名称
		this.modifyRootNodeShowName(ywlx);

		//页面加载时,将"删除"按钮设置为不可用
		//getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		//页面加载时,将"修改"按钮设置为不可用
		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.Moveddown).setEnabled(false);

		updateButtonUI();
	}
	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
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
		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		try{
			//getButtonManager().getButton(201).setEnabled(false);
			String str = (String)snode.getParentnodeID();
			if(str!=null&&str.length()>0){
				DipProcessflowHVO hvo=(DipProcessflowHVO) ((VOTreeNode)snode).getData();
				if(hvo!=null&&hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.ProcessFlow).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				}else{
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.ProcessFlow).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
				}
				
				updateButtonUI();
			}else if(snode==snode.getRoot()){
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.Moveddown).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
				updateButtonUI();
			}
			else{
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.ProcessFlow).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
				updateButtonUI();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}

	}
	@Override
	public void afterEdit(BillEditEvent arg0) {
		super.afterEdit(arg0);
//		自动增加一行。
		if(arg0.getKey().equals("bm")||arg0.getKey().equals("name")||arg0.getKey().equals("orderno")||arg0.getKey().equals("lclx")){
			int k=this.getBillCardPanel().getBillModel("dip_processflow_b").getRowCount();
			int m=this.getBillCardPanel().getBillTable("dip_processflow_b").getSelectedRow();
			if(k-1==m){
				this.getBillCardPanel().getBillModel().addLine();
				getBillCardPanel().getBillTable().changeSelection(k, 0, false, false);
				getBillCardPanel().setBodyValueAt(k+1, k, "orderno");
			}
		}
		
		
		
		if(/*arg0.getKey().equals("flowtype")*/ true){
			/*	VOTreeNode node=getBillTreeSelectNode();
			String pk=node.getNodeID().toString();
			DipProcessflowHVO hvo=null;
			try {
				 hvo=(DipProcessflowHVO) HYPubBO_Client.queryByPrimaryKey(DipProcessflowHVO.class,pk );

			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			String sys=getBillCardPanel().getHeadItem("pk_xt").getValueObject().toString();
			String type=(String) getBillCardPanel().getHeadItem("flowtype").getValueObject();//hvo.getFlowtype();

//			UIRefPane pane=(UIRefPane) getBillCardPanel().getBodyItem("bm").getComponent();
//			DataLCRefModel model=(DataLCRefModel) pane.getRefModel();// DataLCRefModel();
//			if(type.equals("0001AA1000000001TQJ4")){
//
//				model.setWherePart("   nvl(v_dip_sjlc.dr,0)=0 and fpk='"+sys+"'");
//			}else{
//				model.setWherePart("   v_dip_sjlc.tasktyp='"+type+"' and nvl(v_dip_sjlc.dr,0)=0 and fpk='"+sys+"'");
//			}
//			pane.setRefModel(model);
		}
//		if(arg0.getKey().equals("bm")){

//		this.getBillCardPanel().execBodyFormulas(arg0.getRow(), getBillCardWrapper().getBillCardPanel().getBodyItem("bm").getEditFormulas());
//		}
		// 加的

	}
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}

	

}
