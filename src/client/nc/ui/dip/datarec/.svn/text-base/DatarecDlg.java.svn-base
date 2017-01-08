package nc.ui.dip.datarec;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import nc.ui.dip.dataverify.DataVerifyClientUI;
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
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.dataverify.DataverifyHVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class DatarecDlg extends UIDialog implements MessageListener{
   class IvjEventHandler implements ActionListener{
	  final DatarecDlg this$0;
	public void actionPerformed(ActionEvent e) {

		 if(e.getSource() == getBtnSave())
         {
			 try {
				onBtnSave();
				
				setBtnEnable(true);
			 } catch(Exception ee){
			    handleException(ee);
			 }
         }
         if(e.getSource() == getBtnCancel()){
       	  	onBtnCancel();
       	  	setBtnEnable(true);
         }
         if(e.getSource() == getBtnEdit())
         {
          	  onBtnEdit();
          	  

         	  	setBtnEnable(false);
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
	}
	
	public IvjEventHandler(){
		this$0=DatarecDlg.this;
	}
	   
   } 
   private void setBtnEnable(boolean isenableEdit){
		getBtnAddLine().setEnabled(!isenableEdit);
		getBtnCancel().setEnabled(!isenableEdit);
		getBtnDelet().setEnabled(isenableEdit);
		getBtnDelLine().setEnabled(!isenableEdit);
		getBtnEdit().setEnabled(isenableEdit);
		getBtnSave().setEnabled(!isenableEdit);
	}
   private ButtonObject []btns;
   private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	DataVerifyClientUI billUI;
    UFBoolean m_isAuthorizedSettle;
	
	private UIPanel ButtonPane;

	private UIPanel MainPanel;

	private UIScrollPane MainScrollPane;

	private UIScrollPane UIScrollPane;
	
	private IvjEventHandler ivjEventHandler;
	
//	private UIButton btnAdd;
//	
//	private ButtonObject btnAddObj;
	
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
	
	public DatarecDlg(Container parent, UFBoolean isAuthorizedSettle,
			HashMap map) {

		super(parent);
		billUI = null;
		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		ivjEventHandler = new IvjEventHandler();
		m_isAuthorizedSettle = isAuthorizedSettle;
		tableMap = map;
		initialize();
		String pk_bus=(String) map.get("pk");
		try {
			DataverifyHVO[] vos=(DataverifyHVO[]) HYPubBO_Client.queryByCondition(DataverifyHVO.class, "pk_datachange_h ='"+pk_bus+"' and nvl(dr,0)=0") ;
			if(vos!=null&&vos.length>0){
				setBtnEnable(true);
				/*try {
					getBillUI().setBillOperate(IBillOperate.OP_EDIT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			}else{
				setBtnEnable(false);

				getBillUI().setBillOperate(IBillOperate.OP_EDIT);
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public DatarecDlg(Container parent, UFBoolean isAuthorizedSettle,
			HashMap map,boolean isenable) {

		super(parent);
		billUI = null;
		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		ivjEventHandler = new IvjEventHandler();
		m_isAuthorizedSettle = isAuthorizedSettle;
		tableMap = map;
		initialize();
		String pk_bus=(String) map.get("pk");
		try {
			DataverifyHVO[] vos=(DataverifyHVO[]) HYPubBO_Client.queryByCondition(DataverifyHVO.class, "pk_datachange_h ='"+pk_bus+"' and nvl(dr,0)=0") ;
			if(vos!=null&&vos.length>0){
				setBtnEnable(true);
				/*try {
					getBillUI().setBillOperate(IBillOperate.OP_EDIT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			}else{
				setBtnEnable(false);

				getBillUI().setBillOperate(IBillOperate.OP_EDIT);
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getBtnEdit().setEnabled(false);
		getBtnDelet().setEnabled(false);
	}
	public void messageReceived(MessageEvent mevent) {
		// TODO Auto-generated method stub
		if(mevent.getMessageType()==1){
			 throw new BusinessRuntimeException(mevent.getMessageContent().toString());
		}
		else{
			return;
		}
	}
	/*private void init(HashMap map){
//		if(pk!=null){
//			try {
//				DataverifyBVO[] vo=(DataverifyBVO[]) HYPubBO_Client.queryByCondition(DataverifyBVO.class, "fpk='"+pk+"' and nvl(dr,0)=0");
//			    if(vo!=null&&vo.length>0){
//			    	
//			    }
//			} catch (UifException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//		2011-5-21
		DipDatachangeHVO hvo=new DipDatachangeHVO();
		String fpk=(String) map.get("fpk");
		
		DataverifyHVO[] vos;
		try {
			vos = (DataverifyHVO[]) HYPubBO_Client.queryByCondition(DataverifyHVO.class, "fpk ='"+fpk+"' and nvl(dr,0)=0");
			if(vos !=null && vos.length>0){
				
				DataverifyBVO[] bvos=(DataverifyBVO[]) HYPubBO_Client.queryByCondition(DataverifyBVO.class, hvo.getPKFieldName()+"='"+hvo.getPrimaryKey()+"'");
				MyBillVO billvo=new MyBillVO();
				billvo.setParentVO(hvo);
				billvo.setChildrenVO(bvos);
//				((MyEventHandler)getCardEventHandler()).lodDefaultVo(new SuperVO[]{(SuperVO) billvo.getParentVO()},type);
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	private DataVerifyClientUI getBillUI(){
		if(billUI==null){
			if(isAuthorizedSettle()){
				billUI=new DataVerifyClientUI(tableMap);
				
			}else{
				billUI=new DataVerifyClientUI(tableMap);
			}
			billUI.addMessageListener(this);
		}
		/*try {
			//使表体可以处于编辑状态
			billUI.setBillOperate(IBillOperate.OP_EDIT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
//           IBillButton.Edit,
//           IBillButton.Line,
//           IBillButton.Save,
//           IBillButton.Cancel,
//           IBtnDefine.AddEffectFactor,
		    btns=getBillUI().getButtons();
//	       if(!SJUtil.isNull(btns)){
//	    	   for(ButtonObject btn:btns){
//	    		   UIButton btnSave = new UIButton();
//	    		   if(/*btn.getName().equals("增加")||*/btn.getName().equals("行操作")){
//	    			   continue;
//	    		   }
//	    	       btnSave.setName(btn.getName());
//	    	       btnSave.setText(btn.getName());
//	    	       btnSave.addActionListener((ActionListener) ivjEventHandler);
//	    		   ButtonPane.add(btnSave,null);
//	    	   }
//
//	       }
//		    、、ButtonPane.add(getBtnAdd(),null);
		    ButtonPane.add(getBtnEdit(),null);
		    ButtonPane.add(getBtnAddLine(),null);
		    ButtonPane.add(getBtnDelLine(),null);
		    ButtonPane.add(getBtnSave(),null);
		    ButtonPane.add(getBtnCancel(),null);
		    ButtonPane.add(getBtnDelet(),null);
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
		
//		getBtnAdd().addActionListener((ActionListener) ivjEventHandler);
		getBtnEdit().addActionListener((ActionListener) ivjEventHandler);
		getBtnAddLine().addActionListener((ActionListener) ivjEventHandler);
		getBtnDelLine().addActionListener((ActionListener) ivjEventHandler);
		getBtnSave().addActionListener((ActionListener) ivjEventHandler);
		getBtnCancel().addActionListener((ActionListener) ivjEventHandler);
		getBtnDelet().addActionListener((ActionListener) ivjEventHandler);
		
		  
	}
	public boolean isAuthorizedSettle() {
		return m_isAuthorizedSettle.booleanValue();
	}
	private void initialize() {
		setContentPane(getMainScrollPane());
		setSize(1000, 500);
		setLocation(120, 190);
		setTitle("数据校验");
		initTable();
		initConnection();
		// setButtonStatus();
	}
	private void handleException(Exception e) {
		String msg = e.getMessage() != null && e.getMessage().length() != 0 ? e
				.getMessage() : e.getClass().getName();
				Debug.error(msg, e);
				MessageDialog.showErrorDlg(this, null, msg);
	}
	
//	private UIButton getBtnAdd()
//	{
//	   if(btnAdd == null)
//	   {
//		   btnAdd = new UIButton();
//		   btnAdd.setName("btnAdd");
//		   btnAdd.setText(getBtnAddObj().getName());
//	   }
//	   return btnAdd;
//	}
//
//	private ButtonObject getBtnAddObj()
//	{
//	   if(btnAddObj == null)
//	   {
//	       ButtonVO btnVO = ButtonVOFactory.getInstance().build(1);
//	       btnAddObj = btnVO.buildButton();
//	   }
//	   return btnAddObj;
//	}
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
	
	private void onBtnSave() throws Exception
	{
		   BillData bd=getBillUI().getBillCardPanel().getBillData();
		   if(bd!=null){
			   bd.dataNotNullValidate();
		   }
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
	      
	   }
	   catch(Exception e)
	   {
	       handleException(e);
	   }
//	   setButtonStatus();
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
//	   setButtonStatus();
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
//	   setButtonStatus();
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
//	   setButtonStatus();
	}
}
