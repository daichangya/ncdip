package nc.bs.sm.loginui;

import java.sql.Connection;

import nc.bs.logging.Logger;
/**
 * �˴���������˵����
 * �������ڣ�(2003-7-28 12:14:03)
 * @author�������
 */
public class Login3DMO extends nc.bs.pub.DataManageObject {
/**
 * Login3DMO ������ע�⡣
 * @exception javax.naming.NamingException �쳣˵����
 * @exception nc.bs.pub.SystemException �쳣˵����
 */
public Login3DMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super();
}
/**
 * Login3DMO ������ע�⡣
 * @param dbName java.lang.String
 * @exception javax.naming.NamingException �쳣˵����
 * @exception nc.bs.pub.SystemException �쳣˵����
 */
public Login3DMO(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super(dbName);
}
public boolean canConnect() throws java.sql.SQLException{
	boolean connect = false;
	Connection con = null;
	try {
		con = getConnection();
		if(con != null)
			connect = true;
	} catch (Exception e) {
		Logger.error(e.getMessage(), e);
	}finally{
		if (con != null)
			con.close();
	}
	return connect;
}
/**
 * �ж������ݿ����Ƿ����ָ�����û����롣
 * �������ڣ�(2003-7-28 12:15:19)
 * @return boolean
 * @param userCode java.lang.String
 * @exception java.sql.SQLException �쳣˵����
 */
public boolean containUser(String userCode, String psw) throws java.sql.SQLException {
	java.sql.Connection con = null;
	java.sql.PreparedStatement stmt = null;
	java.sql.ResultSet rs = null;
	String sql = "select * from sm_user where user_code = ? and user_password = ? ";
	try {
		con = getConnection();
		stmt = con.prepareStatement(sql);
		if (userCode == null) {
			stmt.setNull(1, java.sql.Types.NULL);
		} else {
			stmt.setString(1, userCode);
		}	
		if (psw == null) {
			stmt.setNull(2, java.sql.Types.NULL);
		} else {
			stmt.setString(2, psw);
		}	
		rs = stmt.executeQuery();
		if (rs.next()){
			return true;
		}else{
			return false;
		}
	} finally {
		if (rs != null)
			rs.close();
		if (stmt != null)
			stmt.close();
		if (con != null)
			con.close();
	}

}
public boolean containUser(String userCode) throws java.sql.SQLException {
	java.sql.Connection con = null;
	java.sql.PreparedStatement stmt = null;
	java.sql.ResultSet rs = null;
	String sql = "select * from sm_user where user_code = ? ";
	try {
		con = getConnection();
		stmt = con.prepareStatement(sql);
		if (userCode == null) {
			stmt.setNull(1, java.sql.Types.NULL);
		} else {
			stmt.setString(1, userCode);
		}	

		rs = stmt.executeQuery();
		if (rs.next()){
			return true;
		}else{
			return false;
		}
	} finally {
		if (rs != null)
			rs.close();
		if (stmt != null)
			stmt.close();
		if (con != null)
			con.close();
	}

}
}
