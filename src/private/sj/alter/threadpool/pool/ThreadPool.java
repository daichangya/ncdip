package sj.alter.threadpool.pool;

import java.util.ArrayList;
import java.util.List;

import nc.itf.dip.pub.IAlert;

//�̳߳���
public class ThreadPool {
	//��ҵ������
	private RunPipe<IAlert> alerts;
	//������Ԫ�б�
	private List<Runner> works=null;
	//�̳߳صĹ�����־
	private boolean runFlag=false;
	private int threadcount=0;
	public ThreadPool(){
		this(1,1);
	}
	/**
	 * @param threadcount �̳߳صĲ����߳���
	 * @param cachesize  ������Ԫ�б�ĳ���
	 * */
	public ThreadPool(int threadcount,int cachesize){
		alerts=new RunPipe<IAlert>(cachesize);
		works=new ArrayList<Runner>(threadcount);
		this.threadcount=threadcount;
	}
	
	/**�߳��ڵĹ�����Ԫ*/
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
	//�������񣬻��ʼ���̳߳������еĹ�����Ԫ��
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
	//ֹͣ�̳߳�
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
	//��������
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
