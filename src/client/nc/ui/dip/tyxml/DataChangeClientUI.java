package nc.ui.dip.tyxml;

import java.awt.event.ActionListener;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.bd.ref.ContDataRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.tyxml.tygs.CredenceListener;
import nc.ui.dip.tyxml.tygs.ProListener;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.tyxml.DipTYXMLDatachangeHVO;
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
public class DataChangeClientUI extends AbstractDataChangeClientUI{
	String ywlx;
	/*public String getYwlx(){
		return ywlx;
	}*/
	public String delstr =" ";//删行时删除条件, cl 2011-07-04
	public DataChangeClientUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
		String[] bitem=new String[]{"changformu","contrl"};
		for(int i=0;i<bitem.length;i++){
			if(getBillCardPanel().getBodyItem(bitem[i])!=null){
				UIRefPane rf=(UIRefPane) getBillCardPanel().getBodyItem(bitem[i]).getComponent();
				CredenceListener listener = new CredenceListener(this,bitem[i],CredenceListener.TYPE_BODY,rf);
				if(rf!=null){
					rf.getUIButton().removeActionListener(rf.getUIButton().getListeners(ActionListener.class)[0]);
					rf.getUIButton().addActionListener(listener);
					rf.setAutoCheck(false);
					rf.setEditable(false);
				}
			}
		}
		String itempro="pro";
		UIRefPane rfpro=(UIRefPane) getBillCardPanel().getBodyItem(itempro).getComponent();
		ProListener lis=new ProListener(this, itempro,CredenceListener.TYPE_BODY,rfpro);
		if(rfpro!=null){
			rfpro.getUIButton().removeActionListener(rfpro.getUIButton().getListeners(ActionListener.class)[0]);
			rfpro.getUIButton().addActionListener(lis);
			rfpro.setAutoCheck(false);
			rfpro.setEditable(false);
		}

		this.getBillCardPanel().getBillTable().setSortEnabled(false);
		getBillCardPanel().getBillModel().setSortColumn("orderno");
	}
	/** 字段描述 */
	private static final long serialVersionUID = 5692169789554885827L;

	public String selectnode = "";//选择树的节点

	//2011-4-11 10:42 chengli 在制作左树右表时添加 begin
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


ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_TYZH);
		//给根节点赋名称
		this.modifyRootNodeShowName(ywlx);

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

	//2011-6-28
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
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
		//getButtonManager().getButton(201).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		
		try{
			String str = (String)snode.getParentnodeID();
			if(str!=null&&str.length()>0){
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.Conversion).setEnabled(true);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
				DipTYXMLDatachangeHVO hvo=(DipTYXMLDatachangeHVO) ((VOTreeNode)snode).getData();
				if(hvo.getIsfolder().booleanValue()){//判断是一级父节点
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.Conversion).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
//					this.getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
//					this.getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.initUFOENV).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
				}else{

					this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.initUFOENV).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(true);
				}
				updateButtonUI();
			}else if(snode==snode.getRoot()){
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.initUFOENV).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
				updateButtonUI();
			}else{
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.Conversion).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.initUFOENV).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
				updateButtonUI();
			}
		}catch (Exception e) {
		}

	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if(e.getKey().equals("targettab")){
			String targettab=(String) getBillCardPanel().getHeadItem("targettab").getValueObject();
			((MyEventHandler)getCardEventHandler()).onLoadTable(targettab);
			

			UIRefPane rf=(UIRefPane) getBillCardPanel().getBodyItem("def_zdym").getComponent();
			ContDataRefModel cdm=(ContDataRefModel) rf.getRefModel();
			cdm.setWhere(" nvl(dip_datadefinit_b.dr,0)=0 and dip_datadefinit_b.pk_datadefinit_h='"+targettab+"'");
		}if(e.getKey().equals("sourcetab")){
			int rowcount=getBillCardPanel().getBillModel().getRowCount();
			if(rowcount>0){
				for(int i=0;i<rowcount;i++){
					getBillCardPanel().setBodyValueAt(null, i, "contrl");
					getBillCardPanel().setBodyValueAt(null, i, "changformu");
				}
			}
			
		
		}

	}
		 @Override
		protected UITree getBillTree() {
			// TODO Auto-generated method stub
			return super.getBillTree();
		}
}
