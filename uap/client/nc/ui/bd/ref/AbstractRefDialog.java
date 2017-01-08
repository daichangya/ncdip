package nc.ui.bd.ref;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.RefPaneIconFactory;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.vo.bd.ref.RefcolumnVO;
import nc.vo.bd.ref.ReftableVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;

/**
 * 
 * <p>
 * <strong>提供者：</strong>UAP
 * <p>
 * <strong>使用者：</strong>
 * 
 * <p>
 * <strong>设计状态：</strong>详细设计
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public abstract class AbstractRefDialog extends UIDialog implements IRefUINew2,
		ValueChangedListener {

	private static final long serialVersionUID = 1L;

	private JPanel dlgContentPane = null;

	private UIPanel pnl_center = null;

	private UIPanel pnl_component = null;

	private UIButton btn_GridTree = null;

	private RefButtonPanelFactory buttonPanelFactory = null;

	private JComponent dataShowComponent = null;

	private AbstractRefModel refModel = null;

	private RefcolumnVO[] RefcolumnVOs = null;

	private boolean isMutilSelected = false;

	private boolean isMultiCorpRef = false;

	// 是否包含下级控件是否显示
	private boolean isIncludeSubShow = false;

	private boolean isNotLeafSelectedEnabled = true;

	private boolean isVersionButtonShow = false;

	private RefUIConfig refUIConfig = null;

	// 是否过滤按钮显示
	private boolean isFilterDlgShow = false;

	private int refType = IRefConst.GRID;

	private final String GRID = "grid";

	private final String TREE = "tree";

	private HashMap hm_dataShowCom = new HashMap();

	private EventHandler eventHandler = new EventHandler();

	private IRefCardInfoComponent refCardInfoComponent = null;

	private boolean okBtnTreeEnableState = true;

	//
	private boolean isRefreshBtnClicked = false;

	/**
	 * resolution 1024*768 refDialog default size
	 */
	int width = 658;

	int height = 390;

	private IDynamicColumn dynamicColClass;

	public AbstractRefDialog(java.awt.Container parent, AbstractRefModel model,
			RefUIConfig refUIConfig) {
		super(parent, model.getRefTitle());
		this.refUIConfig = refUIConfig;
		this.refModel = model;
		initialize();
	}

	public void setFilterDlgShow(boolean isFilterDlgShow) {
		this.isFilterDlgShow = isFilterDlgShow;

	}

	public void setVersionButtonShow(boolean isVersionButtonShow) {
		this.isVersionButtonShow = isVersionButtonShow;

	}

	public AbstractRefModel getRefModel() {
		// TODO 自动生成方法存根
		return refModel;
	}

	public void setIncludeSubShow(boolean newIncludeSubShow) {
		// TODO 自动生成方法存根
		isIncludeSubShow = newIncludeSubShow;
	}

	public void setMultiCorpRef(boolean isMultiCorpRef) {
		// TODO 自动生成方法存根

	}

	public void setMultiSelectedEnabled(boolean isMultiSelectedEnabled) {
		// TODO 自动生成方法存根

	}

	public void setNotLeafSelectedEnabled(boolean newNotLeafSelectedEnabled) {
		isNotLeafSelectedEnabled = newNotLeafSelectedEnabled;

	}

	public void setRefModel(AbstractRefModel refModel) {
		this.refModel = refModel;

	}

	public void setTreeGridNodeMultiSelected(boolean isMulti) {
		// TODO 自动生成方法存根

	}

	/**
	 * Comment
	 */
	protected void onButtonColumn() {

		UFRefColumnsDlg refColumnsDlg = new UFRefColumnsDlg(this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("ref",
						"UPPref-000340")/* @res "栏目选择" */, getRefModel());

		if (refColumnsDlg.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
			// 栏目变化，带公式的数据要转换为Name。这里可以改进提高效率，如果栏目变化且包含有公式，才进行公式执行
			// 考虑用户的实际应用，变化栏目属于小概率事件，这里简单处理。
			
			getRefModel().setShownColumns(refColumnsDlg.getSelectedColumns());
			getRefModel().reloadData();
			
			setRefcolumnVOs(null);
			setColumnSeq();
			hm_dataShowCom.clear();
			//
		}
		refColumnsDlg.destroy();

	}

	/**
	 * Comment
	 */
	protected void onButtonExit() {
		closeCancel();
		return;
	}

	protected void onButtonOK() {
	
		getRefModel().setSelectedData(getIRefDataComponent().getSelectData());
		closeOK();

	}

	protected void onButtonMaintenance() {
		//界面选中的记录
		Object[] selectPks = getRefModel().getValues(
				getRefModel().getPkFieldCode(),
				getIRefDataComponent().getSelectData());
		
		RefAddDocument.getInstance().openDocFrame(
				this,
				getRefModel(),
				new Object[] { selectPks,
						getIRefDataComponent().getSelectData() });
		onButtonRefresh();
	}

	public void onButtonRefresh() {
		// hm_dataShowCom.clear();
		clearOtherDataShowCom();
		getIRefDataComponent().setData(getRefModel().reloadData());
		getIRefDataComponent().requestComponentFocus();
		getIRefDataComponent().setMatchedPKs(getRefModel().getPkValues());
		getBtnOK().setEnabled(true);

	}

	// 子类实现
	protected void onButtonFilter() {

	}

	protected void onButtonGridTree() {
		if (!getBtn_GridTree().isVisible()) {
			return;
		}
		boolean isNeedSetData = false;
		JComponent com = null;

		if (GRID.equals(getBtn_GridTree().getName())) {
			setBtn_GridTreeState(TREE);
			// 切换为表，Model中包含下级的开关设为False
			getChkIncludeSubNode().setVisible(false);
			getRefModel().setIncludeSub(false);
			RefGridComponent gridCom = (RefGridComponent) hm_dataShowCom
					.get(new Integer(IRefConst.GRID));
			if (gridCom == null) {
				gridCom = new RefGridComponent(this);

				isNeedSetData = true;

			}

			com = gridCom;
			okBtnTreeEnableState = getBtnOK().isEnabled();
			getBtnOK().setEnabled(true);

		} else if (TREE.equals(getBtn_GridTree().getName())) {
			getBtnOK().setEnabled(okBtnTreeEnableState);
			setBtn_GridTreeState(GRID);
			getChkIncludeSubNode().setVisible(
					getRefUIConfig().isIncludeSubShow());
			// 切换为树，Model中包含下级的开关设为checkbox选中的值
			getRefModel().setIncludeSub(getChkIncludeSubNode().isSelected());
			RefTreeComponent treeCom = (RefTreeComponent) hm_dataShowCom
					.get(new Integer(IBusiType.TREE));
			if (treeCom == null) {
				treeCom = new RefTreeComponent(this);

				isNeedSetData = true;

			}

			com = treeCom;

		}

		setDataShowComponent(com);

		if (isNeedSetData) {
			initDataToRefDataComponent();
		}

		addDataShowComponent();

	}

	protected void onComboBoxColumnItemStateChanged(
			java.awt.event.ItemEvent itemEvent) {

		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {

			if (getTfLocate().getText() != null
					&& getTfLocate().getText().trim().length() > 0) {
				getIRefDataComponent().blurInputValue(null, null);
			}
			getTfLocate().setText("");
			getTfLocate().grabFocus();

		}

	}

	protected void onTextFieldLocateFocusLost() {
		// int index = getCbbColumn().getSelectedIndex();
		// String fieldCode = getRefModel().getFieldCode()[getRefModel()
		// .getShownColumns()[index]];
		String fieldCode = ((RefcolumnVO) getCbbColumn().getSelectedItem())
				.getFieldname();
		String value = getTfLocate().getText();
		getIRefDataComponent().matchTreeNode(fieldCode, value);

	}

	protected void onButtonQuery() {

		String className = getRefModel().getRefQueryDlgClaseName();
		Object interfaceClass = null;
		IRefQueryDlg queryDlg = null;

		// 是否实现接口检查
		try {
			Class modelClass = Class.forName(className);
			java.lang.reflect.Constructor cs = null;
			try { // 用公司做构造子
				cs = modelClass.getConstructor(new Class[] { Container.class });
				interfaceClass = cs.newInstance(new Object[] { this });
			} catch (NoSuchMethodException ee) { // 缺省构造
				interfaceClass = modelClass.newInstance();
			}
		} catch (Exception e) {
			Debug.error(e.getMessage(), e);
			return;
		}
		// 类型转换
		if (interfaceClass == null) {
			return;
		}
		if (interfaceClass instanceof IRefQueryDlg) {
			queryDlg = (IRefQueryDlg) interfaceClass;
			if (interfaceClass instanceof IRefQueryDlg2) {
				((IRefQueryDlg2) queryDlg).setRefModel(getRefModel());
			}
		} else {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("ref", "UPPref-000341")/* @res "错误" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("ref",
							"UPPref-000366")/*
											 * @res
											 * "未实现IRefQueryDlg或IRefQueryDlg2接口"
											 */);
			return;
		}

		// 显示对话框

		queryDlg.setParent(this);
		queryDlg.setPk_corp(getRefModel().getPk_corp());
		queryDlg.showModal();
		if (queryDlg.getResult() == UIDialog.ID_OK) {
			getRefModel().setQuerySql(queryDlg.getConditionSql());
			Vector vDataAll = getRefModel().getRefData();

			if (vDataAll == null)
				vDataAll = new Vector();
			getIRefDataComponent().setData(vDataAll);

		}
		getRefModel().setQuerySql(null);

	}

	protected void onButtonSimpleQuery() {

	}

	protected void onChkSealedDataButton() {
		getRefModel().setSealedDataShow(getChkSealedDataShow().isSelected());
		onButtonRefresh();
	}

	private void initialize() {
		try {
			setName("AbstrectRefDialog");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

			setDefaultSize();

			setResizable(true);
			setContentPane(getDlgContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// 注册快捷键
		registerKey(getDlgContentPane());
		// 初始化连接
		initConnections();

	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setDefaultSize() {
		// 默认值
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		width = size.width * 51 / 100;
		height = size.height * 51 / 100;

		setSize(width, height);
	}

	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		System.out.println("--------- 未捕捉到的异常 ---------");
		exception.printStackTrace(System.out);
	}

	/**
	 * @return 返回 btnColumn。
	 */
	private nc.ui.pub.beans.UIButton getBtnColumn() {

		return getButtonPanelFactory().getBtnColumn();

	}

	/**
	 * @return 返回 btnExit。
	 */
	private nc.ui.pub.beans.UIButton getBtnExit() {

		return getButtonPanelFactory().getBtnExit();
	}

	/**
	 * @return 返回 btnMaintenanceDoc。
	 */
	private UIButton getBtnMaintenanceDoc() {

		return getButtonPanelFactory().getBtnMaintenanceDoc();

	}

	/**
	 * @return 返回 btnOK。
	 */
	public nc.ui.pub.beans.UIButton getBtnOK() {

		return getButtonPanelFactory().getBtnOK();
	}

	/**
	 * @return 返回 btnQuery。
	 */
	private UIButton getBtnQuery() {

		return getButtonPanelFactory().getBtnQuery();
	}

	/**
	 * @return 返回 btnRefresh。
	 */
	private nc.ui.pub.beans.UIButton getBtnRefresh() {

		return getButtonPanelFactory().getBtnRefresh();
	}

	/**
	 * @return 返回 btnSimpleQuery。
	 */
	private UIButton getBtnSimpleQuery() {

		return getButtonPanelFactory().getBtnSimpleQuery();
	}

	/**
	 * @return 返回 dlgContentPane。
	 */
	private javax.swing.JPanel getDlgContentPane() {
		if (dlgContentPane == null) {
			dlgContentPane = new UIPanel();
			dlgContentPane.setName("dlgContentPane");
			dlgContentPane.setBorder(new EmptyBorder(5, 5, 0, 5));
			dlgContentPane.setLayout(new java.awt.BorderLayout());
			dlgContentPane.add(getPnl_center(), "Center");
			dlgContentPane.add(getPnl_refCorp(), "North");
			dlgContentPane.add(getPnl_south(), "South");
		}
		return dlgContentPane;
	}

	/**
	 * @return 返回 pnl_center。
	 */

	private UIPanel getPnl_center() {
		if (pnl_center == null) {
			pnl_center = new UIPanel();
			pnl_center.setLayout(new BorderLayout());
			pnl_center.add(getPnl_locate_btn(), "North");
			pnl_center.add(getPnl_component(), "Center");
		}
		return pnl_center;
	}

	private nc.ui.pub.beans.UIPanel getPnl_south() {

		return getButtonPanelFactory().getPnl_south(true);
	}

	/**
	 * @return 返回 tfLocate。
	 */
	private nc.ui.pub.beans.UITextField getTfLocate() {

		return getButtonPanelFactory().getTfLocate();
	}

	/**
	 * @return 返回 cbbColumn。
	 */
	private nc.ui.pub.beans.UIComboBox getCbbColumn() {

		return getButtonPanelFactory().getCbbColumn();
	}

	private void initConnections() {

		getBtnRefresh().addActionListener(eventHandler);
		getBtnColumn().addActionListener(eventHandler);
		getBtnOK().addActionListener(eventHandler);
		getBtnExit().addActionListener(eventHandler);
		getBtnQuery().addActionListener(eventHandler);
		getBtn_GridTree().addActionListener(eventHandler);

//		getTfLocate().addKeyListener(eventHandler);
		getTfLocate().addFocusListener(eventHandler);
		getBtnMaintenanceDoc().addActionListener(eventHandler);
		getBtnFilter().addActionListener(eventHandler);
		// getCbbColumn().addItemListener(eventHandler);
		getChkSealedDataShow().addActionListener(eventHandler);
		getRefCorp().addValueChangedListener(this);

	}

	private void registerKey(JComponent c) {
		// Enter key
		InputMap map = c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = c.getActionMap();

		// ESCAPE
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false),
				ShortCutKeyAction.KEY_REFCOLUMNDIALOG_ESCAPE);
		am.put(ShortCutKeyAction.KEY_REFCOLUMNDIALOG_ESCAPE,
				new ShortCutKeyAction(ShortCutKeyAction.VK_ESCAPE));
		// A
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 8, false),
				ShortCutKeyAction.KEY_REFCOLUMNDIALOG_C);
		am.put(ShortCutKeyAction.KEY_REFCOLUMNDIALOG_C, new ShortCutKeyAction(
				ShortCutKeyAction.VK_C));
		// D
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, 8, false),
				ShortCutKeyAction.KEY_REFCOLUMNDIALOG_O);
		am.put(ShortCutKeyAction.KEY_REFCOLUMNDIALOG_O, new ShortCutKeyAction(
				ShortCutKeyAction.VK_O));

		// L
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 8, false),
				ShortCutKeyAction.KEY_REFCOLUMNDIALOG_Q);
		am.put(ShortCutKeyAction.KEY_REFCOLUMNDIALOG_Q, new ShortCutKeyAction(
				ShortCutKeyAction.VK_Q));
		// R
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 8, false),
				ShortCutKeyAction.KEY_REFCOLUMNDIALOG_R);
		am.put(ShortCutKeyAction.KEY_REFCOLUMNDIALOG_R, new ShortCutKeyAction(
				ShortCutKeyAction.VK_R));

		// X
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 8, false),
				ShortCutKeyAction.KEY_REFCOLUMNDIALOG_X);
		am.put(ShortCutKeyAction.KEY_REFCOLUMNDIALOG_X, new ShortCutKeyAction(
				ShortCutKeyAction.VK_X));
		// M
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 8, false),
				ShortCutKeyAction.KEY_REFCOLUMNDIALOG_M);
		am.put(ShortCutKeyAction.KEY_REFCOLUMNDIALOG_M, new ShortCutKeyAction(
				ShortCutKeyAction.VK_M));

		// M
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 8, false),
				ShortCutKeyAction.KEY_REFCOLUMNDIALOG_T);
		am.put(ShortCutKeyAction.KEY_REFCOLUMNDIALOG_T, new ShortCutKeyAction(
				ShortCutKeyAction.VK_T));

	}

	class ShortCutKeyAction extends AbstractAction {

		int keycode = -1;

		final static int VK_ESCAPE = KeyEvent.VK_ESCAPE;

		final static int VK_C = KeyEvent.VK_C;

		final static int VK_O = KeyEvent.VK_O;

		final static int VK_Q = KeyEvent.VK_Q;

		final static int VK_R = KeyEvent.VK_R;

		final static int VK_X = KeyEvent.VK_X;

		final static int VK_M = KeyEvent.VK_M;

		final static int VK_T = KeyEvent.VK_T;

		final static String KEY_REFCOLUMNDIALOG_ESCAPE = "RefcolumnDialog_ESCEPE";

		final static String KEY_REFCOLUMNDIALOG_C = "RefcolumnDialog_C";

		final static String KEY_REFCOLUMNDIALOG_O = "RefcolumnDialog_O";

		final static String KEY_REFCOLUMNDIALOG_Q = "RefcolumnDialog_Q";

		final static String KEY_REFCOLUMNDIALOG_R = "RefcolumnDialog_R";

		final static String KEY_REFCOLUMNDIALOG_X = "RefcolumnDialog_X";

		final static String KEY_REFCOLUMNDIALOG_M = "RefcolumnDialog_M";

		final static String KEY_REFCOLUMNDIALOG_T = "RefcolumnDialog_T";

		public ShortCutKeyAction(int keycode) {
			this.keycode = keycode;
		}

		public void actionPerformed(ActionEvent e) {

			switch (keycode) {
			case VK_ESCAPE:
				onButtonExit();
				break;
			case VK_C:
				onButtonColumn();
				break;
			case VK_O:
				onButtonOK();
				break;
			case VK_Q:
				onButtonQuery();
				break;
			case VK_R:
				onButtonRefresh();
				break;
			case VK_X:
				onButtonExit();
				break;

			case VK_M:
				onButtonMaintenance();
				break;

			case VK_T:
				onButtonGridTree();
				break;
			}
		}
	}

	class EventHandler implements java.awt.event.ActionListener,
			java.awt.event.ItemListener, java.awt.event.KeyListener,
			FocusListener,DocumentListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBtnRefresh()) {
				setRefreshBtnClicked(true);
				onButtonRefresh();
				setRefreshBtnClicked(false);
			}
			if (e.getSource() == getBtnColumn())
				onButtonColumn();
			if (e.getSource() == getBtnOK())
				onButtonOK();
			if (e.getSource() == getBtnExit())
				onButtonExit();
			if (e.getSource() == getBtnQuery())
				onButtonQuery();
			if (e.getSource() == getBtnMaintenanceDoc())
				onButtonMaintenance();
			if (e.getSource() == getBtnFilter())
				onButtonFilter();
			if (e.getSource() == getBtn_GridTree())
				onButtonGridTree();

			if (e.getSource() == getChkSealedDataShow()) {

				onChkSealedDataButton();

			}

			// if (e.getSource() == getTfLocate()) {
			// // 解决汉字定位问题
			// blurInputValue(getTfLocate().getText(),
			// getLocateColumn());
			// }
		};

		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if (e.getSource() == getCbbColumn())
				onComboBoxColumnItemStateChanged(e);
		};

		public void keyPressed(java.awt.event.KeyEvent e) {
			if (e.getSource() == getTfLocate()) {
				if (e.getKeyChar() == java.awt.event.KeyEvent.VK_ESCAPE) {
					String str = getTfLocate().getText();
					if (str == null || str.trim().length() == 0) {
						onButtonExit();
					}
				}
			}
		};

		public void keyReleased(java.awt.event.KeyEvent e) {
//			if (e.getSource() == getTfLocate())
//				uITextFieldLocate_KeyReleased(e);
		}

		public void keyTyped(java.awt.event.KeyEvent e) {
		}

		public void focusGained(FocusEvent e) {
			// TODO 自动生成方法存根

		}

		public void focusLost(FocusEvent e) {
			onTextFieldLocateFocusLost();

		}

		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
//			System.out.println("DocumentEvent:"+e.getType());
//			uITextFieldLocate_documentChanged();
		}

		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
//			System.out.println("DocumentEvent:"+e.getType());
			uITextFieldLocate_documentChanged();
		}

		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
//			System.out.println("DocumentEvent:"+e.getType());
			uITextFieldLocate_documentChanged();
		};
	};

	private void uITextFieldLocate_KeyReleased(java.awt.event.KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_CAPS_LOCK
				|| keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_CONTROL
				|| keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_SHIFT
				|| keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_ALT)
			return;
		String strInput = getTfLocate().getText();
		getIRefDataComponent().blurInputValue(
				getCbbColumn().getSelectedItem().toString(), strInput);
		return;
	}
	
	private void uITextFieldLocate_documentChanged() {
		
		String strInput = getTfLocate().getText();
		getIRefDataComponent().blurInputValue(
				getCbbColumn().getSelectedItem().toString(), strInput);
		return;
	}

	/**
	 * @return 返回 dataShowComponent。
	 */
	public JComponent getDataShowComponent() {
		return dataShowComponent;
	}

	/**
	 * @param dataShowComponent
	 *            要设置的 dataShowComponent。
	 */
	public void setDataShowComponent(JComponent dataShowComponent) {
		this.dataShowComponent = dataShowComponent;
		int refType = ((IRefDataComponent) this.dataShowComponent).getRefType();
		setRefType(refType);
		hm_dataShowCom.put(new Integer(refType), dataShowComponent);

	}

	// 对于可以切换树表的参照，清除非当前的显示控件
	private void clearOtherDataShowCom() {
		int refType = ((IRefDataComponent) this.dataShowComponent).getRefType();
		Object com = hm_dataShowCom.get(new Integer(refType));
		hm_dataShowCom.clear();
		hm_dataShowCom.put(new Integer(refType), com);
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-7-3</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void addDataShowComponent() {
		getPnl_component().removeAll();
		getPnl_component().add(this.dataShowComponent, "Center");
		String refCardClassName = getRefModel()
				.getRefCardInfoComponentImplClass();
		if (refCardClassName != null) {
			getPnl_component().add(
					(JComponent) initRefCardInfoComponent(refCardClassName),
					"East");
		}

		repaint();
	}

	private IRefCardInfoComponent initRefCardInfoComponent(String className) {

		try {

			refCardInfoComponent = (IRefCardInfoComponent) Class.forName(
					className).newInstance();

		} catch (Exception e) {
			Debug.error(e.getMessage(), e);

		}

		return refCardInfoComponent;
	}

	protected IRefDataComponent getIRefDataComponent() {
		return (IRefDataComponent) getDataShowComponent();
	}

	public int showModal() {
		if (getRefModel() == null) {
			return -1;
		}

		setUserSize();

		// 设置显示
		setComponentVisibleState();
		// 初始化动态列信息
		initDynamicColClass();

		setShowIndexToModel();

		initDataToRefDataComponent();

		addDataShowComponent();
		//
		handleSpecial();

		setMultiCorpPK();

		int iResult = super.showModal();

		// 持久化参照的尺寸
		RefUtil.putRefSize(getRefModel().getRefNodeName(), new Dimension(
				getWidth(), getHeight()));

		return iResult;
	}

	private void setShowIndexToModel() {
		ReftableVO vo = getRefModel().getRefTableVO(getRefModel().getPk_corp());
		if (vo != null && vo.getColumnVOs() != null) {

			ArrayList list = new ArrayList();
			for (int i = 0; i < vo.getColumnVOs().length; i++) {
				if (vo.getColumnVOs()[i].getIscolumnshow().booleanValue()) {
					list.add(vo.getColumnVOs()[i].getColumnshowindex());
				}

			}
			if (list.size() > 0) {
				int[] showIndex = new int[list.size()];
				for (int i = 0; i < showIndex.length; i++) {
					showIndex[i] = ((Integer) list.get(i)).intValue();
				}
				getRefModel().setShownColumns(showIndex);
			}

		}
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-9-20</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setMultiCorpPK() {
		if (getPnl_refCorp().isVisible()) {
			getRefCorp().getRefModel().setFilterPks(
					getRefUIConfig().getMultiCorpRefPks());
			getRefCorp().setPK(getRefModel().getPk_corp());
		}
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setUserSize() {
		// 设置参照大小
		Dimension dim = RefUtil.getRefSize(getRefModel().getRefNodeName(),
				new Dimension(width, height));
		width = (int) dim.getWidth();
		height = (int) dim.getHeight();
		setSize(width, height);
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-4-28</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void initDataToRefDataComponent() {

		// Vector vDataAll = getRefModel().getData();
		Vector vDataAll = getData();
		if (getIRefDataComponent().getRefModel() == null) {
			getIRefDataComponent().setRefModel(getRefModel());
		}
		getIRefDataComponent().setData(vDataAll);
		setColumnSeq();

		// 根据 setpks的默认值定位

		getIRefDataComponent().requestComponentFocus();

		getIRefDataComponent().setMatchedPKs(getRefModel().getPkValues());
		if (getRefModel().getPkValues() != null) {
			setRefCardInfoPk(getRefModel().getPkValues()[0] == null ? null
					: getRefModel().getPkValues()[0].toString());
		}

	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-4-27</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setComponentVisibleState() {
		if (RefAddDocument.getInstance().getFunCode(getRefModel()) == null) {
			getBtnMaintenanceDoc().setVisible(false);// 维护按钮是否显示
		}
		if (getRefModel().getRefQueryDlgClaseName() == null) {
			getBtnQuery().setVisible(false);// 高级查询按钮是否显示
			getBtnSimpleQuery().setVisible(false);// 高级查询按钮是否显示
		}

		getPnl_refCorp().setVisible(getRefUIConfig().isMultiCorpRef());
		// 是否是多公司参照
		// if (getRefUIConfig().isMultiCorpRef()) {
		// // 多公司参照用户设置的pks,按用户设置的pks显示。如果没有设置，显示全部公司目录数据
		// getRefCorp().getRefModel().setFilterPks(
		// getRefUIConfig().getMultiCorpRefPks());
		// getRefCorp().setPK(getRefCorp().getRefModel().getPk_corp());
		// }

		// 封存数据是否显示checkbox

		getChkSealedDataShow().setVisible(
				getRefUIConfig().isSealedDataButtonShow());

		if (getRefUIConfig().isSealedDataButtonShow()) {
			getRefModel()
					.setSealedDataShow(getChkSealedDataShow().isSelected());
		}
		// 树型参照相关

		if (getRefType() == IBusiType.TREE) {
			// 表切换按钮是否显示
			if (isNotLeafSelectedEnabled()) {
				getBtn_GridTree().setVisible(
						getRefUIConfig().isNotLeafSelectedEnabled());
				// getPnl_GridTreeRdBtn().setVisible(
				// getRefUIConfig().isNotLeafSelectedEnabled());
				// getRdBtn_Grid().setVisible(
				// getRefUIConfig().isNotLeafSelectedEnabled());
				// getRdBtn_Tree().setVisible(
				// getRefUIConfig().isNotLeafSelectedEnabled());
			}
			// 包含下级按钮是否显示
			if (isIncludeSubShow()) {
				getChkIncludeSubNode().setVisible(
						getRefUIConfig().isIncludeSubShow());

			}
		}
		getBtnColumn().setVisible(getRefUIConfig().isColumnBtnShow());

	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-4-26</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setColumnSeq() {
		setComboBoxData(getRefcolumnVOs());
		getIRefDataComponent().setNewColumnSequence();
	}

	protected RefcolumnVO[] getRefcolumnVOs() {
		if (RefcolumnVOs == null) {
			RefcolumnVOs = RefPubUtil.getColumnSequences(getRefModel());
		}
		return RefcolumnVOs;
	}

	private void setComboBoxData(RefcolumnVO[] items) {
		getCbbColumn().removeAllItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getIscolumnshow().booleanValue()) {
				getCbbColumn().addItem(items[i]);
			}

		}

	}

	/**
	 * @return 返回 isMutilSelected。
	 */
	public boolean isMutilSelected() {
		return isMutilSelected;
	}

	/**
	 * @return 返回 refCorp。
	 */
	public UIRefPane getRefCorp() {

		return getButtonPanelFactory().getRefCorp();
	}

	/**
	 * @return 返回 pnl_refCorp。
	 */
	private UIPanel getPnl_refCorp() {

		return getButtonPanelFactory().getPnl_refCorp();
	}

	/**
	 * @return 返回 isMultiCorpRef。
	 */
	private boolean isMultiCorpRef() {
		return isMultiCorpRef;
	}

	/**
	 * @return 返回 chkSubNode。
	 */
	private UICheckBox getChkIncludeSubNode() {

		return getButtonPanelFactory().getChkIncludeSubNode();
	}

	private UICheckBox getChkSealedDataShow() {

		return getButtonPanelFactory().getChkSealedDataShow();
	}

	/**
	 * 
	 */
	private boolean isIncludeSubShow() {
		return isIncludeSubShow;
	}

	/**
	 * @return 返回 btnFilter。
	 */
	protected UIButton getBtnFilter() {

		return getButtonPanelFactory().getBtnFilter();

	}

	/**
	 * @return 返回 isNotLeafSelectedEnabled。
	 */
	public boolean isNotLeafSelectedEnabled() {
		return isNotLeafSelectedEnabled;
	}

	/**
	 * 
	 */
	public boolean isIncludeSubNode() {
		return isIncludeSubShow() && getChkIncludeSubNode().isSelected();
	}

	private int getRefType() {

		// if (getRefModel() instanceof AbstractRefGridTreeModel) {
		// refType = IBusiType.GRIDTREE;
		// } else if (getRefModel() instanceof AbstractRefTreeModel) {
		// refType = IBusiType.TREE;
		// } else if (getRefModel() instanceof AbstractRefModel) {
		// refType = IBusiType.GRID;
		//
		// }
		return refType;
	}

	/**
	 * 在对话框显示前调用子类的钩子方法
	 */
	abstract void handleSpecial();

	/**
	 * @return 返回 isFilterDlgShow。
	 */
	protected boolean isFilterDlgShow() {
		return isFilterDlgShow;
	}

	/**
	 * @return 返回 btnVersion。
	 */
	protected UIButton getBtnVersion() {

		return getButtonPanelFactory().getBtnVersion();
	}

	/**
	 * @return 返回 isVersionButtonShow。
	 */
	public boolean isVersionButtonShow() {
		return isVersionButtonShow;
	}

	/**
	 * @return 返回 refUIConfig。
	 */
	public RefUIConfig getRefUIConfig() {
		return refUIConfig;
	}

	/**
	 * @param refUIConfig
	 *            要设置的 refUIConfig。
	 */
	public void setRefUIConfig(RefUIConfig refUIConfig) {
		this.refUIConfig = refUIConfig;
	}

	/**
	 * @param refType
	 *            要设置的 refType。
	 */
	public void setRefType(int refType) {
		this.refType = refType;
	}

	/**
	 * @param refcolumnVOs
	 *            要设置的 refcolumnVOs。
	 */
	private void setRefcolumnVOs(RefcolumnVO[] refcolumnVOs) {
		RefcolumnVOs = refcolumnVOs;
	}

	/**
	 * 公司目录参照
	 */
	public void valueChanged(ValueChangedEvent event) {
		String pk = getRefCorp().getRefPK();
		getRefModel().setPk_corp(pk);
		onButtonRefresh();

	}

	protected void processWindowEvent(WindowEvent e) {
		// TODO Auto-generated method stub
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_OPENED) {
			getIRefDataComponent().requestComponentFocus();
		}
		getTfLocate().getDocument().removeDocumentListener(eventHandler);
		getButtonPanelFactory().setTfLocateTextAndColor();
		getTfLocate().getDocument().addDocumentListener(eventHandler);

	}

	/**
	 * 
	 */
	private void initDynamicColClass() {

		String className = getRefModel().getDynamicColClassName();
		if (className == null) {
			return;
		}
		if (dynamicColClass != null) {
			return;
		}
		// 是否实现接口检查
		try {
			dynamicColClass = (IDynamicColumn) Class.forName(className)
					.newInstance();
		} catch (Exception e) {
			Debug.debug(e.getMessage());
			return;

		}

		initDyamicCol();

	}

	private void initDyamicCol() {

		if (getRefModel().isDynamicCol()) {
			if (dynamicColClass == null) {
				return;
			}
			String[][] dynamicFieldNames = dynamicColClass
					.getDynaminColNameAndLoc();
			if (dynamicFieldNames != null) {
				String[] strNames = new String[dynamicFieldNames.length];
				for (int i = 0; i < strNames.length; i++) {
					strNames[i] = dynamicFieldNames[i][0];
				}
				// 设置动态列到model
				getRefModel().setDynamicFieldNames(strNames);

			}

		}

	}

	/**
	 * 
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-6-28</strong>
	 * <p>
	 * 
	 * @param
	 * @return Vector
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private Vector getData() {
		// 树型参照，不用设置数据，model会调用。
		// 树型参照也不支持动态列。
		if (getIRefDataComponent().getRefType() == IBusiType.TREE) {
			return null;
		}
		Vector vDataAll = getRefModel().getRefData();

		if (vDataAll == null)
			vDataAll = new Vector();
		// 记录PK 和 行号的对应关系

		if (vDataAll.size() > 0) {

			// 处理动态列
			if (getRefModel().isDynamicCol()) {

				AbstractRefModel refModel = new ParaRefModel();
				refModel.setFieldCode(getRefModel().getFieldCode());
				refModel.setFieldName(getRefModel().getFieldName());
				refModel.setHiddenFieldCode(getRefModel().getFieldName());
				refModel.setPkFieldCode(getRefModel().getPkFieldCode());
				refModel.setHiddenFieldCode(getRefModel().getHiddenFieldCode());
				refModel.setData(vDataAll);
				refModel.setSelectedData(vDataAll);
				refModel.setIsDynamicCol(true);
				refModel.setDynamicFieldNames(getRefModel()
						.getDynamicFieldNames());
				// refModel.setHtCodeIndex(getRefModel().getHtCodeIndex());
				refModel = dynamicColClass.getDynamicInfo(getRefModel()
						.getUserParameter(), refModel);
				getRefModel().setData(refModel.getSelectedData());
				vDataAll = refModel.getSelectedData();
			}
		}
		return vDataAll;
	}

	/**
	 * @return 返回 pnl_locate_btn。
	 */
	private UIPanel getPnl_locate_btn() {
		return getButtonPanelFactory().getPnl_locate_btn(false,false,false);
	}

	/**
	 * @return 返回 pnl_component。
	 */
	private UIPanel getPnl_component() {

		if (pnl_component == null) {
			pnl_component = new UIPanel();
			pnl_component.setLayout(new BorderLayout());

		}

		return pnl_component;
	}

	/**
	 * @return 返回 btn_GridTree。
	 */
	protected UIButton getBtn_GridTree() {
		if (btn_GridTree == null) {

			btn_GridTree = getButtonPanelFactory().getBtn_GridTree();

		}
		return btn_GridTree;
	}

	private void setBtn_GridTreeState(String state) {

		String name = null;
		String text = null;
		ImageIcon icon = null;

		if (state.equals(GRID)) {
			name = GRID;
			text = nc.ui.ml.NCLangRes.getInstance().getStrByID("ref",
					"UPPref-000501")/* @res "表" */;

			icon = RefPaneIconFactory.getInstance().getImageIcon("参照.表");
		} else if (state.equals(TREE)) {
			name = TREE;
			text = nc.ui.ml.NCLangRes.getInstance().getStrByID("ref",
					"UPPref-000502")/* @res "树 */;
			icon = RefPaneIconFactory.getInstance().getImageIcon("参照.树");
		}
		btn_GridTree.setName(name);
		btn_GridTree.setToolTipText(text + "(ALT+T)");
		btn_GridTree.setText(text);
		btn_GridTree.setPreferredSize(new java.awt.Dimension(50, 20));
		btn_GridTree.setIcon(icon);
	}

	/**
	 * @return 返回 buttonPanelFactory。
	 */
	public RefButtonPanelFactory getButtonPanelFactory() {
		if (buttonPanelFactory == null) {
			buttonPanelFactory = new RefButtonPanelFactory();
		}
		return buttonPanelFactory;
	}

	/**
	 * @return 返回 refCardInfoComponent。
	 */
	private IRefCardInfoComponent getRefCardInfoComponent() {

		return refCardInfoComponent;
	}

	public void setRefCardInfoPk(String pk) {
		if (getRefCardInfoComponent() != null) {
			getRefCardInfoComponent().setPK(pk, getRefModel());

		}
	}

	public boolean isRefreshBtnClicked() {
		return isRefreshBtnClicked;
	}

	public void setRefreshBtnClicked(boolean isRefreshBtnClicked) {
		this.isRefreshBtnClicked = isRefreshBtnClicked;
	}

}
