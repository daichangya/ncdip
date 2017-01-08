package nc.ui.dip.contwhquery;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;

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
import nc.ui.trade.button.IBillButton;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class ContWHQueryDlg extends UIDialog implements MessageListener {
	// /* member class not found */
	class IvjEventHandler implements ActionListener {

		final ContWHQueryDlg this$0;

		public void actionPerformed(ActionEvent e) {

			 if(e.getSource() == getBtnSave())
	          {
				 onBtnSave();
	          }
	          if(e.getSource() == getBtnCancel())
	          {
	        	  onBtnCancel();
	          }else if(e.getSource()==getBtnLineAdd()){
	        	  onBtnLineAdd();
	          }else if(e.getSource()==getBtnLineDel()){
	        	  onBtnLineDel();
	          }
	          
		}

		IvjEventHandler() {
			this$0 = ContWHQueryDlg.this;
			// super();
		}
	}

	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	ContWHQueryClientUI billUI;

	UFBoolean m_isAuthorizedSettle;
	
	private UIPanel ButtonPane;

	private UIPanel MainPanel;

//	private UIScrollPane MainScrollPane;

//	private UIScrollPane UIScrollPane;
	
	private UIButton btnSave;
	private UIButton btnLinAdd;
	private UIButton btnLinDel;
	
	private ButtonObject btnSaveObj;
	private ButtonObject btnLineAddObj;
	private ButtonObject btnLineDelObj;
	
	private UIButton btnCancel;
	
	private ButtonObject btnCancelObj;

	private HashMap tableMap = null;

	private IvjEventHandler ivjEventHandler;
	
	public int BTNSAVE=0;//如果是0表示没有点击保存按钮，如果是1表示点击了保存按钮。
	public int getIsOnboSave(){
		return BTNSAVE;
	}
	public String sqlReturn = "";
	public ContWHQueryDlg(Container parent, UFBoolean isAuthorizedSettle,
			HashMap map) {

		super(parent);
		billUI = null;

		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
//		MainScrollPane = null;
//		UIScrollPane = null;
		ivjEventHandler = new IvjEventHandler();
		btnSave = null;
		btnSaveObj = null;
		btnCancel = null;
		btnCancelObj = null;
		m_isAuthorizedSettle = isAuthorizedSettle;
		tableMap = map;
		initialize();
	}

	private void initConnection()
	{
		
	   getBtnSave().addActionListener((ActionListener) ivjEventHandler);
	   getBtnCancel().addActionListener((ActionListener) ivjEventHandler);
	   getBtnLineAdd().addActionListener((ActionListener) ivjEventHandler);
	   getBtnLineDel().addActionListener((ActionListener) ivjEventHandler);
	}
	
	public ContWHQueryClientUI getBillUI() {
		if (billUI == null) {
			if (isAuthorizedSettle()) {
				billUI = new ContWHQueryClientUI(tableMap);
			} else {
				billUI = new ContWHQueryClientUI(tableMap);
			}
			billUI.addMessageListener(this);
			billUI.setBounds(3, 3, 400, 200);
		}
		return billUI;
	}
	
	private UIButton getBtnSave()
	{
	   if(btnSave == null)
	   {
	       btnSave = new UIButton();
	       btnSave.setName("btnSave");
	       btnSave.setText(getBtnSaveObj().getName());
	       btnSave.setName("确定");
	   }
	   return btnSave;
	}
	private UIButton getBtnLineAdd()
	{
	   if(btnLinAdd == null)
	   {
		   btnLinAdd = new UIButton();
		   btnLinAdd.setName("btnLinAdd");
		   btnLinAdd.setText(getBtnLineAddObj().getName());
		   btnLinAdd.setName("增行");
	   }
	   return btnLinAdd;
	}
	private UIButton getBtnLineDel()
	{
	   if(btnLinDel == null)
	   {
		   btnLinDel = new UIButton();
		   btnLinDel.setName("btnLinDel");
		   btnLinDel.setText(getBtnLineDelObj().getName());
		   btnLinDel.setName("删行行");
	   }
	   return btnLinDel;
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
	private ButtonObject getBtnLineAddObj()
	{
	   if(btnLineAddObj == null)
	   {
	       ButtonVO btnVO = ButtonVOFactory.getInstance().build(IBillButton.AddLine);
	       btnLineAddObj = btnVO.buildButton();
	   }
	   return btnLineAddObj;
	}
	private ButtonObject getBtnLineDelObj()
	{
	   if(btnLineDelObj == null)
	   {
	       ButtonVO btnVO = ButtonVOFactory.getInstance().build(IBillButton.DelLine);
	       btnLineDelObj = btnVO.buildButton();
	   }
	   return btnLineDelObj;
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
	private UIPanel getButtonPane()
	{
	   if(ButtonPane == null)
	   {
	       FlowLayout flowLayout1 = new FlowLayout();
	       ButtonPane = new UIPanel();
	       ButtonPane.setLayout(flowLayout1);
//	       ButtonPane.setName("ButtonPane");
//	       ButtonPane.setPreferredSize(new Dimension(20, 30));
//	       flowLayout1.setAlignment(0);
	       ButtonPane.add(getBtnLineAdd(), null);
	       ButtonPane.add(getBtnLineDel(), null);
	       ButtonPane.add(getBtnSave(), null);
	       ButtonPane.add(getBtnCancel(), null);
	      
	   }
	   return ButtonPane;
	}

	private UIPanel getMainPanel() {
		if (MainPanel == null) {
			MainPanel = new UIPanel();
			MainPanel.setLayout(new BorderLayout());
			MainPanel.setName("MainPanel");
			MainPanel.add(getButtonPane(),BorderLayout.SOUTH);
			MainPanel.add(getBillUI(), "Center");

		}
		return MainPanel;
	}

/*	private UIScrollPane getMainScrollPane() {
		if (MainScrollPane == null) {
			MainScrollPane = new UIScrollPane();
			MainScrollPane.setName("MainScrollPane");
			MainScrollPane.setViewportView(getMainPanel());

		}
		return MainScrollPane;
	}*/

/*	private UIScrollPane getUIScrollPane() {
		if (UIScrollPane == null) {
			UIScrollPane = new UIScrollPane();
			UIScrollPane.setName("TablePane");
		}
		return UIScrollPane;
	}*/

	private void handleException(Exception e) {
		String msg = e.getMessage() != null && e.getMessage().length() != 0 ? e
				.getMessage() : e.getClass().getName();
		Debug.error(msg, e);
		MessageDialog.showErrorDlg(this, null, msg);
	}

	private void initialize() {
		setContentPane(getMainPanel());
		setSize(800, 400);
		setLocation(330, 200);
		setTitle(NCLangRes.getInstance().getStrByID("400122", "查询条件"));
//		initTable();
		initConnection();
		// setButtonStatus();
	}

	/*private void initTable() {
		getUIScrollPane().setViewportView(getBillUI());
	}
*/
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
		   BTNSAVE=1;
	       getBillUI().onButtonAction(getBtnSaveObj());
	       setReturnSql(getBillUI().returnSql);
	       this.close();
	      
	   }
	   catch(Exception e)
	   {
	       handleException(e);
	   }
//	   setButtonStatus();
	}
	private void onBtnLineAdd()
	{
	   try
	   {	
	       getBillUI().onButtonAction(getBtnLineAddObj());
	      
	   }
	   catch(Exception e)
	   {
	       handleException(e);
	   }
	}
	private void onBtnLineDel()
	{
	   try
	   {	
	       getBillUI().onButtonAction(getBtnLineDelObj());
	      
	   }
	   catch(Exception e)
	   {
	       handleException(e);
	   }
	}
	
	public String getReturnSql(){
		return sqlReturn;
	}
	
	public void setReturnSql(String sql){
		sqlReturn = sql;
	}
	private void onBtnCancel()
	{
		this.close();
//	   try
//	   {
//	       getBillUI().onButtonAction(getBtnCancelObj());
//	      
//	   }
//	   catch(Exception e)
//	   {
//	       handleException(e);
//	   }
//	   setButtonStatus();
	}
	public void messageReceived(MessageEvent e) {
		if (e.getMessageType() == 1) {
			throw new BusinessRuntimeException(e.getMessageContent().toString());
		} else {
			return;
		}
	}

	

	
}
