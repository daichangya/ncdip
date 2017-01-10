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
	 * @desc 上传文件
	 * @author wyd
	 * @param filePath 文件路径加文件名
	 * @param ba 文件流
	 * */
	public String upLoadFile(String filePath, byte[] ba) throws BusiBeanException ;
	public  byte[]  downLoadFile(String filePath) throws BusiBeanException ;
	/**
	 *  获取标志头、数据或尾map的list(顺序对照,字段对照)
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public List getDef_hMap(String sql,String type) throws SQLException;
	public List querySupervoByVoname(Class supervoname,String sql) throws Exception;
	public String createVoucherPkTempTable(DipDatadefinitBVO ywzd,String ywbm,String dzbm) throws Exception ;
	
	/**
	 * 获取条数
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public String getNumber(String sql) throws SQLException;
	
	/**
	 * 执行任何sql语句，比如建表语句，授权语句
	 * */
    public boolean exectEverySql(List<String> sql);
    /**
     * lyz
     * @param sql
     * @return
     */
    public RetMessage exectEverySqlByHandCommit(List<String> sql);
/**
 * 执行任何sql语句，比如建表语句，授权语句
 * */
    public boolean exectEverySql(String sql);
	
    public void exectListSql(List<String> sql) throws Exception;
	/**
	 * 只返回一个字段单个结果
	 */
	public String queryfield(String sql)throws BusinessException, SQLException,DbException;
	public String queryfield(String sql,String design)throws BusinessException, SQLException,DbException;
	
	/**
	 * 返回一个字段的所有结果
	 * @param sql
	 * @return
	 * @throws BusinessException
	 * @throws SQLException
	 * @throws DbException
	 */
	public List queryfieldList(String sql) throws BusinessException,SQLException, DbException ;
	
	/**
	 * 返回一个字段不重复的结果
	 * @param sql
	 * @return
	 * @throws BusinessException
	 * @throws SQLException
	 * @throws DbException
	 */
	public Set queryfieldSet(String sql) throws BusinessException,SQLException, DbException ;
	/**
	 * 返回消息流格式相关数据
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
	 * 返回多个sql的结果集
	 */
	public List queryfieldMoreSql(List sql) throws BusinessException,SQLException,DbException;
	
	/**
	 * 返回一个sql语句的结果集
	 */
	public List queryFieldSingleSql(String sql)throws BusinessException,SQLException,DbException;
	
	public java.util.List querySuperVO(String sql) throws BusinessException;

	public void exesql(String sql)throws BusinessException, SQLException,DbException;	
	/**
	 * 返回一个sql的结果集，List 里面包含一个map
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
	 * 数据加工
	 * */
	public String dataProc(String hpk);
	public String dataproc(String sql,String tablename,String procetype) throws BusinessException, SQLException, DbException;

	public List<SuperVO> queryVOBySql(String sql, SuperVO vo);

	/**
	 * 主键生成方式
	 * 
	 */
	public String getOID();
	public String getOID(String pk_corp);
	public String[] getOIDs(String pk_corp,int count);
	/**
	 * 根据主键查找vo
	 * */
	public SuperVO querySupervoByPk(Class supervoname,String pk) throws Exception;
	
	/**数据接收的格式定义，放到一个事务里边*/
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
	 * 从服务器上，把文件或者文件夹上传到ftp服务器上。
	 * @param path 文件夹
	 * @param fileName 文件名称，如果文件名称为null，就上传整个文件夹
	 * @param pk_ftpsourceregister，ftp注册pk
	 * @param ftpFile，ftpFile是上传到ftp的那个路径下
	 * @return
	 */
	public RetMessage upFileFromSevriceToFtp(String path,String fileName,String pk_ftpsourceregister,String ftpFile,String hpk)throws Exception;
	/**
	 * 把服务器上的文件或者文件夹下载到服务器上。
	 * @param ftppath
	 * @param ftpfileName
	 * @param pk_ftpsourceregister
	 * @param serviceFile 下载到服务器serviceFile路径下面。
	 * @return
	 */
	public RetMessage downFileFromFtpToService(String ftppath,String ftpfileName,String pk_ftpsourceregister,String serviceFile,String hpk)throws Exception;
	/**
	 * 测试ftp注册服务器是否可用。如果不可用返回错误消息
	 * @param pk_ftpsourceregister
	 * @return
	 */
	public RetMessage checkFtpServiceRegisterIsTure(String pk_ftpsourceregister)throws Exception;
	
	public RetMessage exeCall(String sql)throws Exception;
	
	public void exesqlCommit(String sql)throws Exception;
	/**
	 * 得到所有的有子公司的下级公司主键，例如1001 有子公司（1002,1003,1004,1005） ，1002有子公司 （1010,1011），其他公司都没有子公司，就返回（1002）。
     * 例如1001 有子公司（1002,1003） ，1002有子公司 （1010,1011），1010有子公司（1101,1102）其他公司都没有子公司，就返回（1002，1010）。
	 * @param pk_corp
	 * @return Map key是pk_corp,value是pk_corp
	 */
	public Map<String,String> getAllChild(String pk_corp,Map<String, String> corpMap) throws Exception;
	
	/**
	 * lyz 增加校验方法，这个校验方法并不全面，暂时只是增加了通用校验，其他的还没有增加，
	 * 如果需要增加可以增加进去
	 * @param pk_bus
	 * @return
	 * @throws Exception
	 */
	public RetMessage docheck(String pk_bus)throws Exception;
	
	public void insertVOs(SuperVO[] vos) throws Exception;
}
