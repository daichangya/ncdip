package sj.alter.threadpool.factory;

import java.util.ArrayList;
import java.util.List;

import sj.alter.threadpool.pool.ThreadPool;

import nc.impl.dip.pub.Alert;
import nc.itf.dip.pub.IAlert;
import nc.util.dip.sj.SJUtil;
/**
 * @author dou
 * @desc 前台界面预警监控的线程池工厂
 * */
public class ThreadpoolFactoryAtOnce {
	/**
	 * @author wyd 
	 * @desc 在监控里马上执行预警的工作列表
	 * */
	private static List<IAlert> alertlist=null;
	/**
	 * @author wyd 
	 * @desc 单例得到在监控里马上执行预警的工作列表
	 * @return 返回执行列表
	 * */
	public static List<IAlert> getAlertList(){
		if(SJUtil.isNull(alertlist)){
			alertlist=new ArrayList<IAlert>();
		}
		return alertlist;
	}
	//------------------------------------------------------------------
	/**
	 * @author wyd
	 * @desc 在监控里的马上执行的预警的一个线程池
	 * */
	private static ThreadPool atonceThread=null;
	/**
	 * @author wyd
	 * @desc 在监控里得到的马上执行的预警的一个线程池
	 * */
	public static ThreadPool getAtonceThread(){
		if(SJUtil.isNull(atonceThread)){
			atonceThread=new ThreadPool(10,20);
			atonceThread.startService();
		}
		return atonceThread;
	}
	//--------------------------------------------------------------------
	public static boolean listContainAlert(List<IAlert> name,Alert alert){
		for(IAlert l:name){
			if(l.equals(alert)){
				return true;
			}
		}
		return false;
	}
	public static boolean listRemoveAlert(List<IAlert> list,Alert alert){
		for(IAlert l:list){
			if(l.equals(alert)){
				return list.remove(l);
			}
		}
		return false;
	}
	/**
	 * @author wyd
	 * @desc 在监控里得到的马上执行的预警的一个线程池
	 * @return 返回执行结果成功与否
	 * */
	public static boolean doTaskAtOnce(Alert alert) {
		boolean succ=false;
		//判断当前的预警任务列表里有没有当前的alert任务，如果有的话，那就直接返回，如果没有，那么就把它添加到任务列表中去
		if(!listContainAlert(getAlertList(),alert)){
			//把alert添加到任务列表中去
			getAlertList().add(alert);
			//判断如果没有加入，并且任务列表有当前任务，那就循环等，知道添加到任务缓冲队列
			while(!succ&&listContainAlert(getAlertList(),alert)){
				//往缓冲队列中加入当前任务
				succ=getAtonceThread().execute(alert);
				if(succ){
					//如果添加缓冲队列成功，那么久把当前任务从任务列表中移除
					listRemoveAlert(getAlertList(),alert);
					getAlertList();
					break;
				}
			}
		}
		return succ;
	}
	/**
	 * @author wyd
	 * @desc 在监控里得到的马上执行的预警的一个线程池
	 * @return 返回执行结果成功与否
	 * */
	public static boolean doStopTaskAtOnce(Alert alert) {
		boolean ret=false;
		if(listContainAlert(getAlertList(),alert)){
			ret=listRemoveAlert(getAlertList(),alert);
		}
		return ret;
	}
}
