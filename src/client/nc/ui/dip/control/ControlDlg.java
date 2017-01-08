package nc.ui.dip.control;

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
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.control.MyBillVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class ControlDlg extends UIDialog implements MessageListener{
	class IvjEventHandler implements ActionListener{
		final ControlDlg this$0;

		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == getBtnSave()){
				try {
					onBtnSave();

					getBtnEdit().setEnabled(true);
					getBtnSave().setEnabled(false);
					getBtnCancel().setEnabled(false);
					getBtnDelet().setEnabled(true);
				} catch(Exception ee){
					handleException(ee);
				}
			}else if(e.getSource() == getBtnCancel()){
				try {
					onBtnCancel();

					getBtnEdit().setEnabled(true);
					getBtnSave().setEnabled(false);
					getBtnCancel().setEnabled(false);
					getBtnDelet().setEnabled(true);
				} catch(Exception ee){
					handleException(ee);
				}
			}else if(e.getSource() == getBtnEdit()){
				try {
					onBtnEdit();

					getBtnEdit().setEnabled(false);
					getBtnSave().setEnabled(true);
					getBtnCancel().setEnabled(true);
					getBtnDelet().setEnabled(false);
				} catch(Exception ee){
					handleException(ee);
				}
			}else if(e.getSource() == getBtnDelet()){
				try {
					onBtnDelet();

					getBtnEdit().setEnabled(true);
					getBtnSave().setEnabled(false);
					getBtnCancel().setEnabled(false);
					getBtnDelet().setEnabled(true);
				} catch(Exception ee){
					handleException(ee);
				}
			}
			
		}

		public IvjEventHandler(){
			this$0=ControlDlg.this;
		}
	}

	private ButtonObject []btns;
	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	ControlClientUI billUI;
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

	private UIButton btnDelet;

	private ButtonObject btnDeletObj;

	
	private ControlHVO chvo;
	private String pk_bus;
	private String ywlx;
	private String tabls;
	private boolean isenable=true;

	public ControlDlg(Container selfUI,ControlHVO chvo,String pk_bus,String ywlx,String tabls) {
		super(selfUI);
		this.billUI=null;
		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		this.chvo=chvo;
		this.pk_bus=pk_bus;
		this.ywlx=ywlx;
		this.tabls=tabls;
		ivjEventHandler = new IvjEventHandler();
		initialize();
		getBtnCancel().setEnabled(false);
		getBtnSave().setEnabled(false);
	}
	public ControlDlg(Container selfUI,ControlHVO chvo,String pk_bus,String ywlx,String tabls,boolean isenable) {
		super(selfUI);
		this.billUI=null;
		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		this.chvo=chvo;
		this.pk_bus=pk_bus;
		this.ywlx=ywlx;
		this.tabls=tabls;
		this.isenable=isenable;
		ivjEventHandler = new IvjEventHandler();
		initialize();
		
	}
	public void messageReceived(MessageEvent mevent) {
		if(mevent.getMessageType()==1){
			throw new BusinessRuntimeException(mevent.getMessageContent().toString());
		}
		else{
			return;
		}
	}

	private ControlClientUI getBillUI(){
		if(billUI==null){
			billUI=new ControlClientUI();
			try {
				billUI.init(  chvo,  pk_bus,  ywlx,  tabls);
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			billUI.addMessageListener(this);
			//界面加载出来处于卡片形式
//			getBillUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
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
//			ButtonPane.add(getBtnAddLine(),null);
//			ButtonPane.add(getBtnDelLine(),null);
			ButtonPane.add(getBtnSave(),null);
			ButtonPane.add(getBtnCancel(),null);
			ButtonPane.add(getBtnDelet(),null);
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
//		getBtnAddLine().addActionListener((ActionListener) ivjEventHandler);
//		getBtnDelLine().addActionListener((ActionListener) ivjEventHandler);
		getBtnSave().addActionListener((ActionListener) ivjEventHandler);
		getBtnCancel().addActionListener((ActionListener) ivjEventHandler);
		getBtnDelet().addActionListener((ActionListener) ivjEventHandler);
//		getBtnList().addActionListener((ActionListener)ivjEventHandler);
	}
	public boolean isAuthorizedSettle() {
		return m_isAuthorizedSettle.booleanValue();
	}
	private void initialize() {
		setContentPane(getMainScrollPane());
		setSize(1000, 500);
		setLocation(120, 190);
		setTitle("控制设置");
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
			btnSave.setEnabled(isenable);
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
			btnCancel.setEnabled(isenable);
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
				btnEdit.setEnabled(isenable);
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
	private UIButton getBtnDelet()
	{
		if(btnDelet == null)
		{
			btnDelet = new UIButton();
			btnDelet.setName("btnDelet");
			btnDelet.setText(getBtnDeletObj().getName());
			btnDelet.setEnabled(isenable);
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
	
	
	

	private void onBtnSave() throws Exception
	{
			BillData bd=getBillUI().getBillCardPanel().getBillData();
			if(bd!=null){
				bd.dataNotNullValidate();
			}
			getBillUI().onButtonAction(getBtnSaveObj());
		
	}

	private void onBtnCancel() throws Exception
	{
			getBillUI().onButtonAction(getBtnCancelObj());
			this.close();

	}
	private void onBtnEdit() throws Exception
	{
			getBillUI().onButtonAction(getBtnEditObj());

	}

	private void onBtnDelet() throws Exception
	{
			getBillUI().onButtonAction(getBtnDeletObj());
			this.close();

	}
}
