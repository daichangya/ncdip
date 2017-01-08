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
	
	
	private JLabel code = null;// ����

	private JTextField refWcode = null;
	
	private JLabel name = null;// ����

	private JTextField refName = null;
	
	private JLabel isnotWarning = null;// �Ƿ�����Ԥ��
	
	private JComboBox refIsnotWarning = null; 
	
	private JLabel  condition = null;// Ԥ����������

	private UIRefPane refCondition = null;
	
	
	private JLabel isnotTiming = null;// �Ƿ�ʱ
	
	private JComboBox refIsnotTiming = null; 
	
	private JLabel  property = null;// Ԥ����������

	private JTextField refProperty = null;
	
	
	
	
	
	
	
	
	
	
	

	private JPanel jPanel = null;

	private UIButton btnOk = null;

	private UIButton btnCancle = null;

	private UICheckBox cproname = null;// δ����ƾ֤

	private JLabel lbcproname = null;

	private JLabel lbMainAcc = null;// ��������

	private UIRefPane refMainAcc = null;// �����˲�����

//	private JLabel lbBeginDate = null;// ��ʼ����
//
//	private UIRefPane refBeginDate = null;

	private JLabel lbEndDate = null;// ��������

	private UIRefPane refEndDate = null;

	private JLabel lbCode = null;// �ɹ�����

	private JTextField refCode = null;// �ɹ�������

	private JTextField refBillCode = null;// �ɹ���������

	private JLabel lbSubject = null;// ��Ŀ

	private UIRefPane refSubject = null;

	private JLabel lbBankAcc = null;// �����˻�

	private UIRefPane refBankAcc = null;

	private JLabel lbComboBox = null;// ����

	private JComboBox jComboBox = null;

	private JComboBox jComboBoxBillType = null;// ��������

	public HashMap whereSql = new HashMap();

	public HashMap bufferCondition = new HashMap();// ��������رմ����һ�εĲ�ѯ����

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
	 * �����棻ֵ��
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
		this.setTitle("Ԥ��");
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
			// lbMainAcc.setText("��������");
			// lbMainAcc.setVisible(true);

			code = new JLabel();
			code.setBounds(new Rectangle(20, 20, 90, 22));
			code.setText("����");
			code.setVisible(true);
			
			name = new JLabel();
			name.setBounds(new Rectangle(210, 20, 130, 22));
			name.setText("����");
			name.setVisible(true);
			
			isnotWarning = new JLabel();
			isnotWarning.setBounds(new Rectangle(20, 60, 90, 22));
			isnotWarning.setText("�Ƿ�����Ԥ��");
			isnotWarning.setVisible(true);
			
			condition = new JLabel();
			condition.setBounds(new Rectangle(210, 60, 130, 22));
			condition.setText("Ԥ����������");
			condition.setVisible(true);

			isnotTiming = new JLabel();
			isnotTiming.setBounds(new Rectangle(20, 100, 90, 22));
			isnotTiming.setText("�Ƿ�ʱ");
			isnotTiming.setVisible(true);
			
			property = new JLabel();
			property.setBounds(new Rectangle(210, 100, 130, 22));
			property.setText("Ԥ����������");
			property.setVisible(true);
			
			
			
			
			
			
			
			
			
			
			
			
			
			
//			lbBeginDate = new JLabel();
//			lbBeginDate.setBounds(new Rectangle(39, 60, 90, 22));
//			lbBeginDate.setText("��ʼ����");
//			lbBeginDate.setVisible(true);

//			lbEndDate = new JLabel();
//			lbEndDate.setBounds(new Rectangle(39, 85, 90, 22));
//			lbEndDate.setText("��������");
//			lbEndDate.setVisible(true);
//
//			lbCode = new JLabel();
//			lbCode.setBounds(new Rectangle(39, 110, 90, 22));
//			lbCode.setText("�ɹ�������");
//			lbCode.setVisible(true);
//
//			lbSubject = new JLabel();
//			lbSubject.setBounds(new Rectangle(39, 135, 90, 22));
//			lbSubject.setText("��������");
//			lbSubject.setVisible(true);

//			 lbBankAcc = new JLabel();
//			 lbBankAcc.setBounds(new Rectangle(39, 135, 90, 22));
//			 lbBankAcc.setText("�����˻�");
//			 lbBankAcc.setVisible(true);
			//        	
			// lbComboBox = new JLabel();
			// lbComboBox.setBounds(new Rectangle(39, 160, 90, 22));
			// lbComboBox.setText("����");
			// lbComboBox.setVisible(true);
			//        	
			// lbcproname = new JLabel();
			// lbcproname.setBounds(new Rectangle(39, 185, 90, 22));
			// lbcproname.setText("�Ƿ�δ����ƾ֤");
			// lbcproname.setVisible(true);

			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getBtnOk(), null);
			jPanel.add(getBtnCancle(), null);

			// jPanel.add(lbMainAcc,null);
			// jPanel.add(getJComboBoxBillType(),null);//��������
			
			
			jPanel.add(code, null); //���
			jPanel.add(getRefWcode(), null);
			
			jPanel.add(name, null);//����
			jPanel.add(getRefName(), null);
			
			jPanel.add(isnotWarning,null);
			jPanel.add(getIsnotWarning(),null);//�Ƿ�����Ԥ��
			
			jPanel.add(condition, null); //Ԥ����������
			jPanel.add(getRefCondition(), null);
			
			jPanel.add(isnotTiming,null);
			jPanel.add(getIsnotTiming(),null);//�Ƿ�ʱ
			
			jPanel.add(property, null); //Ԥ����������
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
			// jPanel.add(lbcproname, null);//δ����ƾ֤
			// jPanel.add(getProjectName(), null);//δ����ƾ֤

		}
		return jPanel;
	}

	/**
	 * �����˲�
	 * 
	 * @return
	 */
	private UIRefPane getRefMainAcc() {
		if (refMainAcc == null) {
			refMainAcc = new UIRefPane();
			// new һ�������˲�������
			// refMainAcc.setRefModel(arg0);
			refMainAcc.setRefNodeName("�˲�����");// ϵͳ����
			refMainAcc.setBounds(new Rectangle(170, 35, 100, 22));
			refMainAcc.setVisible(true);
			// if(this.getBufferCondition().get("pk_mainacc") != null){
			// refMainAcc.setPK(this.getBufferCondition().get("pk_mainacc").toString());
			// }
		}
		return refMainAcc;
	}

	/**
	 * ����
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
	 * ����
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
	 * �Ƿ�Ԥ��
	 */
	private JComboBox getIsnotWarning() {
		if (refIsnotWarning == null) {
			refIsnotWarning = new JComboBox();
			refIsnotWarning.setBounds(new Rectangle(100, 60, 100, 22));
			refIsnotWarning.addItem("��");
			refIsnotWarning.addItem("��");
			refIsnotWarning.setSelectedIndex(0); // �趨Ĭ��Ϊ��
		}
		return refIsnotWarning;
	}
	
	/**
	 * Ԥ����������
	 */
//	 private UIRefPane getRefCondition(){
//		 if(refSubject == null){
//		 refSubject = new UIRefPane();
//		 //new һ����Ŀ������
//		 // refSubject.setRefModel(arg0);
//		 refSubject.setRefNodeName("��ƿ�Ŀ");
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
	 * �Ƿ�ʱ
	 */
	private JComboBox getIsnotTiming() {
		if (refIsnotTiming == null) {
			refIsnotTiming = new JComboBox();
			refIsnotTiming.setBounds(new Rectangle(100, 100, 100, 22));
			refIsnotTiming.addItem("��ʱ");
			refIsnotTiming.addItem("��ʱ");
			refIsnotTiming.setSelectedIndex(0); // �趨Ĭ��Ϊ��
		}
		return refIsnotTiming;
	}
	
	/**
	 * Ԥ����������
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
	 * ��ʼ����
	 * 
	 * @return
	 */
//	private UIRefPane getRefBeginDate() {
//		if (refBeginDate == null) {
//			refBeginDate = new UIRefPane();
//			refBeginDate.setRefNodeName("����");
//			refBeginDate.setBounds(new Rectangle(170, 60, 100, 22));
//			refBeginDate.setVisible(true);
//			refBeginDate.getRefModel();
//			refBeginDate.setValue(new UFDate(new Date()).toString());// Ĭ��Ϊ��������
//			// if(this.getBufferCondition().get("begindate") != null){
//			// refBeginDate.setValue(this.getBufferCondition().get("begindate").toString());
//			// }
//		}
//		return refBeginDate;
//	}

	/**
	 * ��������
	 * 
	 * @return
	 */
//	private UIRefPane getRefEndDate() {
//		if (refEndDate == null) {
//			refEndDate = new UIRefPane();
//			refEndDate.setRefNodeName("����");
//			refEndDate.setBounds(new Rectangle(170, 85, 100, 22));
//			refEndDate.setVisible(true);
//			refEndDate.getRefModel();
//			refEndDate.setValue(new UFDate(new Date()).toString());// Ĭ��Ϊ��������
//			// if(this.getBufferCondition().get("enddate") != null){
//			// refEndDate.setValue(this.getBufferCondition().get("enddate").toString());
//			// }
//		}
//		return refEndDate;
//	}

	/**
	 * �ɹ�������
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
	 * �ɹ���������
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
	// * ��Ŀ
	// * @return
	// */
//	 private UIRefPane getRefSubject(){
//	 if(refSubject == null){
//	 refSubject = new UIRefPane();
//	 //new һ����Ŀ������
//	 // refSubject.setRefModel(arg0);
//	 refSubject.setRefNodeName("��ƿ�Ŀ");
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
			// ��õ�ǰʱ��
			// String newDate = new UFDate(new Date()).toString();
			// // ��ȡ�ɹ������Ž��в�ѯ��
			// String vordercode = getRefCode().getText().trim();
			// String sql_code = "";
			// if (vordercode == null || "".equals(vordercode)) {
			// sql_code = "";
			// } else {
			// sql_code = "and vordercode like '%" + vordercode + "%'";
			// }
			// System.out.println("~~~~~~~~~~~~~~~~~~~~~~" + sql_code);
			// String pk_corp = mapClientUIData.get("pk_corp").toString();//
			// ��ȡ��ǰ��˾
			// // ��֤��ʼʱ�䲻��Ϊ��
			// String begindate = getRefBeginDate().getRefName();// �õ���ʼ����
			// String enddate = getRefEndDate().getRefName();// �õ���������
			// if (begindate == null || "".equals(begindate)) {
			// MessageDialog.showWarningDlg(null, "��ʾ", "��ʼ���ڲ���Ϊ�գ�");
			// } else if (enddate == null || "".equals(enddate)) {
			// MessageDialog.showWarningDlg(null, "��ʾ", "�������ڲ���Ϊ�գ�");
			// } else if (begindate.compareTo(enddate) > 0) {
			// MessageDialog.showWarningDlg(null, "��ʾ", "�������ڴ�����ʼ���ڣ�");
			// // }else
			// // if(!begindate.substring(0,4).equals(enddate.substring(0,4))){
			// // MessageDialog.showWarningDlg(null, "��ʾ", "����Ȳ�ѯӰ��Ч�ʣ�");
			// }
			// String sqlcontion = ""; // �������ղ�ѯ����
			// String pk_busitype =
			// mapClientUIData.get("pk_busitype").toString();//
			// ���������ʱ����ͨ�ɹ�������ҵ�����͵����������շ�����ʱ�����ռ����ҵ����������
			// String sql = mapClientUIData.get("sql").toString();
			// ;// �����������͹��˵����������Ĳɹ������ţ�����Ƿ������͹��˵�����������Ĳɹ�������
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
		 * ��굥���¼�
		 */
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			HashMap mapClientUIData = new HashMap();
			WarningConditionConditionDlg dialog = new WarningConditionConditionDlg(null, mapClientUIData);
			dialog.showModal();
			if (dialog.isCancel()) {// ȡ��ʱ�����ޱ༭״̬
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
	 * �ɹ���������
	 * 
	 * @return
	 */
	public UIRefPane getRefSubject() {
		if (refSubject == null) {
			try {
				refSubject = new UIRefPane();
				// refAccperiod.setRefNodeName(nc.vo.bd.ref.RefNodeNameConst.CORP);//��˾����
				refSubject.setRefNodeName("�ɹ�������");
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
	 * �ɹ������������� ��ѡ���Ĳɹ�����������ѡ;Ȼ����ղɹ�������������� ���Ϊ����ô����
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
//						MessageDialog.showWarningDlg(null, "��ʾ", "�ö�����ʹ�ã�");
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
//						MessageDialog.showWarningDlg(null, "��ʾ", "�ö�����ʹ�ã�");
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
//						MessageDialog.showWarningDlg(null, "��ʾ", "�ö�����ʹ�ã�");
//						getRefSubject().setPK(null);
//					}
//				}
//
//			}
//		});
//	}

	// /**
	// * �Կ�ʼ���ڽ��м�����������������������
	// */
	// private void initBeginReft()
	// {
	// UIRefPane pan=(UIRefPane)getRefBeginDate();
	// pan.addValueChangedListener(new nc.ui.pub.beans.ValueChangedListener() {
	// public void valueChanged(nc.ui.pub.beans.ValueChangedEvent e) {
	// // TODO Auto-generated Event stub valueChanged()
	// String begindate = getRefBeginDate().getRefName();//�õ���ʼ����
	// sqlcontion = sqlcontion + " and dorderdate>='"+begindate+"' ";
	// u.setWhereStr(sqlcontion);
	// }
	// });
	// }

	/**
	 * �����˻�
	 * 
	 * @return
	 */
//	private UIRefPane getRefBankAcc() {
//		if (refBankAcc == null) {
//			refBankAcc = new UIRefPane();
//			// new һ�������˻�������
//			 refSubject.setRefModel(u);
//			refBankAcc.setRefNodeName("��������");
//			refBankAcc.setBounds(new Rectangle(170, 135, 100, 22));
//			refBankAcc.setVisible(true);
//			 
//		}
//		return refBankAcc;
//	}

	/**
	 * ����������
	 * 
	 * @return
	 */
	private JComboBox getJComboBoxType() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.setBounds(new Rectangle(170, 160, 100, 22));
			jComboBox.addItem("����");
			// jComboBox.addItem("����");
			jComboBox.setSelectedIndex(0); // �趨Ĭ��Ϊ����
		}
		return jComboBox;
	}

	/**
	 * δ����ƾ֤
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

		// String mainacc = this.getRefMainAcc().getRefName();//�õ������˲�
		// String begindate = this.getRefBeginDate().getRefName();//�õ���ʼ����
		// String enddate = this.getRefEndDate().getRefName();//�õ���������
		// String subjectName = this.getRefSubject().getRefName();//��Ŀ
		// String bankname = this.getRefBankAcc().getRefName();//�õ������˻�
		// if(mainacc == null || mainacc.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "����", "�����������˲�!");
		// return;
		// }
		// if(begindate == null || begindate.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "����", "��������ʼ����!");
		// return;
		// }
		// if(enddate == null || enddate.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "����", "�������������!");
		// return;
		// }
		// if(!begindate.substring(0,4).trim().equals(enddate.substring(0,4).trim())){
		// MessageDialog.showErrorDlg(this, "����", "��֧�ֿ���������ռ��˲�ѯ!");
		// return;
		// }
		// if(subjectName == null || subjectName.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "����", "�������Ŀ!");
		// return;
		// }
		// if(bankname == null || bankname.trim().length() == 0) {
		// MessageDialog.showErrorDlg(this, "����", "�����뿪������!");
		// return;
		// }
		//        
		// String pk_mainacc = this.getRefMainAcc().getRefPK();//�õ������˲�����
		// String subject = this.getRefSubject().getRefPK();//�õ���Ŀ����
		// String bankacc = this.getRefBankAcc().getRefPK();//�õ������˻�����
		// String currency =
		// getJComboBoxType().getItemAt(getJComboBoxType().getSelectedIndex()).toString();//�õ�����
		// Boolean iswjz = this.getProjectName().isSelected();//�õ��Ƿ�δ����ƾ֤
		//        
		// String mainaccCode = this.getRefMainAcc().getRefCode();//�õ������˲����
		// String subjectCode = this.getRefSubject().getRefCode();//�õ���Ŀ���
		// String bankaccCode = this.getRefBankAcc().getRefCode();//�õ������˻��ı��
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
			MessageDialog.showWarningDlg(null, "��ʾ", "��������Ϊ�գ�");
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
			btnOk.setText("ȷ��");
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
			btnCancle.setText("ȡ��");
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
