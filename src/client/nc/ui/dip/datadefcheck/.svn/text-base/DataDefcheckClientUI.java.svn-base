package nc.ui.dip.datadefcheck;

import javax.swing.event.TableModelListener;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefcheck.DipDatadefinitCVO;
import nc.vo.dip.datadefcheck.DipDatadefinitHVO;
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
public class DataDefcheckClientUI extends AbstractDataDefcheckClientUI implements IBillModelRowStateChangeEventListener{
	public String delstr =" ";//删除条件, ytq 2011-07-02
	public DataDefcheckClientUI(){
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
	protected ManageEventHandler createEventHandler() {
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
		return new nc.ui.dip.datadefcheck.SampleTreeCardData();
	}
	//end_2

	public void afterInit() throws Exception {
		super.afterInit();
		//给根节点赋名称
		this.modifyRootNodeShowName("数据校验定义");

//		//页面加载时,将"删除"按钮设置为不可用
//		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//		//页面加载时,将"修改"按钮设置为不可用
//		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
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
//		super.onTreeSelectSetButtonState(snode);
		String str = (String) snode.getParentnodeID();

		this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(false);
		this.getButtonManager().getButton(IBillButton.InsLine).setVisible(false);//tEnabled(false);
		this.getButtonManager().getButton(IBillButton.CopyLine).setVisible(false);
		this.getButtonManager().getButton(IBillButton.PasteLinetoTail).setVisible(false);
		this.getButtonManager().getButton(IBillButton.PasteLine).setVisible(false);

//		this.getButtonManager().getButton(IBillButton.Query).setEnabled(false);
		if(str!=null&& str.length()>0){
			DipDatadefinitHVO hvo=(DipDatadefinitHVO) ((VOTreeNode)snode).getData();
			int rowcount=getBillListPanel().getBodyBillModel().getRowCount();

			if(!hvo.getIsfolder().booleanValue()){//判断是不是父节点
				if(hvo.getMemorytype().equals("表") && rowcount>0){
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
//					this.getButtonManager().getButton(IBillButton.Query).setEnabled(true);
				}else if(hvo.getMemorytype().equals("表")){
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
//					this.getButtonManager().getButton(IBillButton.Query).setEnabled(true);
				}
			}

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
	
	//禁用该方法
	@Override
	public void bodyRowChange(BillEditEvent e) {
		
//		int headcount = e.getRow();
		MyEventHandler header = (MyEventHandler)this.getManageEventHandler();
		try {
			header.onCheckQuery();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
	}


	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
//		//自动增加一行。
//		int k=this.getBillListPanel().getBodyBillModel("dip_datadefinit_c").getRowCount();
//		int m=this.getBillListPanel().getBodyTable("dip_datadefinit_c").getSelectedRow();
//		if(k-1==m){
//			this.getBillListPanel().getBodyBillModel().addLine();
//		}

	}
	
	
	public void isEditBtn(boolean isEdit){
		getButtonManager().getButton(IBillButton.Save).setEnabled(isEdit);
		getButtonManager().getButton(IBillButton.Cancel).setEnabled(isEdit);
		getButtonManager().getButton(IBillButton.Line).setEnabled(isEdit);
		getButtonManager().getButton(IBillButton.Refresh).setEnabled(!isEdit);
//		getButtonManager().getButton(IBillButton.Query).setEnabled(!isEdit);
		getButtonManager().getButton(IBillButton.Edit).setEnabled(!isEdit);
		getButtonManager().getButton(IBillButton.Delete).setEnabled(!isEdit);
		
		try {
			updateButtonUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//禁用该方法
	@Override
	public void mouse_doubleclick(BillMouseEnent e) {
		// TODO Auto-generated method stub
//		super.mouse_doubleclick(e);
		
	}
	
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
	public void valueChanged(RowStateChangeEvent event) {
////		MyEventHandler header = (MyEventHandler)this.getManageEventHandler();
////		try {
//////			header.onCheckQuery();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		
	}
	
	public void setTreeEnable(boolean b) {
		getBillTree().setEnabled(b);
		
	}
}
