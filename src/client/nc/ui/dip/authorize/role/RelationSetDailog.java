package nc.ui.dip.authorize.role;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IQueryField;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.actionset.ActionSetHVO;
import nc.vo.dip.authorize.define.DipADContdataBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

@SuppressWarnings("deprecation")
public class RelationSetDailog extends UIDialog implements java.awt.event.ActionListener{
	
	private static final long serialVersionUID = 1L;

	private UIButton btnConfirm;
	
	private UIButton btnCancel;
	
	private JPanel ivjUIDialogContentPane;
	
	private UIPanel buttonPanel;
	
	private BillListPanel listPanel;
	
	private String pk_role_group;
	
	private IQueryField iquery = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(getBtnConfirm())) {
			BillModel billModel = getListPanel().getBodyBillModel();
			CircularlyAccessibleValueObject[] bodyValueVOs = billModel.getBodyValueVOs(ActionSetHVO.class.getName());
			ArrayList<DipADContdataBVO> contdataBlist = new ArrayList<DipADContdataBVO>();
			ArrayList<ActionSetHVO> insetSetHlist = new ArrayList<ActionSetHVO>();
			ArrayList<ActionSetHVO> updateSetHlist = new ArrayList<ActionSetHVO>();
			ArrayList<String> delContdataBlist = new ArrayList<String>();
			ArrayList<String> delSetHlist = new ArrayList<String>();
			try {
				for (CircularlyAccessibleValueObject vo : bodyValueVOs) {
					ActionSetHVO bvo = ((ActionSetHVO)vo);
					String pk_role_group2 = bvo.getPk_role_group();
					if(null == pk_role_group2 || "".equals(pk_role_group2)){
						if(null != bvo.getSelected() && bvo.getSelected().booleanValue()){//新增
							DipADContdataBVO contdataBVO = new DipADContdataBVO();
							contdataBVO.setPk_contdata_h(bvo.getPk_contdata_h());
							contdataBVO.setPk_fp(pk_role_group);
							contdataBVO.setIs_master(bvo.getIs_cancal());
							contdataBlist.add(contdataBVO);
							bvo.setPk_role_group(pk_role_group);
							insetSetHlist.add(bvo);
						}
					}else{
						if(null != bvo.getSelected() && bvo.getSelected().booleanValue()){//修改
							DipADContdataBVO[] contdataBVOs = (DipADContdataBVO[])HYPubBO_Client.queryByCondition(DipADContdataBVO.class, " pk_fp='"
									+pk_role_group
									+"' and pk_contdata_h='"
									+bvo.getPk_contdata_h()
									+"'");
							if(null != contdataBVOs && contdataBVOs.length>0){
								for (DipADContdataBVO dipADContdataBVO : contdataBVOs) {
									dipADContdataBVO.setIs_master(bvo.getIs_cancal());
								}
								HYPubBO_Client.updateAry(contdataBVOs);
							}
							if(null != bvo.getIs_cancal() && bvo.getIs_cancal().booleanValue()){
								if(null != bvo.getPk_actionset_h() && !"".equals(bvo.getPk_actionset_h())){
									ActionSetHVO hvo = (ActionSetHVO)HYPubBO_Client.queryByPrimaryKey(ActionSetHVO.class, bvo.getPk_actionset_h());
									hvo.setIs_add(bvo.getIs_add());
									hvo.setIs_addline(bvo.getIs_addline());
									hvo.setIs_del(bvo.getIs_del());
									hvo.setIs_delline(bvo.getIs_delline());
									hvo.setIs_edit(bvo.getIs_edit());
									hvo.setIs_export(bvo.getIs_export());
									hvo.setIs_import(bvo.getIs_import());
									hvo.setIs_query(bvo.getIs_query());
									hvo.setIs_save(bvo.getIs_save());
									updateSetHlist.add(hvo);
								}else{
									bvo.setPk_role_group(pk_role_group);
									insetSetHlist.add(bvo);
								}
							}else{
								if(null != bvo.getPk_actionset_h() && !"".equals(bvo.getPk_actionset_h())){
									HYPubBO_Client.deleteByWhereClause(ActionSetHVO.class, "pk_actionset_h='"+bvo.getPk_actionset_h()+"'");
								}
							}
						}else{//删除
							DipADContdataBVO[] contdataBVOs = (DipADContdataBVO[])HYPubBO_Client.queryByCondition(DipADContdataBVO.class, " pk_fp='"
									+pk_role_group
									+"' and pk_contdata_h='"
									+bvo.getPk_contdata_h()
									+"'");
							for (int i = 0; i < contdataBVOs.length; i++) {
								delContdataBlist.add(contdataBVOs[i].getPrimaryKey());
							}
							if(null != bvo.getPk_actionset_h() && !"".equals(bvo.getPk_actionset_h())){
								delSetHlist.add(bvo.getPk_actionset_h());
							}
						}
					}
				}
				if(contdataBlist.size()>0){
					HYPubBO_Client.insertAry(contdataBlist.toArray(new DipADContdataBVO[contdataBlist.size()]));
				}
				if(insetSetHlist.size()>0){
					HYPubBO_Client.insertAry(insetSetHlist.toArray(new ActionSetHVO[insetSetHlist.size()]));
				}
				if(updateSetHlist.size()>0){
					HYPubBO_Client.updateAry(updateSetHlist.toArray(new ActionSetHVO[updateSetHlist.size()]));
				}
				if(delContdataBlist.size()>0){
					String pks = "";
					for (String pk : delContdataBlist) {
						pks += "'"+pk+"',";
					}
					pks = pks.substring(0, pks.length()-1);
					HYPubBO_Client.deleteByWhereClause(DipADContdataBVO.class," pk_contdata_b in("+pks+")");
				}
				if(delSetHlist.size()>0){
					String pks = "";
					for (String pk : delSetHlist) {
						pks += "'"+pk+"',";
					}
					pks = pks.substring(0, pks.length()-1);
					HYPubBO_Client.deleteByWhereClause(ActionSetHVO.class," pk_actionset_h in("+pks+")");
				}
			} catch (UifException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			closeOK();
			destroy();
		} else if (e.getSource().equals(getBtnCancel())) {
			closeCancel();
			destroy();
		}
	}
	public RelationSetDailog(RoleGroupUI handler,String pk_role_group) {
		super(handler);
		this.pk_role_group = pk_role_group;
		initialize();
		try {
			initData();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initData() throws BusinessException {
		String sql = "SELECT a.code,a.name,c.is_master as is_cancal,is_add,is_del,is_edit,is_query,is_import,is_export,is_addline,is_delline,is_save," 
			+ "a.pk_contdata_h,pk_fp as pk_role_group,b.pk_actionset_h FROM dip_adcontdata a " 
			+ "left join dip_adcontdata_b c on a.pk_contdata_h=c.pk_contdata_h and c.pk_fp='"+pk_role_group+"' and coalesce(c.dr,0)=0 " 
			+ "left join dip_actionset_h b on a.pk_contdata_h=b.pk_contdata_h "
			+ "and b.pk_role_group='"+pk_role_group+"' and coalesce(b.dr,0)=0 "
			+ "WHERE a.ismaster='Y' and coalesce(a.dr,0)=0 and a.isfolder='N' order by a.code";
		List<SuperVO> list = iquery.queryVOBySql(sql, new ActionSetHVO());
		if(null != list && list.size()>0){
			ActionSetHVO[] bvos = list.toArray(new ActionSetHVO[list.size()]);
			for (ActionSetHVO vo : bvos) {
				String pk_role_group2 = vo.getPk_role_group();
				if(null != pk_role_group2 && !"".equals(pk_role_group2)){
					vo.setSelected(new UFBoolean(true));
				}
			}
			getListPanel().setBodyValueVO(bvos);
			BillModel billModel = getListPanel().getBodyBillModel();
			int rowCount = billModel.getRowCount();
			for (int i = 0; i < rowCount; i++) {
				Object valueAt = billModel.getValueAt(i, "selected");
				if(null != valueAt && new UFBoolean(valueAt.toString()).booleanValue()){
					billModel.setCellEditable(i, "is_cancal", true);
				}else{
					billModel.setCellEditable(i, "is_cancal", false);
				}
				Object valueAt1 = billModel.getValueAt(i, "is_cancal");
				if(null != valueAt1 && new UFBoolean(valueAt1.toString()).booleanValue()){
					billModel.setCellEditable(i, "is_add", true);
					billModel.setCellEditable(i, "is_addline", true);
					billModel.setCellEditable(i, "is_del", true);
					billModel.setCellEditable(i, "is_delline", true);
					billModel.setCellEditable(i, "is_edit", true);
					billModel.setCellEditable(i, "is_export", true);
					billModel.setCellEditable(i, "is_import", true);
					billModel.setCellEditable(i, "is_query", true);
					billModel.setCellEditable(i, "is_save", true);
				}else{
					billModel.setCellEditable(i, "is_add", false);
					billModel.setCellEditable(i, "is_addline", false);
					billModel.setCellEditable(i, "is_del", false);
					billModel.setCellEditable(i, "is_delline", false);
					billModel.setCellEditable(i, "is_edit", false);
					billModel.setCellEditable(i, "is_export", false);
					billModel.setCellEditable(i, "is_import", false);
					billModel.setCellEditable(i, "is_query", false);
					billModel.setCellEditable(i, "is_save", false);
				}
				
			}
		}
	}
	private void initialize() {
		try {
			setName("RelationSetDailog");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("关联定义");
			setSize(1000, 400);
			setLocation(200, 190);
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
				listPanel.setEnabled(true);
				listPanel.addBodyEditListener(new BillEditListener() {
					
					public void bodyRowChange(BillEditEvent e) {
						// TODO Auto-generated method stub
						
					}
					public void afterEdit(BillEditEvent e) {
						if(e.getKey().equals("selected")){
							BillModel billModel = listPanel.getBodyBillModel();
							int i = e.getRow();
							Object valueAt = billModel.getValueAt(i, "selected");
							if(null != valueAt && new UFBoolean(valueAt.toString()).booleanValue()){
								billModel.setCellEditable(i, "is_cancal", true);
							}else{
								billModel.setCellEditable(i, "is_cancal", false);
								billModel.setValueAt(null, i, "is_cancal");
								billModel.setCellEditable(i, "is_add", false);
								billModel.setCellEditable(i, "is_addline", false);
								billModel.setCellEditable(i, "is_del", false);
								billModel.setCellEditable(i, "is_delline", false);
								billModel.setCellEditable(i, "is_edit", false);
								billModel.setCellEditable(i, "is_export", false);
								billModel.setCellEditable(i, "is_import", false);
								billModel.setCellEditable(i, "is_query", false);
								billModel.setCellEditable(i, "is_save", false);
								billModel.setValueAt(null, i, "is_add");
								billModel.setValueAt(null, i, "is_addline");
								billModel.setValueAt(null, i, "is_del");
								billModel.setValueAt(null, i, "is_delline");
								billModel.setValueAt(null, i, "is_edit");
								billModel.setValueAt(null, i, "is_export");
								billModel.setValueAt(null, i, "is_import");
								billModel.setValueAt(null, i, "is_query");
								billModel.setValueAt(null, i, "is_save");
							}
						}else if(e.getKey().equals("is_cancal")){
							BillModel billModel = listPanel.getBodyBillModel();
							int i = e.getRow();
							Object valueAt = billModel.getValueAt(i, "is_cancal");
							if(null != valueAt && new UFBoolean(valueAt.toString()).booleanValue()){
								billModel.setCellEditable(i, "is_add", true);
								billModel.setCellEditable(i, "is_addline", true);
								billModel.setCellEditable(i, "is_del", true);
								billModel.setCellEditable(i, "is_delline", true);
								billModel.setCellEditable(i, "is_edit", true);
								billModel.setCellEditable(i, "is_export", true);
								billModel.setCellEditable(i, "is_import", true);
								billModel.setCellEditable(i, "is_query", true);
								billModel.setCellEditable(i, "is_save", true);
								
								billModel.setValueAt("Y", i, "is_add");
								billModel.setValueAt("Y", i, "is_addline");
								billModel.setValueAt("Y", i, "is_del");
								billModel.setValueAt("Y", i, "is_delline");
								billModel.setValueAt("Y", i, "is_edit");
								billModel.setValueAt("Y", i, "is_export");
								billModel.setValueAt("Y", i, "is_import");
								billModel.setValueAt("Y", i, "is_query");
								billModel.setValueAt("Y", i, "is_save");
							}else{
								billModel.setCellEditable(i, "is_add", false);
								billModel.setCellEditable(i, "is_addline", false);
								billModel.setCellEditable(i, "is_del", false);
								billModel.setCellEditable(i, "is_delline", false);
								billModel.setCellEditable(i, "is_edit", false);
								billModel.setCellEditable(i, "is_export", false);
								billModel.setCellEditable(i, "is_import", false);
								billModel.setCellEditable(i, "is_query", false);
								billModel.setCellEditable(i, "is_save", false);
								
								billModel.setValueAt(null, i, "is_add");
								billModel.setValueAt(null, i, "is_addline");
								billModel.setValueAt(null, i, "is_del");
								billModel.setValueAt(null, i, "is_delline");
								billModel.setValueAt(null, i, "is_edit");
								billModel.setValueAt(null, i, "is_export");
								billModel.setValueAt(null, i, "is_import");
								billModel.setValueAt(null, i, "is_query");
								billModel.setValueAt(null, i, "is_save");
							}
						}
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}

		return listPanel;
	}
	private void loadBillListTemplate() {
		getListPanel().loadTemplet("0001AA10000000002EFB");
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
				btnConfirm.setText("保存");
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
