package nc.ui.dip.procondition;

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
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class ProconditionDlg extends UIDialog implements MessageListener {
	// /* member class not found */
	public int onSave=0;
	class IvjEventHandler implements ActionListener {

		final ProconditionDlg this$0;

		public void actionPerformed(ActionEvent e) {

			 if(e.getSource() == getBtnSave())
	          {
				 onBtnSave();
	          }
	          if(e.getSource() == getBtnCancel())
	          {
	        	  onBtnCancel();
	          }
	          
		}

		IvjEventHandler() {
			this$0 = ProconditionDlg.this;
			// super();
		}
	}

	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	ProconditionClientUI billUI;

	UFBoolean m_isAuthorizedSettle;
	
	private UIPanel ButtonPane;

	private UIPanel MainPanel;

	private UIScrollPane MainScrollPane;

	private UIScrollPane UIScrollPane;
	
	private UIButton btnSave;
	
	private ButtonObject btnSaveObj;
	
	private UIButton btnCancel;
	
	private ButtonObject btnCancelObj;

	private HashMap tableMap = null;

	private IvjEventHandler ivjEventHandler;
	
	public String sqlReturn = "";
	public ProconditionDlg(Container parent, UFBoolean isAuthorizedSettle,
			HashMap map) {

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
		m_isAuthorizedSettle = isAuthorizedSettle;
		tableMap = map;
		initialize();
	}

	private void initConnection()
	{
		
	   getBtnSave().addActionListener((ActionListener) ivjEventHandler);
	   getBtnCancel().addActionListener((ActionListener) ivjEventHandler);
	}
	
	@Override
	protected void close() {
		// TODO Auto-generated method stub
		super.close();
	}
	public ProconditionClientUI getBillUI() {
		if (billUI == null) {
			if (isAuthorizedSettle()) {
				billUI = new ProconditionClientUI(tableMap);
			} else {
				billUI = new ProconditionClientUI(tableMap);
			}
			billUI.addMessageListener(this);
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
	       ButtonPane.add(getBtnSave(), null);
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
		setTitle( "数据加工条件");
		initTable();
		initConnection();
		// setButtonStatus();
	}

	private void initTable() {
		getUIScrollPane().setViewportView(getBillUI());
		
		if(tableMap!=null&&"数据卸载".equals(tableMap.get("procetype"))){
			if(tableMap!=null&&tableMap.get("procecond")!=null){
				String table=tableMap.get("procecond").toString();
				int rowcount=getBillUI().getBillCardPanel().getRowCount();
				for(int i=0;i<rowcount;i++){
					Object obj1=getBillUI().getBillCardPanel().getBodyValueAt(i, "def6");
					Object obj=	getBillUI().getBillCardPanel().getBodyValueAt(i, "def5");
					if(obj!=null&&obj.toString().equals(table)){
						getBillUI().getBillCardPanel().setBodyValueAt(new UFBoolean(true), i, "def6");
					}
				
				}
			}
		}
		
		//getBillUI().getBillCardWrapper().getBillCardPanel().setBodyValueAt(arg0, arg1, arg2)
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
	   {	onSave=1;
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
	
	public String getReturnSql(){
		return sqlReturn;
	}
	
	public void setReturnSql(String sql){
		sqlReturn = sql;
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
