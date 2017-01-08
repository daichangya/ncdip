package sj.alter.threadpool.factory;

import java.util.ArrayList;
import java.util.List;

import sj.alter.threadpool.pool.ThreadPool;

import nc.impl.dip.pub.Alert;
import nc.itf.dip.pub.IAlert;
import nc.util.dip.sj.SJUtil;
/**
 * @author dou
 * @desc ǰ̨����Ԥ����ص��̳߳ع���
 * */
public class ThreadpoolFactoryAtOnce {
	/**
	 * @author wyd 
	 * @desc �ڼ��������ִ��Ԥ���Ĺ����б�
	 * */
	private static List<IAlert> alertlist=null;
	/**
	 * @author wyd 
	 * @desc �����õ��ڼ��������ִ��Ԥ���Ĺ����б�
	 * @return ����ִ���б�
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
	 * @desc �ڼ���������ִ�е�Ԥ����һ���̳߳�
	 * */
	private static ThreadPool atonceThread=null;
	/**
	 * @author wyd
	 * @desc �ڼ����õ�������ִ�е�Ԥ����һ���̳߳�
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
	 * @desc �ڼ����õ�������ִ�е�Ԥ����һ���̳߳�
	 * @return ����ִ�н���ɹ����
	 * */
	public static boolean doTaskAtOnce(Alert alert) {
		boolean succ=false;
		//�жϵ�ǰ��Ԥ�������б�����û�е�ǰ��alert��������еĻ����Ǿ�ֱ�ӷ��أ����û�У���ô�Ͱ�����ӵ������б���ȥ
		if(!listContainAlert(getAlertList(),alert)){
			//��alert��ӵ������б���ȥ
			getAlertList().add(alert);
			//�ж����û�м��룬���������б��е�ǰ�����Ǿ�ѭ���ȣ�֪����ӵ����񻺳����
			while(!succ&&listContainAlert(getAlertList(),alert)){
				//����������м��뵱ǰ����
				succ=getAtonceThread().execute(alert);
				if(succ){
					//�����ӻ�����гɹ�����ô�ðѵ�ǰ����������б����Ƴ�
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
	 * @desc �ڼ����õ�������ִ�е�Ԥ����һ���̳߳�
	 * @return ����ִ�н���ɹ����
	 * */
	public static boolean doStopTaskAtOnce(Alert alert) {
		boolean ret=false;
		if(listContainAlert(getAlertList(),alert)){
			ret=listRemoveAlert(getAlertList(),alert);
		}
		return ret;
	}
}
