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
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = 1L;

	public static final int REFINPUTTYPE_CODE = 0; // 编码

	public static final int REFINPUTTYPE_NAME = 1; // 名称

	public static final int REFINPUTTYPE_MNECODE = 2; // 助记码

	/**
	 * 基本档案资源表主键，参照本身不关心该值。供调用者暂存使用
	 */

	private String pk_bdinfo = null;

	/**
	 * 参照界面的配置信息
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

	// 参照在焦点丢失后显示的属性，目前只支持 编码或名称
	private boolean fieldReturnCode = false; // 缺省返回名称

	private boolean fieldEnabled = true;

	// 参照属性
	protected String fieldText = new String();

	private boolean fieldEditable = true;

	private UITable fieldTable = null;

	private java.lang.String fieldRefNodeName = new String();

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	private java.lang.String fieldRefPK = null;

	private java.lang.String fieldRefCode = new String();

	private java.lang.String fieldRefName = new String();

	private java.awt.Color colorBackgroundDefault = null;

	// 允许显示否(只显示一次)
	private boolean displayedEnabled = true;

	// 自动检查否
	private boolean fieldAutoCheck = true;

	// 参照选择对话框确定按钮按下时，是否触发属性变化事件
	private boolean fieldButtonFireEvent = false;

	private boolean fieldCacheEnabled = true;

	private java.awt.Color fieldColor = new java.awt.Color(0);

	private java.lang.String fieldMaxDateStr = null;

	private double fieldMaxValue = Double.MAX_VALUE;

	private java.lang.String fieldMinDateStr = null;

	private double fieldMinValue = -Double.MAX_VALUE;

	private boolean fieldMultiSelectedEnabled = false;

	private boolean fieldNotLeafSelectedEnabled = true;

	// 参照在获得焦点后显示的属性类型。
	private int fieldRefInputType = REFINPUTTYPE_CODE;

	private java.util.Vector fieldSelectedData = new java.util.Vector();

	private boolean fieldTextFieldVisible = true;

	// 在失去焦点前参照按钮被点击否
	protected boolean isButtonClicked = false;

	// 字符键按下否
	protected boolean isKeyPressed = false;

	// 离开参照,丢失焦点否?
	protected boolean isLostFocus = true;

	// 是否多公司选择参照
	private boolean isMultiCorpRef = false;

	// 公司主键
	private String pk_corp = null;

	// 树表参照是否树分类可以多选择
	private boolean isTreeGridNodeMultiSelected = false;

	// private nc.ui.bd.ref.AbstractRefModel m_refModel;
	protected int m_iReturnButtonCode = -1;

	private boolean m_isCustomDefined = false;

	// /////////////
	//
	public UFRefManage m_refManage = null;

	private java.awt.Container parentContainer = null;

	private boolean processFocusLost = true;

	// 2004-5-23 新增开关
	// 树形参照,是否包含下级是否显示,该属性会影响参照model的取数，这也是一个model的属性
	private boolean isIncludeSubShow = false;

	// sxj 2004-06-18
	// 版本按钮是否显示开关
	private boolean isVersionButtonShow = false;

	// 高级过滤对话框是否显示开关
	private boolean isFilerDlgShow = false;

	// 是否批取数据开关
	private boolean isBatchData = true;

	private boolean hasButtonAction = false;

	// 模糊匹配模式,默认任意位置匹配
	private int matchMode = 0; // UFRefManage属性

	// 特殊处理的参照
	private HashMap specialRef_hm = new HashMap();

	// 是否封存数据按钮显示开关
	// 2006-05-9 add
	private boolean isSealedDataButtonShow = false;

	private String[] refNodeNames = new String[] { "", "日历", "文件", "颜色", "计算器",
			"文本框", "参照定制" };

	private String multiInputToken = ",";

	/**
	 * 参照的界面类，不支持颜色、日历、计算器。
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
	 * UIRefPane 构造子注解.
	 */
	public CopyFormatPanl() {
		super();
		initialize();
	}

	/**
	 * 
	 * 创建日期:(2002-6-12 11:32:54)
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
	 * UIRefPane 构造子注解.
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 */
	public CopyFormatPanl(java.awt.LayoutManager p0) {
		super(p0);
		initialize();
	}

	/**
	 * UIRefPane 构造子注解.
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
	 * UIRefPane 构造子注解.
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
	 * 添加 nc.ui.pub.beans.ValueChangedListener.
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
	 * 参照客户化选择界面
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
						"_Beans", "UPP_Beans-000086")/* @res "调色板" */,
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
	/* 警告:此方法将重新生成. */
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
	 * 颜色参照的属性变化事件特殊处理，固定在选择颜色按确认键后触发。 创建日期:(2003-11-27 13:10:03)
	 * 
	 * 处理属性变化事件,不论参照选择对话框确定按钮触发事件开关是否打开
	 * 1、如果打开，在参照选择对话框确定按钮按下后，属性变化事件已将触发，此处调用也不会触发。 2、如果通过输入编码来选择参照数据，触发属性变化事件。
	 * 
	 */
	private void doFireValueChanged() {

		String sText = getUITextField().getText();

		boolean isValueChanged = false;

		// 先判断文本是否发生变化。
		if (!sequals(sText, fieldText)) {
			isValueChanged = true;
		}
		// 再判断model是否变化。 只要有一个值变化，就触发属性事件。
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
	 * 创建日期:(2003-11-27 13:10:03)
	 */
	private void doKeyPressed() {

		String sText = getUITextField().getText().trim();
		String sOriginText = sText;

		if (isCalendar()) {
			// 解析成合法数据
			sText = nc.vo.pub.lang.UFDate.getValidUFDateString(sText);
			getUITextField().setText(sText);

		} else {
			// 自动匹配数据
			if (isButtonVisible()) {
				setBlurValue(sText, false);
			}

			// 助记码为多条的处理
			if (getRefCodes() != null && getRefCodes().length > 1
					&& (!isIncludeBlurChar(sOriginText))
					&& !isMultiInput(sText)) {   //不是多项输入时，才会弹出参照框
				m_refManage.setIsMneCodes(true);
				getRefModel().setBlurValue(sText);
				// Model中已默认设置已选数据为匹配的数据;设置已选数据为空.
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
			// 自动匹配的结果
			sText = getRefCode();

		}

		// 如果不自动匹配，且没有匹配上，显示用户输入的值
		if (!isAutoCheck() && (sText == null || sText.length() == 0)) {
			sText = sOriginText;
			getUITextField().setText(sText);
		}

		// 特殊处理
		// 树型参照,如果包含下级复选框显示,在code匹配是要把下级的信息保存到临时表.
		// if (isAutoCheck()) {
		// 树形参照如果
		// isIncludeSubShow=true,默认是包含下级的.所以当code匹配记录时,要处理该Code的下级(保存到临时表)
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
		return getRefNodeName().equals("日历");
	}

	/**
	 * 用来支持监听器事件的方法.
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
	 * 获取 color 特性 (java.awt.Color) 值.
	 * 
	 * @return color 特性值.
	 * @see #setColor
	 */
	public java.awt.Color getColor() {
		return fieldColor;
	}

	/**
	 * 获取 delStr 特性 (java.lang.String) 值.
	 * 
	 * @return delStr 特性值.
	 * @see #setDelStr
	 */
	public java.lang.String getDelStr() {
		return fieldDelStr;
	}

	/**
	 * 获取 maxDateStr 特性 (java.lang.String) 值.
	 * 
	 * @return maxDateStr 特性值.
	 * @see #setMaxDateStr
	 */
	public java.lang.String getMaxDateStr() {
		return fieldMaxDateStr;
	}

	/**
	 * 获取 maxlength 特性 (int) 值.
	 * 
	 * @return maxlength 特性值.
	 * @see #setMaxlength
	 */
	public int getMaxLength() {
		return fieldMaxLength;
	}

	/**
	 * 获取 maxValue 特性 (double) 值.
	 * 
	 * @return maxValue 特性值.
	 * @see #setMaxValue
	 */
	public double getMaxValue() {
		return fieldMaxValue;
	}

	/**
	 * 获取 minDateStr 特性 (java.lang.String) 值.
	 * 
	 * @return minDateStr 特性值.
	 * @see #setMinDateStr
	 */
	public java.lang.String getMinDateStr() {
		return fieldMinDateStr;
	}

	/**
	 * 获取 minValue 特性 (double) 值.
	 * 
	 * @return minValue 特性值.
	 * @see #setMinValue
	 */
	public double getMinValue() {
		return fieldMinValue;
	}

	/**
	 * 获取 numPoint 特性 (int) 值.
	 * 
	 * @return numPoint 特性值.
	 * @see #setNumPoint
	 */
	public int getNumPoint() {
		return fieldNumPoint;
	}

	/**
	 * 
	 * 创建日期:(2003-4-11 10:15:37)
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
	 * 创建日期:(01-4-12 22:04:34)
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
	 * 返回 Bddata[] ,包含档案内容的具体信息. sxj 2004-07-27 add
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
	 * 获取 refCode 特性 (java.lang.String) 值.
	 * 
	 * @return refCode 特性值.
	 * @see #setRefCode
	 */
	public java.lang.String getRefCode() {

		return getRefCodes() == null ? null : getRefCodes()[0];

	}

	/**
	 * 获取 refCode 特性 (java.lang.String) 值.
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
	 * 获取 refInputType 特性 (int) 值.
	 * 
	 * @return refInputType 特性值.
	 * @see #setRefInputType
	 * 
	 */
	public int getRefInputType() {
		return fieldRefInputType;
	}

	/**
	 * 
	 * 创建日期:(2001-8-25 10:47:18)
	 * 
	 * @return nc.ui.bd.ref.AbstractRefModel
	 */
	public nc.ui.bd.ref.AbstractRefModel getRefModel() {
		// 特殊的参照（日历等）没有model,如果作为文本框来用，也没有model;
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
	 * 获取 refName 特性 (java.lang.String) 值.
	 * 
	 * @return refName 特性值.
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
	 * 获取 refName 特性 (java.lang.String) 值.
	 * 
	 * @return refName 特性值.
	 * @see #setRefName
	 */
	public java.lang.String[] getRefNames() {
		if (getRefModel() == null)
			return null;
		return getRefModel().getRefNameValues();
	}

	/**
	 * 获取 refNodeName 特性 (java.lang.String) 值.
	 * 
	 * @return refNodeName 特性值.
	 * @see #setRefNodeName
	 */
	public java.lang.String getRefNodeName() {
		if (fieldRefNodeName == null)
			fieldRefNodeName = "";
		return fieldRefNodeName;
	}

	/**
	 * 获取 refPK 特性 (java.lang.String) 值.
	 * 
	 * @return refPK 特性值.
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
	 * 获取 refPK 特性 (java.lang.String) 值.
	 * 
	 * @return refPK 特性值.
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
	 * 获取 refType 特性 (int) 值.
	 * 
	 * @return refType 特性值.
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
	 * 创建日期:(2001-9-22 17:33:23)
	 * 
	 * @return int
	 */
	public int getReturnButtonCode() {
		return m_iReturnButtonCode;
	}

	/**
	 * 获取 selectedData 特性 (java.util.Vector) 值.
	 * 
	 * @return selectedData 特性值.
	 * @see #setSelectedData
	 */
	public java.util.Vector getSelectedData() {
		return fieldSelectedData;
	}

	/**
	 * 获取 table 特性 (nc.ui.pub.beans.UITable) 值.
	 * 
	 * @return table 特性值.
	 * @see #setTable
	 */
	public UITable getTable() {
		return fieldTable;
	}

	/**
	 * 
	 * 创建日期:(2001-3-14 18:11:23)
	 * 
	 * @return java.lang.String
	 */
	public String getText() {
		return getUITextField().getText();
	}

	/**
	 * 获取 textType 特性 (java.lang.String) 值.
	 * 
	 * @return textType 特性值.
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
	 * 返回 UIButton1 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
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

				// 鼠标进入时,背景变亮
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
			// button.setIButtonType(0 /** Java默认(自定义) */
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
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-6-9</strong>
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
	 * 返回 UITextField 特性值.
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告:此方法将重新生成. */
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
	 * 创建日期:(2001-3-14 19:33:54)
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
	 * 创建日期:(2001-8-16 13:34:37)
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
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		/* 除去下列各行的注释,以将未捕捉到的异常打印至 stdout. */
		System.out.println("--------- 未捕捉到的异常 ---------");
		exception.printStackTrace(System.out);
	}

	/**
	 * 初始化连接
	 * 
	 * @exception java.lang.Exception
	 *                异常说明.
	 */
	/* 警告:此方法将重新生成. */
	private void initConnections() {

		getUIButton().addActionListener(ivjEventHandler);
	}

	/**
	 * 初始化类.
	 */
	/* 警告:此方法将重新生成. */
	private void initialize() {

		setName("UIRefPane");
		setPreferredSize(new java.awt.Dimension(122, 20));
		setRequestFocusEnabled(false);
		// setSize(100, 20);
		setLayout(new nc.ui.pub.beans.UIRefLayout());
		add(getUITextField(), getUITextField().getName());
		if (!getRefNodeName().equals("文本框")) {
			add(getUIButton(), getUIButton().getName());
		}

		displayedEnabled = true;
		initConnections();

		pk_corp = RefContext.getInstance().getPk_corp();

		// "日历","文件","颜色","计算器"
		initSpecialRef_hm();
		// setBorder(Style.getBorder("TextField.border"));
		setBorder(BorderFactory.createLineBorder(Style.NCborderColor));

	}

	/**
	 * 获取 autoCheck 特性 (boolean) 值.
	 * 
	 * @return autoCheck 特性值.
	 * @see #setAutoCheck
	 */
	public boolean isAutoCheck() {
		return fieldAutoCheck;
	}

	/**
	 * 
	 * 创建日期:(2004-6-25 15:47:14)
	 * 
	 * @return boolean
	 */
	public boolean isBatchData() {
		return isBatchData;
	}

	/**
	 * 监听参照值变化事件的时机的开关： true: 在参照对话框确认按钮按下后 false:在参照输入框离开焦点后。
	 * 
	 * @return buttonFireEvent 特性值.
	 * @see #setButtonFireEvent
	 */
	public boolean isButtonFireEvent() {
		return fieldButtonFireEvent;
	}

	/**
	 * 获取 buttonVisible 特性 (boolean) 值.
	 * 
	 * @return buttonVisible 特性值.
	 * @see #setButtonVisible
	 */
	public boolean isButtonVisible() {
		return fieldButtonVisible;
	}

	/**
	 * 获取 cacheEnabled 特性 (boolean) 值.
	 * 
	 * @return cacheEnabled 特性值.
	 * @see #setCacheEnabled
	 */
	public boolean isCacheEnabled() {
		return fieldCacheEnabled;
	}

	/**
	 * 是否为自定义参照 创建日期:(2001-9-3 15:58:53)
	 * 
	 * @since :V1.00
	 * @return boolean
	 */
	public boolean isCustomDefined() {
		return m_isCustomDefined;
	}

	/**
	 * 获取 editable 特性 (boolean) 值.
	 * 
	 * @return editable 特性值.
	 * @see #setEditable
	 */
	public boolean isEditable() {
		return fieldEditable;
	}

	/**
	 * 
	 * 创建日期:(2001-3-25 11:45:58)
	 * 
	 * @return boolean
	 */
	public boolean isEnabled() {
		return fieldEnabled;
	}

	/**
	 * 
	 * 创建日期:(2004-6-18 15:44:06)
	 * 
	 * @return boolean
	 */
	public boolean isFilerDlgShow() {
		return isFilerDlgShow;
	}

	// 是否包含下级选中
	public boolean isIncludeSub() {
		if (getRefModel() == null)
			return false;
		return getRefModel().isIncludeSub();
	}

	/**
	 * 
	 * 创建日期:(2004-5-23 11:20:46)
	 * 
	 * @return boolean 显示? --> 是否包含下级控件
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
	 * 获取 multiSelectedEnabled 特性 (boolean) 值.
	 * 
	 * @return multiSelectedEnabled 特性值.
	 * @see #setMultiSelectedEnabled
	 */
	public boolean isMultiSelectedEnabled() {
		return fieldMultiSelectedEnabled;
	}

	/**
	 * 获取 notLeafSelectedEnabled 特性 (boolean) 值.
	 * 
	 * @return notLeafSelectedEnabled 特性值.
	 * @see #setNotLeafSelectedEnabled
	 */
	public boolean isNotLeafSelectedEnabled() {
		return fieldNotLeafSelectedEnabled;
	}

	/**
	 * 
	 * 创建日期:(2001-11-27 13:30:13)
	 * 
	 * @return boolean
	 */
	public boolean isProcessFocusLost() {
		return processFocusLost;
	}

	/**
	 * 获取 returnCode 特性 (boolean) 值.
	 * 
	 * @return returnCode 特性值.
	 * @see #setReturnCode
	 */
	public boolean isReturnCode() {
		return fieldReturnCode;
	}

	/**
	 * 获取 textFieldVisible 特性 (boolean) 值.
	 * 
	 * @return textFieldVisible 特性值.
	 * @see #setTextFieldVisible
	 */
	public boolean isTextFieldVisible() {
		return fieldTextFieldVisible;
	}

	/**
	 * 
	 * 创建日期:(2003-8-18 11:14:39)
	 * 
	 * @return boolean
	 */
	public boolean isTreeGridNodeMultiSelected() {
		return isTreeGridNodeMultiSelected;
	}

	/**
	 * 
	 * 创建日期:(2004-6-18 15:44:06)
	 * 
	 * @return boolean
	 */
	public boolean isVersionButtonShow() {
		return isVersionButtonShow;
	}

	/**
	 * 注意:覆盖此方法时,应正确指定父窗体,这里用this.getParent()
	 */
	public void onButtonClicked() {
		DataformatPanel pan=(DataformatPanel) parentContainer;
		int index=pan.getJComboBox1().getSelectedIndex();
		System.out.println("------------选中第I个"+index);
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
						&& getRefNodeName().equalsIgnoreCase("日历")) {
					callCalendar();
				} else if (getRefNodeName().equalsIgnoreCase("计算器")) {
					callCalculator();
				} else if (getRefNodeName().equalsIgnoreCase("颜色")) { //
					callColor();
				} else if (getRefNodeName().equalsIgnoreCase("文件")) { //
					callFile();
				} else if (getRefNodeName().equalsIgnoreCase("参照定制")) {
					callRefCostomizedDefine();
				}
			} else {
				// 业务参照
				// 不用再设
				String blur = getUITextField().getText();
				// 进行转换
				// 增加判断条件,以免在点击退出时清空TextField的值
				if (getRefModel() == null) {
					System.out
							.println("UIRefPane.getModle()==null, 可能的原因为参照没有正确初始化，请检查参照定义。参照的名称为"
									+ getRefNodeName());
					return;
				}
				// 参照ui相关属性设置
				setUIConfig();
				setRefModelConfig();
				if (isKeyPressed) {
					getRefModel().setBlurValue(blur);
					// 如果按键,系统参照自动进行模糊匹配 2004-01-13增加
					getRef().setAutoBlurMatch(isKeyPressed);
				} else {
					getRefModel().setBlurValue(null); // 没有按键，认为不进行模糊匹配
				}
				// 显示
				m_iReturnButtonCode = getRef().showModal();
				if (getReturnButtonCode() == UFRefManage.ID_OK) {
					doReturnOk();
					((DataformatPanel)parentContainer).doCopy(getRefPK());
				}

			}
			/*
			 * 处理属性变化事件
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
	 * 参照焦点获得后触发事件---这里做显示转换,可覆盖此方法. 创建日期:(2001-5-10 12:35:29)
	 * 
	 * @param e
	 *            java.awt.event.FocusEvent
	 */
	protected void processFocusGained(java.awt.event.FocusEvent e1) {
		// 参照disenble,不做转换
		if (!isEditable()) {
			return;
		}

		// 特殊参照，不转化编码
		if (isSpecialRef()) {
			return;
		}
		// 总账系统的凭证中,辅助核算允许录入空值,但是在进行账簿查询时,辅助项查询条件录入框应区分两种情况:1)录入null:表示对辅助项值为空值的数据进行查询统计,2)不设置任何查询条件值(即查询条件录入框为空):表示查询所有辅助项的数据.现在第一种情况无法实现,主要是由于总账调用的是客户化的基础数据的参照,在进行检查时不能识别“null”值,希望客户化的参照为我们提供一个处理方式,能够解决这个问题.
		String text = getUITextField().getText();
		if (text != null && text.equalsIgnoreCase(IRefConst.NULL)) {
			getUITextField().setText(text);
			fieldText = text;
			return;
		}
		// 修改,如果不自动匹配,应该不判断getRefCode()的值 2004-05-13 待测
		if (!isAutoCheck())
			return;

		String[] showStrs = null;
		switch (getRefInputType()) {
		case REFINPUTTYPE_CODE: // 代码参照
			showStrs = getRefCodes();
			break;
		case REFINPUTTYPE_NAME: // 名称参照
			showStrs = getRefNames();
			break;
		case REFINPUTTYPE_MNECODE: // 助记码参照
			break;
		case 3: // 其他
		default: // 不处理
		}
		setTextFieldShowStrs(getComposedString(showStrs));
		return;
	}

	/**
	 * 
	 * 创建日期:(2001-11-27 13:10:03)
	 */
	public void processFocusLost() {

		String sText = getUITextField().getText();

		// 清空时特殊处理
		if (isKeyPressed && (isNull(sText))) {

			clearText(sText);
			// 变量状态
			setfocusLostVariableState();
			return;
		}

		if (!isSpecialRef()) {

			if (isKeyPressed) {

				doKeyPressed();

			} else {

				// 没按键则允许转换：自动匹配且返回名称
				if (!isReturnCode() && isAutoCheck()) {

					processFocusLost(null);

				}
			}
		}
		// 日历要特殊处理
		if (isCalendar() && isKeyPressed) {
			doKeyPressed();
		}

		doFireValueChanged();
		// 变量状态
		setfocusLostVariableState();

	}

	private void setfocusLostVariableState() {
		isButtonClicked = false;
		// 设置按键
		isKeyPressed = false;
		// 处理焦点丢失事件完成
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
	 * 参照焦点丢失后触发事件---这里做显示转换,可覆盖此方法. 创建日期:(2001-5-10 12:38:33)
	 * 
	 * @param e
	 *            java.awt.event.FocusEvent
	 */
	protected void processFocusLost(java.awt.event.FocusEvent e) {
		setTextFieldShowStrs(getRefShowName());
		// 提示
		// getUITextField().setToolTipText(getText());

		return;
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-11</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setTextFieldShowStrs(String text) {
		// 修改长度,以便显示名称
		getUITextField().setMaxLength(Integer.MAX_VALUE);
		getUITextField().setText(text);
		// 修改长度
		getUITextField().setMaxLength(getMaxLength());
		//
		setToolTipText();
	}

	/**
	 * 除去 nc.ui.pub.beans.ValueChangedListener.
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
	 * 设置 autoCheck 特性 (boolean) 值.
	 * 
	 * @param autoCheck
	 *            新的特性值.
	 * @see #getAutoCheck
	 */
	public void setAutoCheck(boolean autoCheck) {
		boolean oldValue = fieldAutoCheck;
		fieldAutoCheck = autoCheck;
		firePropertyChange("autoCheck", new Boolean(oldValue), new Boolean(
				autoCheck));
	}

	/**
	 * 界面设置数据. 创建日期:(2001-4-27 11:44:02)
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
		// 设置model的控制属性
		setRefModelConfig();
		Vector vData = null;
		if (isMultiSelectedEnabled() && isMultiInput(strBlurValue)){
			//多项输入，只支持code列上做匹配。
			vData = getRefModel().matchData(getRefModel().getRefCodeField(),getMultiInputStrs(strBlurValue));
		}else{
//			getRefModel().setBlurValue(strBlurValue);
			// 根据PK获取
			vData = getRefModel().matchBlurData(strBlurValue);
		}
	
		// 模糊匹配不支持多选匹配的记录
		if (vData != null && vData.size() > 1) {
			return;
		}
		// 设置
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
	 * 设置 buttonFireEvent 特性 (boolean) 值.
	 * 
	 * @param buttonFireEvent
	 *            新的特性值.
	 * @see #getButtonFireEvent
	 */
	public void setButtonFireEvent(boolean buttonFireEvent) {
		boolean oldValue = fieldButtonFireEvent;
		fieldButtonFireEvent = buttonFireEvent;
		firePropertyChange("buttonFireEvent", new Boolean(oldValue),
				new Boolean(buttonFireEvent));
	}

	/**
	 * 设置 buttonVisible 特性 (boolean) 值.
	 * 
	 * @param buttonVisible
	 *            新的特性值.
	 * @see #getButtonVisible
	 */
	public void setButtonVisible(boolean buttonVisible) {
		boolean oldValue = fieldButtonVisible;
		fieldButtonVisible = buttonVisible;
		getUIButton().setVisible(buttonVisible);
		if (!isButtonVisible()) {
			// 按钮不可视时,1.返回代码 2.不检测
			setReturnCode(true);
			setAutoCheck(false);
		}
		firePropertyChange("buttonVisible", new Boolean(oldValue), new Boolean(
				buttonVisible));
	}

	/**
	 * 设置 cacheEnabled 特性 (boolean) 值.
	 * 
	 * @param cacheEnabled
	 *            新的特性值.
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
	 * 设置 color 特性 (java.awt.Color) 值.
	 * 
	 * @param color
	 *            新的特性值.
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
	 * 创建日期:(2001-3-14 18:29:45)
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
	 * 设置 editable 特性 (boolean) 值.
	 * 
	 * @param editable
	 *            新的特性值.
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
	 * 创建日期:(2001-3-17 10:32:25)
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
	 * 创建日期:(2004-6-18 15:44:06)
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
	 * 创建日期:(2004-5-23 11:20:46)
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
	 * 创建日期:(2004-6-25 15:47:14)
	 * 
	 * @param newIsBatchData
	 *            boolean
	 */
	public void setIsBatchData(boolean newIsBatchData) {
		isBatchData = newIsBatchData;
	}

	/**
	 * 设置是否为自定义参照 创建日期:(2001-9-3 15:58:36)
	 * 
	 * @since :V1.00
	 * @param isCustomDefined
	 *            boolean
	 */
	public void setIsCustomDefined(boolean isCustomDefined) {
		m_isCustomDefined = isCustomDefined;
	}

	/**
	 * 设置 maxDateStr 特性 (java.lang.String) 值.
	 * 
	 * @param maxDateStr
	 *            新的特性值.
	 * @see #getMaxDateStr
	 */
	public void setMaxDateStr(java.lang.String maxDateStr) {
		// String oldValue = fieldMaxDateStr;
		fieldMaxDateStr = maxDateStr;
		// firePropertyChange("maxDateStr", oldValue, maxDateStr);
	}

	/**
	 * 
	 * 创建日期:(2001-3-14 18:25:15)
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
	 * 设置 maxValue 特性 (double) 值.
	 * 
	 * @param maxValue
	 *            新的特性值.
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
	 * 设置 minDateStr 特性 (java.lang.String) 值.
	 * 
	 * @param minDateStr
	 *            新的特性值.
	 * @see #getMinDateStr
	 */
	public void setMinDateStr(java.lang.String minDateStr) {
		// String oldValue = fieldMinDateStr;
		fieldMinDateStr = minDateStr;
		// firePropertyChange("minDateStr", oldValue, minDateStr);
	}

	/**
	 * 设置 minValue 特性 (double) 值.
	 * 
	 * @param minValue
	 *            新的特性值.
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
	 * 设置 multiSelectedEnabled 特性 (boolean) 值.
	 * 
	 * @param multiSelectedEnabled
	 *            新的特性值.
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
	 * 设置 notLeafSelectedEnabled 特性 (boolean) 值.
	 * 
	 * @param notLeafSelectedEnabled
	 *            新的特性值.
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
	 * 创建日期:(2001-3-14 18:27:45)
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
	 * 创建日期:(2002-6-12 11:32:15)
	 * 
	 * @param newParentContainer
	 *            java.awt.Container
	 */
	public void setParentContainer(java.awt.Container newParentContainer) {
		parentContainer = newParentContainer;
	}

	/**
	 * 根据主键获取参照记录. 创建日期:(2001-4-27 11:44:02)
	 * 
	 * @param pk
	 *            java.lang.String
	 */
	public void setPK(Object pk) {
		if (pk == null) {
			// 调用的是setPK(String pk)
			setPK(null);
		} else {
			setPK(pk.toString());
		}
	}

	/**
	 * 根据主键获取参照记录. 创建日期:(2001-4-27 11:44:02) 设置pk时，不触发属性变化事件
	 * 
	 * @param pk
	 *            java.lang.String
	 */
	public void setPK(String pk) {
		// 根据PK获取
		// if (isSpecialRef()) {
		// // setRefPK(pk);
		// // setRefCode(pk);
		// // setRefName(pk);
		//			
		// if (getRefNodeName().equalsIgnoreCase("日历")) {
		// getUITextField().setText(pk);
		// fieldText = pk;
		// }
		// return;
		// }
		//
		// // 参照的getRef().getRefModelVariable() == null ,说明是第一次调用。
		// if (m_refManage == null && (pk == null || pk.trim().length() == 0)) {
		// return;
		// }
		//
		// // 调试使用
		// if (getRefModel() == null) {
		// Debug
		// .debug("UIRefPane.setPK(),
		// 但参照的Model为空，可能的原因为参照没有正确初始化，请检查参照定义。参照的名称为"
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

		// 说明是第一次调用,在初始化参照,不予理睬

		if (pk == null || pk.trim().length() == 0) {

			resetToNull();
			return;

		}

		setPKs(new String[] { pk });

	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-7-13</strong>
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
	 * 从refModel读取数据。
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-3-17</strong>
	 * <p>
	 * 
	 * @param boolean
	 *            isNeedInitSelectedData 是否要初始化选择的数据
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void resetDataFromModel(boolean isNeedInitSelectedData) {
		if (getRefModel() == null) {
			return;
		}
		// 需要初始化已选择的Vector
		if (isNeedInitSelectedData) {
			setSelectedData(getRefModel().getSelectedData());
			fieldText = getRefShowName();
		}
		//
		setTextFieldShowStrs(getRefShowName());

	}

	/**
	 * 
	 * 创建日期:(2003-4-11 10:15:37)
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
	 * 创建日期:(2001-11-27 13:30:13)
	 * 
	 * @param newProcessFocusLost
	 *            boolean
	 */
	public void setProcessFocusLost(boolean newProcessFocusLost) {
		processFocusLost = newProcessFocusLost;
	}

	/**
	 * 
	 * 创建日期:(2001-5-10 11:25:32)
	 * 
	 * @param refCode
	 *            java.lang.String
	 * @deprecated
	 */
	protected void setRefCode(String refCode) {
		fieldRefCode = refCode;
	}

	/**
	 * 设置 refInputType 特性 (int) 值.
	 * 
	 * @param refInputType
	 *            新的特性值.
	 * @see #getRefInputType 请使用setReturnCode()来定制显示code or name
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
	 * 创建日期:(01-4-12 22:04:03)
	 * 
	 * @param model
	 *            nc.ui.bd.ref.IRefModel
	 */
	public void setRefModel(AbstractRefModel model) {
		// 修改节点名
		getRef().setRefModel(model);
		if (model != null) {
			fieldRefNodeName = model.getRefNodeName();
			if (fieldRefNodeName == null
					|| fieldRefNodeName.trim().length() == 0) {
				fieldRefNodeName = model.getClass().getName();
			}
			// 认为是自定义参照
			setIsCustomDefined(true);

			model.addChangeListener(this);

			model.setCacheEnabled(isCacheEnabled());
		}

	}

	/**
	 * 
	 * 创建日期:(2001-5-10 11:26:06)
	 * 
	 * @param refName
	 *            java.lang.String
	 * @deprecated
	 */
	protected void setRefName(String refName) {
		fieldRefName = refName;
	}

	/**
	 * 设置 refNodeName 特性 (java.lang.String) 值.
	 * 
	 * @param refNodeName
	 *            新的特性值.
	 * @see #getRefNodeName
	 */
	public void setRefNodeName(java.lang.String refNodeName) {
		if (refNodeName != null) {
			if (refNodeName.equalsIgnoreCase("日历")) {
				setTextType("TextDate");
				setMaxLength(10);
				getUITextField().setBackground(colorBackgroundDefault);
				setEditable(true);

			} else if (refNodeName.equalsIgnoreCase("计算器")) {
				setTextType("TextDbl");
				getUITextField().setBackground(colorBackgroundDefault);
				setEditable(true);
				setNumPoint(10);
				setMaxLength(28);
				setAutoCheck(false);
				getUITextField().setAutoParse(false);

			} else if (refNodeName.equalsIgnoreCase("颜色")) {
				setEditable(false);
				getUITextField().setBackground(fieldColor);
			} else if (refNodeName.equalsIgnoreCase("文件")) {
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
		// 清除UFRefManag
		m_refManage = null;

		// 是否是批取数据参照
		if (isSpecialRef()) {
			setIsBatchData(false);
		}
		firePropertyChange("refNodeName", oldValue, refNodeName);

		setButtonIcon();
	}

	/**
	 * 
	 * 创建日期:(2001-5-10 11:24:52)
	 * 
	 * @param refPK
	 *            java.lang.String
	 * @deprecated
	 */
	protected void setRefPK(String refPK) {
		fieldRefPK = refPK;
	}

	/**
	 * 设置 refType 特性 (int) 值.
	 * 
	 * @param refType
	 *            新的特性值.
	 * @see #getRefType
	 */
	public void setRefType(int refType) {
		// int oldValue = fieldRefType;
		fieldRefType = refType;
		// 清除原UFRefManage
		m_refManage = null;
		getRef().setRefType(refType);
		//
		// firePropertyChange("refType", new Integer(oldValue), new Integer(
		// refType));
	}

	/**
	 * 
	 * 创建日期:(2001-8-25 10:44:30)
	 * 
	 * @param refUI
	 *            nc.ui.bd.ref.IRefUI
	 *            用该方法设置自定义UI，可能会导致参照界面没有parent，参照界面会隐藏到主界面后面。
	 *            建议使用setRefUICreator(IRefUICreator creator) 设置RefUi。
	 * 
	 * Deprecated
	 */
	public void setRefUI(nc.ui.bd.ref.IRefUINew refUI) {
		getRef().setRefUI(refUI);
	}

	/**
	 * 设置 returnCode 特性 (boolean) 值.
	 * 
	 * @param returnCode
	 *            新的特性值.
	 * @see #getReturnCode
	 */
	public void setReturnCode(boolean returnCode) {
		// boolean oldValue = fieldReturnCode;
		fieldReturnCode = returnCode;
		// firePropertyChange("returnCode", new Boolean(oldValue), new Boolean(
		// returnCode));
	}

	/**
	 * 设置 selectedData 特性 (java.util.Vector) 值.
	 * 
	 * @param selectedData
	 *            新的特性值.
	 * @see #getSelectedData
	 */
	public void setSelectedData(java.util.Vector selectedData) {
		// java.util.Vector oldValue = fieldSelectedData;
		fieldSelectedData = selectedData;
		// firePropertyChange("selectedData", oldValue, selectedData);
	}

	/**
	 * 
	 * 创建日期:(2001-7-24 15:39:38)
	 * 
	 * @param newStrPatch
	 *            java.lang.String
	 */
	public void setStrPatch(String newStrPatch) {
		if (getRefModel() != null)
			getRef().getRefModel().setStrPatch(newStrPatch);
	}

	/**
	 * 设置 table 特性 (nc.ui.pub.beans.UITable) 值.
	 * 
	 * @param table
	 *            新的特性值.
	 * @see #getTable
	 */
	public void setTable(UITable table) {
		// UITable oldValue = fieldTable;
		fieldTable = table;
		// firePropertyChange("table", oldValue, table);
	}

	/**
	 * 设置文本框值触发事件. 创建日期:(2001-3-14 18:12:45)
	 * 
	 * @param sText
	 *            java.lang.String
	 */
	public void setText(String sText) {
		setTextFieldShowStrs(sText);

		// 通过本方法清空参数旧数据
		if (sText == null || sText.trim().length() == 0) {
			if (getRefModel() != null) {
				getRefModel().setSelectedData(null);
			}
		}
		// 属性变化事件
		doFireValueChanged();
	}

	/**
	 * 设置 textFieldVisible 特性 (boolean) 值.
	 * 
	 * @param textFieldVisible
	 *            新的特性值.
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
	 * 创建日期:(2001-3-14 18:12:45)
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
	 * 设置 toolTipText 特性 (java.lang.String) 值.
	 * 
	 * @param toolTipText
	 *            新的特性值.
	 * @see #getToolTipText
	 */

	/**
	 * 
	 * 创建日期:(2003-8-18 11:14:39)
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
	 * 设置文本框值不触发事件. 创建日期:(2001-3-14 18:12:45)
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
	 * 创建日期:(2004-6-18 15:44:06)
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
	 * 创建日期:(2001-5-11 14:15:37)
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
	 * （非 Javadoc）
	 * 
	 * @see javax.swing.JComponent#requestFocusInWindow()
	 */
	@Override
	public boolean requestFocusInWindow() {
		// TODO 自动生成方法存根
		return getUITextField().requestFocusInWindow();
	}

	/*
	 * 如果父为表格表格，该方法保证输入值到参照上。
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

			if (getRefNodeName().equalsIgnoreCase("日历")
					|| getRefNodeName().equals("参照定制")) {
				getUITextField().setText(pk);
				fieldText = pk;
			}
			return;
		}

		// 说明是第一次调用。
		if ((pks == null || pks.length == 0)) {
			resetToNull();
			return;
		}

		// 调试使用
		if (getRefModel() == null) {
			Debug
					.debug("UIRefPane.setPK(), 但参照的Model为空，可能的原因为参照没有正确初始化，请检查参照定义。参照的名称为"
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
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-10</strong>
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
	 * 此处插入方法说明。 创建日期：(2005-11-9 11:22:57)
	 * 
	 * @return int
	 */
	public int getMatchMode() {
		return matchMode;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2005-11-9 11:22:57)
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
		// 只刷新界面数据，没有触发属性变化事件

		resetDataFromModel(true);

	}

	/**
	 * @return 返回 isSealedDataButtonShow。
	 */
	public boolean isSealedDataButtonShow() {
		return isSealedDataButtonShow;
	}

	/**
	 * @param isSealedDataButtonShow
	 *            要设置的 isSealedDataButtonShow。
	 */
	public void setSealedDataButtonShow(boolean isSealedDataButtonShow) {
		this.isSealedDataButtonShow = isSealedDataButtonShow;
		// getRef().setSealedDataButtonShow(isSealedDataButtonShow);
	}

	/**
	 * 参照UI相关的属性设置
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
	 * @return 返回 pk_bdinfo。
	 */
	public String getPk_bdinfo() {
		return pk_bdinfo;
	}

	/**
	 * @param pk_bdinfo
	 *            要设置的 pk_bdinfo。
	 */
	public void setPk_bdinfo(String pk_bdinfo) {
		this.pk_bdinfo = pk_bdinfo;
	}

	/**
	 * @return 返回 refUIConfig。
	 */
	public RefUIConfig getRefUIConfig() {
		if (refUIConfig == null) {
			refUIConfig = new RefUIConfig();
		}
		return refUIConfig;
	}

	private Icon getRefIcon(String state) {
		String iconName = getRefNodeName();

		if (!isSpecialRef() || "文本框".equals(getRefNodeName())
				|| "参照定制".equals(getRefNodeName()) || "".equals(iconName)) {
			iconName = "参照";
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
	 * @return 返回 refUI。
	 */
	public Object getRefUI() {
		if (isSpecialRef()) {
			return refUI;
		}

		return getRef().getRefUI();

	}

	/**
	 * @param refUI
	 *            要设置的 refUI。
	 */
	private void setRefUI(Object refUI) {
		this.refUI = refUI;
	}

	/**
	 * 直接给参照赋值。 减少远程调用
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
		// 构建一条假记录
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

		// 加入动态列
		if (refModel.isDynamicCol()) {

			String[] dynamicColNames = refModel.getDynamicFieldNames();
			if (refModel.getDynamicFieldNames() != null) {

				for (int i = 0; i < dynamicColNames.length; i++) {

					// 加入到显示的列名中
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
	 * 创建日期:(2001-8-25 10:44:30)
	 * 
	 * @param refUI
	 *            nc.ui.bd.ref.IRefUI Deprecated
	 */
	public void setRefUICreator(IRefUICreator creator) {
		getRef().setRefUICreator(creator);
	}

	// 是否包含",",如果包含认为是多项输入
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