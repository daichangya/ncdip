package sj.alter.threadpool.factory;

import java.util.ArrayList;
import java.util.List;

import sj.alter.threadpool.pool.ThreadPool;

import nc.itf.dip.pub.IAlert;
import nc.util.dip.sj.SJUtil;

/**
 * @author wyd
 * @desc ��̨Ԥ�����̳߳ع���
 * */

public class ThreadpoolFactory {
	/**
	 * @author wyd 
	 * @desc �ں�̨Ԥ���Ĺ����б�
	 * */
	private static List<IAlert> alertlist=null;
	/**
	 * @author wyd 
	 * @desc �����õ��ں�̨Ԥ���Ĺ����б�
	 * @return ����Ԥ���б�
	 * */
	public static List<IAlert> getAlertList(){
		if(SJUtil.isNull(alertlist)){
			alertlist=new ArrayList<IAlert>();
		}
		return alertlist;
	}
	/**
	 * @author wyd 
	 * @desc ������̨Ԥ�����̳߳�
	 * */
	private static ThreadPool tp=null;
	/**
	 * @author wyd 
	 * @desc �����õ���̨Ԥ�����̳߳�
	 * @return �����̳߳�
	 * */
	public static ThreadPool getTP(){
		if(SJUtil.isNull(tp)){
			tp=new ThreadPool(10,20);
			tp.startService();
		}
		return tp;
	}
	
}
