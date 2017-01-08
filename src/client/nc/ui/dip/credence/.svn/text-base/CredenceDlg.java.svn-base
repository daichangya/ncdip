package nc.ui.dip.credence;

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
import nc.util.dip.sj.SJUtil;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;

public class CredenceDlg extends UIDialog implements MessageListener {
	// /* member class not found */
	class IvjEventHandler implements ActionListener {

		final CredenceDlg this$0;

		public void actionPerformed(ActionEvent e) {
			UIButton bt=(UIButton) e.getSource();
			for(int i=0;i<btns.length;i++){
				
				if(bt.getName().equals(btns[i].getName())){
					try {
						getBillUI().onButtonAction(btns[i]);
						if(btns[i].getName().equals("修改")){
							setbtnstatus(false);
						}else if(btns[i].getName().equals("保存")||btns[i].getName().equals("取消")){
							setbtnstatus(true);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					break;
				}
			}
	          
		}

		IvjEventHandler() {
			this$0 = CredenceDlg.this;
			// super();
		}
	}
	boolean isadd;

	public void setbtnstatus(boolean canboedit){
		for(int i=0;i<uib.length;i++){
			if(uib[i]!=null){
			if(uib[i].getName().equals("保存")||btns[i].getName().equals("取消")||btns[i].getName().equals("增行")||btns[i].getName().equals("删行")||btns[i].getName().equals("导入")){
				uib[i].setEnabled(!canboedit);
			}else{
				uib[i].setEnabled(canboedit);
			}
			if(uib[i].getName().equals("初始环境")){
				uib[i].setEnabled(true);
			}
			}
		}
	}
	private ButtonObject[] btns;
	private UIButton[] uib;
	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	CredenceClientUI billUI;

	UFBoolean m_isAuthorizedSettle;
	
	private UIPanel ButtonPane;

	private UIPanel MainPanel;

	private UIScrollPane MainScrollPane;

	private UIScrollPane UIScrollPane;
	
	
	
//数据转换的附表主键
	private String pk;
	private String pk_datadef;

	private IvjEventHandler ivjEventHandler;
	
	public String sqlReturn = "";
	/**
	 * @desc 构造方法
	 * @param parent clientui
	 * @param isAuthorinzendSettle 
	 * @param pk数据转换的附表主键
	 * */
	public CredenceDlg(Container parent, UFBoolean isAuthorizedSettle,
			String pk,String datadef) {

		super(parent);
		billUI = null;
		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		ivjEventHandler = new IvjEventHandler();
		m_isAuthorizedSettle = isAuthorizedSettle;
		this.pk=pk;
		this.pk_datadef=datadef;
		initialize();
		setbtnstatus(!isadd);
	}

	private void initConnection()
	{
//		
//	   getBtnSave().addActionListener((ActionListener) ivjEventHandler);
//	   getBtnCancel().addActionListener((ActionListener) ivjEventHandler);
	}
	
	public CredenceClientUI getBillUI() {
		if (billUI == null) {
			if (isAuthorizedSettle()) {
				billUI = new CredenceClientUI(/*tableMap*/);
				isadd=billUI.initDataDef(pk,pk_datadef);
			} else {
				billUI = new CredenceClientUI(/*tableMap*/);
				isadd=billUI.initDataDef(pk,pk_datadef);
			}
			billUI.addMessageListener(this);
			
		}
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
//           IBillButton.Edit,
//           IBillButton.Line,
//           IBillButton.Save,
//           IBillButton.Cancel,
//           IBtnDefine.AddEffectFactor,
		    btns=getBillUI().getButtons();
		    uib=new UIButton[btns.length];
	       if(!SJUtil.isNull(btns)){
	    	   int i=0;
	    	   for(ButtonObject btn:btns){
	    		   
	    		   UIButton btnSave = new UIButton();
	    		   if(/*btn.getName().equals("增加")||*/btn.getName().equals("行操作")){
	    			   btnSave=null;
	    			   uib[i]=btnSave;
		    		   i++;
	    			   continue;
	    		   }
	    	       btnSave.setName(btn.getName());
	    	       btnSave.setText(btn.getName());
	    	       btnSave.addActionListener((ActionListener) ivjEventHandler);
	    		   ButtonPane.add(btnSave,null);
	    		   uib[i]=btnSave;
	    		   i++;
	    	   }

	       }
	       
//	       ButtonPane.add(getBtnSave(), null);
//	       ButtonPane.add(getBtnCancel(), null);
//	       ButtonPane.add(getBtnAddEffectFactor(),null);
	      
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
		setSize(1000, 500);
		setLocation(120, 190);
		setTitle(NCLangRes.getInstance().getStrByID("H4H3H3", "凭证模板"));
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

	
	public String getReturnSql(){
		return sqlReturn;
	}
	
	public void messageReceived(MessageEvent e) {
		if (e.getMessageType() == 1) {
			throw new BusinessRuntimeException(e.getMessageContent().toString());
		} else {
			return;
		}
	}

	

	
}
