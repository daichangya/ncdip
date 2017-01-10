package nc.ui.dip.datalookquery;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.util.ClientEnvDef;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.MessageEvent;
import nc.ui.pub.MessageListener;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.contwhquery.ContwhqueryVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.util.ClientEvnUtilVO;
import nc.vo.dip.util.QueryUtilVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class DatalookQueryDlg extends UIDialog implements MessageListener {
	// /* member class not found */
	class IvjEventHandler implements ActionListener {

		final DatalookQueryDlg this$0;

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
	          }else if(e.getSource()==getBtnLoadTemplet()){
	        	  onBtnLoadTemplet();
	          }else if(e.getSource()==getBtnSaveTemplet()){
	        	  onBtnSaveTemplet();
	          }else if(e.getSource()==getBtnClearTemplet()){
	        	  onBtnClearTemplet();
	          }
	          
		}

		IvjEventHandler() {
			this$0 = DatalookQueryDlg.this;
			// super();
		}
	}

	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	DatalookQueryClientUI billUI;

	UFBoolean m_isAuthorizedSettle;
	
	private UIPanel ButtonPane;

	private UIPanel MainPanel;

//	private UIScrollPane MainScrollPane;
//
//	private UIScrollPane UIScrollPane;
	
	private UIButton btnSave;
	private UIButton btnLinAdd;
	private UIButton btnLinDel;
	
	private ButtonObject btnSaveObj;
	private ButtonObject btnLineAddObj;
	private ButtonObject btnLineDelObj;
	
	private UIButton btnCancel;
	private UIButton btnLoadTemplet;
	private UIButton btnSaveTemplet;
	private UIButton btnClearTemplet;
	
	private ButtonObject btnCancelObj;

	private String key="";
	
	private HashMap<String, DipDatadefinitBVO> dataBMap =  new HashMap<String, DipDatadefinitBVO>();

	private IvjEventHandler ivjEventHandler;
	
	public int onBtnSave=0;//0表示
	
	private IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	
	public int getOnBtnSave(){
		return onBtnSave;
	}
	
	public String sqlReturn = "";
	public DatalookQueryDlg(Container parent, UFBoolean isAuthorizedSettle,
			String key) {

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
		this.key = key;
		initialize();
	}
	
	public DatalookQueryDlg(Container parent, UFBoolean isAuthorizedSettle,
			String key,HashMap<String, DipDatadefinitBVO> dataBMap) {
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
		this.key = key;
		this.dataBMap = dataBMap;
		initialize();
	}

	private void initConnection()
	{
		
	   getBtnSave().addActionListener((ActionListener) ivjEventHandler);
	   getBtnCancel().addActionListener((ActionListener) ivjEventHandler);
	   getBtnLineAdd().addActionListener((ActionListener) ivjEventHandler);
	   getBtnLineDel().addActionListener((ActionListener) ivjEventHandler);
	   getBtnLoadTemplet().addActionListener((ActionListener) ivjEventHandler);
	   getBtnSaveTemplet().addActionListener((ActionListener) ivjEventHandler);
	   getBtnClearTemplet().addActionListener((ActionListener) ivjEventHandler);
	}
	
	public DatalookQueryClientUI getBillUI() {
		if (billUI == null) {
			if (isAuthorizedSettle()) {
				billUI = new DatalookQueryClientUI(key,isAuthorizedSettle(),dataBMap);
			} else {
				billUI = new DatalookQueryClientUI(key);
			}
			billUI.addMessageListener(this);
		}
		return billUI;
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
	private ButtonObject getBtnSaveObj()
	{
	   if(btnSaveObj == null)
	   {
	       ButtonVO btnVO = ButtonVOFactory.getInstance().build(0);
	       btnSaveObj = btnVO.buildButton();
	       btnSaveObj.setName("确定");
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
	
	private UIButton getBtnLoadTemplet()
	{
	   if(btnLoadTemplet == null)
	   {
		   btnLoadTemplet = new UIButton();
		   btnLoadTemplet.setName("btnLoadTemplet");
		   btnLoadTemplet.setText("加载模板");
		   btnLoadTemplet.setName("加载模板");
	   }
	   return btnLoadTemplet;
	}
	
	private UIButton getBtnSaveTemplet()
	{
	   if(btnSaveTemplet == null)
	   {
		   btnSaveTemplet = new UIButton();
		   btnSaveTemplet.setName("btnSaveTemplet");
		   btnSaveTemplet.setText("保存模板");
		   btnSaveTemplet.setName("保存模板");
	   }
	   return btnSaveTemplet;
	}
	
	private UIButton getBtnClearTemplet()
	{
	   if(btnClearTemplet == null)
	   {
		   btnClearTemplet = new UIButton();
		   btnClearTemplet.setName("btnClearTemplet");
		   btnClearTemplet.setText("重置模板");
		   btnClearTemplet.setName("重置模板");
	   }
	   return btnClearTemplet;
	}
	private UIPanel getButtonPane()
	{
	   if(ButtonPane == null)
	   {
	       FlowLayout flowLayout1 = new FlowLayout();
	       ButtonPane = new UIPanel();
	       ButtonPane.setLayout(flowLayout1);
	       ButtonPane.setName("ButtonPane");
//	       ButtonPane.setPreferredSize(new Dimension(20, 30));
//	       flowLayout1.setAlignment(100);
	       ButtonPane.add(getBtnLineAdd(), null);
	       ButtonPane.add(getBtnLineDel(), null);
	       ButtonPane.add(getBtnSave(), null);
	       ButtonPane.add(getBtnCancel(),null);
	       ButtonPane.add(getBtnLoadTemplet(),null);
	       ButtonPane.add(getBtnSaveTemplet(),null);
	       ButtonPane.add(getBtnClearTemplet(),null);
//	       ButtonPane.add(getBtnCancel(), null);
	      
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

//	private UIScrollPane getMainScrollPane() {
//		if (MainScrollPane == null) {
//			MainScrollPane = new UIScrollPane();
//			MainScrollPane.setName("MainScrollPane");
//			MainScrollPane.setViewportView(getMainPanel());
//
//		}
//		return MainScrollPane;
//	}

//	private UIScrollPane getUIScrollPane() {
//		if (UIScrollPane == null) {
//			UIScrollPane = new UIScrollPane();
//			UIScrollPane.setName("TablePane");
//		}
//		return UIScrollPane;
//	}

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

//	private void initTable() {
//		getUIScrollPane().setViewportView(getBillUI());
//	}

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
	private void onBtnSave()
	{
	   try
	   {
		   onBtnSave=1;
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
	private void onBtnLoadTemplet()
	{
		String primaryKey = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		try {
			ContwhqueryVO[] vos = (ContwhqueryVO[])HYPubBO_Client.queryByCondition(ContwhqueryVO.class, 
					"userid='"
					+primaryKey
					+"' and Pk_datadefinit_h='"
					+key
					+"'");
			if(null != vos && vos.length>0){
				ArrayList<String> list = new ArrayList<String>();
				ClientEvnUtilVO utilVO = ClientEnvDef.queryAuthMap.get(key);
				if(null != utilVO && utilVO.getVos().length>0){
					for (QueryUtilVO vo : utilVO.getVos()) {
						list.add(vo.getEname());
					}
				}
				for (ContwhqueryVO contwhqueryVO : vos) {
					if(!list.contains(contwhqueryVO.getEname())){
						MessageDialog.showErrorDlg(this, "错误", "查询模板不匹配,请重置模板！");
						return;
					}
				}
				BillModel billModel = getBillUI().getBillCardPanel().getBillModel();
				int rowCount = billModel.getRowCount();
				int[] delRows = new int[rowCount];
				for (int i = 0; i < rowCount; i++) {
					delRows[i] = i;
				}
				billModel.delLine(delRows);
				billModel.setBodyDataVO(vos);
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void onBtnSaveTemplet()
	{
		BillModel billModel = getBillUI().getBillCardPanel().getBillModel();
		ContwhqueryVO[] valueVOs = (ContwhqueryVO[])billModel.getBodyValueVOs(ContwhqueryVO.class.getName());
		if(null != valueVOs && valueVOs.length>0){
			String primaryKey = ClientEnvironment.getInstance().getUser().getPrimaryKey();
			try {
				for (ContwhqueryVO contwhqueryVO : valueVOs) {
					contwhqueryVO.setUserid(primaryKey);
					contwhqueryVO.setPk_datadefinit_h(key);
				}
				HYPubBO_Client.deleteByWhereClause(ContwhqueryVO.class, "userid='"
						+primaryKey
						+"' and Pk_datadefinit_h='"
						+key
						+"'");
				iq.insertVOs(valueVOs);
				MessageDialog.showHintDlg(this, "提示", "模板保存成功！");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void onBtnClearTemplet()
	{
		BillModel billModel = getBillUI().getBillCardPanel().getBillModel();
		int rowCount = billModel.getRowCount();
		int[] delRows = new int[rowCount];
		for (int i = 0; i < rowCount; i++) {
			delRows[i] = i;
		}
		billModel.delLine(delRows);
		try {
			getBillUI().setDefaultData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		onBtnSave=0;
		this.close();
	 /*  try
	   {
	       getBillUI().onButtonAction(getBtnCancelObj());
	      
	   }
	   catch(Exception e)
	   {
	       handleException(e);
	   }*/
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
