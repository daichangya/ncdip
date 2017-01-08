package nc.itf.dip.pub;

import nc.util.dip.sj.RetMessage;
import nc.vo.dip.warningset.DipWarningsetVO;

public interface ITaskManage {
	//预警服务状态，TASKSTATE_INIT=0 初始状态,TASKSTATE_START=1运行状态，TASKSTATE_STROP=2 运行结束
	public static final int TASKSTATE_INIT=0; 
	public static final int TASKSTATE_START=1; 
	public static final int TASKSTATE_STROP=2; 
	
	public static final String ISNOTWARING_T="禁用";
	public static final String ISNOTWARING_Y="可用";
	/**
	 * @author 张进双
	 * @param threadName 
	 * @desc 1、从预警管理里取可用的，下次运行时间<当前时间并且最早的那个预警条目
	 * 2、把取到的任务的状态改为运行中
	 * @return 返回预警设置表头
	 * */
	public DipWarningsetVO getTaskItem(String threadName);
	
	/**
	 * @author 张进双
	 * @desc 更新预警服务状态
	 * 两个状态，预警任务运行，预警任务结束。如果是预警任务结束，那么要找到相应的预警时间设置，更新下次预警时间
	 * 注意：更新开始时间，结束时间
	 * @param dwvo 更新后的预警主表vo
	 * @param taskState 状态描述为：TASKSTATE_INIT=0 初始状态,TASKSTATE_START=1运行状态，TASKSTATE_STROP=2 运行结束
	 * @retrun boolean  返回更新状态是否成功
	 * */
	public boolean updatTaskstate(DipWarningsetVO dwvo,int taskState);
	/**
	 * @author 张进双
	 * @desc 更新预警服务状态
	 * 两个状态，预警任务运行，预警任务结束。如果是预警任务结束，那么要找到相应的预警时间设置，更新下次预警时间
	 * 注意：更新开始时间，结束时间
	 * @param pk_dipwarningset 更新后的预警主表vo主键
	 * @param taskState 状态描述为：TASKSTATE_INIT=0 初始状态,TASKSTATE_START=1运行状态，TASKSTATE_STROP=2 运行结束
	 * @retrun boolean  返回更新状态是否成功
	 * */
//	public boolean updatTaskstate(String pk_dipwarningset,int taskState);
	
	/**
	 * @author 张进双
	 * @desc 更改表状态监控
	 * @return boolean 返回更新表状态监控是否成功
	 * */
	public boolean updatMonitorTable(nc.vo.dip.tabstatus.MyBillVO vo);
	
	/**
	 * @author 张进双
	 * @desc 1、保存或更改预警条目设置，2、根据预警条目设置，保存到预警服务管理，并且根据预警时间的设置，更新下次预警时间
	 * @param MyBillVO 预警条目设置的聚合vo
	 * @return String 聚合VO主表主键
	 * */
	public RetMessage saveOrUpdateWarnSet(nc.vo.dip.warningset.MyBillVO mybillvo);
	/**
	 * @author 张进双
	 * @desc 根据预警时间设置的预警时间主键，得到任务下次运行时间
	 * @param String pk_dip_warningsetdaytime 预警时间设置的时间主键
	 * @return String "yyyy-mm-dd hh:mm:ss"格式的时间字符串 
	 * */
	public String getNextDate(String pk_dip_warningsetdaytime)throws Exception;
	/**
	 * @author wyd
	 * @desc 预警的禁用，启用
	 * @param pk_warnset 要启用或禁用的预警主键
	 * @param boolean true：启用;false 禁用
	 * */
	public RetMessage startOrStopWarn(String pk_warnset,boolean isSoS);
	
}
