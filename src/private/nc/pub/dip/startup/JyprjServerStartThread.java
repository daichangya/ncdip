package nc.pub.dip.startup;


import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.execute.Executor;
import nc.bs.logging.Logger;
import nc.bs.uap.scheduler.ITaskAutoLoader;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.messservtype.MessservtypeVO;
import nc.vo.dip.runsys.DipRunsysBVO;
import nc.vo.pub.BusinessException;
import nc.vo.uap.scheduler.TaskPriority;
//nc.pub.jyprj.startup.JyprjServerStartThreadListener
public class JyprjServerStartThread implements ITaskAutoLoader {
	
	public BaseDAO baseDao;
	public BaseDAO getBaseDao(){
		if(baseDao==null){
			baseDao=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return baseDao;
	}
	
	/**
	 * 功能：服务启动时，运行，同时启动线程
	 * 作者:wyd
	 * 日期:2011-07-01
	 * */
	public void loadAllTasks(TaskPriority priority, String dsName) throws BusinessException {
		// wyd 读线程数的方法
		//nc.ui.ml.NCLangRes.getInstance().getStrByID("jyprj", "JYPRJ-0000002");
		
		//多少个预警线程,配置文件路径  nc502\resources\lang\simpchn\jyprj
		//String yjThreadCount=nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("jyprj", "JYPRJ-0000001");
		
		//2011-7-11
		BaseDAO dao=getBaseDao();
		DipRunsysBVO bvo = (DipRunsysBVO) dao.executeQuery("select * from dip_runsys_b where nvl(dr,0)=0 and syscode='DIP-0000001'", new BeanProcessor(DipRunsysBVO.class));
		String yjThreadCount=bvo.getSysvalue();//参数值: 预警线程数
		
		DipRunsysBVO bvo1 = (DipRunsysBVO) dao.executeQuery("select * from dip_runsys_b where nvl(dr,0)=0 and syscode='DIP-0000002'", new BeanProcessor(DipRunsysBVO.class));
		String sleepThread=bvo1.getSysvalue();//参数值: 得到线程休息时间
		Long l=sleepThread==null||sleepThread.length()<=0||!sleepThread.matches("[0-9]*")?12000l:Long.parseLong(sleepThread+"000");
		
		//esb接收时间间隔
		DipRunsysBVO bvo2 = (DipRunsysBVO) dao.executeQuery("select * from dip_runsys_b where nvl(dr,0)=0 and syscode='DIP-0000004'", new BeanProcessor(DipRunsysBVO.class));
		String st=bvo2.getSysvalue();//参数值: 得到线程休息时间
		Long l2=st==null||st.length()<=0||!st.matches("[0-9]*")?12000l:Long.parseLong(st+"000");
		
		//服务器名称
		String serverName=System.getProperty("nc.server.name");
		if(serverName==null||serverName.trim().equals("")){
			return;
		}
		Logger.debug("SERVERNAME-------------"+serverName+"--------");
		/*if(serverName.equals("mServer")){
			List<DipWarningsetVO> vos=(List<DipWarningsetVO>) dao.retrieveByClause(DipWarningsetVO.class, "tabstatus='运行' and nvl(dr,0)=0");
			if(vos!=null&&vos.size()>0){
				ITaskManage itm=(ITaskManage) NCLocator.getInstance().lookup(ITaskManage.class.getName());
				for(int i=0;i<vos.size();i++){
					
					itm.updatTaskstate(vos.get(i), ITaskManage.TASKSTATE_STROP);
				}
			}
		}*/
		//1、定时更新缓存的线程
		Executor upthead=new Executor(new UpdataBuffThread(l,serverName),(serverName+"updatebuf"));
		upthead.start();
		//2、启预警的线程
		if(yjThreadCount!=null&&yjThreadCount.matches("[0-9]*")){
			Integer threadCount=Integer.parseInt(yjThreadCount);
			for(int i=0;i<threadCount;i++){
				Executor thread = new Executor(new WarnThread(l));
//				thread.setName((serverName+"-work"+i));
				thread.start();
			}
		}
		//3、启动ESB的线程 消息流接收的
		Collection vo=dao.retrieveByClause(MessservtypeVO.class, "nvl(dr,0)=0 and vdef2='"+serverName+"' and vdef1 in (select pk_dip_msr from dip_msr where nvl(dr,0)=0  and messageplug='0001AA1000000003CL1V')");
		if(vo!=null&&vo.size()>0){
			for(int i=0;i<vo.size();i++){
				Executor futhread=new Executor(new MessageThread(l,l2,(serverName+"-messWork"+i)));
				futhread.start();
			}
		}
		Logger.debug("初始化启动线程完成");
		
	}

}
