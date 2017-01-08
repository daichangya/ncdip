package nc.ui.dip.authorize.browse;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.dip.actionset.ActionSetBVO;
import nc.vo.pub.BusinessException;

@SuppressWarnings("deprecation")
public class ActionSetDailog extends UIDialog implements java.awt.event.ActionListener{
	
	private static final long serialVersionUID = 1L;

	private UIButton btnConfirm;
	
	private UIButton btnCancel;
	
	private JPanel ivjUIDialogContentPane;
	
	private UIPanel buttonPanel;
	
	private BillListPanel listPanel;
	
	private String pk_contdata_h;
	
	private DataLookClientUI ui;
	
	private ActionSetBVO rowVO;
	
	public ActionSetBVO getRowVO() {
		return rowVO;
	}
	public void setRowVO(ActionSetBVO rowVO) {
		this.rowVO = rowVO;
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(getBtnConfirm())) {
			int selectedRow = getListPanel().getBodyTable().getSelectedRow();
			if(selectedRow<0){
				ui.showWarningMessage("请选择需要执行的动作！");
				return;
			}
			ActionSetBVO rowVO = (ActionSetBVO)getListPanel().getBodyBillModel().getBodyValueRowVO(selectedRow, ActionSetBVO.class.getName());
			setRowVO(rowVO);
			closeOK();
			destroy();
		} else if (e.getSource().equals(getBtnCancel())) {
			closeCancel();
			destroy();
		}
	}
	public ActionSetDailog(DataLookClientUI handler,String pk_contdata_h) {
		super(handler);
		this.ui = handler;
		this.pk_contdata_h = pk_contdata_h;
		initialize();
		try {
			initData();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initData() throws BusinessException {
		String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		String strWhere = "";
		if(!"0001".equals(pk_corp)){
			strWhere = "pk_actionset_h in(select pk_actionset_h from dip_actionset_h where pk_contdata_h='"
				+pk_contdata_h
				+"' and pk_role_group in(SELECT pk_role_group FROM dip_rolegroup_b WHERE pk_role in("
				+"SELECT pk_role FROM sm_user_role WHERE cuserid='"+
				userid+
				"') and coalesce(dr,0)=0 ) and coalesce(dr,0)=0) order by code";
		}else{
			strWhere = "pk_actionset_h in(select pk_actionset_h from dip_actionset_h where pk_contdata_h='"
				+pk_contdata_h
				+"') and coalesce(dr,0)=0 order by code";
		}
		ActionSetBVO[] bvos = (ActionSetBVO[])HYPubBO_Client.queryByCondition(ActionSetBVO.class, strWhere);
		getListPanel().setBodyValueVO(bvos);
	}
	private void initialize() {
		try {
			setName("ActionSetDialog");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("扩展动作");
			setSize(500, 400);
			setLocation(400, 190);
			setContentPane(getContentPanel());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		getBtnConfirm().addActionListener(this);
		getBtnCancel().addActionListener(this);
	}
	private javax.swing.JPanel getContentPanel() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getContentPanel().add(getListPanel(), BorderLayout.CENTER);
				getContentPanel().add(getButtonPanel(), BorderLayout.SOUTH);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}
	public BillListPanel getListPanel() {
		if (listPanel == null) {
			try {
				listPanel = new BillListPanel();
				listPanel.setName("LIST");
				loadBillListTemplate();
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}

		return listPanel;
	}
	private void loadBillListTemplate() {
		getListPanel().loadTemplet("0001AA10000000002030");
	}

	private nc.ui.pub.beans.UIPanel getButtonPanel() {
		if (buttonPanel == null) {
			try {
				buttonPanel = new nc.ui.pub.beans.UIPanel();
				buttonPanel.setName("buttonPanel");
				buttonPanel.setPreferredSize(new java.awt.Dimension(400, 50));
				buttonPanel.setLayout(new FlowLayout());
				buttonPanel.add(getBtnConfirm(), getBtnConfirm().getName());
				buttonPanel.add(getBtnCancel(), getBtnCancel().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return buttonPanel;
	}

	private nc.ui.pub.beans.UIButton getBtnCancel() {
		if (btnCancel == null) {
			try {
				btnCancel = new nc.ui.pub.beans.UIButton();
				btnCancel.setName("BnCancel");
				btnCancel
				.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030201", "UC001-0000008")/*
				 * @res
				 * "取消"
				 */);
				btnCancel.setBounds(435, 15, 70, 22);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return btnCancel;
	}
	private nc.ui.pub.beans.UIButton getBtnConfirm() {
		if (btnConfirm == null) {
			try {
				btnConfirm = new nc.ui.pub.beans.UIButton();
				btnConfirm.setName("BnConfirm");
				btnConfirm.setText("执行");
				btnConfirm.setBounds(355, 15, 70, 22);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return btnConfirm;
	}
	private void handleException(java.lang.Throwable e) {
		Logger.error(e.getMessage(), e);
	}
	

}
