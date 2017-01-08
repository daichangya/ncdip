package nc.ui.dip.datalook;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.datadefinit.WaTreeRenderer;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datalook.DipDatalookVO;
import nc.vo.pub.CircularlyAccessibleValueObject;


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
public class DataLookClientUI extends AbstractDataLookClientUI{
	private IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	
	private Integer count = 3000;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public DataLookClientUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
		UITree t=this.getBillTree();
		WaTreeRenderer renderer=new WaTreeRenderer();
		t.setCellRenderer(renderer);
		getBillCardPanel().setTatolRowShow(true);
		getBillCardPanel().getBodyPanel().getPmBody().removeAll();
		String sql="select ss.sysvalue from dip_runsys_b ss  where ss.Syscode='DIP-0000015' and nvl(dr,0)=0 ";
		try {
			String value=iq.queryfield(sql);
			if(null != value && !"".equals(value)){
				setCount(Integer.valueOf(value));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 字段描述 */
	private static final long serialVersionUID = -2887298593239771766L;
	public String selectnode = "";//选择树的节点
	String statusHiddleItemKey = "";//界面显示设置审批状态字段key
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new DataLookClientUICtrl();
	}

	/*public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {	}*/

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}


	protected CardEventHandler createEventHandler()
	{
		return new MyEventHandler(this, getUIControl());
	}

	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new nc.ui.dip.datalook.SampleTreeCardData();
	}
	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}
	public void afterInit() throws Exception {

		super.afterInit();
		// 给根节点赋名称
		this.modifyRootNodeShowName("数据浏览");
	}
	@Override
	public boolean afterTreeSelected(VOTreeNode node) {
		selectnode = node.getNodeID().toString();

		return super.afterTreeSelected(node);
	}


//	@Override
//	protected void onTreeSelectSetButtonState(nc.ui.trade.pub.TableTreeNode snode) {

//	if ("root".equals(snode.getNodeID().toString().trim())){
//	selectnode = "";
//	}
//	super.onTreeSelectSetButtonState(snode);
//	try{
//	String str = (String)snode.getParentnodeID();
//	if(str!=null&&str.length()>0){
//	this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(true);
//	this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(true);
//	this.getButtonManager().getButton(IBtnDefine.ADD).setEnabled(false);
//	updateButtonUI();

//	}else if(snode==snode.getRoot()){
//	this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//	updateButtonUI();

//	}else{
//	this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
//	this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
//	this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//	this.getButtonManager().getButton(IBtnDefine.ADD).setEnabled(true);
//	this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);

//	updateButtonUI();

//	}
//	} catch (Exception e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//	}
//	}

	@Override
	public void onTreeSelectSetButtonState(nc.ui.trade.pub.TableTreeNode snode) {
		if("root".equals(snode.getNodeID().toString().trim())){
			selectnode="";
		}
		try {
			super.onTreeSelectSetButtonState(snode);
			String str=(String) snode.getParentnodeID();
//			this.getButtonManager().getButton(IBtnDefine.ADD).setEnabled(false);
			if(str!=null&&str.length()>0){
				DipDatalookVO look=(DipDatalookVO) ((VOTreeNode)snode).getData();
				if(look!=null&&look.getIsfolder()!=null&&look.getIsfolder().booleanValue()){
					this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				}else{
					this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.ImportBill).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.ExportBill).setEnabled(true);	
				}
				
				updateButtonUI();

			}else if(snode==snode.getRoot()){
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
				updateButtonUI();
			}else{
				this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
				updateButtonUI();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void afterEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		super.afterEdit(arg0);
		int k=getBillCardPanel().getBillTable().getRowCount();
		int m=getBillCardPanel().getBillTable().getSelectedRow();
		if(m==k-1){
			getBillCardPanel().getBillModel().addLine();
		}
	}


	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		Object value = getBillCardPanel().getBodyValueAt(e.getRow(), statusHiddleItemKey);
		if(null != value && !"0".equals(value)){//审批状态<>0的不允许编辑
			return false;
		}
		return super.beforeEdit(e);
	}
	
}

