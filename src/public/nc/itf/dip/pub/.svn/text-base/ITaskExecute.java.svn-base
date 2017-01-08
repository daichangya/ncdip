package nc.itf.dip.pub;

import java.util.List;
import java.util.Map;

import nc.util.dip.sj.RetMessage;
import nc.vo.dip.message.MsrVO;

public interface ITaskExecute {
	/**
	 * @desc 立即执行--根据预警主键
	 * @author wyd
	 * @param 预警的主表主键
	 * */
	public RetMessage doTaskAtOnce(String pk_warning);
	/**
	 * @desc 前台同步
	 * @param hpk
	 * @param filename
	 * */
	public RetMessage doTbTaskFrom(String hpk,String filename);
	
	
	/**
	 * @desc 数据转换
	 * @author wyd
	 * @param hpk 数据转换的主表主键
	 * */
	public RetMessage doDataChangeTask(String hpk);
	/**
	 * @desc 数据同步
	 * @author wyd
	 * @param hpk 数据转换的主表主键
	 * */
	public RetMessage doTBTask(String hpk);
	/**
	 * @desc 数据加工
	 * @author wyd
	 * @param hpk 数据转换的主表主键
	 * */
	public RetMessage doJGTask(String hpk);
	/**
	 * @desc 数据发送
	 * @author wyd
	 * @param hpk 数据转换的主表主键
	 * */
	public RetMessage doFSTask(String hpk);
	/**
	 * @desc 自动对照数据
	 * @author wyd
	 * @param hpk 数据对照定义的主表主键
	 * */
	public RetMessage doAutoContData(String hpk);
	/**
	 * @desc 数据流程
	 * @author wyd
	 * @param hpk 数据转换的主表主键
	 * */
	public RetMessage doLCTask(String hpk);
	/**
	 * @desc ESB文件发送
	 * @author wyd
	 * @param hpk ESB文件发送的主表主键
	 * */
	public RetMessage doESBSendTask(String hpk);
	/**
	 * @desc 通用数据转换
	 * @author wyd
	 * @param hpk 通用数据转换的主表主键
	 * */
	public RetMessage doTYZHTask(String hpk);
	/**
	 * @desc 通用XML转换
	 * @author wyd
	 * @param hpk 通用xml转换的主表主键
	 * */
	public RetMessage doTYXMLTask(String hpk);
	/**
	 * @desc 创建文件路径，如果path不存在，创建路径，如果存在，就算了
	 * @author wyd
	 * @param path 文件路径
	 * */
	public boolean createFilePath(String path);
	
	/**
	 * @desc 判断是否符合预警条件，如果符合，返回true,否则返回false
	 * @param String pk_warn 预警的主键 
	 * */
	public boolean isFitWarnCondition(String pk_warn);
	
	
	public String dosend(MsrVO vo);
	
	public String dosendTopic(MsrVO vo);
	public String dorec(MsrVO vo );
	/**
	 * @desc 给回执那用的
	 * */
	public String dosend(MsrVO vo,String msg);
/**
 * liyunzhe add 参数 control 和pk_bus,如过传输控制为true，count，pagecount，就在日志里写出开始和结束标志，
 * @param pk_msr  
 * @param msg
 * @param control 传输控制
 * @param pk_bus  业务主键
 * @param count  次数,如果数据量比较大的时候会分页，第一次是0，在发送时候写开始日志时候要用到。
 * @param pagecount  总页数,如果数据量比较大的时候会分页，如果数据量少不做分页时候，pagecount=1；
 * @param delayedmap  延时时间 b,d,e；
 * @return
 */
	public RetMessage dosendListmsg(String pk_msr,List<String> msg,boolean control,String pk_bus,int count,int pagecount,Map<String,Integer> delayedmap);
	/**
	 * 
	 * @desc 状态管理
	 * 
	 */
	public RetMessage doStateChange(String pk);

}
