package nc.impl.dip.pub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import nc.bs.ml.NCLangResOnserver;
import nc.itf.dip.pub.IRecDataService;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.BusinessException;
/**
 * 
 * @author Administrator
 *此接口方便实现  在国通给过了结束标志以后，根据结束位来做以后的操作
 * 0：未接收到的数据
 * 1：数据接收成功
 */
public class RecDataDMO implements IRecDataService{

	//查看  关闭无用连接
	PersistenceManager sessionManager = null;
	JdbcSession session=null;
	
	//从配置文件中获取配置文件中的信息，此为数据源信息
	String source=IContrastUtil.DESIGN_CON;
	
	public String cbuToXML(String dwbm) throws BusinessException {
		return null;
	}

	public String cbuToXMLed(String dwbm) throws BusinessException {
		int i=0;
		try {
			sessionManager = PersistenceManager.getInstance(source);
			session = sessionManager.getJdbcSession();
			String sql="update yz_datarec set flag=4 where  dr=0 and flag=3 and sjdwbm in( "+dwbm+" )";
			i=session.executeUpdate(sql);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			throw new BusinessException(e);
		}finally{
			if(sessionManager!=null){
				sessionManager.release();
			}
		}
		return i+"";
	}

	public String dataToCbur(String dwbm) throws BusinessException {

		return null;
	}

	public String dataToCbured(String dwbm) throws BusinessException {

		int i=0;
		try {
			sessionManager = PersistenceManager.getInstance(source);
			JdbcSession session = sessionManager.getJdbcSession();
			String sql="update yz_datarec set flag=3 where  dr=0 and flag=1 and sjdwbm in ("+dwbm+")";
			i = session.executeUpdate(sql);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			throw new BusinessException(e);
		}
		return i+"";
	}

	public String queDataRec(String flag) throws BusinessException {

		String whereDwbm=null;;
		Connection conn=null;
		PreparedStatement pstmt=null;
		
		try {
			sessionManager = PersistenceManager.getInstance(source);
			JdbcSession session = sessionManager.getJdbcSession();
		    conn=session.getConnection();
			whereDwbm = null;
			String sql="select sjdwbm from yz_datarec where  dr=0 and flag=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, flag);
			ResultSet rs = pstmt.executeQuery();
			ArrayList objRec=new ArrayList();
			while(rs.next()){
				objRec.add(rs.getString(1));
			}
			StringBuffer sb=new StringBuffer();
			for (int i=0;i<objRec.size();i++){
				sb.append("'"+objRec.get(i)+"',");
			}
			if(sb.length()!=0){
				whereDwbm=sb.toString().substring(0, sb.toString().length()-1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new BusinessException(e);
		}finally{
			if(sessionManager!=null){
				sessionManager.release();
			}if (null != pstmt){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}if(null != conn){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return whereDwbm;
	}

	//把标志为4的单位的标志位修改为5，表示该凭证正在转换凭证的过程中
	public String xmlToVou(String dwbm) throws BusinessException {

		int i=0;
		Connection conn=null;
		PreparedStatement pstmt=null;
		
		try {
			sessionManager = PersistenceManager.getInstance(source);
			JdbcSession session = sessionManager.getJdbcSession();
			conn=session.getConnection();
			String sql="update yz_datarec set flag=5 where  dr=0 and flag=4 and sjdwbm=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dwbm);
			i=pstmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BusinessException(e);
		}finally{
			if(sessionManager!=null){
				sessionManager.release();
			}if (null != pstmt){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return i+"";
	}

	//把标志位修改为6表示该省的XML已经完全转换成凭证信息
	public String xmlToVoued(String dwbm) throws BusinessException {

		int i=0;
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			sessionManager = PersistenceManager.getInstance(source);
			JdbcSession session = sessionManager.getJdbcSession();
			conn=session.getConnection();
			String sql="update yz_datarec set flag=6 where  dr=0 and flag=5 and sjdwbm=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dwbm);
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BusinessException(e);
		}finally{
			if(sessionManager!=null){
				sessionManager.release();
			}if (null != pstmt){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return i+"";
	}

}
