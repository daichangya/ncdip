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
 *�˽ӿڷ���ʵ��  �ڹ�ͨ�����˽�����־�Ժ󣬸��ݽ���λ�����Ժ�Ĳ���
 * 0��δ���յ�������
 * 1�����ݽ��ճɹ�
 */
public class RecDataDMO implements IRecDataService{

	//�鿴  �ر���������
	PersistenceManager sessionManager = null;
	JdbcSession session=null;
	
	//�������ļ��л�ȡ�����ļ��е���Ϣ����Ϊ����Դ��Ϣ
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

	//�ѱ�־Ϊ4�ĵ�λ�ı�־λ�޸�Ϊ5����ʾ��ƾ֤����ת��ƾ֤�Ĺ�����
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

	//�ѱ�־λ�޸�Ϊ6��ʾ��ʡ��XML�Ѿ���ȫת����ƾ֤��Ϣ
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
