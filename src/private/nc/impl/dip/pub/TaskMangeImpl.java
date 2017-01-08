package nc.impl.dip.pub;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PaConstant;
import nc.itf.dip.pub.ITaskManage;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.processflow.DipProcessflowBVO;
import nc.vo.dip.tabstatus.DipTabstatusVO;
import nc.vo.dip.tabstatus.TabstatusBVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetDayTimeVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.dip.warningset.MyBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pa.TimingSettingVO;
import nc.vo.uap.scheduler.TimeConfigVO;
/**
 * @desc 预警管理的服务类
 * @author wyd
 * 
 * */
public class TaskMangeImpl implements ITaskManage {

	private BaseDAO baseDao;	
	public static byte lock[] = new byte[0]; 
	private BaseDAO getBaseDao(){
    	if (baseDao==null){
    		baseDao=new BaseDAO(IContrastUtil.DESIGN_CON);
    	}
    	return baseDao;
    }
	private DipWarningsetVO oldhvo=null;
	/**
	 * @desc 1、从服务管理里取可用的，下次运行时间<当前时间并且最早的那个预警条目
	 * 2、把取到的任务的状态改为运行中
	 * @return 返回预警设置表头
	 * @author 张进双
	 * @since nc502 2011-5-15
	 * */
	public synchronized DipWarningsetVO getTaskItem(String threadName) {
		DipWarningsetVO warningset = null;

				// TODO Auto-generated method stub
				List listWrnings = null;
				//获取当前时间
				UFDateTime nowdatetime = new UFDateTime(new Date());
//				String sql = "select * from dip_warningset where tasktype='"+SJUtil.getYWnameByLX(IContrastUtil.YWLX_LC)+"' and isnotwarning = '可用' and isnottiming ='定时' and (tabstatus is null or tabstatus = '停止') and nexttime is not null and nexttime not like '1970-%' and nexttime < '"+nowdatetime.toString()+"' order by nexttime;";
				String sql = "select * from dip_warningset where tasktype='"+"0001AA1000000001FVAD"+"' and isnotwarning = '可用' and isnottiming ='定时' and (tabstatus is null or tabstatus = '停止') and nexttime is not null and nexttime not like '1970-%' and nexttime < '"+nowdatetime.toString()+"' order by nexttime;";
				try {
					listWrnings = (List) getBaseDao().executeQuery(sql,new BeanListProcessor(DipWarningsetVO.class));
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(listWrnings!=null&&listWrnings.size()>0){
					warningset=(DipWarningsetVO) listWrnings.get(0);
					warningset.setRunservice(threadName.split("-")[0]);
					boolean flag=updatTaskstate((DipWarningsetVO) listWrnings.get(0), TASKSTATE_START);
					if(!flag){
						return null;
					}
				}
		return warningset;
	}
	/**
	 * @desc 1、保存或更改预警条目设置，2、根据预警条目设置，保存到预警服务管理，并且根据预警时间的设置，更新下次预警时间
	 * 3、如果是数据流程保存，那么如果表体的流程里在前边的流程中包含，那么禁掉之前的流程
	 * 在预警设置界面，点保存按钮的时候会调用此方法
	 * @param MyBillVO 预警条目设置的聚合vo
	 * @return String 聚合VO主表主键
	 * @author 张进双
	 * @since nc502 2011-5-15
	 * */
	public RetMessage saveOrUpdateWarnSet(MyBillVO mybillvo) {
		oldhvo=null;
		RetMessage msg=new RetMessage();
		msg.setIssucc(false);
		if(mybillvo==null){
			msg.setMessage("没有要保存的数据！");
			return msg;
		}
//			String nextDate = null;
			String pk_warningset = null;
			IUifService iServer = (IUifService)NCLocator.getInstance().lookup(IUifService.class.getName());
			try {
				
				DipWarningsetVO hvo=((DipWarningsetVO)mybillvo.getParentVO());
				
				pk_warningset = hvo.getPrimaryKey();
				//
				DipWarningsetVO[] oldvo=(DipWarningsetVO[]) iServer.queryByCondition(DipWarningsetVO.class, " pk_warningset='"+pk_warningset+"'"+"  and  nvl(dr,0)=0 ");
				if(oldvo!=null&&oldvo.length==1){
					oldhvo=oldvo[0];
				}
				hvo.setThistime(null);
				hvo.setTabstatus(null);
				hvo.setIsnotwarning("禁用");
				//取得主表主键，如果能取到就是更新，否则就是保存，如果没有主键的话，根据业务类型和主表主键，定位，如果能定位到，就走更新，如果不能定位到，那就保存
				if(pk_warningset == null || "".equals(pk_warningset)){
					DipWarningsetVO[] baswvos=(DipWarningsetVO[]) iServer.queryByCondition(DipWarningsetVO.class, "nvl(dr,0)=0 and tasktype='"+hvo.getTasktype()+"' and pk_bustab='"+hvo.getPk_bustab()+"'");
					if(baswvos==null||baswvos.length==0){
						hvo.setDr(0);
						
						pk_warningset = iServer.insert(hvo);
						DipWarningsetBVO[] wbvo = (DipWarningsetBVO[])mybillvo.getChildrenVO();
						if(wbvo!=null){
							for(int i=0;i<wbvo.length;i++){
								wbvo[i].setPk_warningset(pk_warningset);
								wbvo[i].setDr(0);
								iServer.insert(wbvo[i]);
							}
						}
//						updatTaskstate(hvo, TASKSTATE_INIT);
					}else{
						msg.setMessage("改业务单据的预警条目设置已经被编辑过，请查询后重新修改！");
						return msg;
					}
				}else{
				//	 oldhvo=(DipWarningsetVO)getBaseDao().retrieveByClause(DipWarningsetVO.class, " pk_warningset='"+pk_warningset+"'"+"  and  nvl(dr,0)=0 ");
					iServer.update(hvo);
					iServer.deleteByWhereClause(DipWarningsetBVO.class, "pk_warningset='"+pk_warningset+"'");
					DipWarningsetBVO[] wbvo = (DipWarningsetBVO[])mybillvo.getChildrenVO();
					if(wbvo!=null){
						for(int i=0;i<wbvo.length;i++){
							wbvo[i].setPk_warningset(pk_warningset);
							wbvo[i].setDr(0);
							iServer.insert(wbvo[i]);
						}
					}
//					updatTaskstate(hvo, TASKSTATE_INIT);
				}
				/*//得到禁用启用状态
				String isProp=hvo.getIsnotwarning();
				if(isProp!=null&&isProp.equals("可用")){
					hvo.setPrimaryKey(pk_warningset);
					RetMessage isp=startOrStopWarn(hvo, true);
					if(!isp.getIssucc()){
						msg.setMessage("保存成功，但是修改启用状态失败！");
						return msg;
					}
				}*/
			} catch (BusinessException e) {
				e.printStackTrace();
			}  
			msg.setIssucc(true);
			msg.setMessage(pk_warningset);
			oldhvo=null;
			return msg;
	}
	/**
	 * @desc 更改表状态监控
	 * @return boolean 返回更新表状态监控是否成功
	 * @author 张进双
	 * @since nc502 2011-5-15
	 * */
	public boolean updatMonitorTable(nc.vo.dip.tabstatus.MyBillVO vo) {
		// TODO Auto-generated method stub
		IUifService iServer = (IUifService)NCLocator.getInstance().lookup(IUifService.class.getName());
		try {
			iServer.update((DipTabstatusVO)vo.getParentVO());
			TabstatusBVO[] wbvo = (TabstatusBVO[])vo.getChildrenVO();
			for(int i=0;i<wbvo.length;i++){
				iServer.insert(wbvo[i]);
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * @desc 更新预警服务状态 任务运行，运行停止,两种预警任务类型，定时任务和即时任务，任务不一样，更新状态的方法不一样
	 * 两个状态，预警任务运行，预警任务结束。如果是预警任务结束，那么要找到相应的预警时间设置，更新下次预警时间
	 * 注意：更新开始时间，结束时间
	 * @param dwvo 更新后的预警主表vo
	 * @param taskState 状态描述为：TASKSTATE_INIT=0 初始状态,TASKSTATE_START=1运行状态，TASKSTATE_STROP=2 运行结束
	 * @retrun boolean  返回更新状态是否成功
	 * @author 张进双
	 * @since nc502 2011-5-15
	 * */
	public synchronized boolean updatTaskstate(DipWarningsetVO dwvo, int taskState) {
	/*	if(dwvo==null||dwvo.getPrimaryKey()==null){
			return false;
		}
		return updatTaskstate(dwvo.getPrimaryKey(), taskState);
	}
	*//**
	 * @desc 更新预警服务状态 任务运行，运行停止,两种预警任务类型，定时任务和即时任务，任务不一样，更新状态的方法不一样
	 * 两个状态，预警任务运行，预警任务结束。如果是预警任务结束，那么要找到相应的预警时间设置，更新下次预警时间
	 * 注意：更新开始时间，结束时间
	 * @param dwvo 更新后的预警主表vo
	 * @param taskState 状态描述为：TASKSTATE_INIT=0 初始状态,TASKSTATE_START=1运行状态，TASKSTATE_STROP=2 运行结束
	 * @retrun boolean  返回更新状态是否成功
	 * @author 张进双
	 * @since nc502 2011-5-15
	 * *//*
	public boolean updatTaskstate(String pk_dipwarnset, int taskState) {*/

//		synchronized (lock){ 
			try{
//	        	DipWarningsetVO dwvo=(DipWarningsetVO) getBaseDao().retrieveByPK(DipWarningsetVO.class, pk_dipwarnset);
				//当前时间
				String nowdate = String.valueOf(new UFDateTime(new Date()));
				String nextDate = null;
				//两种情况，定时任务和即时任务
				if(dwvo.getIsnottiming()!=null&&dwvo.getIsnottiming().equals("定时")){
				
					try {
						nextDate = getNextDate(dwvo.getWartime());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return false;
					}
					//TASKSTATE_INIT=0 初始状态
					if(taskState == TASKSTATE_INIT){
						if("运行".equals(dwvo.getTabstatus())){
							return false;
						}
						String sql = "update dip_warningset set tabstatus = '', runservice=null,thistime = '',nexttime = '"+nextDate+"' where pk_warningset = '"+dwvo.getPk_warningset()+"'";
							getBaseDao().executeUpdate(sql);
					}
			        //TASKSTATE_INIT=1 运行状态
					if(taskState == TASKSTATE_START){
						String sql = "update dip_warningset set tabstatus = '运行',runservice='"+dwvo.getRunservice()+"', thistime = '"+nowdate+"',nexttime = '' where pk_warningset = '"+dwvo.getPk_warningset()+"'";
							getBaseDao().executeUpdate(sql);
					}
			       //TASKSTATE_INIT=2 结束状态
					if(taskState == TASKSTATE_STROP){
						String sql = "update dip_warningset set tabstatus = '停止',runservice=null, thistime = '',nexttime = '"+nextDate+"' where pk_warningset = '"+dwvo.getPk_warningset()+"'";
						getBaseDao().executeUpdate(sql);
					}
				}else if(dwvo.getIsnottiming()!=null&&dwvo.getIsnottiming().equals("即时")){
//					TASKSTATE_INIT=0 初始状态
					if(taskState == TASKSTATE_INIT){
						if("运行".equals(dwvo.getTabstatus())){
							return false;
						}
						String sql = "update dip_warningset set tabstatus = '',runservice=null, thistime = '',nexttime = '' where pk_warningset = '"+dwvo.getPk_warningset()+"'";
							getBaseDao().executeUpdate(sql);
					}
			        //TASKSTATE_INIT=1 运行状态
					if(taskState == TASKSTATE_START){
						String sql = "update dip_warningset set tabstatus = '运行',runservice='"+dwvo.getRunservice()+"', thistime = '"+nowdate+"',nexttime = '' where pk_warningset = '"+dwvo.getPk_warningset()+"'";
							getBaseDao().executeUpdate(sql);
					}
			       //TASKSTATE_INIT=2 结束状态
					if(taskState == TASKSTATE_STROP){
						String sql = "update dip_warningset set tabstatus = '停止',runservice=null, thistime = '',nexttime = '' where pk_warningset = '"+dwvo.getPk_warningset()+"'";
						getBaseDao().executeUpdate(sql);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
//		}
		return true;
	}
	/**
	 * @desc 预警的禁用，启用
	 * 如果是已经启用的就不能在启用，如果是已经禁用的就不能再禁用
	 * 如果是其他状态到启用，那么判断相应的预警在流程里是否有，如果流程里包含，那么就不能在启用
	 * @author wyd
	 * @param pk_warnset 要启用或禁用的预警主键
	 * @param boolean true：启用;false 禁用
	 * */
	public RetMessage startOrStopWarn(String pk_warnset,boolean isSoS){
		RetMessage msg=new RetMessage();
		msg.setIssucc(false);
//		String tag="";//目标
//		if(isSoS){
//			tag="可用";
//		}else{
//			tag="禁用";
//		}
		boolean isupdate=true;//true 表示更新为可用，false更新为禁用
			try{
				/*if(oldhvo!=null){
					wvo=oldhvo;
				}else{
					wvo=(DipWarningsetVO) getBaseDao().retrieveByPK(DipWarningsetVO.class, pk_warnset);
				}*/
	        	//DipWarningsetVO 
	        	DipWarningsetVO wvo=(DipWarningsetVO)getBaseDao().retrieveByPK(DipWarningsetVO.class, pk_warnset);
	        	
	        	if(wvo==null){
	        		msg.setMessage("不存在此条记录！");
	        	}else{
	        		String isProp=wvo.getIsnotwarning();
	        		//判断是否等于可用，这里是可用
	        		if(isProp!=null&&isProp.equals("可用")){
	        			if(isSoS){
	        				msg.setIssucc(true);
	        				msg.setMessage("已经是可用状态！");
	        			}else{
	        				if(wvo.getTabstatus()!=null&&wvo.getTabstatus().equals("运行")){
	        					msg.setMessage("该条目预警正在运行中！");
	        				}
	        				else{
//	        					String sql = "update dip_warningset set isnotwarning = '禁用' where pk_warningset='"+pk_warnset+"'";
//	        					getBaseDao().executeUpdate(sql);
//	        					msg.setIssucc(true);
	        					isupdate=false;
	        				}
	        			}
	        		}else{//本身是不可用
	        			if(!isSoS){
	        				msg.setIssucc(true);
	        				msg.setMessage("已经是禁用状态！");
	        				isupdate=false;
	        			}else{
	        				if(wvo.getTabstatus()!=null&&wvo.getTabstatus().equals("运行")){
	        					msg.setMessage("该条目预警正在运行中！");
	        				}
	        					else{
//	        					String sql = "update dip_warningset set isnotwarning = '禁用' where pk_warningset='"+pk_warnset+"'";
//	        					getBaseDao().executeUpdate(sql);
//	        					msg.setIssucc(true);
	        						isupdate=true;
	        				}
	        			}
	        		}
	        		

//					wyd 把流程包含的预警其他的预警禁用掉
					String warntype=wvo.getTasktype();
					//pk_bus 业务表的主表主键
					String pk_bus=wvo.getPk_bustab();
					if(warntype!=null&&warntype.length()>0){
						
						//desc wyd 如果是数据流程的任务类型，那就拿到表体，去查所有，禁掉其它地方配置的
						if(warntype.equals("0001AA1000000001FVAD")){
							//1、取到此条数据流程的所有表体（即所有的执行任务）
							Collection bvos= getBaseDao().retrieveByClause(DipProcessflowBVO.class, "pk_processflow_h='"+pk_bus+"' and nvl(dr,0)=0");
							String wherein="";
							if(bvos!=null&&bvos.size()>0){
								for(DipProcessflowBVO bvo:(List<DipProcessflowBVO>)bvos){
									wherein=wherein+"'"+bvo.getCode()+"',";
								}
								if(wherein.length()>0){
									wherein=wherein.substring(0,wherein.length()-1);
									String sql="update dip_warningset set isnotwarning = '禁用' where nvl(dr,0)=0 and isnotwarning = '可用' and pk_bustab in("+wherein+")";
									getBaseDao().executeUpdate(sql);
								}
							}
							
						}else{//如果是其它的任务类型，那么就要去找数据流程，如果任务流程里的某个流程包含了此流程并且此流程还是可用状态，那么就把当前的流程禁用掉
							//1、得到数据流程里，所有包含此业务的流程
							Collection bvos= getBaseDao().retrieveByClause(DipProcessflowBVO.class, "code='"+pk_bus+"' and nvl(dr,0)=0");
							String wherein="";
							//2、把得到的流程，所有的主表主键拼成一个字符串
							if(bvos!=null&&bvos.size()>0){
								for(DipProcessflowBVO bvo:(List<DipProcessflowBVO>)bvos){
									wherein=wherein+"'"+bvo.getPk_processflow_h()+"',";
								}
								//3、如果这个主键的字符串的长度大于零
								if(wherein.length()>0){
									wherein=wherein.substring(0,wherein.length()-1);
									//4、拿到主表主键，就要去找预警vo，如果有预警的vo是启用的，那么，就不能再更新
									Collection wvos=getBaseDao().retrieveByClause(DipWarningsetVO.class, "nvl(dr,0)=0 and isnotwarning='可用' and pk_bustab in("+wherein+")");
									if(wvos!=null&&wvos.size()>0){
										isupdate=false;
										msg.setMessage("已经有数据流程引用此记录");
									}else{
										isupdate=isupdate;
									}
								}
							}
							
						}
						if(isupdate){
        					String sql = "update dip_warningset set isnotwarning = '可用' where pk_warningset='"+wvo.getPrimaryKey()+"'";
        					getBaseDao().executeUpdate(sql);
        					msg.setIssucc(true);
						}else{
							String sql = "update dip_warningset set isnotwarning = '禁用' where pk_warningset='"+wvo.getPrimaryKey()+"'";
        					getBaseDao().executeUpdate(sql);
        					msg.setIssucc(true);
        					msg.setMessage("已经有流程引用此任务");
						}
					}else{
						msg.setMessage("预警任务类型为空！");
					}
					
	        		
	        		
	        	}
			} catch (DAOException e) {
				e.printStackTrace();
				msg.setMessage(e.getMessage());
			}
			
			return msg;
	}

	/**
	 * @desc 获取当前预警下次执行时间
	 * @author 张进双
	 * @since nc502 2011-5-14
	 * @param pk_dip_warningsetdaytime
	 * @return 下次执行时间
	 * @throws Exception
	 */
	public String getNextDate(String pk_dip_warningsetdaytime) throws Exception{
		String nextDate=null;
		if(pk_dip_warningsetdaytime==null||pk_dip_warningsetdaytime.length()<=0){
			return nextDate;
		}
		TimingSettingVO tsvo=(TimingSettingVO) getBaseDao().retrieveByPK(TimingSettingVO.class, pk_dip_warningsetdaytime);
		TimeConfigVO tcvo=PaConstant.transTimingSettingVO2TimeConfigVO(tsvo);
		if(tcvo!=null){
			tcvo.resume();
			Long time=tcvo.getScheExecTime();
			String times=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
			nextDate=times;
		}
		return nextDate;
		/*
		String nextDate = null;
		if(pk_dip_warningsetdaytime==null||pk_dip_warningsetdaytime.length()<=0){
			return nextDate;
		}
		//获取当前时间的年月日时分秒如2011-05-14 18:07:22
		UFDateTime now = new UFDateTime(new Date());
		String nowDate = now.toString();
		DipWarningsetDayTimeVO dayTime = null;
		String sql = "select * from dip_warningsetdaytime where pk_dip_warningsetdaytime = '"+pk_dip_warningsetdaytime+"' ";
		dayTime = (DipWarningsetDayTimeVO) getBaseDao().executeQuery(sql, new BeanProcessor(DipWarningsetDayTimeVO.class));
		
		//当为每月的时候执行
		if(dayTime!=null&&dayTime.getDwm().equals("2")){
			//			获取当前的日
			String nowday = nowDate.substring(8, 10);
			int nowtime = Integer.valueOf(nowday);
			//获取下次执行的日
			int day = Integer.valueOf(getDay(nowday,pk_dip_warningsetdaytime));
			//如果当前日大于下次执行日的月的设计
			int zhixingmoth = 0;
			String year = null;
			//			如果为周期
			if(dayTime.getFo().equals("0")){
//				 当前时间如：时分秒
				String shifenmaio = nowDate.substring(11, 19);
				int i = getdangqianday( pk_dip_warningsetdaytime, nowday);
				String periodstart = dayTime.getPeriodstart();
				String periodsend = dayTime.getPeriodend();
				if(i == 1){
					String ym = nowDate.substring(0, 7);
					// 获取周期开始时间和结束时间差的时分秒
					int hour = Integer.valueOf(periodsend.substring(0, 2))- Integer.valueOf(periodstart.substring(0, 2)) ;
					int minute = Integer.valueOf(periodsend.substring(3, 5))- Integer.valueOf(periodstart.substring(3, 5)) ;
					// 总的时间差用秒算；
					int sumtime = (hour*60) + minute ;
					
				//	 获取周期频率；
						int periodtime = 0;
						if(!"".equals(dayTime)){
							periodtime = dayTime.getPeriodtime();
						}
						
//						当前时间小于周期开始时间
						if(Integer.valueOf(nowDate.substring(11, 13))*60+Integer.valueOf(nowDate.substring(14, 16))<Integer.valueOf(dayTime.getPeriodstart().substring(0, 2))*60+Integer.valueOf(dayTime.getPeriodstart().substring(3, 5))){
							nextDate = nowDate.substring(0, 11) + dayTime.getPeriodstart();
						}
//						当前时间的时大于周期开始时间的时并小于结束的时间
						if(Integer.valueOf(dayTime.getPeriodstart().substring(0, 2))*60+Integer.valueOf(dayTime.getPeriodstart().substring(3, 5)) < Integer.valueOf(shifenmaio.substring(0, 2))*60+Integer.valueOf(shifenmaio.substring(3, 5)) &&  Integer.valueOf(shifenmaio.substring(0, 2))*60+Integer.valueOf(shifenmaio.substring(3, 5))<Integer.valueOf(periodsend.substring(0, 2))*60+Integer.valueOf(periodsend.substring(3, 5))){
//							 判断周期频率是否在时间差内；
							if(sumtime < periodtime){//不在时间差内
								if(day>nowtime){
									nextDate = ym + "-" + day + " " + dayTime.getPeriodstart();
								}else{//如果下次执行时间有比当前时间小的话；先查询出不当前日小的日是几号；
									int getLittleday = Integer.valueOf(getDay(pk_dip_warningsetdaytime));
									if(getLittleday<day){
									int moth = Integer.valueOf(nowDate.substring(5, 7));
								    zhixingmoth = moth+1;
									nextDate = nowDate.substring(0, 5)+ zhixingmoth + "-"  + getLittleday+ " " + dayTime.getPeriodstart();
								}
								}
						}
							//在时间差内
							else{
							// 当前时间的时
							int hournow = Integer.valueOf(nowDate.substring(11, 13));
							if(!"".equals(dayTime)){
								if(dayTime.getPeriodtime() >= 60){
									String nowshi = null;
									String nowfen = null;
									int shi = (dayTime.getPeriodtime())/60;
									int fen = (dayTime.getPeriodtime())%60;
									if((hournow+shi)<10){
										nowshi = "0"+String.valueOf(shi+hournow);
									}else{
										nowshi = String.valueOf(shi+hournow);
									}
									if((Integer.valueOf(nowDate.substring(14, 16))+fen)<10){
										nowfen = "0"+String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
									}else{
										nowfen = String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
									}
									//如果当前时间加周期频率时间大于结束时间
									if((shi+hournow)*60+(Integer.valueOf(nowDate.substring(14, 16))+fen) > Integer.valueOf(dayTime.getPeriodend().substring(0, 2))*60 +Integer.valueOf(dayTime.getPeriodend().substring(3, 5)) ){
										//如果下次执行时间大于当前时间
										if(day>nowtime){
											nextDate = nowDate.substring(0, 7)+  "-"  + day+ " " + dayTime.getPeriodstart();
										}else{//如果下次执行时间有比当前时间小的话；先查询出不当前日小的日是几号；
										int getLittleday = Integer.valueOf(getDay(pk_dip_warningsetdaytime));
										if(getLittleday<day){
										int moth = Integer.valueOf(nowDate.substring(5, 7));
									    zhixingmoth = moth+1;
										nextDate = nowDate.substring(0, 5)+ zhixingmoth + "-"  + getLittleday+ " " + dayTime.getPeriodstart();
										}
										}
									}else{
										nextDate =  nowDate.substring(0, 10) +" " + nowshi +":"+nowfen+nowDate.substring(16, 19);
									}
									
								}
								//时间差小于60秒
								else{
									String nowshi = null;
									String nowfen = null;
									int fen = dayTime.getPeriodtime();
									if((hournow)<10){
										nowshi = "0"+String.valueOf(hournow);
									}else{
										nowshi = String.valueOf(hournow);
									}
									if((Integer.valueOf(nowDate.substring(14, 16))+fen)<10){
										nowfen = "0"+String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
									}else{
										nowfen = String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
									}
//									如果当前时间加周期频率时间大于结束时间
									if((hournow)*60+(Integer.valueOf(nowDate.substring(14, 16))+fen) > Integer.valueOf(dayTime.getPeriodend().substring(0, 2))*60 +Integer.valueOf(dayTime.getPeriodend().substring(3, 5)) ){
										//如果下次执行时间大于当前时间
										if(day>nowtime){
											nextDate = nowDate.substring(0, 7)+  "-"  + day+ " " + dayTime.getPeriodstart();
										}else{//如果下次执行时间有比当前时间小的话；先查询出不当前日小的日是几号；
											int getLittleday = Integer.valueOf(getDay(pk_dip_warningsetdaytime));
											if(getLittleday<day){
											int moth = Integer.valueOf(nowDate.substring(5, 7));
										    zhixingmoth = moth+1;
											nextDate = nowDate.substring(0, 5)+ zhixingmoth + "-"  + day+ " " + dayTime.getPeriodstart();
										}
										}
									}else{
										nextDate =  nowDate.substring(0, 10) +" " + nowshi +":"+nowfen+nowDate.substring(16, 19);
									}
								}
							}
						}
						}
						
//						当前时间大于周期结束时间
						if(Integer.valueOf(periodsend.substring(0, 2))*60+Integer.valueOf(periodsend.substring(3, 5)) < Integer.valueOf(shifenmaio.substring(0, 2))*60+Integer.valueOf(shifenmaio.substring(3, 5))){
							if(day>nowtime){
								nextDate = nowDate.substring(0, 7)+  "-"  + day+ " " + dayTime.getPeriodstart();
							}else{//如果下次执行时间有比当前时间小的话；先查询出不当前日小的日是几号；
								int getLittleday = Integer.valueOf(getDay(pk_dip_warningsetdaytime));
								if(getLittleday<day){
							int moth = Integer.valueOf(nowDate.substring(5, 7));
						    zhixingmoth = moth+1;
							nextDate = nowDate.substring(0, 5)+ zhixingmoth + "-"  + getLittleday+ " " + dayTime.getPeriodstart();
							}
							}
						}
				}else{
					if(nowtime>day){//如果下次执行时间有比当前时间小的话；先查询出不当前日小的日是几号；
						int getLittleday = Integer.valueOf(getDay(pk_dip_warningsetdaytime));
						if(getLittleday<day){
					    year = nowDate.substring(0, 5);
						int moth = Integer.valueOf(nowDate.substring(5, 7));
					    zhixingmoth = moth+1;
						nextDate = year + zhixingmoth+"-" + getLittleday + " " + dayTime.getPeriodstart();
					}
					}
					if(nowtime<day){
						String ym = nowDate.substring(0, 7);
						nextDate = ym + "-"  + day + " " + dayTime.getPeriodstart();
					}
				}
			}
			//不为周期
			else{
				int i = getdangqianday( pk_dip_warningsetdaytime, nowday);
				if(i==1){//判断是为当天
					//		获取当前的时
					int hour = Integer.valueOf(nowDate.substring(11, 13));
					//获取当前的分
					int fen = Integer.valueOf(nowDate.substring(14, 16));
					//获取预警时间的时
					int warninghour = Integer.valueOf(dayTime.getWarntime().substring(0, 2));
					int warningfen = Integer.valueOf(dayTime.getWarntime().substring(3, 5));
//					当前时间小于预警时间
					if(hour*60+fen < warninghour*60+warningfen){
						nextDate = nowDate.substring(0, 10)+ " " + dayTime.getWarntime();
					}else{//当前时间大于预警时间
						year = nowDate.substring(0, 8);
						if(day>nowtime){//下次执行日大于当前日
							if(day<10){
								nextDate = year  + day + " " + dayTime.getWarntime();
							}else{
								nextDate = year  + day + " " + dayTime.getWarntime();
							}
						}else{//下次执行日小于当前日
//							如果下次执行时间有比当前时间小的话；先查询出不当前日小的日是几号；
							int getLittleday = Integer.valueOf(getDay(pk_dip_warningsetdaytime));
							if(getLittleday<day){
							int moth = Integer.valueOf(nowDate.substring(5, 7));
						    zhixingmoth = moth+1;
								nextDate = nowDate.substring(0, 5)+ zhixingmoth + "-" + getLittleday + " " + dayTime.getWarntime();
							}
						}
					}
				}else{//当天没有预警
					if(nowtime>day){
	//					如果下次执行时间有比当前时间小的话；先查询出不当前日小的日是几号；
						int getLittleday = Integer.valueOf(getDay(pk_dip_warningsetdaytime));
						if(getLittleday<day){
					    year = nowDate.substring(0, 5);
						int moth = Integer.valueOf(nowDate.substring(5, 7));
					    zhixingmoth = moth+1;
					    if(Integer.valueOf(zhixingmoth)<10){
					    	nextDate = year + "0"+zhixingmoth+"-" + getLittleday + " " + dayTime.getWarntime();
					    }else{
					    	nextDate = year + zhixingmoth+"-" + getLittleday + " " + dayTime.getWarntime();
					    }
						}
					}
					if(nowtime<day){
						String ym = nowDate.substring(0, 7);
						if(day<10){
							nextDate = ym  +"-"+ "0"+day + " " + dayTime.getWarntime();
						}else{
							nextDate = ym +"-" + day + " " + dayTime.getWarntime();
						}
					}
				}
			}
		}
		//当为星期的时候执行
		else if(dayTime!=null&&dayTime.getDwm().equals("1")){ 
			//获取当前的周
			String nowweek = getWeekOfDate(new Date());
			//下次执行的是周几；
			String nextweek = getNextWeek(nowweek,pk_dip_warningsetdaytime);
			//			下次执行周时应在当前日加的天数；
			int days = 0;
			if("星期一".equals(nextweek)){
				days = 1;
			}
			if("星期二".equals(nextweek)){
				days = 2;
			}
			if("星期三".equals(nextweek)){
				days = 3;
			}
			if("星期四".equals(nextweek)){
				days = 4;
			}
			if("星期五".equals(nextweek)){
				days = 5;
			}
			if("星期六".equals(nextweek)){
				days = 6;
			}
			if("星期日".equals(nextweek)){
				days = 7;
			}
			//当前周指定的天数
			
			int nowdays = 0;
			if("星期一".equals(nowweek)){
				nowdays = 1;
			}
			if("星期二".equals(nowweek)){
				nowdays = 2;
			}
			if("星期三".equals(nowweek)){
				nowdays = 3;
			}
			if("星期四".equals(nowweek)){
				nowdays = 4;
			}
			if("星期五".equals(nowweek)){
				nowdays = 5;
			}
			if("星期六".equals(nowweek)){
				nowdays = 6;
			}
			if("星期日".equals(nowweek)){
				nowdays = 7;
			}
			
			//获取比当前周小的周
			String getlittleweek = getWeek(pk_dip_warningsetdaytime);
			int littleweek = 0;
			if("星期一".equals(getlittleweek)){
				littleweek = 1;
			}
			if("星期二".equals(getlittleweek)){
				littleweek = 2;
			}
			if("星期三".equals(getlittleweek)){
				littleweek = 3;
			}
			if("星期四".equals(getlittleweek)){
				littleweek = 4;
			}
			if("星期五".equals(getlittleweek)){
				littleweek = 5;
			}
			if("星期六".equals(getlittleweek)){
				littleweek = 6;
			}
			if("星期日".equals(getlittleweek)){
				littleweek = 7;
			}
			
			//当前日
			int dangqiantian = Integer.valueOf(nowDate.substring(8, 10));
			
			String ym = nowDate.substring(0, 7);
			String periodstart = dayTime.getPeriodstart();
			String periodsend = dayTime.getPeriodend();
//			 当前时间如：时分秒
			String shifenmaio = nowDate.substring(11, 19);
			//如果为周期
			if(dayTime.getFo().equals("0")){
				int dn = 0;
				String day = null;
				int i = getdangqianweek(pk_dip_warningsetdaytime,nowweek);
				if(i==1){//在当前星期内
					if((days-nowdays)<=0){//判断是下个本周或是下个周
						//当前时间小于周期开始时间
						if(Integer.valueOf(nowDate.substring(11, 13))*60+Integer.valueOf(nowDate.substring(14, 16))<Integer.valueOf(dayTime.getPeriodstart().substring(0, 2))*60+Integer.valueOf(dayTime.getPeriodstart().substring(3, 5))){
							nextDate = nowDate.substring(0, 11) + dayTime.getPeriodstart();
						}
//						当前时间的时大于周期开始时间的时并小于结束的时间
						if(Integer.valueOf(dayTime.getPeriodstart().substring(0, 2))*60+Integer.valueOf(dayTime.getPeriodstart().substring(3, 5)) < Integer.valueOf(shifenmaio.substring(0, 2))*60+Integer.valueOf(shifenmaio.substring(3, 5)) &&  Integer.valueOf(shifenmaio.substring(0, 2))*60+Integer.valueOf(shifenmaio.substring(3, 5))<Integer.valueOf(periodsend.substring(0, 2))*60+Integer.valueOf(periodsend.substring(3, 5))){
							// 获取周期开始时间和结束时间差的时分秒
							int hour = Integer.valueOf(periodsend.substring(0, 2))- Integer.valueOf(periodstart.substring(0, 2)) ;
							int minute = Integer.valueOf(periodsend.substring(3, 5))- Integer.valueOf(periodstart.substring(3, 5)) ;
							// 总的时间差用秒算；
							int sumtime = (hour*60) + minute ;
						//	 获取周期频率；
								int periodtime = 0;
								if(!"".equals(dayTime)){
									periodtime = dayTime.getPeriodtime();
								}
								// 判断周期频率是否在时间差内；
								if(sumtime < periodtime){//不在时间差内
//									判断有没有比当前周小的周没有
									if(littleweek<=nowdays){
									dn = littleweek+nowdays-1;
									day = String.valueOf(dangqiantian+dn);
									nextDate = ym + "-" + day + " " + dayTime.getPeriodstart();
									}
							}
								//在时间差内
								else{
								// 当前时间的时
								int hournow = Integer.valueOf(nowDate.substring(11, 13));
								if(!"".equals(dayTime)){
									if(dayTime.getPeriodtime() >= 60){
										String nowshi = null;
										String nowfen = null;
										int shi = (dayTime.getPeriodtime())/60;
										int fen = (dayTime.getPeriodtime())%60;
										if((hournow+shi)<10){
											nowshi = "0"+String.valueOf(shi+hournow);
										}else{
											nowshi = String.valueOf(shi+hournow);
										}
										if((Integer.valueOf(nowDate.substring(14, 16))+fen)<10){
											nowfen = "0"+String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
										}else{
											nowfen = String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
										}
										//当前时间加上期间频率 大于 周期结束时间
										if((shi+hournow)*60+(Integer.valueOf(nowDate.substring(14, 16))+fen) > Integer.valueOf(dayTime.getPeriodend().substring(0, 2))*60 +Integer.valueOf(dayTime.getPeriodend().substring(3, 5)) ){
//											判断有没有比当前周小的周没有
											if(littleweek<=nowdays){
											dn = littleweek+nowdays-1;
											day = String.valueOf(dangqiantian+dn);
											nextDate = ym + "-" + day + " " + dayTime.getPeriodstart();
											}else{
												dn = days+nowdays-1;
												day = String.valueOf(dangqiantian+dn);
												nextDate = ym + "-" + day + " " + dayTime.getPeriodstart();
											}
										}else{
											nextDate =  nowDate.substring(0, 10) +" " + nowshi +":"+nowfen+nowDate.substring(16, 19);
										}
										
									}
									//时间差小于60秒
									else{
										String nowshi = null;
										String nowfen = null;
										int fen = dayTime.getPeriodtime();
										if((hournow)<10){
											nowshi = "0"+String.valueOf(hournow);
										}else{
											nowshi = String.valueOf(hournow);
										}
										if((Integer.valueOf(nowDate.substring(14, 16))+fen)<10){
											nowfen = "0"+String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
										}else{
											nowfen = String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
										}
//										当前时间加上期间频率 大于 周期结束时间
										if((hournow)*60+(Integer.valueOf(nowDate.substring(14, 16))+fen) > Integer.valueOf(dayTime.getPeriodend().substring(0, 2))*60 +Integer.valueOf(dayTime.getPeriodend().substring(3, 5)) ){
//											判断有没有比当前周小的周没有
											if(littleweek<=nowdays){
											dn = littleweek+nowdays-1;
											day = String.valueOf(dangqiantian+dn);
											nextDate = ym + "-" + day + " " + dayTime.getPeriodstart();
											}else{
												dn = days+nowdays-1;
												day = String.valueOf(dangqiantian+dn);
												nextDate = ym + "-" + day + " " + dayTime.getPeriodstart();
											}
										}else{
											nextDate =  nowDate.substring(0, 10) +" " + nowshi +":"+nowfen+nowDate.substring(16, 19);
										}
									}
								}
								
								} 
						}
						//当前时间大于周期结束时间
						if(Integer.valueOf(periodsend.substring(0, 2))*60+Integer.valueOf(periodsend.substring(3, 5)) < Integer.valueOf(shifenmaio.substring(0, 2))*60+Integer.valueOf(shifenmaio.substring(3, 5))){
//							判断有没有比当前周小的周没有
							if(littleweek<=nowdays){
							dn = littleweek+nowdays-1;
							day = String.valueOf(dangqiantian+dn);
							nextDate = ym + "-"  + day+ " " + dayTime.getPeriodstart();
							}else{
								dn = days+nowdays-1;
								day = String.valueOf(dangqiantian+dn);
								nextDate = ym + "-"  + day+ " " + dayTime.getPeriodstart();
							}
						}
					}
					if(days>nowdays){
						dn = days-nowdays;
						day = String.valueOf(dangqiantian+dn);
						nextDate = ym + "-"  + day+ " " + dayTime.getPeriodstart();
					}
			}
				else{//当前的星期几不是预警星期
					if((days-nowdays)<=0){//判断是下个本周或是下个周
//						判断有没有比当前周小的周没有
						if(littleweek<=nowdays){
							dn = littleweek+nowdays-1;
							day = String.valueOf(dangqiantian+dn);
							nextDate = ym + "-"  + day+ " " + dayTime.getPeriodstart();
						}
					}else{ 
						dn = days-nowdays;
						if(dangqiantian+dn<10){
							day = "0"+String.valueOf(dangqiantian+dn);
						}else{
							day = String.valueOf(dangqiantian+dn);
						}
						nextDate = ym + "-"  + day+ " " + dayTime.getPeriodstart();
						
					}
					}
			}else if(dayTime.getFo().equals("1")){// 执行一次
				int i = getdangqianweek(pk_dip_warningsetdaytime,nowweek);
				int dn = 0;
				String day = null;
					if(i==1){
						//当前时间的时小于预警时间的时；
						if(Integer.valueOf(nowDate.substring(11, 13))*60+Integer.valueOf(nowDate.substring(14, 16))<Integer.valueOf(dayTime.getWarntime().substring(0, 2))*60+Integer.valueOf(dayTime.getWarntime().substring(3, 5))){
							nextDate = nowDate.substring(0, 11) + dayTime.getWarntime();
						}
						else{//大于预警时间
							if(days>nowdays){
								if(dangqiantian+(days-nowdays)<10){
									day = "0"+String.valueOf(dangqiantian+(days-nowdays));
								}else{
									day = String.valueOf(dangqiantian+(days-nowdays));
								}
								nextDate = ym + "-"  + day+ " " + dayTime.getWarntime();
							} 
							if(days<=nowdays){//判断有没有比当前周小的周没有
								if(littleweek<=nowdays){
									if(dangqiantian+(littleweek+nowdays-1)<10){
										day = "0"+String.valueOf(dangqiantian+(littleweek+nowdays-1));
									}else{
										day = String.valueOf(dangqiantian+(littleweek+nowdays-1));
									}
									nextDate = ym + "-" + day+ " " + dayTime.getWarntime();
								}
							}
							 
						}
					}else{
					if(days<nowdays){
//						判断有没有比当前周小的周没有
						if(littleweek<=nowdays){
						dn = (littleweek+nowdays)-1;
					if(dangqiantian+dn<10){
						day = "0"+String.valueOf(dangqiantian+dn);
					}else{
						day = String.valueOf(dangqiantian+dn);
					}
					nextDate = ym + "-"  + day+ " " + dayTime.getWarntime();
						}
					}else{
						dn = days-nowdays;
						if(dangqiantian+dn<10){
							day = "0"+String.valueOf(dangqiantian+dn);
						}else{
							day = String.valueOf(dangqiantian+dn);
						}
						nextDate = ym + "-"  + day+ " " + dayTime.getWarntime();
					}
				}
			}
		}
		//当为每天都执行时
		else{
			//			如果为周期
			if(dayTime.getFo().equals("0")){
				String periodstart = dayTime.getPeriodstart();
				String periodsend = dayTime.getPeriodend();
				//				 当前时间如：时分秒
				String shifenmaio = nowDate.substring(11, 19);
				// 当前时间的时小于周期开始时间的时
				if(Integer.valueOf(nowDate.substring(11, 13))*60+Integer.valueOf(nowDate.substring(14, 16))<Integer.valueOf(dayTime.getPeriodstart().substring(0, 2))*60+Integer.valueOf(dayTime.getPeriodstart().substring(3, 5))){
					nextDate = nowDate.substring(0, 10) + " "+dayTime.getPeriodstart();
				}
			 
				//当前时间的时大于周期开始时间的并小于周期结束时间时
				if(Integer.valueOf(dayTime.getPeriodstart().substring(0, 2))*60+Integer.valueOf(dayTime.getPeriodstart().substring(3, 5)) < Integer.valueOf(shifenmaio.substring(0, 2))*60+Integer.valueOf(shifenmaio.substring(3, 5)) &&  Integer.valueOf(shifenmaio.substring(0, 2))*60+Integer.valueOf(shifenmaio.substring(3, 5))<Integer.valueOf(periodsend.substring(0, 2))*60+Integer.valueOf(periodsend.substring(3, 5))){
					// 获取周期开始时间和结束时间差的时分秒
					int hour = Integer.valueOf(periodsend.substring(0, 2))- Integer.valueOf(periodstart.substring(0, 2)) ;
					int minute = Integer.valueOf(periodsend.substring(3, 5))- Integer.valueOf(periodstart.substring(3, 5)) ;
					int second =  Integer.valueOf(periodsend.substring(6, 8))-Integer.valueOf(periodstart.substring(6, 8));
					// 总的时间差用秒算；
					int sumtime = (hour*60) + minute ;
					// 获取周期频率；
					int periodtime = 0;
					if(!"".equals(dayTime)){
						periodtime = dayTime.getPeriodtime();
					}
					// 判断周期频率是否在时间差内；
					if(sumtime < periodtime){//不在时间差内
						nextDate = nowDate.substring(0, 8)+(Integer.valueOf(nowDate.substring(8, 10))+1) + " " + dayTime.getPeriodstart();
					}else{
						// 当前时间的时
						int hournow = Integer.valueOf(nowDate.substring(11, 13));
						if(!"".equals(dayTime)){
							if(dayTime.getPeriodtime() >= 60){
								String nowshi = null;
								String nowfen = null;
								int shi = (dayTime.getPeriodtime())/60;
								int fen = (dayTime.getPeriodtime())%60;
								if((hournow+shi)<10){
									nowshi = "0"+String.valueOf(shi+hournow);
								}else{
									nowshi = String.valueOf(shi+hournow);
								}
								if((Integer.valueOf(nowDate.substring(14, 16))+fen)<10){
									nowfen = "0"+String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
								}else{
									nowfen = String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
								}
								if((shi+hournow)*60+(Integer.valueOf(nowDate.substring(14, 16))+fen) > Integer.valueOf(dayTime.getPeriodend().substring(0, 2))*60 +Integer.valueOf(dayTime.getPeriodend().substring(3, 5)) ){
//									让日加1
									DateFormat   format=new   SimpleDateFormat("yyyy-MM-dd");  
									Calendar calendar = Calendar.getInstance();
									calendar.add(Calendar.DAY_OF_MONTH,1); 
									nextDate = format.format(calendar.getTime())+" " + dayTime.getPeriodstart();
								}else{
								nextDate =  nowDate.substring(0, 10) +" " + nowshi +":"+nowfen+nowDate.substring(16, 19);
								}
							}
							else{//大于60分钟
								
								String nowshi = null;
								String nowfen = null;
								int fen = dayTime.getPeriodtime();
								if((hournow)<10){
									nowshi = "0"+String.valueOf(hournow);
								}else{
									nowshi = String.valueOf(hournow);
								}
								if((Integer.valueOf(nowDate.substring(14, 16))+fen)<10){
									nowfen = "0"+String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
								}else{
									nowfen = String.valueOf(Integer.valueOf(nowDate.substring(14, 16))+fen);
								}
								
								if((hournow)*60+(Integer.valueOf(nowDate.substring(14, 16))+fen) > Integer.valueOf(dayTime.getPeriodend().substring(0, 2))*60 +Integer.valueOf(dayTime.getPeriodend().substring(3, 5)) ){
//									让日加1
									DateFormat   format=new   SimpleDateFormat("yyyy-MM-dd");  
									Calendar calendar = Calendar.getInstance();
									calendar.add(Calendar.DAY_OF_MONTH,1); 
									nextDate = format.format(calendar.getTime())+" " + dayTime.getPeriodstart();
								}else{
								nextDate =  nowDate.substring(0, 10) +" " + nowshi +":"+nowfen+nowDate.substring(16, 19);
								}
							}
						}
					}
					
				}
				//当前时间大于周期结束时间
				if(Integer.valueOf(periodsend.substring(0, 2))*60+Integer.valueOf(periodsend.substring(3, 5)) < Integer.valueOf(shifenmaio.substring(0, 2))*60+Integer.valueOf(shifenmaio.substring(3, 5))){
					nextDate = nowDate.substring(0, 8)+(Integer.valueOf(nowDate.substring(8, 10))+1)+ " " + dayTime.getPeriodstart();
				}
				}
			
			else{//执行一次
				
			//		获取当前的时
		int hour = Integer.valueOf(nowDate.substring(11, 13));
		//获取当前的分
		int fen = Integer.valueOf(nowDate.substring(14, 16));
		//获取预警时间的时
		int warninghour = Integer.valueOf(dayTime.getWarntime().substring(0, 2));
		int warningfen = Integer.valueOf(dayTime.getWarntime().substring(3, 5));
			//	判断是当天
				if(hour*60+fen < warninghour*60+warningfen){
					nextDate = nowDate.substring(0, 10) + " "+dayTime.getWarntime();
				}else{
					//让日加1
					DateFormat   format=new   SimpleDateFormat("yyyy-MM-dd");  
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_MONTH,1); 
					nextDate = format.format(calendar.getTime())+" "+dayTime.getWarntime();
				}
		}
		}
		return nextDate;
	*/}
	/**
	 * 获取下次执行的日
	 * @param day 当前的日
	 * @return 下次执行的日
	 */
	private String getDay(String nowday,String pk_dip_warningsetdaytime) throws Exception{
		String netday ="";
		DipWarningsetDayTimeVO dayTime = null;
		String sql = "select * from dip_warningsetdaytime where pk_dip_warningsetdaytime = '"+pk_dip_warningsetdaytime+"' ";
		dayTime = (DipWarningsetDayTimeVO) getBaseDao().executeQuery(sql, new BeanProcessor(DipWarningsetDayTimeVO.class));
		if("01".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("02".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("03".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("04".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("05".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("06".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("07".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("08".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("09".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("10".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("11".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("12".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("13".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("14".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("15".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("16".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("17".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("18".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("19".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("20".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("21".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("22".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("23".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("24".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("25".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("26".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("27".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("28".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("29".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("30".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		}else if("31".equals(nowday)){
			netday = getNextDay(pk_dip_warningsetdaytime);
		} 
		return netday;
	}
	//查询下次执行日用的方法
	public String getNextDay(String pk_dip_warningsetdaytime)throws Exception{
		String netday = "";
		DipWarningsetDayTimeVO dayTime = null;
		String sql = "select * from dip_warningsetdaytime where pk_dip_warningsetdaytime = '"+pk_dip_warningsetdaytime+"' ";
		dayTime = (DipWarningsetDayTimeVO) getBaseDao().executeQuery(sql, new BeanProcessor(DipWarningsetDayTimeVO.class));
		UFBoolean d1 = dayTime.getD1();
		if(d1!=null && d1.booleanValue()){
			netday = "01";
			return netday;
		}
		UFBoolean d2 = dayTime.getD2();
		if(d2!=null && d2.booleanValue()){
			netday = "02";
			return netday;
		}
		UFBoolean d3 = dayTime.getD3();
		if(d3!=null && d3.booleanValue()){
			netday = "03";
			return netday;
		}
		UFBoolean d4 = dayTime.getD4();
		if(d4!=null && d4.booleanValue()){
			netday = "04";
		}
		UFBoolean d5 = dayTime.getD5();
		if(d5!=null && d5.booleanValue()){
			netday = "05";
		}
		UFBoolean d6 = dayTime.getD6();
		if(d6!=null && d6.booleanValue()){
			netday = "06";
		}
		UFBoolean d7 = dayTime.getD7();
		if(d7!=null && d7.booleanValue()){
			netday = "07";
		}
		UFBoolean d8 = dayTime.getD8();
		if(d8!=null && d8.booleanValue()){
			netday = "08";
		}
		UFBoolean d9 = dayTime.getD9();
		if(d9!=null && d9.booleanValue()){
			netday = "09";
		}
		UFBoolean d10 = dayTime.getD10();
		if(d10!=null && d10.booleanValue()){
			netday = "10";
		}
		UFBoolean d11 = dayTime.getD11();
		if(d11!=null && d11.booleanValue()){
			netday = "11";
		}
		UFBoolean d12 = dayTime.getD12();
		if(d12!=null && d12.booleanValue()){
			netday = "12";
		}
		UFBoolean d13 = dayTime.getD13();
		if(d13!=null && d13.booleanValue()){
			netday = "13";
		}
		UFBoolean d14 = dayTime.getD14();
		if(d14!=null && d14.booleanValue()){
			netday = "14";
		}
		UFBoolean d15 = dayTime.getD15();
		if(d15!=null && d15.booleanValue()){
			netday = "15";
		}
		UFBoolean d16 = dayTime.getD16();
		if(d16!=null && d16.booleanValue()){
			netday = "16";
		}
		UFBoolean d17 = dayTime.getD17();
		if(d17!=null && d17.booleanValue()){
			netday = "17";
		}
		UFBoolean d18 = dayTime.getD18();
		if(d18!=null && d18.booleanValue()){
			netday = "18";
		}
		UFBoolean d19 = dayTime.getD19();
		if(d19!=null && d19.booleanValue()){
			netday = "19";
		}
		UFBoolean d20 = dayTime.getD20();
		if(d20!=null && d20.booleanValue()){
			netday = "20";
		}
		UFBoolean d21 = dayTime.getD21();
		if(d21!=null && d21.booleanValue()){
			netday = "21";
		}
		UFBoolean d22 = dayTime.getD22();
		if(d22!=null && d22.booleanValue()){
			netday = "22";
		}
		UFBoolean d23 = dayTime.getD23();
		if(d23!=null && d23.booleanValue()){
			netday = "23";
		}
		UFBoolean d24 = dayTime.getD24();
		if(d24!=null && d24.booleanValue()){
			netday = "24";
		}
		UFBoolean d25 = dayTime.getD25();
		if(d25!=null && d25.booleanValue()){
			netday = "25";
		}
		UFBoolean d26 = dayTime.getD26();
		if(d26!=null && d26.booleanValue()){
			netday = "26";
		}
		UFBoolean d27 = dayTime.getD27();
		if(d27!=null && d27.booleanValue()){
			netday = "27";
		}
		UFBoolean d28 = dayTime.getD28();
		if(d28!=null && d28.booleanValue()){
			netday = "28";
		}
		UFBoolean d29 = dayTime.getD29();
		if(d29!=null && d29.booleanValue()){
			netday = "29";
		}
		UFBoolean d30 = dayTime.getD30();
		if(d30!=null && d30.booleanValue()){
			netday = "30";
		}
		UFBoolean d31 = dayTime.getD31();
		if(d31!=null && d31.booleanValue()){
			netday = "31";
		}
		return netday;
	}
	
	
	
	//如果下次执行日小于当前时间时；查询出比当前日小的时间
	public String getDay(String pk_dip_warningsetdaytime)throws Exception{
		String netday = "";
		DipWarningsetDayTimeVO dayTime = null;
		String sql = "select * from dip_warningsetdaytime where pk_dip_warningsetdaytime = '"+pk_dip_warningsetdaytime+"' ";
		dayTime = (DipWarningsetDayTimeVO) getBaseDao().executeQuery(sql, new BeanProcessor(DipWarningsetDayTimeVO.class));
		UFBoolean d1 = dayTime.getD1();
		UFBoolean d2 = dayTime.getD2();
		UFBoolean d3 = dayTime.getD3();
		UFBoolean d4 = dayTime.getD4();
		UFBoolean d5 = dayTime.getD5();
		UFBoolean d6 = dayTime.getD6();
		UFBoolean d7 = dayTime.getD7();
		UFBoolean d8 = dayTime.getD8();
		UFBoolean d9 = dayTime.getD9();
		UFBoolean d10 = dayTime.getD10();
		UFBoolean d11 = dayTime.getD11();
		UFBoolean d12 = dayTime.getD12();
		UFBoolean d13 = dayTime.getD13();
		UFBoolean d14 = dayTime.getD14();
		UFBoolean d15 = dayTime.getD15();
		UFBoolean d16 = dayTime.getD16();
		UFBoolean d17 = dayTime.getD17();
		UFBoolean d18 = dayTime.getD18();
		UFBoolean d19 = dayTime.getD19();
		UFBoolean d20 = dayTime.getD20();
		UFBoolean d21 = dayTime.getD21();
		UFBoolean d22 = dayTime.getD22();
		UFBoolean d23 = dayTime.getD23();
		UFBoolean d24 = dayTime.getD24();
		UFBoolean d25 = dayTime.getD25();
		UFBoolean d26 = dayTime.getD26();
		UFBoolean d27 = dayTime.getD27();
		UFBoolean d28 = dayTime.getD28();
		UFBoolean d29 = dayTime.getD29();
		UFBoolean d30 = dayTime.getD30();
		UFBoolean d31 = dayTime.getD31();
		if(d1!=null && d1.booleanValue()){
			netday = "01";
		}
		else
		if(d2!=null && d2.booleanValue()){
			netday = "02";
		}else
		if(d3!=null && d3.booleanValue()){
			netday = "03";
		}else
		if(d4!=null && d4.booleanValue()){
			netday = "04";
		}else
		if(d5!=null && d5.booleanValue()){
			netday = "05";
		}else
		if(d6!=null && d6.booleanValue()){
			netday = "06";
		}else
		if(d7!=null && d7.booleanValue()){
			netday = "07";
		}else
		if(d8!=null && d8.booleanValue()){
			netday = "08";
		}else
		if(d9!=null && d9.booleanValue()){
			netday = "09";
		}else
		if(d10!=null && d10.booleanValue()){
			netday = "10";
		}else
		if(d11!=null && d11.booleanValue()){
			netday = "11";
		}else
		if(d12!=null && d12.booleanValue()){
			netday = "12";
		}else
		if(d13!=null && d13.booleanValue()){
			netday = "13";
		}else
		if(d14!=null && d14.booleanValue()){
			netday = "14";
		}else
		if(d15!=null && d15.booleanValue()){
			netday = "15";
		}else
		if(d16!=null && d16.booleanValue()){
			netday = "16";
		}else
		if(d17!=null && d17.booleanValue()){
			netday = "17";
		}else
		if(d18!=null && d18.booleanValue()){
			netday = "18";
		}else
		if(d19!=null && d19.booleanValue()){
			netday = "19";
		}else
		if(d20!=null && d20.booleanValue()){
			netday = "20";
		}else
		if(d21!=null && d21.booleanValue()){
			netday = "21";
		}else
		if(d22!=null && d22.booleanValue()){
			netday = "22";
		}else
		if(d23!=null && d23.booleanValue()){
			netday = "23";
		}else
		if(d24!=null && d24.booleanValue()){
			netday = "24";
		}else
		if(d25!=null && d25.booleanValue()){
			netday = "25";
		}else
		if(d26!=null && d26.booleanValue()){
			netday = "26";
		}else
		if(d27!=null && d27.booleanValue()){
			netday = "27";
		}else
		if(d28!=null && d28.booleanValue()){
			netday = "28";
		}else
		if(d29!=null && d29.booleanValue()){
			netday = "29";
		}else
		if(d30!=null && d30.booleanValue()){
			netday = "30";
		}else
		if(d31!=null && d31.booleanValue()){
			netday = "31";
		}
		return netday;
	}
	//获取下次执行的周
	public String getNextWeek(String nowweek,String pk_dip_warningsetdaytime)throws Exception{
		String nextweek ="";
		DipWarningsetDayTimeVO dayTime = null;
		String sql = "select * from dip_warningsetdaytime where pk_dip_warningsetdaytime = '"+pk_dip_warningsetdaytime+"' ";
		dayTime = (DipWarningsetDayTimeVO) getBaseDao().executeQuery(sql, new BeanProcessor(DipWarningsetDayTimeVO.class));
		if("星期一".equals(nowweek)){
			nextweek = getNextWeek(pk_dip_warningsetdaytime);
		}
		if("星期二".equals(nowweek)){
			nextweek = getNextWeek(pk_dip_warningsetdaytime);
		} 
		if("星期三".equals(nowweek)){
			nextweek = getNextWeek(pk_dip_warningsetdaytime);
		}
		if("星期四".equals(nowweek)){
			nextweek = getNextWeek(pk_dip_warningsetdaytime);
		}
		if("星期五".equals(nowweek)){
			nextweek = getNextWeek(pk_dip_warningsetdaytime);
		}
		if("星期六".equals(nowweek)){
			nextweek = getNextWeek(pk_dip_warningsetdaytime);
		}
		if("星期日".equals(nowweek)){
			nextweek = getNextWeek(pk_dip_warningsetdaytime);
		}
		return nextweek;
	}
	
	//张进双 获取下次执行的周；
	public String getNextWeek(String pk_dip_warningsetdaytime)throws Exception{
		String nextweek = "";
		DipWarningsetDayTimeVO dayTime = null;
		String sql = "select * from dip_warningsetdaytime where pk_dip_warningsetdaytime = '"+pk_dip_warningsetdaytime+"' ";
		dayTime = (DipWarningsetDayTimeVO) getBaseDao().executeQuery(sql, new BeanProcessor(DipWarningsetDayTimeVO.class));
		UFBoolean w1 = dayTime.getW1();
		if(w1!=null && w1.booleanValue()){
			nextweek = "星期一";
		}UFBoolean w2 = dayTime.getW2();
		if(w2!=null && w2.booleanValue()){
			nextweek = "星期二";
		}UFBoolean w3 = dayTime.getW3();
		if(w3!=null && w3.booleanValue()){
			nextweek = "星期三";
		}UFBoolean w4 = dayTime.getW4();
		if(w4!=null && w4.booleanValue()){
			nextweek = "星期四";
		}UFBoolean w5 = dayTime.getW5();
		if(w5!=null && w5.booleanValue()){
			nextweek = "星期五";
		}UFBoolean w6 = dayTime.getW6();
		if(w6!=null && w6.booleanValue()){
			nextweek = "星期六";
		}UFBoolean w7 = dayTime.getW7();
		if(w7!=null && w7.booleanValue()){
			nextweek = "星期日";
		}
		return nextweek;
	}
	
	//获取比当前星期小的星期
	public String getWeek(String pk_dip_warningsetdaytime)throws Exception{
		String nextweek = "";
		DipWarningsetDayTimeVO dayTime = null;
		String sql = "select * from dip_warningsetdaytime where pk_dip_warningsetdaytime = '"+pk_dip_warningsetdaytime+"' ";
		dayTime = (DipWarningsetDayTimeVO) getBaseDao().executeQuery(sql, new BeanProcessor(DipWarningsetDayTimeVO.class));
		UFBoolean w1 = dayTime.getW1();
		UFBoolean w2 = dayTime.getW2();
		UFBoolean w3 = dayTime.getW3();
		UFBoolean w4 = dayTime.getW4();
		UFBoolean w5 = dayTime.getW5();
		UFBoolean w6 = dayTime.getW6();
		UFBoolean w7 = dayTime.getW7();
		if(w1!=null && w1.booleanValue()){
			nextweek = "星期一";
		}else
		if(w2!=null && w2.booleanValue()){
			nextweek = "星期二";
		}else
		if(w3!=null && w3.booleanValue()){
			nextweek = "星期三";
		}else
		if(w4!=null && w4.booleanValue()){
			nextweek = "星期四";
		}else
		if(w5!=null && w5.booleanValue()){
			nextweek = "星期五";
		}else
		if(w6!=null && w6.booleanValue()){
			nextweek = "星期六";
		}else
		if(w7!=null && w7.booleanValue()){
			nextweek = "星期日";
		}
		return nextweek;
	}
	
	
//	张进双 获取是否当前的周；
	public int getdangqianweek(String pk_dip_warningsetdaytime,String nowweek)throws Exception{
		int i=0;
		DipWarningsetDayTimeVO dayTime = null;
		String sql = "select * from dip_warningsetdaytime where pk_dip_warningsetdaytime = '"+pk_dip_warningsetdaytime+"' ";
		dayTime = (DipWarningsetDayTimeVO) getBaseDao().executeQuery(sql, new BeanProcessor(DipWarningsetDayTimeVO.class));
		UFBoolean w1 = dayTime.getW1();
		if(w1!=null && w1.booleanValue()){
			if(nowweek.equals("星期一")){
				i=1;
			}
		}UFBoolean w2 = dayTime.getW2();
		if(w2!=null && w2.booleanValue()){
			if(nowweek.equals("星期二")){
				i=1;
			}
		}UFBoolean w3 = dayTime.getW3();
		if(w3!=null && w3.booleanValue()){
			if(nowweek.equals("星期三")){
				i=1;
			} 
		}UFBoolean w4 = dayTime.getW4();
		if(w4!=null && w4.booleanValue()){
			if(nowweek.equals("星期四")){
				i=1;
			}
		}UFBoolean w5 = dayTime.getW5();
		if(w5!=null && w5.booleanValue()){
			if(nowweek.equals("星期五")){
				i=1;
			}
		}UFBoolean w6 = dayTime.getW6();
		if(w6!=null && w6.booleanValue()){
			if(nowweek.equals("星期六")){
				i=1;
			}
		}UFBoolean w7 = dayTime.getW7();
		if(w7!=null && w7.booleanValue()){
			if(nowweek.equals("星期日")){
				i=1;
			}
		}
		return i;
	}
	
	
//	张进双 获取是否当前的日；
	public int getdangqianday(String pk_dip_warningsetdaytime,String nowday)throws Exception{
		int i=0;
		DipWarningsetDayTimeVO dayTime = null;
		String sql = "select * from dip_warningsetdaytime where pk_dip_warningsetdaytime = '"+pk_dip_warningsetdaytime+"' ";
		dayTime = (DipWarningsetDayTimeVO) getBaseDao().executeQuery(sql, new BeanProcessor(DipWarningsetDayTimeVO.class));
		UFBoolean d1 = dayTime.getD1();
		if(d1!=null && d1.booleanValue()){
			if(nowday.equals("01")){
				i=1;
			}
		}
		UFBoolean d2 = dayTime.getD2();
		if(d2!=null && d2.booleanValue()){
			if(nowday.equals("02")){
				i=1;
			}
		}
		UFBoolean d3 = dayTime.getD3();
		if(d3!=null && d3.booleanValue()){
			if(nowday.equals("03")){
				i=1;
			}
		}
		UFBoolean d4 = dayTime.getD4();
		if(d4!=null && d4.booleanValue()){
			if(nowday.equals("04")){
				i=1;
			}
		}
		UFBoolean d5 = dayTime.getD5();
		if(d5!=null && d5.booleanValue()){
			if(nowday.equals("05")){
				i=1;
			}
		}
		UFBoolean d6 = dayTime.getD6();
		if(d6!=null && d6.booleanValue()){
			if(nowday.equals("06")){
				i=1;
			}
		}
		UFBoolean d7 = dayTime.getD7();
		if(d7!=null && d7.booleanValue()){
			if(nowday.equals("07")){
				i=1;
			}
		}
		UFBoolean d8 = dayTime.getD8();
		if(d8!=null && d8.booleanValue()){
			if(nowday.equals("08")){
				i=1;
			}
		}
		UFBoolean d9 = dayTime.getD9();
		if(d9!=null && d9.booleanValue()){
			if(nowday.equals("09")){
				i=1;
			}
		}
		UFBoolean d10 = dayTime.getD10();
		if(d10!=null && d10.booleanValue()){
			if(nowday.equals("10")){
				i=1;
			}
		}
		UFBoolean d11 = dayTime.getD11();
		if(d11!=null && d11.booleanValue()){
			if(nowday.equals("11")){
				i=1;
			}
		}
		UFBoolean d12 = dayTime.getD12();
		if(d12!=null && d12.booleanValue()){
			if(nowday.equals("12")){
				i=1;
			}
		}
		UFBoolean d13 = dayTime.getD13();
		if(d13!=null && d13.booleanValue()){
			if(nowday.equals("13")){
				i=1;
			}
		}
		UFBoolean d14 = dayTime.getD14();
		if(d14!=null && d14.booleanValue()){
			if(nowday.equals("14")){
				i=1;
			}
		}
		UFBoolean d15 = dayTime.getD15();
		if(d15!=null && d15.booleanValue()){
			if(nowday.equals("15")){
				i=1;
			}
		}
		UFBoolean d16 = dayTime.getD16();
		if(d16!=null && d16.booleanValue()){
			if(nowday.equals("16")){
				i=1;
			}
		}
		UFBoolean d17 = dayTime.getD17();
		if(d17!=null && d17.booleanValue()){
			if(nowday.equals("17")){
				i=1;
			}
		}
		UFBoolean d18 = dayTime.getD18();
		if(d18!=null && d18.booleanValue()){
			if(nowday.equals("18")){
				i=1;
			}
		}
		UFBoolean d19 = dayTime.getD19();
		if(d19!=null && d19.booleanValue()){
			if(nowday.equals("19")){
				i=1;
			}
		}
		UFBoolean d20 = dayTime.getD20();
		if(d20!=null && d20.booleanValue()){
			if(nowday.equals("20")){
				i=1;
			}
		}
		UFBoolean d21 = dayTime.getD21();
		if(d21!=null && d21.booleanValue()){
			if(nowday.equals("21")){
				i=1;
			}
		}
		UFBoolean d22 = dayTime.getD22();
		if(d22!=null && d22.booleanValue()){
			if(nowday.equals("22")){
				i=1;
			}
		}
		UFBoolean d23 = dayTime.getD23();
		if(d23!=null && d23.booleanValue()){
			if(nowday.equals("23")){
				i=1;
			}
		}
		UFBoolean d24 = dayTime.getD24();
		if(d24!=null && d24.booleanValue()){
			if(nowday.equals("24")){
				i=1;
			}
		}
		UFBoolean d25 = dayTime.getD25();
		if(d25!=null && d25.booleanValue()){
			if(nowday.equals("25")){
				i=1;
			}
		}
		UFBoolean d26 = dayTime.getD26();
		if(d26!=null && d26.booleanValue()){
			if(nowday.equals("26")){
				i=1;
			}
		}
		UFBoolean d27 = dayTime.getD27();
		if(d27!=null && d27.booleanValue()){
			if(nowday.equals("27")){
				i=1;
			}
		}
		UFBoolean d28 = dayTime.getD28();
		if(d28!=null && d28.booleanValue()){
			if(nowday.equals("28")){
				i=1;
			}
		}
		UFBoolean d29 = dayTime.getD29();
		if(d29!=null && d29.booleanValue()){
			if(nowday.equals("29")){
				i=1;
			}
		}
		UFBoolean d30 = dayTime.getD30();
		if(d30!=null && d30.booleanValue()){
			if(nowday.equals("30")){
				i=1;
			}
		}
		UFBoolean d31 = dayTime.getD31();
		if(d31!=null && d31.booleanValue()){
			if(nowday.equals("31")){
				i=1;
			}
		}
		return i;
	}
	//张进双 获取当前周几
	  public static String getWeekOfDate(Date dt) {
	        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(dt);

	        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if (w < 0)
	            w = 0;

	        return weekDays[w];
	    }
	  
	  public static void main(String[] args) {
		  TaskMangeImpl task = new TaskMangeImpl();
		  String date = null;
		  try {
			  date = task.getNextDate("0001AA1000000002GIQF");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date);
	}
}
