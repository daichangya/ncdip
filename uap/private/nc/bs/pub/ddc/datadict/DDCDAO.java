package nc.bs.pub.ddc.datadict;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.ds.DataSourceMeta;
import nc.bs.framework.ds.DataSourceMetaMgr;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.ddc.datadict.table.TableUpdateProcessor;
import nc.jdbc.framework.ConnectionFactory;
import nc.jdbc.framework.DataSourceCenter;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.jdbc.framework.util.DBConsts;
import nc.util.dip.sj.SJUtil;
import nc.vo.com.utils.UUID;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.core.BizObject;
import nc.vo.pub.core.FolderNode;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.ddc.datadict.DDCData;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.ddc.datadict.DatadictNode;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.ddc.datadict.FindFolderName;
import nc.vo.pub.ddc.datadict.IDatadictCreator;
import nc.vo.pub.ddc.datadict.TableDef;
import nc.vo.pub.ddc.datadict.ViewFld;

/**
 * �����ֵ����DAO
 * 
 * @author ������ 2003-8-26  
 * @modifier ljian 2005-10-18  �����ع����ı����ݷ��ʷ�ʽ
 * @modifier huangzg 2005-12-12 �����ع�.�޸���ĳЩֻ���Լ����õķ���
 */
public class DDCDAO {

	String dsName = null;

	Datadict m_dict = null;

	public DDCDAO() {
		super();
		m_dict = Datadict.getDefaultInstance();
	}

	/**
	 * @param dsName
	 */
	public DDCDAO(String dsName) {
		super();
		this.dsName = dsName;
		this.m_dict = Datadict.getDefaultInstance();
	}

	/**
	 * @param sql
	 * @return
	 * @throws DbException
	 */
	public ViewFld[] getStructBySql(String sql) throws DbException {
		PersistenceManager persist = null;
		Vector vFlds = null;

		try {
			persist = PersistenceManager.getInstance(dsName);
			JdbcSession jdbc = persist.getJdbcSession();

			vFlds = (Vector) jdbc.executeQuery(sql, new ResultSetProcessor() {
				public Object handleResultSet(ResultSet rs) throws SQLException {
					ResultSetMetaData md = rs.getMetaData();
					Vector v = new Vector();
					for (int i = 0; i < md.getColumnCount(); i++) {
						ViewFld fld = new ViewFld();
						fld.setGUID(new UUID().toString());
						fld.setID(md.getColumnName(i + 1));
						fld.setDisplayName(md.getColumnName(i + 1));
						fld.setDataType(md.getColumnType(i + 1));
						v.add(fld);
					}// end of for
					return v;
				}
			});
		} finally {
			if (persist != null) {
				persist.release();
			}
		}

		if (vFlds.size() == 0) {
			return new ViewFld[0];
		} else {
			return (ViewFld[]) vFlds.toArray(new ViewFld[0]);
		}
	}

	/**
	 * ��ȡ���ݿ����б���
	 * FIXME::���Ӧ�����м�����ṩ�ɣ�
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DbException
	 */
	public String[] qryTbNamesFromMetaData() throws SQLException, DbException {
		PersistenceManager persist = null;
		ResultSet rsTables = null;
		ArrayList alTables = new ArrayList();

		try {
			persist = PersistenceManager.getInstance(dsName);
			DatabaseMetaData dbmd = persist.getMetaData();
			//DatabaseMetaData dbmd = con.getMetaData();

			int dbType = persist.getDBType();
			if (dbType == DBConsts.SQLSERVER)
				rsTables = dbmd.getTables(null, null, null, new String[] { "TABLE" });
			else if (dbType == DBConsts.ORACLE || dbType == DBConsts.DB2)
				rsTables = dbmd.getTables(null, dbmd.getUserName().toUpperCase(), null,
						new String[] { "TABLE" });

			while (rsTables.next()) {
				alTables.add(rsTables.getString("TABLE_NAME"));
			}
		} finally {
			try {
				rsTables.close();
			} catch (SQLException e) {
			}

			persist.release();
		}

		if (alTables.size() == 0) {
			return new String[0];
		} else {
			return (String[]) alTables.toArray(new String[0]);
		}
	}

	/**
	 * Ŀǰ��Ԫ������ֻ�����ֶ���Ϣ���ݲ����������������Ϣ
	 * 
	 * @param importType
	 * @return
	 * @throws BusinessException
	 */
	public DDCData fromDBMetaData(int importType) throws DbException, SQLException {
		DDCData data = new DDCData();
		Vector vecNode = new Vector();
		Vector vecTd = new Vector();
		PersistenceManager sessionManager = null;
		Connection con = null;

		try {
			sessionManager = PersistenceManager.getInstance(dsName);

			con = ConnectionFactory.getConnection();

			// ��Ԫ�����еõ����еı���
			DatabaseMetaData dbmd = con.getMetaData();
			int dbType = sessionManager.getDBType();
			ResultSet rsTables = null;
			ArrayList alTables = new ArrayList();
			if (dbType == DBConsts.SQLSERVER)
				rsTables = dbmd.getTables(null, null, null, new String[] { "TABLE" });
			else if (dbType == DBConsts.ORACLE || dbType == DBConsts.DB2)
				rsTables = dbmd.getTables(null, dbmd.getUserName().toUpperCase(), null,
						new String[] { "TABLE" });
			while (rsTables.next()) {
				alTables.add(rsTables.getString("TABLE_NAME"));
			}// end of while
			rsTables.close();

			// ѭ������ÿ�ű�
			for (int i = 0; i < alTables.size(); i++) {

				// ���ݱ����õ���Ӧ�Ķ���ڵ�ͱ����
				String tbName = (String) alTables.get(i);
				ObjectNode node = null;
				TableDef td = null;
				if (m_dict.findObjectNode(tbName) == null) {
					node = createNode(tbName, tbName);
					td = (TableDef) node.createObject();
				} else {
					node = m_dict.findObjectNode(tbName);
					td = (TableDef) node.getObject();
				}
				vecNode.add(node);
				vecTd.add(td);

				// �ñ��������ֵ����Ѵ��ڣ��Ȼ����ֶ���Ϣ
				HashMap hFld = new HashMap();
				for (int j = 0; j < td.getFieldDefs().getCount(); j++) {
					hFld.put(td.getFieldDefs().getFieldDef(j).getID(), td.getFieldDefs().getFieldDef(j));
				}
				td.getFieldDefs().clear();

				// �������ֶ���Ϣ
				ResultSet rsCol = dbmd.getColumns(null, null, tbName, null);
				while (rsCol.next()) {
					String colName = rsCol.getString("COLUMN_NAME");
					int itype = rsCol.getInt("DATA_TYPE");
					int iLen = rsCol.getInt("COLUMN_SIZE");
					int iPre = rsCol.getInt("DECIMAL_DIGITS");
					String strNull = rsCol.getString("IS_NULLABLE");
					boolean isNullable = strNull.equals("NO") ? false : true;
					FieldDef fd = null;
					// ʹ�û������ݣ�ȷ��guid����
					if (hFld.get(colName) != null) {
						FieldDef oldfd = (FieldDef) hFld.get(colName);
						if (importType == IDatadictCreator.IMPORT_TYPE_PHYOVERWRITE)
							fd = oldfd;
						else {
							fd = new FieldDef();
							fd.setID(colName);
							fd.setGUID(oldfd.getGUID());
						}
					} else {
						fd = new FieldDef();
						fd.setID(colName);
						fd.setGUID(new UUID().toString());
					}
					fd.setDisplayName(colName);
					fd.setDataType(itype);
					fd.setLength(iLen);
					fd.setPrecision(iPre);
					fd.setNull(isNullable);
					td.getFieldDefs().add(fd);
				}
				rsCol.close();

				// ���������ֶ���Ϣ
				ResultSet rsPKey = dbmd.getPrimaryKeys(null, null, tbName);
				while (rsPKey.next()) {
					String id = rsPKey.getString("COLUMN_NAME");
					int index = td.getFieldDefs().FindObject(id);
					td.getFieldDefs().getFieldDef(index).setIsPrimary(true);
					td.getFieldDefs().getFieldDef(index).setNull(false);
				}
				rsPKey.close();
			}

			data.setNode(vecNode);
			data.setTable(vecTd);

		} finally {
			if (sessionManager != null)
				sessionManager.release();
		}
		return data;
	}

	/**
	 * @param tbNames
	 * @param importType
	 * @param folderId
	 * @return
	 * @throws DbException
	 * @throws SQLException
	 */
	public DDCData fromDBMetaData(String[] tbNames, int importType, String folderId)
			throws DbException, SQLException {
		DDCData data = new DDCData();
		Vector vecNode = new Vector();
		Vector vecTd = new Vector();
		PersistenceManager persist = null;

		try {
			persist = PersistenceManager.getInstance(dsName);
			DatabaseMetaData dbmd = persist.getMetaData();

			// ѭ������ÿ��ָ�������ݱ�
			for (int i = 0; i < tbNames.length; i++) {
				String tbName = tbNames[i];
				ObjectNode node = null;
				TableDef td = null;

				DipDatadefinitHVO hvo=null;
				try {
					hvo=(DipDatadefinitHVO) new BaseDAO("ncdip").retrieveByPK(DipDatadefinitHVO.class, tbName);//.retrieveByClause(DipDatadefinitHVO.class, "memorytable='"+tbName.toUpperCase()+"' and nvl(dr,0)=0");
				} catch (DAOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String tbNamec="";
				if(!SJUtil.isNull(hvo)){
					tbNamec=hvo.getSysname();
					tbName=hvo.getMemorytable();
				}
//				if(SJUtil.isNull(tbNamec)||tbNamec.length()<=0){
//					tbNamec=tbName;
//				}
				if (m_dict.findObjectNode(tbName) == null) {
					node = createNode(tbName, tbNamec, folderId);
					td = (TableDef) node.createObject();
				} else {
					node = m_dict.findObjectNode(tbName);
					td = (TableDef) node.getObject();
				}
				vecNode.add(node);
				vecTd.add(td);
				Hashtable hFld = new Hashtable();
				//ȷ��guid����
				//if(getImportType() ==
				// IDatadictCreator.IMPORT_TYPE_PHYOVERWRITE)
				for (int j = 0; j < td.getFieldDefs().getCount(); j++)
					hFld.put(td.getFieldDefs().getFieldDef(j).getID(), td.getFieldDefs().getFieldDef(j));
				td.getFieldDefs().clear();
				//��ΪԪ����������ʱ�����������������Ϣ������ԭ���еı���
				//td.getForKeys().clear();
				//td.getIndexDefs().clear();
				ResultSet rsCol = dbmd.getColumns(null, null, tbName, null);

				List<DipDatadefinitBVO> bvo = null;
				try {
					bvo=(List<DipDatadefinitBVO>) (new BaseDAO("ncdip").retrieveByClause(DipDatadefinitBVO.class, " nvl(dr,0)=0 and pk_datadefinit_h=(select pk_datadefinit_h from dip_datadefinit_h where memorytable='"+tbName+"' and nvl(dr,0)=0)"));
				} catch (DAOException e) {
					e.printStackTrace();
				}
				HashMap<String, String> map = new HashMap<String, String>();
				while (rsCol.next()) {
					String colName = rsCol.getString("COLUMN_NAME");
					if(null == map.get(colName)){
						map.put(colName, colName);
						int itype = rsCol.getInt("DATA_TYPE");
						int iLen = rsCol.getInt("COLUMN_SIZE");
						int iPre = rsCol.getInt("DECIMAL_DIGITS");
						String strNull = rsCol.getString("IS_NULLABLE");
						boolean isNullable = true;
						if (strNull.equals("no"))
							isNullable = false;
						FieldDef fd = null;
						//ȷ��guid����
						if (hFld.get(colName) != null) {
							FieldDef oldfd = (FieldDef) hFld.get(colName);
							if (importType == IDatadictCreator.IMPORT_TYPE_PHYOVERWRITE)
								fd = oldfd;
							else {
								fd = new FieldDef();
								fd.setID(colName);
								fd.setGUID(oldfd.getGUID());
							}
						} else {
							fd = new FieldDef();
							fd.setID(colName);
							fd.setGUID(new UUID().toString());
						}
						String displayname="";
						if(!SJUtil.isNull(bvo)){
							for(DipDatadefinitBVO bvoi:bvo){
								if(!SJUtil.isNull(bvoi.getEname())){
									if(colName.toLowerCase().equals(bvoi.getEname().toLowerCase())){
										displayname=bvoi.getCname();
										break;
									}
								}
							}
						}
						fd.setDisplayName(displayname.length()==0?colName:displayname);//
						fd.setDataType(itype);
						fd.setLength(iLen);
						fd.setPrecision(iPre);
						fd.setNull(isNullable);
						td.getFieldDefs().add(fd);
					}
				}
				rsCol.close();
				ResultSet rsPKey = dbmd.getPrimaryKeys(null, null, tbName);
				while (rsPKey.next()) {
					String id = rsPKey.getString("COLUMN_NAME");
					int index = td.getFieldDefs().FindObject(id);
					td.getFieldDefs().getFieldDef(index).setIsPrimary(true);
					td.getFieldDefs().getFieldDef(index).setNull(false);
				}
				rsPKey.close();
			}

			data.setNode(vecNode);
			data.setTable(vecTd);
		} finally {
			if (persist != null)
				persist.release();
		}
		return data;
	}

	private ObjectNode createNode(String id, String Name, String folderId) {
		ObjectNode p = null;
		if (folderId != null) {
			p = m_dict.findObjectNode(folderId);
		} else {
			int iIndex = id.indexOf("_");
			if (iIndex != -1)
				folderId = id.substring(0, iIndex);
			else
				folderId = "qt";
			p = m_dict.findObjectNode(folderId);
			if (p == null)
				p = createFolderNode(folderId);
		}
		String GUID = id;
		ObjectNode node = m_dict.addObjectNode(p, GUID, id, Name, DatadictNode.TableKind);
		return node;
	}

	private ObjectNode createNode(String id, String Name) {
		int iIndex = id.indexOf("_");
		String folderId = null;
		if (iIndex != -1)
			folderId = id.substring(0, iIndex);
		else
			folderId = "qt";
		ObjectNode p = m_dict.findObjectNode(folderId);
		String GUID = id;
		//new UUID().toString();
		if (p == null)
			p = m_dict.findObjectNode("qt");
		if (p == null)
			p = createFolderNode(folderId);
		ObjectNode node = m_dict.addObjectNode(p, GUID, id, Name, DatadictNode.TableKind);
		return node;
	}

	private ObjectNode createFolderNode(String folderId) {
		ObjectNode parent = m_dict.getRoot();
		folderId = folderId.toLowerCase();
		String GUID = folderId;
		//new UUID().toString();
		String folderDisName = FindFolderName.getFolderName(folderId);
		ObjectNode p = m_dict.addObjectNode(parent, GUID, folderId, folderDisName, FolderNode.Kind);
		BizObject obj = p.createObject();
		obj.setNode(p);
		p.saveObject(obj);
		return p;
	}

	/**
	 * ����
	 * @param nodes
	 * @param ops
	 * @return
	 * @throws DbException
	 */
	public String testCreateDB(ObjectNode[] nodes, int[] ops) throws DbException {
		StringBuffer sb = new StringBuffer();
		String str = "select * from ";
		String strWhere = " where 1 = 0";
		PersistenceManager persist = null;

		try {
			persist = PersistenceManager.getInstance(dsName);
			JdbcSession session = persist.getJdbcSession();

			for (int i = 0; i < nodes.length; i++) {
				String hint = nodes[i].getDisplayName() + "(" + nodes[i].getID() + ")";
				String sql = str + nodes[i].getID() + strWhere;
				switch (ops[i]) {
					case 0: {
						try {
							session.executeQuery(sql, new ArrayListProcessor());
							sb.append(hint + ":"
									+ NCLangResOnserver.getInstance().getStrByID("10220102", "UPP10220102-000327"));
						} catch (DbException e) {
							sb.append(hint
									+ ":"
									+ nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("10220102",
											"UPP10220102-000328"));
						}
						break;
					}
					case 1: {
						try {
							session.executeQuery(sql, new ArrayListProcessor());
							sb.append(hint
									+ ":"
									+ nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("10220102",
											"UPP10220102-000329"));
						} catch (DbException e) {
							sb.append(hint
									+ ":"
									+ nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("10220102",
											"UPP10220102-000330"));
						}
						break;
					}
				}
			}
		} finally {
			if (persist != null)
				persist.release();
		}

		return sb.toString();
	}

	/**
	 * ������
	 * @param nodes
	 * @param tds
	 * @param iactions
	 * @throws Exception
	 */
	public void createDB(ObjectNode[] nodes, TableDef[] tds, int[] iactions) throws Exception {
		Connection con = null;
		try {
			//zjb��
			//con = ConnectionFactory.getConnection();
			if(dsName == null)
				dsName = DataSourceCenter.getInstance().getSourceName();
			
			if (DataSourceCenter.getInstance().getDatabaseType(dsName) == DBConsts.ORACLE) {
				//modified by cch 2006-9-4 
				DataSourceMetaMgr manager = DataSourceMetaMgr.getInstance();
				DataSourceMeta meta = manager.getDataSourceMeta(dsName);
				String strUrl = meta.getDatabaseUrl();
				String user = meta.getUser();
				String pwd = meta.getPassword();
				con = DriverManager.getConnection(strUrl, user, pwd);
			} else
				con = ConnectionFactory.getConnection(dsName);

			TableUpdateProcessor ddc = new TableUpdateProcessor();
			ddc.process(nodes, tds, iactions, con);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			try {
				if (con != null)
					con.close();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e1) {
			}
		}

	}
}
