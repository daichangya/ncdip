package nc.itf.dip.pub;

import java.util.List;

import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.message.MsrVO;

public interface ILogAndTabMonitorSys {
	public final int MSTATE_JY=0;
	public final int MSTATE_ZT=1;
	/**
	 * @desc 往表状态监控里写监控状态 1、处理完成；2、处理失败
	 * @author wyd
	 * @param pk_bus 对应的业务表的主表主键
	 * @param yw 业务类别
	 * @param s 成功数量
	 * @param f 失败数量
	 * @param status 要写入的状态 IContrastUtil.TABMONSTA_ON 或者IContrastUtil.TABMONSTA_FIN
	 */
	public boolean writeToTabMonitor_RequiresNew(String pk_bus,String yw,String status,Integer s,Integer f);
	public boolean writeToTabMonitor_RequiresNew(String pk_bus,String yw,int su,int fa,String classname);
	/**
	 * @desc 在状态管理节点，用到的对表状态的修改。
	 * @param pk_bus
	 * @param type
	 * @param status
	 * @param s
	 * @param f
	 * @param tablestate
	 * @param tablekey
	 * @return
	 */
	public boolean writeToTabMonitor_RequiresNew_StateManage(String pk_bus,String type,String status,Integer s, Integer f,String tablestate,String tablekey);
	/**
	 * @desc 往数据处理日志里写日志 1、处理完成；2、处理失败
	 * @param pk_bus 业务主表主键
	 * @param yw 业务类型  IContrastUtil.YWLX_DY...
	 * @param des 工作内容
	 * */
	public boolean writToDataLog_RequiresNew(String pk_bus,String yw,String des);
	public boolean writToDataLog_RequiresNew(String pk_bus,String yw,List<String> errlist);
	/**
	 * @desc 往数据处理日志里写日志:1、处理完成；2、处理失败
	 * @param pk_warn 预警的主键
	 * @desc 工作内容
	 * */
	public boolean writToDataLog_RequiresNew(String pk_warn,String des);
	public boolean writToDataLog_RequiresNew(DateprocessVO vo);
	/**
	 * @desc 往模糊存储表里插入消息
	 * */
	public void writToMhccb_RequiresNew(String msg,MsrVO msrvo);
	
}
