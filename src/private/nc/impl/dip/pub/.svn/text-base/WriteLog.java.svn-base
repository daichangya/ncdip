package nc.impl.dip.pub;

import java.util.ArrayList;
import java.util.List;

import nc.vo.dip.dateprocess.DateprocessVO;

public class WriteLog {
	public static final int LX_ADD=0;   // 写日志
	public static final int LX_GET=1;   // 取日志
	private static List<DateprocessVO> dataplist=new ArrayList<DateprocessVO>();
	 static java.util.Timer timer=new java.util.Timer(true); 
	 static{
		 timer.schedule(new RunTimeLog(),0,5000); //设置时间定时器,5秒中
	 }
	 
	 /**
	  * 功能：操作缓存日志
	  * 作者：王艳冬
	  * 日期:2011-06-12
	  * */
	 public static synchronized void getOrAddList(int lx,DateprocessVO vo,List<DateprocessVO> list){
		 if(lx==LX_ADD){
			 if(dataplist==null){
				 dataplist=new ArrayList<DateprocessVO>();
			 }
			 dataplist.add(vo);
		 }else{
			 if(dataplist!=null&&dataplist.size()>0){
				 list.addAll(dataplist);
			 }
			 dataplist=null;
		 }
	 }
}
