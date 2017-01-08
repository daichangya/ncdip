package nc.ui.dip.datasend;

import java.util.HashMap;

import nc.bs.pfxx.convert.formula.GetNodeText;
import nc.ui.bd.ref.FilePathModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
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
import nc.vo.dip.datasend.DipDatasendVO;
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
public class DataSendClientUI extends AbstractDataSendClientUI{
String ywlx="";
//public String getYwlx(){
//	return ywlx;
//}
	public DataSendClientUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
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
//		UICheckBox selectbox = (UICheckBox)getBillCardPanel().getHeadItem("issendbackground").getComponent();

//		final DataSendClientUI ui = this;
//		selectbox.addChangeListener(new ChangeListener(){

//		public void stateChanged(ChangeEvent arg0) {
//		// TODO Auto-generated method stub
//		String issend=(String) getBillCardPanel().getHeadItem("issendbackground").getValueObject();
//		if(issend.equals("false")){
//		getBillCardPanel().getHeadItem("filepath").setEdit(false);
//		getBillCardPanel().getHeadItem("def_str1").setEdit(true);
//		UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("def_str1").getComponent();//文件路径
//		DataSendListener listener = new DataSendListener(ui,"def_str1","def_str1",CredenceListener.TYPE_BODY,pane);
//		if(pane!=null){
//		pane.getUIButton().removeActionListener(pane.getUIButton().getListeners(ActionListener.class)[0]);
//		pane.getUIButton().addActionListener(listener);
//		pane.setAutoCheck(false);
//		pane.setEditable(false);
//		}

//		}else{
//		getBillCardPanel().getHeadItem("filepath").setEdit(true);
//		getBillCardPanel().getHeadItem("def_str1").setEdit(false);
////		FilePathModel model=new FilePathModel();
////		model.addWherePart(" and nvl(dr,0)=0");
////		pane.setRefModel(model);
////		getBillCardPanel().getHeadItem("filepath").setComponent(pane);
//////	ui.getBillCardPanel().setBillData(ui.getBillCardPanel().getBillData());
////		ui.getBillCardPanel().getHeadUIPanel().updateUI();

////		ui.updateUI();
////		map.put("pk_id", "dip_filepath.pk_filepath");
////		map.put("table_h", "dip_filepath");
////		map.put("table_code", "dip_filepath.code");
////		map.put("table_name", "dip_filepath.name");
////		map.put("table_pk", "dip_filepath.pk_filepath");
////		getBillCardPanel().execHeadFormulas(getBillCardPanel().getHeadItem("filepath").getLoadFormula());

//		}

//		}

//		});
//		selectbox.addValueChangedListener(new ValueChangedListener(){

//		public void valueChanged(ValueChangedEvent arg0) {
//		// TODO Auto-generated method stub
//		System.out.println("aaaaaaaaaaaa");
//		}


//		});

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


	//2011-4-21 12:42 ws 在制作左树右表时添加 begin_2
	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new nc.ui.dip.datasend.SampleTreeCardData();
	}
	//end_2

	public void afterInit() throws Exception {

		super.afterInit();
		ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_FS);
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
		this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		try{
			String str = (String)snode.getParentnodeID();
			if(str!=null&&str.length()>0){
				DipDatasendVO hvo=(DipDatasendVO) ((VOTreeNode)snode).getData();
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.DATASEND).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.SENDFORM).setEnabled(true);
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
					this.getButtonManager().getButton(IBtnDefine.DATASEND).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.SENDFORM).setEnabled(false);
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
				this.getButtonManager().getButton(IBtnDefine.DATASEND).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.SENDFORM).setEnabled(false);
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
		if(e.getKey().equals("issendbackground")){//是否后台发送
			String issend=(String) getBillCardPanel().getHeadItem("issendbackground").getValueObject();

			/*if(issend.equals("false")){
				getBillCardPanel().getHeadItem("filepath").setEdit(false);
				getBillCardPanel().getHeadItem("def_str1").setEdit(true);
				UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("def_str1").getComponent();//文件路径
				DataSendListener listener = new DataSendListener(this,"def_str1","def_str1",CredenceListener.TYPE_BODY,pane);
				if(pane!=null){
					pane.getUIButton().removeActionListener(pane.getUIButton().getListeners(ActionListener.class)[0]);
					pane.getUIButton().addActionListener(listener);
					pane.setAutoCheck(false);
					pane.setEditable(false);
				}

			}else{*/
			getBillCardPanel().getHeadItem("filepath").setEdit(true);
			getBillCardPanel().getHeadItem("def_str1").setEdit(false);
//			FilePathModel model=new FilePathModel();
//			model.addWherePart(" and nvl(dr,0)=0");
//			pane.setRefModel(model);
//			getBillCardPanel().getHeadItem("filepath").setComponent(pane);
////			ui.getBillCardPanel().setBillData(ui.getBillCardPanel().getBillData());
//			ui.getBillCardPanel().getHeadUIPanel().updateUI();

//			ui.updateUI();
//			map.put("pk_id", "dip_filepath.pk_filepath");
//			map.put("table_h", "dip_filepath");
//			map.put("table_code", "dip_filepath.code");
//			map.put("table_name", "dip_filepath.name");
//			map.put("table_pk", "dip_filepath.pk_filepath");
//			getBillCardPanel().execHeadFormulas(getBillCardPanel().getHeadItem("filepath").getLoadFormula());

//			}
		}
		/*end*/

		/* 程莉 2011-6-15 文件路径、目标服务器 begin*/
		if(e.getKey().equals("filepath")){
			getBillCardPanel().execHeadEditFormulas();
			getBillCardPanel().execHeadLoadFormulas();
		}
		if(e.getKey().equals("server")){
			getBillCardPanel().execHeadEditFormulas();
			getBillCardPanel().execHeadLoadFormulas();
		}
		/*end*/
		/*并行线程数，添加为数值型且不能大于30校验  2011-06-21 zlc   begin*/
		if(e.getKey().equals("threadno")){
			String threadno=getBillCardWrapper().getBillCardPanel().getHeadItem("threadno").getValueObject()==null?"":getBillCardWrapper().getBillCardPanel().getHeadItem("threadno").getValueObject().toString();
			if(threadno!=null&&threadno.length()>0){
				if(!threadno.matches("^[1-9][0-9]*$")){
					MessageDialog.showOkCancelDlg(this, "提示", "并行线程数必须为数字！");
					return;
				}else{
					int no=Integer.parseInt(threadno);
					if(no>30){
						MessageDialog.showOkCancelDlg(this, "提示", "并行线程数不能大于30！");
						return;
					}
				}
			}
		}
		/*end*/
	}
	// 2011-06-04 郭义军 改
	public void filePath(){
//		是否后台发送
		String pk_datasend = getBillCardPanel().getHeadItem("pk_datasend").getValueObject().toString();
		UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("filepath").getComponent();//文件路径
		String issend=(String) getBillCardPanel().getHeadItem("issendbackground").getValueObject();

		if(!SJUtil.isNull(pk_datasend)){
			//map.put("pk_id", pk_datasend.toString());
			DipDatasendVO vo=null;
//			if(issend.equals("true")){
			map.put("pk_id", pk_datasend.toString());
//			map.put("table_h", "dip_filepath");
//			map.put("table_code", "dip_filepath.code");
//			map.put("table_name", "dip_filepath.name");
//			map.put("table_pk", "dip_filepath.pk_filepath");
			FilePathModel model=new FilePathModel();
			model.addWherePart(" and nvl(dr,0)=0");
			pane.setRefModel(model);
			/*}else{
				DataSendListener listener = new DataSendListener(this,"filepath","filepath",CredenceListener.TYPE_BODY,pane);
				if(pane!=null){
					pane.getUIButton().removeActionListener(pane.getUIButton().getListeners(ActionListener.class)[0]);
					pane.getUIButton().addActionListener(listener);
					pane.setAutoCheck(false);
					pane.setEditable(false);
				}
			}*/
		}

	}
	
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
}
