package nc.ui.dip.datalook;


import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import nc.ui.pub.bill.BillItem;
import nc.vo.pub.bill.BillRendererVO;

/**
 * 
 * 创建日期:(01-2-28 14:49:01)
 */
public class DefBillTableCellRender extends JLabel implements TableCellRenderer, Serializable {
	protected static Border noFocusBorder;
	// We need a place to store the color the JLabel should be returned
	// to after its foreground and background colors have been set
	// to the selection background color.
	// These ivars will be made protected when their names are finalized.
	private Color unselectedForeground;
	private Color unselectedBackground;
	private int m_iDataType = 0; //数据类型
	private int m_iDecimalDigits = 0; //小数位数
	private BillRendererVO paraVO = new BillRendererVO();
	private int lor;
	//{(row,col),Color}
	Hashtable<String,Color> hashBackGround = new Hashtable<String,Color>();
	Hashtable<String,Color> hashForeGround = new Hashtable<String,Color>();
	/**
	 * Creates a default table cell renderer.
	 */
	public DefBillTableCellRender() {
		super();
		noFocusBorder = new EmptyBorder(1, 2, 1, 2);
		setOpaque(true);
		setBorder(noFocusBorder);
	}
	/**
	 * BillTableCellRenderer 构造子注解.
	 */
	public DefBillTableCellRender(int newDataType) {
		this(newDataType, true, false);
	}
	/**
	 * BillTableCellRenderer 构造子注解.
	 */
	public DefBillTableCellRender(int newDataType, boolean newValue) {
		this(newDataType, newValue, false);
	}
	/**
	 * BillTableCellRenderer 构造子注解.
	 */
	public DefBillTableCellRender(int newDataType, boolean newSign, boolean newRed) {
		this(newDataType, newSign, newRed, 0);
	}
	/**
	 * BillTableCellRenderer 构造子注解.
	 */
	public DefBillTableCellRender(int newDataType,
								 boolean newSign,
								 boolean newRed,
								 int newDecimalDigits) {
		this();
		m_iDataType = newDataType;
		paraVO.setNegativeSign(newSign);
		paraVO.setShowRed(newRed);
		m_iDecimalDigits = newDecimalDigits;
	}
	/**
	 * BillTableCellRenderer 构造子注解.
	 */
	public DefBillTableCellRender(BillItem item, BillRendererVO newParameterVO,Integer lor) {
		this();
		m_iDataType = item.getDataType();
		m_iDecimalDigits = item.getDecimalDigits();
		paraVO = newParameterVO;
		this.lor=lor;
		setHorizontalAlignment(lor);
	}
	/**
	 * BillTableCellRenderer 构造子注解.
	 */
	public DefBillTableCellRender(BillItem item, boolean newSign, boolean newRed) {
		this();
		m_iDataType = item.getDataType();
		paraVO.setNegativeSign(newSign);
		paraVO.setShowRed(newRed);
		m_iDecimalDigits = item.getDecimalDigits();
	}
	/**
	 * BillTableCellRenderer 构造子注解.
	 */
	public DefBillTableCellRender(BillItem item, boolean newSign, boolean newRed, boolean newThMark) {
		this();
		m_iDataType = item.getDataType();
		paraVO.setNegativeSign(newSign);
		paraVO.setShowRed(newRed);
		paraVO.setShowThMark(newThMark);
		m_iDecimalDigits = item.getDecimalDigits();
	}
	/**
	 * BillTableCellRenderer 构造子注解.
	 */
	public DefBillTableCellRender(BillItem item,
								 boolean newSign,
								 boolean newRed,
								 boolean newThMark,
								 boolean newZeroAsNull) {
		this();
		m_iDataType = item.getDataType();
		paraVO.setNegativeSign(newSign);
		paraVO.setShowRed(newRed);
		paraVO.setShowThMark(newThMark);
		paraVO.setShowZeroLikeNull(newZeroAsNull);
		m_iDecimalDigits = item.getDecimalDigits();
	}
	/**
	 * 
	 * 创建日期:(2003-6-19 16:29:41)
	 * @return java.awt.Color
	 */
	public Color getBackGround(int row, int col) {
		return  hashBackGround.get(row + "," + col);
	}
//获得数据类型
	public int getDataType() {
		return m_iDataType;
	}
//获得小数位数
	public int getDecimalDigits() {
		return m_iDecimalDigits;
	}
	/**
	 * 
	 * 创建日期:(2003-6-19 16:29:41)
	 * @return java.awt.Color
	 */
	public Color getForeGround(int row, int col) {
		return hashForeGround.get(row + "," + col);
	}
//得到负数是否显示符号
	public boolean getNegativeSign() {
		return paraVO.isNegativeSign();
	}
//得到负数是否显示红字
	public boolean getShowRed() {
		return paraVO.isShowRed();
	}
//得到是否显示千分位
	public boolean getShowThMark() {
		return paraVO.isShowThMark();
	}
//得到是否将零显示为空串
	public boolean getShowZeroLikeNull() {
		return paraVO.isShowZeroLikeNull();
	}
// implements javax.swing.table.TableCellRenderer
	public Component getTableCellRendererComponent(JTable table,
												   Object value,
												   boolean isSelected,
												   boolean hasFocus,
												   int row,
												   int column) {
		Color color = null;
		if (isSelected) {
			super.setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			//super.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
			//super.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());

			if ((color = getBackGround(row, column)) != null)
				super.setBackground(color);
			else
				super.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
			if ((color = getForeGround(row, column)) != null)
				super.setForeground(color);
			else
				super.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
		}
		setFont(table.getFont());
		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			if (table.isCellEditable(row, column)) {
				super.setForeground(UIManager.getColor("Table.focusCellForeground"));
				super.setBackground(UIManager.getColor("Table.focusCellBackground"));
			}
		} else {
			setBorder(noFocusBorder);
		}

		//when datatype is integer or decimal,the foreground can be modified;
		setValue(value);

		//reset foreground
		if (color != null)
			super.setForeground(color);
		return this;
	}
	public void resumeDefaultBackGround() {
		hashBackGround.clear();
	}
	public void resumeDefaultForeGround() {
		hashForeGround.clear();
	}
	/**
	 * 
	 * 创建日期:(2003-6-19 16:29:41)
	 * @param newBackGround java.awt.Color
	 */
	public void setBackGround(int row, int col, java.awt.Color color) {
		if (color == null)
			hashBackGround.remove(row + "," + col);
		else
			hashBackGround.put(row + "," + col, color);
	}
	/**
	 * Overrides <code>JComponent.setForeground</code> to specify
	 * the unselected-background color using the specified color.
	 */
	public void setBackground(Color c) {
		super.setBackground(c);
		unselectedBackground = c;
	}
//设置数据类型
	public void setDataType(int iDataType) {
		m_iDataType = iDataType;
	}
//获得小数位数
	public void setDecimalDigits(int iDecimalDigits) {
		m_iDecimalDigits = iDecimalDigits;
	}
	/**
	 * 
	 * 创建日期:(2003-6-19 16:29:41)
	 * @param newForeGround java.awt.Color
	 */
	public void setForeGround(int row, int col, java.awt.Color color) {
		if (color == null)
			hashForeGround.remove(row + "," + col);
		else
			hashForeGround.put(row + "," + col, color);
	}
	/**
	 * Overrides <code>JComponent.setForeground</code> to specify
	 * the unselected-foreground color using the specified color.
	 */
	public void setForeground(Color c) {
		super.setForeground(c);
		unselectedForeground = c;
	}
	/**
	 * 加入千分位标志.
	 * 创建日期:(2002-01-29 14:51:16)
	 * @param str java.lang.String
	 * @return java.lang.String
	 */
	public static String setMark(String str) {
		if ((str == null) || (str.trim().length() < 3))
			return str;
		str = str.trim();
		String str0 = "";
		String str1 = str;
		if (str.startsWith("-")) {
			str0 = "-";
			str1 = str.substring(1);
		}
		int pointIndex = str1.indexOf(".");
		String str2 = "";
		String newStr = "";

		//小数
		if (pointIndex != -1) {
			str2 = str1.substring(pointIndex);
			str1 = str1.substring(0, pointIndex);
		}
		int ii = (str1.length() - 1) % 3;
		for (int i = 0; i < str1.length(); i++) {
			newStr += str1.charAt(i);
			if (ii <= 0) {
				newStr += ",";
				ii += 2;
			} else
				ii--;
			ii = ii % 3;
		}
		if (newStr.length() > 0)
			newStr = newStr.substring(0, newStr.length() - 1) + str2;
		else
			newStr = str2;
		newStr = str0 + newStr;
		return newStr;
	}
//设置负数是否显示符号
	public void setNegativeSign(boolean newValue) {
		paraVO.setNegativeSign(newValue);
	}
//设置负数是否显示红字
	public void setShowRed(boolean newValue) {
		paraVO.setShowRed(newValue);
	}
//设置是否显示千分位
	public void setShowThMark(boolean newValue) {
		paraVO.setShowThMark(newValue);
	}
//设置是否将零显示为空串
	public void setShowZeroLikeNull(boolean newValue) {
		paraVO.setShowZeroLikeNull(newValue);
	}
	protected void setValue(Object value) {
		setHorizontalAlignment(lor);
		if (value == null || value.equals("")) {
			setText("");
		} else {
			//数据类型
			switch (m_iDataType) {
			case BillItem.INTEGER:
			case BillItem.DECIMAL:
				if (m_iDataType == BillItem.INTEGER || m_iDataType == BillItem.DECIMAL) {
					setHorizontalAlignment(SwingConstants.RIGHT);
					setForeground(java.awt.Color.black);
	
					//辅助量小计合计特殊处理,其值为ArrayList
					if (value instanceof ArrayList) {
						StringBuffer valueBuf = new StringBuffer();
						ArrayList valueList = (ArrayList) value;
						int unitNum = valueList.size() / 2;
						for (int i = 0; i < unitNum; i++) {
							if (i > 0) {
								valueBuf.append("/ ");
							}
							valueBuf.append(valueList.get(i * 2));
							valueBuf.append(valueList.get(i * 2 + 1));
						}
						value = valueBuf.toString();
					} else {
						String v = value.toString(); //(value ==  null || value.equals(""))已经在if中处理了
						double dou = Double.parseDouble(v);
						if (dou == 0) {
							if (getShowZeroLikeNull())
								value = "";
						} else {
							//负数
							if (dou < 0) {
								if (getShowRed())
									setForeground(java.awt.Color.red);
								if (!getNegativeSign())
									value = Math.abs(dou) + "";
							}
							//显示千分位
							if (getShowThMark()) {
								value = setMark(v);
							}
						}
					}
				}
				break;
			case BillItem.PASSWORDFIELD:
				value = "********";
				break;
			default:
				break;
			}
			setHorizontalAlignment(lor);
			setText(value.toString());
		}
	}
	/**
	 * Notification from the UIManager that the L&F has changed.
	 * Replaces the current UI object with the latest version from the
	 * UIManager.
	 * @see JComponent#updateUI
	 */
	public void updateUI() {
		super.updateUI();
		setForeground(null);
		setBackground(null);
	}
}
