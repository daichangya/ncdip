//package nc.ui.dip.cmdctl;
//
//
///**
// * @nopublish
// * @author£º ºØÑï
// * Date: 2004-11-24
// * Time: 14:56:43
// */
//
//import java.io.Serializable;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.swing.table.AbstractTableModel;
//
//import nc.bs.framework.common.NCLocator;
//import nc.itf.dip.pub.IQueryField;
//import nc.jdbc.framework.ConnectionFactory;
//import nc.ui.dbcache.SQLFacade;
//import nc.ui.dbcache.exception.UiCacheException;
//import nc.ui.dbcache.gui.MessageBox;
//import nc.ui.dbcache.pool.PoolManager;
//import nc.util.dip.sj.IContrastUtil;
//
//public class CtlResultSetTableModel extends AbstractTableModel{
//public boolean issucc=false;
//public int updatecount;
//
//	/**
//	 * number of rows in the ResultSet
//	 */
//	public int rowCount;
//
//	/**
//	 * number of columns in ResultS et
//	 */
//	public int colCount;
//
//	/**
//	 * Simple facade that hides the details of executing JDBC commands
//	 */
//	private SQLFacade sqlFacade;
//
//	/**
//	 * The names of the columns in the ResultSet
//	 */
//	private List columnNames = new ArrayList();
//
//	/**
//	 * The class types of the columns being displayed.
//	 */
//	private List columnTypes = new ArrayList();
//
//	public List result = new ArrayList();
//
//	/**
//	 * Stores the modified cells in each row
//	 */
//	private HashMap modifiedCells = new HashMap();
//
//	/**
//	 * Name of the database table being displayed
//	 */
//	private String tableName;
//
//	public CtlResultSetTableModel(SQLFacade facade) {
//		this.sqlFacade = facade;
//	}
//
//	// -----------------------------------------------------------------------------------------
//	// //
//	// methods of javax.swing.table.TableModel interface
//	// -----------------------------------------------------------------------------------------
//	// //
//	public int getRowCount() {
//		return rowCount;
//	}
//
//	public int getColumnCount() {
//		if (rowCount > 0)
//			return colCount + 1;
//		else
//			return 0;
//	}
//
//	public Object getValueAt(int rowIndex, int columnIndex) {
//		if (columnIndex == 0)
//			return new Integer(rowIndex);
//		else
//			return ((ArrayList) result.get(rowIndex)).get(columnIndex - 1);
//	}
//
//	public boolean isCellEditable(int rowIndex, int columnIndex) {
//		return false;
//	}
//
//	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//		columnIndex = columnIndex - 1;
//		ArrayList v = (ArrayList) result.get(rowIndex);
//		v.set(columnIndex, aValue);
//
//		Set set = (Set) modifiedCells.get(new Integer(rowIndex));
//		if (set == null) {
//			set = new HashSet();
//			modifiedCells.put(new Integer(rowIndex), set);
//		}
//		set.add(new Integer(columnIndex));
//
//		fireTableCellUpdated(rowIndex, columnIndex);
//	}
//
//	public String getColumnName(int colIndex) {
//		if (colIndex == 0)
//			return "ÐÐÊý";
//		else
//			return (String) columnNames.get(colIndex - 1);
//	}
//
//	// -----------------------------------------------------------------------------------------
//	// //
//	// -----------------------------------------------------------------------------------------
//	// //
//
//	/**
//	 * returns the valueList of column names
//	 */
//	public List getColumnNames() {
//		return columnNames;
//	}
//
//	/**
//	 * Pushes the changes made by the user to the database.
//	 */
//	public void updateDatabase() {
//		// The following code was needed to emulate some of the functions not
//		// adopted by
//		// the IBM DB2 JDBC driver in regards to an updatable ResultSet feature
//
//		// get the set of rows that were modified
//		Set rows = modifiedCells.keySet();
//
//		// for each modified row create the update query
//		for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
//			Integer row = (Integer) iterator.next();
//
//			// get the set of modified columns
//			Set cols = (Set) modifiedCells.get(row);
//
//			StringBuffer update = new StringBuffer("UPDATE ");
//			update.append(tableName);
//			update.append(" SET ");
//
//			StringBuffer where = new StringBuffer(" WHERE ");
//			List rowValues = getRow(row.intValue());
//			List l = new ArrayList();
//
//			boolean firstUpdateCol = true;
//			boolean firstWhereCol = true;
//			for (int i = 0; i < columnNames.size(); i++) {
//
//				String colName = (String) columnNames.get(i);
//				Object value = rowValues.get(i);
//				if (cols.contains(new Integer(i))) {
//					if (firstUpdateCol == false) {
//						update.append(",");
//
//					} else {
//						firstUpdateCol = false;
//					}
//					update.append(colName);
//					update.append("=");
//					update.append(appendQuote(i, value));
//				} else {
//					if (value != null) {
//						if (firstWhereCol == false) {
//							where.append(" AND ");
//						} else {
//							firstWhereCol = false;
//						}
//						where.append(colName);
//						where.append("= ?");
//
//						l.add(value);
//					}
//				}
//
//			}
//			String query = update.toString() + where.toString();
//			try {
//				// execute the update query
//				sqlFacade.executeUpdateQuery(query, l);
//			} catch (UiCacheException se) {
//				MessageBox.showErrorMessageDialog(se, "ERROR UPDATING ROW "
//						+ row);
//			}
//
//		}
//		// all rows are made clean again
//		modifiedCells.clear();
//
//	}
//
//	public void executeSQL(String sql) throws Exception {
//		ResultSet rs = null;
//		updatecount=0;
//		issucc=false;
//		if (!sql.toLowerCase().trim().startsWith("select")&&!sql.toLowerCase().trim().startsWith("update")&&!sql.toLowerCase().trim().startsWith("delete")) {
//
//			IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//			iqf.exectEverySql(sql);
//			clearAll();
//			columnTypes=new ArrayList();
//			columnNames=new ArrayList();
//			issucc=true;
//			fireTableStructureChanged();
//			return;
//		}else if(sql.toLowerCase().trim().startsWith("update")||sql.toLowerCase().trim().startsWith("delete")){
//			IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//			int ret=iqf.updateSql(sql);
//			clearAll();
//			columnTypes=new ArrayList();
//			columnNames=new ArrayList();
//			updatecount=ret;
//			issucc=true;
//			fireTableStructureChanged();
//			return;
//		}
////		PreparedStatement st = null;
//		try {
//			IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//			CtlUtilVo cms=iqf.getResultSet(sql,new CtlUtilVo());
//			clearAll();
//			colCount = cms.colCount;
//			
//			columnTypes=cms.getColumnTypes();
//			columnNames=cms.getColumnNames();
//			result=cms.result;
//			rowCount=cms.rowCount;
//			issucc=true;
//			fireTableStructureChanged();
//		} catch (SQLException e) {
//			throw new UiCacheException(e);
//		} finally {
//			if (rs != null) {
//				rs.close();
//			}
//			/*try {
//				if (st != null)
//					st.close();
//			} catch (SQLException e) {
//				throw new UiCacheException(e);
//			}*/
//		}
//	}
//
//	public void reloadTableModel(String tableName) throws Exception {
//		executeSQL("SELECT   * from " + tableName);
//	}
//
//	public void deleteRow(int row) {
//
//		List values = getRow(row);
//
//		StringBuffer query = new StringBuffer("DELETE from " + tableName
//				+ " where ");
//		int size = columnNames.size();
//		boolean first = true;
//		for (int i = 0; i < size; i++) {
//			if (values.get(i) != null) {
//				if (!first) {
//					query.append("AND ");
//				}
//				first = false;
//				String colName = (String) columnNames.get(i);
//				query.append(colName + " = ? ");
//			}
//		}
//		try {
//			sqlFacade.executeUpdateQuery(query.toString(), values);
//			result.remove(row);
//			rowCount--;
//			fireTableDataChanged();
//		} catch (UiCacheException se) {
//			MessageBox.showErrorMessageDialog(se, "Error executing query");
//		}
//	}
//
//	/**
//	 * adds a new row to the database table
//	 * 
//	 * @param values
//	 *            column name/ column values
//	 */
//	public void addRowToDatabase(Map values) throws Exception {
//		StringBuffer query = new StringBuffer("INSERT INTO " + tableName + "(");
//		Set keys = values.keySet();
//		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
//			Integer integer = (Integer) iterator.next();
//			query.append("\"" + columnNames.get(integer.intValue()) + "\"");
//			if (iterator.hasNext())
//				query.append(",");
//		}
//		query.append(") VALUES (");
//
//		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
//			Integer integer = (Integer) iterator.next();
//			query.append(appendQuote(integer.intValue(), values.get(integer)));
//			if (iterator.hasNext())
//				query.append(",");
//		}
//
//		query.append(")");
//
//		// Debug.debug(query);
//
//		sqlFacade.executeUpdateQuery(query.toString(), null);
//		reloadTableModel(tableName);
//
//	}
//
//	/**
//	 * returns true if cell has been modified
//	 */
//	public boolean isCellModified(int row, int column) {
//		if (column != 0) {
//			Set columns = (Set) modifiedCells.get(new Integer(row));
//			if (columns != null) {
//				if (columns.contains(new Integer(column - 1)))
//					return true;
//			}
//		}
//		return false;
//	}
//
//	private String appendQuote(int col, Object value) {
//		Class c = (Class) columnTypes.get(col);
//		boolean isNumber = isNumber(c);
//		StringBuffer buffer = new StringBuffer();
//		if (!isNumber)
//			buffer.append("\'");
//		buffer.append(value);
//		if (!isNumber(c))
//			buffer.append("\'");
//		return buffer.toString();
//	}
//
//	/**
//	 * 
//	 */
//	public void updateColumnModel(ResultSetMetaData metaData)
//			throws SQLException, ClassNotFoundException {
//		colCount = metaData.getColumnCount();
//		for (int i = 1; i <= colCount; i++) {
//			String colName = metaData.getColumnLabel(i);
//			String colClass = metaData.getColumnClassName(i);
//			columnTypes.add(Class.forName(colClass));
//			columnNames.add(colName);
//		}
//	}
//
//	/**
//	 * clears the TableModel
//	 */
//	public void clearAll() {
//		for (int i = 0; i < result.size(); i++) {
//			ArrayList list = (ArrayList) result.get(i);
//			list.clear();
//		}
//		result.clear();
//		modifiedCells.clear();
//		columnNames.clear();
//		columnTypes.clear();
//		colCount = 0;
//		rowCount = 0;
//	}
//
//	/**
//	 * 
//	 */
//	private List getRow(int i) {
//		return (List) result.get(i);
//	}
//
//	/**
//	 * determine wether the given Class is number or not
//	 */
//	private boolean isNumber(Class c) {
//		return c.getSuperclass().equals(java.lang.Number.class);
//	}
//}
//
