package nc.ui.dip.esbfilesend;

import java.util.HashMap;

import nc.ui.bd.ref.FilePathModel;
import nc.ui.bd.ref.FtpSourceRegisterRefModel;
import nc.ui.bd.ref.ZDJSRefModel;
import nc.ui.dip.buttons.IBtnDefine;
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
import nc.vo.dip.esbfilesend.DipEsbSendVO;
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
public class ESBmsgSendUI extends AbstractDataSendClientUI{
String ywlx="";
//public String getYwlx(){
//	return ywlx;
//}
	public ESBmsgSendUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
		getButtonManager().getButton(IBtnDefine.uploadFile).setName("上传/下载文件");
	}
	/** 字段描述 */
	private static final long serialVersionUID = 5692169789554885827L;

	public String selectnode = "";//选择树的节点

	//2011-4-21 12:42 ws 在制作左树右表时添加 begin
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
	 * 2011-4-21 14:10  wangshuai
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
	//end_2

	public void afterInit() throws Exception {

		super.afterInit();
		ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_ESBSEND);
		//给根节点赋名称
		this.modifyRootNodeShowName(ywlx);
		//页面加载时,将"删除"按钮设置为不可用
		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		//页面加载时,将"修改"按钮设置为不可用
		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

		updateButtonUI();
	}
	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}

	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {

		if ("root".equals(snode.getNodeID().toString().trim())){
			selectnode = "";
		}
		super.onTreeSelectSetButtonState(snode);
		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		try{
			String str = (String)snode.getParentnodeID();
			if(str!=null&&str.length()>0){
				DipEsbSendVO hvo=(DipEsbSendVO) ((VOTreeNode)snode).getData();
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.dealFile).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.uploadFile).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
				if(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
					this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.dealFile).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.uploadFile).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
				}else{
					this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
				}
				updateButtonUI();
			}else if(snode==snode.getRoot()){
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
				updateButtonUI();
			}
			else{
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.dealFile).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.uploadFile).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
				updateButtonUI();
			}
			getBillCardPanel().execHeadLoadFormulas();
		}catch (Exception e) {
		}

	}
	// 2011-06-04 郭义军 改
	HashMap map=new HashMap();
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);

		/* 程莉 2011-6-15 文件路径、目标服务器 begin*/
		if(e.getKey().equals("filepath")){
			getBillCardPanel().execHeadEditFormulas();
			getBillCardPanel().execHeadLoadFormulas();
		}
		if(e.getKey().equals("server")){
			getBillCardPanel().execHeadEditFormulas();
			getBillCardPanel().execHeadLoadFormulas();
		}
//		if(e.getKey().equals("issendfolder")){
//			String issendfolder=(String) getBillCardPanel().getHeadItem("issendfolder").getValueObject();
//			if(issendfolder==null||issendfolder.equals("N")||issendfolder.equals("false")){
//
//				getBillCardPanel().getHeadItem("sendfilename").setEnabled(true);
//			}else{
//				getBillCardPanel().setHeadItem("sendfilename", null);
//				getBillCardPanel().getHeadItem("sendfilename").setEnabled(false);
//				
//			}
//		}
		if(e.getKey().equals("deeltype")){
			String deeltype=(String) getBillCardPanel().getHeadItem("deeltype").getValueObject();
			if(deeltype.equals("m1")||deeltype.equals("m6")){
				getBillCardPanel().getHeadItem("bakpath").setEdit(true);
			}else{
				getBillCardPanel().getHeadItem("bakpath").setEdit(false);
				getBillCardPanel().setHeadItem("bakpath", null);
			}
			if(deeltype.equals("m3")){
				UIRefPane ref=(UIRefPane) getBillCardPanel().getHeadItem("server").getComponent();
				
				ZDJSRefModel refModel=new ZDJSRefModel();
				ref.setRefModel(refModel);
				getBillCardPanel().getHeadItem("server").setEdit(true);
				getBillCardPanel().getHeadItem("server").setValue(null);
			}else{
				getBillCardPanel().getHeadItem("server").setEdit(false);
				getBillCardPanel().setHeadItem("server", null);
			}
			if(deeltype.equals("m4")||deeltype.equals("m5")){
				UIRefPane ref=(UIRefPane) getBillCardPanel().getHeadItem("server").getComponent();
				FtpSourceRegisterRefModel ftpModel =new FtpSourceRegisterRefModel();
				ref.setRefModel(ftpModel);
				getBillCardPanel().getHeadItem("server").setEdit(true);
				getBillCardPanel().getHeadItem("server").setValue(null);
				getBillCardPanel().getHeadItem("bakpath").setEdit(true);
				getBillCardPanel().getHeadItem("bakpath").setValue(null);
			}
		}
	}
	// 2011-06-04 郭义军 改
	public void filePath(){
//		是否后台发送
		String pk_datasend = getBillCardPanel().getHeadItem("pk_esbsend").getValueObject().toString();
		UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("filepath").getComponent();//文件路径

		if(!SJUtil.isNull(pk_datasend)){
			DipEsbSendVO vo=null;
			map.put("pk_id", pk_datasend.toString());
			FilePathModel model=new FilePathModel();
			model.addWherePart(" and nvl(dr,0)=0");
			pane.setRefModel(model);
		}

	}
	
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
}
