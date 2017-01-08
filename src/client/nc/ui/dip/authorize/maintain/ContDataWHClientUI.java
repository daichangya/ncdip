package nc.ui.dip.authorize.maintain;



import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.contdatawh.pane.SplitBodyPane;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.SJUtil;


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
public class ContDataWHClientUI extends AbstractContdatawhClientUI{
	private SplitBodyPane bodypan;
	public ContDataWHClientUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getButtonManager().getButton(IBtnDefine.autoContData).setName("自动维护");
		getSplitPane().setDividerLocation(200);
		reWriteUIBody();
		getBillListPanel().getHeadTable().setSortEnabled(false);
	}
	public void reWriteUIBody()
	{ 
		((UISplitPane)this.getBillListPanel().getComponent(0)).setDividerLocation(0.5);
		((UISplitPane)this.getBillListPanel().getComponent(0)).setDividerSize(10);
		UISplitPane bodyScrollPane=this.getBillListWrapper().getBillListPanel().getUISplitPane();
		bodyScrollPane.add(getBodySplitPane(), "bottom");
	}
	public SplitBodyPane getBodySplitPane(){
		if(bodypan==null){
			bodypan=new SplitBodyPane(true);
		}
		return bodypan;
	}
	
	
	
	/**@desc 由于参照是动态的，所以显示公式也是动态的
	 * 外部系统的编码上的显示公式
	 * */
	private String[] wbsql;
	/**@desc 由于参照是动态的，所以显示公式也是动态的
	 * nc系统的编码上的显示公式
	 * */
	private String[] ncsql;


	/** 字段描述 */
	private static final long serialVersionUID = 5692169789554885827L;
	public String selectnode = "";//选择树的节点
	protected ManageEventHandler  createEventHandler() {
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


	@SuppressWarnings("unused")
	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	}

	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new SampleTreeCardData();
	}

	public void afterInit() throws Exception {

		super.afterInit();
		// 给根节点赋名称
		this.modifyRootNodeShowName("数据对照维护");

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
		
	}
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	@Override
	public void afterEdit(BillEditEvent arg0) {
		super.afterEdit(arg0);
	}
	public String[] getNcsql() {
		return ncsql;
	}
	public void setNcsql(String[] ncsql) {
		this.ncsql = ncsql;
	}
	public String[] getWbsql() {
		return wbsql;
	}
	public void setWbsql(String[] wbsql) {
		this.wbsql = wbsql;
	}
	//禁用该方法
	@Override
	public void bodyRowChange(BillEditEvent e) {
		
		int headcount = e.getRow();
		MyEventHandler header = (MyEventHandler)this.getManageEventHandler();
		try {
			header.onConteQuery();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
	}
	//禁用该方法
	@Override
	public void mouse_doubleclick(BillMouseEnent e) {
		// TODO Auto-generated method stub
//		super.mouse_doubleclick(e);
		
	}
	@Override
	public void addMouseSelectListener(BillTableMouseListener ml) {
		// TODO Auto-generated method stub
		super.addMouseSelectListener(ml);
	}
	public void setTreeEnable(boolean b) {
		getBillTree().setEnabled(b);
		
	}

	
	
}
