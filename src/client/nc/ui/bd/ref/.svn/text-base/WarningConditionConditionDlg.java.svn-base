package nc.ui.bd.ref;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.pub.lang.UFDate;

@SuppressWarnings("serial")
public class WarningConditionConditionDlg extends UIDialog {
	
	
	private JLabel code = null;// 编码

	private JTextField refWcode = null;
	
	private JLabel name = null;// 名称

	private JTextField refName = null;
	
	private JLabel isnotWarning = null;// 是否启用预警
	
	private JComboBox refIsnotWarning = null; 
	
	private JLabel  condition = null;// 预警任务条件

	private UIRefPane refCondition = null;
	
	
	private JLabel isnotTiming = null;// 是否定时
	
	private JComboBox refIsnotTiming = null; 
	
	private JLabel  property = null;// 预警任务属性

	private JTextField refProperty = null;
	
	
	
	
	
	
	
	
	
	
	

	private JPanel jPanel = null;

	private UIButton btnOk = null;

	private UIButton btnCancle = null;

	private UICheckBox cproname = null;// 未记账凭证

	private JLabel lbcproname = null;

	private JLabel lbMainAcc = null;// 调拨类型

	private UIRefPane refMainAcc = null;// 主体账簿参照

//	private JLabel lbBeginDate = null;// 起始日期
//
//	private UIRefPane refBeginDate = null;

	private JLabel lbEndDate = null;// 结束日期

	private UIRefPane refEndDate = null;

	private JLabel lbCode = null;// 采购订单

	private JTextField refCode = null;// 采购订单号

	private JTextField refBillCode = null;// 采购订单参照

	private JLabel lbSubject = null;// 科目

	private UIRefPane refSubject = null;

	private JLabel lbBankAcc = null;// 银行账户

	private UIRefPane refBankAcc = null;

	private JLabel lbComboBox = null;// 本币

	private JComboBox jComboBox = null;

	private JComboBox jComboBoxBillType = null;// 调拨类型

	public HashMap whereSql = new HashMap();

	public HashMap bufferCondition = new HashMap();// 如果报表不关闭存放上一次的查询条件

	private boolean isCancel = false;

	public nc.ui.bd.ref.AbstractRefModel absRefModel = null;

	private HashMap mapClientUIData = new HashMap();

	private HashMap mapCondition = new HashMap();

	WarningStatusRefModel u = new WarningStatusRefModel();

	/**
	 * This method initializes
	 * 
	 */
	@SuppressWarnings("deprecation")
	public WarningConditionConditionDlg() {
		super();
		initialize();
	}

	@SuppressWarnings("deprecation")
	public WarningConditionConditionDlg(HashMap bufferCondition) {
		super();
		this.setBufferCondition(bufferCondition);
		initialize();
	}

	/*
	 * 传缓存；值；
	 */
	@SuppressWarnings("deprecation")
	public WarningConditionConditionDlg(HashMap bufferCondition, HashMap clientUIData) {
		super();
		this.setBufferCondition(bufferCondition);
		mapClientUIData = clientUIData;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new Dimension(490, 200));
		this.setContentPane(getJPanel());
		this.setTitle("预警");
		setResizable(false);
		//initGroupReft();
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getJPanel() {
		if (jPanel == null) {
			// lbMainAcc = new JLabel();
			// lbMainAcc.setBounds(new Rectangle(39, 35, 90, 22));
			// lbMainAcc.setText("调拨类型");
			// lbMainAcc.setVisible(true);

			code = new JLabel();
			code.setBounds(new Rectangle(20, 20, 90, 22));
			code.setText("编码");
			code.setVisible(true);
			
			name = new JLabel();
			name.setBounds(new Rectangle(210, 20, 130, 22));
			name.setText("名称");
			name.setVisible(true);
			
			isnotWarning = new JLabel();
			isnotWarning.setBounds(new Rectangle(20, 60, 90, 22));
			isnotWarning.setText("是否启用预警");
			isnotWarning.setVisible(true);
			
			condition = new JLabel();
			condition.setBounds(new Rectangle(210, 60, 130, 22));
			condition.setText("预警任务条件");
			condition.setVisible(true);

			isnotTiming = new JLabel();
			isnotTiming.setBounds(new Rectangle(20, 100, 90, 22));
			isnotTiming.setText("是否定时");
			isnotTiming.setVisible(true);
			
			property = new JLabel();
			property.setBounds(new Rectangle(210, 100, 130, 22));
			property.setText("预警任务属性");
			property.setVisible(true);
			
			
			
			
			
			
			
			
			
			
			
			
			
			
//			lbBeginDate = new JLabel();
//			lbBeginDate.setBounds(new Rectangle(39, 60, 90, 22));
//			lbBeginDate.setText("起始日期");
//			lbBeginDate.setVisible(true);

//			lbEndDate = new JLabel();
//			lbEndDate.setBounds(new Rectangle(39, 85, 90, 22));
//			lbEndDate.setText("结束日期");
//			lbEndDate.setVisible(true);
//
//			lbCode = new JLabel();
//			lbCode.setBounds(new Rectangle(39, 110, 90, 22));
//			lbCode.setText("采购订单号");
//			lbCode.setVisible(true);
//
//			lbSubject = new JLabel();
//			lbSubject.setBounds(new Rectangle(39, 135, 90, 22));
//			lbSubject.setText("订单参照");
//			lbSubject.setVisible(true);

//			 lbBankAcc = new JLabel();
//			 lbBankAcc.setBounds(new Rectangle(39, 135, 90, 22));
//			 lbBankAcc.setText("银行账户");
//			 lbBankAcc.setVisible(true);
			//        	
			// lbComboBox = new JLabel();
			// lbComboBox.setBounds(new Rectangle(39, 160, 90, 22));
			// lbComboBox.setText("本币");
			// lbComboBox.setVisible(true);
			//        	
			// lbcproname = new JLabel();
			// lbcproname.setBounds(new Rectangle(39, 185, 90, 22));
			// lbcproname.setText("是否未记帐凭证");
			// lbcproname.setVisible(true);

			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getBtnOk(), null);
			jPanel.add(getBtnCancle(), null);

			// jPanel.add(lbMainAcc,null);
			// jPanel.add(getJComboBoxBillType(),null);//调拨类型
			
			
			jPanel.add(code, null); //编号
			jPanel.add(getRefWcode(), null);
			
			jPanel.add(name, null);//名称
			jPanel.add(getRefName(), null);
			
			jPanel.add(isnotWarning,null);
			jPanel.add(getIsnotWarning(),null);//是否启用预警
			
			jPanel.add(condition, null); //预警任务条件
			jPanel.add(getRefCondition(), null);
			
			jPanel.add(isnotTiming,null);
			jPanel.add(getIsnotTiming(),null);//是否定时
			
			jPanel.add(property, null); //预警任务属性
			jPanel.add(getRefProperty(), null);
			
			
			
			
			
			
			
			

//			jPanel.add(lbBeginDate, null);
//			jPanel.add(getRefBeginDate(), null);

//			jPanel.add(lbEndDate, null);
//			jPanel.add(getRefEndDate(), null);
//
//			jPanel.add(lbCode, null);
//			jPanel.add(getRefCode(), null);
//
//			jPanel.add(lbSubject, null);
//			// jPanel.add(getRefSubject(), null);
//			jPanel.add(getRefBillCode(), null);

//			 jPanel.add(lbBankAcc,null);
//			 jPanel.add(getRefBankAcc(),null);
			//            
			// jPanel.add(lbComboBox,null);
			// jPanel.add(getJComboBoxType(),null);
			//            
			// jPanel.add(lbcproname, null);//未记账凭证
			// jPanel.add(getProjectName(), null);//未记账凭证

		}
		return jPanel;
	}

	/**
	 * 主体账簿
	 * 
	 * @return
	 */
	private UIRefPane getRefMainAcc() {
		if (refMainAcc == null) {
			refMainAcc = new UIRefPane();
			// new 一个主体账簿参照类
			// refMainAcc.setRefModel(arg0);
			refMainAcc.setRefNodeName("账簿主体");// 系统参照
			refMainAcc.setBounds(new Rectangle(170, 35, 100, 22));
			refMainAcc.setVisible(true);
			// if(this.getBufferCondition().get("pk_mainacc") != null){
			// refMainAcc.setPK(this.getBufferCondition().get("pk_mainacc").toString());
			// }
		}
		return refMainAcc;
	}

	/**
	 * 编码
	 * 
	 * @return
	 */
	 
	private JTextField getRefWcode() {
		if (refCode == null) {
			refCode = new JTextField();
			refCode.setBounds(new Rectangle(100, 20, 100, 22));
			refCode.setVisible(true);
		}
		return refCode;
	}
	
	/**
	 * 名称
	 * 
	 * @return
	 */
	private JTextField getRefName() {
		if (refName == null) { 
			refName = new JTextField();
			refName.setBounds(new Rectangle(300, 20, 100, 22));
			refName.setVisible(true);
			 
		}
		return refName;
	}
	
	/**
	 * 是否预警
	 */
	private JComboBox getIsnotWarning() {
		if (refIsnotWarning == null) {
			refIsnotWarning = new JComboBox();
			refIsnotWarning.setBounds(new Rectangle(100, 60, 100, 22));
			refIsnotWarning.addItem("是");
			refIsnotWarning.addItem("否");
			refIsnotWarning.setSelectedIndex(0); // 设定默认为是
		}
		return refIsnotWarning;
	}
	
	/**
	 * 预警任务条件
	 */
//	 private UIRefPane getRefCondition(){
//		 if(refSubject == null){
//		 refSubject = new UIRefPane();
//		 //new 一个科目参照类
//		 // refSubject.setRefModel(arg0);
//		 refSubject.setRefNodeName("会计科目");
//		 refSubject.setBounds(new Rectangle(300, 60, 100, 22));
//		 refSubject.setVisible(true);
//		 if(this.getBufferCondition().get("subject") != null){
//		 refSubject.setPK(this.getBufferCondition().get("subject").toString());
//		 }
//		 }
//		 return refSubject;
//		 }
	private UIRefPane getRefCondition() {
		if (refCondition == null) { 
			refCondition = new UIRefPane();
			refCondition.setBounds(new Rectangle(300, 60, 100, 22));
			refCondition.setVisible(true);
			refCondition.addMouseListener(new MyMouseListener());
		}
		return refCondition;
	}

	/**
	 * 是否定时
	 */
	private JComboBox getIsnotTiming() {
		if (refIsnotTiming == null) {
			refIsnotTiming = new JComboBox();
			refIsnotTiming.setBounds(new Rectangle(100, 100, 100, 22));
			refIsnotTiming.addItem("定时");
			refIsnotTiming.addItem("即时");
			refIsnotTiming.setSelectedIndex(0); // 设定默认为是
		}
		return refIsnotTiming;
	}
	
	/**
	 * 预警任务属性
	 */
	
	private JTextField getRefProperty() {
		if (refProperty == null) { 
			refProperty = new JTextField();
			refProperty.setBounds(new Rectangle(300, 100, 100, 22));
			refProperty.setVisible(true);
			 
		}
		return refProperty;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 起始日期
	 * 
	 * @return
	 */
//	private UIRefPane getRefBeginDate() {
//		if (refBeginDate == null) {
//			refBeginDate = new UIRefPane();
//			refBeginDate.setRefNodeName("日历");
//			refBeginDate.setBounds(new Rectangle(170, 60, 100, 22));
//			refBeginDate.setVisible(true);
//			refBeginDate.getRefModel();
//			refBeginDate.setValue(new UFDate(new Date()).toString());// 默认为当天日期
//			// if(this.getBufferCondition().get("begindate") != null){
//			// refBeginDate.setValue(this.getBufferCondition().get("begindate").toString());
//			// }
//		}
//		return refBeginDate;
//	}

	/**
	 * 结束日期
	 * 
	 * @return
	 */
//	private UIRefPane getRefEndDate() {
//		if (refEndDate == null) {
//			refEndDate = new UIRefPane();
//			refEndDate.setRefNodeName("日历");
//			refEndDate.setBounds(new Rectangle(170, 85, 100, 22));
//			refEndDate.setVisible(true);
//			refEndDate.getRefModel();
//			refEndDate.setValue(new UFDate(new Date()).toString());// 默认为当天日期
//			// if(this.getBufferCondition().get("enddate") != null){
//			// refEndDate.setValue(this.getBufferCondition().get("enddate").toString());
//			// }
//		}
//		return refEndDate;
//	}

	/**
	 * 采购订单号
	 * 
	 * @return
	 */
//	private JTextField getRefCode() {
//		if (refCode == null) {
//			refCode = new JTextField();
//			refCode.setBounds(new Rectangle(170, 110, 100, 22));
//			refCode.setVisible(true);
//		}
//		return refCode;
//	}

	/**
	 * 采购订单参照
	 * 
	 * @return
	 */
//	private JTextField getRefBillCode() {
//		if (refBillCode == null) {
//			refBillCode = new JTextField();
//			refBillCode.setBounds(new Rectangle(170, 135, 100, 22));
//			refBillCode.setVisible(true);
//			refBillCode.addMouseListener(new MyMouseListener());
//		}
//		return refBillCode;
//	}

	// /**
	// * 科目
	// * @return
	// */
//	 private UIRefPane getRefSubject(){
//	 if(refSubject == null){
//	 refSubject = new UIRefPane();
//	 //new 一个科目参照类
//	 // refSubject.setRefModel(arg0);
//	 refSubject.setRefNodeName("会计科目");
//	 refSubject.setBounds(new Rectangle(170, 110, 100, 22));
//	 refSubject.setVisible(true);
//	 if(this.getBufferCondition().get("subject") != null){
//	 refSubject.setPK(this.getBufferCondition().get("subject").toString());
//	 }
//	 }
//	 return refSubject;
//	 }
	class MyMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			// 获得当前时间
			// String newDate = new UFDate(new Date()).toString();
			// // 获取采购订单号进行查询；
			// String vordercode = getRefCode().getText().trim();
			// String sql_code = "";
			// if (vordercode == null || "".equals(vordercode)) {
			// sql_code = "";
			// } else {
			// sql_code = "and vordercode like '%" + vordercode + "%'";
			// }
			// System.out.println("~~~~~~~~~~~~~~~~~~~~~~" + sql_code);
			// String pk_corp = mapClientUIData.get("pk_corp").toString();//
			// 获取当前公司
			// // 验证起始时间不能为空
			// String begindate = getRefBeginDate().getRefName();// 得到起始日期
			// String enddate = getRefEndDate().getRefName();// 得到结束日期
			// if (begindate == null || "".equals(begindate)) {
			// MessageDialog.showWarningDlg(null, "提示", "起始日期不能为空！");
			// } else if (enddate == null || "".equals(enddate)) {
			// MessageDialog.showWarningDlg(null, "提示", "结束日期不能为空！");
			// } else if (begindate.compareTo(enddate) > 0) {
			// MessageDialog.showWarningDlg(null, "提示", "结束日期大于起始日期！");
			// // }else
			// // if(!begindate.substring(0,4).equals(enddate.substring(0,4))){
			// // MessageDialog.showWarningDlg(null, "提示", "跨年度查询影响效率！");
			// }
			// String sqlcontion = ""; // 订单参照查询条件
			// String pk_busitype =
			// mapClientUIData.get("pk_busitype").toString();//
			// 集收提货单时传普通采购订单的业务类型的主键；分收发货单时传分收集结的业务类型主键
			// String sql = mapClientUIData.get("sql").toString();
			// ;// 如果是提货单就过滤掉提货单里面的采购订单号；如果是发货单就过滤掉发货单里面的采购订单号
			// System.out.println("not in sql!!!!!!!!!!!!!" + sql);
			// // String busitypeSql = " select pk_busitype from bd_busitype
			// where
			// // nvl(dr,0)=0 and pk_corp = '"+pk_corp+"' and businame =
			// // '"+busitype+"' ";
			// sqlcontion = " and isnull(dr,0)=0 and pk_corp='" + pk_corp + "' "
			// + sql_code + " and cbiztype = '" + pk_busitype
			// + "' and dorderdate>='" + begindate + "' and dorderdate<='"
			// + enddate + "' and corderid not in(" + sql + ") ";
			// u.setWhereStr(sqlcontion);
			// System.out.println("u.getWhereStr() :" + u.getWhereStr());
		}

		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("mouseExited========================");
		}

		/**
		 * 鼠标单击事件
		 */
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			HashMap mapClientUIData = new HashMap();
			WarningConditionConditionDlg dialog = new WarningConditionConditionDlg(null, mapClientUIData);
			dialog.showModal();
			if (dialog.isCancel()) {// 取消时返回无编辑状态
				dialog.setCancel(false);
				return;
			}
		}

		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("mouseReleased========================");
		}
	}

	/**
	 * 采购订单参照
	 * 
	 * @return
	 */
	public UIRefPane getRefSubject() {
		if (refSubject == null) {
			try {
				refSubject = new UIRefPane();
				// refAccperiod.setRefNodeName(nc.vo.bd.ref.RefNodeNameConst.CORP);//公司档案
				refSubject.setRefNodeName("采购订单号");
				refSubject.setBounds(new Rectangle(170, 135, 100, 22));// 68,
				// 63,
				// 128,
				// 20
				refSubject.setName("take");
			//	refSubject.setRefModel(u);
				refSubject.addMouseListener(new MyMouseListener());
			} catch (Throwable ivjExc) {
				// handleException(ivjExc);
			}
		}
		return refSubject;
	}

	/**
	 * 采购订单过滤条件 已选过的采购订单不能在选;然后清空采购订单里面的数据 如果为空怎么设置
	 */
//	private void initGroupReft() {
//		UIRefPane pan = (UIRefPane) getRefSubject();
//		// pan.addMouseListener(new MyMouseListener());
//		pan.addValueChangedListener(new nc.ui.pub.beans.ValueChangedListener() {
//			public void valueChanged(nc.ui.pub.beans.ValueChangedEvent e) {
//				// TODO Auto-generated Event stub valueChanged()
//				IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance()
//						.lookup(IUAPQueryBS.class.getName());
//				String corderid = getRefSubject().getRefPK();
//				String pk_busitype = mapClientUIData.get("pk_busitype")
//						.toString();
//				String sql = "";
//				if (pk_busitype.equals("1001A31000000000000F")) {
//					sql = "select * from ladings_bill where nvl(dr,0)=0 and pk_purchaseordercode = '"
//							+ corderid + "'";
//					LadingsBillVO lading = null;
//					try {
//						lading = (LadingsBillVO) queryBS.executeQuery(sql,
//								new BeanProcessor(LadingsBillVO.class));
//					} catch (Exception exc) {
//						// TODO Auto-generated catch block
//						exc.printStackTrace();
//					}
//					if (lading != null) {
//						MessageDialog.showWarningDlg(null, "提示", "该订单已使用！");
//						getRefSubject().setPK(null);
//					}
//				} else if (pk_busitype.equals("0001H510000000001BUL")) {
//					sql = "select * from ladings_bill where nvl(dr,0)=0 and pk_purchaseordercode = '"
//							+ corderid + "'";
//					LadingsBillVO lading = null;
//					try {
//						lading = (LadingsBillVO) queryBS.executeQuery(sql,
//								new BeanProcessor(LadingsBillVO.class));
//					} catch (Exception exc) {
//						// TODO Auto-generated catch block
//						exc.printStackTrace();
//					}
//					if (lading != null) {
//						MessageDialog.showWarningDlg(null, "提示", "该订单已使用！");
//						getRefSubject().setPK(null);
//					}
//				} else {
//					sql = "select * from dispatchs_bill where nvl(dr, 0) = 0 and pk_relaterkey = '"
//							+ corderid + "'";
//					DispatchsBillVO disp = null;
//					try {
//						disp = (DispatchsBillVO) queryBS.executeQuery(sql,
//								new BeanProcessor(DispatchsBillVO.class));
//					} catch (Exception exc) {
//						// TODO Auto-generated catch block
//						exc.printStackTrace();
//					}
//					if (disp != null) {
//						MessageDialog.showWarningDlg(null, "提示", "该订单已使用！");
//						getRefSubject().setPK(null);
//					}
//				}
//
//			}
//		});
//	}

	// /**
	// * 对开始日期进行监听，把条件传到订单参照
	// */
	// private void initBeginReft()
	// {
	// UIRefPane pan=(UIRefPane)getRefBeginDate();
	// pan.addValueChangedListener(new nc.ui.pub.beans.ValueChangedListener() {
	// public void valueChanged(nc.ui.pub.beans.ValueChangedEvent e) {
	// // TODO Auto-generated Event stub valueChanged()
	// String begindate = getRefBeginDate().getRefName();//得到起始日期
	// sqlcontion = sqlcontion + " and dorderdate>='"+begindate+"' ";
	// u.setWhereStr(sqlcontion);
	// }
	// });
	// }

	/**
	 * 银行账户
	 * 
	 * @return
	 */
//	private UIRefPane getRefBankAcc() {
//		if (refBankAcc == null) {
//			refBankAcc = new UIRefPane();
//			// new 一个银行账户参照类
//			 refSubject.setRefModel(u);
//			refBankAcc.setRefNodeName("开户银行");
//			refBankAcc.setBounds(new Rectangle(170, 135, 100, 22));
//			refBankAcc.setVisible(true);
//			 
//		}
//		return refBankAcc;
//	}

	/**
	 * 本币下拉框
	 * 
	 * @return
	 */
	private JComboBox getJComboBoxType() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.setBounds(new Rectangle(170, 160, 100, 22));
			jComboBox.addItem("本币");
			// jComboBox.addItem("其它");
			jComboBox.setSelectedIndex(0); // 设定默认为本币
		}
		return jComboBox;
	}

	/**
	 * 未记账凭证
	 */
	private UICheckBox getProjectName() {
		if (cproname == null) {
			cproname = new UICheckBox();
			cproname.setBounds(new Rectangle(170, 185, 40, 22));
			cproname.setVisible(true);
			if (this.getBufferCondition().get("iswjz") != null) {
				cproname.setSelected(Boolean.parseBoolean(this
						.getBufferCondition().get("iswjz").toString().equals(
								"N") ? "true" : "false"));
			}
		}
		return cproname;
	}

	@SuppressWarnings("unchecked")
	private void onBtnOk() {

		// String mainacc = this.getRefMainAcc().getRefName();//得到主体账簿
		// String begindate = this.getRefBeginDate().getRefName();//得到起始日期
		// String enddate = this.getRefEndDate().getRefName();//得到结束日期
		// String subjectName = this.getRefSubject().getRefName();//科目
		// String bankname = this.getRefBankAcc().getRefName();//得到银行账户
		// if(mainacc == null || mainacc.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "错误", "请输入主体账簿!");
		// return;
		// }
		// if(begindate == null || begindate.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "错误", "请输入起始日期!");
		// return;
		// }
		// if(enddate == null || enddate.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "错误", "请输入结束日期!");
		// return;
		// }
		// if(!begindate.substring(0,4).trim().equals(enddate.substring(0,4).trim())){
		// MessageDialog.showErrorDlg(this, "错误", "不支持跨年度银行日记账查询!");
		// return;
		// }
		// if(subjectName == null || subjectName.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "错误", "请输入科目!");
		// return;
		// }
		// if(bankname == null || bankname.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "错误", "请输入开户银行!");
		// return;
		// }
		//        
		// String pk_mainacc = this.getRefMainAcc().getRefPK();//得到主体账簿主键
		// String subject = this.getRefSubject().getRefPK();//得到科目主键
		// String bankacc = this.getRefBankAcc().getRefPK();//得到银行账户主键
		// String currency =
		// getJComboBoxType().getItemAt(getJComboBoxType().getSelectedIndex()).toString();//得到币种
		// Boolean iswjz = this.getProjectName().isSelected();//得到是否未记账凭证
		//        
		// String mainaccCode = this.getRefMainAcc().getRefCode();//得到主体账簿编号
		// String subjectCode = this.getRefSubject().getRefCode();//得到科目编号
		// String bankaccCode = this.getRefBankAcc().getRefCode();//得到银行账户的编号
		//        
		// if(pk_mainacc != null && pk_mainacc.length()!=0){
		// whereSql.put("pk_mainacc", pk_mainacc);
		// whereSql.put("mainacc", mainacc);
		// whereSql.put("mainaccNO", mainaccCode);
		// }
		// if(begindate != null && begindate.length()!=0){
		// whereSql.put("begindate", begindate);
		// }
		// if(enddate != null && enddate.length()!=0){
		// whereSql.put("enddate", enddate);
		// }
		// if(subject != null && subject.length()!=0){
		// whereSql.put("subject", subject);
		// whereSql.put("subjectName", subjectName);
		// whereSql.put("subjectNO", subjectCode);
		// }
		// if(bankacc != null && bankacc.length()!=0){
		// whereSql.put("bankacc", bankacc);
		// whereSql.put("bankname", bankname);
		// whereSql.put("bankaccNO", bankaccCode);
		// }
		// if(currency != null && currency.length()!=0){
		// whereSql.put("currency", currency);
		// }
		// if(iswjz != null){
		// whereSql.put("iswjz", iswjz?"N":"Y");
		// }

		String corderid = getRefSubject().getRefPK();
		if (corderid == null) {
			mapCondition.put("corderid", "");
			MessageDialog.showWarningDlg(null, "提示", "订单不能为空！");
			return;
		} else {
			mapCondition.put("corderid", corderid);
		}

		this.closeOK();
		setCancel(false);
		UIRefPane ref = getRefSubject();
		ref.getRefModel().clearCacheData();
	}

	@Override
	protected void close() {
		// TODO Auto-generated method stub
		super.close();
		setCancel(true);
	}

	/**
	 * This method initializes btnOk
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new UIButton();
			btnOk.setBounds(new Rectangle(58, 230, 68, 22));
			btnOk.setText("确定");
			btnOk.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onBtnOk();
				}
			});
		}
		return btnOk;
	}

	/**
	 * This method initializes btnCancle
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getBtnCancle() {
		if (btnCancle == null) {
			btnCancle = new UIButton();
			btnCancle.setBounds(new Rectangle(184, 230, 68, 22));
			btnCancle.setText("取消");
			btnCancle.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onBtnCancle();
				}
			});
		}
		return btnCancle;
	}

	protected void onBtnCancle() {
		// TODO Auto-generated method stub
		this.closeCancel();
	}

	protected String _getPk_corp() {
		return nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
				.getPk_corp();
	}

	protected UFDate _getBusiDate() {
		return nc.ui.pub.ClientEnvironment.getInstance().getBusinessDate();
	}

	protected String _getOperator() {
		return nc.ui.pub.ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
	}

	public HashMap getWhereSql() {
		return whereSql;
	}

	public void setWhereSql(HashMap whereSql) {
		this.whereSql = whereSql;
	}

	public HashMap getBufferCondition() {
		return bufferCondition;
	}

	public void setBufferCondition(HashMap bufferCondition) {
		this.bufferCondition = bufferCondition;
	}

	public HashMap getMapCondition() {
		return mapCondition;
	}

	public void setMapCondition(HashMap mapCondition) {
		this.mapCondition = mapCondition;
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

}
