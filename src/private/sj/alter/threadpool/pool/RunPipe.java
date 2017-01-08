package sj.alter.threadpool.pool;

import java.io.Serializable;
import java.util.LinkedList;

import nc.itf.dip.pub.IAlert;
/**���ڶ෢���յĶ��߳�ģʽ�Ĺܵ�������
 *  ��wait/notify�Ĵ���ģʽ,ȡ����ͳ������sleep����ѵģʽ.*/
public class RunPipe<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected LinkedList<E> cache = new LinkedList<E>();
	//���ȴ����г���
	private int maxcount=10;
	public RunPipe(int maxcount){
		this.maxcount=maxcount;
	}
	//ѹ��һ��������󵽶��е����
	public boolean push(E x){
		if(size()>=maxcount){
			return false;
		}
		synchronized(cache){
			if(size()>=maxcount){
				return false;
			}else{
				cache.addLast(x);
				cache.notify();
				return true;
			}
		}
	}
	public int size(){
		return cache.size();
	}
	//�����ǰ�����������ݣ��򷵻ز��Ƴ���һ��Ԫ�أ����û�У�����ָ����ʱ���ڵȴ�
	public E pop(long timeout){
		E e=null;
		synchronized(cache){
			if(size()<=0){
				try{
					cache.wait(timeout);
				}catch(InterruptedException e2){
					e2.printStackTrace();
				}
			}
			if(size()>0){
				e=cache.removeFirst();
			}
		}
		return e;
	}
	//�����ǰ������������, �򷵵����Ƴ���һ��Ԫ��
	public E get(){
		E e=null;
		synchronized(cache){
			if(size()>0){
				e=cache.getFirst();
			}
		}
		return e;
	}
	/**
	 * ���ֹͣ�̣߳���ô�Ͱ����Runner�ӻ����������Ƴ�ȥ
	 * */
	public boolean storpThread(IAlert alert) {
		if(cache.contains(alert)){
			return cache.remove(alert);
		}else{
			return false;
		}
	}
}
