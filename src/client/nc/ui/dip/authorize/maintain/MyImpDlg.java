package nc.ui.dip.authorize.maintain;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.MessageEvent;
import nc.ui.pub.MessageListener;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.button.ButtonVOFactory;
import nc.vo.dip.contwhquery.DEFVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class MyImpDlg extends UIDialog implements MessageListener {
	// /* member class not found */
	class IvjEventHandler implements ActionListener {

		final MyImpDlg this$0;

		public void actionPerformed(ActionEvent e) {
               
//			UIButton bt=(UIButton) e.getSource();
			 if(e.getSource() == getBtnSave()){
				try {
					Object ob1=bcp.getBodyValueAt(0,"npk");
					Object ob2=bcp.getBodyValueAt(1, "npk");
					setRet(new Object[]{ob1,ob2});
					close();
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 }
		
		}

		IvjEventHandler() {
			this$0 = MyImpDlg.this;
			// super();
		}
	}
	private Object[] ret;
	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

    ContDataWHClientUI billUI;

	UFBoolean m_isAuthorizedSettle;
	
	
	private UIPanel ButtonPane;

	private UIPanel MainPanel;

	private UIScrollPane MainScrollPane;

	private UIScrollPane UIScrollPane;
	
	private UIButton btnSave;
	
	private ButtonObject btnSaveObj;
	
	private HashMap<String, String> titlemap;
	private String wpk;
	private String npk;
	private String hpk;
	private boolean isSys;
	private IvjEventHandler ivjEventHandler;
	
	public String sqlReturn = "";
	public MyImpDlg(Container parent, UFBoolean isAuthorizedSettle,
			HashMap<String, String> titlemap) {

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
		m_isAuthorizedSettle = isAuthorizedSettle;
		this.titlemap=titlemap;
		initialize();
	}
	
	private void initConnection()
	{
		
	   getBtnSave().addActionListener((ActionListener) ivjEventHandler);
	}
	
	
	
	@Override
	protected void close() {
		// TODO Auto-generated method stub
		super.close();
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
	       ButtonPane.add(getBtnSave(), null);
	      
	   }
	   return ButtonPane;
	}

	private UIPanel getMainPanel() {
		if (MainPanel == null) {
			MainPanel = new UIPanel();
			MainPanel.setLayout(new BorderLayout());
			MainPanel.add(getButtonPane(),"North");
			MainPanel.add(getBillCardPanel(), "Center");

		}
		return MainPanel;
	}
	private BillCardPanel bcp;
    
	private BillCardPanel getBillCardPanel() {
		String wb="";
		String nc="";
		if (bcp == null) {
			bcp = new BillCardPanel();

			bcp.loadTemplet("H4H1Hg", null, ClientEnvironment.getInstance()
					.getUser().getPrimaryKey(), ClientEnvironment.getInstance()
					.getCorporation().getPk_corp());
			bcp.setSize(800, 200);
			bcp.getBodyTabbedPane().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent mouseevent) {
				}

				@Override
				public void mouseReleased(MouseEvent mouseevent) {
				}

				public void mouseClicked(MouseEvent e) {
					// mouseClick(e);
				}

				public void mousePressed(MouseEvent mouseevent) {

				}
			});
		}
		//显示相应的外部系统字段和NC系统字段 lx 2011-5-20 begin
	//pk,wpk
	DEFVO[] vos = new DEFVO[2];
		vos[0] = new DEFVO();
		vos[0].setPk("对照系统物理字段");
		vos[0].setWpk(wb);
		vos[1] = new DEFVO();
		vos[1].setPk("被对照系统物理");
		vos[1].setWpk(nc);
		bcp.getBillModel().addLine();
		bcp.getBillModel().addLine();
		bcp.getBillModel().setBodyRowVO(vos[0], 0);// .setBodyRowVOs(vos, new
													// int[]{0,1});
		bcp.getBillModel().setBodyRowVO(vos[1], 1);// .setBodyRowVOs(vos, new
													// int[]{0,1});
		bcp.getBodyItem("npk").setEnabled(true);
		bcp.getBillTable().setSortEnabled(false);
		
		//下拉框参照
		UIComboBox ref = (UIComboBox) bcp.getBodyItem("npk").getComponent();
		DefaultComboBoxModel mo = new DefaultComboBoxModel();
		mo.removeAllElements();
		if(titlemap!=null&&titlemap.size()>0){
			Iterator it=titlemap.keySet().iterator();
			while(it.hasNext()){
				String key=(String) it.next();
				mo.addElement(key);
			}
		}
		ref.setModel(mo);
		return bcp;
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
		setTitle(NCLangRes.getInstance().getStrByID("400122", "导入条件"));
//		initTable();
		initConnection();
		//getBillUI().getBillCardPanel().getBodyItem("effectname").setEdit(true);
		// setButtonStatus();
	}

	public boolean isAuthorizedSettle() {
		return m_isAuthorizedSettle.booleanValue();
	}



	public void setIsAuthorizedSettle(UFBoolean authorizedSettle) {
		m_isAuthorizedSettle = authorizedSettle;
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

	public Object[] getRet() {
		return ret;
	}

	public void setRet(Object[] ret) {
		this.ret = ret;
	}
	

	

	
}
