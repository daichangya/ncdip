package nc.ui.dip.dataproce;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dap.factor.DapDefItemVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.messagelogo.MessagelogoVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.DefitemVO;

/**
 * 公式向导界面。
 *
 * @author：贾玉淼 2001-5-11 17:07:33
 * <p>修改人：雷军 2005-1-27 应wangxy要求,增加一个公式"getHL(,,,)"
 */
public class ProFormuDefUI extends UIDialog implements ActionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5587427813551864985L;
	private UIPanel ivjFieldSelectPanel = null;
	private UIPanel ivjFormulaPanel = null;
	private UIPanel ivjMainPanel = null;
	private UIPanel ivjOperatorPanel = null;
	private UIPanel ivjOptButtonPanel = null;
	private UIPanel ivjOptQKButtonPanel = null;
	private JPanel ivjUIDialogContentPane = null;
	private UIButton ivjCancelButton = null;
	private UIButton ivjCfmButton = null;
	private UIButton ivjCheckButton = null;
	private UIButton ivjClearButton = null;
	private UITextArea ivjFormulaTArea = null;
	private UIButton ivjANDButton = null;
	private UIButton ivjBiggerButton = null;
	private UIButton ivjDivButton = null;
	private UIButton ivjEqualButton = null;
	private UITabbedPane ivjTablledPane = null;
	private UIList ivjFieldList = null;
	private UIList ivjFieldListBody = null;

	private UIList ivjFormulaList = null;
	private UIButton ivjLeftPrthButton = null;
	private UIButton ivjLIKEButton = null;
	private UIButton ivjMultButton = null;
	private UIButton ivjNOTButton = null;
	private UIButton ivjORButton = null;
	private UIButton ivjPlusButton = null;
	private UIButton ivjReduceButton = null;
	private UIButton ivjRightPrthButton = null;
	private UIButton ivjSmallerButton = null;

	private UIScrollPane ivjFormulaTAreaScrollPanel = null;
	//记录光标所在位置
	private int m_caretPosi = 0;

	//固定值参照
	//设置固定值参照是否显示
	private UILabel ivjHintLabel = null;
	//保存所有的单据项
	private DapDefItemVO[] m_allBillItems = null;
	private DapDefItemVO[] m_headerBillItems = null;
	private DapDefItemVO[] m_bodyBillItems = null;

	//基本档案
	private nc.ui.pub.beans.UIButton m_btnBaseDoc;
	//外部系统基本档案
	private nc.ui.pub.beans.UIButton m_btnWb;


	private UITextField matchText = null;
	private UIButton btnSearch = null;
	private String pk_sys;
	public  int OK=0;//如果点击确认按钮为1
	
	public void setPk_sys(String ss){
		this.pk_sys=ss;
		
	}
	public String getPk_sys(){
		return pk_sys;
	}
    

	/**
	 * ProFormuDefUI 构造子注解。
	 * @param parent java.awt.Container
	 */
	public ProFormuDefUI(Container parent,String pk_sys) {
		super(parent);
		setPk_sys(pk_sys);
		initialize();
		setResizable(false);
		
	}




	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getANDButton())) {
			getFormulaTArea().insert(" AND ", m_caretPosi);
		}
		if (e.getSource().equals(getBiggerButton())) {
			getFormulaTArea().insert(">", m_caretPosi);
		}
		if (e.getSource().equals(getCancelButton())) {
			this.closeCancel();
		}
		if (e.getSource().equals(getCfmButton())) {
		
			/*if (!nc.vo.pf.pub.PfFormulaParse.isFormula(formulaAnalyze(getFormulaTArea().getText().trim()))) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000039")@res "公式错误",
						nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000040")@res "定义的公式有误，请重新检查！");
				return;
			} else {*/
				OK=1;
				this.closeOK();
//			}
		}
		if (e.getSource().equals(getCheckButton())) {
			String strFormula = formulaAnalyze(getFormulaTArea().getText().trim());
			if (nc.vo.pf.pub.PfFormulaParse.isFormula(strFormula)) {
				MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000041")/*@res "公式正确"*/,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000042")/*@res "定义的公式完全正确！"*/);
			} else {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000039")/*@res "公式错误"*/,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000040")/*@res "定义的公式有误，请重新检查！"*/);
			}

		}
		if (e.getSource().equals(getClearButton())) {
			getFormulaTArea().setText(null);
			m_caretPosi = 0;
		}
		if (e.getSource().equals(getDivButton())) {
			getFormulaTArea().insert("/", m_caretPosi);
		}
		if (e.getSource().equals(getEqualButton())) {
			getFormulaTArea().insert("=", m_caretPosi);
		}
		if (e.getSource().equals(getLeftPrthButton())) {
			getFormulaTArea().insert("(", m_caretPosi);
		}
		if (e.getSource().equals(getLIKEButton())) {
			getFormulaTArea().insert(" LIKE ", m_caretPosi);
		}
		if (e.getSource().equals(getMultButton())) {
			getFormulaTArea().insert("*", m_caretPosi);
		}
		if (e.getSource().equals(getNOTButton())) {
			getFormulaTArea().insert(" IS NOT ", m_caretPosi);
		}
		if (e.getSource().equals(getORButton())) {
			getFormulaTArea().insert(" OR ", m_caretPosi);
		}
		if (e.getSource().equals(getPlusButton())) {
			getFormulaTArea().insert("+", m_caretPosi);
		}
		if (e.getSource().equals(getReduceButton())) {
			getFormulaTArea().insert("-", m_caretPosi);
		}
		if (e.getSource().equals(getRightPrthButton())) {
			getFormulaTArea().insert(")", m_caretPosi);
		}
		if (e.getSource().equals(getSmallerButton())) {
			getFormulaTArea().insert("<", m_caretPosi);
		}
		/**
		 * 进行过滤
		 */
		if (e.getSource().equals(getBtnSearch())) {
			doFilter();
		}
	}

	String _special = null;
	int _index = 0;

	/**
	 * 进行单据项目过滤
	 * 
	 */
	void doFilter() {
		//
		String special = getMatchText().getText();
		if (special == null || special.length() == 0) {
			//do nothing
			return;
		}

		Pattern p = Pattern.compile(special);
		UIList aim = getTabbedPane().getSelectedIndex() == 0 ? getFieldList() : getFieldListBody();
		int count = aim.getModel().getSize();

		int currentSelect = aim.getSelectedIndex() + 1;

		boolean _isSame = special.equalsIgnoreCase(_special);
		boolean _selectSame = currentSelect == _index;

		int begin = 0;

		if (_isSame && _selectSame) {
			begin = _index;
		} else {
			if (_isSame) {
				begin = currentSelect == -1 ? 0 : currentSelect;
			} else {
				begin = 0;
			}
		}
		_special = special;

		for (int i = begin; i < count; i++) {
			DapDefItemVO selectObject = (DapDefItemVO) aim.getModel().getElementAt(i);
			String itemName = selectObject.getItemname();
			Matcher m = p.matcher(itemName);
			if (m.find()) {
				aim.setSelectedIndex(i);
				aim.scrollRectToVisible(aim.getCellBounds(i, i));
				_index = i + 1;
				break;
			}
		}
	}

	/**
	 * 分析公式成为公式处理，从单据项名称转换成属性名称。
	 * 创建日期：(2001-6-8 15:52:03)
	 * @return java.lang.String
	 * @param formula java.lang.String
	 */
	public String formulaAnalyze(String formula) {
		String formulaProc = formula;
		/*add by st 2003-10-21*/
		/*formulaProc = BdInputDlg.formulaAnalyze(formulaProc);
		end 
		if (formulaProc != null) {
			if (m_allBillItems != null && m_allBillItems.length != 0) {
				for (int i = 0; i < m_allBillItems.length; i++) {
					int flag = 0;
					do {
						int situ = formulaProc.indexOf("@" + m_allBillItems[i].getItemname().trim()
								+ m_allBillItems[i].getItemtype().toString().trim() + "@");
						if (situ != -1) {
							int firt = situ;
							int end = situ
									+ ("@" + m_allBillItems[i].getItemname().trim() + m_allBillItems[i].getItemtype().toString().trim() + "@")
											.length();
							//问题出现在这里
							formulaProc = formulaProc.substring(0, firt) + "@" + m_allBillItems[i].getAttrname().trim()
									+ m_allBillItems[i].getItemtype().toString().trim() + "@" + formulaProc.substring(end);
						}
						flag = situ;
					} while (flag != -1);
//					flag = 0;
//					do {
//						int situ = formulaProc.indexOf("#" + m_allBillItems[i].getItemname().trim()
//								/*+ m_allBillItems[i].getItemtype().toString().trim() + "#");*/
//						if (situ != -1) {
//							int firt = situ;
//							int end = situ
//									+ ("#" + m_allBillItems[i].getItemname().trim() /*+ m_allBillItems[i].getItemtype().toString().trim()*/ + "#")
//											.length();
//							formulaProc = formulaProc.substring(0, firt) + "#" + m_allBillItems[i].getAttrname().trim()
//									/*+ m_allBillItems[i].getItemtype().toString().trim()*/ + "#" + formulaProc.substring(end);
//						}
//						flag = situ;
//					} while (flag != -1);
//				}
//			}
//		}*/
		return formulaProc;
	}


	/**
	 * 返回 UIButton182 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getANDButton() {
		if (ivjANDButton == null) {
			try {
				ivjANDButton = new nc.ui.pub.beans.UIButton();
				ivjANDButton.setName("ANDButton");
				ivjANDButton.setText("AND");
				ivjANDButton.setBounds(234, 0, 38, 22);
				ivjANDButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				
				// user code begin {1}
				ivjANDButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjANDButton;
	}
    /**
     * 外系统基本档案
     * 2011-5-19
     * lx
     */
	public nc.ui.pub.beans.UIButton getBtnWb(){
		if(m_btnWb==null){
			m_btnWb=new UIButton();
			m_btnWb.setName("m_btnWb");
			m_btnWb.setText("外系统基本档案");
			m_btnWb.setBounds(220, 0, 100, 20);
			m_btnWb.setMargin(new java.awt.Insets(2,4,2,4));
			m_btnWb.addActionListener(this);
		}
		return m_btnWb;
	}
	
	
	
	
	
	/**
	 * 得到基本档案录入参照
	 * 创建日期：(2003-10-21 09:38:26)
	 * @author：Administrator
	 * @return nc.ui.pub.beans.UIButton
	 */
	public nc.ui.pub.beans.UIButton getBaseDocButton() {
		if (m_btnBaseDoc == null) {
			m_btnBaseDoc = new UIButton();
			m_btnBaseDoc.setName("BaseDocButton");
			m_btnBaseDoc.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000043")/*@res "基本档案"*/);
			m_btnBaseDoc.setBounds(160, 0, 60, 20);
			m_btnBaseDoc.setMargin(new java.awt.Insets(2, 4, 2, 4));
			// user code begin {1}
			m_btnBaseDoc.addActionListener(this);
			// user code end
		}
		return m_btnBaseDoc;
	}


	/**
	 * 返回 UIButton14 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBiggerButton() {
		if (ivjBiggerButton == null) {
			try {
				ivjBiggerButton = new nc.ui.pub.beans.UIButton();
				ivjBiggerButton.setName("BiggerButton");
				ivjBiggerButton.setText(">");
				ivjBiggerButton.setBounds(80, 0, 20, 22);
				ivjBiggerButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjBiggerButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBiggerButton;
	}

	/**
	 * 返回所有的单据项。
	 * 创建日期：(2001-6-8 15:19:20)
	 * @return String[]
	 */
	public DapDefItemVO[] getBillItems() {
		return m_allBillItems;
	}

	/**
	 * 返回 CancelButton 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getCancelButton() {
		if (ivjCancelButton == null) {
			try {
				ivjCancelButton = new nc.ui.pub.beans.UIButton();
				ivjCancelButton.setName("CancelButton");
				ivjCancelButton.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "取消"*/);
				ivjCancelButton.setBounds(70, 0, 70, 22);
				ivjCancelButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
				// user code begin {1}
				ivjCancelButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCancelButton;
	}

	/**
	 * 返回 CfmButton 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getCfmButton() {
		if (ivjCfmButton == null) {
			try {
				ivjCfmButton = new nc.ui.pub.beans.UIButton();
				ivjCfmButton.setName("CfmButton");
				ivjCfmButton.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "确定"*/);
				ivjCfmButton.setBounds(0, 0, 70, 22);
				ivjCfmButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
				// user code begin {1}
				ivjCfmButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCfmButton;
	}

	/**
	 * 返回 CheckButton 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getCheckButton() {
		if (ivjCheckButton == null) {
			try {
				ivjCheckButton = new nc.ui.pub.beans.UIButton();
				ivjCheckButton.setName("CheckButton");
				ivjCheckButton.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000045")/*@res "检查"*/);
				ivjCheckButton.setBounds(80, 0, 40, 20);
				ivjCheckButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
				// user code begin {1}
				ivjCheckButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCheckButton;
	}

	/**
	 * 返回 ClearButton 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getClearButton() {
		if (ivjClearButton == null) {
			try {
				ivjClearButton = new nc.ui.pub.beans.UIButton();
				ivjClearButton.setName("ClearButton");
				ivjClearButton.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000040")/*@res "清空"*/);
				ivjClearButton.setBounds(0, 0, 40, 20);
				ivjClearButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
				// user code begin {1}
				ivjClearButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjClearButton;
	}

	/**
	 * 返回 UIButton11 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getDivButton() {
		if (ivjDivButton == null) {
			try {
				ivjDivButton = new nc.ui.pub.beans.UIButton();
				ivjDivButton.setName("DivButton");
				ivjDivButton.setText("÷");
				ivjDivButton.setBounds(20, 0, 20, 22);
				ivjDivButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjDivButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjDivButton;
	}

	/**
	 * 返回 UIButton16 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getEqualButton() {
		if (ivjEqualButton == null) {
			try {
				ivjEqualButton = new nc.ui.pub.beans.UIButton();
				ivjEqualButton.setName("EqualButton");
				ivjEqualButton.setText("=");
				ivjEqualButton.setBounds(120, 0, 20, 22);
				ivjEqualButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjEqualButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjEqualButton;
	}

	/**
	 * 返回 FieldList 特性值。
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UIList getFieldList() {
		if (ivjFieldList == null) {
			try {
				ivjFieldList = new nc.ui.pub.beans.UIList();
				ivjFieldList.setName("FieldList");
				//ivjFieldList.setBorder(new javax.swing.border.EtchedBorder());
				ivjFieldList.setBounds(0, 0, 107, 146);
				ivjFieldList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				// user code begin {1}
				ivjFieldList.addMouseListener(this);


				//ivjFieldList.ad
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFieldList;
	}

	/**
	 * 返回 FieldList 特性值。
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UIList getFieldListBody() {
		if (ivjFieldListBody == null) {
			try {
				ivjFieldListBody = new nc.ui.pub.beans.UIList();
				ivjFieldListBody.setName("FieldListBody");
				//ivjFieldList.setBorder(new javax.swing.border.EtchedBorder());
				ivjFieldListBody.setBounds(0, 0, 107, 146);
				ivjFieldListBody.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				// user code begin {1}
				ivjFieldListBody.addMouseListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFieldListBody;
	}

	/**
	 * 容纳字段，根据表头、表体进行分组
	 * @return
	 */
	private UITabbedPane getTabbedPane() {
		if (ivjTablledPane == null) {
			try {
				ivjTablledPane = new UITabbedPane();
				ivjTablledPane.setName("FiledTabbedPane");
//				ivjTablledPane.addTab("业务表", getFieldScrollPane());
//				ivjTablledPane.addTab("子表", getFieldScrollPaneBody());
				ivjTablledPane.addTab("NC系统基础表",getNCB());
//				ivjTablledPane.addTab("外部系统表", getFieldScrollPaneBody());
				ivjTablledPane.addTab("外部系统表", getWbXTB());
				
			} catch (Exception ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjTablledPane;
	}
	JPanel jwbxtb;

    DefaultListModel leftModel = new DefaultListModel();
    DefaultListModel rightMOdel = new DefaultListModel();
    JList leftList = new JList(leftModel);
    JList rightList = new JList(rightMOdel);

	private JPanel getWbXTB(){
		if(jwbxtb==null){
			jwbxtb=new JPanel();
			jwbxtb.setLayout(null);
			jwbxtb.setPreferredSize(new java.awt.Dimension(307, 300));
			JScrollPane jScrollPane1 = new JScrollPane();
			jwbxtb.add(jScrollPane1);
			jScrollPane1.setBounds(5, 5, 270, 128);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JScrollPane jScrollPane2 = new JScrollPane();
			jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane2.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jwbxtb.add(jScrollPane2);
			jScrollPane2.setBounds(5, 139, 270, 300);
			try {
				DipDatadefinitHVO[] hvo=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_xt='"+getPk_sys()+"' and (isfolder is null or isfolder='N')");// and  Pk_Datadefinit_h<>'"+getPk_table()+"'");
				if(hvo!=null&&hvo.length>0){
					int i=0;
					wbmap=new HashMap<String, String>();
					String[] str=new String[hvo.length];
					for(DipDatadefinitHVO hvoi:hvo){
						str[i]=hvoi.getMemorytable()+" <"+hvoi.getSysname()+">";
						
						wbmap.put(hvoi.getMemorytable()+" <"+hvoi.getSysname()+">", hvoi.getPrimaryKey());
						i++;
					}
					ListModel jList1Model = 
						new DefaultComboBoxModel(str);
					leftList.setModel(jList1Model);
				leftList.addMouseListener(this);
				}
				jScrollPane1.setViewportView(leftList);
				{
					rightList = new JList();
					rightList.addMouseListener(this);
				}
				jScrollPane2.setViewportView(rightList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jwbxtb;
	}
	JPanel jncb;

    DefaultListModel ncleftModel = new DefaultListModel();
    DefaultListModel ncrightMOdel = new DefaultListModel();
    JList ncleftList = new JList(ncleftModel);
    JList ncrightList = new JList(ncrightMOdel);
    Map<String,String> ncmap;
    Map<String,String> wbmap;
	private JPanel getNCB(){
		if(jncb==null){
			jncb=new JPanel();
			jncb.setLayout(null);
			jncb.setPreferredSize(new java.awt.Dimension(307, 300));
			JScrollPane jScrollPane1 = new JScrollPane();
			jncb.add(jScrollPane1);
			jScrollPane1.setBounds(5, 5, 270, 128);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JScrollPane jScrollPane2 = new JScrollPane();
			jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane2.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jncb.add(jScrollPane2);
			jScrollPane2.setBounds(5, 139, 270, 300);
			try {
				{
					ncleftList = new JList();
					DipDatadefinitHVO[] hvo=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, " pk_xt='0001AA1000000001XQ1B' and (isfolder is null or isfolder='N')");
					if(hvo!=null&&hvo.length>0){
						String[] str=new String[hvo.length+1];
						ncmap=new HashMap<String, String>();
						ncmap.put("DIP_MESSAGELOGO<扩展函数>", "DIP_MESSAGELOGO");
						str[0]="DIP_MESSAGELOGO<扩展函数>";
						int i=1;
						for(DipDatadefinitHVO vo:hvo){
							str[i]=vo.getMemorytable()+" <"+vo.getSysname()+">";
							ncmap.put(vo.getMemorytable()+" <"+vo.getSysname()+">", vo.getPrimaryKey());
							i++;
						}
						ListModel jList1Model = 
							new DefaultComboBoxModel(str);
						ncleftList.setModel(jList1Model);
					}
					ncleftList.addMouseListener(this);
				}
				jScrollPane1.setViewportView(ncleftList);
				{
					ncrightList = new JList();
					ncrightList.addMouseListener(this);
				}
				jScrollPane2.setViewportView(ncrightList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jncb;
	}

	/**
	 * 返回 FieldSelectPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getFieldSelectPanel() {
		if (ivjFieldSelectPanel == null) {
			try {
				ivjFieldSelectPanel = new nc.ui.pub.beans.UIPanel();
				ivjFieldSelectPanel.setName("FieldSelectPanel");
				ivjFieldSelectPanel.setLayout(getFieldSelectPanelGridLayout());
				ivjFieldSelectPanel.setBounds(3,3, 283, 470);
//				ivjFieldSelectPanel.add(getFormulaScrollPane(), getFormulaScrollPane().getName());
				ivjFieldSelectPanel.add(getTabbedPane(), getTabbedPane().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFieldSelectPanel;
	}

	/**
	 * 返回 FieldSelectPanelGridLayout 特性值。
	 * @return java.awt.GridLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.GridLayout getFieldSelectPanelGridLayout() {
		java.awt.GridLayout ivjFieldSelectPanelGridLayout = null;
		try {
			/* 创建部件 */
			ivjFieldSelectPanelGridLayout = new java.awt.GridLayout();
			ivjFieldSelectPanelGridLayout.setHgap(10);
			ivjFieldSelectPanelGridLayout.setColumns(2);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjFieldSelectPanelGridLayout;
	}



	/**
	 * 获取取数公式。
	 * 创建日期：(2001-5-15 16:10:30)
	 * @return java.lang.String
	 */
	public String getFormula() {
		return getFormulaTArea().getText();
	}

	/**
	 * 返回 FormulaList 特性值。
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */

	/**
	 * 返回 FormulaPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getFormulaPanel() {
		if (ivjFormulaPanel == null) {
			try {
				ivjFormulaPanel = new nc.ui.pub.beans.UIPanel();
				ivjFormulaPanel.setName("FormulaPanel");
				ivjFormulaPanel.setBorder(new javax.swing.border.CompoundBorder());
				ivjFormulaPanel.setLayout(new java.awt.GridLayout());
				ivjFormulaPanel.setBounds(290, 7, 510, 435);
				ivjFormulaPanel.add(getFormulaTAreaScrollPanel(), getFormulaTAreaScrollPanel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				ivjExc.printStackTrace();
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormulaPanel;
	}


	/**
	 * 返回 FormulaTArea 特性值。
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextArea getFormulaTArea() {
		if (ivjFormulaTArea == null) {
			try {
				ivjFormulaTArea = new nc.ui.pub.beans.UITextArea();
				ivjFormulaTArea.setName("FormulaTArea");
				ivjFormulaTArea.setBorder(new javax.swing.border.EtchedBorder());
				ivjFormulaTArea.setBounds(0, 0, 201, 124);
				// user code begin {1}
				ivjFormulaTArea.setLineWrap(true);
				ivjFormulaTArea.setWrapStyleWord(true);
				ivjFormulaTArea.addCaretListener(new CaretListener() {
					public void caretUpdate(CaretEvent e) {
						m_caretPosi = e.getMark();
					}
				});
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormulaTArea;
	}

	/**
	 * 返回 FormulaTAreaScrollPanel 特性值。
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getFormulaTAreaScrollPanel() {
		if (ivjFormulaTAreaScrollPanel == null) {
			try {
				ivjFormulaTAreaScrollPanel = new nc.ui.pub.beans.UIScrollPane();
				ivjFormulaTAreaScrollPanel.setName("FormulaTAreaScrollPanel");
				getFormulaTAreaScrollPanel().setViewportView(getFormulaTArea());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormulaTAreaScrollPanel;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-8-21 9:46:30)
	 * @return nc.ui.pub.beans.UILabel
	 */
	public UILabel getHintLabel() {
		if (ivjHintLabel == null) {
			try {
				ivjHintLabel = new nc.ui.pub.beans.UILabel();
				ivjHintLabel.setName("HintLabel");
				ivjHintLabel
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000047")/*@res "注意：手工输入字符串时，请一定使用英文双引号！"*/);
				ivjHintLabel.setBounds(1, 558, 342, 22);
				//ivjHintLabel.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				//ivjHintLabel.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjHintLabel;
	}

	/**
	 * 返回 UIButton17 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getLeftPrthButton() {
		if (ivjLeftPrthButton == null) {
			try {
				ivjLeftPrthButton = new nc.ui.pub.beans.UIButton();
				ivjLeftPrthButton.setName("LeftPrthButton");
				ivjLeftPrthButton.setText("(");
				ivjLeftPrthButton.setBounds(140, 0, 20, 22);
				ivjLeftPrthButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjLeftPrthButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLeftPrthButton;
	}

	/**
	 * 返回 UIButton181 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getLIKEButton() {
		if (ivjLIKEButton == null) {
			try {
				ivjLIKEButton = new nc.ui.pub.beans.UIButton();
				ivjLIKEButton.setName("LIKEButton");
				ivjLIKEButton.setText("LIKE");
				ivjLIKEButton.setBounds(196, 0, 38, 22);
				ivjLIKEButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjLIKEButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLIKEButton;
	}

	/**
	 * 返回 MainPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getMainPanel() {
		if (ivjMainPanel == null) {
			try {
				ivjMainPanel = new nc.ui.pub.beans.UIPanel();
				ivjMainPanel.setName("MainPanel");
				ivjMainPanel.setLayout(null);
				getMainPanel().add(getOptQKButtonPanel(), getOptQKButtonPanel().getName());//清空按钮
				getMainPanel().add(getOptButtonPanel(), getOptButtonPanel().getName());//确定取消按钮
				getMainPanel().add(getFormulaPanel(), getFormulaPanel().getName());
				getMainPanel().add(getOperatorPanel(), getOperatorPanel().getName());
				getMainPanel().add(getFieldSelectPanel(), getFieldSelectPanel().getName());
				getMainPanel().add(getHintLabel());

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjMainPanel;
	}

	/**
	 * 返回 UIButton1 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getMultButton() {
		if (ivjMultButton == null) {
			try {
				ivjMultButton = new nc.ui.pub.beans.UIButton();
				ivjMultButton.setName("MultButton");
				ivjMultButton.setText("×");
				ivjMultButton.setBounds(0, 0, 20, 22);
				ivjMultButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjMultButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMultButton;
	}

	/**
	 * 返回 UIButton184 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getNOTButton() {
		if (ivjNOTButton == null) {
			try {
				ivjNOTButton = new nc.ui.pub.beans.UIButton();
				ivjNOTButton.setName("NOTButton");
				ivjNOTButton.setText("NOT");
				ivjNOTButton.setBounds(307, 0, 35, 22);
				ivjNOTButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjNOTButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjNOTButton;
	}

	/**
	 * 返回 OperatorPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getOperatorPanel() {
		if (ivjOperatorPanel == null) {
			try {
				ivjOperatorPanel = new nc.ui.pub.beans.UIPanel();
				ivjOperatorPanel.setName("OperatorPanel");
				ivjOperatorPanel.setLayout(null);
				ivjOperatorPanel.setBounds(290, 450, 440, 22);
				getOperatorPanel().add(getMultButton(), getMultButton().getName());
				getOperatorPanel().add(getDivButton(), getDivButton().getName());
				getOperatorPanel().add(getPlusButton(), getPlusButton().getName());
				getOperatorPanel().add(getReduceButton(), getReduceButton().getName());
				getOperatorPanel().add(getBiggerButton(), getBiggerButton().getName());
				getOperatorPanel().add(getSmallerButton(), getSmallerButton().getName());
				getOperatorPanel().add(getEqualButton(), getEqualButton().getName());
				getOperatorPanel().add(getLeftPrthButton(), getLeftPrthButton().getName());
				getOperatorPanel().add(getRightPrthButton(), getRightPrthButton().getName());
				getOperatorPanel().add(getLIKEButton(), getLIKEButton().getName());
				getOperatorPanel().add(getANDButton(), getANDButton().getName());
				getOperatorPanel().add(getORButton(), getORButton().getName());
				getOperatorPanel().add(getNOTButton(), getNOTButton().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOperatorPanel;
	}
	/**
	 * 返回 OptButtonPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getOptQKButtonPanel() {
		if (ivjOptQKButtonPanel == null) {
			try {
				ivjOptQKButtonPanel = new nc.ui.pub.beans.UIPanel();
				ivjOptQKButtonPanel.setName("ivjOptQKButtonPanel");
				ivjOptQKButtonPanel.setLayout(null);
				ivjOptQKButtonPanel.setBounds(760, 450, 50, 22);
				getOptQKButtonPanel().add(getClearButton(), getClearButton().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOptQKButtonPanel;
	}

	/**
	 * 返回 OptButtonPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getOptButtonPanel() {
		if (ivjOptButtonPanel == null) {
			try {
				ivjOptButtonPanel = new nc.ui.pub.beans.UIPanel();
				ivjOptButtonPanel.setName("OptButtonPanel");
				ivjOptButtonPanel.setLayout(null);
				ivjOptButtonPanel.setBounds(620, 490, 150, 22);
				getOptButtonPanel().add(getCfmButton(), getCfmButton().getName());
				getOptButtonPanel().add(getCancelButton(), getCancelButton().getName());
//				getOptButtonPanel().add(getClearButton(), getClearButton().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOptButtonPanel;
	}

	/**
	 * 返回 UIButton183 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getORButton() {
		if (ivjORButton == null) {
			try {
				ivjORButton = new nc.ui.pub.beans.UIButton();
				ivjORButton.setName("ORButton");
				ivjORButton.setText("OR");
				ivjORButton.setBounds(272, 0, 35, 22);
				ivjORButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjORButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjORButton;
	}

	/**
	 * 返回 UIButton12 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getPlusButton() {
		if (ivjPlusButton == null) {
			try {
				ivjPlusButton = new nc.ui.pub.beans.UIButton();
				ivjPlusButton.setName("PlusButton");
				ivjPlusButton.setText("+");
				ivjPlusButton.setBounds(40, 0, 20, 22);
				ivjPlusButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjPlusButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPlusButton;
	}

	/**
	 * 返回 UIButton13 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getReduceButton() {
		if (ivjReduceButton == null) {
			try {
				ivjReduceButton = new nc.ui.pub.beans.UIButton();
				ivjReduceButton.setName("ReduceButton");
				ivjReduceButton.setText("-");
				ivjReduceButton.setBounds(60, 0, 20, 22);
				ivjReduceButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjReduceButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjReduceButton;
	}

	/**
	 * 返回 UIButton18 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getRightPrthButton() {
		if (ivjRightPrthButton == null) {
			try {
				ivjRightPrthButton = new nc.ui.pub.beans.UIButton();
				ivjRightPrthButton.setName("RightPrthButton");
				ivjRightPrthButton.setText(")");
				ivjRightPrthButton.setBounds(160, 0, 20, 22);
				ivjRightPrthButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjRightPrthButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRightPrthButton;
	}

	/**
	 * 返回 UIButton15 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getSmallerButton() {
		if (ivjSmallerButton == null) {
			try {
				ivjSmallerButton = new nc.ui.pub.beans.UIButton();
				ivjSmallerButton.setName("SmallerButton");
				ivjSmallerButton.setText("<");
				ivjSmallerButton.setBounds(100, 0, 20, 22);
				ivjSmallerButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjSmallerButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSmallerButton;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.GridLayout());
				getUIDialogContentPane().add(getMainPanel(), getMainPanel().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		// System.out.println("--------- 未捕捉到的异常 ---------");
		Logger.error(exception);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			setName("ProFormuDefUI");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(815, 550);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000048")/*@res "公式定义"*/);
			setContentPane(getUIDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * Invoked when the mouse has been clicked on a component.
	 */
	public void mouseClicked(MouseEvent e) {
		boolean fromHead = e.getSource().equals(getFieldList());
		boolean fromBody = e.getSource().equals(getFieldListBody());
		boolean ncleft=e.getSource().equals(ncleftList);
		boolean ncriget=e.getSource().equals(ncrightList);
		boolean wbleft=e.getSource().equals(leftList);
		boolean wbrigtht=e.getSource().equals(rightList);

		if(ncleft&&e.getClickCount()==2){
			getFormulaTArea().insert(ncleftList.getSelectedValue().toString().split(" <")[0],m_caretPosi);
			
		}else if(ncleft&&e.getClickCount()==1){
			String pk=ncmap.get(ncleftList.getSelectedValue().toString());
			try {
				if("DIP_MESSAGELOGO".equals(pk)){
					MessagelogoVO[] bvos = (MessagelogoVO[])HYPubBO_Client.queryByCondition(MessagelogoVO.class, "coalesce(dr,0)=0");
					String [] str=new String[bvos.length];
					int i=0;
					for(MessagelogoVO bvo:bvos){
						str[i]="@"+bvo.getCdata()+"@ <"+bvo.getCname()+">";
						i++;
					}
					ListModel lm = 
						new DefaultComboBoxModel(str);
					ncrightList.setModel(lm);
				}else{
					DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "Pk_Datadefinit_h='"+pk+"'");
					if(bvos!=null&&bvos.length>0){
						String [] str=new String[bvos.length];
						int i=0;
						for(DipDatadefinitBVO bvo:bvos){
							str[i]=bvo.getEname()+" <"+bvo.getCname()+">";
							i++;
						}
						ListModel lm = 
							new DefaultComboBoxModel(str);
						ncrightList.setModel(lm);
					}
				}
			} catch (UifException e1) {
				e1.printStackTrace();
			}
		}
		if(wbleft&&e.getClickCount()==2){
			getFormulaTArea().insert(leftList.getSelectedValue().toString().split(" <")[0],m_caretPosi);
			
		}else if(wbleft&&e.getClickCount()==1){
			String pk=wbmap.get(leftList.getSelectedValue().toString());
			try {
				DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "Pk_Datadefinit_h='"+pk+"'");
				if(bvos!=null&&bvos.length>0){
					String [] str=new String[bvos.length];
					int i=0;
					for(DipDatadefinitBVO bvo:bvos){
						str[i]=bvo.getEname()+" <"+bvo.getCname()+">";
						i++;
					}
					ListModel lm = 
						new DefaultComboBoxModel(str);
					rightList.setModel(lm);
				}
			} catch (UifException e1) {
				e1.printStackTrace();
			}
		}
		if(wbrigtht&&e.getClickCount()==2){
			getFormulaTArea().insert(rightList.getSelectedValue().toString().split(" <")[0],m_caretPosi);
		}
		if(ncriget&&e.getClickCount()==2){
			getFormulaTArea().insert(ncrightList.getSelectedValue().toString().split(" <")[0],m_caretPosi);
		}
		
		
		if ((fromHead || fromBody) && e.getClickCount() == 2) {

			UIList list = (UIList) e.getSource();
			DapDefItemVO selectObject = (DapDefItemVO) list.getSelectedValue();
			DefitemVO selectedItem = selectObject.getDefItems();
			if (selectedItem.getHeadflag().equals(new nc.vo.pub.lang.UFBoolean("Y"))) {
				getFormulaTArea().insert(selectedItem.getAttrname().trim().toLowerCase()  /*selectedItem.getItemtype().toString().trim() +*/ ,
						m_caretPosi);
			} else {
				getFormulaTArea().insert( selectedItem.getAttrname().trim()  /*selectedItem.getItemtype().toString().trim() +*/ ,
						m_caretPosi);
			}
		}
	}

	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Invoked when the mouse exits a component.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-6-8 15:11:41)
	 * @param items nc.vo.dap.vouchtemp.DefitemVO[]
	 */
	public void setBillItems(DefitemVO[] items) {
		m_allBillItems = DapDefItemVO.toArray(items);
		int len = m_allBillItems == null ? 0 : m_allBillItems.length;
		if (len == 0) {
			MessageDialog.showWarningDlg(new java.awt.Container(), nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("101201", "UPP101201-000049")/*@res "单据项查询结果错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("101201",
					"UPP101201-000050")/*@res "没有任何对应的单据项"*/);
			return;
		}

		List<DapDefItemVO> header = new ArrayList<DapDefItemVO>(len);
		List<DapDefItemVO> body = new ArrayList<DapDefItemVO>(len);
		for (int i = 0; i < len; i++) {
			if (m_allBillItems[i].getHeadflag().booleanValue() == true) {
				header.add(m_allBillItems[i]);
			} else {
				body.add(m_allBillItems[i]);
			}
		}
		m_headerBillItems = new DapDefItemVO[0];
		m_headerBillItems = header.toArray(m_headerBillItems);

		m_bodyBillItems = new DapDefItemVO[0];
		m_bodyBillItems = body.toArray(m_bodyBillItems);

		getFieldList().setListData(m_headerBillItems);
		getFieldListBody().setListData(m_bodyBillItems);

	}



	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-5-22 16:16:47)
	 * @param formula java.lang.String
	 */
	public void setFormula(String formula) {
		getFormulaTArea().setText(formula);
	}

	/**
	 * This method initializes matchText	
	 * 	
	 * @return nc.ui.pub.beans.UITextField	
	 */
	private UITextField getMatchText() {
		if (matchText == null) {
			matchText = new UITextField();
			matchText.setBounds(new Rectangle(322, 570, 125, 20));
		}
		return matchText;
	}

	/**
	 * This method initializes btnSearch	
	 * 	
	 * @return nc.ui.pub.beans.UIButton	
	 */
	private UIButton getBtnSearch() {
		if (btnSearch == null) {
			btnSearch = new UIButton();
			btnSearch.setText("定位");
			btnSearch.setBounds(new Rectangle(459, 570, 31, 20));
			btnSearch.setMargin(new java.awt.Insets(2, 4, 2, 4));
			btnSearch.addActionListener(this);
		}
		return btnSearch;
	}
} //  @jve:decl-index=0:visual-constraint="10,10"