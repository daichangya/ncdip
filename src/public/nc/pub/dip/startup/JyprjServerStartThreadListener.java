package nc.pub.dip.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import nc.bs.logging.Logger;
import nc.bs.uap.scheduler.ITaskAutoLoader;
import nc.pub.dip.startup.WarnThread;
import nc.vo.pub.BusinessException;
import nc.vo.uap.scheduler.TaskPriority;
//nc.pub.jyprj.startup.JyprjServerStartThreadListener
public class JyprjServerStartThreadListener implements ServletContextListener  ,ITaskAutoLoader {

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void contextInitialized(ServletContextEvent arg0) {
		//nc.ui.ml.NCLangRes.getInstance().getStrByID("jyprj", "JYPRJ-0000002");
		String yjThreadCount=nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("jyprj", "JYPRJ-0000001");
		String serverName=System.getProperty("nc.server.name");
		Logger.debug("SERVERNAME-------------"+System.getProperty("nc.server.name")+"--------");
		if(yjThreadCount!=null&&yjThreadCount.matches("[0-9]*")){
			Integer threadCount=Integer.parseInt(yjThreadCount);
			for(int i=0;i<threadCount;i++){
				Thread thread = new Thread(new WarnThread(10000l));
				thread.start();
				
			}
		}

	}

	public void loadAllTasks(TaskPriority priority, String dsName) throws BusinessException {
		//nc.ui.ml.NCLangRes.getInstance().getStrByID("jyprj", "JYPRJ-0000002");
		String yjThreadCount=nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("jyprj", "JYPRJ-0000001");
		String serverName=System.getProperty("nc.server.name");
		Logger.debug("SERVERNAME-------------"+System.getProperty("nc.server.name")+"--------");
		if(yjThreadCount!=null&&yjThreadCount.matches("[0-9]*")){
			Integer threadCount=Integer.parseInt(yjThreadCount);
			for(int i=0;i<threadCount;i++){
				Thread thread = new Thread(new WarnThread(10000l));
				thread.start();
				
			}
		}
		
	}

}
