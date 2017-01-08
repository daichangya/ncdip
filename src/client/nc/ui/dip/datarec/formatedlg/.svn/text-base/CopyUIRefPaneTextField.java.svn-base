package nc.ui.dip.datarec.formatedlg;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import nc.ui.pub.beans.FocusUtils;
import nc.ui.pub.beans.IUIRefPane;
import nc.ui.pub.beans.IUIRefPaneTextField;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIRefPaneTextField;
import nc.ui.pub.beans.table.UIVarLenTextField;
import nc.vo.pub.BusinessException;

public class CopyUIRefPaneTextField  extends UIVarLenTextField implements
		IUIRefPaneTextField {
	public CopyFormatPanl m_UIRefPane;

	//
	protected boolean isFocusLostToButton = false;

	// for bill
	private FocusListener fl = null;

	// 是否输入键按下(Enter Key 不是)
	boolean isInputKeyPress = false;

	/**
	 * UIRefPaneTextField 构造子注解.
	 */
	public CopyUIRefPaneTextField() {
		super();
		//
		init();
	}

	public void setUIRefPaneNull() {
		m_UIRefPane = null;
	}

	/**
	 * UIRefPaneTextField 构造子注解.
	 * 
	 * @param columns
	 *            int
	 */
	public CopyUIRefPaneTextField(int columns) {
		super(columns);
		//
		init();
	}

	/**
	 * UIRefPaneTextField 构造子注解.
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public CopyUIRefPaneTextField(String text) {
		super(text);
		//
		init();
	}

	/**
	 * UIRefPaneTextField 构造子注解.
	 * 
	 * @param text
	 *            java.lang.String
	 * @param columns
	 *            int
	 */
	public CopyUIRefPaneTextField(String text, int columns) {
		super(text, columns);
		//
		init();
	}

	/**
	 * UIRefPaneTextField 构造子注解.
	 * 
	 * @param doc
	 *            javax.swing.text.Document
	 * @param text
	 *            java.lang.String
	 * @param columns
	 *            int
	 */
	public CopyUIRefPaneTextField(javax.swing.text.Document doc, String text,
			int columns) {
		super(doc, text, columns);
		//
		init();
	}

	/**
	 * 
	 * 创建日期:(2003-2-13 11:30:15)
	 */
	public void adjustLength() {

		if (isAutoAdjustLength()) {
			setIsInternalAdjustingSize(true);
			if (getUIRefPane() != null) {
				UIButton btn = getUIRefPane().getUIButton();
				int refPaneWidth = getBestWidth();
				if (getUIRefPane().isButtonVisible() && btn != null) {
					refPaneWidth += btn.getWidth();
				}
				//
				adjustLength(getUIRefPane(), refPaneWidth);
			}
			setIsInternalAdjustingSize(false);
		}
	}

	/**
	 * 
	 * 创建日期:(2003-2-13 11:30:15)
	 */
	protected void adjustLength(CopyFormatPanl pane, int width) {

		if (width < pane.getWidth() && width > getMinWidth()) {
			return;
		}
		//
		if (width > getMinWidth()) {
			if (width < getMaxWidth()) {
				pane.setSize(width, (int) pane.getSize().getHeight());
			} else {
				pane.setSize(getMaxWidth(), (int) pane.getSize().getHeight());
			}
		} else if (pane.getSize().getWidth() > getMinWidth()) {
			pane.setSize(getMinWidth(), (int) pane.getSize().getHeight());
		}
		//
		pane.validate();
		pane.repaint();
	}

	/**
	 * 
	 * 创建日期:(2004-7-2 17:10:38)
	 * 
	 * @return java.awt.event.FocusListener
	 */
	private java.awt.event.FocusListener getFl() {
		return fl;
	}

	/**
	 * 
	 * 创建日期:(2002-11-7 14:03:50)
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	public CopyFormatPanl getUIRefPane() {
		return m_UIRefPane;
	}

	/**
	 * 
	 * 创建日期:(2003-3-22 13:44:11)
	 */
	private void init() {

		setIsAutoAdjustLength(false);
	}

	protected void processFocusEvent(java.awt.event.FocusEvent e) {

		super.processFocusEvent(e);
		/** 本参照已经被销毁 */
		if (getUIRefPane() == null)
			return;
		isFocusLostToButton = false;

		if (e.getID() == java.awt.event.FocusEvent.FOCUS_GAINED
				&& !e.isTemporary()) {
			// 焦点获得,所以尚未处理焦点丢失
			getUIRefPane().setProcessFocusLost(false);
			// getUIRefPane().isKeyPressed = false;
			// 如果获得焦点，且有输入，不做编码和名称的转换
			if (!getUIRefPane().isKeyPressed) {
				getUIRefPane().processFocusGained(e);
			}

			if (isAutoAdjustLength())
				adjustLength();
		}
		if (e.getID() == java.awt.event.FocusEvent.FOCUS_LOST
				&& !e.isTemporary()) {
			stopEditing();

		}

		// for bill
		if (!e.isTemporary())
			processBillItemFocusEvent(e);

		// process onbuttonclicked process
		if (e.getID() == java.awt.event.FocusEvent.FOCUS_GAINED
				&& !e.isTemporary()) {
			if (getUIRefPane() != null && getUIRefPane().hasButtonAction()) {
				Runnable r = new Runnable() {
					public void run() {
						getUIRefPane().onButtonClicked();
					}
				};
				SwingUtilities.invokeLater(r);
			}

		}

		setBorder(null);

	}

	// @Override
	// protected boolean processKeyBind(KeyStroke ks, KeyEvent e, int condition,
	// boolean pressed) {
	//		
	// setIsInputKeyPressed(e);
	// return super.processKeyBind(ks, e, condition, pressed);
	//	
	// }

	protected void setIsInputKeyPressed(java.awt.event.KeyEvent e) {
		if (isEditable() && isEnabled()) {

			if (e.getKeyChar() == '\n' && e.getID() == KeyEvent.KEY_PRESSED) { // 有输入的情况下按回车和没有输入的回车要区别
				if (isInputKeyPress) {
					getUIRefPane().isKeyPressed = true;
				} else {
					getUIRefPane().isKeyPressed = false;
				}
				// 回车后,恢复到初始状态.
				isInputKeyPress = false;
			}

			else if (e.getKeyChar() != '\n' && !getUIRefPane().isKeyPressed
					&& !e.isActionKey() && !(e.getKeyCode() == KeyEvent.VK_ALT) && !e.isAltDown()) {
				getUIRefPane().isKeyPressed = true;
				getUIRefPane().isButtonClicked = false;
				isInputKeyPress = true;
			}

		}
	}

	protected void processKeyEvent(java.awt.event.KeyEvent e) {

		super.processKeyEvent(e);
		setIsInputKeyPressed(e);

	}

	/**
	 * 
	 * 创建日期:(2003-3-19 13:53:34)
	 */
	protected void restoreLength() {

		if (isAutoAdjustLength()) {
			isInternalAdjustingSize = true;
			//
			if (!isFocusLostToButton) {
				if (getUIRefPane() != null) {
					getUIRefPane().setSize(getMinWidth(),
							(int) getUIRefPane().getSize().getHeight());
					getUIRefPane().validate();
					getUIRefPane().repaint();
				}
			}
			//
			isInternalAdjustingSize = false;
		}
	}

	public void setBounds(int x, int y, int width, int height) {

		boolean isInternal = isInternalAdjustingSize();
		setIsInternalAdjustingSize(true);
		//
		super.reshape(x, y, width, 18);
		//
		setIsInternalAdjustingSize(isInternal);
		//
		if (!isInternalAdjustingSize) {
			if (getUIRefPane() != null && getUIRefPane().getUIButton() != null) {
				if (getUIRefPane().isButtonVisible()) {
					setMinWidth(width + getUIRefPane().getUIButton().getWidth());
				} else {
					setMinWidth(width);
				}
			}
		}
	}

	/**
	 * 
	 * 创建日期:(2004-7-2 17:10:38)
	 * 
	 * @param newFl
	 *            java.awt.event.FocusListener
	 */
	public void setFl(java.awt.event.FocusListener newFl) {
		fl = newFl;
	}

	/**
	 * 
	 * 创建日期:(2002-11-7 14:03:50)
	 * 
	 * @param newM_UIRefPane
	 *            nc.ui.pub.beans.UIRefPane
	 */
	public void setUIRefPane(CopyFormatPanl newM_UIRefPane) {
		m_UIRefPane = newM_UIRefPane;
	}

	/*
	 * 单据模版CellEditor专用的。
	 */
	public void stopEditing() {
		if (getUIRefPane() == null)
			return;
		if (!getUIRefPane().isLostFocus) {
			// 丢失焦点并且－－焦点没到按钮上
			isFocusLostToButton = true;
		}
		//
		isFocusLostToButton = false;

		if (getUIRefPane().isLostFocus) {
			// 丢失焦点并且－－焦点没到按钮上
			// 如果是在表格里,特殊处理
			speciallyDealwithInCell();
			getUIRefPane().processFocusLost();

		} else {
			// 丢失焦点并且－－焦点按到按钮上
			getUIRefPane().fieldText = getText().trim();

		}
	}

	/**
	 * <p>
	 * //如果是在表格单元格里,特殊处理 <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2007-5-21</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void speciallyDealwithInCell() {
		if (isTableParent()) {
			backupTextValue();
			setFormatString(true);

		}
	}

	private void processBillItemFocusEvent(java.awt.event.FocusEvent e) {
		// for bill
		FocusListener listener = getFl();
		if (listener != null) {
			int id = e.getID();
			switch (id) {
			case FocusEvent.FOCUS_GAINED:
				listener.focusGained(e);
				break;
			case FocusEvent.FOCUS_LOST:
				listener.focusLost(e);
				break;
			}
		}
	}

	protected void registerKey() {
		super.registerKey();
		String key;
		InputMap map = getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = getActionMap();
		// CalculatorAction
		key = ShortCutKeyAction.KEY_REFDIALOG;
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, false), key);
		// map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0, false), key);
		am.put(key, new ShortCutKeyAction(ShortCutKeyAction.VK_F2));

		//
		String key_enter = ShortCutKeyAction.KEY_TRASFERFOCUS;
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), key_enter);
		am.put(key_enter, new ShortCutKeyAction(ShortCutKeyAction.VK_ENTER));
	}

	class ShortCutKeyAction extends AbstractAction {

		int keycode = -1;

		final static int VK_ENTER = KeyEvent.VK_ENTER;

		final static int VK_F2 = KeyEvent.VK_F2;

		final static String KEY_REFDIALOG = "showDialog";

		final static String KEY_TRASFERFOCUS = "trasferFocus";

		public ShortCutKeyAction(int keycode) {
			this.keycode = keycode;
		}

		public void actionPerformed(ActionEvent e) {
//			if (UIRefPaneTextField.this.isEnabled()
//					&& UIRefPaneTextField.this.isEditable())
//			
			if (m_UIRefPane.getUIButton().isEnabled()) {
				switch (keycode) {
				case VK_F2:
					showRefpaneDialog();
					break;
				case VK_ENTER:
					// break;
					focusNextComponent();
					break;
				}
			}
		}

		private void showRefpaneDialog() {
			String strValue = getText();
			// 判断案F2之前,是否按了其他键
			boolean isKeyPressed = getUIRefPane().isKeyPressed;

			getUIRefPane().isLostFocus = false;
			if (isKeyPressed) {
				getUIRefPane().isKeyPressed = true;
			}
			// e.consume();
			setText(strValue);
			getUIRefPane().connEtoC1();
		}
	}

	private void focusNextComponent() {

		if (isTableParent()) {

			return;
		}

		FocusUtils.focusNextComponent(CopyUIRefPaneTextField.this);
	}

	private boolean isTableParent() {
		boolean isTableParent = false;
		Component parent = CopyUIRefPaneTextField.this.getParent();
		Component parent_refPane = parent.getParent();
		if (parent_refPane instanceof JTable
				|| (parent instanceof IUIRefPane && parent.getParent() instanceof JTable)) {
			isTableParent = true;
		}
		return isTableParent;
	}
}
