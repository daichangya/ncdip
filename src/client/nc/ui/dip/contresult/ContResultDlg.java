package nc.ui.dip.contresult;

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

public class ContResultDlg extends UIDialog implements MessageListener {
	// /* member class not found */
	class IvjEventHandler implements ActionListener {

		final ContResultDlg this$0;

		public void actionPerformed(ActionEvent e) {

			 if(e.getSource() == getBtnDelete())
	          {
				 onBtnDelete();
	          }
	          
	          
		}

		IvjEventHandler() {
			this$0 = ContResultDlg.this;
			// super();
		}
	}

	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	ContResultClientUI billUI;

	UFBoolean m_isAuthorizedSettle;
	
	private UIPanel ButtonPane;

	private UIPanel MainPanel;

	private UIScrollPane MainScrollPane;

	private UIScrollPane UIScrollPane;
	
	private UIButton BtnDelete;
	
	private ButtonObject BtnDeleteObj;
	

	private HashMap tableMap = null;

	private IvjEventHandler ivjEventHandler;
	
	public String sqlReturn = "";
	public ContResultDlg(Container parent, UFBoolean isAuthorizedSettle,
			HashMap map) {

		super(parent);
		billUI = null;

		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		ivjEventHandler = new IvjEventHandler();
		BtnDelete = null;
		BtnDeleteObj = null;
		m_isAuthorizedSettle = isAuthorizedSettle;
		tableMap = map;
		initialize();
	}

	private void initConnection()
	{
		
	   getBtnDelete().addActionListener((ActionListener) ivjEventHandler);
	 
	}
	
	public ContResultClientUI getBillUI() {
		if (billUI == null) {
			if (isAuthorizedSettle()) {
				billUI = new ContResultClientUI(tableMap);
			} else {
				billUI = new ContResultClientUI(tableMap);
			}
			billUI.addMessageListener(this);
		}
		return billUI;
	}
	
	private UIButton getBtnDelete()
	{
	   if(BtnDelete == null)
	   {
	       BtnDelete = new UIButton();
	       BtnDelete.setName("BtnDelete");
	       BtnDelete.setText(getBtnDeleteObj().getName());
	   }
	   return BtnDelete;
	}

	private ButtonObject getBtnDeleteObj()
	{
	   if(BtnDeleteObj == null)
	   {
	       ButtonVO btnVO = ButtonVOFactory.getInstance().build(IBillButton.Delete);
	       BtnDeleteObj = btnVO.buildButton();
	   }
	   return BtnDeleteObj;
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
	       ButtonPane.add(getBtnDelete(), null);
//	       ButtonPane.add(getBtnCancel(), null);
	      
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
		setSize(600, 350);
		setLocation(330, 200);
		setTitle(NCLangRes.getInstance().getStrByID("400122", "对照结果维护"));
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

	private void onBtnDelete()
	{
	   try
	   {
	       getBillUI().onButtonAction(getBtnDeleteObj());
	       setReturnSql(getBillUI().returnSql);
	       this.close();
	      
	   }
	   catch(Exception e)
	   {
	       handleException(e);
	   }
//	   setButtonStatus();
	}
	
	public String getReturnSql(){
		return sqlReturn;
	}
	
	public void setReturnSql(String sql){
		sqlReturn = sql;
	}
	
	public void messageReceived(MessageEvent e) {
		if (e.getMessageType() == 1) {
			throw new BusinessRuntimeException(e.getMessageContent().toString());
		} else {
			return;
		}
	}

	

	
}
