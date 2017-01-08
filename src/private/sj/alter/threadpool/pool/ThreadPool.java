package sj.alter.threadpool.pool;

import java.util.ArrayList;
import java.util.List;

import nc.itf.dip.pub.IAlert;

//线程池类
public class ThreadPool {
	//作业缓冲区
	private RunPipe<IAlert> alerts;
	//工作单元列表
	private List<Runner> works=null;
	//线程池的工作标志
	private boolean runFlag=false;
	private int threadcount=0;
	public ThreadPool(){
		this(1,1);
	}
	/**
	 * @param threadcount 线程池的并发线程数
	 * @param cachesize  工作单元列表的长度
	 * */
	public ThreadPool(int threadcount,int cachesize){
		alerts=new RunPipe<IAlert>(cachesize);
		works=new ArrayList<Runner>(threadcount);
		this.threadcount=threadcount;
	}
	
	/**线程内的工作单元*/
	private class Runner extends Thread{
		public void run(){
			while(runFlag||alerts.size()>0){
				IAlert alert=alerts.pop(5000);
				if(alert==null){
					continue;
				}
				alert.doAlert();
			}
			works.remove(Runner.this);
			synchronized(works){
				works.notifyAll();
			}
		}
	}
	//启动服务，会初始化线程池中所有的工作单元；
	public boolean startService(){
		if(runFlag){
			return true;
		}
		runFlag=true;
		works.clear();
		for(int i=0;i<threadcount;i++){
			Runner r=new Runner();
			works.add(r);
			r.start();
		}
		return true;
	}
	//停止线程池
	public boolean storpService(){
		if(!runFlag){
			return false;
		}
		runFlag=false;
		while(works.size()>0){
			synchronized(works){
				if(works.size()>0){
					try {
						works.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return true;
	}
	//驱动方法
	public boolean execute(IAlert alert){
		if(!runFlag){
			return false;
		}
		return alerts.push(alert);
	}
	public boolean storpThread(IAlert alert) {
		return alerts.storpThread(alert);
	}
	
}
