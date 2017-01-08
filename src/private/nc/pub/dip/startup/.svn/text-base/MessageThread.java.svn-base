package nc.pub.dip.startup;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.dip.pub.DataReceiverJMSImpl;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IServerBufferManage;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.managerserver.DipManagerserverVO;
/**
 * @author wyd
 * @desc ESB管理线程
 * */
public class MessageThread implements Runnable {
	/**
	 * 构造方法
	 * */
	private Long l2;
	public MessageThread(Long l,Long l2,String threadname){
		setSleepTime(l);
		this.l2=l2;
		Thread.currentThread().setName(threadname);
	}
	/**
	 * 得到IServerBufferManage实例
	 * */
	IServerBufferManage isb=(IServerBufferManage) NCLocator.getInstance().lookup(IServerBufferManage.class.getName());
	ILogAndTabMonitorSys ilm=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
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
		//得到当前的线程名
		String threadName=System.getProperty("nc.server.name")+"-dip-mess"+Thread.currentThread().getName().replace("-", "");
		Thread.currentThread().setName(threadName);
		setThreadName(threadName);
		Logger.debug(getLogerHeadStr()+"messageThread--------后台启动的接收esb线程执行一次----------start");
		//得到线程休息时间
			//线程初始化得时候，先停休息时间
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while(true){
				try{
					DipManagerserverVO taskvo=isb.getRunMesVO(threadName);
			//		把当前的线程注册到服务管理中
					if(taskvo!=null){
						Logger.debug(getLogerHeadStr()+"messageThread------------------找到要执行的服务"+taskvo.getMesvo().getName());
						try{
							runTask(taskvo);
						}catch(Exception e){
							DateprocessVO vo=new DateprocessVO();
							vo.setContent(getLogerHeadStr()+e.getMessage()+e.getStackTrace());
							vo.setActive("EMS消息接收");
							vo.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
							ilm.writToDataLog_RequiresNew(vo);
							Logger.debug(getLogerHeadStr()+e.getMessage()+e.getStackTrace());
							e.printStackTrace();
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}else{
						Logger.debug(getLogerHeadStr()+"messageThread------------------没有找到要执行的服务");
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}catch(Exception ee){
					ee.printStackTrace();
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
	 * *//*
	public void doTask(){
		try{
			DipManagerserverVO taskvo=isb.getRunMesVO(threadName);
	//		把当前的线程注册到服务管理中
			if(taskvo!=null){
				Logger.debug(getLogerHeadStr()+"messageThread------------------找到要执行的服务"+taskvo.getMesvo().getName());
				try{
					runTask(taskvo);
				}catch(Exception e){
					DateprocessVO vo=new DateprocessVO();
					vo.setContent(getLogerHeadStr()+e.getMessage()+e.getStackTrace());
					vo.setActive("EMS消息接收");
					ilm.writToDataLog(vo);
					Logger.debug(getLogerHeadStr()+e.getMessage()+e.getStackTrace());
					e.printStackTrace();
				}
			}else{
				Logger.debug(getLogerHeadStr()+"messageThread------------------没有找到要执行的服务");
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}catch(Exception ee){
			ee.printStackTrace();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		doTask();
	}*/
	/**
	 * @author wyd
	 * @desc 执行任务的方法，具体到每一个Task
	 * 
	 * */
	public void runTask(DipManagerserverVO task){
		DataReceiverJMSImpl drj=new DataReceiverJMSImpl(task);
		try {
			Logger.debug(getLogerHeadStr()+"messageThread------------------开始执行接收方法");
			int ret=drj.dataRecive(threadName,l2,sleepTime);
			if(ret==1){
				Thread.sleep(getSleepTime());
			}
		} catch (Exception e) {
			DateprocessVO vo=new DateprocessVO();
			vo.setContent(getLogerHeadStr()+"执行消息接收失败"+e.getMessage()+e.getStackTrace()[0]);
			vo.setActive("EMS消息接收");
			vo.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
			ilm.writToDataLog_RequiresNew(vo);
			e.printStackTrace();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		isb.updateMesVO(task);
	}

}
