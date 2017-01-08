package nc.ui.dip.exp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.MessageEvent;
import nc.ui.pub.MessageListener;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.vo.dip.exp.DataExpVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class ExpDlg extends UIDialog implements MessageListener {
	// /* member class not found */
	class IvjEventHandler implements ActionListener {

		final ExpDlg this$0;

		public void actionPerformed(ActionEvent e) {

//			UIButton bt=(UIButton) e.getSource();
			 if(e.getSource() == getBtnSave()){
				try {
					DataExpVO[] bvo = (DataExpVO[])getBillUI().getVOFromUI().getChildrenVO();
					if(bvo[0].getPrimaryKey()==null||bvo[0].getPrimaryKey().length()<20){
						HYPubBO_Client.insertAry(bvo);
					}else{
						HYPubBO_Client.updateAry(bvo);
					}
					close();
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 }else if(e.getSource()==getBtnNC()){
				 getBillUI().initdef(hpk, false);
			 }else if(e.getSource()==getBtnWB()){
				 getBillUI().initdef(hpk, true);
			 }
			 
			  
			 
		
		}

		IvjEventHandler() {
			this$0 = ExpDlg.this;
			// super();
		}
	}

	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

    MyExpClientUI billUI;

	UFBoolean m_isAuthorizedSettle;
	
	
	private UIPanel ButtonPane;

	private UIPanel MainPanel;

	private UIScrollPane MainScrollPane;

	private UIScrollPane UIScrollPane;
	
	private UIButton btnSave;
	
	private ButtonObject btnSaveObj;
	
	private UIButton wbSys;
//	
//	private ButtonObject btnEffectObj;
//	
	private UIButton ncSys;
//	
//	private ButtonObject btnModelObj;

//	private HashMap tableMap = null;
	private String hpk;
	private boolean isSys;
	private IvjEventHandler ivjEventHandler;
	
	public String sqlReturn = "";
	public ExpDlg(Container parent, UFBoolean isAuthorizedSettle,
			String hpk,boolean isSys) {

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
//		btnEffect=null;
//		btnEffectObj=null;
//		btnModel=null;
//		btnModelObj=null;
//		btnCancel = null;
//		btnCancelObj = null;
		wbSys=null;
		ncSys=null;
		m_isAuthorizedSettle = isAuthorizedSettle;
		this.hpk=hpk;
		this.isSys=isSys;
		initialize();
	}

	private void initConnection()
	{
		
	   getBtnSave().addActionListener((ActionListener) ivjEventHandler);
	   getBtnWB().addActionListener((ActionListener)ivjEventHandler);
	   getBtnNC().addActionListener((ActionListener)ivjEventHandler);
//	   getBtnCancel().addActionListener((ActionListener) ivjEventHandler);
	}
	
	
	
	@Override
	protected void close() {
		// TODO Auto-generated method stub
		super.close();
	}

	public MyExpClientUI getBillUI() {
		if (billUI == null) {
			if (isAuthorizedSettle()) {
				billUI = new MyExpClientUI();
				billUI.initdef(hpk,isSys);
			} else {
				billUI = new MyExpClientUI();
				billUI.initdef(hpk,isSys);
			}
			try {
				//使表体可以处于编辑状态
				billUI.setBillOperate(IBillOperate.OP_EDIT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			billUI.addMessageListener(this);
		}
		//getBillUI().getBillCardPanel().getBillModel().addLine();
		
		
		return billUI;
	}
	//外部系统
	private UIButton getBtnWB(){
		if(wbSys==null){
			wbSys=new UIButton();
			wbSys.setName("wbSys");
			wbSys.setText("外部系统");
			
		}
		return wbSys;
	}
	//NC系统
	private UIButton getBtnNC(){
		if(ncSys==null){
			ncSys=new UIButton();
			ncSys.setName("ncSys");
			ncSys.setText("NC系统");
		}
		return ncSys;
	}
	//保存按钮
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
	
/*	private UIButton getBtnCancel()
	{
	   if(btnCancel == null)
	   {
	       btnCancel = new UIButton();
	       btnCancel.setName("btnCancel");
	       btnCancel.setText(getBtnCancelObj().getName());
	   }
	   return btnCancel;
	}*/

/*	private ButtonObject getBtnCancelObj()
	{
	   if(btnCancelObj == null)
	   {
	       ButtonVO btnVO = ButtonVOFactory.getInstance().build(7);
	       btnCancelObj = btnVO.buildButton();
	   }
	   return btnCancelObj;
	}*/
	private UIPanel getButtonPane()
	{
	   if(ButtonPane == null)
	   {
	       FlowLayout flowLayout1 = new FlowLayout();
	       ButtonPane = new UIPanel();
	       ButtonPane.setLayout(flowLayout1);
	       ButtonPane.setName("ButtonPane");
	       ButtonPane.setPreferredSize(new Dimension(30, 40));
	       flowLayout1.setAlignment(0);
	       ButtonPane.add(getBtnNC(), null);
	       ButtonPane.add(getBtnWB(), null);
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
		setTitle(NCLangRes.getInstance().getStrByID("400122", "查询条件"));
		initTable();
		initConnection();
		//getBillUI().getBillCardPanel().getBodyItem("effectname").setEdit(true);
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

/*	private void onBtnSave()
	{
	   try
	   {
	       getBillUI().onButtonAction(getBtnSaveObj());
	      // setReturnSql(getBillUI().returnSql);
	       this.close();
	      
	   }
	   catch(Exception e)
	   {
	       handleException(e);
	   }
//	   setButtonStatus();
	}*/
	
	public String getReturnSql(){
		return sqlReturn;
	}
	
	public void setReturnSql(String sql){
		sqlReturn = sql;
	}
/*	private void onBtnCancel()
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
	}*/
	public void messageReceived(MessageEvent e) {
		if (e.getMessageType() == 1) {
			throw new BusinessRuntimeException(e.getMessageContent().toString());
		} else {
			return;
		}
	}

	

	
}
