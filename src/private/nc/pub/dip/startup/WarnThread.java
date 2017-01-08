package nc.pub.dip.startup;


import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IServerBufferManage;
import nc.itf.dip.pub.ITaskExecute;
import nc.itf.dip.pub.ITaskManage;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.managerserver.DipManagerserverVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.warningset.DipWarningsetVO;
/**
 * @author wyd
 * @desc 预警管理线程
 * */
public class WarnThread implements Runnable {
	//public static boolean YJLC=false;
	/**
	 * 构造方法
	 * */
	public WarnThread(Long l){
		setSleepTime(l);
//		setThreadName(threadName);
	
	}
	/**
	 * 得到IServerBufferManage实例
	 * */
	IServerBufferManage isb=(IServerBufferManage) NCLocator.getInstance().lookup(IServerBufferManage.class.getName());
	ITaskManage itm=(ITaskManage) NCLocator.getInstance().lookup(ITaskManage.class.getName());
	ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
	private String threadName;
	Long sleepTime;
	public Long getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(Long sleepTime) {
		this.sleepTime = sleepTime;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	private String getLogerHeadStr(){
		return this.getClass().getName()+"; 线程名："+threadName;
	}
	
	public BaseDAO baseDao;
	public BaseDAO getBaseDao(){
		if(baseDao==null){
			baseDao=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return baseDao;
	}
	public void run() {


		String threadName=System.getProperty("nc.server.name")+"-dip-warn-"+Thread.currentThread().getName().replace("-", "");
		Thread.currentThread().setName(threadName);
		setThreadName(threadName);
		//得到当前的线程名
		Logger.debug(getLogerHeadStr()+"------------------start");
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			DipManagerserverVO dmvo=new DipManagerserverVO();
			dmvo.setSysname(threadName);
			dmvo.setRunservice(threadName.split("-")[0]);
			dmvo.setWorkcont("默认启动执行查询预警任务");
			isb.setServerBufferManage(threadName, dmvo);
//			doTask();
			while(true){
//				把当前的线程注册到服务管理中
				DipWarningsetVO	task=itm.getTaskItem(threadName);
				if(task!=null&&task.getPrimaryKey()!=null&&task.getPrimaryKey().length()>0){
					try{
						runTask(task);
					}catch(Exception e){
						Logger.debug(getLogerHeadStr()+e.getMessage()+e.getStackTrace());
						e.printStackTrace();
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException ee) {
							ee.printStackTrace();
						}
					}
					itm.updatTaskstate(task, ITaskManage.TASKSTATE_STROP);
				}else{
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
	}
	/**
	 * @author wyd
	 * @desc 执行任务的方法，是一个循环方法的循环体
	 * 
	 * */
//	public void doTask(){
////		把当前的线程注册到服务管理中
//		DipWarningsetVO task=itm.getTaskItem(threadName);
//		if(task!=null&&task.getPrimaryKey()!=null&&task.getPrimaryKey().length()>0){
//			try{
//				runTask(task);
//			}catch(Exception e){
//				Logger.debug(getLogerHeadStr()+e.getMessage()+e.getStackTrace());
//				e.printStackTrace();
//			}
//			itm.updatTaskstate(task, ITaskManage.TASKSTATE_STROP);
//		}else{
//			try {
//				Thread.sleep(sleepTime);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		doTask();
//	}
	/**
	 * @author wyd
	 * @desc 执行任务的方法，具体到每一个Task
	 * 
	 * */
	public void runTask(DipWarningsetVO task){
		String tasktype=task.getTasktype();
		if(tasktype!=null&&tasktype.length()>0){
//			1	0001AA1000000001A2YB	数据同步	0001	22	22	22	22	22	0	2011-04-20 16:18:41
//		//	2	0001AA1000000001TQJ4	混合流程	0006						0	2011-05-18 11:00:04
//			3	0001AA1000000001A5AX	数据加工	0002						0	2011-04-20 16:18:58
//			4	0001AA1000000001A5AY	数据转换	0003						0	2011-04-20 16:19:11
//			5	0001AA1000000001A5AZ	数据发送	0004						0	2011-04-20 16:19:24
//			6	0001AA1000000001FVAD	数据流程	0005						0	2011-04-28 14:07:11
			String xt="";
			DipManagerserverVO dmvo=new DipManagerserverVO();
			try {
				DipSysregisterHVO dshvo=(DipSysregisterHVO) getBaseDao().retrieveByPK(DipSysregisterHVO.class, task.getPk_sys());
				xt=dshvo.getExtname();
				dmvo=isb.getServerVOByThreadname(threadName);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(tasktype.equals("0001AA1000000001A2YB")){
					doTBTask(xt,task,dmvo,true);
				}else if(tasktype.equals("0001AA1000000001A5AX")){
					doJGTask(xt,task,dmvo,true);
				}else if(tasktype.equals("0001AA1000000001A5AY")){
					doZHTask(xt,task,dmvo,true);
				}else if(tasktype.equals("0001AA1000000001A5AZ")){
					doFSTask(xt,task,dmvo,true);
				}else if(tasktype.equals("0001AA1000000001FVAD")){
					doLCTask(xt,task,dmvo,true);
				}else if(tasktype.equals("0001AA100000000343JO")){
					doAutoContData(xt,task,dmvo,true);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	private void doTBTask(String xt,DipWarningsetVO task,DipManagerserverVO dmvo,Boolean isWriteS){
		String ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_TB);
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx);
			dmvo.setMstatus("运行");
			isb.setServerBufferManage(threadName, dmvo);
		}
		// wyd 判断是否符合预警条件
		if(ite.isFitWarnCondition(task.getPrimaryKey())){
			//执行同步
			ite.doTBTask(task.getPk_bustab());
		}
		//数据同步
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx+"完成");
			dmvo.setMstatus("停止");
			isb.setServerBufferManage(threadName, dmvo);
		}		
	}
	private void doJGTask(String xt,DipWarningsetVO task,DipManagerserverVO dmvo,Boolean isWriteS){
		String ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_JG);
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx);
			dmvo.setMstatus("运行");
			isb.setServerBufferManage(threadName, dmvo);
		}
		
// wyd 判断是否符合预警条件
		
		if(ite.isFitWarnCondition(task.getPrimaryKey())){
			ite.doJGTask(task.getPk_bustab());
		}
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx+"完成");
			dmvo.setMstatus("停止");
			isb.setServerBufferManage(threadName, dmvo);
		}
	}
	private void doZHTask(String xt,DipWarningsetVO task,DipManagerserverVO dmvo,Boolean isWriteS){
		String ywlx=SJUtil.getYWbyYWLX(IContrastUtil.YWLX_ZH);
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx);
			dmvo.setMstatus("运行");
			isb.setServerBufferManage(threadName, dmvo);
		}
// wyd 判断是否符合预警条件
		if(ite.isFitWarnCondition(task.getPrimaryKey())){
			ite.doDataChangeTask(task.getPk_bustab());
		}
		//数据转换
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx+"完成");
			dmvo.setMstatus("停止");
			isb.setServerBufferManage(threadName, dmvo);
		}
	}
	private void doFSTask(String xt,DipWarningsetVO task,DipManagerserverVO dmvo,Boolean isWriteS){
		String ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_FS);
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx);
			dmvo.setMstatus("运行");
			isb.setServerBufferManage(threadName, dmvo);
		}
// wyd 判断是否符合预警条件
		if(ite.isFitWarnCondition(task.getPrimaryKey())){
		
			ite.doFSTask(task.getPk_bustab());
		}
		//数据发送
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx+"完成");
			dmvo.setMstatus("停止");
			isb.setServerBufferManage(threadName, dmvo);
		}
	}
	private void doLCTask(String xt,DipWarningsetVO task,DipManagerserverVO dmvo,Boolean isWriteS){
		String ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_LC);
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx);
			dmvo.setMstatus("运行");
			isb.setServerBufferManage(threadName, dmvo);
		}
		if(ite.isFitWarnCondition(task.getPrimaryKey())){
			//YJLC=true;
			ite.doLCTask(task.getPk_bustab());
			//YJLC=false;
		}
		//数据流程
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx+"完成");
			dmvo.setMstatus("停止");
			isb.setServerBufferManage(threadName, dmvo);
		}
	}
	private void doAutoContData(String xt,DipWarningsetVO task,DipManagerserverVO dmvo,Boolean isWriteS){
		String ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_DZ);
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx);
			dmvo.setMstatus("运行");
			isb.setServerBufferManage(threadName, dmvo);
		}
		if(ite.isFitWarnCondition(task.getPrimaryKey())){
			//YJLC=true;
			ite.doAutoContData(task.getPk_bustab());
			//YJLC=false;
		}
		//数据流程
		if(isWriteS){
			dmvo.setWorkcont(xt+"-"+ywlx+"完成");
			dmvo.setMstatus("停止");
			isb.setServerBufferManage(threadName, dmvo);
		}
	}
}
