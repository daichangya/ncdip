package nc.itf.dip.pub;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dip.fun.FormmulaCache;
import nc.jdbc.framework.exception.DbException;
import nc.ui.dip.cmdctl.CtlUtilVo;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.datarec.DipDatarecSpecialTab;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.uap.busibean.exception.BusiBeanException;

public interface IQueryField {
	public Map<String,String> getAccountMap() throws Exception;
	public boolean checkEnv(String[] str) throws Exception;
	public int updateSql(String sql) throws Exception;
	public CtlUtilVo getResultSet(String sql,CtlUtilVo csm) throws Exception;
	/**
	 * @desc �ϴ��ļ�
	 * @author wyd
	 * @param filePath �ļ�·�����ļ���
	 * @param ba �ļ���
	 * */
	public String upLoadFile(String filePath, byte[] ba) throws BusiBeanException ;
	public  byte[]  downLoadFile(String filePath) throws BusiBeanException ;
	/**
	 *  ��ȡ��־ͷ�����ݻ�βmap��list(˳�����,�ֶζ���)
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public List getDef_hMap(String sql,String type) throws SQLException;
	public List querySupervoByVoname(Class supervoname,String sql) throws Exception;
	public String createVoucherPkTempTable(DipDatadefinitBVO ywzd,String ywbm,String dzbm) throws Exception ;
	
	/**
	 * ��ȡ����
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public String getNumber(String sql) throws SQLException;
	
	/**
	 * ִ���κ�sql��䣬���罨����䣬��Ȩ���
	 * */
    public boolean exectEverySql(List<String> sql);
    /**
     * lyz
     * @param sql
     * @return
     */
    public RetMessage exectEverySqlByHandCommit(List<String> sql);
/**
 * ִ���κ�sql��䣬���罨����䣬��Ȩ���
 * */
    public boolean exectEverySql(String sql);
	
    public void exectListSql(List<String> sql) throws Exception;
	/**
	 * ֻ����һ���ֶε������
	 */
	public String queryfield(String sql)throws BusinessException, SQLException,DbException;
	public String queryfield(String sql,String design)throws BusinessException, SQLException,DbException;
	
	/**
	 * ����һ���ֶε����н��
	 * @param sql
	 * @return
	 * @throws BusinessException
	 * @throws SQLException
	 * @throws DbException
	 */
	public List queryfieldList(String sql) throws BusinessException,SQLException, DbException ;
	
	/**
	 * ����һ���ֶβ��ظ��Ľ��
	 * @param sql
	 * @return
	 * @throws BusinessException
	 * @throws SQLException
	 * @throws DbException
	 */
	public Set queryfieldSet(String sql) throws BusinessException,SQLException, DbException ;
	/**
	 * ������Ϣ����ʽ�������
	 * @param sql
	 * @param messformat
	 * @return
	 * @throws BusinessException
	 * @throws SQLException
	 * @throws DbException
	 */
	public List queryLMGSSql(String sql,String messformat) throws BusinessException,
	SQLException, DbException;
	
	/**
	 * ���ض��sql�Ľ����
	 */
	public List queryfieldMoreSql(List sql) throws BusinessException,SQLException,DbException;
	
	/**
	 * ����һ��sql���Ľ����
	 */
	public List queryFieldSingleSql(String sql)throws BusinessException,SQLException,DbException;
	
	public java.util.List querySuperVO(String sql) throws BusinessException;

	public void exesql(String sql)throws BusinessException, SQLException,DbException;	
	/**
	 * ����һ��sql�Ľ������List �������һ��map
	 * @param sql
	 * @return
	 * @throws BusinessException
	 * @throws SQLException
	 * @throws DbException
	 */
	public List queryListMapSingleSql(String sql)throws BusinessException,SQLException,DbException;
	
//	public nc.bs.wfengine.engine.query.WorkflowQuery getWorkFlowQuery();
	
//	public int ImpExcel(String filePath,String pk_corp,String year,String filename)throws BusinessException;
	
	public void onServerImpExcel(String yyyy,String pk_corp);
	
	public String getNchomeDir();
	
	public int writeTreeNode(String year,String filename,String pk_parent);
	
	public int exePluginalert(String pluginclassname);
	
//	public String upLoadFile(String url, String str) throws BusinessException;	
	
	/**
	 * 
	 * ���ݼӹ�
	 * */
	public String dataProc(String hpk);
	public String dataproc(String sql,String tablename,String procetype) throws BusinessException, SQLException, DbException;

	public List<SuperVO> queryVOBySql(String sql, SuperVO vo);

	/**
	 * �������ɷ�ʽ
	 * 
	 */
	public String getOID();
	public String getOID(String pk_corp);
	public String[] getOIDs(String pk_corp,int count);
	/**
	 * ������������vo
	 * */
	public SuperVO querySupervoByPk(Class supervoname,String pk) throws Exception;
	
	/**���ݽ��յĸ�ʽ���壬�ŵ�һ���������*/
	public boolean saveDataFormat(String recepk,Map<String,DataformatdefBVO[]> desmap ,
	String[] pks,
	String[] lx,
	DataformatdefHVO[] hvos,List<DipDatarecSpecialTab> speciallist);
	
	public FormmulaCache getFormmulaCache() throws Exception;
	public void clearFormmulaCache() throws Exception;
	public void putFormmulaCache(String key,String value) throws Exception;
	public String getFormmulaCacheValue(String key) throws Exception ;
	public int getCountFormmulaCacheValue() throws Exception;
	
	public RetMessage testDSConnect(String pk) throws Exception;
	public long getDate()throws Exception;
	public RetMessage upLoadFile(String path, byte[] ba, String tofilepath);
	/**
	 * 
	 * @param directorie
	 * @return
	 */
	public RetMessage getFileNamesList(String directorie);
	
	public RetMessage getFileNamesList(String directorie,String prex);
	/**
	 * �ӷ������ϣ����ļ������ļ����ϴ���ftp�������ϡ�
	 * @param path �ļ���
	 * @param fileName �ļ����ƣ�����ļ�����Ϊnull�����ϴ������ļ���
	 * @param pk_ftpsourceregister��ftpע��pk
	 * @param ftpFile��ftpFile���ϴ���ftp���Ǹ�·����
	 * @return
	 */
	public RetMessage upFileFromSevriceToFtp(String path,String fileName,String pk_ftpsourceregister,String ftpFile,String hpk)throws Exception;
	/**
	 * �ѷ������ϵ��ļ������ļ������ص��������ϡ�
	 * @param ftppath
	 * @param ftpfileName
	 * @param pk_ftpsourceregister
	 * @param serviceFile ���ص�������serviceFile·�����档
	 * @return
	 */
	public RetMessage downFileFromFtpToService(String ftppath,String ftpfileName,String pk_ftpsourceregister,String serviceFile,String hpk)throws Exception;
	/**
	 * ����ftpע��������Ƿ���á���������÷��ش�����Ϣ
	 * @param pk_ftpsourceregister
	 * @return
	 */
	public RetMessage checkFtpServiceRegisterIsTure(String pk_ftpsourceregister)throws Exception;
	
	public RetMessage exeCall(String sql)throws Exception;
	
	public void exesqlCommit(String sql)throws Exception;
	/**
	 * �õ����е����ӹ�˾���¼���˾����������1001 ���ӹ�˾��1002,1003,1004,1005�� ��1002���ӹ�˾ ��1010,1011����������˾��û���ӹ�˾���ͷ��أ�1002����
     * ����1001 ���ӹ�˾��1002,1003�� ��1002���ӹ�˾ ��1010,1011����1010���ӹ�˾��1101,1102��������˾��û���ӹ�˾���ͷ��أ�1002��1010����
	 * @param pk_corp
	 * @return Map key��pk_corp,value��pk_corp
	 */
	public Map<String,String> getAllChild(String pk_corp,Map<String, String> corpMap) throws Exception;
	
	/**
	 * lyz ����У�鷽�������У�鷽������ȫ�棬��ʱֻ��������ͨ��У�飬�����Ļ�û�����ӣ�
	 * �����Ҫ���ӿ������ӽ�ȥ
	 * @param pk_bus
	 * @return
	 * @throws Exception
	 */
	public RetMessage docheck(String pk_bus)throws Exception;
	
	public void insertVOs(SuperVO[] vos) throws Exception;
}
