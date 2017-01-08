package sj.alter.threadpool.pool;

import java.io.Serializable;
import java.util.LinkedList;

import nc.itf.dip.pub.IAlert;
/**用于多发多收的多线程模式的管道缓冲区
 *  用wait/notify的触发模式,取代传统的那种sleep的轮训模式.*/
public class RunPipe<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected LinkedList<E> cache = new LinkedList<E>();
	//最大等待队列长度
	private int maxcount=10;
	public RunPipe(int maxcount){
		this.maxcount=maxcount;
	}
	//压入一个缓冲对象到队列的最后
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
	//如果当前缓冲区有数据，则返回并移除第一个元素，如果没有，则在指定的时间内等待
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
	//如果当前缓冲区有数据, 则返但不移除第一个元素
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
	 * 如果停止线程，那么就把这个Runner从缓冲队列里边移出去
	 * */
	public boolean storpThread(IAlert alert) {
		if(cache.contains(alert)){
			return cache.remove(alert);
		}else{
			return false;
		}
	}
}
