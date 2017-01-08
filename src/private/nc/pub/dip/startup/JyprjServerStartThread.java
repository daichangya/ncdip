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
	 * ���ܣ���������ʱ�����У�ͬʱ�����߳�
	 * ����:wyd
	 * ����:2011-07-01
	 * */
	public void loadAllTasks(TaskPriority priority, String dsName) throws BusinessException {
		// wyd ���߳����ķ���
		//nc.ui.ml.NCLangRes.getInstance().getStrByID("jyprj", "JYPRJ-0000002");
		
		//���ٸ�Ԥ���߳�,�����ļ�·��  nc502\resources\lang\simpchn\jyprj
		//String yjThreadCount=nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("jyprj", "JYPRJ-0000001");
		
		//2011-7-11
		BaseDAO dao=getBaseDao();
		DipRunsysBVO bvo = (DipRunsysBVO) dao.executeQuery("select * from dip_runsys_b where nvl(dr,0)=0 and syscode='DIP-0000001'", new BeanProcessor(DipRunsysBVO.class));
		String yjThreadCount=bvo.getSysvalue();//����ֵ: Ԥ���߳���
		
		DipRunsysBVO bvo1 = (DipRunsysBVO) dao.executeQuery("select * from dip_runsys_b where nvl(dr,0)=0 and syscode='DIP-0000002'", new BeanProcessor(DipRunsysBVO.class));
		String sleepThread=bvo1.getSysvalue();//����ֵ: �õ��߳���Ϣʱ��
		Long l=sleepThread==null||sleepThread.length()<=0||!sleepThread.matches("[0-9]*")?12000l:Long.parseLong(sleepThread+"000");
		
		//esb����ʱ����
		DipRunsysBVO bvo2 = (DipRunsysBVO) dao.executeQuery("select * from dip_runsys_b where nvl(dr,0)=0 and syscode='DIP-0000004'", new BeanProcessor(DipRunsysBVO.class));
		String st=bvo2.getSysvalue();//����ֵ: �õ��߳���Ϣʱ��
		Long l2=st==null||st.length()<=0||!st.matches("[0-9]*")?12000l:Long.parseLong(st+"000");
		
		//����������
		String serverName=System.getProperty("nc.server.name");
		if(serverName==null||serverName.trim().equals("")){
			return;
		}
		Logger.debug("SERVERNAME-------------"+serverName+"--------");
		/*if(serverName.equals("mServer")){
			List<DipWarningsetVO> vos=(List<DipWarningsetVO>) dao.retrieveByClause(DipWarningsetVO.class, "tabstatus='����' and nvl(dr,0)=0");
			if(vos!=null&&vos.size()>0){
				ITaskManage itm=(ITaskManage) NCLocator.getInstance().lookup(ITaskManage.class.getName());
				for(int i=0;i<vos.size();i++){
					
					itm.updatTaskstate(vos.get(i), ITaskManage.TASKSTATE_STROP);
				}
			}
		}*/
		//1����ʱ���»�����߳�
		Executor upthead=new Executor(new UpdataBuffThread(l,serverName),(serverName+"updatebuf"));
		upthead.start();
		//2����Ԥ�����߳�
		if(yjThreadCount!=null&&yjThreadCount.matches("[0-9]*")){
			Integer threadCount=Integer.parseInt(yjThreadCount);
			for(int i=0;i<threadCount;i++){
				Executor thread = new Executor(new WarnThread(l));
//				thread.setName((serverName+"-work"+i));
				thread.start();
			}
		}
		//3������ESB���߳� ��Ϣ�����յ�
		Collection vo=dao.retrieveByClause(MessservtypeVO.class, "nvl(dr,0)=0 and vdef2='"+serverName+"' and vdef1 in (select pk_dip_msr from dip_msr where nvl(dr,0)=0  and messageplug='0001AA1000000003CL1V')");
		if(vo!=null&&vo.size()>0){
			for(int i=0;i<vo.size();i++){
				Executor futhread=new Executor(new MessageThread(l,l2,(serverName+"-messWork"+i)));
				futhread.start();
			}
		}
		Logger.debug("��ʼ�������߳����");
		
	}

}
