package nc.itf.dip.pub;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.exception.DbException;
import nc.vo.dip.managerserver.DipManagerserverVO;
import nc.vo.pub.BusinessException;
/**
 * 服务管理的管理接口
 * */
public interface IServerBufferManage {
//	public static final Map<String,DipManagerserverVO> map=null;
	/**
	 * @author wyd
	 * @desc 更新服务管理，根据线程id跟新服务管理的状态
	 * @param serverThreadName 服务器以及线程的唯一id
	 * @param dipMVO 要更新的服务管理vo
	 * @return boolean 返回是否更新服务管理成功
	 * */
	public boolean setServerBufferManage(String serverThreadName,DipManagerserverVO dipMVO);
	/**
	 * @author wyd
	 * @desc 返回缓存中，所有服务管理状态
	 * @param 
	 * @return DipManagerserverVO[] 返回缓存中，所有服务管理VO
	 * */
	public DipManagerserverVO[] getServerBufferManage();
	/**
	 * @author wyd
	 * @desc 根据线程名称，返回缓存中的服务VO
	 * @param threadName 当前线程名称
	 * */
	public DipManagerserverVO getServerVOByThreadname(String threadName);
	/**
	 * @author wyd 
	 * @desc 得到预警类型的所有服务管理
	 * */
	public List<DipManagerserverVO> getWarnBufVO();
	/**
	 * @author wyd
	 * @desc 得到可以运行消息服务的任务
	 * @param threadName 取任务的线程
	 * */
	public DipManagerserverVO getRunMesVO(String threadName);
	/**
	 * @desc 停止或启动消息服务
	 * @param String pk_server 消息服务主键
	 * @param boolean isStart 启动是true;停用是false;
	 * */
	public boolean startOrStopMsgServer(String pk_server,boolean isStart);
	
	/**
	 * @desc 把服务管理的消息服务的状态改为没有线程运行
	 * @param DipManagerserverVO mvo 服务管理VO
	 * */
	public boolean updateMesVO(DipManagerserverVO mvo);
	/**
	 * @desc 判断当前的服务是否还是启动着的
	 * */
	public boolean isStart(DipManagerserverVO mvo);
	
//	public Map getBJmap();
	public List<DipManagerserverVO> getOrSetUpdateBufList(String servername,boolean isget,DipManagerserverVO dmsvo);
	/**
	 * @desc 同步主服务器上当前的servername下都有哪些可运行的消息服务设置
	 * */
	public List<DipManagerserverVO> syhDipMserState(String servername);
	
}
