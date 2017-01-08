package nc.itf.dip.optByPluginSrv;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.dip.txt.TxtDataVO;
import nc.jdbc.framework.exception.DbException;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.message.MsrVO;
import nc.vo.pub.BusinessException;

public interface IOptByPlg {
	/**
	 * �õ���Ϣ����ȥ�Ҷ�Ӧ�ĸ�ʽ�����н���
	 *@param hpk ����ͬ��pk
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws DAOException 
	 * @throws MalformedURLException 
	 * @throws DbException 
	 * @throws SQLException 
	 * @throws BusinessException 
	 * @throws Exception 
	 */
	public RetMessage msgToStyle(String hpk,List<String> errlist) throws MalformedURLException, DAOException, IOException, ClassNotFoundException, BusinessException, SQLException, DbException, Exception;
	/**
	 * ����Ϣ��ʽȥ������Ӧ��ʽ����Ϣ
	 *@param ��Ϣ����
	 * @throws DbException 
	 * @throws SQLException 
	 * @throws BusinessException 
	 */
	public void styleToMsg(String sourmsg,String msg,Map gsmap,MsrVO mvo,String[] strerror) throws BusinessException, SQLException, DbException;

	public  TxtDataVO radeFile(String filepath,int bnum) throws Exception;
}
