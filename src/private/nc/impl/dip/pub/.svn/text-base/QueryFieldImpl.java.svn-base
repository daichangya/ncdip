package nc.impl.dip.pub;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.dip.fun.FormmulaCache;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.taskcenter.TaskAdminPojo;
import nc.bs.sm.config.XMLToJavaObject;
import nc.impl.dip.optByPluginSrv.OptByPlgImpl;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.itf.uap.busibean.IFileManager;
import nc.itf.uap.pa.IPreAlertConfigService;
import nc.jdbc.framework.ConnectionFactory;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.dip.cmdctl.CtlUtilVo;
import nc.util.dip.sj.CheckMessage;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.bd.CorpVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.dataproce.DipDataproceHVO;
import nc.vo.dip.datarec.DipDatarecSpecialTab;
import nc.vo.dip.dataverify.DataverifyBVO;
import nc.vo.dip.dataverify.DataverifyHVO;
import nc.vo.dip.ftpsourceregister.FtpSourceRegisterVO;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;
import nc.vo.sm.config.ConfigParameter;
import nc.vo.uap.busibean.exception.BusiBeanException;
import nc.vo.uap.scheduler.TaskStatus;

public class QueryFieldImpl extends DataManageObject implements IQueryField {
	ILogAndTabMonitorSys ilm=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
	public Map<String,String> getAccountMap() throws Exception{
		XMLToJavaObject ob=new XMLToJavaObject();
		Map<String,String> accocuntmap=new HashMap<String, String>();
		try {
			ConfigParameter a=(ConfigParameter) ob.getJavaObjectFromFile("./ierp/bin/account.xml");
			Account[] account=a.getAryAccounts();
			if(account!=null&&account.length>0){
				for(int i=0;i<account.length;i++){
					if(account[i].getDataSourceName()!=null&&account[i].getDataSourceName().length()>0){
						accocuntmap.put(account[i].getAccountName(),account[i].getAccountCode()+"|"+account[i].getDataSourceName());
					}
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return accocuntmap;
	}
	public boolean checkEnv(String[] str) throws Exception{
		BaseDAO bd=new BaseDAO(str[0]);
		
		List<CorpVO> corp=null;
			try{
			corp= (List<CorpVO>) bd.retrieveByClause(CorpVO.class, "unitcode='"+str[2]+"'");
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("没有找到对应的数据");
		}
		if(corp==null||corp.size()<=0){
			throw new Exception("公司编码不存在！");
		}
		List<UserVO> uservo= (List<UserVO>) bd.retrieveByClause(UserVO.class, "user_code='"+str[4]+"'");
		if(uservo==null||uservo.size()<=0){
			throw new Exception("用户名不存在");
		}
		String pwd=queryfield("select user_password from sm_user where user_code='"+str[4]+"'");
		Encode code = new Encode();
		if(!code.decode(pwd).equals(str[5])){
			throw new Exception("用户密码不正确");
		}
		return true;
	}
	private String getTypeName(int type){
        if (type == Types.BLOB || type == Types.LONGVARBINARY || type == Types.VARBINARY || type == Types.BINARY) {
            return String.class.getName();
        }else if (type == Types.CLOB || type == Types.LONGVARCHAR) {
        	return String.class.getName();
        }else if(type==Types.INTEGER||type==Types.SMALLINT){
        	return Integer.class.getName();
        }else if(type==Types.DECIMAL||type==Types.DOUBLE||type==Types.FLOAT||type==Types.LONGVARBINARY){
        	return Double.class.getName();
        }else{
        	return String.class.getName();
        }
	}
	public int updateSql(String sql) throws Exception{
		int res;
		PreparedStatement st = null;
		try{
		st = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON).prepareStatement(sql);

		res = st.executeUpdate();
//		csm.fireTableStructureChanged();
		}finally{
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
		
	}
	public CtlUtilVo getResultSet(String sql,CtlUtilVo csm) throws Exception{
		csm=new CtlUtilVo();
		ResultSet rs = null;
		PreparedStatement st = null;
		try{
		st = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON).prepareStatement(sql);

		rs = st.executeQuery();
		csm.colCount = rs.getMetaData().getColumnCount();
		for (int i = 1; i <= csm.colCount; i++) {
			String colName = rs.getMetaData().getColumnName(i);
			int coltype=rs.getMetaData().getColumnType(i);
			String colClass =getTypeName(coltype);
			csm.getColumnTypes().add(Class.forName(colClass));
			csm.getColumnNames().add(colName);
		}
		while (rs.next()) {
			ArrayList list = new ArrayList();
			for (int i = 1; i <= csm.colCount; i++) {
				Object o = rs.getObject(i);
				list.add(o);
			}
			csm.result.add(list);
			csm.rowCount++;
		}
//		csm.fireTableStructureChanged();
		}finally{
			if (rs != null) {
				rs.close();
			}
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return csm;
		
	}
    private BaseDAO baseDao;	
    public  String convertFilePath(String path) {

        path = path.replace('\\', '/');

        while (path.indexOf("//") > 0) {
        	nc.ui.pub.beans.util.StringReplace.replace(path, "//", "/");
        }

        if (!path.startsWith("/"))
            path = "/" + path;

        if (path.endsWith("/"))
            path = path.substring(0, path.length() - 1);

        return path;
    }
    public  void writeFile(String filePath, byte[] ba) {

        File file = new File(filePath);
        java.io.FileOutputStream out=null;
        try {
        	
            String dir = filePath.substring(0, filePath.lastIndexOf("/"));
            File dirFile = new File(dir);
            if (!dirFile.exists())
                dirFile.mkdirs();
            if(file.exists() && file.canWrite())
            	file.delete();
           out = new java.io.FileOutputStream(file);
            out.write(ba);
            out.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	if(out!=null){
        		try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
    public  RetMessage writeFiler(String filePath, byte[] ba) {

        File file = new File(filePath);
        java.io.FileOutputStream out=null;
        try {
        	
            String dir = filePath.substring(0, filePath.lastIndexOf("/"));
            File dirFile = new File(dir);
            if (!dirFile.exists())
                dirFile.mkdirs();
            if(file.exists() && !file.canWrite())
            	file.delete();
           out = new java.io.FileOutputStream(file);
            out.write(ba);
            out.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            return new RetMessage(false,"失败！"+e.getMessage());
        }finally{
        	if(out!=null){
        		try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
        return new RetMessage(true,"");
    }

	public RetMessage upLoadFile(String path, byte[] ba, String tofilepath){


		path = convertFilePath(path);
		tofilepath=convertFilePath(tofilepath);
		path=tofilepath+"/"+path.split("/")[path.split("/").length-1];

		try {
			/**
			 * 如果父目录不存在，则创建
			 */
			File parentDir = new File(tofilepath);
			if (!parentDir.exists()) {
				parentDir.mkdirs();
			}

			return writeFiler(path, ba);

		} catch (Exception e) {
			Logger.error("error",e);
			return new RetMessage(false,"上传失败！"+e.getMessage());
		}
	
	}
	public  byte[]  downLoadFile(String filePath) throws BusiBeanException {
		java.io.File file = new java.io.File(filePath);
		if(!file.exists()){
			return null;
		}
		byte ba[] = new byte[(int) file.length()];
		try {
			FileInputStream fis = new FileInputStream(file);
			int nBebinReadLoc = 0;
			do {
				int nReadedInSize = fis.read(ba, nBebinReadLoc, ba.length
						- nBebinReadLoc);
				nBebinReadLoc += nReadedInSize;
			} while (nBebinReadLoc < ba.length);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ba;
	}
    /**
	 * 将上载文件保存到相对路径。
	 * 
	 * @filePath文件及其相对路径名，其格式"/test/test.txt"
	 */
	public String upLoadFile(String filePath, byte[] ba) throws BusiBeanException {

		filePath = convertFilePath(filePath);
		filePath = filePath.replace('+',' ');

		try {
			/**
			 * 如果父目录不存在，则创建
			 */
			File parentDir = new File(defaultDir);
			if (!parentDir.exists()) {
				parentDir.mkdirs();
			}

			writeFile(defaultDir + filePath.substring( filePath
					.lastIndexOf("/")), ba);

		} catch (Exception e) {
			Logger.error("error",e);
			return "上传失败"/* @res "上传失败!" */;
		}
		return null;
	}
	public static final String defaultDir = RuntimeEnv.getInstance().getNCHome()+ "/temp";
    private BaseDAO getBaseDao(){
    	if (baseDao==null){
    		baseDao=new BaseDAO(IContrastUtil.DESIGN_CON);
    	}
    	return baseDao;
    }
    /**
     * @desc 建立临时表
     * @param ywzd 业务表的引用字段
     * @param ywbm 业务表的表名
     * @param dzbm 对照表表名
     * */
    public String createVoucherPkTempTable(DipDatadefinitBVO ywzd,String ywbm,String dzbm) throws Exception {

		String tableName = null;
//		String oid = getOID().substring(16, 20);
		java.sql.Connection con = null;
		// java.sql.Statement stmt = null;
		java.sql.PreparedStatement pstmt = null;
		try {
			con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			tableName="dip_temp_"+dzbm.substring(7)+"_"+ywzd.getEname();
			String createTableSql = "create table "+tableName+" as " +
					"Select distinct a."+ywzd+",b.extepk From  "+ywbm+" a, "+dzbm+" b where a."+ywzd+"=b.contpk(+) order by extepk";
			createTableSql=ywzd.getEname()+"  "+ywzd.getType()+"("+ywzd.getLength()+"), extepk char(4)";
			nc.bs.mw.sqltrans.TempTable tmptab = new nc.bs.mw.sqltrans.TempTable();
			tableName = tmptab.createTempTable(con, tableName, createTableSql, null);
			String sql="insert into "+tableName+" Select distinct a."+ywzd.getEname()+",b.extepk From  "+ywbm+" a, "+dzbm+" b where a."+ywzd.getEname()+"=b.contpk(+) order by extepk ";
			exesql(sql);

			/** 分批插入记录 */
//			int size = pk_vouchers.length;
			/*StringBuffer insertSql = new StringBuffer();
			insertSql = new StringBuffer();
			insertSql.append("insert into " + tableName);
			insertSql.append(" (pk_voucher)");
			insertSql.append(" values(?)");
			((nc.jdbc.framework.crossdb.CrossDBConnection) con).setSqlTrans(false);
			((nc.jdbc.framework.crossdb.CrossDBConnection) con).setAddTimeStamp(false);
			pstmt = prepareStatement(con, insertSql.toString());
			for (int i = 0; i < size; i++) {
				pstmt.setString(1, pk_vouchers[i]);
				executeUpdate(pstmt);
			}
			executeBatch(pstmt);*/
			((nc.jdbc.framework.crossdb.CrossDBConnection) con).setSqlTrans(true);
		} catch (Exception e) {
			System.out.println("建临时表出现错误：" + e.getMessage());
			// if (((nc.jdbc.framework.crossdb.CrossDBConnection)
			// con).getDatabaseType() != nc.jdbc.framework.util.DBConsts.ORACLE)
			// dropTempTable(tableName);
			throw e;
		} finally {
			try {
				// if (stmt != null)
				// stmt.close();
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return tableName;
	}
	

	/**
	 * 获取标志头、数据或尾map的list(顺序对照,字段对照)
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public List getDef_hMap(String sql,String type) throws SQLException{
		if (sql == null || sql.equals("")) {
			return null;
		}
		List<Map> values = new ArrayList();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
//		ResultSetMetaData rsmd = null;
		try {
			con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
//			rsmd = rs.getMetaData();
			if("顺序对照".equals(type)){
				while(rs.next()){
					Map<String,String> mapValue = new HashMap<String,String>();
					String key  = rs.getString("CODE");
					//这里的value可能是用逗号分隔的编码字符串，解析数据时需要注意
					String value = rs.getString("DATAKIND");
					mapValue.put(key, value);
					values.add(mapValue);
				}
			}else{//字段对照
				while(rs.next()){
					Map<String,String> mapValue = new HashMap<String,String>();
					String key  = rs.getString("CORRESKIND");
					//这里的value可能是用逗号分隔的编码字符串，解析数据时需要注意
					String value = rs.getString("DATAKIND");
					mapValue.put(key, value);
					values.add(mapValue);
				}
			}
			
			
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return values;
	}
	
	
	public String getNumber(String sql) throws SQLException{
		if (sql == null || sql.equals("")) {
			return null;
		}
		String numberStr = "";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				numberStr = rs.getString(1);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
		return numberStr;
	}
    
    /**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   执行sql语句
	*创建日期：2011-04-14
	*参数：   sql 串的list
	*返回：   返回执行sql语句是否成功，如果有一条失败则失败
	*/
    public boolean exectEverySql(List<String> sql){
    	boolean issuccess = true;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			int len=sql.size();
			if(len<=0){
				return false;
			}
			con=ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			for(String sq:sql){
				if(sq==null||sq.length()<=0){
					continue;
				}
				stmt = prepareStatement(con, sq);
				boolean su=stmt.execute();
				if(!su){
					issuccess=false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			issuccess=true;
			e.printStackTrace();
			Logger.debug(e.getMessage());
		}finally{
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
		return issuccess;

	}
    
    public void exectListSql(List<String> sql) throws Exception{
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			int len=sql.size();
			if(len<=0){
				return;
			}
			con=ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			for(String sq:sql){
				if(sq==null||sq.length()<=0){
					continue;
				}
				stmt = prepareStatement(con, sq);
				stmt.execute();
			}
		}finally{
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
		}
	}
    
    
    /**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   执行sql语句
	*创建日期：2011-04-14
	*参数：   sql 串的list
	*返回：   返回执行sql语句是否成功，如果有一条失败则失败
	*/
    public RetMessage exectEverySqlByHandCommit(List<String> sql){
    	
//			获得连接
			PersistenceManager sessionManager = null;
			JdbcSession session =null;
			RetMessage ret=new RetMessage();
			String exesql="";
			int count=1;
			try {
				//开始JDBC会话
				sessionManager = PersistenceManager.getInstance(IContrastUtil.DESIGN_CON);
				session= sessionManager.getJdbcSession();		
				//SQLParameter param = new SQLParameter(); //构造参数对象
				//param.addParam("1001"); //添加参数
				for(int i=0;i<sql.size();i++){
					exesql=sql.get(i);
					session.executeUpdate(exesql);
					count++;
				}
				session.executeUpdate("commit");
			}catch (Exception e) {
				e.printStackTrace();
				try {
					session.executeUpdate("rollback");
				} catch (DbException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ret.setMessage("执行第"+count+"条sql语句错误["+exesql+"]："+e.getMessage());
				ret.setIssucc(false);
				return ret;
			}finally { 
				
				if (sessionManager!=null)
					sessionManager.release(); //关闭和释放连接会话
				if(session!=null){
					session.closeAll();//关闭释放连接
				}
			}
			ret.setIssucc(true);
			ret.setMessage("成功插入"+count+"条数据");
			return ret;
    	
//    	boolean issuccess = true;
//		Connection con = null;
//		PreparedStatement stmt = null;
//		try {
//			int len=sql.size();
//			if(len<=0){
//				return false;
//			}
//			con=ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
//			con.setAutoCommit(false);
//			for(int i=0;i<sql.size();i++){
//				String sq=sql.get(i);
//				if(sq==null||sq.length()<=0){
//					if(i==sql.size()-1){
//						stmt = prepareStatement(con, "commit");
//						stmt.execute();
//					}
//					continue;
//				}
//				stmt = prepareStatement(con, sq);
//				boolean su=stmt.execute();
//				if(!su){
//					issuccess=false;
//				}
//				if(i==sql.size()-1){
//					con.commit();
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			issuccess=true;
//			e.printStackTrace();
//			Logger.debug(e.getMessage());
//			try {
//				con.rollback();
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}finally{
//			
//			try {
//				if (stmt != null)
//					stmt.close();
//			} catch (Exception e) {
//			}
//			try {
//				if (con != null)
//					con.close();
//			} catch (Exception e) {
//			}
//		}
//		return issuccess;

	}
    
    /**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   执行sql语句
	*创建日期：2011-04-14
	*参数：   sql 串
	*返回：   返回执行sql语句是否成功
	*/
    public boolean exectEverySql(String sql){
    	boolean issuccess = false;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con=ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			stmt = prepareStatement(con, sql);
			issuccess=stmt.execute();
		} catch (SQLException e) {
			issuccess=true;
//			e.printStackTrace();
			Logger.debug(e.getMessage());
		}finally{
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
		return issuccess;

	}
    
	public QueryFieldImpl() throws NamingException {
		super();
		// TODO Auto-generated constructor stub
	}

	public String queryfield(String sql,String designn)throws BusinessException, SQLException,DbException{
//		testwebservice();//测试
		if (sql == null || sql.equals(""))
			return "";

		String value = "";

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if(designn==null||designn.equals("")){
			designn=IContrastUtil.DESIGN_CON;
		}
		try {
			con = ConnectionFactory.getConnection(designn);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			if (rs.next()) {
				value = rs.getString(1);
			} else
				value = "";

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
		return value;
	}
	
	/**
	*版权信息：商佳科技
	*作者：   袁廷勤
	*版本：   
	*描述：   只返回一个字段单个结果
	*创建日期：2011-04-14
	*参数：   sql 串
	*返回：   只返回一个字段单个结果
	*/
	public String queryfield(String sql) throws BusinessException,
			SQLException, DbException {
//		testwebservice();//测试
		if (sql == null || sql.equals(""))
			return "";

		String value = "";

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			if (rs.next()) {
				value = rs.getString(1);
			} else
				value = "";

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
		return value;
	}

	/**
	*版权信息：商佳科技
	*作者：   袁廷勤
	*版本：   
	*描述：   返回一个字段的所有结果
	*创建日期：2011-04-14
	*参数：   sql 串
	*返回：   返回一个字段的所有结果
	*/
	public List queryfieldList(String sql) throws BusinessException,
			SQLException, DbException {
		if (sql == null || sql.equals("")) {
			return null;
		}
		List value = new ArrayList();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

//			if (rs.next()) {
//				// value = rs.getString(1);
//				// int row = rs.getRow();
//				value.add(rs.getString(1));
//				
//			} else {
//				value = null;
//			}
			
			while(rs.next()){
				value.add(rs.getString(1)==null?"":rs.getString(1));
			}
			
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return value;
	}

	
	/**
	*版权信息：商佳科技
	*作者：   袁廷勤
	*版本：   
	*描述：   返回一个字段不重复的结果
	*创建日期：2011-04-14
	*参数：   sql 串
	*返回：   返回一个字段不重复的结果
	*/	
	public Set queryfieldSet(String sql) throws BusinessException,
			SQLException, DbException {
		if (sql == null || sql.equals("")) {
			return null;
		}
		Set value = new HashSet();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while(rs.next()) {
				// value = rs.getString(1);
				// int row = rs.getRow();
				value.add(rs.getString(1)==null?"":rs.getString(1));

			} 
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return value;
	}

	
	/**
	*版权信息：商佳科技
	*作者：   袁廷勤
	*版本：   
	*描述：   返回一个sql语句的结果集
	*创建日期：2011-04-14
	*参数：   sql 串
	*返回：   返回一个sql语句的结果集
	*/	
	public List queryFieldSingleSql(String sql) throws BusinessException, SQLException, DbException {
		if (sql == null || sql.equals("")) {
			return null;
		}
		List values = new ArrayList();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		try {
			con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rsmd = rs.getMetaData();

			while(rs.next()){
				List value = new ArrayList();
				for(int i=1;i<=rsmd.getColumnCount();i++){
					String columnValue = rs.getString(i)==null?"":rs.getString(i);
					value.add(columnValue);
				}
				values.add(value);
			}
			
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return values;
	}

	/**
	*版权信息：商佳科技
	*作者：   袁廷勤
	*版本：   
	*描述：   返回多个sql的结果集
	*创建日期：2011-04-14
	*参数：   sql 串
	*返回：   返回多个sql的结果集
	*/	
	public List queryfieldMoreSql(List sql) throws BusinessException, SQLException, DbException {
		if (sql == null || sql.size()==0) {
			return null;
		}
		List allValues = new ArrayList();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		try {
			con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			for(int i=0;i<sql.size();i++){
				List values = new ArrayList();
				String s = sql.get(i).toString();
				stmt = con.prepareStatement(s);
				rs = stmt.executeQuery();
				rsmd = rs.getMetaData();
				while(rs.next()){
					List value = new ArrayList();
					for(int j=1;j<=rsmd.getColumnCount();j++){
						String columnValue = rs.getString(j)==null?"":rs.getString(j);
						value.add(columnValue);
					}
					values.add(value);
				}
				allValues.add(values);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return allValues;
	}
	public java.util.List querySuperVO(String sql) throws BusinessException
	{
		try{
			java.util.List list=(java.util.List) getBaseDao().executeQuery(sql, new nc.jdbc.framework.processor.ArrayListProcessor());
			return list;
	}catch(Exception ex)
	{
		ex.printStackTrace();
		throw new BusinessException(ex.getMessage());
	}

	}	
	/**
	*版权信息：商佳科技
	*作者：   袁廷勤
	*版本：   
	*描述：   在后台执行sql 语句
	*创建日期：2011-04-14
	*参数：   sql 串
	*返回：   无
	 * @throws DbException 
	*/		
	   public void exesql(String sql)throws BusinessException, DbException{
//			获得连接
			PersistenceManager sessionManager = null;
			JdbcSession session =null;
			try {
				//开始JDBC会话
				sessionManager = PersistenceManager.getInstance(IContrastUtil.DESIGN_CON);
				session= sessionManager.getJdbcSession();		
				session.executeUpdate(sql); 
			}finally { 
				if (sessionManager!=null)
					sessionManager.release(); //关闭和释放连接会话
				if(session!=null){
					session.closeAll();//关闭释放连接
				}
			}
			
	    }

		public List querySupervoByVoname(Class supervoname,String sql) throws Exception{
			return (List) getBaseDao().retrieveByClause(supervoname, sql);
		}
		/**
		*版权信息：商佳科技
		*作者：   冯建芬
		*版本：   
		*描述：   返回单个vo
		*创建日期：2011-04-14
		*参数：   vo类名，主键值
		*返回：   根据主键查找vo
		*/	
		public SuperVO querySupervoByPk(Class supervoname,String pk) throws Exception{
			return (SuperVO) getBaseDao().retrieveByPK(supervoname, pk);
		}
		/**
		*版权信息：商佳科技
		*作者：   袁廷勤
		*版本：   
		*描述：   返回单个sql的List(map)结果集
		*创建日期：2011-04-14
		*参数：   sql 串
		*返回：   返回单个sql的List(map)结果集
		*/		   
	   public List queryListMapSingleSql(String sql) throws BusinessException, SQLException, DbException {
			if (sql == null || sql.equals("")) {
				return null;
			}
			List<Map> values = new ArrayList();

			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			ResultSetMetaData rsmd = null;

			try {
				con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();
				rsmd = rs.getMetaData();

				while(rs.next()){
					Map<String,String> mapValue = new HashMap<String,String>();
					int colCount = rsmd.getColumnCount();
					for(int i=1;i<=colCount;i++){
						String colName = rsmd.getColumnName(i);
						String colValue = rs.getString(colName);
						mapValue.put(colName, colValue);
					}
					values.add(mapValue);
				}
				
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
				}
				try {
					if (stmt != null)
						stmt.close();
				} catch (Exception e) {
				}
				try {
					if (con != null)
						con.close();
				} catch (Exception e) {
				}
			}

			return values;
		}
	   /**
	    * 获取数据格式对照相关的数据
	    * @param sql sql语句
	    * @param messformat 消息流格式
	    * @return
	    * @throws BusinessException
	    * @throws SQLException
	    * @throws DbException
	    */
		@SuppressWarnings("unchecked")
		public List queryLMGSSql(String sql,String messformat) throws BusinessException,
				SQLException, DbException {
			if (sql == null || sql.equals("")) {
				return null;
			}
			List<Map> values = new ArrayList();
			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			ResultSetMetaData rsmd = null;
			String sql1 = "";
			try {
				con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();
				rsmd = rs.getMetaData();
				String key= "";
				String value = "";
				String code = "";
				if("顺序对照".equals(messformat)){
					while (rs.next()) {
						Map<String, String> mapValue = new HashMap<String, String>();
						int colCount = rsmd.getColumnCount();
						for (int i = 1; i <= colCount; i++) {
							String colName = rsmd.getColumnName(i).toLowerCase();
							String colValue = rs.getString(colName);
							if("aaa".equals(colName)){
								key = colValue;
							}else if("bbb".equals(colName)){
								sql1 = "select * from dip_messagelogo where  ='"+colValue+"'";
								code = this.queryfield(sql1);
								value = code;
							}
						}
						mapValue.put(key, value);
						values.add(mapValue);
					}
				}else{
					while (rs.next()) {
						Map<String, String> mapValue = new HashMap<String, String>();
						int colCount = rsmd.getColumnCount(); 
						for (int i = 1; i <= colCount; i++) {
							String colName = rsmd.getColumnName(i).toLowerCase();
							String colValue = rs.getString(colName);
							if("ccc".equals(colName)){
								key = colValue;
							}else if("bbb".equals(colName)){
								sql1 = "select * from where  ='"+colValue+"'";
								code = this.queryfield(sql1);
								value = code;
							}
						}
						mapValue.put(key, value);
						values.add(mapValue);
					}
				}
				
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
				}
				try {
					if (stmt != null)
						stmt.close();
				} catch (Exception e) {
				}
				try {
					if (con != null)
						con.close();
				} catch (Exception e) {
				}
			}

			return values;
		}
		
		
		

		/**
		*版权信息：商佳科技
		*作者：  袁廷勤
		*版本：   
		*描述：   后台执行预警 参数：预警类名 日期：2010-09-26 作者：商佳科技 ytq
		*创建日期：2011-04-14
		*参数：   sql 串
		*返回：   返回单个sql的List(map)结果集
		*/	
public int exePluginalert(String pluginclassname) {
	String sql = " select type_name from pub_alerttype where busi_plugin='nc.bs.bkysxm.plugin.UploadFilePlugin' ";
	try {
		String typename = queryfield(sql);//预警名称
		//System.out.println( "查找到任务:"+typename );
//		NtbLogger.print( "查找到任务:"+typename );
		exePluginServer(pluginclassname);
	} catch (BusinessException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (DbException e) {
		e.printStackTrace();
	}
	return 0;
}	

/**
*版权信息：商佳科技
*作者：   袁廷勤
*版本：   
*描述：   执行预警 参数：预警类名 日期：2010-09-26 作者：商佳科技 ytq
*创建日期：2011-04-14
*参数：   sql 串
*返回：   返回单个sql的List(map)结果集
*/	
	public boolean exePluginServer(String typename) {
		if ("".equals(typename) || null == typename) {
			startTimePreAlert();
			return false;
		} else if ("login".equals(typename)) {
//			startLoginPreAlert();// 执行登录预警
//			startTimePreAlert();
			return false;
		}

		return false;

	}

	/**
	*版权信息：商佳科技
	*作者：   袁廷勤
	*版本：   
	*描述：   启动一个预警,定时 参数：预警类名 日期：2010-09-26 作者：商佳科技 ytq
	*创建日期：2011-04-14
	*参数：   sql 串
	*返回：   返回单个sql的List(map)结果集
	*/	
	public void startTimePreAlert(){
		String typename ="nc.bs.bkysxm.plugin.UploadFilePlugin";
		ArrayList alRet = null;
		try {
//		XXX::查询所有被监控的任务对象
			//System.out.println( "执行任务中1:"+typename );
		IPreAlertConfigService pcs = (IPreAlertConfigService) NCLocator.getInstance().lookup(
		IPreAlertConfigService.class.getName());
		//System.out.println( "执行任务中2:"+typename +":"+PfUtilUITools.getLoginUser().toString());
		alRet = (ArrayList)pcs.listAllTasks(false, "");
		//alRet = (ArrayList)pcs.listAllTasks(false, PfUtilUITools.getLoginUser());
		//System.out.println( "执行任务中3:"+typename );
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		if(alRet!=null && alRet.size()>0){
			//System.out.println( "执行任务中4:"+typename );
		for (Object obj : alRet) {
			//System.out.println( "执行任务中5:"+typename );
				TaskAdminPojo task = (TaskAdminPojo) obj;
				//System.out.println("任务名称:"+task.getTaskname()+":"+task.getStatus());
//				NtbLogger.print("任务名称:"+task.getTaskname()+":"+task.getStatus());
				if (task.getStatus() == TaskStatus.RUNNING.intValue()) {// 运行中
					if (task.getTaskname().contains(typename)) {
						//System.out.println( "执行任务中:"+typename );
//						NtbLogger.print( "执行任务中:"+typename );
				}
					}
				if (task.getStatus() != TaskStatus.RUNNING.intValue()) {// 运行中
					if (task.getTaskname().contains(typename)) {
						//开始执行预警
							try {
							IPreAlertConfigService pcs = (IPreAlertConfigService) NCLocator.getInstance().lookup(
									IPreAlertConfigService.class.getName());
							//System.out.println( "开始执行任务:预算包预警" );
							
//							NtbLogger.print( "开始执行任务:预算包预警");
//							pcs.removeTask(task.getTaskid());
//							System.out.println("pk:"+task.getPrimaryKey());
//							UploadFilePlugin plgin = new UploadFilePlugin();
//							Key[] keys = new  Key[1];
//							Key ke = new Key();
//							ke.setName("vyear");
//							ke.setValue("2011");
//							keys[0]=ke;
////							keys[0].setName("vyear");
////							keys[0].setValue("2011");
////							public String[] implementReturnMessage(Key[] arg0, Object arg1, UFDate arg2) throws BusinessException {
////							public String   implementReturnMessage(Key[] arg0, String arg1, UFDate arg2) throws BusinessException {
//							Object o = new Object();
//					        UFDate   now   =   new   UFDate(new Date()); 
//					        CurrEnvVO vo = new CurrEnvVO();
//					        vo.setPk_corp("0001");
//							String[] ss= plgin.implementReturnMessage(keys, vo, now);
//							BgWorkingContext co= new BgWorkingContext();
//							co.setPk_corp("0001");
//							co.setPk_user("");
//							TimeConfigVO tvo = new TimeConfigVO();
//							tvo.setTerminated(true);
//							pcs.startOnceTask("bkysxm", "nc.bs.bkysxm.plugin.UploadFilePlugin", co, tvo);
//							pcs.wait(1000);
							pcs.manualExecute(task.getTaskid());
							
//							AlertregistryVO registry =null;
//
//							CurrEnvVO env = initImmediateCurrEnvVO(registry);
//
//							// 即时消息的创建和发送时间...
//							long createTime = System.currentTimeMillis();
//							long sendTime = createTime;							
//							String dbfileName = (registry.getFilename() == null || registry.getFilename().trim().length() <= 0) 
//							?registry.getAlertname().trim()	: registry.getFilename().trim();
//							String noPrefileName = PaUtils.bandFileName(dbfileName, new UFDate(SendTime), new UFTime(
//									SendTime));
//							// ****************************************************************************************//
//							// 调用业务插件，生成预警信息，并持久化该信息到指定的文件中...
//							int implementType = callBusinessPlugin(registry.getAlertTypeVo(), registry, noPrefileName,
//									loginDate, envVO);
//
//							if (implementType == -1) {
//								Logger.warn("[Warning]条目:+" + registry.getAlertname() + "...pk=" + registry.getPrimaryKey()
//										+ "没有预警信息或者未成功地持久化预警信息");
//								return null; // 没有预警信息或者不成功地持久化预警信息...
//							}
//	
//							IPreAlertDB pa = (IPreAlertDB)NCLocator.getInstance().lookup(IPreAlertDB.class.getName());
//							long beginMillis = System.currentTimeMillis();
//							boolean workingResult = false;
//							String logStr = null;
//							AlertregistryVO m_registry = new AlertregistryVO();
//							m_registry = (AlertregistryVO) getBaseDao().retrieveByPK(AlertregistryVO.class, "0001ZZ100000000BEQ9T");
////							IVOPersistence vo = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
//							IUAPQueryBS bs  = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//							m_registry = (AlertregistryVO) bs.retrieveByPK(AlertregistryVO.class, "0001ZZ100000000BEQ9T");
//							pa.insertTaskLog_RequiresNew(m_registry, beginMillis, workingResult, logStr);
	
						} catch (Exception e) {
//							Logger.error(e.getMessage(), e);
							//System.out.println( "立即执行任务出现异常，请刷新后重试" );
						}
//						return true;
					}
//					if (task.getTaskname().contains("支付状态下载")) {
//						return true;
//					}
				}				
			}

		}else{
			//System.out.println("没有预警任务!");
//			NtbLogger.print("没有预警任务!");
		}		
	}
    //启动一个预警,登录
//	public void startLoginPreAlert(){
////        if (ClientEnvironment.getInstance().getUserType() == ClientEnvironment.USER) {
//            new Thread() {
//                public void run() {
//                	PreAlertService_bkys bkys = new PreAlertService_bkys();
//                	String[] files=null;
//					try {
//						files = bkys.showMessageAlertFileNameByTime("");
//					} catch (BusinessException e1) {
//						e1.printStackTrace();
//					}
//                	try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
////					showAlterFiles(files);
//                }
//            }.start();
////        }
//	}
//    private void showAlterFiles(String[] files) {
//        //			?0?0?1?7?1?7?1?7?0?4?1?7
//        if (files != null) {
//            System.out.println("==== Funcnode Alter Files ====");
//            for (int i = 0; i < files.length; i++) {
//                System.out.println(files[i]);
//                try {
//                    String str = "";
//                    files[i] = files[i].replace('\\', '/');
//                    StringTokenizer st = new StringTokenizer(files[i], "/");
//                    while (st.hasMoreTokens()) {
//                        str += "/" + java.net.URLEncoder.encode(st.nextToken(), "UTF-8");
//                    }
//                    java.net.URL url = new java.net.URL(ClientAssistant.getServerURL() +"NCFindWeb?service=prealert&filename="+ str.substring(1));
//                    ClientAssistant.showDocument(url, "_blank");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("======== End =========");
//        } else {
//            System.out.println("========No alert files!============");
//        }
//        //
//
//    }
	
	/**
	 * 将上载文件保存到相对路径。
	 * 
	 * @filePath文件及其相对路径名，其格式"/id/test.htm"
	 */
//	public String upLoadFile(String url, String str)
//			throws BusiBeanException {
//		try {
//			FileWriter fw = new FileWriter(RuntimeEnv.getInstance().getNCHome() + url);
//			fw.write(str);
//			fw.close();
//		} catch (Exception e) {
//			Logger.error("error",e);
//			return nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
//					"_beans", "UPP_uapcom0-000003")/* @res "上传失败!" */;
//		}
//		return null;
//	}

	/**
	*版权信息：商佳科技
	*作者：   袁廷勤
	*版本：   
	*描述：   得到IP地址 作者：商佳科技 ytq
	*创建日期：2011-04-14
	*参数：   sql 串
	*返回：   返回单个sql的List(map)结果集
	*/		
	public String getServerIP(){
		InetAddress addr=null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}	
		String IP = addr.getHostAddress().toString();
		return IP;
	}
	/**
	 * 关闭输出文件流
	 * @param bout
	 */
	private static void closeOutFile(OutputStream bout) {
		try {
			if (bout != null)
				bout.close();
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}
	}	
	/**
	 * 上传文件代码
	 * 创建日期:(2003-2-28 10:15:04)
	 */
	private void onUpload(String localFilePath) {
		java.io.File file = new java.io.File(localFilePath);
		byte ba[] = new byte[(int) file.length()];
		try {
			FileInputStream fis = new FileInputStream(file);
			int nBebinReadLoc = 0;
			do {
				int nReadedInSize = fis.read(ba, nBebinReadLoc, ba.length
						- nBebinReadLoc);
				nBebinReadLoc += nReadedInSize;
			} while (nBebinReadLoc < ba.length);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String rs = null;
		try {
			rs = ((IFileManager)NCLocator.getInstance().lookup(IFileManager.class.getName())).upLoadFile(localFilePath, ba);
		} catch (Exception e) {
			e.printStackTrace();
//			NtbLogger.print("从辅服务器上传到主服务器出错！");
		}
	}
	/**
	*版权信息：商佳科技
	*作者：   袁廷勤
	*版本：   
	*描述：   得到nchome的路径
	*创建日期：2011-04-14
	*参数：   
	*返回：   得到nchome的路径
	*/	
	public String getNchomeDir() {
		return RuntimeEnv.getInstance().getNCHome();
	}
	public void onServerImpExcel(String yyyy, String pk_corp) {
		// TODO Auto-generated method stub
		
	}
	public int writeTreeNode(String year, String filename, String pk_parent) {
		// TODO Auto-generated method stub
		return 0;
	}
	public String dataProc(String hpk){
		if(hpk==null||hpk.length()<=0){
			return null;
		}
		DipDataproceHVO hvo = null;
		try {
			hvo = (DipDataproceHVO) baseDao.retrieveByPK(DipDataproceHVO.class, hpk);
			if(hvo==null){
				return null;
			}
			String sql=hvo.getProcecond();
			String tableanme=hvo.getProcetab();
			String procetype=hvo.getProcetype();
			return dataproc(sql,tableanme,procetype);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e1){
			e1.printStackTrace();
		}
		return null;
	}
	public String dataproc(String sql, String tablename,String procetype) throws BusinessException, SQLException, DbException {
		// TODO Auto-generated method stub
		
		if("数据汇总".equals(procetype)){
			sjjg(sql,tablename,procetype);
		}
		if("数据清洗".equals(procetype)){
			sjqx(sql,tablename,procetype);
		}
		if("数据卸载".equals(procetype)){
			sjxz(sql,tablename,procetype);
		}
		
		return null;
	}


	/**
	 * 数据汇总
	 * */
	public void sjjg(String sql, String tablename,String procetype) throws BusinessException, SQLException, DbException{
//		查询加工后的结果集
		List listValue = queryListMapSingleSql(sql);
		String tagertField = ""; //目标字段
		String sourceField = "";//源字段
		String tagertFieldValue = "";//目标字段值
		if(listValue!=null && listValue.size()>0){
			//查询需要插入加工数据表的字段，后续需优化
			sql = "select b.cname,b.ename,b.quotetable,b.quotecolu,b.ispk from dip_dataproce_h h " +
			" left join dip_dataproce_b b on b.pk_dataproce_h = h.pk_dataproce_h " +
			" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.procetab='"+tablename+"' ";
			List listTableV = queryFieldSingleSql(sql);
			UFBoolean isPK = new UFBoolean(false);
			String insertSql = "";
			
			for(int i = 0;i<listValue.size();i++){
				StringBuffer fileds = new StringBuffer();
				StringBuffer fieldsValue = new StringBuffer();
				HashMap mapValue = (HashMap)listValue.get(i);
				//组建插入语句
				insertSql = "insert into "+tablename+" (";
				
				if(listTableV!=null && listTableV.size()>0){
					for(int j = 0;j<listTableV.size();j++){
						List listField = (ArrayList)listTableV.get(j);
						tagertField = listField.get(1) == null ?"":listField.get(1).toString();
						sourceField = listField.get(3) == null?"":listField.get(3).toString();
						isPK = listField.get(4)==null ? new UFBoolean(false):new UFBoolean(listField.get(4).toString());
						//判断是否是PK
						if(isPK.booleanValue()){
							tagertFieldValue = this.getOID();
						}else{
							tagertFieldValue = mapValue.get(sourceField.toUpperCase())==null?"":mapValue.get(sourceField.toUpperCase()).toString();
						}
						
						fileds.append(sourceField+",");
						fieldsValue.append("'"+tagertFieldValue+"',");
					}
				}
				String field = fileds.toString().substring(0, fileds.toString().length()-1);
				String fieldValue = fieldsValue.toString().substring(0, fieldsValue.toString().length()-1);
				insertSql = insertSql+field+") values ("+fieldValue+") ";
				exesql(insertSql);
			}
		}
	}

	/**
	 * 数据清洗
	 * */
	public void sjqx(String sql, String tablename,String procetype) throws BusinessException, SQLException, DbException{
		exesql(sql);
		
	}

	/**
	 * 数据卸载
	 * */
	public void sjxz(String sql, String tablename,String procetype) throws BusinessException, SQLException, DbException{

		//第一种情况，源表追加备份，把当前表的所有数据，插入到备份表，然后清空当前表
		if("DIP_BAK1".equals(sql)){
			sql = "select 1 from user_tables where table_name = '"+tablename+"_BAK'";
			String tablebak = this.queryfield(sql);
			//查询备份表是否已创建
			if("1".equals(tablebak)){
				//直接备份数据
				sql = "insert into "+tablename+"_BAK select * from "+tablename+"";
				exesql(sql);
			}else{
				//创建表，并备份数据
				sql = "create table "+tablename+"_BAK as select * from "+tablename+" ";
				exesql(sql);
			}
			
			sql = "delete from "+tablename;
			exesql(sql);
		}
		if("DIP_BAK日期".equals(sql)){
			UFDate date = new UFDate(new Date());
			//第二种情况，把当前表的数据插入到新创建的“当前表明+日期”的表中，然后清空当前表
			String tablebakname = "";
			tablebakname = tablename+"_BAK_"+date.getYear()+"_"+date.getMonth()+"_"+date.getDay();
			sql = "select 1 from user_tables where table_name = '"+tablebakname+"'";
			String tablebak = this.queryfield(sql);
			if("1".equals(tablebak)){
				sql = "drop table "+tablebakname;
				exesql(sql);
			}
			
			//备份表
			sql = "create table "+tablebakname+" as select * from "+tablename+" ";
			exesql(sql);
			//清空当前表
			sql = "delete from "+tablename;
			exesql(sql);
		}
		
		
	}
	public List<SuperVO> queryVOBySql(String sql, SuperVO vo) {
		List<SuperVO> listvo = new ArrayList<SuperVO>();
		try {
			listvo = (ArrayList) getBaseDao().executeQuery(sql,
					new BeanListProcessor(vo.getClass()));
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listvo;
	}
	
	public String getOID(){


		String pk = null;
		try {
				String pk_corp = "0001";
			pk = new SequenceGenerator(IContrastUtil.DESIGN_CON).generate(pk_corp);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pk;

	
	}
	
	
	public String getOID(String pk_corp){
		String pk = null;
		try {
			if(pk_corp==null||pk_corp.trim().equals("")||pk_corp.length()!=4){
				pk_corp="0001";
			}
			pk = new SequenceGenerator(IContrastUtil.DESIGN_CON).generate(pk_corp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pk;

	
	}
	
	public String[] getOIDs(String pk_corp,int count){
		String[] pks = null;
		try {
			if(pk_corp==null||pk_corp.trim().equals("")||pk_corp.length()!=4){
				pk_corp="0001";
			}
			pks = new SequenceGenerator(IContrastUtil.DESIGN_CON).generate(pk_corp,count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pks;

	
	}
	
	/**数据接收的格式定义，放到一个事务里边*/
	public boolean saveDataFormat(String recepk,Map<String,DataformatdefBVO[]> desmap ,
			String[] pks,
			String[] lx,
			DataformatdefHVO[] hvos,List<DipDatarecSpecialTab> speciallist){
		boolean issucc=false;
		try {
			
			if(recepk!=null&&recepk.length()>0){
				String s1="delete  from  dip_dataformatdef_b bb where bb.pk_dataformatdef_h in(select hh.pk_dataformatdef_h from dip_dataformatdef_h hh  where hh.pk_datarec_h='"+recepk+"')";
				String s2="delete  from  Dip_Dataformatdef_h where pk_datarec_h='"+recepk+"'"; 
				String s3="delete from dip_datarec_specialtab where pk_datarec_h='"+recepk+"'";
				List<String> list=new ArrayList<String>();
				list.add(s1);
				list.add(s2);
				list.add(s3);
				exectEverySql(list);
			}
			
			
			List<DataformatdefBVO> insert =new ArrayList<DataformatdefBVO>();
			List<DataformatdefBVO> update=new ArrayList<DataformatdefBVO>();
			for(int i=0;i<pks.length;i++){
				DataformatdefHVO hvo=hvos[i];
				String hpk=null;
				if(hvo.getPrimaryKey()==null){
						hpk=getBaseDao().insertVO(hvo);
						DataformatdefBVO[] bvo=desmap.get(i+"-r");
						if(bvo!=null&&bvo.length>0){
							for(int j=0;j<bvo.length;j++){
								DataformatdefBVO bvoj=bvo[j];
								bvoj.setPk_dataformatdef_h(hpk);
								insert.add(bvoj);
							}
						}
				}else{
//					getBaseDao().updateVO(hvo);
					getBaseDao().insertVO(hvo);
					DataformatdefBVO[] bvo=desmap.get(i+"-r");
					if(bvo!=null&&bvo.length>0){
						for(int j=0;j<bvo.length;j++){
							DataformatdefBVO bvoj=bvo[j];
//							bvoj.setPk_dataformatdef_h(hpk);
							bvoj.setPk_dataformatdef_h(hvo.getPrimaryKey());
//							if(bvoj.getPrimaryKey()==null){
								insert.add(bvoj);
//							}else{
//								update.add(bvoj);
//							}
						}
					}
				}
			}
			
//			getBaseDao().deleteByClause(DataformatdefBVO.class, "pk_dataformatdef_h  in (select pk_dataformatdef_h from dip_dataformatdef_h where pk_datarec_h='"+recepk+"')");
			if(insert!=null&&insert.size()>0){
				getBaseDao().insertVOList(insert);
			}
			if(speciallist!=null&&speciallist.size()>0){
				getBaseDao().insertVOList(speciallist);
			}
//			if(update!=null&&update.size()>0){
//				getBaseDao().updateVOList(update);
//			}
			issucc=true;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return issucc;
	}
	public FormmulaCache getFormmulaCache() throws Exception {
		// TODO Auto-generated method stub
		if(FormmulaCache.init().formmulaFlag==null){
			FormmulaCache.init().getInstance();
		};
		return FormmulaCache.init();
	}
	public void putFormmulaCache(String key,String value) throws Exception {
		// TODO Auto-generated method stub
		
		FormmulaCache.init().put(key, value);
	}
	public String getFormmulaCacheValue(String key) throws Exception {
		// TODO Auto-generated method stub
		
		return FormmulaCache.init().get(key);
	}
	
	public void clearFormmulaCache() throws Exception {
		// TODO Auto-generated method stub
		
		 FormmulaCache.init().clear();
	}
	public int getCountFormmulaCacheValue() throws Exception{
		return FormmulaCache.init().getCount();
	}
	
	public RetMessage testDSConnect(String pk) throws Exception {
		OptByPlgImpl op=new OptByPlgImpl();
		Connection con=op.getCon(pk);
		if(con==null){
			return new RetMessage(false,"连接失败!");
		}else{
			con.close();
			return new RetMessage(true,"连接成功！");
		}
	}
	
	public long getDate()throws Exception{
		return System.currentTimeMillis();
	}
	
	public RetMessage getFileNamesList(String directorie){
		File file=new File(directorie);
		RetMessage ret=new RetMessage();
		if(file.exists()){
			File files[]=file.listFiles();
			List<String > list=new ArrayList<String >();
			if(files!=null&&files.length>0){
				for(int i=0;i<files.length;i++){
					String name=files[i].getName();
					if(name!=null&&name.length()>0){
						list.add(name);
						ret.setFileName(name);
						ret.setIssucc(true);
					}
				}	
			}else{
				ret.setIssucc(false);
				ret.setMessage(directorie+" 文件夹下没有文件");
			}
		}else{
			ret.setIssucc(false);
			ret.setMessage(directorie+" 文件夹不存在");
		}
		
		
		return ret;
	}
	/**
	 * 得到路径（directorie）下指定的所有类型文件
	 * @param directorie
	 * @param prex
	 * @return
	 */
	private List<String> listFileName=new ArrayList<String>();
	private List<String> listFileDir=new ArrayList<String>();
	public RetMessage getFileNamesList(String directorie,String prex){
		File file=new File(directorie);
		RetMessage ret=new RetMessage();
		if(file.exists()){
			File files[]=file.listFiles();
			if(files!=null&&files.length>0){
				for(int i=0;i<files.length;i++){
					if(files[i].isDirectory()){
						listFileDir.add(files[i].getAbsolutePath());
					}else{
						String name=files[i].getAbsolutePath();
						if(name!=null&&name.length()>0&&name.endsWith(prex)){
							listFileName.add(name);
						}
					}
				}	
			}else{
				ret.setIssucc(false);
				ret.setMessage(directorie+" 文件夹下没有"+prex+"文件");
			}
		}else{
			ret.setIssucc(false);
			ret.setMessage(directorie+" 文件夹不存在");
		}
		if(listFileDir!=null&&listFileDir.size()>0){
			for(int i=0;i<listFileDir.size();i++){
				getFileNames(listFileDir.get(i), prex);
			}
		}
		
		if(listFileName!=null&&listFileName.size()>0){
			ret.setIssucc(true);
			ret.setFielListName(listFileName);
		}else{
			ret.setIssucc(false);
			ret.setMessage(directorie+" 文件夹下没有"+prex+"文件");
		}
		
		
		return ret;
	}
	
	public void getFileNames(String directorie,String prex){
		if(directorie==null||directorie.trim().equals("")||prex==null||prex.trim().equals("")){
			return;
		}
		File file=new File(directorie);
		if(file.exists()){
			File files[]=file.listFiles();
			if(files!=null&&files.length>0){
				for(int i=0;i<files.length;i++){
					if(files[i].isDirectory()){
						getFileNames(files[i].getAbsolutePath(), prex);
					}else{
						String name=files[i].getAbsolutePath();
						if(name!=null&&name.length()>0&&name.endsWith(prex)){
							listFileName.add(name);
						}
					}
				}	
			}
		}
	}
	
	public RetMessage checkFtpServiceRegisterIsTure(String pk_ftpsourceregister) throws Exception {
		// TODO Auto-generated method stub
		RetMessage ret=new RetMessage();
		FtpSourceRegisterVO vo=(FtpSourceRegisterVO) querySupervoByPk(FtpSourceRegisterVO.class, pk_ftpsourceregister);
		
		if(vo!=null){
			ApacheCommonNetToFtpBase ftp=new ApacheCommonNetToFtpBase(vo.getAddress(),vo.getUsername(),vo.getPassword(),Integer.parseInt(vo.getPort()));
			boolean flag=ftp.connectServer();
			if(flag){
				ret.setIssucc(true);
				return ret;
			}else{
				ret.setIssucc(false);
				return ret;
			}
		}else{
			ret.setIssucc(false);
			ret.setMessage("ftp数据源注册信息已经被删除");
		}
		return ret;
	}
	public RetMessage downFileFromFtpToService(String ftppath, String ftpfileName, String pk_ftpsourceregister, String serviceFile,String hpk) throws Exception {
		// TODO Auto-generated method stub
		RetMessage ret=new RetMessage();
		if(ftppath==null||pk_ftpsourceregister==null||serviceFile==null){
			ret.setIssucc(false);
			ret.setMessage("文件路径、ftp数据源注册、备份路径不能为空");
			return ret;
		}
		FtpSourceRegisterVO vo=(FtpSourceRegisterVO) querySupervoByPk(FtpSourceRegisterVO.class, pk_ftpsourceregister);
		if(!serviceFile.endsWith("/")||serviceFile.endsWith("\\")){
			serviceFile=serviceFile+"/";
		}
		File newfile=new File(serviceFile);
		if(!newfile.exists()){
			boolean flag=newfile.mkdir();
			if(!flag){
				ret.setIssucc(false);
				ret.setMessage("创建"+serviceFile+"目录出错");
				return ret;
			}
		}
		if(vo!=null){
			ApacheCommonNetToFtpBase ftp=new ApacheCommonNetToFtpBase(vo.getAddress(),vo.getUsername(),vo.getPassword(),Integer.parseInt(vo.getPort()));
			if(ftpfileName!=null){//下载文件
				String filePath="";
				if(ftppath.endsWith("/")||ftppath.endsWith("\\")){
					filePath=ftppath+ftpfileName;
				}else{
					filePath=ftppath+"/"+ftpfileName;
				}
				filePath=filePath.replace("\\", "/");
//				RetMessage rm=ftp.downloadFile(name,ftppath);
				ret=ftp.writeFile(ftp.downloadFile(filePath), serviceFile+ftpfileName);
				if(ret.getIssucc()){
					ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, "下载文件"+ftpfileName+"到目录"+serviceFile+"成功"+"--"+IContrastUtil.DATAPROCESS_HINT);
				}else{
					ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, "下载文件"+ftpfileName+"到目录"+serviceFile+"失败");
				}
				return ret;
			}else{//下载文件夹
				ret=ftp.getListFileName(ftppath);
				if(!ret.getIssucc()){
					return ret;
				}
				List<String> childrenlistnames=ret.getfilename();
				if(childrenlistnames!=null&&childrenlistnames.size()>0){
					List<String> errList=new ArrayList<String>();
					List<String> succList=new ArrayList<String>();
					
					for(int i=0;i<childrenlistnames.size();i++){
						String name=childrenlistnames.get(i);
						//String filePath="";//
						if(name!=null&&!name.trim().equals("")){
//							if(ftppath.endsWith("/")||ftppath.endsWith("\\")){
//								filePath=ftppath+name;
//							}else{
//								filePath=ftppath+"/"+name;
//							}
							ftppath=ftppath.replace("\\", "/");
							//File file=new File(filePath);//
							RetMessage rm=ftp.downloadFile(name,ftppath);
							if(rm.getIssucc()){
								InputStream in=(InputStream) rm.getValue();
								RetMessage retmes= ftp.writeFile(in, serviceFile+name);	
								if(retmes.getIssucc()){
									succList.add(name);
									
								}else{
									errList.add(name);
									ret.setIssucc(false);
									ret.setFileName(name);
								}
							}else{
								errList.add(name);
								ret.setIssucc(false);
								ret.setFileName(name);
							}
							
							
							
						}
					}
					if(errList.size()<=0&&succList.size()>0){
						ret.setIssucc(true);
						ret.setMessage("成功下载"+succList.size()+"个文件");
						StringBuffer sb=new StringBuffer();
						for(int i=0;i<succList.size();i++){
							if(i==0){
								sb.append("下载"+succList.size()+"个文件[");
							}
							if(i==succList.size()-1){
								sb.append(succList.get(i)+"]到目录"+serviceFile+"成功");
							}else{
								sb.append(succList.get(i)+",");	
							}
							
						}
						if(sb.length()>0){
							ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, sb.toString().substring(0,sb.length())+"--"+IContrastUtil.DATAPROCESS_HINT);
						}
					}else{
						ret.setIssucc(false);
						ret.setMessage("成功下载"+succList.size()+"个文件，失败"+errList.size()+"个文件");
						StringBuffer sb=new StringBuffer();
					//	StringBuffer errsb=new StringBuffer();
						for(int i=0;i<succList.size();i++){
							if(i==0){
								sb.append("下载"+succList.size()+"个文件[");
							}
							if(i==succList.size()-1){
								sb.append(succList.get(i)+"]到目录"+serviceFile+"成功,");
							}else{
								sb.append(succList.get(i)+",");	
							}
						}
						for(int i=0;i<errList.size();i++){
							if(i==0){
								sb.append("下载"+errList.size()+"个文件[");
							}
							if(i==errList.size()-1){
								sb.append(errList.get(i)+"]到目录"+serviceFile+"失败");
							}else{
								sb.append(errList.get(i)+",");	
							}
						}
						if(sb.length()>0){
							ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, sb.toString());
						}
					}
					
				}else{
					ret.setIssucc(false);
					ret.setMessage(ftppath+"路径下边没有文件");
					return ret;
				}
				}
			}else{
				  ret.setIssucc(false);
				  ret.setMessage("没有找到ftp数据源注册");
			  }
		return ret;
	}
	/**
	 * 从服务器上把文件或者文件夹上传到ftp服务器上。
	 */
	public RetMessage upFileFromSevriceToFtp(String path, String fileName, String pk_ftpsourceregister,String ftpFile,String hpk) throws Exception {
		// TODO Auto-generated method stub
		RetMessage ret=new RetMessage();
		if(path==null||pk_ftpsourceregister==null){
			ret.setIssucc(false);
			ret.setMessage("文件路径和ftp数据源注册不能为空");
			return ret;
		}
		FtpSourceRegisterVO vo=(FtpSourceRegisterVO) querySupervoByPk(FtpSourceRegisterVO.class, pk_ftpsourceregister);
		
		if(vo!=null){
			ApacheCommonNetToFtpBase ftp=new ApacheCommonNetToFtpBase(vo.getAddress(),vo.getUsername(),vo.getPassword(),Integer.parseInt(vo.getPort()));
			if(fileName!=null){//上传文件
				String filePath="";
				if(path.endsWith("/")||path.endsWith("\\")){
					filePath=path+fileName;
				}else{
					filePath=path+"/"+fileName;
				}
					
				filePath=filePath.replace("\\", "/");
				File file=new File(filePath);
//				ret=ftp.uploadFile(file,false);
				ret=ftp.uploadFile(file, ftpFile, false);
				if(ret.getIssucc()){
					ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, "上传文件"+fileName+"到目录"+ftpFile+"成功"+"--"+IContrastUtil.DATAPROCESS_HINT);
				}else{
					ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, "上传文件"+fileName+"到目录"+ftpFile+"失败");
				}
				return ret;
			}else{//上传文件夹
				File file=new File(path);
				if(!file.exists()){
					ret.setIssucc(false);
					ret.setMessage(path+"文件夹不存在");
					return ret;
				}else{
					
					ret=getFileNamesList(path);
					int count=0;
					if(ret.getIssucc()){
						List<String> errList=new ArrayList<String>();
						List<String> successList=new ArrayList<String>();
						List<String> list=ret.getfilename();
						if(list!=null&&list.size()>0){
							ret=new RetMessage();
							for(int i=0;i<list.size();i++){
								String childFilename=list.get(i);
								if(childFilename!=null&&!childFilename.equals("")){
									String childFilenamePath="";
									if(path.endsWith("/")||path.endsWith("\\")){
										childFilenamePath=path+childFilename;
									}else{
										childFilenamePath=path+"/"+childFilename;
									}
									childFilenamePath=childFilenamePath.replace("\\", "/");
									File ff=new File(childFilenamePath);
									RetMessage rm=ftp.uploadFile(ff, ftpFile,false);
									if(rm.getIssucc()){
//										ret.setFileName(childFilename);
										successList.add(ff.getName());
									}else{
										if(rm.getMessage()!=null&&rm.getMessage().startsWith("ftp上创建")){
											return rm;
										}
										errList.add(ff.getName());
										ret.setFileName(ff.getName());
									}
								}
							}
							
							if(errList.size()<=0&&successList.size()>0){
								ret.setIssucc(true);
								ret.setMessage("成功上传"+successList.size()+"个文件");
								StringBuffer sb=new StringBuffer();
								for(int i=0;i<successList.size();i++){
									if(i==0){
										sb.append("上传"+successList.size()+"个文件[");
									}
									if(i==successList.size()-1){
										sb.append(successList.get(i)+"]到目录"+ftpFile+"成功");
									}else{
										sb.append(successList.get(i)+",");	
									}
									
								}
								if(sb.length()>0){
									ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, sb.toString()+"--"+IContrastUtil.DATAPROCESS_HINT);
								}
							}else{
								ret.setIssucc(false);
								ret.setMessage("成功上传"+successList.size()+"个文件，失败"+errList.size()+"个文件");
								StringBuffer sb=new StringBuffer();
								//	StringBuffer errsb=new StringBuffer();
									for(int i=0;i<successList.size();i++){
										if(i==0){
											sb.append("上传"+successList.size()+"个文件[");
										}
										if(i==successList.size()-1){
											sb.append(successList.get(i)+"]到目录"+ftpFile+"成功");
										}else{
											sb.append(successList.get(i)+",");	
										}
									}
									for(int i=0;i<errList.size();i++){
										if(i==0){
											if(sb.length()>0){
												sb.append(";");
											}
											sb.append("上传"+errList.size()+"个文件[");
										}
										if(i==errList.size()-1){
											sb.append(errList.get(i)+"]到目录"+ftpFile+"失败");
										}else{
											sb.append(errList.get(i)+",");	
										}
									}
									if(sb.length()>0){
										ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, sb.toString());
									}
								
							}
						}
					}else{
						return ret;
					}
				}
			}
		  }else{
			  ret.setIssucc(false);
			  ret.setMessage("没有找到ftp数据源注册");
		  }
		return ret;
	}
	
	public RetMessage exeCall(String sql) throws Exception {
		// TODO Auto-generated method stub
//		PersistenceManager sessionManager = null;
//		JdbcSession session =null;
//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		Connection ct=DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "nc502", "1");
//		CallableStatement cs=ct.prepareCall("call sp_pro3(?,?)");
//		cs.setString(1, "name1");
//		cs.setString(2, "newpass");
//		cs.execute();
//		cs.close();
//		ct.close();
		java.sql.Connection con = null;
		CallableStatement cs=null;
		// java.sql.Statement stmt = null;
		RetMessage ret=new RetMessage();
		try {
			//开始JDBC会话
			con = ConnectionFactory.getConnection(IContrastUtil.DESIGN_CON);
			if(!sql.startsWith("call")){
				sql="call "+sql;
			}
			sql=sql.replaceAll("'", ""); 
			String exesql=sql.substring(0, sql.indexOf("(")+1);
			 
			 String ss=sql.substring(sql.indexOf("(")+1, sql.lastIndexOf(")"));
			 String can[]=ss.split(",");
			 for(int i=0;i<can.length;i++){
				exesql=exesql+"?";
				if(i==can.length-1){
					exesql=exesql+")";
				}else{
					exesql=exesql+",";
				}
			 }
			 cs=con.prepareCall(exesql);
			 for(int i=0;i<can.length;i++){
				 cs.setObject(i+1, can[i]);
//				 cs.setString(i+1, can[i]);
			 }
			cs.execute();
		    ret.setIssucc(true);
		}finally { 
			
			if (cs!=null)
				cs.close(); //关闭和释放连接会话
			if(con!=null){
				con.close();
			}
		}
		return ret;
	}
	/**
	 * liyunzhe  在数据流程执行时候，如果前边任务执行成功，就直接commit;
	 * @param sql
	 * @throws BusinessException
	 * @throws DbException
	 */
	  public void exesqlCommit(String sql)throws BusinessException, DbException{
//			获得连接
			PersistenceManager sessionManager = null;
			JdbcSession session =null;
			try {
				//开始JDBC会话
				sessionManager = PersistenceManager.getInstance(IContrastUtil.DESIGN_CON);
				session= sessionManager.getJdbcSession();		
				//SQLParameter param = new SQLParameter(); //构造参数对象
				//param.addParam("1001"); //添加参数
				session.executeUpdate(sql); 
				session.executeUpdate("commit");
//			}catch (DbException e) {
//				throw e;
				//异常处理
				//Logger.getAnonymousLogger().info("test");
				//nc.bs.logging.Logger.debug("aaa");
			}finally { 
				
				if (sessionManager!=null)
					sessionManager.release(); //关闭和释放连接会话
			}
			
			if(session!=null){
				session.closeAll();//关闭释放连接
			}
	    }
	public Map<String, String> getAllChild(String pk_corp,Map<String, String> corpMap) throws Exception {
		// TODO Auto-generated method stub
		if(pk_corp==null||pk_corp.trim().equals("")){
			return null;
		}
		String sql=" select pk_corp from bd_corp where fathercorp='"+pk_corp+"'";
		try {
			List list=queryfieldList(sql);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String pk_corpi=list.get(i)==null?"":list.get(i).toString();
					if(pk_corpi!=null&&!pk_corpi.equals("")){
						if(isHasChild(pk_corpi)){
							corpMap.put(pk_corpi, pk_corpi);
							getAllChild(pk_corpi,corpMap);
						}else{
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return corpMap;
	}
	private  boolean isHasChild(String pk_corp ){
		boolean flag=false;
		String sql=" select pk_corp from bd_corp where fathercorp='"+pk_corp+"'";
			try {
				List list=queryfieldList(sql);
				if(list!=null&&list.size()>0){
					flag=true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag=false;
			}
			
		return flag;
	}
	public RetMessage docheck(String pk_bus) throws Exception {
		RetMessage ret=new RetMessage();
		List list=new ArrayList();
		List hvoList=(List) getBaseDao().retrieveByClause(DataverifyHVO.class, "pk_datachange_h='"+pk_bus+"'");
		if(hvoList==null||hvoList.size()<=0){
			ret.setIssucc(true);
			ret.setMessage("没有定义数据校验！");
			return ret;
		}else{
			DataverifyHVO hv=(DataverifyHVO) hvoList.get(0);
			List bvoList =  (List) getBaseDao().retrieveByClause(DataverifyBVO.class, "pk_dataverify_h='"+hv.getPrimaryKey()+"'");
			if(bvoList==null||bvoList.size()<=0){
				ret.setIssucc(true);
				ret.setMessage("没有定义数据校验！");
				return ret;
			}else{
				for(int i=0;i<bvoList.size();i++){
					DataverifyBVO bv=(DataverifyBVO) bvoList.get(i);
					String checkclass=bv.getName();//类的名称。
					Object inst;
					Class cls = Class.forName(checkclass);
					inst = cls.newInstance();
					ICheckPlugin checkPlug=null;
					if (inst instanceof ICheckPlugin) {
						checkPlug = (ICheckPlugin) inst;
					}
					if(bv.getVdef2().equals("通用校验")){
						CheckMessage cmsg=checkPlug.doCheck(bv.getVerifycon(),bv.getVector(),bv.getVdef3());
						if(cmsg.isSuccessful()){
							list.add(cmsg.getMessage()+"--"+IContrastUtil.DATAPROCESS_HINT);
							ret.setIssucc(true);
						}else{
							ret.setIssucc(false);
							ret.setMessage(cmsg.getMessage());
							list.add(cmsg.getMessage()+"--"+IContrastUtil.DATAPROCESS_ERR);
							break;
						}
					}else{
						ret.setMessage("只支持通用校验，不支持其他类型校验");
						ret.setIssucc(false);
					}
				}
			}
		}
		ret.setErrlist(list);
		return ret;
	}
	
}