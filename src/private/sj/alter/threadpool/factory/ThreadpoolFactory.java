package sj.alter.threadpool.factory;

import java.util.ArrayList;
import java.util.List;

import sj.alter.threadpool.pool.ThreadPool;

import nc.itf.dip.pub.IAlert;
import nc.util.dip.sj.SJUtil;

/**
 * @author wyd
 * @desc 后台预警的线程池工厂
 * */

public class ThreadpoolFactory {
	/**
	 * @author wyd 
	 * @desc 在后台预警的工作列表
	 * */
	private static List<IAlert> alertlist=null;
	/**
	 * @author wyd 
	 * @desc 单例得到在后台预警的工作列表
	 * @return 返回预警列表
	 * */
	public static List<IAlert> getAlertList(){
		if(SJUtil.isNull(alertlist)){
			alertlist=new ArrayList<IAlert>();
		}
		return alertlist;
	}
	/**
	 * @author wyd 
	 * @desc 单例后台预警的线程池
	 * */
	private static ThreadPool tp=null;
	/**
	 * @author wyd 
	 * @desc 单例得到后台预警的线程池
	 * @return 返回线程池
	 * */
	public static ThreadPool getTP(){
		if(SJUtil.isNull(tp)){
			tp=new ThreadPool(10,20);
			tp.startService();
		}
		return tp;
	}
	
}
