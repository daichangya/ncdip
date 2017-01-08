package nc.ui.dip.datasynch;

import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.IContrastUtil;
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
 public class DataSynchUI extends AbstractDatasynchUI{
	 String ywlx="";
	/* public String getYwlx(){

    	 return ywlx;
	 }*/
     public DataSynchUI(){
    	 super();
    	 SJUtil.setAllButtonsEnalbe(this.getButtons());
    	 getSplitPane().setDividerLocation(200);
     }
	 /** 字段描述 */
		private static final long serialVersionUID = 5692169789554885827L;
		public String selectnode="";//选择树节点
		
    protected CardEventHandler  createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

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
		return new nc.ui.dip.datasynch.SampleTreeCardData();
	}

	public void afterInit() throws Exception {

		super.afterInit();
   	 ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_TB);
//		// 给根节点赋名称
		this.modifyRootNodeShowName(ywlx);
		//页面加载时,将"删除"按钮设置为不可用
//		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		//页面加载时,将"修改"按钮设置为不可用
//		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

		updateButtonUI();
	}

	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}
	
	
	
	
	@Override
	public boolean afterTreeSelected(nc.ui.trade.pub.VOTreeNode node) {
		selectnode = node.getNodeID().toString();
		
			System.out.println("node:"+selectnode);
			return super.afterTreeSelected(node);
	}

	@Override
	public void onTreeSelectSetButtonState(nc.ui.trade.pub.TableTreeNode snode) {
		if("root".equals(snode.getNodeID().toString().trim())){
			selectnode="";
		}
		super.onTreeSelectSetButtonState(snode);
		DipDatasynchVO hvo=(DipDatasynchVO) ((VOTreeNode)snode).getData();

		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		if(hvo!=null &&hvo.getFpk()!=null&& hvo.getFpk().length()>0){
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			
			if(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){

				getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
				getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
				getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
				getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				setButEnable(false);
			}else{
				getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
				setButEnable(true);
			}
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (snode == snode.getRoot()) {
			this.getButtonManager().getButton(IBillButton.Add)
					.setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(
					false);
			this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
			setButEnable(false);

			getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
			try {
				updateButtonUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else {
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			setButEnable(false);
			getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
			getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(true);
			getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
			getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
			try {
				updateButtonUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void setButEnable(boolean flag){
		getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(flag);
   		getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(flag);
   		getButtonManager().getButton(IBtnDefine.EarlyWarning).setEnabled(flag);
   		getButtonManager().getButton(IBtnDefine.TBFORM).setEnabled(flag);
		this.getButtonManager().getButton(IBtnDefine.Synch).setEnabled(flag);
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		//执行编辑公式
//		this.getBillCardPanel().execHeadEditFormulas();
		if(e.getKey().equals("dataname")){
			getBillCardPanel().execHeadFormulas(getBillCardPanel().getHeadItem("dataname").getEditFormulas());
		}
	}
	
	


	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
}
