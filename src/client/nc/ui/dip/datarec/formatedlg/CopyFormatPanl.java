package nc.ui.dip.datarec.formatedlg;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.DataFormateCopyTreeRefModel;
import nc.ui.bd.ref.IBusiType;
import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.IRefUICreator;
import nc.ui.bd.ref.RefContext;
import nc.ui.bd.ref.RefUIConfig;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.bd.ref.costomize.RefCustomizedDlg;
import nc.ui.pub.beans.RefPaneIconFactory;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.style.Style;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.sm.nodepower.OrgnizeTypeVO;

public class CopyFormatPanl  extends UIPanel implements ChangeListener {

	/**
	 * <code>serialVersionUID</code> ��ע��
	 */
	private static final long serialVersionUID = 1L;

	public static final int REFINPUTTYPE_CODE = 0; // ����

	public static final int REFINPUTTYPE_NAME = 1; // ����

	public static final int REFINPUTTYPE_MNECODE = 2; // ������

	/**
	 * ����������Դ�����������ձ������ĸ�ֵ�����������ݴ�ʹ��
	 */

	private String pk_bdinfo = null;

	/**
	 * ���ս����������Ϣ
	 */
	private RefUIConfig refUIConfig = null;

	private UIButton button = null;

	private CopyUIRefPaneTextField ivjUITextField = null;

	private int fieldRefType = 0;

	private boolean fieldButtonVisible = true;

	private java.lang.String fieldDelStr = new String();

	// private int fieldMaxLength = 100;
	private int fieldMaxLength = Integer.MAX_VALUE;

	private int fieldNumPoint = 0;

	private java.lang.String fieldTextType = "TextStr";

	// �����ڽ��㶪ʧ����ʾ�����ԣ�Ŀǰֻ֧�� ���������
	private boolean fieldReturnCode = false; // ȱʡ��������

	private boolean fieldEnabled = true;

	// ��������
	protected String fieldText = new String();

	private boolean fieldEditable = true;

	private UITable fieldTable = null;

	private java.lang.String fieldRefNodeName = new String();

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	private java.lang.String fieldRefPK = null;

	private java.lang.String fieldRefCode = new String();

	private java.lang.String fieldRefName = new String();

	private java.awt.Color colorBackgroundDefault = null;

	// ������ʾ��(ֻ��ʾһ��)
	private boolean displayedEnabled = true;

	// �Զ�����
	private boolean fieldAutoCheck = true;

	// ����ѡ��Ի���ȷ����ť����ʱ���Ƿ񴥷����Ա仯�¼�
	private boolean fieldButtonFireEvent = false;

	private boolean fieldCacheEnabled = true;

	private java.awt.Color fieldColor = new java.awt.Color(0);

	private java.lang.String fieldMaxDateStr = null;

	private double fieldMaxValue = Double.MAX_VALUE;

	private java.lang.String fieldMinDateStr = null;

	private double fieldMinValue = -Double.MAX_VALUE;

	private boolean fieldMultiSelectedEnabled = false;

	private boolean fieldNotLeafSelectedEnabled = true;

	// �����ڻ�ý������ʾ���������͡�
	private int fieldRefInputType = REFINPUTTYPE_CODE;

	private java.util.Vector fieldSelectedData = new java.util.Vector();

	private boolean fieldTextFieldVisible = true;

	// ��ʧȥ����ǰ���հ�ť�������
	protected boolean isButtonClicked = false;

	// �ַ������·�
	protected boolean isKeyPressed = false;

	// �뿪����,��ʧ�����?
	protected boolean isLostFocus = true;

	// �Ƿ�๫˾ѡ�����
	private boolean isMultiCorpRef = false;

	// ��˾����
	private String pk_corp = null;

	// ��������Ƿ���������Զ�ѡ��
	private boolean isTreeGridNodeMultiSelected = false;

	// private nc.ui.bd.ref.AbstractRefModel m_refModel;
	protected int m_iReturnButtonCode = -1;

	private boolean m_isCustomDefined = false;

	// /////////////
	//
	public UFRefManage m_refManage = null;

	private java.awt.Container parentContainer = null;

	private boolean processFocusLost = true;

	// 2004-5-23 ��������
	// ���β���,�Ƿ�����¼��Ƿ���ʾ,�����Ի�Ӱ�����model��ȡ������Ҳ��һ��model������
	private boolean isIncludeSubShow = false;

	// sxj 2004-06-18
	// �汾��ť�Ƿ���ʾ����
	private boolean isVersionButtonShow = false;

	// �߼����˶Ի����Ƿ���ʾ����
	private boolean isFilerDlgShow = false;

	// �Ƿ���ȡ���ݿ���
	private boolean isBatchData = true;

	private boolean hasButtonAction = false;

	// ģ��ƥ��ģʽ,Ĭ������λ��ƥ��
	private int matchMode = 0; // UFRefManage����

	// ���⴦��Ĳ���
	private HashMap specialRef_hm = new HashMap();

	// �Ƿ������ݰ�ť��ʾ����
	// 2006-05-9 add
	private boolean isSealedDataButtonShow = false;

	private String[] refNodeNames = new String[] { "", "����", "�ļ�", "��ɫ", "������",
			"�ı���", "���ն���" };

	private String multiInputToken = ",";

	/**
	 * ���յĽ����࣬��֧����ɫ����������������
	 */
	private Object refUI = null;

	//
	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == CopyFormatPanl.this.getUIButton())
				connEtoC1();
		};
	};

	/**
	 * UIRefPane ������ע��.
	 */
	public CopyFormatPanl() {
		super();
		initialize();
	}

	/**
	 * 
	 * ��������:(2002-6-12 11:32:54)
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public CopyFormatPanl(java.awt.Container parent) {
		super();
		parentContainer = parent;
		initialize();
	}

	/**
	 * UIRefPane ������ע��.
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 */
	public CopyFormatPanl(java.awt.LayoutManager p0) {
		super(p0);
		initialize();
	}

	/**
	 * UIRefPane ������ע��.
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 * @param p1
	 *            boolean
	 */
	public CopyFormatPanl(java.awt.LayoutManager p0, boolean p1) {
		super(p0, p1);
		initialize();
	}

	/**
	 * UIRefPane ������ע��.
	 * 
	 * @param p0
	 *            boolean
	 */
	public CopyFormatPanl(boolean p0) {
		super(p0);
		initialize();
	}

	public CopyFormatPanl(String refNodeName) {
		fieldRefNodeName = refNodeName;
		initialize();
	}

	/**
	 * ��� nc.ui.pub.beans.ValueChangedListener.
	 */
	public void addValueChangedListener(ValueChangedListener aListener) {
		listenerList.add(ValueChangedListener.class, aListener);
	}

	/**
	 * 
	 */
	private void callCalculator() {
		nc.ui.pub.beans.calculator.UICalculator calculator;
		String input = fieldText;
		if (input.equals("")) {
			calculator = new nc.ui.pub.beans.calculator.UICalculator(this);
		} else {
			calculator = new nc.ui.pub.beans.calculator.UICalculator(this,
					Double.valueOf(input).doubleValue());
		}
		m_iReturnButtonCode = calculator.showModal();
		if (calculator.processCheck()) {
			isKeyPressed = false;
			isButtonClicked = true;
			double result = calculator.getResultData();
			setProcessFocusLost(false);
			setText(new nc.vo.pub.lang.UFDouble(result).toString());

		}
		calculator.destroy();
	}

	/**
	 * 
	 */
	private void callCalendar() {

		nc.ui.pub.beans.calendar.UICalendar calendar = new nc.ui.pub.beans.calendar.UICalendar(
				this, fieldMinDateStr, fieldMaxDateStr);
		try {
			nc.vo.pub.lang.UFDate date = new nc.vo.pub.lang.UFDate(
					getUITextField().getText());
			calendar.setNewdate(date);
		} catch (Exception e) {

		}
		m_iReturnButtonCode = calendar.showModal();
		if (getReturnButtonCode() == UIDialog.ID_OK) {
			isKeyPressed = false;
			isButtonClicked = true;
			setProcessFocusLost(false);
			setText(calendar.getCalendarString());
		}
		calendar.destroy();
	}

	/**
	 * ���տͻ���ѡ�����
	 * 
	 */
	private void callRefCostomizedDefine() {

		RefCustomizedDlg dlg = new RefCustomizedDlg(this);

		dlg.setText(getUITextField().getText());
		m_iReturnButtonCode = dlg.showModal();
		if (getReturnButtonCode() == UIDialog.ID_OK) {
			isKeyPressed = false;
			isButtonClicked = true;
			setProcessFocusLost(false);
			setText(dlg.getResultStr());
		}
		dlg.destroy();
	}

	/**
	 * 
	 */
	private void callColor() {
		java.awt.Container m_parentTemp = null;
		if (parentContainer == null)
			if (this.getParent() != null)
				parentContainer = this.getParent();
			else
				parentContainer = this;
		if (parentContainer == null)
			m_parentTemp = getParent();
		else
			m_parentTemp = parentContainer;
		while (m_parentTemp.getParent() != null) {
			if (m_parentTemp instanceof javax.swing.JApplet
					|| m_parentTemp instanceof javax.swing.JDialog)
				break;
			m_parentTemp = m_parentTemp.getParent();
		} //
		java.awt.Color color = nc.ui.pub.beans.UIColorChooser.showDialog(
				m_parentTemp, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"_Beans", "UPP_Beans-000086")/* @res "��ɫ��" */,
				getUITextField().getBackground());
		if (color != null) {
			isKeyPressed = false;
			isButtonClicked = true;
			setProcessFocusLost(false);
			setColor(color);
		}
	}

	/**
	 * 
	 */
	private void callFile() {
		java.awt.Container m_parentTemp = null;
		if (parentContainer == null)
			if (this.getParent() != null)
				parentContainer = this.getParent();
			else
				parentContainer = this;
		if (parentContainer == null)
			m_parentTemp = getParent();
		else
			m_parentTemp = parentContainer;
		while (m_parentTemp.getParent() != null) {
			if (m_parentTemp instanceof javax.swing.JApplet
					|| m_parentTemp instanceof javax.swing.JDialog)
				break;
			m_parentTemp = m_parentTemp.getParent();
		} //
		nc.ui.pub.beans.UIFileChooser chooser = (UIFileChooser) getRefUI();

		if (chooser == null) {
			return;
		}
		if (fieldText != null && fieldText.trim().length() > 0) {
			chooser.setCurrentDirectory(FileSystemView.getFileSystemView()
					.createFileObject(fieldText));
		}

		if (getRefType() == nc.ui.pub.beans.UIFileChooser.FILES_ONLY)
			chooser
					.setFileSelectionMode(nc.ui.pub.beans.UIFileChooser.FILES_ONLY);
		else if (getRefType() == nc.ui.pub.beans.UIFileChooser.DIRECTORIES_ONLY)
			chooser
					.setFileSelectionMode(nc.ui.pub.beans.UIFileChooser.DIRECTORIES_ONLY);
		else
			chooser
					.setFileSelectionMode(nc.ui.pub.beans.UIFileChooser.FILES_AND_DIRECTORIES);
		chooser.showOpenDialog(m_parentTemp);
		try {
			if (chooser.getSelectedFile() != null) {
				isKeyPressed = false;
				isButtonClicked = true;
				setProcessFocusLost(false);
				getUITextField().setMaxLength(1024);

				if (isReturnCode())
					setText(chooser.getSelectedFile().getName());

				else
					setText(chooser.getSelectedFile().getCanonicalPath());
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * connEtoC1: (UIButton.action.actionPerformed(java.awt.event.ActionEvent)
	 * --> UIRefPane.onButtonClicked()V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* ����:�˷�������������. */
	public void connEtoC1() {
		try {
			// user code begin {1}
			if (displayedEnabled) {
				displayedEnabled = false;
				this.onButtonClicked();
			}

		} catch (java.lang.Throwable ivjExc) {

			handleException(ivjExc);
		}
		isLostFocus = true;
		displayedEnabled = true;
	}
	public  boolean vequals(Vector v1, Vector v2) {
		if (v1 != null && v1.size() == 0)
			v1 = null;
		if (v2 != null && v2.size() == 0)
			v2 = null;
		if (v1 == null && v2 == null)
			return true;
		if ((v1 == null && v2 != null) || (v1 != null && v2 == null))
			return false;
		if (v1.size() != v2.size())
			return false;
		boolean in = false;
		for (int i = 0; i < v1.size(); i++) {
			Vector vTemp1 = (Vector) v1.elementAt(i);
			in = false;
			for (int j = 0; j < v2.size(); j++) {
				Vector vTemp2 = (Vector) v2.elementAt(j);
				if (vTemp1 == null && vTemp2 == null || vTemp1 != null
						&& vTemp1.equals(vTemp2)) {
					in = true;
					break;
				}
			}
			if (!in)
				break;
		}
		return in;
	}
	/**
	 * ��ɫ���յ����Ա仯�¼����⴦���̶���ѡ����ɫ��ȷ�ϼ��󴥷��� ��������:(2003-11-27 13:10:03)
	 * 
	 * �������Ա仯�¼�,���۲���ѡ��Ի���ȷ����ť�����¼������Ƿ��
	 * 1������򿪣��ڲ���ѡ��Ի���ȷ����ť���º����Ա仯�¼��ѽ��������˴�����Ҳ���ᴥ���� 2�����ͨ�����������ѡ��������ݣ��������Ա仯�¼���
	 * 
	 */
	private void doFireValueChanged() {

		String sText = getUITextField().getText();

		boolean isValueChanged = false;

		// ���ж��ı��Ƿ����仯��
		if (!sequals(sText, fieldText)) {
			isValueChanged = true;
		}
		// ���ж�model�Ƿ�仯�� ֻҪ��һ��ֵ�仯���ʹ��������¼���
		if ((getRefModel() != null)
				&& !vequals(getRefModel().getSelectedData(),
						getSelectedData())) {
			isValueChanged = true;
		}

		if (isValueChanged) {

			fieldText = sText;
			if (getRefModel() == null) {
				setSelectedData(null);
			} else {

				if (getRefModel().getSelectedData() == null) {
					setSelectedData(null);
				}

				else {
					setSelectedData((Vector) getRefModel().getSelectedData()
							.clone());
				}
			}
			fireValueChanged(new ValueChangedEvent(
					getUITextField().getParent(), sText));
		}

	}
	public  boolean isIncludeBlurChar(String value) {
		boolean include = false;
		if (value != null
				&& ((value.indexOf('*') >= 0 || value.indexOf('%') >= 0 || value
						.indexOf('?') >= 0))) {
			include = true;
		}
		return include;

	}
	public boolean sequals(String s1, String s2) {
		boolean isEqual = false;
		if (s1 != null && s1.trim().length() == 0)
			s1 = null;
		if (s2 != null && s2.trim().length() == 0)
			s2 = null;
		//
		if (s1 == null && s2 == null) {
			isEqual = true;
		} else if ((s1 == null && s2 != null) || (s1 != null && s2 == null)) {
			isEqual = false;
		} else {

			isEqual = s1.equals(s2);
		}
		return isEqual;
	}


	/**
	 * 
	 * ��������:(2003-11-27 13:10:03)
	 */
	private void doKeyPressed() {

		String sText = getUITextField().getText().trim();
		String sOriginText = sText;

		if (isCalendar()) {
			// �����ɺϷ�����
			sText = nc.vo.pub.lang.UFDate.getValidUFDateString(sText);
			getUITextField().setText(sText);

		} else {
			// �Զ�ƥ������
			if (isButtonVisible()) {
				setBlurValue(sText, false);
			}

			// ������Ϊ�����Ĵ���
			if (getRefCodes() != null && getRefCodes().length > 1
					&& (!isIncludeBlurChar(sOriginText))
					&& !isMultiInput(sText)) {   //���Ƕ�������ʱ���Żᵯ�����տ�
				m_refManage.setIsMneCodes(true);
				getRefModel().setBlurValue(sText);
				// Model����Ĭ��������ѡ����Ϊƥ�������;������ѡ����Ϊ��.
				if (getRefModel() != null) {
					getRefModel().setSelectedData(null);
				}
				m_iReturnButtonCode = getRef().showModal();
				if (getReturnButtonCode() == UFRefManage.ID_OK) {
					doReturnOk();

				}
			}

			if (!isReturnCode()) {
				processFocusLost(null);
			}
			// �Զ�ƥ��Ľ��
			sText = getRefCode();

		}

		// ������Զ�ƥ�䣬��û��ƥ���ϣ���ʾ�û������ֵ
		if (!isAutoCheck() && (sText == null || sText.length() == 0)) {
			sText = sOriginText;
			getUITextField().setText(sText);
		}

		// ���⴦��
		// ���Ͳ���,��������¼���ѡ����ʾ,��codeƥ����Ҫ���¼�����Ϣ���浽��ʱ��.
		// if (isAutoCheck()) {
		// ���β������
		// isIncludeSubShow=true,Ĭ���ǰ����¼���.���Ե�codeƥ���¼ʱ,Ҫ�����Code���¼�(���浽��ʱ��)
		dealWithTreeModel();

		setToolTipText();
		// }

	}

	private void dealWithTreeModel() {
		if (isIncludeSubShow() && getRef().getRefType() == IBusiType.TREE) {
			AbstractRefTreeModel treeModel = (AbstractRefTreeModel) getRefModel();
			if (treeModel != null) {
				treeModel.setIncludeSub(isIncludeSubShow());
				treeModel.saveTempData(treeModel.getSubPks(getRefPK()));
				treeModel.setSelectedData(treeModel.getSubRecords(getRefPK()));
			}
		}
	}

	private boolean isCalendar() {
		return getRefNodeName().equals("����");
	}

	/**
	 * ����֧�ּ������¼��ķ���.
	 */
	protected void fireValueChanged(ValueChangedEvent event) {
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == nc.ui.pub.beans.ValueChangedListener.class) {

				((nc.ui.pub.beans.ValueChangedListener) listeners[i + 1])
						.valueChanged(event);
			}
		}

	}

	/**
	 * ��ȡ color ���� (java.awt.Color) ֵ.
	 * 
	 * @return color ����ֵ.
	 * @see #setColor
	 */
	public java.awt.Color getColor() {
		return fieldColor;
	}

	/**
	 * ��ȡ delStr ���� (java.lang.String) ֵ.
	 * 
	 * @return delStr ����ֵ.
	 * @see #setDelStr
	 */
	public java.lang.String getDelStr() {
		return fieldDelStr;
	}

	/**
	 * ��ȡ maxDateStr ���� (java.lang.String) ֵ.
	 * 
	 * @return maxDateStr ����ֵ.
	 * @see #setMaxDateStr
	 */
	public java.lang.String getMaxDateStr() {
		return fieldMaxDateStr;
	}

	/**
	 * ��ȡ maxlength ���� (int) ֵ.
	 * 
	 * @return maxlength ����ֵ.
	 * @see #setMaxlength
	 */
	public int getMaxLength() {
		return fieldMaxLength;
	}

	/**
	 * ��ȡ maxValue ���� (double) ֵ.
	 * 
	 * @return maxValue ����ֵ.
	 * @see #setMaxValue
	 */
	public double getMaxValue() {
		return fieldMaxValue;
	}

	/**
	 * ��ȡ minDateStr ���� (java.lang.String) ֵ.
	 * 
	 * @return minDateStr ����ֵ.
	 * @see #setMinDateStr
	 */
	public java.lang.String getMinDateStr() {
		return fieldMinDateStr;
	}

	/**
	 * ��ȡ minValue ���� (double) ֵ.
	 * 
	 * @return minValue ����ֵ.
	 * @see #setMinValue
	 */
	public double getMinValue() {
		return fieldMinValue;
	}

	/**
	 * ��ȡ numPoint ���� (int) ֵ.
	 * 
	 * @return numPoint ����ֵ.
	 * @see #setNumPoint
	 */
	public int getNumPoint() {
		return fieldNumPoint;
	}

	/**
	 * 
	 * ��������:(2003-4-11 10:15:37)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_corp() {
		if (getRefModel() != null)
			return getRefModel().getPk_corp();
		return pk_corp;
	}

	/**
	 * 
	 * ��������:(01-4-12 22:04:34)
	 * 
	 * @return nc.ui.bd.ref.UFRef
	 */
	public nc.ui.bd.ref.UFRefManage getRef() {

		if (m_refManage == null) {
			if (parentContainer == null)
				m_refManage = new UFRefManage((java.awt.Container) this,
						getRefNodeName(), getRefType(), isCustomDefined());
			else
				m_refManage = new UFRefManage(
						(java.awt.Container) parentContainer, getRefNodeName(),
						getRefType(), isCustomDefined());

		}

		return m_refManage;
	}

	/**
	 * ���� Bddata[] ,�����������ݵľ�����Ϣ. sxj 2004-07-27 add
	 */
	public nc.vo.bd.access.BddataVO[] getRefBDdats() {
		if (getRefModel() == null) {
			return null;
		}
		String[] pks = getRefModel().getPkValues();
		nc.vo.bd.access.BddataVO[] vos = null;
		if (pks != null) {
			nc.vo.bd.access.BdinfoVO bdinfovo = getRefModel().getBdinfoVO();

			if (bdinfovo != null) {
				vos = new nc.vo.bd.access.BddataVO[pks.length];
				String pk_org = null;
				if (OrgnizeTypeVO.COMPANY_TYPE.equals(getRefModel()
						.getOrgTypeCode())) {
					pk_org = getRefModel().getPk_corp();
				} else if (OrgnizeTypeVO.ZHUZHANG_TYPE.equals(getRefModel()
						.getOrgTypeCode())) {
					pk_org = getRefModel().getPk_GlOrgBook();
				}
				nc.vo.bd.access.IBDAccessor accessor = nc.vo.bd.access.AccessorManager
						.getAccessor(bdinfovo.getPk_bdinfo(), getRefModel()
								.getOrgTypeCode(), pk_org);

				for (int i = 0; i < vos.length; i++) {
					vos[i] = accessor.getDocByPk(pks[i]);
				}
			}
		}
		return vos;
	}

	/**
	 * ��ȡ refCode ���� (java.lang.String) ֵ.
	 * 
	 * @return refCode ����ֵ.
	 * @see #setRefCode
	 */
	public java.lang.String getRefCode() {

		return getRefCodes() == null ? null : getRefCodes()[0];

	}

	/**
	 * ��ȡ refCode ���� (java.lang.String) ֵ.
	 */
	public java.lang.String[] getRefCodes() {
		String[] fieldCodes = null;
		if (getRefModel() == null || isSpecialRef()) {
			fieldCodes = new String[] { getText() };

		} else {
			fieldCodes = getRefModel().getRefCodeValues();
		}
		return fieldCodes;
	}

	/**
	 * ��ȡ refInputType ���� (int) ֵ.
	 * 
	 * @return refInputType ����ֵ.
	 * @see #setRefInputType
	 * 
	 */
	public int getRefInputType() {
		return fieldRefInputType;
	}

	/**
	 * 
	 * ��������:(2001-8-25 10:47:18)
	 * 
	 * @return nc.ui.bd.ref.AbstractRefModel
	 */
	public nc.ui.bd.ref.AbstractRefModel getRefModel() {
		// ����Ĳ��գ������ȣ�û��model,�����Ϊ�ı������ã�Ҳû��model;
		if (isSpecialRef()) {
			return null;
		}
		AbstractRefModel model = getRef().getRefModel();
		if (model != null) {
			model.addChangeListener(this);
		}
		return model;
	}

	/**
	 * ��ȡ refName ���� (java.lang.String) ֵ.
	 * 
	 * @return refName ����ֵ.
	 * @see #setRefName
	 */
	public java.lang.String getRefName() {
		if (getRefModel() == null || isSpecialRef()) {
			fieldRefName = getText();
		} else
			fieldRefName = getRefModel().getRefNameValue();
		return fieldRefName;
	}

	/**
	 * ��ȡ refName ���� (java.lang.String) ֵ.
	 * 
	 * @return refName ����ֵ.
	 * @see #setRefName
	 */
	public java.lang.String[] getRefNames() {
		if (getRefModel() == null)
			return null;
		return getRefModel().getRefNameValues();
	}

	/**
	 * ��ȡ refNodeName ���� (java.lang.String) ֵ.
	 * 
	 * @return refNodeName ����ֵ.
	 * @see #setRefNodeName
	 */
	public java.lang.String getRefNodeName() {
		if (fieldRefNodeName == null)
			fieldRefNodeName = "";
		return fieldRefNodeName;
	}

	/**
	 * ��ȡ refPK ���� (java.lang.String) ֵ.
	 * 
	 * @return refPK ����ֵ.
	 * @see #setRefPK
	 */
	public java.lang.String getRefPK() {
		if (getRefModel() == null || isSpecialRef()) {
			fieldRefPK = getText();
		} else
			fieldRefPK = getRefModel().getPkValue();
		return fieldRefPK;
	}

	/**
	 * ��ȡ refPK ���� (java.lang.String) ֵ.
	 * 
	 * @return refPK ����ֵ.
	 * @see #setRefPK
	 */
	public java.lang.String[] getRefPKs() {
		if (getRefModel() == null)
			return null;
		return getRefModel().getPkValues();
	}

	public java.lang.String getRefTempDataWherePart() {
		if (getRefModel() == null)
			return null;
		return getRefModel().getTempDataWherePart();
	}

	/**
	 * ��ȡ refType ���� (int) ֵ.
	 * 
	 * @return refType ����ֵ.
	 * @see #setRefType
	 */
	public int getRefType() {
		return fieldRefType;
	}

	public Object getRefValue(String dbColumnName) {
		if (getRefModel() == null)
			return null;
		return getRef().getRefModel().getValue(dbColumnName);
	}

	public Object getRefValues(String dbColumnName) {
		if (getRefModel() == null)
			return null;
		return getRef().getRefModel().getValues(dbColumnName);
	}

	/**
	 * 
	 * ��������:(2001-9-22 17:33:23)
	 * 
	 * @return int
	 */
	public int getReturnButtonCode() {
		return m_iReturnButtonCode;
	}

	/**
	 * ��ȡ selectedData ���� (java.util.Vector) ֵ.
	 * 
	 * @return selectedData ����ֵ.
	 * @see #setSelectedData
	 */
	public java.util.Vector getSelectedData() {
		return fieldSelectedData;
	}

	/**
	 * ��ȡ table ���� (nc.ui.pub.beans.UITable) ֵ.
	 * 
	 * @return table ����ֵ.
	 * @see #setTable
	 */
	public UITable getTable() {
		return fieldTable;
	}

	/**
	 * 
	 * ��������:(2001-3-14 18:11:23)
	 * 
	 * @return java.lang.String
	 */
	public String getText() {
		return getUITextField().getText();
	}

	/**
	 * ��ȡ textType ���� (java.lang.String) ֵ.
	 * 
	 * @return textType ����ֵ.
	 * @see #setTextType
	 */
	public java.lang.String getTextType() {
		return fieldTextType;
	}

	/**
	 * @deprecated
	 */
	public java.lang.String getToolTipText() {
		return getText();
	}

	private void setToolTipText() {
		String text = getText();
		if (text != null && text.trim().length() == 0) {
			text = null;
		}
		getUIButton().setToolTipText(text);
		getUITextField().setToolTipText(text);

	}

	/**
	 * ���� UIButton1 ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	public UIButton getUIButton() {
		if (button == null) {

			button = new nc.ui.pub.beans.UIButton() {
				protected void processMouseEvent(java.awt.event.MouseEvent e) {
					if (e.getID() == java.awt.event.MouseEvent.MOUSE_PRESSED) {
						isLostFocus = false;
					}

					super.processMouseEvent(e);
					// setButtonBorderAndBackGround();

				}

				public boolean isFocusTraversable() {
					return false;
				}

				public void setEnabled(boolean bEnabled) {
					super.setEnabled(bEnabled);
					setBorder(null);
					setRefModelEnable(bEnabled);
					// setButtonBorderAndBackGround();

				}

				public void mouseClicked(MouseEvent mouseEvent) {
				}

				// ������ʱ,��������
				public void mouseEntered(MouseEvent mouseEvent) {

				}

				public void mouseExited(MouseEvent mouseEvent) {

				}

				public void mousePressed(MouseEvent mouseEvent) {

				}

				public void mouseReleased(MouseEvent mouseEvent) {

				}

			};

			button.setName("UIButton");
			// button.setIButtonType(0 /** JavaĬ��(�Զ���) */
			// );
			button.setText("");

			// button.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// button.setToolTipText(fieldToolTipText);
			// button.setToolTipText(getText());
			// button.setPreferredSize(new java.awt.Dimension(18, 18));
			// setButtonBorderAndBackGround();
			// button.setBackground(Color.WHITE);
			button.setBorder(null);
			button.setBackground(Color.WHITE);
			setButtonIcon();

		}
		return button;
	}

	// private void setButtonBorderAndBackGround() {
	// getUIButton().setBackground(getBackGroudColor());
	// getUIButton().setBorder(
	// BorderFactory.createLineBorder(getBackGroudColor()));
	// }

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-6-9</strong>
	 * <p>
	 * 
	 * @param
	 * @return Color
	 * @exception BusinessException
	 * @since NC5.0
	 */
	public Color getBackGroudColor() {
		Color color = null;
		if (isEnabled()) {
			color = Color.WHITE;
		} else {
			color = new Color(0XC4C4C4);
		}
		return color;
	}

	/**
	 * ���� UITextField ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* ����:�˷�������������. */
	public CopyUIRefPaneTextField getUITextField() {
		if (ivjUITextField == null) {
			try {

				CopyUIRefPaneTextField ui1 = new CopyUIRefPaneTextField();
				ui1.setUIRefPane(this);
				ivjUITextField = ui1;
				ivjUITextField.setName("UITextField");
				colorBackgroundDefault = ivjUITextField.getBackground();
				ivjUITextField.setTextType(fieldTextType);
				ivjUITextField.setMaxLength(fieldMaxLength);
				ivjUITextField.setNumPoint(fieldNumPoint);
				ivjUITextField.setAutoscrolls(true);
				ivjUITextField.setEditable(fieldEditable);
				ivjUITextField.setMaxValue(getMaxValue());
				ivjUITextField.setMinValue(getMinValue());
				ivjUITextField.setPreferredSize(new Dimension(120, 18));
				ivjUITextField.setBorder(null);

			} catch (java.lang.Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUITextField;
	}

	/**
	 * 
	 * ��������:(2001-3-14 19:33:54)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public nc.vo.pub.ValueObject getVO() {
		if (getRefModel() != null)
			return getRefModel().getVO();
		else
			return null;
	}

	/**
	 * 
	 * ��������:(2001-8-16 13:34:37)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public nc.vo.pub.ValueObject[] getVOs() {
		if (getRefModel() != null)
			return getRefModel().getVOs();
		else
			return null;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		/* ��ȥ���и��е�ע��,�Խ�δ��׽�����쳣��ӡ�� stdout. */
		System.out.println("--------- δ��׽�����쳣 ---------");
		exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵��.
	 */
	/* ����:�˷�������������. */
	private void initConnections() {

		getUIButton().addActionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ����.
	 */
	/* ����:�˷�������������. */
	private void initialize() {

		setName("UIRefPane");
		setPreferredSize(new java.awt.Dimension(122, 20));
		setRequestFocusEnabled(false);
		// setSize(100, 20);
		setLayout(new nc.ui.pub.beans.UIRefLayout());
		add(getUITextField(), getUITextField().getName());
		if (!getRefNodeName().equals("�ı���")) {
			add(getUIButton(), getUIButton().getName());
		}

		displayedEnabled = true;
		initConnections();

		pk_corp = RefContext.getInstance().getPk_corp();

		// "����","�ļ�","��ɫ","������"
		initSpecialRef_hm();
		// setBorder(Style.getBorder("TextField.border"));
		setBorder(BorderFactory.createLineBorder(Style.NCborderColor));

	}

	/**
	 * ��ȡ autoCheck ���� (boolean) ֵ.
	 * 
	 * @return autoCheck ����ֵ.
	 * @see #setAutoCheck
	 */
	public boolean isAutoCheck() {
		return fieldAutoCheck;
	}

	/**
	 * 
	 * ��������:(2004-6-25 15:47:14)
	 * 
	 * @return boolean
	 */
	public boolean isBatchData() {
		return isBatchData;
	}

	/**
	 * ��������ֵ�仯�¼���ʱ���Ŀ��أ� true: �ڲ��նԻ���ȷ�ϰ�ť���º� false:�ڲ���������뿪�����
	 * 
	 * @return buttonFireEvent ����ֵ.
	 * @see #setButtonFireEvent
	 */
	public boolean isButtonFireEvent() {
		return fieldButtonFireEvent;
	}

	/**
	 * ��ȡ buttonVisible ���� (boolean) ֵ.
	 * 
	 * @return buttonVisible ����ֵ.
	 * @see #setButtonVisible
	 */
	public boolean isButtonVisible() {
		return fieldButtonVisible;
	}

	/**
	 * ��ȡ cacheEnabled ���� (boolean) ֵ.
	 * 
	 * @return cacheEnabled ����ֵ.
	 * @see #setCacheEnabled
	 */
	public boolean isCacheEnabled() {
		return fieldCacheEnabled;
	}

	/**
	 * �Ƿ�Ϊ�Զ������ ��������:(2001-9-3 15:58:53)
	 * 
	 * @since :V1.00
	 * @return boolean
	 */
	public boolean isCustomDefined() {
		return m_isCustomDefined;
	}

	/**
	 * ��ȡ editable ���� (boolean) ֵ.
	 * 
	 * @return editable ����ֵ.
	 * @see #setEditable
	 */
	public boolean isEditable() {
		return fieldEditable;
	}

	/**
	 * 
	 * ��������:(2001-3-25 11:45:58)
	 * 
	 * @return boolean
	 */
	public boolean isEnabled() {
		return fieldEnabled;
	}

	/**
	 * 
	 * ��������:(2004-6-18 15:44:06)
	 * 
	 * @return boolean
	 */
	public boolean isFilerDlgShow() {
		return isFilerDlgShow;
	}

	// �Ƿ�����¼�ѡ��
	public boolean isIncludeSub() {
		if (getRefModel() == null)
			return false;
		return getRefModel().isIncludeSub();
	}

	/**
	 * 
	 * ��������:(2004-5-23 11:20:46)
	 * 
	 * @return boolean ��ʾ? --> �Ƿ�����¼��ؼ�
	 */
	public boolean isIncludeSubShow() {
		return isIncludeSubShow;
	}

	/**
	 * 
	 */
	public boolean isMultiCorpRef() {
		return isMultiCorpRef;
	}

	/**
	 * ��ȡ multiSelectedEnabled ���� (boolean) ֵ.
	 * 
	 * @return multiSelectedEnabled ����ֵ.
	 * @see #setMultiSelectedEnabled
	 */
	public boolean isMultiSelectedEnabled() {
		return fieldMultiSelectedEnabled;
	}

	/**
	 * ��ȡ notLeafSelectedEnabled ���� (boolean) ֵ.
	 * 
	 * @return notLeafSelectedEnabled ����ֵ.
	 * @see #setNotLeafSelectedEnabled
	 */
	public boolean isNotLeafSelectedEnabled() {
		return fieldNotLeafSelectedEnabled;
	}

	/**
	 * 
	 * ��������:(2001-11-27 13:30:13)
	 * 
	 * @return boolean
	 */
	public boolean isProcessFocusLost() {
		return processFocusLost;
	}

	/**
	 * ��ȡ returnCode ���� (boolean) ֵ.
	 * 
	 * @return returnCode ����ֵ.
	 * @see #setReturnCode
	 */
	public boolean isReturnCode() {
		return fieldReturnCode;
	}

	/**
	 * ��ȡ textFieldVisible ���� (boolean) ֵ.
	 * 
	 * @return textFieldVisible ����ֵ.
	 * @see #setTextFieldVisible
	 */
	public boolean isTextFieldVisible() {
		return fieldTextFieldVisible;
	}

	/**
	 * 
	 * ��������:(2003-8-18 11:14:39)
	 * 
	 * @return boolean
	 */
	public boolean isTreeGridNodeMultiSelected() {
		return isTreeGridNodeMultiSelected;
	}

	/**
	 * 
	 * ��������:(2004-6-18 15:44:06)
	 * 
	 * @return boolean
	 */
	public boolean isVersionButtonShow() {
		return isVersionButtonShow;
	}

	/**
	 * ע��:���Ǵ˷���ʱ,Ӧ��ȷָ��������,������this.getParent()
	 */
	public void onButtonClicked() {
		DataformatPanel pan=(DataformatPanel) parentContainer;
		int index=pan.getJComboBox1().getSelectedIndex();
		System.out.println("------------ѡ�е�I��"+index);
		DataFormateCopyTreeRefModel refmod=(DataFormateCopyTreeRefModel) getRefModel();
//		refmod.setWherePart("nvl(h.dr,0)=0 and nvl(f.dr,0)=0 and h.pk_xt='"+pan.getHvo().getPk_xt()+"' and h.pk_datarec_h<>'"+pan.getHvo().getPrimaryKey()+"' and f.messflowtype='"+pan.getIpks()[index]+"'");
//		refmod.setWherePart("nvl(dr,0)=0  and h.pk_datarec_h <>'"+pan.getHvo().getPrimaryKey()+"' and f.messflowtype='"+pan.getIpks()[index]+"'");
		refmod.getWherePart();
		refmod.setWherePart(refmod.getWherePart()+"  and dip_dataformatdef_h.messflowtype='"+pan.getIpks()[index]+"'");
		setRefModel(refmod);
		UITextField tf = getUITextField();
		if (!tf.isFocusOwner() && this.isVisible() && this.getParent() != null
				&& tf.isFocusable() && tf.isVisible()
				&& getUIButton().isVisible()) {
			if (!hasButtonAction) {
				tf.requestFocus();
				hasButtonAction = true;
				return;
			}
		}
		hasButtonAction = false;

		// beforeButtonClicked();
		try {
			if (m_refManage != null) {
				m_refManage.setIsMneCodes(false);
			}
			if (isSpecialRef()) {
				if (!isCustomDefined()
						&& getRefNodeName().equalsIgnoreCase("����")) {
					callCalendar();
				} else if (getRefNodeName().equalsIgnoreCase("������")) {
					callCalculator();
				} else if (getRefNodeName().equalsIgnoreCase("��ɫ")) { //
					callColor();
				} else if (getRefNodeName().equalsIgnoreCase("�ļ�")) { //
					callFile();
				} else if (getRefNodeName().equalsIgnoreCase("���ն���")) {
					callRefCostomizedDefine();
				}
			} else {
				// ҵ�����
				// ��������
				String blur = getUITextField().getText();
				// ����ת��
				// �����ж�����,�����ڵ���˳�ʱ���TextField��ֵ
				if (getRefModel() == null) {
					System.out
							.println("UIRefPane.getModle()==null, ���ܵ�ԭ��Ϊ����û����ȷ��ʼ����������ն��塣���յ�����Ϊ"
									+ getRefNodeName());
					return;
				}
				// ����ui�����������
				setUIConfig();
				setRefModelConfig();
				if (isKeyPressed) {
					getRefModel().setBlurValue(blur);
					// �������,ϵͳ�����Զ�����ģ��ƥ�� 2004-01-13����
					getRef().setAutoBlurMatch(isKeyPressed);
				} else {
					getRefModel().setBlurValue(null); // û�а�������Ϊ������ģ��ƥ��
				}
				// ��ʾ
				m_iReturnButtonCode = getRef().showModal();
				if (getReturnButtonCode() == UFRefManage.ID_OK) {
					doReturnOk();
					((DataformatPanel)parentContainer).doCopy(getRefPK());
				}

			}
			/*
			 * �������Ա仯�¼�
			 */
			if (isButtonFireEvent()) {
				doFireValueChanged();
			}
		} catch (Exception e) {
			handleException(e);
		}

		return;
	}

	/**
	 * ���ս����ú󴥷��¼�---��������ʾת��,�ɸ��Ǵ˷���. ��������:(2001-5-10 12:35:29)
	 * 
	 * @param e
	 *            java.awt.event.FocusEvent
	 */
	protected void processFocusGained(java.awt.event.FocusEvent e1) {
		// ����disenble,����ת��
		if (!isEditable()) {
			return;
		}

		// ������գ���ת������
		if (isSpecialRef()) {
			return;
		}
		// ����ϵͳ��ƾ֤��,������������¼���ֵ,�����ڽ����˲���ѯʱ,�������ѯ����¼���Ӧ�����������:1)¼��null:��ʾ�Ը�����ֵΪ��ֵ�����ݽ��в�ѯͳ��,2)�������κβ�ѯ����ֵ(����ѯ����¼���Ϊ��):��ʾ��ѯ���и����������.���ڵ�һ������޷�ʵ��,��Ҫ���������˵��õ��ǿͻ����Ļ������ݵĲ���,�ڽ��м��ʱ����ʶ��null��ֵ,ϣ���ͻ����Ĳ���Ϊ�����ṩһ������ʽ,�ܹ�����������.
		String text = getUITextField().getText();
		if (text != null && text.equalsIgnoreCase(IRefConst.NULL)) {
			getUITextField().setText(text);
			fieldText = text;
			return;
		}
		// �޸�,������Զ�ƥ��,Ӧ�ò��ж�getRefCode()��ֵ 2004-05-13 ����
		if (!isAutoCheck())
			return;

		String[] showStrs = null;
		switch (getRefInputType()) {
		case REFINPUTTYPE_CODE: // �������
			showStrs = getRefCodes();
			break;
		case REFINPUTTYPE_NAME: // ���Ʋ���
			showStrs = getRefNames();
			break;
		case REFINPUTTYPE_MNECODE: // ���������
			break;
		case 3: // ����
		default: // ������
		}
		setTextFieldShowStrs(getComposedString(showStrs));
		return;
	}

	/**
	 * 
	 * ��������:(2001-11-27 13:10:03)
	 */
	public void processFocusLost() {

		String sText = getUITextField().getText();

		// ���ʱ���⴦��
		if (isKeyPressed && (isNull(sText))) {

			clearText(sText);
			// ����״̬
			setfocusLostVariableState();
			return;
		}

		if (!isSpecialRef()) {

			if (isKeyPressed) {

				doKeyPressed();

			} else {

				// û����������ת�����Զ�ƥ���ҷ�������
				if (!isReturnCode() && isAutoCheck()) {

					processFocusLost(null);

				}
			}
		}
		// ����Ҫ���⴦��
		if (isCalendar() && isKeyPressed) {
			doKeyPressed();
		}

		doFireValueChanged();
		// ����״̬
		setfocusLostVariableState();

	}

	private void setfocusLostVariableState() {
		isButtonClicked = false;
		// ���ð���
		isKeyPressed = false;
		// �����㶪ʧ�¼����
		setProcessFocusLost(true);
	}

	private void clearText(String sText) {

		if (getRefModel() != null) {
			getRefModel().setSelectedData(null);
		}
		doFireValueChanged();

		setTextFieldShowStrs(sText);
	}

	private boolean isNull(String sText) {
		return sText != null
				&& (sText.trim().length() == 0 || sText
						.equalsIgnoreCase(IRefConst.NULL));
	}

	/**
	 * ���ս��㶪ʧ�󴥷��¼�---��������ʾת��,�ɸ��Ǵ˷���. ��������:(2001-5-10 12:38:33)
	 * 
	 * @param e
	 *            java.awt.event.FocusEvent
	 */
	protected void processFocusLost(java.awt.event.FocusEvent e) {
		setTextFieldShowStrs(getRefShowName());
		// ��ʾ
		// getUITextField().setToolTipText(getText());

		return;
	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-8-11</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setTextFieldShowStrs(String text) {
		// �޸ĳ���,�Ա���ʾ����
		getUITextField().setMaxLength(Integer.MAX_VALUE);
		getUITextField().setText(text);
		// �޸ĳ���
		getUITextField().setMaxLength(getMaxLength());
		//
		setToolTipText();
	}

	/**
	 * ��ȥ nc.ui.pub.beans.ValueChangedListener.
	 */
	public void removeValueChangedListener(ValueChangedListener aListener) {
		listenerList.remove(ValueChangedListener.class, aListener);
	}

	/**
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>setBounds(int, int, int, int)</code>.
	 */
	public void reshape(int x, int y, int width, int height) {
		super.reshape(x, y, width, height);
		//
		if (getUITextField() instanceof CopyUIRefPaneTextField) {
			CopyUIRefPaneTextField ref = getUITextField();
			if (!ref.isInternalAdjustingSize()) {
				ref.setMinWidth(width);
			}
		}
	}

	/**
	 * ���� autoCheck ���� (boolean) ֵ.
	 * 
	 * @param autoCheck
	 *            �µ�����ֵ.
	 * @see #getAutoCheck
	 */
	public void setAutoCheck(boolean autoCheck) {
		boolean oldValue = fieldAutoCheck;
		fieldAutoCheck = autoCheck;
		firePropertyChange("autoCheck", new Boolean(oldValue), new Boolean(
				autoCheck));
	}

	/**
	 * ������������. ��������:(2001-4-27 11:44:02)
	 * 
	 * @param pk
	 *            java.lang.String
	 */
	public void setBlurValue(String strBlurValue) {

		setBlurValue(strBlurValue, true);
	}

	private void setBlurValue(String strBlurValue,
			boolean isNeedInitSelectedData) {

		if (isSpecialRef()) {
			return;
		}
		if (getRefModel() == null) {
			return;
		}
		// ����model�Ŀ�������
		setRefModelConfig();
		Vector vData = null;
		if (isMultiSelectedEnabled() && isMultiInput(strBlurValue)){
			//�������룬ֻ֧��code������ƥ�䡣
			vData = getRefModel().matchData(getRefModel().getRefCodeField(),getMultiInputStrs(strBlurValue));
		}else{
//			getRefModel().setBlurValue(strBlurValue);
			// ����PK��ȡ
			vData = getRefModel().matchBlurData(strBlurValue);
		}
	
		// ģ��ƥ�䲻֧�ֶ�ѡƥ��ļ�¼
		if (vData != null && vData.size() > 1) {
			return;
		}
		// ����
		resetDataFromModel(isNeedInitSelectedData);
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, 20);
		//
		if (getUITextField() instanceof CopyUIRefPaneTextField) {
			CopyUIRefPaneTextField ref = getUITextField();
			if (!ref.isInternalAdjustingSize()) {
				ref.setMinWidth(width);
			}
		}
	}

	public void setBounds(Rectangle r) {
		r.height = 20;
		super.setBounds(r);

	}

	/**
	 * ���� buttonFireEvent ���� (boolean) ֵ.
	 * 
	 * @param buttonFireEvent
	 *            �µ�����ֵ.
	 * @see #getButtonFireEvent
	 */
	public void setButtonFireEvent(boolean buttonFireEvent) {
		boolean oldValue = fieldButtonFireEvent;
		fieldButtonFireEvent = buttonFireEvent;
		firePropertyChange("buttonFireEvent", new Boolean(oldValue),
				new Boolean(buttonFireEvent));
	}

	/**
	 * ���� buttonVisible ���� (boolean) ֵ.
	 * 
	 * @param buttonVisible
	 *            �µ�����ֵ.
	 * @see #getButtonVisible
	 */
	public void setButtonVisible(boolean buttonVisible) {
		boolean oldValue = fieldButtonVisible;
		fieldButtonVisible = buttonVisible;
		getUIButton().setVisible(buttonVisible);
		if (!isButtonVisible()) {
			// ��ť������ʱ,1.���ش��� 2.�����
			setReturnCode(true);
			setAutoCheck(false);
		}
		firePropertyChange("buttonVisible", new Boolean(oldValue), new Boolean(
				buttonVisible));
	}

	/**
	 * ���� cacheEnabled ���� (boolean) ֵ.
	 * 
	 * @param cacheEnabled
	 *            �µ�����ֵ.
	 * @see #getCacheEnabled
	 */
	public void setCacheEnabled(boolean cacheEnabled) {
		boolean oldValue = fieldCacheEnabled;
		fieldCacheEnabled = cacheEnabled;
		if (getRefModel() != null) {
			getRefModel().setCacheEnabled(isCacheEnabled());
		}
		//
		firePropertyChange("cacheEnabled", new Boolean(oldValue), new Boolean(
				cacheEnabled));
	}

	/**
	 * ���� color ���� (java.awt.Color) ֵ.
	 * 
	 * @param color
	 *            �µ�����ֵ.
	 * @see #getColor
	 */
	public void setColor(java.awt.Color color) {
		if (color == null && fieldColor != null || color != null
				&& fieldColor == null || color != null && fieldColor != null
				&& !fieldColor.equals(color)) {
			java.awt.Color oldValue = fieldColor;
			getUITextField().setBackground(color);
			fieldColor = color;
			firePropertyChange("color", oldValue, color);
			fireValueChanged(new ValueChangedEvent(this, fieldColor));
		}
	}

	/**
	 * 
	 * ��������:(2001-3-14 18:29:45)
	 * 
	 * @param sDelStr
	 *            java.lang.String
	 */
	public void setDelStr(String sDelStr) {
		// String oldValue = fieldDelStr;
		fieldDelStr = sDelStr;
		getUITextField().setDelStr(sDelStr);
		// firePropertyChange("delStr", oldValue, sDelStr);
	}

	/**
	 * ���� editable ���� (boolean) ֵ.
	 * 
	 * @param editable
	 *            �µ�����ֵ.
	 * @see #getEditable
	 */
	public void setEditable(boolean editable) {
		getUITextField().setEditable(editable);
		setRefModelEnable(editable);
		fieldEditable = editable;
	}

	public void setButtonEnable(boolean enable) {
		getUIButton().setEnabled(enable);
		setRefModelEnable(enable);
	}

	/**
	 * 
	 * ��������:(2001-3-17 10:32:25)
	 * 
	 * @param param
	 *            boolean
	 */
	public void setEnabled(boolean param) {
		// boolean oldValue = fieldEnabled;
		fieldEnabled = param;
		getUIButton().setEnabled(param);
		getUITextField().setEnabled(param);

		setRefModelEnable(param);

	}

	private void setRefModelEnable(boolean param) {
		if (getRefModel() != null) {
			getRefModel().setisRefEnable(param);
		}
	}

	/**
	 * 
	 * ��������:(2004-6-18 15:44:06)
	 * 
	 * @param newFilerDlgShow
	 *            boolean
	 */
	public void setFilerDlgShow(boolean newFilerDlgShow) {
		// boolean oldValue = isFilerDlgShow;
		isFilerDlgShow = newFilerDlgShow;
		// getRef().setFilerDlgShow(isFilerDlgShow);
		// firePropertyChange("isFilerDlgShow", new Boolean(oldValue),
		// new Boolean(isFilerDlgShow));
	}

	/**
	 * 
	 * ��������:(2004-5-23 11:20:46)
	 * 
	 * @param newIncludeSubShow
	 *            boolean
	 */
	public void setIncludeSubShow(boolean newIncludeSubShow) {
		// boolean oldValue = isIncludeSubShow;
		isIncludeSubShow = newIncludeSubShow;
		// getRef().setIncludeSubShow(isIncludeSubShow);
		// firePropertyChange("isIncludeSubShow", new Boolean(oldValue),
		// new Boolean(isIncludeSubShow));
	}

	/**
	 * 
	 * ��������:(2004-6-25 15:47:14)
	 * 
	 * @param newIsBatchData
	 *            boolean
	 */
	public void setIsBatchData(boolean newIsBatchData) {
		isBatchData = newIsBatchData;
	}

	/**
	 * �����Ƿ�Ϊ�Զ������ ��������:(2001-9-3 15:58:36)
	 * 
	 * @since :V1.00
	 * @param isCustomDefined
	 *            boolean
	 */
	public void setIsCustomDefined(boolean isCustomDefined) {
		m_isCustomDefined = isCustomDefined;
	}

	/**
	 * ���� maxDateStr ���� (java.lang.String) ֵ.
	 * 
	 * @param maxDateStr
	 *            �µ�����ֵ.
	 * @see #getMaxDateStr
	 */
	public void setMaxDateStr(java.lang.String maxDateStr) {
		// String oldValue = fieldMaxDateStr;
		fieldMaxDateStr = maxDateStr;
		// firePropertyChange("maxDateStr", oldValue, maxDateStr);
	}

	/**
	 * 
	 * ��������:(2001-3-14 18:25:15)
	 * 
	 * @param iMaxLength
	 *            int
	 */
	public void setMaxLength(int maxLength) {
		// int oldValue = fieldMaxLength;
		fieldMaxLength = maxLength;
		getUITextField().setMaxLength(maxLength);
		// firePropertyChange("maxLength", new Integer(oldValue), new Integer(
		// maxLength));
	}

	/**
	 * ���� maxValue ���� (double) ֵ.
	 * 
	 * @param maxValue
	 *            �µ�����ֵ.
	 * @see #getMaxValue
	 */
	public void setMaxValue(double maxValue) {
		// double oldValue = fieldMaxValue;
		fieldMaxValue = maxValue;
		getUITextField().setMaxValue(maxValue);
		// firePropertyChange("maxValue", new Double(oldValue), new Double(
		// maxValue));
	}

	/**
	 * ���� minDateStr ���� (java.lang.String) ֵ.
	 * 
	 * @param minDateStr
	 *            �µ�����ֵ.
	 * @see #getMinDateStr
	 */
	public void setMinDateStr(java.lang.String minDateStr) {
		// String oldValue = fieldMinDateStr;
		fieldMinDateStr = minDateStr;
		// firePropertyChange("minDateStr", oldValue, minDateStr);
	}

	/**
	 * ���� minValue ���� (double) ֵ.
	 * 
	 * @param minValue
	 *            �µ�����ֵ.
	 * @see #getMinValue
	 */
	public void setMinValue(double minValue) {
		// double oldValue = fieldMinValue;
		fieldMinValue = minValue;
		getUITextField().setMinValue(minValue);
		// firePropertyChange("minValue", new Double(oldValue), new Double(
		// minValue));
	}

	/**
	 * 
	 */
	public void setMultiCorpRef(boolean multiCorpRef) {
		// boolean oldValue = isMultiCorpRef;
		isMultiCorpRef = multiCorpRef;
		// getRef().setIsMultiCorpRef(multiCorpRef);
		// firePropertyChange("isMultiCorpRef", new Boolean(oldValue),
		// new Boolean(isMultiCorpRef));
	}

	/**
	 * ���� multiSelectedEnabled ���� (boolean) ֵ.
	 * 
	 * @param multiSelectedEnabled
	 *            �µ�����ֵ.
	 * @see #getMultiSelectedEnabled
	 */
	public void setMultiSelectedEnabled(boolean multiSelectedEnabled) {
		// boolean oldValue = fieldMultiSelectedEnabled;
		fieldMultiSelectedEnabled = multiSelectedEnabled;
		//
		// getRef().setMultiSelectedEnabled(multiSelectedEnabled);
		//
		// firePropertyChange("multiSelectedEnabled", new Boolean(oldValue),
		// new Boolean(multiSelectedEnabled));
	}

	/**
	 * ���� notLeafSelectedEnabled ���� (boolean) ֵ.
	 * 
	 * @param notLeafSelectedEnabled
	 *            �µ�����ֵ.
	 * @see #getNotLeafSelectedEnabled
	 */
	public void setNotLeafSelectedEnabled(boolean notLeafSelectedEnabled) {
		// boolean oldValue = fieldNotLeafSelectedEnabled;
		fieldNotLeafSelectedEnabled = notLeafSelectedEnabled;
		//
		// getRef().setNotLeafSelectedEnabled(notLeafSelectedEnabled);
		//
		// firePropertyChange("notLeafSelectedEnabled", new Boolean(oldValue),
		// new Boolean(notLeafSelectedEnabled));
	}

	/**
	 * 
	 * ��������:(2001-3-14 18:27:45)
	 * 
	 * @param iNumPoint
	 *            int
	 */
	public void setNumPoint(int iNumPoint) {
		// int oldValue = fieldNumPoint;
		fieldNumPoint = iNumPoint;
		getUITextField().setNumPoint(iNumPoint);
		// firePropertyChange("numPoint", new Integer(oldValue), new Integer(
		// iNumPoint));
	}

	/**
	 * 
	 * ��������:(2002-6-12 11:32:15)
	 * 
	 * @param newParentContainer
	 *            java.awt.Container
	 */
	public void setParentContainer(java.awt.Container newParentContainer) {
		parentContainer = newParentContainer;
	}

	/**
	 * ����������ȡ���ռ�¼. ��������:(2001-4-27 11:44:02)
	 * 
	 * @param pk
	 *            java.lang.String
	 */
	public void setPK(Object pk) {
		if (pk == null) {
			// ���õ���setPK(String pk)
			setPK(null);
		} else {
			setPK(pk.toString());
		}
	}

	/**
	 * ����������ȡ���ռ�¼. ��������:(2001-4-27 11:44:02) ����pkʱ�����������Ա仯�¼�
	 * 
	 * @param pk
	 *            java.lang.String
	 */
	public void setPK(String pk) {
		// ����PK��ȡ
		// if (isSpecialRef()) {
		// // setRefPK(pk);
		// // setRefCode(pk);
		// // setRefName(pk);
		//			
		// if (getRefNodeName().equalsIgnoreCase("����")) {
		// getUITextField().setText(pk);
		// fieldText = pk;
		// }
		// return;
		// }
		//
		// // ���յ�getRef().getRefModelVariable() == null ,˵���ǵ�һ�ε��á�
		// if (m_refManage == null && (pk == null || pk.trim().length() == 0)) {
		// return;
		// }
		//
		// // ����ʹ��
		// if (getRefModel() == null) {
		// Debug
		// .debug("UIRefPane.setPK(),
		// �����յ�ModelΪ�գ����ܵ�ԭ��Ϊ����û����ȷ��ʼ����������ն��塣���յ�����Ϊ"
		// + getRefNodeName());
		// return;
		// }
		//
		// if (pk != null && pk.trim().length() > 0) {
		// getRefModel().matchPkData(pk.trim());
		// } else {
		// getRefModel().setSelectedData(null);
		// }
		//
		// resetDataFromModel();
		// // if (isReturnCode())
		// // fieldText = getRefCode();
		// // else {
		// // fieldText = getRefShowName();
		// // }
		//		
		// fieldText = getRefShowName();

		// ˵���ǵ�һ�ε���,�ڳ�ʼ������,�������

		if (pk == null || pk.trim().length() == 0) {

			resetToNull();
			return;

		}

		setPKs(new String[] { pk });

	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-7-13</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void resetToNull() {

		setSelectedData(null);
		setTextFieldShowStrs(null);
		fieldText = new String();

		if (m_refManage == null) {
			return;
		}
		if (m_refManage.getRefModel() != null) {
			m_refManage.getRefModel().setSelectedData(null);
		}
		return;
	}

	/**
	 * ��refModel��ȡ���ݡ�
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-3-17</strong>
	 * <p>
	 * 
	 * @param boolean
	 *            isNeedInitSelectedData �Ƿ�Ҫ��ʼ��ѡ�������
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void resetDataFromModel(boolean isNeedInitSelectedData) {
		if (getRefModel() == null) {
			return;
		}
		// ��Ҫ��ʼ����ѡ���Vector
		if (isNeedInitSelectedData) {
			setSelectedData(getRefModel().getSelectedData());
			fieldText = getRefShowName();
		}
		//
		setTextFieldShowStrs(getRefShowName());

	}

	/**
	 * 
	 * ��������:(2003-4-11 10:15:37)
	 * 
	 * @param newPk_corp
	 *            java.lang.String
	 */
	public void setPk_corp(java.lang.String newPk_corp) {
		if (isSpecialRef()) {
			return;
		}
		pk_corp = newPk_corp;
		getRef().setPk_corp(newPk_corp);

	}

	/**
	 * 
	 * ��������:(2001-11-27 13:30:13)
	 * 
	 * @param newProcessFocusLost
	 *            boolean
	 */
	public void setProcessFocusLost(boolean newProcessFocusLost) {
		processFocusLost = newProcessFocusLost;
	}

	/**
	 * 
	 * ��������:(2001-5-10 11:25:32)
	 * 
	 * @param refCode
	 *            java.lang.String
	 * @deprecated
	 */
	protected void setRefCode(String refCode) {
		fieldRefCode = refCode;
	}

	/**
	 * ���� refInputType ���� (int) ֵ.
	 * 
	 * @param refInputType
	 *            �µ�����ֵ.
	 * @see #getRefInputType ��ʹ��setReturnCode()��������ʾcode or name
	 * 
	 */
	public void setRefInputType(int refInputType) {
		// int oldValue = fieldRefInputType;
		fieldRefInputType = refInputType;
		// firePropertyChange("refInputType", new Integer(oldValue), new
		// Integer(
		// refInputType));
	}

	/**
	 * 
	 * ��������:(01-4-12 22:04:03)
	 * 
	 * @param model
	 *            nc.ui.bd.ref.IRefModel
	 */
	public void setRefModel(AbstractRefModel model) {
		// �޸Ľڵ���
		getRef().setRefModel(model);
		if (model != null) {
			fieldRefNodeName = model.getRefNodeName();
			if (fieldRefNodeName == null
					|| fieldRefNodeName.trim().length() == 0) {
				fieldRefNodeName = model.getClass().getName();
			}
			// ��Ϊ���Զ������
			setIsCustomDefined(true);

			model.addChangeListener(this);

			model.setCacheEnabled(isCacheEnabled());
		}

	}

	/**
	 * 
	 * ��������:(2001-5-10 11:26:06)
	 * 
	 * @param refName
	 *            java.lang.String
	 * @deprecated
	 */
	protected void setRefName(String refName) {
		fieldRefName = refName;
	}

	/**
	 * ���� refNodeName ���� (java.lang.String) ֵ.
	 * 
	 * @param refNodeName
	 *            �µ�����ֵ.
	 * @see #getRefNodeName
	 */
	public void setRefNodeName(java.lang.String refNodeName) {
		if (refNodeName != null) {
			if (refNodeName.equalsIgnoreCase("����")) {
				setTextType("TextDate");
				setMaxLength(10);
				getUITextField().setBackground(colorBackgroundDefault);
				setEditable(true);

			} else if (refNodeName.equalsIgnoreCase("������")) {
				setTextType("TextDbl");
				getUITextField().setBackground(colorBackgroundDefault);
				setEditable(true);
				setNumPoint(10);
				setMaxLength(28);
				setAutoCheck(false);
				getUITextField().setAutoParse(false);

			} else if (refNodeName.equalsIgnoreCase("��ɫ")) {
				setEditable(false);
				getUITextField().setBackground(fieldColor);
			} else if (refNodeName.equalsIgnoreCase("�ļ�")) {
				nc.ui.pub.beans.UIFileChooser chooser = new nc.ui.pub.beans.UIFileChooser(
						fieldText);
				setRefUI(chooser);
			} else {
				setTextType("TextStr");
				getUITextField().setBackground(colorBackgroundDefault);
				setEditable(true);

			}

		}
		String oldValue = fieldRefNodeName;
		fieldRefNodeName = refNodeName;
		// ���UFRefManag
		m_refManage = null;

		// �Ƿ�����ȡ���ݲ���
		if (isSpecialRef()) {
			setIsBatchData(false);
		}
		firePropertyChange("refNodeName", oldValue, refNodeName);

		setButtonIcon();
	}

	/**
	 * 
	 * ��������:(2001-5-10 11:24:52)
	 * 
	 * @param refPK
	 *            java.lang.String
	 * @deprecated
	 */
	protected void setRefPK(String refPK) {
		fieldRefPK = refPK;
	}

	/**
	 * ���� refType ���� (int) ֵ.
	 * 
	 * @param refType
	 *            �µ�����ֵ.
	 * @see #getRefType
	 */
	public void setRefType(int refType) {
		// int oldValue = fieldRefType;
		fieldRefType = refType;
		// ���ԭUFRefManage
		m_refManage = null;
		getRef().setRefType(refType);
		//
		// firePropertyChange("refType", new Integer(oldValue), new Integer(
		// refType));
	}

	/**
	 * 
	 * ��������:(2001-8-25 10:44:30)
	 * 
	 * @param refUI
	 *            nc.ui.bd.ref.IRefUI
	 *            �ø÷��������Զ���UI�����ܻᵼ�²��ս���û��parent�����ս�������ص���������档
	 *            ����ʹ��setRefUICreator(IRefUICreator creator) ����RefUi��
	 * 
	 * Deprecated
	 */
	public void setRefUI(nc.ui.bd.ref.IRefUINew refUI) {
		getRef().setRefUI(refUI);
	}

	/**
	 * ���� returnCode ���� (boolean) ֵ.
	 * 
	 * @param returnCode
	 *            �µ�����ֵ.
	 * @see #getReturnCode
	 */
	public void setReturnCode(boolean returnCode) {
		// boolean oldValue = fieldReturnCode;
		fieldReturnCode = returnCode;
		// firePropertyChange("returnCode", new Boolean(oldValue), new Boolean(
		// returnCode));
	}

	/**
	 * ���� selectedData ���� (java.util.Vector) ֵ.
	 * 
	 * @param selectedData
	 *            �µ�����ֵ.
	 * @see #getSelectedData
	 */
	public void setSelectedData(java.util.Vector selectedData) {
		// java.util.Vector oldValue = fieldSelectedData;
		fieldSelectedData = selectedData;
		// firePropertyChange("selectedData", oldValue, selectedData);
	}

	/**
	 * 
	 * ��������:(2001-7-24 15:39:38)
	 * 
	 * @param newStrPatch
	 *            java.lang.String
	 */
	public void setStrPatch(String newStrPatch) {
		if (getRefModel() != null)
			getRef().getRefModel().setStrPatch(newStrPatch);
	}

	/**
	 * ���� table ���� (nc.ui.pub.beans.UITable) ֵ.
	 * 
	 * @param table
	 *            �µ�����ֵ.
	 * @see #getTable
	 */
	public void setTable(UITable table) {
		// UITable oldValue = fieldTable;
		fieldTable = table;
		// firePropertyChange("table", oldValue, table);
	}

	/**
	 * �����ı���ֵ�����¼�. ��������:(2001-3-14 18:12:45)
	 * 
	 * @param sText
	 *            java.lang.String
	 */
	public void setText(String sText) {
		setTextFieldShowStrs(sText);

		// ͨ����������ղ���������
		if (sText == null || sText.trim().length() == 0) {
			if (getRefModel() != null) {
				getRefModel().setSelectedData(null);
			}
		}
		// ���Ա仯�¼�
		doFireValueChanged();
	}

	/**
	 * ���� textFieldVisible ���� (boolean) ֵ.
	 * 
	 * @param textFieldVisible
	 *            �µ�����ֵ.
	 * @see #getTextFieldVisible
	 */
	public void setTextFieldVisible(boolean textFieldVisible) {
		// boolean oldValue = fieldTextFieldVisible;
		fieldTextFieldVisible = textFieldVisible;
		getUITextField().setVisible(textFieldVisible);
		// firePropertyChange("textFieldVisible", new Boolean(oldValue),
		// new Boolean(textFieldVisible));
	}

	/**
	 * 
	 * ��������:(2001-3-14 18:12:45)
	 * 
	 * @param sTextType
	 *            java.lang.String
	 */
	public void setTextType(String sTextType) {
		// String oldValue = sTextType;
		fieldTextType = sTextType;
		getUITextField().setTextType(sTextType);
		// firePropertyChange("textType", oldValue, sTextType);
	}

	/**
	 * ���� toolTipText ���� (java.lang.String) ֵ.
	 * 
	 * @param toolTipText
	 *            �µ�����ֵ.
	 * @see #getToolTipText
	 */

	/**
	 * 
	 * ��������:(2003-8-18 11:14:39)
	 * 
	 * @param newTreeGridNodeMultiSelected
	 *            boolean
	 */
	public void setTreeGridNodeMultiSelected(
			boolean newTreeGridNodeMultiSelected) {
		isTreeGridNodeMultiSelected = newTreeGridNodeMultiSelected;
		getRef().setTreeGridNodeMultiSelected(isTreeGridNodeMultiSelected);
	}

	/**
	 * �����ı���ֵ�������¼�. ��������:(2001-3-14 18:12:45)
	 * 
	 * @param sText
	 *            java.lang.String
	 */
	public void setValue(String sText) {
		if (sText != null)
			fieldText = sText.trim();
		else
			fieldText = new String();

		setTextFieldShowStrs(fieldText);
		// getUITextField().setText(fieldText);

	}

	/**
	 * 
	 * ��������:(2004-6-18 15:44:06)
	 * 
	 * @param newVersionButtonShow
	 *            boolean
	 */
	public void setVersionButtonShow(boolean newVersionButtonShow) {
		// boolean oldValue = isVersionButtonShow;
		isVersionButtonShow = newVersionButtonShow;
		// getRef().setVersionButtonShow(isVersionButtonShow);
		// firePropertyChange("isVersionButtonShow", new Boolean(oldValue),
		// new Boolean(isVersionButtonShow));
	}

	/**
	 * 
	 * ��������:(2001-5-11 14:15:37)
	 * 
	 * @param where
	 *            java.lang.String
	 */
	public void setWhereString(String where) {
		if (getRefModel() != null)
			getRefModel().setWherePart(where);
	}

	public boolean isFocusOwner() {
		return super.isFocusOwner() || getUITextField().isFocusOwner();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#requestFocus()
	 */
	public void requestFocus() {
		getUITextField().requestFocus();
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see javax.swing.JComponent#requestFocusInWindow()
	 */
	@Override
	public boolean requestFocusInWindow() {
		// TODO �Զ����ɷ������
		return getUITextField().requestFocusInWindow();
	}

	/*
	 * �����Ϊ����񣬸÷�����֤����ֵ�������ϡ�
	 * 
	 * @see javax.swing.JComponent#processKeyBinding(javax.swing.KeyStroke,
	 *      java.awt.event.KeyEvent, int, boolean)
	 */
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
			int condition, boolean pressed) {
boolean flag=true;
		if (isTableParent()) {
			((CopyUIRefPaneTextField) getUITextField()).setIsInputKeyPressed(e);
		}
//		boolean flag = getUITextField().processKeyBind(ks, e, condition,
//				pressed);
		// getUITextField().processKeyEvent(e);

		return flag;
	}

	private boolean isTableParent() {
		boolean isTableParent = false;
		Component parent_refPane = getParent();
		if (parent_refPane instanceof JTable) {
			isTableParent = true;
		}
		return isTableParent;
	}

	public void setPKs(String[] pks) {

		// ************************************************

		if (isSpecialRef()) {
			// setRefPK(pk);
			// setRefCode(pk);
			// setRefName(pk);

			String pk = null;

			if (pks != null || pks.length > 0) {
				pk = pks[0];
			}

			if (getRefNodeName().equalsIgnoreCase("����")
					|| getRefNodeName().equals("���ն���")) {
				getUITextField().setText(pk);
				fieldText = pk;
			}
			return;
		}

		// ˵���ǵ�һ�ε��á�
		if ((pks == null || pks.length == 0)) {
			resetToNull();
			return;
		}

		// ����ʹ��
		if (getRefModel() == null) {
			Debug
					.debug("UIRefPane.setPK(), �����յ�ModelΪ�գ����ܵ�ԭ��Ϊ����û����ȷ��ʼ����������ն��塣���յ�����Ϊ"
							+ getRefNodeName());
			return;
		}

		if (pks != null && pks.length > 0) {
			setRefModelConfig();
			getRefModel().matchPkData(pks);
		} else {
			getRefModel().setSelectedData(null);
		}

		resetDataFromModel(true);

		fieldText = getRefShowName();

	}

	public java.lang.String getRefShowName() {

		String showName = "";
		if (getRefModel() == null) {
			return showName;
		}

		String[] names = null;

		if (isReturnCode()) {
			names = getRefCodes();
		} else {
			names = getRefModel() == null ? null : getRefModel()
					.getRefShowNameValues();
		}

		showName = getComposedString(names);

		return showName;
	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-8-10</strong>
	 * <p>
	 * 
	 * @param
	 * @return String
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private String getComposedString(String[] names) {
		String showName = "";
		if (names != null && names.length > 0) {

			for (int i = 0; i < names.length; i++) {
				if (names[i] == null) {
					continue;
				}
				showName += names[i] + ",";
			}

			if (!showName.equals("")) {
				showName = showName.substring(0, showName.length() - 1);
			}

		}
		return showName;
	}

	boolean hasButtonAction() {
		return hasButtonAction;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2005-11-9 11:22:57)
	 * 
	 * @return int
	 */
	public int getMatchMode() {
		return matchMode;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2005-11-9 11:22:57)
	 * 
	 * @param newMatchMode
	 *            int
	 */
	public void setMatchMode(int newMatchMode) {
		matchMode = newMatchMode;
		// getRef().setMatchMode(matchMode);
	}

	private void doReturnOk() {

		isKeyPressed = false;
		isButtonClicked = true;
		((CopyUIRefPaneTextField) getUITextField()).isInputKeyPress = false;
		setProcessFocusLost(false);

		setTextFieldShowStrs(getRefShowName());

	}

	private void initSpecialRef_hm() {
		for (int i = 0; i < refNodeNames.length; i++) {
			specialRef_hm.put(refNodeNames[i], refNodeNames[i]);
		}
	}

	public boolean isSpecialRef() {

		return (specialRef_hm.containsKey(getRefNodeName()))
				&& !isCustomDefined();
	}

	public void stateChanged(ChangeEvent e) {
		// ֻˢ�½������ݣ�û�д������Ա仯�¼�

		resetDataFromModel(true);

	}

	/**
	 * @return ���� isSealedDataButtonShow��
	 */
	public boolean isSealedDataButtonShow() {
		return isSealedDataButtonShow;
	}

	/**
	 * @param isSealedDataButtonShow
	 *            Ҫ���õ� isSealedDataButtonShow��
	 */
	public void setSealedDataButtonShow(boolean isSealedDataButtonShow) {
		this.isSealedDataButtonShow = isSealedDataButtonShow;
		// getRef().setSealedDataButtonShow(isSealedDataButtonShow);
	}

	/**
	 * ����UI��ص���������
	 */
	private void setUIConfig() {
		UFRefManage refMng = getRef();
		if (refMng == null) {
			return;
		}
		getRefUIConfig().setMutilSelected(isMultiSelectedEnabled());
		getRefUIConfig().setNotLeafSelectedEnabled(isNotLeafSelectedEnabled());
		getRefUIConfig().setMultiCorpRef(isMultiCorpRef());
		getRefUIConfig().setIncludeSubShow(isIncludeSubShow());
		getRefUIConfig().setFilterDlgShow(isFilerDlgShow());
		getRefUIConfig().setVersionButtonShow(isVersionButtonShow());
		getRefUIConfig().setSealedDataButtonShow(isSealedDataButtonShow());
		getRefUIConfig().setTreeGridNodeMultiSelected(
				isTreeGridNodeMultiSelected());

		refMng.setRefUIConfig(getRefUIConfig());

		//
		refMng.setMatchMode(getMatchMode());
		refMng.setTreeGridNodeMultiSelected(isTreeGridNodeMultiSelected());
	}

	private void setRefModelConfig() {
		AbstractRefModel refModel = getRefModel();
		if (refModel == null) {
			return;
		}
		refModel.setNotLeafSelectedEnabled(isNotLeafSelectedEnabled());

	}

	/**
	 * @return ���� pk_bdinfo��
	 */
	public String getPk_bdinfo() {
		return pk_bdinfo;
	}

	/**
	 * @param pk_bdinfo
	 *            Ҫ���õ� pk_bdinfo��
	 */
	public void setPk_bdinfo(String pk_bdinfo) {
		this.pk_bdinfo = pk_bdinfo;
	}

	/**
	 * @return ���� refUIConfig��
	 */
	public RefUIConfig getRefUIConfig() {
		if (refUIConfig == null) {
			refUIConfig = new RefUIConfig();
		}
		return refUIConfig;
	}

	private Icon getRefIcon(String state) {
		String iconName = getRefNodeName();

		if (!isSpecialRef() || "�ı���".equals(getRefNodeName())
				|| "���ն���".equals(getRefNodeName()) || "".equals(iconName)) {
			iconName = "����";
		}

		return RefPaneIconFactory.getInstance().getImageIcon(iconName, state);

	}

	private void setButtonIcon() {
		button.setIcon(getRefIcon(RefPaneIconFactory.REFENABLE));
		button.setDisabledIcon(getRefIcon(RefPaneIconFactory.REFDISENABLE));
		button.setRolloverIcon(getRefIcon(RefPaneIconFactory.REFMOUSEOVER));
		button.setPressedIcon(getRefIcon(RefPaneIconFactory.REFPRESSED));

		Icon icon = getRefIcon(RefPaneIconFactory.REFENABLE);
		button.setPreferredSize(new java.awt.Dimension(icon.getIconWidth(),
				icon.getIconHeight()));

	}

	/**
	 * @return ���� refUI��
	 */
	public Object getRefUI() {
		if (isSpecialRef()) {
			return refUI;
		}

		return getRef().getRefUI();

	}

	/**
	 * @param refUI
	 *            Ҫ���õ� refUI��
	 */
	private void setRefUI(Object refUI) {
		this.refUI = refUI;
	}

	/**
	 * ֱ�Ӹ����ո�ֵ�� ����Զ�̵���
	 * 
	 * @param pk
	 * @param code
	 * @param name
	 */
	public void setSelectedData(String pk, String code, String name) {
		AbstractRefModel model = getRefModel();
		if (model == null) {
			return;
		}
		Vector data = new Vector();
		// ����һ���ټ�¼
		Vector record = getAllColumnNames(model);
		record.set(model.getFieldIndex(model.getPkFieldCode()), pk);
		record.set(model.getFieldIndex(model.getRefCodeField()), code);
		record.set(model.getFieldIndex(model.getRefNameField()), name);
		data.add(record);
		model.setSelectedData(data);
		stateChanged(null);
	}

	private Vector getAllColumnNames(AbstractRefModel refModel) {
		Vector vecAllColumnNames = null;

		vecAllColumnNames = new Vector();
		if (refModel.getFieldCode() != null
				&& refModel.getFieldCode().length > 0) {
			if (refModel.getFieldName() == null
					|| refModel.getFieldName().length == 0) {
				for (int i = 0; i < refModel.getFieldCode().length; i++) {
					vecAllColumnNames.addElement(refModel.getFieldCode()[i]);
				}
			} else {
				if (refModel.getFieldName().length >= refModel.getFieldCode().length) {
					for (int i = 0; i < refModel.getFieldCode().length; i++) {
						vecAllColumnNames
								.addElement(refModel.getFieldName()[i]);
					}
				} else {
					for (int i = 0; i < refModel.getFieldName().length; i++) {
						vecAllColumnNames
								.addElement(refModel.getFieldName()[i]);
					}
					for (int i = refModel.getFieldName().length; i < refModel
							.getFieldCode().length; i++) {
						vecAllColumnNames
								.addElement(refModel.getFieldCode()[i]);
					}
				}
			}

		}

		if (refModel.getHiddenFieldCode() != null)
			for (int i = 0; i < refModel.getHiddenFieldCode().length; i++) {
				vecAllColumnNames.addElement(refModel.getHiddenFieldCode()[i]);
			}

		// ���붯̬��
		if (refModel.isDynamicCol()) {

			String[] dynamicColNames = refModel.getDynamicFieldNames();
			if (refModel.getDynamicFieldNames() != null) {

				for (int i = 0; i < dynamicColNames.length; i++) {

					// ���뵽��ʾ��������
					vecAllColumnNames.addElement(dynamicColNames[i]);
				}
			}
		}
		return vecAllColumnNames;
	}

	public IRefUICreator getRefUICreator() {
		return getRef().getRefUICreator();
	}

	/**
	 * 
	 * ��������:(2001-8-25 10:44:30)
	 * 
	 * @param refUI
	 *            nc.ui.bd.ref.IRefUI Deprecated
	 */
	public void setRefUICreator(IRefUICreator creator) {
		getRef().setRefUICreator(creator);
	}

	// �Ƿ����",",���������Ϊ�Ƕ�������
	private boolean isMultiInput(String inputStr) {

		if (inputStr == null) {
			return false;
		}

		return inputStr.indexOf(multiInputToken) > 0;
	}

	private String[] getMultiInputStrs(String inputStr) {
		return inputStr.split(multiInputToken);
	}
}