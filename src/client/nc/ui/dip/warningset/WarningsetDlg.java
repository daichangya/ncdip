package nc.ui.dip.warningset;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import nc.ui.dip.buttons.WarnTimeBtn;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.MessageEvent;
import nc.ui.pub.MessageListener;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.warningset.MyBillVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class WarningsetDlg extends UIDialog implements MessageListener{
	class IvjEventHandler implements ActionListener{
		final WarningsetDlg this$0;

		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == getBtnSave())
			{
				try {
					onBtnSave();
					
					getBtnEdit().setEnabled(true);
					getBtnAddLine().setEnabled(false);
					getBtnDelLine().setEnabled(false);
					getBtnSave().setEnabled(false);
					getBtnCancel().setEnabled(false);
					getBtnDelet().setEnabled(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(e.getSource() == getBtnCancel())
			{
				onBtnCancel();
				
				getBtnEdit().setEnabled(true);
				getBtnAddLine().setEnabled(false);
				getBtnDelLine().setEnabled(false);
				getBtnSave().setEnabled(false);
				getBtnCancel().setEnabled(false);
				getBtnDelet().setEnabled(true);
			}
			if(e.getSource() == getBtnEdit())
			{
				onBtnEdit();
				
				getBtnEdit().setEnabled(false);
				getBtnAddLine().setEnabled(true);
				getBtnDelLine().setEnabled(true);
				getBtnSave().setEnabled(true);
				getBtnCancel().setEnabled(true);
				getBtnDelet().setEnabled(false);
			}
			if(e.getSource() == getBtnAddLine()){
				onBtnAddLine();
			}
			if(e.getSource() == getBtnDelLine()){
				onBtnDelLine();
			}
			if(e.getSource() == getBtnDelet()){
				onBtnDelet();
			}
			
			if(e.getSource()==getBtnWarintime()){
				onBtnWarin();
			}
			
			/*if(e.getSource()==getBtnList()){
				onBtnList();
			}*/
		}

		public IvjEventHandler(){
			this$0=WarningsetDlg.this;
		}
	}

	private ButtonObject []btns;
	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	WarningSetClientUI billUI;
	UFBoolean m_isAuthorizedSettle;

	private UIPanel ButtonPane;

	private UIPanel MainPanel;

	private UIScrollPane MainScrollPane;

	private UIScrollPane UIScrollPane;

	private IvjEventHandler ivjEventHandler;

	private UIButton btnSave;

	private ButtonObject btnSaveObj;

	private UIButton btnCancel;

	private ButtonObject btnCancelObj;

	private UIButton btnEdit;

	private ButtonObject btnEditObj;

	private UIButton btnAddLine;

	private ButtonObject btnAddLineObj;

	private UIButton btnDelLine;

	private ButtonObject btnDelLineObj;

	private UIButton btnDelet;

	private ButtonObject btnDeletObj;

	private HashMap tableMap = null;
	
	private UIButton btnWarnTime;
	private ButtonObject btnWarnTimeObj;
	
	//列表显示
	/*private UIButton btnList;
	private ButtonObject btnListObj;*/
	
	MyBillVO bill;
	boolean isadd;
	String string;
	int index;

	/*public WarningsetDlg() {
		super();
		billUI = null;
		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		ivjEventHandler = new IvjEventHandler();
//		m_isAuthorizedSettle = isAuthorizedSettle;
//tableMap = map;
		initialize();

	}*/

	public WarningsetDlg(Container selfUI, MyBillVO bill, boolean isadd, String string, int i) {
		super(selfUI);
		this.billUI=null;
		this.bill=bill;
		this.isadd=isadd;
		this.string=string;
		this.index=i;
		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		ivjEventHandler = new IvjEventHandler();
		initialize();
		
		
		getBtnEdit().setEnabled(!isadd);
		getBtnAddLine().setEnabled(isadd);
		getBtnDelLine().setEnabled(isadd);
		getBtnSave().setEnabled(isadd);
		getBtnCancel().setEnabled(isadd);
		getBtnDelet().setEnabled(!isadd);
	}

	public void messageReceived(MessageEvent mevent) {
		if(mevent.getMessageType()==1){
			throw new BusinessRuntimeException(mevent.getMessageContent().toString());
		}
		else{
			return;
		}
	}

	private WarningSetClientUI getBillUI(){
		if(billUI==null){
			billUI=new WarningSetClientUI();
			try {
				billUI.init(bill, isadd, string, index);
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			billUI.addMessageListener(this);
			//界面加载出来处于卡片形式
			getBillUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		}
		/*try {
			//使表体可以处于编辑状态
			billUI.setBillOperate(IBillOperate.OP_EDIT);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return billUI;
	}
	private UIPanel getButtonPane()
	{
		if(ButtonPane == null)
		{
			FlowLayout flowLayout1 = new FlowLayout();
			ButtonPane = new UIPanel();
			ButtonPane.setLayout(flowLayout1);
			ButtonPane.setName("ButtonPane");
			ButtonPane.setPreferredSize(new Dimension(20, 30));
			flowLayout1.setAlignment(0);
			btns=getBillUI().getButtons();
			ButtonPane.add(getBtnEdit(),null);
			ButtonPane.add(getBtnAddLine(),null);
			ButtonPane.add(getBtnDelLine(),null);
			ButtonPane.add(getBtnSave(),null);
			ButtonPane.add(getBtnCancel(),null);
			ButtonPane.add(getBtnDelet(),null);
//			ButtonPane.add(getBtnWarintime(),null);
//			ButtonPane.add(getBtnList(),null);
		}
		return ButtonPane;
	}
	private UIScrollPane getUIScrollPane() {
		if (UIScrollPane == null) {
			UIScrollPane = new UIScrollPane();
			UIScrollPane.setName("TablePane");
		}
		return UIScrollPane;
	}

	private UIPanel getMainPanel() {
		if (MainPanel == null) {
			MainPanel = new UIPanel();
			MainPanel.setLayout(new BorderLayout());
			MainPanel.setName("MainPanel");
			MainPanel.add(getButtonPane(),"North");
			MainPanel.add(getUIScrollPane(), "Center");

		}
		return MainPanel;
	}
	private UIScrollPane getMainScrollPane() {
		if (MainScrollPane == null) {
			MainScrollPane = new UIScrollPane();
			MainScrollPane.setName("MainScrollPane");
			MainScrollPane.setViewportView(getMainPanel());

		}
		return MainScrollPane;
	}
	private void initTable() {
		getUIScrollPane().setViewportView(getBillUI());
	}
	private void initConnection()
	{

		getBtnEdit().addActionListener((ActionListener) ivjEventHandler);
		getBtnAddLine().addActionListener((ActionListener) ivjEventHandler);
		getBtnDelLine().addActionListener((ActionListener) ivjEventHandler);
		getBtnSave().addActionListener((ActionListener) ivjEventHandler);
		getBtnCancel().addActionListener((ActionListener) ivjEventHandler);
		getBtnDelet().addActionListener((ActionListener) ivjEventHandler);
		getBtnWarintime().addActionListener((ActionListener)ivjEventHandler);
//		getBtnList().addActionListener((ActionListener)ivjEventHandler);
	}
	public boolean isAuthorizedSettle() {
		return m_isAuthorizedSettle.booleanValue();
	}
	private void initialize() {
		setContentPane(getMainScrollPane());
		setSize(1000, 500);
		setLocation(120, 190);
		setTitle("预警设置");
		initTable();
		initConnection();
	}
	private void handleException(Exception e) {
		String msg = e.getMessage() != null && e.getMessage().length() != 0 ? e
				.getMessage() : e.getClass().getName();
				Debug.error(msg, e);
				MessageDialog.showErrorDlg(this, null, msg);
	}

	private UIButton getBtnSave()
	{
		if(btnSave == null)
		{
			btnSave = new UIButton();
			btnSave.setName("btnSave");
			btnSave.setText(getBtnSaveObj().getName());
		}
		return btnSave;
	}

	private ButtonObject getBtnSaveObj()
	{
		if(btnSaveObj == null)
		{
			ButtonVO btnVO = ButtonVOFactory.getInstance().build(0);
			btnSaveObj = btnVO.buildButton();
		}
		return btnSaveObj;
	}

	private UIButton getBtnCancel()
	{
		if(btnCancel == null)
		{
			btnCancel = new UIButton();
			btnCancel.setName("btnCancel");
			btnCancel.setText(getBtnCancelObj().getName());
		}
		return btnCancel;
	}

	private ButtonObject getBtnCancelObj()
	{
		if(btnCancelObj == null)
		{
			ButtonVO btnVO = ButtonVOFactory.getInstance().build(7);
			btnCancelObj = btnVO.buildButton();
		}
		return btnCancelObj;
	}

	private UIButton getBtnEdit()
	{
		if(btnEdit == null)
		{
			btnEdit = new UIButton();
			btnEdit.setName("btnEdit");
			btnEdit.setText(getBtnEditObj().getName());
		}
		return btnEdit;
	}

	private ButtonObject getBtnEditObj()
	{
		if(btnEditObj == null)
		{
			ButtonVO btnVO = ButtonVOFactory.getInstance().build(3);
			btnEditObj = btnVO.buildButton();
		}
		return btnEditObj;
	}

	private UIButton getBtnAddLine()
	{
		if(btnAddLine == null)
		{
			btnAddLine = new UIButton();
			btnAddLine.setName("btnAddLine");
			btnAddLine.setText(getBtnAddLineObj().getName());
		}
		return btnAddLine;
	}

	private ButtonObject getBtnAddLineObj()
	{
		if(btnAddLineObj == null)
		{
			ButtonVO btnVO = ButtonVOFactory.getInstance().build(IBillButton.AddLine);
			btnAddLineObj = btnVO.buildButton();
		}
		return btnAddLineObj;
	}
	private UIButton getBtnDelLine()
	{
		if(btnDelLine == null)
		{
			btnDelLine = new UIButton();
			btnDelLine.setName("btnDelLine");
			btnDelLine.setText(getBtnDelLineObj().getName());
		}
		return btnDelLine;
	}

	private ButtonObject getBtnDelLineObj()
	{
		if(btnDelLineObj == null)
		{
			ButtonVO btnVO = ButtonVOFactory.getInstance().build(IBillButton.DelLine);
			btnDelLineObj = btnVO.buildButton();
		}
		return btnDelLineObj;
	}

	private UIButton getBtnDelet()
	{
		if(btnDelet == null)
		{
			btnDelet = new UIButton();
			btnDelet.setName("btnDelet");
			btnDelet.setText(getBtnDeletObj().getName());
		}
		return btnDelet;
	}

	private ButtonObject getBtnDeletObj()
	{
		if(btnDeletObj == null)
		{
			ButtonVO btnVO = ButtonVOFactory.getInstance().build(IBillButton.Delete);
			btnDeletObj = btnVO.buildButton();
		}
		return btnDeletObj;
	}
	
	WarnTimeBtn wtb=new WarnTimeBtn();
	private UIButton getBtnWarintime(){
		if(btnWarnTime==null){
			btnWarnTime=new UIButton();
			btnWarnTime.setName("btnWarnTime");
			btnWarnTime.setText(getBtnWarinObj().getName());
		}
		return btnWarnTime;
	}
	
	private ButtonObject getBtnWarinObj(){
		if(btnWarnTimeObj==null){
			ButtonVO btnVO=wtb.getButtonVO();
			btnWarnTimeObj=btnVO.buildButton();
		}
		
		return btnWarnTimeObj;
	}
	
	/*private UIButton getBtnList(){
		if(btnList==null){
			btnList=new UIButton();
			btnList.setName("btnList");
			btnList.setText(getBtnListObj().getName());
		}
		return btnList;
	}
	private ButtonObject getBtnListObj(){
		if(btnListObj==null){
			ButtonVO btnVO=ButtonVOFactory.getInstance().build(31);
			btnListObj=btnVO.buildButton();
		}
		return btnListObj;
	}
	
	private void onBtnList(){
		try {
			getBillUI().onButtonAction(getBtnListObj());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	private void onBtnWarin(){
		try {
			getBillUI().onButtonAction(getBtnWarinObj());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onBtnSave() throws Exception
	{
			BillData bd=getBillUI().getBillCardPanel().getBillData();
			/*if(bd!=null){
				bd.dataNotNullValidate();
			}*/
			getBillUI().onButtonAction(getBtnSaveObj());
	}

	private void onBtnCancel()
	{
		try
		{
			getBillUI().onButtonAction(getBtnCancelObj());
			this.close();

		}
		catch(Exception e)
		{
			handleException(e);
		}
	}
	private void onBtnEdit()
	{
		try
		{
			getBillUI().onButtonAction(getBtnEditObj());
//			add by lyz 2012-05-17 只要在数据流程节点中表头字段:是否启用预警;预警任务类型;时间设置可以编辑
			if(index != 2){
				getBillUI().getBillCardPanel().getHeadItem("isnottiming").setEdit(false);
				getBillUI().getBillCardPanel().getHeadItem("def_timeset").setEdit(false);
				getBillUI().getBillCardPanel().getHeadItem("isnotwarning").setEdit(false);
			}
			//end add by lyz 2012-05-17 只要在数据流程节点中表头字段:是否启用预警;预警任务类型;时间设置可以编辑
		}
		catch(Exception e)
		{
			handleException(e);
		}
	}
	private void onBtnAddLine()
	{
		try
		{
			getBillUI().onButtonAction(getBtnAddLineObj());

		}
		catch(Exception e)
		{
			handleException(e);
		}
	}
	private void onBtnDelLine()
	{
		try
		{
			getBillUI().onButtonAction(getBtnDelLineObj());

		}
		catch(Exception e)
		{
			handleException(e);
		}
	}

	private void onBtnDelet()
	{
		try
		{
			getBillUI().onButtonAction(getBtnDeletObj());
			this.close();

		}
		catch(Exception e)
		{
			handleException(e);
		}
	}
}
