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
 * ��������:(01-2-28 14:49:01)
 */
public class DefBillTableCellRender extends JLabel implements TableCellRenderer, Serializable {
	protected static Border noFocusBorder;
	// We need a place to store the color the JLabel should be returned
	// to after its foreground and background colors have been set
	// to the selection background color.
	// These ivars will be made protected when their names are finalized.
	private Color unselectedForeground;
	private Color unselectedBackground;
	private int m_iDataType = 0; //��������
	private int m_iDecimalDigits = 0; //С��λ��
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
	 * BillTableCellRenderer ������ע��.
	 */
	public DefBillTableCellRender(int newDataType) {
		this(newDataType, true, false);
	}
	/**
	 * BillTableCellRenderer ������ע��.
	 */
	public DefBillTableCellRender(int newDataType, boolean newValue) {
		this(newDataType, newValue, false);
	}
	/**
	 * BillTableCellRenderer ������ע��.
	 */
	public DefBillTableCellRender(int newDataType, boolean newSign, boolean newRed) {
		this(newDataType, newSign, newRed, 0);
	}
	/**
	 * BillTableCellRenderer ������ע��.
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
	 * BillTableCellRenderer ������ע��.
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
	 * BillTableCellRenderer ������ע��.
	 */
	public DefBillTableCellRender(BillItem item, boolean newSign, boolean newRed) {
		this();
		m_iDataType = item.getDataType();
		paraVO.setNegativeSign(newSign);
		paraVO.setShowRed(newRed);
		m_iDecimalDigits = item.getDecimalDigits();
	}
	/**
	 * BillTableCellRenderer ������ע��.
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
	 * BillTableCellRenderer ������ע��.
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
	 * ��������:(2003-6-19 16:29:41)
	 * @return java.awt.Color
	 */
	public Color getBackGround(int row, int col) {
		return  hashBackGround.get(row + "," + col);
	}
//�����������
	public int getDataType() {
		return m_iDataType;
	}
//���С��λ��
	public int getDecimalDigits() {
		return m_iDecimalDigits;
	}
	/**
	 * 
	 * ��������:(2003-6-19 16:29:41)
	 * @return java.awt.Color
	 */
	public Color getForeGround(int row, int col) {
		return hashForeGround.get(row + "," + col);
	}
//�õ������Ƿ���ʾ����
	public boolean getNegativeSign() {
		return paraVO.isNegativeSign();
	}
//�õ������Ƿ���ʾ����
	public boolean getShowRed() {
		return paraVO.isShowRed();
	}
//�õ��Ƿ���ʾǧ��λ
	public boolean getShowThMark() {
		return paraVO.isShowThMark();
	}
//�õ��Ƿ�����ʾΪ�մ�
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
	 * ��������:(2003-6-19 16:29:41)
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
//������������
	public void setDataType(int iDataType) {
		m_iDataType = iDataType;
	}
//���С��λ��
	public void setDecimalDigits(int iDecimalDigits) {
		m_iDecimalDigits = iDecimalDigits;
	}
	/**
	 * 
	 * ��������:(2003-6-19 16:29:41)
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
	 * ����ǧ��λ��־.
	 * ��������:(2002-01-29 14:51:16)
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

		//С��
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
//���ø����Ƿ���ʾ����
	public void setNegativeSign(boolean newValue) {
		paraVO.setNegativeSign(newValue);
	}
//���ø����Ƿ���ʾ����
	public void setShowRed(boolean newValue) {
		paraVO.setShowRed(newValue);
	}
//�����Ƿ���ʾǧ��λ
	public void setShowThMark(boolean newValue) {
		paraVO.setShowThMark(newValue);
	}
//�����Ƿ�����ʾΪ�մ�
	public void setShowZeroLikeNull(boolean newValue) {
		paraVO.setShowZeroLikeNull(newValue);
	}
	protected void setValue(Object value) {
		setHorizontalAlignment(lor);
		if (value == null || value.equals("")) {
			setText("");
		} else {
			//��������
			switch (m_iDataType) {
			case BillItem.INTEGER:
			case BillItem.DECIMAL:
				if (m_iDataType == BillItem.INTEGER || m_iDataType == BillItem.DECIMAL) {
					setHorizontalAlignment(SwingConstants.RIGHT);
					setForeground(java.awt.Color.black);
	
					//������С�ƺϼ����⴦��,��ֵΪArrayList
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
						String v = value.toString(); //(value ==  null || value.equals(""))�Ѿ���if�д�����
						double dou = Double.parseDouble(v);
						if (dou == 0) {
							if (getShowZeroLikeNull())
								value = "";
						} else {
							//����
							if (dou < 0) {
								if (getShowRed())
									setForeground(java.awt.Color.red);
								if (!getNegativeSign())
									value = Math.abs(dou) + "";
							}
							//��ʾǧ��λ
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
