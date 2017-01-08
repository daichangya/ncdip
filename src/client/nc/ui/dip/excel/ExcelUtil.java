package nc.ui.dip.excel;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import nc.ui.pub.bill.BillCardPanel;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

/**
 * 把BillCardPanel内容转换为excel文件格式。
 * 创建日期：(2004-5-28 12:20:57)
 * @author：祝奇
 */
public class ExcelUtil
{
	private HSSFCellStyle m_label = null;
	private HSSFCellStyle m_content = null;
	/**
	 * ExcelUtil 构造子注解。
	 */
	public ExcelUtil() {
		super();
	}
private int drawBillHead(
	HSSFSheet sheet,
	HSSFWorkbook wb,
	BillCardPanel billCardPanel,
	int rowIndex,
	int bodycols)
{
	//1，对需要输出的项目按照显示顺序排序。
	Map mItems = new HashMap();
	String[] tabcodes = billCardPanel.getBillData().getHeadTableCodes();
	if (tabcodes == null || tabcodes.length == 0)
		{
		return rowIndex;
	}
	for (int j = 0, k = tabcodes.length; j < k; j++)
		{
		nc.ui.pub.bill.BillItem[] items =
			billCardPanel.getBillData().getHeadShowItems(tabcodes[j]);
		for (int i = 0; i < items.length; i++)
			{
			mItems.put(new Integer(j*10000+items[i].getShowOrder()), items[i]);
		}
	}
	mItems = new TreeMap(mItems);

	return drawBillHeadOrTail(sheet,wb,mItems,rowIndex,bodycols);
}
private int drawBillHeadOrTail(
	HSSFSheet sheet,
	HSSFWorkbook wb,
	Map mItems,
	int rowIndex,
	int bodycols)
{
	int colIndex = 0;
	//1，对需要输出的项目按照显示顺序排序。

	//2，首先创建1行。
	HSSFRow row = sheet.createRow((short) rowIndex);
	for (Iterator iter = mItems.keySet().iterator(); iter.hasNext();)
		{
		//3.5如果1行放不下，另起1行。
		if (colIndex >= (bodycols - 1))
			{
			rowIndex++;
			row = sheet.createRow((short) rowIndex);
			colIndex = 0;
		}
		nc.ui.pub.bill.BillItem item =
			(nc.ui.pub.bill.BillItem) mItems.get(iter.next());
		//3.1创建1个单元格，存储Label.
		HSSFCell cell = row.createCell((short) colIndex++);
		//3.2设置编码方式和值及背景颜色。
		writeOneCell(wb, sheet, cell, item.getName());
		cell.setCellStyle(getLabelCellstyle(wb));
		//3.3创建1个单元格，存储数据值。
		cell = row.createCell((short) colIndex);
		cell = row.getCell((short) (colIndex));
		colIndex++;
		String value = null;
		if (item.getComponent() instanceof nc.ui.pub.beans.UIRefPane) //如果是参照
			{
			value = ((nc.ui.pub.beans.UIRefPane) item.getComponent()).getRefName();
			if (value == null || value.trim().length() == 0)
				{
				value = ((nc.ui.pub.beans.UIRefPane) item.getComponent()).getText();
			}
		}
		else
			value = item.getValue();
		//3.4设置编码方式和值。
		cell.setCellStyle(getContentCellstyle(wb));
		writeOneCell(wb, sheet, cell, value);
	}
	rowIndex++;
	return rowIndex;
}
private int drawBillTail(
	HSSFSheet sheet,
	HSSFWorkbook wb,
	BillCardPanel billCardPanel,
	int rowIndex,
	int bodycols)
{
	 //1，对需要输出的项目按照显示顺序排序。
	Map mItems = new HashMap();
	String[] tabcodes = billCardPanel.getBillData().getTailTableCodes();
	if (tabcodes == null || tabcodes.length == 0)
		{
		return rowIndex;
	}
	for (int j = 0, k = tabcodes.length; j < k; j++)
		{
		nc.ui.pub.bill.BillItem[] items =
			billCardPanel.getBillData().getTailShowItems(tabcodes[j]);
		for (int i = 0; i < items.length; i++)
			{
			mItems.put(new Integer(j*10000+items[i].getShowOrder()), items[i]);
		}
	}
	mItems = new TreeMap(mItems);

	return drawBillHeadOrTail(sheet,wb,mItems,rowIndex,bodycols);
}
private int drawTail(HSSFSheet sheet, HSSFWorkbook wb, String userName, int rowIndex)
{
	HSSFRow row = sheet.createRow(rowIndex++);
	HSSFCell cell;
	cell = row.createCell((short) 0);
	cell.setCellStyle(getLabelCellstyle(wb));
	writeOneCell(wb, sheet, cell, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000439")/*@res "创建者:"*/);
	cell = row.createCell((short) 1);
	cell.setCellStyle(getContentCellstyle(wb));
	writeOneCell(wb, sheet, cell, userName);
	cell = row.createCell((short) 2);
	cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	cell.setCellStyle(getLabelCellstyle(wb));
	writeOneCell(wb, sheet, cell, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000440")/*@res "创建日期:"*/);
	cell = row.createCell((short) 3);
	cell.setCellStyle(getContentCellstyle(wb));
	writeOneCell(wb, sheet, cell, new nc.vo.pub.lang.UFDateTime(new Date()));
	rowIndex++;
	return rowIndex;
}
private int drawTitle(
	HSSFSheet sheet,
	HSSFWorkbook wb,
	String title,
	int rowIndex,
	int bodycols)
{
	//1,创建行列。
	HSSFRow row = sheet.createRow(rowIndex);
	for (int i = 0; i < bodycols; i++)
		{
		row.createCell((short) i);
	}
	//2,所有列合并单元格。
	sheet.addMergedRegion(
		new Region(rowIndex, (short) 0, rowIndex, (short) (bodycols - 1)));
	//3，设置单元格的对齐方式。
	HSSFCell cell = row.getCell((short) 0);
	cell.setCellStyle(getLabelCellstyle(wb));
	writeOneCell(wb, sheet, cell, title);

	rowIndex++;

	return rowIndex;
}
public byte[] exceltoByte(HSSFWorkbook wb)
{
	byte[] fileContent = null;
	try
		{
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		wb.write(byteArrayOut);
		fileContent = byteArrayOut.toByteArray();
		byteArrayOut.close();
	}
	catch (Exception e)
		{
		e.printStackTrace();
	}
	return fileContent;
}
public void exceltoFile(HSSFWorkbook wb, String file)
{
	byte[] fileContent = null;
	try
		{
		FileOutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		fileOut.close();
	}
	catch (Exception e)
		{
		e.printStackTrace();
	}
}
public HSSFWorkbook exporttoExcel(String userName, BillCardPanel billCardPanel)
{
	System.gc();
	byte[] fileContent = null;
	String title = billCardPanel.getTitle();
	//1,首先计算出总列数，用于以后的布局使用。
	int bodycols = 1;
	int bodyrow= 0 ;
	Map mItems = new HashMap();
	String[] tabcodes = billCardPanel.getBillData().getBodyTableCodes();
	if (tabcodes == null || tabcodes.length == 0)
		{
		return null;
	}
	for (int j = 0, k = tabcodes.length; j < k; j++)
		{
		nc.ui.pub.bill.BillItem[] items =
			billCardPanel.getBillData().getBodyItemsForTable(tabcodes[j]);
		nc.ui.pub.bill.BillScrollPane bsp=billCardPanel.getBodyPanel(tabcodes[j]);

		int rows=billCardPanel.getBillModel(tabcodes[j]).getRowCount();
		if(rows>bodyrow) bodyrow=rows;
		for (int i = 0; i < items.length; i++)
			{
	        if(bsp.getShowCol(items[i].getKey())!=null)
	        {
			mItems.put(new Integer(j*10000+items[i].getShowOrder()), items[i]);
			bodycols++;
	        }
		}
	}

	//2,创建excel文件和页签。
	HSSFWorkbook wb = new HSSFWorkbook();
	HSSFSheet sheet = wb.createSheet(title);
	int colIndex = 0, rowIndex = 0;
	//3,生成标题
	rowIndex = drawTitle(sheet, wb, title, rowIndex, bodycols);
	//4,生成单据头
	rowIndex =
		drawBillHead(sheet, wb, billCardPanel, rowIndex, bodycols);
	//5,生成单据体
	mItems = new TreeMap(mItems);
	colIndex = 0;
	HSSFRow row = sheet.createRow((short) rowIndex++);
	HSSFCell cell1 = row.createCell((short) colIndex++);
	cell1.setCellStyle(getLabelCellstyle(wb));
	writeOneCell(wb, sheet, cell1, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001821")/*@res "序号"*/);
	//5.1 表格表头
	for (Iterator iter = mItems.keySet().iterator(); iter.hasNext();)
		{
		nc.ui.pub.bill.BillItem item =
			(nc.ui.pub.bill.BillItem) mItems.get(iter.next());
		sheet.setColumnWidth((short) colIndex, (short) (256 * 20));
		HSSFCell cell = row.createCell((short) colIndex++);
		cell.setCellStyle(getLabelCellstyle(wb));
		writeOneCell(wb, sheet, cell, item.getName());
	}
	//5.2 表格表体
	if (mItems.size() > 0)
		{
		colIndex = 1;
		for (int i = 0;
			i < bodyrow;
			i++, colIndex = 1)
			{
			row = sheet.createRow((short) rowIndex++);
			HSSFCell cell = null;
			cell = row.createCell((short) 0);
			cell.setCellStyle(getLabelCellstyle(wb));
			writeOneCell(wb, sheet, cell, new Integer(i + 1));
			for (Iterator iter = mItems.keySet().iterator(); iter.hasNext();)
				{
				nc.ui.pub.bill.BillItem item =
					(nc.ui.pub.bill.BillItem) mItems.get(iter.next());
				cell = row.createCell((short) colIndex++);
				Object value = billCardPanel.getBillModel(item.getTableCode()).getValueAt(i, item.getKey());
				if (value == null)
					value = "";
				cell.setCellStyle(getContentCellstyle(wb));
				writeOneCell(wb, sheet, cell, value);
			}
		}
	}
	//生成单据尾
	rowIndex =
		drawBillTail(sheet, wb, billCardPanel, rowIndex, bodycols);
	//生成 日期，生成者
	rowIndex = drawTail(sheet, wb, userName, rowIndex);
	return wb;
}
private HSSFCellStyle getContentCellstyle(HSSFWorkbook wb)
{
	if (m_content == null)
		{
		//设置颜色
		m_content = wb.createCellStyle();
		m_content.setFillForegroundColor(HSSFColor.WHITE.index);
		m_content.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//设置边框
		m_content.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		m_content.setBottomBorderColor(HSSFColor.BLACK.index);
		m_content.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		m_content.setLeftBorderColor(HSSFColor.BLACK.index);
		m_content.setBorderRight(HSSFCellStyle.BORDER_THIN);
		m_content.setRightBorderColor(HSSFColor.BLACK.index);
		m_content.setBorderTop(HSSFCellStyle.BORDER_THIN);
		m_content.setTopBorderColor(HSSFColor.BLACK.index);
	}
	return m_content;
}
private HSSFCellStyle getLabelCellstyle(HSSFWorkbook wb)
{
	if (m_label == null)
		{
		//设置颜色
		m_label = wb.createCellStyle();
		m_label.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
		m_label.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//设置边框
		m_label.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		m_label.setBottomBorderColor(HSSFColor.BLACK.index);
		m_label.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		m_label.setLeftBorderColor(HSSFColor.BLACK.index);
		m_label.setBorderRight(HSSFCellStyle.BORDER_THIN);
		m_label.setRightBorderColor(HSSFColor.BLACK.index);
		m_label.setBorderTop(HSSFCellStyle.BORDER_THIN);
		m_label.setTopBorderColor(HSSFColor.BLACK.index);
	}
	return m_label;
}
/**
 * 输出内容到一个单元格。
 * 创建日期：(2004-6-2 12:16:04)
 * @param book org.apache.poi.hssf.usermodel.HSSFWorkbook
 * @param sheet org.apache.poi.hssf.usermodel.HSSFSheet
 * @param cell org.apache.poi.hssf.usermodel.HSSFCell
 * @param value java.lang.Object
 */
private void writeOneCell(
	HSSFWorkbook book,
	HSSFSheet sheet,
	HSSFCell cell,
	Object value)
{
	cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	if (value instanceof Boolean)
		{
		value = ((Boolean) value).booleanValue() ? nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000244")/*@res "是"*/ : nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000108")/*@res "否"*/;
		cell.setCellValue(value.toString());
	}
	else if (value instanceof UFBoolean)
		{
		value = ((Boolean) value).booleanValue() ? nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000244")/*@res "是"*/ : nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000108")/*@res "否"*/;
		cell.setCellValue(value.toString());
	}
	else if (value instanceof UFDouble)
		{
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(((UFDouble) value).doubleValue());
	}
	else if (value instanceof Float)
		{
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(((Float) value).doubleValue());
	}
	else if (value instanceof java.math.BigDecimal)
		{
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(((java.math.BigDecimal) value).doubleValue());
	}
	else if (value instanceof Double)
		{
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(((Double) value).doubleValue());
	}
	else if (value instanceof Integer)
		{
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(((Integer) value).doubleValue());
	}
	else if (value instanceof Short)
		{
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(((Short) value).doubleValue());
	}
	else if (value instanceof Long)
		{
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(((Long) value).doubleValue());
	}
	else if (value instanceof Byte)
		{
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(((Byte) value).doubleValue());
	}
	else
		{
		if (value == null)
			value = "";
		cell.setCellValue(value.toString().trim());
	}
}
}