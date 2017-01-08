package nc.ui.dip.dataformatdef;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import nc.ui.dip.buttons.MoveddownBtn;
import nc.ui.dip.buttons.MovedupBtn;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.MessageEvent;
import nc.ui.pub.MessageListener;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.trade.button.ButtonVOFactory;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;


public class DataFormatDlg  extends UIDialog
implements MessageListener{
//	/* member class not found */
	class IvjEventHandler implements ActionListener {

		final DataFormatDlg this$0;

		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == getBtnSave())
			{
				onBtnSave();
			}
			if(e.getSource() == getBtnCancel())
			{
				onBtnCancel();
			}
			if(e.getSource() == getBtnAdd())
			{
				onBtnAdd();
			}

			if(e.getSource()==getBtnDel()){
				onBtnDel();
			}

			if(e.getSource()==getBtnUp()){
				onBtnUp();
			}

			if(e.getSource()==getBtnAddLine()){
				onBtnAddLine();
			}

			if(e.getSource()==getBtnInsLine()){
				onBtnInsLine();
			}

			if(e.getSource()==getBtnDown()){
				onBtnDown();
			}
		}

		IvjEventHandler() {
			this$0 = DataFormatDlg.this;
			// super();
		}
	}

	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	DataForDefClientUI billUI;

	UFBoolean m_isAuthorizedSettle;

	private UIPanel ButtonPane;

	private UIPanel MainPanel;

	private UIScrollPane MainScrollPane;

	private UIScrollPane UIScrollPane;

	private UIButton btnSave;

	private ButtonObject btnSaveObj;

	private UIButton btnCancel;

	private ButtonObject btnCancelObj;

	private UIButton btnAdd;

	private ButtonObject btnAddObj;

	private UIButton btnDel;
	private ButtonObject btnDelObj;

	private UIButton btnAddLine;
	private ButtonObject btnAddLineObj;

	private UIButton btnInsLine;
	private ButtonObject btnInsLineObj;

	//上移、下移按钮
	private UIButton btnUp;
	private ButtonObject btnUpObj;
	private UIButton btnDown;
	private ButtonObject btnDownObj;

	private DipDatarecHVO hvo = null;

	private IvjEventHandler ivjEventHandler;

	public String sqlReturn = "";
	public DataFormatDlg(Container parent, UFBoolean isAuthorizedSettle,
			DipDatarecHVO hvo) {

		super(parent);
		billUI = null;

		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		ivjEventHandler = new IvjEventHandler();
		btnSave = null;
		btnSaveObj = null;
		btnCancel = null;
		btnCancelObj = null;
		btnAdd = null;
		btnAddObj = null;

		btnAddLine=null;
		btnAddLineObj=null;

		btnInsLine=null;
		btnInsLineObj=null;

		btnDel=null;
		btnDelObj=null;

		btnUp=null;
		btnUpObj=null;
		btnDown=null;
		btnDownObj=null;

		m_isAuthorizedSettle = isAuthorizedSettle;
		this.hvo=hvo;
		initialize();
	}

	private void initConnection()
	{
		//按钮事件监听
		getBtnSave().addActionListener((ActionListener) ivjEventHandler);
		//getBtnCancel().addActionListener((ActionListener) ivjEventHandler);
		getBtnUp().addActionListener((ActionListener)ivjEventHandler);
		getBtnDown().addActionListener((ActionListener)ivjEventHandler);
		getBtnDel().addActionListener((ActionListener)ivjEventHandler);
		//getBtnInsLine().addActionListener((ActionListener)ivjEventHandler);
	}

	public DataForDefClientUI getBillUI() {
		if (billUI == null) {
			if (isAuthorizedSettle()) {
				billUI = new DataForDefClientUI(hvo);
			} else {
				billUI = new DataForDefClientUI(hvo);
			}
			billUI.addMessageListener(this);
		}
		//界面加载出来时处于可编辑状态
//		billUI.getBillCardPanel().getHeadItem("messflowtype").setEdit(true);
//		billUI.getBillCardPanel().getHeadItem("messflowdef").setEdit(true);
//		billUI.getBillCardPanel().getHeadItem("beginsign").setEdit(true);
//		billUI.getBillCardPanel().getHeadItem("endsign").setEdit(true);
		
		/*int bodyrow=billUI.getBillCardPanel().getRowCount();
		if(bodyrow>=1){
			for(int i=0;i<8;i++){
				if(i==0 || i==1 || i==2 || (i+1)==bodyrow){
					//设置某一行的某一列不可编辑状态
					billUI.getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(i, "datakind", false);
				}else{
					try {
						billUI.setBillOperate(IBillOperate.OP_EDIT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}*/
		
		/*
		 * 作者：程莉
		 * 日期：2011-6-14
		 * 描述："顺序"对照，则"对应项(correskind)"字段不可编辑；"字段"对照，则"对应项"可编辑 0:顺序对照；1：字段对照
		 */
		//消息流格式：0：顺序,1：字段
		String messflowdef=(String) billUI.getBillCardPanel().getHeadItem("messflowdef").getValueObject();
		int row=billUI.getBillCardPanel().getRowCount();
		//如果为"顺序"对照，则"对应项"不可编辑 0:顺序对照；1：字段对照
		if("0".equals(messflowdef)){
			for(int i=0;i<row;i++){
				billUI.getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(i, "correskind", false);
			}
		}else{
			for(int i=0;i<row;i++){
				billUI.getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(i, "correskind", true);
			}
		}

		//设置不可排序
		billUI.getBillCardPanel().getBillTable().setSortEnabled(false);
		return billUI;
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
	private UIButton getBtnAdd()
	{
		if(btnAdd == null)
		{
			btnAdd = new UIButton();
			btnAdd.setName("btnAdd");
			btnAdd.setText(getBtnAddObj().getName());
		}
		return btnAdd;
	}

	private ButtonObject getBtnAddObj()
	{
		if(btnAddObj == null)
		{
			ButtonVO btnVO = ButtonVOFactory.getInstance().build(1);
			btnAddObj = btnVO.buildButton();
		}
		return btnAddObj;
	}

	//	2011-5-13 cl
	public UIButton getBtnDel(){
		if(btnDel==null){
			btnDel=new UIButton();
			btnDel.setName("btnDel");
			btnDel.setText(getBtnDelObj().getName());
		}

		return btnDel;
	}

	private ButtonObject getBtnDelObj(){
		if(btnDelObj==null){
			ButtonVO btnVO=ButtonVOFactory.getInstance().build(32);
			btnDelObj=btnVO.buildButton();
		}
		return btnDelObj;
	}

	public UIButton getBtnAddLine(){
		if(btnAddLine==null){
			btnAddLine=new UIButton();
			btnAddLine.setName("btnAddLine");
			btnAddLine.setText(getBtnAddLineObj().getName());
		}
		return btnAddLine;
	}
	public ButtonObject getBtnAddLineObj(){
		if(btnAddLineObj==null){
			ButtonVO btnVO=ButtonVOFactory.getInstance().build(11);
			btnAddLineObj=btnVO.buildButton();
		}
		return btnAddLineObj;
	}
	public UIButton getBtnInsLine(){
		if(btnInsLine==null){
			btnInsLine=new UIButton();
			btnInsLine.setName("btnInsLine");
			btnInsLine.setText(getBtnInsLineObj().getName());
		}
		return btnInsLine;
	}
	public ButtonObject getBtnInsLineObj(){
		if(btnInsLineObj==null){
			ButtonVO btnVO=ButtonVOFactory.getInstance().build(15);
			btnInsLineObj=btnVO.buildButton();
		}
		return btnInsLineObj;
	}

	//“上移”按钮
	MovedupBtn up=new MovedupBtn();
	public UIButton getBtnUp(){
		if(btnUp==null){
			btnUp=new UIButton();
			btnUp.setName("Movedup");
			btnUp.setText(getBtnUpObj().getName());
		}
		return btnUp;
	}	
	private ButtonObject getBtnUpObj(){
		if(btnUpObj==null){
			ButtonVO btnVO=up.getButtonVO();
			btnUpObj=btnVO.buildButton();
		}
		return btnUpObj;
	}

	//”下移“ 按钮
	MoveddownBtn down=new MoveddownBtn();
	public UIButton getBtnDown(){
		if(btnDown==null){
			btnDown=new UIButton();
			btnDown.setName("Moveddown");
			btnDown.setText(getBtnDownObj().getName());
		}
		return btnDown;
	}
	public ButtonObject getBtnDownObj(){
		if(btnDownObj==null){
			ButtonVO btnVO=down.getButtonVO();
			btnDownObj=btnVO.buildButton();
		}
		return btnDownObj;
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
			//ButtonPane.add(getBtnAdd(),null);
			//ButtonPane.add(getBtnAddLine(), null);
			ButtonPane.add(getBtnSave(), null);
			//ButtonPane.add(getBtnCancel(), null);
			//上移、下移、插入、删除
			ButtonPane.add(getBtnUp(), null);
			ButtonPane.add(getBtnDown(), null);
			//ButtonPane.add(getBtnInsLine(),null);
			ButtonPane.add(getBtnDel(), null);
		}
		return ButtonPane;
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

	private UIScrollPane getUIScrollPane() {
		if (UIScrollPane == null) {
			UIScrollPane = new UIScrollPane();
			UIScrollPane.setName("TablePane");
		}
		return UIScrollPane;
	}

	private void handleException(Exception e) {
		String msg = e.getMessage() != null && e.getMessage().length() != 0 ? e
				.getMessage() : e.getClass().getName();
				Debug.error(msg, e);
				MessageDialog.showErrorDlg(this, null, msg);
	}

	private void initialize() {
		setContentPane(getMainScrollPane());
		setSize(800, 530);
		setLocation(350, 250);
		setTitle(NCLangRes.getInstance().getStrByID("400122", "数据格式定义"));
		initTable();
		initConnection();
		// setButtonStatus();
	}

	private void initTable() {
		getUIScrollPane().setViewportView(getBillUI());
	}

	public boolean isAuthorizedSettle() {
		return m_isAuthorizedSettle.booleanValue();
	}



	public void setIsAuthorizedSettle(UFBoolean authorizedSettle) {
		m_isAuthorizedSettle = authorizedSettle;
	}

	protected void processWindowEvent(WindowEvent e) {
		try {
			if (e.getID() == 201) {
				boolean closeResult = getBillUI().onClosing();
				if (!closeResult) {
					return;
				}
			}
		} catch (Exception exception) {
			MessageDialog.showErrorDlg(this, null, exception.getMessage());
			return;
		}
		super.processWindowEvent(e);
	}

	private void onBtnSave()
	{
		try
		{
			getBillUI().onButtonAction(getBtnSaveObj());
			this.close();

		}
		catch(Exception e)
		{
			handleException(e);
		}
//		setButtonStatus();
	}

	private void onBtnCancel()
	{
		try
		{
			getBillUI().onButtonAction(getBtnCancelObj());

		}
		catch(Exception e)
		{
			handleException(e);
		}
//		setButtonStatus();
	}

	private void onBtnAdd()
	{
		try
		{
			getBillUI().onButtonAction(getBtnAddObj());

		}
		catch(Exception e)
		{
			handleException(e);
		}
//		setButtonStatus();
	}

	private void onBtnDel(){
		try{
			getBillUI().onButtonAction(getBtnDelObj());
			this.close();
		}catch (Exception e) {
			// TODO: handle exception
			handleException(e);
		}
	}

	private void onBtnAddLine(){
		try {
			getBillUI().onButtonAction(getBtnAddLineObj());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void onBtnInsLine(){
		try {
			getBillUI().onButtonAction(getBtnInsLineObj());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void onBtnUp(){
		try{
			getBillUI().onButtonAction(getBtnUpObj());
		}catch (Exception e) {
			// TODO: handle exception
			handleException(e);
		}
	}
	private void onBtnDown(){
		try {
			getBillUI().onButtonAction(getBtnDownObj());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void messageReceived(MessageEvent e) {
		if (e.getMessageType() == 1) {
			throw new BusinessRuntimeException(e.getMessageContent().toString());
		} else {
			return;
		}
	}




}
